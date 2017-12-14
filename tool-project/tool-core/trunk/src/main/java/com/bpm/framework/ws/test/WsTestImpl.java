package com.bpm.framework.ws.test;

public class WsTestImpl implements WsTest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8576440740276510348L;

	@Override
	public Object test(String param) {
		System.out.println("======================请求：" + param);
		return "successfully";
	}
}
