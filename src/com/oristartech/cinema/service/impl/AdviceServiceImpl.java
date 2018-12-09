package com.oristartech.cinema.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.oristartech.cinema.mapper.AdviceMapper;
import com.oristartech.cinema.service.AdviceService;

@Service
public class AdviceServiceImpl implements AdviceService {

	@Autowired
	private AdviceMapper adviceDao;

	@Override
	public List<Map> getInvestorCinemaID(int userID) {
		return adviceDao.getInvestorCinemaID(userID);
	}

	@Override
	public String getEmpCinemaID(String userID) {
		return adviceDao.getEmpCinemaID(userID);
	}

	@Override
	public int insertUserBug(int indentID, String userID) {
		return adviceDao.insertUserBug(indentID, userID);
	}

	@Override
	public List<String> getUserAdvList(String userID) {
		return adviceDao.getUserAdvList(userID);
	}
}
