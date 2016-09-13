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
import com.betterjr.modules.wechat.data.EventType;
import com.betterjr.modules.wechat.data.MPAccount;
import com.betterjr.modules.wechat.data.api.AccessToken;
import com.betterjr.modules.wechat.data.api.QRTicket;
import com.betterjr.modules.wechat.data.event.BasicEvent;
import com.betterjr.modules.wechat.data.event.ScanEvent;
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

    private static final String wechatQrcodePrefix = "wechat::qrcodekey::";
    private static final String wechatUserPrefix = "wechat::userkey::";
    private static final String wechatKeyPrefixPattern = wechatQrcodePrefix +"*";

    private static final int qCodeTimeOut = 180;

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

    public String saveMobileTradePass(final String anNewPasswd, final String anOkPasswd, final String anLoginPasswd){
        return wechatService.webSaveMobileTradePass(anNewPasswd, anOkPasswd, anLoginPasswd);
    }

    public synchronized MPAccount getMpAccount() {
        if (mpAccount == null) {
            mpAccount = wechatService.findMpAccount();
        }
        return mpAccount;
    }
    /*
    @PostConstruct
    public synchronized void init() {
        // 修改为实际的公众号信息,可以在开发者栏目中查看
        mpAccount = wechatService.findMpAccount();
    }*/

    /**
     * 检查微信账户是否已经绑定操作员，如果已经绑定或没有登录返回false；表示不能绑定
     */
    protected boolean checkOperatorBinding() {
        final CustOperatorInfo op = UserUtils.getOperatorInfo();
        if (op != null) {
            // 从 CustWeChatInfoMapper 中取回用户
            //return Collections3.isEmpty(this.selectByProperty("operId", op.getId()));
            return wechatService.checkWeChatInfoByOperId(op.getId());
        }

        return false;
    }

    /**
     *检查是否已经扫描，如果已经扫描，则返回true
     * @return
     */
    public Object checkScanStatus() {
        final CustOperatorInfo operator = UserUtils.getOperatorInfo();
        if (operator != null){
            final String userKey = wechatUserPrefix + operator.getId();
            final Boolean result = JedisUtils.getObject(userKey);
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
        final Integer keysCount = JedisUtils.keysCount(wechatKeyPrefixPattern);
        BTAssert.isTrue(keysCount < 100000, "现在申请绑定账户太多，请稍后再试试");

        anWorkType = Math.abs(anWorkType);
        BTAssert.isTrue(anWorkType < 21, "业务类型不能大于21");

        final WechatAPIImpl wechatApi = WechatAPIImpl.create(this.getMpAccount());
        final int workQCode = findQCodeKey(anWorkType);
        final QRTicket qrt = wechatApi.createQR(workQCode, qCodeTimeOut);

        try {
            return URLEncoder.encode(qrt.getTicket(), "UTF-8");
        }
        catch (final UnsupportedEncodingException e) {
            throw BytterException.unchecked(e);
        }
    }

    protected int findQCodeKey(final int anWorkType) {
        int limitKey;
        String tmpStr;
        while (true) {
            limitKey = (anWorkType * 10000 * 10000) + SerialGenerator.randomInt(10000 * 10000);
            tmpStr = Integer.toString(limitKey);
            final String qcodeKey = wechatQrcodePrefix + tmpStr;
            if (JedisUtils.exists(qcodeKey) == false) {
                final CustOperatorInfo operator = UserUtils.getOperatorInfo();
                final String userKey = wechatUserPrefix + operator.getId();
                JedisUtils.setObject(userKey, Boolean.FALSE, qCodeTimeOut);
                JedisUtils.setObject(qcodeKey, operator, qCodeTimeOut);
                break;
            }
        }
        return limitKey;
    }

    public CustWeChatInfo saveWeChatInfo(final CustWeChatInfo anWeChatInfo, final String anStatus) {
        BTAssert.notNull(anWeChatInfo, "客户微信信息必须存在");
        BTAssert.notNull(anStatus, "状态必须存在");

        return wechatService.saveWeChatInfo(anWeChatInfo, anStatus);

        /* logger.info("saveWeChatInfo from WeChatInfo:" + anWeChatInfo);
        final CustWeChatInfo weChatInfo = this.selectByPrimaryKey(anWeChatInfo.getOpenId());
        if (weChatInfo != null) {
            anWeChatInfo.modifyValue(UserUtils.getOperatorInfo(), weChatInfo);
            anWeChatInfo.setAppId(weChatInfo.getAppId());
            if (StringUtils.isNotBlank(anStatus)) {
                if ("1".equals(anStatus)) {
                    BTAssert.isTrue("1".equals(anWeChatInfo.getSubscribeStatus()), "只有在已订阅情况下，才能修改微信客户的状态为正常");
                }
                anWeChatInfo.setBusinStatus(anStatus);
            }
            this.updateByPrimaryKey(anWeChatInfo);
        }

        return anWeChatInfo;*/
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
     * @param anEvent
     * @return
     */
    public CustWeChatInfo saveBindingWeChat(final ScanEvent anWeChatEvent) {
        final CustWeChatInfo weChatInfo = wechatService.findWeChatInfo(anWeChatEvent.getFromUserName());//this.selectByPrimaryKey(anWeChatEvent.getFromUserName());
        final String tmpKey = anWeChatEvent.getEventKey();
        if (weChatInfo != null && StringUtils.isNotBlank(tmpKey)) {
            final String qrcodeKey = wechatQrcodePrefix + tmpKey;
            final CustOperatorInfo custOperator = JedisUtils.getObject(qrcodeKey);
            if (custOperator != null) {
                JedisUtils.delObject(qrcodeKey);
                final CustOperatorInfo operator = UserUtils.getOperatorInfo();
                if (operator != null){
                    final String userKey = wechatUserPrefix + operator.getId();
                    JedisUtils.setObject(userKey, Boolean.TRUE, qCodeTimeOut);
                }
                custOperator.setContIdentNo(weChatInfo.getOpenId());
                weChatInfo.setOperId(custOperator.getId());
                weChatInfo.setOperName(custOperator.getName());
                weChatInfo.setOperOrg(custOperator.getOperOrg());
                // this.updateByPrimaryKey(weChatInfo);
                return wechatService.saveWeChatInfo(weChatInfo);
            }
        }
        return null;
    }


}
