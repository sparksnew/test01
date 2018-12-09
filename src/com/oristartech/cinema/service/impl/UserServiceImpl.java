package com.oristartech.cinema.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oristartech.cinema.mapper.UserMapper;
import com.oristartech.cinema.service.UserService;
import com.oristartech.cinema.utils.MessageUtils;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userDao;

	@Override
	public List getInvInfo(Map<Object, Object> map) {
		return userDao.getInvInfo(map);
	}

	@Override
	public List getEmpInfo(Map<Object, Object> map) {
		return userDao.getEmpInfo(map);
	}

	@Override
	public List updateUserEmail(Map<Object, Object> map) {
		return userDao.updateUserEmail(map);
	}

	@Override
	public List updateUserPass(Map<Object, Object> map) {
		return userDao.updateUserPass(map);
	}

	@Override
	public List updateUserPhone(Map<Object, Object> map) {
		return userDao.updateUserPhone(map);
	}

	@Override
	public void updateOpeSet(Map<Object, Object> map) {
		userDao.updateOpeSet(map);
	}

	@Override
	public void updateAdvSet(Map<Object, Object> map) {
		userDao.updateAdvSet(map);
	}

	@Override
	public void updateAccSet(Map<Object, Object> map) {
		userDao.updateAccSet(map);
	}

	@Override
	public List getMsgList(Map<Object, Object> map) {
		return userDao.getMsgList(map);
	}

	@Override
	public List getMsgDetail(Map<Object, Object> map) {
		return userDao.getMsgDetail(map);
	}

	@Override
	public List<Map> forbiddenUser(Map<Object, Object> map) {
		return userDao.forbiddenUser(map);
	}

	@Override
	public void deleteUserMsg(String[] arr) {
		userDao.deleteUserMsg(arr);
	}

	@Override
	public List getUserInfo(Map<Object, Object> map) {
		return userDao.getUserInfo(map);
	}

	@Override
	public void updateMsgStatus(String[] arr) {
		userDao.updateMsgStatus(arr);
	}

	@Override
	public List queryUserInfo(String loginName, String customerCode) {
		return userDao.queryUserInfo(loginName, customerCode);
	}

	@Override
	public int updateUserLoginLog(String cinemaCode, String userID) {
		int count = userDao.updateUserLoginLog(cinemaCode, userID);
		if (count == 1) {
			return MessageUtils.SUCCESS_CODE;
		} else {
			return MessageUtils.FAIL_CODE;
		}
	}
}
