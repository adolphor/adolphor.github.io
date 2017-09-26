---
layout:     post
title:      Java8 新特性 —— lambda表达式
date:       2016-08-24 09:00:57 +0800
postId:     2016-08-24-09-00-57
categories: [Java]
tags:       [Java, Java8]
geneMenu:   true
excerpt:    Java8 新特性 —— lambda表达式
---

本文主要介绍 Java8 的以下几个新特性：

* [函数式接口（Functional Interfaces）](#FunctionalInterfaces)  
* [lambda表达式（Lambda Expressions）](#LambdaExpressions)  
* [方法引用（Method References）](#MethodReferences)  
* [默认方法（Default Methods）](#DefaultMethods)  
* [流操作（Streams）](#Streams)  

## lambda表达式（Lambda Expressions） {#LambdaExpressions}
lambda 表达式是 Java8 中引入的最重要的一个概念之一，使得 Java 拥有了进行函数式编程的能力。

### 简介
lambda 表达式就是一个匿名函数，Java8中可以使用lambda语法来代替匿名的内部类，先看一个范例：
首先定义一个String类型的List变量：

```java
List<String> names = Arrays.asList("Mahesh", "Suresh", "Ramesh", "Naresh", "Kalpesh");
```

如果要对这个list排序，按照以前Java7版本的JDK，需要使用 `Comparator` 接口，对于接口我们一般的使用方法是继承接口，
实现自己的实现类，则有如下的实现类：
```java
class MyComparator implements Comparator<String> {
  @Override
  public int compare(String a, String b) {
    return a.compareTo(b);
  }
}
```

比较的时候传入 `Comparator` 实例对象:
    
```java
// 初始化实例
Comparator<String> myComparator = new MyComparator();
Collections.sort(names, myComparator);
// 匿名方式
Collections.sort(names, new MyComparator());
```

简单一些的话，不需要定义具体的实现类，只需要在实例化接口的时候覆写接口内的抽象方法即可：

```java
Collections.sort(names, new Comparator<String>() {
  @Override
  public int compare(String a, String b) {
    return a.compareTo(b);
  }
});
```

上面使用了一个匿名内部实现类，覆写了compare方法，这是Java8之前进行排序时最简单的实现方式。
下面使用lambda表达式的方式实现：

```java
Collections.sort(names, (String a, String b) -> {
  return a.compareTo(b);
});
```

使用 lambda表达式 代替了匿名内部实现类，这就是lambda表达式的使用情景之一。
虽然这样看起来并不比旧方法简单，但下面会对此方法更进一步进行简化。


### 语法（Syntax）

参数列表 | 表达式符号 | 函数体
---|---|---
parameter | -> | expression body
(args1, args2, args3, ……) | -> | { expression body }

lambda语法结构如上所示，具体规则如下：

* 参数类型声明是可选项，可写可不写，Java8可以自动进行类型推断
* 如果只有一个参数，圆括号可选
* 如果只有一个表达式，花括号可选
* 如果只有一个返回表达式，return关键字可选

也就是说，lambda可能的表达式形式，有如下几种：  

*  () -> System.out.println(this)
*  (String str) -> System.out.println(str)
*  str -> System.out.println(str)
*  (String s1, String s2) -> { return s2.length() - s1.length(); }
*  (s1, s2) -> s2.length() - s1.length()

至此，已经知道了lambda表达式的详细语法，利用此规则，我们对前面的排序代码继续进行简化，
因为它只有一个返回表达式，所以可以省略 return 和 大括号，简写为：
    
```java
Collections.sort(names, (String a, String b) -> a.compareTo(b));
```
    
又 lambda 可以自动进行类型推断，所以可以省略参数类型，更进一步简写为：

```java
Collections.sort(names, (a, b) -> a.compareTo(b));
```

至此，已经将排序方法代码由四行简化为一行。

### 目标类型（target type）

既然可以使用lambda表达式替换匿名内部类，那么lambda表达式就具有匿名内部类的特性，
而匿名内部类是具有特定的接口(对象)类型的，那么lambda表达式也应该具有特定的目标类型（target type）。
比如上面的排序方法：

```java
Collections.sort(names, new Comparator<String>() {
  @Override
  public int compare(String a, String b) {
    return a.compareTo(b);
  }
});
```

此匿名内部类的类型是 `Comparator`：

```java
Comparator<String> comparator = new Comparator<String>() {
  @Override
  public int compare(String a, String b) {
    return a.compareTo(b);
  }
};
```
    
那么对于上述排序方法处使用的lambda表达式的 target type 同样也是 `Comparator`：

```java
Comparator<String> comparator = (a, b) -> a.compareTo(b);
```

也就是说，上面排序方法需要的只是 `Comparator` 接口类的实现，至于是自定义继承类实现实例，还是使用接口实例化，
或者使用lambda表达式，效果都是等价的，都是为了进行如下的动作：

```java
Collections.sort(names, comparator);
```

这也是list排序可以简写为如下 lambda 方式的原因： 
   
```java
Collections.sort(names, (a, b) -> a.compareTo(b));
```

但 lambda 表达式肯定不是只用于排序，target type 也不只是 `Comparator`，
只要一个接口符合函数式接口规范（函数式接口见下面章节的介绍），那么在使用此接口
的地方都可以使用 lambda 表达式。所以，lambda表达式的泛型化，也是真的吊了。

### 作用域（effective scope）

lambda 表达式可以访问表达式外部的非final变量，但不能进行修改，
这样，这个变量对于lambda表达式来说相当于一个隐式的final变量，比如：

```java
String scopeTestStr = "Bob";
Collections.sort(names, (a, b) -> {
  System.out.println(scopeTestStr);   // 可以访问非final变量
  // scopeTestStr = "change";         // 但不能进行修改
  return a.compareTo(b);
});
```

### 范例代码

    LambdaExpressionDemo.java

```java
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
    class MyComparator implements Comparator<String> {
      @Override
      public int compare(String a, String b) {
        return a.compareTo(b);
      }
    }
    // 初始化实例
    Comparator<String> myComparator = new MyComparator();
    Collections.sort(names, myComparator);
    // 匿名方式
    Collections.sort(names, new MyComparator());

    Collections.sort(names, new Comparator<String>() {
      @Override
      public int compare(String a, String b) {
        return a.compareTo(b);
      }
    });

    Collections.sort(names, (String a, String b) -> {  // 带有参数类型声明 和 return 关键字
      return a.compareTo(b);
    });
    Collections.sort(names, (String a, String b) -> a.compareTo(b));  // 省略return关键字
    Collections.sort(names, (a, b) -> a.compareTo(b)); // 省略参数类型声明

    // target type
    Comparator<String> comparator1 = new Comparator<String>() {
      @Override
      public int compare(String a, String b) {
        return a.compareTo(b);
      }
    };
    Collections.sort(names, comparator1);

    Comparator<String> comparator2 = (a, b) -> a.compareTo(b);
    Collections.sort(names, comparator2);

    // scope
    String scopeTestStr = "Bob";
    Collections.sort(names, (a, b) -> {
      System.out.println(scopeTestStr);   // 可以访问非final变量
      // scopeTestStr = "change";         // 但不能进行修改
      return a.compareTo(b);
    });

  }
}
```


## 函数式接口（Functional Interfaces） {#FunctionalInterfaces}

### 简介

简单来说，函数式接口是只包含一个抽象方法（但可以包含一个或一个以上的默认实现方法）的接口。比如 Java 标准库中的
 `java.lang.Runnable` 和 `java.util.Comparator` 
都是典型的函数式接口。对于函数式接口，除了可以使用 Java 中标准的方法来创建实现对象之外，还可以使用 lambda 表达式来创建
实现对象。这可以在很大程度上简化代码的实现。在使用 lambda 表达式时，只需要提供形式参数和方法体。由于函数式接口只有一个
抽象方法，所以通过 lambda 表达式声明的方法体就肯定是这个唯一的抽象方法的实现，而且形式参数的类型可以根据方法的类型声明
进行自动推断。函数式接口可以在类上加上 `@FunctionalInterface` 注解，其作用相当于 `@Override` 注解的作用。

### 实现方式（implements）

函数式接口只是一种特定类型的接口（只含有一个抽象方法），接口的各个性质同样适用于函数式接口。
作为函数式接口实现有三种方式，第一种是最原始的接口的实现类，第二种是lambda表达式，第三种是方法引用。
本节主要讲解前两种。

接口的实现类，是最原始的实现方式，比如上面的 `MyComparator` 就是 `Comparator` 接口的实现类，
或者在需要接口实例的地方，使用如上面的匿名实现类的方式：

```java
    Collections.sort(names, new Comparator<String>() {
      @Override
      public int compare(String a, String b) {
        return a.compareTo(b);
      }
    });
```

第二种就是在需要接口实例的地方，使用 lambda表达式方式：
```java
    Collections.sort(names, (a, b) -> a.compareTo(b)});
```


### 函数式接口列表

在 Java 8 中定义了很多函数接口供 lambda 表达式使用， `java.util.Function` 包中定义的函数接口列表有下面五类：

接口类型 | 接口作用
---|---
Function | 对数据内容进行操作，比如修改、删除，比如数据类型转换，有返回值
Operator | 对数据内容进行操作，有返回值，只不过参数类型和返回值类型全部一致，可以看做Function的特例情况
Consumer | 对数据内容进行消费，没有返回值
Supplier | 不需要传入参数，有返回值，可以看做Consumer的反函数，而且Supplier主要用于引用函数
Predicate | 对数据进行检测判断的规则，相当于判断条件的封装

Function系列接口使用范例，比如输入两个 String 字符串，求出 int 类型的两个字符串长度之和：
```java
BiFunction<String, String, Integer> biFunction = (a, b) -> (a + b).length();
System.out.println("BiFunction<String, String, Integer> => " + biFunction.apply("Hello", "Bob"));
```

Operator系列接口使用范例，Operator相当于Function的特例，只是入参类型和返回值类型都一样，比如都是String类型：
```java
BinaryOperator<String> binaryOperator = (a, b) -> a + b;
System.out.println("BinaryOperator<String> => " + binaryOperator.apply("Hello", "Bob"));
```

Consumer系列接口使用范例，比如一个任意类型，一个int类型：
```java
ObjIntConsumer<String> objIntConsumer = (a, b) -> System.out.println(a + b);
objIntConsumer.accept("Hello Num: ", 11);
```

Supplier系列接口使用范例，一种重要的使用情景是方法引用，具体的方法引用见下节详解：
```java
Supplier<Person> personSupplier = Person::new;
Person person = personSupplier.get();
System.out.println(person.name);
```

Predicate系列接口使用范例，可以看做是判断条件的封装，比如判断当前对象是否大于18岁，是否是男性：
```java
BiPredicate<Integer, String> biPredicate = (a, b) -> a > 18 && b.equals("Male");
System.out.println("BiPredicate<Integer, String> => " + biPredicate.test(person.age, person.gender));
```

Java8中的函数接口一般位于 `java.util.function` 包下，各详细接口定义汇总如下：

<table>
  <thead></thead>
  <tbody>
  <tr>
    <td rowspan="17">
      <h4>Function系列</h4>
    </td>
    <td>Function&lt;T,R&gt;</td>
    <td>
      T：入参类型<br/>
      R：返回值类型
    </td>
  </tr>
  <tr>
    <td>BiFunction&lt;T,U,R&gt;</td>
    <td>
      T：入参1类型<br/>
      U：入参2类型<br/>
      R：返回值类型
    </td>
  </tr>
  <tr>
    <td>DoubleFunction&lt;R&gt;</td>
    <td>
      R：返回值类型
    </td>
  </tr>
  <tr>
    <td>DoubleToIntFunction</td>
    <td></td>
  </tr>
  <tr>
    <td>DoubleToLongFunction</td>
    <td></td>
  </tr>
  <tr>
    <td>IntFunction&lt;R&gt;</td>
    <td>
      R：返回值类型
    </td>
  </tr>
  <tr>
    <td>IntToDoubleFunction</td>
    <td></td>
  </tr>
  <tr>
    <td>IntToLongFunction</td>
    <td></td>
  </tr>
  <tr>
    <td>LongFunction&lt;R&gt;</td>
    <td>
      R：返回值类型
    </td>
  </tr>
  <tr>
    <td>LongToDoubleFunction</td>
    <td></td>
  </tr>
  <tr>
    <td>LongToIntFunction</td>
    <td></td>
  </tr>
  <tr>
    <td>ToDoubleFunction&lt;T&gt;</td>
    <td>
      T：入参类型
    </td>
  </tr>
  <tr>
    <td>ToDoubleBiFunction&lt;T,U&gt;</td>
    <td>
      T：入参1类型<br/>
      U：入参2类型
    </td>
  </tr>
  <tr>
    <td>ToIntFunction&lt;T&gt;</td>
    <td>
      T：入参类型
    </td>
  </tr>
  <tr>
    <td>ToIntBiFunction&lt;T,U&gt;</td>
    <td>
      T：入参1类型<br/>
      U：入参2类型
    </td>
  </tr>
  <tr>
    <td>ToLongFunction&lt;T&gt;</td>
    <td>
      T：入参类型
    </td>
  </tr>
  <tr>
    <td>ToLongBiFunction&lt;T,U&gt;</td>
    <td>
      T：入参1类型<br/>
      U：入参2类型
    </td>
  </tr>
  <tr>
    <td rowspan="8">
      <h4>Operator系列</h4>
    </td>
    <td>BinaryOperator&lt;T&gt;</td>
    <td>
      T：入参类型
    </td>
  </tr>
  <tr>
    <td>UnaryOperator&lt;T&gt;</td>
    <td>
      T：入参类型
    </td>
  </tr>
  <tr>
    <td>DoubleBinaryOperator</td>
    <td></td>
  </tr>
  <tr>
    <td>DoubleUnaryOperator</td>
    <td></td>
  </tr>
  <tr>
    <td>IntBinaryOperator</td>
    <td></td>
  </tr>
  <tr>
    <td>IntUnaryOperator</td>
    <td></td>
  </tr>
  <tr>
    <td>LongBinaryOperator</td>
    <td></td>
  </tr>
  <tr>
    <td>LongUnaryOperator</td>
    <td></td>
  </tr>
  <tr>
    <td rowspan="8">
      <h4>Consumer系列</h4>
    </td>
    <td>Consumer&lt;T&gt;</td>
    <td>
      T：入参类型
    </td>
  </tr>
  <tr>
    <td>BiConsumer&lt;T,U&gt;</td>
    <td>
      T：入参1类型<br/>
      U：入参2类型
    </td>
  </tr>
  <tr>
    <td>DoubleConsumer</td>
    <td></td>
  </tr>
  <tr>
    <td>IntConsumer</td>
    <td></td>
  </tr>
  <tr>
    <td>LongConsumer</td>
    <td></td>
  </tr>
  <tr>
    <td>ObjDoubleConsumer&lt;T&gt;</td>
    <td>
      T：入参类型
    </td>
  </tr>
  <tr>
    <td>ObjIntConsumer&lt;T&gt;</td>
    <td>
      T：入参类型
    </td>
  </tr>
  <tr>
    <td>ObjLongConsumer&lt;T&gt;</td>
    <td>
      T：入参类型
    </td>
  </tr>
  <tr>
    <td rowspan="5">
      <h4>Supplier系列</h4>
    </td>
    <td>Supplier&lt;T&gt;</td>
    <td>
      T：入参类型
    </td>
  </tr>
  <tr>
    <td>DoubleSupplier</td>
    <td></td>
  </tr>
  <tr>
    <td>IntSupplier</td>
    <td></td>
  </tr>
  <tr>
    <td>LongSupplier</td>
    <td></td>
  </tr>
  <tr>
    <td>BooleanSupplier</td>
    <td></td>
  </tr>
  <tr>
    <td rowspan="5">
      <h4>Predicate系列</h4>
    </td>
    <td>Predicate&lt;T&gt;</td>
    <td>
      T：入参类型
    </td>
  </tr>
  <tr>
    <td>BiPredicate&lt;T,U&gt;</td>
    <td>
      T：入参1类型<br/>
      U：入参2类型
    </td>
  </tr>
  <tr>
    <td>DoublePredicate</td>
    <td></td>
  </tr>
  <tr>
    <td>IntPredicate</td>
    <td></td>
  </tr>
  <tr>
    <td>LongPredicate</td>
    <td></td>
  </tr>
  </tbody>
</table>

### 范例代码

    FunctionalInterfaceDemo.java

```java
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.DoubleFunction;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.IntToLongFunction;
import java.util.function.ObjIntConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

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

    // Java8 函数接口
    // Function 范例
    Function<String, Integer> function = (a) -> a.length();
    System.out.println("Function<String, String> => " + function.apply("Bob"));

    BiFunction<String, String, Integer> biFunction = (a, b) -> (a + b).length();
    System.out.println("BiFunction<String, String, Integer> => " + biFunction.apply("Hello", "Bob"));

    DoubleFunction<String> doubleFunction1 = (a) -> "Double arg is " + a;
    System.out.println("DoubleFunction<String> => " + doubleFunction1.apply(11.11));

    IntToLongFunction intToLongFunction = (a) -> a;
    System.out.println("IntToLongFunction => " + intToLongFunction.applyAsLong(11));

    // Operator 范例（operator就是Function的特例情况）
    BinaryOperator<String> binaryOperator = (a, b) -> a + b;
    System.out.println("BinaryOperator<String> => " + binaryOperator.apply("Hello", "Bob"));

    UnaryOperator<String> unaryOperator = (a) -> "unary msg: " + a;
    System.out.println("UnaryOperator<String> => " + unaryOperator.apply("Hello"));

    IntBinaryOperator intBinaryOperator = (a, b) -> a + b;
    System.out.println("IntBinaryOperator => " + intBinaryOperator.applyAsInt(13, 14));

    // Consumer 范例
    Consumer<String> consumer = (a) -> System.out.println(a);
    System.out.println("Consumer<String> => ");
    consumer.accept("Hello Bob");

    ObjIntConsumer<String> objIntConsumer = (a, b) -> System.out.println(a + b);
    objIntConsumer.accept("Hello Num: ", 11);

    // Supplier 范例：Supplier用于方法引用
    Supplier<String> supplier = () -> "Supplier test";
    System.out.println("Supplier<String> => " + supplier.get());

    DoubleSupplier doubleSupplier = () -> 11.11;
    System.out.println("DoubleSupplier => " + doubleSupplier.getAsDouble());

    // Suppliers may reference constructor methods:
    Supplier<Person> personSupplier1 = Person::new;
    Person person1 = personSupplier1.get();
    System.out.println(person1.name);
    // Supplier referencing a static method
    Supplier<Person> personSupplier2 = PersonFactory::producePerson;
    Person person2 = personSupplier2.get();
    System.out.println(person2.name);
    // Supplier referencing an instance method
    Person person3 = new FunctionalInterfaceDemo().getPerson();
    System.out.println(person3.name);

    // Predicate 范例
    Predicate<Integer> predicate = (a) -> a > 18;
    System.out.println("Predicate<String> => " + predicate.test(10));

    BiPredicate<Integer, String> biPredicate = (a, b) -> a > 18 && b.equals("Male");
    System.out.println("BiPredicate<Integer, String> => " + biPredicate.test(20, "Male"));

  }

  private Person getPerson() {
    // Supplier referencing an instance method
    Supplier<Person> userSupplier = this::producePerson;
    Person person = userSupplier.get();
    return person;
  }

  private Person producePerson() {
    Person person = new Person();
    person.name = "Person by instance method";
    return person;
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

class Person {
  public String name;

  Person() {
    name = "Person by constructor methods";
  }
}

class PersonFactory {
  public static Person producePerson() {
    Person person = new Person();
    person.name = "Person by static method";
    return person;
  }
}
```


## 方法引用（Method References） {#MethodReferences}

### 简介
在lambda表达式章节中可知，在需要特定接口类型参数的地方，就可以使用lambda表达式进行替换。
而部分lambda表达式有一种更简单的书写方式，那就是使用方法引用。
虽然不是所有的lambda表达式都可以使用方法引用替换，但所有的方法引用都可以使用lambda表达式进行替换。
还有一个特点就是方法引用可以直接引用已经存在的实例的方法作为函数式接口的实现，而这个实例方法并不要求
必须继承自函数式接口，任何符合函数式接口定义的方法，都可以被引用。

### 使用方法
方法引用有以下三种使用方式，而且方法引用不需要声明和指定参数及参数类型，JVM会根据函数式接口的定义，自动进行参数和类型的推断：

* 静态方法
* 构造函数
* 实例方法

引用静态方法范例，
下面是调用Integer的valueOf静态方法，将一个String类型转换为Integer类型：

```java
// lambda表达式方式，作为函数式接口实现
Function<String, Integer> converter1 = num -> Integer.valueOf(num);  
// 方法引用方式：调用静态方法，作为函数式接口实现
Function<String, Integer> converter2 = Integer::valueOf;  

// 再来看另外一个范例：
List<String> names = Arrays.asList("Mahesh", "Suresh", "Ramesh", "Naresh", "Kalpesh");
// lambda表达式方式进行遍历的方式
names.forEach((name) -> System.out.println(name));    
// 方法引用：调用系统静态方法进行遍历
names.forEach(System.out::println); 
```

引用构造函数范例：

```java
// lambda表达式方式
BiFunction<String, Integer, Person> biFunction = (name, age) -> new Person(name, age); 
// 方法引用方式：构造函数
BiFunction<String, Integer, Person> factory2 = Person::new;  
```

引用实例方法范例：

```java
// 定义任意一个服务类
class Something {
  String startsWith(String s) {
    return String.valueOf(s.charAt(0));
  }
  String endsWith(String s) {
    return String.valueOf(s.charAt(s.length() - 1));
  }
}
// 实例化
Something something = new Something();
// 将实例的startsWith方法作为函数接口的实现
UnaryOperator<String> starts = something::startsWith;
UnaryOperator<String> ends = something::endsWith;
```

不同于前面两个的测试，我们这里增加一个Function测试类
```java
class TestFunction {
  public Object funResult(Function function, String str) {
    return function.apply(str);
  }
}
```

一共需要两个参数，一个 Function 实例，一个被操作的参数，用这个函数式接口实例对数据进行处理。
因为所有Operator系列的接口都是 `Function` 类的子类，所以，UnaryOperator的实例可以作为参数传入，
测试如下：

```java
TestFunction testFun = new TestFunction();
System.out.println("Java starts with: " + testFun.funResult(starts, "Java"));
System.out.println("Java ends with: " + testFun.funResult(ends, "Java"));
```

也就是说，funResult方法相当于一个接口规范，需要的是一个 Function 实例，这个实例的具体实现可以根据需要自由实现，
本例的实现是直接调用其他实例（Something）的方法（startsWith、endsWith）作为自己的实现，Something不必遵守函数式接口规范，
但它的方法却可以被函数式接口调用，这样使得函数式接口的实现方式更加灵活。



### 范例代码

    LambdaExpressionDemo.java

```java
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Created by Bob on 2016/8/24.
 */

public class MethodReferenceDemo {
  public static void main(String[] args) {
    /**
     * 范例1
     */
    // lambda表达式：作为函数式接口实现参考上节范例
    Function<String, Integer> converter1 = num -> Integer.valueOf(num);  // lambda表达式方式
    Integer i1 = converter1.apply("123");
    System.out.println(i1);
    Function<String, Integer> converter2 = Integer::valueOf;  // 方法引用：调用静态方法，作为函数式接口实现
    Integer i2 = converter2.apply("123");
    System.out.println(i2);

    /**
     * 范例2
     */
    List<String> names = Arrays.asList("Mahesh", "Suresh", "Ramesh", "Naresh", "Kalpesh");
    Collections.sort(names, (a, b) -> a.compareTo(b)); // lambda表达式进行排序
    names.forEach((name) -> System.out.println(name));    // lambda表达式方式进行遍历的方式
    names.forEach(System.out::println); // 方法引用：调用系统静态方法进行遍历

    /**
     * 范例3
     */
    BiFunction<String, Integer, Person> biFunction1 = (name, age) -> new Person(name, age); // lambda表达式
    Person person1 = biFunction1.apply("Person1", 28);
    person1.eat();

    BiFunction<String, Integer, Person> biFunction2 = Person::new;  // 方法引用：构造函数
    Person person2 = biFunction2.apply("Person2", 28);
    person2.run(100);

    /**
     * 范例4
     */
    Something something = new Something();
    UnaryOperator<String> starts = something::startsWith;
    UnaryOperator<String> ends = something::endsWith;
    TestFunction testFun = new TestFunction();
    System.out.println("Java starts with: " + testFun.funResult(starts, "Java"));
    System.out.println("Java ends with: " + testFun.funResult(ends, "Java"));

  }
}

class Person {
  public String name;
  public int age;

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

class TestFunction {
  public Object funResult(Function function, String str) {
    return function.apply(str);
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
```


## 默认方法（Default Methods） {#DefaultMethods}

### 简介

默认方法是 Java8 在接口中引入的一个新的概念：当往一个接口中添加新的方法时，可以提供该方法的默认实现。
一般所遵循的接口方法的原则是不删除或修改已有的功能，而是添加新的功能作为替代。已有代码可以继续使用原有的功能，而新的代码则可以使用新的功能。
默认方法的主要目标之一是解决接口的演化问题：对于已有的接口使用者来说，代码可以继续运行；新的代码则可以使用该方法，也可以覆写默认的实现。
因为 Java 开发中所推荐的实践是面向接口而不是实现来编程，虽然具体实现可以不断地演化，但接口作为不同组件之间的契约，接口本身的演化则比较困难。
当接口发生变化时，该接口的所有实现类都需要做出相应的修改。所以，如果在新版本中对接口进行了修改或添加了新的方法，都会导致早期版本的代码无法运行。
而为了这种二进制向后兼容（新的JDK要能够运行旧版本JDK编译的出的二进制代码），引入了默认方法的概念。
这样，一些原有的接口，就可以使用Java8的lambda表达式功能。比如，`List` 和 `Collection` 
接口并没有声明 `forEach` 函数，增加默认方法之后，集合类就被赋予了lambda函数式计算的能力。

### 范例代码

    DefaultMethodDemo.java

```java
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
    Vehicle.super.print();
    FourWheeler.super.print();
    Vehicle.blowHorn();
    System.out.println("I am a car!");
  }
}
```


## 流操作（Streams） {#Streams}
流操作提供了一种新的数据处理方式，可以像SQL语句一样的声明方式进行数据的处理。
比如，下面的SQL语句：

    SELECT max(salary), employee_id, employee_name FROM Employee

上面的SQL语句返回了工资最高雇员的详细信息，而开发人员并不需要自己来进行计算和比较。
在Java的集合框架中，开发人员必须使用loop遍历比较每个数据，而如果使用流处理，能够
更加简单高效，而且可以利用计算机的多核性能并发进行。

### 流相关概念

主要有以下几个数据流操作相关的概念：

概念 | 详解
---|---
Sequence of elements  |    流是一组按照一定顺序排列的特定类型的元素
Source   |    源可以将集合、数组和 I/O 资源作为输入源
Aggregate operations  |    流提供了一系列的聚合操作，像 filter, map, limit, reduce, find, match 等
Pipelining  |    大多数流操作都是在管道（Pipeline）中进行的，这些操作叫做中间操作（intermediate operations），他们的功能是加载并处理数据流，之后输出到目标地点。一般将 `collect()` 方法用于数据流操作的末尾，标志着数据流处理的结束。
Automatic iterations  |     流操作提供了数据遍历的功能

### 数据流操作详解
流操作带来最大的遍历就是可以链式操作，更加语义化，使得代码可读性更高。

先定义两个List变量如下

```java
List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
List<String> strings = Arrays.asList("abc", "bc", "efg", "abcd", "jkl");
```

数据流操作的主要方法有：

方法名 | 作用 | 范例
---|---|---
stream()  |    此方法返回顺序数据流 | numbers.stream()
parallelStream()  |    此方法返回并行计算数据流 | numbers.parallelStream().filter(n -> n > 3);
map | 对数据流数据进行操作 | numbers.stream().map(n -> n * n);
filter | 过滤符合条件的数据 | numbers.stream().filter(n -> n > 3);
reduce | 求最大/小值,和,平均值等操作 | 
limit | 截取特定数量的数据元素 | numbers.stream().limit(4);
sorted | 排序 | numbers.stream((a, b) -> a.compareTo(b)).sorted();
distinct | 排重 |stream.distinct();
forEach | 遍历数据流 | numbers.stream().forEach(System.out::println);
Collectors | 集合操作 | strings.stream().collect(Collectors.toList());
Statistics | Statistics | numbers.stream().mapToInt((x) -> x).summaryStatistics();


### 数据流操作方法

Stream类中定义的函数式接口汇总如下：

方法名 | 接口规范
---|---
limit |     Stream<T> limit(long maxSize);
map | <R> Stream<R> map(Function<? super T, ? extends R> mapper)
mapToInt | IntStream mapToInt(ToIntFunction<? super T> mapper);
mapToLong | LongStream mapToLong(ToLongFunction<? super T> mapper);
mapToDouble | DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper);
flatMap | <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper)
flatMapToInt | IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper);
flatMapToLong | LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper);
flatMapToDouble | DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper);
skip |     Stream<T> skip(long n);
filter | Stream<T> filter(Predicate<? super T> predicate) 
anyMatch | boolean anyMatch(Predicate<? super T> predicate);
allMatch | boolean allMatch(Predicate<? super T> predicate);
noneMatch | boolean noneMatch(Predicate<? super T> predicate);
findFirst | Optional<T> findFirst();
findAny | Optional<T> findAny();
peek | Stream<T> peek(Consumer<? super T> action) 
distinct | Stream<T> distinct()
min | Optional<T> min(Comparator<? super T> comparator);
max | Optional<T> max(Comparator<? super T> comparator);
sorted | Stream<T> sorted() 
sorted | Stream<T> sorted(Comparator<? super T> comparator);
reduce | T reduce(T identity, BinaryOperator<T> accumulator);
reduce | Optional<T> reduce(BinaryOperator<T> accumulator);
reduce | <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner);
forEach | void forEach(Consumer<? super T> action);
forEachOrdered | void forEachOrdered(Consumer<? super T> action);
toArray | Object[] toArray();
toArray | <A> A[] toArray(IntFunction<A[]> generator);
count | long count();
collect | <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner);
collect | <R, A> R collect(Collector<? super T, A, R> collector);



范例代码：

    StreamDemo.java

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Bob on 2016/8/25.
 */
public class StreamDemo {
  public static void main(String[] args) {

    List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
    List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");

    List<Person> persons = Arrays.asList(
            new Person("Max", 18),
            new Person("Peter", 23),
            new Person("Pamela", 23),
            new Person("David", 12)
    );

    System.out.print("原数据：");
    numbers.stream().forEach(n -> System.out.print(n + "、"));

    System.out.print("\n乘方：");
    numbers.stream().map(n -> n * n).forEach(n -> System.out.print(n + "、"));

    System.out.print("\n求和方法1：");
    int resultSum1 = numbers.stream().mapToInt(Integer::new).sum();
    System.out.print(resultSum1);

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

    System.out.print("\n集合：");
    List<String> filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
    System.out.print(filtered);

    System.out.print("\n集合：");
    String mergedString = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.joining(", "));
    System.out.print(mergedString);


    // reduce相关操作：操作的是两个元素之间的关系
    System.out.print("\n求和方法2：");
    Optional<Integer> resultSum2 = numbers.stream().reduce((x, y) -> x + y);
    resultSum2.ifPresent(System.out::print);
    System.out.print("\n求和方法3：");
    Optional<Integer> resultSum3 = numbers.stream().reduce(Integer::sum);
    resultSum3.ifPresent(System.out::print);
    // —— 第一个参数0，相当于默认值
    System.out.print("\n求和方法4：");
    Integer resultSum4 = numbers.stream().reduce(0, (x, y) -> x + y);
    System.out.print(resultSum4);
    System.out.print("\n求和方法5：");
    Integer resultSum5 = numbers.stream().reduce(0, Integer::sum);
    System.out.print(resultSum5);

    System.out.print("\n求和方法6：");
    int resultSum6 = strings.stream().mapToInt(str -> str.length()).reduce(0, (x, y) -> x + y);
    System.out.print(resultSum6);

    /**
     * Collectors
     */
    System.out.print("\n分组：");
    Map<Integer, List<Person>> personsByAge = persons
            .stream()
            .collect(Collectors.groupingBy(p -> p.age));
    personsByAge
            .forEach((age, p) -> System.out.format("age %s: %s\n", age, p));

    System.out.print("\n平均值：");
    Double averageAge = persons
            .stream()
            .collect(Collectors.averagingInt(p -> p.age));
    System.out.println(averageAge);

    System.out.print("\n统计：");
    IntSummaryStatistics ageSummary = persons
            .stream()
            .collect(Collectors.summarizingInt(p -> p.age));
    System.out.println(ageSummary);

    System.out.print("\n统计：\n");
    IntSummaryStatistics stats = numbers.stream().mapToInt((x) -> x).summaryStatistics();
    System.out.println("\t最大值: " + stats.getMax());
    System.out.println("\t最小值: " + stats.getMin());
    System.out.println("\t求和: " + stats.getSum());
    System.out.println("\t平均值: " + stats.getAverage());

    System.out.print("\njoining：");
    String phrase = persons
            .stream()
            .filter(p -> p.age >= 18)
            .map(p -> p.name)
            .collect(Collectors.joining(" and ", "In Germany ", " are of legal age."));
    System.out.println(phrase);

    System.out.print("\nCollectors.toMap：");
    Map<Integer, String> map = persons
            .stream()
            .collect(
                    Collectors.toMap(
                            p -> p.age,
                            p -> p.name,
                            (name1, name2) -> {
                              System.out.println(name2);
                              return name1 + ";" + name2;
                            }));

    System.out.print("\nCollectors.of：");
    Collector<Person, StringJoiner, String> personNameCollector =
            Collector.of(
                    () -> new StringJoiner(" | "),          // supplier
                    (j, p) -> j.add(p.name.toUpperCase()),  // accumulator
                    (j1, j2) -> j1.merge(j2),               // combiner
                    StringJoiner::toString);                // finisher
    String names = persons
            .stream()
            .collect(personNameCollector);
    System.out.println(names);  // MAX | PETER | PAMELA | DAVID

    System.out.println(map);

    /**
     * FlatMap
     */
    List<Foo> foos = new ArrayList<>();

// create foos
    IntStream
            .range(1, 4)
            .forEach(i -> foos.add(new Foo("Foo" + i)));
// create bars
    foos.forEach(f ->
            IntStream
                    .range(1, 4)
                    .forEach(i -> f.bars.add(new Bar("Bar" + i + " <- " + f.name))));

    System.out.print("\nflatMap：");
    foos.stream()
            .flatMap(f -> f.bars.stream())
            .forEach(b -> System.out.println(b.name));

    IntStream.range(1, 4)
            .mapToObj(i -> new Foo("Foo" + i))
            .peek(f -> IntStream.range(1, 4)
                    .mapToObj(i -> new Bar("Bar" + i + " <- " + f.name))
                    .forEach(f.bars::add))
            .flatMap(f -> f.bars.stream())
            .forEach(b -> System.out.println(b.name));

    Outer outer = new Outer();
    if (outer != null && outer.nested != null && outer.nested.inner != null) {
      System.out.println(outer.nested.inner.foo);
    }

    Optional.of(new Outer())
            .flatMap(o -> Optional.ofNullable(o.nested))
            .flatMap(n -> Optional.ofNullable(n.inner))
            .flatMap(i -> Optional.ofNullable(i.foo))
            .ifPresent(System.out::println);

    /**
     * Reduce
     */
    persons
            .stream()
            .reduce((p1, p2) -> p1.age > p2.age ? p1 : p2)
            .ifPresent(System.out::println);    // Pamela
    Person result =
            persons
                    .stream()
                    .reduce(new Person("", 0), (p1, p2) -> {
                      p1.age += p2.age;
                      p1.name += p2.name;
                      return p1;
                    });

    System.out.format("name=%s; age=%s", result.name, result.age);

    Integer ageSum = persons
            .stream()
            .reduce(0, (sum, p) -> sum += p.age, (sum1, sum2) -> sum1 + sum2);

    System.out.println(ageSum);  // 76

    Integer ageSum2 = persons
            .stream()
            .reduce(0,
                    (sum, p) -> {
                      System.out.format("accumulator: sum=%s; person=%s\n", sum, p);
                      return sum += p.age;
                    },
                    (sum1, sum2) -> {
                      System.out.format("combiner: sum1=%s; sum2=%s\n", sum1, sum2);
                      return sum1 + sum2;
                    });

    Integer ageSum3 = persons
            .parallelStream()
            .reduce(0,
                    (sum, p) -> {
                      System.out.format("accumulator: sum=%s; person=%s\n", sum, p);
                      return sum += p.age;
                    },
                    (sum1, sum2) -> {
                      System.out.format("combiner: sum1=%s; sum2=%s\n", sum1, sum2);
                      return sum1 + sum2;
                    });


    /**
     * Parallel Streams
     */
    System.out.print("\n并行：");
    numbers.parallelStream().filter(n -> n > 3).forEach(n -> System.out.print(n + "、"));
    System.out.print("\n并行排序：");
    numbers.parallelStream().sorted().forEachOrdered(n -> System.out.print(n + "、"));

    ForkJoinPool commonPool = ForkJoinPool.commonPool();
    System.out.println(commonPool.getParallelism());    // 根据机器配置情况不同而不同

    Arrays.asList("a1", "a2", "b1", "c2", "c1")
            .parallelStream()
            .filter(s -> {
              System.out.format("filter: %s [%s]\n",
                      s, Thread.currentThread().getName());
              return true;
            })
            .map(s -> {
              System.out.format("map: %s [%s]\n",
                      s, Thread.currentThread().getName());
              return s.toUpperCase();
            })
            .forEach(s -> System.out.format("forEach: %s [%s]\n",
                    s, Thread.currentThread().getName()));

    Arrays.asList("a1", "a2", "b1", "c2", "c1")
            .parallelStream()
            .filter(s -> {
              System.out.format("filter: %s [%s]\n",
                      s, Thread.currentThread().getName());
              return true;
            })
            .map(s -> {
              System.out.format("map: %s [%s]\n",
                      s, Thread.currentThread().getName());
              return s.toUpperCase();
            })
            .sorted((s1, s2) -> {
              System.out.format("sort: %s <> %s [%s]\n",
                      s1, s2, Thread.currentThread().getName());
              return s1.compareTo(s2);
            })
            .forEach(s -> System.out.format("forEach: %s [%s]\n",
                    s, Thread.currentThread().getName()));

    persons
            .parallelStream()
            .reduce(0,
                    (sum, p) -> {
                      System.out.format("accumulator: sum=%s; person=%s [%s]\n",
                              sum, p, Thread.currentThread().getName());
                      return sum += p.age;
                    },
                    (sum1, sum2) -> {
                      System.out.format("combiner: sum1=%s; sum2=%s [%s]\n",
                              sum1, sum2, Thread.currentThread().getName());
                      return sum1 + sum2;
                    });

    /**
     * Spliterator
     */
    Spliterator<Integer> mySpliterator = numbers.spliterator();
    System.out.println("mySpliterator.estimateSize() = " + mySpliterator.estimateSize());
    // watch my ArrayList forEach() tutorial for a detailed explanation on how a Consumer functional interface works.
    Consumer<Integer> c = x -> System.out.println("mySpliterator.forEachRemaining = " + x);
    mySpliterator.forEachRemaining(c);
    System.out.println("mySpliterator.estimateSize() = " + mySpliterator.estimateSize());

  }
}

class Person {
  String name;
  int age;

  Person(String name, int age) {
    this.name = name;
    this.age = age;
  }

  @Override
  public String toString() {
    return name;
  }
}

class Foo {
  String name;
  List<Bar> bars = new ArrayList<>();

  Foo(String name) {
    this.name = name;
  }
}

class Bar {
  String name;

  Bar(String name) {
    this.name = name;
  }
}

class Outer {
  Nested nested;
}

class Nested {
  Inner inner;
}

class Inner {
  String foo;
}
```


## 参考资料

* [Java 8 新特性](http://blog.techbeta.me/tags/java8/)
* [Java 8 官方教程翻译](http://blog.csdn.net/code_for_fun/article/details/42169993)
* [Java8 tutorials point](http://www.tutorialspoint.com/java8/index.htm)
* [Nashorn: JavaScript made great in Java 8](http://www.javaworld.com/article/2144908/scripting-jvm-languages/nashorn--javascript-made-great-in-java-8.html)
* [jvmlangsummit - Nashorn](http://wiki.jvmlangsummit.com/images/c/ce/Nashorn.pdf)
* [Java 8 Stream Tutorial](http://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/)
* [State of the Lambda](http://cr.openjdk.java.net/~briangoetz/lambda/lambda-state-4.html)
* [State of the Lambda: Libraries Edition](http://cr.openjdk.java.net/%7Ebriangoetz/lambda/collections-overview.html)
* [Translation of Lambda Expressions](http://cr.openjdk.java.net/%7Ebriangoetz/lambda/lambda-translation.html)
* [Java 8 Parallel Streams](http://www.byteslounge.com/tutorials/java-8-parallel-streams)


```java
```
