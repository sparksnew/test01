package com.oristartech.cinema.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;

public class Cinema365DBUtil {
	private final static String TYPE_SQLSERVER = "mysql";
	public static final String driverName = "com.mysql.jdbc.Driver";
	public static final String dbURL = "jdbc:mysql://139.129.232.79/smartcinema";
	public static final String userName = "root";
	public static final String userPwd = "x5";
	public static final String dbIP = "139.129.232.79:";
	public static final String dbPort = "3306";
	public static final String dbName = "smartcinema";
	private final static String CONN_SQLSERVER = "jdbc:mysql://";

	public static Connection getDBConn() throws Exception {
		Connection conn = getConn(driverName, TYPE_SQLSERVER, userName, userPwd, dbIP, dbPort);
		return conn;
	}

	public static Connection getConn(String driver, String type, String username, String password, String ip,
			String port) throws Exception {
		Class.forName(driver);
		return DriverManager.getConnection(dbURL, username, password);

	}

	public static void closeConn(Connection conn) {
		try {
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
