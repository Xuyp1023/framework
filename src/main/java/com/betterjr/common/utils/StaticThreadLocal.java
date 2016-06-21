package com.betterjr.common.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StaticThreadLocal {
	private static final String sesessionIdKey="sessionId";
	
	private static ThreadLocal<Map<String,Object>> user=new ThreadLocal<Map<String,Object>>();
	
	protected static void storeThreadVar(String key,Object value){
		Map<String,Object> map=user.get();
		if(map==null){
			map=new ConcurrentHashMap<String,Object>();
			user.set(map);
		}
		map.put(key, value);
	}
	
	public static void storeSessionId(String anId){
		StaticThreadLocal.storeThreadVar(sesessionIdKey, anId);
	}
	
	public static String getSessionId(){
		return (String)StaticThreadLocal.getThreadVar(sesessionIdKey);
	}
	
	protected static Object getThreadVar(String key){
		return user.get().get(key);
	}

}
