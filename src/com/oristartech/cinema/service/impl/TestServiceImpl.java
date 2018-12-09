package com.oristartech.cinema.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oristartech.cinema.mapper.TestMapper;
import com.oristartech.cinema.service.TestService;

@Service
public class TestServiceImpl implements TestService {
	@Autowired
	private TestMapper testDao;

	@Override
	public void updateTest(Map<Object, Object> map) {
		testDao.updateTest(map);
	}
}
