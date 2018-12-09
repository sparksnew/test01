package com.oristartech.cinema.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.oristartech.cinema.mapper.SumIncomeMapper;
import com.oristartech.cinema.service.SumIncomeService;

@Service
public class SumIncomeServiceImpl implements SumIncomeService {

	@Autowired
	private SumIncomeMapper mapper;

	@Override
	public JSONObject getThirtyDayIncome(String theaterID, String startDate, String endDate) {
		JSONObject json = new JSONObject();
		List<Map<String, Object>> ThirtyFilmList = mapper.getThirtyDayFilmIncome(theaterID, startDate, endDate);
		List<Map<String, Object>> ThirtyGoodsList = mapper.getThirtyDayGoodsIncome(theaterID, startDate, endDate);
		List<Map<String, Object>> ThirtyOtherList = mapper.getThirtyDayOtherIncome(theaterID, startDate, endDate);
		json.put("ThirtyFilmList", ThirtyFilmList);
		json.put("ThirtyGoodsList", ThirtyGoodsList);
		json.put("ThirtyOtherList", ThirtyOtherList);
		return json;
	}

	@Override
	public JSONObject getTodayIncome(String theaterID, String today) {
		JSONObject json = new JSONObject();
		List<Map<String, Object>> todayFilmList = mapper.getTodayFilmIncome(theaterID, today);
		List<Map<String, Object>> todayGoodsList = mapper.getTodayGoodsIncome(theaterID, today);
		List<Map<String, Object>> todayOtherList = mapper.getTodayOtherIncome(theaterID, today);
		json.put("todayFilmList", todayFilmList);
		json.put("todayGoodsList", todayGoodsList);
		json.put("todayOtherList", todayOtherList);
		return json;
	}

	@Override
	public JSONObject getRealTimePFIncome(String theaterID, String today) {
		JSONObject json = new JSONObject();
		List<Map<String, Object>> todayFilmList = mapper.getTodayFilmIncome(theaterID, today);
		json.put("todayFilmList", todayFilmList);
		return json;
	}

	@Override
	public JSONObject getRealTimeMPIncome(String theaterID, String today) {
		JSONObject json = new JSONObject();
		List<Map<String, Object>> todayGoodsList = mapper.getTodayGoodsIncome(theaterID, today);
		json.put("todayGoodsList", todayGoodsList);
		return json;
	}

	@Override
	public JSONObject getRealTimeQTIncome(String theaterID, String today) {
		JSONObject json = new JSONObject();
		List<Map<String, Object>> todayOtherList = mapper.getTodayOtherIncome(theaterID, today);
		json.put("todayOtherList", todayOtherList);
		return json;
	}

	@Override
	public JSONObject getHistoryIncome(String theaterID, String startDate, String endDate) {
		JSONObject json = new JSONObject();
		List<Map<String, Object>> HistoryList = mapper.getHistoryIncome(theaterID, startDate, endDate);
		json.put("HistoryList", HistoryList);
		return json;
	}

	@Override
	public JSONObject getRealTimeTTIncome(String theaterID, String today) {
		JSONObject json = new JSONObject();
		List<Map<String, Object>> todayList = mapper.getRealTimeTTIncome(theaterID, today);
		json.put("todayList", todayList);
		return json;
	}
}
