---
layout:     post
title:      Netty源码学习汇总目录
date:       2020-02-01 21:11:24 +0800
postId:     2020-02-01-21-11-24
categories: [Netty]
tags:       [Netty]
geneMenu:   true
excerpt:    Netty源码学习汇总目录
---

## 预备知识

### 设计模式

* [设计模式之 —— 观察者模式]({% post_url design-pattern/2020-01-23-java-design-patterns-Observer %})
* [设计模式之 —— 观察者模式进阶：监听器模式]({% post_url design-pattern/2020-03-14-java-design-patterns-listener %})

### 多线程知识

* [Java源码-并发包 ScheduledExecutorService]({% post_url concurrent/2020-02-09-java-util-concurrent-ScheduledExecutorService %})

## Netty 核心类库介绍

* [Netty源码 - Netty主要类介绍]({% post_url netty/2020-03-12-netty-main-class %})

## 事件驱动：Reactor模式

* [Netty源码 - 事件驱动]({% post_url netty/2020-02-01-netty-event-driven %})
* [Netty4之Future/Promise异步模型]({% post_url netty/2018-04-02-Future-and-Promise-of-Netty4 %})
* [Netty之Reactor模式](https://zhuanlan.zhihu.com/p/33272452)

## ByteBuf详解

* [ByteBuf of Netty]({% post_url 2019-02-10-bytebuf-of-netty %})

## Netty中的设计模式

* 参考：[Netty中的设计模式](https://juejin.im/post/5d04aa52e51d45109b01b18a)

## 关键问题记录

* 上面的 ScheduledExecutorService 和 订阅者模式之间的关系
* Reactor模式 是否就是订阅者模式，那么跟epoll之间的关系如何
* 



## 备忘

### 参考书籍

* [Doug Lea - Scalable IO in Java](http://gee.cs.oswego.edu/dl/cpjslides/nio.pdf)

### 文章

* [源码之下无秘密 ── 做最好的 Netty 源码分析教程](https://segmentfault.com/a/1190000007282628)
* [Netty 实现原理与源码解析系统](http://www.iocoder.cn/Netty/Netty-collection/?vip)
* [Github - netty-learning](https://github.com/code4craft/netty-learning)
* [认真的 Netty 源码解析（一）](https://juejin.im/post/5bdfde8251882516f6632dfe)
* [epoll 的本质是什么？](https://zhuanlan.zhihu.com/p/63179839)

### 视频
* [junfeng hu - 57 Reactor模式透彻理解及其在Netty中的应用](https://www.youtube.com/watch?v=zR4Bro5bphI&list=PLfFz9jdZIa8cj6XIwkSaUbzEdPQZMmhCj&index=28)
* [junfeng hu - Reactor模式5大角色彻底分析](https://www.youtube.com/watch?v=PyiPA_liKKo)
* [Reactive Programming in Java by Venkat Subramaniam](https://www.youtube.com/watch?v=f3acAsSZPhU)
* [Introduction to CompletableFuture in Java 8](https://www.youtube.com/watch?v=ImtZgX1nmr8)

## 待细化知识点

* 服务端启动的时候bootStrap配置的handler是bossGroup使用，childHandler是workGroup所使用的配置







