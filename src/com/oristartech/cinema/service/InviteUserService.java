package com.oristartech.cinema.service;

import java.util.List;
import java.util.Map;

public interface InviteUserService {
	
	public List getFcodeInfo(Map<Object, Object> map);
	
	public int checkPhoneExit(Map<Object, Object> map);
	
	public int submitAccount(Map<Object, Object> map);
}
