---
layout:     post
title:      ByteBuf of Netty
date:       2019-02-10 02:39:05 +0800
postId:     2019-02-10-02-39-05
categories: [blog]
tags:       [开源框架, Netty]
geneMenu:   true
excerpt:    ByteBuf of Netty
---

## ByteBuf简介

### What

ByteBuf是Netty的基础组件之一，是为了解决JDK ByteBuffer使用过于繁杂而创建的替代品。

### Why

网络数据的基本单位总是字节，Java NIO 提供了ByteBuffer作为它的字节容器，但是这个类使用起来过于复杂，而且有些繁琐。Netty的ByteBuffer的
替代品是ByteBuf，一个强大的实现，既解决了JDK API的局限性，又为网络应用程序的开发者提供了更好的API。

Netty的数据处理API通过另个组件暴露——abstract class ByteBuf 和 interface ByteBufHolder。下面是ByteBuf API的优点：
* 它可以被用户自定义的缓冲区类型扩展
* 通过内置的复合缓冲区类型实现了代理的零拷贝
* 容量可以按需增长（类似于JDK的StringBuilder）
* 在读和写这两种模式之间切换不需要调用ByteBuffer的flip()方法
* 读和写使用了不同的索引
* 支持方法的链式调用
* 支持引用计数
* 支持池化

### How

* ByteBuf维护了两个不同的索引：一个用于读取，一个用于写入。
    - 当你从ByteBuf读取时，它的readerIndex将会被递增已经被读取的字节数
    - 当你写入ByteBuf时，它的writerIndex也会被递增
* 名称以read或者write开头的ByteBuf方法，将会推进其对应的索引
* 而名称以set或者get开头的操作则不会，这些方法将在作为一个参数传入的一个相对索引上执行操作

## Buffer大家族

### 数据类型概述
最高层的抽象是ByteBuf，Netty首先根据直接内存和堆内存，将Buffer按照这两个方向去扩展，之后再分别对具体的直接内存和堆内存缓冲区按照是否池化
这两个方向再进行扩展。除了这两个维度，Netty还扩展了基于Unsafe的Buffer：

* 按照底层存储空间划分
    * 堆缓冲区：HeapBuffer
    * 直接缓冲区：DirectBuffer
* 按照对否池化划分
    * 池化：PooledBuffer
    * 非池化：UnPooledBuffer
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

### 堆缓冲区

最常用的ByteBuf模式是将数据存储在JVM的堆空间中，这种模式被称为支撑数组（backing array），它能在没有使用池化的情况下提供快速的分配和释放。
这种方式，非常 __适合于有遗留的数据需要处理的情况__：

    Y2019.M02.D10_ByteBuf.heapBuf.HeapBuf_5_1

```java
ByteBuf heapBuf = Unpooled.wrappedBuffer("Hello Netty ByteBuf".getBytes(StandardCharsets.UTF_8));
if (heapBuf.hasArray()) {                                       // 堆缓冲区 才有 支撑数组
  byte[] array = heapBuf.array();                               // 获取对该数组的一弄
  int offset = heapBuf.arrayOffset() + heapBuf.readerIndex();   // 计算第一个字节偏移量：arrayOffset 是数据起始位，readerIndex 是当前读指针
  int length = heapBuf.readableBytes();                         // 剩余可读字节数
  handlerArray(array, offset, length);                          // 使用数组、偏移量和长度，作为参数调用自定义的相关业务方法
}
```

* heapBuf.array() 方法不会使用减少引用计数，那么堆缓冲区如何判断是否回收？
* 对堆缓冲区的数据使用read方法，返回的是 UnpooledByteBufAllocator 对象类型

### 直接缓冲区

我们期望用于对象创建的内存分配永远都来自于堆中，但这并不是必须的——NIO在JDK 1.4中引入的ByteBuffer类允许JVM实现通过本地调用来分配内存。
这主要是为了避免在每次调用本地I/O操作之前(或之后)将缓冲区的内容复制到一个中间缓冲区(或者从中间缓冲区把内容复制到缓冲区)。

ByteBuffer的Javadoc8明确指出：直接缓冲区的内容将驻留在常规的会被垃圾回收的堆之外。这也就解释了，为何 __直接缓冲区对于网络数据传输是
理想的选择__。如果你的数据包含在一个在堆上分配的缓冲区中，那么事实上，在通过套接字发送它之前，JVM将会在内部把你的缓冲区复制到一个直接
缓冲区中。

直接缓冲区的缺点是，相对于基于堆的缓冲区，他们的分配和释放都较为昂贵。如果你正在处理遗留代码，你也可能会遇到另外一个缺点：因为数据不在堆上，
所以你不得不尽心过一次复制。

显然，与使用支撑数组相比，这设计的工作更多。因此，如果实现知道容器中的数据将会被作为数组来访问，你可能更愿意使用堆内存。


    Y2019.M02.D10_ByteBuf.heapBuf.DirectBuf_5_2

```java
ByteBuf directBuf = new UnpooledDirectByteBuf(ByteBufAllocator.DEFAULT, 1, 100);
directBuf.writeBytes(HELLO_NETTY.getBytes(StandardCharsets.UTF_8));
if (!directBuf.hasArray()) {                                  // 直接缓冲区，没有支撑数组
  int length = directBuf.readableBytes();                     // 可读字节数
  byte[] array = new byte[length];                            // 创建新数组，作为接收容器
  directBuf.getBytes(directBuf.readerIndex(), array);         // 将数据转移到新建数组中：直接缓存不考虑偏移量？
  handlerArray(array, 0, length);                             // 使用新数组调用自定义业务方法
}
```

* 直接缓存没有 `array()`，以及 `arrayOffset()` 方法

### 复合缓冲区

第三种也是最后一种模式，使用的是复合缓冲区，它为多个ByteBuf提供了一个聚合师徒。在这里你可以根据需要添加或者删除ByteBuf实例，这是JDK
的ByteBuffer完全缺失的一个特性。Netty通过一个ByteBuf子类——CompositeByteBuf——实现了这个模式，它提供了一个将多个缓冲区表示为单个
合并缓冲区的虚拟表示。

其他特性暂时略过。

## ByteBuf基本操作

### 随机访问索引

如同在普通的Java字节数组中一样，ByteBuf的索引从零开始：第一个字节的索引是0，最后一个字节的索引是capacity()-1。对于get方法来访问ByteBuf，
既不会改变readerIndex也不会改变writerIndex。如果有需要，也可以通过调用readerIndex(index)或者writerIndex(index)来手动移动这两者。

### 顺序访问索引

在JDK中，ByteBuffer只有一个索引，这就是为什么必须调用flip()方法来在读和写模式之间进行切换的原因。在Netty的ByteBuf中同时具有读索引和
写索引，下图展示了ByteBuf是如何被他的两个索引划分成3个区域的：

    +-------------------+--------------------+------------------+
    |    可丢弃字节       | 可读字节(CONTENT)   |      可写字节      |
    +-------------------+--------------------+------------------+
    0 <---------- readerIndex <-------- writerIndex <------- capacity

* 可丢弃字节：已经被读过的字节
    - 通过调用 discardReadBytes() 方法可以丢弃他们并回收空间
    - 回收之后，readerIndex会变为0，回收的空间会追加到可写字节，也就是可写字节变大
    - 频繁调用回收方法会导致内存复制，所以建议只有在真正需要的时候才这么做
* 可读字节：尚未被读过的字节
    - get开头的方法不会改变readerIndex
    - read或skip开头的方法会改变readerIndex
* 可写字节：可以添加更多字节的空间
    - 任何write开头的操作都将从当前的writeIndex处开始写数据，并将它增加已经写入的字节数

### 派生缓冲区

派生缓冲区为ByteBuf提供了以专门的方式来呈现其内容的视图。这类视图是通过以下方法被创建的：

* duplicate()
* slice()
* slice(int, int)
* Unpooled.unmodifiableBuffer(...)
* order(ByteOrder)
* readSlice(int)

这些方法每个都将返回一个新的ByteBuf实例，它具有自己的读索引、写索引和标记索引。其内部存储是共享的，这使得派生缓冲区的创建成本很低廉，但也
意味着，如果你需改了它的内容，则也同时修改了其对应的源实例，所以要小心。

那派生缓冲区的用途是什么？

## ByteBufHolder接口

略过，没看懂

## ByteBuf 分配

### 按需分配：ByteBufAllocator接口

为了降低分配和释放内存的开销，Netty通过 interface ByteBufAllocator 实现了 ByteBuf 的池化，它可以用来分配上面描述过的任意类型的
ByteBuf实例。使用池化是特定于应用程序的决定，并不会以任何方式改变ByteBuf API 的语义。


ByteBufAllocator的主要方法：
* buffer()
* heapBuffer()
* directBuffer()
* compositeBuffer()
* compositeDirectBuffer()
* compositeHeapBuffer()
* ioBuffer()

可以通过Channel(每个都可以有一个不同的ByteBufAllocator实例)或者绑定到 ChannelHandler 的 ChannelHandlerContext 获取一个到
ByteBufAllocator 的引用。Netty提供了两种ByteBufAllocator的实现：PooledByteBufAllocator 和 UnpooledByteBufAllocator。
前者池化了ByteBuf的实例以提高性能并最大限度的减少内存碎片，后者不池化ByteBuf实例，并在每次滴啊用时都返回一个新的实例。

### Unpooled缓冲区

可能某些情况下，未能获取一个ByteBufAllocator引用，对于这种情况，Netty提供了一个简单的称为Unpooled的工具类，它提供了讲台的辅助方法
来创建未池化的ByteBuf实例：

* buffer()
* directBuffer()
* wrapperBuffer()
* copiedBuffer()

Unpooled类还使得ByteBuf同样可用于哪些并不需要Netty的其他组件的非网络项目，使得其得益于高性能可扩展的缓冲区API。

### ByteBufUtil类

ByteBufUtil 提供了用于操作 ByteBuf 的静态的辅助方法，因为这个API是通用的，并且和池化无关，所以这些方法依然在分配类的外部实现。
最有价值的可能就是 hexdump() 方法，它以十六进制的形式打印 ByteBuf 的内容。



## 参考资料

* [Netty 权威指南](https://github.com/normanmaurer/netty-in-action)
* [Netty精粹之玩转NIO缓冲区 - Kyrie lrving](https://www.cnblogs.com/wxd0108/p/6681627.html)