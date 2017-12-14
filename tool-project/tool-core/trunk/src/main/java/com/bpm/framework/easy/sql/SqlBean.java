package com.bpm.framework.easy.sql;

/**
 * 
 * 
 * @author lixx
 * @since 1.0
 */
public class SqlBean {

	private String key;

	private String value;

	private String type = "sql";
	
	public final static String db_hql = "hql" ;
	public final static String db_sql = "sql" ;

	public SqlBean() {
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
