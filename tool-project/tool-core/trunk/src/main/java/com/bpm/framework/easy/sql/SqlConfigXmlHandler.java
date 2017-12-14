package com.bpm.framework.easy.sql;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.jar.JarEntry;

import org.apache.log4j.Logger;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.springframework.util.CollectionUtils;
import org.xml.sax.InputSource;

import com.bpm.framework.SystemConst;
import com.bpm.framework.exception.utils.ExceptionUtils;
import com.bpm.framework.utils.ArrayUtils;
import com.bpm.framework.utils.file.FileUtils;

/**
 * 
 * 
 * @author lixx
 * @since 1.0
 */
public class SqlConfigXmlHandler {

	private static Logger log = Logger.getLogger(SqlConfigXmlHandler.class);
	private static SqlConfigXmlHandler instance;
	private SqlBeanCache sqlBeanCache = SqlBeanCache.getInstance();
	private Mapping mapping;
	private String sqlcofigFile = "/sqlconfig.xml";
	private String sqlconfigmappingFile = (new StringBuilder())
			.append(getPackagePath()).append("/sqlconfig_mapping.xml")
			.toString();

	private SqlConfigXmlHandler() {
		parseXml();
	}

	/**
	 * 
	 * @return
	 */
	public static synchronized SqlConfigXmlHandler getInstance() {
		if (instance == null) {
			instance = new SqlConfigXmlHandler();
		}
		return instance;

	}

	private String getPackagePath() {
		String clsName = SqlConfigXmlHandler.class.getCanonicalName();
		return clsName.substring(0, clsName.lastIndexOf(".")).replace(".", "/");
	}

	public OutputStreamWriter write(String outFile, Object obj)
			throws Exception {
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(
				outFile), SystemConst.ENCODING_UTF8);
		Marshaller mar = new Marshaller(out);
		mar.setEncoding(SystemConst.ENCODING_UTF8);
		mar.setMapping(mapping);
		mar.marshal(obj);
		return out;
	}

	public String toXml(Object obj) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		Marshaller mar = null;
		try {
			mar = new Marshaller(pw);
		} catch (IOException e) {
			ExceptionUtils.throwRuntimeException(e);
		}
		mar.setEncoding(SystemConst.ENCODING_UTF8);
		try {
			mar.setMapping(this.mapping);
		} catch (MappingException e) {
			ExceptionUtils.throwRuntimeException(e);
		}
		try {
			mar.marshal(obj);
		} catch (MarshalException e) {
			ExceptionUtils.throwRuntimeException(e);
		} catch (ValidationException e) {
			ExceptionUtils.throwRuntimeException(e);
		}
		return sw.getBuffer().toString();
	}

	/**
	 * 解析配置文件sqlconfig.xml
	 */
	private void parseXml() {
		try {
			log.info("Loading sql definitions start.");
			Unmarshaller un = new Unmarshaller(SqlBeanMap.class);
			mapping = new Mapping();
			InputSource config = new InputSource(SqlConfigXmlHandler.class
					.getClassLoader().getResourceAsStream(sqlconfigmappingFile));
			mapping.loadMapping(config);
			un.setMapping(mapping);
			// 默认的配置文件
			parseXml0(sqlcofigFile, un);

			/**
			 * 取消自动加载classPathc和jar包中的配置文件，因为这样可能是配置文件不可控，在不同的应用服务器也有不用的表现，有潜在问题
			String[] configFilesName = getCpConfigFileNames();// classpath下的配置文件
			String[] jarConfigFiles = getJarConfigFileNames();// jar包中的配置文件
			String[] configFiles = ArrayUtils.join(configFilesName,
					jarConfigFiles);

			for (String configFile : configFiles) {
				parseXml0("/" + configFile, un);
			}
			*/
			log.info("Loading sql definitions start end, total "
					+ sqlBeanCache.size() + " sqls.");
		} catch (Exception e) {
			// 如果初始化出错，那么就清空sqlBeanMap和instance，以便下次加载时可以再对sqlBeanMap进行初始化
			instance = null;
			ExceptionUtils.throwRuntimeException(e);
		}
	}

	/**
	 * 获取classpath下的配置文件名称
	 * 
	 * @return
	 */
	private String[] getCpConfigFileNames() {
		File[] configFiles = FileUtils.getFilesFromClasspath("sqlconfig-*.xml");
		if (ArrayUtils.isEmpty(configFiles)) {
			return new String[0];
		}
		String[] configFilesName = new String[configFiles.length];
		for (int i = 0; i < configFiles.length; i++) {
			configFilesName[i] = configFiles[i].getName();
		}
		return configFilesName;
	}

	/**
	 * 获取jar包中的配置文件名称
	 * 
	 * @return
	 */
	private String[] getJarConfigFileNames() {
		JarEntry[] jar = FileUtils.getFilesFromJar("bpm*.jar",
				"sqlconfig-*.xml");
		if (ArrayUtils.isEmpty(jar)) {
			return new String[0];
		}
		String[] jarConfigFiles = new String[jar.length];
		for (int i = 0; i < jarConfigFiles.length; i++) {
			jarConfigFiles[i] = jar[i].getName();
		}
		return jarConfigFiles;
	}

	/**
	 * 递归解析配置文件的方法，同主解析方法parseXml调用
	 * 
	 * @param filepath
	 * @param un
	 * @throws Exception
	 */
	private void parseXml0(String filepath, Unmarshaller un) throws Exception {
		log.info("Loading sql definitions [" + filepath + "].");
		try {
			InputStream is = SqlConfigXmlHandler.class
					.getResourceAsStream(filepath);
			InputStreamReader in = new InputStreamReader(is,
					SystemConst.ENCODING_UTF8);
			SqlBeanMap tempSqlBeanMap = (SqlBeanMap) un.unmarshal(in);

			sqlBeanCache.put(tempSqlBeanMap);
			if (!CollectionUtils.isEmpty(tempSqlBeanMap.getSqlFiles())) {
				for (SqlFile sqlFile : tempSqlBeanMap.getSqlFiles()) {
					parseXml0(sqlFile.getResource(), un);
				}
			}
		} catch (Exception e) {
			log.error( ExceptionUtils.getFullStackTrace(e) ) ;
			throw e;
		}
	}

	public SqlBean getSqlBean(String key) {
		SqlBean sqlBean = sqlBeanCache.get(key);
		if (sqlBean == null) {
			throw new RuntimeException("sql definitions don't contain sql[key="
					+ key + "].");
		} else {
			return sqlBean;
		}
	}

	public static void main(String[] args) throws Exception {
		SqlConfigXmlHandler handler = SqlConfigXmlHandler.getInstance();
		for (int i = 0; i < 10; i++) {
			SqlBean bean = handler.getSqlBean("fw.app.test");
			log.info(i + " : " + bean.getValue() + " : " + bean.getType());
		}
	}
}
