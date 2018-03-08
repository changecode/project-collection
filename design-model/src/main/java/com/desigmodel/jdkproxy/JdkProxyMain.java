package com.desigmodel.jdkproxy;

public class JdkProxyMain {

	public static void main(String[] args) {
		JavaCodeFadedImpl imp = new JavaCodeFadedImpl();
		JavaCodeFadeProxy proxy = new JavaCodeFadeProxy();
		
		CodeFaded fade = (CodeFaded)proxy.bind(imp);
		fade.writeCode();
	}
	
	
}
