---
layout:     post
title:      【转】唯一索引重复导致插入失败的解决方案
date:       2023-08-28 16:37:02 +0800
postId:     2023-08-28-16-37-02
categories: [MySQL]
keywords:   [database, MySQL]
---
我们知道，mysql 如果设置了主键或者唯一约束，再插入相同的值会报错。
假设表中设置name字段为唯一索引，在存在 name = '张三' 的情况下，再插入"张三"，
```
insert into sc (name,class,score) values ('张三','三年二班',90);
```
报错信息如下：
```
> 1062 - Duplicate entry '张三' for key 'name'
```
那么，对于这种存在唯一键冲突时，如何避免呢？下面，介绍三种方式。

## insert ignore

insert ignore会忽略数据库中已经存在的数据(根据主键或者唯一索引判断)，如果数据库没有数据，就插入新的数据，如果有数据的话就跳过这条数据。

```
insert ignore into sc (name,class,score) values ('张三','三年二班',90)
```
执行上面的语句，会发现并没有报错，但是主键还是自动增长了。

## replace into
* replace into 首先尝试插入数据到表中。如果发现表中已经有此行数据(根据主键或者唯一索引判断)则先删除此行数据，然后插入新的数据，否则，直接插入新数据。
* 使用 replace into，你必须具有delete和insert权限

```
replace into sc (name,class,score) values ('张三','三年二班',90);
```
此时会发现吕布的班级跟年龄都改变了，但是id也变成最新的了，所以不是更新，是删除再新增。

## insert on duplicate key update
* 如果在insert into 语句末尾指定了on duplicate key update，并且插入行后会导致在一个UNIQUE索引或PRIMARY KEY中出现重复值，则在出现重复值的行执行UPDATE；如果不会导致重复的问题，则插入新行，跟普通的insert into一样。
* 使用insert into，你必须具有insert和update权限
* 如果有新记录被插入，则受影响行的值显示1；如果原有的记录被更新，则受影响行的值显示2；如果记录被更新前后值是一样的，则受影响行数的值显示0

```
insert into sc (name,class,score) values ('张三','三年二班',90) on duplicate key update update_time=now();
```
旧数据中张三是三年二班，90分，现在插入，最后发现只有更新时间变成了最新时间，其他核心字段未改变。
id没有发生变化，数据只更新，但是auto_increment还是增长1了。

## 结论
这三种方法都能避免主键或者唯一索引重复导致的插入失败问题，但自增id都会变化：
* `insert ignore` 能忽略重复数据，只插入不重复的数据，但是id会自增，下一条数据的id会不连贯
* `insert … on duplicate key update` 在遇到重复行时，会直接更新原有的行，具体更新哪些字段怎么更新，取决于update后的语句，但是id会自增，下一条数据的id会不连贯
* **【慎用】** `replace into` 是删除原有的行后，再插入新行，如有自增id，重复数据的id会变成最新的id，如果有其他表使用此id作为外键，就会出现关联数据错误的问题


## 参考资料
* [【转】唯一索引重复导致插入失败的解决方案]({% post_url database/mysql/content/2023-08-28-03-insert-with-unique-index %})
* [mysql如何避免主键或者唯一索引重复导致的插入失败问题](https://blog.csdn.net/u012660464/article/details/117416047)

开始写作吧
```
![image-alter]({{ site.baseurl }}/image/post/2023/08/28/03/xxx.png)
```
