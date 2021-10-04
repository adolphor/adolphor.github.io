---
layout:     post
title:      Java并发 - 多线程顺序执行案例
date:       2021-10-04 21:18:56 +0800
postId:     2021-10-04-21-18-56
categories: [concurrent]
keywords:   [Java,concurrent]
---
多线程顺序执行，主要考察多线程之间的通信问题。

## 3个线程顺序打印0到100

其实这个题目的重点就是怎么实现多个线程交替顺序打印，想一想，这里我们需要用到的就是线程间的通信，
然后怎么通信呢？我们可以通过 **synchronized + wait + notifyAll**，
或者 **ReentrantLock + Condition + await + signalAll**；

知道了线程间怎么通信，然后就是在每个线程内部怎么实现了，是不是多个线程共用一把锁，
然后自然数0-100是公共资源，让3个线程去消费；

我们可以每个线程加上一个标号：0，1，2，来表示具体是哪个线程； 通过一个计数器对3进行求余，
余数和具体的线程标号去比较，只有当余数和线程标号相等的时候才进行打印(不等加入等待队列)，
打印完计数器进行自增； 然后唤醒等待队列里面线程去获取锁，获取锁成功则继续执行以上步骤，
否则加入锁的同步队列中，等待上一个线程唤醒；

### synchronized 实现

```java
public class PrintThreadBySynchronized implements Runnable {

  private static final Object LOCK = new Object();

  // 计数，同时确定线程是否要加入等待队列，还是可以直接去资源队列里面去获取数据进行打印
  private static int count = 0;
  private LinkedList<Integer> queue;
  private Integer threadNo;

  public PrintThreadBySynchronized(LinkedList<Integer> queue, Integer threadNo) {
    this.queue = queue;
    this.threadNo = threadNo;
  }

  @Override
  public void run() {
    while (true) {
      synchronized (LOCK) {
        while (count % 3 != this.threadNo) {
          if (count >= 101) {
            break;
          }
          try {
            LOCK.wait();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        if (count >= 101) {
          break;
        }
        Integer val = this.queue.poll();
        System.out.println("thread-" + this.threadNo + ":" + val);
        count++;

        LOCK.notifyAll();
      }
    }
  }
}
```

### ReentrantLock 实现
```java
public class PrintThreadByReentrantLock implements Runnable{
  private static final ReentrantLock lock = new ReentrantLock();
  private static final Condition c = lock.newCondition();

  //作为计数，同时也作为资源；因为这道题目是自然数作为资源，所以正好可以公用；
  private static int count = 0;
  private Integer threadNo;

  public PrintThreadByReentrantLock(Integer threadNo) {
    this.threadNo = threadNo;
  }

  @Override
  public void run() {
    while (true) {
      try {
        lock.lock();
        while (count % 3 != this.threadNo) {
          if (count >= 101) {
            break;
          }
          try {
            c.await();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        if (count >= 101) {
          break;
        }
        System.out.println("thread-" + this.threadNo + ":" + count);
        count++;
        c.signalAll();
      } finally {
        lock.unlock();
      }
    }
  }
}
```

### Main 测试
```java
public class OrderPrintNum {
  private static final LinkedList<Integer> QUEUE = new LinkedList();
  private static ExecutorService POOL_SERVICE = Executors.newFixedThreadPool(3);
  public static void main(String[] args) {
    initData();
    runPrint1();

    initData();
    runPrint2();
  }

  private static void initData() {
    for (int i = 0; i <= 100; i++) {
      QUEUE.add(i);
    }
    if (POOL_SERVICE.isTerminated()) {
      POOL_SERVICE = Executors.newFixedThreadPool(3);
    }
  }

  private static void runPrint1() {
    POOL_SERVICE.execute(new PrintThreadBySynchronized(QUEUE, 2));
    POOL_SERVICE.execute(new PrintThreadBySynchronized(QUEUE, 1));
    POOL_SERVICE.execute(new PrintThreadBySynchronized(QUEUE, 0));

    POOL_SERVICE.shutdown();

    while (true) {
      if (POOL_SERVICE.isTerminated()) {
        System.out.println("finished!!");
        break;
      }
    }
  }

  private static void runPrint2() {
    POOL_SERVICE.execute(new PrintThreadByReentrantLock(0));
    POOL_SERVICE.execute(new PrintThreadByReentrantLock(1));
    POOL_SERVICE.execute(new PrintThreadByReentrantLock(2));

    POOL_SERVICE.shutdown();

    while (true) {
      if (POOL_SERVICE.isTerminated()) {
        System.out.println("finished!!");
        break;
      }
    }
  }
}
```


开始写作吧
```
![image-alter]({{ site.baseurl }}/image/post/2021/10/04/03/xxx.jpg)
```

## 参考资料
* [Java并发 - 多线程顺序执行案例]({% post_url java/concurrent/content/2021-10-04-03-multi-thread-sequential-execution %})
* [多线程(使用线程池)按顺序打印 0-100;(至少3个线程交替按顺序打印)](https://zhuanlan.zhihu.com/p/71098723)
