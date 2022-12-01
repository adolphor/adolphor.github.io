---
layout:     post
title:      nginx的健康检查机制&灾备使用
date:       2022-05-20 11:46:48 +0800
postId:     2022-05-20-11-46-49
categories: [Nginx]
keywords:   [Nginx]
---

当一个域名服务下有3个实例节点的时候，可以启动2个节点作为常规服务节点，另外一个节点作为备用服务
节点，这种策略有很多实际用途。比如，两个内网专线的服务节点作为常规服务节点，但是当专线出现问题的
时候，可以自动切换到备用公网节点。这样虽然公网访问可以能会慢一点，但至少能够保障服务可用。

## nginx 被动健康检查
直接使用ngx_http_upstream_module模块自带的被动式的健康检查。

### nginx 配置

> nginx-backup-8088.conf

```nginx
upstream tree {
    server 127.0.0.1:8901 max_fails=3 fail_timeout=10s; 
    server 127.0.0.1:8902 max_fails=3 fail_timeout=10s;
    server 127.0.0.1:8903 max_fails=3 fail_timeout=10s backup;
}

server {
    listen 8088 default;
    server_name localhost;
    location /tree {
        proxy_pass http://tree;
        proxy_connect_timeout    1s;
        proxy_read_timeout    5s;
        proxy_send_timeout    5s;
        proxy_next_upstream    error timeout;
        access_log  /Users/adolphor/logs/nginx/backup-test.log  main;
    }
}
```

### 测试服务
本地启动三个服务节点，因为使用同一个电脑启动，所以我们使用不同的端口进行测试：
* http://127.0.0.1:8901/tree/test/nginx
* http://127.0.0.1:8902/tree/test/nginx
* http://127.0.0.1:8903/tree/test/nginx

### 测试过程
当三个节点都启动的时候，使用PostMan进行请求测试，会发现`节点1`和`节点2`轮询调用：
```
$ http://localhost:8088/tree/test/nginx
127.0.0.1 - - [20/May/2022:14:47:03 +0800] "POST /tree/test/nginx HTTP/1.1" "127.0.0.1:8901" [200] 0.009S "-" "PostmanRuntime/7.29.0" "-"
127.0.0.1 - - [20/May/2022:14:47:03 +0800] "POST /tree/test/nginx HTTP/1.1" "127.0.0.1:8902" [200] 0.008S "-" "PostmanRuntime/7.29.0" "-"
127.0.0.1 - - [20/May/2022:14:47:04 +0800] "POST /tree/test/nginx HTTP/1.1" "127.0.0.1:8901" [200] 0.010S "-" "PostmanRuntime/7.29.0" "-"
127.0.0.1 - - [20/May/2022:14:47:05 +0800] "POST /tree/test/nginx HTTP/1.1" "127.0.0.1:8902" [200] 0.005S "-" "PostmanRuntime/7.29.0" "-"
127.0.0.1 - - [20/May/2022:14:47:05 +0800] "POST /tree/test/nginx HTTP/1.1" "127.0.0.1:8901" [200] 0.008S "-" "PostmanRuntime/7.29.0" "-"
127.0.0.1 - - [20/May/2022:14:47:06 +0800] "POST /tree/test/nginx HTTP/1.1" "127.0.0.1:8902" [200] 0.004S "-" "PostmanRuntime/7.29.0" "-"
```

当关闭`节点1`的的时候，使用PostMan进行请求测试，会发现会尝试调用`节点1`，但如果`节点1`调用失败，
就继续调用`节点2`，且如果10S内`节点1`失败3次，那么就直接调用`节点2`进行响应：
```
$ http://localhost:8088/tree/test/nginx
127.0.0.1 - - [20/May/2022:14:48:37 +0800] "POST /tree/test/nginx HTTP/1.1" "127.0.0.1:8902" [200] 0.014S "-" "PostmanRuntime/7.29.0" "-"
127.0.0.1 - - [20/May/2022:14:48:38 +0800] "POST /tree/test/nginx HTTP/1.1" "127.0.0.1:8901, 127.0.0.1:8902" [200] 0.003S "-" "PostmanRuntime/7.29.0" "-"
127.0.0.1 - - [20/May/2022:14:48:39 +0800] "POST /tree/test/nginx HTTP/1.1" "127.0.0.1:8902" [200] 0.007S "-" "PostmanRuntime/7.29.0" "-"
127.0.0.1 - - [20/May/2022:14:48:40 +0800] "POST /tree/test/nginx HTTP/1.1" "127.0.0.1:8901, 127.0.0.1:8902" [200] 0.005S "-" "PostmanRuntime/7.29.0" "-"
127.0.0.1 - - [20/May/2022:14:48:41 +0800] "POST /tree/test/nginx HTTP/1.1" "127.0.0.1:8902" [200] 0.017S "-" "PostmanRuntime/7.29.0" "-"
127.0.0.1 - - [20/May/2022:14:48:59 +0800] "POST /tree/test/nginx HTTP/1.1" "127.0.0.1:8901, 127.0.0.1:8902" [200] 0.029S "-" "PostmanRuntime/7.29.0" "-"
127.0.0.1 - - [20/May/2022:14:48:59 +0800] "POST /tree/test/nginx HTTP/1.1" "127.0.0.1:8902" [200] 0.015S "-" "PostmanRuntime/7.29.0" "-"
127.0.0.1 - - [20/May/2022:14:49:00 +0800] "POST /tree/test/nginx HTTP/1.1" "127.0.0.1:8902" [200] 0.006S "-" "PostmanRuntime/7.29.0" "-"
127.0.0.1 - - [20/May/2022:14:49:01 +0800] "POST /tree/test/nginx HTTP/1.1" "127.0.0.1:8902" [200] 0.025S "-" "PostmanRuntime/7.29.0" "-"
127.0.0.1 - - [20/May/2022:14:49:01 +0800] "POST /tree/test/nginx HTTP/1.1" "127.0.0.1:8902" [200] 0.016S "-" "PostmanRuntime/7.29.0" "-"
```

当`节点1`和`节点2`都关闭的时候，就会转发到 `备用节点3`，且也是失败3次之后，直接转发到`节点3`，跳过尝试步骤：
```
$ http://localhost:8088/tree/test/nginx
127.0.0.1 - - [20/May/2022:14:49:36 +0800] "POST /tree/test/nginx HTTP/1.1" "127.0.0.1:8902, 127.0.0.1:8901, 127.0.0.1:8903" [200] 0.479S "-" "PostmanRuntime/7.29.0" "-"
127.0.0.1 - - [20/May/2022:14:49:37 +0800] "POST /tree/test/nginx HTTP/1.1" "127.0.0.1:8902, 127.0.0.1:8903" [200] 0.013S "-" "PostmanRuntime/7.29.0" "-"
127.0.0.1 - - [20/May/2022:14:49:38 +0800] "POST /tree/test/nginx HTTP/1.1" "127.0.0.1:8902, 127.0.0.1:8903" [200] 0.085S "-" "PostmanRuntime/7.29.0" "-"
127.0.0.1 - - [20/May/2022:14:49:39 +0800] "POST /tree/test/nginx HTTP/1.1" "127.0.0.1:8903" [200] 0.009S "-" "PostmanRuntime/7.29.0" "-"
127.0.0.1 - - [20/May/2022:14:49:39 +0800] "POST /tree/test/nginx HTTP/1.1" "127.0.0.1:8903" [200] 0.031S "-" "PostmanRuntime/7.29.0" "-"
127.0.0.1 - - [20/May/2022:14:49:40 +0800] "POST /tree/test/nginx HTTP/1.1" "127.0.0.1:8903" [200] 0.003S "-" "PostmanRuntime/7.29.0" "-"
127.0.0.1 - - [20/May/2022:14:49:41 +0800] "POST /tree/test/nginx HTTP/1.1" "127.0.0.1:8903" [200] 0.004S "-" "PostmanRuntime/7.29.0" "-"
127.0.0.1 - - [20/May/2022:14:49:41 +0800] "POST /tree/test/nginx HTTP/1.1" "127.0.0.1:8903" [200] 0.022S "-" "PostmanRuntime/7.29.0" "-"
```

10秒之后，会重新尝试 `节点1` 和 `节点2`，但只会尝试1次，不成功还是直接转发到 `备用节点3`：
```
$ http://localhost:8088/tree/test/nginx
127.0.0.1 - - [20/May/2022:14:51:01 +0800] "POST /tree/test/nginx HTTP/1.1" "127.0.0.1:8901, 127.0.0.1:8902, 127.0.0.1:8903" [200] 0.009S "-" "PostmanRuntime/7.29.0" "-"
127.0.0.1 - - [20/May/2022:14:51:04 +0800] "POST /tree/test/nginx HTTP/1.1" "127.0.0.1:8903" [200] 0.006S "-" "PostmanRuntime/7.29.0" "-"
127.0.0.1 - - [20/May/2022:14:51:05 +0800] "POST /tree/test/nginx HTTP/1.1" "127.0.0.1:8903" [200] 0.015S "-" "PostmanRuntime/7.29.0" "-"
```

## Nginx主动健康检查
由Nginx定期的向每台应用服务器发送特殊的请求，来监测应用服务器是否可以正常访问。这种方式称为主动监测。
需要指出的是，主动检测指令health_check目前只有nginx商业版本才提供。

为了实现主动监测这种方式，我们需要在Nginx负载均衡的配置文件中加入health_check指令。除此之外，
我们还需要在设置应用服务器信息的组里加入zone指令。

```nginx
http {
    upstream onmpw {
        zone onmpw 64k;
        server 192.168.144.128;
        server 192.168.144.132;
        server 192.168.144.131;
    }
    server {
        listen 80;
        location / {
            proxy_pass http://onmpw;
            health_check;
        }
    }
  }
```
在这里我们设置了一组应用服务器。通过一个单一的location，将所有的请求都分发到这组应用服务器上。
在这种情况下，每隔5s Nginx Plus就会向每一台应用服务器发送’/’请求。任何一台应用服务器连接错误
或者响应超时亦或者是被代理的服务器响应了一个状态码2xx或者是3xx，health_check机制就会认为是失败的。
对于任何一台应用服务器，如果health_check失败，则就会被认为是不稳定的。那么Nginx Plus就不再向
这台应用服务器分发访问请求。

zone指令定义了一块儿内存空间。这块儿空间存储在各个工作进程中共享的运行环境的状态和应用服务器组
的配置信息。这块儿空间应该根据实际情况尽量申请的大一些，要保证能存下这些信息。

```nginx
location / {
    proxy_pass http://onmpw;
    health_check interval=10 fails=3 passes=2;
}
```
在上面的例三中，interval=10表示两次进行health_check的间隔为10s，如果不设置默认两次的间隔是5s。
fails=3表示一台应用服务器如果请求失败次数达到3次，则该应用服务器被认为不能访问。最后是passes=2表示，
被认定为不能访问的服务器需要再次进行两次health_check 以后才会再次被认为是可以正常访问的。

在health_check中，我们可以指定请求的url：
```nginx
location / {
    proxy_pass http://onmpw;
    health_check uri=/some/path;
}
```
对于onmpw组中的第一台应用服务器128来说，一次health check请求的url是http://192.168.144.128/some/path。



## Nginx自定义健康检查方案
nginx_upstream_check_module 是专门提供负载均衡器内节点的健康检查的外部模块，
由淘宝的姚伟斌大神开发，通过它可以用来检测后端 realserver 的健康状态。

在淘宝自己的 tengine 上是自带了该模块的，大家可以访问淘宝tengine的官网来获取该版本的nginx，
官方地址：http://tengine.taobao.org/。

一个常用的健康检查配置如下：
```
check fall=3 interval=3000 rise=2 timeout=2000 type=http;
check_http_expect_alive http_2xx http_3xx ;
check_http_send "GET /checkAlive HTTP/1.0\r\n\r\n" ;
```

### 参数详解

#### check
check 字段参数如下：
```
Syntax: check interval=milliseconds [fall=count] [rise=count] [timeout=milliseconds] [default_down=true|false] [type=tcp|http|ssl_hello|mysql|ajp] [port=check_port]
Default: 如果没有配置参数，默认值是：interval=30000 fall=5 rise=2 timeout=1000 default_down=true type=tcp
```
* `interval`：向后端发送的健康检查包的间隔
* `fall`(fall_count)：如果连续失败次数达到fall_count，服务器就被认为是down
* `rise`(rise_count)：如果连续成功次数达到rise_count，服务器就被认为是up
* `timeout`：后端健康请求的超时时间
* `default_down`：设定初始时服务器的状态，如果是true，说明默认是down的；如果是false，就是up的
* `type`：健康检查包的类型，现在支持以下多种类型
  * `tcp`：简单的tcp连接，如果连接成功，就说明后端正常
  * `ssl_hello`：发送一个初始的SSL hello包，并接受服务器的SSL hello包
  * `http`：发送HTTP请求，通过后端的回复包的状态来判断后端是否存活
  * `mysql`：向mysql服务器连接，通过接收服务器的greeting包来判断后端是否存活
  * `ajp`：向后端发送AJP协议的Cping包，通过接收Cpong包来判断后端是否存活
  * `port`：指定后端服务器的检查端口。可以指定不同于真是服务器的后端服务器的端口，比如后端提供的是443端口，我们可以检查80端口；默认和请求端口一直

#### check_http_expect_alive
check_http_expect_alive 指定主动健康检查时HTTP回复的成功状态：
```
Syntax: check_http_expect_alive [ http_2xx | http_3xx | http_4xx | http_5xx ]
Default: http_2xx | http_3xx
```

#### check_http_send
check_http_send 配置http健康检查包发送的请求内容：
```
Syntax: check_http_send http_packet
Default: "GET /tree/checkAlive HTTP/1.0\r\n\r\n"
```

### docker环境
参考 `src/main/java/y2022/m05/d20/nginx-check` 下的 `Dockerfile`，打包编译准备环境：
```shell
docker build -t adolphor/nginx-check .
```

### 完整示例

> nginx-check-8988.conf

```nginx
upstream cluster {
  # 127.0.0.1
  server mbp.local:8901;
  server mbp.local:8902;
  server mbp.local:8903;

  check interval=3000 rise=2 fall=5 timeout=1000 type=http;
  check_http_send "GET /tree/checkAlive HTTP/1.0\r\n\r\n";
  check_http_expect_alive http_2xx http_3xx;
}

server {
  listen 8988 default;
  server_name localhost;
  location /tree {
    proxy_pass http://cluster;
    proxy_connect_timeout    1s;
    proxy_read_timeout    5s;
    proxy_send_timeout    5s;
    proxy_next_upstream    error timeout;
    health_check uri=/tree/checkAlive;
    access_log  logs/nginx-check-8988.log  main;
  }
  location /status {
    check_status;
    access_log off;
    #allow SOME.IP.ADD.RESS;
    #deny all;
  }
}
```

## 参考资料
* [nginx的原生被动健康检查机制&灾备使用]({% post_url framework/nginx/2022-05-20-02-nginx-server-health-and-backup %})
* [详解nginx的原生被动健康检查机制&灾备使用](http://www.net-add.com/devops/sre/sysops/lb/82.html)
* [nginx安装nginx_upstream_check_module模块](https://www.365seal.com/y/xPnQRgzlvK.html)
* [Nginx负载均衡健康检测，你了解过吗？](https://cloud.tencent.com/developer/article/1427225)