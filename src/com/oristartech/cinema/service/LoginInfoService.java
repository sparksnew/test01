package com.oristartech.cinema.service;

import java.util.List;
import java.util.Map;

public interface LoginInfoService {
	public Integer getLoginCountInfo(Map<Object, Object> map);

	public void updateUserLoginInfo(Map<Object, Object> map);

	public void UpdateUserWeixinInfo(Map<Object, Object> map);

	public void updateRegistrationId(Map<Object, Object> map);

	public List UnAccountLogin(Map<Object, Object> map);

	public List getWeixinCountInfo(Map<Object, Object> map);
}
