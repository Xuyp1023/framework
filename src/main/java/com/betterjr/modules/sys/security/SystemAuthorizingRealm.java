package com.betterjr.modules.sys.security;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.betterjr.common.data.UserType;
import com.betterjr.common.security.KeyReader;
import com.betterjr.common.security.SecurityConstants;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.Digests;
import com.betterjr.common.utils.Encodes;
import com.betterjr.common.web.Servlets;
import com.betterjr.modules.account.data.CustContextInfo;
import com.betterjr.modules.account.dubboclient.CustCertDubboClientService;
import com.betterjr.modules.account.dubboclient.CustLoginDubboClientService;
import com.betterjr.modules.account.dubboclient.CustOperatorDubboClientService;
import com.betterjr.modules.account.dubboclient.CustPassDubboClientService;
import com.betterjr.modules.account.entity.CustCertInfo;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.entity.CustPassInfo;

public class SystemAuthorizingRealm extends AuthorizingRealm {
    private static final Logger log = LoggerFactory.getLogger(SystemAuthorizingRealm.class);

    private static final int INTERATIONS = 1024;
    private static final int SALT_SIZE = 20;
    private static final String ALGORITHM = "SHA-256";
    private CustCertInfo certInfo = null;
   
    private CustCertDubboClientService certService;

    
    private CustLoginDubboClientService userService;

    
    private CustOperatorDubboClientService operatorService;

    
    private CustPassDubboClientService passService;

    /**
     * 给ShiroDbRealm提供编码信息，用于密码密码比对 描述
     */
    public SystemAuthorizingRealm() {
        super();
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(ALGORITHM);
        matcher.setHashIterations(INTERATIONS);

        setCredentialsMatcher(matcher);
    }

    public boolean supports(AuthenticationToken token) {

        return true;
    }

    /**
     * 认证回调函数, 登录时调用.
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {

        CustOperatorInfo user = null;
        String passWD = null;
        String saltStr = null;
        Object workData = null;
        CustContextInfo contextInfo = null;
        boolean mobileLogin = false;
        log.warn("this work for doGetAuthenticationInfo");
        try {
            if (authcToken instanceof CaptchaUsernamePasswordToken) {
                CaptchaUsernamePasswordToken token = (CaptchaUsernamePasswordToken) authcToken;
                // 数字证书认证
                try {
                    System.out.println("this is request :" + token.getRequest());
                    X509Certificate cert = findCertificate(token.getRequest(), token.getResponse());
                    if (cert != null) {
                        checkValid((HttpServletRequest) token.getRequest(), cert);
                    }
                    else {
                        throw new AuthenticationException("the request has X509Certificate");
                        // throw new AuthenticationException("数字证书验证失败");
                    }
                }
                catch (AuthenticationException e) {
                    throw e;
                    // throw new AuthenticationException("数字证书验证失败");
                }
                catch (Exception e) {
                    throw new AuthenticationException("数字证书验证失败");
                }

                // 如果未登录，处理验证码
                /*
                 * if (useCaptcha) { String parm = token.getCaptcha(); }
                 */
                // user = operatorService.findCustOperatorByIndentInfo(token.getIdentType(), token.getUsername());
                // user = operatorService.findCustOperatorByIndentInfo("0", "522228199110010632");
                user = operatorService.findCustOperatorByOperCode(certInfo.getOperOrg(), token.getUsername());
                if (user != null) {
                    log.warn(user.toString());
                    CustPassInfo passInfo = passService.getOperaterPassByCustNo(user.getId());
                    BTAssert.notNull(passInfo.getCustNo(), "找不到操作员密码");
                    // 临时锁定
                    if (passInfo.validLockType()) {

                        throw new DisabledAccountException("操作员密码被锁定，请稍后再试");
                    }
                    mobileLogin = token.isMobileLogin();
                    passWD = passInfo.getPasswd();
                    saltStr = passInfo.getPassSalt();
                    contextInfo = userService.saveFormLogin(user);
                }
            }
            else if (authcToken instanceof BetterjrSsoToken) {
                BetterjrSsoToken ssoToken = (BetterjrSsoToken) authcToken;
                String ticket = (String) ssoToken.getTicket();
                contextInfo = userService.saveTokenLogin(ticket);
                user = contextInfo.getOperatorInfo();
                // 用证书登录方式，控制在拜特资金管理系统，原则上不存在过期或无效的问题
                // 统一使用Form形式的验证！
                saltStr = "985a44369b063938a6a7";
                ssoToken.setUsername(user.getName());
                passWD = "8438d772e1eac7d8e57aecaae5fb0b8c2e369283cbe31857d89dc87430160a2b";
            }
            workData = contextInfo;

            if (user != null) {
                log.warn(user.toString());
                if (user.getStatus().equals("1") == false) {
                    throw new DisabledAccountException("操作员被要求暂停业务或者已经被注销");
                }

                UserType ut = UserType.OPERATOR_USER;
                // 如果是默认操作员，则是管理员
                if (user.getDefOper()) {
                    ut = UserType.OPERATOR_ADMIN;
                }

                ShiroUser shiroUser = new ShiroUser(ut, user.getId(), user.getName(), user,this.certInfo.getInnerRules(), mobileLogin, workData);
                byte[] salt = Encodes.decodeHex(saltStr);

                // 这里可以缓存认证
                log.warn("ready invoke SimpleAuthenticationInfo");
                return new SimpleAuthenticationInfo(shiroUser, passWD, ByteSource.Util.bytes(salt), getName());
            }
            else {
                return null;
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    private void checkValid(HttpServletRequest request, X509Certificate anCert) {
//        CustCertInfo certInfo = (CustCertInfo) request.getSession().getAttribute(SecurityConstants.CUST_CERT_INFO);
//        if (certInfo == null) {
            certInfo = certService.checkValidity(anCert);
            request.getSession().setAttribute(SecurityConstants.CUST_CERT_INFO, certInfo);
//        }
///        AccessClientImpl.set(certInfo);
        this.certInfo = certInfo;
    }

    protected CustContextInfo formLogin(CustOperatorInfo custOperatorInfo) {
        String token = Servlets.getSession().getId();
        CustContextInfo contextInfo = new CustContextInfo(token, null, null);
        CustContextInfo.putCustContextInfo(contextInfo);
        CustOperatorInfo tmpInfo = custOperatorInfo;
        // tmpInfo.setName("欧尼");
        // tmpInfo.setOperCode("9857");
        // tmpInfo.setOperOrg("aXXAAAFWQWEQWEQWEEQWEXXXXddpppp");
        tmpInfo.initStatus();
        contextInfo.setOperatorInfo(tmpInfo);
        List<CustInfo> custList = new ArrayList();
        contextInfo.login(custList);

        // 增加交易账户信息
        //contextInfo.addTradeAccount(new ArrayList());

        // todo;登录信息和状态暂时不处理
        return contextInfo;
    }

    /**
     * 获取数字证书
     */
    private X509Certificate findCertificate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        X509Certificate[] certs = (X509Certificate[]) request.getAttribute(SecurityConstants.CERT_ATTR_CER);
        X509Certificate tmpCerts = null;
        Enumeration<String> headers=request.getHeaderNames();
        Enumeration<String> attrs=request.getAttributeNames();
        while(headers.hasMoreElements()){
        	String headKey=headers.nextElement();
        	log.debug("request heads:"+headKey+"="+request.getHeader(headKey));
        }
        while(attrs.hasMoreElements()){
        	String attrKey=attrs.nextElement();
        	log.debug("request attrs:"+attrKey+"="+request.getAttribute(attrKey));
        }
        
        
        
        if (certs == null) {
            String tmpStr = request.getHeader("X-SSL-Client-Cert");
            if (BetterStringUtils.isNotBlank(tmpStr)) {
                //log.info("the request Cert is :" + tmpStr);
                tmpCerts = (X509Certificate) KeyReader.fromCerBase64String(
                        tmpStr.replaceFirst("-----BEGIN CERTIFICATE-----", "").replaceFirst("-----END CERTIFICATE-----", "").replaceAll("\t", ""));
            }
        }
        else {
            tmpCerts = certs[0];
        }

        return tmpCerts;
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.warn("this work for doGetAuthorizationInfo 1231");
        Collection<?> collection = principals.fromRealm(getName());
        if (Collections3.isEmpty(collection)) {
            return null;
        }

        ShiroUser shiroUser = (ShiroUser) collection.iterator().next();

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        for (String userRule : shiroUser.fingUserRule()) {
            log.warn("this use attach rule is :" + userRule);
            info.addRole(userRule);
        }

        return info;
    }

    public static class HashPassword {
        public String salt;
        public String password;
    }

    public HashPassword encrypt(String plainText) {
        HashPassword result = new HashPassword();
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        result.salt = Encodes.encodeHex(salt);

        byte[] hashPassword = Digests.sha256(plainText.getBytes(), salt, INTERATIONS);
        result.password = Encodes.encodeHex(hashPassword);

        return result;
    }

    public static String findEncrypt(String plainText, String anSalt) {
        byte[] salt = Encodes.decodeHex(anSalt);
        byte[] hashPassword = Digests.sha256(plainText.getBytes(), salt, INTERATIONS);

        return Encodes.encodeHex(hashPassword);
    }

    /**
     * 更新用户授权信息缓存.
     */
    public void clearCachedAuthorizationInfo(String principal) {
        SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
        clearCachedAuthorizationInfo(principals);
    }

    /**
     * 清除所有用户授权信息缓存.
     */
    public void clearAllCachedAuthorizationInfo() {
        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
        if (cache != null) {
            for (Object key : cache.keys()) {
                cache.remove(key);
            }
        }
    }

}
