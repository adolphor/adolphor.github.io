---
layout:     post
title:      分布式以及CAP理论
date:       2017-03-23 16:43:07 +0800
postId:     2017-03-23-16-43-07
categories: [blog]
tags:       [分布式]
geneMenu:   true
excerpt:    分布式以及CAP理论
---

## CAP定理

2000年7月，加州大学伯克利分校的Eric Brewer教授在ACM PODC会议上提出CAP猜想。
2年后，麻省理工学院的Seth Gilbert和Nancy Lynch从理论上证明了CAP。之后，CAP
理论正式成为分布式计算领域的公认定理。

在理论计算机科学中，CAP定理（CAP theorem），又被称作布鲁尔定理（Brewer's
theorem），它指出对于一个分布式计算系统来说，不可能同时满足以下三点：

1. 一致性（Consistence)

    All clients always have the same view of the data。
    等同于所有节点访问同一份最新的数据副本

2. 可用性（Availability）

    Each client can alwa read and write。
    每次请求都能获取到非错的响应——但是不保证获取的数据为最新数据

3. 分区容错性（Network partitioning）

    The system works well despite physical network partitions。
    以实际效果而言，分区相当于对通信的时限要求。系统如果不能在时限内达成数据一致
    性，就意味着发生了分区的情况，必须就当前操作在C和A之间做出选择。

根据定理，分布式系统只能满足三项中的两项而不可能满足全部三项。理解CAP理论的
最简单方式是想象两个节点分处分区两侧。允许至少一个节点更新状态会导致数据不一
致，即丧失了C性质。如果为了保证数据一致性，将分区一侧的节点设置为不可用，那
么又丧失了A性质。除非两个节点可以互相通信，才能既保证C又保证A，这又会导致丧
失P性质。

## 取舍分类

### CA without P

如果不要求P（不允许分区），则C（强一致性）和A（可用性）是可以保证的。但其实
分区不是你想不想的问题，而是始终会存在，因此CA的系统更多的是允许分区后各子系
统依然保持CA。

### CP without A

如果不要求A（可用），相当于每个请求都需要在Server之间强一致，而P（分区）会
导致同步时间无限延长，如此CP也是可以保证的。很多传统的数据库分布式事务都属于
这种模式。

### AP wihtout C

要高可用并允许分区，则需放弃一致性。一旦分区发生，节点之间可能会失去联系，为
了高可用，每个节点只能用本地数据提供服务，而这样会导致全局数据的不一致性。现
在众多的NoSQL都属于此类。


## 分类图表参考

### 图表1

![CAP基本分类](/image/post/2017/03/23/20170323-0101-cap.jpg)

### 图表2

![CAP详细分类](/image/post/2017/03/23/20170323-0102-cap.jpg)

## 参考资料

* [CAP定理 —— 维基百科](https://zh.wikipedia.org/wiki/CAP%E5%AE%9A%E7%90%86)
* [可能是CAP理论的最好解释](http://blog.csdn.net/dc_726/article/details/42784237)
* [分布式系统的CAP理论](http://www.hollischuang.com/archives/666)
* [不懂点CAP理论，你好意思说你是做分布式的吗？](http://www.yunweipai.com/archives/8432.html)
