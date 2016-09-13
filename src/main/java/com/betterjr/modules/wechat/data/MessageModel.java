// Copyright (c) 2014-2016 Betty. All rights reserved.
// ============================================================================
// CURRENT VERSION 
// ============================================================================
// CHANGE LOG
// V2.0 : 2016年9月9日, liuwl, creation
// ============================================================================
package com.betterjr.modules.wechat.data;

import java.io.Serializable;
import java.util.List;

/**
 * @author liuwl
 *
 */
public class MessageModel implements Serializable {
    private static final long serialVersionUID = -8093858353031125453L;

    private String templateId;

    private String color;

    private String url;

    private List<MessageItemModel> items;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String anTemplateId) {
        templateId = anTemplateId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String anColor) {
        color = anColor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String anUrl) {
        url = anUrl;
    }

    public List<MessageItemModel> getItems() {
        return items;
    }

    public void setItems(List<MessageItemModel> anItems) {
        items = anItems;
    }

    public static class MessageItemModel implements Serializable {
        private static final long serialVersionUID = 5993531196759789395L;

        private String key;
        private String color;
        private String content;

        public String getKey() {
            return key;
        }

        public void setKey(String anKey) {
            key = anKey;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String anColor) {
            color = anColor;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String anContent) {
            content = anContent;
        }

    }
}

