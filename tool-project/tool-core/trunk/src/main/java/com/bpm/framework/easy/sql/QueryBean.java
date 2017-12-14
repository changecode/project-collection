package com.bpm.framework.easy.sql;

import java.util.ArrayList;

/**
 * 
 * 
 * @author lixx
 * @since 1.0
 */
public class QueryBean {

	private String key;

	private ArrayList<QueryDialect> queryDialects;
	
//	private SqlBean  sqlBean = new SqlBean() ;
	
	public QueryBean(){
		queryDialects = new ArrayList<QueryDialect>() ;
	}
	

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public ArrayList<QueryDialect> getQueryDialects() {
		return queryDialects;
	}

	public void setQueryDialects(ArrayList<QueryDialect> queryDialects) {
		this.queryDialects = queryDialects;

//		String dbType = Application.getInstance().getProp("app.db.type");
//		String hql = "";
//		for (QueryDialect q : queryDialects) {
//			if (SqlBean.db_hql.equals(q.getDb()))
//				hql = q.getValue();
//			if (dbType.equals(q.getDb())) {
//				this.sqlBean.setKey(key) ;
//				this.sqlBean.setType(SqlBean.db_sql) ;
//				this.sqlBean.setValue( q.getValue()) ;
//				break;
//			}
//		}
//		if ("".equals(hql)) {
//			throw new RuntimeException("sqlconfig(Query type) key '" + key
//					+ "' is not local Sql or hql .");
//		}
//		this.sqlBean.setKey(key) ;
//		this.sqlBean.setType(SqlBean.db_hql) ;
//		this.sqlBean.setValue( hql ) ;
		
	}

//	public SqlBean getSqlBean() {
//		return sqlBean;
//	}
//
//	public void setSqlBean(SqlBean sqlBean) {
//		this.sqlBean = sqlBean;
//	}
	
}
