package com.bpm.framework.plugins.mybatis.interceptor;

import java.io.Serializable;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.log4j.Logger;

/**
 * 
 * 拦截器抽象类
 * 
 * @author andyLee
 * @createDate 2015-10-27 09:27:00
 */
public abstract class AbstractInterceptor implements Interceptor, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6364820235334383221L;
	
	protected final Logger log = Logger.getLogger(getClass());
	
	// 是否打印sql
	protected boolean showSql = false;

	public boolean isShowSql() {
		return showSql;
	}

	public void setShowSql(boolean showSql) {
		this.showSql = showSql;
	}
}
