//package com.oristartech.cinema.controller;
//
//import java.io.IOException;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import me.chanjar.weixin.common.exception.WxErrorException;
//import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
//import me.chanjar.weixin.mp.bean.result.WxMpUser;
//
//import org.codehaus.jackson.JsonGenerationException;
//import org.codehaus.jackson.map.JsonMappingException;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.alibaba.fastjson.JSONObject;
//import com.oristartech.cinema.weixin.WxMpServiceInstance;
//
//@RequestMapping("/weixin/")
//@Controller
//public class WeiXinUserInfoController {
//
//	static WxMpServiceInstance instance = WxMpServiceInstance.getInstance();
//
//	@RequestMapping(value = "weixinUserInfoService")
//	@ResponseBody
//	public static JSONObject service(HttpServletRequest request, HttpServletResponse response)
//			throws JsonGenerationException, JsonMappingException, IOException, WxErrorException {
//		String userCode = request.getParameter("code");
//		System.out.println(userCode);
//		WxMpOAuth2AccessToken oauth2AccessToken = instance.getWxMpService().oauth2getAccessToken(userCode);
//		request.getSession().setAttribute("weixinOauth2AccessToken", oauth2AccessToken);
//		WxMpUser userInfo = instance.getWxMpService().oauth2getUserInfo(oauth2AccessToken, "zh_CN");
//		JSONObject map = new JSONObject();
//		map.put("openid", oauth2AccessToken.getOpenId());
//		map.put("nickname", userInfo.getNickname());
//		map.put("country", userInfo.getCountry());
//		map.put("province", userInfo.getProvince());
//		map.put("city", userInfo.getCity());
//		map.put("headimgurl", userInfo.getHeadImgUrl());
//
//		return map;
//	}
//
//}
