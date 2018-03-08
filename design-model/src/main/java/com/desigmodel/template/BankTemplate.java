package com.desigmodel.template;

/**
 * 定义银行业务处理抽象类
 * 业务流程: 取号 --》办理业务 --》 服务评价
 * @author xxx
 * 
 * 模板模式:
	 * 完成一件事情，有固定的数个步骤，但是每个步骤根据对象的不同，而实现细节不同；
	 * 就可以在父类中定义一个完成该事情的总方法，按照完成事件需要的步骤去调用其每个步骤的实现方法。
	 * 每个步骤的具体实现，由子类完成.
 */
public abstract class BankTemplate {

	/**
	 * 标准流程定义方法
	 */
	public void doSomeThings() {
		this.getNumber();
		this.doing();
		this.assess();
	}
	
	/**取号*/
	protected abstract void getNumber();
	
	/**办理业务*/
	protected abstract void doing();
	
	/**服务评价*/
	protected abstract void assess();
	
}
