---
layout:     post
title:      Java并发 - 执行器
date:       2021-07-27 23:14:40 +0800
postId:     2021-07-27-23-14-40
categories: [concurrent]
keywords:   [Java,concurrent]
---

执行器框架是在实现并发任务时将线程的创建和管理分割开来的一种机制。你不必担心线程的创 建和管理，
只需要关心任务的创建并且将其发送给执行器。执行器框架另一个重要的优势是 Callable 接口。它类似
于 Runnable 接口，但是却提供了两方面的增强：
* 这个接口的主方法名称为 call() ，可以返回结果。
* 当发送一个 Callable 对象给执行器时，将获得一个实现了 Future 接口的对象。可以使用这个对象
  来控制 Callable 对象的状态和结果。

### Executor 接口和 ExecutorService 接口
它们包含了所有执行器共有的 execute()方法。

### ThreadPoolExecutor 类
该类允许你获取一个含有线程池的执行器，而且可以定义并行任务的最大数目。

### ScheduledThreadPoolExecutor 类
这是一种特殊的执行器，可以使你在某段延迟之后执行任务或者周期性执行任务。

### Executors
该类使执行器的创建更为容易。

### Callable 接口
这是 Runnable 接口的替代接口——可返回值的一个单独的任务。

### Future 接口
该接口包含了一些能获取 Callable 接口返回值并且控制其状态的方法。

## 参考资料

* [Java并发 - 执行器]({% post_url java/concurrent/content/2021-07-27-03-concurrent-executor %})
* [7.4' - 精通Java并发编程（第2版）](https://book.douban.com/subject/30327401/)
