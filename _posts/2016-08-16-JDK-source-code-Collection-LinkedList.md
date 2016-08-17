---
layout:     post
title:      【Java8源码阅读笔记】Collection框架之LinkedList
date:       2016-08-16 15:59:31 +0800
postId:     2016-08-16-15-59-31
categories: [Collection]
tags:       [Collection, List]
geneMenu:   true
excerpt:    excerpt
---

## LinkedList
LinkedList since 1.2，使用的是双向链表存储结构。

LinkedList同样非线程安全，如果有多线程共同操作这个list实例，就需要将其加锁，
将锁加在包含这个list的对象上，或者使用如下形式：
{% highlight java %}
List list = Collections.synchronizedList(new LinkedList(...));
{% endhighlight %}

因为LinkedList是基于链表实现，其内部封装了一个Node节点内部类作为元素的载体，
有两个自关联属性：next 和 prev，作为双向连接的实现，具体代码如下：
{% highlight java %}
private static class Node<E> {
    E item;
    Node<E> next;
    Node<E> prev;
    Node(Node<E> prev, E element, Node<E> next) {
        this.item = element;            // 当前节点元素
        this.next = next;               // 上一个节点
        this.prev = prev;               // 下一个节点
    }
}
{% endhighlight %}


## 接口实现

### add(E e)
默认追加元素到链尾，  
时间复杂度是O(1)

### add(int index, E element)
在指定位置增加元素，将老链接断开，并将新Node插入到此位置，并建立新链接，  
时间复杂度O(N)

在查找指定位置元素的时候，虽然使用的是遍历循环的方式，但用了一个小技巧，也就是使用了一次
二分法：如果index在链表前部，就从前往后查找；如果index在链表后部，就从后往前查找。
这样避免index在最后一位的时候从头找到尾，提高效率：

###### Node(int index)方法实现 {#nodeIndexCode}
{% highlight java %} 
Node<E> node(int index) {
    // assert isElementIndex(index);
    if (index < (size >> 1)) {  // 如果index在链表前半部分
        Node<E> x = first;      // x 作为中间变量，从前往后定位
        for (int i = 0; i < index; i++)
            x = x.next;
        return x;
    } else {                    // index在链表后半部分
        Node<E> x = last;       // x作为中间变量，从后往前定位
        for (int i = size - 1; i > index; i--)
            x = x.prev;
        return x;
    }
}
{% endhighlight %}

### addFirst(E e)
追加元素到链首，  
时间复杂度是O(1)

### addLast(E e)
追加元素到链尾，  
时间复杂度是O(1)

### addAll(Collection<? extends E> c)
增加集合到LinkedList，首先将参数col组装为链表形式，之后将这个参数链表追加到链表尾部，  
时间复杂度为O(1)

### addAll(int index, Collection<? extends E> c)
增加集合到LinkedList指定位置，首先将参数col组装为链表形式，之后将这个参数链表追加到链表指定位置index，  
时间复杂度为O(N)

### offer(E e)
since 1.5，调用add方法，为了向前兼容？

### offerFirst(E e)
since 1.6，调用addFirst方法，为了向前兼容？

### offerLast(E e)
since 1.6，调用addLast方法，为了向前兼容？

### set(int index, E element)
将指定位置元素设置为新值：ele，相当于修改操作，  
时间复杂度为O(N)

### push(E e)
since 1.6，调用addFirst方法

### get(int index)
获取指定位置的节点元素，和add方法一样，使用 [node(index)](#nodeIndexCode) 方法实现，时间复杂度O(N)

### getFirst() {#getFirst}
获取第一个元素，如果集合为空，则抛出`NoSuchElementException`异常，  
直接访问first节点属性，时间复杂度O(1)

### getLast()
获取最后一个元素，如果集合为空，则抛出`NoSuchElementException`异常，  
直接访问last节点属性，时间复杂度O(1)

### element()
since 1.5，直接调用的 [getFirst](#getFirst) 方法，应该是为了向前兼容吧，我想……

### peek()
since 1.5，查看第一个元素，和 [getFirst](#getFirst) 类似，但如果集合为空，则返回null。
直接访问first节点属性，时间复杂度O(1)。

### peekFirst()
since 1.6，查看第一个元素，但如果集合为空，则返回null。

### peekLast()
since 1.6，查看最后一个元素，但如果集合为空，则返回null。

### poll()
since 1.5，移除第一个元素并返回，如果集合为空，返回null。
直接访问first节点元素，时间复杂度为O(1)。
移除第一个元素的实现如下：

###### unlinkFirst(Node<E> f) 方法实现 {#unlinkFirstCode}
{% highlight java %}
private E unlinkFirst(Node<E> f) {
    // assert f == first && f != null;  // 确保first不为空
    final E element = f.item;           // 中间变量，用于返回
    final Node<E> next = f.next;        // 中间变量
    f.item = null;
    f.next = null; // help GC           // 帮助垃圾回收
    first = next;                       // 将next作为移除之后的first
    if (next == null)
        last = null;                    // 如果集合为空，last也需要置空
    else
        next.prev = null;               // 如果集合不为空，原来第二个元素（当前的第一个元素）的prev已经移除，需要置空
    size--;                             // size减少1
    modCount++;                         // 操作数增加1（这个是用来防止多线程操作引起的BUG）
    return element;                     // 返回移除的那个元素
}
{% endhighlight %}


### pollFirst()
since 1.6，等同于poll()方法

### pollLast()
since 1.6，删除最后一个元素并返回，如果集合为空，返回null。实现方法和 [unlinkFirst(Node<E> f)](#unlinkFirstCode) 类似。
直接访问last节点元素，时间复杂度为O(1)。

### remove()
since 1.5，调用 removeFirst 方法

### remove(int index)
删除定点位置元素， [node(index)](#nodeIndexCode) 方法实现，时间复杂度O(N)。

### removeFirst()
删除第一个元素，如果集合为空，抛出NoSuchElementException异常。

### removeLast()
删除第一个元素，如果集合为空，抛出NoSuchElementException异常。

### pop()
since 1.6，调用 removeFirst() 。

### remove(Object o) {#removeObjectCode}
如果集合中含有equals此参数的元素，进行移除。
从first进行遍历比较，实现方式如下：
{% highlight java %}
public boolean remove(Object o) {
    if (o == null) {
        for (Node<E> x = first; x != null; x = x.next) {
            if (x.item == null) {
                unlink(x);  // 解除此元素链接，进行链接的重新建立操作
                return true;
            }
        }
    } else {
        for (Node<E> x = first; x != null; x = x.next) {
            if (o.equals(x.item)) {
                unlink(x);
                return true;
            }
        }
    }
    return false;
}
{% endhighlight %}

### removeFirstOccurrence(Object o)
since 1.6，删除第一个符合条件的元素，直接调用的 [remove(Object o)](removeObjectCode) 方法。

### removeLastOccurrence(Object o)
since 1.6，删除最后一个符合条件的元素，从last开始进行遍历比较。

### clear()
清空集合中的所有元素，虽然“没有必要”将所有节点之间的链接都解除，但是这样做有助于垃圾回收。
{% highlight java %}
public void clear() {
    for (Node<E> x = first; x != null; ) {
        Node<E> next = x.next;
        x.item = null;
        x.next = null;
        x.prev = null;
        x = next;
    }
    first = last = null;
    size = 0;
    modCount++;
}
{% endhighlight %}

### clone()

### contains(Object o)

### descendingIterator()

### indexOf(Object o)

### lastIndexOf(Object o)

### listIterator(int index)

### size()

### spliterator()

### toArray()

### toArray(T[] a)


{% highlight java %}
{% endhighlight %}

## 参考资料

[JDK文档 之 LinkedList](https://docs.oracle.com/javase/8/docs/api/java/util/LinkedList.html)
