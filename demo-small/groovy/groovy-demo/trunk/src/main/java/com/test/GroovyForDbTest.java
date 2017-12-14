package com.test;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import com.alibaba.druid.pool.DruidDataSource;
import groovy.lang.GroovyObject;
import groovy.util.GroovyScriptEngine;

public class GroovyForDbTest {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		DruidDataSource ds = new DruidDataSource();
		ds.setUrl("jdbc:mysql://120.25.253.57:13306/bbs?useUnicode=true&characterEncoding=UTF-8");
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUsername("root");
		ds.setPassword("r123456");
		Connection conn = ds.getConnection();
		ResultSet rs = conn.prepareStatement("select * from test").executeQuery();
		rs.next();
		// 1.get db text
		String text = rs.getString("content_");
		rs.close();
		conn.close();
		ds.close();
		// 2.writer file
		File file = new File("c:/a/HelloWord.groovy");
		FileWriter writer = new FileWriter(file);
		writer.write(text);
		writer.flush();
		writer.close();
		// 3.load groovy script
		String[] roots = new String[] { file.getParent() };
        //通过指定的roots来初始化GroovyScriptEngine  
        GroovyScriptEngine engine = new GroovyScriptEngine(roots);  
        GroovyObject groovyObject = (GroovyObject) engine.loadScriptByName("HelloWord.groovy").newInstance();  
        // 4.delete file
        file.deleteOnExit();
        
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("param1", "1");
        param.put("param2", 2);
        param.put("param3", 3D);
        
        // 设置全局变量
        groovyObject.setProperty("global", "global");
        
       Object result = groovyObject.invokeMethod("method1", new Object[]{"1", 2, 3D});
       System.out.println("method1 = " + result);
       Map<String, Object> resultMap = (Map<String, Object>) groovyObject.invokeMethod("method2", param);
       for(Map.Entry<String, Object> e : resultMap.entrySet()) {
    	   System.out.println("key = " + e.getKey() + "；value = " + e.getValue());
       }
	}
}
