---
layout:     post
title:      【Java8源码阅读笔记】Collection框架之ArrayList
date:       2016-08-11 15:15:50 +0800
postId:     2016-08-11-15-15-50
categories: [Collection]
tags:       [Collection, List]
geneMenu:   true
excerpt:    【Java8源码阅读笔记】Collection框架之ArrayList
---

## ArrayList
ArrayList使用的是数组存储结构，所有数组的特性都可以用于此ArrayList集合的操作。

ArrayList非线程安全，如果有多线程共同操作这个list实例，就需要将其加锁，
将锁加在包含这个list的对象上，或者使用如下形式：
{% highlight java %}
List list = Collections.synchronizedList(new ArrayList(...));
{% endhighlight %}

## 接口实现

### add
如果不指定List的初始化大小，在初始化的时候会初始化一个空数组，
在添加元素的时候进行数组的初始化扩容，默认值为10；
当十个元素位置都被填满，会进行第二次扩容，此次扩容量为当前集合的一半，
整个过程如下：

> 0 + 10 => 10 + 5 => 15 + 7 => 22 + 11 => 33 ...

扩容代码具体实现如下：

{% highlight java %}
private void grow(int minCapacity) {
    // overflow-conscious code
    int oldCapacity = elementData.length;                   // 当前集合长度
    int newCapacity = oldCapacity + (oldCapacity >> 1);     // 位移操作，获得当前长度的一半：当前长度 + 一半的长度
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;                          // 最小扩容大小
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);            // 最大扩容值：不能大于Integer类型的最大值
    // minCapacity is usually close to size, so this is a win:
    elementData = Arrays.copyOf(elementData, newCapacity);  // 将数组拷贝到新集合中
}
{% endhighlight %}

所以当数组长度越大的时候，扩容消耗的资源越多；
如果能够在数组初始化的时候大概估算出数组长度上限进行初始化，
就能够减少数组扩容所引起的资源消耗，提升性能。

### add (index, ele)
定点插入元素到集合，实现方式是使用数组拷贝功能：
{% highlight java %}
public void add(int index, E element) {
    rangeCheckForAdd(index);            // 检查是否越界
    ensureCapacityInternal(size + 1);   // 保证集合有空余存储空间
    System.arraycopy(elementData, index, elementData, index + 1,
                     size - index);     // 数组拷贝，空出指定位置
    elementData[index] = element;       // 指定位置赋值
    size++;
}
{% endhighlight %}

### trimToSize
trimToSize方法用来释放list中空余的存储空间，当不指定list初始化大小的时候，元素增加过程中可能自动进行扩容，
数据越多，可能空余的存储空间就越多，如果这个集合之后不进行增删操作，那么进行除空操作，能够节约内存空间：
{% highlight java %}
public void trimToSize() {
    modCount++;
    if (size < elementData.length) {
        elementData = (size == 0)
          ? EMPTY_ELEMENTDATA       // 如果是空集合，那么使用默认空集
          : Arrays.copyOf(elementData, size);   // 非空，进行数据拷贝
    }
}
{% endhighlight %}

### ensureCapacity
用来进行手动扩容，扩容大小作为参数即可。具体实现参考add方法讲解中的"扩容代码具体实现"。
此接口是ArrayList独有接口。

### isEmpty
根据size属性是否为0来判断ArrayList集合是否为空

### indexOf
查找元素在ArrayList中的位置，foreach遍历进行equals判断
{% highlight java %}
public int indexOf(Object o) {
    if (o == null) {    // 是否为空
        for (int i = 0; i < size; i++)  // 遍历
            if (elementData[i]==null)   // == 判断
                return i;
    } else {
        for (int i = 0; i < size; i++)
            if (o.equals(elementData[i]))   // equals判断
                return i;
    }
    return -1;
}
{% endhighlight %}

### lastIndexOf
查找最有一个所查元素在ArrayList中的位置，foreach倒叙遍历进行equals判断

### contains
判断此ArrayList是否包含此元素，调用indexOf方法来判断。
这里并没有使用二分查找或者别的查找方法进行优化，而是直接使用的遍历操作，
时间复杂度为O(N)。


{% highlight java %}
{% endhighlight %}


## 参考资料

[JDK文档 之 ArrayList](https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html)
