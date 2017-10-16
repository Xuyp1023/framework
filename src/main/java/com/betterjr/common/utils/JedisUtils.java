/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.betterjr.common.utils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterjr.common.config.Global;
import com.betterjr.common.service.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisException;

/**
 * Jedis Cache 工具类
 *
 * @author ThinkGem
 * @version 2014-6-29
 */
public class JedisUtils {

    private static Logger logger = LoggerFactory.getLogger(JedisUtils.class);

    private static JedisPool jedisPool = SpringContextHolder.getBean("defaultJedisPool");

    public static final String KEY_PREFIX = Global.getConfig("redis.keyPrefix");

    public static Integer keysCount(final String keyPartten) {
        final Optional<Set<String>> keysOpt = Optional.ofNullable(keys(keyPartten));
        final Optional<Integer> keysCount = keysOpt.map(keys -> keys.size());
        return keysCount.orElse(0);
    }

    public static Set<String> keys(final String keyPartten) {
        Jedis jedis = null;
        Set<String> keys = null;
        try {
            jedis = getResource();
            keys = jedis.keys(keyPartten);
        }
        catch (final Exception e) {
            logger.warn("keys {}", keyPartten, e);
        }
        finally {
            returnResource(jedis);
        }
        return keys;
    }

    /**
    * checkBigThanAndSet
    * 确保写入redis的值是升序的，重试10次，如果写入不成功，则返回anValue+anIdGap
    * @param anKey
    * @param anValue
    * @return
    */
    public static long checkBigThanAndSet(String anKey, long anValue, long anIdGap, int expireSeconds) {

        Jedis jedis = getResource();
        try {
            for (int index = 0; index < 10; index++) {
                jedis.watch(anKey);
                String valueStr = jedis.get(anKey);
                if (valueStr == null) {
                    valueStr = String.valueOf(anValue);
                }
                Long redisOriValue = Long.valueOf(valueStr);
                if (anValue < redisOriValue) {
                    anValue = anValue + (redisOriValue - anValue) + anIdGap;
                }

                Transaction tran = jedis.multi();
                tran.set(anKey, String.valueOf(anValue));
                List<Object> result = tran.exec();
                if (result != null && result.size() > 0 && "OK".equals(result.get(0))) {
                    if (expireSeconds != 0) {
                        jedis.expire(anKey, expireSeconds);
                    }
                    return anValue;
                }

                jedis.unwatch();

                try {
                    int rad = new Random().nextInt(10);
                    Thread.sleep(100 * rad);
                }
                catch (Exception ex) {}
            }
        }
        finally {
            jedis.unwatch();
            returnResource(jedis);
        }
        return anValue + anIdGap;
    }

    /**
    * incrby
    */
    public static long incrby(String key, long step) {
        Jedis jedis = getResource();
        try {
            return jedis.incrBy(key, step);
        }
        finally {
            returnResource(jedis);
        }
    }

    /**
     * 获取缓存
     *
     * @param key
     *           键
     * @return 值
     */
    public static String get(final String key) {
        String value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                value = jedis.get(key);
                value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
                logger.debug("get {} = {}", key, value);
            }
        }
        catch (final Exception e) {
            logger.warn("get {} = {}", key, value, e);
        }
        finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 获取缓存
     *
     * @param key
     *           键
     * @return 值
     */
    public static <T> T getObject(final String key) {
        Object value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                value = toObject(jedis.get(getBytesKey(key)));
                logger.debug("getObject {} = {}", key, value);
            }
        }
        catch (final Exception e) {
            logger.warn("getObject {} = {}", key, value, e);
        }
        finally {
            returnResource(jedis);
        }
        return (T) value;
    }

    /**
     * 设置缓存
     *
     * @param key
     *           键
     * @param value
     *           值
     * @param cacheSeconds
     *           超时时间，0为不超时
     * @return
     */
    public static String set(final String key, final String value, final int cacheSeconds) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.set(key, value);
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("set {} = {}", key, value);
        }
        catch (final Exception e) {
            logger.warn("set {} = {}", key, value, e);
        }
        finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 设置缓存
     *
     * @param key
     *           键
     * @param value
     *           值
     * @param cacheSeconds
     *           超时时间，0为不超时
     * @return
     */
    public static String setObject(final String key, final Object value, final int cacheSeconds) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.set(getBytesKey(key), toBytes(value));
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("setObject {} = {}", key, value);
        }
        catch (final Exception e) {
            logger.warn("setObject {} = {}", key, value, e);
        }
        finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 获取List缓存
     *
     * @param key
     *           键
     * @return 值
     */
    public static List<String> getList(final String key) {
        List<String> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                value = jedis.lrange(key, 0, -1);
                logger.debug("getList {} = {}", key, value);
            }
        }
        catch (final Exception e) {
            logger.warn("getList {} = {}", key, value, e);
        }
        finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 获取List缓存
     *
     * @param key
     *           键
     * @return 值
     */
    public static List<Object> getObjectList(final String key) {
        List<Object> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                final List<byte[]> list = jedis.lrange(getBytesKey(key), 0, -1);
                value = Lists.newArrayList();
                for (final byte[] bs : list) {
                    value.add(toObject(bs));
                }
                logger.debug("getObjectList {} = {}", key, value);
            }
        }
        catch (final Exception e) {
            logger.warn("getObjectList {} = {}", key, value, e);
        }
        finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 设置List缓存
     *
     * @param key
     *           键
     * @param value
     *           值
     * @param cacheSeconds
     *           超时时间，0为不超时
     * @return
     */
    public static long setList(final String key, final List<String> value, final int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                jedis.del(key);
            }
            result = jedis.rpush(key, (String[]) value.toArray());
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("setList {} = {}", key, value);
        }
        catch (final Exception e) {
            logger.warn("setList {} = {}", key, value, e);
        }
        finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 设置List缓存
     *
     * @param key
     *           键
     * @param value
     *           值
     * @param cacheSeconds
     *           超时时间，0为不超时
     * @return
     */
    public static long setObjectList(final String key, final List<Object> value, final int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                jedis.del(key);
            }
            final List<byte[]> list = Lists.newArrayList();
            for (final Object o : value) {
                list.add(toBytes(o));
            }
            result = jedis.rpush(getBytesKey(key), (byte[][]) list.toArray());
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("setObjectList {} = {}", key, value);
        }
        catch (final Exception e) {
            logger.warn("setObjectList {} = {}", key, value, e);
        }
        finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 向List缓存中添加值
     *
     * @param key
     *           键
     * @param value
     *           值
     * @return
     */
    public static long listAdd(final String key, final String... value) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.rpush(key, value);
            logger.debug("listAdd {} = {}", key, value);
        }
        catch (final Exception e) {
            logger.warn("listAdd {} = {}", key, value, e);
        }
        finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 向List缓存中添加值
     *
     * @param key
     *           键
     * @param value
     *           值
     * @return
     */
    public static long listObjectAdd(final String key, final Object... value) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            final List<byte[]> list = Lists.newArrayList();
            for (final Object o : value) {
                list.add(toBytes(o));
            }
            result = jedis.rpush(getBytesKey(key), (byte[][]) list.toArray());
            logger.debug("listObjectAdd {} = {}", key, value);
        }
        catch (final Exception e) {
            logger.warn("listObjectAdd {} = {}", key, value, e);
        }
        finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 获取缓存
     *
     * @param key
     *           键
     * @return 值
     */
    public static Set<String> getSet(final String key) {
        Set<String> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                value = jedis.smembers(key);
                logger.debug("getSet {} = {}", key, value);
            }
        }
        catch (final Exception e) {
            logger.warn("getSet {} = {}", key, value, e);
        }
        finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 获取缓存
     *
     * @param key
     *           键
     * @return 值
     */
    public static Set<Object> getObjectSet(final String key) {
        Set<Object> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                value = Sets.newHashSet();
                final Set<byte[]> set = jedis.smembers(getBytesKey(key));
                for (final byte[] bs : set) {
                    value.add(toObject(bs));
                }
                logger.debug("getObjectSet {} = {}", key, value);
            }
        }
        catch (final Exception e) {
            logger.warn("getObjectSet {} = {}", key, value, e);
        }
        finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 设置Set缓存
     *
     * @param key
     *           键
     * @param value
     *           值
     * @param cacheSeconds
     *           超时时间，0为不超时
     * @return
     */
    public static long setSet(final String key, final Set<String> value, final int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                jedis.del(key);
            }
            result = jedis.sadd(key, (String[]) value.toArray());
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("setSet {} = {}", key, value);
        }
        catch (final Exception e) {
            logger.warn("setSet {} = {}", key, value, e);
        }
        finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 设置Set缓存
     *
     * @param key
     *           键
     * @param value
     *           值
     * @param cacheSeconds
     *           超时时间，0为不超时
     * @return
     */
    public static long setObjectSet(final String key, final Set<Object> value, final int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                jedis.del(key);
            }
            final Set<byte[]> set = Sets.newHashSet();
            for (final Object o : value) {
                set.add(toBytes(o));
            }
            result = jedis.sadd(getBytesKey(key), (byte[][]) set.toArray());
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("setObjectSet {} = {}", key, value);
        }
        catch (final Exception e) {
            logger.warn("setObjectSet {} = {}", key, value, e);
        }
        finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 向Set缓存中添加值
     *
     * @param key
     *           键
     * @param value
     *           值
     * @return
     */
    public static long setSetAdd(final String key, final String... value) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.sadd(key, value);
            logger.debug("setSetAdd {} = {}", key, value);
        }
        catch (final Exception e) {
            logger.warn("setSetAdd {} = {}", key, value, e);
        }
        finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 向Set缓存中添加值
     *
     * @param key
     *           键
     * @param value
     *           值
     * @return
     */
    public static long setSetObjectAdd(final String key, final Object... value) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            final Set<byte[]> set = Sets.newHashSet();
            for (final Object o : value) {
                set.add(toBytes(o));
            }
            result = jedis.rpush(getBytesKey(key), (byte[][]) set.toArray());
            logger.debug("setSetObjectAdd {} = {}", key, value);
        }
        catch (final Exception e) {
            logger.warn("setSetObjectAdd {} = {}", key, value, e);
        }
        finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 获取Map缓存
     *
     * @param key
     *           键
     * @return 值
     */
    public static Map<String, String> getMap(final String key) {
        Map<String, String> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                value = jedis.hgetAll(key);
                logger.debug("getMap {} = {}", key, value);
            }
        }
        catch (final Exception e) {
            logger.warn("getMap {} = {}", key, value, e);
        }
        finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 获取Map缓存
     *
     * @param key
     *           键
     * @return 值
     */
    public static <T> Map<String, T> getObjectMap(final String key) {
        Map<String, T> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                value = Maps.newHashMap();
                final Map<byte[], byte[]> map = jedis.hgetAll(getBytesKey(key));
                for (final Map.Entry<byte[], byte[]> e : map.entrySet()) {
                    value.put(BetterStringUtils.toString(e.getKey()), (T) toObject(e.getValue()));
                }
                logger.debug("getObjectMap {} = {}", key, value);
            }
        }
        catch (final Exception e) {
            logger.warn("getObjectMap {} = {}", key, value, e);
        }
        finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 获取Map缓存
     *
     * @param key
     *           键
     * @return 值
     */
    public static <T> T getObjectMapField(final String key, final String field) {
        T value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                final byte[] result = jedis.hget(getBytesKey(key), getBytesKey(field));
                value = (T) toObject(result);
                logger.debug("getObjectMapField {}.{} = {}", key, field, value);
            }
        }
        catch (final Exception e) {
            logger.warn("getObjectMapField {}.{} = {}", key, field, value, e);
        }
        finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 设置Map缓存
     *
     * @param key
     *           键
     * @param value
     *           值
     * @param cacheSeconds
     *           超时时间，0为不超时
     * @return
     */
    public static String setMap(final String key, final Map<String, String> value, final int cacheSeconds) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                jedis.del(key);
            }
            result = jedis.hmset(key, value);
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("setMap {} = {}", key, value);
        }
        catch (final Exception e) {
            logger.warn("setMap {} = {}", key, value, e);
        }
        finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 设置Map缓存
     *
     * @param key
     *           键
     * @param value
     *           值
     * @param cacheSeconds
     *           超时时间，0为不超时
     * @return
     */
    public static <T> String setObjectMap(final String key, final Map<String, T> value, final int cacheSeconds) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                jedis.del(key);
            }
            final Map<byte[], byte[]> map = Maps.newHashMap();
            for (final Map.Entry<String, T> e : value.entrySet()) {
                map.put(getBytesKey(e.getKey()), toBytes(e.getValue()));
            }
            result = jedis.hmset(getBytesKey(key), map);
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("setObjectMap {} = {}", key, value);
        }
        catch (final Exception e) {
            logger.warn("setObjectMap {} = {}", key, value, e);
        }
        finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 向Map缓存中添加值
     *
     * @param key
     *           键
     * @param value
     *           值
     * @return
     */
    public static String mapPut(final String key, final Map<String, String> value) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.hmset(key, value);
            logger.debug("mapPut {} = {}", key, value);
        }
        catch (final Exception e) {
            logger.warn("mapPut {} = {}", key, value, e);
        }
        finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 向Map缓存中添加值
     *
     * @param key
     *           键
     * @param value
     *           值
     * @return
     */
    public static String mapObjectPut(final String key, final Map<String, Object> value) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            final Map<byte[], byte[]> map = Maps.newHashMap();
            for (final Map.Entry<String, Object> e : value.entrySet()) {
                map.put(getBytesKey(e.getKey()), toBytes(e.getValue()));
            }
            result = jedis.hmset(getBytesKey(key), map);
            logger.debug("mapObjectPut {} = {}", key, value);
        }
        catch (final Exception e) {
            logger.warn("mapObjectPut {} = {}", key, value, e);
        }
        finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 向Map缓存中添加值
     *
     * @param key
     *           键
     * @param value
     *           值
     * @return
     */
    public static <T> String mapFieldObjectPut(final String key, final String field, final T value) {
        Long result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.hset(getBytesKey(key), getBytesKey(field), getBytesKey(value));
            logger.debug("mapObjectPut {} = {}", key, value);
        }
        catch (final Exception e) {
            logger.warn("mapObjectPut {} = {}", key, value, e);
        }
        finally {
            returnResource(jedis);
        }
        return result == null ? null : result.toString();
    }

    /**
     * 移除Map缓存中的值
     *
     * @param key
     *           键
     * @param value
     *           值
     * @return
     */
    public static long mapRemove(final String key, final String mapKey) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.hdel(key, mapKey);
            logger.debug("mapRemove {}  {}", key, mapKey);
        }
        catch (final Exception e) {
            logger.warn("mapRemove {}  {}", key, mapKey, e);
        }
        finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 移除Map缓存中的值
     *
     * @param key
     *           键
     * @param value
     *           值
     * @return
     */
    public static long mapObjectRemove(final String key, final String mapKey) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.hdel(getBytesKey(key), getBytesKey(mapKey));
            logger.debug("mapObjectRemove {}  {}", key, mapKey);
        }
        catch (final Exception e) {
            logger.warn("mapObjectRemove {}  {}", key, mapKey, e);
        }
        finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 判断Map缓存中的Key是否存在
     *
     * @param key
     *           键
     * @param value
     *           值
     * @return
     */
    public static boolean mapExists(final String key, final String mapKey) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.hexists(key, mapKey);
            logger.debug("mapExists {}  {}", key, mapKey);
        }
        catch (final Exception e) {
            logger.warn("mapExists {}  {}", key, mapKey, e);
        }
        finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 判断Map缓存中的Key是否存在
     *
     * @param key
     *           键
     * @param value
     *           值
     * @return
     */
    public static boolean mapObjectExists(final String key, final String mapKey) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.hexists(getBytesKey(key), getBytesKey(mapKey));
            logger.debug("mapObjectExists {}  {}", key, mapKey);
        }
        catch (final Exception e) {
            logger.warn("mapObjectExists {}  {}", key, mapKey, e);
        }
        finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 删除缓存
     *
     * @param key
     *           键
     * @return
     */
    public static long del(final String key) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                result = jedis.del(key);
                logger.debug("del {}", key);
            } else {
                logger.debug("del {} not exists", key);
            }
        }
        catch (final Exception e) {
            logger.warn("del {}", key, e);
        }
        finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 删除缓存
     *
     * @param key
     *           键
     * @return
     */
    public static long delObject(final String key) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                result = jedis.del(getBytesKey(key));
                logger.debug("delObject {}", key);
            } else {
                logger.debug("delObject {} not exists", key);
            }
        }
        catch (final Exception e) {
            logger.warn("delObject {}", key, e);
        }
        finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 缓存是否存在
     *
     * @param key
     *           键
     * @return
     */
    public static boolean exists(final String key) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.exists(key);
            logger.debug("exists {}", key);
        }
        catch (final Exception e) {
            logger.warn("exists {}", key, e);
        }
        finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 缓存是否存在
     *
     * @param key
     *           键
     * @return
     */
    public static boolean existsObject(final String key) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.exists(getBytesKey(key));
            logger.debug("existsObject {}", key);
        }
        catch (final Exception e) {
            logger.warn("existsObject {}", key, e);
        }
        finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 获取资源
     *
     * @return
     * @throws JedisException
     */
    public static Jedis getResource() throws JedisException {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // logger.debug("getResource.", jedis);
        }
        catch (final JedisException e) {
            logger.warn("getResource.", e);
            returnBrokenResource(jedis);
            throw e;
        }
        return jedis;
    }

    /**
     * 归还资源
     *
     * @param jedis
     * @param isBroken
     */
    public static void returnBrokenResource(final Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnBrokenResource(jedis);
        }
    }

    /**
     * 释放资源
     *
     * @param jedis
     * @param isBroken
     */
    public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 获取byte[]类型Key
     *
     * @param key
     * @return
     */
    public static byte[] getBytesKey(final Object object) {
        if (object instanceof String) {
            return BetterStringUtils.getBytes((String) object);
        } else {
            return BTObjectUtils.serialize(object);
        }
    }

    /**
     * Object转换byte[]类型
     *
     * @param key
     * @return
     */
    public static byte[] toBytes(final Object object) {
        return BTObjectUtils.serialize(object);
    }

    /**
     * byte[]型转换Object
     *
     * @param key
     * @return
     */
    public static Object toObject(final byte[] bytes) {
        return BTObjectUtils.unserialize(bytes);
    }

}
