package com.betterjr.common.security.shiro.cache;

import java.util.List;
import java.util.Random;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;

public class RedisManager {

    private String host = "127.0.0.1";

    private int port = 6379;

    // 0 - never expire
    private int expire = 0;

    // timeout for jedis try to connect to redis server, not expire time! In milliseconds
    private int timeout = 0;

    private String password = "";

    private JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

    private static JedisPool jedisPool = null;

    public RedisManager() {

    }

    /**
     * 初始化方法
     */

    public void init() {
        if (jedisPool == null) {
            if (password != null && !"".equals(password)) {
                jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);
            }
            else if (timeout != 0) {
                jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout);
            }
            else {
                jedisPool = new JedisPool(jedisPoolConfig, host, port);
            }

        }
    }

    /**
     * get value from redis
     * 
     * @param key
     * @return
     */
    public byte[] get(byte[] key) {
        byte[] value = null;
        Jedis jedis = jedisPool.getResource();
        try {
            value = jedis.get(key);
        }
        finally {
            jedisPool.returnResource(jedis);
        }
        return value;
    }

    /**
     * set
     * 
     * @param key
     * @param value
     * @return
     */
    public String set(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(key, value);
            if (this.expire != 0) {
                jedis.expire(key, this.expire);
            }
        }
        finally {
            jedisPool.returnResource(jedis);
        }
        return value;
    }

    /**
     * checkBigThanAndSet
     * 确保写入redis的值是升序的，重试10次，如果写入不成功，则返回anValue+anIdGap
     * @param anKey
     * @param anValue
     * @return
     */
    public long checkBigThanAndSet(String anKey, long anValue,long anIdGap) {
        
        Jedis jedis = jedisPool.getResource();
        try {
            for (int index = 0; index < 10; index++) {
                jedis.watch(anKey);
                String valueStr = jedis.get(anKey);
                if(valueStr==null){
                    valueStr=String.valueOf(anValue);
                }
                Long redisOriValue = Long.valueOf(valueStr);
                if (anValue < redisOriValue) {
                    anValue = anValue + (redisOriValue - anValue) + anIdGap;
                }

                Transaction tran = jedis.multi();
                tran.set(anKey, String.valueOf(anValue));
                List<Object> result = tran.exec();
                if (result != null && result.size() > 0 && "OK".equals(result.get(0))) {
                    if (this.expire != 0) {
                        jedis.expire(anKey, this.expire);
                    }
                    return anValue;
                }
                
                jedis.unwatch();
                
                try{
                    int rad=new Random().nextInt(10);
                    Thread.sleep(100*rad);
                }catch(Exception ex){}
            }
        }
        finally {
            jedis.unwatch();
            jedisPool.returnResource(jedis);
        }
        return anValue+anIdGap;
    }

    /**
     * set
     * 
     * @param key
     * @param value
     * @return
     */
    public byte[] set(byte[] key, byte[] value) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(key, value);
            if (this.expire != 0) {
                jedis.expire(key, this.expire);
            }
        }
        finally {
            jedisPool.returnResource(jedis);
        }
        return value;
    }

    /**
     * set
     * 
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public byte[] set(byte[] key, byte[] value, int expire) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(key, value);
            if (expire != 0) {
                jedis.expire(key, expire);
            }
        }
        finally {
            jedisPool.returnResource(jedis);
        }
        return value;
    }

    /**
     * del
     * 
     * @param key
     */
    public void del(byte[] key) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.del(key);
        }
        finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * incrby
     */
    public long incrby(String key, long step) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.incrBy(key, step);
        }
        finally {
            jedis.close();
        }
    }

    /**
     * exists
     */
    public boolean exists(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.exists(key);
        }
        finally {
            jedis.close();
        }
    }

    /**
     * 
     */

    /**
     * flush
     */
    public void flushDB() {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.flushDB();
        }
        finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * size
     */
    public Long dbSize() {
        Long dbSize = 0L;
        Jedis jedis = jedisPool.getResource();
        try {
            dbSize = jedis.dbSize();
        }
        finally {
            jedisPool.returnResource(jedis);
        }
        return dbSize;
    }

    /**
     * keys
     * 
     * @param regex
     * @return
     */
    public Set<byte[]> keys(String pattern) {
        Set<byte[]> keys = null;
        Jedis jedis = jedisPool.getResource();
        try {
            keys = jedis.keys(pattern.getBytes());
        }
        finally {
            jedisPool.returnResource(jedis);
        }
        return keys;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public JedisPoolConfig getJedisPoolConfig() {
        return jedisPoolConfig;
    }

    public void setJedisPoolConfig(JedisPoolConfig jedisPoolConfig) {
        this.jedisPoolConfig = jedisPoolConfig;
    }

}
