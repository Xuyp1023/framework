package com.betterjr.common.mq.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 使用@Configuration 启用RocketMQ 
 * 通过 RocketMQBootstrapConfiguration 加载 RocketMQAnnotationBeanPostProcessor
 *
 * @author liuwl
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RocketMQBootstrapConfiguration.class)
public @interface EnableRocketMQ {
}
