package com.oristartech.cinema.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oristartech.cinema.mapper.CinemaInfoMapper;
import com.oristartech.cinema.pojo.CinemaInfo;
import com.oristartech.cinema.pojo.UserInput;
import com.oristartech.cinema.service.CinemaInfoService;
@Service
public class CinemaInfoServiceImpl implements CinemaInfoService {
	@Autowired
	private CinemaInfoMapper cinemaInfoMapper;
	@Override
	public List getCinemaInfoByInput(UserInput userInput) {
		List cinemaInfo = cinemaInfoMapper.queryCinemaInfoByInput(userInput);
		return cinemaInfo;
	}
	@Override
	public List getCinemaCode(String customerID) {
		List list = cinemaInfoMapper.queryCinemaCode(customerID);
		return list;
	}
	@Override
	public List queryPlanSeat(String theaterCode, String startTime, String endTime, String hallID) {
		List planSeatList = cinemaInfoMapper.queryPlanSeat(theaterCode, startTime,endTime, hallID);
		return planSeatList;
	}

}
