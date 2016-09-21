package com.betterjr.modules.notification.constants;

public interface NotificationConstants {
    // 消息队列
    String NOTIFICATION_TOPIC = "NOTIFICATION_TOPIC";
    // 消息通道消息队列
    String NOTIFICATION_INBOX_TOPIC = "NOTIFICATION_INBOX_TOPIC";
    String NOTIFICATION_EMAIL_TOPIC = "NOTIFICATION_EMAIL_TOPIC";
    String NOTIFICATION_SMS_TOPIC = "NOTIFICATION_SMS_TOPIC";
    String NOTIFICATION_WECHAT_TOPIC = "NOTIFICATION_WECHAT_TOPIC";

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

    // 模板启用状态 1 启用  0 禁用
    String PROFILE_STATUS_ENABLED = "1";
    String PROFILE_STATUS_DISABLED = "0";


    String CUST_NO = "custNo";
    String OPER_ID = "operId";
    String PROFILE_NAME = "profileName";

    // 模板类型  0:平台,1:保理公司,2:核心企业,3:供应商,4:经销商
    String PROFILE_TYPE_PLATFORM = "0";
    String PROFILE_TYPE_FACTOR = "1";
    String PROFILE_TYPE_CORE = "2";
    String PROFILE_TYPE_SUPPLIER = "3";
    String PROFILE_TYPE_SELLER = "4";


    // 是否客户自定义模板
    String PROFILE_CUSTOM = "1";

    // 立即发送
    String IMMEDIATE_TRUE = "1";
    String IMMEDIATE_FALSE = "0";
}
