---
layout:     post
title:      Redis - 分布式锁
date:       2021-10-02 20:20:05 +0800
postId:     2021-10-02-20-20-05
categories: [Redis]
keywords:   [database, Redis]
---
在分布式系统中，使用Redis作为分布式锁，是常用的一种分布式锁解决方案。

Redis 锁主要利用 Redis 的 setnx 命令。

## SETNX 和 EXPIRE 非原子性
* 加锁命令：SETNX key value，当键不存在时，对键进行设置操作并返回成功，否则返回失败。KEY 是锁的唯一标识，一般按业务来决定命名。
* 解锁命令：DEL key，通过删除键值对释放锁，以便其他线程可以通过 SETNX 命令来获取锁。
* 锁超时：EXPIRE key timeout, 设置 key 的超时时间，以保证即使锁没有被显式释放，锁也可以在一定时间后自动释放，避免资源被永远锁住。

* 加锁解锁伪代码如下：
```java
if (setnx(key, 1) == 1){
    expire(key, 30)
    try {
     // TODO 业务逻辑
    } finally {
        del(key)
    }
}
```

如果 SETNX 成功，在设置锁超时时间后，服务器挂掉、重启或网络问题等，导致 EXPIRE 命令没有执行，锁没有设置超时时间变成死锁。
![Redis-分布式锁-多步骤-非原子性]({{ site.baseurl }}/image/post/2021/10/02/06/Redis-分布式锁-多步骤-非原子性.png)

有很多开源代码来解决这个问题，比如使用 lua 脚本。示例：
```lua
if (redis.call('setnx', KEYS[1], ARGV[1]) < 1)
then return 0;
end;
redis.call('expire', KEYS[1], tonumber(ARGV[2]));
return 1;

// 使用实例
EVAL "if (redis.call('setnx',KEYS[1],ARGV[1]) < 1) then return 0; end; redis.call('expire',KEYS[1],tonumber(ARGV[2])); return 1;" 1 key value 100
```

## 锁误解除
如果线程 A 成功获取到了锁，并且设置了过期时间 30 秒，但线程 A 执行时间超过了 30 秒，
锁过期自动释放，此时线程 B 获取到了锁；随后 A 执行完成，线程 A 使用 DEL 命令来释放锁，
但此时线程 B 加的锁还没有执行完成，线程 A 实际释放的线程 B 加的锁。

![Redis-分布式锁-锁误解除]({{ site.baseurl }}/image/post/2021/10/02/06/Redis-分布式锁-锁误解除.png)

通过在 value 中设置当前线程加锁的标识，在删除之前验证 key 对应的 value 判断锁是否是当前线程持有。
可生成一个 UUID 标识当前线程，使用 lua 脚本做验证标识和解锁操作。
```lua
// 加锁
String uuid = UUID.randomUUID().toString().replaceAll("-","");
SET key uuid NX EX 30
// 解锁
if (redis.call('get', KEYS[1]) == ARGV[1])
    then return redis.call('del', KEYS[1])
else return 0
end
```

## 超时解锁导致并发
如果线程 A 成功获取锁并设置过期时间 30 秒，但线程 A 执行时间超过了 30 秒，锁过期自动释放，
此时线程 B 获取到了锁，线程 A 和线程 B 并发执行。

![Redis-分布式锁-超时解锁导致并发]({{ site.baseurl }}/image/post/2021/10/02/06/Redis-分布式锁-超时解锁导致并发.png)

A、B 两个线程发生并发显然是不被允许的，一般有两种方式解决该问题：

将过期时间设置足够长，确保代码逻辑在锁释放之前能够执行完成。
为获取锁的线程增加守护线程，为将要过期但未释放的锁增加有效时间。

![Redis-分布式锁-超时解锁-守护线程]({{ site.baseurl }}/image/post/2021/10/02/06/Redis-分布式锁-超时解锁-守护线程.png)

## 不可重入
当线程在持有锁的情况下再次请求加锁，如果一个锁支持一个线程多次加锁，那么这个锁就是可重入的。
如果一个不可重入锁被再次加锁，由于该锁已经被持有，再次加锁会失败。Redis 可通过对锁进行重入计数，
加锁时加 1，解锁时减 1，当计数归 0 时释放锁。

在本地记录记录重入次数，如 Java 中使用 ThreadLocal 进行重入次数统计，简单示例代码：
```java
private static ThreadLocal<Map<String, Integer>> LOCKERS = ThreadLocal.withInitial(HashMap::new);
// 加锁
public boolean lock(String key) {
  Map<String, Integer> lockers = LOCKERS.get();
  if (lockers.containsKey(key)) {
    lockers.put(key, lockers.get(key) + 1);
    return true;
  } else {
    if (SET key uuid NX EX 30) {
      lockers.put(key, 1);
      return true;
    }
  }
  return false;
}
// 解锁
public void unlock(String key) {
  Map<String, Integer> lockers = LOCKERS.get();
  if (lockers.getOrDefault(key, 0) <= 1) {
    lockers.remove(key);
    DEL key
  } else {
    lockers.put(key, lockers.get(key) - 1);
  }
}
```
本地记录重入次数虽然高效，但如果考虑到过期时间和本地、Redis 一致性的问题，就会增加代码的复杂性。
另一种方式是 Redis Map 数据结构来实现分布式锁，既存锁的标识也对重入次数进行计数。Redission 加锁示例：
```java
// 如果 lock_key 不存在
if (redis.call('exists', KEYS[1]) == 0)
then
    // 设置 lock_key 线程标识 1 进行加锁
    redis.call('hset', KEYS[1], ARGV[2], 1);
    // 设置过期时间
    redis.call('pexpire', KEYS[1], ARGV[1]);
    return nil;
    end;
// 如果 lock_key 存在且线程标识是当前欲加锁的线程标识
if (redis.call('hexists', KEYS[1], ARGV[2]) == 1)
    // 自增
    then redis.call('hincrby', KEYS[1], ARGV[2], 1);
    // 重置过期时间
    redis.call('pexpire', KEYS[1], ARGV[1]);
    return nil;
    end;
// 如果加锁失败，返回锁剩余时间
return redis.call('pttl', KEYS[1]);
```

## 无法等待锁释放
上述命令执行都是立即返回的，如果客户端可以等待锁释放就无法使用。

可以通过客户端轮询的方式解决该问题，当未获取到锁时，等待一段时间重新获取锁，
直到成功获取锁或等待超时。这种方式比较消耗服务器资源，当并发量比较大时，会影响服务器的效率。
另一种方式是使用 Redis 的发布订阅功能，当获取锁失败时，订阅锁释放消息，获取锁成功后释放时，
发送锁释放消息。如下：
![Redis-分布式锁-等待锁]({{ site.baseurl }}/image/post/2021/10/02/06/Redis-分布式锁-等待锁.png)

## Java 实现
了解了Redis的原理和局限，那么实际使用一下吧。首先添加 jedis 依赖：
```xml
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
    <version>2.9.0</version>
</dependency>
```
### 加锁代码
```java
public class RedisTool {
    private static final String LOCK_SUCCESS = "OK";
    /** NX：意思是SET IF NOT EXIST，即当key不存在时，我们进行set操作；若key已经存在，则不做任何操作；*/
    private static final String SET_IF_NOT_EXIST = "NX";
    /** 意思是我们要给这个key加一个过期的设置，具体时间由第五个参数决定。*/
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    /**
     * 尝试获取分布式锁
     * @param jedis      Redis客户端
     * @param lockKey    锁标识，
     * @param requestId  请求标识，防止锁误解除
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public static boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, int expireTime) {
        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }
}
```

### 解锁代码
使用Lua脚本进行解锁，那为什么执行eval()方法可以确保原子性，源于Redis的特性，
下面是官网对eval命令的部分解释：
简单来说，就是在eval命令执行Lua代码的时候，Lua代码将被当成一个命令去执行，
并且直到eval命令执行完成，Redis才会执行其他命令。

```java
public class RedisTool {
    private static final Long RELEASE_SUCCESS = 1L;
    /**
     * 释放分布式锁
     * @param jedis      Redis客户端
     * @param lockKey    锁
     * @param requestId  请求标识
     * @return 是否释放成功
     */
    public static boolean releaseDistributedLock(Jedis jedis, String lockKey, String requestId) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }
}
```

## 参考资料
* [Redis - 分布式锁]({% post_url database/redis/content/2021-10-02-06-redis-distributed-lock %})
* [分布式锁的实现之 redis 篇](https://xiaomi-info.github.io/2019/12/17/redis-distributed-lock/)
* [Redis 分布式锁的正确实现方式（ Java 版 ）](https://developer.aliyun.com/article/307547)
