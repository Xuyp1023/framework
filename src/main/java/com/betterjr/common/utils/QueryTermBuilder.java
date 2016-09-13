package com.betterjr.common.utils;

import java.util.*;

/**
 * 查询条件生成器
 * 
 * @author zhoucy
 *
 */
public class QueryTermBuilder {
    private final Map<String, Object> map;

    public QueryTermBuilder put(String anKey, Object anValue) {
        if (anValue != null){
            if (anValue instanceof String){
                if (BetterStringUtils.isBlank( (String) anValue )){
                    return this;
                }
            }
            map.put(anKey, anValue);
        }
        
        return this;
    }

    public Map build() {

        return map;
    }

    public QueryTermBuilder addAll(Map<String, Object> anMap){        
        this.map.putAll(anMap);
        
        return this;
    }
    
    public QueryTermBuilder remove(String anKey){
       this.map.remove(anKey);
       
       return this;
    }
    
    public static Map<String, Object> buildSingle(String anKey, Object anValue){
       Map<String, Object>  result = new HashMap();
       result.put(anKey, anValue);
       return result;
    }
    
    public static QueryTermBuilder newInstance(Map anMap) {

        return new QueryTermBuilder();
    }

    public static QueryTermBuilder newInstance() {

        return new QueryTermBuilder();
    }

    public QueryTermBuilder() {
        map = new HashMap();
    }

    public QueryTermBuilder(Map anMap) {
        if (anMap == null) {
            map = new HashMap();
        }
        else {
            map = anMap;
        }
    }
}
