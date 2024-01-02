---
layout:     post
title:      Redis - 订阅与发布
date:       2021-10-02 17:42:08 +0800
postId:     2021-10-02-17-42-08
categories: [Redis]
keywords:   [database, Redis]
---

Redis 通过 **`PUBLISH`** 、 **`SUBSCRIBE`** 等命令实现了订阅与发布模式， 这个功能提供两种信息机制， 
分别是 **`订阅/发布到频道`** 和 **`订阅/发布到模式`**。

## 频道的订阅与发布

Redis 的 SUBSCRIBE 命令可以让客户端订阅任意数量的频道， 每当有新信息发送到被订阅的频道时， 
信息就会被发送给所有订阅指定频道的客户端。
作为例子， 下图展示了频道 channel1 ， 以及订阅这个频道的三个客户端 —— client2 、 
client5 和 client1 之间的关系：
![Redis-消息订阅和发布-频道-订阅]({{ site.baseurl }}/image/post/2021/10/02/03/Redis-消息订阅和发布-频道-订阅.svg)

当有新消息通过 PUBLISH 命令发送给频道 channel1 时， 这个消息就会被发送给订阅它的三个客户端：
![Redis-消息订阅和发布-频道-发布]({{ site.baseurl }}/image/post/2021/10/02/03/Redis-消息订阅和发布-频道-发布.svg)

### 频道底层实现
每个 Redis 服务器进程都维持着一个表示服务器状态的 redis.h/redisServer 结构， 
结构的 pubsub_channels 属性是一个字典，这个字典就用于保存订阅频道的信息：
```c
struct redisServer {
    // ...
    dict *pubsub_channels;
    // ...
};
```

其中，字典的键为正在被订阅的频道， 而字典的值则是一个链表， 链表中保存了所有订阅这个频道的客户端。
比如说，在下图展示的这个 pubsub_channels 示例中， client2 、 client5 和 client1 
就订阅了 channel1 ， 而其他频道也分别被别的客户端所订阅：
![Redis-消息订阅和发布-频道-数据结构]({{ site.baseurl }}/image/post/2021/10/02/03/Redis-消息订阅和发布-频道-数据结构.svg)

### 订阅
订阅的时候，只需要将客户端增加到字典中的 channel 对应的链表中即可。

### 发布
发布消息后，根据 key 找到字典中的 channel，然后遍历所有订阅的客户端发送消息即可。

### 退订
退订也只需要根据 channel 找到客户端列表，将对应的客户端移除即可。

## 模式的订阅与发布
当使用 PUBLISH 命令发送信息到某个频道时， 不仅所有订阅该频道的客户端会收到信息， 
如果有某个/某些模式和这个频道匹配的话， 那么所有订阅这个/这些频道的客户端也同样会收到信息。
下图展示了一个带有频道和模式的例子， 其中 tweet.shop.* 模式匹配了 tweet.shop.kindle 
频道和 tweet.shop.ipad 频道， 并且有不同的客户端分别订阅它们三个：

![Redis-消息订阅和发布-模式-订阅]({{ site.baseurl }}/image/post/2021/10/02/03/Redis-消息订阅和发布-模式-订阅.svg)

当有信息发送到 tweet.shop.kindle 频道时， 信息除了发送给 clientX 和 clientY 之外， 
还会发送给订阅 tweet.shop.* 模式的 client123 和 client256 ：
![Redis-消息订阅和发布-模式-发布1]({{ site.baseurl }}/image/post/2021/10/02/03/Redis-消息订阅和发布-模式-发布1.svg)

另一方面， 如果接收到信息的是频道 tweet.shop.ipad ， 那么 client123 和 client256 同样会收到信息：
![Redis-消息订阅和发布-模式-发布2]({{ site.baseurl }}/image/post/2021/10/02/03/Redis-消息订阅和发布-模式-发布2.svg)

### 模式底层实现
redisServer.pubsub_patterns 属性是一个链表，链表中保存着所有和模式相关的信息：
```c
struct redisServer {
    // ...
    list *pubsub_patterns;
    // ...
};
```
链表中的每个节点都包含一个 redis.h/pubsubPattern 结构：
```c
typedef struct pubsubPattern {
    redisClient *client;
    robj *pattern;
} pubsubPattern;
```
client 属性保存着订阅模式的客户端，而 pattern 属性则保存着被订阅的模式。

每当调用 PSUBSCRIBE 命令订阅一个模式时， 程序就创建一个包含客户端信息和被订阅模式的 
pubsubPattern 结构， 并将该结构添加到 redisServer.pubsub_patterns 链表中。

作为例子，下图展示了一个包含两个模式的 pubsub_patterns 链表， 其中 client123 
和 client256 都正在订阅 tweet.shop.* 模式：
![Redis-消息订阅和发布-模式-数据结构]({{ site.baseurl }}/image/post/2021/10/02/03/Redis-消息订阅和发布-模式-数据结构.svg)

如果这时客户端 client10086 执行 PSUBSCRIBE broadcast.list.* ， 
那么 pubsub_patterns 链表将被更新成这样：
![Redis-消息订阅和发布-模式-数据结构-更新]({{ site.baseurl }}/image/post/2021/10/02/03/Redis-消息订阅和发布-模式-数据结构-更新.svg)

通过遍历整个 pubsub_patterns 链表，程序可以检查所有正在被订阅的模式，以及订阅这些模式的客户端。

### 订阅
订阅就是将订阅消息追加到上面的链表信息中。

### 发布
发送信息到模式的工作也是由 PUBLISH 命令进行的， 因为 PUBLISH 除了将 message 发送到所有订阅 
channel 的客户端之外， 它还会将 channel 和 pubsub_patterns 中的模式进行对比， 如果 
channel 和某个模式匹配的话， 那么也将 message 发送到订阅那个模式的客户端。

### 退订
退订就是从模式链表中删除此订阅节点信息。

## 参考资料
* [Redis - 订阅与发布]({% post_url database/redis/content/2021-10-02-03-redis-publish-subscribe %})
* [Redis 设计与实现：订阅与发布](https://redisbook.readthedocs.io/en/latest/feature/pubsub.html)
