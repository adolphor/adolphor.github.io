---
layout:     post
title:      linux 基本操作指令
date:       2018-04-28 09:12:20 +0800
postId:     2018-04-28-09-12-20
categories: [article]
tags:       [Linux,shell]
geneMenu:   true
excerpt:    linux 基本操作指令
---

## 网络

### 端口

#### netstat命令
```bash
netstat -an | grep <port>
```

范例：

```bash
$ netstat -an | grep 1087
tcp4       0      0  *.1087                 *.*                    LISTEN    
```

#### lsof命令 

通过`list open file`命令可以查看到当前打开文件，在linux中所有事物都是以文件形式存在，包括网络连接及硬件设备。
-i参数表示网络链接，:80指明端口号，该命令会同时列出PID，方便kill。

```bash
lsof -i:<port>
```

查看所有进程监听的端口（耗时很长，慎用）：
```bash
sudo lsof -i -P | grep -i "listen"
```

范例：
```bash
$ lsof -i:1087
COMMAND   PID      USER   FD   TYPE              DEVICE  SIZE/OFF  NODE  NAME
privoxy  1052  adolphor    3u  IPv4  0xf5bc06c5ea3112fd       0t0   TCP  *:cplscrambler-in (LISTEN)
```
