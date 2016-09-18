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
import com.betterjr.modules.wechat.data.message.TextMsg;

/**
 * @author liuwl
 *
 */
public class DefaultQrcodeHandler implements QrcodeHandler {

    /* (non-Javadoc)
     * @see com.betterjr.modules.wechat.handler.QrcodeHandler#process(com.betterjr.modules.wechat.data.event.BasicEvent)
     */
    @Override
    public BasicMsg process(final BasicEvent anEvent) {
        final TextMsg textMsg = new TextMsg(anEvent);
        textMsg.setContent("欢迎关注前海拜特微信开发公众号!");
        return textMsg;
    }

}
