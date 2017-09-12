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

## Docker相关指令

### 查看docker信息

```
docker info
```

###  帮助信息
```
# docker帮助信息
docker
docker help
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

### 官方镜像

#### 搜索镜像
```
docker search ubuntu
```
#### 查看镜像所有TAG

可以使用如下方法，查看上面镜像的所有标签。
在`/usr/local/bin`目录下创建名为`dockertags`的文件，内容如下：

{% highlight shell %}
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
{% endhighlight %}

赋执行权限：

```
chmod a+x dockertags
```

使用范例：
```
dockertags ubuntu
```

#### 下载镜像
```
docker pull ubuntu
# 指定TAG版本
docker pull ubuntu:16.04
```
### 基于镜像创建容器

```
# 使用默认last镜像TAG生成随机容器名称
docker run -i -t ubuntu /bin/bash 
# 使用默认last镜像TAG生成指定容器名称
docker run —name myName i -t ubuntu /bin/bash 
# 指定镜像TAG生成指定容器名称
docker run —name myName i -t ubuntu:16.04 /bin/bash 
# 基于本地镜像生成指定容器名称
docker run —name myName i -t adolphor/myImage:tag01 /bin/bash 
```

#### 创建后台守护容器：
```
docker run -i -t -d ubuntu /bin/bash
```

#### 查看容器列表
```
docker ps 当前运行容器
docker ps -a 所有容器（包括停止运行的容器）
```

#### 查看某个容器详细信息
```
docker inspect dockerId/dockerName
```

### 启动容器

```
# 容器启动但是没有进入shell交互页：
docker start dockerId/dockerName
# 如果已经启动，则如下操作进入交互：
docker attach dockerId/dockerName
```

### 日志
```
docker logs dockerId/dockerName
```

### 统计信息
```
docker stats
```

### 删除容器
```
docker rm dockerId/dockerName
# 小技巧：删除所有容器
docker rm `docker ps -a -q`
```

## Dockerfile
### 编写Dockerfile
### 执行Dockerfile
```
docker build -t="adolphor/myImage:tag01" .
```
### 查看镜像创建过程
```
docker images adolphor/myImage:tag01
```
### 查看镜像创建过程
```
docker history ad2a3b2cc976
```

### 推送镜像到docker仓库
https://hub.docker.com/
```
docker push adolphor/myImage:tag01
```
只要用户名正确，如果没有static_web仓库的话会自动创建的

### 删除镜像
删除本地镜像（此镜像下不能有关联的容器才可以删除）
```
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
* [clean-docker-cache-for-mac](https://blog.mrtrustor.net/post/clean-docker-for-mac/)
* [How to list all tags for a Docker image on a remote registry](https://stackoverflow.com/questions/28320134/how-to-list-all-tags-for-a-docker-image-on-a-remote-registry)
