package com.oristartech.cinema.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class TicketDBUtil {
	private final static String TYPE_SQLSERVER = "mysql";
	public static final String driverName = "com.mysql.jdbc.Driver";
	public static final String dbURL = "jdbc:mysql://58.67.170.156/DCS_DW";
	public static final String userName = "dcs_bj";
	public static final String userPwd = "bj@123456";
	public static final String dbIP = "58.67.170.156";
	public static final String dbPort = "3306";
	public static final String dbName = "DCS_DW";
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
