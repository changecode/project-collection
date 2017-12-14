package com.bpm.framework.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 标识类是否是前台controller
 * 
 * @author andyLee
 * @createDate 2016-04-29 13:30:00
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
public @interface Web {
	
	WebValue value() default WebValue.WEB;
}
