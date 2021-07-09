---
layout:     post
title:      Java基础类 - String
date:       2021-07-03 11:03:29 +0800
postId:     2021-07-03-11-03-29
categories: [Java]
tags:       [Java]
geneMenu:   true
excerpt:    Java基础类 - String
---

## 字符型常量和字符串常量的区别

* 形式
    - 字符常量是单引号引起的一个字符，字符串常量是双引号引起的 0 个或若干个字符
*  含义
    - 字符常量相当于一个整型值( ASCII 值),可以参加表达式运算; 字符串常量代表一个地址值(该字符串在内存中存放位置)
* 占内存大小
    - 字符常量只占 2 个字节; 字符串常量占若干个字节 (注意： char 在 Java 中占两个字节)

## String 为什么不可变
String 类中使用 final 关键字修饰字符数组来保存字符串，private final char value[]，
所以String 对象是不可变的。

而 StringBuilder 与 StringBuffer 都继承自 AbstractStringBuilder 类，在 
AbstractStringBuilder 中也是使用字符数组保存字符串char[]value 但是没有用 final 
关键字修饰，所以这两种对象都是可变的。

##  String StringBuffer 和 StringBuilder 的区别
每次对 String 类型进行改变的时候，都会生成一个新的 String 对象，然后将指针指向新的 String 
对象。StringBuffer 每次都会对 StringBuffer 对象本身进行操作，而不是生成新的对象并改变对象
引用。相同情况下使用 StringBuilder 相比使用 StringBuffer 仅能获得 10%~15% 左右的性能提升，
但却要冒多线程不安全的风险。

对于三者使用的总结：
* 操作少量的数据: 适用 `String`
* 单线程操作字符串缓冲区下操作大量数据: 适用 `StringBuilder`
* 多线程操作字符串缓冲区下操作大量数据: 适用 `StringBuffer`


## 参考资料

* [Java基础知识](https://snailclimb.gitee.io/javaguide/#/docs/java/basis/Java基础知识)
* [说说如何重写Java的equals方法](https://hellofrank.github.io/2019/09/21/说说如何重写Java的equals方法/?utm_source=tuicool&utm_medium=referral)
