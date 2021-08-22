---
layout:     post
title:      final关键字解析
date:       2016-07-26 12:00:00 +0800
postId:     2016-07-26-22-00-00
categories: []
keywords:   [Java]
---

`final` 表示“不能改变”，不进行改变的理由可能是设计或者效率。但修饰不同的对象的时候，含义不尽相同，
下面分为数据，方法和类分别进行阐述。

## 1 final 数据
当使用`final`修饰变量时，表示这个变量不能被修改。在一般情况下，`final` 和 `static` 一起使用，
`static`强调只有一份，`final`说明它是一个常量。一个既是`static`又是`final`的域只占据一段不能被修改的存储空间。

但是需要注意的一点是，被`final`修饰变量的初始化时间并不固定，既可以是编译时常量，也可以是运行时被初始化。
但相同的一点是，一旦被初始化，就保持不变。

编译时常量可以在编译时进行计算，减轻了运行时负担，但必须是基本数据类型。

### 1.2 基本数据类型
如果是基本对象类型，`final`使数值保持不变，也就是不能被修改。

### 1.3 多线程下final的基本数据类型
我们知道由于重排序的作用(参见清单2-10)，一个线程读取到一个对象引用时，该对象可能尚未初始化完毕，
即这些线程可能读取到该对象字段的默认值而不是初始值(通过构造器或者初始化语句指定的值)。在多线程
环境下final关键字有其特殊的作用:

当一个对象被发布到其他线程的时候，该对象的所有final字段(实例变量)都是初始化完毕的，即其他线程
读取这些字段的时候所读取到的值都是相应字段的初始值(而不是默认值)。而非final字段没有这种保障，
即这些线程读取该对象的非final字段时所读取到的值可能仍然是相应字段的默认值。对于引用型final
字段，final 关键字还进一步确保该字段所引用的对象已经初始化完毕，即这些线程读取该字段所引用的
对象的各个字段时所读取到的值都是相应字段的初始值。

```java
public class FinalFieldExample {
  static FinalFieldExample instance;

  final int x;
  int y;

  public FinalFieldExample() {
    x = 1;
    y = 2;
  }

  public static void writer() {
    instance = new FinalFieldExample();
  }

  public static void reader() {
    final FinalFieldExample theInstance = instance;
    if (theInstance != null) {
      int diff = instance.y - instance.x;
      print(diff);
    }
  }

  public static void print(int val) {
    System.out.println(val);
  }

}
```

在JIT编译器的内联( Inline )优化的作用下, FinalFieldExample方法中的语句会被"挪入"writer方法，因此writer方法对应的指令可能被编译为与如下伪代码等效的代码:

```
objRef = allocate (FinalFieldExample.class);//子操作①:分配对象所需的存储空间
objRef.x = 1;//子操作②:对象初始化
objRef.y = 2;//子操作③:对象初始化
instance = objRef; //子操作④:将对象引用写入共享变量
```

其中，子操作③(非final 字段初始化)可能被JIT编译器、处理器重排序到子操作④(对象发布)之后,因此
当其他线程通过共享变量instance看到对象引用objRef的时候，该对象的实例变量y可能还没有被初始化
(因为此时子操作③可能尚未被执行或者其结果尚未对其他处理器可见)，即这些线程看到的
FinalFieldExample对象的y字段的值可能仍然是其默认值0。而FinalFieldExample的字段x则是
采用final 关键字修饰，因此Java虚拟机会将子操作②(final字段初始化)限定在子操作④前完成。
这里所谓的限定是指JIT编译器不会将构造器中对final字段的赋值操作重排到子操作④之后，并且还会禁止处理
器做这种重排序18。通过这种限定，Java 虚拟机、处理器一起保障了对象instance被发布前其final字段x
必然是初始化完毕的。

### 1.4 引用数据类型
如果是不是基本类型，`final`使对象引用保持不变；但这并不能保证对象内的属性不被修改。

### 1.5 多线程下final的引用数据类型
进一步，对于引用型final字段，Java语言规范还会保障其他线程看到包含该字段的对象时，这个字段
所引用的对象必然是初始化完毕的。如下所示，当一个线程看到一个HTTPRangeRequest实例的时候，
该线程所看到的实例变量range所引用的对象必然是初始化完毕的，但是该线程所看到的实例变量url的值
可能仍然是null(默认值)。

```java
public class HttpRangeRequest {
  private final Range range;
  private String url;

  public HttpRangeRequest(String url, int lowerBound, int upperBound) {
    this.url = url;
    this.range = new Range(lowerBound, upperBound);
  }

  public Range getRange() {
    return range;
  }

  public static class Range {
    private long lowerBound;
    private long upperBound;

    public Range(long lowerBound, long upperBound) {
      this.lowerBound = lowerBound;
      this.upperBound = upperBound;
    }
  }

}
```

在JIT编译器的内联( Inline)优化的作用下，如下语句：
```java
instance = new HTTPRangeRequest ("http:/ /xyz. com/ download/big. tar",0, 1048576) ;
```

可能会被编译成与如下伪代码等效的指令:
```
objRef = allocate (HTTPRangeRequest.class);//子操作①:分配对象所需的存储空间
objRef.url = "http:/ /xyz . com/ download/big. tar";
objRange = allocate (Range.class) ;
obj Range. lowerBound = 0;//子操作②:初始化对象objRange
obj Range. upperBound = 1048576;//子操作③:初始化对象obj Range
objRef. range = objRange;//子操作④:发布对象obj Range
instance = objRef; //子操作⑤:发布对象objRef
```
由于实例变量range(引用型变量)采用final关键字修饰，因此Java语言会保障构造器中对该变量的
初始化(赋值)操作(子操作④)以及该变量值所引用的对象(Range实例)的初始化(子操作②和子操作③)
被限定在子操作⑤前完成。这就保障了HTTPRangeRequest实例对外可见的时候，该实例的 range 字段
所引用的对象已经初始化完毕。而url字段由于没有采用final修饰，因此Java虚拟机仍然可能将其重排序
到子操作⑤之后。

这里需要注意,final关键字只能保障有序性,即保障一个对象对外可见的时候该对象的final字段必然是
初始化完毕的。final关键字并不保障对象引用本身对外的可见性。

注意：当一个对象的引用对其他线程可见的时候,这些线程所看到的该对象的final字段必然是初始化完毕的。
final关键字的作用仅是这种有序性的保障，它并不能保障包含final字段的对象的引用自身对其他线程的可见性。

### 1.6 空白final

### 1.7 范例
```java
class PeoPle {
  public String name;
  PeoPle() {
    name = "jack";
  }
}

public class FinalDemo01 {
  // 编译时常量（编译时计算）
  public static final int INT_1 = 10;
  // 运行时初始化的常量
  public static final int INT_2 = new Random(47).nextInt();
  // 引用对象（不能改变引用）
  public static final PeoPle PEOPLE = new PeoPle();

  public static void main(String[] args) {
    System.out.println(INT_1);
    System.out.println(INT_2);
    System.out.println(PEOPLE.name);
    PEOPLE.name = "adolphor"; // 不能改变PEOPLE的引用，但能改变属性的值
    System.out.println(PEOPLE.name);
  }
}
```

## 2 final 方法

## 3 final 对象

## 4 final 和 volatile
从1.3 和 1.5 可以看出，final关键字可以保证变量初始化的时候时候不被指令重排且被引用之前一定
被初始化完毕，另外我们知道volatile关键字的作用除了对其他线程可见就是避免指令重排，既然两个
关键字都有避免指令重拍的作用，那么两者有什么区别呢？我的理解是两者的使用场景和时机不同：
* final关键字主要是在指令初始化的时候避免指令重排
* volatile关键字是在更改变量值的时候避免指令重排

## 参考资料

* [final关键字解析]({% post_url java/basic/content/2016-07-26-final-keyword %})
* [8.8' - Java多线程编程实战指南-核心篇](https://book.douban.com/subject/27034721/)
* [Java并发 - volatile 关键字]({% post_url java/concurrent/content/2021-07-29-02-concurrent-keyword-volatile %})

