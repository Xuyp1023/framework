package com.betterjr.common.mq.core;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.betterjr.common.mq.codec.MQCodecUtils;
import com.betterjr.common.mq.config.MethodRocketMQListenerEndpoint;
import com.betterjr.common.mq.exception.BetterMqException;
import com.betterjr.common.mq.message.MQMessage;

/**
 * @author liuwl
 */
public class RocketMQMessageListener implements MessageListenerConcurrently {
    private static final Log logger = LogFactory.getLog(RocketMQMessageListener.class);

    private final String topic;
    private final String tags;
    private final Object bean;
    private final Method method;

    /**
     * @param anEndpoint
     */
    public RocketMQMessageListener(final MethodRocketMQListenerEndpoint anEndpoint) {
        this.bean = anEndpoint.getBean();
        this.method = anEndpoint.getMethod();
        this.topic = anEndpoint.getTopic();
        this.tags = anEndpoint.getTags();
    }

    /**
     * @param anMsgs
     * @param anContext
     * @return
     */
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> anMsgs, ConsumeConcurrentlyContext anContext) {
        MessageExt messageExt = anMsgs.get(0);
        try {
            final Object object = MQCodecUtils.unwrap(messageExt);
            if (checkMQMessage(object)) {
                method.invoke(bean, object);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            } else {// TODO 在本系统中此类消息直接丢掉
                logger.error("消息格式不正确 Topic:" + messageExt.getTopic() + " Object:" + object.toString());
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        }
        catch (BetterMqException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | ClassNotFoundException | IOException e) {// TODO 在本系统中此类消息直接丢掉
            logger.error("消息消费失败 Topic:" + messageExt.getTopic(), e);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
    }

    public boolean checkMQMessage(Object anMessage) {
        return (anMessage instanceof MQMessage);
    }

    public String getTopic() {
        return this.topic;
    }

    public String getTags() {
        return tags;
    }
}
