package com.desigmodel.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JavaCodeFadeProxy implements InvocationHandler{

	private Object target;
	
	public Object bind(Object _obj) {
		this.target = _obj;
		return Proxy.newProxyInstance(target.getClass().getClassLoader(), 
				target.getClass().getInterfaces(), this);
	}
	
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = null;
		System.out.println("调用代理方法前");
		result = method.invoke(target, args);
		System.out.println("调用代理方法之后");
		return result;
	}

}
