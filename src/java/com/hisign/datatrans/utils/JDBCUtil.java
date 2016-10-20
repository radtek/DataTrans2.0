package com.hisign.datatrans.utils;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCUtil {
	
	static {
		Driver driver = new oracle.jdbc.driver.OracleDriver();
		try {
			DriverManager.registerDriver(driver);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	public static Connection getConnection(String url, String user, String password) throws SQLException {
		Connection connection = DriverManager.getConnection(url, user, password);
		return connection;
	}
	
	public static void close(ResultSet rs, Statement stmt, Connection conn) {
		try {
			if (null != rs) rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		try {
			if (null != stmt) stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		try {
			if (null != conn) conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
}
