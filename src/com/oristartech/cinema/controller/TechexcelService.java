package com.oristartech.cinema.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.oristartech.cinema.service.BugInfoService;
import com.oristartech.cinema.service.CinemaDeviceInfoService;
import com.oristartech.cinema.utils.DataSourceContextHolder;
import com.oristartech.cinema.utils.RequestToMapUtil;
import com.oristartech.cinema.utils.TechDBUtil;
import com.oristartech.cinema.utils.ToolUtils;

import net.sf.json.JSONArray;

public class TechexcelService {
	/**
	 * 封装requesst请求参数
	 */
	public static Map getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getResponse();
		Map<Object, Object> reqMap = new HashMap<Object, Object>();
		HttpServletResponse rs = ToolUtils.getResponseType(response);
		Map<Object, Object> map = RequestToMapUtil.transfer(request);
		String jsonp = (String) map.get("callback");
		reqMap.put("rs", rs);
		reqMap.put("jsonp", jsonp);
		reqMap.put("request", request);
		reqMap.put("response", response);
		return reqMap;

	}

	public static JSONObject getAdvTrackingList(int bugID) throws Exception {
		JSONObject json = new JSONObject();
		Connection conn = TechDBUtil.getDBConn();
		String sql = "SELECT (SELECT S.PROGRESSSTATUSNAME FROM PROGRESSSTATUSTYPES S WHERE S.PROGRESSSTATUSID =G.PROGRESSSTATUSID AND S.PROJECTID= '209')  as TRANSITIONNAME, "
				+ "\n SUBSTRING(CONVERT(VARCHAR(50), G.DATEASSIGNED, 111),6,5) +' '+SUBSTRING(CONVERT(VARCHAR(100), G.DATEASSIGNED, 13),11,6) AS DATEASSIGNED,"
				+ "\n G.SEQUENCENO FROM BUGTRACKING G WHERE G.PROJECTID = 209 AND G.BUGID =" + bugID
				+ "  and SequenceNo<>1 ORDER BY G.SEQUENCENO DESC";
		System.out.println("getAdvTrackingList:" + sql);
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		Map<String, String> map = null;
		List<Map<String, String>> trackList = new ArrayList<>();
		String finishTime = null;
		while (rs.next()) {
			map = new HashMap<>();
			map.put("TRANSITIONNAME", rs.getString("TRANSITIONNAME"));
			map.put("DATEASSIGNED", rs.getString("DATEASSIGNED"));
			map.put("SEQUENCENO", rs.getString("SEQUENCENO"));
			trackList.add(map);
			if (rs.getString("TRANSITIONNAME").equals("关闭")) {
				finishTime = (String) rs.getString("DATEASSIGNED");
			}
		}
		json.put("trackList", trackList);
		String sql_bugDetail = " SELECT A.BugTitle,A.ProblemDescription,A.CloseDescription,CASE WHEN (SELECT S.PROGRESSSTATUSNAME FROM PROGRESSSTATUSTYPES S WHERE S.PROGRESSSTATUSID =A.PROGRESSSTATUSID AND S.PROJECTID= '209')= '服务台待响应' THEN '待处理' "
				+ "\n WHEN (SELECT S.PROGRESSSTATUSNAME FROM PROGRESSSTATUSTYPES S WHERE S.PROGRESSSTATUSID = A.PROGRESSSTATUSID "
				+ "\n AND S.PROJECTID= '209')= '关闭' THEN '已完成' ELSE '处理中'  END AS PROGRESSSTATUSNAME,B.FieldMemo2,A.DateCreated,A.EvaluationComments,A.EvaluationValueID "
				+ "\n from Bug A left join BugCustomFieldsData1 B on A.BugID = B.BugID where A.BugID = " + bugID
				+ " and A.ProjectID = 209";
		System.out.println("sql_bugDetail=" + sql_bugDetail);
		PreparedStatement pstmt1 = conn.prepareStatement(sql_bugDetail);
		ResultSet rs1 = pstmt1.executeQuery();
		String BugTitle = null;
		String Des = null;
		String cDes = null;
		while (rs1.next()) {
			JSONObject json1 = new JSONObject();
			String DateCreated = String.valueOf(rs1.getString("DateCreated")).substring(0, 19);
			String DateCreated1 = null;
			if (rs1.getString("PROGRESSSTATUSNAME").equals("已完成")) {
				DateCreated1 = getTime(DateCreated, finishTime);
			} else {
				DateCreated1 = getTime1(DateCreated);
			}
			// System.out.println("DataCreated=" + DateCreated);
			Des = rs1.getString("ProblemDescription");
			cDes = rs1.getString("CloseDescription");
			// System.out.println("Des=" + Des);
			// System.out.println("Des=" + Des);
			json1.put("title", rs1.getString("BugTitle"));
			json1.put("creatTime", DateCreated1);
			json1.put("content", Des);
			json1.put("status", rs1.getString("PROGRESSSTATUSNAME"));
			json1.put("evaluateID", rs1.getInt("EvaluationValueID"));
			json1.put("evaluateContent", rs1.getString("EvaluationComments"));
			json1.put("FieldMemo2", rs1.getString("FieldMemo2"));
			json.put("orderDetail", json1);
			json.put("orderDetail1", TechexcelService.getDesDetail(Des, DateCreated));
			json.put("orderDetail2", TechexcelService.getDesDetail(cDes, DateCreated));
		}
		if (conn != null) {
			conn.close();
		}
		System.out.println(json);
		return json;
	}

	public static List getDesDetail(String DesDetail, String DateCreated)
			throws ParseException, java.text.ParseException {
		String[] strArray = null;
		// System.out.println("DesDetail=" + DesDetail);
		if(DesDetail!=null){
			strArray = DesDetail.split("\\[");
		}
		if (strArray!=null && strArray.length > 1) {
			// JSONObject jsons = new JSONObject();
			// String description = null;
			ArrayList<Object> list = new ArrayList<Object>();
			for (int i = 0; i < strArray.length; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				if (i == 0) {
					// String Date=getShortDateTime(DateCreated);
					// System.out.println(DateCreated);
					map.put("date", DateCreated);
					map.put("content", strArray[0]);
				} else {
					String date1 = strArray[i].substring(strArray[i].indexOf("--") + 2, strArray[i].indexOf("M") + 1);
					String date = convert(date1);
					// System.out.println(date);
					String content = strArray[i].substring(strArray[i].indexOf("]") + 1);
					// String Date1=getShortDateTime(date);
					map.put("date", date);
					map.put("content", content);
				}
				list.add(map);
			}
			return list;
		} else if(strArray!=null && strArray.length > 0){
			ArrayList<Object> list = new ArrayList<Object>();
			Map<String, Object> map = new HashMap<String, Object>();
			// String Date2=getShortDateTime(DateCreated);
			// System.out.println(DateCreated);
			map.put("date", DateCreated);
			map.put("content", strArray[0]);
			list.add(map);
			return list;
		}else{
			ArrayList<Object> list = new ArrayList<Object>();
			Map<String, Object> map = new HashMap<String, Object>();
			// String Date2=getShortDateTime(DateCreated);
			// System.out.println(DateCreated);
			map.put("date", DateCreated);
			map.put("content", "");
			list.add(map);
			return list;
		}
	}

	public static String getTime(String user_time, String date) throws java.text.ParseException {
		String re_time = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dateEnd;
		Date dateStart;
		try {
			dateStart = sdf.parse(user_time);
			String year = user_time.substring(0, user_time.indexOf("-"));
			String date1 = year + "-" + date.substring(0, date.indexOf("/")) + "-"
					+ date.substring(date.indexOf("/") + 1, date.indexOf(" "))
					+ date.substring(date.indexOf(" ") + 1, date.length()) + ":00";
			dateEnd = sdf.parse(date1);
			re_time = getDistanceTimes(dateStart, dateEnd);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re_time;
	}

	public static String getTime1(String user_time) throws java.text.ParseException {
		String re_time = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date1 = sdf.format(new Date());
		Date dateEnd;
		Date dateStart;
		try {
			dateStart = sdf.parse(user_time);
			dateEnd = sdf.parse(date1);
			re_time = getDistanceTimes(dateStart, dateEnd);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re_time;
	}

	public static String getDistanceTimes(Date dateStart, Date dateEnd) {
		long day = 0;
		long hour = 0;
		double hour1 = 0;
		double ss = 60;
		long min = 0;
		long sec = 0;
		long diff;
		// diff = System.currentTimeMillis() - date.getTime();
		diff = dateEnd.getTime() - dateStart.getTime();
		day = diff / (24 * 60 * 60 * 1000);
		hour = (diff / (60 * 60 * 1000) - day * 24);
		// hour1 = ((diff / (60 * 60 * 1000)) - day * 24 - hour);
		min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
		hour1 = min / ss;
		String hour2;
		if (hour1 >= 0.01 && hour1 <= 0.09) {
			hour1 = 0.1;
		} else {
			hour2 = String.format("%.1f", hour1);
			hour1 = Double.valueOf(hour2);
		}
		sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		String res = "";
		if (day > 0 && hour > 0)
			res = day + "天" + hour + "小时";
		else if (day > 0 && hour == 0)
			res = day + "天";
		else if (day == 0)
			res = hour1 + "小时";
		return res;
	}

	public static String convert(String ampm) throws java.text.ParseException {
		// 在此处写出解决问题的程序代码 …
		SimpleDateFormat sdft = new SimpleDateFormat("MM/dd/yy HH:mm");
		SimpleDateFormat sdft1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = "";
		if (ampm.contains("AM")) {
			str = ampm.substring(0, ampm.indexOf("AM"));
			Date date = sdft.parse(str);
			return sdft1.format(date);
		} else if (ampm.contains("PM")) {
			str = ampm.substring(0, ampm.indexOf("PM"));
			Date date = sdft.parse(str);
			return sdft1.format(date);
		} else {
			return ampm;
		}
	}

}
