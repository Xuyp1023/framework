package com.betterjr.mapper.mapperhelper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 名字转换样式，注解的优先级高于全局配置
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface NameStyle {

    Style value() default Style.normal;

}
