package com.oristartech.cinema.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oristartech.cinema.mapper.UserMapper;
import com.oristartech.cinema.service.LoginInfoService;

@Service
public class LoginInfoServiceImpl implements LoginInfoService {
	@Autowired
	private UserMapper userDao;

	@Override
	public Integer getLoginCountInfo(Map<Object, Object> map) {
		return userDao.getLoginCountInfo(map);
	}

	@Override
	public void updateUserLoginInfo(Map<Object, Object> map) {
		userDao.updateUserLoginInfo(map);
	}

	@Override
	public void UpdateUserWeixinInfo(Map<Object, Object> map) {
		userDao.UpdateUserWeixinInfo(map);
	}

	@Override
	public void updateRegistrationId(Map<Object, Object> map) {
		userDao.updateRegistrationId(map);
	}

	@Override
	public List UnAccountLogin(Map<Object, Object> map) {
		return userDao.UnAccountLogin(map);
	}

	@Override
	public List getWeixinCountInfo(Map<Object, Object> map) {
		return userDao.getWeixinCountInfo(map);
	}
}
