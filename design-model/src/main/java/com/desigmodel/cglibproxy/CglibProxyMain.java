package com.desigmodel.cglibproxy;

public class CglibProxyMain {

	public static void main(String[] args) {
		
		CodeFadedImpl impl = new CodeFadedImpl();
		CodeFadedCglibProxy proxy = new CodeFadedCglibProxy();
		
		CodeFadedImpl result = (CodeFadedImpl)proxy.getInstance(impl);
		result.cglib();
	}
}
