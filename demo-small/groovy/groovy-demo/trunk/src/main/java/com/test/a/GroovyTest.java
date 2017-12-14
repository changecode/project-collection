package com.test.a;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import groovy.lang.GroovyObject;
import groovy.util.GroovyScriptEngine;

public class GroovyTest {

	public static void main(String[] args) throws Exception {
		String[] roots = new String[] { new File(GroovyTest.class.getResource("/").getPath()).getParentFile().getParent() + "/src/main/java/com/test/a" };
        //通过指定的roots来初始化GroovyScriptEngine  
        GroovyScriptEngine engine = new GroovyScriptEngine(roots);  
        GroovyObject groovyObject = (GroovyObject) engine.loadScriptByName("ChildC.groovy").newInstance();  
        
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("param1", "1");
        param.put("param2", 2);
        param.put("param3", 3D);
        
       Object result = groovyObject.invokeMethod("method1", new Object[]{"1", 2, 3D});
       System.out.println("method1 = " + result);
	}
}
