---
layout:     post
title:      Java并发 - Fork/Join 框架
date:       2021-07-27 23:17:38 +0800
postId:     2021-07-27-23-17-38
categories: [Concurrent]
keywords:   [Java,concurrent]
---

Fork/Join 框架定义了一种特殊的执行器，尤其针对采用分治方法进行求解的问题。针对解决这类 问题的并发任务，它还提供了一种优化其执行的机制。Fork/Join 是为细粒度并行处理量身定制的，因 为它的开销非常小，这也是将新任务加入队列中并且按照队列排序执行任务的需要。该框架涉及的主 要类和接口如下。

### ForkJoinPool
该类实现了要用于运行任务的执行器。

### ForkJoinTask
这是一个可以在 ForkJoinPool 类中执行的任务。

### ForkJoinWorkerThread
这是一个准备在 ForkJoinPool 类中执行任务的线程。

## 参考资料

* [Java并发 - Fork/Join 框架]({% post_url java/concurrent/content/2021-07-27-04-concurrent-fork-join-framework %})
* [7.4' - 精通Java并发编程（第2版）](https://book.douban.com/subject/30327401/)
