---
layout:     post
title:      Tomcat 优化
date:       2016-10-11 14:22:43 +0800
postId:     2016-10-11-14-22-43
categories: [article]
tags:       [Tomcat]
geneMenu:   true
excerpt:    Tomcat 优化
---
tomcat优化有如下几个方面：

## 1.windows下修改JVM内存大小:

### 1.1 解压版本的Tomcat

通过startup.bat启动tomcat才能加载配置，要添加在tomcat 的bin 下catalina.bat 里

```shell
rem Guess CATALINA_HOME if not defined  
set CURRENT_DIR=%cd%   
# 后面添加,红色的为新添加的.  
set JAVA_OPTS=-Xms256m -Xmx512m -XX:PermSize=128M -XX:MaxNewSize=256m -XX:MaxPermSize=256m -Djava.awt.headless=true
```

### 1.2 安装版的Tomcat下没有catalina.bat
windows服务执行的是bin\tomcat.exe.他读取注册表中的值,而不是catalina.bat的设置.
修改注册表HKEY_LOCAL_MACHINE\SOFTWARE\Apache Software Foundation\Tomcat Service Manager\Tomcat5\Parameters\JavaOptions
原值为

```
-Dcatalina.home="C:\ApacheGroup\Tomcat 5.0"
-Djava.endorsed.dirs="C:\ApacheGroup\Tomcat 5.0\common\endorsed"
-Xrs
```
加入 `-Xms300m -Xmx350m`，重起tomcat服务,设置生效

### 1.3 jvm参数说明

* `-server` 一定要作为第一个参数，启用JDK的server版本，在多个CPU时性能佳
* `-Xms` java Heap初始大小。 默认是物理内存的1/64。
* `-Xmx` java heap最大值。建议均设为物理内存的80%。不可超过物理内存。
* `-Xmn` java heap最小值，一般设置为Xmx的3、4分之一。
* `-XX:PermSize` 设定内存的永久保存区初始大小，缺省值为64M。
* `-XX:MaxPermSize` 设定内存的永久保存区最大大小，缺省值为64M。
* `-XX:SurvivorRatio=2` 生还者池的大小，默认是2。如
* `-XX:NewSize` 新生成的池的初始大小。 缺省值为2M。
* `-XX:MaxNewSize` 新生成的池的最大大小。 缺省值为32M。
* `+XX:AggressiveHeap` 让jvm忽略Xmx参数，疯狂地吃完一个G物理内存，再吃尽一个G的swap。
* `-Xss` 每个线程的Stack大小
* `-verbose:gc` 现实垃圾收集信息
* `-Xloggc:gc.log` 指定垃圾收集日志文件
* `-XX:+UseParNewGC` 缩短minor收集的时间
* `-XX:+UseConcMarkSweepGC` 缩短major收集的时间
* `-XX:userParNewGC` 可用来设置并行收集(多CPU)
* `-XX:ParallelGCThreads` 可用来增加并行度(多CPU)
* `-XX:UseParallelGC` 设置后可以使用并行清除收集器(多CPU)




建议和注意事项：

* Java 8 以后 `-XX:PermSize` 与 `-XX:MaxPermSize` 两个配置项被废弃
* -Xms和-Xmx选项设置为相同堆内存分配，以避免在每次GC 后调整堆的大小，堆内存建议占内存的60%~80%;非堆内存是不可回收内存，大小视项目而定;线程栈大小推荐256k.
* 32G内存配置如下：

    ```
    JAVA_OPTS="-Xms20480m -Xmx20480m -Xss1024K -XX:PermSize=512m -XX:MaxPermSize=2048m"
    ```

## 2.并发优化
在Tomcat 配置文件 server.xml 中的 <Connector ... /> 配置中
* `acceptCount`：监听端口队列最大数，也就是当线程数达到maxThreads后，可以放到处理队列中的请求数，超过这个数的请求将不予处理。设置时应大于等于 maxProcessors ，默认值为100
* `maxThreads`  表示Tomcat可创建的最大的线程数，即最大并发数。默认200
* `minSpareThreads`    Tomcat初始化时创建的 socket 线程数，默认10
* `maxConnections` For BIO the default is the value of maxThreads unless an Executor is used in which case the default will be the value of maxThreads from the executor. For NIO the default is 10000. For APR/native, the default is 8192.


* `enableLookups`：是否反查域名，取值为： true 或 false 。若设为true, 则支持域名解析，可把 ip 地址解析为主机名。为了提高处理能力，应使用默认设置 false
* `URIEncoding`    URL统一编码
* `connectionTimeout`：网络连接超时，单位：毫秒。设置为 0 表示永不超时，这样设置有隐患的。通常可设置为 30000 毫秒。其中和最大连接数相关的参数为maxProcessors 和 acceptCount 。如果要加大并发连接数，应同时加大这两个参数。
* `redirectPort`        在需要基于安全通道的场合，把客户请求转发到基于SSL 的 redirectPort 端口


web server允许的最大连接数还受制于操作系统的内核参数设置，通常 Windows 是 2000 个左右， Linux 是 1000 个左右。


```
rotocol="org.apache.coyote.http11.Http11NioProtocol"
axThreads="500" 
inSpareThreads="100"
cceptCount="1000"
nableLookups="false"
axPostSize="10485760"
ompression="false"
cceptorThreadCount="2"
erver="Neo App Srv 1.0"
```

## 3.Tomcat压缩
启用压缩有利有弊，默认是关闭状态。
压缩会增加Tomcat负担，最好采用Nginx + Tomcat 或者 Apache + Tomcat 方式，压缩交由Nginx/Apache 去做。

## 4.虚拟主机
不要使用Tomcat的虚拟主机，每个站点一个实例。即，启动多个tomcat.
Tomcat 是多线程,共享内存，任何一个虚拟主机中的应用出现崩溃，会影响到所有应用程序。采用多个实例方式虽然开销比较大，但保证了应用程序隔离与安全。


## 5.应用程序安全

* 关闭war自动部署 unpackWARs="false" autoDeploy="false"。防止被植入木马等恶意程序

* 关闭 reloadable="false" 也用于防止被植入木马

## 6.隐藏版本号

server="Neo App Srv 1.0"

```shell
cd apache-tomcat-7.0.59/lib
mkdir test
cd test
jar xf ../catalina.jar
vi org/apache/catalina/util/ServerInfo.properties
server.info=Tomcat
server.number=6
server.built=Jan 18 2013 14:51:10 UTC
jar cf ../catalina.jar ./*
rm -rf test
```

## 参考文章

* [Tomcat 8 安装和配置、优化](https://github.com/judasn/Linux-Tutorial/blob/master/Tomcat-Install-And-Settings.md)
* [web应用性能测试-Tomcat 7 连接数和线程数配置](http://www.cnblogs.com/tyb1222/p/4583983.html)
* [官网：tomcat8.5 配置参数详解(启动本地tomcat即可)](http://127.0.0.1:8080/docs/config/http.html)
* [Tomcat7优化 ](http://blog.csdn.net/funchs/article/details/50978576) —— 配置字段解析
* [Tomcat 安全配置与性能优化](https://netkiller.github.io/journal/tomcat.html) —— 优化的各个方面
* [Tomcat 配置详解/优化方案](http://www.jianshu.com/p/637d462262ec) —— Tomcat结构讲解
