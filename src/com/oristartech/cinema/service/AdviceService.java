package com.oristartech.cinema.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface AdviceService {
    
	public List<Map> getInvestorCinemaID(int userID);
	
	public String getEmpCinemaID(String cinemaCode);
	
	public int insertUserBug(int indentID, String userID);
	
	public List<String> getUserAdvList(String userID);
}
