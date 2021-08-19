---
layout:     post
title:      Java并发 - CAS与原子变量
date:       2021-08-06 17:53:51 +0800
postId:     2021-08-06-17-53-51
categories: [concurrent]
keywords:   [Java,concurrent]
---

在 Java 并发领域，我们解决并发安全问题最粗暴的方式就是使用 synchronized 关键字了，但它是
一种独占形式的锁，属于悲观锁机制，性能会大打折扣。volatile 貌似也是一个不错的选择，但 
volatile 只能保持变量的可见性，并不保证变量的原子性操作。

CAS 全称是 compare and swap，即比较并交换，它是一种原子操作，同时 CAS 是一种乐观机制。
java.util.concurrent 包很多功能都是建立在 CAS 之上，如 ReenterLock 内部的 AQS，各种
原子类，其底层都用 CAS来实现原子操作。



开始写作吧
```
![image-alter]({{ site.baseurl }}/image/post/2021/08/06/01/xxx.jpg)
```

## 参考资料

* [Java并发 - CAS与原子变量]({% post_url java/concurrent/content/2021-08-06-01-concurrent-cas-atomic %})
* [后端进阶 - Java并发之CAS原理分析](https://objcoding.com/2018/11/29/cas/)
