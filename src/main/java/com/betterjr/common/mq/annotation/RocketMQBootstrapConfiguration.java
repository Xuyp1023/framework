package com.betterjr.common.mq.annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

import com.betterjr.common.mq.config.RocketMQListenerConfigUtils;

/**
 * @see EnableRocketMQ
 */
@Configuration
public class RocketMQBootstrapConfiguration {

    @Bean(name = RocketMQListenerConfigUtils.ROCKETMQ_ANNOTATION_PROCESSOR_BEAN_NAME)
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public RocketMQAnnotationBeanPostProcessor rocketMQListenerAnnotationProcessor() {
        return new RocketMQAnnotationBeanPostProcessor();
    }

}
