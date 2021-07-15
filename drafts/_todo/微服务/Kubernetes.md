
# 概念

### Kubernetes 能做什么
* 服务发现
    - DNS名称
    - IP地址
* 负载均衡
    - 根据策略自动分配流量
* 存储编排
    - 自动挂载你选择的存储系统
* 自动完成装箱计算
    - CPU 设置
    - RAM 设置
* 自动部署和回滚
    - 根据配置自动调整为期望的状态
* 自我修复
    - 剔除无响应容器
    - 替换容器
    - 重启失败的容器
* 密钥与配置管理
    - 密码
    - OAuth 令牌
    - ssh 密钥

### Kubernetes 不能做什么


# kubernetes组件

![components-of-kubernetes](components-of-kubernetes.svg)

### 控制平面组件（Control Plane Components）
控制平面的组件对集群做出全局决策(比如调度)，以及检测和响应集群事件（例如，当不满足部署的 replicas 字段时，启动新的 pod）。


* etcd  
  键值数据库，兼具一致性和高可用性，可以作为保存 Kubernetes 所有集群数据的后台数据库
* kube-apiserver （API 服务器）  
    API 服务器是 Kubernetes 控制面的前端组件
* kube-scheduler  
    控制平面组件，负责监视新创建的、未指定运行节点（node）的 Pods，选择节点让 Pod 在上面运行。
* kube-controller-manager  
    在主节点上运行 控制器 的组件，包括以下几个组成部分：
    - 节点控制器（Node Controller）: 负责在节点出现故障时进行通知和响应
    - 任务控制器（Job controller）: 监测代表一次性任务的 Job 对象，然后创建 Pods 来运行这些任务直至完成
    - 端点控制器（Endpoints Controller）: 填充端点(Endpoints)对象(即加入 Service 与 Pod)
    - 服务帐户和令牌控制器（Service Account & Token Controllers）: 为新的命名空间创建默认帐户和 API 访问令牌
* cloud-controller-manager  
    云控制器管理器是指嵌入特定云的控制逻辑的 控制平面组件。 云控制器管理器允许您链接聚合到云提供商的应用编程接口中， 并分离出相互作用的组件与您的集群交互的组件。 下面的控制器都包含对云平台驱动的依赖：
    - 节点控制器（Node Controller）: 用于在节点终止响应后检查云提供商以确定节点是否已被删除
    - 路由控制器（Route Controller）: 用于在底层云基础架构中设置路由
    - 服务控制器（Service Controller）: 用于创建、更新和删除云提供商负载均衡器

### Node 组件
节点组件在每个节点上运行，维护运行的 Pod 并提供 Kubernetes 运行环境。

* kubelet  
    一个在集群中每个节点（node）上运行的代理。 它保证容器（containers）都 运行在 Pod 中。
    kubelet 接收一组通过各类机制提供给它的 PodSpecs，确保这些 PodSpecs 中描述的容器处于运行状态且健康。 kubelet 不会管理不是由 Kubernetes 创建的容器。
  
* kube-proxy  
    kube-proxy 是集群中每个节点上运行的网络代理， 实现 Kubernetes 服务（Service） 概念的一部分。
    kube-proxy 维护节点上的网络规则。这些网络规则允许从集群内部或外部的网络会话与 Pod 进行网络通信。
    如果操作系统提供了数据包过滤层并可用的话，kube-proxy 会通过它来实现网络规则。否则， kube-proxy 仅转发流量本身。

* 容器运行时（Container Runtime）  
    容器运行环境是负责运行容器的软件。 Kubernetes 支持多个容器运行环境: 
    Docker、 containerd、CRI-O 以及任何实现 Kubernetes CRI (容器运行环境接口)。

### 插件（Addons）
* DNS  
    几乎所有 Kubernetes 集群都应该 有集群 DNS， 因为很多示例都需要 DNS 服务。
    集群 DNS 是一个 DNS 服务器，和环境中的其他 DNS 服务器一起工作，它为 Kubernetes 服务提供 DNS 记录。

* Web 界面（仪表盘）  
    Dashboard 是 Kubernetes 集群的通用的、基于 Web 的用户界面。 它使用户可以管理集群中运行的应用程序以及集群本身并进行故障排除。

* 容器资源监控  
    容器资源监控 将关于容器的一些常见的时间序列度量值保存到一个集中的数据库中，并提供用于浏览这些数据的界面。





# 命名空间
https://www.cnblogs.com/cocowool/p/kubernetes_namespace.html


# Ingress 资源
Ingress 是对集群中服务的外部访问进行管理的 API 对象，典型的访问方式是 HTTP。
Ingress 可以提供负载均衡、SSL 终结和基于名称的虚拟托管。
Ingress 公开了从集群外部到集群内服务的 HTTP 和 HTTPS 路由。 流量路由由 Ingress 资源上定义的规则控制。




# Service 资源

https://kubernetes.io/zh/docs/concepts/services-networking/service/

Pod 是非永久性资源。 如果你使用 Deployment 来运行你的应用程序，则它可以
动态创建和销毁 Kubernetes Pod 以匹配集群状态。
Kubernetes Service 定义了这样一种抽象：逻辑上的一组 Pod，一种可以访问
它们的策略 —— 通常称为微服务。 Service 所针对的 Pods 集合通常是通过选择
算符来确定的。
Service 定义的抽象能够解耦这种关联。

```
apiVersion: v1 # 有哪些版本可以使用？
kind: Service # 描述的资源类型，可选项：Pod，Service，gateway，Endpoints 等
metadata:
  name: my-service
spec:
  selector: # 选择器
    app: MyApp # 选择所有具有标签"app=MyApp"的Pod
  ports:
    - protocol: TCP # 通讯协议类型
      port: 80 # pod内微服务使用 80 端口
      targetPort: 9376 # 对外暴露了 9376 端口
```

# 服务发现
https://kubernetes.io/zh/docs/concepts/services-networking/service/

Kubernetes 支持两种基本的服务发现模式 —— 环境变量和 DNS。


# 发布服务（服务类型)
https://kubernetes.io/zh/docs/concepts/services-networking/service/

对一些应用的某些部分（如前端），可能希望将其暴露给 Kubernetes 集群外部 的 IP 地址。

Kubernetes ServiceTypes 允许指定你所需要的 Service 类型，默认是 ClusterIP。

注：你也可以使用 Ingress 来暴露自己的服务。 Ingress 不是一种服务类型，但它充当集群
的入口点。 它可以将路由规则整合到一个资源中，因为它可以在同一IP地址下公开多个服务。


Type 的取值以及行为如下：
* ClusterIP  
  通过集群的内部 IP 暴露服务，选择该值时服务只能够在集群内部访问。 这也是默认的 ServiceType。
* NodePort  
  通过每个节点上的 IP 和静态端口（NodePort）暴露服务。 NodePort 服务会路由到
  自动创建的 ClusterIP 服务。 通过请求 <节点 IP>:<节点端口>，你可以从集群的外
  部访问一个 NodePort 服务。
* LoadBalancer  
  使用云提供商的负载均衡器向外部暴露服务。 外部负载均衡器可以将流量路由到自动创建
  的 NodePort 服务和 ClusterIP 服务上。
* ExternalName  
  通过返回 CNAME 和对应值，可以将服务映射到 externalName 字段的内容（例如，
  foo.bar.example.com）。 无需创建任何类型代理。



# 参考

* [Kubernetes 文档](https://kubernetes.io/zh/docs/home/)
* [Kubernetes 部署 Laravel 应用的最佳实践](https://learnku.com/articles/41811)









