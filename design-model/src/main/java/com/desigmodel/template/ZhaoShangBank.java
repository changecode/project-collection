package com.desigmodel.template;

public class ZhaoShangBank extends BankTemplate{

	@Override
	protected void getNumber() {
		System.out.println("在招商银行取号");
	}

	@Override
	protected void doing() {
		System.out.println("在招商银行处理业务");
	}

	@Override
	protected void assess() {
		System.out.println("在招商银行对服务做出评价");
	}

}
