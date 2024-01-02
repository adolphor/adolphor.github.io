---
layout:     post
title:      【转】Mysql 自增主键回溯的坑
date:       2023-08-10 15:06:07 +0800
postId:     2023-08-10-15-06-07
categories: [MySQL]
keywords:   [database, MySQL]
---
使用mysql的时候，很多时候用自增主键。正常使用一般是没有问题的，但是极小概率情况下会碰到主键回溯的问题。在业务上可能造成id一样，但是对应的业务数据不一样的问题。这个问题发生在Mysql 8.0版本之前。

## 出现场景
插入一条数据返回主键3， 接着删除了主键为3的数据，这个时候突然数据库宕机了，重启之后再进行插入，返回的主键还是3。正常这个时候应该是返回主键4的，与预期不一致，对业务数据会造成不一致。

## 模拟场景

### 建一张表
```shell
mysql> CREATE TABLE t (
    ->      a INT AUTO_INCREMENT PRIMARY KEY,
    ->      b VARCHAR(255) default null
    ->  );
Query OK, 0 rows affected (0.03 sec)
```

### 插入数据
```shell
mysql> insert into t(`b`) values ('xxx'),('yyy'),('zzz');
Query OK, 3 rows affected (0.01 sec)
Records: 3  Duplicates: 0  Warnings: 0
```

### 查看插入的数据
```shell
mysql> select * from t;
+---+------+
| a | b    |
+---+------+
| 1 | xxx  |
| 2 | yyy  |
| 3 | zzz  |
+---+------+
3 rows in set (0.00 sec)
```

### 删除主键为3的数据
```shell
mysql> delete from t where a=3;
Query OK, 1 row affected (0.01 sec)
```

### 主键回溯复现
这个时候如果直接插入数据，返回的主键是4，这是正常的行为。但是如果这个时候突然宕机了，重启再插入数据，返回的主键还是3。这里只模拟异常情况，将mysql服务器重启，然后再插入数据。
```shell
mysql> insert into t (`b`) values ('new_line');
Query OK, 1 row affected (0.00 sec)

mysql> select * from t;
+---+----------+
| a | b        |
+---+----------+
| 1 | xxx      |
| 2 | yyy      |
| 3 | new_line |
+---+----------+
3 rows in set (0.00 sec)
```
可以看到刚插入的数据，主键还是3。

## 原因解释
MySQL 8.0 版本前，自增不持久化，数据库重启时会发生主键回溯。

## 解决方法
* 升级 MySQL 版本到 8.0 版本，每张表的自增值会持久化；
* 若无法升级数据库版本，则强烈不推荐在核心业务表中使用自增数据类型做主键。

## 造成影响
如果前端插入一条user的数据，返回给前端一个id，这个时候如果有客户端删除了这个id对应的数据，刚好宕机，重启后又插入一条新的user数据，那么会覆盖掉之前的user。这是非常严重的业务数据错乱，造成生产事故的。


## 参考资料
* [【转】Mysql 自增主键回溯的坑]({% post_url database/mysql/content/2023-08-10-02-mysql-auto-increment-id-bug %})
* [Mysql 自增主键回溯的坑](https://www.cnblogs.com/xstar-website/p/14987476.html)

