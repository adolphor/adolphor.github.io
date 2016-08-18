---
layout:     post
title:      【Java8源码阅读笔记】Collection框架之Collection
date:       2016-08-04 16:29:34 +0800
postId:     2016-08-04-16-29-34
categories: [Collection]
tags:       [Collection]
geneMenu:   true
excerpt:    【Java8源码阅读笔记】Collection框架之Collection
---
## Collection

`Collection`集合的继承以及各个实现类之间的关系，参考[【Java8源码阅读】Collection框架总述](/collection/2016/08/03/JDK-source-code-Collection.html),
本文主要讲解各个接口定义以及用途。

## 接口定义


### boolean add(E e) 

新增元素到集合，对于是否支持null值，按照需要在实现类中自行控制。

新增成功返回true，新增失败返回false。

### boolean addAll(Collection<? extends E> c)

将参数中包含的所有元素添加到集合。

新增成功返回true，新增失败返回false。

### boolean remove(Object o) 

从集合中删除元素，如果存在至少一个相等的元素，就为true。

删除成功返回true，删除失败返回false。

### boolean removeAll(Collection<?> c)

将参数中包含的所有元素从集合中删除（如果集合中存在的话）

### boolean retainAll(Collection<?> c)

集合中的对象如果不在参数c中就进行删除操作，最后集合中保留下来的元素是参数c的子集。

如果方法调用过程中删除了集合中的至少一个元素，返回true；如果没有删除任何一个元素，返回false。

    RetainAllDemo.java

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

### void clear()

将集合清空


### int hashCode()

此hashCode方法，是计算整个集合的hashCode，本方法使用较少。

{% highlight java %}
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bob on 2016/8/8.
 */
public class HashCodeDemo {
  public static void main(String[] args) {

    List<String> list = new ArrayList<>();
    int hashCode = list.hashCode();
    System.out.println(hashCode);

    list.add("A");
    hashCode = list.hashCode();
    System.out.println(hashCode);
  }
}
{% endhighlight %}


### boolean equals(Object o)

此接口用于判断两个集合是否相等：只有集合和参数具有相同的元素的个数，而且在对应的元素相等（equals），
才能称这两个集合相等。

另外，要注意集合中的元素的equals方法是否被覆写，如果没有被覆写，按照默认equels方法进行判断：
基本数据类型比较值是否相等；引用对象使用equals方法判断否相等（范例：Teacher.java）。
如果equals方法有被覆写，则按照覆写之后的规则判断两个对象是否相等（范例：Student.java）。

    EqualsDemo.java
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
  public boolean equals(Object o) {
    if (o == null)
      return false;
    if (!(o instanceof Student))
      return false;
    Student other = (Student) o;
    boolean result = this.cardNum == other.cardNum; // 学号相同就认为是同一个学生
    return result;
  }

  // 如果覆写了equals方法，一定要同时进行hashCode方法的覆写
  // 否则，对于hash表存储类型的数据，会有潜在错误
  @Override
  public int hashCode() {
    return this.cardNum;
  }
}
{% endhighlight %}

### boolean contains(Object o)
是否包含参数元素

### boolean containsAll(Collection<?> c)
集合中是否包含参数集合中的所有元素

### boolean isEmpty() 

集合是否为空

### int size()

集合当前含有的元素的个数

### Object[] toArray()

将集合转换为数组

### <T> T[] toArray(T[] a)

将集合转换为指定数据类型的数组

### Iterator<E> iterator()
集合遍历封装类

### default Spliterator<E> spliterator()

Java8 TODO

### default boolean removeIf(Predicate<? super E> filter)

Java8 TODO

### default Stream<E> stream()

Java8 TODO

### default Stream<E> parallelStream()

Java8 TODO

## 接口中的默认方法
Java8之后，还有一个新特性，就是接口中支持默认方法实现：
{% highlight java %}
public interface  {
  void method1();
  default void method2() {
    System.out.println("I'm method2 from interface.");
  }
}
public class DefaultMehtodImplements implements DefaultMethodOfInterface {
  @Override
  public void method1() {
    System.out.println("I'm method1 from implement.");
  }
}
public class Demo {
  public static void main(String[] args) {
    DefaultMehtodImplements dmi = new DefaultMehtodImplements();
    dmi.method1();
    dmi.method2();
  }
}
{% endhighlight %}

TODO 问题：默认方法相比于抽象实现类，有点在哪里？
* 继承的单一性限制

## 参考资料

* [JDK文档 之 Collection](https://docs.oracle.com/javase/8/docs/api/java/util/Collection.html)



