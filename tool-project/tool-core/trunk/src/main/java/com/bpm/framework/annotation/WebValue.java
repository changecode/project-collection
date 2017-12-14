package com.bpm.framework.annotation;

public enum WebValue {

	/**
	 * 
	 * 如果指定这里指定了该属性，则表示controller既是前台controller又是后台controller
	 * 
	 */
	ALL,
	
	/**
	 * 
	 * 如果指定这里指定了该属性，则表示controller是前台controller
	 * 
	 */
	WEB,
	
	/**
	 * 
	 * 如果指定这里指定了该属性，则表示controller是后台controller
	 * 
	 */
	ADMIN
}
