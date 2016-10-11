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
解释何为注解的最佳方式就是元数据这个词：描述数据自身的数据。
注解就是代码的元数据，他们包含了代码自身的信息，是一种代码级别的说明。
它是JDK1.5及以后版本引入的一个特性，与类、接口、枚举是在同一个层次。
它可以声明在包、类、字段、方法、局部变量、方法参数等的前面，
用来对这些元素进行说明，注释。

注解可以被用在包，类，方法，变量，参数上。
自Java8起，有一种注解几乎可以被放在代码的任何位置，叫做类型注解。
被注解的代码并不会直接被注解影响。**这只会向第三系统提供关于自己的信息以用于不同的需求**。
注解可以被编译至class文件中，而且会在运行时被处理程序提取出来用于业务逻辑。
当然，创建在运行时不可用的注解也是可能的，甚至可以创建只在源文件中可用，
在编译时不可用的注解。

获取注解信息的时候，是根据类的Class信息来获取，不需要有对象实例，
不方便的地方在于，使用注解的时候注解元素内容要在编译之前就要确定，
不能够进行动态赋值。

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
* 编译期间或者部署期间：可使用注解生成代码，XML文件等  
* 运行期间：一些注解可在运行期间执行  



Java之后开始支持类型注解：

{% highlight java %}
// Class instance creation expression:
new @Interned MyObject();

// Type cast:
myString = (@NonNull String) str;

// implements clause:
class UnmodifiableList<T> implements @Readonly List<@Readonly T> { ... }

// Thrown exception declaration:
void monitorTemperature() throws @Critical TemperatureException { ... }
{% endhighlight %}

### 预定义注解
预定义注解有两类，一类是可以直接用于程序代码的注解，另一个是用于定义注解时的注解。

第一类注解，java.lang 包下：

* `@Deprecated`  
* `@Override`  
* `@SuppressWarnings`  
* `@SafeVarargs`  
* `@FunctionalInterface`  

第二类注解，java.lang.annotation 包下，称为元注解（meta-annotations）：  

* `@Retention` 表示注解被如何存储，有如下三个可选项：  

    * `RetentionPolicy.SOURCE` 源码级，被编译器忽略  
    * `RetentionPolicy.CLASS` 文件级，编译期间有效，被JVM忽略  
    * `RetentionPolicy.RUNTIME` 运行期，一直到运行期间都有效      
* `@Documented` 可被 Javadoc tool 工具调用   
* `@Target` 注解可被用于何处：  

    * `ElementType.ANNOTATION_TYPE` 注解类  
    * `ElementType.CONSTRUCTOR` 构造器  
    * `ElementType.FIELD` 变量  
    * `ElementType.LOCAL_VARIABLE` 本地变量  
    * `ElementType.METHOD` 方法  
    * `ElementType.PACKAGE` 包  
    * `ElementType.PARAMETER` 参数  
    * `ElementType.TYPE` Java类  
* `@Inherited` 是否可被子类继承  
* `@Repeatable` 是否支持Repeat  

## 使用范例

定义一个类注解`CustomClassAnnotation`，一个方法注解`CustomMethodAnnotation`，
然后在 `AnnotatedTest` 类中使用此注解，在 `TestMain` 方法中获取注解内容：

{% highlight java %}
/**
 * CustomClassAnnotation Created by Bob on 2016/9/28.
 */
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CustomClassAnnotation {
  String author() default "danibuiza";
  String date();
}


/**
 * CustomMethodAnnotation Created by Bob on 2016/9/28.
 */
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CustomMethodAnnotation {
  String author() default "danibuiza";
  String date();
  String description();
}


/**
 * AnnotatedTest Created by Bob on 2016/9/28.
 */
@CustomClassAnnotation(date = "2016-09-28")
public class AnnotatedTest {
  @CustomMethodAnnotation(date = "2014-06-05", description = "annotated method")
  public String annotatedMethod() {
    return "nothing niente";
  }
  @CustomMethodAnnotation(author = "friend of mine", date = "2014-06-05", description = "annotated method")
  public String annotatedMethodFromAFriend() {
    return "nothing niente";
  }
}


/**
 * TestMain Created by Bob on 2016/9/28.
 */
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class TestMain {
  public static void main(String[] args) throws Exception {

    Class<AnnotatedTest> object = AnnotatedTest.class;
    // Retrieve all annotations from the class
    Annotation[] annotations = object.getAnnotations();
    for (Annotation annotation : annotations) {
      System.out.println(annotation);
    }

    // Checks if an annotation is present
    if (object.isAnnotationPresent(CustomClassAnnotation.class)) {
      // Gets the desired annotation
      Annotation annotation = object.getAnnotation(CustomClassAnnotation.class);
      CustomClassAnnotation customClassAnnotation = (CustomClassAnnotation) annotation;
      String author = customClassAnnotation.author();
      String date = customClassAnnotation.date();
      System.out.println(customClassAnnotation + " => " + author + ", " + date);
    }

    // the same for all methods of the class
    for (Method method : object.getDeclaredMethods()) {
      if (method.isAnnotationPresent(CustomMethodAnnotation.class)) {
        Annotation annotation = method.getAnnotation(CustomMethodAnnotation.class);
        CustomMethodAnnotation customMethodAnnotation = (CustomMethodAnnotation) annotation;
        String author = customMethodAnnotation.author();
        String date = customMethodAnnotation.date();
        String description = customMethodAnnotation.description();
        System.out.println(annotation + " => " + author + ", " + date + ", " + description);
      }
    }
  }
}

{% endhighlight %}


## 参考资料

* [Oracle: Annotations](https://docs.oracle.com/javase/tutorial/java/annotations/)
* [JAVA注解终极指导](http://blog.csdn.net/u013067223/article/details/47860165)

