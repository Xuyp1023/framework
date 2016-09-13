package com.betterjr.modules.wechat.data.message;

import com.betterjr.modules.wechat.data.MessageType;

/**
 * 客服消息转发
 *
 * @author zhoucy
 */
public class CustomerServiceMsg extends BasicMsg {


    public CustomerServiceMsg() {
        this.msgType = MessageType.transfer_customer_service.name();
    }

    @Override
    public String toString() {
        return "CustomerServiceMsg [toUserName="
               + toUserName
               + ", fromUserName="
               + fromUserName
               + ", createTime="
               + createTime
               + ", msgType="
               + msgType
               + "]";
    }

}
