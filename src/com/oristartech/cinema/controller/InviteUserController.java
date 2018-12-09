package com.oristartech.cinema.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.oristartech.cinema.service.InviteUserService;
import com.oristartech.cinema.utils.DataSourceContextHolder;
import com.oristartech.cinema.utils.DateUtil;
import com.oristartech.cinema.utils.MessageUtils;
import com.oristartech.cinema.utils.RandomUtil;
import com.oristartech.cinema.utils.RequestToMapUtil;
import com.oristartech.cinema.utils.ToolUtils;

@Controller
@RequestMapping(value = "/invite")
public class InviteUserController {
	@Autowired
	private InviteUserService invService;

	@RequestMapping(value = "/DisFcode")
	public void DisFcode(HttpServletRequest request, HttpServletResponse response) {
		Map<Object, Object> map = RequestToMapUtil.transfer(request);
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
		List list = invService.getFcodeInfo(map);
		DataSourceContextHolder.clearDbType();
		System.out.println("list=" + list);
		JSONObject json = new JSONObject();
		try {
			json.put("list", list);
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_SENDMESSAGE);
		} catch (Exception e) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
	}

	@RequestMapping(value = "/sendMessage")
	@ResponseBody
	public JSONObject sendMessage(HttpServletRequest request, HttpServletResponse response) {
		Map<Object, Object> map = RequestToMapUtil.transfer(request);
		JSONObject json = new JSONObject();
		String phone = (String) map.get("phone");
		try {
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
			int count = invService.checkPhoneExit(map);
			DataSourceContextHolder.clearDbType();
			System.out.println("count=" + count);
			if (count > 0) {
				json.put("I_status", 1);// 代表该手机号已经被注册
			} else {
				String phoneCode = getPhoneCode(phone, "尊敬的用户，您正在申请绑定智慧影院帐号，验证码为：");
				System.out.println("phoneCode=" + phoneCode);
				if (phoneCode != null && !phoneCode.isEmpty()) {
					json.put("phoneCode", phoneCode);
					json.put("I_status", 2);// 代表短信发送成功
				} else {
					json.put("I_status", 3);// 代表短信发送失败
				}
			}
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_SENDMESSAGE);
			System.out.println("messagejson="+json);
		} catch (Exception e) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
		return json;
	}

	// 获取短信验证码
	public static String getPhoneCode(String phone, String content) {
		String url = "http://service.winic.org:8009/sys_port/gateway/index.asp";
		String randomString = RandomUtil.getNumAndCharacter();
		String msg = content + randomString + "。仅本次操作有效，请及时填写";
		OutputStreamWriter out = null;
		BufferedReader bufr = null;
		String line = null;
		String result = "";
		String phoneCode = "";
		try {
			String beep = "id=" + URLEncoder.encode("辰星科技", "GB2312") + "&pwd=chenxing123&to=" + phone + "&content="
					+ URLEncoder.encode(msg, "GB2312") + "&time=" + DateUtil.getCurrentDate();
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			out = new OutputStreamWriter(conn.getOutputStream(), "ISO-8859-1");
			out.write(beep);
			out.flush();
			bufr = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = bufr.readLine()) != null) {
				result += line;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		} catch (IOException e3) {
			e3.printStackTrace();
		} finally {
			if (bufr != null) {
				try {
					bufr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		String[] str = result.split("/");
		if (str[0].equals("000")) {
			phoneCode = randomString;
		}
		return phoneCode;
	}

	@RequestMapping(value = "/submitAccount")
	public void submitAccount(HttpServletRequest request, HttpServletResponse response) {
		HttpServletResponse rs = ToolUtils.getResponseType(response);
		Map map = getHashMap(request);
		JSONObject json = new JSONObject();
		String userName = null;
		// try {
		// userName = new
		// String(map.get("userName").toString().getBytes("iso8859-1"),"UTF-8");
		userName = (String) map.get("userName");
		// } catch (UnsupportedEncodingException e1) {
		// e1.printStackTrace();
		// }
		map.put("userName", userName);
		String jsonp = request.getParameter("callback");
		System.out.println("jsonp=" + jsonp);
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
		int I_status = invService.submitAccount(map);
		DataSourceContextHolder.clearDbType();
		json.put("I_status", I_status);
		try {
			ToolUtils.setSuccess(json, jsonp, rs);
		} catch (Exception e) {
			ToolUtils.setUnsuccess(json, jsonp, rs);
		}
	}

	public static Map getHashMap(HttpServletRequest request) {
		Map map = new HashMap();
		map.put("customerID", Integer.parseInt(request.getParameter("customerID")));
		map.put("roleID", Integer.parseInt(request.getParameter("roleID")));
		if (Integer.parseInt(request.getParameter("roleID")) == 1) {
			map.put("cinemaID", "");
		} else {
			map.put("cinemaID", Integer.parseInt(request.getParameter("cinemaID")));
		}
		map.put("mobile", request.getParameter("mobile"));
		map.put("passWord", request.getParameter("passWord"));
		map.put("userName", request.getParameter("userName"));
		map.put("fCode", request.getParameter("fCode"));
		map.put("invitationID", Integer.parseInt(request.getParameter("invitationID")));
		map.put("uID", Integer.parseInt(request.getParameter("uID")));
		map.put("date", DateUtil.getCurrentDate());
		return map;
	}
}
