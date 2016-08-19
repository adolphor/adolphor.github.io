---
layout:     post
title:      【Java8源码阅读笔记】Collection框架之AbstractSet
date:       2016-08-19 10:14:44 +0800
postId:     2016-08-19-10-14-44
categories: [Collection]
tags:       [Collection, Set]
geneMenu:   true
excerpt:    【Java8源码阅读笔记】Collection框架之AbstractSet
---

## AbstractSet
Since 1.2，本类没有覆写 `AbstractCollection` 中的任何方法，
只是单纯的增加了 `equals` 和 `hashCode` 方法的实现。

## 接口实现



## 接口继承

### AbstractCollection
add, addAll, clear, contains, containsAll, isEmpty, iterator, remove, retainAll, size, toArray, toArray, toString

## 参考资料

* [JDK文档 之 AbstractSet](https://docs.oracle.com/javase/8/docs/api/java/util/AbstractSet.html)

{% highlight java %}
{% endhighlight %}
