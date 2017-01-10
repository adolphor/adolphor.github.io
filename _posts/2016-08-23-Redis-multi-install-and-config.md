---
layout:     post
title:      Redis源码安装和多服务配置
date:       2016-08-23 15:33:17 +0800
postId:     2016-08-23-15-33-17
categories: [blog]
tags:       [Redis]
geneMenu:   true
excerpt:    Redis源码安装和多服务配置
---

## 源码下载和安装

### 安装
通过查看官网源码下载地址 `http://redis.io/download` 可知，
当前最新版本为3.2.3，[下载地址](http://download.redis.io/releases/redis-3.2.3.tar.gz)，
如果是其他版本，下面的命令替换版本号即可。进入下载目录，依次进行如下操作：

    # 下载
    wget http://download.redis.io/releases/redis-3.2.3.tar.gz
    
    # 解压
    tar -zxvf redis-3.2.3.tar.gz && cd redis-3.2.3
    
    # 编译
    make
    
    # 测试
    make test
    
    # 安装
    make install
    
    # 安装为系统服务，所有设置使用默认的情况，直接回车即可
    cd utils
    sudo ./install_server.sh
    
安装完毕之后可以使用如下指令测试

    redis-cli
    set foo bar 
        # => OK
    get foo
        # => "bar"
    del foo
        # => (integer) 1
    get foo 
        # => (nil)

### 密码配置
默认安装是没有配置密码的，如果想要配置密码，需要修改 `/etc/redis/6379.conf` 文件：

    # 将
    # requirepass foobared
    # 取消注释，修改为
    requirepass adolphor
    
adolphor 替换为你需要的密码，为了方便地使用 `/etc/init.d/redis_6379` 脚本，
在修改密码之后需要填充上面的密码到此脚本：

    # 将
    $CLIEXEC -p $REDISPORT shutdown
    # 修改为 ==>
    $CLIEXEC -a adolphor -p $REDISPORT shutdown

### 外网访问配置
如果需要开放外网访问，需要配置IP绑定为对外开放， `/etc/redis/6379.conf` 文件中：

    # 将只允许本机访问
    bind 127.0.0.1
    # 改为全部开放
    bind 0.0.0.0
    
### 重启

重启使用上面的 `/etc/init.d/redis_6379` 脚本即可：

    cd /etc/init.d/
    ./redis_6379 restart

## 多Redis服务
想要在同一台PC上运行多个Redis服务应该怎么弄呢？
可以再次安装Redis服务，指定不同的端口即可，也就是再次执行

    sudo ./install_server.sh
    
在显示如下信息

    Please select the redis port for this instance: [6379]

的时候，输入不同于已存在得 6379 的端口号，比如 6380 之后回车即可。
如果需要别的配置，参考 6379 服务的配置即可。


## 配置参数详解
TODO

## 参考资料

{% highlight java %}
{% endhighlight %}
