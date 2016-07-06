/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.betterjr.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.betterjr.modules.account.data.CustContextInfo;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.sys.entity.WorkUserInfo;
import com.betterjr.modules.sys.security.ShiroUser;
import com.betterjr.common.entity.*;
import com.betterjr.common.security.shiro.session.RedisSessionDAO;
import com.betterjr.common.service.SpringContextHolder;

/**
 * 用户工具类
 * 
 * @author zhoucy
 */
public class UserUtils {
	private static final String SesessionIdKey = StaticThreadLocal.class.getName() + "_sessionId";
	private static final String SesessionKey = StaticThreadLocal.class.getName() + "_session";
	private static ThreadLocal<Map<String, Object>> sessionLocal = new ThreadLocal<Map<String, Object>>();

	protected static void storeThreadVar(String key, Object value) {
		Map<String, Object> map = sessionLocal.get();
		if (map == null) {
			map = new ConcurrentHashMap<String, Object>();
			sessionLocal.set(map);
		}
		if (key != null && value != null) {
			map.put(key, value);
		}
	}

	protected static Object getThreadVar(String key) {
		if (key == null) {
			return null;
		}
		Map<String, Object> map = sessionLocal.get();
		if (map == null) {
			return null;
		}
		return map.get(key);
	}

	public static void storeSessionId(String anId) {
		storeThreadVar(SesessionIdKey, anId);
	}

	public static String getSessionId() {

		//
		String id = (String) getThreadVar(SesessionIdKey);
		if (id != null) {
			return id;
		}

		// web access
		try {
			Subject subject = SecurityUtils.getSubject();
			if (subject != null) {
				Session se = subject.getSession();
				if (se != null) {
					return se.getId().toString();
				}
			}
		} catch (Exception ex) {
		}

		return null;
	}

	private static RedisSessionDAO redisSessionDAO = SpringContextHolder.getBean(RedisSessionDAO.class);

	/**
	 * 获取当前用户
	 * 
	 * @return 取不到返回 new User()
	 */
	public static WorkUserInfo getUser() {
		ShiroUser principal = getPrincipal();
		if (principal != null) {
			return principal.getUser();
		}
		return null;
	}

	public static List<String> findRuleList() {
		ShiroUser principal = getPrincipal();
		List<String> tmpList = new ArrayList();
		if (principal != null) {
			tmpList.addAll(Arrays.asList(principal.fingUserRule()));
		}
		return tmpList;
	}

	public static String getUserName() {
		WorkUserInfo principal = getUser();
		if (principal != null) {
			return principal.getName();
		}
		return null;
	}

	public static boolean isCoreUser(String label) {

		String tmpStr = DictUtils.getDictLabel(label, findOperOrg(), null);

		return BetterStringUtils.isNotBlank(tmpStr);
	}

	public static boolean isBytterUser() {
		CustContextInfo contextInfo = getOperatorContextInfo();
		if (contextInfo != null) {
			return contextInfo.isBytterUser();
		} else {
			return false;
		}
	}

	public static CustContextInfo getOperatorContextInfo() {
		ShiroUser principal = getPrincipal();
		if (principal == null) {
			return null;
		}
		Object obj = principal.getData();
		if (obj instanceof CustContextInfo) {
			return (CustContextInfo) obj;
		} else {
			return null;
		}
	}

	public static List<Long> findCustNoList() {
		CustContextInfo custContext = getOperatorContextInfo();
		if (custContext != null) {
			return custContext.findCustList();
		} else {
			return null;
		}
	}

	public static String findOperOrg() {
		CustOperatorInfo cop = getOperatorInfo();
		if (cop != null) {
			return cop.getOperOrg();
		} else {
			return "";
		}
	}

	public static CustInfo findCustInfo(Long anCustNo) {
		CustContextInfo custContext = getOperatorContextInfo();
		if (custContext != null) {
			return custContext.findCust(anCustNo);
		} else {
			return null;
		}
	}

	public static List<CustInfo> findCustInfoList() {
		CustContextInfo custContext = getOperatorContextInfo();
		if (custContext != null) {
			return custContext.findCustInfoList();
		} else {
			return null;
		}
	}

	public static CustOperatorInfo getOperatorInfo() {
		CustContextInfo custContext = getOperatorContextInfo();
		if (custContext != null) {
			return custContext.getOperatorInfo();
		} else {
			return null;
		}
	}

	/**
	 * 获取当前登录者对象
	 */
	public static ShiroUser getPrincipal() {
		Session session = UserUtils.getSession();
		if (session != null) {
			SimplePrincipalCollection col = (SimplePrincipalCollection) session
					.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
			if (col != null) {
				return (ShiroUser) col.getPrimaryPrincipal();
			}
		}
		return null;
	}

	public static Long getContactor() {
		ShiroUser principal = getPrincipal();
		if (principal != null) {
			if (UserType.PERSON_USER.equals(principal.getUserType()) == false) {

				return principal.getId();
			}
		}

		return null;
	}

	public static Session getSession() {

		//
		Object obj = getThreadVar(SesessionKey);
		Session session = (Session) obj;
		if (session != null) {
			return session;
		}
		String sessionId = (String) getThreadVar(SesessionIdKey);
		session = redisSessionDAO.doReadSession(sessionId);
		if (session != null) {
			storeThreadVar(SesessionKey, session);
			return session;
		}

		// web access
		Subject subject = SecurityUtils.getSubject();
		if (subject != null) {
			Session se = subject.getSession();
			if (se != null) {
				return se;
			}
		}
		return null;
	}

	// ============== User Cache ==============

	public static Object getCache(String key) {
		return getCache(key, null);
	}

	public static Object getCache(String key, Object defaultValue) {
		// Object obj = getCacheMap().get(key);
		Object obj = getSession().getAttribute(key);
		return obj == null ? defaultValue : obj;
	}

	public static void putCache(String key, Object value) {
		// getCacheMap().put(key, value);
		getSession().setAttribute(key, value);
	}

	public static void removeCache(String key) {
		// getCacheMap().remove(key);
		getSession().removeAttribute(key);
	}

}
