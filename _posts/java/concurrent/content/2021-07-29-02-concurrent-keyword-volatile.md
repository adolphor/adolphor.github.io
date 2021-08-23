---
layout:     post
title:      Java并发 - volatile 实现原理
date:       2021-07-29 00:51:35 +0800
postId:     2021-07-29-00-51-35
categories: [concurrent]
keywords:   [Java,concurrent]
---

在多线程并发编程中 synchronized 和 volatile 都扮演着重要的角色，volatile 是轻量级的 
synchronized，它在多处理器开发中保证了共享变量的"可见性"。可见性的意思是当一个线程修改一个
共享变量时，另外一个线程能读到这个修改的值。如果 volatile 变量修饰符使用恰当的话，它比 
synchronized 的使用和执行成本更低，因为它不会引起线程上下文的切换和调度。但是，volatile 
不能保证原子性。

## 定义
Java 语言规范中对 volatile 的定义如下：Java 编程语言允许线程访问共享变量，为了确保共享变量
能被准确和一致地更新，线程应该确保通过排他锁单独获得这个变量。

## volatile 关键字的特性

### 保证可见性
从实现原理中可以看出，当任何线程修改了某个变量的值，都会写入系统内存且让其他缓存失效，那么其他
线程每次获取的变量的值都会是主内存中最新的值，这样就保证了这个变量对其他线程的可见性。
可见性的保证是基于CPU的内存屏障指令，被JSR-133抽象为happens-before原则。

### 禁止指令重排
阻止编译时和运行时的指令重排。编译时JVM编译器遵循内存屏障的约束，运行时依靠CPU屏障指令来阻止重排。

## 实现原理

Java虚拟机规范中定义了一种Java内存 模型（Java Memory Model，即JMM）来屏蔽掉各种硬件和
操作系统的内存访问差异，以实现让Java程序在各种平台下都能达到一致的并发效果。Java内存模型的
主要目标就是定义程序中各个变量的访问规则，即在虚拟机中将变量存储到内存和从内存中取出变量这样的细节。

JMM中规定所有的变量都存储在主内存（Main Memory）中，每条线程都有自己的工作内存（Work Memory），
线程的工作内存中保存了该线程所使用的变量的从主内存中拷贝的副本。线程对于变量的读、写都必须在
工作内存中进行，而不能直接读、写主内存中的变量。同时，本线程的工作内存的变量也无法被其他线程
直接访问，必须通过主内存完成。

整体内存模型如下图所示：

![Java内存模型]({{ site.baseurl }}/image/post/2021/08/06/01/jmm.jpg)

volatile定义：
* 当对volatile变量执行写操作后，JMM会把工作内存中的最新变量值强制刷新到主内存
* 写操作会导致其他线程中的缓存无效
  
这样，其他线程使用缓存时，发现本地工作内存中此变量无效，便从主内存中获取，这样获取到的变量
便是最新的值，实现了线程的可见性。

## 为什么能禁止指令重排

### 什么是指令重排
像Java等高级语言最后都要被编译器转换为机器语言，或者称作机器指令，以便能被机器直接执行。所以，
我们编写的程序最后都是一条条机器指令，从而能被CPU执行。在计算机组成原理这门课中，我们得知，
这些指令被放在指令寄存器中，由程序计数器PC去指示CPU下一条该执行那条指令。CPU先从程序计数器中
拿到下一条指令在指令寄存器的地址，计数器加一，然后把指令从指令寄存器中读出来，最后执行，然后
又循环这个过程。又由于CPU的速度远大于寄存器的速度，所以为了增加CPU的并发度，在多核CPU中可能
会有多核去拿指令，然后执行，所以在这个过程中，原本的代码顺序可能会被重新排序，这就是指令重排。

### 内存屏障
volatile是通过编译器在生成字节码时，在指令序列中添加“内存屏障”来禁止指令重排序的。

内存屏障也称为内存栅栏或栅栏指令，是一组处理器指令，它使CPU或编译器对屏障指令之前和之后发出的
内存操作顺序做限制。这通常意味着在屏障之前发布的操作被保证在屏障之后发布的操作之前执行。
为了实现volatile可见性和happen-befor的语义。JVM底层就是通过“内存屏障”的东西来完成。

### 硬件层面的"内存屏障"
* sfence：即写屏障(Store Barrier)，在写指令之后插入写屏障，能让写入缓存的最新数据写回到主内存，以保证写入的数据立刻对其他线程可见
* lfence：即读屏障(Load Barrier)，在读指令前插入读屏障，可以让高速缓存中的数据失效，重新从主内存加载数据，以保证读取的是最新的数据。
* mfence：即全能屏障(modify/mix Barrier )，兼具sfence和lfence的功能
* lock 前缀：lock不是内存屏障，而是一种锁。执行时会锁住内存子系统来确保执行顺序，甚至跨多个CPU。

### JMM层面的"内存屏障"
* LoadLoad屏障： 对于这样的语句Load1; LoadLoad; Load2，在Load2及后续读取操作要读取的数据被访问前，保证Load1要读取的数据被读取完毕。
* StoreStore屏障：对于这样的语句Store1; StoreStore; Store2，在Store2及后续写入操作执行前，保证Store1的写入操作对其它处理器可见。
* LoadStore屏障：对于这样的语句Load1; LoadStore; Store2，在Store2及后续写入操作被刷出前，保证Load1要读取的数据被读取完毕。
* StoreLoad屏障： 对于这样的语句Store1; StoreLoad; Load2，在Load2及后续所有读取操作执行前，保证Store1的写入对所有处理器可见。

在一个变量被volatile修饰后，JVM会为我们做两件事：
1.在每个volatile写操作前插入StoreStore屏障，在写操作后插入StoreLoad屏障。
2.在每个volatile读操作前插入LoadLoad屏障，在读操作后插入LoadStore屏障。

## 使用场景和范例

### 单次操作的原子性
volatile只能禁止指令重排，并不能保证多步骤操作的原子性。但反过来，单步骤操作的原子性可以保证，
由于long和double两种数据类型的操作可分为高32位和低32位两部分，因此普通的long或double类型
读/写可能不是原子的。因此，鼓励大家将共享的long和double变量设置为volatile类型，这样能保证
任何情况下对long和double的单次读/写操作都具有原子性。

### 单例模式
单例模式会使用synchronize关键字保证判断和创建过程中的原子性，但还需要volatile关键字修饰
单利变量，以保证创建过程中不会发生指令重排导致创建对象却未初始化的情况。具体参考：
[设计模式之 - 单例模式]({% post_url design-pattern/content/2021-08-06-01-singleton %})

## 参考资料

* [Java并发 - volatile 关键字]({% post_url java/concurrent/content/2021-07-29-02-concurrent-keyword-volatile %})
* [7.4' - Java并发编程的艺术](https://book.douban.com/subject/26591326/)
* [后端进阶 - Java并发之CAS原理分析](https://objcoding.com/2018/11/29/cas/)
* [方程的博客 - volatile为什么能禁止指令重排](https://www.chuckfang.com/2020/07/05/volatile/)
* [volatile底层原理详解](https://zhuanlan.zhihu.com/p/133851347)
* [Java 并发编程：volatile的使用及其原理](https://www.cnblogs.com/paddix/p/5428507.html)
* [设计模式之 - 单例模式]({% post_url design-pattern/content/2021-08-06-01-singleton %})
* [Java并发 - Java内存模型]({% post_url java/concurrent/content/2021-08-23-04-concurrent-memory-model %})

