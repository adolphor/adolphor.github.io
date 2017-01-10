---
layout:     post
title:      【Java8源码阅读笔记】Collection框架之HashSet
date:       2016-08-19 21:18:41 +0800
postId:     2016-08-19-21-18-41
categories: [blog]
tags:       [Java]
geneMenu:   true
excerpt:    【Java8源码阅读笔记】Collection框架之HashSet
---

## HashSet
Since 1.2，Collection框架实现类中，使用最多的存储结构是数组（List分支中的ArrayList、Vector、Stack，
Queue分支中的ArrayDeque、queue），还有一些使用链表结构（List分支中的LinkedList）。
只有Set分支实现类使用的是Map存储结构：HashSet使用的是HashMap存储结构；LinkedHashSet继承自HashSet，
同样是HashMap存储结构；TreeSet是TODO。

首先，HashSet声明了如下的全局变量，以及两个构造函数实现：

{% highlight java %}
private transient HashMap<E,Object> map;
private static final Object PRESENT = new Object();
public HashSet() {
    map = new HashMap<>();
}
public HashSet(Collection<? extends E> c) { // 
    map = new HashMap<>(Math.max((int) (c.size()/.75f) + 1, 16));
    addAll(c);  // 构造函数中直接调用addAll方法
}
public HashSet(int initialCapacity) {
    map = new HashMap<>(initialCapacity);
}
public HashSet(int initialCapacity, float loadFactor) {
    map = new HashMap<>(initialCapacity, loadFactor);
}
HashSet(int initialCapacity, float loadFactor, boolean dummy) {
    map = new LinkedHashMap<>(initialCapacity, loadFactor);
}

{% endhighlight %}
全局变量 `map` 作为HashSet内部实现的存储容器，
全局静态变量 `PRESENT` 作为所有的key键的value值。
第一个构造函数，直接初始化一个新的HashMap容器；
第二个构造函数，`? extends E` 表示接收的参数c中的元素必须是E的子类，
这样才能保证添加到集合中的时候不会出现类型转换错误，关键字 `extends` 是 “上界通配符”。
下面在看下 `Math.max((int) (c.size()/.75f) + 1, 16)` ，将c长度扩大三分之一之后和16进行比较，
取两者之间较大的值：这样如果c长度小于12，初始化长度就是16；如果c长度大于12，初始化长度就是c的长度
的三分之四倍大小（除以0.75等于乘以三分之四，也就是增加了三分之一）。
第三个构造函数指定了初始化大小，第四个构造函数指定初始化大小和负载因子（loadFactor）。
第五个构造函数指定了初始化大小、负载因子以及 `dummy` 标志参数，如果有这个参数，
则内部容器使用LinkedHashMap，如果没有这个参数（前四种构造函数）则使用HashMap作为存储容器。
HashSet 的子类 LinkedHashSet 就是调用这个构造函数来进行初始化，这也是 HashSet 和 LinkedHashSet 最主要的区别。

## 接口实现
因为内部存储结构是HashMap，所以绝大部分函数实现都依赖于HashMap函数特性。

### boolean add(E e)
TODO：为什么==null？
{% highlight java %}
public boolean add(E e) {
    return map.put(e, PRESENT)==null;
}
{% endhighlight %}

### void clear()
{% highlight java %}
public void clear() {
    map.clear();
}
{% endhighlight %}
### Object clone()
{% highlight java %}
public Object clone() {
    try {
        HashSet<E> newSet = (HashSet<E>) super.clone();
        newSet.map = (HashMap<E, Object>) map.clone();
        return newSet;
    } catch (CloneNotSupportedException e) {
        throw new InternalError(e);
    }
}
{% endhighlight %}
### boolean contains(Object o)
{% highlight java %}
public boolean contains(Object o) {
    return map.containsKey(o);
}
{% endhighlight %}
### boolean isEmpty()
{% highlight java %}
public boolean isEmpty() {
    return map.isEmpty();
}
{% endhighlight %}
### Iterator<E> iterator()
{% highlight java %}
public Iterator<E> iterator() {
    return map.keySet().iterator();
}
{% endhighlight %}
### boolean remove(Object o)
TODO：为什么要 ==PRESENT？
{% highlight java %}
public boolean remove(Object o) {
    return map.remove(o)==PRESENT;
}
{% endhighlight %}
### int size()
{% highlight java %}
public int size() {
    return map.size();
}
{% endhighlight %}
### Spliterator<E> spliterator()
TODO：HashMap.KeySpliterator函数释义
{% highlight java %}
public Spliterator<E> spliterator() {
    return new HashMap.KeySpliterator<E,Object>(map, 0, -1, 0, 0);
}
{% endhighlight %}

## 接口继承

### AbstractSet
equals, hashCode, removeAll

### AbstractCollection
addAll, containsAll, retainAll, toArray, toArray, toString


## 遗留问题
* 为什么使用 PRESENT 作为value值，而不是使用null？

## 参考资料

* [JDK文档 之 HashSet](https://docs.oracle.com/javase/8/docs/api/java/util/HashSet.html)
* [Java 泛型 <? super T> 中 super 怎么 理解？与 extends 有何不同？](https://www.zhihu.com/question/20400700/answer/117464182)

{% highlight java %}
{% endhighlight %}
