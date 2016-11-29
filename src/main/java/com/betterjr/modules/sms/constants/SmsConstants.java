// Copyright (c) 2014-2016 Betty. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2016年9月14日, liuwl, creation
// ============================================================================
package com.betterjr.modules.sms.constants;

/**
 * @author liuwl
 *
 */
public final class SmsConstants {
    private SmsConstants() {
        throw new AssertionError();
    }

    public static final String smsVerifiCodePrefix = "sms::verifiCode::"; // verification code 存储
    public static final String smsVerifiCodeFrequencyPrefix = "sms::verifiCodeFrequency::"; // verification code 频率 存储

    // 供用户保存用
    public static final String smsModifyTradePassVerifyCodePrefix = "sms::modifyTradePassVerifyCode::";
    public static final String smsOpenAccountVerifyCodePrefix = "sms::openAccountVerifyCode::";

    // 常用秒数
    public static final int SEC_1800 = 1800;
    public static final int SEC_1200 = 1200;
    public static final int SEC_600 = 600;
    public static final int SEC_300 = 300;
    public static final int SEC_60 = 60;
}
