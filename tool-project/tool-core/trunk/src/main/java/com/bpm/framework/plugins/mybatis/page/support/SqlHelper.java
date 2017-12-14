package com.bpm.framework.plugins.mybatis.page.support;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bpm.framework.plugins.mybatis.dialect.Dialect;

/**
 *
 * @author poplar.yfyang
 * @author miemiedev
 * @author andyLee
 */
public class SqlHelper implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -242148315586031853L;
	
	private static Logger logger = LoggerFactory.getLogger(SqlHelper.class);

    /**
     * 查询总纪录数，该方法的连接不能关闭，否则会导致连接池状态错误
     *
     * @param mappedStatement mapped
     * @param parameterObject 参数
     * @param boundSql        boundSql
     * @param dialect         database dialect
     * @return 总记录数
     * @throws java.sql.SQLException sql查询错误
     */
    public static int getCount(
                               final MappedStatement mappedStatement, final Transaction transaction, final Object parameterObject,
                               final BoundSql boundSql, Dialect dialect) throws SQLException {
        final String count_sql = dialect.getCountSQL();
        logger.debug("Total count SQL [{}] ", count_sql);
        logger.debug("Total count Parameters: {} ", parameterObject);

        Connection connection = transaction.getConnection();
        PreparedStatement countStmt = connection.prepareStatement(count_sql);
        DefaultParameterHandler handler = new DefaultParameterHandler(mappedStatement,parameterObject,boundSql);
        handler.setParameters(countStmt);

        ResultSet rs = countStmt.executeQuery();
        int count = 0;
        if (rs.next()) {
            count = rs.getInt(1);
        }
        logger.debug("Total count: {}", count);
        return count;
    }
}