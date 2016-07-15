package com.betterjr.common.mq.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RocketMQListener 注解
 * 
 * @author liuwl
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RocketMQListener {
    /**
     * 消费的topic 在一个 consumer中不允许重复
     */
    String topic();

    /**
     * 接入 RocketMQConsumer 必须是Spring容器中存在的一个RocketMQConsumer实例
     * 
     * @return
     */
    String consumer();
}
