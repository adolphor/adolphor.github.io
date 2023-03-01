package com.joyoung.aiot.util;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Collections;

@Slf4j
@Component
public class RedisDistributedLock {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static String PREFIX = "REDIS_LOCK:";

    /**
     * 获取一个redis分布锁
     *
     * @param lockKey       锁住的key
     * @param value         锁住的value,  解锁时,value 必须一致
     * @param lockExpireSec 锁住的时长。如果超时未解锁，视为加锁线程死亡，其他线程可夺取锁
     */
    public Boolean getLock(String lockKey, String value, Long lockExpireSec) {
        if (StringUtils.isBlank(lockKey)) {
            return false;
        }
        // key序列化 // value序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new FastJsonRedisSerializer<>(Object.class));
        return this.redisTemplate.opsForValue().setIfAbsent(PREFIX + lockKey, value, Duration.ofSeconds(lockExpireSec));
    }

    /**
     * 获取一个redis分布锁
     *
     * @param lockKey            锁住的key
     * @param value              锁住的value,  解锁时,value 必须一致
     * @param acquireTimeoutMils 获取超时时间
     * @param lockExpireSec      锁住的时长。如果超时未解锁，视为加锁线程死亡，其他线程可夺取锁
     * @return
     */
    public boolean getLock(String lockKey, String value, long acquireTimeoutMils, long lockExpireSec) {
        long now = System.currentTimeMillis();
        Boolean isLock = false;
        while (true) {
            isLock = getLock(lockKey, value, lockExpireSec);
            if (isLock == true) {
                return true;
            }
            if (System.currentTimeMillis() >= now + acquireTimeoutMils) {
                //获取超时
                return false;
            }
            try {
                Thread.sleep(10l);
            } catch (Exception e) {
                log.error("", e);
            }

        }
    }

    /**
     * 释放锁
     **/
    public Boolean unLock(String key, String value) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        RedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);
        Long releaseStatus = this.redisTemplate.execute(redisScript, Collections.singletonList(PREFIX + key), value);
        return releaseStatus == 1L;
    }

    /**
     * 删除锁
     *
     * @param key
     */
    public void delete(String key) {
        redisTemplate.delete(PREFIX + key);
    }

}