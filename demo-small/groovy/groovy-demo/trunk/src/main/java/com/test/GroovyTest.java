package com.test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import groovy.lang.GroovyObject;
import groovy.util.GroovyScriptEngine;

public class GroovyTest {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		String[] roots = new String[] { new File(GroovyTest.class.getResource("/").getPath()).getParentFile().getParent() + "/src/main/java/com/test" };
        //通过指定的roots来初始化GroovyScriptEngine  
        GroovyScriptEngine engine = new GroovyScriptEngine(roots);  
        GroovyObject groovyObject = (GroovyObject) engine.loadScriptByName("HelloWord.groovy").newInstance();  
        
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("param1", "1");
        param.put("param2", 2);
        param.put("param3", 3D);
        
        // 设置全局变量
        groovyObject.setProperty("global", "******global");
        
       Object result = groovyObject.invokeMethod("method1", new Object[]{"1", 2, 3D});
       System.out.println("method1 = " + result);
       Map<String, Object> resultMap = (Map<String, Object>) groovyObject.invokeMethod("method2", param);
       for(Map.Entry<String, Object> e : resultMap.entrySet()) {
    	   System.out.println("key = " + e.getKey() + "；value = " + e.getValue());
       }
	}
}
