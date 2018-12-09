package com.oristartech.cinema.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oristartech.cinema.mapper.RoleInfoMapper;
import com.oristartech.cinema.pojo.UserInput;
import com.oristartech.cinema.service.RoleInfoService;
@Service
public class RoleInfoServiceImpl implements RoleInfoService {
	@Autowired
	private RoleInfoMapper roleInfoMapper;
	@Override
	public List getRoleInfoByInput(UserInput userInput) {
		
		List roleInfo = roleInfoMapper.queryRoleInfoByInput(userInput);
		return roleInfo;
	}

}
