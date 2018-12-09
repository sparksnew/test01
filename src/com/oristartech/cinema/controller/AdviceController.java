package com.oristartech.cinema.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.oristartech.cinema.service.AdviceService;
import com.oristartech.cinema.service.TroubleListService;
import com.oristartech.cinema.utils.DataSourceContextHolder;
import com.oristartech.cinema.utils.MessageUtils;
import com.oristartech.cinema.utils.RequestToMapUtil;
import com.oristartech.cinema.utils.TechDBUtil;
import com.oristartech.cinema.utils.ToolUtils;

@Controller
@RequestMapping(value = "/adv")
public class AdviceController {

	private static final String techExcle_url = "http://118.190.91.92/LinkPlusWebServiceSetup/WsIncident.asmx/";
//	private static final String techExcle_url = "http://114.215.42.232/LinkPlusService/WsIncident.asmx/";
	private Logger logger = Logger.getLogger(AdviceController.class);
	@Autowired
	private AdviceService adviceService;
	@Autowired
	private TroubleListService troubleListService;

	@RequestMapping(value = "/addAdviseInfo")
	@ResponseBody
	public JSONObject addAdviseInfo(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		Map<Object, Object> map = RequestToMapUtil.transfer(request);
		String title = null;
		String content = null;
		// try {
		// title = new
		// String(map.get("title").toString().getBytes("iso8859-1"),"UTF-8");
		// content = new
		// String(map.get("content").toString().getBytes("iso8859-1"),"UTF-8");
		title = request.getParameter("title");
		title = java.net.URLDecoder.decode(title, "UTF-8");
		content = request.getParameter("content");
		content = java.net.URLDecoder.decode(content, "UTF-8");
		// } catch (UnsupportedEncodingException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		map.remove("title");
		map.remove("content");
		map.put("title", title);
		map.put("content", content);
		JSONObject json = new JSONObject();
		AdviceController adviceController = null;
		try {
			String userID = map.get("userID").toString();
			String cinemaCode = map.get("cinemaCode").toString();
			System.out.println("userID=" + userID);
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TECHEXCEL);
			String cinemaID = adviceService.getEmpCinemaID(cinemaCode);
			System.out.println("cinemaID=" + cinemaID);
			DataSourceContextHolder.clearDbType();
			if (cinemaID != null && cinemaID != "") {
				if (String.valueOf(cinemaID) != null || !String.valueOf(cinemaID).isEmpty()) {
					adviceController = new AdviceController();
					json = adviceController.addAdviseData(map, String.valueOf(cinemaID));
					int indentID = json.getIntValue("bugID");
					if (!"null".equals(String.valueOf(indentID)) || !"0".equals(String.valueOf(indentID))) {
						DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
						int count = adviceService.insertUserBug(indentID, userID);
						DataSourceContextHolder.clearDbType();
						if (count == 0) {
							json.put("code", MessageUtils.FAIL_CODE);
							json.put("msg", "后台服务异常，但提交成功");
						} else {
							json.put("bugID", indentID);
							json.put("code", MessageUtils.SUCCESS_CODE);
							json.put("msg", "提交成功");
						}
					} else {
						json.put("code", MessageUtils.FAIL_CODE);
						json.put("msg", "报障平台异常");
					}

				}
			} else {
				json.put("code", MessageUtils.FAIL_CODE);
				json.put("msg", "请联系管理在报障平台完善影院信息");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", MessageUtils.FAIL_CODE);
			json.put("msg", "后台服务异常");
		}
		return json;
	}

	@RequestMapping(value = "/getAdviseList")
	@ResponseBody
	public JSONObject getAdviseList(HttpServletRequest request, HttpServletResponse response) {
		Map<Object, Object> map = RequestToMapUtil.transfer(request);
		JSONObject json = new JSONObject();
		try {
			String userID = map.get("userID").toString();
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
			List<String> bugList = adviceService.getUserAdvList(userID);
			DataSourceContextHolder.clearDbType();
			List<Map<String, String>> dataList = new ArrayList<>();
			if (bugList.size() > 0) {
				int length = bugList.toString().length();
				String list = bugList.toString().substring(1, length - 1);
				System.out.println("list=" + list);
				Connection conn = TechDBUtil.getDBConn();
				String sql = "SELECT A.PROJECTID,  " + "\n A.BUGID," + "\n A.BUGTITLE,"
						+ "\n CASE WHEN (SELECT S.PROGRESSSTATUSNAME FROM PROGRESSSTATUSTYPES S WHERE S.PROGRESSSTATUSID = A.PROGRESSSTATUSID AND S.PROJECTID= '209')= '服务台待响应' THEN '待处理' "
						+ "\n WHEN (SELECT S.PROGRESSSTATUSNAME FROM PROGRESSSTATUSTYPES S WHERE S.PROGRESSSTATUSID = A.PROGRESSSTATUSID AND S.PROJECTID= '209')= '关闭' THEN '已完成' ELSE '处理中'  END AS PROGRESSSTATUSNAME,"
						+ "\n CASE WHEN (SELECT S.PROGRESSSTATUSNAME FROM PROGRESSSTATUSTYPES S WHERE S.PROGRESSSTATUSID = A.PROGRESSSTATUSID AND S.PROJECTID= '209')= '关闭' THEN '已完成' ELSE '未完成'  END AS PROGRESSSTATUSNAME1,"
						+ "\n A.CUSTOMERID, " + "\n B.BUGDESCRIPTION,"
						+ "\n SUBSTRING(CONVERT(varchar(50), b.DateAssigned, 111),6,5) +' '+SUBSTRING(CONVERT(varchar(100), b.DateAssigned, 13),11,6) as DATEASSIGNED ,"
						+ "\n (SELECT S.PROGRESSSTATUSNAME FROM PROGRESSSTATUSTYPES S WHERE S.PROGRESSSTATUSID =A.PROGRESSSTATUSID AND S.PROJECTID= '209')  as TRANSITIONNAME"
						+ "\n FROM BUG A INNER JOIN (SELECT * "
						+ "\n FROM ( SELECT *,ROW_NUMBER() OVER(PARTITION BY BUGID ORDER BY SEQUENCENO DESC) RN  FROM BUGTRACKING  where ProjectID = 209 ) T "
						+ "\n WHERE T.RN <=1 AND T.PROJECTID = '209') B " + "\n  ON A.PROJECTID = B.PROJECTID "
						+ "\n AND A.BUGID = B.BUGID " + "\n   AND A.BUGID IN(" + list + ")  AND A.PROJECTID = '209' "
						+ "\n ORDER BY PROGRESSSTATUSNAME1 ASC,DATEASSIGNED DESC";
				System.out.println("sql=" + sql);
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rst = pstmt.executeQuery();
				Map<String, String> dataMap = null;
				while (rst.next()) {
					dataMap = new HashMap<>();
					dataMap.put("PROJECTID", rst.getString("PROJECTID"));
					dataMap.put("BUGID", rst.getString("BUGID"));
					dataMap.put("BUGTITLE", rst.getString("BUGTITLE"));
					dataMap.put("PROGRESSSTATUSNAME", rst.getString("PROGRESSSTATUSNAME"));
					dataMap.put("PROGRESSSTATUSNAME1", rst.getString("PROGRESSSTATUSNAME1"));
					dataMap.put("CUSTOMERID", rst.getString("CUSTOMERID"));
					dataMap.put("BUGDESCRIPTION", rst.getString("BUGDESCRIPTION"));
					dataMap.put("DATEASSIGNED", rst.getString("DATEASSIGNED"));
					dataMap.put("TRANSITIONNAME", rst.getString("TRANSITIONNAME"));
					dataList.add(dataMap);
				}
			}
			json.put("dataList", dataList);
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_MSG);
		} catch (Exception e) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping(value = "/getAdvTrackingList")
	@ResponseBody
	public JSONObject getAdvTrackingList(HttpServletRequest request, HttpServletResponse response) {
		Map<Object, Object> map = RequestToMapUtil.transfer(request);
		JSONObject json = new JSONObject();
		int bugID = Integer.parseInt(map.get("bugID").toString());
		try {
			json = TechexcelService.getAdvTrackingList(bugID);
			String msg = "这是从39.108.75.176tomcat走的";
			json.put("msg", msg);
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_MSG);
		} catch (Exception e) {
			e.printStackTrace();
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
		return json;
	}

	/**
	 * 新增投诉建议
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public JSONObject addAdviseData(Map<Object, Object> map, String theaterId) {
		String title = (String) map.get("title");
		String remark = (String) map.get("content");
		String datetimes = ((String) map.get("datetimes")).substring(0, 19);

		StringBuilder sb = new StringBuilder();
		sb.append("<Incident>");
		sb.append("<CustomerID>");
		sb.append(theaterId);
		sb.append("</CustomerID>");
		sb.append("<DateCreated>");
		sb.append(datetimes);
		sb.append("</DateCreated>");
		sb.append("<IncidentTitle>");
		sb.append(title);
		sb.append("</IncidentTitle>");
		sb.append("<IncidentDescription>");
		sb.append(remark);
		sb.append("</IncidentDescription>");
		sb.append("<CustomerID>38</CustomerID> ");
		sb.append("<LastTransitionID>-111</LastTransitionID>");
		sb.append("<ProgressStatusID>144</ProgressStatusID>");
		sb.append("<ProjectID>209</ProjectID>"); // 服务台受理
		sb.append("<ProgressStatus>服务台待响应</ProgressStatus>");
		sb.append("</Incident>");

		String LinkedSystemID = "LinkedAdviceID";
		String LinkedProjectID = "LinkedAdviceName";
		String xmlIncident = sb.toString();
		JSONObject json = new JSONObject();
		json = AddNewAdviseIncident(LinkedSystemID, LinkedProjectID, xmlIncident, datetimes);
		return json;
	}

	public static void main(String[] args) {
		Map<Object, Object> map = new HashMap<>();
		map.put("title", "1");
		map.put("content", "1");
		map.put("phone", "11111111111");
		String theaterId = "2100";
		map.put("datetimes", "2017-03-03T01:01:03");
		AdviceController adviceController = new AdviceController();
		adviceController.addAdviseData(map, theaterId);
	}

	/**
	 * 新增投诉建议功能
	 * 
	 * @param LinkedSystemID
	 * @param LinkedProjectID
	 * @param xmlIncident
	 * @return
	 */
	public JSONObject AddNewAdviseIncident(String LinkedSystemID, String LinkedProjectID, String xmlIncident,
			String datetimes) {
		String http_request_path = techExcle_url + "AddNewIncident";
		HttpResponse httpResponse = null;
		HttpClient httpClient = buildHttpClient();
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("LinkedSystemID", LinkedSystemID));
		formparams.add(new BasicNameValuePair("LinkedProjectID", LinkedProjectID));
		formparams.add(new BasicNameValuePair("xmlIncident", xmlIncident));
		HttpPost httpPost = new HttpPost(http_request_path);
		JSONObject json = new JSONObject();
		try {
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httpPost.setEntity(uefEntity);
			httpResponse = httpClient.execute(httpPost);
			int code = httpResponse.getStatusLine().getStatusCode();
			System.out.println("code=" + code);
			if (code == 200) {
				String incidentDetail = getIncidentDetail(datetimes);
				json.put("code", code);
				json.put("bugID", incidentDetail);
				// AddItemBugLinks(code, deviceID, context);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public String getIncidentDetail(String datetimes) throws Exception {
		String date = datetimes.replace("T", " ");
		Connection conn = TechDBUtil.getDBConn();
		String sql = "SELECT BugID from Bug where DateCreated = '" + date + "'";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		String BugID = null;
		while (rs.next()) {
			BugID = rs.getString("BugID");
		}
		return BugID;
	}

	public static HttpClient buildHttpClient() {
		HttpClient httpClient = new DefaultHttpClient();
		SSLContext sslcontext = loadSSLContext();
		SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
		X509HostnameVerifier hostnameVerifier = new X509HostnameVerifier() {
			@Override
			public boolean verify(String arg0, SSLSession arg1) {
				return true;
			}

			@Override
			public void verify(String arg0, SSLSocket arg1) throws IOException {
			}

			@Override
			public void verify(String arg0, String[] arg1, String[] arg2) throws SSLException {
			}

			@Override
			public void verify(String arg0, X509Certificate arg1) throws SSLException {
			}
		};
		sf.setHostnameVerifier(hostnameVerifier);
		httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", sf, 443));
		return httpClient;
	}

	private static SSLContext loadSSLContext() {
		SSLContext ctx = null;
		try {
			X509TrustManager xtm = new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			ctx = SSLContext.getInstance("TLS");
			ctx.init(null, new TrustManager[] { xtm }, null);
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ctx;
	}
}
