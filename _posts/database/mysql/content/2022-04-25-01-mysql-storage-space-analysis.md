---
layout:     post
title:      MySQL - 存储空间占用分析
date:       2022-04-25 17:06:04 +0800
postId:     2022-04-25-17-06-04
categories: [database]
keywords:   [database,MySQL]
---

MySQL 数据库存储空间使用情况分析，为优化提供依据。

## 数据库占用分析
```mysql
SELECT table_schema                                 as '数据库',
       sum(table_rows)                              as '记录数',
       sum(truncate(data_length / 1024 / 1024, 2))  as '数据容量(MB)',
       sum(truncate(index_length / 1024 / 1024, 2)) as '索引容量(MB)',
       sum(truncate(DATA_FREE / 1024 / 1024, 2))    as '碎片占用(MB)'
from information_schema.tables
group by table_schema
order by sum(data_length) desc, sum(index_length) desc;
```

## 表占用分析
```mysql
SELECT table_schema                            as '数据库',
       table_name                              as '表名',
       table_rows                              as '记录数',
       truncate(data_length / 1024 / 1024, 2)  as '数据容量(MB)',
       truncate(index_length / 1024 / 1024, 2) as '索引容量(MB)',
       truncate(DATA_FREE / 1024 / 1024, 2)    as '碎片占用(MB)'
from information_schema.tables
where table_schema = 'adolphor'
order by data_length desc, index_length desc;
```

## 其他
```
![image-alter]({{ site.baseurl }}/image/post/2022/04/25/01/xxx.jpg)
```

## 参考资料
* [MySQL - 存储空间占用分析]({% post_url database/mysql/content/2022-04-25-01-mysql-storage-space-analysis %})
