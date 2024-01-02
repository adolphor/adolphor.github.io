package com.bitsun.core.common.util;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Description: redis分布式锁工具类
 * @author:刘杨
 * @date Date:2019年08月21日17:42
 * @Company: 上海比升互联网技术
 * @Version V1.0
 */
@Component
@Slf4j
public class RedisLock {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final int DEFAULT_RETRY_INTERVAL_MILLIS = 50;

    /**
     * 加锁
     *
     * @param key               lock key
     * @param willExpireMillis  当前线程操作时的 System.currentTimeMillis() + 450，450是超时时间，这个地方不需要去设置redis的expire，
     *                          也不需要超时后手动去删除该key，因为会存在并发的线程都会去删除，造成上一个锁失效，结果都获得锁去执行，并发操作失败了就。
     * @param retryTimes        重试次数
     * @param tryIntervalMillis 重试间隔，毫秒
     * @return
     */
    public boolean lock(String key, long willExpireMillis, int retryTimes, int tryIntervalMillis) {
        //如果key值不存在，则返回 true，且设置 value
        if (redisTemplate.opsForValue().setIfAbsent(key, willExpireMillis)) {
            return true;
        }

        //获取key的值，判断是是否超时
        for (int i = 0; i < retryTimes; i++) {
            Long curVal = (Long)redisTemplate.opsForValue().get(key);
            if (curVal == null) {
                redisTemplate.opsForValue().set(key, willExpireMillis);
                return true;
            }

            if (curVal < System.currentTimeMillis()) {
                //获得之前的key值，同时设置当前的传入的value。这个地方可能几个线程同时过来，但是redis本身天然是单线程的，所以getAndSet方法还是会安全执行，
                //首先执行的线程，此时curVal当然和oldVal值相等，因为就是同一个值，之后该线程set了自己的value，后面的线程就取不到锁了
                Long oldVal = (Long)redisTemplate.opsForValue().getAndSet(key, willExpireMillis);
                if (oldVal != null && Objects.equals(oldVal, curVal)) {
                    return true;
                }
            }
            // 自旋操作
            try {
                log.info("线程" + Thread.currentThread().getName() + "占用锁失败，自旋等待结果");
                Thread.sleep(tryIntervalMillis);
            } catch (InterruptedException e) {
                continue;
            }

        }
        return false;
    }

    /**
     * 加锁
     *
     * @param key              lock key
     * @param willExpireMillis 当前线程操作时的 System.currentTimeMillis() + 450，450是超时时间，这个地方不需要去设置redis的expire，
     *                         也不需要超时后手动去删除该key，因为会存在并发的线程都会去删除，造成上一个锁失效，结果都获得锁去执行，并发操作失败了就。
     * @param retryTimes       重试次数
     * @return
     */
    public boolean lock(String key, long willExpireMillis, int retryTimes) {
        return lock(key, willExpireMillis, retryTimes, DEFAULT_RETRY_INTERVAL_MILLIS);
    }

    /**
     * 解锁
     *
     * @param key              lock key
     * @param willExpireMillis
     */
    public void unlock(String key, long willExpireMillis) {
        try {
            Object curVal = redisTemplate.opsForValue().get(key);
            if (curVal != null && curVal.equals(willExpireMillis)) {
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            log.error("解锁失败");
        }
    }

}
