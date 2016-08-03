package com.betterjr.common.data;

import java.util.Map;

import com.betterjr.common.utils.Collections3;
import com.google.common.collect.Maps;

public class KeyAndValueObject {
    private final Object key;
    private final Object value;

    public KeyAndValueObject(Object anKey, Object anValue) {
        this.key = anKey;
        this.value = anValue;
    }

    public Object getKey() {
        return this.key;
    }

    public Object getValue() {
        
        return this.value;
    }

    public String getStrKey() {
        if (key != null) {
            return key.toString();
        }
        else {
            return "";
        }
    }
    
    public Map<Object,Object> toMap(){
        Map<Object,Object> map=Maps.newHashMap();
        map.put(key, value);
        return map;
    }
    
    public static KeyAndValueObject newInstanceByMap(Map<Object,Object> map){
        if(!Collections3.isEmpty(map)){
            Object anKey=Collections3.getFirst(map.keySet());
            return new KeyAndValueObject(anKey,map.get(anKey));
        }
        return null;
    }
}