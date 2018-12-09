package com.oristartech.cinema.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oristartech.cinema.pojo.UserInput;

public interface CinemaInfoMapper {


	List queryCinemaInfoByInput(UserInput userInput);

	@SuppressWarnings("rawtypes")
	List queryCinemaCode(String customerID);

	List queryPlanSeat(@Param("theaterCode")String theaterCode, @Param("startTime")String startTime, @Param("endTime")String endTime, @Param("hallID")String hallID);

	

}
