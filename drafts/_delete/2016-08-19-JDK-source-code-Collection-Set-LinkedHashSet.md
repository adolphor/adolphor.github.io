---
layout:     post
title:      Collection框架之LinkedHashSet
date:       2016-08-19 23:00:28 +0800
postId:     2016-08-19-23-00-28
categories: [blog]
tags:       [Java]
geneMenu:   true
excerpt:    Collection框架之LinkedHashSet
---

## LinkedHashSet
Since 1.4，LinkedHashSet 继承自 HashSet，两者之间最大的区别就是
HashSet 默认使用 HashMap 作为存储容器，LinkedHashSet 使用 LinkedHashMap
作为存储容器，除了下面的 spliterator 方法不同之外，其余所有的方法都相同，
都是直接调用的map的原生实现。

## 接口实现

### Spliterator<E> spliterator()
```java
public Spliterator<E> spliterator() {
    return Spliterators.spliterator(this, Spliterator.DISTINCT | Spliterator.ORDERED);
}
```

## 接口继承

### HashSet
add, clear, clone, contains, isEmpty, iterator, remove, size

### AbstractSet
equals, hashCode, removeAll

### AbstractCollection
addAll, containsAll, retainAll, toArray, toArray, toString

## 参考资料

* [JDK文档 之 LinkedHashSet](https://docs.oracle.com/javase/8/docs/api/java/util/LinkedHashSet.html)
