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
* Streams  
    New stream API to facilitate pipeline processing.
* Optional Class  
    Emphasis on best practices to handle null values properly.
* Nashorn JavaScript  
    A Java-based engine to execute JavaScript code.
* New Date/Time API  
    Improved date time API.
* Base64  

## Lambda Expressions
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



## Functional Interfaces
当接口中只有一个抽象方法（但可以包含一个或一个以上的默认实现方法）的时候，就可以作为函数式接口使用。
作为函数式接口实现有三种方式，第一种是最原始的继承接口的实现类，第二种是lambda表达式，第三种是方法引用。
本节主要讲解前两种。

## 方法引用（Method References）
在lambda表达式章节中可知，在需要特定接口类型参数的地方，就可以使用lambda表达式进行替换。
而部分lambda表达式有一种更简单的书写方式，那就是使用方法引用。
虽然不是所有的lambda表达式都可以使用方法引用替换，但所有的方法引用都可以使用lambda表达式进行替换。
还有一个特点就是方法引用可以直接引用已经存在的实例的方法作为函数式接口的实现，而这个实例方法并不要求
必须继承自函数式接口，任何符合函数式接口定义的方法，都可以被引用。

方法引用有以下三种使用方式：

* 静态方法
* 构造函数
* 实例方法



## Default Methods

## Streams

## Optional Class

## Nashorn JavaScript

## New Date/Time API

## Base64




## 参考资料

* [Java 8 新特性](http://blog.techbeta.me/2014/03/java8-method-reference/)
* [Java 8 官方教程翻译](http://blog.csdn.net/code_for_fun/article/details/42169993)
* [Java8 tutorials point](http://www.tutorialspoint.com/java8/index.htm)

{% highlight Java %}
{% endhighlight %}
