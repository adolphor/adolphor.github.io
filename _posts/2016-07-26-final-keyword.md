---
layout:     post
title:      final关键字解析
date:       2016-07-26 12:00:00 +0800
postId:     2016-07-26-22-00-00
categories: [blog]
tags:       [Java]
geneMenu:   true
excerpt:    java final关键字解析
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

### 1.3 对象引用
如果是不是基本类型，`final`使对象引用保持不变；但这并不能保证对象内的属性不被修改。


### 1.4 空白final


### 1.5 范例
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





