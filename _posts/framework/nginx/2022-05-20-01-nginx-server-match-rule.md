---
layout:     post
title:      Nginx中server的匹配顺序
date:       2022-05-20 09:44:29 +0800
postId:     2022-05-20-09-44-29
categories: [Nginx]
keywords:   [Nginx]
---

在开始处理一个http请求时，nginx会取出header头中的`host`，与 nginx.conf 中每个 server 的 
`server_name` 进行匹配，以此决定到底由哪一个server块来处理这个请求。

## 匹配规则
server_name与host匹配优先级如下：
1、完全匹配
2、通配符在前的，如 `*.test.com`
3、在后的，如 `www.test.*`
4、正则匹配，如 `~^\.www\.test\.com$`

如果都不匹配
1、优先选择 listen 配置项后有 `default` 或 `default_server` 的
2、找到匹配 listen 端口的第一个 `server` 块

## 准备测试环境

### 安装nginx

使用的brew进行nginx的安装，因为需要使用echo指令，所以需要安装nginx-full才能使用相关模块：
```shell
brew tap denji/nginx
brew install nginx-full --with-echo-module
# 安装的时候需要使用编译工具，如果没指定需要指定
sudo xcode-select --switch /Applications/Xcode.app
# 启动
brew services restart denji/nginx/nginx-full
```

### 配置host记录
```shell
sudo vim /eth/hosts
```
增加解析：
```text
127.0.0.1 server.1
127.0.0.1 server.2
127.0.0.1 test.1
127.0.0.1 test.2
```

测试host生效：
```log
➜ ping server.1
PING server.1 (127.0.0.1): 56 data bytes
64 bytes from 127.0.0.1: icmp_seq=0 ttl=64 time=0.076 ms
64 bytes from 127.0.0.1: icmp_seq=1 ttl=64 time=0.074 ms
……
```

## 最高优先级：完全匹配

### 配置文件
> all-match-8081.conf
```nginx
server {
    listen 8081;
    server_name *.1;
    location / {
        default_type text/html;
        echo "通配符在前";
    }
}
server {
    listen 8081;
    server_name server.1;
    location / {
        default_type text/html;
        echo "完全匹配";
    }
}
```
### 结果验证

```log
$ curl http://server.1:8081
完全匹配
$  curl http://test.1:8081
通配符在前
```

## listen配置项中default的影响

### 配置文件

> default-8082.conf
```nginx
server {
    listen 8082;
    server_name ~^\w+\.1$;
    location / {
        default_type text/html;
        echo "正则匹配";
    }
}
server {
    listen 8082;
    server_name xixi.xixi;
    location / {
        default_type text/html;
        echo "不匹配";
    }
}
server {
    listen 8082 default;
    server_name haha.haha;
    location / {
        default_type text/html;
        echo "不匹配 default";
    }
}
```
### 结果验证

```log
$ curl http://server.1:8082
正则匹配
$ curl http://server.2:8082
不匹配 default
```

## 没有匹配成功，且没有default的情况

### 配置文件

> no-default-8083.conf
```nginx
server {
    listen 8083;
    server_name xixi.xixi;
    location / {
        default_type text/html;
        echo "不匹配，位置靠前";
    }
}
server {
    listen 8083;
    server_name haha.haha;
    location / {
        default_type text/html;
        echo "不匹配，位置靠后";
    }
}
```
### 结果验证

```log
$ curl http://server.1:8083
不匹配，位置靠前
```

## 参考资料
* [Nginx中server的匹配顺序]({% post_url framework/nginx/2022-05-20-01-nginx-server-match-rule %})
* [nginx中server的匹配顺序](https://www.cnblogs.com/wangzhisdu/p/7839109.html)