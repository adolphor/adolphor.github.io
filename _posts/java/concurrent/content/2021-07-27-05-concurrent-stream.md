---
layout:     post
title:      Java并发 - 并行流
date:       2021-07-27 23:19:10 +0800
postId:     2021-07-27-23-19-11
categories: [concurrent]
keywords:   [Java,concurrent]
---

流和 lambda 表达式可能是 Java 8 中最重要的两个新特性。流已经被增加为 Collection 接口和 9 其他一些数据源的方法，它允许处理某一数据结构的所有元素、生成新的结构、筛选数据和使用 MapReduce 方法来实现算法。

并行流是一种特殊的流，它以一种并行方式实现其操作。使用并行流时涉及的最重要的元素如下。

### Stream 接口
该接口定义了所有可以在一个流上实施的操作。

### Optional
这是一个容器对象，可能(也可能不)包含一个非空值。

### Collectors
该类实现了约简(reduction)操作，而该操作可作为流操作序列的一部分使用。

### lambda 表达式
流被认为是可以处理 lambda 表达式的。大多数流方法都会接收一个 lambda 表达式作为参数，这让你可以实现更为紧凑的操作。

## 参考资料

* [Java并发 - 并行流]({% post_url java/concurrent/content/2021-07-27-05-concurrent-stream %})
* [7.4' - 精通Java并发编程（第2版）](https://book.douban.com/subject/30327401/)
