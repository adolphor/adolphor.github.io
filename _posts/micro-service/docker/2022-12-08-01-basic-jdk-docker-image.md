---
layout:     post
title:      基础JDK的Docker镜像
date:       2022-12-08 17:44:05 +0800
postId:     2022-12-08-17-44-05
categories: [Docker]
keywords:   [Microservice, Docker]
---

定制化基础镜像可以方便的进行运维部署，也可以方便的进行JDK版本的管控。

## 基础镜像脚本文件

```dockerfile
FROM centos:7
LABEL maintainer="adolphor.github.io"

#设置系统语言和时区
ENV LANG en_US.UTF-8
ENV TZ Asia/Shanghai

# 安装打包必备软件
RUN yum -y install wget unzip

# 准备 JDK/Tomcat 系统变量与路径
ENV JAVA_HOME /usr/java/latest
ENV PATH $PATH:$JAVA_HOME/bin
ENV CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
RUN echo $JAVA_HOME

# 下载安装 JDK 8
RUN wget http://microservice-oss.oss-cn-hangzhou.aliyuncs.com/plugins/jdk-8u341-linux-x64.rpm -O /tmp/jdk-8u191-linux-x64.rpm && \
  yum -y install /tmp/jdk-8u191-linux-x64.rpm && \
  rm -rf /tmp/jdk-8u191-linux-x64.rpm

CMD ["/bin/bash"]
```

## 阿里云流水线构建镜像
1) 使用阿里云CodeUp代码仓库，创建上面的 `dockerfile` 文件
2) 使用阿里云流水线构建工具，使用上面的源码文件构建镜像，并推送到镜像仓库

## 手动构建并推送到仓库
参考如下构建和推送指令：
```shell
####### 构建 #######
docker build -f Dockerfile -t adolphor/jdk-basic:jdk8u341 .

####### 推送到仓库 #######
# 使用名称打TAG
docker tag adolphor/jdk-basic:jdk8u341 docker.adolphor.github.io/base/jdk-basic:jdk8u341
# 或者查询验证基础镜像后根据ID打TAG
docker images | grep jdk-basic
docker tag b8659becaeba docker.adolphor.github.io/base/jdk-basic:jdk8u341
# 登录
docker login --username=adolphor https://docker.adolphor.github.io
# 推送
docker push docker.adolphor.github.io/base/jdk-basic:jdk8u341
```

## Java项目范例文件

```dockerfile
#FROM centos
FROM registry.cn-hangzhou.aliyuncs.com/adolphor/jdk-basic:jdk8u341
MAINTAINER yangyuwei

# 业务镜像打包
WORKDIR /data
ADD target/FILE_NAME.jar FILE_NAME-PORT.jar

ENV jvmConfig="-Xmx2G"
ENV springConfig="--spring.profiles.active=PROFILE"
ENV targetFile=FILE_NAME-PORT.jar
EXPOSE PORT
CMD ["sh","-c","java -Djava.security.egd=file:/dev/./urandom $jvmConfig -jar $targetFile $springConfig"]
```

## 参考资料
* [基础JDK的Docker镜像]({% post_url micro-service/docker/2022-12-08-01-basic-jdk-docker-image %})

开始写作吧
```
![image-alter]({{ site.baseurl }}/image/post/2022/12/08/01/xxx.jpg)
```
