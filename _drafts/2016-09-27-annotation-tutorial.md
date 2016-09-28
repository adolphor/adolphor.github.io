---
layout:     post
title:      Annotation 注解
date:       2016-09-27 16:47:28 +0800
postId:     2016-09-27-16-47-28
categories: [Java]
tags:       [Java]
geneMenu:   true
excerpt:    Annotation 注解
---

## 简介

### 规范
最基本的注解形式如下：
{% highlight java %}
@Entity
{% endhighlight %}

上面的 `@` 字符表示告知编译器后面跟的是一个注解，`Entity` 是注解的名称。最常见的比如表示
覆写的Override注解：
{% highlight java %}
@Override
void mySuperMethod() { ... }
{% endhighlight %}

使用注解有如下几种方式：
* 注解可以不包括元素内容，一般用作标记
* 注解包含元素内容的时候，如果只有一个元素，可以省略元素名称
* 可以多个注解同时标注在同一个类（变量/方法等）上
* Java8中支持重复注解（Repeating Annotations）

{% highlight java %}

// 没有元素
@Override
void mySuperMethod() { ... }

// 省略name
@SuppressWarnings(value = "unchecked")
void myMethod() { ... }

@SuppressWarnings("unchecked")
void myMethod() { ... }

// 多个元素
@Author(
   name = "Benjamin Franklin",
   date = "3/27/2003"
)
class MyClass() { ... }

// 多个注解（multiple annotations）  
@Author(name = "Jane Doe")
@EBook
class MyClass { ... }

// Repeating annotations 
@Author(name = "Jane Doe")
@Author(name = "John Smith")
class MyClass { ... }

{% endhighlight %}


### 用途
注解有很多用途，比如：
* 提供编译器所需信息：编译器可以用使用注解来检测错误或忽略警告信息
* 编译期间或者部署期间：可使用注解生成代码，XML文件等。
* 运行期间：一些注解可在运行期间执行



As of the Java SE 8 release, annotations can also be applied to the use of types. 
Here are some examples:

Class instance creation expression:
    new @Interned MyObject();

Type cast:
    myString = (@NonNull String) str;

implements clause:
    class UnmodifiableList<T> implements
        @Readonly List<@Readonly T> { ... }

Thrown exception declaration:
    void monitorTemperature() throws
        @Critical TemperatureException { ... }

### 预定义注解
预定义注解有两类，一类是可以直接用于程序代码的注解，另一个是用于定义注解时的注解。

第一类注解，java.lang 包下：
* @Deprecated 
* @Override
* @SuppressWarnings
* @SafeVarargs
* @FunctionalInterface

第二类注解，java.lang.annotation 包下，称为元注解（meta-annotations）：
* @Retention  
    表示注解被如何存储，有如下三个可选项：  
    * RetentionPolicy.SOURCE      
        源码级，被编译器忽略  
    * RetentionPolicy.CLASS   
        文件级，编译期间有效，被JVM忽略  
    * RetentionPolicy.RUNTIME  
        运行期，一直到运行期间都有效      
* @Documented  
    可被 Javadoc tool 工具调用
* @Target  
    注解可被用于何处：
    * ElementType.ANNOTATION_TYPE  
        注解类  
    * ElementType.CONSTRUCTOR  
        构造器  
    * ElementType.FIELD  
        变量
    * ElementType.LOCAL_VARIABLE  
        本地变量
    * ElementType.METHOD  
        方法
    * ElementType.PACKAGE  
        包
    * ElementType.PARAMETER  
        参数
    * ElementType.TYPE 
        Java类
* @Inherited  
    是否可被子类继承  
* @Repeatable  
    是否支持Repeat

## 注解原理详解


## 参考资料

* [Oracle: Annotations](https://docs.oracle.com/javase/tutorial/java/annotations/)
* [test](test.html)

{% highlight java %}
{% endhighlight %}
