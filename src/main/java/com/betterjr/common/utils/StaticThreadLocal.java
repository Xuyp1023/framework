package com.betterjr.common.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StaticThreadLocal {

    private static final String CurrentDubboMethodParasKey = "CurrentDubboMethodParasKey";

    private static ThreadLocal<Map<String, Object>> methodParas = new ThreadLocal<Map<String, Object>>();

    protected static void storeThreadVar(String key, Object value) {
        Map<String, Object> map = methodParas.get();
        if (map == null) {
            map = new ConcurrentHashMap<String, Object>();
            methodParas.set(map);
        }
        map.put(key, value);
    }

    protected static Object getThreadVar(String key) {
        Map<String, Object> map = methodParas.get();
        if (map == null) {
            return null;
        }
        return map.get(key);
    }

    public static void storeDubboMethodParaMap(Map map) {
        StaticThreadLocal.storeThreadVar(CurrentDubboMethodParasKey, map);
    }

    public static Map getDubboMethodParaMap() {
        Object obj = StaticThreadLocal.getThreadVar(CurrentDubboMethodParasKey);
        return BTObjectUtils.castSafety(obj, Map.class);
    }

    public static void clearThreadLocal() {
        methodParas.remove();
    }

}
