---
layout:     post
title:      微服务目录汇总
date:       2021-09-05 20:02:51 +0800
postId:     2021-09-05-20-02-51
categories: [menus]
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
* [Docker基本操作]({% post_url micro-service/docker/2017-08-23-docker-basic-operation %})
* [修改docker容器内hosts配置的方法]({% post_url micro-service/docker/2022-11-07-01-change-docker-hosts-content %})
* [配置K8S集群中JVM相关内存参数]({% post_url micro-service/docker/2022-11-24-01-k8s-jvm-memory-limit %})
* [基础JDK的Docker镜像]({% post_url micro-service/docker/2022-12-08-01-basic-jdk-docker-image %})
* [使用Docker在本地部署SkyWalking]({% post_url micro-service/skywalking/2022-12-09-03-skywalking-in-docker %})
* [使用Docker在本地部署MySQL]({% post_url micro-service/docker/2019-01-02-manage-mysql-by-docker-of-macos %})
* [Docker内查询jvm状态报错：Can't attach to the process: ptrace]({% post_url micro-service/docker/2023-03-07-01-k8s-docker-jvm-ptrace %})

## K8S
* [K8S滚动升级策略]({% post_url micro-service/k8s/2022-12-09-02-k8s-rolling-upgrade-policy %})
* [k8s 中 ingress 重写请求路径]({% post_url micro-service/k8s/2022-04-14-01-k8s-ingress-rewrite-path %})
* [不同K8S集群间的通信方式]({% post_url micro-service/k8s/2023-08-28-01-connect-with-different-k8s %})

## SkyWalking
* [使用Docker在本地部署SkyWalking]({% post_url micro-service/skywalking/2022-12-09-03-skywalking-in-docker %})

## 模板代码

### content
```content
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
private static String tags = "[Docker]";
private static String folder = "micro-service" + File.separator + "docker";
```

### k8s
```java
private static String postTitle = "不同K8S集群间的通信方式";
private static String urlTitle = "connect-with-different-k8s";
private static String categories = "[K8S]";
private static String tags = "[K8S]";
private static String folder = "micro-service" + File.separator + "k8s";
```

### skywalking
```java
private static String postTitle = "使用Docker在本地部署SkyWalking";
private static String urlTitle = "skywalking-in-docker";
private static String categories = "[SkyWalking]";
private static String tags = "[SkyWalking]";
private static String folder = "micro-service" + File.separator + "skywalking";
```

## 参考资料
* [微服务目录汇总]({% post_url micro-service/2021-10-03-01-micro-service-menus %})
