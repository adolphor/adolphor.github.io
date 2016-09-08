---
layout:     post
title:      【Java8源码阅读笔记】Collection框架之Set
date:       2016-08-18 11:38:47 +0800
postId:     2016-08-18-11-38-47
categories: [Java]
tags:       [Java, Collection, Set]
geneMenu:   true
excerpt:    【Java8源码阅读笔记】Collection框架之Set
---

## Set

since 1.2，Set是一个不包含重复元素的集合。一般来说，不会包含 `e1.equals(e2)` 的两个元素，
而且最后含有一个 `null` 值。和它名称所暗示的一样，此接口模拟了数学中的set概念。
(As implied by its name, this interface models the mathematical set abstraction.)

Set接口中的所有接口都继承自 `Collection`，只是增加了不能含有重复元素的限制条件。

此接口下有 `TreeSet`, `HashSet`, `LinkedHashSet` 三个实现类。

## Set独有接口
Set没有独有接口，所有的接口都继承自父类，sad.... 

## 覆写Collection的接口
下面这些接口在`Collection`中都已经定义：

    boolean add(E e);
    boolean addAll(Collection<? extends E> c);
    boolean contains(Object o);
    boolean containsAll(Collection<?> c);

    boolean remove(Object o);
    boolean removeAll(Collection<?> c);
    boolean retainAll(Collection<?> c);

    void clear();
    boolean isEmpty();

    Iterator<E> iterator();
    default Spliterator<E> spliterator();
    Object[] toArray();
    <T> T[] toArray(T[] a);
    int size();

    int hashCode();
    boolean equals(Object o);

## 继承的方法

### Collection

equals, hashCode, parallelStream, removeIf, spliterator, stream

### Iterable

#### default void forEach(Consumer<? super T> action)
since 1.8，TODO


## 参考资料

* [JDK文档 之 Set](https://docs.oracle.com/javase/8/docs/api/java/util/Set.html)

{% highlight java %}
{% endhighlight %}

