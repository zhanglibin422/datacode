package com.huntkey.rx.sceo.orm.annotation;

import java.lang.annotation.*;

/**
 * Created by zhanglb on 2017/6/26.
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {
    String value();
}