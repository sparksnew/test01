//package com.oristartech.cinema.weixin;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.httpclient.HttpException;
//
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import com.mysql.jdbc.Messages;
//import com.oristartech.cinema.pojo.Massages;
//import com.oristartech.cinema.pojo.Token;
//import com.oristartech.cinema.pojo.WeixinUserInfo;
//import com.oristartech.cinema.utils.CommonUtil;
//
//import net.sf.json.JSONObject;
//
///**
// * 微信推送信息
// * 
// * @author My
// * @CreateDate 2016-1-19
// * @param
// */
//public class WeixinPushMassage {
//
//	public static void wxTuisong(Massages massages) {
//
//		// 微信接口
//		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
//		// Token token = headtoken("wx1c98444c34477234",
//		// "5f7005d182d340cd13c844d5e10dbe32");// 获取token By谢宇测试号
//		Token token = headtoken("wxa194b285a43b3faa", "f0efc5f2d9868d2047bd0dd1b0c5b029");// 获取token
//		url = url.replace("ACCESS_TOKEN", token.getAccessToken());// 转换为响应接口模式
//
//		// 封装数据
//		JSONObject json = new JSONObject();
//		json.put("touser", massages.getWxName());// 接收者wxName
//		json.put("template_id", "6fKD0kI-a1N942PSlm165D0anBH3GEta3lTBIy5uzQI");// 消息模板
//		// json.put("url", "http://weix");//填写url可查看详情
//
//		JSONObject dd = new JSONObject();
//
//		JSONObject dd2 = new JSONObject();
//		dd2.put("value", "你收到来自于techExcel的报账单，请注意查看...");// 消息提示
//		dd2.put("color", "#173177");
//		dd.put("first", dd2);
//
//		JSONObject cc2 = new JSONObject();
//		cc2.put("value", massages.getBugDetail());// 故障现象
//		cc2.put("color", "#173177");
//		dd.put("performance", cc2);
//
//		JSONObject ee2 = new JSONObject();
//		ee2.put("value", massages.getBugTime());// 故障时间
//		ee2.put("color", "#173177");
//		dd.put("time", ee2);
//
//		JSONObject gg2 = new JSONObject();
//		// gg2.put("value", "发货人：" + massages.getSndPerson() +
//		// massages.getCellPhone() + "\n收货人：" + massages.getRcvPerson()
//		// + massages.getRcvPhone() + "操作类型" + massages.getLastTime() +
//		// massages.getLastType() + "\n\n\n");
//		gg2.put("value", massages.getRemarks() + "\n");
//		gg2.put("color", "#000000");
//		dd.put("remark", gg2);
//
//		json.put("data", dd);
//		System.out.println(json.toString());
//		JSONObject js = CommonUtil.httpsRequest(url, "POST", json.toString());
//		System.out.println("js==" + js);
//	}
//
//	/**
//	 * 请求token
//	 * 
//	 * @Description :
//	 * @param
//	 * @return ---------------
//	 * @Author : My
//	 * @CreateData : 2016-1-18
//	 */
//	public static Token headtoken(String appId, String appSrecet) {
//		Token token = new Token();
//		token = CommonUtil.getToken(appId, appSrecet);
//		return token;
//	}
//
//	/**
//	 * 获取公众号关注的用户openid
//	 * 
//	 * @return
//	 */
//	public static List<String> getUserOpenId(String access_token, String nextOpenid) throws HttpException, IOException {
//		String path = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID";
//		path = path.replace("ACCESS_TOKEN", access_token).replace("NEXT_OPENID", nextOpenid);
//		System.out.println("path:" + path);
//		List<String> result = null;
//		JSONObject js = CommonUtil.httpsRequest(path, "POST", "");
//		System.out.println("js==" + js);
//		Map map = js;
//		Map tmapMap = (Map) map.get("data");
//		result = (List<String>) tmapMap.get("openid");
//		System.out.println(result.toString());
//		return result;
//	}
//
//	/**
//	 * 通过用户openid 获取用户信息
//	 * 
//	 * @param userOpenids
//	 * @return
//	 * @throws HttpException
//	 */
//	public static List<WeixinUserInfo> getUserInfo(List<String> userOpenids, String token) throws HttpException {
//		// 1、获取access_token
//		// 使用测试 wx9015ccbcccf8d2f5 02e3a6877fa5fdeadd78d0f6f3048245
//		// 2、封装请求数据
//		List user_list = new ArrayList<Map>();
//		for (int i = 0; i < userOpenids.size(); i++) {
//			String openid = userOpenids.get(i);
//			Map tUserMap = new HashMap<String, String>();
//			tUserMap.put("openid", openid);
//			tUserMap.put("lang", "zh_CN");
//			user_list.add(tUserMap);
//		}
//		System.out.println(user_list.toString());
//		Map requestMap = new HashMap<String, List>();
//		requestMap.put("user_list", user_list);
//		String tUserJSON = JSONObject.fromObject(requestMap).toString();
//
//		// 3、请求调用
//		JSONObject result = getUserInfobyHttps(token, tUserJSON);
//		// 4、解析返回将结果
//		return parseUserInfo(result);
//	}
//
//	/**
//	 * 解析返回用户信息数据
//	 * 
//	 * @param result
//	 * @return
//	 */
//	private static List<WeixinUserInfo> parseUserInfo(JSONObject result) {
//		List user_info_list = new ArrayList<WeixinUserInfo>();
//
//		Map tMapData = result;
//
//		List<Map> tUserMaps = (List<Map>) tMapData.get("user_info_list");
//
//		for (int i = 0; i < tUserMaps.size(); i++) {
//			WeixinUserInfo tUserInfo = new WeixinUserInfo();
//			tUserInfo.setSubscribe((Integer) tUserMaps.get(i).get("subscribe"));
//			tUserInfo.setSex((Integer) tUserMaps.get(i).get("sex"));
//			tUserInfo.setOpenId((String) tUserMaps.get(i).get("openid"));
//			tUserInfo.setNickname((String) tUserMaps.get(i).get("nickname"));
//			tUserInfo.setLanguage((String) tUserMaps.get(i).get("language"));
//			tUserInfo.setCity((String) tUserMaps.get(i).get("city"));
//			tUserInfo.setProvince((String) tUserMaps.get(i).get("province"));
//			tUserInfo.setCountry((String) tUserMaps.get(i).get("country"));
//			tUserInfo.setHeadimgurl((String) tUserMaps.get(i).get("headimgurl"));
//			tUserInfo.setSubscribetime((Integer) tUserMaps.get(i).get("subscribe_time"));
//			tUserInfo.setRemark((String) tUserMaps.get(i).get("remark"));
//			tUserInfo.setGroupid((Integer) tUserMaps.get(i).get("groupid"));
//			user_info_list.add(tUserInfo);
//		}
//
//		return user_info_list;
//	}
//
//	/**
//	 * 调用HTTPS接口，获取用户详细信息
//	 * 
//	 * @param access_token
//	 * @param requestData
//	 * @return
//	 */
//	private static JSONObject getUserInfobyHttps(String access_token, String requestData) throws HttpException {
//		// 返回报文
//		JSONObject js = null;
//		String path = "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=ACCESS_TOKEN";
//		path = path.replace("ACCESS_TOKEN", access_token);
//
//		try {
//			js = CommonUtil.httpsRequest(path, "POST", requestData);
//			System.out.println(js);
//		} catch (Exception e) {
//			System.out.println(e);
//		} finally {
//		}
//		return js;
//	}
//
//	public static void main(String args[]) {
//		Massages massages = new Massages();
//		massages.setWxName("oXx9IwaRSaU1hjgz4FjKzfvRNehU");
//		massages.setBugDetail("放映机坏了");
//		massages.setBugTime("2017年11月7日");
//		massages.setRemarks("故障编号：100" + "\n故障描述：设备不能用了，需要尽快帮助恢复" + "\n故障位置：2号厅");
//		wxTuisong(massages);
////		Token token = headtoken("wxa194b285a43b3faa", "f0efc5f2d9868d2047bd0dd1b0c5b029");// 获取token
////		try {
////			getUserInfo(getUserOpenId(token.getAccessToken(), ""), token.getAccessToken());
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//	}
//}
