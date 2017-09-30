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
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.data.PlatformBaseRuleType;
import com.betterjr.common.data.UserType;
import com.betterjr.common.data.WebAccessType;
import com.betterjr.common.data.WorkUserInfo;
import com.betterjr.common.security.shiro.session.RedisSessionDAO;
import com.betterjr.common.service.SpringContextHolder;
import com.betterjr.modules.account.data.CustContextInfo;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.cert.entity.CustCertInfo;
import com.betterjr.modules.cert.entity.CustCertRule;
import com.betterjr.modules.sys.security.ShiroUser;

/**
 * 用户工具类
 *
 * @author zhoucy
 */
public class UserUtils {
    private static Logger logger = LoggerFactory.getLogger(UserUtils.class);

    private static final String SesessionIdKey = UserUtils.class.getName() + "_sessionId";
    private static final String SesessionKey = UserUtils.class.getName() + "_session";
    private static final String REQUEST_ADDRESS_KEY = UserUtils.class.getName() + "_requestAddressKey";
    private static ThreadLocal<Map<String, Object>> sessionLocal = new ThreadLocal<Map<String, Object>>();

    protected static void storeThreadVar(final String key, final Object value) {
        Map<String, Object> map = sessionLocal.get();
        if (map == null) {
            map = new ConcurrentHashMap<String, Object>();
            sessionLocal.set(map);
        }
        if (key != null && value != null) {
            map.put(key, value);
        }
    }

    protected static Object getThreadVar(final String key) {
        if (key == null) {
            return null;
        }
        final Map<String, Object> map = sessionLocal.get();
        if (map == null) {
            return null;
        }
        return map.get(key);
    }

    public static String getRequestIp() {

        return (String) getThreadVar(REQUEST_ADDRESS_KEY);
    }

    public static void storeRequestIp(final String anRequstIp) {

        storeThreadVar(REQUEST_ADDRESS_KEY, anRequstIp);
    }

    public static void storeSessionId(final String anId) {
        clearThreadLocal();
        storeThreadVar(SesessionIdKey, anId);
    }

    public static void clearThreadLocal() {
        sessionLocal.remove();
    }

    public static String getSessionId() {

        //
        final String id = (String) getThreadVar(SesessionIdKey);
        if (id != null) {
            return id;
        }

        // web access
        try {
            final Subject subject = SecurityUtils.getSubject();
            if (subject != null) {
                final Session se = subject.getSession();
                if (se != null) {
                    return se.getId().toString();
                }
            }
        }
        catch (final Exception ex) {
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
        final ShiroUser principal = getPrincipal();
        if (principal != null) {
            return principal.getUser();
        }
        return null;
    }

    public static CustInfo getDefCustInfo() {
        return Collections3.getFirst(findCustInfoList());
    }

    /**
     * 获取当前证书信息
     * 
     * @return
     */
    public static CustCertInfo getCertInfo() {
        final ShiroUser principal = getPrincipal();
        if (principal != null) {
            return principal.getCretInfo();
        }
        return null;
    }

    public static PlatformBaseRuleType getUserRole() {
        final ShiroUser principal = getPrincipal();
        if (principal != null) {
            return principal.getInnerRules().iterator().next();
        }
        return null;
    }

    /**
     * 获取机构名称
     * 
     * @return
     */
    public static String getOrgName() {
        final CustCertInfo certInfo = getCertInfo();
        if (certInfo != null) {
            return certInfo.getCustName();
        }
        return null;
    }

    /**
     * 获取OperOrg
     * 
     * @return
     */
    public static String getOperOrg() {
        final CustCertInfo certInfo = getCertInfo();
        if (certInfo != null) {
            return certInfo.getOperOrg();
        }
        return null;
    }

    /**
     * 找当前用户的权限列表
     * 
     * @return
     */
    public static List<String> findRuleList() {
        final ShiroUser principal = getPrincipal();
        final List<String> tmpList = new ArrayList();
        if (principal != null) {
            tmpList.addAll(Arrays.asList(principal.fingUserRule()));
        }
        return tmpList;
    }

    /**
     * 取用户名
     * 
     * @return
     */
    public static String getUserName() {
        final WorkUserInfo principal = getUser();
        if (principal != null) {
            return principal.getName();
        }
        return null;
    }

    public static boolean isBytterUser() {
        final CustContextInfo contextInfo = getOperatorContextInfo();
        if (contextInfo != null) {
            return contextInfo.isBytterUser();
        }
        else {
            return false;
        }
    }

    public static CustContextInfo getOperatorContextInfo() {
        final ShiroUser principal = getPrincipal();
        if (principal == null) {
            return null;
        }
        final Object obj = principal.getData();
        if (obj instanceof CustContextInfo) {
            return (CustContextInfo) obj;
        }
        else {
            return null;
        }
    }

    public static List<Long> findCustNoList() {
        final CustContextInfo custContext = getOperatorContextInfo();
        if (custContext != null) {
            return custContext.findCustList();
        }
        else {
            return null;
        }
    }

    public static String findOperOrg() {
        final CustOperatorInfo cop = getOperatorInfo();
        if (cop != null) {
            return cop.getOperOrg();
        }
        else {
            return "";
        }
    }

    public static CustInfo findCustInfo(final Long anCustNo) {
        final CustContextInfo custContext = getOperatorContextInfo();
        if (custContext != null) {
            return custContext.findCust(anCustNo);
        }
        else {
            return null;
        }
    }

    public static List<CustInfo> findCustInfoList() {
        final CustContextInfo custContext = getOperatorContextInfo();
        if (custContext != null) {
            return custContext.findCustInfoList();
        }
        else {
            return null;
        }
    }

    public static CustOperatorInfo getOperatorInfo() {
        final CustContextInfo custContext = getOperatorContextInfo();
        if (custContext != null) {
            return custContext.getOperatorInfo();
        }
        else {
            final CustOperatorInfo operator = new CustOperatorInfo();
            operator.setId(-1L);
            operator.setName("system");
            operator.setOperOrg("system");
            return operator;
        }
    }

    /**
     * 获取当前登录者对象
     */
    public static ShiroUser getPrincipal() {
        final Session session = UserUtils.getSession();
        if (session != null) {
            final SimplePrincipalCollection col = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (col != null) {
                return (ShiroUser) col.getPrimaryPrincipal();
            }
        }
        return null;
    }

    public static Long getContactor() {
        final ShiroUser principal = getPrincipal();
        if (principal != null) {
            if (UserType.PERSON_USER.equals(principal.getUserType()) == false) {

                return principal.getId();
            }
        }

        return null;
    }

    public static Session getSession() {
        final Object obj = getThreadVar(SesessionKey);
        Session session = (Session) obj;
        if (session != null) {
            return session;
        }
        final String sessionId = (String) getThreadVar(SesessionIdKey);
        session = redisSessionDAO.doReadSession(sessionId);
        if (session != null) {
            storeThreadVar(SesessionKey, session);
            return session;
        }

        try {
            // web access
            final Subject subject = SecurityUtils.getSubject();
            if (subject != null) {
                final Session se = subject.getSession();
                if (se != null) {
                    return se;
                }
            }
        }
        catch (final Exception e) {
            logger.error("获取session出错：", e);
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

    private static boolean custRuleCheck(final CustCertInfo anCertInfo, final PlatformBaseRuleType anBaseRule) {
        if (anCertInfo == null) {
            return false;
        }
        final List<CustCertRule> certRuleList = anCertInfo.getCertRuleList();
        if (Collections3.isEmpty(certRuleList)) {
            return false;
        }
        final List<PlatformBaseRuleType> innerRules = PlatformBaseRuleType.checkList(certRuleList);
        if (innerRules == null) {
            return false;
        }
        return innerRules.contains(anBaseRule);
    }

    public static boolean coreCustomer(final CustCertInfo anCertInfo) {
        return custRuleCheck(anCertInfo, PlatformBaseRuleType.CORE_USER);
    }

    public static boolean sellerCustomer(final CustCertInfo anCertInfo) {
        return custRuleCheck(anCertInfo, PlatformBaseRuleType.SELLER_USER);
    }

    public static boolean supplierCustomer(final CustCertInfo anCertInfo) {
        return custRuleCheck(anCertInfo, PlatformBaseRuleType.SUPPLIER_USER);
    }

    public static boolean platformCustomer(final CustCertInfo anCertInfo) {
        return custRuleCheck(anCertInfo, PlatformBaseRuleType.PLATFORM_USER);
    }

    public static boolean factorCustomer(final CustCertInfo anCertInfo) {
        return custRuleCheck(anCertInfo, PlatformBaseRuleType.FACTOR_USER);
    }

    /**
     * 操作员内部角色，列表，定义基础的角色！参见枚举类PlatformBaseRuleType，定义在表T_CUST_CERTINFO.C_RULE_LIST中
     *
     * @return
     */
    public static List<PlatformBaseRuleType> findInnerRules() {
        final ShiroUser user = getPrincipal();
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

    public static Object getCache(final String key) {
        return getCache(key, null);
    }

    public static Object getCache(final String key, final Object defaultValue) {
        // Object obj = getCacheMap().get(key);
        final Object obj = getSession().getAttribute(key);
        return obj == null ? defaultValue : obj;
    }

    public static void putCache(final String key, final Object value) {
        // getCacheMap().put(key, value);
        getSession().setAttribute(key, value);
    }

    public static void removeCache(final String key) {
        // getCacheMap().remove(key);
        getSession().removeAttribute(key);
    }

    private static boolean checkWebAccessList(final WebAccessType[] anArray, final String[] anReturn, final WebAccessType... anWatArr) {
        for (final WebAccessType outWt : anWatArr) {
            for (final WebAccessType tmpWt : anArray) {
                if (tmpWt == outWt) {
                    anReturn[0] = tmpWt.getPassType();
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isMobileLogin() {
        final ShiroUser su = getPrincipal();
        if (su == null) {

            return false;
        }
        else {
            return su.isMobileLogin();
        }
    }

    /**
     * 检查是否有访问权限；如果没有定义表示全部可以访问，如果定义了手机访问，只能是手机访问；如果定义了PC访问，只能是PC访问
     *
     * @param anMetaData
     * @return
     */
    public static boolean checkAccess(final MetaData anMetaData, final String anPass, final String[] anResultMsg) {
        if (anMetaData == null) {

            return true;
        }
        final WebAccessType[] watArray = anMetaData.acccessType();
        final String[] valueReturn = new String[1];

        if (checkWebAccessList(watArray, valueReturn, WebAccessType.ALL)) {

            return true;
        }
        else {
            final ShiroUser su = getPrincipal();
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
                if (checkWebAccessList(watArray, valueReturn, WebAccessType.ORG_PC_PASS, WebAccessType.PERSON_PC_PASS)) {

                    anResultMsg[0] = "交易密码错误！";
                    return su.checkPass(valueReturn[0], anPass);
                }

                return checkWebAccessList(watArray, valueReturn, WebAccessType.ORG_PC, WebAccessType.PERSON_PC);
            }
        }
    }

    public static void StoreTempDataForTest(final ShiroUser user) {
        final Session session = new SimpleSession();
        final SimplePrincipalCollection col = new SimplePrincipalCollection();
        session.setAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY, col);
        col.add(user, "test");
        storeThreadVar(SesessionKey, session);
    }

    /**
     * 检查操作是否包含此custNo
     * 
     * @param anCustNo
     * @return
     */
    public static boolean containsCustNo(final Long anCustNo) {
        return findCustNoList().contains(anCustNo);
    }

}
