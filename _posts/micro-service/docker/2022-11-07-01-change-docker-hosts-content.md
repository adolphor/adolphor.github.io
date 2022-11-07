---
layout:     post
title:      修改docker容器内hosts配置的方法
date:       2022-11-07 11:14:59 +0800
postId:     2022-11-07-11-14-59
categories: [docker]
keywords:   [micro-service, docker]
---

当docker内使用HBase数据库的时候，由于zookeeper特性，需要在hosts文件内配置host域名映射，
具体问题和解决方案如下。

## 原理
docker在build过程中修改的hosts文件，会在启动镜像的时候被覆盖掉，导致build过程中所有的修改
都失效了，所以只有在容器启动之后，再进行相关hosts文件的修改，才能保证修改生效。

## 具体操作
在同一层级目录下：
* Dockerfile
* hosts
* entrypoint.sh

> Dockerfile

```dockerfile
# 工作目录
WORKDIR /data
# 配置hosts映射-开始
ADD hosts /data/hosts
ADD entrypoint.sh /data/entrypoint.sh
RUN ["chmod","+x","/data/entrypoint.sh"]
# 配置hosts映射-结束
# 启动入口
ENTRYPOINT /data/entrypoint.sh
```

> hosts

```hosts
192.168.1.176   hadoop1
192.168.1.177   hadoop2
192.168.1.178   hadoop3
```

> entrypoint.sh

```shell
#!/bin/sh
# 向hosts文件追加内容
cat /data/hosts >> /etc/hosts;
echo /etc/hosts;

# 启动项目
java -Djava.security.egd=file:/dev/./urandom $jvmConfig -jar $targetFile $springConfig
```

## 参考资料
* [修改docker容器内hosts配置的方法]({% post_url micro-service/docker/2022-11-07-01-change-docker-hosts-content %})
* [How to edit /etc/hosts file in running docker container](https://stackoverflow.com/questions/37860601/how-to-edit-etc-hosts-file-in-running-docker-container)
