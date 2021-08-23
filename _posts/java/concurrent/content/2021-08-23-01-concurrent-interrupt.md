---
layout:     post
title:      Java并发 - 线程中断
date:       2021-08-23 00:20:40 +0800
postId:     2021-08-23-00-20-40
categories: [concurrent]
keywords:   [Java,concurrent]
---

线程间协作还有一种常见的形式是，一个线程请求另外一个线程停止其正在执行的操作。比如，对于有些
比较耗时的任务，我们往往会采用专门的工作者线程来负责其执行，如果中途要取消(比如用户不想等了)
这类任务的执行，那么我们就需要借助Java 线程中断机制。

## 发起interrupt请求
Java线程中断机制相当于Java线程与线程间协作的一套协议框架(合同范本)。中断(Interrupt)可被
看作由一个线程(发起线程Originator)发送给另外一个线程(目标线程Target)的一种指示(Indication)，
该指示用于表示发起线程希望目标线程停止其正在执行的操作。中断仅仅代表发起线程的一一个诉求，
而这个诉求能否被满足则取决于目标线程自身一目标线程可能会满足发起线程的诉求，也可能根本不理会
发起线程的诉求!Java平台会为每个线程维护一个被称为中断标记(InterruptStatus)的布尔型状态变量
用于表示相应线程是否接收到了中断，中断标记值为true表示相应线程收到了中断。目标线程可以通过
`Thread.currentThread().isInterrupted()`调用来获取该线程的中断标记值，也可以通过
`Thread.interrupted()`来获取并重置(也称清空)中断标记值，即`Thread.interrupted()`会返回
当前线程的中断标记值并将当前线程中断标记重置为`false`。 调用一个线程的`interrupt()`相当于将
该线程(目标线程)的中断标记置为true。

## 响应interrupt请求
目标线程检查中断标记后所执行的操作，被称为目标线程对中断的响应，简称中断响应。设有个发起线程
originator和目标线程target，那么target对中断的响应一般包括:

* 无影响  
  originator 调用target.interrupt()不 会对target的运行产生任何影响。这种情形也可以称为
  目标线程无法对中断进行响应。InputStream.read()、ReentrantLock.lock()以及申请内部锁等
  阻塞方法/操作就属于这种类型。
* 取消任务的运行  
  originator 调用target.interrupt()会 使target 在侦测到中断(即中断标记值为true) 那一刻
  所执行的任务被取消(中止)，而这并不会运行target继续处理其他任务。
* 工作者线程停止  
  originator 调用target.interrupt()会使 target 终止，即target 的生命周期状态变更为TERMINATED。

## 中断异常处理及响应
能够响应中断的方法通常是在执行阻塞操作前判断中断标志，若中断标志值为true则抛出InterruptedException。
例如, `ReentrantLock.lockInterruptibly()`的功能与`ReentrantLock.lock()`类似，二者都能用于
申请相应的显式锁，但是ReentrantLock.lockInterruptibly()能够对中断做出响应。
ReentrantLock.lockInterruptibly()方法对中断的响应是通过其调用的一个名为`acquireInterruptibly`
的方法实现的。acquireInterruptibly方法会在执行申请锁这个阻塞操作前检查当前线程的中断标记，
若中断标记值为true 则抛出`InterruptedException`异常，如清单5-15 所示。依照惯例，凡是抛出
InterruptedException 异常的方法，通常会在其抛出该异常之前将当前线程的线程中断标记重置为false。
因此，acquireInterruptibly方法在判断中断标记时调用的是`Thread.interrupted()`而非
`Thread.currentThread).isInterrupted()`。

```
清単5-15 ReentrantLock.locklnterruptibly()对中断的响应
```
```java
public final void acqui reInterruptibly(int arg) throws Inter ruptedException 1
if (Thread.interrupted())
  throw new InterruptedException();
if(!tryAcquire(arg))
  doAcquireInterruptibly(arg);
```

因此,Java应用层代码通常可以通过对InterruptedException等异常进行处理的方式来
实现中断响应。对InterruptedException异常的正确处理方式包括以下几种。
* 不捕获InterruptedException  
  如果应用代码的某个方法调用了能够对中断进行响应的阻塞方法，那么我们也可以选择在这个方法的异常声明( throws )中也加一个InterruptedException。这种做法实质上是当前方法不知道如何处理中断比较恰当，因此将“难题”抛给其上层代码(比如这个方法的调用方)
* 捕获InterruptedException后重新将该异常抛出。  
  使用这种策略通常是由于应用代码需要捕获InterruptedException并对此做一-些中间处理(比如处理部分完成的任务)，接着再将“难题”抛给其上层代码。
* 捕获InterruptedException 并在捕获该异常后中断当前线程。  
  这种策略实际上在捕获到InterruptedException后又恢复中断标志，这相当于当前代码告诉其他代码:“我发现了中断，但我并不知道如何处理比较妥当，因此我为你保留了中断标记，你看着办吧!”本书源码所用的工具类Tools的randomPause方法就采用了这种处理策略，如清单5-16所示。

```
清单5-16捕 获InterruptedException后恢复中断标志
```
```java
public final class Tools {
  public static void randomPause (int maxPauseTime) {
  int sleepTime = rnd.nextInt(maxPauseTime);
  try {
    Thread. sleep (sleepTime) ;
  } catch (InterruptedException e) {
  Thread. currentThread() . interrupt() ;//保留线程中断标记
}
//
```

## 参考资料
* [Java并发 - 线程中断]({% post_url java/concurrent/content/2021-08-23-01-concurrent-interrupt %})
* [8.8' - Java多线程编程实战指南-核心篇](https://book.douban.com/subject/27034721/)
