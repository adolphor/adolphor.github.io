---
layout:     post
title:      Java数据结构和算法 - 链表实现栈和队列
date:       2021-04-14 17:05:15 +0800
postId:     2021-04-14-17-05-15
categories: [system]
tags:       [数据结构和算法]
geneMenu:   true
excerpt:    Java数据结构和算法 - 链表实现栈和队列
---

## 链表实现栈

```java
public class LinkStack {

  private LinkList thisList;

  public LinkStack(){
    thisList = new LinkList();
  }

  public void push(int data){
    thisList.insertFirst(data);
  }

  public int pop(){
    return thisList.deleteFirst().data;
  }

  public boolean isEmpty(){
    return thisList.isEmpty();
  }

  public void display(){
    thisList.display();
  }

}
```

## 链表实现队列
```java
public class LinkQueue {
  private DoubleEndedLink theList;

  public LinkQueue() {
    theList = new DoubleEndedLink();
  }

  public void insert(int data) {
    theList.insertLast(data);
  }

  public int remove() {
    return theList.deleteFirst().data;
  }

  public boolean isEmpty() {
    return theList.isEmpty();
  }

}
```

## 参考资料
* [Java数据结构和算法](https://book.douban.com/subject/1144007/)
