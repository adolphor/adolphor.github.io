---
layout:     post
title:      【转】分布式 ID 服务实践
date:       2023-08-10 14:21:18 +0800
postId:     2023-08-10-14-21-18
categories: [MySQL]
keywords:   [database, MySQL]
---

vivo 自研鲁班分布式 ID 服务实践，文章来源：[vivo 自研鲁班分布式 ID 服务实践](https://mp.weixin.qq.com/s?__biz=MzI4NjY4MTU5Nw==&mid=2247496863&idx=1&sn=5aa18659b98346517ddeda80e42baf2a&chksm=ebdb840ddcac0d1bcc322e464fcdec41f80304190611f06d056d0b4d8a6660bd047f57b70b75&scene=178&cur_album_id=1612326847164284932#rd)

## 一、方案背景

### 1.1 分布式ID应用的场景
随着系统的业务场景复杂化、架构方案的优化演进，我们在克服问题的过程中，也总会延伸出新的技术诉求。分布式ID也是诞生于这样的IT发展过程中，在不同的关联模块内，我们需要一个全局唯一的ID来让模块既能并行地解耦运转，也能轻松地进行整合处理。以下，首先让我们一起回顾这些典型的分布式ID场景。

#### 1.1.1 系统分库分表
随着系统的持续运作，常规的单库单表在支撑更高规模的数量级时，无论是在性能或稳定性上都已经难以为继，需要我们对目标逻辑数据表进行合理的物理拆分，这些同一业务表数据的拆分，需要有一套完整的 ID生成方案来保证拆分后的各物理表中同一业务ID不相冲突，并能在后续的合并分析中可以方便快捷地计算。

以公司的营销系统的订单为例，当前不但以分销与零售的目标组织区别来进行分库存储，来实现多租户的数据隔离，并且会以订单的业务属性（订货单、退货单、调拔单等等）来进一步分拆订单数据。在订单创建的时候，根据这些规则去构造全局唯一ID，创建订单单据并保存在对应的数据库中；在通过订单号查询时，通过ID的规则，快速路由到对应的库表中查询；在BI数仓的统计业务里，又需要汇总这些订单数据进行报表分析。

#### 1.1.2 系统多活部署
无论是面对着全球化的各国数据合规诉求，还是针对容灾高可用的架构设计，我们都会对同一套系统进行多活部署。多活部署架构的各单元化服务，存储的单据（如订单/出入库单/支付单等）均带有部署区域属性的ID结构去构成全局唯一ID，创建单据并保存在对应单元的数据库中，在前端根据单据号查询的场景，通过ID的规则，可快速路由到对应的单元区域进行查询。对应多活部署架构的中心化服务，同步各单元的单据数据时，单据的ID是全局唯一，避免了汇聚数据时的ID冲突。

在公司的系统部署中，公共领域的 BPM 、待办、营销领域的系统都大范围地实施多活部署。

#### 1.1.3 链路跟踪技术
在微服务架构流行的大背景下，此类微服务的应用对比单体应用的调用链路会更长、更复杂，对问题的排查带来了挑战，应对该场景的解决方案，会在流量入口处产生全局唯一的TraceID，并在各微服务之间进行透传，进行流量染色与关联，后续通过该全局唯一的TraceID，可快速地查询与关联全链路的调用关系与状态，快速定位根因问题。

在公司的各式各样的监控系统、灰度管理平台、跨进程链路日志中，都会伴随着这么一个技术组件进行支撑服务。

### 1.2 分布式ID核心的难点
* 唯一性: 保持生成的ID全局唯一，在任何情况下也不会出现重复的值（如防止时间回拔，时钟周期问题）。
* 高性能: ID的需求场景多，中心化生成组件后，需要高并发处理，以接近 0ms的响应大规模并发执行。
* 高可用: 作为ID的生产源头，需要100%可用，当接入的业务系统多的时候，很难调整出各方都可接受的停机发布窗口，只能接受无损发布。
* 易接入: 作为逻辑上简单的分布式ID要推广使用，必须强调开箱即用，容易上手。
* 规律性: 不同业务场景生成的ID有其特征，例如有固定的前后缀，固定的位数，这些都需要配置化管理。

### 1.3 分布式ID常见的方案
常用系统设计中主要有下图9种ID生成的方式：
![image-alter]({{ site.baseurl }}/image/post/2023/08/10/01/img01-generate-method.png)
![image-alter]({{ site.baseurl }}/image/post/2023/08/10/01/img02-generate-method-detail.png)

### 1.4 分布式ID鲁班的方案
我们的系统跨越了公共、生产制造、营销、供应链、财经等多个领域。在分布式ID诉求下还有如下的特点：
* 在业务场景上除了常规的Long类型ID，也需要支持“String类型”、“MixId类型”（后详述）等多种类型的ID生成，每一种类型也需要支持不同的长度的ID。
* 在ID的构成规则上需要涵盖如操作类型、区域、代理等业务属性的标识；需要集中式的配置管理。
* 在一些特定的业务上，基于安全的考虑，还需要在尾部加上随机数来保证ID不能被轻易猜测。

综合参考了业界优秀的开源组件与常用方案均不能满足，为了统一管理这类基础技术组件的诉求，我们选择基于公司业务场景自研一套分布式ID服务：鲁班分布式ID服务。

## 二、系统架构
![image-alter]({{ site.baseurl }}/image/post/2023/08/10/01/img0201-system-structrue.png)

### 2.1 架构说明
![image-alter]({{ site.baseurl }}/image/post/2023/08/10/01/img0202-system-structrue-detail.png)

## 三、 设计要点
### 3.1 支持多种类型的ID规则
目前鲁班分布式ID服务共提供"Long类型"、“String类型”、“MixId类型”等三种主要类型的ID，相关ID构成规则与说明如下：

#### 3.1.1 Long类型

##### 构成规则
静态结构由以下三部分数据组成，组成部分共19位：
* 固定部分（4位）： 由FixPart+ServerPart组成。
  - ① FixPart（4位）：由大区zone 1位/代理 agent 1位/项目 project 1位/应用 app 1位,组成的4位数字编码。
  - ② ServerPart（4位）：用于定义产生全局ID的服务器标识位，服务节点部署时动态分配。
* 动态部分DynPart（13位）： System.currentTimeMillis()-固定配置时间的TimeMillis （可满足使用100年）。
* 自增部分SelfIncreasePart（2位）： 用于在全局ID的客户端SDK内部自增部分，由客户端SDK控制，业务接入方无感知。共 2位组成。

##### 降级机制
主要自增部分在服务器获取初始值后，由客户端SDK维护，直到自增99后再次访问服务端获取下一轮新的ID以减少服务端交互频率，提升性能，服务端获取失败后抛出异常，接入业务侧需介入进行处理。

##### 样例说明
![image-alter]({{ site.baseurl }}/image/post/2023/08/10/01/img0301-long-id.png)

#### 3.1.2 String类型
##### 构成规则
静态结构由以下五部分数据组成，组成部分共25~27位：
* 固定部分操作位op+FixPart（9~11位）：
  - ① 操作位op（2~4位）：2~4位由业务方传入的业务类型标识字符。
  - ② FixPart（7位）：业务接入时申请获取，由大区zone 1位，代理 agent 2位，项目 project 2位，应用 app 2位组成。
* 服务器标识部分 ServerPart（1位）: 用于定义产生全局ID的服务器标识位，服务节点部署时动态分配A~Z编码。
* 动态部分DynPart（9位）： System.currentTimeMillis()-固定配置时间的TimeMillis ，再转换为32进制字符串（可满足使用100年）。
* 自增部分SelfIncreasePart（3位）：用于在全局ID的客户端SDK内部自增部分，由客户端SDK控制，业务接入方无感知。
* 随机部分secureRandomPart（3位）：用于在全局ID的客户端SDK的随机部分，由SecureRandom随机生成3位0-9,A-Z字母数字组合的安全随机数，业务接入方无感知。
##### 降级机制
主要自增部分由客户端SDK内部维护，一般情况下只使用001–999 共999个全局ID。也就是每向服务器请求一次，都在客户端内可以自动维护999个唯一的全局ID。特殊情况下在访问服务器连接出问题的时候，可以使用带字符的自增来做服务器降级处理，使用产生00A, 00B... 0A0, 0A1,0A2....ZZZ. 共有36 * 36 * 36 - 1000 （999纯数字，000不用）= 45656个降级使用的全局ID。

##### 样例说明
![image-alter]({{ site.baseurl }}/image/post/2023/08/10/01/img0302-string-id.png)

#### MixId类型
##### 构成规则
静态结构由以下三部分数据组成，组成部分共17位：
* 固定部分FixPart(4~6位）：
  - ① 操作位op（2~4位）：2~4位由业务方传入的业务类型标识字符
  - ② FixPart（2位）：业务接入时申请获取由代理 agent 2位组成。
* 动态部分DynPart（6位）: 生成ID的时间，年（2位）月（2位）日（2位）。
* 自增部分SelfIncreasePart（7位）：用于在全局ID的客户端SDK内部自增部分，由客户端SDK控制，业务接入方无感知。

##### 降级机制
无，每次ID产生均需到服务端请求获取，服务端获取失败后抛出异常，接入业务侧需介入进行处理。

##### 样例说明
![image-alter]({{ site.baseurl }}/image/post/2023/08/10/01/img0303-mixed-id.png)

### 3.2 业务自定义ID规则实现
鲁班分布式ID服务内置“Long类型”，“String类型”，“MixId类型”等三种长度与规则固定的ID生成算法，除以上三种类型的ID生成算法外，业务侧往往有自定义ID长度与规则的场景诉求，在鲁班分布式ID服务内置ID生成算法未能满足业务场景时，为了能在该场景快速支持业务，鲁班分布式ID服务提供了业务自定义接口并通过SPI机制在服务运行时动态加载，以实现业务自定义ID生成算法场景的支持，相关能力的实现设计与接入流程如下：
* 1）ID的构成部分主要分FixPart、DynPart、SelfIncreasePart三个部分。 
* 2）鲁班分布式ID服务的客户端SDK提供 LuBanGlobalIDClient的接口与getGlobalId（...）/setFixPart(...)/setDynPart(...)/setSelfIncreasePart(...)等四个接口方法。
* 3）业务侧实现LuBanGlobalIDClient接口内的4个方法，通过SPI机制在业务侧服务进行加载，并向外暴露出HTTP或DUBBO协议的接口。
* 4）用户在鲁班分布式ID服务管理后台对自定义ID生成算法的类型名称与服务地址信息进行配置，并关联需要使用的AK接入信息。
* 5）业务侧使用时调用客户端SDK提供的LuBanGlobalIDClient的接口与getGlobalId方法，并传入ID生成算法类型与IdRequest入参对象，鲁班分布式ID服务接收请求后，动态识别与路由到对应ID生产算法的实现服务，并构建对象的ID返回给客户端，完成整个ID生成与获取的过程。

### 3.3 保证ID生成不重复方案
![image-alter]({{ site.baseurl }}/image/post/2023/08/10/01/img0304-none-repeate-id.png)

### 3.4 ID服务无状态无损管理
服务部署的环境在虚拟机上，ip是固定，常规的做法是在配置表里配置ip与机器码的绑定关系（这样在服务扩缩容的时候就需要人为介入操作，存在一定的遗漏配置风险，也带来了一定的运维成本），但在容器的部署场景，因为每次部署时IP均是动态变化的，以前通过配置表里ip与机器码的映射关系的配置实现方式显然不能满足运行在容器场景的诉求，故在服务端设计了通过心跳上报实现机器码动态分配的机制，实现服务端节点ip与机器码动态分配、绑定的能力，达成部署自动化与无损发布的目的。

相关流程如下：
![image-alter]({{ site.baseurl }}/image/post/2023/08/10/01/img0305-stateles.png)

【注意】
> 服务端节点可能因为异常,非正常地退出,对于该场景,这里就需要有一个解绑的过程，当前实现是通过公司平台团队的分布式定时任务服务，检查持续5分钟(可配置)没有上报心跳的机器码分配节点进行数据库绑定信息清理的逻辑,重置相关机器码的位置供后续注册绑定使用。

### 3.5 ID使用方接入SDK设计
SDK设计主要以"接入快捷,使用简单"的原则进行设计。

#### 接入时
鲁班分布式ID服务提供了spring-starter包,应用只需再pom文件依赖该starter，在启动类里添加@EnableGlobalClient，并配置AK/SK等租户参数即可完成接入。

同时鲁班分布式ID服务提供Dubbo & Http的调用方式，通过在启动注解配置accessType为HTTP/DUBBO来确定，SDK自动加载相关依赖。

#### 使用时
根据"Long"、"String"、"MixId"等三种id类型分别提供GlobalIdLongClient、 GlobalIdStringClient、GlobalIdMixIDClient等三个客户端对象，并封装了统一的入参RequestDTO对象，业务系统使用时只需构建对应Id类型的RequestDTO对象（支持链式构建），并调用对应id类型的客户端对象getGlobalID（GlobalBaseRequestDTO globalBaseRequestDTO）方法，即可完成ID的构建。

#### 代码示例
```java
package com.vivo.it.demo.controller;
 
import com.vivo.it.platform.luban.id.client.GlobalIdLongClient;
import com.vivo.it.platform.luban.id.dto.GlobalLongIDRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
 
@RequestMapping("/globalId")
public class GlobalIdDemoController {
 
    @Autowired
    private GlobalIdLongClient globalIdLongClient;
 
    @RequestMapping("/getLongId")
    public String getLongId() {
        GlobalLongIDRequestDTO globalLongIDRequestDTO = GlobalLongIDRequestDTO.Builder()
                .setAgent("1") //代理,接入申请时确定
                .setZone("0") //大区,接入申请时确定
                .setApp("8") //应用,接入申请时确定
                .setProject("7") //项目,接入申请时确定
                .setIdNumber(2); //当次返回的id数量,只对getGlobalIDQueue有效,对getGlobalID(...)无效
        long longId = globalIdLongClient.getGlobalID(globalLongIDRequestDTO);
        return String.valueOf(longId);
    }
}
```

### 3.6 关键运行性能优化场景

#### 3.6.1 内存使用优化
在项目上线初时，经常发生FGC，导致服务停顿，获取ID超时，经过分析，鲁班分布式ID服务的服务端主要为内存敏感的应用，当高并发请求时，过多对象进入老年代从而触发FGC，经过排查主要是JVM内存参数上线时是使用默认的，没有经过优化配置，JVM初始化的内存较少，高并发请求时JVM频繁触发内存重分配，相关的对象也流程老年代导致最终频繁发送FGC。

对于这个场景的优化思路主要是要相关内存对象在年轻代时就快速经过YGC回收，尽量少的对象进行老年代而引起FGC。

基于以上的思路主要做了以下的优化：
* 增大JVM初始化内存（-Xms，容器场景里为-XX:InitialRAMPercentage）
* 增大年轻代内存（-Xmn）
* 优化代码，减少代码里临时对象的复制与创建

#### 3.6.2 锁颗粒度优化
客户端SDK再自增值使用完或一定时间后会向服务端请求新的id生成，这个时候需要保证该次请求在多线程并发时是只请求一次，当前设计是基于用户申请ID的接入配置，组成为key，去获取对应key的对象锁，以减少同步代码块锁的粒度，避免不同接入配置去在并发去远程获取新的id时，锁粒度过大，造成线程的阻塞，从而提升在高并发场景下的性能。

## 四、 业务应用
当前鲁班分布式ID服务日均ID生成量亿级，平均RT在0~1ms内,单节点可支持 万级QPS，已全面应用在公司IT内部营销订单、支付单据、库存单据、履约单据、资产管理编码等多个领域的业务场景。

## 五、未来规划
在可用性方面，当前鲁班分布式ID服务仍对Redis、Mysql等外部DB组件有一定的依赖（如应用接入配置信息、MixId类型自增部分ID计数器），规划在该依赖极端宕机的场景下，鲁班分布式ID服务仍能有一些降级策略，为业务提供可用的服务。

同时基于业务场景的诉求，支持标准形式的雪花算法等ID类型。

## 六、 回顾总结
本文通过对分布式ID的3种应用场景，实现难点以及9种分布式ID的实现方式进行介绍，并对结合vivo业务场景特性下自研的鲁班分布式id服务从系统架构，ID生成规则与部分实现源码进行介绍，希望对本文的阅读者在分布式ID的方案选型或自研提供参考。

## 参考资料
* [【转】分布式 ID 服务实践]({% post_url database/mysql/content/2023-08-10-01-distributed-id-practice %})
* [vivo 自研鲁班分布式 ID 服务实践](https://mp.weixin.qq.com/s?__biz=MzI4NjY4MTU5Nw==&mid=2247496863&idx=1&sn=5aa18659b98346517ddeda80e42baf2a&chksm=ebdb840ddcac0d1bcc322e464fcdec41f80304190611f06d056d0b4d8a6660bd047f57b70b75&scene=178&cur_album_id=1612326847164284932#rd)
