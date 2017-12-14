package com.bpm.framework.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.bpm.framework.SystemConst;
import com.bpm.framework.console.Application;

public class InitialServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1429495397499511952L;

	@Override
	public void init() throws ServletException {
		initApplication();
	}
	
	private void initApplication() {
		Application app = Application.getInstance();
		this.getServletContext().setAttribute(SystemConst.APPLICATION_CONTEXT, app);
	}
	
	@Override
	public void destroy() {
		System.exit(0);// 退出jvm
		super.destroy();
	}
}
