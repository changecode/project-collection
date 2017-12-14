package cn.sunline.server.impl;

import cn.sunline.server.DemoService;

public class DemoServiceImpl implements DemoService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 695753869118240506L;

	@Override
	public String hello(String msg) {
		return "dubbo server msg = " + msg;
	}
}
