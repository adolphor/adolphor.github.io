---
layout:     post
title:      JVM - OOM排查经历
date:       2023-03-01 21:13:10 +0800
postId:     2023-03-01-21-13-10
categories: [JVM]
keywords:   [Java,JVM]
---

## 分布式全局锁
锁范围过大，导致获取锁超时；
但是，对于JVM为什么没有进行垃圾回收，应该与分布式锁没有关系。

## JVM常用指令

查询pid:
jps

配置情况:
jinfo -flags pid

占用情况:
jmap -heap pid


## 参数配置
会内存溢出:
K8S参数: 512，1024
JVM参数: 256，512

不会内存溢出的配置:
K8S参数: 512，512
JVM参数: 不配置

## 内存查询

jinfo -flags 1:
```
Attaching to process ID 1, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.341-b10
Non-default VM flags: -XX:CICompilerCount=2 -XX:InitialHeapSize=268435456 -XX:MaxHeapSize=536870912 -XX:MaxNewSize=178913280 -XX:MinHeapDeltaBytes=196608 -XX:NewSize=89456640 -XX:OldSize=178978816 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseFastUnorderedTimeStamps
Command line:  -javaagent:/data/agent.jar -Dxmiast.ip=172.31.1.194 -Dxmiast.port=9090 -Dxmiast.projectname=cloud-aiot -Dxmiast.nodename=cloud-aiot -Dxmiast.region= -Dxmiast.token=puzWfJyx9qVZxk19Xb5b5LQ -Dxmiast.writeconfig=false -Dxmiast.debug=DEBUG -Djava.security.egd=file:/dev/./urandom -Xms256m -Xmx512m
```

jmap -heap 1:
```
Attaching to process ID 1, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.341-b10

using thread-local object allocation.
Mark Sweep Compact GC

Heap Configuration:
MinHeapFreeRatio         = 40
MaxHeapFreeRatio         = 70
MaxHeapSize              = 536870912 (512.0MB)
NewSize                  = 89456640 (85.3125MB)
MaxNewSize               = 178913280 (170.625MB)
OldSize                  = 178978816 (170.6875MB)
NewRatio                 = 2
SurvivorRatio            = 8
MetaspaceSize            = 21807104 (20.796875MB)
CompressedClassSpaceSize = 1073741824 (1024.0MB)
MaxMetaspaceSize         = 17592186044415 MB
G1HeapRegionSize         = 0 (0.0MB)

Heap Usage:
New Generation (Eden + 1 Survivor Space):
capacity = 161021952 (153.5625MB)
used     = 105337928 (100.45807647705078MB)
free     = 55684024 (53.10442352294922MB)
65.41836482022029% used
Eden Space:
capacity = 143130624 (136.5MB)
used     = 95070776 (90.66655731201172MB)
free     = 48059848 (45.83344268798828MB)
66.4223863091661% used
From Space:
capacity = 17891328 (17.0625MB)
used     = 10267152 (9.791519165039062MB)
free     = 7624176 (7.2709808349609375MB)
57.38619290865385% used
To Space:
capacity = 17891328 (17.0625MB)
used     = 0 (0.0MB)
free     = 17891328 (17.0625MB)
0.0% used
tenured generation:
capacity = 357957632 (341.375MB)
used     = 283499712 (270.36639404296875MB)
free     = 74457920 (71.00860595703125MB)
79.19923662921092% used

37552 interned Strings occupying 4193280 bytes.
```

## TODO
1、验证配置了同样的xms数值，为什么本地有GC但是K8S却没有进行GC?
2、对于不确定参数，是否不需要进行额外配置，只需要保证K8S的限制加上了就可以?
3、默认新生代和老年代的比例是1:2，但是xms这个参数应该包含两部分的和吧?
4、是否只是JVM参数配置不正确的原因?做个只调用controller不调用okhttp的接口进行测试，是否会内存溢出。
5、还有一个手段，在Pod中打印查看GC日志，看看是否进行了GC，以及GC效果怎样

## 参考资料
* [JVM - OOM排查经历]({% post_url java/jvm/content/2023-03-01-04-jvm-oom-practice %})
* https://blog.csdn.net/m0_45406092/article/details/110314473
* https://juejin.cn/post/6884852550571556877
* https://www.cnblogs.com/guanghe/p/13558412.html

开始写作吧
```
![image-alter]({{ site.baseurl }}/image/post/2023/03/01/04/xxx.jpg)
```
