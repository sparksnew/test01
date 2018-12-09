package com.oristartech.cinema.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oristartech.cinema.mapper.AppDeviceMapper;
import com.oristartech.cinema.service.CinemaDeviceInfoService;

@Service
public class CinemaDeviceInfoServiceImpl implements CinemaDeviceInfoService {
	@Autowired
	private AppDeviceMapper appDeviceMapper;

	@Override
	public List getDeviceListByCustomerID(String theaterId) {
		List list = appDeviceMapper.queryDeviceListByCustomerID(theaterId);
		return list;
	}

	@Override
	public List getTroubleField(String theaterId) {
		List list = appDeviceMapper.queryTroubleField(theaterId);
		return list;
	}

	@Override
	public List getEquipment(String theaterId) {
		List list = appDeviceMapper.queryEquipment(theaterId);
		return list;
	}

	@Override
	public List getEquipment1(String theaterId, String positionID) {
		List list = appDeviceMapper.queryEquipment1(theaterId, positionID);
		return list;
	}

	@Override
	public List getEquipment2(String theaterId) {
		List list = appDeviceMapper.queryEquipment2(theaterId);
		return list;
	}
}
