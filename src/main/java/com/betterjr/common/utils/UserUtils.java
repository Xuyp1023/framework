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
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.betterjr.modules.account.data.CustContextInfo;
import com.betterjr.modules.account.entity.CustCertInfo;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.sys.security.ShiroUser;
import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.data.PlatformBaseRuleType;
import com.betterjr.common.data.UserType;
import com.betterjr.common.data.WebAccessType;
import com.betterjr.common.data.WorkUserInfo;
import com.betterjr.common.entity.*;
import com.betterjr.common.security.shiro.session.RedisSessionDAO;
import com.betterjr.common.service.SpringContextHolder;

/**
 * 用户工具类
 * 
 * @author zhoucy
 */
public class UserUtils {
    private static final String SesessionIdKey = UserUtils.class.getName() + "_sessionId";
    private static final String SesessionKey = UserUtils.class.getName() + "_session";
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
        clearThreadLocal();
        storeThreadVar(SesessionIdKey, anId);
    }
    
    public static void clearThreadLocal(){
        sessionLocal.remove();
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
        }
        catch (Exception ex) {
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

    public static boolean isBytterUser() {
        CustContextInfo contextInfo = getOperatorContextInfo();
        if (contextInfo != null) {
            return contextInfo.isBytterUser();
        }
        else {
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
        Session session = UserUtils.getSession();
        if (session != null) {
            SimplePrincipalCollection col = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
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

    /**
     * 是否是核心企业客户
     * 
     * @return
     */
    public static boolean coreUser() {

        return ShiroUser.coreUser(getPrincipal());
    }

    /**
     * 是否是经销商客户
     * 
     * @return
     */
    public static boolean sellerUser() {

        return ShiroUser.sellerUser(getPrincipal());
    }

    /**
     * 是否是供应商客户
     * 
     * @return
     */
    public static boolean supplierUser() {

        return ShiroUser.supplierUser(getPrincipal());
    }
    
    private static boolean custRuleCheck(CustCertInfo anCertInfo, PlatformBaseRuleType anBaseRule) {
        if (anCertInfo == null) {
            return false;
        }
        String tempInnerRules = anCertInfo.getRuleList();
        if (BetterStringUtils.isBlank(tempInnerRules)) {
            return false;
        }
        List<PlatformBaseRuleType> innerRules = PlatformBaseRuleType.checkList(tempInnerRules);
        if (innerRules == null) {
            return false;
        }
        return innerRules.contains(anBaseRule);
    }

    public static boolean coreCustomer(CustCertInfo anCertInfo) {
        return custRuleCheck(anCertInfo, PlatformBaseRuleType.CORE_USER);
    }

    public static boolean sellerCustomer(CustCertInfo anCertInfo) {
        return custRuleCheck(anCertInfo, PlatformBaseRuleType.SELLER_USER);
    }

    public static boolean supplierCustomer(CustCertInfo anCertInfo) {
        return custRuleCheck(anCertInfo, PlatformBaseRuleType.SUPPLIER_USER);
    }

    public static boolean platformCustomer(CustCertInfo anCertInfo) {
        return custRuleCheck(anCertInfo, PlatformBaseRuleType.PLATFORM_USER);
    }

    public static boolean factorCustomer(CustCertInfo anCertInfo) {
        return custRuleCheck(anCertInfo, PlatformBaseRuleType.FACTOR_USER);
    }

    /**
     * 操作员内部角色，列表，定义基础的角色！参见枚举类PlatformBaseRuleType，定义在表T_CUST_CERTINFO.C_RULE_LIST中
     * 
     * @return
     */
    public static List<PlatformBaseRuleType> findInnerRules() {
        ShiroUser user = getPrincipal();
        if (user != null) {
            return user.getInnerRules();
        }
        return new ArrayList(0);
    }

    /**
     * 是否是资金提供方
     * 
     * @return
     */
    public static boolean factorUser() {

        return ShiroUser.factorUser(getPrincipal());
    }

    /**
     * 是否是平台自己的操作员
     * 
     * @return
     */
    public static boolean platformUser() {

        return ShiroUser.platformUser(getPrincipal());
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

    private static boolean checkWebAccessList(WebAccessType[] anArray , String[] anReturn , WebAccessType... anWatArr) {
        for (WebAccessType outWt : anWatArr) {
            for (WebAccessType tmpWt : anArray) {
                if (tmpWt == outWt) {
                    anReturn[0] = tmpWt.getPassType();
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isMobileLogin(){
        ShiroUser su = getPrincipal();
        if (su == null) {
            
            return false;
        }
        else{
            return su.isMobileLogin();
        }
    }
    /**
     * 检查是否有访问权限；如果没有定义表示全部可以访问，如果定义了手机访问，只能是手机访问；如果定义了PC访问，只能是PC访问
     * 
     * @param anMetaData
     * @return
     */
    public static boolean checkAccess(MetaData anMetaData, String anPass, String[] anResultMsg) {
        if (anMetaData == null) {
            
            return true;
        }
        WebAccessType[] watArray = anMetaData.acccessType();
        String[] valueReturn = new String[1];

        if (checkWebAccessList(watArray, valueReturn, WebAccessType.ALL)) {
            
            return true;
        }
        else {
            ShiroUser su = getPrincipal();
            if (su == null) {
                
                return true;
            }
            if (su.isMobileLogin()) {
                if (checkWebAccessList(watArray, valueReturn, WebAccessType.ORG_MOBILE_PASS, WebAccessType.PERSON_MOBILE_PASS)) {
                    anResultMsg[0] = "交易密码错误！";
                    return su.checkPass(valueReturn[0], anPass);
                }
                
                return checkWebAccessList(watArray, valueReturn, WebAccessType.ORG_MOBILE, WebAccessType.PERSON_MOBILE);
            }
            else {
                if (checkWebAccessList(watArray, valueReturn, WebAccessType.ORG_PC_PASS, WebAccessType.PERSON_PC_PASS)){
                    
                    anResultMsg[0] = "交易密码错误！";
                    return su.checkPass(valueReturn[0], anPass);
                }
                
                return checkWebAccessList(watArray, valueReturn, WebAccessType.ORG_PC, WebAccessType.PERSON_PC);
            }
        }
    }
    
    
    public static void StoreTempDataForTest(ShiroUser user){
        Session session = new SimpleSession();
        SimplePrincipalCollection col = new SimplePrincipalCollection();
        session.setAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY,col);
        col.add(user, "test");
        storeThreadVar(SesessionKey,session);
    }

}
