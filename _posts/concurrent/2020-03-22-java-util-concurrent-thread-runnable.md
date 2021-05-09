---
layout:     post
title:      Java并发包 ——  Thread 和 Runnable
date:       2020-03-22 17:19:35 +0800
postId:     2020-03-22-17-19-36
categories: [concurrent]
tags:       [concurrent]
geneMenu:   true
excerpt:    Java并发包 ——  Thread 和 Runnable
---

## 创建线程的两种方法

Java 使用 Thread 类实现执行线程。你可以使用以下机制在应用程序中创建执行线程：
* 扩展 Thread 类并重载 run()方法
* 实现 Runnable 接口，并将该类的对象传递给 Thread 对象的构造函数

一旦有了 Thread 对象，就必须使用 start()方法创建新的执行线程并且执行 Thread 类的 run() 方法。如果直接调用 run()方法，那么你将调用常规 Java 方法而不会创建新的执行线程。

这两种情况下你都会得到一个 Thread 对象，但是相对于第一种方式来说，更推荐使用第二种，其主要优势如下：
* Runnable 是一个接口: 你可以实现其他接口并扩展其他类；对于采用 Thread 类的方式，你只能扩展这一个类。
* 可以通过线程来执行 Runnable 对象，但也可以通过其他类似执行器的 Java 并发对象来执行。这样可以更灵活地更改并发应用程序。
* 可以通过不同线程使用同一 Runnable 对象。

## 线程的状态

在给定时间内，线程只能处于一个状态。这些状态不能映射到操作系统的线程状态，它们是 JVM 使用的状态。线程的可能状态如下：

* NEW: Thread 对象已经创建，但是还没有开始执行。
* RUNNABLE: Thread 对象正在 Java 虚拟机中运行。
* BLOCKED: Thread 对象正在被锁定。
* WAITING: Thread 对象正在等待另一个线程的动作。
* TIME_WAITING: Thread 对象正在等待另一个线程的操作，但是有时间限制。
* THREAD: Thread 对象已经完成了执行。


## 守护线程 & 非守护线程

在 Java 中，可以创建两种线程，二者之间的区别在于它们如何影响程序的结束。当有下列情形之一时，Java 程序将结束其执行过程：
* 程序执行 Runtime 类的 exit()方法，而且用户有权执行该方法
* 应用程序的所有非守护线程均已结束执行，无论是否有正在运行的守护线程

具有这些特征的守护线程通常用在作为垃圾收集器或缓存管理器的应用程序中，执行辅助任务。 你可以使用 isDaemon()方法检查线程是否为守护线程，也可以使用 setDaemon()方法将某个线程 确立为守护线程。要注意，必须在线程使用 start()方法开始执行之前调用此方法。


## 参考资料

* [8.2' - 精通Java并发编程（第二版）]({% post_url book/2020-09-15-mastering-concurrency-programming-with-java9-2th-edition %})