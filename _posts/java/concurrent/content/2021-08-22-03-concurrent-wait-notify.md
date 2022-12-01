---
layout:     post
title:      Java并发 - wait和notify
date:       2021-08-22 21:39:57 +0800
postId:     2021-08-22-21-39-57
categories: [Concurrent]
keywords:   [Java,concurrent]
---

在多线程编程中，一个线程因其执行目标动作所需的保护条件未满足而被暂停的过程就被称为等待(Wait)。
一个线程更新了系统的状态，使得其他线程所需的保护条件得以满足的时候唤醒那些被暂停的线程的过程就被称为通知(Notify)。
在Java平台中，Object.wait()/Object.wait(long)以 及Object.notify()/Object.notifyAll()
可用于实现等待和通知：Object.wait()的作用是使其执行线程被暂停(其生命周期状态变更为WAITING)，
该方法可用来实现等待；Object.notify()的作用是唤醒一个被暂停的线程，调用该方法可实现通知。
相应地，Object.wait()的执行线程就被称为等待线程；Object.notify()的执行线程就被称为通知线程。
由于Object类是Java中任何对象的父类，因此使用Java中的任何对象都能够实现等待与通知。

在单线程编程中，程序要执行的操作(目标动作)如果需要满足一定的条件(保护条件)才能执行，那么我们
可以将该操作放在一个if语句体中，这使得目标动作只有在保护条件得以满足的时候才会被执行。在多线程
编程中处理这种情形我们有另外一个选择一保护条件未满足可能只是暂时的，稍后其他线程可能更新了保护条件
涉及的共享变量而使得其成立，因此我们可以将当前线程暂停，直到其所需的保护条件得以成立时再将其唤醒，
如下伪代码所示:

```
// 原子操作
atomic{
    while (保护条件不成立) {
        暂停当前线程;
    }
    //执行目标动作
    doAction ();
}
```

## wait
使用Object.wait()实现等待，其代码模板如下伪代码所示:
```
//在调用wait方法前获得相应对象的内部锁
synchronized (some0bject) {
    while (保护条件不成立) {
        // 调用Object. wait ()暂停当前线程
        someObject.wait () ;
    }
    //代码执行到这里说明保护条件已经满足
    //执行目标动作
    doAction() ;
}
```

### 受保护方法
其中，保护条件是一个包含共享变量的布尔表达式。当这些共享变量被其他线程(通知线程)更新之后使相应的
保护条件得以成立时，这些线程会通知等待线程。由于一个线程只有在持有一个对象的内部锁的情况下才能够
调用该对象的wait方法,因此Object.wait()调用总是放在相应对象所引导的临界区之中。包含，上述
模板代码的方法被称为受`保护方法`(Guarded Method)。
受保护方法包括3个要素：保护条件、暂停当前线程和目标动作。

### 等待线程
设 someObject 为Java中任意一个类的实例，因执行 `someObject.wait()` 而被暂停的线程就称为
对象 someObject 上的 `等待线程`。由于同一个对象的同一个方法(someObject.wait())可以被
多个线程执行，因此一个对象可能存在多个等待线程。someObject上的等待线程可以通过其他线程
执行 `someObject.notify()` 来唤醒。someObject.wait()会以原子操作的方式使其执行线程
(当前线程)暂停并使该线程释放其持有的someObject对应的内部锁。当前线程被暂停的时候其对
someObject.wait()的调用并未返回。其他线程在该线程所需的保护条件成立的时候执行相应的 
notify 方法，即someObject.notify() 可以唤醒 someObject 上的一个(任意的)等待线程。
被唤醒的等待线程在其占用处理器继续运行的时候，需要再次申请someObject对应的内部锁。被唤醒的
线程在其再次持有someObject 对应的内部锁的情况下继续执行someObject.wait()中剩余的指令，
直到wait方法返回。

等待线程只在保护条件不成立的情况下才执行Object.wait()进行 等待，即在执行 Object.wait() 
前我们需要判断保护条件是否成立(当然，此时保护条件也是有可能成立的)。另外，等待线程在其被唤醒、
继续运行到其再次持有相应对象的内部锁的这段时间内，由于其他线程可能抢先获得相应的内部锁并更新了
相关共享变量而导致该线程所需的保护条件又再次不成立，因此Object.wait()调用返回之后我们需要
再次判断此时保护条件是否成立。所以，对保护条件的判断以及Object.wait()调用应该放在循环语句之中，
以确保目标动作只有在保护条件成立的情况下才能够执行!

另外，等待线程对保护条件的判断以及目标动作的执行必须是个原子操作，否则可能产生竞态一一目标动作
被执行前的那--刻其他线程对共享变量的更新又使得保护条件重新不成立。因此，目标动作的执行必须和
保护条件的判断以及Object.wait()调用放在同一个对象所引导的临界区中。

### wait注意事项
* 等待线程对保护条件的判断、Object.wait()的调用总是应该放在相应对象所引导的临界区中的一个循环语句之中。
* 等待线程对保护条件的判断、Object.wait()的执行以及目标动作的执行必须放在同一个对象(内部锁)所引导的临界区之中。
* Object.wait()暂 停当前线程时释放的锁只是与该wait方法所属对象的内部锁。当前线程所持有的其他内部锁、显式锁并不会因此而被释放。

## notify
使用Object.notify()实 现通知，其代码模板如下伪代码所示:
```
synchroni zed (someObject) {
    //更新等待线程的保护条件涉及的共享变量
    upda teSharedState () ;
    //唤醒其他线程
    someObject.notify() ;
}
```

### 通知方法
包含上述模板代码的方法被称为 `通知方法`，它包含两个要素：更新共享变量、唤醒其他线程。
由于一个线程只有在持有一个对象的内部锁的情况下才能够执行该对象的notify 方法，因此
Object.notify()调用 总是放在相应对象内部锁所引导的临界区之中。也正是由于
Object.notify()要求其执行线程必须持有该方法所属对象的内部锁，因此Object.wait()
在暂停其执行线程的同时必须释放相应的内部锁;否则通知线程无法获得相应的内部锁，
也就无法执行相应对象的notify方法来通知等待线程!Object.notify()的执行线程持有的相
应对象的内部锁只有在Object.notify()调用所在的临界区代码执行结束后才会被释放，而
Object.notify()本身并不会将这个内部锁释放。因此，为了使等待线程在其被唤醒之后能够
尽快再次获得相应的内部锁，我们要尽可能地将0bject.notify()调用放在靠近临界区结束的地方。、
等待线程被唤醒之后占用处理器继续运行时，如果有其他线程持有了相应对象的内部锁，
那么这个等待线程可能又会再次被暂停,以等待再次获得相应内部锁的机会,而这会导致上下文切换。

### notify注意事项
等待线程和通知线程必须调用同一个对象的wait方法、notify方法来实现等待和通知。调用一个对象的
notify方法所唤醒的线程仅是该对象上的一个任意等待线程。notify方法调用应该尽可能地放在靠近临界区结束的地方。

## 范例

```
AlarmAgent.java
```
```java
  // 调用 sendAlarm 方法的时候，首先检测连接是否正常，如果不正常则调用wait()方法，进入WAITING状态
  // 于此同时，有个heartbeat对象while轮询当前连接状态，等待连接恢复正常
  public void sendAlarm(String message) throws InterruptedException {
    synchronized (this) {
      // 使当前线程等待直到告警代理与告警服务器的连接建立完毕或者恢复
      while (!connectedToServer) {
        Debug.info("Alarm agent was not connected to server.");
        wait();
      }
      // 真正将告警消息上报到告警服务器
      doSendAlarm(message);
    }
  }
  // 建立连接 
  private void connectToServer() {
    // 创建并启动网络连接线程，在该线程中与告警服务器建立连接
    new Thread(() -> doConnect()).start();
  }
  private void doConnect() {
    // 模拟实际操作耗时
    Tools.randomPause(100);
    synchronized (this) {
      connectedToServer = true;
      // 连接已经建立完毕，通知以唤醒告警发送线程
      notify();
    }
  }
  // 轮训连接状态，不正常则进行连接
  class HeartbeatThread extends Thread {
    @Override
    public void run() {
      try {
        // 留一定的时间给网络连接线程与告警服务器建立连接
        Thread.sleep(1000);
        while (true) {
          if (checkConnection()) {
            connectedToServer = true;
          } else {
            connectedToServer = false;
            Debug.info("Alarm agent was disconnected from server.");
            // 检测到连接中断，重新建立连接
            connectToServer();
          }
          Thread.sleep(2000);
        }
      } catch (InterruptedException e) {
        // 什么也不做;
      }
    }
  }
```


## 参考资料
* [Java并发 - wait和notify]({% post_url java/concurrent/content/2021-08-22-03-concurrent-wait-notify %})
* [8.8' - Java多线程编程实战指南-核心篇](https://book.douban.com/subject/27034721/)

