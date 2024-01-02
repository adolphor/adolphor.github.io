---
layout:     post
title:      Collection框架总述
date:       2016-08-03 21:08:24 +0800
postId:     2016-08-03-21-08-24
categories: [Collection]
keywords:   [Java, collection]
---

## Collection实现汇总


Interface   |   Hash Table  |   Resizable Array |   Balanced Tree   |   Linked List     |   Hash Table + Linked List
----|----|----|----|----|----
Set         |   HashSet     |                   |  TreeSet          |                   |   LinkedHashSet   
List        |               |   ArrayList       |                   |   LinkedList      | 
Deque       |               |   ArrayDeque      |                   |   LinkedList      |    
Map         |   HashMap     |                   |  TreeMap          |                   |   LinkedHashMap


##  Collection 单列集合

- List
    * ArrayList
    * LinkedList
- Queue
    * ArrayDeque
    * LinkedList
- Set
    * HashSet
        - LinkedHashSet
    * TreeSet

### 继承关系图示

先看下Java8中Collection各继承和实现关系图，先看下精简之后的关系图：

![Collection类图]({{ site.baseurl }}/image/post/2016/08/03/20160803-Collection01.png)

再看下稍微补充抽象继承类之后的关系图：

![Collection类图]({{ site.baseurl }}/image/post/2016/08/03/20160803-Collection02.png)

下面是Collection集合类的全部关系图：

![Collection类图]({{ site.baseurl }}/image/post/2016/08/03/20160803-Collection03.png)

##  Map 双列集合

- Hashtable
- HashMap
    * LinkedHashMap
- TreeMap

### 继承关系图示


## 参考资料

* [The Collections Framework](https://docs.oracle.com/javase/8/docs/technotes/guides/collections/index.html)  
* [Collections API](https://docs.oracle.com/javase/8/docs/api/java/util/package-summary.html)  

