// Copyright (c) 2014-2016 Betty. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2016年9月12日, liuwl, creation
// ============================================================================
package com.betterjr.modules.wechat.dubbo.interfaces;

import java.util.Map;

import com.betterjr.common.annotation.NoSession;
import com.betterjr.modules.wechat.data.EventType;
import com.betterjr.modules.wechat.data.MPAccount;
import com.betterjr.modules.wechat.data.api.AccessToken;
import com.betterjr.modules.wechat.data.event.BasicEvent;
import com.betterjr.modules.wechat.entity.CustWeChatInfo;

/**
 * @author liuwl
 *
 */
public interface ICustWeChatService {

    /**
     * @param anId
     * @return
     */
    boolean checkWeChatInfoByOperId(Long anId);

    /**
     *
     */
    @NoSession
    MPAccount getMpAccount();

    /**
     * @param anWeChatEvent
     * @param anEventType
     * @return
     */
    @NoSession
    CustWeChatInfo saveWeChatInfo(BasicEvent anWeChatEvent, EventType anEventType);

    /**
     * @param anFromUserName
     * @return
     */
    CustWeChatInfo findWeChatInfo(String anFromUserName);

    /**
     * @param anWeChatInfo
     * @return
     */
    @NoSession
    CustWeChatInfo saveWeChatInfo(CustWeChatInfo anWeChatInfo);

    /**
     *
     * @param anToken
     * @return
     */
    Map<String, Object> saveLogin(final AccessToken anToken);

    /**
     * @param anOperId
     * @return
     */
    boolean checkFristLogin(Long anOperId);

    /**
     * @param anTradePassword
     * @return
     */
    String saveCheckFristLogin(String anTradePassword);
}
