package com.oristartech.cinema.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oristartech.cinema.mapper.CinemaInfoMapper;
import com.oristartech.cinema.mapper.TroubleListMapper;
import com.oristartech.cinema.pojo.CinemaInfo;
import com.oristartech.cinema.pojo.UserInput;
import com.oristartech.cinema.service.CinemaInfoService;
import com.oristartech.cinema.service.TroubleListService;
@Service
public class TroubleListServiceImpl implements TroubleListService {
	@Autowired
	private TroubleListMapper troubleListMapper;

	@Override
	public List queryTroubleListByCustomerID(String theaterId) {
		List list = troubleListMapper.queryTroubleListByCustomerID(theaterId);
		return list;
	}

	@Override
	public String getFlag(String deviceID) {
		String flag = troubleListMapper.queryFlag(deviceID);
		return flag;
	}

	@Override
	public List getBugId(String datetimes) {
		List list = troubleListMapper.queryBugId(datetimes);
		return list;
	}

	@Override
	public void addIncidentAssetLinks(int projectID, int incidentID, int linkedProjectID, int linkedAssetID) {
		troubleListMapper.insertIncidentAssetLinks(projectID,incidentID,linkedProjectID,linkedAssetID);
	}
}
