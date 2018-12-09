package com.oristartech.cinema.controller;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.naming.NamingException;
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
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.oristartech.cinema.service.AdviceService;
import com.oristartech.cinema.service.BugInfoService;
import com.oristartech.cinema.service.CinemaDeviceInfoService;
import com.oristartech.cinema.service.CustomerInfoService;
import com.oristartech.cinema.service.TicketSystemService;
import com.oristartech.cinema.service.TroubleListService;
import com.oristartech.cinema.utils.DataSourceContextHolder;
import com.oristartech.cinema.utils.MessageUtils;
import com.oristartech.cinema.utils.ToolUtils;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import Decoder.BASE64Decoder;
import net.sf.json.JSONArray;

@RequestMapping("/trouble")
@Controller
public class TroubleController {
	private static final String techExcle_url = "http://118.190.91.92/LinkPlusWebServiceSetup/WsIncident.asmx/";//
	// 通过接口进行访问
//	private static final String techExcle_url = "http://114.215.42.232/LinkPlusService/WsIncident.asmx/";// 通过接口进行访问
	@Autowired
	private TroubleListService troubleListService;
	@Autowired
	private CinemaDeviceInfoService cinemaDeviceInfoService;
	@Autowired
	private BugInfoService bugInfoService;
	@Autowired
	private TicketSystemService ticketSystemService;
	@Autowired
	private AdviceService adviceService;
	private Image img;
	private int width;
	private int height;

	public TroubleController(String fileName) throws IOException {
		File file = new File(fileName);// 读入文件
		img = ImageIO.read(file); // 构造Image对象
		width = img.getWidth(null); // 得到源图宽
		height = img.getHeight(null); // 得到源图长
	}

	public TroubleController() {

	}

	/**
	 * 此方法用于获取报障的基本信息，例如报障位置，报障设备等基础信息此
	 * 
	 * @param type
	 * @param theaterId
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	@RequestMapping(value = "/troubleDate")
	@ResponseBody
	public JSONObject getTroubleData(String theaterId, String type) {
		JSONObject json = new JSONObject();
		try {
			json = getEquipment(theaterId, type); // 调用接口方法，此方法是直接通过数据库进行获取的信息
		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	/**
	 * 新增免登陆的提交报障
	 * 
	 * @param params
	 * @param context
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 * @throws NamingException
	 * @throws SQLException
	 * @throws java.sql.SQLException
	 */
	@RequestMapping(value = "/addunsignbug")
	@ResponseBody
	public void AddUnsignBug()
			throws SQLException, NamingException, IOException, ParseException, java.sql.SQLException {
		JSONObject json = new JSONObject();
		Map requestMap = TechexcelService.getRequest();
		HttpServletRequest request = (HttpServletRequest) requestMap.get("request");
		HttpServletResponse response = (HttpServletResponse) requestMap.get("response");
		String type = request.getParameter("type");
		int UserID = Integer.parseInt(request.getParameter("UserID"));
		int indentID = 9999;
		if (type.equals("0")) {
			// 无图片
			addTroubleData(request, response);
			indentID = json.getIntValue("bugID");
		} else {
			// 有图片
			// json = addTroubleAndPictureData();
			indentID = json.getIntValue("bugID");
		}
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
		bugInfoService.addUserBugs(UserID, indentID, type);
		DataSourceContextHolder.clearDbType();
	}

	/**
	 * 新增报障
	 * 
	 * @param params
	 * @param context
	 * @return
	 * @throws java.sql.SQLException
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/addtrouble")
	@ResponseBody
	public JSONObject addTroubleData(HttpServletRequest request, HttpServletResponse response)
			throws java.sql.SQLException, UnsupportedEncodingException {
		String positionName = null;
		String deviceName = null;
		String remark = null;
		// try {
		// positionName = new
		// String(request.getParameter("positionName").toString().getBytes("iso8859-1"),
		// "UTF-8");
		// deviceName = new
		// String(request.getParameter("deviceName").toString().getBytes("iso8859-1"),
		// "UTF-8");
		// remark = new
		// String(request.getParameter("remark").toString().getBytes("iso8859-1"),
		// "UTF-8");
		positionName = request.getParameter("positionName");
		deviceName = request.getParameter("deviceName");
		remark = request.getParameter("remark");
		positionName = java.net.URLDecoder.decode(positionName, "UTF-8");
		deviceName = java.net.URLDecoder.decode(deviceName, "UTF-8");
		remark = java.net.URLDecoder.decode(remark, "UTF-8");
		System.out.println("positionName=" + positionName);
		System.out.println("deviceName=" + deviceName);
		System.out.println("remark=" + remark);
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }
		String cinemaCode = request.getParameter("cinemaCode");
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TECHEXCEL);
		String cinemaID = adviceService.getEmpCinemaID(cinemaCode);
		DataSourceContextHolder.clearDbType();
		String deviceID = request.getParameter("deviceID");
		String datetimes = request.getParameter("datetimes").substring(0, 19);
		StringBuilder sb = new StringBuilder();
		sb.append("<Incident>");
		sb.append("<CustomerID>");
		sb.append(cinemaID);
		sb.append("</CustomerID>");
		sb.append("<DateCreated>");
		sb.append(datetimes);
		sb.append("</DateCreated>");
		sb.append("<IncidentTitle>");
		if (positionName.equals("未知")) {
			sb.append(deviceName);
		} else {
			sb.append(positionName + deviceName);
		}
		sb.append("</IncidentTitle>");
		sb.append("<IncidentDescription>");
		sb.append(remark);
		sb.append("</IncidentDescription>");
		sb.append("<CurrentOwnerID>");
		sb.append("-26");
		sb.append("</CurrentOwnerID>");
		// sb.append("<CurrentOwner>");
		// sb.append("16");
		// sb.append("</CurrentOwner>");
		sb.append("<CustomerID>38</CustomerID> ");
		sb.append("<LastTransitionID>-111</LastTransitionID>");
		sb.append("<ProgressStatusID>169</ProgressStatusID>");
		sb.append("<ProjectID>192</ProjectID>"); // 服务台受理
		sb.append("<ProgressStatus>服务台待响应</ProgressStatus>");
		sb.append("</Incident>");
		String LinkedSystemID = "LinkedSystemID";
		String LinkedProjectID = "LinkedProjectID";
		String xmlIncident = sb.toString();
		JSONObject json = new JSONObject();
		// try {
		// System.out.println("这是什么鬼：" + xmlIncident);
		json = AddNewIncident(LinkedSystemID, LinkedProjectID, xmlIncident, datetimes, deviceID);
		json.put("flag", getDeviceQAIsOver(deviceID));

		json.put("validateCode", MessageUtils.SUCCESS_CODE);
		json.put("msg", MessageUtils.SUCCESS_SENDMESSAGE);
		// } catch (Exception e) {
		// json.put("validateCode", MessageUtils.FAIL_CODE);
		// json.put("msg", MessageUtils.FAIL_MSG);
		// }
		return json;
	}

	/**
	 * 新增投诉建议
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	@RequestMapping(value = "/addAdv")
	@ResponseBody
	public JSONObject addAdviseData(HttpServletRequest request, HttpServletResponse response) {
		String title = request.getParameter("title");
		String remark = request.getParameter("content");
		// String phone = request.getParameter("phone");
		String theaterId = request.getParameter("theaterId");
		String datetimes = request.getParameter("datetimes").substring(0, 19);
		System.out.println("测试一下时间输出：" + datetimes);

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
		sb.append("<ProgressStatusID>189</ProgressStatusID>");
		sb.append("<ProjectID>209</ProjectID>"); // 服务台受理
		sb.append("<ProgressStatus>建议受理</ProgressStatus>");
		sb.append("</Incident>");

		String LinkedSystemID = "LinkedAdviceID";
		String LinkedProjectID = "LinkedAdviceName";
		String xmlIncident = sb.toString();
		JSONObject json = new JSONObject();
		// System.out.println("这又是什么鬼：" + xmlIncident);
		json = AddNewAdviseIncident(LinkedSystemID, LinkedProjectID, xmlIncident, datetimes);
		return json;

	}

	@RequestMapping(value = "/troublelist")
	@ResponseBody
	public void getTroubleList() {
		Map map = TechexcelService.getRequest();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		String cinemaCode = request.getParameter("cinemaCode");
		String deviceId = request.getParameter("deviceId");
		try {
			getBugList(cinemaCode, deviceId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取故障记录列表
	 * 
	 * @Title: getBugTrackingList
	 * @Description: TODO
	 * @param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/bugtracking")
	@ResponseBody
	public JSONObject getBugTrackingList() throws NumberFormatException, Exception {
		Map requestMap = TechexcelService.getRequest();
		HttpServletRequest request = (HttpServletRequest) requestMap.get("request");
		String bugID = request.getParameter("bugID");
		JSONObject json = new JSONObject();
		// try {
		json = getBugTrackingList(Integer.valueOf(bugID));
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
		List bugDetail = bugInfoService.bugDetail(bugID);
		DataSourceContextHolder.clearDbType();
		ArrayList<Object> list = new ArrayList<Object>();
		if (null != bugDetail && bugDetail.size() > 0) {
			for (int i = 0; i < bugDetail.size(); i++) {
				Map bugDetailMap = (Map) bugDetail.get(i);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("bugID", bugDetailMap.get("bugID"));
				map.put("BigUrl", bugDetailMap.get("BigUrl"));
				map.put("SmUrl", bugDetailMap.get("SmUrl"));
				list.add(map);
			}
			json.put("imgList", list);
		} else {
			json.put("imgList", list);
		}
		json.put("validateCode", MessageUtils.SUCCESS_CODE);
		json.put("msg", MessageUtils.SUCCESS_SENDMESSAGE);
		// } catch (Exception e) {
		// e.printStackTrace();
		// json.put("validateCode", MessageUtils.FAIL_CODE);
		// json.put("msg", MessageUtils.FAIL_MSG);
		// }
		return json;
	}

	/**
	 * 直接编辑报障记录
	 * 
	 * @param params
	 * @param context
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/edittroubledata")
	@ResponseBody
	public JSONObject editTroubleData() throws UnsupportedEncodingException {
		Map requestMap = TechexcelService.getRequest();
		HttpServletRequest request = (HttpServletRequest) requestMap.get("request");
		String bugID = request.getParameter("bugID");
		String currentRole = null;
		// try {
		// Des = new
		// String(request.getParameter("Des").toString().getBytes("iso8859-1"),
		// "UTF-8");
		// currentRole = new
		// String(request.getParameter("currentRole").toString().getBytes("iso8859-1"),
		// "UTF-8");
		String Des = request.getParameter("Des");
		Des = java.net.URLDecoder.decode(Des, "UTF-8");
		// } catch (UnsupportedEncodingException e1) {
		// Des = "";
		// }
		// String currentRole = request.getParameter("currentRole");
		String telPhone = request.getParameter("telPhone");
		JSONObject json = new JSONObject();
		// try {
		json = editTroubleByBugID(Integer.valueOf(bugID), Des, telPhone);
		json.put("validateCode", MessageUtils.SUCCESS_CODE);
		json.put("msg", MessageUtils.SUCCESS_SENDMESSAGE);
		// } catch (Exception e) {
		// json.put("validateCode", MessageUtils.FAIL_CODE);
		// json.put("msg", MessageUtils.FAIL_MSG);
		// e.printStackTrace();
		// }
		return json;
	}

	/**
	 * 添加描述功能
	 * 
	 * @param params
	 * @param context
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/addtroubleandpicturedata")
	@ResponseBody
	public JSONObject addTroubleAndPictureData() throws IOException, SQLException, ParseException, NamingException {
		@SuppressWarnings("rawtypes")
		Map requestMap = TechexcelService.getRequest();
		HttpServletRequest request = (HttpServletRequest) requestMap.get("request");
		String positionName = request.getParameter("positionName");
		String deviceName = request.getParameter("deviceName");
		String remark = request.getParameter("remark");
		String theaterId = request.getParameter("theaterId");
		String deviceID = request.getParameter("deviceID");
		String datetimes = request.getParameter("datetimes").substring(0, 19);
		String imgData = request.getParameter("imgData");
		/*
		 * String fileName = "test.png"; String fileContent = new
		 * String(Base64.encode(imgData.getBytes()));
		 */
		StringBuilder sb = new StringBuilder();
		sb.append("<Incident>");
		sb.append("<CustomerID>");
		sb.append(theaterId);
		sb.append("</CustomerID>");
		sb.append("<DateCreated>");
		sb.append(datetimes);
		sb.append("</DateCreated>");
		sb.append("<IncidentTitle>");
		if (positionName.equals("未知")) {
			sb.append(deviceName);
		} else {
			sb.append(positionName + deviceName);
		}
		sb.append("</IncidentTitle>");
		sb.append("<IncidentDescription>");
		sb.append(remark);
		sb.append("</IncidentDescription>");
		sb.append("<CurrentOwnerID>");
		sb.append("-26");
		sb.append("</CurrentOwnerID>");
		sb.append("<CustomerID>38</CustomerID> ");
		sb.append("<LastTransitionID>-111</LastTransitionID>");
		sb.append("<ProgressStatusID>111</ProgressStatusID>");
		sb.append("<ProjectID>192</ProjectID>"); // 服务台受理
		sb.append("<ProgressStatus>服务台受理</ProgressStatus>");
		sb.append("</Incident>");
		String LinkedSystemID = "LinkedSystemID";
		String LinkedProjectID = "LinkedProjectID";
		String xmlIncident = sb.toString();
		JSONObject json = new JSONObject();
		json = AddNewIncident(LinkedSystemID, LinkedProjectID, xmlIncident, datetimes, deviceID);
		json.put("flag", getDeviceQAIsOver(request.getParameter("deviceID")));
		System.out.println(json);
		int indentID = json.getIntValue("bugID");
		String userID = "2";
		String incidentID = String.valueOf(indentID);
		String size = "1000";
		// uploadPicfile(incidentID, imgData, LinkedSystemID, LinkedProjectID,
		// userID, incidentID, size);
		return json;
	}

	/**
	 * 添加评价功能
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/editevaluateData")
	@ResponseBody
	public JSONObject editevaluateData() throws UnsupportedEncodingException {
		Map requestMap = TechexcelService.getRequest();
		HttpServletRequest request = (HttpServletRequest) requestMap.get("request");
		String bugID = request.getParameter("bugID");
		// try {
		// remark = new
		// String(request.getParameter("remark").toString().getBytes("iso8859-1"),
		// "UTF-8");
		String remark = request.getParameter("remark");
		remark = java.net.URLDecoder.decode(remark, "UTF-8");
		// } catch (UnsupportedEncodingException e1) {
		// e1.printStackTrace();
		// }
		String evaluate = request.getParameter("evaluate");
		JSONObject json = new JSONObject();
		// try {
		json = editTroubleevaluateByBugID(Integer.valueOf(bugID), evaluate, remark);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		return json;
	}

	/**
	 * 获取影院的所有设备
	 * 
	 * @param params
	 * @param context
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/theaterdevicedata")
	@ResponseBody
	public JSONObject getTheaterDeviceData() throws UnsupportedEncodingException {
		Map requestMap = TechexcelService.getRequest();
		HttpServletRequest request = (HttpServletRequest) requestMap.get("request");
		String cinemaCode = request.getParameter("cinemaCode");
		// String positionID = new
		// String(request.getParameter("positionID").toString().getBytes("iso8859-1"),
		// "UTF-8");
		String positionID = request.getParameter("positionID");
		positionID = java.net.URLDecoder.decode(positionID, "UTF-8");
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TECHEXCEL);
		String cinemaID = adviceService.getEmpCinemaID(cinemaCode);
		JSONObject json = new JSONObject();
		JSONArray theaterDeviceData = getTheaterDeviceData(cinemaID, positionID);
		DataSourceContextHolder.clearDbType();
		// try {
		json.put("logs", theaterDeviceData);
		json.put("validateCode", MessageUtils.SUCCESS_CODE);
		json.put("msg", MessageUtils.SUCCESS_SENDMESSAGE);
		// } catch (Exception e) {
		// json.put("validateCode", MessageUtils.FAIL_CODE);
		// json.put("msg", MessageUtils.FAIL_MSG);
		// }
		return json;
	}

	@RequestMapping(value = "/saveadvice")
	@ResponseBody
	public JSONObject saveAdvice() {
		Map requestMap = TechexcelService.getRequest();
		HttpServletRequest request = (HttpServletRequest) requestMap.get("request");
		String title = request.getParameter("title");
		String remark = request.getParameter("content");
		String phone = request.getParameter("phone");
		String theaterId = request.getParameter("theaterId");
		String datetimes = request.getParameter("datetimes").substring(0, 19);
		System.out.println("测试一下时间输出：" + datetimes);
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
		sb.append("<ProgressStatusID>189</ProgressStatusID>");
		sb.append("<ProjectID>209</ProjectID>"); // 服务台受理
		sb.append("<ProgressStatus>建议受理</ProgressStatus>");
		sb.append("</Incident>");

		String LinkedSystemID = "LinkedAdviceID";
		String LinkedProjectID = "LinkedAdviceName";
		String xmlIncident = sb.toString();
		JSONObject json = new JSONObject();
		// System.out.println("这又是什么鬼：" + xmlIncident);
		json = AddNewAdviseIncident(LinkedSystemID, LinkedProjectID, xmlIncident, datetimes);
		return json;
	}

	@RequestMapping(value = "/schedulelist")
	@ResponseBody
	public void getScheduleList()
			throws SQLException, NamingException, ClassNotFoundException, ParseException, java.text.ParseException {
		Map requestMap = TechexcelService.getRequest();
		HttpServletRequest request = (HttpServletRequest) requestMap.get("request");
		String theaterCode = request.getParameter("theaterCode");
		String date = request.getParameter("date");

		JSONObject json = new JSONObject();
		json.put("SchedualList", getSchedualData(theaterCode, date));
	}

	/**
	 * 通过二维码获得设备的详细信息
	 * 
	 * @param params
	 * @param context
	 * @return
	 * @throws NamingException
	 * @throws java.sql.SQLException
	 */
	@RequestMapping(value = "/deviceinfobycode")
	@ResponseBody
	public void getDeviceInfoByCode() throws java.sql.SQLException, NamingException {
		Map requestMap = TechexcelService.getRequest();
		HttpServletRequest request = (HttpServletRequest) requestMap.get("request");
		JSONObject json = new JSONObject();
		String DeviceID = request.getParameter("DeviceID");
		System.out.println("设备ID:" + DeviceID);
		getTheaterInfoByDeviceCode(DeviceID);

	}

	/**
	 * 获得免登陆账户下的所有报障列表
	 * 
	 * @param params
	 * @param context
	 * @return
	 * @return
	 * @throws NamingException
	 * @throws java.sql.SQLException
	 */
	@RequestMapping(value = "/unsignbuglist")
	@ResponseBody
	public void getUnsignBugList() throws java.sql.SQLException, NamingException {
		Map requestMap = TechexcelService.getRequest();
		HttpServletRequest request = (HttpServletRequest) requestMap.get("request");
		String UserID = request.getParameter("UserID");
		JSONObject json = new JSONObject();
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
		List BugID = bugInfoService.getBugIDByUserID(UserID);
		DataSourceContextHolder.clearDbType();
		String BugIDs = "";
		for (int i = 0; i < BugID.size(); i++) {
			Map map = (Map) BugID.get(i);
			if (i > 0)
				BugIDs = BugIDs + ",'" + map.get("BugID") + "'";
			else
				BugIDs = "'" + map.get("BugID") + "'";
		}
		try {
			getUnsignBugList(BugIDs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取影院的所有设备
	 * 
	 * @param params
	 * @param context
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/theaterpositiondata")
	@ResponseBody
	public void getTheaterPsitionData() throws Exception {
		Map requestMap = TechexcelService.getRequest();
		HttpServletRequest request = (HttpServletRequest) requestMap.get("request");
		String TheaterId = request.getParameter("theaterId");
		JSONObject list = getEquipment(TheaterId, "1");
		JSONObject json = new JSONObject();
		try {
			json.put("logs", list);
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_SENDMESSAGE);
		} catch (Exception e) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
	}

	/**********************************************************/
	/**
	 * 新增报障功能
	 * 
	 * @param LinkedSystemID
	 * @param LinkedProjectID
	 * @param xmlIncident
	 * @return
	 */
	public JSONObject AddNewIncident(String LinkedSystemID, String LinkedProjectID, String xmlIncident,
			String datetimes, String deviceID) {
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
			// //System.out.println(code);
			if (code == 200) {
				// //System.out.println("插入成功");
				json.put("code", code);
				String incidentDetail = getIncidentDetail(datetimes);
				json.put("bugID", incidentDetail);
				AddItemBugLinks(incidentDetail, deviceID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping("/deviceqaisover")
	public String getDeviceQAIsOver(String deviceID) throws SQLException {
		String flag = "1";
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TECHEXCEL);
		flag = troubleListService.getFlag(deviceID);
		DataSourceContextHolder.clearDbType();
		return flag;
	}

	public static HttpClient buildHttpClient() {
		HttpClient httpClient = new DefaultHttpClient();
		SSLContext sslcontext = loadSSLContext();
		SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
		X509HostnameVerifier hostnameVerifier = new X509HostnameVerifier() {
			public boolean verify(String arg0, SSLSession arg1) {
				return true;
			}

			public void verify(String arg0, SSLSocket arg1) throws IOException {
			}

			public void verify(String arg0, String[] arg1, String[] arg2) throws SSLException {
			}

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
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

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

	@RequestMapping("/incidentdetail")
	public String getIncidentDetail(String datetimes) throws Exception {
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TECHEXCEL);
		List troubleList = troubleListService.getBugId(datetimes);
		DataSourceContextHolder.clearDbType();
		String BugID = null;
		// String Des = null;
		// BugID = String.valueOf(rows.getInteger("BugID"));
		for (int i = 0; i < troubleList.size(); i++) {
			Map troubleMap = (Map) troubleList.get(i);
			Integer BugID1 = (Integer) troubleMap.get("BugID");
			BugID = String.valueOf(BugID1);
		}
		return BugID;
	}

	@RequestMapping("/additembuglinks")
	public void AddItemBugLinks(String BugID, String ItemID) {
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TECHEXCEL);
		int ProjectID = 192;
		int IncidentID = Integer.parseInt(BugID);
		int LinkedProjectID = 1;
		int LinkedAssetID = Integer.parseInt(ItemID);
		troubleListService.addIncidentAssetLinks(ProjectID, IncidentID, LinkedProjectID, LinkedAssetID);
		DataSourceContextHolder.clearDbType();
	}

	// 获得影院设备列表
	@RequestMapping(value = "/equipment")
	@ResponseBody
	public JSONObject getEquipment(String theaterId, String type) throws Exception {
		Map mp = TechexcelService.getRequest();
		HttpServletResponse rs = (HttpServletResponse) mp.get("rs");
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TECHEXCEL);
		JSONObject json = new JSONObject();
		List list = null;
		if (!"".equals(type) && type != null) {
			if (type.equals("1")) {
				list = cinemaDeviceInfoService.getTroubleField(theaterId);
			} else {
				list = cinemaDeviceInfoService.getEquipment(theaterId);
			}
		}
		DataSourceContextHolder.clearDbType();
		if (list != null && list.size() > 0) {
			JSONArray fieldJson = JSONArray.fromObject(list);
			try {
				json.put("logs", fieldJson);
				json.put("status", "1");
				json.put("msg", "成功");
			} catch (Exception e) {
				json.put("status", "0");
				json.put("msg", "失败");
			}
		} else {
			json.put("status", "2");
			json.put("msg", "没有相关数据");
		}

		return json;
	}

	// 获取bug列表
	@RequestMapping(value = "/buglist")
	@ResponseBody
	public JSONObject getBugList(String cinemaCode, String deviceId) throws Exception {
		Map requestMap = TechexcelService.getRequest();
		HttpServletResponse rs = (HttpServletResponse) requestMap.get("rs");
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TECHEXCEL);
		JSONObject json = new JSONObject();
		// List list = cinemaDeviceInfoService.getEquipment(theaterId);
		System.out.println("cinemaCode=" + cinemaCode);
		String cinemaID = adviceService.getEmpCinemaID(cinemaCode);
		System.out.println("cinemaID=" + cinemaID);
		List list = bugInfoService.getBugList(cinemaID, deviceId);
		ArrayList<Object> blackList = new ArrayList<Object>();
		DataSourceContextHolder.clearDbType();
		if (list != null && list.size() > 0) {
			// try {
			JSONArray bugInfoJson = JSONArray.fromObject(list);
			json.put("logs_gc", bugInfoJson);
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_SENDMESSAGE);
			// } catch (Exception e) {
			// json.put("validateCode", MessageUtils.FAIL_CODE);
			// json.put("msg", MessageUtils.FAIL_MSG);
			// }
		} else {
			json.put("logs_gc", blackList);
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
		return json;
	}

	// getBugTrackingList
	/**
	 * 
	 * @param bugID
	 * @param context
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public JSONObject getBugTrackingList(int bugID) throws Exception {
		JSONObject json = new JSONObject();
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TECHEXCEL);
		List bugtrackList = bugInfoService.getBugTrace(bugID);
		List bugDealStatus = bugInfoService.getBugStatus(bugID);
		DataSourceContextHolder.clearDbType();
		ArrayList<Object> list1 = new ArrayList<Object>();
		String finishTime = null;
		if (bugtrackList != null && bugtrackList.size() > 0) {
			for (int i = 0; i < bugtrackList.size(); i++) {
				Map bugtrackMap = (Map) bugtrackList.get(i);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("TRANSITIONNAME", bugtrackMap.get("TRANSITIONNAME"));
				map.put("SEQUENCENO", bugtrackMap.get("SEQUENCENO"));
				map.put("DATEASSIGNED", bugtrackMap.get("DATEASSIGNED"));
				list1.add(map);
				if(!(bugtrackMap.get("TRANSITIONNAME").equals(""))||!((bugtrackMap.get("TRANSITIONNAME")).equals(null))){
					if (bugtrackMap.get("TRANSITIONNAME").equals("处理完成")) {
						finishTime = (String) bugtrackMap.get("DATEASSIGNED");
					}
				}
			}
			json.put("trackList", list1);
		} else {
			json.put("trackList", list1);
		}
		String Des = null;
		ArrayList<Object> list3 = new ArrayList<Object>();
		if (bugDealStatus != null && bugDealStatus.size() > 0) {
			for (int i = 0; i < bugDealStatus.size(); i++) {
				Map bugStatus = (Map) bugDealStatus.get(i);
				HashMap<String, Object> map = new HashMap<String, Object>();
				String DateCreated = String.valueOf(bugStatus.get("DateCreated")).substring(0, 19);
				Des = (String) bugStatus.get("ProblemDescription");
				String DateCreated1 = null;
				if (bugStatus.get("PROGRESSSTATUSNAME").equals("已完成")) {
					DateCreated1 = getTime(DateCreated, finishTime);
				} else {
					DateCreated1 = getTime1(DateCreated);
				}
				String evaluateID = String.valueOf(bugStatus.get("EvaluationValueID"));
				Integer evaluateID1 = null;
				if ("" != evaluateID && "null" != evaluateID) {
					evaluateID1 = Integer.parseInt(evaluateID);
				}
				map.put("title", bugStatus.get("BugTitle"));
				map.put("creatTime", DateCreated1);
				map.put("content", Des);
				map.put("status", bugStatus.get("PROGRESSSTATUSNAME"));
				map.put("evaluateID", evaluateID1);
				map.put("evaluateContent", bugStatus.get("EvaluationComments"));
				json.put("orderDetail", map);
				json.put("orderDetail1", getDesDetail(Des, DateCreated));
			}
		} else {
			HashMap<String, Object> map = new HashMap<String, Object>();
			json.put("orderDetail", map);
			json.put("orderDetail1", list3);
		}

		return json;
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

	public static String convert(String ampm) throws java.text.ParseException {
		// 在此处写出解决问题的程序代码 …
		System.out.println("ampm=" + ampm);
		SimpleDateFormat sdft = new SimpleDateFormat("MM/dd/yy HH:mm");
		SimpleDateFormat sdft1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = "";
		if (ampm.contains("AM")) {
			str = ampm.substring(0, ampm.indexOf("AM"));
			Date date = sdft.parse(str);
			return sdft1.format(date);
		} else if (ampm.contains("PM")) {
			int i = ampm.indexOf(":");
			String s = ampm.substring(10, i);
			int hour = Integer.parseInt(s);
			if (hour >= 12) {
				String res = ampm.substring(0, ampm.indexOf("PM"));
				return res;
			} else {
				String sHour = hour + 12 + "";
				String ampm1 = ampm.substring(0, 10) + sHour + ampm.substring(ampm.indexOf(":"), ampm.indexOf("PM"));
				Date date = sdft.parse(ampm1);
				return sdft1.format(date);
			}
			/*
			 * str = ampm.substring(0, ampm.indexOf("PM")); Date date =
			 * sdft.parse(str); return sdft1.format(date);
			 */
		} else {
			return ampm;
		}
	}

	/***
	 * 
	 */
	// 直接编辑报障记录
	public JSONObject editTroubleByBugID(int bugID, String Des, String telPhone) {
		JSONObject json = new JSONObject();
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TECHEXCEL);
		List bugDesc = bugInfoService.getProblemDesc(bugID);
		DataSourceContextHolder.clearDbType();
		String description = null;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bugDesc.size(); i++) {
			Map descMap = (Map) bugDesc.get(i);
			description = (String) descMap.get("ProblemDescription");
			sb.append(description);
		}
		sb.append(getCurrentDescription(telPhone));
		sb.append(Des);
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TECHEXCEL);
		Integer i = bugInfoService.updatemDesc(bugID, sb.toString());
		DataSourceContextHolder.clearDbType();
		// try {
		json.put("code", i);
		// json.put("status", "1");
		// json.put("msg", "成功");
		// } catch (Exception e) {
		// json.put("status", "0");
		// json.put("msg", "失败");
		// }
		return json;
	}

	// 输出编辑内容的最终结果
	public static String getCurrentDescription(String currentUser) {
		StringBuilder sb = new StringBuilder("\n\n");
		sb.append("[");
		sb.append(currentUser);
		// sb.append(" ");
		// sb.append(currentRole);
		// sb.append(" ");
		sb.append("--");
		sb.append(" ");
		sb.append(getCurrentTime());
		sb.append("]");
		sb.append("\n");
		return sb.toString();
	}
	// 输出当前时间

	public static String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy hh:mm");
		Date date = new Date();
		String new_date = sdf.format(date);
		Calendar c1 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		String final_date = null;
		int time = c1.get(Calendar.HOUR_OF_DAY);
		if (time > 12) {
			final_date = new_date + " " + "PM";
		} else {
			final_date = new_date + " " + "AM";
		}
		// //System.out.println(final_date);
		return final_date;
	}

	public JSONObject uploadPicfile(String bugID, String picData, String linkedSystemID, String linkedProjectID,
			String userID, String incidentID, String size)
			throws SQLException, NamingException, IOException, ParseException {
		Long start = System.currentTimeMillis();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDate = df.format(new Date());
		// System.out.println("uploadPicfile is running...");
		String path = Thread.currentThread().getContextClassLoader().getResource(".").getPath();
		String filepath = path.substring(0, path.lastIndexOf("lib/"));
		String docStorePath = filepath + "webapps/pcp/tmp";
		File docstoreDir = new File(docStorePath);
		if (!(docstoreDir.exists() && docstoreDir.isDirectory())) {
			docstoreDir.mkdirs();
		}
		String storePath = filepath + "webapps/pcp/tmp/img";
		File storeDir = new File(storePath);
		if (!(storeDir.exists() && storeDir.isDirectory())) {
			storeDir.mkdirs();
		}
		BASE64Decoder decoder = new BASE64Decoder();
		String[] arr = picData.split(",");
		JSONObject json = new JSONObject();
		String fids = "";
		filepath = filepath.substring(1);
		// System.out.println(filepath);
		for (int i = 0; i < arr.length; i++) {
			// try {
			String fid = UUID.randomUUID().toString().replaceAll("-", "");
			String storeFileName = fid;
			byte[] decodedBytes = decoder.decodeBuffer(arr[i]);
			InetAddress inet = InetAddress.getLocalHost();
			// 大图真实的物理地址
			String RealityBigUrl = storePath + "/" + storeFileName + "big.jpg";
			// 小图真实的物理地址
			String RealitySmUrl = storePath + "/" + storeFileName + "small.jpg";
			// 原图所在服务器FTP地址
			String TomcatBigUrl = "http://" + inet.getHostAddress() + ":8080/pcp/tmp/img/" + storeFileName + "big.jpg";
			// 小图所在服务器FTP地址
			String TomcatSmUrl = "http://" + inet.getHostAddress() + ":8080/pcp/tmp/img/" + storeFileName + "small.jpg";
			// 上传原图
			FileOutputStream out = new FileOutputStream(RealityBigUrl);
			out.write(decodedBytes);
			out.close();
			// 上传小图
			File file = new File(RealityBigUrl);
			Image img = ImageIO.read(file); // 构造Image对象
			int width = img.getWidth(null); // 得到源图宽
			int height = img.getHeight(null); // 得到源图长
			TroubleController imgCom = new TroubleController(RealityBigUrl);
			imgCom.resizeFix(width, height, RealitySmUrl);
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
			bugInfoService.addImg(bugID, TomcatBigUrl, TomcatSmUrl);
			DataSourceContextHolder.clearDbType();
			if (fids.equals("") || fids == null)
				fids = fid;
			else
				fids = fids + "," + fid;
		}
		json.put("fids", fids);
		return json;
	}

	/**
	 * 按照宽度还是高度进行压缩
	 * 
	 * @param w
	 *            int 最大宽度
	 * @param h
	 *            int 最大高度
	 * @param realitySmUrl
	 */
	public void resizeFix(int w, int h, String realitySmUrl) throws IOException {
		if (width / height > w / h) {
			resizeByWidth(w, realitySmUrl);
		} else {
			resizeByHeight(h, realitySmUrl);
		}
	}

	/**
	 * 以宽度为基准，等比例放缩图片
	 * 
	 * @param w
	 *            int 新宽度
	 * @param realitySmUrl
	 */
	public void resizeByWidth(int w, String realitySmUrl) throws IOException {
		int h = (int) (height * w / width);
		resize(w, h, realitySmUrl);
	}

	/**
	 * 以高度为基准，等比例缩放图片
	 * 
	 * @param h
	 *            int 新高度
	 * @param realitySmUrl
	 */
	public void resizeByHeight(int h, String realitySmUrl) throws IOException {
		int w = (int) (width * h / height);
		resize(w, h, realitySmUrl);
	}

	/**
	 * 强制压缩/放大图片到固定的大小
	 * 
	 * @param w
	 *            int 新宽度
	 * @param h
	 *            int 新高度
	 * @param realitySmUrl
	 */
	public void resize(int w, int h, String realitySmUrl) throws IOException {
		// SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图
		File destFile = new File(realitySmUrl);
		FileOutputStream out = new FileOutputStream(destFile); // 输出到文件流
		// 可以正常实现bmp、png、gif转jpg
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		encoder.encode(image); // JPEG编码
		out.close();
	}

	/**
	 * 添加评价功能
	 * 
	 * @param bugID
	 * @param evaluate
	 * @param context
	 * @param remark
	 * @return
	 */

	public JSONObject editTroubleevaluateByBugID(int bugID, String evaluate, String remark) {
		Map requestMap = TechexcelService.getRequest();
		HttpServletResponse rs = (HttpServletResponse) requestMap.get("rs");
		JSONObject json = new JSONObject();
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TECHEXCEL);
		int i = bugInfoService.editTrouble(remark, evaluate, bugID);
		DataSourceContextHolder.clearDbType();
		try {
			json.put("code", i);
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_SENDMESSAGE);
		} catch (Exception e) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
		return json;
	}

	public JSONArray getTheaterDeviceData(String TheaterId, String positionID) {
		JSONObject json = new JSONObject();
		// 获取影院下所有设备的sql
		List theater_device;
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TECHEXCEL);
		if (positionID.equals("") || positionID.equals(null)) {
			theater_device = cinemaDeviceInfoService.getEquipment(TheaterId);
		} else {
			if (positionID.equals("未知")) {
				theater_device = cinemaDeviceInfoService.getEquipment(TheaterId);
			} else {
				theater_device = cinemaDeviceInfoService.getEquipment1(TheaterId, positionID);
			}
		}
		DataSourceContextHolder.clearDbType();
		JSONArray theaterdevice = JSONArray.fromObject(theater_device);
		return theaterdevice;
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
			// //System.out.println(code);
			if (code == 200) {
				// //System.out.println("插入成功");
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

	// 获得影院设备列表
	@RequestMapping(value = "/schedualdata")
	@ResponseBody
	public JSONObject getSchedualData(String theaterId, String date)
			throws SQLException, ClassNotFoundException, ParseException, java.text.ParseException {
		Map mp = TechexcelService.getRequest();
		HttpServletResponse rs = (HttpServletResponse) mp.get("rs");
		String jsonp = (String) mp.get("callback");
		JSONObject json = new JSONObject();
		SimpleDateFormat myFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if (!date.equals("")) {
			String date0 = myFmt.format(new Date());
			String date1 = date + " 05:30";
			Date dt1 = myFmt.parse(date0);
			Date dt2 = myFmt.parse(date1);
			String Time1 = "";
			String Time2 = "";
			// 判断当前事前是否过了今日的5：30，如果过了 就获取今日的排期，如果没过就还是昨日的排期
			if (dt1.getTime() < dt2.getTime()) {
				Date Date1 = myFmt.parse(date1);
				Calendar rightNow = Calendar.getInstance();
				rightNow.setTime(Date1);
				rightNow.add(Calendar.DAY_OF_YEAR, -1);// 日期减1天
				String date2 = myFmt.format(rightNow.getTime());
				Time1 = date2;
				Time2 = date1;
			} else {
				Date Date1 = myFmt.parse(date1);
				Calendar rightNow = Calendar.getInstance();
				rightNow.setTime(Date1);
				rightNow.add(Calendar.DAY_OF_YEAR, 1);// 日期加1天
				String date2 = myFmt.format(rightNow.getTime());
				Time1 = date1;
				Time2 = date2;
			}
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TICKET);
			List schedualList = ticketSystemService.getSchedualInfo(date, theaterId);
			List schedualDetail = ticketSystemService.getSchedualDetail(Time1, Time2, theaterId);
			DataSourceContextHolder.clearDbType();
			ArrayList<Object> list = new ArrayList<Object>();
			if (schedualList != null && schedualList.size() > 0 && schedualDetail != null
					&& schedualDetail.size() > 0) {
				for (int i = 0; i < schedualList.size(); i++) {
					Map schedualMap = (Map) schedualList.get(i);
					String hallName = (String) schedualMap.get("hallName");
					ArrayList<Object> list1 = new ArrayList<Object>();
					for (int j = 0; j < schedualDetail.size(); j++) {
						Map schedualDetail1 = (Map) schedualDetail.get(j);
						String hallName1 = (String) schedualDetail1.get("hallName");
						if (hallName1.equals(hallName)) {
							HashMap<String, Object> map1 = new HashMap<String, Object>();
							map1.put("showName", schedualDetail1.get("showName"));
							map1.put("moiveCode", schedualDetail1.get("moiveCode"));
							map1.put("duration", schedualDetail1.get("duration"));
							map1.put("showTime", schedualDetail1.get("showTime"));
							map1.put("audiences", schedualDetail1.get("audiences"));
							list1.add(map1);
						}
					}

					if (hallName.length() >= 5) {
						hallName = hallName.substring(0, 4);
						hallName = hallName + "...";
					}
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("hallName", hallName);
					map.put("seats", schedualMap.get("seats"));
					map.put("showList", list1);
					list.add(map);
				}
				try {
					json.put("logs", list);
					json.put("validateCode", MessageUtils.SUCCESS_CODE);
					json.put("msg", MessageUtils.SUCCESS_SENDMESSAGE);
				} catch (Exception e) {
					json.put("logs", list);
					json.put("validateCode", MessageUtils.FAIL_CODE);
					json.put("msg", MessageUtils.FAIL_MSG);
				}
			} else {
				json.put("validateCode", MessageUtils.FAIL_CODE);
				json.put("msg", MessageUtils.FAIL_MSG);
			}

			return json;
		}
		return null;
	}

	public JSONObject getTheaterInfoByDeviceCode(String DeviceCode) throws SQLException, NamingException {
		Map requestMap = TechexcelService.getRequest();
		String jsonp = (String) requestMap.get("jsonp");
		HttpServletResponse rs = (HttpServletResponse) requestMap.get("rs");
		JSONObject json = new JSONObject();
		DeviceCode = DeviceCode.trim();
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TECHEXCEL);
		List theaterInfoList = bugInfoService.getTheaterInfoByDeviceCode(DeviceCode);
		DataSourceContextHolder.clearDbType();
		try {
			JSONArray theaterInfoList1 = JSONArray.fromObject(theaterInfoList);
			json.put("DeviceInfo", theaterInfoList1);
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_SENDMESSAGE);
		} catch (Exception e) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
		return json;
	}

	public void getUnsignBugList(String BugIDs) throws Exception {
		Map requestMap = TechexcelService.getRequest();
		String jsonp = (String) requestMap.get("jsonp");
		HttpServletResponse rs = (HttpServletResponse) requestMap.get("rs");
		JSONObject json = new JSONObject();
		if (BugIDs.length() > 0) {
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TECHEXCEL);
			List unsignBugList = bugInfoService.getUnsignBugList(BugIDs);
			DataSourceContextHolder.clearDbType();
			try {
				JSONArray unsignBugList1 = JSONArray.fromObject(unsignBugList);
				json.put("BugList", unsignBugList1);
				json.put("validateCode", MessageUtils.SUCCESS_CODE);
				json.put("msg", MessageUtils.SUCCESS_SENDMESSAGE);
			} catch (Exception e) {
				json.put("validateCode", MessageUtils.FAIL_CODE);
				json.put("msg", MessageUtils.FAIL_MSG);
			}
		} else {
			json.put("BugList", "");
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
	}

}
