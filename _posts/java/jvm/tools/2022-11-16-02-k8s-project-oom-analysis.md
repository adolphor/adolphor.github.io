---
layout:     post
title:      K8S项目异常的内存分析
date:       2022-11-16 14:56:32 +0800
postId:     2022-11-16-14-56-32
categories: [JVM]
keywords:   [Java, JVM]
---

K8S容器内的Java项目无响应，导致服务异常，通过分析Java的dump文件，来分析具体原因，记录备忘。

## 分析工具

mat：Eclipse Memory Analyzer Version

* incoming：拥有当前对象的引用的所有对象
* outcoming：当前对象引用的所有对象

![top-cosumer.png]({{ site.baseurl }}/image/post/2022/11/16/02/top-cosumer.png)
![outgoing-reference.png]({{ site.baseurl }}/image/post/2022/11/16/02/outgoing-reference.png)

## 经验教训
* K8S：设置K8S中Pod节点的所需内存和最大内存
* JVM：JVM参数中也要设置匹配的最大和最小内存限制

## 参考资料

* [K8S项目异常的内存分析]({% post_url java/jvm/tools/2022-11-16-02-k8s-project-oom-analysis %})

