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

* [函数式接口（Functional Interfaces）](#FunctionalInterfaces)  
* [lambda表达式（Lambda Expressions）](#LambdaExpressions)  
* [方法引用（Method References）](#MethodReferences)  
* [默认方法（Default Methods）](#DefaultMethods)  
* [流操作（Streams）](#Streams)  
* [New Date/Time API](#NewDateTimeAPI)  
* [Base64](#Base64)  
* [Optional Class](#OptionalClass)  
* [Nashorn JavaScript](#NashornJavaScript)  

## lambda表达式（Lambda Expressions） {#LambdaExpressions}
lambda 表达式是 Java8 中引入的最重要的一个概念之一，使得 Java 拥有了进行函数式编程的能力。

### 简介
lambda 表达式就是一个匿名函数，Java8中可以使用lambda语法来代替匿名的内部类，先看一个范例：
首先定义一个String类型的List变量：

    List<String> names = Arrays.asList("Mahesh", "Suresh", "Ramesh", "Naresh", "Kalpesh");

如果要对这个list排序，按照以前Java7版本的JDK，需要使用 `Comparator` 接口，对于接口我们一般的使用方法是继承接口，
实现自己的实现类，则有如下的实现类：

    class MyComparator implements Comparator<String> {
      @Override
      public int compare(String a, String b) {
        return a.compareTo(b);
      }
    }

比较的时候传入 `Comparator` 实例对象:
    
    // 初始化实例
    Comparator<String> myComparator = new MyComparator();
    Collections.sort(names, myComparator);
    // 匿名方式
    Collections.sort(names, new MyComparator());

简单一些的话，不需要定义具体的实现类，只需要在实例化接口的时候覆写接口内的抽象方法即可：

    Collections.sort(names, new Comparator<String>() {
      @Override
      public int compare(String a, String b) {
        return b.compareTo(a);
      }
    });

上面使用了一个匿名内部实现类，复写了compare方法，这是Java8之前进行排序时最简单的实现方式。
下面使用lambda表达式的方式实现：

    Collections.sort(names, (String a, String b) -> {
      return b.compareTo(a);
    });

使用 `(a, b) -> b.compareTo(a)` 代替了匿名内部类，这就是lambda表达式的使用方式。
下面会对此方法更进一步进行简化。


### 语法

参数列表 | 表达式符号 | 函数体
---|---|---
parameter | -> | expression body


* 参数类型声明是可选项，可写可不写，Java8可以自动进行类型推断
* 如果只有一个参数，圆括号可选
* 如果只有一个表达式，花括号可选
* 如果只有一个返回表达式，return关键字可选

形如下面的几种用法：  

*  () -> System.out.println(this)
*  (String str) -> System.out.println(str)
*  str -> System.out.println(str)
*  (String s1, String s2) -> { return s2.length() - s1.length(); }
*  (s1, s2) -> s2.length() - s1.length()

我们对上面的排序代码继续进行简化，因为它只有一个返回表达式，所以可以省略 return 和 大括号，简写为：
    
    Collections.sort(names, (String a, String b) -> b.compareTo(a));
    
又 lambda 可以自动进行类型推断，所以可以省略参数类型，更进一步简写为：

    Collections.sort(names, (a, b) -> b.compareTo(a));

至此，已经将排序方法代码由四行简化为一行。

### 目标类型（target type）

既然可以使用lambda表达式替换匿名内部类，那么lambda表达式就具有匿名内部类的特性，
而匿名内部类是具有特定的接口(对象)类型的，那么lambda表达式也应该具有特定的目标类型（target type）。
比如上面的排序方法：

    Collections.sort(names, new Comparator<String>() {
      @Override
      public int compare(String a, String b) {
        return b.compareTo(a);
      }
    });

此匿名内部类的类型是 `Comparator`：

    Comparator<String> comparator = new Comparator<String>() {
      @Override
      public int compare(String a, String b) {
        return b.compareTo(a);
      }
    };
    
那么对于上述排序方法处使用的lambda表达式的 target type 同样也是 `Comparator`：

    Comparator<String> comparator = (a, b) -> a.compareTo(b);

也就是说，上面排序方法需要的只是 `Comparator` 接口类的实现，至于是自定义继承类实现实例，还是使用接口实例化，
或者使用lambda表达式，效果都是等价的，都是为了进行如下的动作：

    Collections.sort(names, comparator);

但lambda表达式肯定不是只用于排序，那么

### 作用域


### 范例


范例代码：

    LambdaExpressionDemo.java

{% highlight Java %}
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Bob on 2016/8/24.
 */
public class LambdaExpressionDemo {
  public static void main(String[] args) {

    List<String> names = Arrays.asList("Mahesh", "Suresh", "Ramesh", "Naresh", "Kalpesh");

    /**
     * 原始实现方式
     */
    Collections.sort(names, new Comparator<String>() {
      @Override
      public int compare(String a, String b) {
        return b.compareTo(a);
      }
    });

    /**
     * lambda表达式方式，以下三种方式是等价的
     */
    Collections.sort(names, (String a, String b) -> {  // 带有参数类型声明 和 return 关键字
      return b.compareTo(a);
    });
    Collections.sort(names, (String a, String b) -> b.compareTo(a));  // 省略return关键字
    Collections.sort(names, (a, b) -> b.compareTo(a)); // 省略参数类型声明

    /**
     * 使用lambda表达式进行遍历
     */
    names.forEach(name -> System.out.println(name));


    /**
     * 范例：没有返回值
     */
    GreetingService greetService2 = (message) -> System.out.println("Hello " + message);    // 带有圆括号
    GreetingService greetService1 = message -> System.out.println("Hello " + message);    // 省略圆括号

    greetService1.sayMessage("Mahesh");
    greetService2.sayMessage("Suresh");

    /**
     * 范例：有返回值
     */
    Converter<String, Integer> converter = source -> Integer.valueOf(source); // 只有一个参数，省略圆括号
    Integer integer = converter.convert("123");
    System.out.println(integer);

  }
}

@FunctionalInterface
interface GreetingService {
  void sayMessage(String message);
}

@FunctionalInterface
interface Converter<V, T> {
  T convert(V v);
}

{% endhighlight %}


## 函数式接口（Functional Interfaces） {#FunctionalInterfaces}

### 什么是函数式接口
简单来说，函数式接口是只包含一个抽象方法（但可以包含一个或一个以上的默认实现方法）的接口。比如 Java 标准库中的
 `java.lang.Runnable` 和 `java.util.Comparator` 
都是典型的函数式接口。对于函数式接口，除了可以使用 Java 中标准的方法来创建实现对象之外，还可以使用 lambda 表达式来创建
实现对象。这可以在很大程度上简化代码的实现。在使用 lambda 表达式时，只需要提供形式参数和方法体。由于函数式接口只有一个
抽象方法，所以通过 lambda 表达式声明的方法体就肯定是这个唯一的抽象方法的实现，而且形式参数的类型可以根据方法的类型声明
进行自动推断。


作为函数式接口实现有三种方式，第一种是最原始的继承接口的实现类，第二种是lambda表达式，第三种是方法引用。
本节主要讲解前两种。


在 Java 8 中定义了很多函数接口供 lambda 表达式使用，下面是 `java.util.Function` 包中定义的函数接口列表：
TODO 参考：http://www.tutorialspoint.com/java8/java8_functional_interfaces.htm


范例代码：

    FunctionalInterfaceDemo.java

{% highlight Java %}
/**
 * Created by Bob on 2016/1/22.
 */
public class FunctionalInterfaceDemo {

  public static void main(String args[]) {

    /**
     * 范例：
     */
    // 分别实现如下4个函数式接口：
    MathOperation addition = (a, b) -> a + b;
    MathOperation subtraction = (a, b) -> a - b;
    MathOperation multiplication = (a, b) -> a * b;
    MathOperation division = (a, b) -> a / b;

    // 1、调用范例
    System.out.println("10 + 5 = " + addition.operate(10, 5));
    System.out.println("10 - 5 = " + subtraction.operate(10, 5));
    System.out.println("10 x 5 = " + multiplication.operate(10, 5));
    System.out.println("10 / 5 = " + division.operate(10, 5));

    TestMathOperation testOperation = new TestMathOperation();
    // 2、像实现类实例一样，函数式接口实现也可以作为参数传递，之后再进行调用
    System.out.println("10 + 5 = " + testOperation.operateResult(10, 5, addition));
    System.out.println("10 - 5 = " + testOperation.operateResult(10, 5, subtraction));
    System.out.println("10 x 5 = " + testOperation.operateResult(10, 5, multiplication));
    System.out.println("10 / 5 = " + testOperation.operateResult(10, 5, division));

    // 3、也可以使用匿名实现的方式
    System.out.println("10 + 5 = " + testOperation.operateResult(10, 5, (a, b) -> a + b));
    System.out.println("10 - 5 = " + testOperation.operateResult(10, 5, (a, b) -> a - b));
    System.out.println("10 x 5 = " + testOperation.operateResult(10, 5, (a, b) -> a * b));
    System.out.println("10 / 5 = " + testOperation.operateResult(10, 5, (a, b) -> a / b));
  }

}

@FunctionalInterface
interface MathOperation {

  // 只能含有一个抽象方法
  int operate(int a, int b);

  // 但是可以含有默认的实现方法
  default void other(int num) {
    System.out.println("num is: " + num);
  }
}

// 其实这个是回调方法的实现
class TestMathOperation {
  public int operateResult(int a, int b, MathOperation operation) {
    return operation.operate(a, b);
  }
}

{% endhighlight %}

运行结果如下：

    10 + 5 = 15
    10 - 5 = 5
    10 x 5 = 50
    10 / 5 = 2

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


范例代码：

    LambdaExpressionDemo.java

{% highlight Java %}

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Bob on 2016/8/24.
 */

public class MethodReferenceDemo {
  public static void main(String[] args) {
    /**
     * 范例1
     */
    // lambda表达式：作为函数式接口实现参考上节范例
    Converter<String, Integer> converter = Integer::valueOf;  // 方法引用：调用静态方法，作为函数式接口实现
    Integer i = converter.convert("123");
    System.out.println(i);

    /**
     * 范例2
     */
    List<String> names = Arrays.asList("Mahesh", "Suresh", "Ramesh", "Naresh", "Kalpesh");
    Collections.sort(names, (a, b) -> a.compareTo(b)); // lambda表达式进行排序
    names.forEach((name) -> System.out.println(name));    // 前面讲过，lambda表达式方式进行遍历的方式
    names.forEach(System.out::println); // 方法引用：调用系统静态方法进行遍历

    /**
     * 范例3
     */
    PersonFactory<Person> factory1 = (name, age) -> new Person(); // lambda表达式
    Person person1 = factory1.create("Person1", 28);
    person1.eat();

    PersonFactory<Person> factory2 = Person::new;  // 方法引用：构造函数
    Person person2 = factory2.create("Person2", 28);
    person2.eat();

    /**
     * 范例4
     */
    Something something = new Something();
    OperateUtil<String, String> startsUtil = something::startsWith;
    OperateUtil<String, String> endsUtil = something::endsWith;
    TestOperateUtil testOperate = new TestOperateUtil();
    System.out.println("Java starts with: " + testOperate.oerateResult(startsUtil, "Java"));
    System.out.println("Java ends with: " + testOperate.oerateResult(endsUtil, "Java"));
    // 相当于此接口的实现类调用自身的方法用作函数式接口的实现，只是这里的实现类和函数式接口之间并没有继承关系，
    // 只要符合参数类型，任何函数式接口都可以作为此实例方法的引用，而函数式接口实例对此实例方法本身没有任何影响
    // 比如，此实例方法甚至可以使用 Converter 函数式接口进行接收和引用
    Converter<String, String> startsWith = something::startsWith;
    // 只不过换了一个方法名，也就是将一个实例方法抽象化为另一个接口类型的实现了
    System.out.println("Java starts with: " + startsWith.convert("Java"));

  }
}

@FunctionalInterface
interface Converter<V, T> {
  T convert(V v);
}

class Person {
  public String name;
  public int age;

  Person() {
  }

  Person(String name) {
    this.name = name;
  }

  Person(String name, int age) {
    this.name = name;
    this.age = age;
  }

  public void eat() {
    System.out.println(name + "正在吃饭...");
  }

  public void run(int miles) {
    System.out.println(name + "跑了" + miles + "米");
  }

}

@FunctionalInterface
interface PersonFactory<P extends Person> {
  P create(String name, int age);
}

/**
 * 泛型化的函数式接口
 *
 * @param <T> 参数类型
 * @param <V> 返回值类型
 */
@FunctionalInterface
interface OperateUtil<T, V> {
  V operate(T t);
}

class TestOperateUtil {
  public Object oerateResult(OperateUtil operateUtil, String str) {
    return operateUtil.operate(str);
  }
}

class Something {

  String startsWith(String s) {
    return String.valueOf(s.charAt(0));
  }

  String endsWith(String s) {
    return String.valueOf(s.charAt(s.length() - 1));
  }
}
{% endhighlight %}


## 默认方法（Default Methods） {#DefaultMethods}

默认方法是 Java8 在接口中引入的一个新的概念：当往一个接口中添加新的方法时，可以提供该方法的默认实现。
一般所遵循的接口方法的原则是不删除或修改已有的功能，而是添加新的功能作为替代。已有代码可以继续使用原有的功能，而新的代码则可以使用新的功能。
默认方法的主要目标之一是解决接口的演化问题：对于已有的接口使用者来说，代码可以继续运行；新的代码则可以使用该方法，也可以覆写默认的实现。
因为 Java 开发中所推荐的实践是面向接口而不是实现来编程，虽然具体实现可以不断地演化，但接口作为不同组件之间的契约，接口本身的演化则比较困难。
当接口发生变化时，该接口的所有实现类都需要做出相应的修改。所以，如果在新版本中对接口进行了修改或添加了新的方法，都会导致早期版本的代码无法运行。
而为了这种二进制向后兼容（新的JDK要能够运行旧版本JDK编译的出的二进制代码），引入了默认方法的概念。
这样，一些原有的接口，就可以使用Java8的lambda表达式功能。比如，‘List’ 和 ‘Collection’ 
接口并没有声明 ‘forEach’ 函数，增加默认方法之后，集合类就被赋予了lambda函数式计算的能力。

参考[Default Methods](http://www.tutorialspoint.com/java8/java8_default_methods.htm)

> Q：在集合框架中有一个Collection的抽象实现，即便没有默认方法功能，也只需要在集合框架接口中声明
之后再抽象实现类中实现即可。而所有的实现类都是此抽象类的子类，所以也并没有增加过多冗余代码。那么，
Java8中引入默认方法的实现是单纯为了Foreach方法的复用吗？

范例代码：

    DefaultMethodDemo.java

{% highlight Java %}
/**
 * Created by Bob on 2016/8/25.
 */
public class DefaultMethodDemo {
  public static void main(String[] args) {
    Vehicle vehicle = new Car();
    vehicle.print();
  }
}

interface Vehicle {
  default void print() {
    System.out.println("I am a vehicle!");
  }

  static void blowHorn() {
    System.out.println("Blowing horn!!!");
  }
}

interface FourWheeler {
  default void print() {
    System.out.println("I am a four wheeler!");
  }
}

class Car implements Vehicle, FourWheeler {
  public void print() {
    // TODO “super” 关键字：在非静态方法中引用静态方法
    Vehicle.super.print();
    FourWheeler.super.print();
    Vehicle.blowHorn();
    System.out.println("I am a car!");
  }
}

{% endhighlight %}


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
Parallel Processing | 并行操作 | numbers.parallelStream().filter(n -> n > 3);
distinct | 排重 |stream.distinct();
Collectors | 集合操作 | strings.stream().collect(Collectors.toList());
Statistics | Statistics | numbers.stream().mapToInt((x) -> x).summaryStatistics();

    TODO: Stream类接口详解


范例代码：

    StreamDemo.java

{% highlight Java %}

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Bob on 2016/8/25.
 */
public class StreamDemo {
  public static void main(String[] args) {

    List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
    List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");

    System.out.print("原数据：");
    numbers.stream().forEach(n -> System.out.print(n + "、"));

    System.out.print("\n乘方：");
    numbers.stream().map(n -> n * n).forEach(n -> System.out.print(n + "、"));

    System.out.print("\n排重：");
    numbers.stream().distinct().forEach(n -> System.out.print(n + "、"));

    System.out.print("\n过滤：");
    numbers.stream().filter(n -> n > 3).forEach(n -> System.out.print(n + "、"));

    System.out.print("\n截取：");
    numbers.stream().limit(4).forEach(n -> System.out.print(n + "、"));

    System.out.print("\n排序：");
    numbers.stream().sorted().forEach(n -> System.out.print(n + "、"));
    System.out.print("\n排序：");
    numbers.stream().sorted((a, b) -> a.compareTo(b)).forEach(n -> System.out.print(n + "、"));
    System.out.print("\n排序：");
    numbers.stream().sorted((a, b) -> b.compareTo(a)).forEach(n -> System.out.print(n + "、"));

    System.out.print("\n并行：");
    numbers.parallelStream().filter(n -> n > 3).forEach(n -> System.out.print(n + "、"));
    // 但是要注意，不能使用并行数据流进行排序，每次排序结果都不一样
    System.out.print("\n并行排序失败：");
    numbers.parallelStream().sorted().forEach(n -> System.out.print(n + "、"));


    System.out.print("\n集合：");
    List<String> filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
    System.out.print(filtered);

    System.out.print("\n集合：");
    String mergedString = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.joining(", "));
    System.out.print(mergedString);


    System.out.print("\nStatistics：\n");
    IntSummaryStatistics stats = numbers.stream().mapToInt((x) -> x).summaryStatistics();
    System.out.println("Highest number in List : " + stats.getMax());
    System.out.println("Lowest number in List : " + stats.getMin());
    System.out.println("Sum of all numbers : " + stats.getSum());
    System.out.println("Average of all numbers : " + stats.getAverage());

  }
}
{% endhighlight %}


## New Date/Time API {#NewDateTimeAPI}


## Base64 {#Base64}

## Optional Class {#OptionalClass}
见范例，但是好像并没有什么实际卵用……

## Nashorn JavaScript {#NashornJavaScript}
在JVM上运行JS，可以实现Java和JS的互相调用，也没什么卵用……



## 参考资料

* [Java 8 新特性](http://blog.techbeta.me/tags/java8/)
* [Java 8 官方教程翻译](http://blog.csdn.net/code_for_fun/article/details/42169993)
* [Java8 tutorials point](http://www.tutorialspoint.com/java8/index.htm)
* [Nashorn: JavaScript made great in Java 8](http://www.javaworld.com/article/2144908/scripting-jvm-languages/nashorn--javascript-made-great-in-java-8.html)
* [jvmlangsummit - Nashorn](http://wiki.jvmlangsummit.com/images/c/ce/Nashorn.pdf)

* [State of the Lambda](http://cr.openjdk.java.net/~briangoetz/lambda/lambda-state-4.html)
* [State of the Lambda: Libraries Edition](http://cr.openjdk.java.net/%7Ebriangoetz/lambda/collections-overview.html)
* [Translation of Lambda Expressions](http://cr.openjdk.java.net/%7Ebriangoetz/lambda/lambda-translation.html)

{% highlight Java %}
{% endhighlight %}
