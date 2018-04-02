---
layout:     post
title:      Netty4之Future/Promise异步模型
date:       2018-04-02 22:23:56 +0800
postId:     2018-04-02-22-23-56
categories: [blog]
tags:       [Java]
geneMenu:   true
excerpt:    Netty4之Future/Promise异步模型
---

# Future & Promise

在并发编程中，我们通常会用到一组非阻塞的模型：Promise，Future 和 Callback。其中的 Future 表示一个可能还没有实际完成的异步任务的结果，
针对这个结果可以添加 Callback 以便在任务执行成功或失败后做出对应的操作，而 Promise 交由任务执行者，任务执行者通过 Promise 可以标记任务
完成或者失败。 可以说这一套模型是很多异步非阻塞架构的基础。Netty 4中正提供了这种Future/Promise异步模型。

Netty文档说明Netty的网络操作都是异步的， 在源码上大量使用了Future/Promise模型，在Netty里面也是这样定义的：
* Future接口定义了isSuccess(),isCancellable(),cause(),这些判断异步执行状态的方法。（read-only）
* Promise接口在extneds future的基础上增加了setSuccess(), setFailure()这些方法。（writable）

# Handler & ChildHandler

在基类AbstractBootstrap有handler方法，目的是添加一个handler，监听Bootstrap的动作，客户端的Bootstrap中，继承了这一点。

在服务端的ServerBootstrap中增加了一个方法childHandler，它的目的是添加handler，用来监听已经连接的客户端的Channel的动作和状态。

handler在初始化时就会执行，而childHandler会在客户端成功connect后才执行，这是两者的区别。

# Pipeline

pipeline是伴随Channel的存在而存在的，交互信息通过它进行传递，我们可以addLast（或者addFirst）多个handler，第一个参数是名字，无具体要求，
如果填写null，系统会自动命名。

```java
//添加一个Hanlder用来处理各种Channel状态  
pipeline.addLast("handlerIn", new ClientHandler());  
//添加一个Handler用来接收监听IO操作的  
pipeline.addLast("handlerOut", new OutHandler());  
```


## 参考资料

* [Netty4之Future/Promise异步模型](https://blog.csdn.net/kobejayandy/article/details/47778359)
* [Netty:Bootstrap的handler和childHandler](https://blog.csdn.net/bdmh/article/details/49927787)
