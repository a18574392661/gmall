package com.atguigu.gmall.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value =ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface LoginRequired {
	//默认是要登录
	   boolean loginSuccess() default true;
}
