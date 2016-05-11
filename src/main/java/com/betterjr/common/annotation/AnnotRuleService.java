package com.betterjr.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义所属的Java 对象适应用于规则服务应用
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface AnnotRuleService {

    // 如果未指定名称，则使用类名，第一字母小写
    String value() default "";

    // 定義的類型，默认是功能；可以指定为服务过程。
    RuleServiceType type() default RuleServiceType.FUNC;

    String description() default "";
    String groupName() default "";
}
