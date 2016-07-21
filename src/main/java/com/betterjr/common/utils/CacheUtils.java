/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.betterjr.common.utils;

import java.util.Map;

import com.betterjr.common.service.SpringContextHolder;
import com.google.common.collect.Maps;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * Cache工具类
 * 
 * @author ThinkGem
 * @version 2013-5-29
 */
public class CacheUtils {


   private static final String SYS_CACHE = "sysCache";

   /**
    * 获取SYS_CACHE缓存
    * 
    * @param key
    * @return
    */
   public static Object get(String key) {
     return CacheUtils.get(SYS_CACHE, key);
   }

   /**
    * 写入SYS_CACHE缓存
    * 
    * @param key
    * @return
    */
   public static void put(String key, Object value) {
       CacheUtils.put(SYS_CACHE, key, value);
   }

   /**
    * 从SYS_CACHE缓存中移除
    * 
    * @param key
    * @return
    */
   public static void remove(String key) {
       CacheUtils.remove(SYS_CACHE, key);
   }

   /**
    * 获取缓存
    * 
    * @param cacheName
    * @param key
    * @return
    */
   public static Object get(String cacheName, String key) {
       Map<String, Object> map= JedisUtils.getObjectMap(cacheName);
       if(map==null){
           return null;
       }
       return map.get(key);
   }

   /**
    * 写入缓存
    * 
    * @param cacheName
    * @param key
    * @param value
    */
   public static void put(String cacheName, String key, Object value) {
       Map<String,Object> map=Maps.newHashMap();
       map.put(key, value);
       JedisUtils.mapObjectPut(cacheName, map);
   }

   /**
    * 从缓存中移除
    * 
    * @param cacheName
    * @param key
    */
   public static void remove(String cacheName, String key) {
       JedisUtils.mapRemove(cacheName, key);
   }

}
