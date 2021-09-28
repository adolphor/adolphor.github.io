---
layout:     post
title:      Spring Cloud 学习
date:       2019-01-07 13:43:28 +0800
postId:     2019-01-07-13-43-28
categories: []
keywords:   [分布式,Spring Cloud]
---

## 微服务

​	`微服务`最初是由 MartinFowler 在 2014年写的一篇文章 《MicroServices》中提出来的。
关于 Martin Fowler 的介绍，维基百科上是这样描述的:

```
	Martin Fowler，软件工程师，也是一个软件开发方面的著作者和国际知名演说家，专注于丽向对象分析与设 计、统一建模语言、领域建模，以及敏捷软件开发方法，包括极限编程。 主要著作有 〈可重用对象模型 〉〈重构一一改 善既有代码的设计 〉〈企业应用架构模式 〉〈规划极限编程 〉 等。
```

对于微服务，业界没有一个严格统一的定义，但是作为“微服务”这一名词的发明人， Martin Fowler 对微服务的定义似乎更具有权威性和指导意义 。 他的理解如下: 

```
	简而言之，微服务架构的风格，就是将单一程序开发成一个微服务，每个微服务运行在自己的进程中，并使用轻量级机制通信，通常是 HTTP RESTFUL API。这些服务围绕业务能力来划分构建的，并通过完全自动化部署机制来独立部署。 这些服务可以使用不同的编程语言，以及不同数据存储技术，以保证最低限度的集中式管理。
```

### 微服务 和 单体服务对比

### 单体服务架构图

![单体服务的集群化]({{ site.baseurl }}/image/post/2019/01/07/The-Cluster-of-single-Service.jpg)

### 微服务优势

* 根据业务拆分为各个服务，边界明确，将复杂问题简单化，新人学习成本低，上手快
* 各个微服务之间通过HTTP协议通信，开发语言和技术选型灵活
* 耦合性小，横向扩展能力强；服务独立部署，对其他业务没有影响
* 微服务在CAP理论中采用的是AP架构，及具有高可用和分区容错的特点。（缺少 `Consistence` 强一致性）

### 微服务缺点

* 复杂度
* 分布式事务
* 服务的划分
* 服务的部署



## Spring Cloud 简介

### Spring Cloud 是什么

Spring Cloud 是一系列框架的有序集合，它利用 Spring Boot 的开发便利性简化了分布式系统的开发，比如服务发现、服务网关、服务路由、链路追踪等。Spring Cloud 并不重复造轮子，而是将市面上开发得比较好的模块集成进去，进行封装，从而减少了各模块的开发成本。换句话说：Spring Cloud 提供了构建分布式系统所需的“全家桶”。

### Sprig Cloud 优缺点

#### 优点

* 集大成者，Spring Cloud 包含了微服务架构的方方面面。
* 约定优于配置，基于注解，没有配置文件。
* 轻量级组件，Spring Cloud 整合的组件大多比较轻量级，且都是各自领域的佼佼者。
* 开发简便，Spring Cloud 对各个组件进行了大量的封装，从而简化了开发。
* 开发灵活，Spring Cloud 的组件都是解耦的，开发人员可以灵活按需选择组件。

#### 缺点

* 项目结构复杂，每一个组件或者每一个服务都需要创建一个项目。
* 部署门槛高，项目部署需要配合 Docker 等容器技术进行集群部署，而要想深入了解 Docker，学习成本高。

### Spring Cloud 和 Dubbo 对比

| 核心要素       | Dubbo            | Spring Cloud                                                 |
| -------------- | ---------------- | ------------------------------------------------------------ |
| 服务注册中心   | Zookeeper、Redis | Spring Cloud Netflix Eureka（Zookeeper、Consul）             |
| 负载均衡       | 自带             | Spring Cloud Netflix Ribbon（配合Feign使用：声明式远程调度组件） |
| 服务网关       | 无               | Spring Cloud Netflix Zuul                                    |
| 断路器         | 不完善           | Spring Cloud Netflix Hystrix                                 |
| 服务调用方式   | RPC              | HTTP REST API、Message                                       |
| 分布式配置     | 无               | Spring Cloud Config                                          |
| 分布式追踪系统 | 无               | Spring Cloud Sleuth                                          |
| 消息总线       | 无               | Spring Cloud Bus                                             |
| 数据流         | 无               | Spring Cloud Stream<br /> (基于Redis、Rabbit、Kafka实现的消息微服务) |
| 批量任务       | 无               | Spring Cloud Task                                            |
| 安全模块       | 无               | Spring Cloud Security                                        |





## Spring boot（待单独抽离）

Spring Boot 是由 Pivotal 团队提供的基于 Spring 的全新框架，其设计目的是为了简化 Spring 应用的搭建和开发过程。该框架遵循“约定大于配置”原则，采用特定的方式进行配置，从而使开发者无需定义大量的 XML 配置。通过这种方式，Spring Boot 致力于在蓬勃发展的快速应用开发领域成为领导者。

Spring Boot 并不重复造轮子，而且在原有 Spring 的框架基础上封装了一层，并且它集成了一些类库，用于简化开发。换句话说，Spring Boot 就是一个大容器。

对于Spring boot ，可以不配置任务配置参数，系统会有默认的值；如果配置的话，需要将配置文件命名为 `application`，后缀名可以为 `properties` 或者 `yaml`。

### 配置范例：

```properties
server.port=8081
server.servlet.context-path=/api
```

```yaml
server:
	port: 8081
	servlet:
		context-path: /api
```

### 多环境配置

```yaml
spring:
	profiles:
		active: dev
```

然后创建多环境配置文件，文件名的格式为：application-{profile}.yml，其中，{profile} 替换为环境名字，如 application-dev.yml，我们就实现了多环境的配置，每次编译打包我们无需修改任何东西，编译为 jar 文件后，运行命令：

```shell
java -jar api.jar --spring.profiles.active=dev
```















## 参考资料

* [Spring Cloud 从入门到精通](https://blog.csdn.net/valada/article/details/80892573)
* [官方 - Spring Boot Reference Guide](https://docs.spring.io/spring-boot/docs/2.1.1.RELEASE/reference/html/)
* [segmentfault - Spring Cloud从入门到精通](https://segmentfault.com/blog/dd-springcloud)
* [tutorialspoint - Spring Boot Tutorial](https://www.tutorialspoint.com/spring_boot/index.htm)
* [《深入理解Spring Cloud 与微服务构建》勘误、源码下载](https://blog.csdn.net/forezp/article/details/79638403)
