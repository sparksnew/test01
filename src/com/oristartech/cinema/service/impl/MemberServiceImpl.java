package com.oristartech.cinema.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oristartech.cinema.mapper.MemberMapper;
import com.oristartech.cinema.service.MemberService;

@Service
public class MemberServiceImpl implements MemberService{

	@Autowired
	private MemberMapper mapper;
	@Override
	public List<Map<String, Object>> queryMemberInfo(Map<Object, Object> map) {
		return mapper.queryMemberInfo(map);
	}

}
