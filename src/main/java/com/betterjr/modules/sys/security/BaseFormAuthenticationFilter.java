package com.betterjr.modules.sys.security;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.common.web.Servlets;

public class BaseFormAuthenticationFilter extends FormAuthenticationFilter {
    private static final Logger log = LoggerFactory.getLogger(BaseFormAuthenticationFilter.class);

    protected static final String[] blackUrlPathPattern = new String[] { "*.aspx*", "*.asp*", "*.php*", "*.exe*",
            "*.pl*", "*.py*", "*.groovy*", "*.sh*", "*.rb*", "*.dll*", "*.bat*", "*.bin*", "*.dat*", "*.bas*", "*.c*",
            "*.cmd*", "*.com*", "*.cpp*", "*.jar*", "*.class*", "*.lnk*" }; // 防文件攻击

    /*
     * 覆盖默认实现，打印日志便于调试，查看具体登录是什么错误。（可以扩展把错误写入数据库之类的。） (non-Javadoc)
     * 
     * @see org.apache.shiro.web.filter.authc.FormAuthenticationFilter#onLoginFailure (org.apache.shiro.authc.AuthenticationToken,
     * org.apache.shiro.authc.AuthenticationException, javax.servlet.ServletRequest, javax.servlet.ServletResponse)
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
            ServletResponse response) {
        if (log.isDebugEnabled()) {
            Class<?> clazz = e.getClass();
            if (clazz.equals(AuthenticationException.class)) {
                log.debug("login failure!", e);
            }
        }

        return super.onLoginFailure(token, e, request, response);
    }

    public static boolean simpleMatch(String pattern, String original) {
        if (original.length() < pattern.length()) {
            return false;
        }
        pattern = pattern.replace("*", "");
        return original.indexOf(pattern) > -1;
    }

    /**
     * 覆盖isAccessAllowed，改变shiro的验证逻辑。 避免不能多次登录的错误。
     * 
     * @param request
     * @param response
     * @param mappedValue
     * @return
     * @see org.apache.shiro.web.filter.authc.AuthenticatingFilter#isAccessAllowed(javax.servlet.ServletRequest, javax.servlet.ServletResponse,
     *      java.lang.Object)
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        String requestUrl = WebUtils.getPathWithinApplication(httpRequest);
        for (String pattern : blackUrlPathPattern) {
            if (simpleMatch(pattern, requestUrl)) {
                log.error(new StringBuffer().append("unsafe request >>> ").append(" request time: ")
                        .append( // 不安全的请求
                                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                        .append("; request ip: ").append(Servlets.getRemoteAddr(httpRequest)).append("; request url: ")
                        .append(httpRequest.getRequestURI()).toString());
                return false;
            }
        }

        try {
            // 先判断是否是登录操作
            if (isLoginSubmission(request, response)) {
                if (log.isTraceEnabled()) {
                    log.trace("Login submission detected.  Attempting to execute login.");
                }
                return false;
            }
        }
        catch (Exception e) {
            log.error("isAccessAllowed", e);
        }

        return super.isAccessAllowed(request, response, mappedValue);
    }

    /**
     * 覆盖默认实现，用sendRedirect直接跳出框架，以免造成js框架重复加载js出错。
     * 
     * @param token
     * @param subject
     * @param request
     * @param response
     * @return
     * @throws Exception
     * @see org.apache.shiro.web.filter.authc.FormAuthenticationFilter#onLoginSuccess(org.apache.shiro.authc.AuthenticationToken,
     *      org.apache.shiro.subject.Subject, javax.servlet.ServletRequest, javax.servlet.ServletResponse)
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
            ServletResponse response) throws Exception {
        log.warn("isAccessAllowed onLoginSuccess");
        PrincipalCollection cc = SecurityUtils.getSubject().getPrincipals();
        if (cc != null) {
            boolean bb = SecurityUtils.getSubject().hasRole("admin");
            log.warn("this is user is " + bb);
            for (Object obj : cc.asList()) {
                log.warn("this is " + obj);
            }
        }
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (!"XMLHttpRequest".equalsIgnoreCase(httpServletRequest.getHeader("X-Requested-With"))) {// 不是ajax请求
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + this.getSuccessUrl());
        } else {
            // httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/login/timeout/success");
            Map<String, Object> reslut = new HashMap<>();
            reslut.put("code", 200);
            reslut.put("message", "ok");
            response.setContentType("application/json");
            httpServletResponse.getWriter().write(JsonMapper.toJsonString(reslut));
        }
        log.warn("loginSuccess : 登录成功");
        return false;
    }

    @Override
    protected boolean isLoginSubmission(ServletRequest request, ServletResponse response) {
        // && WebUtils.toHttp(request).getMethod().equalsIgnoreCase(POST_METHOD);
        return (request instanceof HttpServletRequest);
    }
}
