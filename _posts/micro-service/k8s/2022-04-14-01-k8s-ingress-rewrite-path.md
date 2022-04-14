---
layout:     post
title:      k8s 中 ingress 重写请求路径
date:       2022-04-14 09:14:15 +0800
postId:     2022-04-14-09-14-15
categories: [Microservice]
keywords:   [Microservice]
---

在swagger管理的时候，希望通过一个域名可以访问所有集群的swagger文档，设计思路是域名解析到负载均衡，负载均衡通过请求路径关键字分发到不同K8S的
Ingress，然后Ingress再根据路径策略分发到具体的服务。

## 请求链路
整个链路如下：
![image-alter]({{ site.baseurl }}/image/post/2022/04/14/01/K8S-request-router.png)

* 发起请求，域名解析到负载均衡IP地址
* 负载均衡中配置路由到后端Ingress的规则
  * `/cloud` 路径路由到云上K8S集群的Ingress
  * `/local` 路径路由到本地K8S集群的Ingress
* K8S集群中Ingress配置服务的路由规则
  * `/cloud/app-a/` 路由到 `service1`
  * `/local/app-d/` 路由到 `service4`
* Service 接收到请求，路由到相关Pod，返回相关结果

## 问题&优化

因为增加了 `/local` 和 `/cloud` 关键字来进行K8S集群的划分，但是这个请求路径在基础服务中是没有的，所以需要通过路径重写来解决。
```xml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /$2
    nginx.ingress.kubernetes.io/use-regex: 'true'
spec:
  rules:
    - host: swagger.adolphor.com
      http:
        paths:
          - backend:
              service:
                name: omni-application-back-end-svc
                port:
                  number: 8086
            path: /(cloud)/(swagger/api/.*)
            pathType: ImplementationSpecific
          - backend:
              service:
                name: docking-qimen-service-svc
                port:
                  number: 8088
            path: /(cloud)/(swagger/api/.*)
            pathType: ImplementationSpecific
```

![image-alter]({{ site.baseurl }}/image/post/2022/04/14/01/K8S-Ingress-rewrite.png)

优化后的流程：

* 发起请求，域名解析到负载均衡IP地址
* 负载均衡中配置路由到后端Ingress的规则
  * `/cloud` 路径路由到云上K8S集群的Ingress
  * `/local` 路径路由到本地K8S集群的Ingress
* K8S集群中Ingress配置服务的路由规则
  * `/cloud/app-a/` 路径重写为 `/app-a/` 后路由到 `service1`
  * `/local/app-d/` 路径重写为 `/app-d/` 后路由到 `service4`
* Service 接收到请求，路由到相关Pod，返回相关结果

## 参考资料
* [K8S Nginx Ingress高级用法](https://help.aliyun.com/document_detail/86533.html)
* [ingress中虚拟路径解决方案](https://cloud.tencent.com/developer/article/1681598)
* [k8s 中 ingress 重写请求路径]({% post_url micro-service/k8s/2022-04-14-01-k8s-ingress-rewrite-path %})

