---
layout:     post
title:      在Nacos和K8S架构下的数据库无感切换方案
date:       2023-10-10 16:57:06 +0800
postId:     2023-10-10-16-57-06
categories: [K8S]
keywords:   [K8S]
---

在开发过程中面临数据库拆分的场景，如两大业务集群在发展到一定成都之后，为了支撑更大的访问量，
或者数据库增长到一定成都需要面临数据库分库分表的优化操作，在这些场景下如何无感滚动升级到新数据库，
是一个值得思考的问题。

## 问题难点
有一下几个问题需要解决。
* 无感发布
  * 不能中断服务，影响业务。
* 新老数据库同步
  * 第一步：全量数据同步
  * 第二步：发布成功之前的增量数据同步
* 避免数据双库写入
  * 为了保证数据的准确，不能存在一部分数据写入老库，一部分数据写入新库的情况

## 可选方案
数据库同步之后，为了避免双端写入，必须保证所有流量在一瞬间切换完成，打入新的数据库。
但是因为一般来说，生产环境肯定不止又一个服务实例在运行，所以必须考虑如何实现所有实例同步切换的问题。
考虑了以下两个方案：
* 将服务实例缩减为1个：新的实例启动成功之后，立即停止老的服务实例
* 服务实例前置增加一个负载均衡：通过负载均衡来控制请求转发到所有老的实例还是转发到所有新的实例

## 具体实施

动态流程示意图：

![flow.gif]({{ site.baseurl }}/image/post/2023/10/10/02/flow.gif){: width="450" }

备忘：使用ffmpeg将png转为gif：
```shell
ffmpeg -pattern_type glob -i '*.png' -vf palettegen palette.png
ffmpeg  -framerate 1 -pattern_type glob -i '*.png' -i palette.png -lavfi paletteuse out.gif
```

### 未改动之前的请求链路
当前请求流转路径：
* Naocs中注册的是Pod Ip
* 未使用K8S的Service负载均衡

![01-未改动之前的请求链路]({{ site.baseurl }}/image/post/2023/10/10/02/01-未改动之前的请求链路.png){: width="450" }

### 注册到Nacos的PodIp改为ServiceIP
* 创建K8S的Service
  * 设置固定IP，如 `172.16.4.246`
  * 路由规则指定为老的Pod标签：`cloud-user`
* 将服务Nacos上报的IP地址改为此固定IP，并进行服务的滚动发布
![02-service注册]({{ site.baseurl }}/image/post/2023/10/10/02/02-service注册.png){: width="450" }

### 新数据库和数据同步
申请数据库，并进行新老数据的数据同步
![03-新数据库和数据同步]({{ site.baseurl }}/image/post/2023/10/10/02/03-新数据库和数据同步.png){: width="450" }
  
### 新服务启动但是不接收请求
* K8S启动新服务，但是Pod标签改为：`cloud-user-new`
* 将服务Nacos上报的IP地址改为固定IP：`172.16.4.246`，这样虽然启动了新服务，但是上报的是老的Service IP，所以流量不会流转到新的服务

![04-新服务启动但是不接收请求]({{ site.baseurl }}/image/post/2023/10/10/02/04-新服务启动但是不接收请求.png){: width="450" }

### 切换流量到新服务
更改K8S Service 配置：
```yaml
spec:
  allocateLoadBalancerNodePorts: true
  externalTrafficPolicy: Cluster
  internalTrafficPolicy: Cluster
  ports:
    - name: cloud-user
      nodePort: 32767
      port: 8760
      protocol: TCP
      targetPort: 8760
  selector:
    app: cloud-user
  sessionAffinity: None
  type: LoadBalancer
```
将 `app: cloud-user` 改为 `app: cloud-user-new`，就能够将流量在一瞬间切换到所有的新服务实例。

![05-切换流量到新服务]({{ site.baseurl }}/image/post/2023/10/10/02/05-切换流量到新服务.png){: width="450" }

### 去掉负载均衡
当切换完成，验证完毕之后，负载均衡的使命就已经完成了，运行一段时间之后，就可以恢复到注册到Nacos的IP改为Pod IP即可：

![06-去掉负载均衡]({{ site.baseurl }}/image/post/2023/10/10/02/06-去掉负载均衡.png){: width="450" }

### 停止老服务和数据库
最后收尾工作：
* 停止老的Pod服务
* 老的数据库更名禁止使用，并最终停止销毁
![07-停止老服务和数据库]({{ site.baseurl }}/image/post/2023/10/10/02/07-停止老服务和数据库.png){: width="450" }

## 参考资料
* [在Nacos和K8S架构下的数据库无感切换方案]({% post_url micro-service/k8s/2023-10-10-02-change-database-without-pain %})

开始写作吧
```
![image-alter]({{ site.baseurl }}/image/post/2023/10/10/02/xxx.png)
```
