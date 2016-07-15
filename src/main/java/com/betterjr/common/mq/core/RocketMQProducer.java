package com.betterjr.common.mq.core;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.StringUtils;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.betterjr.common.exception.BytterException;
import com.betterjr.common.mq.codec.BtCodecUtils;
import com.betterjr.common.mq.message.BtMessage;

public class RocketMQProducer implements ApplicationListener<ContextRefreshedEvent> {
    private static final Log logger = LogFactory.getLog(RocketMQProducer.class);

    private final String namesrvAddr;

    private final String producerGroupName;

    private DefaultMQProducer producer;

    private boolean isStart = false;

    public RocketMQProducer(final String anNamesrvAddr, final String anProducerGroupName) {
        this.namesrvAddr = anNamesrvAddr;
        this.producerGroupName = anProducerGroupName;
    }

    private void initialize() throws Exception {
        if (StringUtils.isEmpty(namesrvAddr)) {
            throw new Exception("namesrvAddr 不允许为空。");
        }

        producer = new DefaultMQProducer(producerGroupName);
        producer.setNamesrvAddr(namesrvAddr);
        producer.setInstanceName("BtProducer");

        start();
    }

    public SendResult sendMessage(final BtMessage anMessage)
            throws Exception {
        final Message message = BtCodecUtils.wrap(anMessage);
        if (message == null) {
            throw new BytterException("包装消息出错！");
        }
        SendResult sendResult = producer.send(message);
        return sendResult;
    }

    private void start() throws MQClientException {
        if (logger.isDebugEnabled()) {
            logger.debug("启动消息生产者" + producer.getProducerGroup());
        }
        producer.start();
        shutdownHook();
    }

    public void shutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                shutdown();
            }
        }));
    }

    /**
     * 关闭服务
     */
    public void shutdown() {
        if (logger.isDebugEnabled()) {
            logger.debug("关闭消息生产者" + producer.getProducerGroup());
        }
        producer.shutdown();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent arg0) {
        if (isStart) {
            return;
        }
        isStart = true;

        try {
            initialize();
        }
        catch (Exception e) {
            logger.error("初始化rocketMq生产者失败！", e);
        }
    }

}
