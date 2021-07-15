---
layout:     post
title:      Java数据结构和算法 - 双端链表
date:       2021-04-14 13:50:55 +0800
postId:     2021-04-14-13-50-55
categories: [algorithm]
keywords:   [数据结构和算法]
---

## 定义
双端列表与传统的链表非常类似，但是他有一个新增的特性：即对最后一个节点的引用，就像对第一个节点的引用一样。
要注意：双端列表不是双向列表，只是第一个节点保留了最后一个节点的引用。

增加这个引用的好处在于，传统的单链表只有在表头增加元素比较方便，如果想要在表尾增加节点，那么就需要遍历列表找到
最后一个节点再添加上去，但如果有了尾节点的引用，则可以直接定位到尾节点添加即可。

![双端链表数据结构]({{ site.baseurl }}/image/post/2021/04/14/link/双端链表.png)

[图片来源](https://houzi.blog.csdn.net/article/details/8153165)

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
public class DoubleEndedLink {
  private Node first;
  private Node last;

  public DoubleEndedLink() {
    first = null;
    last = null;
  }

  public boolean isEmpty() {
    return first == null;
  }

  public void insertFirst(int data) {
    Node newNode = new Node(data);
    if (isEmpty()) {
      last = newNode;
    }
    newNode.next = first;
    first = newNode;
  }

  public void insertLast(int data) {
    Node newNode = new Node(data);
    if (isEmpty()) {
      first = newNode;
    } else {
      last.next = newNode;
    }
    last = newNode;
  }

  public Node find(int data) {
    Node current = first;
    while (current.data != data) {
      if (current.next == null) {
        return null;
      } else {
        current = current.next;
      }
    }
    return current;
  }

  public Node deleteFirst() {
    Node temp = first;
    if (first.next == null) {
      last = null;
    }
    first = first.next;
    return temp;
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
* [wiki-链表](https://zh.wikipedia.org/wiki/链表)
