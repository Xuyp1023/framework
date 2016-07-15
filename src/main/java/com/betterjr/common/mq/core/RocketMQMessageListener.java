package com.betterjr.common.mq.core;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.betterjr.common.codec.BtCodec;
import com.betterjr.common.codec.BtObjectInput;
import com.betterjr.common.mq.codec.BtCodecUtils;
import com.betterjr.common.mq.codec.BtCodecFactory;
import com.betterjr.common.mq.config.MethodRocketMQListenerEndpoint;

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
            final Object object = BtCodecUtils.unwrap(messageExt);
            method.invoke(bean, object);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        catch (final Exception e) {
            logger.error("消息消费失败 Topic:" + messageExt.getTopic() + " Keys:" + messageExt.getKeys(), e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }

    public String getTopic() {
        return this.topic;
    }

    public String getTags() {
        return tags;
    }
}
