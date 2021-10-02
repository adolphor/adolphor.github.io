---
layout:     post
title:      Redis - 数据结构
date:       2021-06-15 09:09:39 +0800
postId:     2021-06-15-09-09-39
categories: [Redis]
keywords:   [database, Redis]
---

## 内部数据结构

### 简单动态字符串

#### SDS结构定义
Redis没有直接使用C语言传统的字符串表示，而是自己构建了一种名为简单字符串
（simple dynamic string, SDS）的抽象类型，并将SDS用做Redis的默认字符串表示。

```c
typedef struct sdshdr {
    int len;    // 已使用长度
    int free;   // 未使用长度
    char buf[]; // 字节数组，用于保存字符串
}
```

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

### 双端链表
C语言并没有实现链表结构，所以Redis构建了自己的链表实现。
链表键、发布与订阅、慢查询、监视器等功能也用到了链表。
Redis服务器还使用链表来保存多个客户端的状态信息，以及使用链表来构建客户端输出缓冲区。

#### 链表和链表节点的实现

双端链表的实现由 listNode 和 list 两个数据结构构成， 下图展示了由这两个结构组成的一个双端链表实例：
![Redis-双端链表]({{ site.baseurl }}/image/post/2021/06/15/Redis-双端链表.svg)

其中， listNode 是双端链表的节点：
```c
typedef struct listNode {
    // 前驱节点
    struct listNode *prev;
    // 后继节点
    struct listNode *next;
    // 值
    void *value;
} listNode;
```

而 list 则是双端链表本身：
```c
typedef struct list {
    listNode *head;     // 表头节点
    listNode *tail;     // 表尾节点
    unsigned long len;  // 链表所包含的节点数量
    void *(*dup) {void *ptr};   // 节点值复制函数
    void (*free) {void *ptr};   // 节点值释放函数
    int (*match) {void *ptr, void *key};    // 节点值对比函数
} list;
```

#### Redis实现的链表的特性
* 双端：节点带有前驱和后继指针，访问前驱节点和后继节点的复杂度为 O(1) 
* 无环
* 带表头指针和表尾指针：对表头和表尾进行处理的复杂度为 O(1)，对链表的迭代可以在从表头到表尾和从表尾到表头两个方向进行
* 带链表长度计数器：有记录节点数量的属性，所以可以在 O(1) 复杂度内返回链表的节点数量（长度）
* 多态

### 字典
字典，又称为符号表（symbol table）、关联数组（associative array）或映射（map），
是一种保存键值对（key-value pair）的抽象数据结构。字典中的 key 唯一，不能重复，比如
Java语言中 HashSet 的内部存储就是利用 HashMap 的 key 来实现无重复 Set 集合。
C语言并没有实现字典结构，所以Redis构建了自己的字典实现。

Redis 的 Hash 类型键使用 **`字典`** 和 **`压缩列表`** 两种数据结构作为底层实现，因为压缩列表比字典更节省内存，
所以程序在创建新 Hash 键时，默认使用压缩列表作为底层实现，当有需要时，程序才会将底层实现从压缩列表转换到字典。

#### 字典的实现
实现字典的方法有很多可选项：
* 最简单的就是使用链表或数组，但是这种方式只适用于元素个数不多的情况下；
* 要兼顾高效和简单性，可以使用哈希表；
* 如果追求更为稳定的性能特征，并希望高效地实现排序操作的话，则可使用更为复杂的平衡树；

在众多可能的实现中， Redis 选择了高效、实现简单的哈希表，作为字典的底层实现。
dict.h/dict 给出了这个字典的定义，字典实现结构如下：
```c
// 字典
typedef struct dict {
    dictType *type; // 类型特定函数
    void *privData; // 私有数据
    dictht ht[2];   // 哈希表（2 个）
    int rehashidx;  // 记录 rehash 进度的标志，值为 -1 表示 rehash 未进行
    int iterators;  // 当前正在运作的安全迭代器数量
}
// 字典类型
typedef struct dictType {
    ......          // 一系列函数
} dictType;
```
需要注意的是，dict中定义的 `ht[2]` 是两个哈希表，一个用于正常存储hash键值对，一个用于触发
rehash的时候使用。

字典所使用的哈希表实现由 dict.h/dictht 类型定义：
```c
// 哈希表
typedef struct dictht {
    dictEntry **table;      // 哈希表数组（俗称桶，bucket）
    unsigned long size;     // 哈希表大小
    unsigned long sizemask; // 哈希表大小掩码，用于计算索引值，总是等于 size-1
    unsigned long used;     // 该哈希表已有节点的数量
} dictht;
```
table 属性是个数组，数组的每个元素都是个指向 dictEntry 结构的指针。
每个 dictEntry 都保存着一个键值对， 以及一个指向另一个 dictEntry 结构的指针：
```c
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
```
next 属性指向另一个 dictEntry 结构， 多个 dictEntry 可以通过 next 指针串连成链表， 
从这里可以看出， dictht 使用链地址法来处理键碰撞： 当多个不同的键拥有相同的哈希值时，
哈希表用一个链表将这些键连接起来。

下图展示了一个由 dictht 和数个 dictEntry 组成的哈希表例子：
![Redis-字典主体数据结构]({{ site.baseurl }}/image/post/2021/06/15/Redis-字典主体数据结构.svg)

如果再加上之前列出的 dict 类型，那么整个字典结构可以表示如下：
![Redis-字典完整数据结构]({{ site.baseurl }}/image/post/2021/06/15/Redis-字典完整数据结构.svg)

在上图的字典示例中， 字典虽然创建了两个哈希表， 但正在使用的只有 0 号哈希表， 这说明字典未进行 rehash 状态。

#### 哈希算法
Redis 目前使用两种不同的哈希算法：
* MurmurHash2 32 bit 算法：这种算法的分布率和速度都非常好，具体信息请参考 [MurmurHash](http://code.google.com/p/smhasher/) 的主页。
* 基于 djb 算法实现的一个大小写无关散列算法：具体信息请参考 [djb-hash](http://www.cse.yorku.ca/~oz/hash.html)。

使用哪种算法取决于具体应用所处理的数据：
* 算法 1 的应用更加广泛：数据库、集群、哈希键、阻塞操作等功能都用到了这个算法。
* 命令表以及 Lua 脚本缓存都用到了算法 2 。

#### 触发 rehash 操作
使用链地址法可以存储多于数组大小的节点数量，但如果链节点过长，则效率会变得低下。 
为了在字典的键值对不断增多的情况下保持良好的性能， 字典需要对所使用的哈希表（ht[0]）
进行 rehash 操作：在不修改任何键值对的情况下，对哈希表进行扩容，尽量将比率维持在 1:1 左右。

dictAdd 在每次向字典添加新键值对之前， 都会对哈希表 ht[0] 进行检查， 对于 ht[0] 的 
size 和 used 属性， 如果它们之间的比率 ratio = used / size 满足以下任何一个条件的话，
rehash 过程就会被激活：
* 自然 rehash ： ratio >= 1 ，且变量 dict_can_resize 为真。
* 强制 rehash ： ratio 大于变量 dict_force_resize_ratio （目前版本中，dict_force_resize_ratio 的值为 5 ）。

> 什么时候 dict_can_resize 会为假？

在前面介绍字典的应用时也说到过， 数据库就是字典， 数据库里的哈希类型键也是字典， 当 Redis 
使用子进程对数据库执行后台持久化任务时（比如执行 BGSAVE 或 BGREWRITEAOF 时）， 
为了最大化地利用系统的 copy on write 机制， 程序会暂时将 dict_can_resize 设为假， 
避免执行自然 rehash ， 从而减少程序对内存的触碰（touch）。
当持久化任务完成之后， dict_can_resize 会重新被设为真。
另一方面， 当字典满足了强制 rehash 的条件时， 即使 dict_can_resize 不为真
（有 BGSAVE 或 BGREWRITEAOF 正在执行）， 这个字典一样会被 rehash 。

#### Rehash 执行过程
字典的 rehash 操作实际上就是执行以下任务：
* 创建一个比 ht[0]->table 更大的 ht[1]->table ；
* 将 ht[0]->table 中的所有键值对迁移到 ht[1]->table ；
* 将原有 ht[0] 的数据清空，并将 ht[1] 替换为新的 ht[0] ；

需要特别指出的是， rehash 程序并不是在激活之后，就马上执行直到完成的， 
而是分多次、渐进式地完成的。因为对于用户或者服务器来说，如果阻塞等待rehash完成，
是相当不友好的。

为了解决这个问题， Redis **`使用了渐进式（incremental）`** 的 rehash 方式： 
通过将 rehash 分散到多个步骤中进行， 从而避免了集中式的计算。

渐进式 rehash 主要由 _dictRehashStep 和 dictRehashMilliseconds 两个函数进行：
* _**`dictRehashStep`**：用于对数据库字典、以及哈希键的字典进行被动 rehash ；
* **`dictRehashMilliseconds`**：则由 Redis 服务器常规任务程序（server cron job）执行，用于对数据库字典进行主动 rehash ；

每次执行 _**`dictRehashStep`** ，ht[0]->table 哈希表第一个不为空的索引上的所有节点就会全部迁移到 ht[1]->table 。
在 rehash 开始进行之后（d->rehashidx 不为 -1）， 每次执行一次添加、查找、删除操作， _dictRehashStep 都会被执行一次：

![Redis-渐进式hash]({{ site.baseurl }}/image/post/2021/06/15/Redis-渐进式hash.svg)

其他措施：
在哈希表进行 rehash 时， 字典还会采取一些特别的措施， 确保 rehash 顺利、正确地进行：
* 因为在 rehash 时，字典会同时使用两个哈希表，所以在这期间的所有查找、删除等操作，除了在 ht[0] 上进行，还需要在 ht[1] 上进行。
* 在执行添加操作时，新的节点会直接添加到 ht[1] 而不是 ht[0] ，这样保证 ht[0] 的节点数量在整个 rehash 过程中都只减不增。

### 跳跃表
跳跃表（skipList）是一种有序数据结构，它通过在每个节点中维持多个指向其他节点的指针，从而达到快速访问的目的。
大部分情况下，跳跃表的效率可以和平衡树媲美，但实现要比平衡树简单。Redis中只有两个地方用到了跳跃表，一个是
有序集合键，一个是在集群节点中用作内部数据结构。

和字典、链表或者字符串这几种在 Redis 中大量使用的数据结构不同， 跳跃表在 Redis 的唯一作用， 就是实现有序集数据类型。
跳跃表将指向有序集的 score 值和 member 域的指针作为元素， 并以 score 值为索引， 对有序集元素进行排序。

#### 跳跃表的实现
```c
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
```
在底层实现中， Redis 为 x 、 y 和 z 三个 member 分别创建了三个字符串， 值分别为 double 
类型的 6 、 10 和 15 ， 然后用跳跃表将这些指针有序地保存起来， 形成这样一个跳跃表：

![Redis-跳表]({{ site.baseurl }}/image/post/2021/06/15/Redis-跳表.svg)

为了方便展示， 在图片中我们直接将 member 和 score 值包含在表节点中， 但是在实际的定义中， 
因为跳跃表要和另一个实现有序集的结构（字典）分享 member 和 score 值， 所以跳跃表只保存指向 
member 和 score 的指针。 更详细的信息，请参考《有序集》章节。

## 内存映射数据结构
虽然内部数据结构非常强大， 但是创建一系列完整的数据结构本身也是一件相当耗费内存的工作， 
当一个对象包含的元素数量并不多， 或者元素本身的体积并不大时， 使用代价高昂的内部数据结构并不是最好的办法。

为了解决这一问题， Redis 在条件允许的情况下， 会使用内存映射数据结构来代替内部数据结构。

### 整数集合
整数集合（intset）用于有序、无重复地保存多个整数值， 根据元素的值， 自动选择该用什么长度的整数类型来保存元素。

整数集合（intset）是集合键的底层实现之一。用于保存整数值的抽象数据结构，它可以保存类型为
int16_t、int32_t 或者 int64_t 的整数值，并且保证集合中不会出现重复元素。

#### 整数集合的实现
```c
typedef struct intset {
    uint32_t encoding;  // 编码方式
    uint32_t length;    // 集合包含的元素数量
    int8_t contents[];  // 保存元素的数组
} intset;
```
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
```c
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
```

## Redis 数据类型

### 对象处理机制

* Redis数据库中的每个键值对的键和值都是一个对象
* Redis是基于上面的数据结构创造的一个对象系统，包含如下五种对象：
    * 字符串对象（string Object）
    * 列表对象（list Object）
    * 哈希对象（hash Object）
    * 集合对象（set Object）
    * 有序集合对象（sorted set Object）
* 服务器在执行某些命令之前，会向检查给定键的类型能否执行指定的命令，而检查一个键的类型就是检查键的值的对象的类型
* Redis的对下给你系统带有应用计数实现的内存回收机制
* Redis会共享值为 0 到 9999 的字符串对象
* 对象会记录自己的最后一次被访问的时间，这个时间可以用于计算对象的空转时间

![Redis-对象处理机制]({{ site.baseurl }}/image/post/2021/06/15/Redis-对象处理机制.svg)

Redis中的每个对象都由一个redisObject结构表示，该结构中和保存数据有关的三个属性分别是
type属性、encoding属性和ptr属性：
```c
typedef struct redisObject {
    unsigned type:4;    // 类型
    unsigned encoding:4;// 编码
    void *ptr;          // 指向底层实现数据结构的指针
} redisObject;
```

有了 redisObject 结构的存在， 在执行处理数据类型的命令时， 进行类型检查和对编码进行多态操作就简单得多了。

当执行一个处理数据类型的命令时， Redis 执行以下步骤：
* 根据给定 key ，在数据库字典中查找和它相对应的 redisObject ，如果没找到，就返回 NULL 。
* 检查 redisObject 的 type 属性和执行命令所需的类型是否相符，如果不相符，返回类型错误。
* 根据 redisObject 的 encoding 属性所指定的编码，选择合适的操作函数来处理底层的数据结构。
* 返回数据结构的操作结果作为命令的返回值。

#### 类型
对象的type属性记录了对象的类型，对于Redis来说，键总是一个字符串对象，
而值可以是字符串对象、列表对象、哈希对象、集合对象或者有序集合对象的其中一种。

类型常量         | 对象名称
---             | ---
REDIS_STRING    | 字符串对象
REDIS_LIST      | 列表对象
REDIS_HASH      | 哈希对象
REDIS_SET       | 集合对象
REDIS_ZSET      | 有序集合对象

#### 编码方式（底层实现）和多态

对象的ptr指针指向对象的底层实现数据结构，而这些数据结构由对象的encoding属性决定。
encoding属性记录了对象所使用的编码，也就是说这个对象使用了声明数据结构作为对象的底层实现。

### 字符串
REDIS_STRING （字符串）是 Redis 使用得最为广泛的数据类型， 它除了是 SET 、 GET 
等命令的操作对象之外， 数据库中的所有键， 以及执行命令时提供给 Redis 的参数， 都是用这种类型保存的。

#### 编码

字符串类型分别使用 REDIS_ENCODING_INT 和 REDIS_ENCODING_RAW 两种编码：
* REDIS_ENCODING_INT 使用 long 类型来保存 long 类型值。
* REDIS_ENCODING_RAW 则使用 sdshdr 结构来保存 sds （也即是 char* )、 long long 、 double 和 long double 类型值。

![Redis-字符串]({{ site.baseurl }}/image/post/2021/06/15/Redis-字符串.svg)

#### 编码的选择
新创建的字符串默认使用 REDIS_ENCODING_RAW 编码， 在将字符串作为键或者值保存进数据库时， 
程序会尝试将字符串转为 REDIS_ENCODING_INT 编码。

### 哈希表

![Redis-哈希表]({{ site.baseurl }}/image/post/2021/06/15/Redis-哈希表.svg)

#### 编码
* 哈希表：将哈希表的键（key）保存为字典的键， 将哈希表的值（value）保存为字典的值
![Redis-哈希表-字典编码]({{ site.baseurl }}/image/post/2021/06/15/Redis-哈希表-字典编码.svg)

* 压缩列表：将键和值一同推入压缩列表， 从而形成保存哈希表所需的键-值对结构
```
+---------+------+------+------+------+------+------+------+------+---------+
| ZIPLIST |      |      |      |      |      |      |      |      | ZIPLIST |
| ENTRY   | key1 | val1 | key2 | val2 | ...  | ...  | keyN | valN | ENTRY   |
| HEAD    |      |      |      |      |      |      |      |      | END     |
+---------+------+------+------+------+------+------+------+------+---------+
```

#### 编码的选择
创建空白哈希表时， 程序默认使用 REDIS_ENCODING_ZIPLIST 编码， 当以下任何一个条件被满足时，
程序将编码从 REDIS_ENCODING_ZIPLIST 切换为 REDIS_ENCODING_HT ：
* 哈希表中某个键或某个值的长度大于 server.hash_max_ziplist_value （默认值为 64 ）。
* 压缩列表中的节点数量大于 server.hash_max_ziplist_entries （默认值为 512 ）。

### 列表
![Redis-列表]({{ site.baseurl }}/image/post/2021/06/15/Redis-列表.svg)

#### 编码
* 压缩列表
* 双端列表

#### 编码选择
创建新列表时 Redis 默认使用 REDIS_ENCODING_ZIPLIST 编码， 当以下任意一个条件被满足时，
列表会被转换成 REDIS_ENCODING_LINKEDLIST 编码：
* 试图往列表新添加一个字符串值，且这个字符串的长度超过 server.list_max_ziplist_value （默认值为 64 ）。
* ziplist 包含的节点超过 server.list_max_ziplist_entries （默认值为 512 ）。

#### 阻塞命令
**`BLPOP`** 、 **`BRPOP`** 和 **`BRPOPLPUSH`** 这几个都是阻塞命令，可能造成客户端被阻塞，
将这些命令统称为列表的阻塞原语，但阻塞原语并不是一定会造成客户端阻塞：
* 只有当这些命令被用于空列表时， 它们才会阻塞客户端。
* 如果被处理的列表不为空的话， 它们就执行无阻塞版本的 LPOP 、 RPOP 或 RPOPLPUSH 命令。

作为例子，以下流程图展示了 BLPOP 决定是否对客户端进行阻塞过程：
![Redis-列表-阻塞原语]({{ site.baseurl }}/image/post/2021/06/15/Redis-列表-阻塞原语.svg)

当客户端被阻塞之后，脱离阻塞状态有以下三种方法：
* 被动脱离：有其他客户端为造成阻塞的键推入了新元素。
* 主动脱离：到达执行阻塞原语时设定的最大阻塞时间。
* 强制脱离：客户端强制终止和服务器的连接，或者服务器停机。

### 集合
REDIS_SET （集合）是 SADD 、 SRANDMEMBER 等命令的操作对象，
它使用 REDIS_ENCODING_INTSET 和 REDIS_ENCODING_HT 两种方式编码：
![Redis-集合]({{ site.baseurl }}/image/post/2021/06/15/Redis-集合.svg)

#### 编码
* 压缩列表
* 哈希表

#### 编码的选择
第一个添加到集合的元素， 决定了创建集合时所使用的编码：
* 如果第一个元素可以表示为 long long 类型值（也即是，它是一个整数）， 那么集合的初始编码为 REDIS_ENCODING_INTSET 。
* 否则，集合的初始编码为 REDIS_ENCODING_HT 。

如果一个集合使用 REDIS_ENCODING_INTSET 编码， 那么当以下任何一个条件被满足时，
这个集合会被转换成 REDIS_ENCODING_HT 编码：
* 试图往集合里添加一个新元素，并且这个元素不能被表示为 long long 类型（也即是，它不是一个整数）。
* intset 保存的整数值个数超过 server.set_max_intset_entries （默认值为 512 ）。

### 有序集
REDIS_ZSET （有序集）是 ZADD 、 ZCOUNT 等命令的操作对象， 它使用 REDIS_ENCODING_ZIPLIST 
和 REDIS_ENCODING_SKIPLIST 两种方式编码：
![Redis-有序集]({{ site.baseurl }}/image/post/2021/06/15/Redis-有序集.svg)

#### 编码
* 压缩列表
```
          |<--  element 1 -->|<--  element 2 -->|<--   .......   -->|

+---------+---------+--------+---------+--------+---------+---------+---------+
| ZIPLIST |         |        |         |        |         |         | ZIPLIST |
| ENTRY   | member1 | score1 | member2 | score2 |   ...   |   ...   | ENTRY   |
| HEAD    |         |        |         |        |         |         | END     |
+---------+---------+--------+---------+--------+---------+---------+---------+

score1 <= score2 <= ...
```

* 跳表
zset 同时使用字典和跳跃表两个数据结构来保存有序集元素。
其中， 元素的成员由一个 redisObject 结构表示， 而元素的 score 则是一个 double 类型的浮点数，
* 字典和跳跃表两个结构通过将指针共同指向这两个值来节约空间 （不用每个元素都复制两份）。
![Redis-有序集-跳表]({{ site.baseurl }}/image/post/2021/06/15/Redis-有序集-跳表.svg)

#### 编码的选择
在通过 ZADD 命令添加第一个元素到空 key 时， 程序通过检查输入的第一个元素来决定该创建什么编码的有序集。
如果第一个元素符合以下条件的话， 就创建一个 REDIS_ENCODING_ZIPLIST 编码的有序集：
* 服务器属性 server.zset_max_ziplist_entries 的值大于 0 （默认为 128 ）。
* 元素的 member 长度小于服务器属性 server.zset_max_ziplist_value 的值（默认为 64 ）。
否则，程序就创建一个 REDIS_ENCODING_SKIPLIST 编码的有序集。

对于一个 REDIS_ENCODING_ZIPLIST 编码的有序集， 只要满足以下任一条件， 就将它转换为 REDIS_ENCODING_SKIPLIST 编码：
* 新添加元素的 member 的长度大于服务器属性 server.zset_max_ziplist_value 的值（默认值为 64 ）
* ziplist 所保存的元素数量超过服务器属性 server.zset_max_ziplist_entries 的值（默认值为 128 ）

### 参考资料
* [Redis - 数据结构]({% post_url database/redis/content/2021-06-15-redis-data-structure %})
* [Redis 设计与实现（第一版）](https://redisbook.readthedocs.io/en/latest/index.html)
* [Redis设计与实现 —— 黄健宏](http://redisbook.com)
* [最详细的Redis五种数据结构详解](https://zhuanlan.zhihu.com/p/148562122)
