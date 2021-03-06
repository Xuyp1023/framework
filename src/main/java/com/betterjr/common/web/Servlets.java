/**
 * Copyright (c) 2005-2012 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.betterjr.common.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.betterjr.common.config.Global;
import com.betterjr.common.security.KeyReader;
import com.betterjr.common.security.SecurityConstants;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Encodes;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.sys.security.ShiroUser;
import com.google.common.net.HttpHeaders;

/**
 * Http与Servlet工具类.
 * 
 * @author calvin/thinkgem
 * @version 2014-8-19
 */
public class Servlets {

    // -- 常用数值定义 --//
    public static final long ONE_YEAR_SECONDS = 60 * 60 * 24 * 365;

    // 静态文件后缀
    private final static String[] staticFiles = BetterStringUtils.split(Global.getConfig("web.staticFile"), ",");

    // 动态映射URL后缀
    private final static String urlSuffix = Global.getUrlSuffix();

    /**
     * 设置客户端缓存过期时间 的Header.
     */
    public static void setExpiresHeader(final HttpServletResponse response, final long expiresSeconds) {
        // Http 1.0 header, set a fix expires date.
        response.setDateHeader(HttpHeaders.EXPIRES, System.currentTimeMillis() + expiresSeconds * 1000);
        // Http 1.1 header, set a time after now.
        response.setHeader(HttpHeaders.CACHE_CONTROL, "private, max-age=" + expiresSeconds);
    }

    /**
     * 设置禁止客户端缓存的Header.
     */
    public static void setNoCacheHeader(final HttpServletResponse response) {
        // Http 1.0 header
        response.setDateHeader(HttpHeaders.EXPIRES, 1L);
        response.addHeader(HttpHeaders.PRAGMA, "no-cache");
        // Http 1.1 header
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0");
    }

    /**
     * 设置LastModified Header.
     */
    public static void setLastModifiedHeader(final HttpServletResponse response, final long lastModifiedDate) {
        response.setDateHeader(HttpHeaders.LAST_MODIFIED, lastModifiedDate);
    }

    /**
     * 设置Etag Header.
     */
    public static void setEtag(final HttpServletResponse response, final String etag) {
        response.setHeader(HttpHeaders.ETAG, etag);
    }

    /**
     * 根据浏览器If-Modified-Since Header, 计算文件是否已被修改.
     * 
     * 如果无修改, checkIfModify返回false ,设置304 not modify status.
     * 
     * @param lastModified
     *            内容的最后修改时间.
     */
    public static boolean checkIfModifiedSince(final HttpServletRequest request, final HttpServletResponse response,
            final long lastModified) {
        final long ifModifiedSince = request.getDateHeader(HttpHeaders.IF_MODIFIED_SINCE);
        if ((ifModifiedSince != -1) && (lastModified < ifModifiedSince + 1000)) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return false;
        }
        return true;
    }

    /**
     * 根据浏览器 If-None-Match Header, 计算Etag是否已无效.
     * 
     * 如果Etag有效, checkIfNoneMatch返回false, 设置304 not modify status.
     * 
     * @param etag
     *            内容的ETag.
     */
    public static boolean checkIfNoneMatchEtag(final HttpServletRequest request, final HttpServletResponse response,
            final String etag) {
        final String headerValue = request.getHeader(HttpHeaders.IF_NONE_MATCH);
        if (headerValue != null) {
            boolean conditionSatisfied = false;
            if (!"*".equals(headerValue)) {
                final StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");

                while (!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
                    final String currentToken = commaTokenizer.nextToken();
                    if (currentToken.trim().equals(etag)) {
                        conditionSatisfied = true;
                    }
                }
            } else {
                conditionSatisfied = true;
            }

            if (conditionSatisfied) {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                response.setHeader(HttpHeaders.ETAG, etag);
                return false;
            }
        }
        return true;
    }

    /**
     * 设置让浏览器弹出下载对话框的Header.
     * 
     * @param fileName
     *            下载后的文件名.
     */
    public static void setFileDownloadHeader(final HttpServletResponse response, final String fileName) {
        try {
            // 中文文件名支持
            final String encodedfileName = new String(fileName.getBytes(), "ISO8859-1");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedfileName + "\"");
        }
        catch (final UnsupportedEncodingException e) {
            e.getMessage();
        }
    }

    /**
     * 取得带相同前缀的Request Parameters, copy from spring WebUtils.
     * 
     * 返回的结果的Parameter名已去除前缀.
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, Object> getParametersStartingWith(final ServletRequest request, final String prefix) {
        Validate.notNull(request, "Request must not be null");
        final Enumeration paramNames = request.getParameterNames();
        final Map<String, Object> params = new TreeMap<String, Object>();
        String pre = prefix;
        if (pre == null) {
            pre = "";
        }
        while (paramNames != null && paramNames.hasMoreElements()) {
            final String paramName = (String) paramNames.nextElement();
            if ("".equals(pre) || paramName.startsWith(pre)) {
                final String unprefixed = paramName.substring(pre.length());
                String[] values = request.getParameterValues(paramName);
                if (values == null || values.length == 0) {
                    values = new String[] {};
                    // Do nothing, no values found at all.
                } else if (values.length > 1) {
                    params.put(unprefixed, values);
                } else {
                    if (paramName.toUpperCase().contains("DATE")) {
                        final String tmpStr = values[0];
                        if (StringUtils.isNotBlank(tmpStr)) {
                            values[0] = tmpStr.replace("-", "");
                        }
                    }
                    params.put(unprefixed, values[0]);
                }
            }
        }
        return params;
    }

    public static Map<String, String> getParameters(final ServletRequest anRequest) {
        Validate.notNull(anRequest, "Request must not be null");
        final Enumeration paramNames = anRequest.getParameterNames();
        final Map<String, String> params = new TreeMap<String, String>();
        while (paramNames != null && paramNames.hasMoreElements()) {
            final String paramName = (String) paramNames.nextElement();
            params.put(paramName, anRequest.getParameter(paramName));
        }
        return params;
    }

    /**
     * 组合Parameters生成Query String的Parameter部分,并在paramter name上加上prefix.
     * 
     */
    public static String encodeParameterStringWithPrefix(final Map<String, Object> params, final String prefix) {
        final StringBuilder queryStringBuilder = new StringBuilder();

        String pre = prefix;
        if (pre == null) {
            pre = "";
        }
        final Iterator<Entry<String, Object>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            final Entry<String, Object> entry = it.next();
            queryStringBuilder.append(pre).append(entry.getKey()).append("=").append(entry.getValue());
            if (it.hasNext()) {
                queryStringBuilder.append("&");
            }
        }
        return queryStringBuilder.toString();
    }

    /**
     * 客户端对Http Basic验证的 Header进行编码.
     */
    public static String encodeHttpBasic(final String userName, final String password) {
        final String encode = userName + ":" + password;
        return "Basic " + Encodes.encodeBase64(encode.getBytes());
    }

    /**
     * 是否是Ajax异步请求
     * 
     * @param request
     */
    public static boolean isAjaxRequest(final HttpServletRequest request) {

        final String accept = request.getHeader("accept");
        final String xRequestedWith = request.getHeader("X-Requested-With");

        final Subject subject = SecurityUtils.getSubject();
        final ShiroUser principal = (ShiroUser) subject.getPrincipal();

        // 如果是异步请求或是手机端，则直接返回信息
        return ((accept != null && accept.indexOf("application/json") != -1
                || (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1)
                || (principal != null && principal.isMobileLogin())));
    }

    /**
     * 获取当前请求对象
     * 
     * @return
     */
    public static HttpServletRequest getRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        }
        catch (final Exception e) {
            return null;
        }
    }

    public static HttpSession getSession() {

        return getRequest().getSession();
    }

    public static void invalidSession() {

        getRequest().getSession().invalidate();

    }

    public static Object getSessionValue(final String anKey) {
        final HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest().getSession();
        return session.getAttribute(anKey);
    }

    public static void putSessionValue(final String anKey, final Object anValue) {
        final HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest().getSession();
        session.setAttribute(anKey, anValue);
    }

    /**
     * 判断访问URI是否是静态文件请求
     * 
     * @throws Exception
     */
    public static boolean isStaticFile(final String uri) {
        if (staticFiles == null) {
            try {
                throw new Exception("检测到“app.properties”中没有配置“web.staticFile”属性。配置示例：\n#静态文件后缀\n"
                        + "web.staticFile=.css,.js,.png,.jpg,.gif,.jpeg,.bmp,.ico,.swf,.psd,.htc,.crx,.xpi,.exe,.ipa,.apk");
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }
        // if ((StringUtils.startsWith(uri, "/static/") ||
        // StringUtils.endsWithAny(uri, sfs))
        // && !StringUtils.endsWithAny(uri, ".jsp") &&
        // !StringUtils.endsWithAny(uri, ".java")){
        // return true;
        // }
        if (StringUtils.endsWithAny(uri, staticFiles) && !StringUtils.endsWithAny(uri, urlSuffix)
                && !StringUtils.endsWithAny(uri, ".jsp") && !StringUtils.endsWithAny(uri, ".java")) {
            return true;
        }
        return false;
    }

    /**
     * 获得用户远程地址
     */
    public static String getRemoteAddr() {
        final String tmpIP = UserUtils.getRequestIp();
        if (tmpIP == null) {
            return getRemoteAddr(Servlets.getRequest());
        } else {
            return tmpIP;
        }

    }

    public static String getRemoteAddr(final HttpServletRequest request) {
        if (request == null) {
            return "127.0.0.1";
        }
        String ip;
        for (final String tmpStr : new String[] { "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP" }) {
            ip = request.getHeader(tmpStr);
            if (StringUtils.isNotBlank(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

    /**
     * 获取数字证书
     */
    public static X509Certificate findCertificate(final HttpServletRequest anRequest)
            throws ServletException, IOException {
        final X509Certificate[] certs = (X509Certificate[]) anRequest.getAttribute(SecurityConstants.CERT_ATTR_CER);
        X509Certificate tmpCerts = null;
        if (certs == null) {
            final String tmpStr = anRequest.getHeader("X-SSL-Client-Cert");
            if (StringUtils.isNotBlank(tmpStr)) {
                // log.info("the request Cert is :" + tmpStr);
                tmpCerts = (X509Certificate) KeyReader
                        .fromCerBase64String(tmpStr.replaceFirst("-----BEGIN CERTIFICATE-----", "")
                                .replaceFirst("-----END CERTIFICATE-----", "").replaceAll("\t", ""));
            }
        } else {
            tmpCerts = certs[0];
        }

        return tmpCerts;
    }

    public static X509Certificate findCertificate() throws ServletException, IOException {

        return findCertificate(Servlets.getRequest());
    }

    public static Map<String, Object> convertDate(final Map<String, Object> anParams) {
        final Iterator<Entry<String, Object>> it = anParams.entrySet().iterator();
        while (it.hasNext()) {
            final Entry<String, Object> entry = it.next();
            final String paramName = entry.getKey();
            String value = "";
            if (paramName.toUpperCase().contains("DATE")) {
                final String tmpStr = (String) entry.getValue();
                if (StringUtils.isNotBlank(tmpStr)) {
                    value = tmpStr.replace("-", "");
                }
                anParams.put(paramName, value);
            }
        }
        return anParams;
    }

}
