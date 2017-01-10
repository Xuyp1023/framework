package com.betterjr.common.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.JedisUtils;

public class DoubleCheckServletFilter implements Filter {

    private static final String WEB_AJAX_TOKEN_KEY = "webAjaxToken::AccessToken::";

    @Override
    public void init(FilterConfig anFilterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest anRequest, ServletResponse anResponse, FilterChain anChain) throws IOException, ServletException {
        String tmpAjaxToken = anRequest.getParameter("AjaxToken");
        HttpServletRequest request = (HttpServletRequest)anRequest; 
        if (BetterStringUtils.isNotBlank(tmpAjaxToken)) {
            tmpAjaxToken = request.getRequestURI().concat("@****@").concat(tmpAjaxToken);
            String tmpValue = JedisUtils.get(WEB_AJAX_TOKEN_KEY.concat(tmpAjaxToken));
            if (BetterStringUtils.isNotBlank(tmpValue)) {
                anResponse.setCharacterEncoding("UTF-8");
                anResponse.setContentType("application/json");
                anResponse.getWriter().write(new AjaxObject(204, "请求太频繁").toJson());
                return;
            }
            else {
                JedisUtils.set(WEB_AJAX_TOKEN_KEY.concat(tmpAjaxToken), tmpAjaxToken, 30);
            }
        }
        anChain.doFilter(anRequest, anResponse);
    }

    @Override
    public void destroy() {

    }

}
