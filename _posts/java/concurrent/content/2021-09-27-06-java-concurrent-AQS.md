---
layout:     post
title:      Java并发 - AQS 原理分析
date:       2021-09-27 15:04:01 +0800
postId:     2021-09-27-15-04-01
categories: [concurrent]
keywords:   [Java,concurrent]
---

AQS 的全称为（**AbstractQueuedSynchronizer**），是一个用来构建锁和同步器的框架，
内部实现的关键是维护了一个先进先出的队列以及state状态变量。先进先出队列的存储载体叫做
Node 节点，该节点标记着当前的状态值、独占/共享 模式、以及它的前驱和后驱节点 等信息。

这个类在java.util.concurrent.locks包下面，使用 AQS 能简单且高效地构造出应用广泛的大量的同步器，
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

## AQS基础之 CLH锁
CLH锁也是一种基于链表的可扩展、高性能、公平（提供先来先服务）的自旋锁，申请线程只在本地变量上自旋，
它不断轮询前驱的状态，如果发现前驱释放了锁就结束自旋。由于是 Craig、Landin 和 Hagersten
三位大佬的发明，因此命名为CLH锁。

### 数据结构
* **locked**：volatile 修饰的boolean变量保证此变量对不同的线程可见，表示加锁状态
  - true：代表持有锁成功或正在等待加锁
  - false：表示锁被释放
* **tailNode**：尾节点
* **currentThreadNode**：当前节点

```java
public class CLHLock {

  private AtomicReference<CLHLock> tailNode = new AtomicReference<>();
  private ThreadLocal<CLHLock> currentThreadNode = new ThreadLocal<>();

  static final class CLHNode {
    private volatile boolean locked = true;

    public boolean isLocked() {
      return locked;
    }

    public void setLocked(boolean locked) {
      this.locked = locked;
    }
  }
}
```

### 核心思想
获取尾节点，如果尾节点是null，则表示当前线程是第一个过来抢锁的，可以直接加锁成功；
如果不为空，则将当前节点设置为尾节点，并对当前节点的前驱结点的locked进行自旋，
如果发现其前驱结点的locked字段变为了false，则给当前节点加锁成功。

#### 加锁过程
获取尾节点，如果尾节点是null，则表示当前线程是第一个过来抢锁的，可以直接加锁成功；
如果不为空，则将当前节点设置为尾节点，并对当前节点的前驱结点的locked进行自旋，
如果发现其前驱结点的locked字段变为了false，则给当前节点加锁成功。

```java
public class CLHLock {
  public void lock() {
    // 首先对当前节点进行初始化
    CLHNode currentNode = currentThreadNode.get();
    if (currentNode == null) {
      currentNode = new CLHNode();
      // 设置状态，标识当前节点正在加锁
      currentNode.setLocked(true);
      currentThreadNode.set(currentNode);
    }

    // 先判断当前尾节点，是否有其他线程节点
    CLHNode preNode = tailNode.getAndSet(currentNode);
    // 如果没有尾节点，则当前节点之前不存在其他线程竞争锁，直接加锁成功
    if (preNode==null){
      return;
    }
    // 如果有尾节点，则说明前驱节点不为空，需要自选等待前驱节点的线程释放锁以后，再进行加锁
    while (preNode.isLocked()){
    }
  }
}
```

#### 释放锁过程
释放锁的过程主要是将当前节点locked标志位置为false的过程。也分情况，
如果当前释放锁的线程节点是尾节点，则说明没有其他线程在等待队列中，
直接将尾节点设置为null即可，否则需要将当前节点的locked标志位设置为false，
来通知等待队列中线程锁已被释放。

```java
public class CLHLock {
  public void unlock() {
    // 获取当前线程节点
    CLHNode currentNode = currentThreadNode.get();
    if (currentNode == null || currentNode.isLocked() == false) {
      // 当前线程没有锁，所以不用释放直接返回，也可以抛出异常
      return;
    }
    // CAS 尝试将尾节点设置为null
    if (tailNode.compareAndSet(currentNode, null)) {
      // 成功，说明当前线程节点是尾节点，阻塞队列中没有其他线程在竞争锁，将尾节点设置为null，即可释放锁
    } else {
      // 失败，说明当前线程节点不是尾节点，有其他线程正在自旋当前线程的locked变量
      currentNode.setLocked(false);
    }
  }
}
```

## AQS 原理概览

### 双向队列节点
对应实现为 **`java.util.concurrent.locks.AbstractQueuedSynchronizer.Node`**

* **共享 OR 独占**
  - **Exclusive（独占）**：只有一个线程能执行，如 ReentrantLock。又可分为公平锁和非公平锁：
    * **公平锁**：按照线程在队列中的排队顺序，先到者先拿到锁
    * **非公平锁**：当线程要获取锁时，无视队列顺序直接去抢锁，谁抢到就是谁的
  - **Share（共享）**：多个线程可同时执行，如 CountDownLatch、Semaphore、 CyclicBarrier、ReadWriteLock
* **等待状态**
  - Canceled = 1：代表此节点因超时或者中断取消了争夺锁
  - Signal = -1：代表本节点释放锁或者放弃争夺锁时，需要唤醒当前线程节点的后继节点
  - Condition = -2：代表当前节点正等待一个condition
  - propagate = -3：释放共享锁需要传播到其他节点
* **双向队列组成**
  - Node prev：当前节点的前驱结点
  - Node next：当前节点的后继节点
* **其他**
  - volatile Thread thread：当前节点的线程
  - Node nextWaiter：下一个等待condition的node

![AQS原理图]({{ site.baseurl }}/image/post/2021/09/27/06/AQS原理图.png)

AQS 使用一个 int 成员变量来表示同步状态，通过内置的 FIFO 队列来完成获取资源线程的排队工作。
AQS 使用 CAS 对该同步状态进行原子操作实现对其值的修改。

### 核心属性

* **state**：表示当前锁的状态，在不同的功能实现中代表不同的含义。
* **head**：等待队列的头节点，是懒加载的
* **tail**：等待队列的尾节点，也是延迟初始化的
* **exclusiveOwnerThread**：代表当前持有独占锁的线程。

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

### 扩展方法

AbstractQueuedSynchronizer采用模板方法，将排队、阻塞等操作统一包装起来，
仅暴漏核心方法根据需要实现功能覆写对应的方法即可，所有其他方法都声明为final，
因为它们不能被独立更改。这些核心方法的实现需要是线程安全的。

#### 排它锁对应相关方法
```java
protected boolean tryAcquire(int arg) {
  throw new UnsupportedOperationException();
}
protected boolean tryRelease(int arg) {
  throw new UnsupportedOperationException();
}
```

#### 共享锁对应相关方法
```java
protected int tryAcquireShared(int arg) {
  throw new UnsupportedOperationException();
}
protected boolean tryReleaseShared(int arg) {
  throw new UnsupportedOperationException();
}
```

#### 其他方法
该线程是否正在独占资源。只有用到condition才需要去实现它
```java
protected boolean isHeldExclusively() {
  throw new UnsupportedOperationException();
}
```

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


## 补遗

### 公平锁 & 非公平锁
使用 AQS 既可以实现公平锁，也可以实现非公平锁，那具体什么是公平锁和非公平锁，以及如何实现呢？

公平锁和非公平锁的区别就是，在执行同步代码块的时候，是否会尝试获取锁。
如果会先尝试获取锁，那就是非公平锁，如果不尝试获取，而是直接进入队列，
那就是公平锁。

#### 公平锁
在竞争环境下，先到临界区的线程一定比后到的线程更快的获得锁。
实现起来非常简单，把竞争的线程放在一个先进先出的队列上就可以了，
持有锁的线程执行完毕之后，唤醒队列的下一个线程去获取锁就好了。

#### 非公平锁
先到临界区的线程，未必比后到的线程更快的获得锁。
实现起来也非常简单，线程先尝试能不能获取到锁，如果获取到锁直接运行同步代码；
如果获取不到锁，把这个线程放入到队列中等待唤醒后再去获取锁。

#### 为什么需要队列
为什么不一致尝试获取锁，而是加入队列？
一直尝试获取锁，叫做**自旋**，需要消耗CPU资源。如果多个线程同时进行自旋，
那么大部分线程都会获取失败，就会极大浪费服务器资源。

## 参考资料
* [Java并发 - AQS 原理分析]({% post_url java/concurrent/content/2021-09-27-06-java-concurrent-AQS %})
* [AQS](https://snailclimb.gitee.io/javaguide/#/docs/java/multi-thread/Java并发进阶常见面试题总结?id=_6-aqs)
* [锁原理 - AQS 源码分析：有了 synchronized 为什么还要重复造轮子](https://www.cnblogs.com/binarylei/p/12555166.html)
* [我有一只喵喵 - 终于搞懂了JUC中的AQS](https://juejin.cn/post/6913925439723405319)
