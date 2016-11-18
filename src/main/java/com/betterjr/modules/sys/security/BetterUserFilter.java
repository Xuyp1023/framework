// Copyright (c) 2014-2016 Bytter. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2016年11月18日, liuwl, creation
// ============================================================================
package com.betterjr.modules.sys.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterjr.common.mapper.JsonMapper;

/**
 * @author liuwl
 *
 */
public class BetterUserFilter extends UserFilter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /**
     * 拒绝访问的情况下，返回401
     */
    @Override
    protected boolean onAccessDenied(final ServletRequest anRequest, final ServletResponse anResponse) throws Exception {
        final HttpServletRequest request = WebUtils.toHttp(anRequest);
        logger.info("AccessDenied:" + request.getRequestURI());
        if (!isLoginRequest(anRequest, anResponse)) {
            if ("XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))) {// 是ajax请求
                final HttpServletResponse response = WebUtils.toHttp(anResponse);

                final Map<String, Object> reslut = new HashMap<>();
                reslut.put("code", 401);
                reslut.put("message", "无访问权限");
                response.setContentType("application/json");
                try {
                    response.getWriter().write(JsonMapper.toJsonString(reslut));
                }
                catch (final IOException e) {
                    logger.info("AccessDenied result write error.", e);
                }
                return false;
            }
        }
        return super.onAccessDenied(anRequest, anResponse);
    }

}
