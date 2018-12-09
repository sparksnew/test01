package com.oristartech.cinema.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.oristartech.cinema.mapper.UserManageMapper;
import com.oristartech.cinema.service.UrManageService;
import com.oristartech.cinema.utils.ConnCloudUtil;
@Service
public class UrManageServiceImpl implements UrManageService{
    @Autowired
    private UserManageMapper urDao;
    
	@Override
	public List getInvUser(Map<Object, Object> map) {
		return urDao.getInvUser(map);
	}

	@Override
	public List getInvCinema(Map<Object, Object> map) {
		return urDao.getInvCinema(map);
	}

	@Override
	public List getInvRole(Map<Object, Object> map) {
		return urDao.getInvRole(map);
	}

	@Override
	public List getEmpUser(Map<Object, Object> map) {
		return urDao.getEmpUser(map);
	}

	@Override
	public List getEmpCinema(Map<Object, Object> map) {
		return urDao.getEmpCinema(map);
	}

	@Override
	public List getEmpRole(Map<Object, Object> map) {
		return urDao.getEmpRole(map);
	}

	@Override
	public void forbiddenUser(Map<Object, Object> map) {
		urDao.forbiddenUser(map);		
	}

	@Override
	public List<Map> createFCode(Map<Object, Object> map) {
		return urDao.createFCode(map);
	}

//	@Override
//	public JSONObject getUserInfo(String userID) {
//		return urDao.get;
//	}

//	@Override
//	public JSONObject getUserList(String userID) {
//		return JSON.parseObject(ConnCloudUtil.getUserList(userID, "1"));
//	}

}
