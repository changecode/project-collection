package org.mybatis.generator.ant.utils;

import java.io.Serializable;

import org.mybatis.generator.ant.GeneratorAntTask;

import com.bpm.framework.utils.StringUtils;

public abstract class GeneratorUtils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7220468623747027027L;

	public static final String CONFIGFILE = "generatorConfig-run.xml";
	
	public static final void generator(String configFile) {
		
		if(StringUtils.isNullOrBlank(configFile)) {
			configFile = GeneratorUtils.CONFIGFILE;
		}
		
		GeneratorAntTask task = new GeneratorAntTask();
		// generatorConfig-run.xml
		/**
		 * 
		 * 配置文件中驱动jar如果指定了，则以指定的为准，未指定，默认到classpath下面去找名称为：mysql-connector-java-5.1.29.jar的jar
		 * 生成的代码将生成到src下面，避免覆盖已有JAVA代码，建议包名配置个性化一些（见配置文件）
		 */
		String config = GeneratorUtils.class.getClassLoader().getResource(configFile).getPath();
		task.setConfigfile(config);
		task.execute();
	}
}
