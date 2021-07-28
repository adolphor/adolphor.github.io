---
layout:     post
title:      Java并发目录
date:       2020-03-15 22:28:27 +0800
postId:     2020-03-15-22-28-28
categories: [concurrent]
keywords:   [concurrent]
---

## 并发基础知识
* [基本概念]({% post_url java/concurrent/content/2021-07-27-01-concurrent-basic-conception %})

## 基本元素
* [Thread 和 Runnable]({% post_url java/concurrent/2020-03-22-concurrent-thread-runnable %})
* [synchronized 关键字]({% post_url java/concurrent/content/2021-07-29-01-concurrent-keyword-synchronized %})
* [volatile 关键字]({% post_url java/concurrent/content/2021-07-29-02-concurrent-keyword-volatile %})

## 锁
* [锁分类]({% post_url java/concurrent/content/2021-07-29-03-concurrent-lock-type %})

## 同步机制
* [同步机制]({% post_url java/concurrent/content/2021-07-27-02-concurrent-synchronization-mechanism %})

## 执行器
* [执行器]({% post_url java/concurrent/content/2021-07-27-03-concurrent-executor %})

## Fork/Join 框架
* [Fork/Join 框架]({% post_url java/concurrent/content/2021-07-27-04-concurrent-fork-join-framework %})

## 并行流
* [并行流]({% post_url java/concurrent/content/2021-07-27-05-concurrent-stream %})

## 并发数据结构
* [并发数据结构]({% post_url java/concurrent/content/2021-07-27-06-concurrent-data-structure %})

## 并发设计模式
* [并发设计模式]({% post_url java/concurrent/content/2021-07-27-07-concurrent-design-patterns %})


## 模板代码

```java
private static String postTitle = "Java并发 - 基本概念";
private static String urlTitle = "java-concurrent-basic-conception";
private static String categories = "[concurrent]";
private static String tags = "[Java,concurrent]";
private static String folder = "java" + File.separator + "concurrent" + File.separator + "content";
private static String number = "01";
```

## 参考资料

* [Java并发目录]({% post_url java/concurrent/2020-03-15-java-concurrent-menus %})
* 书籍
    - [7.4' - 精通Java并发编程（第2版）](https://book.douban.com/subject/30327401/)
    - [9.1' - Concurrent Programming in Java](https://book.douban.com/subject/1440218/)
    - [8.6' - 图解Java多线程设计模式](https://book.douban.com/subject/27116724/)

* 文章
    - [如何学习Java多线程](https://zhuanlan.zhihu.com/p/35382932)
    - [Java Concurrency and Multithreading Tutorial](http://tutorials.jenkov.com/java-concurrency/index.html)
    - [Java Concurrency Utilities - ScheduledExecutorService](http://tutorials.jenkov.com/java-util-concurrent/scheduledexecutorservice.html)
    - [Java concurrency (multi-threading) - Tutorial](https://www.vogella.com/tutorials/JavaConcurrency/article.html)

