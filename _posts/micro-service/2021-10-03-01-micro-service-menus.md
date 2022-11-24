---
layout:     post
title:      微服务目录汇总
date:       2021-09-05 20:02:51 +0800
postId:     2021-09-05-20-02-51
categories: [Microservice]
keywords:   [Microservice]
---
微服务体系相关文章目录汇总

## 分布式
* [分布式以及CAP理论]({% post_url micro-service/content/2017-03-23-distributed-and-cap %})
* [领域驱动设计]({% post_url micro-service/content/2021-10-03-02-micro-service-ddd %})
* [分布式事务解决方案]({% post_url micro-service/content/2021-10-05-05-distributed-transaction %})

## Nacos
* [docker方式部署启动nacos]({% post_url micro-service/nacos/2022-03-16-02-nacos-docker-deploy %})

## Docker
* [修改docker容器内hosts配置的方法]({% post_url micro-service/docker/2022-11-07-01-change-docker-hosts-content %})
* [配置K8S集群中JVM相关内存参数]({% post_url micro-service/docker/2022-11-24-01-k8s-jvm-memory-limit %})

## 模板代码

### content
```java
private static String postTitle = "领域驱动设计";
private static String urlTitle = "micro-service-ddd";
private static String categories = "[Microservice]";
private static String tags = "[Microservice]";
private static String folder = "micro-service" + File.separator + "content";
```

### docker
```java
private static String postTitle = "配置K8S集群中JVM相关内存参数";
private static String urlTitle = "k8s-jvm-memory-limit";
private static String categories = "[Docker]";
private static String tags = "[Microservice, Docker]";
private static String folder = "micro-service" + File.separator + "docker";
```

## 参考资料
* [开原框架目录汇总]({% post_url framework/2021-09-05-03-framework-menus %})
