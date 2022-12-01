---
layout:     post
title:      Java并发 - 执行器
date:       2021-07-27 23:14:40 +0800
postId:     2021-07-27-23-14-40
categories: [Concurrent]
keywords:   [Java,concurrent]
---

执行器框架是在实现并发任务时将线程的创建和管理分割开来的一种机制。你不必担心线程的创 建和管理，
只需要关心任务的创建并且将其发送给执行器。执行器框架另一个重要的优势是 `Callable接口`。它类似
于 `Runnable 接口`，但是却提供了两方面的增强：
* 这个接口的主方法名称为 call() ，可以返回结果。
* 当发送一个 Callable 对象给执行器时，将获得一个实现了 Future 接口的对象。可以使用这个对象
  来控制 Callable 对象的状态和结果。

Runnable接口 和 Callable接口 都是对任务处理逻辑的抽象，这种抽象使得我们无须关心任务的具体
处理逻辑：不管是什么样的任务，其处理逻辑总是展现为一个具有统一签名的方法 —— `Runnable.run()`
或者 `Callable.call()`。`java.util.concurrent.Executor` 接口则是对任务的执行进行的抽象，
该接口仅定义了如下方法:
```java
void execute (Runnable command);
```

其中，command参数代表需要执行的任务。Executor接口使得任务的提交方(相当于生产者)只需要知道
它调用 `Executor.execute` 方法便可以使指定的任务被执行，而无须关心任务具体的执行细节：比如，
任务是采用一个专门的工作者线程执行的，还是采用线程池执行的；采用什么样的线程池执行的；多个任务
是以何种顺序被执行的。可见，Executor接口使得任务的提交能够与任务执行的具体细节解耦(Decoupling)。
和对任务处理逻辑的抽象类似，对任务执行的抽象也能给我们带来信息隐藏(Information)和关注点分离
(Separation Of Concern)的好处。

## 基础架构

### Executor 接口
Executor接口比较简单，功能也十分有限：首先，它只能为客户端代码执行任务，而无法将任务的处理结果
返回给客户端代码;其次，Executor接口实现类内部往往会维护一些工作者线程；当我们不再需要一个
Executor实例的时候，往往需要主动将该实例内部维护的工作者线程停掉以释放相应的资源，而
Executor接口并没有定义相应的方法。

### ExecutorService 接口
ExecutorService接口继承自Executor 接口，它解决了上述问题。ExecutorService 接口定义了
几个submit方法，这些方法能够接受Callable接口或者Runnable接口表示的任务并返回相应的Future实例，
从而使客户端代码提交任务后可以获取任务的执行结果。ExecutorService接口还定义了shutdown()方法
和shutdownNow()方法来关闭相应的服务(比如关闭其维护的工作者线程)。ThreadPoolExecutor 是
ExecutorService的默认实现类。

### Executors 工具类
实用工具类java.util.concurrent.Executors， 它除了能够返回默认线程工厂
(Executors.defaultThreadFactory() )、能够将Runnable 实例转换为Callable 实例
(Executors.callable方法)之外，还提供了一些能够返回ExecutorService实例的快捷方法，
这些ExecutorService实例往往使我们在不必手动创建ThreadPoolExecutor 实例的情况下使用线程池。
该类使执行器的创建更为容易。

### Callable 接口
这是 Runnable 接口的替代接口 —— 可返回值的一个单独的任务。

### Future 接口
该接口包含了一些能获取 Callable 接口返回值并且控制其状态的方法。

## 各场景实现

### ThreadPoolExecutor 类
该类允许你获取一个含有线程池的执行器，而且可以定义并行任务的最大数目。

### ScheduledThreadPoolExecutor 类
这是一种特殊的执行器，可以使你在某段延迟之后执行任务或者周期性执行任务。

在有些情况下，我们可能需要事先提交一个任务,这个任务并不是立即被执行的，而是要在指定的时间或者
周期性地被执行，这种任务就被称为计划任务(Scheduled Task)。典型的计划任务包括清理系统垃圾数据、
系统监控、数据备份等。

ExecutorService接口的子类ScheduledExecutorService 接口定义了一组方法用于执行计划任务。
ScheduledExecutorService 接口的默认实现类是 `java.util.concurrent.ScheduledThreadPoolExecutor` 类，
它是ThreadPoolExecutor的一个子类。Executors除了提供创建ExecutorService实例的便捷工厂方法之外，
它还提供了两个静态工厂方法用于创建ScheduledExecutorService实例:
```java
public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize);
public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize, ThreadFactory threadFactory);
```

### CompletionService 接口
尽管Future接口使得我们能够方便地获取异步任务的处理结果，但是如果需要一次性提交一批异步任务
并获取这些任务的处理结果的话,那么仅使用Future接口写出来的代码将颇为烦琐。
java.util.concurrent.CompletionService接口为异步任务的批量提交以及获取这些任务的
处理结果提供了便利。CompletionService接口定义的一个submit 方法可用于提交异步任务，
该方法的签名与ThreadPoolExecutor的一一个submit方法相同：
```java
Future<V> submit(Callable<V> task);
```

task参数代表待执行的异步任务，该方法的返回值可用于获取相应异步任务的处理结果。如果是批量提交
异步任务，那么通常我们并不关心该方法的返回值。若要获取批量提交的异步任务的处理结果，那么我们
可以使用CompletionService 接口专门为此定义的方法，其中的一一个方法是：
```java
Future<V> take() throws InterruptException;
```
该方法与`BlockingQueue.take()`相似，它是-一个阻塞方法，其返回值是一个已经执行结束的异步任务
对应的Future实例，该实例就是提交相应任务时submit(Callable<V>)调用的返回值。如果take()
被调用时没有已执行结束的异步任务，那么take()的执行线程就会被暂停，直到有异步任务执行结束。
因此，我们批量提交了多少个异步任务，则多少次连续调用`CompletionService.take()`便可以获取
这些任务的处理结果。

### FutureTask 接口
无论是Runnable实例还是Callable 实例所表示的任务，只要我们将其提交给线程池执行，那么这些任务
就是异步任务。采用Runnable 实例来表示异步任务，其优点是任务既可以交给一个专门的工作者线程执行
(以相应的Runnable 实例为参数创建并启动一个工作者线程)，也可以交给一个线程池或者Executor的
其他实现类来执行；其缺点是我们无法直接获取任务的执行结果。使用Callable实例来表示异步任务，其
优点是我们可以通过ThreadPoolExecutor.submit(Callable<T>)的返回值获取任务的处理结果；其
缺点是Callable实例表示的异步任务只能交给线程池执行，而无法直接交给一个专门的工作者线程或者
Executor实现类执行。因此，使用Callable实例来表示异步任务会使任务执行方式的灵活性大为受限。

`java.util.concurrent.FutureTask` 类则融合了Runnable 接口和Callable 接口的优点：
FutureTask是Runnable接口的一个实现类，因此FutureTask表示的异步任务可以交给专门的
工作者线程执行，也可以交给Executor实例(比如线程池)执行; FutureTask还能够直接返回其代表的
异步任务的处理结果。ThreadPoolExecutor.submit(Callable<T> task)的返回值就是一个
FutureTask实例。FutureTask是`java.util.concurrent.RunnableFuture`接口的一个实现类。
由于RunnableFuture接口继承了Future 接口和Runnable 接口，因此FutureTask 既是
Runnable接口的实现类也是Future接口的实现。FutureTask的一个构造器可以将Callable实例
转换为Runnable实例，该构造器的声明如下：
```java
public FutureTask (Callable<V> callable);
```

该构造器使得我们能够方便地创建--个能够返回处理结果的异步任务。我们可以将任务的处理逻辑封装在
一个Callable实例中，并以该实例为参数创建-一个FutureTask实例。由于FutureTask类实现了Runnable 
接口，因此上述构造器的作用就相当于将Callable实例转换为Runnable实例，而FutureTask 实例本身
也代表了我们要执行的任务。我们可以用FutureTask实例(Runnable实例)为参数来创建并启动一个
工作者线程以执行相应的任务，也可以将FutureTask实例交给Executor执行(通过`Executor.execute(Runnable task)`
调用)。FutureTask类还实现了Future 接口，这使得我们在调用 `Executor.execute(Runnable task)`
这样只认Runnable接口的方法来执行任务的情况下依然能够获取任务的执行结果：一个工作者线程(可以是
线程池中的一个工作者线程)负责调用FutureTask.run()执行相应的任务，另外一个线程则调用
FutureTask.get()来获取任务的执行结果。因此，FutureTask 实例可被看作一个异步任务，
它使得任务的执行和对任务执行结果的处理得以并发执行，从而有利于提高系统的并发性。

### AsyncTask 异步任务类
FutureTask基本上是被设计用来表示- -次性执行的任务，其内部会维护一个表示任务运行状态
(包括未开始运行、已经运行结束等)的状态变量，FutureTask.run()在 执行任务处理逻辑前会先判断
相应任务的运行状态.如果该任务已经被执行过，那么FutureTask.run()会直接返回(并不会抛出异常)。
因此，FutureTask 实例所代表的任务是无法被重复执行的。这意味着同一个FutureTask实例不能多次
提交给Executor实例执行(尽管这样做不会导致异常的抛出)。FutureTask.runAndReset()能够打破
这种限制，使得一个FutureTask实例所代表的任务能够多次被执行。FutureTask.runAndReset()是
一个protected方法，它能够执行FutureTask实例所代表的任务但是不记录任务的处理结果。因此，如果
同一个对象所表示的任务需要被多次执行，并且我们需要对该任务每次的执行结果进行处理，那么 
FutureTask仍然是不适用的，此时我们可以考虑使用AsyncTask抽象异步任务类来表示这种任务。

AsyncTask抽象类同时实现了Runnable 接口和Callable接口。AsyncTask 子类通过覆盖call方法
来实现其任务处理逻辑，而AsyncTask.run()则充当任务处理逻辑的执行人口。AsyncTask实例可以
提交给Executor实例执行。当任务执行成功结束后，相应AsyncTask实例的onResult方法会被调用以
处理任务的执行结果;当任务执行过程中抛出异常时，相应AsyncTask实例的onError 方法会被调用以
处理这个异常。

## 参考资料

* [Java并发 - 执行器]({% post_url java/concurrent/content/2021-07-27-03-concurrent-executor %})
* [7.4' - 精通Java并发编程（第2版）](https://book.douban.com/subject/30327401/)
* [8.8' - Java多线程编程实战指南-核心篇](https://book.douban.com/subject/27034721/)
