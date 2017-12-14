package com.bpm.framework.plugins.mybatis.dialect;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.reflection.wrapper.BeanWrapper;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.util.StringUtils;

import com.bpm.framework.console.Application;
import com.bpm.framework.plugins.mybatis.page.bean.Order;
import com.bpm.framework.plugins.mybatis.page.bean.PageBounds;
import com.bpm.framework.utils.ClassUtils;
import com.bpm.framework.utils.CollectionUtils;

/**
 * 类似hibernate的Dialect,但只精简出分页部分
 * 
 * @author badqiu
 * @author miemiedev
 * @author andyLee
 */
public class Dialect implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4509789964348271061L;
	
	public static final String DATA_AUTH_METHOD = "getRequestOrganizationId";
	public static final String DATA_AUTH_SUB_SQL = " and request_organization_id in(?) ";

	protected TypeHandlerRegistry typeHandlerRegistry;
	protected MappedStatement mappedStatement;
	protected PageBounds pageBounds;
	protected Object parameterObject;
	protected BoundSql boundSql;
	protected List<ParameterMapping> parameterMappings;
	protected Map<String, Object> pageParameters = new HashMap<String, Object>();

	private String pageSQL;
	private String countSQL;

	public Dialect(MappedStatement mappedStatement, Object parameterObject, PageBounds pageBounds) {
		this.mappedStatement = mappedStatement;
		this.parameterObject = parameterObject;
		this.pageBounds = pageBounds;
		this.typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();

		init();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void init() {
		boundSql = mappedStatement.getBoundSql(parameterObject);
		parameterMappings = new ArrayList(boundSql.getParameterMappings());
		if (parameterObject instanceof Map) {
			pageParameters.putAll((Map) parameterObject);
		} else {
			MetaObject metaObject = mappedStatement.getConfiguration().newMetaObject(parameterObject);
			BeanWrapper wrapper = new BeanWrapper(metaObject, parameterObject);
			for (ParameterMapping parameterMapping : parameterMappings) {
				PropertyTokenizer prop = new PropertyTokenizer(parameterMapping.getProperty());
				pageParameters.put(parameterMapping.getProperty(), wrapper.get(prop));
			}
		}

		StringBuffer bufferSql = new StringBuffer(boundSql.getSql().trim());
		if (bufferSql.lastIndexOf(";") == bufferSql.length() - 1) {
			bufferSql.deleteCharAt(bufferSql.length() - 1);
		}
		// 构建sql modify by lixx 2015-10-14 16:10:00
		String sql = buildSql(bufferSql.toString());
		
		pageSQL = sql;
		if (pageBounds.getOrders() != null && !pageBounds.getOrders().isEmpty()) {
			pageSQL = getSortString(sql, pageBounds.getOrders());
		}
		if (pageBounds.getOffset() != RowBounds.NO_ROW_OFFSET || pageBounds.getLimit() != RowBounds.NO_ROW_LIMIT) {
			pageSQL = getLimitString(pageSQL, "__offset", pageBounds.getOffset(), "__limit", pageBounds.getLimit());
		}

		countSQL = getCountString(sql);
	}

	public List<ParameterMapping> getParameterMappings() {
		return parameterMappings;
	}

	public Object getParameterObject() {
		return pageParameters;
	}

	public String getPageSQL() {
		return pageSQL;
	}

	protected void setPageParameter(String name, Object value, Class<?> type) {
		ParameterMapping parameterMapping = new ParameterMapping.Builder(mappedStatement.getConfiguration(), name, type)
				.build();
		parameterMappings.add(parameterMapping);
		pageParameters.put(name, value);
	}

	public String getCountSQL() {
		return countSQL;
	}

	/**
	 * 将sql变成分页sql语句
	 */
	protected String getLimitString(String sql, String offsetName, int offset, String limitName, int limit) {
		throw new UnsupportedOperationException("paged queries not supported");
	}

	/**
	 * 将sql转换为总记录数SQL
	 * 
	 * @param sql
	 *            SQL语句
	 * @return 总记录数的sql
	 */
	protected String getCountString(String sql) {
		return "select count(1) from (" + sql + ") tmp_count";
	}

	/**
	 * 将sql转换为带排序的SQL
	 * 
	 * @param sql
	 *            SQL语句
	 * @return 总记录数的sql
	 */
	protected String getSortString(String sql, List<Order> orders) {
		if (orders == null || orders.isEmpty()) {
			return sql;
		}

		StringBuffer buffer = new StringBuffer("select * from (").append(sql).append(") temp_order order by ");
		for (Order order : orders) {
			if (order != null) {
				buffer.append(order.toString()).append(", ");
			}
		}
		buffer.delete(buffer.length() - 2, buffer.length());
		return buffer.toString();
	}
	
	/**
	 * 
	 * 数据权限处理，把查询语句自动拼接数据权限语句
	 * 
	 * 注意：mapper文件的命名空间对应的实体类必须包含<code>Dialect.DATA_AUTH_METHOD</code>字段
	 * 
	 * @author andyLee
	 * @createDate 2015-10-14 11:10:00
	 */
	@SuppressWarnings("unchecked")
	private String buildSql(String sql) {
		try {
			// 判断是否需要数据权限过滤
			if(!Application.getInstance().needDataAuth()) {
				return sql;
			}
			
			// 如果不为查询sql，则直接执行下一个拦截器
			if(mappedStatement.getSqlCommandType() != SqlCommandType.SELECT) {
				return sql;
			}
			
			// 得到当前人有数据权限的组织
//			List<Long> roleIds = ControllerUtils.getCurrentRoleIds();
//			if(CollectionUtils.isEmpty(roleIds)) {
//				return sql;
//			}
			
			// 判断sql中查询的表是否存在数据权限过滤关键字段
			int index = mappedStatement.getId().lastIndexOf(".");
			String clazzName = mappedStatement.getId().substring(0, index);
			Class<?> clazz = ClassUtils.forName(clazzName);
			Method[] methods = clazz.getMethods();
			boolean isContainsDataAuthField = false;// 包含数据权限过滤的字段标识
			for(Method m : methods) {
				if(DATA_AUTH_METHOD.equalsIgnoreCase(m.getName())) {
					isContainsDataAuthField = true;
					break;
				}
			}
			
			// 如果不包含数据权限过滤的字段，则直接执行下一个拦截器
			if(!isContainsDataAuthField) {
				return sql;
			}
			
			// 根据权限得到组织
			Set<Long> orgIds = new HashSet<>();// 去除重复
//			for(Long key : roleIds) {
//				List<Long> value = DataAuthCache.getInstance().get(key);
//				if(CollectionUtils.isEmpty(value)) continue;
//				orgIds.addAll(value);
//			}
			
			// 如果没有设置数据权限，则查不到数据
			if(CollectionUtils.isEmpty(orgIds)) {
				orgIds.add(0L);
			}
			
			StringBuilder newSql = new StringBuilder();
			newSql.append(sql);
			if(sql.toLowerCase().contains(" order ") && sql.toLowerCase().contains(" by ")) {
				index = sql.toLowerCase().indexOf(" order ");
				newSql.insert(index, " " + DATA_AUTH_SUB_SQL.replace("?", StringUtils.arrayToCommaDelimitedString(orgIds.toArray())) + " ");
			} else {
				newSql.append(" ").append(DATA_AUTH_SUB_SQL.replace("?", StringUtils.arrayToCommaDelimitedString(orgIds.toArray()))).append(" ");
			}
			return newSql.toString();
		} catch(Exception e) {
			return sql;
		}
	}
}
