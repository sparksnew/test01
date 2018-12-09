package com.oristartech.cinema.mapper;

import java.util.List;
import java.util.Map;

public interface InviteUserMapper {
    //获取邀请码的信息 
	public List getFcodeInfo(Map<Object, Object> map);
	//查询手机号是否存在 
	public int checkPhoneExit(Map<Object, Object> map);
	//注册手机用户
	public int submitAccount(Map<Object, Object> map);
}
