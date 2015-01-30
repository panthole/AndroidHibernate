package com.panthole.hibernate.ahibernate.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义数据库字段注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.FIELD})
public @interface Column {
    public abstract String name();

    public abstract String type() default "";

    public abstract int length() default 0;
}