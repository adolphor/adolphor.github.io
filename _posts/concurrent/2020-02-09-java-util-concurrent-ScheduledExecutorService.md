---
layout:     post
title:      Java并发包 ——  ScheduledExecutorService
date:       2020-02-09 14:46:19 +0800
postId:     2020-02-09-14-46-19
categories: [concurrent]
tags:       [Java,concurrent]
geneMenu:   true
excerpt:    Java并发包 ——  ScheduledExecutorService
---

`java.util.concurrent.ScheduledExecutorService` 是一种执行服务，它可以安排任务在延迟一定时间后运行，或者在每次执行之间以固定的时间间隔重复执行。而且此任务是由一个工作线程异步执行的，而不是由将任务交给预定执行程序服务的线程执行。

## ScheduledExecutorService例子

首先创建一个带有5个线程的SechuleExecutorService。然后将Callbale接口的匿名实例类被传递给schedule()方法。最后两个参数指定了Callable将在5秒后执行。


```java
public class ScheduledExecutorServiceDemo {
  public static void main(String[] args) throws ExecutionException, InterruptedException {
    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
    ScheduledFuture scheduledFuture = executorService.schedule(() -> {
      System.out.println("Executed!");
      return "Called!";
    }, 5, TimeUnit.SECONDS);
    Object result = scheduledFuture.get();
    System.out.println("result = " + result);
  }
}
```



## 官方接口实现

`ScheduledExecutorService` 是一个接口标记类，`java.util.concurrent` 包中有以下关于此接口的实现类：`ScheduledThreadPoolExecutor`



## 相关概念

### Java Callable

The Java `Callable` interface, `java.util.concurrent.Callable`, represents an asynchronous task which can be executed by a separate thread. For instance, it is possible to submit a `Callable` object to a [Java ExecutorService](http://tutorials.jenkov.com/java-util-concurrent/executorservice.html) which will then execute it asynchronously.

`java.util.concurrent.Callable` 接口，表示可以用另外一个线程执行一个异步任务。比如，可以提交一个 `Callable` 对象给 [Java ExecutorService](http://tutorials.jenkov.com/java-util-concurrent/executorservice.html) 来异步执行。

### Java Future

A Java *Future*, `java.util.concurrent.Future`, represents the result of an asynchronous computation. When the asynchronous task is created, a Java `Future` object is returned. This `Future` object functions as a handle to the result of the asynchronous task. Once the asynchronous task completes, the result can be accessed via the `Future` object returned when the task was started.


## 参考资料

* [Java常用类源码——Thread源码解析](https://wangchangchung.github.io/2016/12/05/Java常用类源码——Thread源码解析/)
* [Java并发包：ScheduledExecutorService](https://blog.csdn.net/zxc123e/article/details/51911652)
* [Java Concurrency Utilities - ScheduledExecutorService](http://tutorials.jenkov.com/java-util-concurrent/scheduledexecutorservice.html)
* [Java concurrency (multi-threading) - Tutorial](https://www.vogella.com/tutorials/JavaConcurrency/article.html)
