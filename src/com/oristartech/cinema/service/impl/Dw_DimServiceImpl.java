package com.oristartech.cinema.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oristartech.cinema.mapper.Dw_DimMapper;
import com.oristartech.cinema.service.Dw_DimInfoService;
@Service
public class Dw_DimServiceImpl implements Dw_DimInfoService {
	@Autowired
	private Dw_DimMapper dw_DimMapper;
	@Override
	public Integer getCountByTheaterCode(String theaterCode) {
		Integer count = dw_DimMapper.getCountByTheaterCode(theaterCode);
		return count;
	}

}
