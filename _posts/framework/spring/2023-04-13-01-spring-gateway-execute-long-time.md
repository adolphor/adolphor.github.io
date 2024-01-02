---
layout:     post
title:      spring网关耗时过长排查
date:       2023-04-13 11:41:09 +0800
postId:     2023-04-13-11-41-09
categories: [Spring]
keywords:   [Spring]
---

## 项目信息

* `-Xms1024m -Xmx1536m`

## SkyWalking 信息
* 内存信息
* GC信息

可能存在的问题：
* Young GC 时间过长：500ms左右
* 看不到 Full GC 日志

## JVM

```shell
jinfo -flags 1
```
```log
Attaching to process ID 1, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.191-b12
Non-default VM flags: -XX:CICompilerCount=2 -XX:InitialHeapSize=1073741824 -XX:MaxHeapSize=1610612736 -XX:MaxNewSize=536870912 -XX:MinHeapDeltaBytes=196608 -XX:NewSize=357892096 -XX:OldSize=715849728 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseFastUnorderedTimeStamps 
Command line:  -javaagent:skywalking-agent/skywalking-agent.jar=agent.service_name=cloud-gateway,collector.backend_service=110.110.110.110:11800 -Djava.security.egd=file:/dev/./urandom -Xms1024m -Xmx1536m
```

```shell
jstat -gcutil 1 1000
```
```log
 S0     S1     E      O      M     CCS    YGC     YGCT    FGC    FGCT     GCT   
  0.00  62.95  29.24  80.12  94.43  91.93   2431  162.934     4    1.148  164.082
  0.00  62.95  36.79  80.12  94.43  91.93   2431  162.934     4    1.148  164.082
  0.00  62.95  44.99  80.12  94.43  91.93   2431  162.934     4    1.148  164.082
  0.00  62.95  49.13  80.12  94.43  91.93   2431  162.934     4    1.148  164.082
  0.00  62.95  51.13  80.12  94.43  91.93   2431  162.934     4    1.148  164.082
  0.00  62.95  54.88  80.12  94.43  91.93   2431  162.934     4    1.148  164.082
  0.00  62.95  61.21  80.12  94.43  91.93   2431  162.934     4    1.148  164.082
  0.00  62.95  73.55  80.12  94.43  91.93   2431  162.934     4    1.148  164.082
  0.00  62.95  77.84  80.12  94.43  91.93   2431  162.934     4    1.148  164.082
  0.00  62.95  80.41  80.12  94.43  91.93   2431  162.934     4    1.148  164.082
```


```shell
jmap -heap 1
```
```log
Attaching to process ID 1, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.191-b12

using thread-local object allocation.
Mark Sweep Compact GC

Heap Configuration:
   MinHeapFreeRatio         = 40
   MaxHeapFreeRatio         = 70
   MaxHeapSize              = 1610612736 (1536.0MB)
   NewSize                  = 357892096 (341.3125MB)
   MaxNewSize               = 536870912 (512.0MB)
   OldSize                  = 715849728 (682.6875MB)
   NewRatio                 = 2
   SurvivorRatio            = 8
   MetaspaceSize            = 21807104 (20.796875MB)
   CompressedClassSpaceSize = 1073741824 (1024.0MB)
   MaxMetaspaceSize         = 17592186044415 MB
   G1HeapRegionSize         = 0 (0.0MB)

Heap Usage:
New Generation (Eden + 1 Survivor Space):
   capacity = 322437120 (307.5MB)
   used     = 159123696 (151.75218200683594MB)
   free     = 163313424 (155.74781799316406MB)
   49.35030309165396% used
Eden Space:
   capacity = 286654464 (273.375MB)
   used     = 137000968 (130.65430450439453MB)
   free     = 149653496 (142.72069549560547MB)
   47.7930697775563% used
From Space:
   capacity = 35782656 (34.125MB)
   used     = 22122728 (21.097877502441406MB)
   free     = 13659928 (13.027122497558594MB)
   61.82528205843636% used
To Space:
   capacity = 35782656 (34.125MB)
   used     = 0 (0.0MB)
   free     = 35782656 (34.125MB)
   0.0% used
tenured generation:
   capacity = 715849728 (682.6875MB)
   used     = 570876144 (544.4299163818359MB)
   free     = 144973584 (138.25758361816406MB)
   79.74804231538383% used

31778 interned Strings occupying 3735840 bytes.
```

## TODO
cloud-user：CPU限制 和 启动检查




## 参考资料
* [spring网关耗时过长排查]({% post_url framework/spring/2023-04-13-01-spring-gateway-execute-long-time %})
* [一次线上 JVM Young GC 调优，搞懂了这么多东西！](https://xie.infoq.cn/article/2d78c54cc0c3b65acc289029a)
