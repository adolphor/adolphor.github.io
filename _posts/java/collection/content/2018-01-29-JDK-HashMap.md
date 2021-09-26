---
layout:     post
title:      Map框架之HashMap
date:       2018-01-29 23:49:35 +0800
postId:     2018-01-29-23-49-35
categories: [collection]
keywords:   [Java, collection]
---

## 数据结构

哈希表

## 工具类

Node节点类，




Map 接口中有一个内部接口 Entry，在AbstractMap中有两个内部实现 SimpleEntry 和 SimpleImmutableEntry 静态类，而且在 HashMap中又自己
实现了一个 Entry类，继承自原始的Map.Entry。那么：为什么要使用内部接口？ 抽象类中的实现类有什么用处？


## 参考资料

* [Java 8系列之重新认识HashMap](https://tech.meituan.com/java-hashmap.html)
* [HashMap(JDK1.8)源码+底层数据结构分析](https://snailclimb.gitee.io/javaguide/#/docs/java/collection/HashMap(JDK1.8)源码+底层数据结构分析)