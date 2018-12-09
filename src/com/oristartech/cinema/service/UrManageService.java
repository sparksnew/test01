package com.oristartech.cinema.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface UrManageService {

	public List getInvUser(Map<Object, Object> map);

	public List getInvCinema(Map<Object, Object> map);

	public List getInvRole(Map<Object, Object> map);

	public List getEmpUser(Map<Object, Object> map);

	public List getEmpCinema(Map<Object, Object> map);

	public List getEmpRole(Map<Object, Object> map);
	
	public void forbiddenUser(Map<Object, Object> map);
	
	public List<Map> createFCode(Map<Object, Object> map);
	
//	public JSONObject getUserInfo(String userID);
//	
//	public JSONObject getUserList(String userID);
}
