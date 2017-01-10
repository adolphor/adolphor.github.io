---
layout:     post
title:      《Redis设计与实现 —— 黄健宏》读书笔记
date:       2016-08-20 00:39:42 +0800
postId:     2016-08-20-00-39-42
categories: [blog]
tags:       [Redis, 读书笔记]
geneMenu:   true
excerpt:    【redis设计与实现】读书笔记
---

## 引言

## 数据结构与对象

### 简单动态字符串

#### SDS结构定义
Redis没有直接使用C语言传统的字符串表示，而是自己构建了一种名为简单字符串
（simple dynamic string, SDS）的抽象类型，并将SDS用做Redis的默认字符串表示。

{% highlight C %}
typedef struct sdshdr {
    int len;    // 已使用长度
    int free;   // 未使用长度
    char buf[]; // 字节数组，用于保存字符串
}
{% endhighlight %}

#### SDS与C语言字符串区别

* 获取字符串长度的时间复杂度是O(1)
* 杜绝缓冲区溢出  
    根据len属性使用内存
* 减少修改字符串时带来的内存重分配次数  
    C语言每次修改字符串都要对底层数组进行空间的增加和减少操作，而是用SDS增加了下面两个特性：
    * 空间预分配  
        对字符串预留了一些额外的空间，少量的字符串修改就不必进行重新分配内存操作
    * 惰性空间释放  
        减少内容之后，并不立即进行空间的回收，但也不会造成内存泄漏
* 二进制安全  
    因为C语言字符串以空字符来表示结束，所以中间不能含有空字符；但SDS以len来判断是否结束，所以可以含有空字符
* 兼容部分C字符串函数  
    都以空字符结尾，符合C语言字符串规范（当然，中间含有空字符除外）


### 链表
C语言并没有实现链表结构，所以Redis构建了自己的链表实现。
链表键、发布与订阅、慢查询、监视器等功能也用到了链表。
Redis服务器还使用链表来保存多个客户端的状态信息，以及使用链表来构建客户端输出缓冲区。

#### 链表和链表节点的实现
链表结构如下：
{% highlight C %}
typedef struct list {
    listNode *head;     // 表头节点
    listNode *tail;     // 表尾节点
    unsigned long len;  // 链表所包含的节点数量
    void *(*dup) {void *ptr};   // 节点值复制函数
    void (*free) {void *ptr};   // 节点值释放函数
    int (*match) {void *ptr, void *key};    // 节点值对比函数
    
} list;
{% endhighlight %}

Redis实现的链表的特性：

* 双端
* 无环
* 带表头指针和表尾指针
* 带链表长度计数器
* 多态


### 字典
字典，又称为符号表（symbol table）、关联数组（associative array）或映射（map），
是一种保存键值对（key-value pair）的抽象数据结构。字典中的 key 唯一，不能重复，比如
Java语言中 HashSet 的内部存储就是利用 HashMap 的 key 来实现无重复 Set 集合。
C语言并没有实现字典结构，所以Redis构建了自己的字典实现。

#### 字典的实现
字典实现结构如下：
{% highlight C %}
// 字典
typedef struct dict {
    dictType *type; // 类型特定函数
    void *privData; // 私有数据
    dictht ht[2];   // 哈希表
}
// 字典类型
typedef struct dictType {
    ......          // 一系列函数
} dictType;
// 哈希表
typedef struct dictht {
    dictEntry **table;      // 哈希表数组
    unsigned long size;     // 哈希表大小
    unsigned long sizemask; // 哈希表大小掩码，用于计算索引值，总是等于 size-1
    unsigned long used;     // 该哈希表已有节点的数量
} dictht;
// 哈希表节点
typedef struct dictEntry {
    void *key;              // key
    union{                  // value存储单元
        void *val;              // 指针类型
        uint64_tu64;            // uint64_t类型
        int64_ts64;             // int64_t类型
    } v;
    struct dictEntry *next; // 指向下个哈希表节点，形成链表
} dictEntry;
{% endhighlight %}
dict 中的 type 属性是一个指向 dictType 结构的指针，每个 dictType 包含了对于不同的键值对操作相关的函数。
而 dict 中的 dictht 属性是一个 dictht 类型的数组 ht[2]，之所以有两个哈希表，是为了 rehash 的时候使用。
dictht 的 table 属性是一个指向数组的指针，而数组中的每个元素是指向一个 dict.h / dictEntry 结构的指针，
每个 dictEntry 保存着一个键值对：key 和 v 。Key 属性保存这键值对中的键，如果按照一定的规则对这个键进行散列，
也就是求取得到 hashCode，之后根据这个 hashCode 值决定这个 dictEntry 的在数组中的存储地址。虽然不同的 hashCode
对应的 key 值肯定不同，但是不同的 key 值却可能有相同的 hashCode，为了解决这种情况有两种解决方案：对求取的 hashCode 
继续进行散列，直至不冲突，另一种方案是直接挂在当前数组所含有的 dictEntry 后面，形成数组中的链表。Redis 采用的是第二种方案。
dictEntry 中的 v 属性保存着键值对中的值，这个值可以是一个指针，或者是一个 uint64_t 整数，或者是一个 int64_t 整数。


### 跳跃表
跳跃表（skipList）是一种有序数据结构，它通过在每个节点中维持多个指向其他节点的指针，从而达到快速访问的目的。
大部分情况下，跳跃表的效率可以和平衡树媲美，但实现要比平衡树简单。Redis中只有两个地方用到了跳跃表，一个是
有序集合键，一个是在集群节点中用作内部数据结构。

#### 跳跃表的实现
{% highlight C %}
// 跳跃表
typedef struct zskiplist {
    structz skiplistNode *header, *tail;    // 表头节点 和表尾节点
    unsigned long length;                   // 表中那个节点的数量
    int level;                              // 表中层数最大的节点的层数
}

// 跳跃表节点
typedef struct zskiplistNode {
    // 层
    struct zskiplistLevel {
        struct zskiplistNode *forward;  // 前进指针
        unsigned int span;      // 跨度
    } level[];
    struct zskiplistNode *backward;     // 后退指针
    double score;   // 分值
    robj *obj;      // 成员对象
} zskiplistNode;
{% endhighlight %}


### 整数集合
整数集合（intset）是集合键的底层实现之一。用于保存整数值的抽象数据结构，它可以保存类型为
int16_t、int32_t 或者 int64_t 的整数值，并且保证集合中不会出现重复元素。

#### 整数集合的实现
{% highlight C %}
typedef struct intset {
    uint32_t encoding;  // 编码方式
    uint32_t length;    // 集合包含的元素数量
    int8_t contents[];  // 保存元素的数组
} intset;
{% endhighlight %}
虽然 intset 结构将 contents 属性生命为 int8_t 类型的数组，
但实际上 contents 数组并不保存 int8_t 类型的值，contents 数组
的真正类型取决于 encoding 属性的值。也就是所 contents 中的数据的
长度是可变的，还能够进行 “升级” 操作。


### 压缩列表
压缩列表（ziplist）是为了节约内存空间而开发的，是由一系列特殊编码的连续内存块组成的顺序型数据结构。
当一个列表键只包含少量列表项，并且每个列表项要么就是小整数值，要么就是长度比较短的字符串，
那么Redis就会使用压缩列表来做列表键的底层实现。

* 压缩列表是一种为节约内存而开发的顺序新数据结构
* 压缩列表是列表键和哈希键的底层实现之一。
* 压缩列表可以包含多个节点，每个节点可以保存一个字节数组或者整数值
* 添加新节点到压缩列表，或者从压缩列表中删除节点，可能会引发连锁更新操作，但这种操作出现的几率并不高

#### 压缩列表的实现
{% highlight C %}
typedef struct ziplist {
    uint32_t zlbytes;   // 压缩列表所占用的内存字节数
    uint32_t zltail;    // 压缩列表表尾节点距离压缩列表的起始地址有多少字节
    uint16_t zllen;     // 压缩列表包含的节点数量
    zipNode *entryA;    // 压缩列表节点(注意，这里并不是指向数组的指针，此结构就包含数组本身，分别指向每个节点)
    zipNode *entryB;
    ......
    zipNode *entryX;
    uint8_t zlend;      // 特殊值0xFF(十进制255)，用于标记压缩列表末端
} ziplist;
typedef struct zipNode {
    previous_entry_length;  // 压缩列表中前一个节点的长度
    encoding;               // content属性所保存的数据的类型和长度
    content;                // content属性负责保存节点的值，可以是一个字节数组或者整数
} zipNode;
{% endhighlight %}


### 对象 {#redisObject}

* Redis数据库中的每个键值对的键和值都是一个对象
* Redis是基于上面的数据结构创造的一个对象系统，包含如下五种对象：  
    * 字符串对象（string Object）
    * 列表对象（list Object）
    * 哈希对象（hash Object）
    * 集合对象（set Object）
    * 有序集合对象（sorted set Object）
* 服务器在执行某些命令之前，会向检查给定键的类型能否执行指定的命令，而检查一个键的类型
就是检查键的值的对象的类型
* Redis的对下给你系统带有应用计数实现的内存回收机制
* Redis会共享值为 0 到 9999 的字符串对象
* 对象会记录自己的最后一次被访问的时间，这个时间可以用于计算对象的空转时间


#### 对象的类型和编码
Redis中的每个对象都由一个redisObject结构表示，该结构中和保存数据有关的三个属性分别是
type属性、encoding属性和ptr属性：
{% highlight C %}
typedef struct redisObject {
    unsigned type:4;    // 类型
    unsigned encoding:4;// 编码
    void *ptr;          // 指向底层实现数据结构的指针
} redisObject;
{% endhighlight %}

##### 类型
对象的type属性记录了对象的类型，对于Redis来说，键总是一个字符串对象，
而值可以是字符串对象、列表对象、哈希对象、集合对象或者有序集合对象的其中一种。

类型常量         | 对象名称
---             | ---
REDIS_STRING    | 字符串对象
REDIS_LIST      | 列表对象
REDIS_HASH      | 哈希对象
REDIS_SET       | 集合对象
REDIS_ZSET      | 有序集合对象

##### 编码和底层实现

对象的ptr指针指向对象的底层实现数据结构，而这些数据结构由对象的encoding属性决定。
encoding属性记录了对象所使用的编码，也就是说这个对象使用了生命数据结构作为对象的底层实现。

编码常量                     | 编码所对应的底层数据结构
---                         | ---
REDIS_ENCODING_INT          | long类型的整数
REDIS_ENCODING_EMBSTR       | embstr编码的简单动态字符串
REDIS_ENCODING_RAW          | 简单动态字符串
REDIS_ENCODING_HT           | 字典
REDIS_ENCODING_LINKEDLIST   | 双端链表
REDIS_ENCODING_ZIPLIST      | 压缩列表
REDIS_ENCODING_INTSET       | 整数集合
REDIS_ENCODING_SKIPLIST     | 跳跃表和字典

每种类型的对象都至少使用了两种不同的编码：

类型            | 编码
---             | ---
REDIS_STRING    | REDIS_ENCODING_INT 
REDIS_STRING    | REDIS_ENCODING_EMBSTR
REDIS_STRING    | REDIS_ENCODING_RAW
REDIS_LIST      | REDIS_ENCODING_ZIPLIST
REDIS_LIST      | REDIS_ENCODING_LINKEDLIST
REDIS_HASH      | REDIS_ENCODING_ZIPLIST
REDIS_HASH      | REDIS_ENCODING_HT
REDIS_SET       | REDIS_ENCODING_INTSET
REDIS_SET       | REDIS_ENCODING_HT
REDIS_ZSET      | REDIS_ENCODING_ZIPLIST
REDIS_ZSET      | REDIS_ENCODING_SKIPLIST

使用 `OBJECT ENCODING` 命令可以查看一个数据库键的值对象的编码。


## 单机数据库的实现

### 数据库

#### 数据库的实现
数据库服务端和客户端实现的数据结构：
{% highlight C %}
typedef struct redisServer {
    int dbnum;          // 服务器的数据库数量，默认值是16
    redisDb[] *db;      // 一个数组，保存着服务器中的所有数据库，数组大小由dbnum决定
    ......
    list *clients;      // 连接到此服务器的客户端链表
} redisServer;
typedef struct redisClient {
    redisDb *db;    // 记录客户端当前正在使用的数据库
    dict *dict;     // 键空间，字典类型，保存着数据库中的所有键值对
    dict *expires;  // 过期时间，字典类型，键是指向对象的指针，值是long类型的时间戳
    ......
} redisClient;
{% endhighlight %}

数据库客户端和服务端的关系如图所示：
![数据库](/image/post/2016/08/20/20160822-0902.png)


上节图示中连接的是1号数据库，切换数据库指令：`SELECT <dbnum>`，比如 `SELECT 2`，就能够切换到2号数据库。

#### 数据库键空间

* 键空间的键也就是数据库的键，每个键都是一个字符串对象
* 键空间的值也就是数据库的值，每个值都可以是前面所述五种 [对象](#redisObject) 的其中一种

![数据库键空间范例](/image/post/2016/08/20/20160822-0912.jpg)

#### Redis的过期键删除策略

对于过期键的删除一般有如下三种方式：  

* 定时删除
    在设置键的过期时间的同时，创建一个定时器（timer），定时结束立即删除
* 惰性删除
    每次响应请求从键空间获取键的时候检查是否过期，如果过期进行删除
* 定期删除
    每隔一段时间，对数据库进行检查，批量删除过期的键，是上述两种方法的整合和折中

Redis是配合使用惰性删除和定期删除两种策略，以很好得在合理使用CPU时间和避免浪费内存空间之间取得平衡。

#### AOF、RDB 和 复制功能 对过期键的处理

当服务器运行在复制模式（主从模式）下时，从服务器的过期键删除动作由主服务器控制。
也就是说主服务器检测到一个键过期时候会进行删除，同时发送给所有从服务器一条删除指令，进行从服务器过期键的删除。
我们知道，主服务收到请求的时候，会先检查键是否过期，如果不过期返回value，如果过期，返回nil。
但从服务器收到请求的时候不会进行检查，只要没有收到主服务器发送过来的删除指令进行删除，即便过期也会返回value。
 
![数据库键空间范例](/image/post/2016/08/20/20160822-0918.jpg)

#### 数据库通知

* 键空间通知（key-space notification）：某个键执行了什么命令
* 键事件通知（key-event notification）：某个命令被什么键执行了


### RDB持久化

#### RDB文件的创建与载入

Redis所有数据都保存在内存中，那么一旦服务器进程退出，Redis数据就消失。
为了避免数据意外丢失，Redis提供了RDB持久化功能。
RDB持久化生成的RDB文件是一个经过压缩的二进制文件，通过RDB文件可以还原到
生成文件时候的数据库状态。

![RDB持久化](/image/post/2016/08/20/20160822-1002.jpg)

持久化命令有两个：  

* SAVE：阻塞式持久化，持久化过程中不能使用Redis其他指令
* BGSAVE：非阻塞式持久化，会新开一个子线程来进行持久化，不影响Redis其他指令

只要有RDB文件，启动Redis的时候就会自动载入，不需要手动调用指令。

#### 自动间隔性保存

默认配置：  

    save 900 1
    save 300 10
    save 60 10000

只要满足900秒修改了一次，300秒修改了10次 或 60秒修改了1000次，那就自动执行一次 BGSAVE 操作。


### AOF持久化

RDB持久化的是数据库状态，也就是将数据库中所有有效数据保存到RDB文件；
而AOF持久化的是操作命令，也就是将对数据新增、修改的指令保存到AOF文件。

* 如果开启了AOF持久化功能，那么服务器会优先使用AOF文件还原数据库状态
* 只有AOF持久化出于关闭状态时，服务器才会使用RDB文件来还原数据库状态

![AOF持久化](/image/post/2016/08/20/20160822-1101.jpg)

#### AOF持久化的实现

* 命令追加（append）  
    appendfsync有三个配置选项：always、everysec、no
* 文件写入
* 文件同步（sync）

#### AOF文件的载入与数据还原
读取AOF文件，将所有命令重新执行一遍

#### AOF重写
如果一条list数据有新增，有修改删除，那么很多条指令之后可能只包含了很少的有效数据，
这时候可以从数据库读取当前有效的数据，用一条新增指令替换掉之前的很多条指令。

### 事件
Redis是一个事件驱动服务器，服务器需要处理以下两类事件:  

* 文件事件（file event）
* 时间时间（time event）

#### 文件事件
Redis服务器通过套接字与客户端连接，而文件事件就是套接字对文件事件的抽象。
服务器与客户端的通信会产生相应的文件事件，而服务器则通过监听并处理这些事件来完成一系列网络通信操作。

![文件事件](/image/post/2016/08/20/20160822-1201.jpg)

#### 时间事件
时间事件分为如下两类：  

* 定时事件
* 周期性事件

### 客户端

#### 客户端属性

* 通用属性
* 特定功能相关属性


### 服务器

#### 命令请求的执行过程

![客户端请求](/image/post/2016/08/20/20160822-1401.jpg)
![服务端回复](/image/post/2016/08/20/20160822-1408.jpg)

#### serverCron函数
Redis服务器中的serverCron函数每隔100毫秒执行一次，这个函数负责管理服务器的资源，
并保持服务器自身的良好运转。

* 更新服务器时间缓存
* 更新LRU时钟
* 更新服务器每秒执行命令次数
* 更新服务器内存峰值记录
* 处理SIGTERM信号
* 管理客户端资源
* 管理数据库资源
* 执行被延迟的BGREWRITEAOF
* 检查持久化操作的运行状态
* 将AOF缓冲区的内容写入到AOF文件
* 关闭异步客户端
* 更新cronloops计数器的值


## 多机数据库的实现

### 复制
![主从结构](/image/post/2016/08/20/20160822-1501.jpg)

#### 旧版功能的实现

* 同步（sync）  
    使用 `SLAVEOF` 指令，将从服务器的状态更新至主服务器当前的数据库状态
* 命令传播（command propagate）  
    主服务器数据被修改时，主服务器将指令发送给从服务器，将主从服务器状态再次同步为一致状态

#### 旧版功能缺陷

* 初次复制：使用同步指令 `sync` 生成RDB文件进行同步，一般来说这个没问题
* 断线后重复制：断线后重连复制的是全部RDB文件，这里是不需要的，新版主要解决这个问题

#### 新版复制功能的实现

* 完整重同步（full resynchronization）：用于处理初次复制的情况，相当于 `SALVEOF` 指令
* 部分重同步（partial resynchronization）：用于处理断线后重复值的情况

#### 部分重同步的实现

* 主服务器的复制偏移量（replication offset）和从服务器的复制偏移量
* 主服务器的复制挤压缓冲区（replication backlog）：存储断线时间段内的指令，默认1M内存空间
* 服务器的运行ID

#### PSYNC命令的实现

![主从结构](/image/post/2016/08/20/20160822-1512.jpg)

### Sentinel
Sentinel（哨岗、哨兵）是Redis高可用性（high availability）解决方案：由一个或多个Sentinel实例（instance）
组成的Sentinel系统（system）可以监视任意多个主服务器，以及这些主服务器属下的所有从服务器，并在被监视的主服务器
进入下线状态时，自动将下线主服务器属下的某个服务器升级为新的主服务器，然后由新的主服务器代替已下线的从服务器继续
处理命令请求。当已下线的主服务器重新上线之后，降级为从服务器继续运行。


### 集群
Redis集群是Redis提供的分布式数据库方案，集群通过分片（sharing）来进行数据共享，并提供复制和故障转移功能。

#### 节点


## 独立功能的实现

### 发布与订阅

### 事务

### Lua脚本

### 排序

### 二进制位数组

### 慢查询日志

### 监视器

## 整理

* 集合键



## 参考资料

* [Redis设计与实现 —— 黄健宏](http://redisbook.com)

{% highlight C %}
{% endhighlight %}


