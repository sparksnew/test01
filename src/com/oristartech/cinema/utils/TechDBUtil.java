package com.oristartech.cinema.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class TechDBUtil {
	private final static String url = "jdbc:sqlserver://118.190.91.92:1433;DatabaseName=CRM_ChenXing_Final;"
			+ "user=sa;password=Oristart!ECH";
	
	static{
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Connection getDBConn() throws Exception {
		Connection conn = DriverManager.getConnection(url);
		return conn;
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
