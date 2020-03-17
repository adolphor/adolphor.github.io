---
layout:     post
title:      Netty源码 - 事件驱动
date:       2020-02-01 21:37:49 +0800
postId:     2020-02-01-21-37-49
categories: [article,Netty]
tags:       [Netty]
geneMenu:   true
excerpt:    Netty源码 - 事件驱动
---

在本文之前有两方面的基础知识需要掌握：
* [设计模式之 —— 观察者模式]({% post_url design-pattern/2020-01-23-java-design-patterns-Observer %})
* [Java源码 —— 并发包 ScheduledExecutorService]({% post_url concurrent/2020-02-09-java-util-concurrent-ScheduledExecutorService %})

Netty使用了异步的事件驱动模型，来触发网络I/O的各种操作，其在socket层上面封装一层异步事件驱动模型，使得业务代码不需要关心网络底层，
就可以编写异步的无网络I/O阻塞的代码。



## 参考资料

* [小飞侠 - netty之事件驱动原理](https://blog.csdn.net/qq_26562641/article/details/50392308)
* [从Java NIO 到 Reactor模式](https://www.javazhiyin.com/52132.html)
* [Java设计模式之Reactor（反应器）模式初探](https://blog.csdn.net/u013412772/article/details/80190460)
* 图片清晰无水印 [Netty 那些事儿 ——— Reactor模式详解](https://www.jianshu.com/p/1ccbc6a348db)
* 原创文章 [netty学习系列二：NIO Reactor模型 & Netty线程模型](https://www.jianshu.com/p/38b56531565d)
* 【推荐】实现并演绎了NIO发展历程，且UML图片清晰[Java进阶（五）Java I/O模型从BIO到NIO和Reactor模式](http://www.jasongj.com/java/nio_reactor/)
* 自己实现了一套简单的Reactor：[Reactor模式详解＋源码实现](https://www.jianshu.com/p/188ef8462100)
* [Java NIO 系列文章之 浅析Reactor模式](https://juejin.im/post/5ba3845e6fb9a05cdd2d03c0)
* [Java NIO 系列文章之 浅析Reactor模式](https://pjmike.github.io/2018/09/20/Java-NIO-系列文章之-浅析Reactor模式/)
