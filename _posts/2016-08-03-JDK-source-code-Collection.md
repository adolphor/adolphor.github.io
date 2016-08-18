---
layout:     post
title:      【Java8源码阅读笔记】Collection框架总述
date:       2016-08-03 21:08:24 +0800
postId:     2016-08-03-21-08-24
categories: [Collection]
tags:       [Collection]
excerpt:    【Java8源码阅读笔记】Collection框架总述
---

## Collection总类图

### 集合实现类


Interface   |   Hash Table  |   Resizable Array |   Balanced Tree   |   Linked List     |   Hash Table + Linked List
----|----|----|----|----|----
Set         |   HashSet     |                   |  TreeSet          |                   |   LinkedHashSet   
List        |               |   ArrayList       |                   |   LinkedList      | 
Deque       |               |   ArrayDeque      |                   |   LinkedList      |    
Map         |   HashMap     |                   |  TreeMap          |                   |   LinkedHashMap

### 图示
先看下Java8中Collection各继承和实现关系图，先看下精简之后的关系图：

![Collection类图](/image/post/2016/08/03/20160803-Collection01.png)

再看下稍微补充抽象继承类之后的关系图：

![Collection类图](/image/post/2016/08/03/20160803-Collection02.png)

下面是Collection集合类的全部关系图：

![Collection类图](/image/post/2016/08/03/20160803-Collection03.png)


## 公共接口

### Iterable 接口

> [java.lang.Iterable](https://docs.oracle.com/javase/8/docs/api/java/lang/Iterable.html)

官方文档说 “Implementing this interface allows an object to be the target of the "for-each loop" statement.”
也就是实现此接口的对象，可以使用foreach语句进行对象的遍历。
有关foreach语句的使用方法参考：[for-each](https://docs.oracle.com/javase/8/docs/technotes/guides/language/foreach.html)

多说一点，对于foreach语句，同样可以用于数组的遍历。

### Collection 接口

> [java.util.Collection](https://docs.oracle.com/javase/8/docs/api/java/util/Collection.html)

虽然 `Collection` 继承 `Iterable` 接口，但是前者才是 <b>集合</b> 框架的在概念上的根接口。JDK并没有提供`Collection`的直接实现类，
只提供了一些继承此接口的特殊子接口，像`Set`和`List`。所有这些通用的`Collection`实现类，都应该实现两个“标准”构造器：一个
无参构造器，调用此构造器的时候会创建一个空集合；一个含有一个`Collection`类型参数的构造器，会创建一个和此参数元素相同的集合。

Collection接口具体定义，参考[【Java8源码阅读笔记】Collection框架之Collection](/collection/2016/08/04/JDK-source-code-Collection-Collection.html)

### AbstractCollection 抽象类
此抽象类实现了`Collection`接口中的大部分接口定义，只需要在子类实现中实现`iterator`和`size`方法即可。
但是需要注意的一点是，iterator方法的返回對象必须实现`hasNext`和`next`方法。


AbstractCollection具体实现，参考[【Java8源码阅读笔记】Collection框架之AbstractCollection](/collection/2016/08/08/JDK-source-code-Collection-AbstractCollection.html)

## List 族群

### List 接口

### AbstractList 抽象类

### ArrayList 实现类

### LinkedList 实现类

### Vector 实现类

### Stack 实现类

## Set 族群

### Set 接口

### AbstractSet 抽象类

### HashSet 实现类

### LinkedHashSet 实现类

### TreeSet 实现类

### EnumSet 抽象类

[EnumSet参考资料1](http://www.cnblogs.com/accessking/p/4200000.html)  
[EnumSet参考资料2](http://blog.csdn.net/hudashi/article/details/6943843/)

## 其他相关类

### Iterator接口


## 参考资料

* [The Collections Framework](https://docs.oracle.com/javase/8/docs/technotes/guides/collections/index.html)  
* [Collections API](https://docs.oracle.com/javase/8/docs/api/java/util/package-summary.html)  

