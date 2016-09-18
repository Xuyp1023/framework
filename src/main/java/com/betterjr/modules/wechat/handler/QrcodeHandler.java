// Copyright (c) 2014-2016 Betty. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2016年9月18日, liuwl, creation
// ============================================================================
package com.betterjr.modules.wechat.handler;

import com.betterjr.modules.wechat.data.event.BasicEvent;
import com.betterjr.modules.wechat.data.message.BasicMsg;

/**
 * @author liuwl
 *
 */
public interface QrcodeHandler {
    public BasicMsg process(BasicEvent anEvent);
}
