// Copyright (c) 2014-2016 Betty. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2016年9月19日, liuwl, creation
// ============================================================================
package com.betterjr.modules.sms.entity;

import java.io.Serializable;
import java.util.Date;

import com.betterjr.modules.sms.util.VerifyCodeType;

/**
 * @author liuwl
 *
 */
public class VerifyCode implements Serializable {
    private static final long serialVersionUID = -8937949314903190731L;
    private String mobile; // 手机
    private String key; // key
    private String verifiCode; // verifiCode
    private Date genTime; // 产生时间
    private int seconds; // 过期时间 秒数
    private VerifyCodeType type; // 类型

    public String getMobile() {
        return mobile;
    }

    public void setMobile(final String anMobile) {
        mobile = anMobile;
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String anKey) {
        key = anKey;
    }

    public String getVerifiCode() {
        return verifiCode;
    }

    public void setVerifiCode(final String anVerifiCode) {
        verifiCode = anVerifiCode;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(final Date anGenTime) {
        genTime = anGenTime;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(final int anSeconds) {
        seconds = anSeconds;
    }

    public VerifyCodeType getType() {
        return type;
    }

    public void setType(final VerifyCodeType anType) {
        type = anType;
    }

}
