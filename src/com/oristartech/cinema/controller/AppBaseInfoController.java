package com.oristartech.cinema.controller;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.oristartech.cinema.pojo.UserInput;
import com.oristartech.cinema.service.CinemaInfoService;
import com.oristartech.cinema.service.CustomerInfoService;
import com.oristartech.cinema.service.Dw_DimInfoService;
import com.oristartech.cinema.service.LoginInfoService;
import com.oristartech.cinema.service.RoleInfoService;
import com.oristartech.cinema.service.UserInfoService;
import com.oristartech.cinema.service.UserService;
import com.oristartech.cinema.utils.BaiduMap;
import com.oristartech.cinema.utils.ConnCloudUtil;
import com.oristartech.cinema.utils.DataSourceContextHolder;
import com.oristartech.cinema.utils.MessageUtils;
import com.oristartech.cinema.utils.RequestToMapUtil;
import com.oristartech.cinema.utils.ToolUtils;
import com.oristartech.cinema.utils.TransferMapUtil;

@RequestMapping("/AppBaseInterface/")
@Controller
public class AppBaseInfoController {

	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleInfoService roleInfoService;
	@Autowired
	private CustomerInfoService customerInfoService;
	@Autowired
	private CinemaInfoService cinemaInfoService;
	@Autowired
	private Dw_DimInfoService dw_DimInfoService;
	@Autowired
	private LoginInfoService loginInfoService;

	@RequestMapping("Login")
	@ResponseBody
	public JSONObject queryLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long startTime = System.currentTimeMillis();
		// HttpServletResponse rs = ToolUtils.getResponseType(response);
		Map<Object, Object> map = RequestToMapUtil.transfer(request);
		String sPhone = (String) map.get("sPhone");
		String sPassWord = (String) map.get("sPassWord");
		// String jsonp = (String) map.get("callback");
		System.out.println("sPhone:" + sPhone + ";sPassWord:" + sPassWord);
		com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
		Integer i = getUserNum(sPhone, sPassWord);
		if (i == 0) {
			// ToolUtils.setLoginError(json, jsonp, rs);
			json.put("status", 3);
			json.put("msg", "用户名或密码错误");
			return json;
		} else {
			try {
				json.put("list", getUserInfos(sPhone, sPassWord));
				// ToolUtils.setSuccess(json, jsonp, rs);
				json.put("status", "1");
				json.put("msg", "成功");
				long endTime = System.currentTimeMillis();
				System.out.println((endTime - startTime));
				return json;
			} catch (Exception e) {
				json.put("status", "0");
				json.put("msg", "失败");
				// ToolUtils.setUnsuccess(json, jsonp, rs);
				long endTime = System.currentTimeMillis();
				System.out.println((endTime - startTime));
				return json;
			}
		}
	}

	/**
	 * 登录
	 * 
	 * @throws Exception
	 */
	@RequestMapping("login")
	@ResponseBody
	public JSONObject login(HttpServletRequest request, HttpServletResponse response) {
		String customerCode1 = request.getParameter("customerCode");
		String customerCode = null;
		if(customerCode1!=null){
			customerCode = customerCode1.trim();
		}else{
			customerCode = customerCode1;
		}
		String loginName = request.getParameter("loginName");
		String password = request.getParameter("password");
		int isPhone = Integer.parseInt(request.getParameter("isPhone"));
		JSONObject json = new JSONObject();
		// try {
		json = userInfoService.login(loginName, password, isPhone, customerCode);
		String code = json.get("validateCode").toString();
		if (code == "200" || "200".equals(code)) {
			json.put("userInfoList", userService.queryUserInfo(loginName, customerCode));
			System.out.println("json=" + json);
		}
		// } catch (Exception e) {
		// json.put("validateCode", MessageUtils.FAIL_CODE);
		// json.put("msg", MessageUtils.FAIL_MSG);
		// }
		return json;
	}

	/**
	 * 
	 * 绑定手机号
	 * 
	 * @param userID
	 *            用户ID
	 * @param mobile
	 *            手机号
	 * @throws Exception
	 */

	@RequestMapping("bingMobile")
	@ResponseBody
	public JSONObject bingMobile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userID = request.getParameter("userID");
		String mobile = request.getParameter("mobile");
		String userName = request.getParameter("userName");
		String passWord = request.getParameter("passWord");
		String customerCode = request.getParameter("customerCode");
		JSONObject json = new JSONObject();
		try {
//			int count = userInfoService.queryMobileIsExist(mobile);
//			if (count == 200) {// 改手机号未绑定
				json = userInfoService.bindMobile(userID, userName, passWord, mobile, customerCode);
//			} else {
//				json.put("validateCode", MessageUtils.FAIL_CODE);
//				json.put("msg", MessageUtils.FAIL_MSG3);
//			}
		} catch (Exception e) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
		return json;
	}

	/**
	 * 验证令牌接口
	 * 
	 * @param token
	 * @return JSONObject
	 */
	@RequestMapping(value = "validateToken")
	@ResponseBody
	public JSONObject validateToken(HttpServletRequest request, HttpServletResponse response) {
		String customerCode = request.getParameter("customerCode");
		String token = request.getParameter("token");
		JSONObject json = new JSONObject();
		String result = ConnCloudUtil.validateToken(token, customerCode);
		if (result == null || result.length() == 0) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG2);
		} else {
			json = JSON.parseObject(result);
			String code = (String) json.get("code");
			if (code.equals("0")) {
				json.put("validateCode", MessageUtils.SUCCESS_CODE);
				json.put("msg", MessageUtils.SUCCESS_MSG1);
			} else {
				json.put("validateCode", MessageUtils.FAIL_CODE);
				json.put("msg", MessageUtils.FAIL_MSG2);
				return json;
			}
		}
		return json;
	}

	private Integer getUserNum(String sPhone, String sPassWord) {
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
		Integer num = userInfoService.getUserNum(sPhone, sPassWord);
		DataSourceContextHolder.clearDbType();
		return num;
	}

	/**
	 * 获取登录所有详细信息
	 * 
	 * @param mobile
	 * @param sPassWord
	 * @return
	 * @throws Exception
	 * @author xy
	 * @Time 2017-08-10
	 */

	@SuppressWarnings("unchecked")
	public JSONObject getUserInfos(String mobile, String sPassWord) throws Exception {
		JSONObject json = new JSONObject();
		try {
			String customerID = "";
			int roleID = getUserRoleID(mobile);
			UserInput userInput = new UserInput();
			userInput.setMobile(mobile);
			userInput.setsPassWord(sPassWord);
			List userInfo = null;
			if (roleID == 1) {
				DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
				userInfo = userInfoService.getUserInfoByInput(userInput);
				json.put("userInfo", userInfo);
			} else {
				DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
				userInfo = userInfoService.getUserInfoByInput1(userInput);
				json.put("userInfo", userInfo);
			}
			List roleInfo = roleInfoService.getRoleInfoByInput(userInput);
			json.put("roleInfo", roleInfo);
			List customerInfo = customerInfoService.getCustomerInfoByInput(userInput);
			json.put("customerInfo", customerInfo);
			List cinemaInfo = cinemaInfoService.getCinemaInfoByInput(userInput);
			DataSourceContextHolder.clearDbType();
			json.put("cinemaInfo", cinemaInfo);
			if (cinemaInfo.size() == 1) {
				Iterator<Object> i = cinemaInfo.iterator();
				while (i.hasNext()) {
					HashMap<String, Object> map = (HashMap<String, Object>) i.next();
					json.put("isTMS", getTheaterIsTms((String) map.get("cinemaCode")));
				}
			} else {
				Map map = (Map) userInfo.get(0);
				customerID = map.get("customerID").toString();
				String ThreaterIDs = getCustomerTheaterIDs(customerID);
				Integer theaterIsTms = getTheaterIsTms(ThreaterIDs);
				json.put("isTMS", theaterIsTms);
			}
			json.put("status", "1");
			json.put("msg", "获取用户信息成功");
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", "2");
			json.put("msg", "获取用户信息失败");
		}
		return json;
	}

	/**
	 * 
	 * @param theaterCode
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @author xy
	 * @Time 2017-08-10
	 */
	public Integer getTheaterIsTms(String theaterCode) throws SQLException, ClassNotFoundException {
		Integer count = 0;
		try {
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
			count = dw_DimInfoService.getCountByTheaterCode(theaterCode);
			DataSourceContextHolder.clearDbType();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (count >= 1) {
			count = 1;
		}
		return count;
	}

	/**
	 * 
	 * @param mobile
	 * @param context
	 * @return
	 * @throws Exception
	 * @author xy
	 * @Time 2017-08-10
	 */
	public int getUserRoleID(String mobile) throws Exception {
		int roleID = 0;
		try {
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
			roleID = userInfoService.getRoleIdByMobile(mobile).getRoleID();
			DataSourceContextHolder.clearDbType();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return roleID;
	}

	/**
	 * 
	 * @param customerID
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 * @author xy
	 * @Time 2017-08-10
	 */
	public String getCustomerTheaterIDs(String customerID) throws SQLException, NamingException {
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
		List list = cinemaInfoService.getCinemaCode(customerID);
		DataSourceContextHolder.clearDbType();
		/*
		 * String sql =
		 * "select cinemaID,cinemaCode,cinemaName from cinema where customerID='"
		 * + customerID + "' and cinemaCode is not null";
		 * System.out.println(sql); String theaterId = ""; try { theaterId =
		 * InterfaceCinemaUtils.getStringsBySql(sql); } catch (Exception e) {
		 * e.printStackTrace(); } System.out.println(theaterId);
		 */

		String Strings = "";
		int j = 0;
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			String cinemaCode = (String) map.get("cinemaCode");
			if (j > 0) {
				Strings = Strings + ",'" + cinemaCode + "'";
			} else {
				Strings = "'" + cinemaCode + "'";
			}
			j++;
		}
		return Strings;
	}

	/**
	 * 更新用户登录信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author xy
	 * @Time 2017-08-23
	 */
	@RequestMapping(value = "updateUserLoginInfo")
	public void updateUserLoginInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long startTime = System.currentTimeMillis();
		HttpServletResponse rs = ToolUtils.getResponseType(response);
		Map<Object, Object> map = RequestToMapUtil.transfer(request);
		String latitude = request.getParameter("latitude");
		String longitude = request.getParameter("longitude");
		// String phoneType = new
		// String(map.get("phoneType").toString().getBytes("iso8859-1"),
		// "UTF-8");
		String phoneType = (String) map.get("phoneType");
		phoneType = java.net.URLDecoder.decode(phoneType, "UTF-8");
		map.remove("phoneType");
		map.put("phoneType1", phoneType);
		System.out.println("phoneType=" + phoneType);
		String jsonp = (String) map.get("callback");
		String address = "";
		com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
		if (!latitude.equals("") && !longitude.equals("")) {
			address = BaiduMap.getAddressByLg(latitude, longitude);
		} else {
			address = "未知地址";
		}
		map.put("address", address);
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
		Integer count = loginInfoService.getLoginCountInfo(map);
		DataSourceContextHolder.clearDbType();
		// System.out.println(list.toString());
		// if (list != null && list.size() > 0) {
		// String map1 = String.valueOf(list.get(0));
		// JSONObject json1 = JSON.parseObject(map1);
		// Integer count = 0;
		// try {
		// count = Integer.parseInt(json1.getString("count"));
		// } catch (NullPointerException e) {
		// count = 0;
		// }
		count++;
		map.put("count", count);
		map.put("lastLoginTime", getCurrentDate());
		System.out.println(map);
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
		loginInfoService.updateUserLoginInfo(map);
		DataSourceContextHolder.clearDbType();
		try {
			ToolUtils.setSuccess(json, jsonp, rs);
		} catch (Exception e) {
			ToolUtils.setUnsuccess(json, jsonp, rs);
		}
		// } else {
		// ToolUtils.setUnData(json, jsonp, rs);
		// }
	}

	public static String getCurrentDate() {
		SimpleDateFormat sdft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdft.format(new Date());
		return date;
	}

	/**
	 * 更新用户推送信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author xy
	 * @Time 2017-08-23
	 */
	@RequestMapping(value = "SignInJpushID")
	public void SignInJpushID(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long startTime = System.currentTimeMillis();
		HttpServletResponse rs = ToolUtils.getResponseType(response);
		Map<Object, Object> map = RequestToMapUtil.transfer(request);
		String registrationId = request.getParameter("registrationId");
		String name = request.getParameter("name");
		String jsonp = (String) map.get("callback");
		com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
		System.out.println(map);
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
		loginInfoService.updateRegistrationId(map);
		DataSourceContextHolder.clearDbType();
		try {
			ToolUtils.setSuccess(json, jsonp, rs);
		} catch (Exception e) {
			ToolUtils.setUnsuccess(json, jsonp, rs);
		}
	}

	/**
	 * 无账号登录方式
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author xy
	 * @Time 2017-08-23
	 */

	@RequestMapping(value = "UnAccountLogin")
	public void UnAccountLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long startTime = System.currentTimeMillis();
		Map<Object, Object> map = RequestToMapUtil.transfer(request);
		String sPhone = request.getParameter("sPhone");
		map.put("mobile", sPhone);
		com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
		try {
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
			List<Map> list = loginInfoService.UnAccountLogin(map);
			DataSourceContextHolder.clearDbType();
			int status = (int) list.get(0).get("I_status");
			int userID = (int) list.get(0).get("I_userID");
			String password = (String) list.get(0).get("I_password");
			if (status == 1 || status == 2) {
				json.put("list", getUserInfos(sPhone, password));
				json.put("validateCode", MessageUtils.SUCCESS_CODE);
				json.put("msg", MessageUtils.SUCCESS_SENDMESSAGE);
			} else {
				if (status == 0) {
					json.put("validateCode", MessageUtils.FAIL_CODE);
					json.put("msg", "系统错误");
				} else if (status == 3) {
					json.put("validateCode", MessageUtils.FAIL_CODE);
					json.put("msg", "新增用户信息失败");
				} else if (status == 4) {
					json.put("validateCode", MessageUtils.FAIL_CODE);
					json.put("msg", "绑定角色信息失败");
				}
			}
		} catch (Exception e) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
	}

	/**
	 * 获取登录短信验证码
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author xy
	 * @Time 2017-08-23
	 */

	@RequestMapping(value = "getLoginPhoneCode")
	@ResponseBody
	public JSONObject getLoginPhoneCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<Object, Object> map = RequestToMapUtil.transfer(request);
		JSONObject json = new JSONObject();
		String phone = (String) map.get("phone");
		try {
			String phoneCode = InviteUserController.getPhoneCode(phone, "尊敬的用户，您正在绑定智慧影院帐号，验证码为：");
			System.out.println("phoneCode=" + phoneCode);
			if (phoneCode != null && !phoneCode.isEmpty()) {
				json.put("phoneCode", phoneCode);
				json.put("msg", MessageUtils.SUCCESS_SENDMESSAGE);
				json.put("validateCode", MessageUtils.SUCCESS_CODE);
			} else {
				json.put("msg", MessageUtils.FAIL_SENDMESSAGE);
				json.put("validateCode", MessageUtils.ACTION_FAILCODE);
			}
		} catch (Exception e) {
			json.put("msg", MessageUtils.FAIL_MSG);
			json.put("validateCode", MessageUtils.FAIL_CODE);
		}
		return json;
	}

	/**
	 * 更新用户微信信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author xy
	 * @Time 2017-08-23
	 */
	@RequestMapping(value = "UpdateUserWeixinInfo")
	public void UpdateUserWeixinInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long startTime = System.currentTimeMillis();
		Map<Object, Object> map = RequestToMapUtil.transfer(request);
		com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
		loginInfoService.updateUserLoginInfo(map);
		DataSourceContextHolder.clearDbType();
		try {
			json.put("msg", MessageUtils.SUCCESS_SENDMESSAGE);
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
		} catch (Exception e) {
			json.put("msg", MessageUtils.FAIL_SENDMESSAGE);
			json.put("validateCode", MessageUtils.ACTION_FAILCODE);
		}

	}

}
