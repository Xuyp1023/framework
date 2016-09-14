// Copyright (c) 2014-2016 Betty. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2016年9月12日, liuwl, creation
// ============================================================================
package com.betterjr.modules.wechat.dubboclient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.exception.BytterException;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.JedisUtils;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.wechat.constants.WechatConstants;
import com.betterjr.modules.wechat.data.EventType;
import com.betterjr.modules.wechat.data.MPAccount;
import com.betterjr.modules.wechat.data.api.AccessToken;
import com.betterjr.modules.wechat.data.api.QRTicket;
import com.betterjr.modules.wechat.data.event.BasicEvent;
import com.betterjr.modules.wechat.dubbo.interfaces.ICustWeChatService;
import com.betterjr.modules.wechat.entity.CustWeChatInfo;
import com.betterjr.modules.wechat.util.WechatAPIImpl;

/**
 * WeChat dubbo client service
 *
 * @author liuwl
 *
 */
@Service
public class CustWeChatDubboClientService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());



    @Reference(interfaceClass = ICustWeChatService.class)
    ICustWeChatService wechatService;

    private MPAccount mpAccount = null;

    /**
     *
     * @param anToken
     * @return
     */
    public Map<String, Object> saveLogin(final AccessToken anToken){
        return this.wechatService.saveLogin(anToken);
    }

    /**
     * 获取 mpAccount
     * @return
     */
    public synchronized MPAccount getMpAccount() {
        if (mpAccount == null) {
            mpAccount = wechatService.getMpAccount();
        }
        return mpAccount;
    }

    /**
     * 保存交易密码
     * @param anNewPasswd
     * @param anOkPasswd
     * @param anLoginPasswd
     * @return
     */
    public String saveMobileTradePass(final String anNewPasswd, final String anOkPasswd, final String anLoginPasswd){
        return wechatService.webSaveMobileTradePass(anNewPasswd, anOkPasswd, anLoginPasswd);
    }


    /**
     * 检查微信账户是否已经绑定操作员，如果已经绑定或没有登录返回false；表示不能绑定
     */
    protected boolean checkOperatorBinding() {
        final CustOperatorInfo op = UserUtils.getOperatorInfo();
        if (op != null) {
            return wechatService.checkWeChatInfoByOperId(op.getId());
        }

        return false;
    }

    /**
     *检查是否已经扫描，如果已经扫描，则返回true
     *如果返回空，说明已经超时
     * @return
     */
    public Object checkScanStatus() {
        final CustOperatorInfo operator = UserUtils.getOperatorInfo();
        if (operator != null){
            final String scanKey = WechatConstants.wechatScanPrefix + operator.getId();
            final String result = JedisUtils.get(scanKey);
            return result;
        }

        return false;
    }

    /**
     * 创建微信扫描码
     *
     * @param anWorkType
     *            工作类型，每种工作类型在1亿的区间；可以有21种类型
     * @return
     */
    public String createQcode(int anWorkType) {
        BTAssert.isTrue(checkOperatorBinding(), "已经绑定账户或没有登录，不能获取扫描二维码");
        final Integer keysCount = JedisUtils.keysCount(WechatConstants.wechatQrcodePattern);
        BTAssert.isTrue(keysCount < 100000, "现在申请绑定账户太多，请稍后再试试");

        anWorkType = Math.abs(anWorkType);
        BTAssert.isTrue(anWorkType < 21, "业务类型不能大于21");

        final WechatAPIImpl wechatApi = WechatAPIImpl.create(this.getMpAccount());
        final int workQCode = findQrcodeKey(anWorkType);
        final QRTicket qrt = wechatApi.createQR(workQCode, WechatConstants.scanTimeOut);

        try {
            return URLEncoder.encode(qrt.getTicket(), "UTF-8");
        }
        catch (final UnsupportedEncodingException e) {
            throw BytterException.unchecked(e);
        }
    }

    /**
     * 获取一个 qrcodekey
     * @param anWorkType
     * @return
     */
    private int findQrcodeKey(final int anWorkType) {
        int limitKey;
        String tmpStr;
        while (true) {
            limitKey = (anWorkType * 10000 * 10000) + SerialGenerator.randomInt(10000 * 10000);
            tmpStr = String.valueOf(limitKey);
            final String qrcodeKey = WechatConstants.wechatQrcodePrefix + tmpStr;            // 得到Qrcode key
            if (JedisUtils.exists(qrcodeKey) == false) {
                final CustOperatorInfo operator = UserUtils.getOperatorInfo();
                final String scanKey = WechatConstants.wechatScanPrefix + operator.getId(); // 得到 scan flag key
                JedisUtils.setObject(qrcodeKey, operator, WechatConstants.scanTimeOut);        // scanTimeOut
                JedisUtils.set(scanKey, WechatConstants.SCAN_NO, WechatConstants.scanTimeOut);                 // scanTimeOut
                break;
            }
        }
        return limitKey;
    }

    /**
     * 根据微信订阅事件，保存微信订阅状态信息
     * @param anEvent
     * @param anSubscribe
     */
    public CustWeChatInfo saveWeChatInfo(final BasicEvent anWeChatEvent, final EventType anEventType) {
        BTAssert.notNull(anWeChatEvent, "微信事件必须存在");
        BTAssert.notNull(anEventType, "微信事件类型必须存在");

        return wechatService.saveWeChatInfo(anWeChatEvent, anEventType);
    }

    /**
     * @param anEventKey
     * @param anFromUserName
     */
    public String saveBindingWeChat(final String anEventKey, final String anFromUserName) {
        if (StringUtils.isBlank(anEventKey) || StringUtils.isBlank(anEventKey)) {
            return null;
        }
        final CustWeChatInfo wechatInfo = wechatService.findWeChatInfo(anFromUserName); // 通过用户OpenId找到系统中的wechatInfo
        if (wechatInfo != null) {
            final String qrcodeKey = WechatConstants.wechatQrcodePrefix + anEventKey; // 拿到wechatKey
            final CustOperatorInfo operator = JedisUtils.getObject(qrcodeKey);            // 取到wechatKey 存储的用户信息  userId 即可
            if (operator != null) {
                JedisUtils.delObject(qrcodeKey);                                        // delete qrcodeKey
                final String scanKey = WechatConstants.wechatScanPrefix + operator.getId();
                JedisUtils.set(scanKey, WechatConstants.SCAN_YES, WechatConstants.scanTimeOut); //置为已扫描

                final String userKey = WechatConstants.wechatUserPrefix + operator.getId();
                JedisUtils.set(userKey, wechatInfo.getOpenId(), WechatConstants.userTimeOut);   // 给定超时时间
                return operator.getName();
            }
        }
        return null;
    }

}
