---
layout:     post
title:      MySQL 命令行基础指令
date:       2021-05-19 13:51:04 +0800
postId:     2021-05-19-13-51-04
categories: [database]
tags:       [database,MySQL]
geneMenu:   true
excerpt:    MySQL 命令行基础指令
---

## 登录连接

```bash
mysql -uroot -p'Adolphor!@#123'
```

## 数据库

### 创建数据库

### 切换数据库
```mysql
# 使用 adolphor 这个库
use adophor;
```

## 数据库编码

### 数据库编码
```

```

### 表编码

## 事务

```mysql
SELECT @@autocommit;
show variables like 'autocommit';
```

### 手动管理事务
```mysql
begin; # begin 回车后并不会立即开启事务，而是在执行真正的CRUD语句后才开启事务
commit; # 提交事务，操作生效并持久化
rollback; # 回滚，撤销所有操作（可能出现脏读）
```

### 事务等级

## 参考资料

* [test](test.html)