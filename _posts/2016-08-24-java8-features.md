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
* Method References  
    Referencing functions by their names instead of invoking them directly. Using functions as parameter.    
* Functional Interfaces

* Default Methods  
    Interface to have default method implementation.
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
当接口只定义了一个方法的时候，lambda表达式可以代替接口的实现类。

### 语法

    parameter -> expression body

* 参数类型声明是可选项，可写可不写
* 如果只有一个参数，圆括号可选
* 如果只有一个表达式，花括号可选
* 如果只有一个返回表达式，return关键字可选

### 范例
*  () -> System.out.println(this)
*  (String str) -> System.out.println(str)
*  str -> System.out.println(str)
*  (String s1, String s2) -> { return s2.length() - s1.length(); }
*  (s1, s2) -> s2.length() - s1.length()


范例代码：

    LambdaExpressionDemo.java

{% highlight Java %}
/**
 * Created by Bob on 2016/1/22.
 */
interface MathOperation {
  int operation(int a, int b);
}

class Operate {
  public int execute(int a, int b, MathOperation mathOperation) {
    return mathOperation.operation(a, b);
  }
}

interface GreetingService {
  void sayMessage(String message);
}

public class LambdaExpressionDemo {

  public static void main(String args[]) {

    /**
     * 范例1：省略实现继承类
     */
    GreetingService greetService2 = (message) -> System.out.println("Hello " + message);    // 带有圆括号
    GreetingService greetService1 = message -> System.out.println("Hello " + message);    // 省略圆括号

    greetService1.sayMessage("Mahesh");
    greetService2.sayMessage("Suresh");

    /**
     * 范例2：（这个应该是服务注册的实现吧）
     */
    // 分别实现如下4个接口：
    MathOperation addition = (int a, int b) -> a + b;    // 带有参数类型声明
    MathOperation subtraction = (a, b) -> a - b;    // 省略参数类型声明
    MathOperation multiplication = (a, b) -> { return a * b; };    // 带有return关键字
    MathOperation division = (a, b) -> a / b;    // 省略return关键字

    Operate operate = new Operate();

    // 2.1、调用范例
    System.out.println("10 + 5 = " + operate.execute(10, 5, addition));
    System.out.println("10 - 5 = " + operate.execute(10, 5, subtraction));
    System.out.println("10 x 5 = " + operate.execute(10, 5, multiplication));
    System.out.println("10 / 5 = " + operate.execute(10, 5, division));

    // 2.2、也可以使用匿名实现的方式
    System.out.println("10 + 5 = " + operate.execute(10, 5, (a, b) -> a + b));
    System.out.println("10 - 5 = " + operate.execute(10, 5, (a, b) -> a - b));
    System.out.println("10 x 5 = " + operate.execute(10, 5, (a, b) -> a * b));
    System.out.println("10 / 5 = " + operate.execute(10, 5, (a, b) -> a / b));

  }
}
{% endhighlight %}

运行结果如下：

    Hello Mahesh
    Hello Suresh
    10 + 5 = 15
    10 - 5 = 5
    10 x 5 = 50
    10 / 5 = 2
    10 + 5 = 15
    10 - 5 = 5
    10 x 5 = 50
    10 / 5 = 2



## Functional Interfaces
当接口中只有一个抽象方法的时候，就可以作为函数式接口使用，
但可以包含一个或一个以上的默认实现方法。

## 方法引用（Method References）

方法引用有以下三种使用方式：

* 静态方法
* 实例方法
* 构造函数初始化




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
