# CPU占用过高的问题

打开client客户端，浏览器访问几个页面之后，就会出现CPU占用过高的问题。

```
$ top
$ ps aux | grep 48451
$ jstack 48451 > stack.txt
```

经过排查发现大部分线程卡在PacFilter中，主要的就是正则过滤执行时间过长。
后来发现是正则表达式写的有问题，具体正则规则后续分析：

* 有问题的正则
    - Pattern.matches("^(\\w?.?)+" + conf, domain)
* 改造后的正则
    - Pattern.matches("([a-z0-9]+[.])*" + conf, domain)


# HTTP代理

## 编解码

http 的 tunnel 代理直接连接，去掉所有编解码器之后就可以进行盲转了，
但是 HTTP 代理，在进行转发的时候需要加上编解码器，这样 proxy 和 server 
进行数据交换的时候，才能够识别到正确的 HTTP 内容。

* 加密实例不能初始化之后保存为静态变量来引用，不知道为什么就是会报错？


## HttpObjectAggregator
当 HTTP content 内容太大的时候，是不是会出现内容不全的情况？

好像会，改成分段模式吧：

* HttpRequest（1个）
* HttpContent（可能有 0~N 个 content）
* LastHttpContent（1个）

## 信息发送之后没反应
首先考虑编解码问题，怎么可以快速查看当前channel的所有handler？


# Netty 相关问题

## io.netty.util.IllegalReferenceCountException: refCnt: 0, increment: 1

这个问题的原因是释放msg的时候发现msg的引用计数为0，解决方法是在传递msg的地方使用retain()方法
告诉netty此消息传递给channel中的下个handler进行消费。


## Netty中ctx.writeAndFlush与ctx.channel().writeAndFlush的区别
https://blog.csdn.net/FishSeeker/article/details/78447684

先说实验结论就是 ctx 的writeAndFlush是从当前handler直接发出这个消息，
而 channel 的writeAndFlush是从整个pipline最后一个outhandler发出。

## netty channelActive 方法
只有channel建立的时候会会调用，当增加handler或者删除handler的时候，除非手动调用，否则不再执行active方法。

Q: 如果有多个handler，那么每个handler中的active方法都会执行吗？

## Netty 超时时间
对于HTTP 的 connection 头信息，keep-alive是否要特别设置超时时间来处理？

## host is down

自己电脑的防火墙拦截，或者其他网络拦截工具导致的拦截

## java.lang.IllegalStateException: executor not accepting a task

断线重连时的异常，原因是上次连接的 EventLoopGroup 被关闭了，重连需要重新创建。

## handler 是否复用的问题

现在走向了一个极端，就是handler全部使用了channel存储变量，是否有必要？是否能够提高效率？待验证

* 如果复用，handler中不能含有私有变量，需要改为channel的attr形式
* 私有变量的好处：可以再handler的不同方法中中直接调用，新增或移除handler对这些变量没有影响 (哪些变量会在移除或新增handler的时候收到影响？)
* 私有变量的缺点：channel active方法中设置的变量只对当前handler有效，如果handler有移除或新增，那些在active方法中赋值的变量就会丢失
* attr的好处：就是在任何handler中都可以获取到channel active的时候设置的变量，只要channel还存活就可以获取
* attr的缺点：设置和获取的时候比较麻烦，debug的时候也不知道当前设置了哪些变量（看看有没有方法优化）

## netty 的 ByteBuf 相关操作

* alloc
```java
ByteBuf buf = ctx.alloc().buffer(msg.getData().readableBytes());
buf.writeBytes(msg.getData());
```

* copy() 操作是否消耗引用计数？

在log日志中打印ByteBuf的时候会消耗引用计数，那么使用copy方法的时候消耗吗？

## postman 使用HTTPS MITM的时候报错

待解决

## channel间的影响

如果一次有好多网站请求，比如google，baidu，当google访问失败的时候，会导致
baidu也受到影响，会直接返回失败，是各个channel之间的通信互相影响了吗？

## channnel 的异常管理

* 报错怎么处理？
* 报错需要关闭channel吗？关闭和不关闭的判断标准？各有什么影响？
* exception 在 channel 之间的传递过程？ 

## 方法覆写，是否需要调用父类实现？
* 心跳相关，超时的时候，在自定义的方法中是否有必要调用 `super.channelIdle(ctx, evt);` ？


## lan接收消息过长

```
[2019-02-16 00:31:15:540 WARN ] 110079 [nioEventLoopGroup-5-1] com.adolphor.mynety.common.wrapper.AbstractSimpleHandler.exceptionCaught(61): [ 726bc8ad ] [com.adolphor.mynety.server.lan.LanConnInBoundHandler] error
io.netty.handler.codec.TooLongFrameException: Adjusted frame length exceeds 2097152: 15044400 - discarded
	at io.netty.handler.codec.LengthFieldBasedFrameDecoder.fail(LengthFieldBasedFrameDecoder.java:522) ~[netty-all-4.1.32.Final.jar:4.1.32.Final]
	at io.netty.handler.codec.LengthFieldBasedFrameDecoder.failIfNecessary(LengthFieldBasedFrameDecoder.java:500) ~[netty-all-4.1.32.Final.jar:4.1.32.Final]
	at io.netty.handler.codec.LengthFieldBasedFrameDecoder.exceededFrameLength(LengthFieldBasedFrameDecoder.java:387) ~[netty-all-4.1.32.Final.jar:4.1.32.Final]
	at io.netty.handler.codec.LengthFieldBasedFrameDecoder.decode(LengthFieldBasedFrameDecoder.java:430) ~[netty-all-4.1.32.Final.jar:4.1.32.Final]
	at com.adolphor.mynety.common.bean.lan.LanMessageDecoder.decode(LanMessageDecoder.java:36) ~[mynety-common-0.0.6-SNAPSHOT.jar:?]
	at com.adolphor.mynety.common.bean.lan.LanMessageDecoder.decode(LanMessageDecoder.java:27) ~[mynety-common-0.0.6-SNAPSHOT.jar:?]
	at io.netty.handler.codec.LengthFieldBasedFrameDecoder.decode(LengthFieldBasedFrameDecoder.java:343) ~[netty-all-4.1.32.Final.jar:4.1.32.Final]
	at io.netty.handler.codec.ByteToMessageDecoder.decodeRemovalReentryProtection(ByteToMessageDecoder.java:502) ~[netty-all-4.1.32.Final.jar:4.1.32.Final]
	at io.netty.handler.codec.ByteToMessageDecoder.callDecode(ByteToMessageDecoder.java:441) ~[netty-all-4.1.32.Final.jar:4.1.32.Final]
	at io.netty.handler.codec.ByteToMessageDecoder.channelRead(ByteToMessageDecoder.java:278) ~[netty-all-4.1.32.Final.jar:4.1.32.Final]
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:362) [netty-all-4.1.32.Final.jar:4.1.32.Final]
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:348) [netty-all-4.1.32.Final.jar:4.1.32.Final]
	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:340) [netty-all-4.1.32.Final.jar:4.1.32.Final]
	at io.netty.channel.DefaultChannelPipeline$HeadContext.channelRead(DefaultChannelPipeline.java:1434) [netty-all-4.1.32.Final.jar:4.1.32.Final]
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:362) [netty-all-4.1.32.Final.jar:4.1.32.Final]
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:348) [netty-all-4.1.32.Final.jar:4.1.32.Final]
	at io.netty.channel.DefaultChannelPipeline.fireChannelRead(DefaultChannelPipeline.java:965) [netty-all-4.1.32.Final.jar:4.1.32.Final]
	at io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:163) [netty-all-4.1.32.Final.jar:4.1.32.Final]
	at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:656) [netty-all-4.1.32.Final.jar:4.1.32.Final]
	at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:591) [netty-all-4.1.32.Final.jar:4.1.32.Final]
	at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:508) [netty-all-4.1.32.Final.jar:4.1.32.Final]
	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:470) [netty-all-4.1.32.Final.jar:4.1.32.Final]
	at io.netty.util.concurrent.SingleThreadEventExecutor$5.run(SingleThreadEventExecutor.java:909) [netty-all-4.1.32.Final.jar:4.1.32.Final]
	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30) [netty-all-4.1.32.Final.jar:4.1.32.Final]
	at java.lang.Thread.run(Thread.java:748) [?:1.8.0_151]
```
## 内存溢出

```
[2019-02-16 11:05:00:382 ERROR] 9574626 [nioEventLoopGroup-130-1] io.netty.util.ResourceLeakDetector.reportTracedLeak(317): LEAK: ByteBuf.release() was not called before it's garbage-collected. See http://netty.io/wiki/reference-counted-objects.html for more information.
Recent access records: 
Created at:
	io.netty.buffer.PooledByteBufAllocator.newDirectBuffer(PooledByteBufAllocator.java:331)
	io.netty.buffer.AbstractByteBufAllocator.directBuffer(AbstractByteBufAllocator.java:185)
	io.netty.buffer.AbstractByteBufAllocator.directBuffer(AbstractByteBufAllocator.java:176)
	io.netty.buffer.AbstractByteBufAllocator.ioBuffer(AbstractByteBufAllocator.java:137)
	io.netty.channel.DefaultMaxMessagesRecvByteBufAllocator$MaxMessageHandle.allocate(DefaultMaxMessagesRecvByteBufAllocator.java:114)
	io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:147)
	io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:656)
	io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:591)
	io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:508)
	io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:470)
	io.netty.util.concurrent.SingleThreadEventExecutor$5.run(SingleThreadEventExecutor.java:909)
	io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
	java.lang.Thread.run(Thread.java:745)
```



