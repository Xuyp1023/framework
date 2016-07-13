package com.betterjr.common.mq.config;

import com.betterjr.common.mq.core.RocketMQConsumer;

public interface RocketMQListenerEndpoint {

    String getId();

    String getTopic();

    void setConsumer(final RocketMQConsumer anConsumer);

    RocketMQConsumer getConsumer();

}
