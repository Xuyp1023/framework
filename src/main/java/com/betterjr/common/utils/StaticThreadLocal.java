package com.betterjr.common.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.shiro.session.Session;

public class StaticThreadLocal {
	private static final String sesessionIdKey=StaticThreadLocal.class.getName()+"_sessionId";
	private static final String sesessionKey=StaticThreadLocal.class.getName()+"_session";
	private static final String principalKey=StaticThreadLocal.class.getName()+"_principal";
	
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
	
	public static void storeSession(Session sess){
		StaticThreadLocal.storeThreadVar(sesessionKey, sess);
	}
	
	public static Session getSession(){
		return (Session)StaticThreadLocal.getThreadVar(sesessionKey);
	}
	
	public static void storePrincipal(Object principal){
		StaticThreadLocal.storeThreadVar(principalKey, principal);
	}
	
	public static Object getPrincipal(){
		return StaticThreadLocal.getThreadVar(principalKey);
	}
	
	protected static Object getThreadVar(String key){
		return user.get().get(key);
	}
}
