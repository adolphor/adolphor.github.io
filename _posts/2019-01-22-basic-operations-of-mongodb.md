---
layout:     post
title:      mongodb数据库基本操作
date:       2019-01-22 20:51:31 +0800
postId:     2019-01-22-20-51-31
categories: []
tags:       [mongodb]
geneMenu:   true
excerpt:    mongodb数据库基本操作
---

## 登录

```bash

```

## 数据库导入导出

### 查看帮助文档

```bash
mongoexport --help
```

* -h 数据库主机名[以及可选端口号]
* -u 用户名
* -p 密码
* -d 数据库名称
* -c 集合
* -o 导出的文件名，需要填写，不然只是在命令行显示不会在本地存储

### 导出范例
```bash
mongoexport -h 127.0.0.1:27017 -u root -p password -d database -c collection -o ./collection_backup.json
```



## 参考资料

* [test](test.html)