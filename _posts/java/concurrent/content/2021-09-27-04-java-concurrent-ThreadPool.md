---
layout:     post
title:      Java并发 - 线程池
date:       2021-09-27 11:22:22 +0800
postId:     2021-09-27-11-22-22
categories: [concurrent]
keywords:   [Java,concurrent]
---

线程池（Thread Pool）是一种基于池化思想管理线程的工具，经常出现在多线程服务器中，如MySQL。
线程过多会带来额外的开销，其中包括创建销毁线程的开销、调度线程的开销等等，同时也降低了计算机的整体性能。
线程池维护多个线程，等待监督管理者分配可并发执行的任务。这种做法，一方面避免了处理任务时创建销毁线程开销的代价，
另一方面避免了线程数量膨胀导致的过分调度问题，保证了对内核的充分利用。

《阿里巴巴 Java 开发手册》中强制线程池不允许使用 Executors 去创建，而是通过 ThreadPoolExecutor 的方式，
这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。

## 线程池

### 池化技术
池化，顾名思义，是为了最大化收益并最小化风险，而将资源统一在一起管理的一种思想。
```
Pooling is the grouping together of resources (assets, equipment, personnel, effort, etc.) 
for the purposes of maximizing advantage or minimizing risk to the users. The term is used 
in finance, computing and equipment management. ——wikipedia
```
“池化”思想不仅仅能应用在计算机领域，在金融、设备、人员管理、工作管理等领域也有相关的应用。
在计算机领域中的表现为：统一管理IT资源，包括服务器、存储、和网络资源等等。通过共享资源，
使用户在低投入中获益。除去线程池，还有其他比较典型的几种使用策略包括：

1. 内存池(Memory Pooling)：预先申请内存，提升申请内存速度，减少内存碎片。
2. 连接池(Connection Pooling)：预先申请数据库连接，提升申请连接的速度，降低系统的开销。
3. 实例池(Object Pooling)：循环使用对象，减少资源在初始化和释放时的昂贵损耗。

### 线程池好处
* **`降低资源消耗`**：通过池化技术重复利用已创建的线程，降低线程创建和销毁造成的损耗。
* **`提高响应速度`**：任务到达时，无需等待线程创建即可立即执行。
* **`提高线程的可管理性`**：线程是稀缺资源，如果无限制创建，不仅会消耗系统资源，还会因为线程的不合理分布导致资源调度失衡，降低系统的稳定性。使用线程池可以进行统一的分配、调优和监控。
* **`提供更多更强大的功能`**：线程池具备可拓展性，允许开发人员向其中增加更多的功能。比如延时定时线程池ScheduledThreadPoolExecutor，就允许任务延期执行或定期执行。


## 线程池总体设计
![ThreadPollExecutor继承关系]({{ site.baseurl }}/image/post/2021/09/27/04/ThreadPollExecutor继承关系.png)

ThreadPoolExecutor实现的顶层接口是Executor，顶层接口Executor提供了一种思想：将任务提交和任务执行进行解耦。
用户无需关注如何创建线程，如何调度线程来执行任务，用户只需提供Runnable对象，将任务的运行逻辑提交到执行器(Executor)中，
由Executor框架完成线程的调配和任务的执行部分。ExecutorService接口增加了一些能力：（1）扩充执行任务的能力，
补充可以为一个或一批异步任务生成Future的方法；（2）提供了管控线程池的方法，比如停止线程池的运行。
AbstractExecutorService则是上层的抽象类，将执行任务的流程串联了起来，保证下层的实现只需关注一个执行任务的方法即可。
最下层的实现类ThreadPoolExecutor实现最复杂的运行部分，ThreadPoolExecutor将会一方面维护自身的生命周期，
另一方面同时管理线程和任务，使两者良好的结合从而执行并行任务。

线程池在内部实际上构建了一个生产者消费者模型，将线程和任务两者解耦，并不直接关联，从而良好的缓冲任务，复用线程。
线程池的运行主要分成两部分：任务管理、线程管理。任务管理部分充当生产者的角色，当任务提交后，
线程池会判断该任务后续的流转：（1）直接申请线程执行该任务；（2）缓冲到队列中等待线程执行；
（3）拒绝该任务。线程管理部分是消费者，它们被统一维护在线程池内，根据任务请求进行线程的分配，
当线程执行完任务后则会继续获取新的任务去执行，最终当线程获取不到任务的时候，线程就会被回收。

## 创建方式

### 通过构造方法实现
![construct创建方式]({{ site.baseurl }}/image/post/2021/09/27/04/construct创建.png)

### 通过 Executor 框架的工具类 Executors 来实现
我们可以创建三种类型的 ThreadPoolExecutor：
* SingleThreadExecutor
* FixedThreadPool
* CachedThreadPool

![Executor框架的工具类]({{ site.baseurl }}/image/post/2021/09/27/04/Executor框架的工具类.png)

#### SingleThreadExecutor
方法返回一个只有一个线程的线程池。若多余一个任务被提交到该线程池，任务会被保存在一个任务队列中，
待线程空闲，按先入先出的顺序执行队列中的任务。

#### FixedThreadPool
该方法返回一个固定线程数量的线程池。该线程池中的线程数量始终不变。当有一个新的任务提交时，
线程池中若有空闲线程，则立即执行。若没有，则新的任务会被暂存在一个任务队列中，待有线程空闲时，
便处理在任务队列中的任务。

#### CachedThreadPool
该方法返回一个可根据实际情况调整线程数量的线程池。线程池的线程数量不确定，但若有空闲线程可以复用，
则会优先使用可复用的线程。若所有线程均在工作，又有新的任务提交，则会创建新的线程处理任务。
所有线程在当前任务执行完毕后，将返回线程池进行复用。

## ThreadPoolExecutor 类分析
`ThreadPoolExecutor` 类中提供的四个构造方法。我们来看最长的那个，其余三个都是在这个构造方法的基础上产生
（其他几个构造方法说白点都是给定某些默认参数的构造方法比如默认制定拒绝策略是什么），这里就不贴代码讲了，比较简单。
```java
public ThreadPoolExecutor(int corePoolSize,
                          int maximumPoolSize,
                          long keepAliveTime,
                          TimeUnit unit,
                          BlockingQueue<Runnable> workQueue,
                          ThreadFactory threadFactory,
                          RejectedExecutionHandler handler) {
    if (corePoolSize < 0 ||
        maximumPoolSize <= 0 ||
        maximumPoolSize < corePoolSize ||
        keepAliveTime < 0)
        throw new IllegalArgumentException();
    if (workQueue == null || threadFactory == null || handler == null)
        throw new NullPointerException();
    this.acc = System.getSecurityManager() == null ? null : AccessController.getContext();
    this.corePoolSize = corePoolSize;
    this.maximumPoolSize = maximumPoolSize;
    this.workQueue = workQueue;
    this.keepAliveTime = unit.toNanos(keepAliveTime);
    this.threadFactory = threadFactory;
    this.handler = handler;
}
```

`ThreadPoolExecutor` 3 个最重要的参数：
* **`corePoolSize`**：核心线程数线程数定义了最小可以同时运行的线程数量。
* **`maximumPoolSize`**：当队列中存放的任务达到队列容量的时候，当前可以同时运行的线程数量变为最大线程数。
* **`workQueue`**：当新任务来的时候会先判断当前运行的线程数量是否达到核心线程数，如果达到的话，新任务就会被存放在队列中。

`ThreadPoolExecutor` 其他常见参数:
* **`keepAliveTime`**：当线程池中的线程数量大于 corePoolSize 的时候，如果这时没有新的任务提交，核心线程外的线程不会立即销毁，而是会等待，直到等待的时间超过了 keepAliveTime才会被回收销毁；
* **`unit`**：keepAliveTime 参数的时间单位。
* **`threadFactory`**：executor 创建新线程的时候会用到。
* **`handler`**：饱和策略。关于饱和策略下面单独介绍一下。

![图解线程池实现原理]({{ site.baseurl }}/image/post/2021/09/27/04/图解线程池实现原理.png)

### corePoolSize
### maximumPoolSize
### workQueue
### keepAliveTime
### unit
### threadFactory
### handler
如果当前同时运行的线程数量达到最大线程数量并且队列也已经被放满了任务时，ThreadPoolTaskExecutor 定义一些策略:

#### ThreadPoolExecutor.AbortPolicy
抛出 `RejectedExecutionException` 来拒绝新任务的处理。这也是缺省时的默认策略。

#### ThreadPoolExecutor.CallerRunsPolicy
调用执行自己的线程运行任务，也就是直接在调用execute方法的线程中运行(run)被拒绝的任务，
如果执行程序已关闭，则会丢弃该任务。因此这种策略会降低对于新任务提交速度，影响程序的整体性能。
如果您的应用程序可以承受此延迟并且你要求任何一个任务请求都要被执行的话，你可以选择这个策略。

#### ThreadPoolExecutor.DiscardPolicy 
不处理新任务，直接丢弃掉。

#### ThreadPoolExecutor.DiscardOldestPolicy
此策略将丢弃最早的未处理的任务请求。


## 示例
```java
public class ThreadPoolExecutorDemo {

  private static final int CORE_POOL_SIZE = 5;
  private static final int MAX_POOL_SIZE = 10;
  private static final int QUEUE_CAPACITY = 100;
  private static final Long KEEP_ALIVE_TIME = 1L;

  public static void main(String[] args) {

    //使用阿里巴巴推荐的创建线程池的方式
    //通过ThreadPoolExecutor构造函数自定义参数创建
    ThreadPoolExecutor executor = new ThreadPoolExecutor(
      CORE_POOL_SIZE,
      MAX_POOL_SIZE,
      KEEP_ALIVE_TIME,
      TimeUnit.SECONDS,
      new ArrayBlockingQueue<>(QUEUE_CAPACITY),
      new ThreadPoolExecutor.CallerRunsPolicy());

    for (int i = 0; i < 10; i++) {
      //创建WorkerThread对象（WorkerThread类实现了Runnable 接口）
      Runnable worker = new MyRunnable(String.valueOf(i));
      //执行Runnable
      executor.execute(worker);
    }
    //终止线程池
    executor.shutdown();
    while (!executor.isTerminated()) {
    }
    System.out.println("Finished all threads");
  }
}

public class MyRunnable implements Runnable {
  private String command;

  public MyRunnable(String s) {
    this.command = s;
  }

  @Override
  public void run() {
    System.out.println(Thread.currentThread().getName() + " Start. Time = " + new Date());
    processCommand();
    System.out.println(Thread.currentThread().getName() + " End. Time = " + new Date());
  }

  private void processCommand() {
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String toString() {
    return this.command;
  }

}
```

## 参考资料
* [Java并发 - 线程池]({% post_url java/concurrent/content/2021-09-27-04-java-concurrent-ThreadPool %})
* [线程池](https://snailclimb.gitee.io/javaguide/#/docs/java/multi-thread/Java并发进阶常见面试题总结?id=_4-线程池)
* [Java线程池实现原理及其在美团业务中的实践](https://tech.meituan.com/2020/04/02/java-pooling-pratice-in-meituan.html)
