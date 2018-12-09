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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



public class HttpClientUtil {
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
						}else if(url.contains("CurrentShowInfo")){
							
							res =CurrentShowInfo1();}
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
		/*params.put("theaterCode", "91905531");
		params.put("type", "");
		params.put("search", "");
		params.put("pageSize", "1");
		params.put("page", "1");
//		params.put("endDate", "2018-11-28");
//		params.put("isAll", "0");
		//String tmsGET = tmsGET(SmartAgentAdress.NextShowInfo,"91905531");
		//String res = HttpClientUtil.tmsGET(SmartAgentAdress.SplDetailInfo,"91905531","172");
		String res = HttpClientUtil.tmsGET(SmartAgentAdress.FilmKeyInfo,"91905531","5fc95c4a-cdd4-4b51-979d-d2da15391dd5");
		System.out.println(res);*/
		String currentShowInfo = CurrentShowInfo();
		System.out.println(currentShowInfo);
	}
	///////////////////////挡板///////////////////////////
	public static String ShowInfo(){
		JSONObject arr = new JSONObject();
		arr.put("status", "800");
		Map msgMap = new HashMap<>();
		msgMap.put("hall_id", 11);
		msgMap.put("hall_name", "3号墨艺绘馆");
		msgMap.put("ticket_hall_code", "0000000000000003");
		//List showList = new ArrayList<>();
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
			
		}
	public static String CurrentShowInfo(){
		JSONObject arr = new JSONObject();
		arr.put("status", "800");
		Map msgMap = new HashMap<>();
		msgMap.put("play_status", 1);
		msgMap.put("start_time", "1541483408");
		msgMap.put("end_time", "1541490990");
		msgMap.put("spl_id", "150");
		msgMap.put("spl_name", "kongzijt0124_DCP_e_cpl");
		msgMap.put("show_mode", "0");
		msgMap.put("duration", "7582");
		msgMap.put("play_time", "4952");
		List cplList = new ArrayList<>();
		Map map1 = new HashMap<>();
		map1.put("cpl_uuid", "5fc95c4a-cdd4-4b51-979d-d2da15391dd5");
		map1.put("file_name", "kongzijt0124_DCP_e_cpl");
		map1.put("duration", "10");
		Map map2 = new HashMap<>();
		map2.put("cue_code", "4001");
		map2.put("cue_name", "打开灯光1");
		map2.put("exe_status", "0");
		map2.put("exe_time", "");
		map2.put("offset_time", "0");
		
		map1.put("cueList", map2);
		
		///////////////////////////////////////////////
		Map map4 = new HashMap<>();
		List cueList = new ArrayList<>();
		map4.put("cpl_uuid", "9acf22d7-3a06-415b-bec3-78f832038041");
		map4.put("file_name", "tianchengzhubao-jpg239");
		map4.put("duration", "30");
		Map map3 = new HashMap<>();
		map3.put("cue_code", "4002");
		map3.put("cue_name", "打开灯光2");
		map3.put("exe_status", "10");
		map3.put("exe_time", "1");
		map3.put("offset_time", "10");
		
		Map map5 = new HashMap<>();
		map5.put("cue_code", "4003");
		map5.put("cue_name", "打开灯光3");
		map5.put("exe_status", "1");
		map5.put("exe_time", "13");
		map5.put("offset_time", "10");
		cueList.add(map3);
		cueList.add(map5);
		map4.put("cueList", cueList);
		///////////////////////////////////////////////
		Map map6 = new HashMap<>();
		map6.put("cpl_uuid", "sd2da153-cdd4-4b51-979d-d2da15391dd5");
		map6.put("file_name", "jingmen0124_DCP_e_cpl");
		map6.put("duration", "70");
		map6.put("cueList", "");
		///////////////////////////////////////////////
		cplList.add(map1);
		cplList.add(map4);
		cplList.add(map6);
		msgMap.put("cplList", cplList);
		arr.put("msg", msgMap);
		String arr1 = arr.toString();
		return arr1;
		
	}
	public static String errorMsg(){
		JSONObject arr = new JSONObject();
		arr.put("status", "800");
		arr.put("msg", "TMS连接失败");
		String arr1 = arr.toString();
		return arr1;
		
	}
	
	public static String CurrentShowInfo1(){
		JSONObject arr = new JSONObject();
		arr.put("status", "800");
		JSONObject msgJson = new JSONObject();
		//Map msgMap = new HashMap<>();
		msgJson.put("play_status", 1);
		msgJson.put("start_time", "1541483408");
		msgJson.put("end_time", "1541490990");
		msgJson.put("spl_id", "150");
		msgJson.put("spl_name", "kongzijt0124_DCP_e_cpl");
		msgJson.put("show_mode", "0");
		msgJson.put("duration", "7582");
		msgJson.put("play_time", "4952");
		List  cplList = new ArrayList<>();
		//JSONArray cplList1 = new JSONArray();
		//List cplList = new ArrayList<>();
		JSONObject arr1 = new JSONObject();
		arr1.put("cpl_uuid", "5fc95c4a-cdd4-4b51-979d-d2da15391dd5");
		arr1.put("file_name", "kongzijt0124_DCP_e_cpl");
		arr1.put("duration", "10");
		arr1.put("type", "1");
		List cueList = new ArrayList<>();
		JSONObject arr2 = new JSONObject();
		arr2.put("cue_code", "4001");
		arr2.put("cue_name", "打开灯光1");
		arr2.put("exe_status", "1");
		arr2.put("exe_time", "");
		arr2.put("offset_time", "0");
		cueList.add(arr2);
		arr1.put("cueList", cueList);
		
		///////////////////////////////////////////////
		JSONObject arr4 = new JSONObject();
		//Map map4 = new HashMap<>();
		arr4.put("cpl_uuid", "9acf22d7-3a06-415b-bec3-78f832038041");
		arr4.put("file_name", "tianchengzhubao-jpg239");
		arr4.put("duration", "30");
		arr4.put("type", "3");
		List cueList1 = new ArrayList<>();
		JSONObject cueInfo = new JSONObject();
		//Map map3 = new HashMap<>();
		cueInfo.put("cue_code", "4002");
		cueInfo.put("cue_name", "打开灯光2");
		cueInfo.put("exe_status", "-1");
		cueInfo.put("exe_time", "1");
		cueInfo.put("offset_time", "10");
		cueList.add(cueInfo);
		JSONObject cueInfo1 = new JSONObject();
		//Map map5 = new HashMap<>();
		cueInfo1.put("cue_code", "4003");
		cueInfo1.put("cue_name", "打开灯光3");
		cueInfo1.put("exe_status", "1");
		cueInfo1.put("exe_time", "13");
		cueInfo1.put("offset_time", "10");
		cueList1.add(cueInfo);
		cueList1.add(cueInfo1);
		arr4.put("cueList", cueList1);
		///////////////////////////////////////////////
		/////////////////如下为不同type的影院/////////////////////////////
		JSONObject arr5 = new JSONObject();
		arr5.put("cpl_uuid", "5fc95c4a-cdd4-4b51-979d-d2da15391dd5");
		arr5.put("file_name", "kongzijt0124_DCP_e_cpl");
		arr5.put("duration", "10");
		arr5.put("type", "4");
		List cueList3 = new ArrayList<>();
		JSONObject arr6 = new JSONObject();
		arr6.put("cue_code", "4001");
		arr6.put("cue_name", "打开灯光1");
		arr6.put("exe_status", "1");
		arr6.put("exe_time", "");
		arr6.put("offset_time", "0");
		cueList3.add(arr6);
		arr5.put("cueList", cueList3);
		////////////////////////////////////////////////////////////
		JSONObject arr7 = new JSONObject();
		arr7.put("cpl_uuid", "5fc95c4a-cdd4-4b51-979d-d2da15391dd5");
		arr7.put("file_name", "kongzijt0124_DCP_e_cpl");
		arr7.put("duration", "10");
		arr7.put("type", "8");
		List cueList4 = new ArrayList<>();
		JSONObject arr8 = new JSONObject();
		arr8.put("cue_code", "4001");
		arr8.put("cue_name", "打开灯光1");
		arr8.put("exe_status", "1");
		arr8.put("exe_time", "");
		arr8.put("offset_time", "0");
		cueList4.add(arr8);
		arr7.put("cueList", cueList4);
		////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////
		JSONObject arr9 = new JSONObject();
		arr9.put("cpl_uuid", "5fc95c4a-cdd4-4b51-979d-d2da15391dd5");
		arr9.put("file_name", "kongzijt0124_DCP_e_cpl");
		arr9.put("duration", "10");
		arr9.put("type", "9");
		List cueList5 = new ArrayList<>();
		JSONObject arr10 = new JSONObject();
		arr10.put("cue_code", "4001");
		arr10.put("cue_name", "打开灯光1");
		arr10.put("exe_status", "1");
		arr10.put("exe_time", "");
		arr10.put("offset_time", "0");
		cueList5.add(arr10);
		arr9.put("cueList", cueList5);
		////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////
		JSONObject arr11 = new JSONObject();
		arr11.put("cpl_uuid", "5fc95c4a-cdd4-4b51-979d-d2da15391dd5");
		arr11.put("file_name", "kongzijt0124_DCP_e_cpl");
		arr11.put("duration", "10");
		arr11.put("type", "10");
		List cueList6 = new ArrayList<>();
		JSONObject arr12 = new JSONObject();
		arr12.put("cue_code", "4001");
		arr12.put("cue_name", "打开灯光1");
		arr12.put("exe_status", "1");
		arr12.put("exe_time", "");
		arr12.put("offset_time", "0");
		cueList6.add(arr12);
		arr11.put("cueList", cueList6);
		////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////
		JSONObject arr13 = new JSONObject();
		arr13.put("cpl_uuid", "5fc95c4a-cdd4-4b51-979d-d2da15391dd5");
		arr13.put("file_name", "kongzijt0124_DCP_e_cpl");
		arr13.put("duration", "10");
		arr13.put("type", "11");
		List cueList7 = new ArrayList<>();
		JSONObject arr14 = new JSONObject();
		arr14.put("cue_code", "4001");
		arr14.put("cue_name", "打开灯光1");
		arr14.put("exe_status", "1");
		arr14.put("exe_time", "");
		arr14.put("offset_time", "0");
		cueList7.add(arr14);
		arr13.put("cueList", cueList7);
		////////////////////////////////////////////////////////////
		/////////////////如上为不同type的影院/////////////////////////////
		JSONObject arr20 = new JSONObject();
		//Map map6 = new HashMap<>();
		arr20.put("cpl_uuid", "sd2da153-cdd4-4b51-979d-d2da15391dd5");
		arr20.put("file_name", "jingmen0124_DCP_e_cpl");
		arr20.put("duration", "70");
		arr20.put("type", "2");
		List cueList2 = new ArrayList<>();
		arr20.put("cueList", cueList2);
		///////////////////////////////////////////////
		cplList.add(arr1);
		cplList.add(arr4);
		cplList.add(arr5);
		cplList.add(arr7);
		cplList.add(arr9);
		cplList.add(arr11);
		cplList.add(arr13);
		cplList.add(arr20);
		msgJson.put("cplList", cplList);
		arr.put("msg", msgJson);
		String res = arr.toString();
		return res;
		
	}
	
}
