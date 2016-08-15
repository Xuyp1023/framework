package com.betterjr.common.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mq.codec.MQCodecType;
import com.betterjr.common.mq.core.RocketMQProducer;
import com.betterjr.common.mq.message.MQMessage;
import com.betterjr.common.notification.NotificationConstants;
import com.betterjr.common.notification.NotificationModel;
import com.betterjr.common.utils.BTAssert;

/**
 * 消息发送Service
 * 
 * @author liuwl
 *
 */
public class NotificationService {
    private final static Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private RocketMQProducer producer;

    public NotificationService(final RocketMQProducer anProducer) {
        this.producer = anProducer;
    }

    /**
     * 发送消息通知 NotificationModel
     */
    public boolean sendNotifition(NotificationModel anNotificationModel) {
        final MQMessage message = new MQMessage(NotificationConstants.NOTIFICATION_TOPIC, MQCodecType.FST);
        message.setObject(anNotificationModel);

        try {
            final SendResult sendResult = producer.sendMessage(message);

            if (sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                return true;
            }
            else {
                logger.warn("消息通知发送失败 SendResult=" + sendResult.toString());
                return false;
            }
        }
        catch (Exception e) {
            logger.error("消息通知发送错误", e);
            return false;
        }
    }
}
