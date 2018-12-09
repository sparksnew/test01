package com.oristartech.cinema.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.oristartech.cinema.pojo.UserInput;
import com.oristartech.cinema.pojo.UserRoles;

public interface UserInfoService {

	UserRoles getRoleIdByMobile(String mobile);

	List getUserInfoByInput(UserInput userInput);

	List getUserInfoByInput1(UserInput userInput);

	Integer getUserNum(String sPhone, String sPassWord);

	List getUserBase(String userID);
	
	public JSONObject login(String loginName,String password,int isPhone,String customerCode);
	
	public Integer queryMobileIsExist(String mobile);
	
	public JSONObject bindMobile(String userID, String userName, String passWord, String mobile, String customerCode);

}
