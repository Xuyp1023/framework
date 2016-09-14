// Copyright (c) 2014-2016 Betty. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2016年9月13日, liuwl, creation
// ============================================================================
package com.betterjr.modules.notification;

/**
 * @author liuwl
 *
 */
public interface INotificationSendService {
    /**
     * 发送消息接口
     */
    public boolean sendNotification(NotificationModel anNotificationModel);
}
