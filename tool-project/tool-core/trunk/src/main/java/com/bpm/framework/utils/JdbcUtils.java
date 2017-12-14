package com.bpm.framework.utils;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.bpm.framework.exception.FrameworkRuntimeException;

public abstract class JdbcUtils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -969878441123412591L;

	/**
	 * 
	 * 注意事项：该方法返回的connection不会自动提交事务
	 * 
	 * @param user
	 * @param password
	 * @param url
	 * @param driver
	 * @return
	 */
	public static Connection getConnection(String user, 
			String password, String url, String driver) {
		try {
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(false);
			return conn;
		} catch(Exception e) {
			throw new FrameworkRuntimeException(e);
		}
	}
	
	public static void close(Connection conn) {
		try {
			if(null != conn) {
				conn.close();
				conn = null;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void close(PreparedStatement pstmt) {
		try {
			if(null != pstmt) {
				pstmt.close();
				pstmt = null;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void close(ResultSet rs) {
		try {
			if(null != rs) {
				rs.close();
				rs = null;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		close(rs);
		close(pstmt);
		close(conn);
	}
}
