package com.betterjr.common.mq.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

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
public @interface EnableRocketMQ {}
