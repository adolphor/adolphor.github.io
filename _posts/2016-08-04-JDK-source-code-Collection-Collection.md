---
layout:     post
title:      【JDK8源码阅读】Collection框架之Collection
date:       2016-08-04 16:29:34 +0800
postId:     2016-08-04-16-29-34
categories: [JAVA8]
tags:       [JAVA8, Collection]
geneMenu:   true
---
## Collection

`Collection`集合的继承以及各个实现类之间的关系，参考[【JDK8源码阅读】Collection框架总述]({{ site.url }}),
本文主要讲解各个接口定义以及用途。

## 比较熟悉的接口

### add 
{% highlight java %}
boolean add(E e)
{% endhighlight %}

{% highlight java %}
{% endhighlight %}

新增元素到集合，对于是否支持null值，按照需要在实现类中自行控制。

### addAll
{% highlight java %}
boolean addAll(Collection<? extends E> c)
{% endhighlight %}

{% highlight java %}
{% endhighlight %}
将参数中包含的所有元素添加到集合

### remove 
{% highlight java %}
boolean remove(Object o)
{% endhighlight %}

{% highlight java %}
{% endhighlight %}
从集合中删除元素，如果存在至少一个相等的元素，就为true。


### removeAll
{% highlight java %}
boolean removeAll(Collection<?> c)
{% endhighlight %}

{% highlight java %}
{% endhighlight %}
将参数中包含的所有元素从集合中删除（如果集合中存在的话）

### clear
{% highlight java %}
void clear()
{% endhighlight %}

### isEmpty 
{% highlight java %}
boolean isEmpty()
{% endhighlight %}

### size
{% highlight java %}
int size()
{% endhighlight %}

### equals
{% highlight java %}
boolean equals(Object o)
{% endhighlight %}

### contains
{% highlight java %}
boolean contains(Object o)
{% endhighlight %}

### containsAll
{% highlight java %}
boolean containsAll(Collection<?> c)
{% endhighlight %}

{% highlight java %}
{% endhighlight %}
集合中是否包含参数集合中的所有元素

### iterator
{% highlight java %}
Iterator<E> iterator()
{% endhighlight %}

### toArray
{% highlight java %}
Object[] toArray()
{% endhighlight %}


## 比较陌生的接口

### removeIf
{% highlight java %}
default boolean removeIf(Predicate<? super E> filter)
{% endhighlight %}

此接口是java8新特性

{% highlight java %}
{% endhighlight %}

mark TODO

### retainAll
{% highlight java %}
boolean retainAll(Collection<?> c)
{% endhighlight %}

集合中的对象如果不在参数c中就进行删除操作，最后集合中保留下来的元素是参数c的子集。

如果方法调用过程中删除了集合中的至少一个元素，返回true；如果没有删除任何一个元素，返回false。

{% highlight java %}
import java.util.ArrayList;

public class RetainAllDemo {
  public static void main(String[] args) {
    ArrayList<String> temp1 = new ArrayList<>();
    temp1.add("A");
    temp1.add("B");
    temp1.add("C");
    ArrayList<String> temp2 = new ArrayList<>();
    temp2.add("A");
    temp2.add("B");

    boolean result = temp1.retainAll(temp2);  // true：删除元素C
    System.out.println(result);
    System.out.println("temp1：" + temp1);
    System.out.println("temp2：" + temp2);

    result = temp1.retainAll(temp2);          // false：没有需要移除的元素
    System.out.println(result);
    System.out.println("temp1：" + temp1);
    System.out.println("temp2：" + temp2);

    temp1.remove("A");
    temp1.add("D");
    result = temp1.retainAll(temp2);          // true：删除元素D，temp2中的A对集合中的元素没有影响
    System.out.println(result);
    System.out.println("temp1：" + temp1);
    System.out.println("temp2：" + temp2);
  }
}
{% endhighlight %}

### hashCode
{% highlight java %}
int hashCode()
{% endhighlight %}

### spliterator
{% highlight java %}
default Spliterator<E> spliterator()
{% endhighlight %}

### stream
{% highlight java %}
default Stream<E> stream()
{% endhighlight %}

since 1.8

### parallelStream
{% highlight java %}
default Stream<E> parallelStream()
{% endhighlight %}

### toArray
{% highlight java %}
<T> T[] toArray(T[] a)
{% endhighlight %}
