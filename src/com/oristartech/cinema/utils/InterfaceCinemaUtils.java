package com.oristartech.cinema.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InterfaceCinemaUtils {
	/**
	 * rs转json
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 * @throws JSONException
	 * @author xy
	 * @Time 2017-08-14
	 */
	public static String resultSetToJson(ResultSet rs) throws SQLException, JSONException {
		// json数组
		JSONArray array = new JSONArray();
		// 获取列数
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		// 遍历ResultSet中的每条数据
		while (rs.next()) {
			JSONObject jsonObj = new JSONObject();
			// 遍历每一�?
			for (int i = 1; i <= columnCount; i++) {
				String columnName = metaData.getColumnLabel(i);
				String value = rs.getString(columnName);
				jsonObj.put(columnName, value);
			}
			array.put(jsonObj);
		}
		return array.toString();
	}

	/**
	 * 通过传入sql返回json
	 * 
	 * @param sql
	 * @return
	 * @author xy
	 * @Time 2017-08-14
	 */
	public static String getJsonBySql(String sql) {
		try {
			java.sql.Connection conn = Cinema365DBUtil.getDBConn();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = null;
			rs = pstmt.executeQuery();
			return resultSetToJson(rs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} //
	}

	/**
	 * 通过传入sql返回list
	 * 
	 * @param sql
	 * @return
	 * @author xy
	 * @Time 2017-08-14
	 */
	public static ArrayList getListBySql(String sql) {
		ArrayList<Object> list = new ArrayList<Object>();
		try {
			java.sql.Connection conn = Cinema365DBUtil.getDBConn();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = null;
			rs = pstmt.executeQuery();
			ResultSetMetaData md = rs.getMetaData();
			int columnCount = md.getColumnCount();
			while (rs.next()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					map.put(md.getColumnName(i), rs.getObject(i));
				}
				list.add(map);
			}
			pstmt.close();
			rs.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} //
		return list;
	}

	/**
	 * 通过sql查询返回某字段�??
	 * 
	 * @param sql
	 * @return
	 * @author xy
	 * @Time 2017-08-14
	 */
	public static Object getValueBySql(String sql) {
		Object count = "";
		try {
			java.sql.Connection conn = Cinema365DBUtil.getDBConn();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = null;
			rs = pstmt.executeQuery();
			while (rs.next()) {
				count = rs.getObject(1);
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * 通过sql返回查询的字符拼接串
	 * 
	 * @param sql
	 * @return
	 * @author xy
	 * @Time 2017-08-14
	 */
	public static String getStringsBySql(String sql) {
		String Strings = "";
		try {
			java.sql.Connection conn = Cinema365DBUtil.getDBConn();
			int i = 0;
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = null;
			rs = pstmt.executeQuery();
			for (int j = 0; rs.next(); j++) {
				if (i > 0)
					Strings = Strings + ",'" + rs.getObject(1) + "'";
				else
					Strings = "'" + rs.getObject(1) + "'";
				i++;
			}
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(Strings);
		return Strings;
	}
}
