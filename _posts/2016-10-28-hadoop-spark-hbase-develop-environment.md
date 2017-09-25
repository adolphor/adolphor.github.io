---
layout:     post
title:      Hadoop，Spark，HBase 开发环境配置
date:       2016-10-28 15:00:01 +0800
postId:     2016-10-28-15-00-01
categories: [blog]
tags:       [Hadoop, Spark, HBase]
geneMenu:   true
excerpt:    Hadoop，Spark，HBase 开发环境配置
---


## 准备工作

### 软件版本
* JDK：jdk1.8.0_102
* scala：2.11.8
* spark：spark-2.0.1-bin-hadoop2.6
* hbase：1.0.3
* hadoop：2.6.0
* ubuntu：16.04.1

### 工作目录
原则是所有软件安装在当前用户的 `workspace` 目录：
各个软件直接安装在此目录，pid 一般设置为当前安装软件目录下的 `pid` 文件夹；
日志文件放在子目录 `logs` 下，再分别划分 Hadoop，HBase 等目录；
数据文件放在子目录 `data` 下，再分别划分 Hadoop，HBase 等目录；
如下所示：

* 用户名称：adolphor
* 安装目录：/home/adolphor/workspace
* 日志目录：/home/adolphor/workspace/logs
* 数据目录：/home/adolphor/workspace/data

## 免密登陆

配置免密登陆可以在使用shell登陆远程服务器的时候免去输入用户名和密码的步骤。

### 生成公匙和密匙

```shell
# 安装 openssh
$ sudo apt-get install openssh-server
# 生成密匙对
$ ssh-keygen -t rsa
# 将公钥加入被认证的公钥文件
$ cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
```

###  验证

```shell
$ ssh localhost
```

如果显示如下内容，表示配置成功：

```
Welcome to Ubuntu 16.04.1 LTS (GNU/Linux 4.4.0-31-generic x86_64)

 * Documentation:  https://help.ubuntu.com
 * Management:     https://landscape.canonical.com
 * Support:        https://ubuntu.com/advantage

202 packages can be updated.
77 updates are security updates.

The programs included with the Ubuntu system are free software;
the exact distribution terms for each program are described in the
individual files in /usr/share/doc/*/copyright.

Ubuntu comes with ABSOLUTELY NO WARRANTY, to the extent permitted by
applicable law.
```

## 安装hadoop

### 安装路径

```
/home/adolphor/workspace/hadoop-2.6.0
```

### hadoop-env.sh

```shell
# 将
export JAVA_HOME=${JAVA_HOME}
export HADOOP_LOG_DIR=${HADOOP_LOG_DIR}
export HADOOP_SECURE_DN_LOG_DIR=${HADOOP_LOG_DIR}
export HADOOP_PID_DIR=${HADOOP_PID_DIR}
export HADOOP_SECURE_DN_PID_DIR=${HADOOP_PID_DIR}
# 分别改为
export JAVA_HOME=/home/adolphor/workspace/jdk1.8.0_102
export HADOOP_LOG_DIR=/home/adolphor/workspace/logs/hadoop
export HADOOP_SECURE_DN_LOG_DIR=/home/adolphor/workspace/logs/hadoop
export HADOOP_PID_DIR=/home/adolphor/workspace/hadoop-2.6.0/pid
export HADOOP_SECURE_DN_PID_DIR=/home/adolphor/workspace/hadoop-2.6.0/pid
# 新增
export HADOOP_PREFIX=/home/adolphor/workspace/hadoop-2.6.0
```


### yarn-env.sh

```shell
# 新增
export JAVA_HOME=/home/adolphor/workspace/jdk1.8.0_102
export YARN_LOG_DIR=/home/adolphor/workspace/logs/yarn
```

### core-site.xml

默认配置文件路径：

```
\share\doc\hadoop\hadoop-project-dist\hadoop-common\core-default.xml
```

自定义配置：

```xml
<property>
  <name>fs.defaultFS</name>
  <value>hdfs://localhost:9000</value>
</property>
```

### hdfs-site.xml

默认配置文件路径：

```
\share\doc\hadoop\hadoop-project-dist\hadoop-hdfs\hdfs-default.xml
```

自定义配置：
```xml
<property>
  <name>dfs.replication</name>
  <value>1</value>
  <description>默认是3，改为1</description>
</property>
<property>
  <name>dfs.namenode.name.dir</name>
  <value>file:///home/adolphor/workspace/data/hadoop/dfs/name</value>
  <description>默认是${hadoop.tmp.dir}/dfs/name</description>
</property>
<property>
  <name>dfs.datanode.data.dir</name>
  <value>file:///home/adolphor/workspace/data/hadoop/dfs/data</value>
  <description>默认是${hadoop.tmp.dir}/dfs/data</description>
</property>
<property>
  <name>dfs.namenode.checkpoint.dir</name>
  <value>file:///home/adolphor/workspace/data/hadoop/dfs/namesecondary</value>
  <description>默认是${hadoop.tmp.dir}/dfs/namesecondary</description>
</property>
```

### mapred-site.xml

默认配置文件路径：

```
\share\doc\hadoop\hadoop-mapreduce-client\hadoop-mapreduce-client-core\mapred-default.xml
```

从`mapred-site.xml.template`复制并更名为`mapred-site.xml`，自定义配置：
```xml
<property>
  <name>mapreduce.framework.name</name>
  <value>yarn</value>
  <description>默认local，可选：local, classic 或 yarn</description>
</property>
```

### yarn-site.xml

默认配置文件路径：

```
\share\doc\hadoop\hadoop-yarn\hadoop-yarn-common\yarn-default.xml
```

自定义配置：
```xml
<property>
  <name>yarn.resourcemanager.hostname</name>
  <value>localhost</value>
  <description>默认：0.0.0.0</description>
</property>    
<property>
  <name>yarn.nodemanager.aux-services</name>
  <value>mapreduce_shuffle</value>
  <description>NodeManager上运行的附属服务。需配置成mapreduce_shuffle，才可运行MapReduce程序</description>
</property>
```

### 初始化

初始化指令：

```shell
$ cd /home/adolphor/workspace/hadoop-2.6.0
$ ./bin/hdfs namenode -format
```

显示如果结果表示初始化成功：

```
………………

16/10/27 21:51:27 INFO util.GSet: 0.029999999329447746% max memory 889 MB = 273.1 KB
16/10/27 21:51:27 INFO util.GSet: capacity      = 2^15 = 32768 entries
16/10/27 21:51:27 INFO namenode.NNConf: ACLs enabled? false
16/10/27 21:51:27 INFO namenode.NNConf: XAttrs enabled? true
16/10/27 21:51:27 INFO namenode.NNConf: Maximum size of an xattr: 16384
16/10/27 21:51:27 INFO namenode.FSImage: Allocated new BlockPoolId: BP-384695647-127.0.1.1-1477630287326
16/10/27 21:51:27 INFO common.Storage: Storage directory /home/adolphor/workspace/data/hadoop/dfs/name has been successfully formatted.
16/10/27 21:51:27 INFO namenode.NNStorageRetentionManager: Going to retain 1 images with txid >= 0
16/10/27 21:51:27 INFO util.ExitUtil: Exiting with status 0
16/10/27 21:51:27 INFO namenode.NameNode: SHUTDOWN_MSG: 
/************************************************************
SHUTDOWN_MSG: Shutting down NameNode at ubuntu/127.0.1.1
************************************************************/
```

启动指令：

```shell
#启动HDFS
$ ./sbin/start-dfs.sh
#启动资源管理器
$ ./sbin/start-yarn.sh
```

启动日志：

```
Starting namenodes on [localhost]
localhost: starting namenode, logging to /home/adolphor/workspace/logs/hadoop/adolphor/hadoop-adolphor-namenode-ubuntu.out
localhost: starting datanode, logging to /home/adolphor/workspace/logs/hadoop/adolphor/hadoop-adolphor-datanode-ubuntu.out
Starting secondary namenodes [0.0.0.0]
0.0.0.0: starting secondarynamenode, logging to /home/adolphor/workspace/logs/hadoop/adolphor/hadoop-adolphor-secondarynamenode-ubuntu.out

starting yarn daemons
starting resourcemanager, logging to /home/adolphor/workspace/logs/yarn/yarn-adolphor-resourcemanager-ubuntu.out
localhost: starting nodemanager, logging to /home/adolphor/workspace/logs/yarn/yarn-adolphor-nodemanager-ubuntu.out
```

查看启动结果：

```shell
$ jps

16643 SecondaryNameNode
16339 NameNode
16791 ResourceManager
16903 NodeManager
16938 Jps
16476 DataNode
```

页面管理地址：
http://localhost:50070

## 安装Spark

### spark-env.sh
从 `spark-env.sh.template` 复制并更名为 `spark-env.sh` ，
```shell
export JAVA_HOME=/home/adolphor/workspace/jdk1.8.0_102
export SCALA_HOME=/home/adolphor/workspace/scala-2.11.8
export HADOOP_HOME=/home/adolphor/workspace/hadoop-2.6.0
export SPARK_HOME=/home/adolphor/workspace/spark-2.0.1-bin-hadoop2.6
export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
export SPARK_CONF_DIR=$SPARK_HOME/conf
export SPARK_PID_DIR=$SPARK_HOME/pid
export SPARK_LOCAL_DIRS=/home/adolphor/workspace/data/spark/local
export SPARK_WORKER_DIR=/home/adolphor/workspace/data/spark/work
export SPARK_LOG_DIR=/home/adolphor/workspace/logs/spark
```

### 其它配置

从 `log4j.properties.template` 复制并更名为 `log4j.properties` ，
从 `slaves.template` 复制并更名为 `slaves` ，


### 启动

```shell
$ cd /home/adolphor/workspace/spark-2.0.1-bin-hadoop2.6
$ ./sbin/start-all.sh
```

启动日志：
```
starting master, logging to /home/adolphor/workspace/logs/hbase/hbase-adolphor-master-ubuntu.out
```

页面管理地址：
http://localhost:8080

## 安装HBase
作为单机模式

### hbase-env.sh

```shell
export JAVA_HOME=/home/adolphor/workspace/jdk1.8.0_102
export HBASE_PID_DIR=/home/adolphor/workspace/hbase-1.0.3/pids
export HBASE_LOG_DIR=/home/adolphor/workspace/logs/hbase
export HBASE_MANAGES_ZK=true
HBASE_ROOT_LOGGER=INFO,DRFA
```


### hbase-site.xml
```xml
<property>
  <name>hbase.rootdir</name>
  <value>file:///home/adolphor/workspace/hbase-1.0.3</value>
</property>
<property>
  <name>hbase.tmp.dir</name>
  <value>/home/adolphor/workspace/data/hbase/tmp</value>
</property>
```

### 启动

```shell
$ cd /home/adolphor/workspace/hbase-1.0.3
$ ./bin/start-hbase.sh
```


启动日志：

```
starting master, logging to /home/adolphor/workspace/logs/hbase/hbase-adolphor-master-ubuntu.out
```

查看进程：

```shell
$ jps

18072 HMaster
18171 Jps
```

### 测试


## shell脚本

方便启动和关闭：

```shell
#! /bin/bash

HADOOP_HOME=/home/adolphor/workspace/hadoop-2.6.0/
SPARK_HOME=/home/adolphor/workspace/spark-2.0.1-bin-hadoop2.6/
HBASE_HOME=/home/adolphor/workspace/hbase-1.0.3/

start(){
  echo "start Hadoop:"
  cd $HADOOP_HOME
  ./sbin/start-dfs.sh
  ./sbin/start-yarn.sh

  echo "start Spark:"
  cd $SPARK_HOME
  ./sbin/start-all.sh

  echo "start HBase:"
  cd $HBASE_HOME
  ./bin/start-hbase.sh
}

stop(){
  echo "stop Hadoop:"
  cd $HADOOP_HOME
  ./sbin/stop-dfs.sh
  ./sbin/stop-yarn.sh

  echo "stop Spark:"
  cd $SPARK_HOME
  ./sbin/stop-all.sh

  echo "stop HBase:"
  cd $HBASE_HOME
  ./bin/stop-hbase.sh
}

restart(){
  stop
  start
}

echo "\n\n可选操作:\n"
echo "1) STOP"
echo "2) START"
echo "3) RESTART"
echo "\n"

read -p '输入对应数字并回车：' actionNum

case "$actionNum" in
  '1')
     stop
     ;;
  '2')
     start
     ;;
  '3')
     restart
     ;;
  *)
     echo "输入错误，请输入操作对应数字"
esac

echo "============================ successed ============================"
exit 0

```

## 参考文档
* Hadoop docs：
  * hadoop-2.6.0/share/doc/hadoop/index.html
* HBase docs：
  * hbase-1.0.3/docs/index.html
  * hbase-1.0.3/docs/book.html

