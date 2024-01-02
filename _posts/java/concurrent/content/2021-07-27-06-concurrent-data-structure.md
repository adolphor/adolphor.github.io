---
layout:     post
title:      Java并发 - 并发数据结构
date:       2021-07-27 23:19:56 +0800
postId:     2021-07-27-23-19-56
categories: [Concurrent]
keywords:   [Java,concurrent]
---

Java API 中的常见数据结构(例如 ArrayList、Hashtable 等)并不能在并发应用程序中使用，除非采用某种外部同步机制。但是如果你采用了某种同步机制，应用程序就会增加大量的额外计算时 间。而如果你不采用同步机制，那么应用程序中很可能出现竞争条件。如果你在多个线程中修改数据， 那么就会出现竞争条件，你可能会面对各种异常(例如 ConcurrentModificationException 和 ArrayIndexOutOfBoundsException)，出现隐性数据丢失，或者应用程序会陷入死循环。

Java 并发 API 中含有大量可以在并发应用中使用而没有风险的数据结构。我们将它们分为以下两大类别：
* 阻塞型数据结构：这些数据结构含有一些能够阻塞调用任务的方法，例如，当数据结构为空 而你又要从中获取值时。
* 非阻塞型数据结构：如果操作可以立即进行，它并不会阻塞调用任务。否则，它将返回 null 值或者抛出异常。

### ConcurrentLinkedDeque
这是一个非阻塞型的列表。

### ConcurrentLinkedQueue
这是一个非阻塞型的队列。
### LinkedBlockingDeque
这是一个阻塞型的列表

### LinkedBlockingQueue
这是一个阻塞型的队列。

### PriorityBlockingQueue
这是一个基于优先级对元素进行排序的阻塞型队列。

### ConcurrentSkipListMap
这是一个非阻塞型的 NavigableMap。

### ConcurrentHashMap
这是一个非阻塞型的哈希表。

### Atomic相关
这些是基本 Java 数据类型的原子实现：
* AtomicBoolean
* AtomicInteger
* AtomicLong
* AtomicReference

## 参考资料

* [Java并发 - 并发数据结构]({% post_url java/concurrent/content/2021-07-27-06-concurrent-data-structure %})
* [7.4' - 精通Java并发编程（第2版）](https://book.douban.com/subject/30327401/)
