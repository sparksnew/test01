package com.oristartech.cinema.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.oristartech.cinema.service.TestService;
import com.oristartech.cinema.service.UserService;
import com.oristartech.cinema.utils.DataSourceContextHolder;
import com.oristartech.cinema.utils.RequestToMapUtil;
import com.oristartech.cinema.utils.ToolUtils;

@Controller
@RequestMapping(value = "/test")
public class TestController {
	@Autowired
	private TestService testService;

	@RequestMapping(value = "/updateTest")
	public void updateAccSet(HttpServletRequest request, HttpServletResponse response) {
		HttpServletResponse rs = ToolUtils.getResponseType(response);
		Map<Object, Object> map = RequestToMapUtil.transfer(request);
		String jsonp = (String) map.get("callback");
		JSONObject json = new JSONObject();
		try {
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
			testService.updateTest(map);
			DataSourceContextHolder.clearDbType();
			ToolUtils.setSuccess(json, jsonp, rs);
		} catch (Exception e) {
			ToolUtils.setUnsuccess(json, jsonp, rs);
		}
	}
}
