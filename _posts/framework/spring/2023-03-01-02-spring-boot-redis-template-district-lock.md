---
layout:     post
title:      spring-boot 使用 redisTemplate DIY 分布式锁
date:       2023-03-01 11:48:02 +0800
postId:     2023-03-01-11-48-02
categories: [Spring]
keywords:   [Spring]
---

## 参考资料
* [spring-boot 使用 redisTemplate DIY 分布式锁]({% post_url framework/spring/2023-03-01-02-spring-boot-redis-template-district-lock %})
* [redis实现分布式锁，再领个看门狗](https://blog.csdn.net/weixin_42645678/article/details/124449623)
* [手写一个基于redis的分布式锁（watch dog看门狗 / redisson分布式锁的底层原理）](https://blog.51cto.com/u_15127642/2754894)
* [redisson中的看门狗机制总结](https://www.cnblogs.com/jelly12345/p/14699492.html)
* [舒服了，踩到一个关于分布式锁的非比寻常的BUG！](https://heapdump.cn/article/3751655)

开始写作吧
```
![image-alter]({{ site.baseurl }}/image/post/2023/03/01/02/xxx.jpg)
```
