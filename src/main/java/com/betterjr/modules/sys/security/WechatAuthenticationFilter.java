package com.betterjr.modules.sys.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterjr.common.security.SignHelper;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.web.Servlets;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class WechatAuthenticationFilter extends BaseFormAuthenticationFilter {

    private static Logger logger = LoggerFactory.getLogger(WechatAuthenticationFilter.class);

    private static final String TICKET_PARAMETER = "code";

    private String failureUrl;
    private static final Map<String, String> urlMap = createURLMap();
    
    private static Map createURLMap(){
        Map<String, String> data = new HashMap();
        data.put("1", "/scf/app/account/register.html?state=1");
        data.put("2", "/scf/app/account/register.html?state=2");
        data.put("3", "/scf/app/account/register.html?state=3");
        data.put("4", "/scf/app/account/register.html?state=4");
        data.put("5", "/scf/app/account/register.html?state=5");
        data.put("6", "/scf/app/account/register.html?state=6");
        return data;
    }
    
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String ticket = httpRequest.getParameter(TICKET_PARAMETER);

        String tmpIp;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest workRequest = (HttpServletRequest) request;
            tmpIp = Servlets.getRemoteAddr(workRequest);
        }
        else {
            tmpIp = "";
        }
        String username = SignHelper.randomBase64(20);
        String password = "1X2Y3W4o5m6";

        BetterjrWechatToken token = new BetterjrWechatToken(ticket, username, password, tmpIp );

        return token;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        logger.error("this is onAccessDenied");
        if (request instanceof HttpServletRequest) {
            HttpServletRequest workRequest = (HttpServletRequest) request;
            logger.info("onAccessDenied this request Session ID = " + workRequest.getSession().getId());
        }
        // WebUtils.issueRedirect(request, response, failureUrl);
        AuthenticationToken token = createToken(request, response);
        getSubject(request, response).login(token);
        return true;
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException ae, ServletRequest request, ServletResponse response) {

        Subject subject = getSubject(request, response);
        if (subject.isAuthenticated() || subject.isRemembered()) {
            try {
                issueSuccessRedirect(request, response);
            }
            catch (Exception e) {
                logger.error("Cannot redirect to the default success url", e);
            }
        }
        else {
            try {
                WebUtils.issueRedirect(request, response, failureUrl);
            }
            catch (IOException e) {
                logger.error("Cannot redirect to failure url : {}", failureUrl, e);
            }
        }
        return true;
    }

    public void setFailureUrl(String failureUrl) {
        this.failureUrl = failureUrl;
    }

    public static String findWorkUrl(String anKey){
        
        return urlMap.get(anKey);
    }
    
    protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
        String tmpKey = request.getParameter("state");
        if (BetterStringUtils.isBlank(tmpKey)){
            tmpKey = getSuccessUrl();
        }
        else{
           tmpKey = urlMap.get(tmpKey);
           if (BetterStringUtils.isBlank(tmpKey)){
               tmpKey = getSuccessUrl();
           }
        }
        WebUtils.redirectToSavedRequest(request, response, tmpKey);
    }
    
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        System.out.println("this onLoginSuccess");
        System.out.println(subject);
        super.onLoginSuccess(token, subject, request, response);
        return true;

    }

}