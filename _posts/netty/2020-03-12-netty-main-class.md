---
layout:     post
title:      Netty源码 - 核心类
date:       2020-03-12 20:25:55 +0800
postId:     2020-03-12-20-25-55
categories: [Netty]
tags:       [Netty]
geneMenu:   true
excerpt:    Netty源码 - 核心类
---

## 核心的概念

Netty的异步事件驱动模型主要涉及到下面几个核心的概念：
* Channel：表示一个与socket关联的通道
* ChannelPipeline： 管道，一个Channel拥有一个ChannelPipeline，ChannelPipeline维护着一个处理链（严格的说是两个：upstream、downstream），处理链是由很多处理句柄ChannelHandler所构成，每个ChannelHandler处理完以后会传递给链中的下一个处理句柄继续处理。
* ChannelHandler：处理句柄，用户可以定义自己的处理句柄来处理每个请求，或发出请求前进行预处理，典型的有编码/解码器：decoder、encoder。
* ChannelEvent：事件，是整个模型的处理对象，当产生或触发（fire）一个事件时，该事件会沿着ChannelPipeline处理链依次被处理。
* ChannelFuture： 异步结果，这个是异步事件处理的关键，当一个事件被处理时，可以直接以ChannelFuture的形式直接返回，不用在当前操作中被阻塞。可以通过 ChannelFuture得到最终的执行结果，具体的做法是在ChannelFuture添加监听器listener，当操作最终被执行完 后，listener会被触发，我们可以在listener的回调函数中预定义我们的业务代码。
* EventLoopGroup：事件循环组，
* EventExecutorGroup（接口）：继承自java并发包ScheduledExecutorService，执行定时任务，只要使用其`next()`方法
* BootStrap：启动辅助类，

## Channel

![Channel]({{ site.baseurl }}/image/post/2020/03/12/netty/Channel.png)

## ChannelPipeline

![Channel]({{ site.baseurl }}/image/post/2020/03/12/netty/ChannlePipeline.png)

## ChannelFuture && ChannelPromise

* ChannelFuture: The result of an asynchronous `Channel` I/O operation.
* ChannelPromise: Special `ChannelFuture` which is writable.

![Channel]({{ site.baseurl }}/image/post/2020/03/12/netty/ChannelPromise.png)

## NioEventLoop.png

* EventExecutorGroup: The EventExecutorGroup is responsible for providing the EventExecutor's to use via its next() method. Besides this, it is also responsible for handling their life-cycle and allows shutting them down in a global fashion.
* EventExecutor: The EventExecutor is a special EventExecutorGroup which comes with some handy methods to see if a Thread is executed in a event loop. Besides this, it also extends the EventExecutorGroup to allow for a generic way to access methods.
* EventLoopGroup: Special EventExecutorGroup which allows registering Channels that get processed for later selection during the event loop.

![Channel]({{ site.baseurl }}/image/post/2020/03/12/netty/NioEventLoop.png)

## NioEventLoopGroup

![Channel]({{ site.baseurl }}/image/post/2020/03/12/netty/NioEventLoopGroup.png)

## SimpleChannelInboundHandler

![Channel]({{ site.baseurl }}/image/post/2020/03/12/netty/SimpleChannelInboundHandler.png)




## 参考资料

* [test](test.html)
