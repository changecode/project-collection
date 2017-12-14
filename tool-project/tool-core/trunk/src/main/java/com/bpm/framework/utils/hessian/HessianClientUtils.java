package com.bpm.framework.utils.hessian;

import java.io.Serializable;

import com.bpm.framework.console.Application;
import com.bpm.framework.exception.FrameworkRuntimeException;
import com.bpm.framework.utils.Assert;
import com.bpm.framework.utils.StringUtils;
import com.caucho.hessian.client.HessianProxyFactory;

/**
 * 
 * 
 * @author andyLee
 *
 */
public final class HessianClientUtils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5866915313126619199L;

	@SuppressWarnings("unchecked")
	public static final <T> T createProxy(String serviceName, Class<T> interfaceClazz) {
		try {
			Assert.hasLength(serviceName);
			Assert.notNull(interfaceClazz);
			String url = Application.getInstance().getByKey("hessian.service.url") + serviceName;
			HessianProxyFactory factory = new HessianProxyFactory();
			factory.setOverloadEnabled(true);   // 启用重载
			factory.setUser(Application.getInstance().getByKey("hessian.user"));
			factory.setPassword(Application.getInstance().getByKey("hessian.password"));
			return (T) factory.create(interfaceClazz, url);//创建IService接口的实例对象
		} catch(Exception e) {
			throw new FrameworkRuntimeException("create proxy exception：", e);
		}
	}
	
	/**
	 * 
	 * 指定baseUrl
	 * 
	 * @param baseUrl
	 * @param serviceName
	 * @param interfaceClazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static final <T> T createProxy(String baseUrl, String serviceName, Class<T> interfaceClazz) {
		try {
			Assert.hasLength(serviceName);
			Assert.notNull(interfaceClazz);
			if(StringUtils.isNullOrBlank(baseUrl)) {
				return createProxy(serviceName, interfaceClazz);
			}
			String url = baseUrl;
			if(!url.endsWith("/")) {
				url += "/";
			}
			HessianProxyFactory factory = new HessianProxyFactory();
			factory.setOverloadEnabled(true);   // 启用重载
			factory.setUser(Application.getInstance().getByKey("hessian.user"));
			factory.setPassword(Application.getInstance().getByKey("hessian.password"));
			return (T) factory.create(interfaceClazz, url + serviceName);//创建IService接口的实例对象
		} catch(Exception e) {
			throw new FrameworkRuntimeException("create proxy exception：", e);
		}
	}
}
