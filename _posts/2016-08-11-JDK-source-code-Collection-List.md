---
layout:     post
title:      【Java8源码阅读笔记】Collection框架之List
date:       2016-08-11 10:14:15 +0800
postId:     2016-08-11-10-14-15
categories: [Collection]
tags:       [Collection, List]
geneMenu:   true
excerpt:    【Java8源码阅读笔记】Collection框架之List
---
## List
`List`接口是List集合分支的中接口定义，所有的List实现类都直接或间接实现此接口，而此接口继承自`Collection`，
此接口下有 `ArrayList`, `LinkedList`, `Vector`, `Stack` 四个实现类。

List允许重复元素，允许空值null，

## List独有接口
List继承自Collection，除了继承自Collection的接口之外，还有自己的独有接口。

### void add(int index, E element)
定点位置插入元素

### boolean addAll(int index, Collection<? extends E> c)
定点位置插入元素集合

### E set(int index, E element)
修改定点位置元素

### default void replaceAll(UnaryOperator<E> operator)
since 1.8，Java8新特性，TODO

### E remove(int index)
删除定点位置元素

### List<E> subList(int fromIndex, int toIndex)
获取list的子集

### int indexOf(Object o)
查找当前元素下标

### int lastIndexOf(Object o)
查找当前等于当前元素的最后一个位置，说明list允许重复。

### default void sort(Comparator<? super E> c)
since 1.8，
{% highlight java %}
default void sort(Comparator<? super E> c) {
    Object[] a = this.toArray();
    Arrays.sort(a, (Comparator) c);
    ListIterator<E> i = this.listIterator();
    for (Object e : a) {
        i.next();
        i.set((E) e);
    }
}
{% endhighlight %}


### E get(int index)
获取定点位置元素

### ListIterator<E> listIterator()
遍历

### ListIterator<E> listIterator(int index)
定点位置开始遍历

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

## 遗留问题

* Collection中已经定义的接口为什么重新定义一遍？出于什么样的考虑，有何意义？

## 参考资料

* [JDK文档 之 List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)


{% highlight java %}
{% endhighlight %}


