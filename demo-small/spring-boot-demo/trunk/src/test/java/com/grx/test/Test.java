package com.grx.test;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class Test {

	public static void main(String[] args) {
		ExpressionParser parser = new SpelExpressionParser();
		Object value = parser.parseExpression("1 == 1").getValue(boolean.class);
		System.out.println(value);
	}
}
