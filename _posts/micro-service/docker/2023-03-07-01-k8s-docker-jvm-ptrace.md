---
layout:     post
title:      Docker内查询jvm状态报错：Can't attach to the process: ptrace
date:       2023-03-07 17:03:49 +0800
postId:     2023-03-07-17-03-49
categories: [Docker]
keywords:   [Docker]
---

Docker内排查JVM状态的时候，报错的解决方式。

## 启动指令
```shell
docker run --name aiot-8u341 -p 8750:8750 -d adolphor/aiot:jdk8u341
```

## 查询JVM的PID
```shell
jps
```
```
1 jar
78 Jps
```

## 查询PID的具体信息
```shell
jinfo -flags 1
```
## 报错内容
```
Attaching to process ID 1, please wait...
ERROR: ptrace(PTRACE_ATTACH, ..) failed for 1: Operation not permitted
Error attaching to process: sun.jvm.hotspot.debugger.DebuggerException: Can't attach to the process: ptrace(PTRACE_ATTACH, ..) failed for 1: Operation not permitted
sun.jvm.hotspot.debugger.DebuggerException: sun.jvm.hotspot.debugger.DebuggerException: Can't attach to the process: ptrace(PTRACE_ATTACH, ..) failed for 1: Operation not permitted
        at sun.jvm.hotspot.debugger.linux.LinuxDebuggerLocal$LinuxDebuggerLocalWorkerThread.execute(LinuxDebuggerLocal.java:163)
        at sun.jvm.hotspot.debugger.linux.LinuxDebuggerLocal.attach(LinuxDebuggerLocal.java:278)
        at sun.jvm.hotspot.HotSpotAgent.attachDebugger(HotSpotAgent.java:671)
        at sun.jvm.hotspot.HotSpotAgent.setupDebuggerLinux(HotSpotAgent.java:611)
        at sun.jvm.hotspot.HotSpotAgent.setupDebugger(HotSpotAgent.java:337)
        at sun.jvm.hotspot.HotSpotAgent.go(HotSpotAgent.java:304)
        at sun.jvm.hotspot.HotSpotAgent.attach(HotSpotAgent.java:140)
        at sun.jvm.hotspot.tools.Tool.start(Tool.java:185)
        at sun.jvm.hotspot.tools.Tool.execute(Tool.java:118)
        at sun.jvm.hotspot.tools.JInfo.main(JInfo.java:138)
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.lang.reflect.Method.invoke(Method.java:498)
        at sun.tools.jinfo.JInfo.runTool(JInfo.java:108)
        at sun.tools.jinfo.JInfo.main(JInfo.java:76)
Caused by: sun.jvm.hotspot.debugger.DebuggerException: Can't attach to the process: ptrace(PTRACE_ATTACH, ..) failed for 1: Operation not permitted
        at sun.jvm.hotspot.debugger.linux.LinuxDebuggerLocal.attach0(Native Method)
        at sun.jvm.hotspot.debugger.linux.LinuxDebuggerLocal.access$100(LinuxDebuggerLocal.java:62)
        at sun.jvm.hotspot.debugger.linux.LinuxDebuggerLocal$1AttachTask.doit(LinuxDebuggerLocal.java:269)
        at sun.jvm.hotspot.debugger.linux.LinuxDebuggerLocal$LinuxDebuggerLocalWorkerThread.run(LinuxDebuggerLocal.java:138)
```

## 修复方式

启动Docker的时候增加 `--cap-add=SYS_PTRACE` 参数：
```shell
docker run --cap-add=SYS_PTRACE --name aiot-8u341 -p 8750:8750 -d adolphor/aiot:jdk8u341
```

```shell
jinfo -flags 1
```

```
Attaching to process ID 1, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.341-b10
Non-default VM flags: -XX:CICompilerCount=3 -XX:InitialHeapSize=1558183936 -XX:InitialRAMPercentage=null -XX:MaxHeapSize=1558183936 -XX:MaxNewSize=519045120 -XX:MaxRAMPercentage=null -XX:MinHeapDeltaBytes=524288 -XX:NewSize=519045120 -XX:OldSize=1039138816 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseParallelGC 
Command line:  -XX:InitialRAMPercentage=25.0 -XX:MaxRAMPercentage=25.0
```

```shell
jmap -heap 1
```

```
Attaching to process ID 1, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.341-b10

using thread-local object allocation.
Parallel GC with 4 thread(s)

Heap Configuration:
   MinHeapFreeRatio         = 0
   MaxHeapFreeRatio         = 100
   MaxHeapSize              = 1558183936 (1486.0MB)
   NewSize                  = 519045120 (495.0MB)
   MaxNewSize               = 519045120 (495.0MB)
   OldSize                  = 1039138816 (991.0MB)
   NewRatio                 = 2
   SurvivorRatio            = 8
   MetaspaceSize            = 21807104 (20.796875MB)
   CompressedClassSpaceSize = 1073741824 (1024.0MB)
   MaxMetaspaceSize         = 17592186044415 MB
   G1HeapRegionSize         = 0 (0.0MB)

Heap Usage:
PS Young Generation
Eden Space:
   capacity = 436207616 (416.0MB)
   used     = 239489128 (228.3946304321289MB)
   free     = 196718488 (187.6053695678711MB)
   54.90255539233868% used
From Space:
   capacity = 11534336 (11.0MB)
   used     = 0 (0.0MB)
   free     = 11534336 (11.0MB)
   0.0% used
To Space:
   capacity = 11534336 (11.0MB)
   used     = 0 (0.0MB)
   free     = 11534336 (11.0MB)
   0.0% used
PS Old Generation
   capacity = 1039138816 (991.0MB)
   used     = 23572400 (22.480392456054688MB)
   free     = 1015566416 (968.5196075439453MB)
   2.268455343698758% used

24083 interned Strings occupying 2214872 bytes.
```

## 参考资料
* [Docker内查询jvm状态报错：Can't attach to the process: ptrace]({% post_url micro-service/docker/2023-03-07-01-k8s-docker-jvm-ptrace %})
* [Docker容器：Can't attach to the process: ptrace](https://www.zhangjc.com/2021/10/06/Docker%E5%AE%B9%E5%99%A8%EF%BC%9ACan-t-attach-to-the-process-ptrace-PTRACE-ATTACH-failed-for-XX/)
* [Can't attach to the process: ptrace(PTRACE_ATTACH, ..) Operation not permitted](https://blog.csdn.net/zqz_zqz/article/details/105078992)