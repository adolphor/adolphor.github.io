---
layout:     post
title:      不同K8S集群间的通信方式
date:       2023-08-28 14:41:02 +0800
postId:     2023-08-28-14-41-02
categories: [K8S]
keywords:   [K8S]
---

当前微服务部署分散在了不同的K8S集群，比如有一些在阿里云K8S集群中，有一些在本地部署的K8S集群中，
如果解决不同区域的K8S集群间的通信问题，想到了如下几个方案。

## 阿里云官方方案
![阿里云官方方案]({{ site.baseurl }}/image/post/2023/08/28/01/阿里云官方方案.png)

### 优点
* 官方和开源组件支持
* 请求链路较短，架构清晰

### 缺点
* 阿里云收费较贵
* 本地组件集成需要运维成本

## K8S组件方案：service和ingress
![K8S组件方案]({{ site.baseurl }}/image/post/2023/08/28/01/K8S组件方案.png)

### 优点
* 官方组件支持

### 缺点
* 增加了service和ingress的链路长度
* 已经部署的服务需要修改网络配置
* 本地组件集成需要运维成本：绑定固定IP

## 桥接方案：agent和bridge
![桥接方案]({{ site.baseurl }}/image/post/2023/08/28/01/桥接方案.png)

### 优点
* 省钱

### 缺点
* 增加了链路长度

## 参考资料
* [不同K8S集群间的通信方式]({% post_url micro-service/k8s/2023-08-28-01-connect-with-different-k8s %})
* [混合云部署 Kubernetes 集群最佳实践](https://bp.aliyun.com/detail/102)
* [云企业网计费说明](https://help.aliyun.com/document_detail/189836.html)

开始写作吧
```
![image-alter]({{ site.baseurl }}/image/post/2023/08/28/01/xxx.png)
```
