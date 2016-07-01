package com.betterjr.common.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.shiro.session.Session;

public class StaticThreadLocal {
	private static final String SesessionIdKey=StaticThreadLocal.class.getName()+"_sessionId";
	private static final String SesessionKey=StaticThreadLocal.class.getName()+"_session";
	private static final String PrincipalKey=StaticThreadLocal.class.getName()+"_principal";
	private static final String CurrentDubboMethodParasKey="CurrentDubboMethodParasKey";
	
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
		StaticThreadLocal.storeThreadVar(SesessionIdKey, anId);
	}
	
	public static String getSessionId(){
		Object obj=StaticThreadLocal.getThreadVar(SesessionIdKey);
		return BTObjectUtils.castSafety(obj, String.class);
	}
	
	public static void storeSession(Session sess){
		StaticThreadLocal.storeThreadVar(SesessionKey, sess);
	}
	
	public static Session getSession(){
		Object obj=StaticThreadLocal.getThreadVar(SesessionKey);
		return BTObjectUtils.castSafety(obj, Session.class);
	}
	
	public static void storePrincipal(Object principal){
		StaticThreadLocal.storeThreadVar(PrincipalKey, principal);
	}
	
	public static Object getPrincipal(){
		return StaticThreadLocal.getThreadVar(PrincipalKey);
	}
	
	public static void storeDubboMethodParaMap(Map map){
		StaticThreadLocal.storeThreadVar(CurrentDubboMethodParasKey, map);
	}
	
	public static Map getDubboMethodParaMap(){
		Object obj=StaticThreadLocal.getThreadVar(CurrentDubboMethodParasKey);
		return BTObjectUtils.castSafety(obj,Map.class);
	}
	
	protected static Object getThreadVar(String key){
		return user.get().get(key);
	}
}
