package com.betterjr.modules.sys.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authc.UsernamePasswordToken;

public class CaptchaUsernamePasswordToken extends UsernamePasswordToken {
    /** 描述 */
    private static final long serialVersionUID = -3178260335127476542L;

    private final String captcha;
    private HttpSession session;
    private final String ipAddress;
    private final String identType;
    private final boolean mobileLogin;
    private HttpServletRequest request = null;
    private HttpServletResponse response = null;

    public boolean isMobileLogin() {
        return mobileLogin;
    }

    public String getIdentType() {
        return identType;
    }

    public HttpSession getSession() {
        return session;
    }

    public String getCaptcha() {
        return captcha;
    }

    public CaptchaUsernamePasswordToken() {
        this.identType = "";
        this.ipAddress = "";
        this.captcha = "";
        this.mobileLogin = false;
    }

    public CaptchaUsernamePasswordToken(String anIdentType, String username, String password, String anHost, String captcha, boolean anMobileLogin,
            HttpServletRequest request, HttpServletResponse response) {
        super(username, password, false, anHost);
        this.captcha = captcha;
        this.ipAddress = anHost;
        this.identType = anIdentType;
        this.mobileLogin = anMobileLogin;
        this.request = request;
        this.response = response;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

}
