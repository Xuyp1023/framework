package com.betterjr.modules.account.data;

import java.io.Serializable;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterjr.common.security.KeyReader;
import com.betterjr.common.security.SignHelper;
import com.betterjr.common.utils.CacheUtils;
import com.betterjr.common.utils.JedisUtils;
import com.betterjr.common.web.WorkSessionContext;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.entity.SaleTradeAccountInfo;
import com.betterjr.modules.cert.entity.CustCertInfo;


/**
 * 当前客户的上下文信息,打通前端和后端的信息
 *
 * @author zhoucy
 *
 */
public class CustContextInfo implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(CustContextInfo.class);

    public static final String custContextPrefix = "cust::context::";

    public static final String CACHE_CUST_CONTEXT_MAP = "custContextMap";

    public static Map<String, CustContextInfo> contextMap = (Map<String, CustContextInfo>) CacheUtils.get(CACHE_CUST_CONTEXT_MAP);
    private static final long serialVersionUID = -3157823563301650231L;
    /**
     * 证书信息
     */
    private final CustCertInfo certInfo;

    /**
     * 后端连接的服务会话信息
     */
    private final String backSessionID;

    /**
     * 前端连接的会话信息
     */
    private String beforeSessionID;

    /**
     * 当前操作员信息
     */
    private CustOperatorInfo contactInfo;

    private boolean valid = false;

    private final String token;

    private boolean logined = false;

    private boolean bytterUser = false;

    public boolean isBytterUser() {
        return this.bytterUser;
    }

    public void setBytterUser(final boolean anBytterUser) {
        this.bytterUser = anBytterUser;
    }

    private final Map<Long, CustInfo> cutInfoMap = new HashMap();
    private final Map<String, SaleTradeAccountInfo> tradeAccoMap = new HashMap();

    public boolean isLogined() {
        return logined;
    }

    /**
     *
     * 登录，并处理相关登录信息
     *
     * @param 客户信息
     * @return 出参说明，结果条件
     * @throws 异常情况
     */
    public void login(final List<CustInfo> anCustList) {
        logined = true;
        for (final CustInfo cInfo : anCustList) {
            this.cutInfoMap.put(cInfo.getCustNo(), cInfo);
        }
    }

    public void addNewCustInfo(final CustInfo cInfo) {

        this.cutInfoMap.put(cInfo.getCustNo(), cInfo);
    }

    public void addNewTradeAccount(final SaleTradeAccountInfo anTradeAcco) {

        this.tradeAccoMap.put(anTradeAcco.getTradeAccount(), anTradeAcco);
    }

    public void addTradeAccount(final List<SaleTradeAccountInfo> anTradeAccoList) {
        for (final SaleTradeAccountInfo cInfo : anTradeAccoList) {

            this.tradeAccoMap.put(cInfo.getTradeAccount(), cInfo);
        }
    }

    // 获得操作员所操作的所有客户信息
    public List<Long> findCustList() {
        final List list = new ArrayList();
        list.addAll(this.cutInfoMap.keySet());

        return list;
    }

    // 获得操作员所操作的所有客户信息
    public List<CustInfo> findCustInfoList() {
        final List list = new ArrayList();
        list.addAll(this.cutInfoMap.values());

        return list;
    }

    public CustInfo findCust(final Long anCustNo) {

        return this.cutInfoMap.get(anCustNo);
    }

    public SaleTradeAccountInfo findTradeAccount(final String anTradeAccount) {

        return this.tradeAccoMap.get(anTradeAccount);
    }

    private long lastLoginTime = 0L;

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public String getToken() {
        return token;
    }

    public boolean isValid() {
        return valid;
    }

    // 客户从后台登录到前台登录的时间间隔，如果超时，则不能在前端登录
    public boolean isBeforeLoginValid(final int anTime) {

        return this.lastLoginTime + (anTime * 10000) > System.currentTimeMillis();
    }

    public void setValid(final boolean valid) {
        this.valid = valid;
    }

    public CustOperatorInfo getOperatorInfo() {
        return contactInfo;
    }

    public void setOperatorInfo(final CustOperatorInfo contactInfo) {
        this.valid = true;
        this.contactInfo = contactInfo;
        this.lastLoginTime = System.currentTimeMillis();
    }

    public HttpSession getBeforeSession() {
        return WorkSessionContext.getSession(beforeSessionID);
    }

    public void setBeforeSession(final HttpSession anBeforeSession) {
        WorkSessionContext.addSession(anBeforeSession);
        this.beforeSessionID = anBeforeSession.getId();
    }

    public CustCertInfo getCertInfo() {
        return certInfo;
    }

    public HttpSession getBackSession() {

        return WorkSessionContext.getSession(backSessionID);
    }

    /**
     *
     * 使用对方的公钥验证签名信息
     *
     * @param 原始内容
     * @param1 签名信息
     * @param2 证书公钥序列化内容
     * @return 验证成功返回True, 否则返回False
     * @throws 异常情况
     */
    public boolean verifySign(final String anContent, final String anSign) {
        final Certificate workCert = KeyReader.fromCerBase64String(this.certInfo.getCertInfo());
        final boolean bb = SignHelper.verifySign(anContent, anSign, workCert);
        logger.info("verifySign result is " + bb);

        return bb;
    }

    /**
     *
     * 根据令牌，获取客户上下文信息
     *
     * @param 客户提供的令牌
     * @return 获取的上下文信息，如果没有上下文信息，将创建一个空的上下文信息，状态为无效
     * @throws 异常情况
     */
    public static CustContextInfo findCustContextInfo(final String anToken) {
        //Map<String, CustContextInfo> contextMap = (Map<String, CustContextInfo>) CacheUtils.get(CACHE_CUST_CONTEXT_MAP);
        CustContextInfo custContext = JedisUtils.getObject(custContextPrefix + anToken);

        if (custContext == null) {
            logger.warn("the token " + anToken+", context info is null");
            custContext = new CustContextInfo();
        }
        else{
            logger.info( "this is token =" + anToken +", custContext is " + custContext.getOperatorInfo());
        }

        return custContext;
    }

    public void invalid() {
        this.valid = false;
        // Map<String, CustContextInfo> contextMap = (Map<String, CustContextInfo>) CacheUtils.get(CACHE_CUST_CONTEXT_MAP);
        if (contextMap != null) {
            contextMap.remove(token);
        }
        try {
            HttpSession session = WorkSessionContext.getSession(this.backSessionID);
            if (session != null) {
                session.invalidate();
            }
            session = WorkSessionContext.getSession(this.beforeSessionID);
            if (session != null) {
                session.invalidate();
            }
        }
        catch (final Exception ex) {

        }
    }

    /**
     *
     * 保存上下文信息到缓存
     *
     * @param 需要保存的上下文信息
     * @return 无
     * @throws 异常情况
     */
    public static void putCustContextInfo(final CustContextInfo anCustContext) {
        putCustContextInfo(anCustContext.getToken(), anCustContext);
    }

    public static void putCustContextInfo(final String anToken, final CustContextInfo anCustContext) {
        //  Map<String, CustContextInfo> contextMap = (Map<String, CustContextInfo>) CacheUtils.get(CACHE_CUST_CONTEXT_MAP);
        //        if (contextMap == null) {
        //            contextMap = new ConcurrentHashMap();
        //            CacheUtils.put(CACHE_CUST_CONTEXT_MAP, contextMap);
        //        }
        //        contextMap.put(anCustContext.getToken(), anCustContext);
        JedisUtils.setObject(custContextPrefix + anToken, anCustContext, 0);
    }

    public CustContextInfo() {
        this.token = "";
        this.certInfo = null;
        this.backSessionID = null;
    }

    public CustContextInfo(final String anToken, final CustCertInfo anCertInfo, final HttpSession anBackSess) {
        this.token = anToken;
        this.certInfo = anCertInfo;
        if (anBackSess != null) {
            WorkSessionContext.addSession(anBackSess);
            this.backSessionID = anBackSess.getId();
        }
        else{
            this.backSessionID = null;
        }
    }
}
