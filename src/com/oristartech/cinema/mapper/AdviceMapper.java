package com.oristartech.cinema.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface AdviceMapper {
    //获取投资人的所有影院信息
	public List<Map> getInvestorCinemaID(@Param("userID")int userID);
	//获取雇员的所有影院信息
	public String getEmpCinemaID(@Param("userID") String userID);
	//插入userBug关联信息
	public int insertUserBug(@Param("indentID")int indentID,@Param("userID")String userID);
	//获取用户所有的投诉建议单号 
	public List<String> getUserAdvList(@Param("userID")String userID);
}
