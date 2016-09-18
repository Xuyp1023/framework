// Copyright (c) 2014-2016 Betty. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2016年9月18日, liuwl, creation
// ============================================================================
package com.betterjr.modules.wechat.handler;

import org.apache.commons.lang3.StringUtils;

import com.betterjr.common.service.SpringContextHolder;
import com.betterjr.modules.wechat.data.event.BasicEvent;
import com.betterjr.modules.wechat.data.message.BasicMsg;
import com.betterjr.modules.wechat.data.message.TextMsg;
import com.betterjr.modules.wechat.dubboclient.CustWeChatDubboClientService;

/**
 * 绑定事件
 *
 * @author liuwl
 *
 */
public class UserBindQrcodeHandler implements QrcodeHandler {
    final CustWeChatDubboClientService wechatDubboClientService;

    public UserBindQrcodeHandler() {
        wechatDubboClientService = SpringContextHolder.getBean(CustWeChatDubboClientService.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.betterjr.modules.wechat.handler.QrcodeHandler#process(com.betterjr.modules.wechat.data.event.BasicEvent)
     */
    @Override
    public BasicMsg process(final BasicEvent anEvent) {
        final TextMsg textMsg = new TextMsg(anEvent);
        String eventKey = null;
        final String fromUserName = anEvent.getFromUserName();
        if (StringUtils.startsWith(anEvent.getEventKey(), "qrscene_")) {// 关注带参数场景扫描
            eventKey = StringUtils.substring(anEvent.getEventKey(), 8);
        }
        else {
            eventKey = anEvent.getEventKey();
        }
        final String operName = wechatDubboClientService.saveBindingWeChat(eventKey, fromUserName);
        if (StringUtils.isBlank(operName)) {
            textMsg.setContent("欢迎关注前海拜特微信开发公众号! 账户绑定扫描失败，账户绑定扫描只能一次，请重新获取扫描二维码。");
        }
        else {
            textMsg.setContent("账户绑定已扫描，操作员是：" + operName + "，请继续在平台输入交易密码！");
        }

        return textMsg;
    }

}
