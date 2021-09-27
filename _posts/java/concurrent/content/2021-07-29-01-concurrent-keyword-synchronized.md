---
layout:     post
title:      Java并发 - synchronized 关键字
date:       2021-07-29 00:50:32 +0800
postId:     2021-07-29-00-50-32
categories: [concurrent]
keywords:   [Java,concurrent]
---

在多线程并发编程中 synchronized 一直是元老级角色，很多人都会称呼它为重量级锁。但是，随着 
Java SE 1.6 对 synchronized 进行了各种优化之后，有些情况下它就并不那么重了。本文详细介绍
Java SE 1.6 中为了减少获得锁和释放锁带来的性能消耗而引入的偏向锁和轻量级锁，以及锁的存储结构
和升级过程。

## 锁对象
利用 synchronized 实现同步的基础:Java 中的每一个对象都可以作为锁。具体表现为以下 3 种形式：
* 普通方法：锁是当前实例对象
* 静态方法：锁是当前类的 Class 对象
* 方法块：  锁是 Synchonized 括号里配置的对象

## 实现原理
当一个线程试图访问同步代码块时，它首先必须得到锁，退出或抛出异常时必须释放锁。那么锁到底存在
哪里呢？锁里面会存储什么信息呢？

JVM基于进入和退出Monitor对象来实现方法同步和代码块同步，但是两者的实现细节不一样：
* 代码块同步: 通过使用monitorenter和monitorexit指令实现的.
* 同步方法: ACC_SYNCHRONIZED修饰

monitorenter指令是在编译后插入到同步代码块的开始位置，而monitorexit指令是在编译后插入到同步代码块的结束处或异常处

### 示例代码
为了证明JVM的实现方式，下面通过反编译代码来证明：
```java
public class SynchronizedDemo {
  
  public void f1() {
    synchronized (SynchronizedDemo.class) {
      System.out.println("Hello World.");
    }
  }

  public synchronized void f2() {
    System.out.println("Hello World.");
  }

}
```

### 查看字节码

先编译：`javac SynchronizedDemo.java`，然后通过 `javap -c SynchronizedDemo` 看看 add 方法的字节码指令：

```java
public class y2021.m07.d29.synchronize.SynchronizedDemo {
  public y2021.m07.d29.synchronize.SynchronizedDemo();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return

  public void f1();
    Code:
       0: ldc           #2                  // class y2021/m07/d29/synchronize/SynchronizedDemo
       2: dup
       3: astore_1
       4: monitorenter
       5: getstatic     #3                  // Field java/lang/System.out:Ljava/io/PrintStream;
       8: ldc           #4                  // String Hello World.
      10: invokevirtual #5                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
      13: aload_1
      14: monitorexit
      15: goto          23
      18: astore_2
      19: aload_1
      20: monitorexit
      21: aload_2
      22: athrow
      23: return
    Exception table:
       from    to  target type
           5    15    18   any
          18    21    18   any

  public synchronized void f2();
    Code:
       0: getstatic     #3                  // Field java/lang/System.out:Ljava/io/PrintStream;
       3: ldc           #4                  // String Hello World.
       5: invokevirtual #5                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
       8: return
}
```
先说f1()方法，发现其中一个monitorenter对应了两个monitorexit，这是不对的. 但是仔细看#15: goto语句，
直接跳转到了#23: return处，再看#22: athrow语句发现，原来第二个monitorexit是保证同步代码块抛出异常时
锁能得到正确的释放而存在的，这就理解了。

综上: 发现同步代码块是通过monitorenter和monitorexit来实现的；同步方法是加了一个
ACC_SYNCHRONIZED修饰来实现的。

### monitorenter
```
Each object is associated with a monitor. A monitor is locked if and only if it 
has an owner. The thread that executes monitorenter attempts to gain ownership 
of the monitor associated with objectref, as follows:
```
每个对象都与一个 monitor 相关联，当且仅当 monitor 属于某个线程时，monitor才会被锁定。
执行到 monitorenter 指令的线程，会尝试去获得对应的 monitor，如下：
* 每个对象维护着一个记录着被锁次数的计数器, 对象未被锁定时，该计数器为0。线程进入monitor（执行monitorenter指令）时，会把计数器设置为1.
* 当同一个线程再次获得该对象的锁的时候，计数器再次自增.
* 当其他线程想获得该monitor的时候，就会阻塞，直到计数器为0才能成功。

### monitorexit
```
The thread that executes monitorexit must be the owner of the monitor associated 
with the instance referenced by objectref.
```
monitor的拥有者线程才能执行 monitorexit指令。

线程执行monitorexit指令，就会让monitor的计数器减一。
如果计数器为0，表明该线程不再拥有monitor。其他线程就允许尝试去获得该monitor了。

### ACC_SYNCHRONIZED
方法级别的同步是隐式的，作为方法调用的一部分。同步方法的常量池中会有一个 ACC_SYNCHRONIZED 标志。
当调用一个设置了ACC_SYNCHRONIZED标志的方法，执行线程需要先获得monitor锁，然后开始执行方法，
方法执行之后再释放monitor锁，当方法不管是正常return还是抛出异常都会释放对应的monitor锁。
在这期间，如果其他线程来请求执行方法，会因为无法获得监视器锁而被阻断住。
如果在方法执行过程中，发生了异常，并且方法内部并没有处理该异常，那么在异常被抛到方法外面之前监视器锁会被自动释放。

其实 `ACC_SYNCHRONIZED` 的最终实现还是通过monitor来实现同步的，这与 `monitorenter` & `monitorexit`
并没有本质的不同。

### monitor监视器
既然synchronized锁依赖于 monitor监视器 来实现，那么 monitor监视器 到底是什么东西，又是如何实现的呢？
它可以理解为一种同步工具，或者说是同步机制，它通常被描述成一个对象。操作系统的管程是概念原理，
ObjectMonitor是它的原理实现。

![ObjectMonitor]({{ site.baseurl }}/image/post/2021/07/29/01/ObjectMonitor.awebp)

#### 操作系统的管程

并发编程的两大核心问题：一是互斥，即同一时刻只允许一个线程访问共享资源；二是同步，即线程之间的 "wait-notify" 机制。
管程能够解决这两大问题。Java SDK 并发包通过 Lock 和 Condition 两个接口来实现管程，其中 Lock 
用于解决互斥问题，Condition 用于解决同步问题。

* 管程 (英语：Monitors，也称为监视器) 是一种程序结构，结构内的多个子程序（对象或模块）形成的多个工作线程互斥访问共享资源。
* 这些共享资源一般是硬件设备或一群变量。管程实现了在一个时间点，最多只有一个线程在执行管程的某个子程序。
* 与那些通过修改数据结构实现互斥访问的并发程序设计相比，管程实现很大程度上简化了程序设计。
* 管程提供了一种机制，线程可以临时放弃互斥访问，等待某些条件得到满足后，重新获得执行权恢复它的互斥访问。

#### ObjectMonitor
在Java虚拟机（HotSpot）中，Monitor（管程）是由ObjectMonitor实现的，其主要数据结构如下：
```java
ObjectMonitor() {
  _header       = NULL;
  _count        = 0; // 记录个数
  _waiters      = 0,
  _recursions   = 0;
  _object       = NULL;
  _owner        = NULL;
  _WaitSet      = NULL;  // 处于wait状态的线程，会被加入到_WaitSet
  _WaitSetLock  = 0 ;
  _Responsible  = NULL ;
  _succ         = NULL ;
  _cxq          = NULL ;
  FreeNext      = NULL ;
  _EntryList    = NULL ;  // 处于等待锁block状态的线程，会被加入到该列表
  _SpinFreq     = 0 ;
  _SpinClock    = 0 ;
  OwnerIsThread = 0 ;
}
```
ObjectMonitor中几个关键字段的含义如图所示：
![ObjectMonitor-property]({{ site.baseurl }}/image/post/2021/07/29/01/ObjectMonitor-property.awebp)

#### 工作机理
Java Monitor 的工作机理如图所示：
![Monitor-工作原理]({{ site.baseurl }}/image/post/2021/07/29/01/Monitor-工作原理.awebp)

* 想要获取monitor的线程,首先会进入_EntryList队列。
* 当某个线程获取到对象的monitor后,进入_Owner区域，设置为当前线程,同时计数器_count加1。
* 如果线程调用了wait()方法，则会进入_WaitSet队列。它会释放monitor锁，即将_owner赋值为null,_count自减1,进入_WaitSet队列阻塞等待。
* 如果其他线程调用 notify() / notifyAll() ，会唤醒_WaitSet中的某个线程，该线程再次尝试获取monitor锁，成功即进入_Owner区域。
* 同步方法执行完毕了，线程退出临界区，会将monitor的owner设为null，并释放监视锁。

为了形象生动一点，举个例子：
```java
synchronized(this){  //进入_EntryList队列
  doSth();
  this.wait();  //进入_WaitSet队列
}

```

### 对象与monitor关联
对象是如何跟monitor关联的呢？直接先看图：
![对象与Monitor关联]({{ site.baseurl }}/image/post/2021/07/29/01/对象与Monitor关联.awebp)

看完上图，其实对象跟monitor怎样关联，我们已经有个大概认识了，接下来我们分对象内存布局，对象头，MarkWord继续往下探讨。

## Java对象头(存储锁类型)
在HotSpot虚拟机中, 对象在内存中的布局分为三块区域: `对象头`, `实例数据`和`对齐填充`.
对象头 中包含两部分: `MarkWord` 和 `类型指针`；如果是数组对象的话, 对象头还有一部分是存储数组的长度.
多线程下synchronized的加锁就是对同一个对象的对象头中的MarkWord中的变量进行CAS操作.

* 对象头
  * MarkWord
  * 类型指针
  * (数组)数组长度
* 实例数据
* 对齐填充

### MarkWord
Mark Word用于存储对象自身的运行时数据, 如HashCode, GC分代年龄, 锁状态标志, 线程持有的锁, 偏向线程ID等等.
占用内存大小与虚拟机位长一致(32位JVM -> MarkWord是32位, 64位JVM -> MarkWord是64位)。

![Markword]({{ site.baseurl }}/image/post/2021/07/29/01/Markword.awebp)

* 前面分析可知，monitor特点是互斥进行，你再喵一下上图，重量级锁，指向互斥量的指针。
* 其实synchronized是重量级锁，也就是说Synchronized的对象锁，Mark Word锁标识位为10，其中指针指向的是Monitor对象的起始地址。
* 顿时，是不是感觉柳暗花明又一村啦！对象与monitor怎么关联的？答案：Mark Word重量级锁，指针指向monitor地址。

对象与monitor怎么关联？

* 对象里有对象头
* 对象头里面有Mark Word
* Mark Word指针指向了monitor

### 类型指针
类型指针指向对象的类元数据, 虚拟机通过这个指针确定该对象是哪个类的实例.

### 对象头的长度

| 长度     | 内容                   | 说明                           |
| -------- | ---------------------- | ------------------------------ |
| 32/64bit | MarkWord               | 存储对象的hashCode或锁信息等   |
| 32/64bit | Class Metadada Address | 存储对象类型数据的指针         |
| 32/64bit | Array Length           | 数组的长度(如果当前对象是数组) |

如果是数组对象的话, 虚拟机用3个字宽(32/64bit + 32/64bit + 32/64bit)存储对象头; 
如果是普通对象的话, 虚拟机用2字宽存储对象头(32/64bit + 32/64bit).

## 锁优化和锁升级
下面分两个部分详细讲解synchronized锁分类以及锁的优化和升级过程。

## 锁分类

锁级别从低到高依次是：
1. 无锁状态
2. 偏向锁状态
3. 轻量级锁状态
4. 重量级锁状态

锁可以升级, 但不能降级. 即: 无锁 -> 偏向锁 -> 轻量级锁 -> 重量级锁是单向的

下面看一下每个锁状态时, 对象头中的MarkWord这一个字节中的内容是什么. 以32位为例：

### 无锁状态

| 25bit          | 4bit         | 1bit(是否是偏向锁) | 2bit(锁标志位) |
| -------------- | ------------ | ------------------ | -------------- |
| 对象的hashCode | 对象分代年龄 | 0                  | 01             |

### 偏向锁状态

| 23bit  | 2bit  | 4bit         | 1bit | 2bit |
| ------ | ----- | ------------ | ---- | ---- |
| 线程ID | epoch | 对象分代年龄 | 1    | 01   |

### 轻量级锁状态

| 30bit                | 2bit |
| -------------------- | ---- |
| 指向栈中锁记录的指针 | 00   |

### 重量级锁状态

| 30bit                      | 2bit |
| -------------------------- | ---- |
| 指向互斥量(重量级锁)的指针 | 10   |

## 锁升级(进化)

### 偏向锁
偏向锁是针对于一个线程而言的, 线程获得锁之后就不会再有解锁等操作了, 这样可以省略很多开销. 
假如有两个线程来竞争该锁话, 那么偏向锁就失效了, 进而升级成轻量级锁了.
为什么要这样做呢? 因为经验表明, 其实大部分情况下, 都会是同一个线程进入同一块同步代码块的. 
这也是为什么会有偏向锁出现的原因.
在Jdk1.6中, 偏向锁的开关是默认开启的, 适用于只有一个线程访问同步块的场景.

#### 偏向锁的加锁
当一个线程访问同步块并获取锁时, 会在锁对象的对象头和栈帧中的锁记录里存储锁偏向的线程ID, 
以后该线程进入和退出同步块时不需要进行CAS操作来加锁和解锁, 只需要简单的测试一下锁对象的对象头的
MarkWord里是否存储着指向当前线程的偏向锁(线程ID是当前线程), 如果测试成功, 表示线程已经获得了锁; 
如果测试失败, 则需要再测试一下MarkWord中偏向锁的标识是否设置成1(表示当前是偏向锁), 如果没有设置, 
则使用CAS竞争锁, 如果设置了, 则尝试使用CAS将锁对象的对象头的偏向锁指向当前线程.

#### 偏向锁的撤销
偏向锁使用了一种等到竞争出现才释放锁的机制, 所以当其他线程尝试竞争偏向锁时, 持有偏向锁的线程才会释放锁. 
偏向锁的撤销需要等到全局安全点(在这个时间点上没有正在执行的字节码). 首先会暂停持有偏向锁的线程, 
然后检查持有偏向锁的线程是否存活, 如果线程不处于活动状态, 则将锁对象的对象头设置为无锁状态; 
如果线程仍然活着, 则锁对象的对象头中的MarkWord和栈中的锁记录要么重新偏向于其它线程要么恢复到无锁状态, 
最后唤醒暂停的线程(释放偏向锁的线程).

#### 总结
偏向锁在Java6及更高版本中是默认启用的, 但是它在程序启动几秒钟后才激活. 可以使用
`-XX:BiasedLockingStartupDelay=0` 来关闭偏向锁的启动延迟, 也可以使用
`-XX:-UseBiasedLocking=false` 来关闭偏向锁, 那么程序会直接进入轻量级锁状态.

### 轻量级锁
当出现有两个线程来竞争锁的话, 那么偏向锁就失效了, 此时锁就会膨胀, 升级为轻量级锁.

#### 轻量级锁加锁
线程在执行同步块之前, JVM会先在当前线程的栈帧中创建用户存储锁记录的空间, 并将对象头中的
MarkWord复制到锁记录中. 然后线程尝试使用CAS将对象头中的MarkWord替换为指向锁记录的指针. 
如果成功, 当前线程获得锁; 如果失败, 表示其它线程竞争锁, 当前线程便尝试使用自旋来获取锁, 
之后再来的线程, 发现是轻量级锁, 就开始进行自旋.

#### 轻量级锁解锁
轻量级锁解锁时, 会使用原子的CAS操作将当前线程的锁记录替换回到对象头, 如果成功, 表示没有竞争发生; 
如果失败, 表示当前锁存在竞争, 锁就会膨胀成重量级锁.

#### 总结
总结一下加锁解锁过程, 有线程A和线程B来竞争对象c的锁(如: synchronized(c){} ), 
这时线程A和线程B同时将对象c的MarkWord复制到自己的锁记录中, 两者竞争去获取锁, 假设线程A成功获取锁, 
并将对象c的对象头中的线程ID(MarkWord中)修改为指向自己的锁记录的指针, 这时线程B仍旧通过CAS去获取对象c的锁, 
因为对象c的MarkWord中的内容已经被线程A改了, 所以获取失败. 此时为了提高获取锁的效率, 线程B会循环去获取锁, 
这个循环是有次数限制的, 如果在循环结束之前CAS操作成功, 那么线程B就获取到锁, 如果循环结束依然获取不到锁, 
则获取锁失败, 对象c的MarkWord中的记录会被修改为重量级锁, 然后线程B就会被挂起, 之后有线程C来获取锁时, 
看到对象c的MarkWord中的是重量级锁的指针, 说明竞争激烈, 直接挂起.

解锁时, 线程A尝试使用CAS将对象c的MarkWord改回自己栈中复制的那个MarkWord, 因为对象c中的MarkWord
已经被指向为重量级锁了, 所以CAS失败. 线程A会释放锁并唤起等待的线程, 进行新一轮的竞争.

### 锁的比较

| 锁       | 优点                                                         | 缺点                                            | 适用场景                           |
| -------- | ------------------------------------------------------------ | ----------------------------------------------- | ---------------------------------- |
| 偏向锁   | 加锁和解锁不需要额外的消耗, 和执行非同步代码方法的性能相差无几. | 如果线程间存在锁竞争, 会带来额外的锁撤销的消耗. | 适用于只有一个线程访问的同步场景   |
| 轻量级锁 | 竞争的线程不会阻塞, 提高了程序的响应速度                     | 如果始终得不到锁竞争的线程, 使用自旋会消耗CPU   | 追求响应时间, 同步快执行速度非常快 |
| 重量级锁 | 线程竞争不适用自旋, 不会消耗CPU                              | 线程堵塞, 响应时间缓慢                          | 追求吞吐量, 同步快执行时间速度较长 |


## 参考资料

* [Java并发 - synchronized 关键字]({% post_url java/concurrent/content/2021-07-29-01-concurrent-keyword-synchronized %})
* [8.8' - Java多线程编程实战指南-核心篇](https://book.douban.com/subject/27034721/)
* [Java并发 - Java内存模型]({% post_url java/concurrent/content/2021-08-23-04-concurrent-memory-model %})
* [Synchronized解析——如果你愿意一层一层剥开我的心](https://juejin.cn/post/6844903918653145102#heading-18)
* [Java6及以上版本对synchronized的优化](https://www.cnblogs.com/wuqinglong/p/9945618.html)
* [锁原理 - 信号量 vs 管程：JDK 为什么选择管程](https://www.cnblogs.com/binarylei/p/12544002.html)
