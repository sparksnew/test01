package com.oristartech.cinema.mapper;

import java.util.List;
import java.util.Map;

public interface UserManageMapper {
	// 获取投资人雇员列表信息
	public List getInvUser(Map<Object, Object> map);

	// 获取投资人下属影院列表信息
	public List getInvCinema(Map<Object, Object> map);

	// 获取投资人下属影院的角色列表信息
	public List getInvRole(Map<Object, Object> map);

	// 获取影院经理下属雇员列表信息 
	public List getEmpUser(Map<Object, Object> map);

	// 获取影院经理下属影院列表信息
	public List getEmpCinema(Map<Object, Object> map);

	// 获取投资人下属影院的角色列表信息
	public List getEmpRole(Map<Object, Object> map);
	
	//禁用用户
	public void forbiddenUser(Map<Object, Object> map);
	
	//获取邀请码
	public List<Map> createFCode(Map<Object, Object> map);
}
