package com.oristartech.cinema.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oristartech.cinema.mapper.InviteUserMapper;
import com.oristartech.cinema.service.InviteUserService;

@Service
public class InviteUserServiceImpl implements InviteUserService{
	@Autowired
    private InviteUserMapper invDao;
	
	@Override
	public List getFcodeInfo(Map<Object, Object> map) {
		return invDao.getFcodeInfo(map);
	}

	@Override
	public int checkPhoneExit(Map<Object, Object> map) {
		return invDao.checkPhoneExit(map);
	}

	@Override
	public int submitAccount(Map<Object, Object> map) {
		return invDao.submitAccount(map);
	}

}
