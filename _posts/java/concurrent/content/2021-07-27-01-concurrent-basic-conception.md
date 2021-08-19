---
layout:     post
title:      Java并发 - 基本概念
date:       2021-07-27 22:10:44 +0800
postId:     2021-07-27-22-10-45
categories: [concurrent]
keywords:   [Java,concurrent]
---


## 并发三特性
* 原子性
* 可见性
* 有序性

## 线程和进程
一个最最基础的事实：CPU太快，太快，太快了，寄存器仅仅能够追的上他的脚步，RAM和别的挂在
各总线上的设备则难以望其项背。那当多个任务要执行的时候怎么办呢？轮流着来?或者谁优先级高谁来？
不管怎么样的策略，一句话就是在CPU看来就是轮流着来。而且因为速度差异，CPU实际的执行时间和等待
执行的时间是数量级的差异。比如工作1秒钟，休息一个月。所以多个任务，轮流着来，让CPU不那么无聊，
给流逝的时间增加再多一点点的意义。这些任务，在外在表现上就仿佛是同时在执行。

一个必须知道的事实：执行一段程序代码，实现一个功能的过程之前 ，当得到CPU的时候，相关的资源
必须也已经就位，就是万事俱备只欠CPU这个东风。所有这些任务都处于就绪队列，然后由操作系统的
调度算法，选出某个任务，让CPU来执行。然后就是PC指针指向该任务的代码开始，由CPU开始取指令，
然后执行。

这里要引入一个概念：除了CPU以外所有的执行环境，主要是寄存器的一些内容，就构成了的进程的
上下文环境。进程的上下文是进程执行的环境。当这个程序执行完了，或者分配给他的CPU时间片用完了，
那它就要被切换出去，等待下一次CPU的临幸。在被切换出去做的主要工作就是保存程序上下文，因为这个
是下次他被CPU临幸的运行环境，必须保存。串联起来的事实：前面讲过在CPU看来所有的任务都是一个一个
的轮流执行的，具体的轮流方法就是：先加载进程A的上下文，然后开始执行A，保存进程A的上下文，调入
下一个要执行的进程B的进程上下文，然后开始执行B,保存进程B的上下文……

进程和线程就是这样的背景出来的，两个名词不过是对应的CPU时间段的描述，名词就是这样的功能。

参考内容：[知乎 zhonyong](https://www.zhihu.com/question/25532384/answer/81152571)

Bob：进程和线程都是独立的执行单位。典型的区别是（同一进程的）线程在共享内存空间中运行，
而进程在单独的内存空间中运行。一个进程包含的所有线程共享其虚拟地址空间和系统资源。

### 进程
进程( Process )是程序的运行实例。例如，一个运行的Eclipse 就是一个进程。进程
与程序之间的关系就好比播放中的视频(如《摩登时代》这部电影)与相应的视频文件(如
mp4文件)之间的关系，前者从动态的角度刻画事物而后者从静态的角度刻画事物。运行
一个Java程序的实质是启动一个Java虚拟机进程，也就是说一个运行的Java程序就是一
个Java虚拟机进程。

### 线程
进程是程序向操作系统申请资源(如内存空间和文件句柄)的基本单位。线程(Thread)
是进程中可独立执行的最小单位。例如，一个实现从服务器上下载大文件功能的程序为了
提高其文件下载效率可以使用多个线程，这些线程各自独立地从服务器上下载大文件中的
段数据。

一个进程可以包含多个线程。同一个进程中的所有线程共享该进程中的资源，如内存
空间、文件句柄等。进程与线程之间的关系，好比一个营业中的饭店与其正在工作的员工
之间的关系。一个营业中的饭店对外为顾客提供餐饮服务，而这种服务最终是通过该饭店
的员工的工作实现的。这些工作中的员工有的在迎宾，有的在烹调，有的给顾客上菜。他
们在其工作过程中共享该饭店的资源，如食材、餐具、清洁用具等。

### 任务
线程所要完成的计算就被称为任务，特定的线程总是在执行着特定的任务。任务代表
线程所要完成的工作，它是一个相对的概念。一个任务可以是从服务器上下载一个文件、
解压缩一批文件、解压缩一个文件、监视某个文件的最后修改时间等。这些任务也正是相
应线程存在的理由。

## 并发名词解释

### 并发与并行
并发和并行是非常相似的概念。并发是在单个处理器上采用单核执行多个任务即为并发，在这种情况下，
操作系统的任务调度程序会很快从一个任务切换到另一个任务，因此看起来所有任务都是同时运行的。
同一时间在不同的计算机、处理器或处理器核心上同时运行多个任务，就是所谓的“并行”。

### 同步
将 `同步` 定义为一种协调两个或更多任务以获得预期结果的机制。同步方式有两种：
* 控制同步：当一个任务的开始依赖于另一个任务的结束时，第二个任务不能在第一个任务完成之前开始
* 数据访问同步：当两个或更多任务访问共享变量时，在任意时间里，只有一个任务可以访问该变量。

### 临界段
与同步密切相关的一个概念是临界段。临界段是一段代码，由于它可以访问共享资源，因此在任何给定时间
内，只能够被一个任务执行。

### 互斥
互斥是用来保证 `临界段` 这一要求的机制，而且可以采用不同的方式来实现。

### 粒度
同步可以帮助你在完成并发任务的同时避免一些错误，但是它也为 你的算法引入了一些开销。这就涉及并发
算法的粒度。如果算法有着粗粒度(低互通信的大型任务)，同步方 面的开销就会较低。如果算法有着细粒度
(高互通信的小型 任务)，同步方面的开销就会很高，而且该算法的吞吐量可能不会很好。

### 不可变对象
不可变对象是一种非常特殊的对象。在其初始化后，不能修改其可视状态(其属性值)。如果想修改一个
不可变对象，那么你就必须创建一个新的对象。不可变对象的主要优点在于它是线程安全的。

### 原子操作和原子变量
锁是通过互斥保障原子性的。所谓互斥( Mutual Exclusion)，就是指一个锁一次只能
被一个线程持有。因此一个线程持有一个锁的时候，其他线程无法获得该锁，而只能等待
其释放该锁后再申请。这就保证了临界区代码一次只能够被一个线程执行。因此，一个线
程执行临界区期间没有其他线程能够访问相应的共享数据，这使得临界区代码所执行的操
作自然而然地具有不可分割的特性，即具备了原子性。

#### 原子操作
原子操作是一种发生在瞬间的操作。在并发应用程序中，可以通过一个临界段来实现原子操作，以便对整个
操作采用同步机制。

#### 原子变量
原子变量是一种通过原子操作来设置和获取其值的变量。可以使用某种同步机制来实现一个原子变量，或者
也可以使用 CAS 以无锁方式来实现一个原子变量，而这种方式并不需要任何同步机制。

### 可见性
可见性的保障是通过写线程冲刷处理器缓存和读线程刷新处理器缓存这两个动作实现的。在Java平台中，
锁的获得隐含着刷新处理器缓存这个动作，这使得读线程在执行临界区代码前(获得锁之后)可以将写线程
对共享变量所做的更新同步到该线程执行处理器的高速缓存中;而锁的释放隐含着冲刷处理器缓存这个动作，
这使得写线程对共享变量所做的更新能够被“推送”到该线程执行处理器的高速缓存中，从而对读线程可同步。
因此，锁能够保障可见性。

## 通信方式
任务可以通过两种不同的方法来相互通信：共享内存 & 消息传递。

### 共享内存
同一台计算机上 运行多任务的情况时，任务在读取和写入值的时候使用相同的内存区域。为了避免出现
问题，对该共享内存的访问必须在一个由同步机制保护的临界段内完成。

### 消息传递
在不同计算机上运行多任务的情形时，当一个任务需要与另一个任务通信时，它会发送一个遵循预定义协议
的消息。如果发送方保持阻塞并等待响应，那么该通信就是同步的；如果发送方在发送消息后继续执行自己
的流程，那么该通信就是异步的。

### 线程安全
如果共享数据的所有用户都受 到同步机制的保护，那么代码(或方法、对象)就是线程安全的。

## 同步机制
并发系统中有不同的同步机制。从理论角度来看，最流行的机制如下：信号量 & 监视器。

### 信号量
信号量(semaphore)，一种用于控制对一个或多个单位资源进行访问的机制。它有一个用于存放可用资源
数量的变量，并且可以采用两种原子操作来管理该变量的值。互斥(mutex，mutual exclusion 的简写
形式)是一种特殊类型的信号量，它只能取两个值(即资源空闲和资源忙)， 而且只有将互斥设置为忙的那个
进程才可以释放它。互斥可以通过保护临界段来帮助你避免出现竞争条件。

### 监视器
监视器是一种在共享资源之上实现互斥的机制。它有一个互斥、一个条件变量、两种操作(等待条件和通报
条件)。一旦你通报了该条件，在等待它的任务中只有一个会继续执行。

## 并发应用程序中可能出现的问题
编写并发应用程序并不是一件容易的工作。如果不能正确使用同步机制，应用程序中的任务就会出现各种
问题。

### 数据竞争/竞争条件
如果有两个或者多个任务在临界段之外对一个共享变量进行写入操作，也就是说没有使用任何同步机制，
那么应用程序可能存在数据竞争(也叫作竞争条件)。

### 死锁
当两个(或多个)任务正在等待必须由另一线程释放的某个共享资源，而该线程又正在等待必须由前述任务
之一释放的另一共享资源时，并发应用程序就出现了死锁。当系统中同时出现如下四种条件时，就会导致
这种情形。我们将其称为 Coffman 条件：
* 互斥：死锁中涉及的资源必须是不可共享的。一次只有一个任务可以使用该资源。
* 占有并等待条件：一个任务在占有某一互斥的资源时又请求另一互斥的资源。当它在等待时，不会释放任何资源。
* 不可剥夺：资源只能被那些持有它们的任务释放。
* 循环等待：任务 1 正等待任务 2 所占有的资源， 而任务 2 又正在等待任务 3 所占有的资源，以此类推，最终任务 n 又在等待由任务 1 所占有的资源，这样就出现了循环等待。

如何避免死锁：
* 避免一个线程同时获取多个锁
* 避免一个线程在锁内同时占用多个资源，尽量保证每个锁只占用一个资源
* 尝试使用定时锁，使用 lock.tryLock(timeout)来替代使用内部锁机制
* 对于数据库锁，加锁和解锁必须在一个数据库连接里，否则会出现解锁失败的情况

### 活锁
如果系统中有两个任务，它们总是因对方的行为而改变自己的状态，那么就出现了活锁。最终结果是它们
陷入了状态变更的循环而无法继续向下执行。

### 资源不足
资源限制是指在进行并发编程时，程序的执行速度受限于计算机硬件资源或软件资 源。例如，服务器的带宽
只有 2Mb/s，某个资源的下载速度是 1Mb/s 每秒，系统启动 10 个线程下载资源，下载速度不会变成 
10Mb/s，所以在进行并发编程时，要考虑这些资源 的限制。硬件资源限制有带宽的上传/下载速度、硬盘
读写速度和 CPU 的处理速度。软 件资源限制有数据库的连接数和 socket 连接数等。

如果将某段串行的代码并发执行，因为受限于资源，仍然在串行执行，这时候 程序不仅不会加快执行，反而
会更慢，因为增加了上下文切换和资源调度的时间。

当某个任务在系统中无法获取维持其继续执行所需的资源时，就会出现资源不足。当有多个任务在等待某一
资源且该资源被释放时，系统需要选择下一个可以使用该资源的任务。如果你的系统中没有设计良好的算法，
那么系统中有些线程很可能要为获取该资源而等待很长时间。

### 优先权反转
当一个低优先权的任务持有了一个高优先级任务所需的资源时，就会发生优先权反转。这样的话，低优先权
的任务就会在高优先权的任务之前执行。

## 上下文切换
单核处理器可以支持多线程执行代码，CPU通过给每个线程分配CPU时间片来实现这个机制。CPU通过时间片
分配算法来循环执行多个线程的任务，当前任务执行一个时间片后悔切换到下一个任务。但是，在切换前会
保存上一个任务的状态，以便下次切换回这个任务时，可以再加载这个任务的状态。所以任务从保存到再加载
的过程就是一次上下文切换。

线程有创建和上下文切换的开销，所以多线程并不一定比单线程执行更快。


## 参考资料

* [Java并发 - 基本概念]({% post_url java/concurrent/content/2021-07-27-01-concurrent-basic-conception %})
* [7.4' - 精通Java并发编程（第2版）](https://book.douban.com/subject/30327401/)
* [7.4' - Java并发编程的艺术](https://book.douban.com/subject/26591326/)
* [8.8' - Java多线程编程实战指南-核心篇](https://book.douban.com/subject/27034721/)