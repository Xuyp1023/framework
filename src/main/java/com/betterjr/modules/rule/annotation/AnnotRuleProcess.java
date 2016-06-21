package com.betterjr.modules.rule.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * 定义规则服务功能；属性信息和对象：RuleFunction 一致；系统将内容解析到RuleFunction中。
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Documented
@Component
public @interface AnnotRuleProcess {
    // 处理过程的名称
    String name();

    // 定义的规则列表，规则定义顺序就是规则的处理顺序
    String[] rules();

    String description() default "";

    String businGroup() default "";

    String errorInfo() default "";
}
