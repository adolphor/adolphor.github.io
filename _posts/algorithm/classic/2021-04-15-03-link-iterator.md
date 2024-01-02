---
layout:     post
title:      Java数据结构和算法 - 迭代器
date:       2021-04-15 13:52:35 +0800
postId:     2021-04-15-13-52-35
categories: [Algorithm]
keywords:   [数据结构和算法]
---

## 迭代器
链表不能像数组一样直接操作下标，链表中的所有方法都没有提供给用户任何遍历上的控制手段，一遍找到指定节点。
这种情况下，如果需要遍历一个链表，并在某些符合条件的节点上执行一些操作，就无法在只遍历一次链表的情况下
完成。

那么，如果放在链表内部呢？作为类的用户，需要能存取指向任意节点的引用，这样就可以考察和修改节点。引用应该
能递增，因此可以沿着整个链表遍历，依次查看每个节点，而且可以访问这个引用所指向的节点。

设想创建这么一个引用，把它安放在哪里？一个可能性是使用链表本身的一个字段，叫做current或者其它名字。可以
用current存取一个节点，然后使用current递增，移动到下一个节点。

这个方法存在一个问题是，可能同时需要不止一个这种引用，就像经常需要同时使用几个下标。多少个合适？无法知道
用户会需要几个。因此，很容易想到允许用户按使用的需求创建多个引用。为了在面向对象语言中使其成为可能，自然
是考虑在一个类中嵌入每个引用。这个类不能和链表类相同，因为只有一个链表对象，所以把它做成另一个类。

## 完整代码
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
public class IterLinkList {

  private Node first;

  public IterLinkList() {
    first = null;
  }

  public Node getFirst() {
    return first;
  }

  public void setFirst(Node node){
    first=node;
  }

  public boolean isEmpty(){
    return (first==null);
  }

  public ListIterator getIterator(){
    return new ListIterator(this);
  }

  public void display(){
    Node current = first;
    while (current != null) {
      current.display();
      current = current.next;
    }
  }

}
```
```java
public class ListIterator {
  private Node current;
  private Node previous;
  private IterLinkList ourList;

  public ListIterator(IterLinkList list) {
    ourList = list;
    reset();
  }

  public void reset() {
    current = ourList.getFirst();
    previous = null;
  }

  public boolean atEnd() {
    return (current.next == null);
  }

  public void nextNode() {
    previous = current;
    current = current.next;
  }

  public Node getCurrent() {
    return current;
  }

  public void insertAfter(int data) {
    Node newNode = new Node(data);
    if (ourList.isEmpty()) {
      ourList.setFirst(newNode);
      current = newNode;
    } else {
      newNode.next = current.next;
      current.next = newNode;
      nextNode();
    }
  }

  public void insertBefore(int data) {
    Node newNode = new Node(data);
    if (previous == null) {
      newNode.next = ourList.getFirst();
      ourList.setFirst(newNode);
      reset();
    } else {
      newNode.next = previous.next;
      previous.next = newNode;
      current = newNode;
    }
  }

  public int deleteCurrent() {
    int data = current.data;
    if (previous == null) {
      ourList.setFirst(current.next);
      reset();
    } else {
      previous.next = current.next;
      if (atEnd()) {
        reset();
      } else {
        current = current.next;
      }
    }
    return data;
  }
}
```

## 参考资料
* [Java数据结构和算法](https://book.douban.com/subject/1144007/)
