package com.betterjr.common.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HashMapIgnoreCase<K, V> extends HashMap {
    private static final long serialVersionUID = 8247702009535997212L;

    public HashMapIgnoreCase(int anP) {
        super(anP);
    }

    public HashMapIgnoreCase() {
        super();
    }

    public HashMapIgnoreCase(Map anMap) {
        super(anMap);
    }

    /**
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    @Override
    public boolean containsKey(Object key) {
        return super.containsKey(key.toString().toLowerCase());
    }

    /**
     * @see java.util.Map#get(java.lang.Object)
     */
    @Override
    public Object get(Object key) {
        return super.get(key.toString().toLowerCase());
    }

    /**
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    @Override
    public Object put(Object key, Object value) {
        if (key != null) {
            return super.put(key.toString().toLowerCase(), value);
        } else {
            return value;
        }
    }

    /**
     * @see java.util.Map#putAll(java.util.Map)
     */
    @Override
    public void putAll(Map m) {
        Iterator iter = m.keySet().iterator();
        while (iter.hasNext()) {
            Object key = iter.next();
            Object value = m.get(key);
            this.put(key, value);
        }
    }

    /**
     * @see java.util.Map#remove(java.lang.ObjecT)
     */
    @Override
    public Object remove(Object key) {
        return super.remove(key.toString().toLowerCase());
    }
}
