package com.bpm.framework.utils.i18n;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContext;

public abstract class I18NUtils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 501124345134432262L;

	private static RequestContext getRequestContext() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return new RequestContext(request);
	}
	
	public static String getText(String code) {
		return getRequestContext().getMessage(code);
	}
	
	/**
	 * 
	 * 做try{...}catch{...}处理，因为通过WebService访问的时候，该处无法获取request
	 * 
	 * @param code
	 * @param defaultValue
	 * @return
	 */
	public static String getText(String code, String defaultValue) {
		try {
			return getRequestContext().getMessage(code, defaultValue);
		} catch(Exception e) {
			return defaultValue;
		}
	}
	
	public static String getText(String code, Object[] args) {
		return getRequestContext().getMessage(code, args);
	}
	
	public static String getText(String code, List<?> args) {
		return getRequestContext().getMessage(code, args);
	}
	
	public static String getText(String code, Object[] args, String defaultValue) {
		return getRequestContext().getMessage(code, args, defaultValue);
	}
	
	public static String getText(String code, List<?> args, String defaultValue) {
		return getRequestContext().getMessage(code, args, defaultValue);
	}
}
