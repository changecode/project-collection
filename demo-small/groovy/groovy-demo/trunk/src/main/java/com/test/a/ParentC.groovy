package com.test.a;

abstract class ParentC {

	abstract Object invoke();
	
	Object method1(param1, param2, param3) {
		def obj = invoke();
		def param = param1 + " : " + param2 + " : " + param3 + " : " + obj;
		return param;
	}
} 
 