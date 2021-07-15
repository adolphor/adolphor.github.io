---
layout:     post
title:      Java数据结构和算法 - 单链表
date:       2021-04-14 14:01:50 +0800
postId:     2021-04-14-14-01-50
categories: [algorithm]
keywords:   [数据结构和算法]
---

## 定义

链表中最简单的一种是单向链表，它包含两个域，一个信息域和一个指针域。这个链接指向列表中的下一个节点，
而最后一个节点则指向一个空值。
一个单向链表的节点被分成两个部分。第一个部分保存或者显示关于节点的信息，
第二个部分存储下一个节点的地址。单向链表只可向一个方向遍历。

![单链表数据结构]({{ site.baseurl }}/image/post/2021/04/14/link/单链表.png)

[图片来源](https://houzi.blog.csdn.net/article/details/8152816)

## 核心代码
```java
public class Node {
  public int data;
  public Node next;
  
  public Node(int data) {
    this.data = data;
  }

  public void display() {
    System.out.println(data);
  }
}
```
```java
public class LinkList {
  private Node first;

  public LinkList() {
    first = null;
  }

  public boolean isEmpty() {
    return (first == null);
  }

  public void insertFirst(int data) {
    Node newNode = new Node(data);
    newNode.next = first;  // 将原首节点挂在新节点上
    first = newNode;       // 将新节点设置为首节点
  }

  public Node deleteFirst() {
    Node temp = first;
    first = first.next;    // 不需要删除temp对next的引用吗？是否影响垃圾回收？
    return temp;
  }

  public Node find(int key) {
    Node current = first;
    while (current.data != key) {
      if (current.next == null) {
        return null;
      } else {
        current = current.next;
      }
    }
    return current;
  }

  public Node delete(int key) {
    Node current = first;
    Node previous = first;
    while (current.data != key) {
      if (current.next == null) {
        current = null;
      } else {
        previous = current;
        current = current.next;
      }
    }
    if (current == first) {
      first = first.next;
    } else {
      previous.next = current.next;
    }
    return current;
  }

  public void display() {
    Node current = first;
    while (current != null) {
      current.display();
      current = current.next;
    }
  }
}
```

## 参考资料
* [Java数据结构和算法](https://book.douban.com/subject/1144007/)

