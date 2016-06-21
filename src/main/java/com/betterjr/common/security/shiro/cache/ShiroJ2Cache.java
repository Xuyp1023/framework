package com.betterjr.common.security.shiro.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import net.oschina.j2cache.CacheChannel;
 
import net.oschina.j2cache.CacheObject;

/**
 * 封装j2cache为shiro的Cache接口
 * @author wendal
 */
@SuppressWarnings("unchecked")
public class  ShiroJ2Cache<K, V> implements Cache<K, V> {
    
    protected String region;
    
    protected CacheChannel channel;
    
    public ShiroJ2Cache(String region, CacheChannel channel) {
        this.region = region;
        this.channel = channel;
    }

    public V get(K key) throws CacheException {
        CacheObject val = this.channel.get(region, key);
        if (val == null)
            return null;
        return (V) val.getValue();
    }

    public V put(K key, V value) throws CacheException {
        this.channel.set(region, key, value);
        return null;
    }

    public V remove(K key) throws CacheException {
        this.channel.evict(region, key);
        return null;
    }

    public void clear() throws CacheException {
        this.channel.clear(region);
    }

    public int size() {
        return this.channel.keys(region).size();
    }

    public Set<K> keys() {
        return new HashSet<K>(this.channel.keys(region));
    }

    public Collection<V> values() {
        List<V> list = new ArrayList<V>();
        for (K k : keys()) {
            list.add(get(k));
        }
        return list;
    }

}

