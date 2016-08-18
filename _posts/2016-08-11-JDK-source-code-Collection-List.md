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
此接口下有`ArrayList`, `LinkedList`, `Vector`, `Stack` 四个实现类。

List允许重复元素，允许空值null，

## List独有接口
List继承自Collection，对于Collection接口之外，还有自己的独有接口。

### add
{% highlight java %}
void add(int index, E element);
{% endhighlight %}

### addAll (index, c)
{% highlight java %}
boolean addAll(int index, Collection<? extends E> c);
{% endhighlight %}

### set (index, ele)
{% highlight java %}
E set(int index, E element);
{% endhighlight %}

### remove
{% highlight java %}
E remove(int index);
{% endhighlight %}

### replaceAll
{% highlight java %}
default void replaceAll(UnaryOperator<E> operator);
{% endhighlight %}

### sort
{% highlight java %}
default void sort(Comparator<? super E> c);
{% endhighlight %}

### get
{% highlight java %}
E get(int index);
{% endhighlight %}

### indexOf
{% highlight java %}
int indexOf(Object o);
{% endhighlight %}

### lastIndexOf
{% highlight java %}
int lastIndexOf(Object o);
{% endhighlight %}

### listIterator
{% highlight java %}
ListIterator<E> listIterator();
{% endhighlight %}

### listIterator (int)
{% highlight java %}
ListIterator<E> listIterator(int index);
{% endhighlight %}

### subList
{% highlight java %}
List<E> subList(int fromIndex, int toIndex);
{% endhighlight %}

## 覆写Collection的接口
下面这些接口在`Collection`中都已经定义：
{% highlight java %}
int size();
boolean isEmpty();
boolean contains(Object o);
Iterator<E> iterator();
Object[] toArray();
<T> T[] toArray(T[] a);
boolean add(E e);
boolean remove(Object o);
boolean containsAll(Collection<?> c);
boolean addAll(Collection<? extends E> c);
boolean removeAll(Collection<?> c);
boolean retainAll(Collection<?> c);
void clear();
boolean equals(Object o);
int hashCode();
default Spliterator<E> spliterator();
{% endhighlight %}

## 未覆写接口
Java8相关新增接口基本上都没有覆写

## 遗留问题

* Collection中已经定义的接口为什么重新定义一遍？出于什么样的考虑，有何意义？

## 参考资料

[JDK文档 之 List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)




