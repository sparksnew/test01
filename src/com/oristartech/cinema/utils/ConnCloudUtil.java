package com.oristartech.cinema.utils;

import java.io.IOException;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ConnCloudUtil {

	private static final String charset = "utf-8";
	private static final String appCode = "SCA";
	private static final String customerCode = "a1859dd31fa079ce";
	private static final String secretKey = "bb540564caf97a319ea375f959c66c48";
	private static final String conn_url = "http://api.cxcpm.com";

	public static void main(String[] args) {
//		 ConnCloudUtil.login("lwh", "123qwe","c281e09f28ee9b49");// 获取加密后的数据
		// ConnCloudUtil.getUserList("","88888888","32064211");//
		// 获取用户列表接口
		// ConnCloudUtil.getAuthority("1",customerCode); // 获取用户权限信息接口
		// ConnCloudUtil.getAuthTree("",customerCode); //获取组织机构树接口
		// ConnCloudUtil.getRoleList("0"); //获取角色列表接口
		// ConnCloudUtil.getCinemaList("7f06c2ff87f24eb484c285e54a64b809"); //
		// 获取影院信息接口
		// ConnCloudUtil.getOrgList("101",customerCode);
		 ConnCloudUtil.continueToken("f6ffa8e2-1add-4e7f-bfa2-3a72bb2efdc7",5,"c281e09f28ee9b49");//令牌续期接口
		// 未调通
		// ConnCloudUtil.validateToken("684d461a2d384597ad746f18fd7c0899");//令牌验证接口
		// 未调通
		// ConnCloudUtil.logoutToken("4b8c5da9982e4f6196c69420732f6805");
		// //令牌注销接口 未调通
		// ConnCloudUtil.push(cinemaUid, cinemaCode, systemCode, version,
		// productCode,
		// ip, port, contextPath);//系统注册信息推送接口 未调通
//		String res = null;
//		CloseableHttpClient httpCilent = HttpClients.createDefault();
//		try {
//			HttpGet httpGet = new HttpGet(
//					"http://114.215.42.232/MyCWAPI/V1/IncidentNextOwnerList?ProjectID=192&IncidentID=3944&CrntStatusID=196&IsNew=0");
//			httpGet.addHeader("Cookie", "06a6362c-b2f8-409e-9dc3-9ea8284aa42c");
//			httpGet.addHeader("LanguageID", "2");
//			// JSONObject json = new JSONObject();
//			// json.put("ProjectID", "192");
//			// json.put("IncidentID", "3944");
//			// json.put("CrntStatusID", "196");
//			// json.put("IsNew", "0");
//			// String content = json.toJSONString();
//			// StringEntity entity = new StringEntity(content, "utf-8");
//			CloseableHttpResponse response;
//			response = httpCilent.execute(httpGet);
//			if (response.getStatusLine().getStatusCode() == 200) {
//				HttpEntity responseEntity = response.getEntity();
//				res = EntityUtils.toString(responseEntity);
//				System.out.println(res);
//			}
//		} catch (ClientProtocolException e1) {
//			e1.printStackTrace();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
	}

	/**
	 * 获取加密后的数据
	 */
	public static String getVerifyInfo(String body, String timestamp, String customerCode) {
		String verifyInfo = MD5.md5(appCode + customerCode + timestamp + body + secretKey);
		return verifyInfo;
	}

	/**
	 * 系统注册信息推送接口
	 * 
	 * @return result
	 */
	public static String login(String loginName, String password, String customerCode) {
		JSONObject json = new JSONObject();
		String timestamp = String.valueOf(System.currentTimeMillis());
		String url = conn_url + "/customers/users/login";
		json.put("loginName", loginName);
		json.put("password", password);
		String body = JSONObject.toJSONString(json);
		String verifyInfo = ConnCloudUtil.getVerifyInfo(body, timestamp, customerCode);
		String result = HttpClientUtil.doPost(url, appCode, customerCode, timestamp, verifyInfo, charset, body);
		System.out.println("result=" + result);
		JSONObject resultJson = JSON.parseObject(result);
		System.out.println(resultJson);
		// ConnCloudUtil.loginDate(resultJson);
		return result;
	}

	public static JSONObject loginDate(JSONObject json, String loginName) {
		// JSONObject resultJson = json.fluentRemove("funcRights");
		return json;
	}

	/**
	 * 获取用户列表接口
	 * 
	 * @result result
	 */
	public static JSONObject getUserList(String users, String customerCode, String cinemaCode) {
		String url = conn_url + "/customers/users/list";
		String timestamp = String.valueOf(System.currentTimeMillis());
		JSONObject json = new JSONObject();
		json.put("users", users);
		String body = JSONObject.toJSONString(json);
		String verifyInfo = ConnCloudUtil.getVerifyInfo(body, timestamp, customerCode);
		String result = HttpClientUtil.doPost(url, appCode, customerCode, timestamp, verifyInfo, charset, body);
		JSONObject resultJson = JSON.parseObject(result);
		System.out.println(resultJson);
		JSONArray jsonArray = resultJson.getJSONArray("list");
		JSONArray returnArray = new JSONArray();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject userJson = (JSONObject) jsonArray.get(i);
			System.out.println(userJson);
			String dept = userJson.getString("dept");
			if (dept == null || dept.length() == 0) {
				continue;
			}
			if (dept.equals(cinemaCode)) {
				returnArray.add(userJson);
			}
		}
		System.out.println("==================");
		for (int i = 0; i < returnArray.size(); i++) {
			System.out.println(((JSONObject) returnArray.get(i)).getString("deptName")
					+ ((JSONObject) returnArray.get(i)).getString("dept"));
		}
		JSONObject json1 = new JSONObject();
		json1.put("userList", returnArray);
		return json1;
	}

	/**
	 * 获取用户权限信息接口
	 * 
	 * @result result
	 */
	public static String getAuthority(String account, String customerCode) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		String url = conn_url + "/customers/users/rights";
		JSONObject json = new JSONObject();
		json.put("account", account);
		String body = JSONObject.toJSONString(json);
		String verifyInfo = ConnCloudUtil.getVerifyInfo(body, timestamp, customerCode);
		System.out.println("body=" + body);
		System.out.println("加密后的数据=" + verifyInfo);
		String result = HttpClientUtil.doPost(url, appCode, customerCode, timestamp, verifyInfo, charset, body);
		System.out.println(result);
		return result;
	}

	/**
	 * 获取组织机构树接口
	 * 
	 * @result result
	 */
	public static String getAuthTree(String account, String customerCode) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		String url = conn_url + "/customers/orgs/tree";
		JSONObject json = new JSONObject();
		json.put("account", account);
		String body = JSONObject.toJSONString(json);
		String verifyInfo = ConnCloudUtil.getVerifyInfo(body, timestamp, customerCode);
		System.out.println("body=" + body);
		System.out.println("加密后的数据=" + verifyInfo);
		String result = HttpClientUtil.doPost(url, appCode, customerCode, timestamp, verifyInfo, charset, body);
		System.out.println(result);
		return result;
	}

	/**
	 * 获取角色列表接口
	 * 
	 * @result result
	 */
	public static String getRoleList(String roles, String customerCode) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		String url = conn_url + "/customers/roles/list";
		JSONObject json = new JSONObject();
		json.put("roles", roles);
		String body = JSONObject.toJSONString(json);
		String verifyInfo = ConnCloudUtil.getVerifyInfo(body, timestamp, customerCode);
		System.out.println("body=" + body);
		System.out.println("加密后的数据=" + verifyInfo);
		String result = HttpClientUtil.doPost(url, appCode, customerCode, timestamp, verifyInfo, charset, body);
		System.out.println(result);
		return result;
	}

	/**
	 * 获取组织下级信息接口
	 * 
	 */
	public static String getOrgList(String orgUid, String customerCode) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		String url = conn_url + "/customers/orgs/leafs";
		JSONObject json = new JSONObject();
		JSONArray userArray = new JSONArray();
		json.put("orgCode", orgUid);
		String body = JSONObject.toJSONString(json);
		String verifyInfo = ConnCloudUtil.getVerifyInfo(body, timestamp, customerCode);
		String result = HttpClientUtil.doPost(url, appCode, customerCode, timestamp, verifyInfo, charset, body);
		System.out.println(result);
		if (result != null) {
			json = JSONObject.parseObject(result);
			JSONArray jsonArray = (JSONArray) json.get("userList");
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject userJSON = (JSONObject) jsonArray.get(i);
				String status = userJSON.getString("status");
				if (status.equals("1")) {
					userArray.add(userJSON);
				}
			}
		}
		json.fluentRemove("userList");
		json.put("userList", userArray);
		System.out.println(json.toString());
		return json.toString();
	}

	/**
	 * 获取影院信息接口
	 * 
	 * @result result
	 */
	public static String getCinemaList(String cinema, String customerCode) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		String url = conn_url + "/cinemas/info";
		JSONObject json = new JSONObject();
		json.put("cinema", cinema);
		String body = JSONObject.toJSONString(json);
		String verifyInfo = ConnCloudUtil.getVerifyInfo(body, timestamp, customerCode);
		System.out.println("body=" + body);
		System.out.println("加密后的数据=" + verifyInfo);
		String result = HttpClientUtil.doPost(url, appCode, customerCode, timestamp, verifyInfo, charset, body);
		System.out.println(result);
		return result;
	}

	/**
	 * 令牌续期接口
	 * 
	 * @result result
	 */
	public static String continueToken(String token, int extendTime, String customerCode) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		String url = conn_url + "/tokens/value";
		JSONObject json = new JSONObject();
		json.put("token", token);
		json.put("extendTime", extendTime);
		String body = JSONObject.toJSONString(json);
		String verifyInfo = ConnCloudUtil.getVerifyInfo(body, timestamp, customerCode);
		System.out.println("body=" + body);
		System.out.println("加密后的数据=" + verifyInfo);
		String result = HttpClientUtil.doPost(url, appCode, customerCode, timestamp, verifyInfo, charset, body);
		System.out.println(result);
		return result;
	}

	/**
	 * 令牌验证接口
	 * 
	 * @result result
	 */
	public static String validateToken(String token, String customerCode) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		String url = conn_url + "/tokens/validate";
		JSONObject json = new JSONObject();
		json.put("token", token);
		String body = JSONObject.toJSONString(json);
		String verifyInfo = ConnCloudUtil.getVerifyInfo(body, timestamp, customerCode);
		String result = HttpClientUtil.doPost(url, appCode, customerCode, timestamp, verifyInfo, charset, body);
		return result;
	}

	/**
	 * 令牌注销接口
	 * 
	 * @result result
	 */
	public static String logoutToken(String token, String customerCode) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		String url = conn_url + "/tokens/logout";
		JSONObject json = new JSONObject();
		json.put("token", token);
		String body = JSONObject.toJSONString(json);
		String verifyInfo = ConnCloudUtil.getVerifyInfo(body, timestamp, customerCode);
		String result = HttpClientUtil.doPost(url, appCode, customerCode, timestamp, verifyInfo, charset, body);
		return result;
	}

	/**
	 * 系统注册信息推送接口
	 * 
	 * @result result
	 */
	public static String push(String cinemaUid, String cinemaCode, String systemCode, String version,
			String productCode, String ip, String port, String contextPath, String customerCode) {
		String timestamp = String.valueOf(System.currentTimeMillis());
		String url = conn_url + "/systems/push";
		JSONObject json = new JSONObject();
		json.put("cinemaUid", cinemaUid);
		json.put("cinemaCode", cinemaCode);
		json.put("systemCode", systemCode);
		json.put("version", version);
		json.put("productCode", productCode);
		json.put("ip", ip);
		json.put("port", port);
		json.put("contextPath", contextPath);
		String body = JSONObject.toJSONString(json);
		String verifyInfo = ConnCloudUtil.getVerifyInfo(body, timestamp, customerCode);
		System.out.println("body=" + body);
		System.out.println("加密后的数据=" + verifyInfo);
		String result = HttpClientUtil.doPost(url, appCode, customerCode, timestamp, verifyInfo, charset, body);
		System.out.println(result);
		return result;
	}
}
