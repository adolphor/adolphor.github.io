---
layout:     post
title:      【Java8源码阅读笔记】Collection框架之Vector
date:       2016-08-18 10:32:30 +0800
postId:     2016-08-18-10-32-30
categories: [blog]
tags:       [Java]
geneMenu:   true
excerpt:    【Java8源码阅读笔记】Collection框架之Vector
---

## Vector

Vector是Java遗留类，一般情况下，Vector能实现的功能，都可以用ArrayList类实现。
两者最大的区别是：Vector是线程安全，ArrayList非线程安全。

ArrayList 和 Vector 具体区别如下：

num|ArrayList | Vector
---|---|---
1) |  ArrayList is not synchronized. | Vector is synchronized.
2) |  ArrayList increments 50% of current array size if number of element exceeds from its capacity. | Vector increments 100% means doubles the array size if total number of element exceeds than its capacity.
3) |  ArrayList is not a legacy class, it is introduced in JDK 1.2. | Vector is a legacy class.
4) |  ArrayList is fast because it is non-synchronized. | Vector is slow because it is synchronized i.e. in multithreading environment, it will hold the other threads in runnable or non-runnable state until current thread releases the lock of object.
5) |  ArrayList uses Iterator interface to traverse the elements. | Vector uses Enumeration interface to traverse the elements. But it can use Iterator also.

```java
```

## 参考资料

* [JDK文档 之 Vector](https://docs.oracle.com/javase/8/docs/api/java/util/Vector.html)

* [Difference between ArrayList and Vector](http://www.javatpoint.com/difference-between-arraylist-and-vector)