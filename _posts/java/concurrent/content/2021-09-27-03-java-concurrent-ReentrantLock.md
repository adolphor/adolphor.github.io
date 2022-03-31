---
layout:     post
title:      Java并发 - ReentrantLock
date:       2021-09-27 11:05:56 +0800
postId:     2021-09-27-11-05-56
categories: [concurrent]
keywords:   [Java,concurrent]
---


![AQS-ReentrantLock]({{ site.baseurl }}/image/post/2021/09/27/03/AQS-ReentrantLock.webp)


## synchronized 和 ReentrantLock 的区别

### 两者都是可重入锁
synchronized 是依赖于 JVM 实现的，前面我们也讲到了 虚拟机团队在 JDK1.6 为 synchronized 
关键字进行了很多优化，但是这些优化都是在虚拟机层面实现的，并没有直接暴露给我们。ReentrantLock 
是 JDK 层面实现的（也就是 API 层面，需要 lock() 和 unlock() 方法配合 try/finally 语句块来完成），
所以我们可以通过查看它的源代码，来看它是如何实现的。

### ReentrantLock 增加了一些高级功能
相比synchronized，ReentrantLock增加了一些高级功能。主要来说主要有三点：
* **等待可中断** : ReentrantLock提供了一种能够中断等待锁的线程的机制，通过 `lock.lockInterruptibly()` 来实现这个机制。也就是说正在等待的线程可以选择放弃等待，改为处理其他事情。
* **可实现公平锁** : ReentrantLock可以指定是公平锁还是非公平锁。而synchronized只能是非公平锁。所谓的公平锁就是先等待的线程先获得锁。ReentrantLock默认情况是非公平的，可以通过 ReentrantLock类的`ReentrantLock(boolean fair)`构造方法来制定是否是公平的。
* **可实现选择性通知（锁可以绑定多个条件）**: synchronized关键字与wait()和notify()/notifyAll()方法相结合可以实现等待/通知机制。ReentrantLock类当然也可以实现，但是需要借助于`Condition接口`与`newCondition()`方法。

## 参考资料
* [Java并发 - ReentrantLock]({% post_url java/concurrent/content/2021-09-27-03-java-concurrent-ReentrantLock %})
* [synchronized-和-reentrantlock-的区别](https://snailclimb.gitee.io/javaguide/#/docs/java/multi-thread/Java并发进阶常见面试题总结?id=_15-谈谈-synchronized-和-reentrantlock-的区别)
* [AQS&&ReentrantLock](https://mp.weixin.qq.com/s?__biz=MzU4NzA3MTc5Mg==&mid=2247484035&idx=1&sn=ccaec352e192f1fd40020d9a984e9461&chksm=fdf0eadcca8763ca5c44bd19118fd00e843c163deb40cda444b3fc08430c57760db15eca1ea6&scene=21&cur_album_id=1657204970858872832#wechat_redirect)

