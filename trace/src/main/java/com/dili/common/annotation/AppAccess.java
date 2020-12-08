package com.dili.common.annotation;


import com.dili.customer.sdk.enums.CustomerEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于session拦截、登陆验证
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AppAccess {
    Role role() default  Role.Manager;
    CustomerEnum.CharacterType[] subRoles();
    String method() default "post";
    String url() default "";
}
