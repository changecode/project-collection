package com.bpm.framework.plugins.mybatis.page;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class CleanupMybatisPageListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		PageInterceptor.Pool.shutdownNow();
	}
}