package com.betterjr.modules.sys.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.common.security.SecurityConstants;
import com.betterjr.common.web.Servlets;

public class CaptchaFormAuthenticationFilter extends BaseFormAuthenticationFilter {
    private static final Logger log = LoggerFactory.getLogger(BaseFormAuthenticationFilter.class);
    public static final String DEFAULT_MOBILE_PARAM = "mobileLogin";
    public static final String DEFAULT_MESSAGE_PARAM = "message";
    private String captchaParam = SecurityConstants.CAPTCHA_KEY;
    private String mobileLoginParam = DEFAULT_MOBILE_PARAM;

    public String getCaptchaParam() {
        return captchaParam;
    }

    protected String getCaptcha(ServletRequest request) {
        return WebUtils.getCleanParam(request, getCaptchaParam());
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String tmpIp = null;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest workRequest = (HttpServletRequest) request;
            tmpIp = Servlets.getRemoteAddr(workRequest);
        }
        else {
            tmpIp = "";
        }

        String username = getUsername(request);
        String password = getPassword(request);
        String captcha = getCaptcha(request);
        String identType = WebUtils.getCleanParam(request, "indetType");

        CaptchaUsernamePasswordToken token = new CaptchaUsernamePasswordToken(identType, username, password, tmpIp, captcha, isMobileLogin(request),
                (HttpServletRequest) request, (HttpServletResponse) response);

        return token;
    }

    protected boolean isMobileLogin(ServletRequest request) {

        return WebUtils.isTrue(request, mobileLoginParam);
    }

    /**
     * 登录成功之后跳转URL
     */
    public String getSuccessUrl() {
        return super.getSuccessUrl();
    }

    @Override
    protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
        // Principal p = UserUtils.getPrincipal();
        // if (p != null && !p.isMobileLogin()){
        WebUtils.issueRedirect(request, response, getSuccessUrl(), null, true);
        // }else{
        // super.issueSuccessRedirect(request, response);
        // }
    }

    /**
     * 登录失败调用事件
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        String className = e.getClass().getName(), message = "";
        if (IncorrectCredentialsException.class.getName().equals(className) || UnknownAccountException.class.getName().equals(className)) {
            message = "用户或密码错误, 请重试.";
        }
        else if (e.getMessage() != null && StringUtils.startsWith(e.getMessage(), "msg:")) {
            message = StringUtils.replace(e.getMessage(), "msg:", "");
        }
        else if (e.getMessage() != null) {
            message = e.getMessage();
        }
        else {
            message = "系统出现点问题，请稍后再试！";
            e.printStackTrace(); // 输出到控制台
        }
        log.warn("loginFailure : " + message);
        request.setAttribute(getFailureKeyAttribute(), className);
        request.setAttribute("message", message);
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (!"XMLHttpRequest".equalsIgnoreCase(httpServletRequest.getHeader("X-Requested-With"))) {// 不是ajax请求
            try {
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/p/pages/login.html?success=0&message=" + message);
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        else {
            // httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/login/timeout/success");
            Map<String, Object> reslut = new HashMap<>();
            reslut.put("code", 4001);
            reslut.put("message", message);
            response.setContentType("application/json");
            try {
                httpServletResponse.getWriter().write(JsonMapper.toJsonString(reslut));
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
            return false;
        }
        return false;
    }
}
