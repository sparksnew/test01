package com.oristartech.cinema.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserService {
	public List getUserInfo(Map<Object,Object> map);
	
    public List getInvInfo(Map<Object,Object> map);
    
    public List getEmpInfo(Map<Object,Object> map);
    
    public List updateUserEmail(Map<Object,Object> map);
    
    public List updateUserPass(Map<Object, Object> map);
    
    public List updateUserPhone(Map<Object, Object> map);
    
    public void updateOpeSet(Map<Object, Object> map);
    
    public void updateAdvSet(Map<Object, Object> map);
    
    public void updateAccSet(Map<Object, Object> map);
    
    public List getMsgList(Map<Object, Object> map);
    
    public List<Map> getMsgDetail(Map<Object, Object> map);
    
    public List forbiddenUser(Map<Object, Object> map);
    
    public void deleteUserMsg(String[] arr);
    
    public void updateMsgStatus(String[] arr);
    
    public List queryUserInfo(String loginName, String customerCode);
    
    public int updateUserLoginLog(String cinemaCode,String userID);
}
