---
layout:     post
title:      Nginx 配置SSL支持HTTPS协议
date:       2022-04-22 09:51:08 +0800
postId:     2022-04-22-09-51-09
categories: [Nginx]
keywords:   [Nginx]
---

两个目的：1. 配置https的ssl证书，2. 对于http请求自动跳转到https

## 配置范例

```
server {
    listen                       443  ssl;
    listen                       [::]:443  ssl;
    server_name                  adolphor.github.io;
    access_log                   /var/log/nginx/hosts.sentry.access.log  main;
    ssl_certificate              /home/adolphor/certs/fullchain.cer;
    ssl_certificate_key          /home/adolphor/certs/adolphor.github.io.key;
    ssl_protocols                TLSv1 TLSv1.1 TLSv1.2;
    ssl_ciphers                  HIGH:!aNULL:!MD5;
    fastcgi_intercept_errors     on;
    proxy_intercept_errors       on;
    proxy_ssl_server_name        on;
    # 需要指定body size，不能太小，以支持sentry需求
    client_max_body_size         20m;

    location / {
        proxy_pass http://127.0.0.1:8080;
    }
}

server {
    listen                      80;
    listen                      [::]:80;
    server_name                 adolphor.github.io;
    access_log                  /var/log/nginx/host.sentry.access.log  main;
    # 将http转为https
    rewrite ^(.*)$   https://$host$1    permanent;
    # location / {
    #     proxy_pass http://127.0.0.1:8080;
    # }
}
```

## 安装测试

* 排查nginx启动是否成功
  * `netstat -anp | grep nginx`
* 排查防火墙是否开放了80和443端口
* 测试外网访问是否连接成功
  * `telnet adolphor.github.io 443`
* 网页请求是否成功
  * `curl adolphor.github.io`

## 错误排查

### ssl协议错误
需要在指定443端口的时候，指定`ssl`协议，否则会出现如下错误：
```
➜  ~ curl https://sentry.adolphor.github.io/auth/login/jyfe/\#exception
curl: (35) error:1400410B:SSL routines:CONNECT_CR_SRVR_HELLO:wrong version number
```

## 参考资料
* [Nginx 配置SSL支持HTTPS协议]({% post_url framework/nginx/2022-04-22-01-nginx-ssl-https %})
