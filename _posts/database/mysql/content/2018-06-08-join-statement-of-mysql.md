---
layout:     post
title:      MySQL 之 join 语句
date:       2018-06-08 13:20:16 +0800
postId:     2018-06-08-13-20-17
categories: [MySQL]
keywords:   [database,MySQL]
---

我们在日常开发中，时常会面临sql优化的问题，通常情况下，我们总会说join查询比普通的关联查询效率会高很多，
话虽如此，但是为何join查询效率通常情况下比关联查询效率高呢，我们一起探究一下。

## join原理
简单来说，join查询，就类似于java中的for循环，通过两层for循环嵌套，
从而遍历两张表中所有的数据并筛选出符合条件的数据集合，如下图所示：
```java
for (行 左表具体行 : 左表所有行){
    for (行 右表具体行: 右表所有行){
        // 对比条件 on *** = *** 是否满足
        // 满足条件则进行记录
    }
    // 将当前左表数据和右表数据组合，当右表中没有匹配到数据，字段值填充null
    // 如果内for循环有多行记录，则左表数据记录多行匹配结果集
}
// 返回所有的符合条件的行数据集合
```

两层for循环即可遍历两张表的全部数据信息进行比较。首先，我们拿到左表的每一行数据，
然后循环右表数据的每一行，进行条件对比，当满足条件时，则与左表拼装数据记录；并在中间循环层做记载，
当在右表中匹配到多条记录时，则在中间循环层使用同一左表数据与右表符合条件行拼装成多条记录值；
当左表数据与右表全部数据没有匹配到相关结果时，则使用左表数据与右表字段进行拼装为一条记录，
并且右表字段其值为：null

这就是最简单的Simple Nested-Loop Join ，也叫做“简单嵌套循环连接”，当左表有n条记录，
右表有m条数据时，总匹配次数为 n * m ，当数据库表数据量比较大，由于数据库数据是保存在磁盘中，
也就相当于要做很多次的I/O操作，这个过程是相当消耗资源的，所以我们平常在sql语句中的join查询，
肯定不会直接采用这种形式。

**Index Nested-Loop Join 索引嵌套循环连接的触发前提是：查询条件必须命中右表索引。**

## join 类型

![Visual_SQL_JOINS]({{ site.baseurl }}/image/post/2018/06/08/Visual_SQL_JOINS_orig.jpg)

## 参考资料
* [MySQL 之 join 语句]({% post_url database/mysql/content/2018-06-08-join-statement-of-mysql %})
* [Visual Representation of SQL Joins](https://www.codeproject.com/Articles/33052/Visual-Representation-of-SQL-Joins)
* TODO [深入理解mysql数据库join查询](https://zhuanlan.zhihu.com/p/54919968)
