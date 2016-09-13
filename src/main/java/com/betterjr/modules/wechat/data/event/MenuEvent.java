package com.betterjr.modules.wechat.data.event;

import java.util.Map;

/**
 * 菜单事件
 * 
 * @author zhoucy
 */
public class MenuEvent extends BasicEvent {

    public MenuEvent() {
        super();
    }

    public MenuEvent(Map<String, String> values) {
        super(values);
    }

    @ Override
    public String toString() {
        return "MenuEvent [toUserName="
               + toUserName
               + ", fromUserName="
               + fromUserName
               + ", createTime="
               + createTime
               + ", msgType="
               + msgType
               + ", event="
               + event
               + ", eventKey="
               + eventKey
               + "]";
    }

}
