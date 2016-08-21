---
layout:     post
title:      【redis设计与实现】读书笔记
date:       2016-08-20 00:39:42 +0800
postId:     2016-08-20-00-39-42
categories: [redis]
tags:       [redis, 读书笔记]
geneMenu:   true
excerpt:    【redis设计与实现】读书笔记
---

## 引言

## 数据结构与对象

### 简单动态字符串

#### SDS结构定义
Redis没有直接使用C语言传统的字符串表示，而是自己构建了一种名为简单字符串
（simple dynamic string, SDS）的抽象类型，并将SDS用做Redis的默认字符串表示。

{% highlight java %}
struct sdshdr {
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

#### SDS API
对SDS进行的各种CRUD操作，略

### 链表
C语言并没有实现链表结构，所以Redis构建了自己的链表实现。
链表键、发布与订阅、慢查询、监视器等功能也用到了链表。
Redis服务器还使用链表来保存多个客户端的状态信息，以及使用链表来构建客户端输出缓冲区。

#### 链表和链表节点的实现
链表结构如下：
{% highlight java %}
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

#### 链表和链表节点API
对链表的各种CRUD操作，略

### 字典
字典，又称为符号表（symbol table）、关联数组（associative array）或映射（map），
是一种保存键值对（key-value pair）的抽象数据结构。字典中的 key 唯一，不能重复，比如
Java语言中 HashSet 的内部存储就是利用 HashMap 的 key 来实现无重复 Set 集合。
C语言并没有实现字典结构，所以Redis构建了自己的字典实现。

#### 字典的实现
字典实现结构如下：
{% highlight java %}
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

#### 字典API
对字典的各种CRUD操作，略

### 跳跃表
跳跃表（skipList）是一种有序数据结构，它通过在每个节点中维持多个指向其他节点的指针，从而达到快速访问的目的。
大部分情况下，跳跃表的效率可以和平衡树媲美，但实现要比平衡树简单。Redis中只有两个地方用到了跳跃表，一个是
有序集合键，一个是在集群节点中用作内部数据结构。

#### 跳跃表的实现
{% highlight java %}
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

#### 跳跃表API
略

### 整数集合
整数集合（intset）是集合键的底层实现之一。用于保存整数值的抽象数据结构，它可以保存类型为
int16_t、int32_t 或者 int64_t 的整数值，并且保证集合中不会出现重复元素。

#### 整数集合的实现
{% highlight java %}
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

#### 整数集合API
略

### 压缩列表
压缩列表（ziplist）是为了节约内存空间而开发的，是由一系列特殊编码的连续内存块组成的顺序型数据结构。
当一个列表键只包含少量列表项，并且每个列表项要么就是小整数值，要么就是长度比较短的字符串，
那么Redis就会使用压缩列表来做列表键的底层实现。

* 压缩列表是一种为节约内存而开发的顺序新数据结构
* 压缩列表是列表键和哈希键的底层实现之一。
* 压缩列表可以包含多个节点，每个节点可以保存一个字节数组或者整数值
* 添加新节点到压缩列表，或者从压缩列表中删除节点，可能会引发连锁更新操作，但这种操作出现的几率并不高

#### 压缩列表的构成
{% highlight java %}
typedef struct ziplist {
    uint32_t zlbytes;   // 压缩列表所占用的内存字节数
    uint32_t zltail;    // 压缩列表表尾节点距离压缩列表的起始地址有多少字节
    uint16_t zllen;     // 压缩列表包含的节点数量
    zipNode *entryX;    // 压缩列表节点(注意，这里并不是指向数组的指针，此结构就包含数组本身，分别指向每个节点)
    uint8_t zlend;      // 特殊值0xFF(十进制255)，用于标记压缩列表末端
} ziplist;
typedef struct zipNode {
    previous_entry_length;  // 压缩列表中前一个节点的长度
    encoding;               // content属性所保存的数据的类型和长度
    content;                // content属性负责保存节点的值，可以是一个字节数组或者整数
} zipNode;
{% endhighlight %}

#### 压缩列表API
略

### 对象

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
{% highlight java %}
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
REDIS_STRING    |REDIS_ENCODING_INT 
REDIS_STRING    |REDIS_ENCODING_EMBSTR
REDIS_STRING    |REDIS_ENCODING_RAW
REDIS_LIST      |REDIS_ENCODING_ZIPLIST
REDIS_LIST      |REDIS_ENCODING_LINKEDLIST
REDIS_HASH      |REDIS_ENCODING_ZIPLIST
REDIS_HASH      |REDIS_ENCODING_HT
REDIS_SET       |REDIS_ENCODING_INTSET
REDIS_SET       |REDIS_ENCODING_HT
REDIS_ZSET      |REDIS_ENCODING_ZIPLIST
REDIS_ZSET      |REDIS_ENCODING_SKIPLIST

使用 `OBJECT ENCODING` 命令可以查看一个数据库键的值对象的编码。

## 单机数据库的实现

### 数据库

### RDB持久化

### AOF持久化

### 事件

### 客户端

### 服务器

## 多机数据库的实现

### 复制

### Sentinel

### 集群

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

* [redis设计与实现](http://redisbook.com)

{% highlight java %}
{% endhighlight %}
