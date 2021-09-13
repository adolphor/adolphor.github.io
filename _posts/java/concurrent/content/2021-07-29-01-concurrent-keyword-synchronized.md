---
layout:     post
title:      Java并发 - synchronized 关键字
date:       2021-07-29 00:50:32 +0800
postId:     2021-07-29-00-50-32
categories: [concurrent]
keywords:   [Java,concurrent]
---

在多线程并发编程中 synchronized 一直是元老级角色，很多人都会称呼它为重量级锁。但是，随着 
Java SE 1.6 对 synchronized 进行了各种优化之后，有些情况下它就并不那么重了。本文详细介绍
Java SE 1.6 中为了减少获得锁和释放锁带来的性能消耗而引入的偏向锁和轻量级锁，以及锁的存储结构
和升级过程。

## 锁对象
利用 synchronized 实现同步的基础:Java 中的每一个对象都可以作为锁。具体表现为以下 3 种形式：
* 普通方法：锁是当前实例对象
* 静态方法：锁是当前类的 Class 对象
* 方法块：  锁是 Synchonized 括号里配置的对象

## 实现原理
当一个线程试图访问同步代码块时，它首先必须得到锁，退出或抛出异常时必须释放锁。那么锁到底存在
哪里呢？锁里面会存储什么信息呢？


开始写作吧
```
![image-alter]({{ site.baseurl }}/image/post/2021/07/29/01/xxx.jpg)
```

## 参考资料

* [Java并发 - synchronized 关键字]({% post_url java/concurrent/content/2021-07-29-01-concurrent-keyword-synchronized %})
* [8.8' - Java多线程编程实战指南-核心篇](https://book.douban.com/subject/27034721/)
* [Java并发 - Java内存模型]({% post_url java/concurrent/content/2021-08-23-04-concurrent-memory-model %})
* [Synchronized解析——如果你愿意一层一层剥开我的心](https://juejin.cn/post/6844903918653145102#heading-18)
