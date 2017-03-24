---
layout:     post
title:      Redis缓存集群解决方案：Redis cluster 安装
date:       2017-03-21 14:57:27 +0800
postId:     2017-03-21-14-57-27
categories: [blog]
tags:       [Redis]
geneMenu:   true
excerpt:    Redis缓存集群解决方案：Redis cluster 安装
---

## 集群基础概念

redis缓存单机的时候有扩容限制以及宕机风险，在使用redis集群之后就可以解决
这两个问题。在redis3.0之后，增加了redis-cluster实现，所以当前主要依靠以
下几种方式解决集群分布式问题：

* 客户端分片
    将分片工作放在业务程序端，程序代码根据预先设置的路由规则，
    直接对多个Redis实例进行分布式访问。
* 代理分片
    将分片工作交给专门的代理程序来做。代理程序接收到来自业务程序的数据请求，
    根据路由规则，将这些请求分发给正确的Redis实例并返回给业务程序。
    - Twemproxy
    - Codis
* Redis Cluster
    在这种机制下，没有中心节点（和代理模式的重要不同之处）。
    所以，一切开心和不开心的事情，都将基于此而展开。

### 端口号

redis启动实例的时候，一般都会指定客户端连接端口，默认的是6379，这个端口主要
供客户端调用。其实启动的时候，还有另外一个端口被占用，就是当前指定的端口加上
10000，也就是1639。这个端口是在cluster集群中，用于集群中两个节点之间的通信。
注意，不能用于客户端的连接。

### 数据切分

想要解决将数据分布式的存储在多态机器上，就要使用按照一定的规则进行数据的拆分。
在redis中，有一个slot (槽) 的概念，将所有的存储空间分为16384个slot。存储的
时候将key进行CRC16计算之后，对16384取模，得到的结果就是数据存储的slot位置。
而slot可以存储在任意一个可以连接的服务器上，比如如果一共使用3台服务器，那么
slot分布如下：

* Node A contains hash slots from 0 to 5500.
* Node B contains hash slots from 5501 to 11000.
* Node C contains hash slots from 11001 to 16383.

### 主从结构

上面的数据切分解决了单台服务器上不能存储所有数据的问题，还有一个问题就是如果
集群中的任意一台服务器宕机，都会造成部分数据的丢失。想要解决数据丢失的问题，
redis cluster使用了主从备份的方法。一个主服务器可以配置一个或者多个从服务器。

## 安装实例

### 安装路径

按需配置更改为自己所需路径即可：

* 主目录：/home/redis
* redis最新源码：/home/redis/redis-unstable
* redis cluster目录：/home/redis/redis-cluster

### 编译源码

进入目录，从github下载主干最新代码，并编译：

```
cd /home/redis/
git clone https://github.com/antirez/redis.git redis-unstable
cd /home/redis/redis-unstable
make
make test
```

编译完成之后，会在redis-unstable/src目录下生成redis-service可运行程序。
只要有此程序辅以相应的配置文件配置，就可以运行redis程序实现相关功能操作。

### 配置cluster

配置cluster使用到了redis-trib.rb工具，此工具需要系统安装了ruby，以及
gem中安装redis组件：

```
ruby -v
gem install redis
```

之后，创建6个实例目录，以及相关配置文件，并启动：

```
cd /home/redis/redis-cluster
mkdir 7000 7001 7002 7003 7004 7005
# 在这6个目录下分别创建redis.conf文件，相应端口设置为和文件夹名称一致即可：
    port 700X
    cluster-enabled yes
    cluster-config-file nodes.conf
    cluster-node-timeout 5000
    appendonly yes

# 拷贝上面编译好的redis-server到redis-cluster目录下：
cp /home/redis/redis-unstable/src/redis-server /home/redis/redis-cluster/
# 打开6个终端，分别进入6个目录，之后启动redis实例：
cd /home/redis/redis-cluster/7000/ && ../redis-server ./redis.conf

# 拷贝上面编译好的redis-trib.rb到redis-cluster目录下：
cp /home/redis/redis-unstable/src/redis-trib.rb /home/redis/redis-cluster/
# 启动cluster
cd /home/redis/redis-cluster
./redis-trib.rb create --replicas 1 127.0.0.1:7000 \
127.0.0.1:7001 127.0.0.1:7002 127.0.0.1:7003 127.0.0.1:7004 127.0.0.1:7005
```

启动成功之后会有如下信息显示：

```
>>> Creating cluster
>>> Performing hash slots allocation on 6 nodes...
Using 3 masters:
127.0.0.1:7000
127.0.0.1:7001
127.0.0.1:7002
Adding replica 127.0.0.1:7003 to 127.0.0.1:7000
Adding replica 127.0.0.1:7004 to 127.0.0.1:7001
Adding replica 127.0.0.1:7005 to 127.0.0.1:7002
M: 84072fd637abd3e29094a653e3f25ac52d3ca798 127.0.0.1:7000
   slots:0-5460 (5461 slots) master
M: 081da8052edae9a1b373569caa007733b47b50bd 127.0.0.1:7001
   slots:5461-10922 (5462 slots) master
M: 18c6ed2578cf04e44754ebc02ee04211f2e64cca 127.0.0.1:7002
   slots:10923-16383 (5461 slots) master
S: 6ea73447f79bedb8dd1fdc20791f8c168dbaa8f3 127.0.0.1:7003
   replicates 84072fd637abd3e29094a653e3f25ac52d3ca798
S: d0453790e80fbbf41a7296d2a005066b5b72c171 127.0.0.1:7004
   replicates 081da8052edae9a1b373569caa007733b47b50bd
S: 73236ac83b7f3d1696c072027fd1b24fd4eedbc0 127.0.0.1:7005
   replicates 18c6ed2578cf04e44754ebc02ee04211f2e64cca
Can I set the above configuration? (type 'yes' to accept):
```

需要输入 `yes` 并回车继续：

```
>>> Nodes configuration updated
>>> Assign a different config epoch to each node
>>> Sending CLUSTER MEET messages to join the cluster
Waiting for the cluster to join..
>>> Performing Cluster Check (using node 127.0.0.1:7000)
M: 84072fd637abd3e29094a653e3f25ac52d3ca798 127.0.0.1:7000
   slots:0-5460 (5461 slots) master
   1 additional replica(s)
S: 6ea73447f79bedb8dd1fdc20791f8c168dbaa8f3 127.0.0.1:7003
   slots: (0 slots) slave
   replicates 84072fd637abd3e29094a653e3f25ac52d3ca798
S: 73236ac83b7f3d1696c072027fd1b24fd4eedbc0 127.0.0.1:7005
   slots: (0 slots) slave
   replicates 18c6ed2578cf04e44754ebc02ee04211f2e64cca
M: 081da8052edae9a1b373569caa007733b47b50bd 127.0.0.1:7001
   slots:5461-10922 (5462 slots) master
   1 additional replica(s)
S: d0453790e80fbbf41a7296d2a005066b5b72c171 127.0.0.1:7004
   slots: (0 slots) slave
   replicates 081da8052edae9a1b373569caa007733b47b50bd
M: 18c6ed2578cf04e44754ebc02ee04211f2e64cca 127.0.0.1:7002
   slots:10923-16383 (5461 slots) master
   1 additional replica(s)
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.
```

如果出现如上信息，则表示 cluster 启动完成。

### cluster测试

使用redis-cli测试客户端，也是从编译好的目录中拷贝到cluster目录，比较清晰：

```
# 拷贝测试客户端
cp /home/redis/redis-unstable/src/redis-cli /home/redis/redis-cluster/
cd /home/redis/redis-cluster/

# 启动
redis-cli -c -p 7000

# 测试具体操作
redis 127.0.0.1:7000> set foo bar
-> Redirected to slot [12182] located at 127.0.0.1:7002
OK
redis 127.0.0.1:7002> set hello world
-> Redirected to slot [866] located at 127.0.0.1:7000
OK
redis 127.0.0.1:7000> get foo
-> Redirected to slot [12182] located at 127.0.0.1:7002
"bar"
redis 127.0.0.1:7000> get hello
-> Redirected to slot [866] located at 127.0.0.1:7000
"world"
```

可以看出，redis集群操作已经生效。集群节点的增加和删除，以及相关的rehash操作
后面继续处理。

## 参考资料

* [Redis cluster tutorial](https://redis.io/topics/cluster-tutorial)
* [Redis常见集群方案、Codis实践及与Twemproxy比较](http://blog.csdn.net/mawming/article/details/52171116)
* [redis集群搭建之二~使用redis-trib.rb方法](http://blog.csdn.net/naixiyi/article/details/51339374)
* [redis cluster管理工具redis-trib.rb详解](http://blog.csdn.net/huwei2003/article/details/50973967)
* [分布式缓存集群方案特性使用场景优缺点对比及选型](https://my.oschina.net/tantexian/blog/685620)

