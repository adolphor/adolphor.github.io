---
layout:     post
title:      博文目录汇总
date:       1997-01-01 17:18:40 +0800
postId:     1997-01-01-17-18-40
categories: []
keywords:   []
topmost:    true
---

## 数据结构和算法
[数据结构和算法 汇总目录]({% post_url algorithm/2020-01-20-data-structures-and-algorithm-menus %})

### 时间复杂度
* [时间复杂度]({% post_url algorithm/classic/2021-03-30-01-time-complexity %})

### 数组
* [无序数组]({% post_url algorithm/classic/2021-03-30-02-array %}#无序数组)
* [有序数组]({% post_url algorithm/classic/2021-03-30-02-array %}#有序数组)

### 简单排序
* [冒泡排序]({% post_url algorithm/classic/2021-03-31-simple-sorting %}#冒泡排序)
* [选择排序]({% post_url algorithm/classic/2021-03-31-simple-sorting %}#选择排序)
* [插入排序]({% post_url algorithm/classic/2021-03-31-simple-sorting %}#插入排序)

### 栈和队列
* [栈]({% post_url algorithm/classic/2021-04-07-stack %})
* [队列]({% post_url algorithm/classic/2021-04-10-queue %})
* [中缀、后缀 表达式及计算]({% post_url algorithm/classic/2021-04-13-infix-postfix-expression %})

### 链表
* [链表]({% post_url algorithm/classic/2021-04-14-01-link %})
* [单链表]({% post_url algorithm/classic/2021-04-14-02-single-ended-list %})
* [双端链表]({% post_url algorithm/classic/2021-04-14-03-double-ended-list %})
* [使用链表实现栈和队列]({% post_url algorithm/classic/2021-04-14-04-stack-queue-by-link %})
* [有序链表]({% post_url algorithm/classic/2021-04-15-01-sorted-link %})
* [双向链表]({% post_url algorithm/classic/2021-04-15-02-doubly-link %})
* [迭代器]({% post_url algorithm/classic/2021-04-15-03-link-iterator %})

### 分治算法
* [递归]({% post_url algorithm/classic/2021-04-15-04-recursion %})
* 归并排序：[参考](https://zhuanlan.zhihu.com/p/95080265)

### 高级排序
* [希尔排序]({% post_url algorithm/classic/2021-04-23-01-advanced-sorting %}#希尔排序)
* [快速排序]({% post_url algorithm/classic/2021-04-23-01-advanced-sorting %}#快速排序)
* [基数排序]({% post_url algorithm/classic/2021-04-23-01-advanced-sorting %}#基数排序)

### 树
* [B树]({% post_url algorithm/classic/2021-07-13-01-binary-tree %})
* [B+树]({% post_url algorithm/classic/2021-07-16-01-b+tree %})
* [红黑树]({% post_url algorithm/classic/2021-07-16-02-red-black-tree %})

## Java基础
* [Java基础目录汇总]({% post_url java/basic/2016-08-22-java-basic-menus %})
* [Java基本概念：JVM & JDK & JRE]({% post_url java/basic/content/2021-07-03-00-java-basic-knowledge %})
* 面向对象
    - [类]({% post_url java/basic/content/2021-07-03-10-java-basic-class %})
    - [方法]({% post_url java/basic/content/2021-07-03-11-java-basic-method %})
* 关键字
    - [final]({% post_url java/basic/content/2021-08-22-01-static-keyword %})
    - [static]({% post_url java/basic/content/2021-08-22-01-static-keyword %})
* JDK 类
    - [String]({% post_url java/basic/content/2021-07-03-01-java-basic-string %})
* 集合
    - [Collection继承图]({% post_url java/collection/content/2016-08-03-JDK-Collection %})
    - [ArrayList]({% post_url java/collection/content/2016-08-11-JDK-ArrayList %})
    - [LinkedList]({% post_url java/collection/content/2016-08-16-JDK-LinkedList %})
* [Annotation 注解]({% post_url java/basic/content/2016-09-27-annotation-tutorial %})
* [异常体系]({% post_url java/basic/content/2021-07-03-17-java-basic-exception %})
* lambda
    - [lambda表达式]({% post_url java/lambda/2016-08-24-Java8-features-lambda-expression %})
* [Java class 编译版本相关设置和查看]({% post_url java/jvm/content/2016-08-06-java-class-file-version %})

## JVM

### JVM知识梳理
* [内存区域]({% post_url java/jvm/content/2021-08-23-05-jvm-memory %})
* [Class对象]({% post_url java/jvm/content/2021-07-29-02-jvm-class-object %})
* [引用类型]({% post_url java/jvm/content/2021-08-24-02-jvm-reference-type %})
* [垃圾回收]({% post_url java/jvm/content/2021-08-24-01-jvm-gc %})


### JVM工具介绍
* [jstack工具]({% post_url java/jvm/tools/2021-07-29-01-jvm-jstack %})

## 并发
* [Java并发目录]({% post_url java/concurrent/2020-03-15-java-concurrent-menus %})

### 并发基础知识
* [基本概念]({% post_url java/concurrent/content/2021-07-27-01-concurrent-basic-conception %})
* [同步机制]({% post_url java/concurrent/content/2021-07-27-02-concurrent-synchronization-mechanism %})
    - synchronized 关键字
    - volatile 关键字
    - final 关键字
    - static 关键字
    - Lock 接口
    - Semaphore 类
    - CountDownLatch 类
    - CyclicBarrier 类
    - Phaser 类

### 基本元素
* [Thread 和 Runnable]({% post_url java/concurrent/content/2020-03-22-concurrent-thread-runnable %})
* [synchronized 关键字]({% post_url java/concurrent/content/2021-07-29-01-concurrent-keyword-synchronized %})
* [volatile 关键字]({% post_url java/concurrent/content/2021-07-29-02-concurrent-keyword-volatile %})

### 同步机制
* [Java内存模型]({% post_url java/concurrent/content/2021-08-23-04-concurrent-memory-model %})
* [CAS与原子变量]({% post_url java/concurrent/content/2021-08-06-01-concurrent-cas-atomic %})
* [线程安全的设计方式]({% post_url java/concurrent/content/2021-08-23-03-concurrent-thread-safe-design %})
* [锁类型和演化过程]({% post_url java/concurrent/content/2021-07-29-03-concurrent-lock-type %})

### 线程间协作
* [wait和notify]({% post_url java/concurrent/content/2021-08-22-03-concurrent-wait-notify %})
* [线程中断]({% post_url java/concurrent/content/2021-08-23-01-concurrent-interrupt %})
* [线程停止]({% post_url java/concurrent/content/2021-08-23-02-concurrent-thread-stop %})
* [执行器]({% post_url java/concurrent/content/2021-07-27-03-concurrent-executor %})
    - Executor 接口
    - ExecutorService 接口
    - Callable 接口
    - Future 接口
* [Fork/Join 框架]({% post_url java/concurrent/content/2021-07-27-04-concurrent-fork-join-framework %})

### 并行流
* [并行流]({% post_url java/concurrent/content/2021-07-27-05-concurrent-stream %})

### 并发数据结构
* [并发数据结构]({% post_url java/concurrent/content/2021-07-27-06-concurrent-data-structure %})

### 并发设计模式
* [并发设计模式]({% post_url java/concurrent/content/2021-07-27-07-concurrent-design-patterns %})

## 数据库 MySQL
* [MySQL 汇总目录]({% post_url database/mysql/2021-05-19-mysql-article-menus %})
* [MySQL逻辑架构]({% post_url database/mysql/content/2021-07-13-01-mysql-logical-architecture %})
* [MySQL 锁机制]({% post_url database/mysql/content/2021-07-13-02-mysql-lock-system %})
* [MySQL 多版本并发控制 MVVC]({% post_url database/mysql/content/2021-07-13-03-mysql-mvcc %})
* [MySQL 事务]({% post_url database/mysql/content/2021-05-18-mysql-transaction-isolation %})
* [MySQL 存储引擎]({% post_url database/mysql/content/2021-05-20-mysql-engines %})
* [MySQL 数据类型]({% post_url database/mysql/content/2021-07-13-05-mysql-data-type %})
* [MySQL 索引]({% post_url database/mysql/content/2021-05-20-mysql-index %})
* [MySQL 调优]({% post_url database/mysql/content/2021-07-14-01-mysql-performance-optimization %})
* [MySQL 复制]({% post_url database/mysql/content/2021-07-14-02-mysql-replication %})
* [MySQL - 分表分库组件]({% post_url database/mysql/content/2021-10-02-07-mysql-sharding-component %})

## Netty
* [Netty源码学习]({% post_url framework/netty/2020-02-01-netty-source-menus %})

## Redis
* [Redis源码安装和多服务配置]({% post_url database/redis/content/2016-08-23-Redis-multi-install-and-config %})
* [Redis - 数据结构]({% post_url database/redis/content/2021-06-15-redis-data-structure %})
* [Redis - 事务]({% post_url database/redis/content/2021-10-02-02-redis-transaction %})
* [Redis - 订阅与发布]({% post_url database/redis/content/2021-10-02-03-redis-publish-subscribe %})
* [Redis - 持久化]({% post_url database/redis/content/2021-10-02-04-redis-persistence %})
* [Redis - 分布式锁]({% post_url database/redis/content/2021-10-02-06-redis-distributed-lock %})

## Tomcat
* [Tomcat 优化]({% post_url framework/tomcat/2016-10-11-tomcat-optimizing %})

## MyBatis

## Hadoop & Spark & HBase
* [Hadoop，Spark，HBase 开发环境配置]({% post_url database/hadoop/2016-10-28-hadoop-spark-hbase-develop-environment %})
* [Windows 10 x64 下编译 Hadoop 源码]({% post_url database/hadoop/2016-11-01-build-hadoop-on-windows-10 %})

## 设计模式 [Design pattern]

* [UML中的类图关系]({% post_url tools/uml/2016-08-01-UML-relationship %})
* [6大接口设计原则]({% post_url design-pattern/content/2020-01-21-design-pattern-principle %})
* [3大类24种设计模式]({% post_url design-pattern/2016-11-03-design-pattern-menus %})

## 分布式微服务

## 操作系统

* [Linux 排查进程和线程]({% post_url system/linux/2021-09-28-01-linux-progress-thread %})
* [Linux 磁盘空间分析和删除日志文件]({% post_url system/linux/2021-09-28-02-linux-disk-analysis-delete-log-file %})

## 编译原理

## 读书笔记
* [Effective Java —— Joshua Bloch]({% post_url book/2016-09-20-effective-java-second-edition %})

## Git
* [GIT回滚commit/push到指定版本]({% post_url tools/git/2016-11-03-git-commit-push-roll-back %})
