---
layout:     post
title:      Collection框架之ArrayList
date:       2016-08-11 15:15:50 +0800
postId:     2016-08-11-15-15-50
categories: [Collection]
keywords:   [Java, collection]
---

ArrayList 的底层是数组队列，相当于动态数组。与 Java 中的数组相比，它的容量能动态增长。在添加大量元素前，
应用程序可以使用ensureCapacity操作来增加 ArrayList 实例的容量。这可以减少递增式再分配的数量。

由于ArrayList使用的是数组存储结构，所有数组的特性都可以用于此ArrayList集合的操作。
ArrayList非线程安全，如果有多线程共同操作这个list实例，就需要将其加锁，
将锁加在包含这个list的对象上，或者使用如下形式：
```java
List list = Collections.synchronizedList(new ArrayList(...));
```

## 扩容机制

### add
如果不指定List的初始化大小，在初始化的时候会初始化一个空数组，
在添加第一个元素的时候才进行数组的初始化扩容，默认值为10；

```java
// 将指定的元素追加到此列表的末尾。
public boolean add(E e) {
    // 添加元素之前，先调用ensureCapacityInternal方法
    ensureCapacityInternal(size + 1);  // Increments modCount!!
    // Increments modCount!!
    elementData[size++] = e;
    return true;
}
```

### grow
当十个元素位置都被填满，会进行第二次扩容，此次扩容量为当前集合的一半，
(扩容使用的是位移算法，10>>1=5，15>>1=7) 整个过程如下：

> 0 + 10 => 10 + 5 => 15 + 7 => 22 + 11 => 33 ...

扩容代码具体实现如下：

```java
private void grow(int minCapacity) {
  // oldCapacity为旧容量，newCapacity为新容量
  int oldCapacity = elementData.length;
  // 将oldCapacity 右移一位，容量扩大到1.5倍。(位运算的速度远远快于整除运算)
  int newCapacity = oldCapacity + (oldCapacity >> 1);
  // 然后检查新容量是否大于最小需要容量，若还是小于最小需要容量，那么就把最小需要容量当作数组的新容量，
  if (newCapacity - minCapacity < 0)
    newCapacity = minCapacity;
  // 如果新容量大于 MAX_ARRAY_SIZE,进入(执行) `hugeCapacity()` 方法来比较 minCapacity 和 MAX_ARRAY_SIZE
  if (newCapacity - MAX_ARRAY_SIZE > 0)
    newCapacity = hugeCapacity(minCapacity);
  // minCapacity is usually close to size, so this is a win:
  elementData = Arrays.copyOf(elementData, newCapacity);
}

```

所以当数组长度越大的时候，扩容消耗的资源越多；
如果能够在数组初始化的时候大概估算出数组长度上限进行初始化，
就能够减少数组扩容所引起的资源消耗，提升性能。

## 数组拷贝

阅读源码的话，我们就会发现 ArrayList 中大量调用了 `System.arraycopy()` 和 `Arrays.copyOf()` 
这两个方法。比如：我们上面讲的扩容操作以及 add(int index, E element)、toArray() 等方法中都用到了该方法。

### System.arraycopy()

```java
/**
 * @param src 源数组
 * @param srcPos 源数组中的起始位置
 * @param dest 目标数组
 * @param destPos 目标数组中的起始位置
 * @param length 要复制的数组元素的数量
 */
public static native void arraycopy(Object src,  int  srcPos,
                                        Object dest, int destPos,
                                        int length);
```

### Arrays.copyOf()
看两者源代码可以发现 copyOf()内部实际调用了 System.arraycopy() 方法：
```java
public static <T> T[] copyOf(T[] original, int newLength) {
    return (T[]) copyOf(original, newLength, original.getClass());
}
public static <T,U> T[] copyOf(U[] original, int newLength, Class<? extends T[]> newType) {
  @SuppressWarnings("unchecked")
        T[] copy = ((Object)newType == (Object)Object[].class)
  ? (T[]) new Object[newLength]
  : (T[]) Array.newInstance(newType.getComponentType(), newLength);
  System.arraycopy(original, 0, copy, 0,
  Math.min(original.length, newLength));
  return copy;
}
```

## 参考资料

* [JDK文档 之 ArrayList](https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html)
* [ArrayList源码+扩容机制分析](https://snailclimb.gitee.io/javaguide/#/docs/java/collection/ArrayList源码+扩容机制分析)
