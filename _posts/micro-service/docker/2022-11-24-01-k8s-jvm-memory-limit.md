---
layout:     post
title:      配置K8S集群中JVM相关内存参数
date:       2022-11-24 11:53:37 +0800
postId:     2022-11-24-11-53-37
categories: [Docker]
keywords:   [Microservice, Docker]
---

将Java项目部署在Docker容器中的时候，既需要配置K8S内存参数也需要配置JVM相关参数，才能保证项目正常运行：
* K8S参数：避免某一个Pod挤占集群中的全部内存和CPU
* JVM参数：避免无法触发GC以及避免频繁GC

## 参数配置的意义
- K8S：`所需资源`默认0.25的CPU、512M内存，`资源限制`默认无限制
  - 没有配置K8S最大内存限制，会一直占用非必要内存，导致资源浪费
  - 没有配置K8S最大内存限制，会无法触发JVM的GC动作，导致OOM
- JVM：初始分配-Xms，默认是物理内存的1/64；最大分配内存-Xmx，默认是物理内存的1/4
  - 如果配置了较小的K8S内存限制不配置JVM参数，会导致默认堆空间很小，无法满足项目运行需求
  - 如果配置了较大的K8S内存限制，来满足默认堆空间需求，会导致内存空间的浪费

所以，既需要配置K8S的资源限制参数，也需要配置JVM的资源限制参数。

## 准备条件
熟悉查看JVM运行情况的基本指令：
```shell
# 查看JVM进程号
jps
# 查看此进程号对应的运行状态
jinfo -flags 7628
# 查看JVM具体的堆栈信息
jmap -heap 7628
```
## K8S参数配置

### 最大值 & 最小值
![最大值最小值]({{ site.baseurl }}/image/post/2022/11/24/01/最大值最小值.png)
**资源限制**：也就是当前单个Pod可以占用的最大值；
**所需资源**：也就是启动当前单个Pod最少需要的资源。
项目运行占用的资源就在这个区间范围内。

### CPU限制

#### 参数释义

- 设置`所需资源`保证独占资源，使其免受其他项目的影响；
- 设置 `资源限制` 避免项目占用当前节点CPU资源过多，影响其他项目运行。

#### 参数影响
配置CPU除了影响项目运行本身的速度之外，还会影响Pod的启动时间，测试结果如下表所示：

| JVM \\ CPU | 0.25 核 | 0.5 核 | 1 核 | 2 核 | 不限制 |
| --- | --- | --- | --- | --- | --- |
| 256 m | 145 秒 | 83 秒 | 79 秒 | 82 秒 | 77 秒 |
| 512 m | 126 秒 | 70 秒 | 77 秒 | x | x |

上表可以看出：影响启动时间最大的因素是CPU上限资源限制，且从0.25核提升到0.5核的时候，启动时间提升最快。
备注：基准测试参数配置如下

- 256 m 验证基准参数：
    - K8S：request 512m，limit 1024m
    - JVM：-Xms256m，-Xmx640m
- 1024 m 验证基准参数：
    - K8S：request 1024m，limit 2048m
    - JVM：-Xms512m -Xmx1536m

#### 推荐配置

- `所需资源`值为0.25，建议不做调整
- `资源限制`控制在2以内
    - 测试环境推荐配置为 0.5，既不会启动过慢，也避免影响其他项目运行
    - 正式环境推荐配置为 1，CPU密集型项目根据需要自行调整

![CPU推荐配置]({{ site.baseurl }}/image/post/2022/11/24/01/CPU推荐配置.png)

### 内存限制

#### 参数释义

和上面的CPU参数限制相同：

- 设置`所需资源`保证独占资源，使其免受其他项目的影响；
- 设置 `资源限制` 避免项目占用当前节点内存资源过多，影响其他项目运行。

#### 参数影响
K8S的内存参数会影响Pod内Java项目的可用资源：
- 如果K8S内存参数小于Java项目配置的最大可用内存，会导致Java项目的OOM
- 如果K8S内存参数远大于Java项目配置的最大可用内存，那么会造成内存资源的浪费

#### 推荐配置
一般默认`所需资源`值为512，建议不做调整；`资源限制`控制在2048m以内，一般的小流量应用推荐配置为1024m即可。但是要注意一点：K8S的最大内存参数限制，一定要大于JVM的最大堆内存参数限制，不然会导致Java程序的OOM。
K8S内存限制配置示例：最小512，最大1024m：

![内存推荐配置]({{ site.baseurl }}/image/post/2022/11/24/01/内存推荐配置.png)

## JVM参数配置

### 参数释义
默认大家都知道JVM参数的作用和影响了，略。

### JVM配置方式

#### 自定义 JVM_OPTION 启动参数
推荐使用此方式，避免跟其他人的配置冲突，导致配置失效。

Dockerfile配置范例如下:

```shel
ENV springConfig="--spring.profiles.active=PROFILE"
ENV JVM_OPTS="-XX:InitialRAMPercentage=10.0 -XX:MaxRAMPercentage=50.0"
ENV targetFile=FILE_NAME-PORT.jar

java $JVM_OPTS -jar $targetFile $springConfig
```

K8S配置界面如下所示:
![JVM_OPTION]({{ site.baseurl }}/image/post/2022/11/24/01/JVM_OPTION.png)

#### JAVA_TOOL_OPTIONS 参数
在K8S的虚拟机中，可以通过这个参数，方便的配置JVM堆内存大小，不需要重新打包镜像，很方便，但是容易覆盖运维相关JVM的参数配置，比如安全部门配置的安全检测相关插桩，所以不推荐使用此方式：

![JAVA_TOOL_OPTIONS参数]({{ site.baseurl }}/image/post/2022/11/24/01/JAVA_TOOL_OPTIONS参数.png)

这个`-XX:InitialRAMPercentage=15.0 -XX:MaxRAMPercentage=50.0`，表示占用内存的百分比，在K8S内存最大值1024m的情况下，等价于如下配置：`-Xms153.6m -Xmx512m`。

实际运行的时候，内存占用资源情况如下：

![内存使用情况]({{ site.baseurl }}/image/post/2022/11/24/01/内存使用情况.png)

需要注意：无论内存参数设置多大，在Pod中至少要预留400M以上的堆外内存，来保证Java项目的正常运行。因为Java的栈内存、以及Pod容器和虚拟机系统资源等都要使用到剩下的堆外内存。

如果1G内存不足，那么参考下面的参数配置最佳实践来调整即可。

## 参数设置最佳实践

### 配置原则
- 满足需求即可，不要配置过大造成资源浪费
- K8S内存限制要大于JVM内存参数限制，避免Java未触发GC导致项目OOM
- JVM堆内存配置不能过大，必须保证堆外内存有300M以上，避免栈内存溢出导致OOM
- CPU的`限制资源`一般应用配置0.5核到1核即可，小于0.5会导致Pod启动速度变慢
- CPU的`所需资源`按照内存比例来配置即可，1G内存对应0.25个CPU，不要过大，不然会导致CPU资源不足

### 推荐配置
不建议将 `-Xms`和 `-Xmx`参数配置为一样大，因为配置为一样大之后，虽然避免了JVM频繁GC，但也导致K8S集群监控以及其他运维监控工具失效，看起来Java项目所需内存一直是固定大小，无法观察到真实使用情况。
根据具体项目的大小，具体项目具体分析，进行适当调整，下面是推荐的K8S和JVM相配套的配置参数（试运行中，如有调整会及时更新，有意见和建议也欢迎反馈）：

#### 512m
测试环境下，很小的应用配置这个参数即可：
```
K8S CPU：request 0.25，limit 0.5
K8S 内存：request 512m，limit 640m
JVM：-Xms128m -Xmx256m
JVM：-XX:InitialRAMPercentage=10.0 -XX:MaxRAMPercentage=40.0
```

#### 1024m
测试环境大一点的应用以及正式环境的小型应用，使用这个配置即可：
```
K8S CPU：request 0.25，limit 0.5
K8S 内存：request 640m，limit 1024m
JVM：-Xms256m -Xmx512m
JVM：-XX:InitialRAMPercentage=10.0 -XX:MaxRAMPercentage=60.0
```

#### 2048m
正式环境大一点的应用，使用这个配置：

```
K8S CPU：request 0.25，limit 0.5
K8S 内存：request 1536m，limit 2048m
JVM：-Xms512m -Xmx1536m
JVM：-XX:InitialRAMPercentage=10.0 -XX:MaxRAMPercentage=75.0
```

#### 3072m
正式环境较大的应用，使用这个配置：
```
K8S CPU：request 0.25，limit 0.75
K8S 内存：request 2048m，limit 3072m
JVM：-Xms1024m -Xmx2304m
JVM：-XX:InitialRAMPercentage=10.0 -XX:MaxRAMPercentage=80.0
```

#### 4096m
正式环境较大的应用，使用这个配置：
```
K8S CPU：request 0.25，limit 1
K8S 内存：request 3072m，limit 4096m
JVM：-Xms2048m -Xmx3328m
JVM：-XX:InitialRAMPercentage=10.0 -XX:MaxRAMPercentage=80.0
```

### TODO：参数配置调优

测试环境测试JVM的GC日志，验证是否可以根据GC日志进行相关参数的调优指标。

## 集群扩容机制
### 什么时候扩容

资源申请有两个节点：

- 新项目上线，申请独立集群或者老集群增加资源
- 项目运行过程中，资源不足，经评估之后增加资源

### 扩容标准和规范

根据K8S的资源监控，`Prometheus监控`，评估参数配置是否合理：

![Prometheus监控]({{ site.baseurl }}/image/post/2022/11/24/01/Prometheus监控.png)

![资源画像]({{ site.baseurl }}/image/post/2022/11/24/01/资源画像.png)

### 能否动态扩容

不做集群节点的动态扩容，避免资源使用不可控。

## 平常运维

- JVM是否合理，业务管理员自己评估，微服务配合一起检查
- 最小值和最大值不要设置成一样，资源复用好一点
- 根据资源监控信息，调整Pod参数
- 根据实际使用情况，来评估是否需要扩容或则收缩


## 参考资料
* [配置K8S集群中JVM相关内存参数]({% post_url micro-service/docker/2022-11-24-01-k8s-jvm-memory-limit %})
* [Java/Spring应用在k8s环境中的内存配置实践](https://segmentfault.com/a/1190000040295369?from_wecom=1)
* [Difference Between InitialRAMPercentage, MinRAMPercentage, MaxRAMPercentage](https://dzone.com/articles/difference-between-initialrampercentage-minramperc)


