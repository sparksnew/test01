package com.oristartech.cinema.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oristartech.cinema.mapper.LoginMapper;
import com.oristartech.cinema.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService{

	@Autowired
	private LoginMapper mapper;
	
	@Override
	public int loginAction(String username, String userpass) {
		return mapper.loginAction(username, userpass);
	}

}
