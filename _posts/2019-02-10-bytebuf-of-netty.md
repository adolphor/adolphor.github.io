---
layout:     post
title:      ByteBuf of netty
date:       2019-02-10 02:39:05 +0800
postId:     2019-02-10-02-39-05
categories: [blog]
tags:       [开源框架, netty]
geneMenu:   true
excerpt:    ByteBuf of netty
---

## Buffer大家族

最高层的抽象是ByteBuf，Netty首先根据直接内存和堆内存，将Buffer按照这两个方向去扩展，之后再分别对具体的直接内存和堆内存缓冲区按照是否池化
这两个方向再进行扩展。除了这两个维度，Netty还扩展了基于Unsafe的Buffer：

* 按照底层存储空间划分
    * DirectBuffer
    * HeapBuffer
* 按照对否池化划分
    * PooledBuffer
    * UnPooledBuffer
* 第三个维度
    * Unsafe

我们分别挑出一个比较典型的实现来进行介绍：

* PooledHeapByteBuf：池化的基于堆内存的缓冲区。
* PooledDirectByteBuf：池化的基于直接内存的缓冲区。
* PooledUnsafeDirectByteBuf：池化的基于Unsafe和直接内存实现的缓冲区。
* UnPooledHeapByteBuf：非池化的基于堆内存的缓冲区。
* UnPooledDirectByteBuf：非池化的基于直接内存的缓冲区。
* UnPooledUnsafeDirectByteBuf：非池化的基于Unsafe和直接内存实现的缓冲区。

除了上面这些，另外Netty的Buffer家族还有CompositeByteBuf、ReadOnlyByteBufferBuf、ThreadLocalDirectByteBuf等等，这里还要说一下
UnsafeBuffer，当当前平台支持Unsafe的时候，我们就可以使用UnsafeBuffer，JAVA DirectBuffer的实现也是基于unsafe来对内存进行操作的，
我们可以看到不同的地方是PooledUnsafeDirectByteBuf或UnPooledUnsafeDirectByteBuf维护着一个memoryAddress变量，这个变量代表着缓冲区
的内存地址，在使用的过程中加上一个offer就可以对内存进行灵活的操作。总的来说，Netty围绕着ByteBuf及其父接口定义的行为分别从是直接内存还是
使用堆内存，是池话还是非池化，是否支持Unsafe来对ByteBuf进行不同的扩展实现。

## 参考资料

* [Netty精粹之玩转NIO缓冲区 - Kyrie lrving](https://www.cnblogs.com/wxd0108/p/6681627.html)