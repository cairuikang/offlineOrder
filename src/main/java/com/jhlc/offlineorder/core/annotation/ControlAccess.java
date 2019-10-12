package com.jhlc.offlineorder.core.annotation;

import java.lang.annotation.*;

/**
 * 控制统一个方法访问多次导致
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ControlAccess {
    String value();
}
