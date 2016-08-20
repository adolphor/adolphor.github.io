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

### 数据库对象（Object）

#### 字符串对象（string Object）

#### 列表对象（list Object）

#### 哈希对象（hash Object）

#### 集合对象（set Object）

#### 有序集合对象（sorted set Object）

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
// 
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
    uint32_t encoding;
    uint32_t length;
    int8_t contents[];
} intset;
{% endhighlight %}


#### 整数集合API


### 压缩列表

### 对象

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
