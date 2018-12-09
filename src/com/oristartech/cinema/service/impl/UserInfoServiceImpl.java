package com.oristartech.cinema.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.oristartech.cinema.mapper.UserInfoMapper;
import com.oristartech.cinema.mapper.UserRolesMapper;
import com.oristartech.cinema.pojo.UserInput;
import com.oristartech.cinema.pojo.UserRoles;
import com.oristartech.cinema.service.UserInfoService;
import com.oristartech.cinema.utils.ConnCloudUtil;
import com.oristartech.cinema.utils.DateUtil;
import com.oristartech.cinema.utils.MessageUtils;

@Service
public class UserInfoServiceImpl implements UserInfoService {

	@Autowired
	private UserRolesMapper userRoleMapper;

	@Autowired
	private UserInfoMapper userInfoMapper;

	@Override
	public UserRoles getRoleIdByMobile(String mobile) {
		UserRoles userroles = userRoleMapper.queryRoleByMobile(mobile);
		return userroles;
	}

	@Override
	public List getUserInfoByInput(UserInput userInput) {
		List userInfo = userInfoMapper.queryUserInfoByInput(userInput);
		return userInfo;

	}

	@Override
	public List getUserInfoByInput1(UserInput userInput) {
		List userInfo = userInfoMapper.queryUserInfoByInput1(userInput);
		return userInfo;
	}

	@Override
	public Integer getUserNum(String sPhone, String sPassWord) {
		Integer num = userInfoMapper.queryUserNum(sPhone, sPassWord);
		return num;
	}

	@Override
	public List getUserBase(String userID) {
		List funcList = userInfoMapper.getUserBase(userID);
		return funcList;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED, readOnly = false, timeout = 5)
	@Override
	public JSONObject login(String loginName, String password, int isPhone, String customerCode) {
		String falseReason = "";
		int isTrue = 0;
		String userID = "";
		String userName = "";
		JSONObject resultJson1 = null;
		String loginTime = DateUtil.getCurrentDate();
		if (isPhone == 1) {// 不是手机号
			Integer count = userInfoMapper.validateUserName(loginName, customerCode);
			// 登录云平台获取账号,提示信息开始
			String result = ConnCloudUtil.login(loginName, password, customerCode);
			if (result == null || result == "") {
				resultJson1 = new JSONObject();
				resultJson1.put("validateCode", MessageUtils.FAIL_CODE);
				resultJson1.put("falseReason", MessageUtils.FAIL_GETCONNINFO);
				return resultJson1;
			}
			JSONObject resultJson = JSON.parseObject(result);
			resultJson1 = ConnCloudUtil.loginDate(resultJson, loginName);
			String code = resultJson.getString("code");
			String account = resultJson.getString("account");
			String accountName = resultJson.getString("accountName");
			String accountLogin= resultJson.getString("accountLogin");
			String message = resultJson.getString("message");
			// System.out.println("count=" + count + ";code=" + code +
			// ";account=" + account + ";accountName="
			// + accountName + ";message=" + message);
			// 登录云平台获取账号,提示信息结束
			if ("0".equals(code) || code == "0") {
				if (count == 0) {
					// System.out.println("000000000000");
					falseReason = MessageUtils.LOGIN_FAIL_ACCOUNTBIND;
					isTrue = MessageUtils.YES_CODE;
					userID = account;
					userName = accountName;
					resultJson1.put("falseReason", MessageUtils.LOGIN_FAIL_ACCOUNTBIND);
					resultJson1.put("validateCode", MessageUtils.ACTION_FAILCODE);
					userInfoMapper.bindMobile(userID, accountLogin, password, "", customerCode);
				} else {
					// System.out.println("1111111111111");
					String mobile = userInfoMapper.validateUserMobile(loginName, customerCode);
					if("".equals(mobile)||mobile == null){
						resultJson1.put("falseReason", MessageUtils.LOGIN_FAIL_PHONEBIND);
						resultJson1.put("validateCode", MessageUtils.ACTION_FAILCODE);
						return resultJson1;
					}
					isTrue = MessageUtils.YES_CODE;
					userID = account;
					userName = accountName;
					resultJson1.put("falseReason", MessageUtils.LOGIN_SUCCESSMSG);
					resultJson1.put("validateCode", MessageUtils.SUCCESS_CODE);
				}
			} else {
				// System.out.println("222222222222");
				isTrue = MessageUtils.NO_CODE;
				userID = null;
				userName = null;
				resultJson1.put("falseReason", message);
				resultJson1.put("validateCode", MessageUtils.FAIL_CODE);
			}
		} else {
			Map<String, String> map = userInfoMapper.queryMobile(loginName);
			System.out.println("map=" + map);
			if (map == null) {
				isTrue = MessageUtils.NO_CODE;
				userID = null;
				userName = null;
				resultJson1 = new JSONObject();
				resultJson1.put("falseReason", MessageUtils.LOGIN_FAIL_PHONEBIND);
				resultJson1.put("validateCode", MessageUtils.FAIL_CODE);
			} else {
				String username = map.get("userName");
				String passWord = password;
				// 登录云平台获取账号,提示信息开始
				String result = ConnCloudUtil.login(username, passWord, customerCode);
				if (result == null || result == "") {
					resultJson1 = new JSONObject();
					resultJson1.put("validateCode", MessageUtils.FAIL_CODE);
					resultJson1.put("falseReason", MessageUtils.FAIL_GETCONNINFO);
					return resultJson1;
				}
				JSONObject resultJson = JSON.parseObject(result);
				resultJson1 = ConnCloudUtil.loginDate(resultJson, loginName);
				String code = resultJson.getString("code");
				String account = resultJson.getString("account");
				String accountName = resultJson.getString("accountName");
				String message = resultJson.getString("message");
				// System.out.println(
				// "code=" + code + ";account=" + account + ";accountName=" +
				// accountName + ";message=" + message);
				// 登录云平台获取账号,提示信息结束
				if (code == "1") {
					falseReason = message;
					isTrue = MessageUtils.NO_CODE;
					userID = account;
					userName = accountName;
					resultJson1.put("falseReason", message);
					resultJson1.put("validateCode", MessageUtils.FAIL_CODE);
				} else {
					isTrue = MessageUtils.YES_CODE;
					userID = account;
					userName = accountName;
					resultJson1.put("falseReason", MessageUtils.LOGIN_SUCCESSMSG);
					resultJson1.put("validateCode", MessageUtils.SUCCESS_CODE);
				}
			}
		}
		userInfoMapper.insertLoginLog(loginTime, null, null, null, isTrue, falseReason, userID, userName);
		return resultJson1;
	}

	@Override
	public JSONObject bindMobile(String userID, String userName, String passWord, String mobile, String customerCode) {
		JSONObject json = new JSONObject();
		int count = userInfoMapper.updateMobile(userID, mobile,customerCode);
		if (count == 0) {
			json.put("msg", MessageUtils.ACTION_FAILCODE);
			json.put("validateCode", MessageUtils.ACTION_FAILCODE);
		} else {
			json.put("msg", MessageUtils.SUCCESS_BIND_MSG);
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
		}
		return json;
	}

	@Override
	public Integer queryMobileIsExist(String mobile) {
		int count = userInfoMapper.queryMobileIsExist(mobile);
		if(count == 0){
			return 200;
		}else{
			return 500;
		}
	}
}
