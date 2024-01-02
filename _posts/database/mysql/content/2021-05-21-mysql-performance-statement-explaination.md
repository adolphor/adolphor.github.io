---
layout:     post
title:      MySQL 性能调优 - SQL语句解析
date:       2021-05-21 10:28:35 +0800
postId:     2021-05-21-10-28-35
categories: [MySQL]
keywords:   [database,MySQL]
---
MySQL 性能调优的主要手段，就是分析SQL语句，查看是否使用了索引，是否有回表操作。使用的主要关键字
就是 **explain**， 分析结果主要关注 type 类型、索引 key、以及 Extra 信息。

## 准备工作

本文章所有执行代码均在版本为 `mysql: stable 8.0.23 (bottled)` 的MySQL上执行。

使用 **`mysql -uroot -p'Adolphor!@#123'`** 登录本地MySQL，初始化语句如下：
```mysql
create table recipe (
    id int auto_increment primary key,
    name varchar(32) null,
    cook_time int null comment '烹饪时间',
    introduction varchar(128) null comment '食谱介绍',
    tips varchar(128) null comment '小贴士',
    user_id int null comment '作者ID',
    constraint recipe_id_uindex unique (id)
);
INSERT INTO recipe (id, name, cook_time, introduction, tips, user_id) VALUES (1, '番茄炒蛋', 20, '番茄炒蛋是一道老少皆宜的家常菜', '番茄切得越小越容易出汁~', 1);
INSERT INTO recipe (id, name, cook_time, introduction, tips, user_id) VALUES (2, '酸汤肥牛', 30, '清爽可口，米饭连吃三大碗', '肥牛要过水，不然有点腥哦~', 2);
create table recipe_step (
    id int auto_increment primary key,
    recipe_id int null comment '食谱ID',
    seq int null comment '序号',
    content varchar(256) null comment '具体步骤描述',
    user_id int null,
    constraint recipe_step_id_uindex unique (id)
);
create index recipe_step_recipe_id_index on recipe_step (recipe_id);
INSERT INTO recipe_step (id, recipe_id, seq, content, user_id) VALUES (1, 1, 1, '番茄切小块，鸡蛋打散 备用', 1);
INSERT INTO recipe_step (id, recipe_id, seq, content, user_id) VALUES (2, 1, 2, '起锅热油，加入打散的鸡蛋，翻炒成块后出锅待用', 1);
INSERT INTO recipe_step (id, recipe_id, seq, content, user_id) VALUES (3, 1, 3, '再次起锅热油，加入番茄，炒出汁', 1);
INSERT INTO recipe_step (id, recipe_id, seq, content, user_id) VALUES (4, 1, 4, '加入炒好的鸡蛋，加入食盐，翻炒入味后即可出锅食用', 1);
INSERT INTO recipe_step (id, recipe_id, seq, content, user_id) VALUES (5, 2, 1, '油，锅里倒~', 1);
INSERT INTO recipe_step (id, recipe_id, seq, content, user_id) VALUES (6, 2, 2, '番茄粒、青椒粒锅里倒~', 1);
INSERT INTO recipe_step (id, recipe_id, seq, content, user_id) VALUES (7, 2, 3, '肥牛锅里倒~', 1);
INSERT INTO recipe_step (id, recipe_id, seq, content, user_id) VALUES (8, 2, 4, '巴拉巴拉，出锅~', 1);
create table user (
    id int auto_increment primary key,
    name varchar(30) null,
    age tinyint null,
    constraint user_id_uindex unique (id)
);
INSERT INTO user (id, name, age) VALUES (1, '古时的风筝', 10);
INSERT INTO user (id, name, age) VALUES (2, '可爱的梦奇', 8);
create table user_balance (
    user_id int not null primary key,
    balance int null
);
INSERT INTO user_balance (user_id, balance) VALUES (1, 100);
INSERT INTO user_balance (user_id, balance) VALUES (2, 200);
INSERT INTO user_balance (user_id, balance) VALUES (3, 300);
INSERT INTO user_balance (user_id, balance) VALUES (4, 400);
INSERT INTO user_balance (user_id, balance) VALUES (5, 500);
```

## 关键字
describe 和 explain 是一个同义词，都是查询相关相关信息的具体描述，
而且都可以用来查询表和列信息，但也有一部分不同：

### describe
描述表和列等信息，等价于 `show` 关键字的使用
```mysql
describe user;
show full columns from user;
```

```
+-------+-------------+------+-----+---------+----------------+
| Field | Type        | Null | Key | Default | Extra          |
+-------+-------------+------+-----+---------+----------------+
| id    | int         | NO   | PRI | NULL    | auto_increment |
| name  | varchar(30) | YES  |     | NULL    |                |
| age   | tinyint     | YES  |     | NULL    |                |
+-------+-------------+------+-----+---------+----------------+
3 rows in set (0.00 sec)
```

### explain
描述SQL语句相关信息，也可以描述表信息。
它向我们展示了mysql接收到一条sql语句的执行计划，根绝explain返回的结果可以知道sql写的怎样。

```mysql
explain user;
explain select * from user;
```

```
+----+-------------+-------+------------+------+---------------+------+---------+------+------+----------+-------+
| id | select_type | table | partitions | type | possible_keys | key  | key_len | ref  | rows | filtered | Extra |
+----+-------------+-------+------------+------+---------------+------+---------+------+------+----------+-------+
|  1 | SIMPLE      | user  | NULL       | ALL  | NULL          | NULL | NULL    | NULL |    2 |   100.00 | NULL  |
+----+-------------+-------+------------+------+---------------+------+---------+------+------+----------+-------+
1 row in set, 1 warning (0.00 sec)
```

## MySQL优化器
sql在执行时，并不一定就会按照我们写的顺序执行，mysql优化器会重写sql，如何才能看到sql优化器
重写后的sql呢？这就要用到 **explain extended** 和 **show warnings** 了。
```mysql
EXPLAIN <你的SQL>;
SHOW WARNINGS;
```
其中第一句EXPLAIN是查看SQL执行计划的语句，通常用来查看MySQL如何使用索引来处理SELECT语句以及连接表。

第二句SHOW WARNINGS表示显示上一句的警告，所以一定要和上一句一起运行，我们要查看的优化后语句就在
SHOW WARNINGS 运行后显示的 result 的 message 字段里。

## exlpain详解


### id
select查询的序列号，包含一组数字，表示查询中执行select字句或操作表的顺序。 其中id的取值分为三种情况：

* id相同：执行顺序由上往下
* id不同：如果是子查询，id的序号会递增，id值越大优先级越高，越先被执行
* id相同不同，同时存在：大的先执行，相同的从上往下执行

#### id相同
非嵌套查询的时候（inner join 或者 隐式 inner join），可以看到id相同：

```mysql
explain
select rcp.name, rcp.introduction, rcp.tips, u.name author, group_concat(rcps.content) steps
from recipe rcp,
     recipe_step rcps,
     user u
where rcp.id = rcps.recipe_id
  and rcp.user_id = u.id
  and rcp.id = 1;
```

```
+----+-------------+-------+------------+-------+--------------------------+---------+---------+-------+------+----------+-------------+
| id | select_type | table | partitions | type  | possible_keys            | key     | key_len | ref   | rows | filtered | Extra       |
+----+-------------+-------+------------+-------+--------------------------+---------+---------+-------+------+----------+-------------+
|  1 | SIMPLE      | rcp   | NULL       | const | PRIMARY,recipe_id_uindex | PRIMARY | 4       | const |    1 |   100.00 | NULL        |
|  1 | SIMPLE      | u     | NULL       | const | PRIMARY,user_id_uindex   | PRIMARY | 4       | const |    1 |   100.00 | NULL        |
|  1 | SIMPLE      | rcps  | NULL       | ALL   | NULL                     | NULL    | NULL    | NULL  |    8 |    12.50 | Using where |
+----+-------------+-------+------------+-------+--------------------------+---------+---------+-------+------+----------+-------------+
3 rows in set, 1 warning (0.00 sec)
```
#### id不同
嵌套查询的时候，如果外层条件使用 `=` 匹配子查询，那么会看到id不同：

```mysql
explain
select *
from recipe_step
where recipe_id = (
    select id
    from recipe
    where name = '番茄炒蛋'
)
order by seq asc;
```
```
+----+-------------+-------------+------------+------+---------------+------+---------+------+------+----------+-----------------------------+
| id | select_type | table       | partitions | type | possible_keys | key  | key_len | ref  | rows | filtered | Extra                       |
+----+-------------+-------------+------------+------+---------------+------+---------+------+------+----------+-----------------------------+
|  1 | PRIMARY     | recipe_step | NULL       | ALL  | NULL          | NULL | NULL    | NULL |    8 |    12.50 | Using where; Using filesort |
|  2 | SUBQUERY    | recipe      | NULL       | ALL  | NULL          | NULL | NULL    | NULL |    2 |    50.00 | Using where                 |
+----+-------------+-------------+------------+------+---------------+------+---------+------+------+----------+-----------------------------+
2 rows in set, 1 warning (0.00 sec)
```

#### 部分相同
如果有一部分相同，有一部分不同，那么会按照id值大小执行，再按照相等id顺序执行。略。
也就是 id值如果相同，可以认为是一组，从上往下顺序执行；在所有组中，id值越大，优先级越高，越先执行。

### select_type

字段select_type的取值有如下几种:

* SIMPLE
* PRIMARY
* SUBQUERY
* DERIVED
* UNION
* UNION RESULT

#### SIMPLE
简单的select查询，查询中不包含子查询或者UNION；但如果含有inner join查询，依然是SIMPLE类型。
范例参考上面介绍explain关键时的单表select基础查询，以及id相同时的隐式 inner join 查询。

#### PRIMARY
查询中若包含任何复杂的子部分，最外层查询则被标记为PRIMARY。
参考介绍id不同时的嵌套查询，最外层是PRIMARY，内层是SUBQUERY。

#### SUBQUERY
在select或where列表中包含了子查询。
参考介绍id不同时的嵌套查询，最外层是PRIMARY，内层是SUBQUERY。
？但如果在select中使用子查询的时候，出来的是 `DEPENDENT SUBQUERY`？

```mysql
explain select rcps.*,
       (select u.name from user u where u.id=rcps.user_id) author
from recipe_step rcps
where recipe_id = (
    select id
    from recipe
    where name = '番茄炒蛋'
)
order by seq asc;
```

```
+----+--------------------+--------+------------+--------+------------------------+---------+---------+-----------------------+------+----------+-----------------------------+
| id | select_type        | table  | partitions | type   | possible_keys          | key     | key_len | ref                   | rows | filtered | Extra                       |
+----+--------------------+--------+------------+--------+------------------------+---------+---------+-----------------------+------+----------+-----------------------------+
|  1 | PRIMARY            | rcps   | NULL       | ALL    | NULL                   | NULL    | NULL    | NULL                  |    8 |    12.50 | Using where; Using filesort |
|  3 | SUBQUERY           | recipe | NULL       | ALL    | NULL                   | NULL    | NULL    | NULL                  |    2 |    50.00 | Using where                 |
|  2 | DEPENDENT SUBQUERY | u      | NULL       | eq_ref | PRIMARY,user_id_uindex | PRIMARY | 4       | adolphor.rcps.user_id |    1 |   100.00 | NULL                        |
+----+--------------------+--------+------------+--------+------------------------+---------+---------+-----------------------+------+----------+-----------------------------+
3 rows in set, 2 warnings (0.00 sec)
```

#### DERIVED
在from列表中包含的子查询被标记为DERIVER(衍生)，Mysql会递归执行这些子查询，把结果放在临时表中。
？并不是所有的子查询都会标记位DERIVER？

会标记为DERIVER的范例：
```mysql
explain
select *
from (
         select rcps.*,
                (select u.name from user u where u.id = rcps.user_id) author
         from recipe_step rcps
         where recipe_id = (
             select id
             from recipe
             where name = '番茄炒蛋'
         )
         order by seq asc
     ) temp;
```

```
+----+--------------------+------------+------------+--------+------------------------+---------+---------+-----------------------+------+----------+-----------------------------+
| id | select_type        | table      | partitions | type   | possible_keys          | key     | key_len | ref                   | rows | filtered | Extra                       |
+----+--------------------+------------+------------+--------+------------------------+---------+---------+-----------------------+------+----------+-----------------------------+
|  1 | PRIMARY            | <derived2> | NULL       | ALL    | NULL                   | NULL    | NULL    | NULL                  |    2 |   100.00 | NULL                        |
|  2 | DERIVED            | rcps       | NULL       | ALL    | NULL                   | NULL    | NULL    | NULL                  |    8 |    12.50 | Using where; Using filesort |
|  4 | SUBQUERY           | recipe     | NULL       | ALL    | NULL                   | NULL    | NULL    | NULL                  |    2 |    50.00 | Using where                 |
|  3 | DEPENDENT SUBQUERY | u          | NULL       | eq_ref | PRIMARY,user_id_uindex | PRIMARY | 4       | adolphor.rcps.user_id |    1 |   100.00 | NULL                        |
+----+--------------------+------------+------------+--------+------------------------+---------+---------+-----------------------+------+----------+-----------------------------+
4 rows in set, 2 warnings (0.01 sec)
```

不会标记为DERIVER的范例：
```mysql
explain
select *
from (
         select rcps.*
         from recipe_step rcps
         where recipe_id = (
             select id
             from recipe
             where name = '番茄炒蛋'
         )
         order by seq asc
     ) temp;
```

```
+----+-------------+--------+------------+------+---------------+------+---------+------+------+----------+-----------------------------+
| id | select_type | table  | partitions | type | possible_keys | key  | key_len | ref  | rows | filtered | Extra                       |
+----+-------------+--------+------------+------+---------------+------+---------+------+------+----------+-----------------------------+
|  1 | PRIMARY     | rcps   | NULL       | ALL  | NULL          | NULL | NULL    | NULL |    8 |    12.50 | Using where; Using filesort |
|  3 | SUBQUERY    | recipe | NULL       | ALL  | NULL          | NULL | NULL    | NULL |    2 |    50.00 | Using where                 |
+----+-------------+--------+------------+------+---------------+------+---------+------+------+----------+-----------------------------+
2 rows in set, 1 warning (0.00 sec)
```

#### UNION
若第二个select出现在UNION之后，则被标记为 `UNION`； 若union包含在from字句的查询中，外层select将被标记为 `DERIVER`。

```mysql
explain
select id, name
from recipe
where id = 1
union
select id, content
from recipe_step
where recipe_id = 1;
```

```
+------+--------------+-------------+------------+-------+--------------------------+---------+---------+-------+------+----------+-----------------+
|  id  | select_type  | table       | partitions | type  | possible_keys            | key     | key_len | ref   | rows | filtered | Extra           |
+------+--------------+-------------+------------+-------+--------------------------+---------+---------+-------+------+----------+-----------------+
|   1  | PRIMARY      | recipe      | NULL       | const | PRIMARY,recipe_id_uindex | PRIMARY | 4       | const |    1 |   100.00 | NULL            |
|   2  | UNION        | recipe_step | NULL       | ALL   | NULL                     | NULL    | NULL    | NULL  |    8 |    12.50 | Using where     |
| NULL | UNION RESULT | <union1,2>  | NULL       | ALL   | NULL                     | NULL    | NULL    | NULL  | NULL |     NULL | Using temporary |
+------+--------------+-------------+------------+-------+--------------------------+---------+---------+-------+------+----------+-----------------+
3 rows in set, 1 warning (0.00 sec)
```

#### UNION RESULT
从UNION表获取结果的select。
参考上面的UNION。

### table
显示这一行的数据是关于哪张表的，也可能是临时表数据

### partitions


### type
类型，官方全程“join type”,意思是“连接类型”，注意这里不是字面意思两表之间的链接，确切说是
数据库引擎查找表的一种方式，在《高性能mysql》一书中作者更是觉得称呼它为访问类型更贴切一些；
type的类型达到了14种之多，这里只记录和理解最重要且经常遇见的六种类型，它们分别如下，从左到
右，它们的效率依次是增强的：

* all
* index
* range
* ref
* eq_ref
* const

#### all

Full Table Scan 全表扫描，如果只是查找一个数据项的sql出现了all类型，代表sql处于一种最
原始的状态，有很大的优化空间，就好比一万个人中找一个人，只能挨个找一遍：

```mysql
explain
select *
from recipe
where tips like '%番茄%';
```
```
+----+-------------+--------+------------+------+---------------+------+---------+------+------+----------+-------------+
| id | select_type | table  | partitions | type | possible_keys | key  | key_len | ref  | rows | filtered | Extra       |
+----+-------------+--------+------------+------+---------------+------+---------+------+------+----------+-------------+
|  1 | SIMPLE      | recipe | NULL       | ALL  | NULL          | NULL | NULL    | NULL |    2 |    50.00 | Using where |
+----+-------------+--------+------------+------+---------------+------+---------+------+------+----------+-------------+
1 row in set, 1 warning (0.00 sec)
```

#### index
Full Index Scan，index与All的区别为index类型只遍历索引树。这通常比All快，因为索引文件
通常比数据文件小。（也就是说虽然all和index都是读全表,但index是从索引中读取的，而all是从
硬盘中读取的）

比如：
```mysql
explain select id from recipe;
```
？因为id是主键索引，所以查询id的时候是覆盖索引的情况，那么不需要进行回表，查询的就是index类型；

```
+----+-------------+--------+------------+-------+---------------+------------------+---------+------+------+----------+-------------+
| id | select_type | table  | partitions | type  | possible_keys | key              | key_len | ref  | rows | filtered | Extra       |
+----+-------------+--------+------------+-------+---------------+------------------+---------+------+------+----------+-------------+
|  1 | SIMPLE      | recipe | NULL       | index | NULL          | recipe_id_uindex | 4       | NULL |    2 |   100.00 | Using index |
+----+-------------+--------+------------+-------+---------------+------------------+---------+------+------+----------+-------------+
1 row in set, 1 warning (0.02 sec)
```

？但如果查询索引中没有的字段，那么就需要回表二次查询，那么type就是ALL类型了，
```mysql
explain select * from recipe;
```
```
+----+-------------+--------+------------+------+---------------+------+---------+------+------+----------+-------+
| id | select_type | table  | partitions | type | possible_keys | key  | key_len | ref  | rows | filtered | Extra |
+----+-------------+--------+------------+------+---------------+------+---------+------+------+----------+-------+
|  1 | SIMPLE      | recipe | NULL       | ALL  | NULL          | NULL | NULL    | NULL |    2 |   100.00 | NULL  |
+----+-------------+--------+------------+------+---------------+------+---------+------+------+----------+-------+
1 row in set, 1 warning (0.02 sec)
```

另外一种场景，如果where条件中增加了id作为查询条件，那么type的类型就变成了const：
```mysql
explain select id from recipe where id=1;
```
```
+----+-------------+--------+------------+-------+--------------------------+---------+---------+-------+------+----------+-------------+
| id | select_type | table  | partitions | type  | possible_keys            | key     | key_len | ref   | rows | filtered | Extra       |
+----+-------------+--------+------------+-------+--------------------------+---------+---------+-------+------+----------+-------------+
|  1 | SIMPLE      | recipe | NULL       | const | PRIMARY,recipe_id_uindex | PRIMARY | 4       | const |    1 |   100.00 | Using index |
+----+-------------+--------+------------+-------+--------------------------+---------+---------+-------+------+----------+-------------+
1 row in set, 1 warning (0.00 sec)
```

#### range
只索引给定范围的行，使用一个索引来选择行。key列显示使用了哪个索引，一般就是在你
的where语句中出现了 between、< 、>、in等查询，这种范围扫描索引扫描比权标扫描要
好，因为它只需要开始与索引的某一点，而结束于另一点，不用扫描全部索引。

```mysql
explain select * from recipe where id > 1 and id < 3;
explain select * from recipe where id between 1 and 3;
```
```
+----+-------------+--------+------------+-------+--------------------------+---------+---------+------+------+----------+-------------+
| id | select_type | table  | partitions | type  | possible_keys            | key     | key_len | ref  | rows | filtered | Extra       |
+----+-------------+--------+------------+-------+--------------------------+---------+---------+------+------+----------+-------------+
|  1 | SIMPLE      | recipe | NULL       | range | PRIMARY,recipe_id_uindex | PRIMARY | 4       | NULL |    2 |   100.00 | Using where |
+----+-------------+--------+------------+-------+--------------------------+---------+---------+------+------+----------+-------------+
1 row in set, 1 warning (0.05 sec)
```

#### ref
非唯一性索引扫描，返回匹配某个单独值的所有行。本质上也是一种索引访问，它返回所有
匹配某个单独值的行，然而，它会可能找到多个符合条件的行，所以他应该属于查找和扫描
的混合体

```mysql
explain select * from recipe_step where recipe_id=1;
```
```
+----+-------------+-------------+------------+------+-----------------------------+-----------------------------+---------+-------+------+----------+-------+
| id | select_type | table       | partitions | type | possible_keys               | key                         | key_len | ref   | rows | filtered | Extra |
+----+-------------+-------------+------------+------+-----------------------------+-----------------------------+---------+-------+------+----------+-------+
|  1 | SIMPLE      | recipe_step | NULL       | ref  | recipe_step_recipe_id_index | recipe_step_recipe_id_index | 5       | const |    4 |   100.00 | NULL  |
+----+-------------+-------------+------------+------+-----------------------------+-----------------------------+---------+-------+------+----------+-------+
1 row in set, 1 warning (0.00 sec)
```

#### eq_ref
唯一索引扫描，对于每个索引键，表中只有一条记录与之匹配。常见于主键或唯一索引扫描。
```mysql
explain select u.id,u.name,u.age,ub.balance from user u left join user_balance ub on u.id=ub.user_id;
```

```
+----+-------------+-------+------------+--------+---------------+---------+---------+---------------+------+----------+-------+
| id | select_type | table | partitions | type   | possible_keys | key     | key_len | ref           | rows | filtered | Extra |
+----+-------------+-------+------------+--------+---------------+---------+---------+---------------+------+----------+-------+
|  1 | SIMPLE      | u     | NULL       | ALL    | NULL          | NULL    | NULL    | NULL          |    2 |   100.00 | NULL  |
|  1 | SIMPLE      | ub    | NULL       | eq_ref | PRIMARY       | PRIMARY | 4       | adolphor.u.id |    1 |   100.00 | NULL  |
+----+-------------+-------+------------+--------+---------------+---------+---------+---------------+------+----------+-------+
2 rows in set, 1 warning (0.00 sec)
```

上例中对于前表user表中的每一行（row），对应后user_balance表只有一行被扫描，这类扫描的速度也非常的快

#### const
表示通过索引一次就找到了，const用于比较primary key或unique索引。因为只匹配一行数据，所以很快。如将
主键置于where列表中，Mysql就能将该查询转化为一个常量。
```mysql
explain select * from recipe where id=2;
```
```
+----+-------------+--------+------------+-------+--------------------------+---------+---------+-------+------+----------+-------+
| id | select_type | table  | partitions | type  | possible_keys            | key     | key_len | ref   | rows | filtered | Extra |
+----+-------------+--------+------------+-------+--------------------------+---------+---------+-------+------+----------+-------+
|  1 | SIMPLE      | recipe | NULL       | const | PRIMARY,recipe_id_uindex | PRIMARY | 4       | const |    1 |   100.00 | NULL  |
+----+-------------+--------+------------+-------+--------------------------+---------+---------+-------+------+----------+-------+
1 row in set, 1 warning (0.09 sec)
```

### possible_keys

### key

### key_len

### ref

### rows

### filtered

### Extra



## 参考资料

* [mysql 中的explain关键字](https://juejin.cn/post/6844903694505508878)
* [Mysql之EXPLAIN关键字学习笔记](https://zhuanlan.zhihu.com/p/101087603)
* [MySQL 8.0 Reference Manual - DESCRIBE Statement](https://dev.mysql.com/doc/refman/8.0/en/describe.html)

