# solr

## 分词器
使用的是IK分词器，其他的还有mmseg4j。

配置：
这需要看配置，字段有两个配置属性：indexed、stored
indexed：缺省为true， 说明这个数据应被搜索和排序
stored:    缺省true，说明这个字段被包含在搜索结果中是合适的
因为要该字段要做检索，所以indexed肯定设置为true，因此“开心”分词后得到的“开”和“心”的索引一定会被存储
若stored为true，则该字段的原始值也会被存储；反之，则不会，导致在检索“开”或“心”时，会返回该索引项，但其字段内容“开心”则不会返回。

## 分布式

概念

* Collection：在SolrCloud集群中逻辑意义上的完整的索引。它常常被划分为一个或多个Shard，它们使用相同的Config Set。如果Shard数超过一个，它就是分布式索引，SolrCloud让你通过Collection名称引用它，而不需要关心分布式检索时需要使用的和Shard相关参数。

* Config Set: Solr Core提供服务必须的一组配置文件。每个config set有一个名字。最小需要包括solrconfig.xml (SolrConfigXml)和schema.xml (SchemaXml)，除此之外，依据这两个文件的配置内容，可能还需要包含其它文件。它存储在Zookeeper中。Config sets可以重新上传或者使用upconfig命令更新，使用Solr的启动参数bootstrap_confdir指定可以初始化或更新它。

* Core: 也就是Solr Core，一个Solr中包含一个或者多个Solr Core，每个Solr Core可以独立提供索引和查询功能，每个Solr Core对应一个索引或者Collection的Shard，Solr Core的提出是为了增加管理灵活性和共用资源。在SolrCloud中有个不同点是它使用的配置是在Zookeeper中的，传统的Solr core的配置文件是在磁盘上的配置目录中。

* Leader: 赢得选举的Shard replicas。每个Shard有多个Replicas，这几个Replicas需要选举来确定一个Leader。选举可以发生在任何时间，但是通常他们仅在某个Solr实例发生故障时才会触发。当索引documents时，SolrCloud会传递它们到此Shard对应的leader，leader再分发它们到全部Shard的replicas。

* Replica: Shard的一个拷贝。每个Replica存在于Solr的一个Core中。一个命名为“test”的collection以numShards=1创建，并且指定replicationFactor设置为2，这会产生2个replicas，也就是对应会有2个Core，每个在不同的机器或者Solr实例。一个会被命名为test_shard1_replica1，另一个命名为test_shard1_replica2。它们中的一个会被选举为Leader。

* Shard: Collection的逻辑分片。每个Shard被化成一个或者多个replicas，通过选举确定哪个是Leader。

* Zookeeper: Zookeeper提供分布式锁功能，对SolrCloud是必须的。它处理Leader选举。Solr可以以内嵌的Zookeeper运行，但是建议用独立的，并且最好有3个以上的主机。

参考：
* http://www.cnblogs.com/flybird2014/p/4093057.html
* http://www.cnblogs.com/rainbowzc/p/3695058.html

# 数据交换

## 数据量和用时
1000条/10秒 = 100条/秒

## 多线程的使用
并未使用，呵呵哒








































