package com.betterjr.modules.wechat.data.api;

import java.util.*;

public class WechatPushTemplate implements java.io.Serializable {
    private static final long serialVersionUID = -8046453957602446226L;
    
    //模板标题
    private String title;
    
    //微信的模板ID
    private String tempId;
    
    //点击消息的"详情"调整地址
    private String invokeUrl;
    
    //标题的颜色
    private String topColor ="#000000";
    
    
    //所以微信模板的内容，包括first,remark, keyword1,keyword2,keyword3,keyword4,keyword5等字段。
    private Set<WechatPushTempField> fields = new LinkedHashSet<WechatPushTempField>(9);
    
    public WechatPushTemplate(){
        
    }
    
    public void addField(WechatPushTempField anFeild){
        if (anFeild != null){
            fields.add(anFeild);
        }
    }
    
    public WechatPushTemplate(String anTitle, String anTempId, String anInvokeUrl){
        this.title = anTitle;
        this.tempId = anTempId;
        this.invokeUrl = anInvokeUrl;
    }
    
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String anTitle) {
        this.title = anTitle;
    }
    public String getTempId() {
        return this.tempId;
    }
    public void setTempId(String anTempId) {
        this.tempId = anTempId;
    }
    public String getInvokeUrl() {
        return this.invokeUrl;
    }
    public void setInvokeUrl(String anInvokeUrl) {
        this.invokeUrl = anInvokeUrl;
    }
    public String getTopColor() {
        return this.topColor;
    }
    public void setTopColor(String anTopColor) {
        this.topColor = anTopColor;
    }
    public Set<WechatPushTempField> getFields() {
        
        return this.fields;
    }
    
    public void setFields(Set<WechatPushTempField> anFields) {
        this.fields = anFields;
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append("title=").append(title);
        sb.append(", tempId=").append(tempId);
        sb.append(", invokeUrl=").append(invokeUrl);
        sb.append(", topColor=").append(topColor);
        sb.append(", \r\n fields=").append(fields);
        sb.append("]");
        return sb.toString();
    }    
}
