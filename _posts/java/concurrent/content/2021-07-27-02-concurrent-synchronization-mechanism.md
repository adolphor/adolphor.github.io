---
layout:     post
title:      Java并发 - 同步机制
date:       2021-07-27 23:10:36 +0800
postId:     2021-07-27-23-10-36
categories: [Concurrent]
keywords:   [Java,concurrent]
---

下面是最重要的同步机制

## synchronized 关键字
synchronized 关键字允许你在某个代码块或者某个完整的方法中定义一个临界段。

## volatile 关键字

## final 关键字

## static 关键字

## Lock 接口
Lock 提供了比 synchronized 关键字更为灵活的同步操作。Lock 接口有多种不同类型: 
ReentrantLock 用于实现一个可与某种条件相关联的锁; ReentrantRead- WriteLock 将读写操作
分离开来; StampedLock 是 Java 8 中增加的一种新特性，它包括三种 控制读/写访问的模式。

## Semaphore 类
该类通过实现经典的信号量机制来实现同步。Java 支持二进制信号量和一般信号量。

## CountDownLatch 类
该类允许一个任务等待多项操作的结束。

## CyclicBarrier 类
该类允许多线程在某一共同点上进行同步。

## Phaser 类
该类允许你控制那些分割成多个阶段的任务的执行。在所有任务都完成当前阶段之前，任何任务都不能进入下一阶段

## 参考资料
* [Java并发 - 同步机制]({% post_url java/concurrent/content/2021-07-27-02-concurrent-synchronization-mechanism %})
* [7.4' - 精通Java并发编程（第2版）](https://book.douban.com/subject/30327401/)
* [8.8' - Java多线程编程实战指南-核心篇](https://book.douban.com/subject/27034721/)
