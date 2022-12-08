---
layout:     post
title:      基础JDK的Docker镜像
date:       2022-12-08 17:44:05 +0800
postId:     2022-12-08-17-44-05
categories: [Docker]
keywords:   [Microservice, Docker]
---

基础镜像脚本文件：

```dockerfile
FROM centos:7
LABEL maintainer="wang.zhe8@iwhalecloud.com"

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

Java项目范例文件：

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
