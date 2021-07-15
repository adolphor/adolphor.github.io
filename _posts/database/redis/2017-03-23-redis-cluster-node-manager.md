---
layout:     post
title:      Redis缓存集群解决方案：Redis cluster 节点管理
date:       2017-03-23 16:41:30 +0800
postId:     2017-03-23-16-41-30
categories: []
keywords:   [Redis]
---

在上篇文章《[Redis缓存集群解决方案：Redis cluster 安装]({% post_url database/redis/2017-03-21-redis-cluster-init %})》
中进行了redis cluster的安装，本文重点侧重于cluster节点的操作和维护。

## cluster管理工具

`redis-trib.rb` 是ruby脚本的cluster管理工具，使用 `ruby redis-trib.rb help`
查看使用方法：

```
Usage: redis-trib <command> <options> <arguments ...>

  create          host1:port1 ... hostN:portN
                  --replicas <arg>
  check           host:port
  info            host:port
  fix             host:port
                  --timeout <arg>
  reshard         host:port
                  --from <arg>
                  --to <arg>
                  --slots <arg>
                  --yes
                  --timeout <arg>
                  --pipeline <arg>
  rebalance       host:port
                  --weight <arg>
                  --auto-weights
                  --use-empty-masters
                  --timeout <arg>
                  --simulate
                  --pipeline <arg>
                  --threshold <arg>
  add-node        new_host:new_port existing_host:existing_port
                  --slave
                  --master-id <arg>
  del-node        host:port node_id
  set-timeout     host:port milliseconds
  call            host:port command arg arg .. arg
  import          host:port
                  --from <arg>
                  --copy
                  --replace
  help            (show this help)

For check, fix, reshard, del-node, set-timeout you can specify the host and port of any working node in the cluster.
```

## 初始化

初始化节点配置上文已经操作过：

```shell
./redis-trib.rb create --replicas 1 127.0.0.1:7000 \
127.0.0.1:7001 127.0.0.1:7002 127.0.0.1:7003 127.0.0.1:7004 127.0.0.1:7005
```

## 查看当前节点信息

```shell
# 使用cli登陆任一节点
redis-cli -c -p 7000
# 查看cluster信息
cluster nodes
```

```
127.0.0.1:7000> cluster nodes
18c6ed2578cf04e44754ebc02ee04211f2e64cca 127.0.0.1:7002@17002 master - 0 1492052450848 3 connected 10923-16383
081da8052edae9a1b373569caa007733b47b50bd 127.0.0.1:7001@17001 master - 0 1492052448842 2 connected 5461-10922
73236ac83b7f3d1696c072027fd1b24fd4eedbc0 127.0.0.1:7005@17005 slave 18c6ed2578cf04e44754ebc02ee04211f2e64cca 0 1492052449846 6 connected
d0453790e80fbbf41a7296d2a005066b5b72c171 127.0.0.1:7004@17004 slave 081da8052edae9a1b373569caa007733b47b50bd 0 1492052449846 5 connected
84072fd637abd3e29094a653e3f25ac52d3ca798 127.0.0.1:7000@17000 myself,slave 6ea73447f79bedb8dd1fdc20791f8c168dbaa8f3 0 0 1 connected
6ea73447f79bedb8dd1fdc20791f8c168dbaa8f3 127.0.0.1:7003@17003 master - 0 1492052448841 7 connected 0-5460
```

## 增加节点
新增节点如果是master，那么需要rehashing，如果是slave那么需要关联master。

### 新增master

* 新建7006文件夹
* 添加redis.conf文件并修改对应的端口信息

```
port 7006
cluster-enabled yes
cluster-config-file nodes.conf
cluster-node-timeout 5000
appendonly yes

databases 1
pidfile ./redis_7006.pid
logfile ./redis_7006.log
loglevel debug
```

#### 启动实例

```shell
cd /home/redis/redis-cluster/7006/ && \
../redis-server ./redis.conf &
```

#### 增加实例为 master

使用 `redis-trib.rb` 工具，管理节点：

```shell
cd /home/redis/redis-cluster && \
./redis-trib.rb  add-node 127.0.0.1:7006 127.0.0.1:7000
```

此时，再次使用 `cluster nodes` 查看节点信息，就会多出一个master节点，但是
此时节点的slots为空：

```
b75acaf4a0194d0c1b3cd15f967a802c83a85d8c 127.0.0.1:7006@17006 master - 0 1492052599167 0 connected
```

#### 新增加的 master 再分区

```shell
cd /home/redis/redis-cluster && \
./redis-trib.rb reshard \
--from 18c6ed2578cf04e44754ebc02ee04211f2e64cca \
--to b75acaf4a0194d0c1b3cd15f967a802c83a85d8c \
--slots 200 \
--yes \
--timeout 5000 \
127.0.0.1:7000
```

需要注意的是，虽然help说明中，host:port放在首位，但实际操作的时候放在首位会报错，
具体愿意不清楚。迁移完毕之后，再查看当前节点信息：

```
18c6ed2578cf04e44754ebc02ee04211f2e64cca 127.0.0.1:7002@17002 master - 0 1492057855700 3 connected 11123-16383
081da8052edae9a1b373569caa007733b47b50bd 127.0.0.1:7001@17001 master - 0 1492057855199 2 connected 5461-10922
73236ac83b7f3d1696c072027fd1b24fd4eedbc0 127.0.0.1:7005@17005 slave 18c6ed2578cf04e44754ebc02ee04211f2e64cca 0 1492057855199 6 connected
d0453790e80fbbf41a7296d2a005066b5b72c171 127.0.0.1:7004@17004 slave 081da8052edae9a1b373569caa007733b47b50bd 0 1492057856703 5 connected
84072fd637abd3e29094a653e3f25ac52d3ca798 127.0.0.1:7000@17000 myself,slave 6ea73447f79bedb8dd1fdc20791f8c168dbaa8f3 0 0 1 connected
6ea73447f79bedb8dd1fdc20791f8c168dbaa8f3 127.0.0.1:7003@17003 master - 0 1492057856201 7 connected 0-5460
b75acaf4a0194d0c1b3cd15f967a802c83a85d8c 127.0.0.1:7006@17006 master - 0 1492057855700 8 connected 10923-11122
```

可以发现节点并不是从尾部开始截取，而是从源节点的开始进行截取。

问题：
* rehash期间，数据的更改和查询机制，是怎么处理的？

### 新增slave

参考新增master，同样需要先新建个redis实例配置，

* 新建7007文件夹
* 添加redis.conf文件并修改对应的端口信息

#### 启动实例

```shell
cd /home/redis/redis-cluster/7007/ && \
../redis-server ./redis.conf &
```

#### 增加实例为 slave 并 挂载在 master

从上面的nodes信息可以看到，端口7006的master的id为
 `b75acaf4a0194d0c1b3cd15f967a802c83a85d8c`，所以：

```shell
cd /home/redis/redis-cluster && \
./redis-trib.rb add-node \
--slave \
--master-id b75acaf4a0194d0c1b3cd15f967a802c83a85d8c \
127.0.0.1:7007 127.0.0.1:7000
```

## 删除节点

slave直接删除并没有什么影响，主要考虑删除master的情况。
正常情况下，是rehash之后删除节点。


### 迁移 slots

先将需要删除节点的slot进行迁移：

From master4 => 7006：b75acaf4a0194d0c1b3cd15f967a802c83a85d8c
To master3 => 7002：18c6ed2578cf04e44754ebc02ee04211f2e64cca

```shell
cd /home/redis/redis-cluster && \
./redis-trib.rb reshard \
--from b75acaf4a0194d0c1b3cd15f967a802c83a85d8c \
--to 18c6ed2578cf04e44754ebc02ee04211f2e64cca \
--slots 1000 \
--yes \
--timeout 5000 \
127.0.0.1:7000
```

再次查看节点的时候会发现，master4 清空 slot 之后，其 slave 节点变成了其他
 master 的 slave 节点。

### 删除节点

master4 => 7006：b75acaf4a0194d0c1b3cd15f967a802c83a85d8c
slave4 => 7007：8dc7291c74bd7fae0a23388464db7b8c63f211f5

```shell
cd /home/redis/redis-cluster && \
./redis-trib.rb del-node 127.0.0.1:7000 b75acaf4a0194d0c1b3cd15f967a802c83a85d8c && \
./redis-trib.rb del-node 127.0.0.1:7000 8dc7291c74bd7fae0a23388464db7b8c63f211f5
```

删除成功信息：

```
>>> Removing node b75acaf4a0194d0c1b3cd15f967a802c83a85d8c from cluster 127.0.0.1:7000
>>> Sending CLUSTER FORGET messages to the cluster...
>>> SHUTDOWN the node.
[8]   Done                    cd /home/redis/redis-cluster/7006/ && ../redis-server ./redis.conf
>>> Removing node 8dc7291c74bd7fae0a23388464db7b8c63f211f5 from cluster 127.0.0.1:7000
>>> Sending CLUSTER FORGET messages to the cluster...
>>> SHUTDOWN the node.
[10]-  Done                    cd /home/redis/redis-cluster/7007/ && ../redis-server ./redis.conf
```

## 节点失效的处理

cluster关闭之后会，调用的时候会出现如下信息：

```
(error) CLUSTERDOWN The cluster is down
```

## 问题

* 虽然进行了集群，但是连接的时候连接哪个机器哪个端口呢？如果这个机器宕机，集
群中的其他服务器是不是就无法使用了？
* 多少节点失效之后cluster会关闭？

## 参考资料

* [Redis Cluster Specification](https://redis.io/topics/cluster-spec)
* [全面剖析Redis Cluster原理和应用](http://blog.csdn.net/dc_726/article/details/48552531)
* [唯品会大规模 Redis Cluster 的生产实践](https://mp.weixin.qq.com/s?__biz=MzA4Nzg5Nzc5OA==&mid=2651660079&idx=1&sn=bca50ad39792deadf167077308120264&scene=1&srcid=0603h3CpGqpli4btm4CAhwXC&key=f5c31ae61525f82e72b90a2923b3315c5f9601eb09ab586f91ee7bd376a7f2c29e9c67b143c79bae1dae981c421ee3f9&ascene=0&uin=MjE0MTYxODc2MA%3D%3D&devicetype=iMac+MacBookPro11%2C1+OSX+OSX+10.10.5+build(14F1713)&version=11020201&pass_ticket=b4gGUIHoYiAJF133TykQNLc9g%2BcBN2k5kpq1HtSP%2F7gq1Z5Fuq9KbzOyweWu8XsF)
* [Spring Data Redis](http://docs.spring.io/spring-data/redis/docs/current/reference/html/)
* [redis cluster管理工具redis-trib.rb详解](http://weizijun.cn/2016/01/08/redis%20cluster%E7%AE%A1%E7%90%86%E5%B7%A5%E5%85%B7redis-trib-rb%E8%AF%A6%E8%A7%A3/)
