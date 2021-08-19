---
layout:     post
title:      Java并发 - 锁分类
date:       2021-07-29 00:54:10 +0800
postId:     2021-07-29-00-54-10
categories: [concurrent]
keywords:   [Java,concurrent]
---

### 锁概述
线程安全问题的产生前提是多个线程并发访问共享变量、共享资源(以下统称为共享数据)。
那么一种保障线程安全的方法是将多个线程对共享数据的并发访问转换为串行访问，即一个共享数据一次
只能被一个线程访问，该线程访问结束后其他线程才能对其进行访问。锁(Lock)就是利用这种思路以保障
线程安全的线程同步机制。

按照上述思路，锁可以理解为对共享数据进行保护的许可证。对于同一个许可证所保
护的共享数据而言，任何线程访问这些共享数据前必须先持有该许可证。一一个线程只有在
持有许可证的情况下才能够对这些共享数据进行访问；并且，一个许可证一次只能够被一
个线程持有；许可证的持有线程在其结束对这些共享数据的访问后必须让出(释放)其持
有的许可证，以便其他线程能够对这些共享数据进行访问。

一个线程在访问共享数据前必须申请相应的锁(许可证)，线程的这个动作被称为锁
的获得( Acquire)。一个线程获得某个锁(持有许可证)，我们就称该线程为相应锁的持
有线程(线程持有许可证)，一个锁一次只能被一个线程持有。锁的持有线程可以对该锁
所保护的共享数据进行访问，访问结束后该线程必须释放(Release)相应的锁。锁的持有
线程在其获得锁之后和释放锁之前这段时间内所执行的代码被称为临界区(Critical
Section)。因此，共享数据只允许在临界区内进行访问，临界区一次只能被一个线程执行。

按照Java虛拟机对锁的实现方式划分，Java平台中的锁包括内部锁(IntrinsicLock)
和显式锁(ExplicitLock)。内部锁是通过synchronized关键字实现的；显式锁是通过
java.concurrent.locks.Lock接口的实现类(如java.concurrent.locks.ReentrantLock类)实
现的。



开始写作吧
```
![image-alter]({{ site.baseurl }}/image/post/2021/07/29/03/xxx.jpg)
```

## 参考资料

* [Java并发 - 锁分类]({% post_url java/concurrent/content/2021-07-29-03-concurrent-lock-type %})
* [美团技术团队 - 不可不说的Java“锁”事](https://tech.meituan.com/2018/11/15/java-lock.html)
* [Java中的锁分类](https://www.cnblogs.com/qifengshi/p/6831055.html)
* [Java之锁的分类、原理及实例](https://juejin.cn/post/6882622510148354056)
* [阿里巴巴中间件 - 聊聊 Java 的几把 JVM 级锁](https://www.infoq.cn/article/4wofifidqz076zdaiau6)
