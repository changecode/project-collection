package com.bpm.framework.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 标识类/方法是否需要进行session验证
 * 
 * @author andyLee
 * @createDate 2016-04-29 13:30:00
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
public @interface ValidateSession {
	
	/**
	 * 
	 * 是否需要验证session
	 * 
	 * @return  true需要验证session     false 不需要验证session
	 */
	boolean validate();
}
