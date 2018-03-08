package com.desigmodel.template;

public class BankTemplateMain {

	public static void main(String[] args) {
		BankTemplate template = new GongShangBank();
		template.doSomeThings();
		System.out.println("####################");
		BankTemplate template_1 = new ZhaoShangBank();
		template_1.doSomeThings();
	}
}
