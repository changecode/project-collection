package com.bpm.framework.easy.sql;

import java.util.HashMap;
import java.util.Map;

import com.bpm.framework.utils.CollectionUtils;
import com.bpm.framework.utils.StringUtils;

/**
 * 
 * 
 * @author lixx
 * @since 1.0
 */
public class SqlBeanCache {

	private static final SqlBeanCache instance = new SqlBeanCache();

	private static final Map<String, SqlBean> map = new HashMap<String, SqlBean>();

	private SqlBeanCache() {

	}

	public static SqlBeanCache getInstance() {
		return instance;
	}

	public synchronized void put(SqlBeanMap sqlBeanMap) {
		for (SqlBean bean : sqlBeanMap.getItems()) {
			if (map.containsKey(bean.getKey())) {
				throw new RuntimeException("sqlconfig key '" + bean.getKey()
						+ "' is not unique.");
			}
			map.put(bean.getKey(), bean);
		}
		
		//query 节点处理.
		if(CollectionUtils.isNotEmpty(sqlBeanMap.getQueryItems())){
			for (QueryBean bean : sqlBeanMap.getQueryItems() ) {
				
				if (map.containsKey(bean.getKey())) {
					throw new RuntimeException("sqlconfig key '" + bean.getKey()
							+ "' is not unique.");
				}
				
				String dbType = null;
				if(!StringUtils.hasLength(dbType)){
					throw new RuntimeException("cant find attribute 'app.db.type'.");
				}
				SqlBean  sqlBean = new SqlBean() ;
				sqlBean.setKey(bean.getKey()) ;
				st:for (QueryDialect q : bean.getQueryDialects()) {
					if (SqlBean.db_hql.equals(q.getDb())){
						sqlBean.setValue(q.getValue()) ;
						sqlBean.setType(SqlBean.db_hql) ;
					}
					if (dbType.equals(q.getDb())) {
						sqlBean.setValue(q.getValue()) ;
						sqlBean.setType(SqlBean.db_sql) ;
						break st;
					}
				}
				if ( !StringUtils.hasText(sqlBean.getValue()) ) {
					throw new RuntimeException("sqlconfig(Query type) key '" + bean.getKey()
							+ "' is not local Sql or hql .");
				}
				map.put( bean.getKey(), sqlBean );
			}
		}
		
	}

	public SqlBean get(String key) {
		return map.get(key);
	}

	public int size() {
		return map.size();
	}
}
