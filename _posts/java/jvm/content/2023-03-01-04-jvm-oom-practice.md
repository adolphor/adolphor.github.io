---
layout:     post
title:      JVM - OOM排查经历
date:       2023-03-01 21:13:10 +0800
postId:     2023-03-01-21-13-10
categories: [JVM]
keywords:   [Java,JVM]
---

记一次Pod容器OOM的问题排查。

## OOM场景
参数配置详情如下：
* K8S配置：`request 512M`，`limit 512M`
* JVM配置：`-XX:InitialRAMPercentage=80.0 -XX:MaxRAMPercentage=80.0`

在生产环境中，K8S的内存限制一般是1024M或者2048M，JVM也都是按照统一的规则配置80%，且一直运行正常。
在测试环境中，只缩小了K8S的限制参数到512，并没有更改JVM的内存参数限制，却导致OOM，这个问题比较特殊。

## 第一步：查看dump文件

![Leak-Suspects]({{ site.baseurl }}/image/post/2023/03/01/04/Leak-Suspects.png)

可疑内存泄漏分析：
* The class java.lang.ref.Finalizer, loaded by <system class loader>, occupies 61,986,936 (32.32%) bytes. Keywords：java.lang.ref.Finalizer
* 1,106 instances of sun.security.ssl.AppInputStream, loaded by <system class loader> occupy 26,767,568 (13.96%) bytes. Keywords：sun.security.ssl.AppInputStream
* 1,108 instances of sun.security.ssl.SSLContextImpl$TLSContext, loaded by <system class loader> occupy 26,307,528 (13.72%) bytes. Keywords：sun.security.ssl.SSLContextImpl$TLSContext
* 1,106 instances of okhttp3.internal.connection.RealConnectionPool, loaded by org.springframework.boot.loader.LaunchedURLClassLoader @ 0xeaaa0000 occupy 22,533,304 (11.75%) bytes. Keywords：okhttp3.internal.connection.RealConnectionPool org.springframework.boot.loader.LaunchedURLClassLoader @ 0xeaaa0000

大概分析：
1、Finalizer 已经标记了很多需要GC的对象，但并未触发GC
2、1106个 SSL和 okhttp 相关的类，考虑 okhttp 内存溢出

## 第二步：将 okhttp 每次创建新的Client改为单例模式

优化okhttp工具类，保证无内存溢出：

> OkHttpClientObject.java

```java
package com.adolphor.common.utils.okhttp;
import okhttp3.OkHttpClient;
import java.util.concurrent.TimeUnit;
public enum OkHttpClientObject {
    CLIENT;
    private OkHttpClient clientInstance;
    private Integer connectTimeout_time = 10;
    private Integer writeTimeout_time = 10;
    private Integer readTimeout_time = 30;
    OkHttpClientObject() {
        clientInstance = new OkHttpClient.Builder()
                .connectTimeout(connectTimeout_time, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout_time, TimeUnit.SECONDS)
                .readTimeout(readTimeout_time, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }
    public OkHttpClient getClientInstance() {
        return clientInstance;
    }
}
```
使用方式：
```java
public class OkHttpUtil {
    private final static OkHttpClient client = OkHttpClientObject.CLIENT.getClientInstance();
    public static Response postResponse(String url, Map<String, String> headers,
                                        Map<String, Object> params) throws IOException {
        Objects.requireNonNull(url, "接口请求地址不能为空！");
        Request.Builder reqBuilder = new Request.Builder();
        final Request request = reqBuilder.build();
        Call call = client.newCall(request);
        return call.execute();
    }
}
```

但是，okhttp 优化更改为单例模式之后，依然 OOM，所以还需要排查其他问题。

## 第三步：JVM 内存分析
继续分析JVM运行内存相关数据，
确认配置的xms和xmx已经生效：
```shell
jinfo -flags 1
```

查看堆内存使用情况：
```shell
jmap -heap 1
```

汇总发现堆内存总量并没有超出配置限制，但此时Pod总内存使用量已经接近K8S内存限制极限，
所以，此时问题原因就明显了：堆内存使用量未超限，但堆内存使用量和其他内存使用量的总和
却超过了K8S的内存限制，也就是堆外内存和Docker以及Linux系统本身也使用了很大一部分内存资源，
经过逐步测试发现，高并发场景的CPU密集型服务，至少需要预留300M的堆外内存供Docker和非堆内存使用。

TODO：日志数据补充

## 原来疑问解答

> 1、验证配置了同样的xms数值，为什么本地有GC但是K8S却没有进行GC?

本地和K8S都会进行GC，只不过本地堆外内存充足，触发了堆内存回收的条件；K8S堆外内存不足，还未达到堆内存GC的时候就Pod节点OOM了

> 2、对于不确定参数，是否不需要进行额外配置，只需要保证K8S的限制加上了就可以?

并不完全对，如果K8S的内存限制`小于512`，那么就必须保证xmx最大只能设置`128`，才能保证堆外内存300M左右满足服务运行条件。
如果K8S的内存限制`512`，那么默认的xmx的值就是 `512/4=128`，堆外内存还剩余 `512-128=384`，所以一般也不会出现问题。
如果K8S的内存限制`1024`，那么默认的xmx的值就是 `1024/4=256`，堆外内存还剩余 `1024-256=768`，所以更不会有问题。

> 3、是否只是JVM参数配置不正确的原因?做个只调用controller不调用okhttp的接口进行测试，是否会内存溢出。

测试结果不会OOM，最大的原因是没有创建okhttp相关实例，并没有占用太多堆外内存。

> 4、还有一个手段，在Pod中打印查看GC日志，看看是否进行了GC，以及GC效果怎样

TODO：K8S中配置的gc日志文件一直未生成，还需要排查。

## 参数配置最佳实践

在K8S的Pod中配置JVM参数的时候，需要注意一个特别事项：
1、总原则：要保证堆外内存有300M以上的容量，否则高并发场景的时候，会因为堆外内存使用完毕导致OOM
2、堆外内存主要使用人：JVM的非堆空间、Docker本身以及Docker内的Linux系统所需资源
3、最佳实践：
3.1、K8S的内存限制不要小于512
3.2、如果K8S的内存限制等于512，那么最简单的方式就是不配置JVM参数，xms默认512/64=8M，xmx默认512/4=128M，
3.3、如果K8S的内存限制等于1024，那么可以按照比例配置80%，也可以直接指定xmx=256M


| K8S\JVM | Null    | 128     | 256    | 512 |
|---------|---------|---------|--------|-----|
| 256     | OOM     | OOM     |        |     |
| 512     | 稳定在440M | 稳定在430M | OOM    |     |
| 1024    | 稳定在590M | 稳定运行？  | 稳定在590M |     |
| 2048    | 稳定在850M | 稳定运行？  |   稳定运行？     |  稳定运行？   |

## JVM常用指令
```shell
# 查询pid:
jps
# 配置情况:
jinfo -flags pid
# 占用情况:
jmap -heap pid
```

## 参考资料
* [JVM - OOM排查经历]({% post_url java/jvm/content/2023-03-01-04-jvm-oom-practice %})
* https://blog.csdn.net/m0_45406092/article/details/110314473
* https://juejin.cn/post/6884852550571556877
* https://www.cnblogs.com/guanghe/p/13558412.html
