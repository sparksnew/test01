package com.oristartech.cinema.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface UserMapper {
	// 获取用户权限信息
	public List getUserInfo(Map<Object, Object> map);

	// 获取投资人的个人信息
	public List getInvInfo(Map<Object, Object> map);

	// 获取雇员的个人信息
	public List getEmpInfo(Map<Object, Object> map);

	// 更新用户邮箱
	public List updateUserEmail(Map<Object, Object> map);

	// 更新用户密码
	public List updateUserPass(Map<Object, Object> map);

	// 更新用户手机号
	public List updateUserPhone(Map<Object, Object> map);

	// 更新报障进度状态
	public void updateOpeSet(Map<Object, Object> map);

	// 更新投诉建议状态
	public void updateAdvSet(Map<Object, Object> map);

	// 更新账户状态
	public void updateAccSet(Map<Object, Object> map);

	// 获取用户登录次数
	public Integer getLoginCountInfo(Map<Object, Object> map);

	// 更新用户登录信息
	public void updateUserLoginInfo(Map<Object, Object> map);

	// 更新用户微信信息
	public void UpdateUserWeixinInfo(Map<Object, Object> map);

	// 更新用户的推送ID
	public void updateRegistrationId(Map<Object, Object> map);

	// 获取个人消息列表
	public List getMsgList(Map<Object, Object> map);

	// 点击消息事件
	public List<Map> getMsgDetail(Map<Object, Object> map);

	// 禁用用户
	public List forbiddenUser(Map<Object, Object> map);

	// 删除用户信息
	public void deleteUserMsg(String[] arr);

	// 获取无账号登录信息
	public List UnAccountLogin(Map<Object, Object> map);

	// 获取微信是否已经绑定
	public List getWeixinCountInfo(Map<Object, Object> map);

	// 删除用户信息
	public void updateMsgStatus(String[] arr);

	// 获取用户信息
	public List queryUserInfo(@Param("loginName") String loginName, @Param("customerCode") String customerCode);

	// 更新用户最后登录的影院code
	public Integer updateUserLoginLog(@Param("cinemaCode") String cinemaCode, @Param("userID") String userID);
}
