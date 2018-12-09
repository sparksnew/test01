package com.oristartech.cinema.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.TimeUtil;
import com.oristartech.cinema.service.UserService;
import com.oristartech.cinema.utils.DataSourceContextHolder;
import com.oristartech.cinema.utils.DateUtil;
import com.oristartech.cinema.utils.MessageUtils;
import com.oristartech.cinema.utils.RequestToMapUtil;
import com.oristartech.cinema.utils.ToolUtils;

@Controller
@RequestMapping(value = "/user")
public class UserController {
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/getUserInfo")
	@ResponseBody
	public JSONObject getUserInfo(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		Map<Object, Object> map = RequestToMapUtil.transfer(request);
		List list = null;
		try {
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
			list = userService.getUserInfo(map);
			if(list == null || list.isEmpty()){
				json.put("validateCode", MessageUtils.FAIL_CODE);
				json.put("msg", MessageUtils.FAIL_MSG);
				return json;
			}
			DataSourceContextHolder.clearDbType();
			json.put("list", list);
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_MSG);
		} catch (Exception e) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping(value = "/getInvInfo")
	public void getInvInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JSONObject json = new JSONObject();
		Map<Object, Object> map = RequestToMapUtil.transfer(request);
		List list = null;
		try {
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
			list = userService.getInvInfo(map);
			DataSourceContextHolder.clearDbType();
			json.put("list", list);
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_SENDMESSAGE);
		} catch (Exception e) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
	}

	@RequestMapping(value = "/getEmpInfo")
	public void getEmpInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JSONObject json = new JSONObject();
		Map<Object, Object> map = RequestToMapUtil.transfer(request);
		List list = null;
		try {
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
			list = userService.getEmpInfo(map);
			DataSourceContextHolder.clearDbType();
			json.put("list", list);
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_SENDMESSAGE);
		} catch (Exception e) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
	}

	@RequestMapping(value = "/updateUserEmail")
	public void updateUserEmail(HttpServletRequest request, HttpServletResponse response) {
		Map<Object, Object> map = RequestToMapUtil.transfer(request);
		int userID = Integer.parseInt(map.get("userID").toString());
		map.remove("userID");
		map.put("userID", userID);
		JSONObject json = new JSONObject();
		List list = null;
		try {
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
			list = userService.updateUserEmail(map);
			DataSourceContextHolder.clearDbType();
			json.put("list", list);
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_SENDMESSAGE);
		} catch (Exception e) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
	}

	@RequestMapping(value = "/updateUserPass")
	public void updateUserPass(HttpServletRequest request, HttpServletResponse response) {
		Map<Object, Object> map = RequestToMapUtil.transfer(request);
		String date = DateUtil.getCurrentDate();
		System.out.println("date=" + date);
		map.put("date", date);
		List list = null;
		JSONObject json = new JSONObject();
		try {
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
			list = userService.updateUserPass(map);
			DataSourceContextHolder.clearDbType();
			json.put("list", list);
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_SENDMESSAGE);
		} catch (Exception e) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
	}

	@RequestMapping(value = "/updateUserPhone")
	public void updateUserPhone(HttpServletRequest request, HttpServletResponse response) {
		Map<Object, Object> map = RequestToMapUtil.transfer(request);
		String date = DateUtil.getCurrentDate();
		System.out.println("date=" + date);
		map.put("date", date);
		List list = null;
		JSONObject json = new JSONObject();
		try {
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
			list = userService.updateUserPhone(map);
			DataSourceContextHolder.clearDbType();
			json.put("list", list);
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_SENDMESSAGE);
		} catch (Exception e) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
	}

	@RequestMapping(value = "/updateOpeSet")
	public void updateOpeSet(HttpServletRequest request, HttpServletResponse response) {
		Map<Object, Object> map = RequestToMapUtil.transfer(request);
		JSONObject json = new JSONObject();
		try {
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
			userService.updateOpeSet(map);
			DataSourceContextHolder.clearDbType();
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_SENDMESSAGE);
		} catch (Exception e) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
	}

	@RequestMapping(value = "/updateAdvSet")
	public void updateAdvSet(HttpServletRequest request, HttpServletResponse response) {
		Map<Object, Object> map = RequestToMapUtil.transfer(request);
		JSONObject json = new JSONObject();
		try {
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
			userService.updateAdvSet(map);
			DataSourceContextHolder.clearDbType();
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_SENDMESSAGE);
		} catch (Exception e) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
	}

	@RequestMapping(value = "/updateAccSet")
	public void updateAccSet(HttpServletRequest request, HttpServletResponse response) {
		Map<Object, Object> map = RequestToMapUtil.transfer(request);
		JSONObject json = new JSONObject();
		try {
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
			userService.updateAccSet(map);
			DataSourceContextHolder.clearDbType();
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_SENDMESSAGE);
		} catch (Exception e) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
	}

	@RequestMapping(value = "/getMsgList")
	@ResponseBody
	public JSONObject getMsgList(HttpServletRequest request, HttpServletResponse response) {
		Map<Object, Object> map = RequestToMapUtil.transfer(request);
		List list = null;
		JSONObject json = new JSONObject();
		try {
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
			list = userService.getMsgList(map);
			DataSourceContextHolder.clearDbType();
			json.put("list", list);
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_MSG);
		} catch (Exception e) {
			json.put("list", list);
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping(value = "/getMsgDetail")
	@ResponseBody
	public JSONObject getMsgDetail(HttpServletRequest request, HttpServletResponse response) {
		Map<Object, Object> map = RequestToMapUtil.transfer(request);
		List<Map> list = null;
		JSONObject json = new JSONObject();
		try {
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
			list = userService.getMsgDetail(map);
			DataSourceContextHolder.clearDbType();
			if (!list.get(0).containsKey("I_status")) {
				list.get(0).put("I_status", 5);
			}
			json.put("list", list);
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_MSG);
		} catch (Exception e) {
			json.put("list", list);
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
		return json;
	}

	@RequestMapping(value = "/forbiddenUser")
	public void forbiddenUser(HttpServletRequest request, HttpServletResponse response) {
		Map<Object, Object> map = RequestToMapUtil.transfer(request);
		List list = null;
		JSONObject json = new JSONObject();
		try {
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
			userService.forbiddenUser(map);
			DataSourceContextHolder.clearDbType();
			json.put("list", list);
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_SENDMESSAGE);
		} catch (Exception e) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
	}

	@RequestMapping(value = "/deleteUserMsg")
	public void deleteUserMsg(HttpServletRequest request, HttpServletResponse response) {
		Map<Object, Object> map = RequestToMapUtil.transfer(request);
		String id_array = (String) map.get("id_array");
		String[] arr = id_array.split(",");
		JSONObject json = new JSONObject();
		try {
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
			userService.deleteUserMsg(arr);
			DataSourceContextHolder.clearDbType();
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_SENDMESSAGE);
		} catch (Exception e) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
	}

	@RequestMapping(value = "/updateMsgStatus")
	public void updateMsgStatus(HttpServletRequest request, HttpServletResponse response) {
		Map<Object, Object> map = RequestToMapUtil.transfer(request);
		String id_array = (String) map.get("id_array");
		String[] arr = id_array.split(",");
		JSONObject json = new JSONObject();
		try {
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
			userService.updateMsgStatus(arr);
			DataSourceContextHolder.clearDbType();
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_SENDMESSAGE);
		} catch (Exception e) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
	}
}
