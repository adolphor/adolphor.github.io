---
layout:     post
title:      SpringBoot使用 redisTemplate DIY 分布式锁
date:       2023-03-01 11:48:02 +0800
postId:     2023-03-01-11-48-02
categories: [Spring]
keywords:   [Spring]
---

## 未实现看门狗的版本

已应用于 `cloud-aiot` 项目:

> ./redis-lock/RedisDistributedLock.java

```
public static @NotNull JSONObject getTokenObj(String username, String password) throws IOException {
    Object token = redisTemplate.opsForValue().get(redisKey(username));
    // 不为空直接返回，不使用全局锁，增加执行效率
    if (Objects.isNull(token)) {
        // 如果为空，则取锁，重新获取token
        boolean lock = redisDistributedLock.getLock(lockKey(), lockKey(), 3000, 5000);
        if (lock) {
            token = redisTemplate.opsForValue().get(redisKey(username));
            // 双重判断，避免上一次获取锁的线程已经重新获取过token，如果再次判断唯恐，则重新获取token
            if (Objects.isNull(token)){
                try {
                    token = refreshToken(username, password);
                } finally {
                    redisDistributedLock.unLock(lockKey(), lockKey());
                }
            }
        } else {
            throw new SystemException(RPC_ERROR);
        }
    }
    return (JSONObject) token;
}
```

## 参考资料
* [SpringBoot使用 redisTemplate DIY 分布式锁]({% post_url framework/spring/2023-03-01-02-spring-boot-redis-template-district-lock %})
* [redis实现分布式锁，再领个看门狗](https://blog.csdn.net/weixin_42645678/article/details/124449623)
* [手写一个基于redis的分布式锁（watch dog看门狗 / redisson分布式锁的底层原理）](https://blog.51cto.com/u_15127642/2754894)
* [redisson中的看门狗机制总结](https://www.cnblogs.com/jelly12345/p/14699492.html)
* [舒服了，踩到一个关于分布式锁的非比寻常的BUG！](https://heapdump.cn/article/3751655)

开始写作吧
```
![image-alter]({{ site.baseurl }}/image/post/2023/03/01/02/xxx.jpg)
```
