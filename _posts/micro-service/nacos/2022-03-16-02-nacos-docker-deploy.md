---
layout:     post
title:      docker方式部署启动nacos
date:       2022-03-16 10:40:08 +0800
postId:     2022-03-16-10-40-09
categories: [Nacos]
keywords:   [Nacos]
---

## 准备条件

### 数据库
需要先配置一个数据库实例，并初始化相关表：

```
端口：3306
数据库名称：nacos
用户名：bobzhu
密码：Adolphor!@#123
```

初始化语句参考：[Mysql初始化脚本](https://github.com/alibaba/nacos/blob/develop/distribution/conf/nacos-mysql.sql)

## 启动命令

使用桥接网络方式，：
```shell
docker run -d --name nacos --restart=always \
-e MODE=standalone \
-e JVM_XMS=256m \
-e JVM_XMX=256m \
-e SPRING_DATASOURCE_PLATFORM=mysql \
-e MYSQL_SERVICE_HOST=10.0.16.4 \
-e MYSQL_SERVICE_PORT=3306 \
-e MYSQL_SERVICE_DB_NAME=nacos \
-e MYSQL_SERVICE_USER=bobzhu \
-e MYSQL_SERVICE_PASSWORD='Adolphor!@#123' \
-e MYSQL_DATABASE_NUM=1 \
-v /home/nacos/init.d/custom.properties:/home/nacos/init.d/custom.properties \
-v /home/nacos/logs:/home/nacos/logs \
-p 8848:8848 \
nacos/nacos-server:last
```

## 参考资料
* [docker方式部署启动nacos]({% post_url micro-service/nacos/2022-03-16-02-nacos-docker-deploy %})
* [Github - Nacos](https://github.com/alibaba/nacos)
* [nacos.io](https://nacos.io/zh-cn/docs/what-is-nacos.html)
* [nacos-docker](https://github.com/nacos-group/nacos-docker)
* [Mysql初始化脚本](https://github.com/alibaba/nacos/blob/develop/distribution/conf/nacos-mysql.sql)