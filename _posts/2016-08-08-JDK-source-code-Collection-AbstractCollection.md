---
layout:     post
title:      【JDK8源码阅读笔记】Collection框架之AbstractCollection
date:       2016-08-08 10:58:19 +0800
postId:     2016-08-08-10-58-19
categories: [Collection]
tags:       [Collection, AbstractCollection]
geneMenu:   true
excerpt:    excerpt
---

## AbstractCollection
`AbstractCollection`是集合实现类的根抽象实现类，实现了`Collection`接口，集合中的三个分支`Set`,`List`,`Queue`
都是继承此类之后再进行各自实现的扩展，分别是`AbstractSet`,`AbstractList`,`AbstractQueue`。

## 实现的方法

### contains
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


### toArray
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
    return it.hasNext() ? finishToArray(r, it) : r;
}
private static <T> T[] finishToArray(T[] r, Iterator<?> it) {
    int i = r.length;
    while (it.hasNext()) {
        int cap = r.length;
        if (i == cap) {
            int newCap = cap + (cap >> 1) + 1;
            // overflow-conscious code
            if (newCap - MAX_ARRAY_SIZE > 0)
                newCap = hugeCapacity(cap + 1);
            r = Arrays.copyOf(r, newCap);
        }
        r[i++] = (T)it.next();
    }
    // trim if overallocated
    return (i == r.length) ? r : Arrays.copyOf(r, i);
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
        ```
        public Iterator<E> iterator() {
            return map.keySet().iterator();
        }
        ```
    * LinkedHashSet：继承`HashSet`  
    * TreeSet：实现iterator()方法  
        ```
        public Iterator<E> iterator() {
            return m.navigableKeySet().iterator();
        }
        ```
        
* Queue  
    * ArrayDeque：`System.arraycopy()`  
    * PriorityQueue：`Arrays.copyOf(queue, size)`  


### T[] toArray

{% highlight java %}
public <T> T[] toArray(T[] a) {
    // Estimate size of array; be prepared to see more or fewer elements
    int size = size();
    T[] r = a.length >= size ? a :
              (T[])java.lang.reflect.Array
              .newInstance(a.getClass().getComponentType(), size);
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

指定数据类型的数组转换，

### remove
{% highlight java %}
public boolean remove(Object o) {
    Iterator<E> it = iterator();    // 需要子类实现iterator方法
    if (o==null) {
        while (it.hasNext()) {
            if (it.next()==null) {
                it.remove();
                return true;
            }
        }
    } else {
        while (it.hasNext()) {
            if (o.equals(it.next())) {
                it.remove();
                return true;
            }
        }
    }
    return false;
}
{% endhighlight %}

### containsAll
{% highlight java %}
public boolean containsAll(Collection<?> c) {
    for (Object e : c)
        if (!contains(e)) // contains需要子类实现iterator方法
            return false;
    return true;
}
{% endhighlight %}

### addAll
{% highlight java %}
public boolean addAll(Collection<? extends E> c) {
    boolean modified = false;
    for (E e : c)
        if (add(e))      // 需要子类实现add方法
            modified = true;
    return modified;
}
{% endhighlight %}

### removeAll
{% highlight java %}
public boolean removeAll(Collection<?> c) {
    Objects.requireNonNull(c);
    boolean modified = false;
    Iterator<?> it = iterator();    // 需要子类实现add方法
    while (it.hasNext()) {
        if (c.contains(it.next())) {
            it.remove();
            modified = true;
        }
    }
    return modified;
}
{% endhighlight %}


### retainAll
{% highlight java %}
public boolean retainAll(Collection<?> c) {
    Objects.requireNonNull(c);
    boolean modified = false;
    Iterator<E> it = iterator();    // 需要子类实现add方法
    while (it.hasNext()) {
        if (!c.contains(it.next())) {
            it.remove();
            modified = true;
        }
    }
    return modified;
}
{% endhighlight %}

### clear
{% highlight java %}
public void clear() {
    Iterator<E> it = iterator();    // 需要子类实现add方法
    while (it.hasNext()) {
        it.next();
        it.remove();
    }
}
{% endhighlight %}




