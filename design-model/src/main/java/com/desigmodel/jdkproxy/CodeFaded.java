package com.desigmodel.jdkproxy;

/**
 * 
 * @author xxx
 * 
 * JDK动态代理:
 	* JDK动态代理所用到的代理类在程序调用到代理类对象时才由JVM真正创建，JVM根据传进来的 业务实现类对象 以及 方法名 ，
 	* 动态地创建了一个代理类的class文件并被字节码引擎执行，然后通过该代理类对象进行方法调用。我们需要做的，只需指定代理类的预处理、调用后操作即可.
 	* 被代理的类要去实现接口
 */
public interface CodeFaded {

	/**写代码*/
	public void writeCode();
}
