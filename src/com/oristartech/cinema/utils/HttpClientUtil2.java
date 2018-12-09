package com.oristartech.cinema.utils;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;



public class HttpClientUtil2 {
	@SuppressWarnings("resource")

	// public static String doPost(String url,String appCode,String
	// customerCode,Long timestamp,
	// String verifyInfo, String charset,List<BasicNameValuePair> list) {
	public static String doPost(String url, String appCode, String customerCode, String timestamp, String verifyInfo,
			String charset, String body) {
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String res = null;
		try {
			httpClient = new SSLClient();
			httpPost = new HttpPost(url);
			httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8");
			httpPost.addHeader("appCode", appCode);
			httpPost.addHeader("customerCode", customerCode);
			httpPost.addHeader("timestamp", timestamp);
			httpPost.addHeader("verifyInfo", verifyInfo);
			StringEntity stringEntity = new StringEntity(body, "utf-8");
			httpPost.setEntity(stringEntity);
			HttpEntity entity2 = httpPost.getEntity();
			HttpResponse response = httpClient.execute(httpPost);
			if (response != null) {
				int code = response.getStatusLine().getStatusCode();
				if (code == 200) {
					HttpEntity responseEntity = response.getEntity();
					res = EntityUtils.toString(responseEntity);
				}
			}
			System.out.println("POST 请求...." + httpPost.getURI());
			System.out.println("请求体的内容为" + EntityUtils.toString(entity2, "utf-8")); // 获取网页内容
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return res;
	}
	
	public static String doPost1(String url) {
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String res = null;
		try {
			httpClient = new SSLClient();
			httpPost = new HttpPost(url);
			httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8");
			httpPost.addHeader("ProjectID", "192");
			httpPost.addHeader("IncidentID", "3944");
			httpPost.addHeader("CrntStatusID", "169");
			httpPost.addHeader("IsNew", "0");
			StringEntity stringEntity = new StringEntity("296091af-fd6d-4852-be4d-908776f583e3", "utf-8");
			httpPost.setEntity(stringEntity);
			HttpEntity entity2 = httpPost.getEntity();
			HttpResponse response = httpClient.execute(httpPost);
			if (response != null) {
				int code = response.getStatusLine().getStatusCode();
				System.out.println("code="+code);
				if (code == 200) {
					HttpEntity responseEntity = response.getEntity();
					res = EntityUtils.toString(responseEntity);
					System.out.println("res="+res);
				}
			}
			System.out.println("POST 请求...." + httpPost.getURI());
			System.out.println("请求体的内容为" + EntityUtils.toString(entity2, "utf-8")); // 获取网页内容
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return res;
	}
	
	public static String doGET(String url) {
		HttpClient httpClient = null;
		HttpGet httpGet = null;
		String res = null;
		try {
			httpClient = new SSLClient();
			httpGet = new HttpGet(url);
			httpGet.addHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8");
			httpGet.addHeader("ProjectID", "192");
			httpGet.addHeader("IncidentID", "3944");
			httpGet.addHeader("CrntStatusID", "169");
			httpGet.addHeader("IsNew", "0");
			httpGet.addHeader("UserToken", "296091af-fd6d-4852-be4d-908776f583e3");
			HttpResponse response = httpClient.execute(httpGet);
			if (response != null) {
				int code = response.getStatusLine().getStatusCode();
				System.out.println("code="+code);
				if (code == 200) {
					HttpEntity responseEntity = response.getEntity();
					res = EntityUtils.toString(responseEntity);
					System.out.println("res="+res);
				}
			}
			System.out.println("POST 请求...." + httpGet.getURI());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return res;
	}

	/*public static void main(String[] args) throws Exception {
		String url = "http://114.215.42.232/MyCWAPI/V1/IncidentNextOwnerList";
		HttpClientUtil.doGET(url);
	}*/
	//针对TMS所有传入数据全不为空
	public static String tmsGET(String url,String... param) {
		System.out.println(url);
		String params = ParamLinked.link(param);
		//return link;
		HttpClient httpClient = null;
		HttpGet httpGet = null;
		String res = null;
		try {
			httpClient = new SSLClient();
			/*theaterCode="91905531";
			cplUuid = "5fc95c4a-cdd4-4b51-979d-d2da15391dd5";*/
			//String params = ParamLinked.link(theaterCode,cplUuid);
			httpGet = new HttpGet(url+params);
			httpGet.addHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8");
			httpGet.setHeader("Accept", "application/json");
			httpGet.addHeader("ip", SmartAgentAdress.TMSSeverIp);
			httpGet.addHeader("port", SmartAgentAdress.TMSSeverPort);
			HttpResponse response = httpClient.execute(httpGet);
			if (response != null) {
				int code = response.getStatusLine().getStatusCode();
				if (code == 200) {
					HttpEntity responseEntity = response.getEntity();
					res = EntityUtils.toString(responseEntity);
					if(!res.isEmpty()&&res.contains("{")){
						JSONObject  json = JSONObject.fromObject(res);
						String msg = json.get("msg").toString();
						if(msg.isEmpty()){
							res = errorMsg();	
							//res=ShowInfo();
						}
					}else{
						res = errorMsg();
						//res=ShowInfo();
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return res;
	}
	//针对TMS有些数据传入为空
	public static String tmsGETs(String url,Map<String,String> params) {
		HttpClient httpClient = null;
		HttpGet httpGet = null;
		String res = null;
		try {
			httpClient = new SSLClient();
			URIBuilder builder = new URIBuilder(url);
            if (params != null) {
                for (String key : params.keySet()) {
                    builder.addParameter(key, params.get(key));
                }
            }
            URI uri = builder.build();
			httpGet = new HttpGet(uri);
			httpGet.addHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8");
			httpGet.setHeader("Accept", "application/json");
			httpGet.addHeader("ip", SmartAgentAdress.TMSSeverIp);
			httpGet.addHeader("port", SmartAgentAdress.TMSSeverPort);
			HttpResponse response = httpClient.execute(httpGet);
			if (response != null) {
				int code = response.getStatusLine().getStatusCode();
				if (code == 200) {
					HttpEntity responseEntity = response.getEntity();
					res = EntityUtils.toString(responseEntity);
					JSONObject  json = JSONObject.fromObject(res);
					String msg = json.get("msg").toString();
					if(msg.isEmpty()){
						res = errorMsg();
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return res;
	}
	public static void main(String[] args) {
		Map<String, String> params= new HashMap<String, String>();
		params.put("theaterCode", "91905531");
		params.put("type", "");
		params.put("search", "");
		params.put("pageSize", "1");
		params.put("page", "1");
//		params.put("endDate", "2018-11-28");
//		params.put("isAll", "0");
		String tmsGET = tmsGET(SmartAgentAdress.NextShowInfo,"91905531");
		//String res = HttpClientUtil.tmsGET(SmartAgentAdress.SplDetailInfo,"91905531","172");
		//String res = HttpClientUtil2.tmsGET(SmartAgentAdress.FilmKeyInfo,"91905531","5fc95c4a-cdd4-4b51-979d-d2da15391dd5");
		System.out.println(tmsGET);
	}
	///////////////////////挡板///////////////////////////
	/*public static String ShowInfo(){
		JSONObject arr = new JSONObject();
		arr.put("status", "800");
		Map msgMap = new HashMap<>();
		msgMap.put("hall_id", 11);
		msgMap.put("hall_name", "3号墨艺绘馆");
		msgMap.put("ticket_hall_code", "0000000000000003");
		List showList = new ArrayList<>();
		Map map1 = new HashMap<>();
		map1.put("play_status", 3);
		map1.put("start_time", "1543645820");
		map1.put("end_time", "1543653020");
		map1.put("spl_id", 172);
		map1.put("spl_name", "霍元甲6");
		map1.put("show_mode", 0);
		map1.put("duration", 625);
		map1.put("play_time", 625);
		msgMap.put("showList", map1);
		arr.put("msg", msgMap);
		String arr1 = arr.toString();
		return arr1;
		
	}
	public String ShowInfoMore(){
		JSONObject arr = new JSONObject();
		arr.put("status", "800");
		Map msgMap = new HashMap<>();
		msgMap.put("hall_id", 11);
		msgMap.put("hall_name", "3号墨艺绘馆");
		msgMap.put("ticket_hall_code", "0000000000000003");
		List showList = new ArrayList<>();
		Map map1 = new HashMap<>();
		map1.put("play_status", 3);
		map1.put("start_time", "1543645820");
		map1.put("end_time", "1543653020");
		map1.put("spl_id", 172);
		map1.put("spl_name", "霍元甲6");
		map1.put("show_mode", 0);
		map1.put("duration", 625);
		map1.put("play_time", 625);
		msgMap.put("showList", map1);
		Map msgMap1 = new HashMap<>();
		msgMap1.put("hall_id", 11);
		msgMap1.put("hall_name", "3号墨艺绘馆");
		msgMap1.put("ticket_hall_code", "0000000000000004");
		List showList1 = new ArrayList<>();
		Map map2 = new HashMap<>();
		map2.put("play_status", 3);
		map2.put("start_time", "1543645820");
		map2.put("end_time", "1543660220");
		map2.put("spl_id", 172);
		map2.put("spl_name", "霍元甲999");
		map2.put("show_mode", 0);
		map2.put("duration", 625);
		map2.put("play_time", 625);
		msgMap1.put("showList", map2);
		List list = new ArrayList<>();
		list.add(msgMap);
		list.add(msgMap1);
		arr.put("msg", list);
			String arr1 = arr.toString();
			return arr1;
			
		}*/
	public static String errorMsg(){
		JSONObject arr = new JSONObject();
		arr.put("status", "800");
		arr.put("msg", "TMS连接失败");
		String arr1 = arr.toString();
		return arr1;
		
	}
}
