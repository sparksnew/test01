package com.oristartech.cinema.controller;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oristartech.cinema.service.UrManageService;
import com.oristartech.cinema.utils.ConnCloudUtil;
import com.oristartech.cinema.utils.DataSourceContextHolder;
import com.oristartech.cinema.utils.DateUtil;
import com.oristartech.cinema.utils.MessageUtils;
import com.oristartech.cinema.utils.RandomUtil;
import com.oristartech.cinema.utils.RequestToMapUtil;
import com.oristartech.cinema.utils.ToolUtils;

@Controller
@RequestMapping(value = "/urManage")
public class UserManageController {
	@Autowired
	private UrManageService urService;

	@RequestMapping(value = "/getOrgList")
	@ResponseBody
	public JSONObject getOrgList(HttpServletRequest request, HttpServletResponse response) {
		String customerCode = request.getParameter("customerCode");
		String code = request.getParameter("code");
		JSONObject json = new JSONObject();
		JSONObject userJson = new JSONObject();
		// try{
		String result = ConnCloudUtil.getOrgList(code, customerCode);
		if (result == null || result.length() == 0) {
			json.put("code", MessageUtils.SUCCESS_CODE);
			json.put("code", MessageUtils.SUCCESS_MSG);
			return json;
		}
		userJson = JSONObject.parseObject(result);
		json.put("userList", userJson);
		json.put("code", MessageUtils.SUCCESS_CODE);
		json.put("msg", MessageUtils.SUCCESS_MSG);
		// }catch(Exception e){
		// json.put("code", MessageUtils.FAIL_CODE);
		// json.put("code", MessageUtils.FAIL_MSG);
		// }
		return json;
	}

	@RequestMapping(value = "/getUserList")
	@ResponseBody
	public JSONObject getUserList(HttpServletRequest request, HttpServletResponse response) {
		String cinemaCode = request.getParameter("cinemaCode");
		String customerCode = request.getParameter("customerCode");
		JSONObject json = new JSONObject();
		JSONObject userJson = new JSONObject();
		// try{
		userJson = ConnCloudUtil.getUserList("", customerCode, cinemaCode);
		if (userJson == null || userJson.size() == 0) {
			json.put("code", MessageUtils.SUCCESS_CODE);
			json.put("code", MessageUtils.SUCCESS_MSG);
			return json;
		}
		json.put("userList", userJson);
		json.put("code", MessageUtils.SUCCESS_CODE);
		json.put("msg", MessageUtils.SUCCESS_MSG);
		// }catch(Exception e){
		// json.put("code", MessageUtils.FAIL_CODE);
		// json.put("code", MessageUtils.FAIL_MSG);
		// }
		return json;
	}
	
	@RequestMapping(value = "/getUserListOfCustomer")
	@ResponseBody
	public JSONObject getUserListOfCustomer(HttpServletRequest request, HttpServletResponse response) {
		String cinemaCode = request.getParameter("cinemaCode");
		String customerCode = request.getParameter("customerCode");
		String code = request.getParameter("code");
		JSONObject json = new JSONObject();
		JSONObject userJson = new JSONObject();
		// try{
		userJson = ConnCloudUtil.getUserList("", customerCode, cinemaCode);
		JSONArray jsonArray = (JSONArray)userJson.get("userList");
		String result = ConnCloudUtil.getOrgList(code,customerCode);
		if (result == null || result.length() == 0) {
			json.put("code", MessageUtils.SUCCESS_CODE);
			json.put("code", MessageUtils.SUCCESS_MSG);
			return json;
		}
		JSONObject orgList = JSONObject.parseObject(result);
		JSONArray array = orgList.getJSONArray("userList");
		for (int i = 0; i < array.size(); i++) {
			jsonArray.add(array.get(i));
		}
		json.put("userList", jsonArray);
		json.put("code", MessageUtils.SUCCESS_CODE);
		json.put("msg", MessageUtils.SUCCESS_MSG);
		System.out.println("json="+json);
		// }catch(Exception e){
		// json.put("code", MessageUtils.FAIL_CODE);
		// json.put("code", MessageUtils.FAIL_MSG);
		// }
		return json;
	}

	@RequestMapping(value = "/forbiddenUser")
	public void forbiddenUser(HttpServletRequest request, HttpServletResponse response) {
		Map<Object, Object> map = RequestToMapUtil.transfer(request);
		JSONObject json = new JSONObject();
		try {
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
			urService.forbiddenUser(map);
			DataSourceContextHolder.clearDbType();
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_SENDMESSAGE);
		} catch (Exception e) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
	}

	@RequestMapping(value = "/createFCode")
	public void createFCode(HttpServletRequest request, HttpServletResponse response) {
		Map<Object, Object> map = RequestToMapUtil.transfer(request);
		String fCode = RandomUtil.getNumAndCharacter();
		map.put("fCode", fCode);
		List<Map> list = null;
		String InvalidTime = null;
		try {
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
			list = urService.createFCode(map);
			DataSourceContextHolder.clearDbType();
			String date = (String) list.get(0).get("createDate");
			int timeLong = Integer.parseInt(list.get(0).get("timeLong").toString());
			InvalidTime = DateUtil.later48HoursTime(date, timeLong);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		JSONObject json = new JSONObject();
		json.put("list", list);
		json.put("InvalidTime", InvalidTime);
		try {
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_SENDMESSAGE);
		} catch (Exception e) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
	}
}
