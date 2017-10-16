// Copyright (c) 2014-2016 Betty. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2016年9月19日, liuwl, creation
// ============================================================================
package com.betterjr.modules.sms.util;

/**
 * 验证码业务类型
 * @author liuwl
 *
 */
public enum VerifyCodeType {
    CHANGE_TRADE_PASSWORD("1"), OPEN_ACCOUNT_PASSWORD("2");

    private final String type;

    VerifyCodeType(final String anType) {
        this.type = anType;
    }

    public String getType() {
        return type;
    }

}
