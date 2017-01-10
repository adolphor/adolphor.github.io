---
layout:     post
title:      【Java8源码阅读笔记】Collection框架之AbstractCollection
date:       2016-08-08 10:58:19 +0800
postId:     2016-08-08-10-58-19
categories: [blog]
tags:       [Java]
geneMenu:   true
excerpt:    【Java8源码阅读笔记】Collection框架之AbstractCollection
---

## AbstractCollection
`AbstractCollection`是集合实现类的根抽象实现类，实现了`Collection`接口，集合中的三个分支`Set`,`List`,`Queue`
都是继承此类之后再进行各自实现的扩展，分别是`AbstractSet`,`AbstractList`,`AbstractQueue`。
但是三个分支的具体实现是不一样的，那么Collection作为抽象父类，具体实现时内部存储结构是数组实现，
而LinkedList内部存储结构是链表，这样大部分方法都要自己重新实现。

## 实现的方法

### boolean addAll(Collection<? extends E> c)
{% highlight java %}
public boolean addAll(Collection<? extends E> c) {
    boolean modified = false;
    for (E e : c)        // 遍历新增
        if (add(e))      // 需要子类实现add方法
            modified = true;
    return modified;
}
{% endhighlight %}

### boolean remove(Object o)
{% highlight java %}
public boolean remove(Object o) {
    Iterator<E> it = iterator();    // 需要子类实现iterator方法
    if (o==null) {
        while (it.hasNext()) {
            if (it.next()==null) {
                it.remove();
                return true;        // 这样移除的是符合条件的第一个元素
            }
        }
    } else {
        while (it.hasNext()) {
            if (o.equals(it.next())) {      // equals方法进行比较
                it.remove();
                return true;
            }
        }
    }
    return false;
}
{% endhighlight %}

### boolean removeAll(Collection<?> c)
{% highlight java %}
public boolean removeAll(Collection<?> c) {
    Objects.requireNonNull(c);
    boolean modified = false;
    Iterator<?> it = iterator();    // 需要子类实现iterator方法
    while (it.hasNext()) {
        if (c.contains(it.next())) {    // 遍历移除
            it.remove();
            modified = true;
        }
    }
    return modified;
}
{% endhighlight %}

### boolean retainAll(Collection<?> c)
{% highlight java %}
public boolean retainAll(Collection<?> c) {
    Objects.requireNonNull(c);
    boolean modified = false;
    Iterator<E> it = iterator();    // 需要子类实现iterator方法
    while (it.hasNext()) {
        if (!c.contains(it.next())) {
            it.remove();
            modified = true;
        }
    }
    return modified;
}
{% endhighlight %}

### boolean contains(Object o)
{% highlight java %}
public boolean contains(Object o) {
    Iterator<E> it = iterator();    // 需要子类实现add方法
    if (o==null) {
        while (it.hasNext())
            if (it.next()==null)
                return true;
    } else {
        while (it.hasNext())
            if (o.equals(it.next()))
                return true;
    }
    return false;
}
public abstract Iterator<E> iterator();
{% endhighlight %}

遍历集合进行元素的对比，当集合中有元素与参数`equals`（相等），则返回true。
equals 是集合中的元素的方法实现。如果有覆写，则按照覆写之后的规则进行比较。

AbstractCollection虽然实现了`contains`方法，但是并没有实现`iterate()`方法，此方法需要实现类自己实现。

虽然只需要实现iterate()方法就可以了，但是JDK中各个抽象实现类都有自己的contains方法实现，
这样可以减少一次遍历，除去iterate的中转提高效率：

* List  
    List接口的实现类主要依靠`indexOf`，如果此元素在集合中的下标大于-1，则表示存在此元素
    * ArrayList：内部存储结构是数组，依赖于`public int indexOf(Object o)`方法，遍历数组进行equals比较  
    * LinkedList：内部存储结构是链表，依赖于`public int indexOf(Object o)`方法，遍历链表进行equals比较  
    * Vector：内部存储结构是数组，依赖于`public synchronized int indexOf(Object o, int index)`方法，TODO：为什么此方法需要加同步锁？  
    * Stack：继承自`Vector`   

* Set  
    Set接口的实现类主要依靠Map，
    * HashSet：内部存储结构是`HashMap`，所以，实现方式是直接调用HashMap的实现：`boolaen hashMap.containsKey(o)`  
    * LinkedHashSet：继承自`HashSet`  
    * TreeSet：内部存储结构是`TreeMap`(`SortedMap`接口的实现类)，所以，实现方式是直接调用TreeMap的实现：`boolean treeMap.containsKey(o)`  

* Queue  
    比较意外的是，队列没有使用链表来实现，而是使用了数组，这样的话扩容就不是一次增加一个了
    * ArrayDeque：内部存储结构是数组，遍历数组进行equals方法  
    * PriorityQueue：内部存储结构是数组，依赖于`private int indexOf(Object o)`方法，遍历数组进行equals方法  

### boolean containsAll(Collection<?> c)
{% highlight java %}
public boolean containsAll(Collection<?> c) {
    for (Object e : c)
        if (!contains(e)) // contains需要子类实现iterator方法，参考本文最上面的contains方法
            return false;
    return true;
}
{% endhighlight %}

### void clear()
{% highlight java %}
public void clear() {
    Iterator<E> it = iterator();    // 需要子类实现iterator方法
    while (it.hasNext()) {          // 遍历移除
        it.next();
        it.remove();
    }
}
{% endhighlight %}

### boolean isEmpty()
{% highlight java %}
public boolean isEmpty() {
    return size() == 0;
}
{% endhighlight %}

### Object[] toArray()
{% highlight java %}
public Object[] toArray() {
    // Estimate size of array; be prepared to see more or fewer elements
    Object[] r = new Object[size()];
    Iterator<E> it = iterator();    // 需要子类实现iterator方法
    for (int i = 0; i < r.length; i++) {
        if (! it.hasNext()) // fewer elements than expected
            return Arrays.copyOf(r, i);
        r[i] = it.next();
    }
    return it.hasNext() ? finishToArray(r, it) : r;     // finishToArray用来修正toArray方法执行过程中的数组长度的变化
}
{% endhighlight %}

同样的，虽然实现类只需要实现`iterator()`方法即可，但是各实现类大部分还是拥有自己的`toArray`实现：

* List  
    * ArrayList：存储结构是数组，实现方式`Arrays.copyOf(elementData, size)`，  
    * LinkedList：存储结构是链表，for-each遍历赋值  
    * Vector：存储结构是数组，实现方式`Arrays.copyOf(elementData, elementCount)`  
    * Stack：继承自`Vector`   

* Set  
    * HashSet：实现iterator()方法  

            public Iterator<E> iterator() {
                return map.keySet().iterator();
            }

    * LinkedHashSet：继承`HashSet`  
    * TreeSet：实现iterator()方法  

            public Iterator<E> iterator() {
                return m.navigableKeySet().iterator();
            }
        
* Queue  
    * ArrayDeque：`System.arraycopy()`  
    * PriorityQueue：`Arrays.copyOf(queue, size)`  


### <T> T[] toArray(T[] a)
指定数据类型的数组转换

{% highlight java %}
public <T> T[] toArray(T[] a) {
    // Estimate size of array; be prepared to see more or fewer elements
    int size = size();
    T[] r = a.length >= size ? a :
              (T[])java.lang.reflect.Array
              .newInstance(a.getClass().getComponentType(), size);  // 获取正确数据类型的容器
    Iterator<E> it = iterator();    // 需要子类实现iterator方法

    for (int i = 0; i < r.length; i++) {
        if (! it.hasNext()) { // fewer elements than expected
            if (a == r) {
                r[i] = null; // null-terminate
            } else if (a.length < i) {
                return Arrays.copyOf(r, i);
            } else {
                System.arraycopy(r, 0, a, 0, i);
                if (a.length > i) {
                    a[i] = null;
                }
            }
            return a;
        }
        r[i] = (T)it.next();
    }
    // more elements than expected
    return it.hasNext() ? finishToArray(r, it) : r;
}
{% endhighlight %}


### String toString()
{% highlight java %}
public String toString() {
    Iterator<E> it = iterator();
    if (! it.hasNext())
        return "[]";
    StringBuilder sb = new StringBuilder();
    sb.append('[');
    for (;;) {
        E e = it.next();
        sb.append(e == this ? "(this Collection)" : e);
        if (! it.hasNext())
            return sb.append(']').toString();
        sb.append(',').append(' ');
    }
}
{% endhighlight %}


## 未实现方法

### boolean add(E e)
add方法是一个比较特殊的存在，它并不是抽象方法，但也并没有完全实现，而是需要子类去覆写，在本抽象类中的实现方式如下：
{% highlight java %}
public boolean add(E e) {
    throw new UnsupportedOperationException();
}
{% endhighlight %}

本方法只是抛出了一个未支持的操作的异常信息，既然如此，为什么不直接设置为抽象呢？

### abstract Iterator<E> iterator()
所有涉及到集合遍历的操作都要使用

### abstract int size()
size方法获取当前集合的元素的个数，也就是集合的长度，在新增和删除的时候都有变动

## 继承的方法

AbstractCollection 继承自 `Collection`，`Collection` 继承自 `Iterable` 。
方法的具体实现参考：[Collection框架之Collection]({% post_url 2016-08-04-JDK-source-code-Collection-Collection %})

### Collection

equals, hashCode, parallelStream, removeIf, spliterator, stream, forEach

### Iterable

forEach

## 参考资料

* [JDK文档 之 AbstractCollection](https://docs.oracle.com/javase/8/docs/api/java/util/AbstractCollection.html)
