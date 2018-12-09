package com.oristartech.cinema.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.oristartech.cinema.service.MemberService;
import com.oristartech.cinema.utils.ApplicationContextUtil;
import com.oristartech.cinema.utils.DataSourceContextHolder;
import com.oristartech.cinema.utils.MessageUtils;
import com.oristartech.cinema.utils.RedisUtil;
import com.oristartech.cinema.utils.RequestToMapUtil;
import com.oristartech.cinema.utils.ToolUtils;


@Controller
@RequestMapping("/member")
public class MemberController {
     
	@Autowired
	private MemberService service;
	
	@RequestMapping(value="/queryMemberInfo",method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public JSONObject queryMemberInfo(HttpServletRequest request,
			HttpServletResponse response){
		JSONObject json = new JSONObject();
//		try{
			Map<Object, Object> map = RequestToMapUtil.transfer(request);
			if(map.size()==0){
				json.put("validateCode", MessageUtils.FAIL_CODE);
				json.put("msg", MessageUtils.FAIL_MSG);
			}
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TICKET);
			List<Map<String, Object>> memberList = null;
			memberList = service.queryMemberInfo(map);
			System.out.println("list="+memberList); 
			DataSourceContextHolder.clearDbType();
			json.put("list", memberList);
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_MSG);
//		} catch (Exception e) {
//			json.put("validateCode", MessageUtils.FAIL_CODE);
//			json.put("msg", MessageUtils.FAIL_MSG);
//		}
		return json;
	}
	
}
