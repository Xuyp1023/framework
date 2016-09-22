// Copyright (c) 2014-2016 Betty. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2016年9月12日, liuwl, creation
// ============================================================================
package com.betterjr.modules.wechat.dubbo;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.wechat.data.EventType;
import com.betterjr.modules.wechat.data.MPAccount;
import com.betterjr.modules.wechat.data.api.AccessToken;
import com.betterjr.modules.wechat.data.event.BasicEvent;
import com.betterjr.modules.wechat.dubbo.interfaces.ICustWeChatService;
import com.betterjr.modules.wechat.entity.CustWeChatInfo;
import com.betterjr.modules.wechat.service.CustWeChatService;

/**
 * @author liuwl
 *
 */
@Service(interfaceClass = ICustWeChatService.class)
public class CustWeChatDubboService implements ICustWeChatService {
    @Inject
    private CustWeChatService wechatService;


    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.wechat.dubbo.interfaces.ICustWeChatService#findWeChatInfoByOperId(java.lang.Long)
     */
    @Override
    public boolean checkWeChatInfoByOperId(final Long anId) {
        return wechatService.checkWeChatInfoByOperId(anId);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.wechat.dubbo.interfaces.ICustWeChatService#findMpAccount()
     */
    @Override
    public MPAccount getMpAccount() {
        return wechatService.getMpAccount();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.wechat.dubbo.interfaces.ICustWeChatService#saveWeChatInfo(com.betterjr.modules.wechat.data.event.BasicEvent,
     * com.betterjr.modules.wechat.data.EventType)
     */
    @Override
    public CustWeChatInfo saveWeChatInfo(final BasicEvent anWeChatEvent, final EventType anEventType) {
        return wechatService.saveWeChatInfo(anWeChatEvent, anEventType);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.wechat.dubbo.interfaces.ICustWeChatService#findWechatInfo(java.lang.String)
     */
    @Override
    public CustWeChatInfo findWeChatInfo(final String anFromUserName) {
        return wechatService.findWeChatInfo(anFromUserName);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.wechat.dubbo.interfaces.ICustWeChatService#saveWeChatInfo(com.betterjr.modules.wechat.entity.CustWeChatInfo)
     */
    @Override
    public CustWeChatInfo saveWeChatInfo(final CustWeChatInfo anWeChatInfo) {
        return wechatService.saveWeChatInfo(anWeChatInfo);
    }


    @Override
    public Map<String, Object> saveLogin(final AccessToken anToken) {
        final Map<String, Object> result = new HashMap<>();
        final String [] returnMsg = new String[1];
        final CustOperatorInfo operator = wechatService.saveLogin(anToken, returnMsg);
        if (operator != null) {
            result.put("operator", operator);
        } else {
            result.put("message", returnMsg[0]);
        }
        return result;
    }

}
