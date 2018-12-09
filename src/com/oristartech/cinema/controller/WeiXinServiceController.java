package com.oristartech.cinema.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.oristartech.cinema.weixin.WxMpServiceInstance;

@RequestMapping("/exterInt/test/")
@Controller
public class WeiXinServiceController extends HttpServlet {
	private static final long serialVersionUID = -3085539764084393258L;

	@RequestMapping(value = "weixinService")
	@ResponseBody
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		WxMpServiceInstance.getInstance().doResponse(request, response);
		return;
	}
}