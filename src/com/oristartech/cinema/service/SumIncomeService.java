package com.oristartech.cinema.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface SumIncomeService {

	public JSONObject getThirtyDayIncome(String theaterID, String startDate, String endDate);

	public JSONObject getTodayIncome(String theaterID, String today);

	public JSONObject getRealTimePFIncome(String theaterID, String today);

	public JSONObject getRealTimeMPIncome(String theaterID, String today);

	public JSONObject getRealTimeQTIncome(String theaterID, String today);

	public JSONObject getHistoryIncome(String theaterID, String startDate, String endDate);

	public JSONObject getRealTimeTTIncome(String theaterID, String today);

}
