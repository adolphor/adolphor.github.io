---
layout:     post
title:      log4j/log4j2 配置方法
date:       2018-05-29 10:06:51 +0800
postId:     2018-05-29-10-06-52
categories: [Tools]
keywords:   [Tools]
---

## log4j

### 格式化

```
[%d{yyyy-MM-dd HH:mm:ss}] [%p] [%c.%M()] %m%n
```

## log4j2

### 格式化

```
[%d{yyyy-MM-dd HH:mm:ss} %-5level] %class{36}.%M() - %L - %msg%xEx%n
```

## 参考资料

* [log4j/log4j2 配置方法]({% post_url java/log/content/2018-05-29-log4j-log4j2-configuration %})
