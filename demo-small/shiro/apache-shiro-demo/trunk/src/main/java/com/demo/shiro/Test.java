package com.demo.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

public class Test {

	public static void main(String[] args) {
		// 1.初始化配置文件
		Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
		org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
		// 2.设置SecutiryManager
		SecurityUtils.setSecurityManager(securityManager);
		// 3.说去session
		Subject subject = SecurityUtils.getSubject();
		// 4.验证用户名/密码
		UsernamePasswordToken token = new UsernamePasswordToken("test", "test123");
		// 5.登录验证
		subject.login(token);
		// 6.用户名
		System.out.println(subject.getPrincipal());
		// 7.登出
		subject.logout();
	}
}
