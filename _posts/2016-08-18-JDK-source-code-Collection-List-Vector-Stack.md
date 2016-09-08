---
layout:     post
title:      【Java8源码阅读笔记】Collection框架之Stack
date:       2016-08-18 11:22:53 +0800
postId:     2016-08-18-11-22-54
categories: [Java]
tags:       [Java, Collection, List]
geneMenu:   true
excerpt:    【Java8源码阅读笔记】Collection框架之Stack
---

## Stack
Stack是基于Vector实现的栈操作类，所以内部存储结构是数组，
Vector是线程安全，同样，Stack也是线程安全：涉及操作的方法都有`synchronized`关键字。

## 接口实现
虽然Stack继承自Vector，拥有Vector的所有方法，
但栈操作本身主要有以下几个方法。

### empty()
since unknown，查看栈是否为空

### peek()
since unknown，查看栈顶端的元素，但并不删除。

### pop()
since unknown，删除栈顶端的元素并返回

### push(E item)
since unknown，新增元素到栈顶端

### search(Object o)
since unknown，查找元素在栈中的位置并返回

{% highlight java %}
{% endhighlight %}

## 参考资料

* [JDK文档 之 Stack](https://docs.oracle.com/javase/8/docs/api/java/util/Stack.html)
