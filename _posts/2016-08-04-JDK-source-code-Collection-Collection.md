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

* [官方文档地址](https://docs.oracle.com/javase/8/docs/api/java/util/Collection.html)

## 比较熟悉的接口

### add 
{% highlight java %}
boolean add(E e)
{% endhighlight %}

新增元素到集合，对于是否支持null值，按照需要在实现类中自行控制。

新增成功返回true，新增失败返回false。

### addAll
{% highlight java %}
boolean addAll(Collection<? extends E> c)
{% endhighlight %}

将参数中包含的所有元素添加到集合。

新增成功返回true，新增失败返回false。


### remove 
{% highlight java %}
boolean remove(Object o)
{% endhighlight %}

从集合中删除元素，如果存在至少一个相等的元素，就为true。

删除成功返回true，删除失败返回false。

### removeAll
{% highlight java %}
boolean removeAll(Collection<?> c)
{% endhighlight %}

将参数中包含的所有元素从集合中删除（如果集合中存在的话）

### clear
{% highlight java %}
void clear()
{% endhighlight %}

将集合清空

### isEmpty 
{% highlight java %}
boolean isEmpty()
{% endhighlight %}

集合是否为空

### size
{% highlight java %}
int size()
{% endhighlight %}

集合当前含有的元素的个数


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

### equals
{% highlight java %}
boolean equals(Object o)
{% endhighlight %}

此接口用于判断两个集合是否相等：只有集合和参数具有相同的元素的个数，而且在对应的元素相等（equals），
才能称这两个集合相等。

另外，要注意集合中的元素的equals方法是否被覆写，如果没有被覆写，按照默认equels方法进行判断：
基本数据类型比较值是否相等；引用对象使用equals方法判断否相等。

{% highlight java %}

import java.util.ArrayList;
import org.junit.Assert;

/**
 * Created by Bob on 2016/8/5.
 */
public class EqualsDemo {
  public static void main(String[] args) {
    // 基本数据类型
    ArrayList<String> temp1 = new ArrayList<>();
    temp1.add("A");
    ArrayList<String> temp2 = new ArrayList<>();
    temp2.add("A");
    temp2.add("B");
    boolean equals = temp1.equals(temp2);
    Assert.assertFalse(equals);
    temp1.add("B");
    equals = temp1.equals(temp2);
    Assert.assertTrue(equals);

    // 引用对象类型
    // —— 没有覆写元素equals方法的情形
    Teacher teacher1 = new Teacher(110);
    Teacher teacher2 = new Teacher(110);

    ArrayList<Teacher> teachers11 = new ArrayList<>();
    ArrayList<Teacher> teachers12 = new ArrayList<>();
    ArrayList<Teacher> teachers2 = new ArrayList<>();
    teachers11.add(teacher1);
    teachers12.add(teacher1);

    equals = teachers11.equals(teachers12);     // true：同一个对象放入两个不同的集合
    Assert.assertTrue(equals);
    equals = teachers11.equals(teacher2);       // false：不同对象放入不同的集合，虽然cardNum相同，但是仍然不相等
    Assert.assertFalse(equals);

    // —— 覆写元素equals方法的情形
    ArrayList<Student> students11 = new ArrayList<>();
    ArrayList<Student> students12 = new ArrayList<>();
    ArrayList<Student> students2 = new ArrayList<>();
    Student student1 = new Student(1);
    Student student2 = new Student(1);

    students11.add(student1);
    students12.add(student1);
    students2.add(student2);

    equals = students11.equals(students12);     // true
    Assert.assertTrue(equals);

    equals = students11.equals(students2);         // true
    Assert.assertTrue(equals);

  }
}

/**
 * 不覆写equals方法
 */
class Teacher {
  private Integer cardNum;

  Teacher(Integer cardNum) {
    this.cardNum = cardNum;
  }
}

/**
 * 覆写equals方法
 */
class Student {
  private Integer cardNum;

  Student(Integer cardNum) {
    this.cardNum = cardNum;
  }

  @Override
  public boolean equals(Object object) { // 学号相同就认为是同一个学生
    Student student = (Student) object;
    if (this.cardNum == student.cardNum)
      return true;
    else
      return false;
  }
}
{% endhighlight %}


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

## 参考文档

[JDK文档 之 Collection](https://docs.oracle.com/javase/8/docs/api/java/util/Collection.html)



