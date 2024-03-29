---
layout:     post
title:      Java并发目录
date:       2020-03-15 22:28:27 +0800
postId:     2020-03-15-22-28-28
categories: [menus]
keywords:   [Concurrent]
---

## 并发基础知识
* [基本概念]({% post_url java/concurrent/content/2021-07-27-01-concurrent-basic-conception %})
* [Thread 和 Runnable]({% post_url java/concurrent/content/2020-03-22-concurrent-thread-runnable %})
* [ThreadLocal]({% post_url java/concurrent/content/2021-09-05-01-java-concurrent-threadLocal %})
* [synchronized 关键字]({% post_url java/concurrent/content/2021-07-29-01-concurrent-keyword-synchronized %})
* [volatile 关键字]({% post_url java/concurrent/content/2021-07-29-02-concurrent-keyword-volatile %})
* [AQS 原理分析]({% post_url java/concurrent/content/2021-09-27-06-java-concurrent-AQS %})
* [ReentrantLock]({% post_url java/concurrent/content/2021-09-27-03-java-concurrent-ReentrantLock %})
* [线程池]({% post_url java/concurrent/content/2021-09-27-04-java-concurrent-ThreadPool %})

## 同步机制
* [Java内存模型]({% post_url java/concurrent/content/2021-08-23-04-concurrent-memory-model %})
* [CAS与原子变量]({% post_url java/concurrent/content/2021-08-06-01-concurrent-cas-atomic %})
* [线程安全的设计方式]({% post_url java/concurrent/content/2021-08-23-03-concurrent-thread-safe-design %})
* [锁类型和演化过程]({% post_url java/concurrent/content/2021-07-29-03-concurrent-lock-type %})
* [同步机制]({% post_url java/concurrent/content/2021-07-27-02-concurrent-synchronization-mechanism %})
  - synchronized 关键字
  - volatile 关键字
  - final 关键字
  - static 关键字
  - Lock 接口
  - Semaphore 类
  - CountDownLatch 类
  - CyclicBarrier 类
  - Phaser 类

## 线程间协作
* [wait和notify]({% post_url java/concurrent/content/2021-08-22-03-concurrent-wait-notify %})
* [线程中断]({% post_url java/concurrent/content/2021-08-23-01-concurrent-interrupt %})
* [线程停止]({% post_url java/concurrent/content/2021-08-23-02-concurrent-thread-stop %})
* [执行器]({% post_url java/concurrent/content/2021-07-27-03-concurrent-executor %})
  - Executor 接口
  - ExecutorService 接口
  - Callable 接口
  - Future 接口
* [Fork/Join 框架]({% post_url java/concurrent/content/2021-07-27-04-concurrent-fork-join-framework %})

## 并行流
* [并行流]({% post_url java/concurrent/content/2021-07-27-05-concurrent-stream %})

## 并发数据结构
* [并发数据结构]({% post_url java/concurrent/content/2021-07-27-06-concurrent-data-structure %})

## 并发设计模式
* [并发设计模式]({% post_url java/concurrent/content/2021-07-27-07-concurrent-design-patterns %})
* [Java并发 - 【转】在 Java 中我们该如何应对阻塞调用]({% post_url java/concurrent/content/2023-03-11-02-a-tale-of-two-cities-how-blocking-calls-are-treated %})
* [Java并发 -【转】Java 19 发布，Loom 怎么解决 Java 的并发模型缺陷？]({% post_url java/concurrent/content/2023-03-12-01-state-of-java-project-loom %})

## 模板代码
```java
private static String postTitle = "Java并发 - 基本概念";
private static String urlTitle = "basic-conception";
private static String categories = "[Concurrent]";
private static String tags = "[Java,concurrent]";
private static String folder = "java" + File.separator + "concurrent" + File.separator + "content";
```

## 参考资料
* [Java并发目录]({% post_url java/concurrent/2020-03-15-java-concurrent-menus %})
* 书籍
    - [7.4' - 精通Java并发编程（第2版）](https://book.douban.com/subject/30327401/)
    - [9.1' - Concurrent Programming in Java](https://book.douban.com/subject/1440218/)
    - [8.6' - 图解Java多线程设计模式](https://book.douban.com/subject/27116724/)
    - [7.4' - Java并发编程的艺术](https://book.douban.com/subject/26591326/)
    - [8.8' - Java多线程编程实战指南-核心篇](https://book.douban.com/subject/27034721/) ：[源码下载](https://github.com/viscent/javamtia)

* 文章
    - [如何学习Java多线程](https://zhuanlan.zhihu.com/p/35382932)
    - [Java Concurrency and Multithreading Tutorial](http://tutorials.jenkov.com/java-concurrency/index.html)
    - [Java Concurrency Utilities - ScheduledExecutorService](http://tutorials.jenkov.com/java-util-concurrent/scheduledexecutorservice.html)
    - [Java concurrency (multi-threading) - Tutorial](https://www.vogella.com/tutorials/JavaConcurrency/article.html)

