package com.bpm.framework.easy.sql;

import java.util.ArrayList;

public class SqlBeanMap {

	private ArrayList<SqlBean> items;

	private ArrayList<SqlFile> sqlFiles;
	
	private ArrayList<QueryBean> queryItems;

	public SqlBeanMap() {
		items = new ArrayList<SqlBean>();
		queryItems = new ArrayList<QueryBean>() ;
	}

	public ArrayList<SqlBean> getItems() {
		return items;
	}

	public ArrayList<SqlFile> getSqlFiles() {
		return sqlFiles;
	}

	public void setSqlFiles(ArrayList<SqlFile> sqlFiles) {
		this.sqlFiles = sqlFiles;
	}

	public void setItems(ArrayList<SqlBean> items) {
		this.items = items;
	}

	public ArrayList<QueryBean> getQueryItems() {
		return queryItems;
	}

	public void setQueryItems(ArrayList<QueryBean> queryItems) {
		this.queryItems = queryItems;
	}
}
