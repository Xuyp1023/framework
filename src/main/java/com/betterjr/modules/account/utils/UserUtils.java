/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.betterjr.modules.account.utils;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.betterjr.modules.account.data.CustContextInfo;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.sys.entity.WorkUserInfo;
import com.betterjr.modules.sys.security.ShiroUser;
import com.betterjr.common.entity.*;

/**
 * 用户工具类
 * 
 * @author zhoucy
 */
public class UserUtils {

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

    public static String getUserName() {
        WorkUserInfo principal = getUser();
        if (principal != null) {
            return principal.getName();
        }
        return null;
    }

    public static   boolean isBytterUser() {
        CustContextInfo contextInfo = getOperatorContextInfo();
        if (contextInfo != null){
            return contextInfo.isBytterUser();
        }
        else{
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
        }
        else {
            return null;
        }
    }

    /**
     * 获取授权主要对象
     */
    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static List<Long> findCustNoList() {
        CustContextInfo custContext = getOperatorContextInfo();
        if (custContext != null) {
            return custContext.findCustList();
        }
        else {
            return null;
        }
    }

    public static String findOperOrg() {
        CustOperatorInfo cop = getOperatorInfo();
        if (cop != null) {
            return cop.getOperOrg();
        }
        else {
            return "";
        }
    }

    public static CustInfo findCustInfo(Long anCustNo) {
        CustContextInfo custContext = getOperatorContextInfo();
        if (custContext != null) {
            return custContext.findCust(anCustNo);
        }
        else {
            return null;
        }
    }

    public static List<CustInfo> findCustInfoList() {
        CustContextInfo custContext = getOperatorContextInfo();
        if (custContext != null) {
            return custContext.findCustInfoList();
        }
        else {
            return null;
        }
    }

    public static CustOperatorInfo getOperatorInfo() {
        CustContextInfo custContext = getOperatorContextInfo();
        if (custContext != null) {
            return custContext.getOperatorInfo();
        }
        else {
            return null;
        }
    }

    /**
     * 获取当前登录者对象
     */
    public static ShiroUser getPrincipal() {
        try {
            Subject subject = SecurityUtils.getSubject();
            ShiroUser principal = (ShiroUser) subject.getPrincipal();
            if (principal != null) {
                return principal;
            }
            // subject.logout();
        }
        catch (UnavailableSecurityManagerException e) {

        }
        catch (InvalidSessionException e) {

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
        try {
            Subject subject = SecurityUtils.getSubject();
            Session session = subject.getSession(false);
            if (session == null) {
                session = subject.getSession();
            }
            if (session != null) {
                return session;
            }
            // subject.logout();
        }
        catch (InvalidSessionException e) {

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
