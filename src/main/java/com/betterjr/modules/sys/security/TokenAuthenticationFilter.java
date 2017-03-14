package com.betterjr.modules.sys.security;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterjr.common.security.SignHelper;
import com.betterjr.common.web.Servlets;

public class TokenAuthenticationFilter extends BaseFormAuthenticationFilter {

    private static Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    private static final String TICKET_PARAMETER = "ticket";
    private static final String OPERCODE_PARAMETER = "operCode";
    private static final String CORPID_PARAMETER = "corpId";

    private String failureUrl;

    @Override
    protected AuthenticationToken createToken(final ServletRequest request, final ServletResponse response) {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String ticket = SignHelper.clearBase64InStr(httpRequest.getParameter(TICKET_PARAMETER));
        final String operCode = SignHelper.clearBase64InStr(httpRequest.getParameter(OPERCODE_PARAMETER));
        final String corpId = SignHelper.clearBase64InStr(httpRequest.getParameter(CORPID_PARAMETER));

        String tmpIp = null;
        if (request instanceof HttpServletRequest) {
            final HttpServletRequest workRequest = (HttpServletRequest) request;
            tmpIp = Servlets.getRemoteAddr(workRequest);
        }
        else {
            tmpIp = "";
        }
        final String username = SignHelper.randomBase64(20);
        final String password = "1X2Y3W4o5m6";

        final BetterjrSsoToken token = new BetterjrSsoToken(ticket, username, password, tmpIp);

        return token;
    }

    @Override
    protected boolean onAccessDenied(final ServletRequest request, final ServletResponse response) throws Exception {
        logger.error("this is onAccessDenied");
        if (request instanceof HttpServletRequest) {
            final HttpServletRequest workRequest = (HttpServletRequest) request;
            logger.info("onAccessDenied this request Session ID = " + workRequest.getSession().getId());
        }
        // WebUtils.issueRedirect(request, response, failureUrl);
        final AuthenticationToken token = createToken(request, response);
        final Subject subject = getSubject(request, response);
        subject.login(token);

        return true;
    }

    @Override
    protected boolean onLoginFailure(final AuthenticationToken token, final AuthenticationException ae, final ServletRequest request,
            final ServletResponse response) {

        final Subject subject = getSubject(request, response);
        if (subject.isAuthenticated() || subject.isRemembered()) {
            try {
                issueSuccessRedirect(request, response);
            }
            catch (final Exception e) {
                logger.error("Cannot redirect to the default success url", e);
            }
        }
        else {
            try {
                WebUtils.issueRedirect(request, response, failureUrl);
            }
            catch (final IOException e) {
                logger.error("Cannot redirect to failure url : {}", failureUrl, e);
            }
        }
        return true;
    }

    public void setFailureUrl(final String failureUrl) {
        this.failureUrl = failureUrl;
    }

    @Override
    protected boolean onLoginSuccess(final AuthenticationToken token, final Subject subject, final ServletRequest request,
            final ServletResponse response) throws Exception {
        System.out.println("this onLoginSuccess");
        System.out.println(subject);
        super.onLoginSuccess(token, subject, request, response);

        return true;

    }

}