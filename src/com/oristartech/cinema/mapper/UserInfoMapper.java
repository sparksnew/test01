package com.oristartech.cinema.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.oristartech.cinema.pojo.UserInput;

public interface UserInfoMapper {

	List queryUserInfoByInput(UserInput userInput);

	List queryUserInfoByInput1(UserInput userInput);

	Integer queryUserNum(@Param("sPhone") String sPhone, @Param("sPassWord") String sPassWord);

	List getUserBase(String userID);

	public Integer validateUserName(@Param("loginName") String loginName, @Param("customerCode") String customerCode);
	
	public String validateUserMobile(@Param("loginName") String loginName, @Param("customerCode") String customerCode);

	public Map<String, String> queryMobile(@Param("loginName") String loginName);

	public Integer insertLoginLog(@Param("loginTime") String loginTime, @Param("loginAddress") String loginAddress,
			@Param("deviceType") String deviceType, @Param("loginIP") String loginIP, @Param("isTrue") int isTrue,
			@Param("falseReason") String falseReason, @Param("userID") String userID,
			@Param("userName") String userName);
    
	public Integer queryMobileIsExist(@Param("mobile")String mobile);
	
	public Integer bindMobile(@Param("userID") String userID, @Param("userName") String userName,
			@Param("passWord") String passWord, @Param("mobile") String mobile,
			@Param("customerCode") String customerCode);
	
	public Integer updateMobile(@Param("userID") String userID,@Param("mobile") String mobile,
			@Param("customerCode") String customerCode);
}
