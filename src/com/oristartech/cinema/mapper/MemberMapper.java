package com.oristartech.cinema.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface MemberMapper {
	
     public List<Map<String, Object>> queryMemberInfo(Map<Object, Object> map);
     
}
