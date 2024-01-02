---
layout:     post
title:      更改Docker的hosts文件
date:       2023-11-24 17:10:10 +0800
postId:     2023-11-24-17-10-10
categories: [Docker]
keywords:   [Microservice, Docker]
---
部署集群服务或者其他场景的时候需要更改Docker的hosts文件。
有三个文件需要处理
* Dockerfile：增加`myhosts.txt`和`entrypoint.sh`两个配置
* myhosts.txt：需要追加的hosts内容
* entrypoint.sh：启动项目前执行的脚本

## Dockerfile

```dockerfile
FROM centos:7
MAINTAINER adolphor

WORKDIR /data

ENV JVM_OPTS="-XX:InitialRAMPercentage=80.0 -XX:MaxRAMPercentage=80.0"
ADD target/app.jar app.jar

# 配置hosts映射-开始
ADD myhosts.txt /data/hosts
ADD entrypoint.sh /data/entrypoint.sh
RUN ["chmod","+x","/data/entrypoint.sh"]
ENTRYPOINT /data/entrypoint.sh
# 配置hosts映射-结束
```

## myhosts.txt

```javascript
172.31.1.176   hadoop1
172.31.1.177   hadoop2
172.31.1.178   hadoop3
```

## entrypoint.sh

```shell
#!/bin/sh
# 向hosts文件追加内容
cat /data/hosts >> /etc/hosts;
echo /etc/hosts;

# 启动项目
java $JVM_OPTS -jar app.jar
```


## 参考资料
* [更改Docker的hosts文件]({% post_url micro-service/docker/2023-11-24-01-change-docker-hosts %})

开始写作吧
```
![image-alter]({{ site.baseurl }}/image/post/2023/11/24/01/xxx.png)
```
