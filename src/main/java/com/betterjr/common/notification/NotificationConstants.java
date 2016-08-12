package com.betterjr.common.notification;

public interface NotificationConstants {
    // 消息队列
    String NOTIFICATION_TOPIC = "NOTIFICATION_TOPIC";
    // 消息通道消息队列
    String NOTIFICATION_INBOX_TOPIC = "NOTIFICATION_INBOX_TOPIC";
    String NOTIFICATION_EMAIL_TOPIC = "NOTIFICATION_EMAIL_TOPIC";
    String NOTIFICATION_SMS_TOPIC = "NOTIFICATION_SMS_TOPIC";
    
    Boolean IS_DELETED_FALSE = Boolean.FALSE;
    Boolean IS_DELETED_TRUE = Boolean.TRUE;
    
    Boolean IS_READ_FALSE = Boolean.FALSE;
    Boolean IS_READ_TRUE = Boolean.TRUE;
    
    String SEND_STATUS_NORMAL = "0";
    String SEND_STATUS_SUCCESS = "1";
    String SEND_STATUS_FAIL = "2";
    
    // 通道类型:0站内消息，1电子邮件，2短信，3微信
    String CHANNEL_INBOX = "0";
    String CHANNEL_EMAIL = "1";
    String CHANNEL_SMS = "2";
    String CHANNEL_WECHAT = "3";
    
    String CUST_NO = "custNo";
    String OPER_ID = "operId";
    String PROFILE_NAME = "profileName";
}
