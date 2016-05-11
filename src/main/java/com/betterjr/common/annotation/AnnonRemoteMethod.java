package com.betterjr.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.betterjr.common.data.BusinClassType;

/**
 * 
 * @author zhoucy 标注方法调用的入参定义，具体业务， 业务类别，已经业务类型
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface AnnonRemoteMethod {
    String businFlag() default "";

    String custType() default "0";

    BusinClassType businClass();
}
