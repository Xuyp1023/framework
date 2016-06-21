package com.betterjr.common.web;

import com.betterjr.common.exception.BytterException;
import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.mapper.pagehelper.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;

public class AjaxObject {
    // 状态码
    private final static int STATUS_CODE_SUCCESS = 200;
    private final static int STATUS_CODE_FAILURE = 300;
    private final static int STATUS_CODE_TIMEOUT = 301;
    private final static int STATUS_CODE_FORBIDDEN = 403;
    private final static int STATUS_CODE_BUSIN = 500;
    private static JsonMapper jMapper = JsonMapper.buildOutDefaultMapper();

    // callbackType类型
    public final static String CALLBACK_TYPE_CLOSE_CURRENT = "closeCurrent";
    private final static String CALLBACK_TYPE_FORWARD = "forward";

    @JsonProperty(index = 0, value = "code")
    private int statusCode = 0;
    
    private String message = "";
    
    @JsonIgnore
    private String navTabId = "";
    @JsonIgnore
    private String forwardUrl = "";
    @JsonIgnore
    private String rel = "";
    @JsonIgnore
    
    private String callbackType = "";// CALLBACK_TYPE_CLOSE_CURRENT;
    private Object data;

    private WorkPageInfo page;

    public void setPage(WorkPageInfo anPage) {
        this.page = anPage;
    }

    public AjaxObject() {

    }

    public AjaxObject(String message) {
        this.message = message;
    }

    /**
     * 构造函数
     * 
     * @param statusCode
     */
    public AjaxObject(int statusCode) {
        super();
        this.statusCode = statusCode;
    }

    /**
     * 构造函数
     * 
     * @param statusCode
     * @param message
     */
    public AjaxObject(int statusCode, String message) {
        super();
        this.statusCode = statusCode;
        this.message = message;
    }

    /**
     * 构造函数
     * 
     * @param statusCode
     * @param message
     */
    public AjaxObject(int statusCode, String message, Object anData, WorkPageInfo anPage) {
        super();
        this.statusCode = statusCode;
        this.message = message;
        this.data = anData;
        this.page = anPage;
    }

    /**
     * 构造函数
     * 
     * @param statusCode
     * @param message
     */
    public AjaxObject(int statusCode, String message, Object anData) {
        super();
        this.statusCode = statusCode;
        this.message = message;
        this.data = anData;
    }

    /**
     * 构造函数
     * 
     * @param statusCode
     * @param message
     * @param callbackType
     */
    public AjaxObject(int statusCode, String message, String callbackType) {
        this.statusCode = statusCode;
        this.message = message;
        this.callbackType = callbackType;
    }

    /**
     * 构造函数
     * 
     * @param statusCode
     * @param message
     * @param forwardUrl
     * @param rel
     * @param callbackType
     */
    public AjaxObject(int statusCode, String message, String navTabId, String forwardUrl, String rel, String callbackType) {
        this.statusCode = statusCode;
        this.message = message;
        this.navTabId = navTabId;
        this.forwardUrl = forwardUrl;
        this.rel = rel;
        this.callbackType = callbackType;
    }

    public Object getData() {
        return this.data;
    }

    public WorkPageInfo getPage() {
        return this.page;
    }

    /**
     * 返回 statusCode 的值
     * 
     * @return statusCode
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * 设置 statusCode 的值
     * 
     * @param statusCode
     */
    public AjaxObject setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    /**
     * 返回 message 的值
     * 
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置 message 的值
     * 
     * @param message
     */
    public AjaxObject setMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * 返回 forwardUrl 的值
     * 
     * @return forwardUrl
     */
    public String getForwardUrl() {
        return forwardUrl;
    }

    /**
     * 设置 forwardUrl 的值
     * 
     * @param forwardUrl
     */
    public AjaxObject setForwardUrl(String forwardUrl) {
        this.forwardUrl = forwardUrl;
        return this;
    }

    /**
     * 返回 rel 的值
     * 
     * @return rel
     */
    public String getRel() {
        return rel;
    }

    /**
     * 设置 rel 的值
     * 
     * @param rel
     */
    public AjaxObject setRel(String rel) {
        this.rel = rel;
        return this;
    }

    /**
     * 返回 callbackType 的值
     * 
     * @return callbackType
     */
    public String getCallbackType() {
        return callbackType;
    }

    /**
     * 设置 callbackType 的值
     * 
     * @param callbackType
     */
    public AjaxObject setCallbackType(String callbackType) {
        this.callbackType = callbackType;
        return this;
    }

    /**
     * 返回 navTabId 的值
     * 
     * @return navTabId
     */
    public String getNavTabId() {
        return navTabId;
    }

    /**
     * 设置 navTabId 的值
     * 
     * @param navTabId
     */
    public AjaxObject setNavTabId(String navTabId) {
        this.navTabId = navTabId;
        return this;
    }/*
      * 
      * public static String createJson(Object anObj) {
      * 
      * try { return JsonMapper.getInstance().writeValueAsString(anObj); } catch (JsonProcessingException e) { e.printStackTrace(); return ""; } }
      */

    public static AjaxObject newOk(String message) {
        return new AjaxObject(STATUS_CODE_SUCCESS, message);
    }

    public static AjaxObject newOk(String message, Object anData) {
        return new AjaxObject(STATUS_CODE_SUCCESS, message, anData);
    }

    public static AjaxObject newOkWithPage(String message, Page anData) {
        return new AjaxObject(STATUS_CODE_SUCCESS, message, anData, new WorkPageInfo(anData));
    }

    public static AjaxObject newOk(Object anData) {

        return new AjaxObject(STATUS_CODE_SUCCESS, "ok", anData);
    }

    public static AjaxObject newOk(Object anData, WorkPageInfo anPage) {

        return new AjaxObject(STATUS_CODE_SUCCESS, "ok", anData, anPage);
    }

    public static AjaxObject newOk(String message, Object anData, WorkPageInfo anPage) {

        return new AjaxObject(STATUS_CODE_SUCCESS, message, anData, anPage);
    }

    public static AjaxObject newError(String message) {
        return new AjaxObject(STATUS_CODE_FAILURE, message);
    }

    public static AjaxObject newError(Exception anEx){
        String message;
        if (anEx instanceof BytterException) {
            message = anEx.getMessage();
        }
        else {
            message = "未知错误！";
        }
        
        return new AjaxObject(STATUS_CODE_FAILURE, message);
    }

    public static AjaxObject newTimeout(String message) {
        return new AjaxObject(STATUS_CODE_TIMEOUT, message);
    }

    public static AjaxObject newForbidden(String message) {
        return new AjaxObject(STATUS_CODE_FORBIDDEN, message);
    }

    public static AjaxObject newBusin(String message) {
        return new AjaxObject(STATUS_CODE_BUSIN, message);
    }
    
    public static AjaxObject newRefreshNavtab(String navTabId, String message) {
        AjaxObject ajaxObject = new AjaxObject(message);
        ajaxObject.navTabId = navTabId;
        return ajaxObject;
    }

    public static AjaxObject newRefreshRel(String rel, String message) {
        AjaxObject ajaxObject = new AjaxObject(message);
        ajaxObject.rel = rel;
        return ajaxObject;
    }

    public static AjaxObject newForward(String forwardUrl) {
        AjaxObject ajaxObject = new AjaxObject(CALLBACK_TYPE_FORWARD);
        ajaxObject.forwardUrl = forwardUrl;
        return ajaxObject;
    }

    public void createDataInfo(StringBuilder anSB) {
        if (this.data != null) {
            anSB.append(",\"data\":");
            try {
                anSB.append(JsonMapper.getInstance().writeValueAsString(data));
            }
            catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public void createPageInfo(StringBuilder anSB) {
        if (this.page != null) {
            anSB.append(",\"page\":");
            try {
                anSB.append(JsonMapper.getInstance().writeValueAsString(page));
            }
            catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public String toJson() {

        return jMapper.toJson(this);
    }

    /**
     * @return
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder(200);
        buffer.append("{").append("\"code\":\"" + statusCode + "\",").append("\"message\":\"" + message + "\",")
                .append("\"navTabId\":\"" + navTabId + "\",").append("\"rel\":\"" + rel + "\",").append("\"callbackType\":\"" + callbackType + "\",")
                .append("\"forwardUrl\":\"" + forwardUrl + "\"");
        createPageInfo(buffer);
        createDataInfo(buffer);
        buffer.append("}");
        // System.out.println(buffer.toString());
        return buffer.toString();
    }

}
