package com.betterjr.modules.rule.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.betterjr.modules.rule.entity.RuleFuncType;

/**
 * 定义规则服务功能；属性信息和对象：RuleFunction 一致；系统将内容解析到RuleFunction中。
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface AnnotRuleFunc {
    String name();

    RuleFuncType fundType() default RuleFuncType.MACRO;

    String errorInfo() default "";

    String groupName() default "";

    String description() default "";
}
