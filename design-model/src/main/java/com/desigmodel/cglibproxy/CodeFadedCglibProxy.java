package com.desigmodel.cglibproxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class CodeFadedCglibProxy implements MethodInterceptor{
	
	private Object targetObject;
	
	/**
	 * 绑定目标对象
	 * @param _obj
	 * @return
	 */
	public Object getInstance(Object _obj) {
		this.targetObject = _obj;
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(this.targetObject.getClass());
		enhancer.setCallback(this);
		return enhancer.create();
	}

	public Object intercept(Object targetobj, Method method, Object[] params, MethodProxy proxy) throws Throwable {
		System.out.println("调用cglib代理方法之前");
		proxy.invokeSuper(targetobj, params);
		System.out.println("调用cglib代理方法之后");
		return null;
	}

}
