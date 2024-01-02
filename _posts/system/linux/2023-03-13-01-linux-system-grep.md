---
layout:     post
title:      Linux系统-grep指令
date:       2023-03-13 10:01:43 +0800
postId:     2023-03-13-10-01-43
categories: [Linux]
keywords:   [Linux,grep]
---

## linux利用grep查看打印匹配的下几行或前后几行的命令

linux系统中，利用grep打印匹配的上下几行，如果在只是想匹配模式的上下几行，grep可以实现。

```shell
# 打印匹配行的前后5行
grep -5 'parttern' inputfile 
# 打印匹配行的前后5行 
grep -C 5 'parttern' inputfile
# 打印匹配行的后5行
grep -A 5 'parttern' inputfile
# 打印匹配行的前5行
grep -B 5 'parttern' inputfile
```

查看mysql慢日志中ip地址为192.168.0.10发送过来的SQL语句的后面三行

```shell
tail -50 /usr/local/mysql/data/sql-slow.log |grep -3 '192.168.0.10'
```

匹配php错误日志中某一个字段

```shell
tail -100 /data/logs/php/php_error_5.3.log  | grep  "Memcache::get()"
```

查看某一个文件第5行和第10行
```shell
sed -n '5,10p' filename
```

## 参考资料
* [Linux系统-grep指令]({% post_url system/linux/2023-03-13-01-linux-system-grep %})

开始写作吧
```
![image-alter]({{ site.baseurl }}/image/post/2023/03/13/01/xxx.jpg)
```
