---
layout:     post
title:      Java数据结构和算法 - 栈
date:       2021-04-07 16:14:56 +0800
postId:     2021-04-07-16-14-56
categories: [system]
tags:       [数据结构和算法]
geneMenu:   true
excerpt:    Java数据结构和算法 - 栈
---

## 栈
栈、队列、优先级队列是比数组和其他数据存储结构更为抽象的结构，主要通过接口对栈、队列和优先级队列
进行定义，这些接口表明通过他们可以完成的操作，而他们的主要实现机制对用户来说是不可见的。

栈只允许访问一个数据项：即最后插入的数据项。移除这个数据项后才能访问倒数第二个插入的数据项，以此类推。

## 实现机制
栈的主要机制可以用数组来实现，本范例就是这样处理；但它也可以用链表来实现。优先级队列的内部实现可以
用数组或者一种特别的树——堆来实现。

## 核心代码

主要接口：
* push：入栈
* peek：查看
* pop：出栈

```java
private int top;
private long[] stackArray;

public void push(long j) {
  stackArray[++top] = j;
}
public long pop() {
  return stackArray[top--];
}
public long peek() {
  return stackArray[top];
}
```
## 使用场景
* 单词逆序：先压入所有数据，出栈的时候顺序打印即可
* 符号匹配：应该成对出现的小括号、中括号、大括号是否匹配：`BracketChecker.java`
* 解析算术表达式：比如解析 3*(4+5)
* 栈也是其他数据结构算法的遍历工具：
  - 用栈来辅助遍历树的节点
  - 用栈来辅助查找图的顶点（一种可以解决迷宫问题的技术）
* 微处理器中的使用：当调用一个方法时，把它的返回地址和参数压入栈；当方法结束返回时，那些数据出栈。

## 参考资料

* [Java数据结构和算法](https://book.douban.com/subject/1144007/)
