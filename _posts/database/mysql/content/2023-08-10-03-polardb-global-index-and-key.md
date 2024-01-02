---
layout:     post
title:      PolarDB 全局索引和一般索引
date:       2023-08-10 17:00:06 +0800
postId:     2023-08-10-17-00-06
categories: [MySQL]
keywords:   [database, MySQL]
---
接手的项目中有很多以`g_i_xxxxx`开头的表，看了下PolarDB的索引策略，在这里稍作记录。

## 普通索引

### 索引介绍
每个分区表中都创建自己分区表中数据的索引，在PolarDB中的关键字是 `KEY`。

### 使用场景
多条件查询的场景更有用，因为需要

## 全局索引

### 索引介绍
全局索引相当于创建一个索引表，一般是两个字段：主键ID以及需要创建索引的列，这样相当于只查询一个索引表就可以获取到所有符合条件的主键ID，
避免了去每个分区表中进行搜索的操作。在PolarDB中的关键字是 `GLOBAL INDEX`。

### 使用场景
全局索引更适合单个条件查询的场景，可以通过查找全局索引定位到需要查询的主键ID，然后根据主键ID在分区表中根据ID直接定位查询对应数据。

## 方法借鉴
可以看出来，核心思想就是冗余一分主键ID和索引字段的值，拿空间换时间，而且跟ES的思想有点像。
那么，如果想要在MySQL中实现应该怎么操作呢？
* 先根据哈希算法，进行数据库的分区分表
* 创建索引表，核心两个字段：主键ID、索引字段（索引表其实也可以进行哈希分表，不过是根据索引字段进行哈希，这样直接查询）
* 插入数据的时候，同时插入索引表
* 查询数据的时候，先根据查询条件找对应的索引表，查询符合条件的主键
* 根据符合条件的主键，进行哈希分组，去各个分区表中根据主键进行数据查询
* 将所有分区表中的数据进行排序汇总，并返回

可以参考 [MySQL - 一种简单易懂的 MyBatis 分库分表方案]({% post_url database/mysql/content/2023-08-09-01-mybatis-shard-for-mysql %})，
手动实现相关分表策略，再结合全局索引思想，就可以自己简单实现PolarDB的核心能力了（计算和存储分离除外～）。

不过可以将索引的分区分表查询策略进行公共方法的封装，会更加方便。

## 参考资料
* [PolarDB 全局索引和一般索引]({% post_url database/mysql/content/2023-08-10-03-polardb-global-index-and-key %})
* [全局索引（GLOBAL INDEX）](https://help.aliyun.com/apsara/enterprise/v_3_18_0/polardb/development-guide-1/global-index.html?spm=a2c4g.14484438.10001.98)
* [MySQL - 一种简单易懂的 MyBatis 分库分表方案]({% post_url database/mysql/content/2023-08-09-01-mybatis-shard-for-mysql %})

开始写作吧
```
![image-alter]({{ site.baseurl }}/image/post/2023/08/10/03/xxx.png)
```
