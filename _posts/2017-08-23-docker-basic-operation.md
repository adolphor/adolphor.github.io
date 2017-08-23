---
layout:     post
title:      Docker基本操作
date:       2017-08-23 19:00:47 +0800
postId:     2017-08-23-19-00-47
categories: [blog]
tags:       [Docker]
geneMenu:   true
excerpt:    Docker基本操作
---

Docker 基本操作

## 查看docker信息
```
docker info
```

## 帮助信息
```
docker help
```

## 创建镜像容器
```
docker run -i -t ubuntu /bin/bash 随机名称
docker run —name myName i -t ubuntu /bin/bash 指定名称
```

退出的时候输入 exist 回车即可

创建后台守护容器：
```
docker run -i -t -d ubuntu /bin/bash 随机名称
```

## 查看容器列表
```
docker ps 当前运行容器
docker ps -a 所有容器（包括停止运行的容器）
```

## 查看某个容器详细信息
```
docker inspect dockerId/dockerName
```

## 启动容器

```
# 容器启动但是没有进入shell交互页：
docker start dockerId/dockerName
# 如果已经启动，则如下操作进入交互：
docker attach dockerId/dockerName
```

## 日志
```
docker logs dockerId/dockerName
```

## 统计信息
```
docker stats
```

## 删除容器
```
docker rm dockerId/dockerName
# 小技巧：删除所有容器
docker rm `docker ps -a -q`
```

## 本地镜像地址
在MacOS系统中镜像地址是：
```
/Users/{username}/Library/Containers/com.docker.docker/Data/com.docker.driver.amd64-linux
```
其中Docker.qcow2文件比较大

## Dockerfile
### 编写Dockerfile
### 执行Dockerfile
```
docker build -t="adolphor/static_web:demo01" .
```
### 查看镜像创建过程
```
docker images adolphor/static_web:demo01
```
### 查看镜像创建过程
```
docker history ad2a3b2cc976
```

？？？？创建之后的镜像在哪里？？？？？？

## Dockerfile指令清单

* CMD
* ENTRYPOINT
* WORKDIR
* ENV
* USER
* VOLUME
* ADD
* COPY
* LABEL
* STOPSIGNAL
* ARG
* ONBUILD

## 推送镜像到docker仓库
https://hub.docker.com/
```
docker push adolphor/static_web:demo01
```
只要用户名正确，如果没有static_web仓库的话会自动创建的

## 删除镜像
删除本地镜像（此容器下不能有关联的容器才可以删除）
```
docker rmi adolphor/static_web:demo01
# 小技巧：删除所有容器
docker rmi `docker images -a -q`
```

## 参考资料

* 《第一本Docker书（修订版）》

