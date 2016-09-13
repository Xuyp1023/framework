package com.betterjr.modules.sys.security;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.betterjr.common.data.CustPasswordType;
import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.common.data.UserType;
import com.betterjr.common.exception.BytterSecurityException;
import com.betterjr.common.security.SecurityConstants;
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
import com.betterjr.modules.wechat.data.api.AccessToken;
import com.betterjr.modules.wechat.dubboclient.CustWeChatDubboClientService;
import com.betterjr.modules.wechat.util.WechatDefHandler;
import com.betterjr.modules.wechat.util.WechatKernel;

public class SystemAuthorizingRealm extends AuthorizingRealm {
    private static final Logger log = LoggerFactory.getLogger(SystemAuthorizingRealm.class);

    private static final int INTERATIONS = 1024;
    private static final int SALT_SIZE = 20;
    private static final String ALGORITHM = "SHA-256";
   
    private CustCertDubboClientService certService;

    
    private CustLoginDubboClientService userService;

    
    private CustOperatorDubboClientService operatorService;

    
    private CustPassDubboClientService passService;
    
    
    private CustWeChatDubboClientService weChatService;

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
        List<SimpleDataEntity> userPassData = null;
        try {
            CustCertInfo certInfo = null;
            if ((authcToken instanceof BetterjrWechatToken) == false) {
                try {
                    X509Certificate cert = Servlets.findCertificate();
                    if (cert != null) {
                        certInfo = checkValid(cert);
                    }
                    else {

                        throw new AuthenticationException("the request has X509Certificate");
                    }
                }
                catch (AuthenticationException e) {

                    throw e;
                }
                catch (Exception e) {
                    e.printStackTrace();
                    throw new AuthenticationException("数字证书验证失败");
                }
            }
            if (authcToken instanceof CaptchaUsernamePasswordToken) {
                CaptchaUsernamePasswordToken token = (CaptchaUsernamePasswordToken) authcToken;
                user = operatorService.findCustOperatorByOperCode(certInfo.getOperOrg(), token.getUsername());
                if (user != null) {
                    log.warn(user.toString());
                    CustPassInfo passInfo = passService.getOperaterPassByCustNo(user.getId(), CustPasswordType.ORG);

                    // 临时锁定
                    if (passInfo == null || passInfo.validLockType()) {

                        throw new DisabledAccountException("操作员密码被锁定，请稍后再试");
                    }
                    mobileLogin = token.isMobileLogin();
                    passWD = passInfo.getPasswd();
                    saltStr = passInfo.getPassSalt();
                    user.setAccessType("1");
                    contextInfo = userService.saveFormLogin(user);
                }
            }
            else if (authcToken instanceof BetterjrSsoToken || authcToken instanceof BetterjrWechatToken) {
                if (authcToken instanceof BetterjrSsoToken) {
                    BetterjrSsoToken ssoToken = (BetterjrSsoToken) authcToken;
                    String ticket = (String) ssoToken.getTicket();
                    contextInfo = userService.saveTokenLogin(ticket);
                    user = contextInfo.getOperatorInfo();
                    ssoToken.setUsername(user.getName());
                }
                else {
                    BetterjrWechatToken wechatToken = (BetterjrWechatToken) authcToken;
                    WechatKernel wk = new WechatKernel(weChatService.getMpAccount(), new WechatDefHandler(weChatService), new HashMap());
                    AccessToken at = wk.findUserAuth2(wechatToken.getTicket());
                    
                    Map<String, Object> mapResult = weChatService.saveLogin(at);
                    Object operator=mapResult.get("operator");
                    Object message=mapResult.get("message");
                    if(operator instanceof CustOperatorInfo){
                        user= (CustOperatorInfo)operator;
                    }
                    if (user == null) {
                        Servlets.getSession().invalidate();
                        throw new AuthenticationException(new BytterSecurityException(20401, message.toString()));
                    }
                    else {
                        contextInfo = userService.saveFormLogin(user);
                        certInfo = certService.findFirstCertInfoByOperOrg(user.getOperOrg());
                    }
                    wechatToken.setUsername(user.getName());
                    mobileLogin = true;
                }
                // 用证书登录方式，控制在拜特资金管理系统，原则上不存在过期或无效的问题
                // 统一使用Form形式的验证！
                saltStr = "985a44369b063938a6a7";
                passWD = "8438d772e1eac7d8e57aecaae5fb0b8c2e369283cbe31857d89dc87430160a2b";
            }
            workData = contextInfo;

            if (user != null) {
                userPassData = passService.findPassAndSalt(user.getId(), new String[]{CustPasswordType.PERSON_TRADE.getPassType(), CustPasswordType.ORG_TRADE.getPassType()});
                log.warn(user.toString());
                if (user.getStatus().equals("1") == false) {
                    throw new DisabledAccountException("操作员被要求暂停业务或者已经被注销");
                }

                UserType ut = UserType.ORG_USER;
                // 如果是默认操作员，则是管理员
                if (user.getDefOper()) {
                    ut = UserType.OPERATOR_ADMIN;
                }

                ShiroUser shiroUser = new ShiroUser(ut, user.getId(), user.getName(), user, certInfo.getRuleList(), mobileLogin, workData, userPassData);
                log.info("this login user Info is :" + shiroUser);
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


    private CustCertInfo checkValid(X509Certificate anCert) {
        CustCertInfo certInfo = certService.checkValidity(anCert);
        Servlets.getSession().setAttribute(SecurityConstants.CUST_CERT_INFO, certInfo);
//        AccessClientImpl.set(certInfo);
        return certInfo;
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
        contextInfo.addTradeAccount(new ArrayList());

        // todo;登录信息和状态暂时不处理
        return contextInfo;
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

    public static HashPassword encrypt(String plainText) {
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
