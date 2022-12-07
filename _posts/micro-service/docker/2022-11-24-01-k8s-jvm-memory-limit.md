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

## 准备条件
熟悉查看JVM运行情况的基本指令：
```shell
# 查看JVM进程号
jps
# 查看此进程号对应的运行状态
jinfo -flags 7628
```

## 限制K8S
也会读取到K8S的内存限制：
```log
JVM version is 25.191-b12
Non-default VM flags: -XX:CICompilerCount=2 -XX:InitialHeapSize=20971520 -XX:MaxHeapSize=335544320 -XX:MaxNewSize=111804416 -XX:MinHeapDeltaBytes=196608 -XX:NewSize=6946816 -XX:OldSize=14024704 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseFastUnorderedTimeStamps
Command line:  -javaagent:/data/agent.jar -Dxmiast.ip=172.31.1.194 -Dxmiast.port=9090 -Dxmiast.projectname=cloud-aiot -Dxmiast.nodename=cloud-aiot -Dxmiast.region= -Dxmiast.token=puzWfJyx9qVZxk19Xb5b5LQ -Dxmiast.writeconfig=false -Dxmiast.debug=DEBUG -Djava.security.egd=file:/dev/./urandom
```

## 限制K8S + JVM
* 要为您的应用程序设置初始堆大小，请使用“-XX:InitialRAMPercentage”
* '-XX:MinRAMPercentage' 和 '-XX:MaxRAMPercentage' 都用于设置应用程序的最大堆大小。
* 如果配置了“-Xms”，“-XX:InitialRAMPercentage”将不会生效以确定初始堆大小。
* 如果配置了“-Xmx”，“-XX:MinRAMPercentage”和“-XX:MaxRAMPercentage”将不会生效以确定最大堆大小。
* 如果你的整体物理服务器（或容器）内存大小超过250MB，那么你不必配置'-XX:MinRAMPercentage'，配置'-XX:MaxRAMPercentage'就足够了。大多数企业级 Java 应用程序将运行超过 250MB（除非您正在使用 Java 构建物联网或网络设备应用程序）。

> -XX:MaxRAMPercentage=80.0

```log
JVM version is 25.191-b12
Non-default VM flags: -XX:CICompilerCount=2 -XX:InitialHeapSize=20971520 -XX:MaxHeapSize=1073741824 -XX:MaxNewSize=357892096 -XX:MaxRAMPercentage=null -XX:MinHeapDeltaBytes=196608 -XX:NewSize=6946816 -XX:OldSize=14024704 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseFastUnorderedTimeStamps
Command line:  -javaagent:/data/agent.jar -Dxmiast.ip=172.31.1.194 -Dxmiast.port=9090 -Dxmiast.projectname=cloud-aiot -Dxmiast.nodename=cloud-aiot -Dxmiast.region= -Dxmiast.token=puzWfJyx9qVZxk19Xb5b5LQ -Dxmiast.writeconfig=false -Dxmiast.debug=DEBUG -Djava.security.egd=file:/dev/./urandom -XX:MaxRAMPercentage=80.0
```

> -XX:MinRAMPercentage=80.0 -XX:MaxRAMPercentage=80.0

```log
JVM version is 25.191-b12
Non-default VM flags: -XX:CICompilerCount=2 -XX:InitialHeapSize=20971520 -XX:MaxHeapSize=1073741824 -XX:MaxNewSize=357892096 -XX:MaxRAMPercentage=null -XX:MinHeapDeltaBytes=196608 -XX:MinRAMPercentage=null -XX:NewSize=6946816 -XX:OldSize=14024704 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseFastUnorderedTimeStamps 
Command line:  -javaagent:/data/agent.jar -Dxmiast.ip=172.31.1.194 -Dxmiast.port=9090 -Dxmiast.projectname=cloud-aiot -Dxmiast.nodename=cloud-aiot -Dxmiast.region= -Dxmiast.token=puzWfJyx9qVZxk19Xb5b5LQ -Dxmiast.writeconfig=false -Dxmiast.debug=DEBUG -Djava.security.egd=file:/dev/./urandom -XX:MaxRAMPercentage=80.0 -XX:MinRAMPercentage=80.0
```

> -XX:InitialRAMPercentage=80.0 -XX:MaxRAMPercentage=80.0

```log
JVM version is 25.191-b12
Non-default VM flags: -XX:CICompilerCount=2 -XX:InitialHeapSize=1073741824 -XX:InitialRAMPercentage=null -XX:MaxHeapSize=1073741824 -XX:MaxNewSize=357892096 -XX:MaxRAMPercentage=null -XX:MinHeapDeltaBytes=196608 -XX:NewSize=357892096 -XX:OldSize=715849728 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseFastUnorderedTimeStamps 
Command line:  -javaagent:/data/agent.jar -Dxmiast.ip=172.31.1.194 -Dxmiast.port=9090 -Dxmiast.projectname=cloud-aiot -Dxmiast.nodename=cloud-aiot -Dxmiast.region= -Dxmiast.token=puzWfJyx9qVZxk19Xb5b5LQ -Dxmiast.writeconfig=false -Dxmiast.debug=DEBUG -Djava.security.egd=file:/dev/./urandom -XX:InitialRAMPercentage=80.0 -XX:MaxRAMPercentage=80.0
```

# 推荐配置参数
### K8S参数
CPU：一般项目配置0.2即可，最大值和最小值保持一致，避免资源挤占出现资源不足
RAM：根据Java项目需要配置即可，最大值和最小值保持一致，避免资源挤占出现资源不足

### JVM参数
```
# 会默认开启，不需要显示配置
-XX:+UseContainerSupport
# 最小内存50%，
# 最大内存80%，不要使用100%，需要给Docker内的其他进程预留内存空间
-XX:InitialRAMPercentage=50.0 -XX:MaxRAMPercentage=80.0
```

## 参考资料
* [配置K8S集群中JVM相关内存参数]({% post_url micro-service/docker/2022-11-24-01-k8s-jvm-memory-limit %})
* [Java/Spring应用在k8s环境中的内存配置实践](https://segmentfault.com/a/1190000040295369?from_wecom=1)
* [Difference Between InitialRAMPercentage, MinRAMPercentage, MaxRAMPercentage](https://dzone.com/articles/difference-between-initialrampercentage-minramperc)

开始写作吧
```
![image-alter]({{ site.baseurl }}/image/post/2022/11/24/01/xxx.jpg)
```

