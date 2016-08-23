package com.betterjr.common.mq.core;

import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by liuwl on 2016/6/13.
 */
public class RocketMQConsumer implements ApplicationListener<ContextRefreshedEvent> {
    private static final Log logger = LogFactory.getLog(RocketMQConsumer.class);

    /**
     * 消费最小线程数
     */
    private static final int DEFAULT_CONSUME_THREAD_MIN = 20;
    /**
     * 消费最大线程数
     */
    private static final int DEFAULT_CONSUME_THREAD_MAX = 64;
    /**
     * 一次pull消息数量
     */
    private static final int DEFAULT_PULL_BATCH_SIZE = 32;

    /**
     * 消费者实例
     */
    private DefaultMQPushConsumer consumer;

    /**
     * 是否已初始化
     */
    private boolean isStart = false;

    /**
     * 具体消费者列表
     */
    private Map<String, RocketMQMessageListener> messageListeners = new HashMap<>();

    private final String namesrvAddr;

    private final String consumerGroupName;

    public RocketMQConsumer(final String anNamesrvAddr, final String anConsumerGroupName) {
        this.namesrvAddr = anNamesrvAddr;
        this.consumerGroupName = anConsumerGroupName;
    }

    public void initialize() throws Exception {
        if (StringUtils.isEmpty(namesrvAddr)) {
            throw new Exception("namesrvAddr 不允许为空。");
        }

        consumer = new DefaultMQPushConsumer(consumerGroupName);
        consumer.setNamesrvAddr(namesrvAddr);
//        consumer.setMessageModel(MessageModel.CLUSTERING);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        consumer.setConsumeThreadMin(DEFAULT_CONSUME_THREAD_MIN);
        consumer.setConsumeThreadMax(DEFAULT_CONSUME_THREAD_MAX);
        consumer.setPullBatchSize(DEFAULT_PULL_BATCH_SIZE);

        for (Map.Entry<String, RocketMQMessageListener> consumerEntry : messageListeners.entrySet()) {
            RocketMQMessageListener messageListener = consumerEntry.getValue();
            consumer.subscribe(messageListener.getTopic(), messageListener.getTags());
            logger.info("订阅消息topic:" + messageListener.getTopic() + ",tags:" + messageListener.getTags());
        }
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> anMsgs, ConsumeConcurrentlyContext anContext) {
                MessageExt msgExt = anMsgs.get(0);
                return messageListeners.get(msgExt.getTopic()).consumeMessage(anMsgs, anContext);
            }
        });
        start();
    }

    public void start() throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("启动消息消费者" + consumer.getConsumerGroup());
        }

        consumer.start();
        shutdownHook();
    }

    public void shutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                shutdown();
            }
        }));
    }

    public void shutdown() {
        if (logger.isDebugEnabled()) {
            logger.debug("关闭消息消费者" + consumer.getConsumerGroup());
        }
        consumer.shutdown();
    }

    public void addSubscriber(final RocketMQMessageListener anMessageListener) throws Exception {
        if (messageListeners.get(anMessageListener.getTopic()) != null) {
            throw new Exception("同一个Consumer中topic不能重复订阅！");
        }
        messageListeners.put(anMessageListener.getTopic(), anMessageListener);
    }

    public void removeSubscriber(String anTopic) {
        RocketMQMessageListener messageListener = messageListeners.get(anTopic);
        if (messageListener != null) {
            messageListeners.remove(anTopic);
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent anEvent) {
        if (isStart) {
            return;
        }
        isStart = true;

        try {
            initialize();
        }
        catch (Exception e) {
            logger.error("初始化rocketMq消费者失败！", e);
        }
    }

}
