---
layout:     post
title:      Java并发 - AQS 原理分析
date:       2021-09-27 15:04:01 +0800
postId:     2021-09-27-15-04-01
categories: [concurrent]
keywords:   [Java,concurrent]
---

AQS 的全称为（AbstractQueuedSynchronizer），这个类在java.util.concurrent.locks包下面。
AQS 是一个用来构建锁和同步器的框架，使用 AQS 能简单且高效地构造出应用广泛的大量的同步器，
比如我们提到的 `ReentrantLock`、`Semaphore`，其他的诸如 `ReentrantReadWriteLock`、
`SynchronousQueue`、`FutureTask` 等等皆是基于 AQS 的。当然，我们自己也能利用 AQS 
非常轻松容易地构造出符合我们自己需求的同步器。

## 为什么需要 AQS
性能是否可以成为“重复造轮子”的理由呢？Java1.5 中 synchronized 性能不如 AQS，但 1.6 之后，
synchronized 做了很多优化，将性能追了上来。显然性能不能重复造轮子的理由，因为性能问题优化一下就可以了，
完全没必要“重复造轮子”。

在前面在介绍死锁问题的时候，我们知道可以通过破坏死锁产生的条件从而避免死锁，但这个方案 
synchronized 没有办法解决。原因是 synchronized 申请资源的时候，如果申请不到，
线程直接进入阻塞状态，也释放不了线程已经占有的资源。我们需要新的方案解决这问题。

如果我们重新设计一把互斥锁去解决这个问题，那该怎么设计呢？AQS 提供了以下方案：
* **`能够响应中断`**：synchronized 一旦进入阻塞状态，就无法被中断。但如果阻塞状态的线程能够响应中断信号，能够被唤醒。这样就破坏了不可抢占条件了。
* **`支持超时`**：如果线程在一段时间之内没有获取到锁，不是进入阻塞状态，而是返回一个错误，那这个线程也有机会释放曾经持有的锁。这样也能破坏不可抢占条件。
* **`非阻塞地获取锁`**：如果尝试获取锁失败，并不进入阻塞状态，而是直接返回，那这个线程也有机会释放曾经持有的锁。这样也能破坏不可抢占条件。

这三种方案可以全面弥补 synchronized 的问题。这三个方案就是“重复造轮子”的主要原因，
体现在 API 上，就是 Lock 接口的三个方法。详情如下：
```java
// 支持中断的API
void lockInterruptibly() throws InterruptedException;
// 支持超时的API
boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
// 支持非阻塞获取锁的API
boolean tryLock();
```

## AQS 原理概览
AQS 核心思想是，如果被请求的共享资源空闲，则将当前请求资源的线程设置为有效的工作线程，
并且将共享资源设置为锁定状态。如果被请求的共享资源被占用，那么就需要一套线程阻塞等待以及被唤醒时锁分配的机制，
这个机制 AQS 是用 CLH 队列锁实现的，即将暂时获取不到锁的线程加入到队列中。

> CLH(Craig,Landin,and Hagersten)队列是一个虚拟的双向队列（虚拟的双向队列即不存在队列实例，
> 仅存在结点之间的关联关系）。AQS 是将每条请求共享资源的线程封装成一个 CLH 锁队列的一个结点（Node）
> 来实现锁的分配。

![AQS原理图]({{ site.baseurl }}/image/post/2021/09/27/06/AQS原理图.png)

AQS 使用一个 int 成员变量来表示同步状态，通过内置的 FIFO 队列来完成获取资源线程的排队工作。
AQS 使用 CAS 对该同步状态进行原子操作实现对其值的修改。

```java
public abstract class AbstractQueuedSynchronizer
    extends AbstractOwnableSynchronizer
    implements java.io.Serializable {
  // 共享变量，使用volatile修饰保证线程可见性
  private volatile int state;
  // 返回同步状态的当前值
  protected final int getState() {
    return state;
  }
  // 设置同步状态的值
  protected final void setState(int newState) {
    state = newState;
  }
  // 原子地（CAS操作）将同步状态值设置为给定值update如果当前同步状态的值等于expect（期望值）
  protected final boolean compareAndSetState(int expect, int update) {
    // See below for intrinsics setup to support this
    return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
  }
}
```

## AQS 对资源的共享方式
AQS 定义两种资源共享方式：
* Exclusive（独占）
* Share（共享）

### Exclusive（独占）
只有一个线程能执行，如 ReentrantLock。又可分为公平锁和非公平锁：
* 公平锁：按照线程在队列中的排队顺序，先到者先拿到锁
* 非公平锁：当线程要获取锁时，无视队列顺序直接去抢锁，谁抢到就是谁的

### Share（共享）
多个线程可同时执行，如 CountDownLatch、Semaphore、 CyclicBarrier、ReadWriteLock 我们都会在后面讲到。
ReentrantReadWriteLock 可以看成是组合式，因为 ReentrantReadWriteLock 也就是读写锁允许多个线程同时对某一资源进行读。

不同的自定义同步器争用共享资源的方式也不同。自定义同步器在实现时只需要实现共享资源 state 
的获取与释放方式即可，至于具体线程等待队列的维护（如获取资源失败入队/唤醒出队等），AQS 已经在顶层实现好了。

## AQS 组件总结

### Semaphore(信号量)
允许多个线程同时访问：`synchronized` 和 `ReentrantLock` 都是一次只允许一个线程访问某个资源，
`Semaphore(信号量)` 可以指定多个线程同时访问某个资源。

### CountDownLatch(倒计时器)
`CountDownLatch` 是一个同步工具类，用来协调多个线程之间的同步。这个工具通常用来控制线程等待，
它可以让某一个线程等待直到倒计时结束，再开始执行。

### CyclicBarrier(循环栅栏)
`CyclicBarrier` 和 `CountDownLatch` 非常类似，它也可以实现线程间的技术等待，但是它的功能比 
`CountDownLatch` 更加复杂和强大。主要应用场景和 `CountDownLatch` 类似。`CyclicBarrier` 
的字面意思是可循环使用（`Cyclic`）的屏障（`Barrier`）。它要做的事情是，让一组线程到达一个屏障
（也可以叫同步点）时被阻塞，直到最后一个线程到达屏障时，屏障才会开门，所有被屏障拦截的线程才会继续干活。
`CyclicBarrier` 默认的构造方法是 `CyclicBarrier(int parties)`，其参数表示屏障拦截的线程数量，
每个线程调用 `await()` 方法告诉 `CyclicBarrier` 我已经到达了屏障，然后当前线程被阻塞。

## 参考资料
* [Java并发 - AQS 原理分析]({% post_url java/concurrent/content/2021-09-27-06-java-concurrent-AQS %})
* [AQS](https://snailclimb.gitee.io/javaguide/#/docs/java/multi-thread/Java并发进阶常见面试题总结?id=_6-aqs)
* [锁原理 - AQS 源码分析：有了 synchronized 为什么还要重复造轮子](https://www.cnblogs.com/binarylei/p/12555166.html)
