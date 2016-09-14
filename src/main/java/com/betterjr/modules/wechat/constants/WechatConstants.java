// Copyright (c) 2014-2016 Betty. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2016年9月14日, liuwl, creation
// ============================================================================
package com.betterjr.modules.wechat.constants;

/**
 * @author liuwl
 *
 */
public final class WechatConstants {
    private WechatConstants() {
        throw new AssertionError();
    }

    public static final String wechatQrcodePrefix = "wechat::qrcodekey::"; // qrcode key 存储
    public static final String wechatScanPrefix = "wechat::scankey::";     // scan flag 存储
    public static final String wechatUserPrefix = "wechat::userkey::";     // usre flag
    public static final String wechatQrcodePattern = wechatQrcodePrefix +"*";

    public static final int scanTimeOut = 180;     //180秒扫码时间
    public static final int userTimeOut = 300;     //300秒输入交易密码时间

    public static final String SCANED = "1";
    public static final String UNSCAN = "0";
}
