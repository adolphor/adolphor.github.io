---
layout:     post
title:      macOS下mongodb的安装和GUI工具介绍
date:       2017-09-25 14:57:25 +0800
postId:     2017-09-25-14-57-25
categories: []
keywords:   [mongodb]
---

## macOS下使用brew安装mongodb

### mongodb的安装

> 安装

```shell
# 查找
brew search mongodb
# 安装
brew install mongodb
# 重启
brew services restart mongodb
brew services stop mongodb
brew services start mongodb
```

> 直接使用`mongo`命令进行连接：

```
ongoDB shell version v3.4.9
connecting to: mongodb://127.0.0.1:27017
MongoDB server version: 3.4.9
Server has startup warnings:
2017-09-25T14:49:50.229+0800 I CONTROL  [initandlisten]
2017-09-25T14:49:50.229+0800 I CONTROL  [initandlisten] ** WARNING: Access control is not enabled for the database.
2017-09-25T14:49:50.229+0800 I CONTROL  [initandlisten] **          Read and write access to data and configuration is unrestricted.
2017-09-25T14:49:50.229+0800 I CONTROL  [initandlisten]
```

### mongodb的默认配置

> 查看配置文件路径：

```shell
# mongodb相关信息
brew info mongodb
```

```
mongodb: stable 3.4.9 (bottled), devel 3.5.11
High-performance, schema-free, document-oriented database
https://www.mongodb.org/
/usr/local/Cellar/mongodb/3.4.9 (19 files, 284.9MB) *
  Poured from bottle on 2017-09-25 at 14:49:23
From: https://github.com/Homebrew/homebrew-core/blob/master/Formula/mongodb.rb

......

==> Caveats
To have launchd start mongodb now and restart at login:
  brew services start mongodb
Or, if you don't want/need a background service you can just run:
  mongod --config /usr/local/etc/mongod.conf
```

> 查看具体配置信息：

```shell
cat /usr/local/etc/mongod.conf
```

```
systemLog:
  destination: file
  path: /usr/local/var/log/mongodb/mongo.log
  logAppend: true
storage:
  dbPath: /usr/local/var/mongodb
net:
  bindIp: 127.0.0.1
```

* 注意：当前使用brew安装mongodb已经无需自己创建`/data/db`以及`mongod.conf`，也无需
再手动将mongodb的bin目录添加至path，直接启动即可使用。

### 密码设置

#### 创建 administrator 权限用户

> 创建用户

```shell
use admin
db.createUser(
  {
    user: "superAdmin",
    pwd: "admin123",
    roles: [ { role: "root", db: "admin" } ]
  })
```

> 修改配置


```shell
vi /usr/local/etc/mongod.conf
```

内容如下所示：

```
systemLog:
 destination: file
 path: /usr/local/var/log/mongodb/mongo.log
 logAppend: true
storage:
 dbPath: /usr/local/var/mongodb
net:
 bindIp: 127.0.0.1
security:
 authorization: enabled
```

> 重启应用

```shell
brew services restart mongodb
```

> 命令行连接

```shell
mongo --port 27017 -u "superAdmin" -p "admin123" --authenticationDatabase "admin"
```

## GUI工具介绍

* idea系列ide的 mongo 插件
* studio 3T 的 Non-Commercial 许可
* Robo 3T

## 参考资料

* [Mongodb enable authentication ](https://medium.com/@raj_adroit/mongodb-enable-authentication-enable-access-control-e8a75a26d332)
