---
layout:     post
title:      Docker基本操作
date:       2017-08-23 19:00:47 +0800
postId:     2017-08-23-19-00-47
categories: [Docker]
keywords:   [Microservice, Docker]
---

## Docker相关指令

### 查看docker信息

```shell
docker info

docker --version
docker-compose --version
docker-machine --version
```

###  控制台指令相关参数及解析
```shell
docker --help
# docker具体指令帮助信息，比如：
docker build --help
```

## Docker容器基本操作

### 本地镜像地址
在MacOS系统中镜像地址是：
```
/Users/{username}/Library/Containers/com.docker.docker/Data/com.docker.driver.amd64-linux
```
其中Docker.qcow2文件比较大

### 镜像

#### 搜索镜像
```shell
docker search ubuntu
```
#### 查看镜像所有TAG

可以使用如下方法，查看上面镜像的所有标签。
在`/usr/local/bin`目录下创建名为`dockertags`的文件，内容如下：

```shell
#!/bin/bash

if [ $# -lt 1 ]
then
cat << HELP

dockertags  --  list all tags for a Docker image on a remote registry.

EXAMPLE: 
    - list all tags for ubuntu:
       dockertags ubuntu

    - list all php tags containing apache:
       dockertags php apache

HELP
fi

image="$1"
tags=`wget -q https://registry.hub.docker.com/v1/repositories/${image}/tags -O -  | sed -e 's/[][]//g' -e 's/"//g' -e 's/ //g' | tr '}' '\n'  | awk -F: '{print $3}'`

if [ -n "$2" ]
then
    tags=` echo "${tags}" | grep "$2" `
fi

echo "${tags}"
```

赋执行权限：

```shell
chmod a+x dockertags
```

使用范例：
```shell
dockertags ubuntu
```

#### 下载镜像
```shell
docker pull ubuntu
# 指定TAG版本
docker pull ubuntu:16.04
```
### 容器

```shell
# 使用默认latest镜像生成随机容器名称
docker run -i -t ubuntu /bin/bash 
# 使用默认latest镜像生成指定容器名称
docker run --name myName -i -t ubuntu /bin/bash 
# 指定镜像TAG生成指定容器名称
docker run --name myName -i -t ubuntu:16.04 /bin/bash 
# 基于本地镜像生成指定容器名称
docker run --name myName -i -t adolphor/myImage:tag01 /bin/bash 
```

#### 查看容器列表

```shell
# 当前运行容器
docker ps 
# 所有容器（包括停止运行的容器）
docker ps -a
```

#### 查看某个容器详细信息

```shell
docker inspect dockerId/dockerName
```

#### 创建容器
```shell
# 创建后台守护容器
docker run -i -t -d ubuntu /bin/bash
```

#### 启动容器

```shell
# 启动容器并进入bash交互
docker start dockerId/dockerName -it
# 容器启动但是不进入shell交互页：
docker start dockerId/dockerName
```

#### 停止容器
```shell
docker stop dockerId/dockerName
```

#### 删除容器
```shell
docker rm dockerId/dockerName
# 小技巧：删除所有容器
docker rm `docker ps -a -q`
```

#### 进入容器
```shell
# 进入容器，并使用ssh交互
docker exec -it dockerId/dockerName
# 执行容器内部的指令，但不进入ssh交互
docker exec my-nginx nginx -t # 测试nginx配置文件
docker exec my-nginx nginx -s reload # 重载nginx配置文件
```

### 挂载

可以使用 `-v` 参数来挂在宿主机的文件夹到docker容器：
```shell
docker start rascms -i -v /Users/adolphor/IdeaProjects/adolphor:/root/adolphor/workspace /bin/bash
```
以上语句的含义为：初始化一个名为rascms的容器，并挂在宿主机的`/Users/adolphor/IdeaProjects/adolphor`目录到docker容器的
`/root/adolphor/workspace`目录。

注意：如果此容器关闭之后想要重新打开再次使用的时候，无需再次指定挂在目录，也就是只要执行 docker start 指令就行，无需其他参数。

### 日志
```shell
# 最后的日志
docker logs [dockerId/dockerName]

# 实时日志
docker logs -f [dockerId/dockerName]

# --since : 此参数指定了输出日志开始日期，即只输出指定日期之后的日志。
# -f : 查看实时日志
# -t : 查看日志产生的日期
# -tail=10 : 查看最后的10条日志。
# [dockerId/dockerName] : 容器名称
docker logs -f -t --since="2017-05-31" --tail=10 [dockerId/dockerName]
```

### 统计信息
```shell
docker stats
```
还有一个第三方的工具可以使用，top可以查看电脑的使用情况，ctop可以查看容器的使用情况：
```shell
# 安装
brew install ctop
# 运行
ctop
```


## Dockerfile
### 编写Dockerfile
### 执行Dockerfile
```shell
docker build -t="adolphor/myImage:tag01" .
```

如果不想使用缓存镜像，需要添加如下参数：--no-cache
```shell
docker build -t adolphor/myImage:tag01 . --no-cache
```

### 查看镜像创建过程
```shell
docker images adolphor/myImage:tag01
```
### 查看镜像创建过程
```shell
docker history ad2a3b2cc976
```

### 推送镜像到docker仓库
https://hub.docker.com/
```shell
docker push adolphor/myImage:tag01
```
只要用户名正确，如果没有static_web仓库的话会自动创建的

### 删除镜像
删除本地镜像（此镜像下不能有关联的容器才可以删除）
```shell
docker rmi adolphor/myImage:tag01
# 小技巧：删除所有容器
docker rmi `docker images -a -q`
```

### Dockerfile指令清单

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


## 参考资料

* [docker官方文档](https://docs.docker.com/reference/)
* 《第一本Docker书（修订版）》
* [Docker — 从入门到实践](https://yeasy.gitbooks.io/docker_practice/content/)
* [clean-docker-cache-for-mac](https://blog.mrtrustor.net/post/clean-docker-for-mac/)
* [How to list all tags for a Docker image on a remote registry](https://stackoverflow.com/questions/28320134/how-to-list-all-tags-for-a-docker-image-on-a-remote-registry)
* [docker logs 查看实时日志](https://www.cnblogs.com/qufanblog/p/6927411.html)
