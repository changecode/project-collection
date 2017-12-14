package com.test;

class HelloWord {
	
	def global;
	
	Object method1(param1, param2, param3) {
		def param = param1 + " : " + param2 + " : " + param3 + " : " + global;
		return param;
	}
	
	Object method2(Map<String, Object> paramMap) {
		for(def i : paramMap) {
			i.value = i.value + "xx";
		}
		return paramMap;
	}
} 
 