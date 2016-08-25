---
layout:     post
title:      Java8 新特性
date:       2016-08-24 09:00:57 +0800
postId:     2016-08-24-09-00-57
categories: [Java]
tags:       [Java, Java8]
geneMenu:   true
excerpt:    Java8 新特性
---

Java8 新特性主要有一下几个方面：

* lambda表达式（Lambda Expressions）  
    Java中加入了函数式处理功能
* 方法引用（Method References）  
    直接使用方法名称而不是invoking方法进行方法的引用，将方法作为一个参数
* 函数式接口（Functional Interfaces）  
    只有一个抽象方法的接口，可以作为函数式接口
* 默认方法（Default Methods）  
    在接口中有默认实现的方法，相等于抽象类中有抽象接口，有实现方法
* 流操作（Streams）  
    New stream API to facilitate pipeline processing.
* New Date/Time API  
    Improved date time API.
* Base64  

* Optional Class  
    Emphasis on best practices to handle null values properly.
* Nashorn JavaScript  
    A Java-based engine to execute JavaScript code.

## lambda表达式（Lambda Expressions） {#LambdaExpressions}
当接口只定义了一个方法的时候，lambda表达式可以代替接口的实现类。也就是lambda表达式是有返回值的，
这个返回值类型就是被实现的函数式接口的类型。这也是下节所讲的函数式接口可以直接等于一个lambda表达式。
所以，在需要特定接口类型参数的地方，就可以使用lambda表达式进行替换。

### 语法

    parameter -> expression body

* 参数类型声明是可选项，可写可不写
* 如果只有一个参数，圆括号可选
* 如果只有一个表达式，花括号可选
* 如果只有一个返回表达式，return关键字可选

### 作用域


### 范例
*  () -> System.out.println(this)
*  (String str) -> System.out.println(str)
*  str -> System.out.println(str)
*  (String s1, String s2) -> { return s2.length() - s1.length(); }
*  (s1, s2) -> s2.length() - s1.length()


范例代码：

    LambdaExpressionDemo.java

{% highlight Java %}

{% endhighlight %}

运行结果如下：



## 函数式接口（Functional Interfaces） {#FunctionalInterfaces}
当接口中只有一个抽象方法（但可以包含一个或一个以上的默认实现方法）的时候，就可以作为函数式接口使用。

在 Java 8 中定义了很多函数接口供lambda表达式使用，下面是 `java.util.Function` 包中定义的函数接口列表：
TODO 参考：http://www.tutorialspoint.com/java8/java8_functional_interfaces.htm


作为函数式接口实现有三种方式，第一种是最原始的继承接口的实现类，第二种是lambda表达式，第三种是方法引用。
本节主要讲解前两种。


## 方法引用（Method References） {#MethodReferences}
在lambda表达式章节中可知，在需要特定接口类型参数的地方，就可以使用lambda表达式进行替换。
而部分lambda表达式有一种更简单的书写方式，那就是使用方法引用。
虽然不是所有的lambda表达式都可以使用方法引用替换，但所有的方法引用都可以使用lambda表达式进行替换。
还有一个特点就是方法引用可以直接引用已经存在的实例的方法作为函数式接口的实现，而这个实例方法并不要求
必须继承自函数式接口，任何符合函数式接口定义的方法，都可以被引用。

方法引用有以下三种使用方式：

* 静态方法
* 构造函数
* 实例方法



## 默认方法（Default Methods） {#DefaultMethods}

默认方法是 Java8 在接口中引入的一个新的概念，之所以加入这个特性是为了（二进制）向后兼容，
这样，一些原有的接口，就可以使用Java8的lambda表达式功能。比如，‘List’ 和 ‘Collection’ 
接口并没有声明 ‘forEach’ 函数，即便在Java8中添加这个接口定义，也需要在每个继承类中进行实现，
而这个并没有区别，所以可以直接在方法中进行默认实现，这样所有的继承类都不必再进行自己的实现，而
可以直接拿来使用。

参考[Default Methods](http://www.tutorialspoint.com/java8/java8_default_methods.htm)

> Q：在集合框架中有一个Collection的抽象实现，即便没有默认方法功能，也只需要在集合框架接口中声明
之后再抽象实现类中实现即可。而所有的实现类都是此抽象类的子类，所以也并没有增加过多冗余代码。那么，
Java8中引入默认方法的实现是单纯为了Foreach方法的复用吗？


## 流操作（Streams） {#Streams}
流操作提供了一种新的数据处理方式，可以像SQL语句一样的声明方式进行数据的处理。
比如，下面的SQL语句：

    SELECT max(salary), employee_id, employee_name FROM Employee

上面的SQL语句返回了工资最高雇员的详细信息，而开发人员并不需要自己来进行计算和比较。
在Java的集合框架中，开发人员必须使用loop遍历比较每个数据，而如果使用流处理，能够
更加简单高效，而且可以利用计算机的多核性能并发进行。

### 流相关概念

主要有以下几个数据流操作相关的概念：

* Sequence of elements  
    流是一组按照一定顺序排列的特定类型的元素
* Source   
    源可以将集合、数组和 I/O 资源作为输入源
* Aggregate operations  
    流提供了一系列的聚合操作，像 filter, map, limit, reduce, find, match 等
* Pipelining  
    大多数流操作都是在管道（Pipeline）中进行的，这些操作叫做中间操作（intermediate operations），
    他们的功能是加载并处理数据流，之后输出到目标地点。一般将 `collect()` 方法用于数据流操作的末尾，
    标志着数据流处理的结束。
* Automatic iterations  
     流操作提供了数据遍历的功能

获取数据流有如下两个方法：

* stream()  
    此方法返回顺序数据流
* parallelStream()  
    此方法返回并行计算数据流

### 数据流操作方法

范例中的streams定义如下

    List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
    List<String> strings = Arrays.asList("abc", "bc", "efg", "abcd", "jkl");

方法名 | 作用 | 范例
---|---|---
forEach | 遍历数据流 | numbers.stream().forEach(System.out::println);
map | 对数据流数据进行操作 | numbers.stream().map(n -> n * n);
filter | 过滤符合条件的数据 | numbers.stream().filter(n -> n > 3);
limit | 截取特定数量的数据元素 | numbers.stream().limit(4);
sorted | 排序 | numbers.stream((a, b) -> a.compareTo(b)).sorted();
Parallel Processing | numbers.parallelStream().filter(n -> n > 3);
distinct | 排重 |stream.distinct();
Collectors | 集合操作 | strings.stream().collect(Collectors.toList());
Statistics | Statistics | numbers.stream().mapToInt((x) -> x).summaryStatistics();

    TODO: Stream类接口详解

## New Date/Time API {#NewDateTimeAPI}


## Base64 {#Base64}

## Optional Class {#OptionalClass}
见范例，但是好像并没有什么实际卵用……

## Nashorn JavaScript {#NashornJavaScript}
在JVM上运行JS，可以实现Java和JS的互相调用，也没什么卵用……



## 参考资料

* [Java 8 新特性](http://blog.techbeta.me/2014/03/java8-method-reference/)
* [Java 8 官方教程翻译](http://blog.csdn.net/code_for_fun/article/details/42169993)
* [Java8 tutorials point](http://www.tutorialspoint.com/java8/index.htm)
* [Nashorn: JavaScript made great in Java 8](http://www.javaworld.com/article/2144908/scripting-jvm-languages/nashorn--javascript-made-great-in-java-8.html)

{% highlight Java %}
{% endhighlight %}
