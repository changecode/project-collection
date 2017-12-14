package com.bpm.framework.plugins.mybatis.interceptor;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.bpm.framework.utils.json.JsonUtils;

/**
 * 
 * 打印Mybatis执行的sql日志
 * 
 * @author andyLee
 * @createDate 2015-10-14 10:25:00
 */
@Intercepts({ @Signature(type = Executor.class, method = "query", args = {
		MappedStatement.class, Object.class, RowBounds.class,
		ResultHandler.class }), 
			  @Signature(type = Executor.class, method = "update", args = {
				MappedStatement.class, Object.class }) })
public class MybatisLogInterceptor extends AbstractInterceptor {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3179712389512080709L;
	
	@Override
	public Object intercept(final Invocation invocation) throws Throwable {
		final Object[] queryArgs = invocation.getArgs();
		final MappedStatement ms = (MappedStatement) queryArgs[0];
		final Object parameter = queryArgs[1];

		// 判断是否需要打印sql
		if(showSql) {
			String sql = ms.getSqlSource().getBoundSql(parameter).getSql();
			StringBuilder info = new StringBuilder();
			info.append("mybatis sql=").append(sql).append(";")
				.append("mapper id=").append(ms.getId()).append(";")
				.append("mapper file=").append(ms.getResource()).append(";")
				.append("parameters=").append(JsonUtils.fromObject(parameter)).append(";")
				.append("parameter class=").append((null == parameter ? null : parameter.getClass().getName()));
			log.info(info.toString());
		}
		
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
	}
}