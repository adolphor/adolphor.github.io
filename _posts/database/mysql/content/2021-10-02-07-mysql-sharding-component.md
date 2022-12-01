---
layout:     post
title:      MySQL - 分表分库组件
date:       2021-10-02 23:07:00 +0800
postId:     2021-10-02-23-07-00
categories: [Database]
keywords:   [database, MySQL]
---

当业务数据量达到一定程度，单表数据量过大，操作效率会迅速下滑，这时候就需要考虑分库分表的问题。
针对分库分表，需要梳理清楚如何分库分表、以及使用什么组件实现分库分表。

## 优化手段
* 优化索引
* 读写分离
* 垂直分库
* 水平分表

## 分库分表标准
按照什么逻辑分库？DDD？
单表多大需要分表？

## 分表分库原理
对于中间件解决方案，MyCat技术原理中最重要的一个动词是“拦截”，它拦截了用户发送过来的SQL语句，
首先对SQL语句做了一些特定的分析：如分片分析、路由分析、读写分离分析、缓存分析等，
然后将此SQL发往后端的真实数据库，并将返回的结果做适当的处理，最终再返回给用户。

## 常用组件

### Cobar
Cobar属于中间层方案，在前台应用和实际数据库之间搭建一层Proxy，对前台的开放的接口是MySQL通信协议，
将前台SQL语句变更并按照数据分布规则发到合适的后台数据分库，再合并返回结果，模拟单库下的数据库行为。

Cobar属于阿里B2B事业群，始于2008年，在阿里服役3年多，接管3000+个MySQL数据库的schema，
集群日处理在线SQL请求50亿次以上。

由于Cobar发起人的离职，Cobar停止维护。后续的类似中间件，比如MyCAT建立于Cobar之上，
包括现在阿里服役的RDRS其中也复用了Cobar-Proxy的相关代码。

### MyCAT
MyCAT是社区爱好者在阿里cobar基础上进行二次开发，解决了cobar当时存 在的一些问题，
并且加入了许多新的功能在其中。目前MyCAT社区活跃度很高，

它的后端可以支持MySQL, SQL Server, Oracle, DB2, PostgreSQL等主流数据库，
也支持MongoDB这种新型NoSQL方式的存储，未来还会支持更多类型的存储。

MyCAT是一个强大的数据库中间件，不仅仅可以用作读写分离，以及分表分库、容灾管理，
而且可以用于多租户应用开发、云平台基础设施，让你的架构具备很强的适应性和灵活性，

MyCAT是在Cobar基础上发展的版本，两个显著提高：后端由BIO改为NIO，并发量有大幅提高；
增加了对Order By, Group By, Limit等聚合功能。

### TDDL
TDDL是Tabao根据自己的业务特点开发(Tabao Distributed Data Layer, 外号：头都大了)。
主要解决了分库分表对应用的透明化以及异构数据库之间的数据复制。

TDDL并非独立的中间件，只能算作中间层，处于业务层和JDBC层中间，是以Jar包方式提供给应用调用，
属于JDBC Shard的思想。TDDL复杂度相对较高。当前公布的文档较少，只开源动态数据源，
分表分库部分还未开源，还需要依赖diamond，不推荐使用。

### DRDS
DRDS是阿里巴巴自主研发的分布式数据库服务（此项目不开源），DRDS脱胎于阿里巴巴开源的 Cobar 
分布式数据库引擎，吸收了Cobar核心的Cobar-Proxy源码，实现了一套独立的类似MySQL-Proxy 
协议的解析端，能够对传入的SQL进行解析和处理，对应用程序屏蔽各种复杂的底层DB拓扑结构，
获得单机数据库一样的使用体验，

同时借鉴了淘宝TDDL丰富的分布式数据库实践经验，实现了对分布式Join支持，SUM/MAX/COUNT/AVG 
等聚合函数支持以及排序等函数支持，

通过异构索引、小表广播等解决分布式数据库使用场景下衍生出的一系列问题，
最终形成了完整的分布式数据库方案。

### sharding-JDBC
sharding-JDBC是当当应用框架ddframe中，从关系型数据库模块dd-rdb中分离出来的数据库水平分片框架，
实现透明化数据库分库分表访问。

Sharding-JDBC是继dubbox和elastic-job之后，ddframe系列开源的第3个项目。
Sharding-JDBC直接封装JDBC API，可以理解为增强版的JDBC驱动，旧代码迁移成本几乎为零：
* 可适用于任何基于Java的ORM框架，如JPA、Hibernate、Mybatis、Spring JDBC Template或直接使用JDBC。
* 可基于任何第三方的数据库连接池，如DBCP、C3P0、 BoneCP、Druid等。
* 理论上可支持任意实现JDBC规范的数据库。虽然目前仅支持MySQL，但已有支持Oracle、SQLServer等数据库的计划。

Sharding-JDBC定位为轻量Java框架，使用客户端直连数据库，以jar包形式提供服务，无proxy代理层，无需额外部署，无其他依赖，DBA也无需改变原有的运维方式。
Sharding-JDBC分片策略灵活，可支持等号、between、in等多维度分片，也可支持多分片键。
SQL解析功能完善，支持聚合、分组、排序、limit、or等查询，并支持Binding Table以及笛卡尔积表查询。

### DBProxy
DBProxy是美团点评DBA团队针对公司内部需求，在奇虎360公司开源的Atlas做了很多改进工作，
形成了新的高可靠、高可用企业级数据库中间件。

其特性主要有：读写分离、负载均衡、支持分表、IP过滤、sql语句黑名单、DBA平滑下线DB、
从库流量配置、动态加载配置项。

## 参考资料
* [MySQL - 分表分库组件]({% post_url database/mysql/content/2021-10-02-07-mysql-sharding-component %})
* [数据库分库分表中间件对比（很全）](https://blog.csdn.net/xuheng8600/article/details/80336094)
