package com.betterjr.common.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterjr.common.service.SpringContextHolder;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class SimpleLock {
    private static Logger logger = LoggerFactory.getLogger(SimpleLock.class);

    private static JedisPool jedisPool;
    private final JedisLock jedisLock;
    private final String lockKey;
    private final Jedis jedis;
    private final int timeoutMsecs;
    private final int expireMsecs;

    public SimpleLock(final String lockKey) {
        this(lockKey, 3000, 60000);
    }

    public SimpleLock(final String lockKey, final int timeoutMsecs, final int expireMsecs) {
        jedisPool = SpringContextHolder.getBean("defaultJedisPool");
        this.lockKey = lockKey;
        this.jedis = jedisPool.getResource();
        this.timeoutMsecs = timeoutMsecs;
        this.expireMsecs = expireMsecs;
        this.jedisLock = new JedisLock(jedis, lockKey.intern(), timeoutMsecs, expireMsecs);
    }

    public void wrap(final Runnable runnable) {
        final long begin = System.currentTimeMillis();
        try {
            // timeout超时，等待入锁的时间，设置为3秒；expiration过期，锁存在的时间设置为1分钟
            logger.info("begin logck,lockKey={},timeoutMsecs={},expireMsecs={}", lockKey, timeoutMsecs, expireMsecs);
            if (jedisLock.acquire()) { // 启用锁
                runnable.run();
            }
            else {
                logger.info("The time wait for lock more than [{}] ms ", timeoutMsecs);
            }
        }
        catch (final Throwable t) {
            // 分布式锁异常
            logger.warn(t.getMessage(), t);
        }
        finally {
            this.lockRelease(jedisLock, jedis);
        }
        logger.info("[{}]cost={}", lockKey, System.currentTimeMillis() - begin);
    }

    /**
     * 释放锁
     */
    private void lockRelease(final JedisLock lock, final Jedis jedis) {
        if (lock != null) {
            try {
                lock.release();// 解锁
            }
            catch (final Exception e) {
            }
        }
        if (jedis != null) {
            try {
                jedisPool.returnResource(jedis);// 还到连接池里
            }
            catch (final Exception e) {
            }
        }
        logger.info("release logck,lockKey={},timeoutMsecs={},expireMsecs={}", lockKey, timeoutMsecs, expireMsecs);
    }

}
