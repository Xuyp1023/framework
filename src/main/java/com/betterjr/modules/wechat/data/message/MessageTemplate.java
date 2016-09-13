package com.betterjr.modules.wechat.data.message;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageTemplate implements java.io.Serializable {
    private static final long serialVersionUID = -1611128561933613417L;

    @JsonProperty("template_id")
    private String tempId;

    private String title;

    @JsonProperty("primary_industry")
    private String primaryIndustry;

    @JsonProperty("deputy_industry")
    private String secondIndustry;

    private String content;

    private String example;

    public String getTempId() {
        return this.tempId;
    }

    public void setTempId(String anTempId) {
        this.tempId = anTempId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String anTitle) {
        this.title = anTitle;
    }

    public String getPrimaryIndustry() {
        return this.primaryIndustry;
    }

    public void setPrimaryIndustry(String anPrimaryIndustry) {
        this.primaryIndustry = anPrimaryIndustry;
    }

    public String getSecondIndustry() {
        return this.secondIndustry;
    }

    public void setSecondIndustry(String anSecondIndustry) {
        this.secondIndustry = anSecondIndustry;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String anContent) {
        this.content = anContent;
    }

    public String getExample() {
        return this.example;
    }

    public void setExample(String anExample) {
        this.example = anExample;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append("title=").append(title);
        sb.append(",tempId=").append(tempId);
        sb.append(",primaryIndustry=").append(primaryIndustry);
        sb.append(",secondIndustry=").append(secondIndustry);
        sb.append("\r\ncontent=").append(content);
        sb.append("\r\nexample=").append(example);
        sb.append("]");

        return sb.toString();
    }
}
