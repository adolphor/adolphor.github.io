---
layout:     post
title:      【Java8源码阅读笔记】Collection框架之AbstractList
date:       2016-08-11 15:02:58 +0800
postId:     2016-08-11-15-02-58
categories: [blog]
tags:       [Java]
geneMenu:   true
excerpt:    【Java8源码阅读笔记】Collection框架之AbstractList
---

## AbstractList
AbstractList提供了实现类的一些基础实现，对于不可变List只需要实现 get(int) 和 size() 方法即可，
对于可变List，还需要再实现 e add(int, E)，set(int, E) 和 remove(int) 方法。

## 接口实现

### boolean add(E e)
{% highlight java %}
public boolean add(E e) {
    add(size(), e); // 调用 add(index, ele)方法，size()作为index，表示追加到最后一位
    return true;
}
{% endhighlight %}


### void add(int index, E element
{% highlight java %}
public void add(int index, E element) {
    throw new UnsupportedOperationException();  // 需要子类自己实现
}
{% endhighlight %}



### boolean addAll(int index, Collection<? extends E> c)
{% highlight java %}
public boolean addAll(int index, Collection<? extends E> c) {
    rangeCheckForAdd(index);    // 检查下标是否越界
    boolean modified = false;
    for (E e : c) {             // 遍历元素
        add(index++, e);        // 追加到集合
        modified = true;
    }
    return modified;
}
{% endhighlight %}

### E set(int index, E element)
{% highlight java %}
public E set(int index, E element) {
    throw new UnsupportedOperationException();  // 需要子类自己实现
}
{% endhighlight %}

### E remove(int index)
{% highlight java %}
public E remove(int index) {
    throw new UnsupportedOperationException();  // 需要子类自己实现
}
{% endhighlight %}

### protected void removeRange(int fromIndex, int toIndex)
{% highlight java %}
protected void removeRange(int fromIndex, int toIndex) {
    ListIterator<E> it = listIterator(fromIndex);   // 截取定点位置之后的元素
    for (int i=0, n=toIndex-fromIndex; i<n; i++) {  // 遍历移除至endIndex
        it.next();      // 下个节点
        it.remove();    // 删除
    }
}
{% endhighlight %}

### abstract public E get(int index)
需要子类自己实现

### List<E> subList(int fromIndex, int toIndex)
根据集合是否是RandomAccess来调用不同的方法，实现高性能，但本抽象类中RandomAccessSubList
依然调用的是SubList实现，所以两者并没有什么区别：
{% highlight java %}
public List<E> subList(int fromIndex, int toIndex) {
    return (this instanceof RandomAccess ?
            new RandomAccessSubList<>(this, fromIndex, toIndex) :
            new SubList<>(this, fromIndex, toIndex));
}
{% endhighlight %}

### int hashCode()
哈希码的计算，遍历计算每个元素的哈希码，并按照 `result = 31 * result + C` 的公式进行计算，
之所以选择31这个奇素数作为系数，是因为31可以转换为 `31 * i == (i << 5) - i` 的位运算，提高效率：
{% highlight java %}
public int hashCode() {
    int hashCode = 1;
    for (E e : this)
        hashCode = 31*hashCode + (e==null ? 0 : e.hashCode());
    return hashCode;
}
{% endhighlight %}

### boolean equals(Object o)
{% highlight java %}
{% endhighlight %}

### int indexOf(Object o)
{% highlight java %}
{% endhighlight %}

### Iterator<E> iterator()
{% highlight java %}
{% endhighlight %}

### int lastIndexOf(Object o)
{% highlight java %}
{% endhighlight %}

### ListIterator<E> listIterator()
{% highlight java %}
{% endhighlight %}

### ListIterator<E> listIterator(int index)
{% highlight java %}
{% endhighlight %}

## 覆写实现

### void clear()
{% highlight java %}
{% endhighlight %}


## 接口继承

### AbstractCollection

addAll, contains, containsAll, isEmpty, remove, removeAll, retainAll, size, toArray, toArray, toString

## 参考资料

* [JDK文档 之 AbstractList](https://docs.oracle.com/javase/8/docs/api/java/util/AbstractList.html)

{% highlight java %}
{% endhighlight %}
