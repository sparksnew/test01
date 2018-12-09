package com.oristartech.cinema.service;

import java.util.List;

import com.oristartech.cinema.pojo.CinemaInfo;
import com.oristartech.cinema.pojo.UserInput;

public interface CinemaInfoService {

	List getCinemaInfoByInput(UserInput userInput);


	List getCinemaCode(String customerID);


	List queryPlanSeat(String theaterCode, String startTime, String endTime, String hallID);

}
