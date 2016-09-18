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

    public static final String wechatAccessTokenLockPrefix = "wechat::accessTokenLock::";
    public static final String wechatJSTicketLockPrefix= "wechat::jsTicketLock::";
    
    public static final String wechatAccessTokenPrefix = "wechat::accessToken::";
    public static final String wechatJSTicketPrefix = "wechat::jsTicket::";

    public static final String wechatQrcodePrefix = "wechat::qrcodekey::"; // qrcode key 存储
    public static final String wechatScanPrefix = "wechat::scankey::";     // scan flag 存储
    public static final String wechatUserPrefix = "wechat::userkey::";     // user flag
    public static final String wechatQrcodePattern = wechatQrcodePrefix +"*";

    public static final int scanTimeOut = 180;     //180秒扫码时间
    public static final int userTimeOut = 300;     //300秒输入交易密码时间

    public static final String SCAN_YES = "1";      // 已扫描
    public static final String SCAN_NO = "0";       // 未扫描
}
