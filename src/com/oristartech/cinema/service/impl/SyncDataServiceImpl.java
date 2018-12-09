package com.oristartech.cinema.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oristartech.cinema.mapper.SyncDataMapper;
import com.oristartech.cinema.service.SyncDataService;
@Service
public class SyncDataServiceImpl implements SyncDataService{
	@Autowired
	private SyncDataMapper syncDataMapper;
	@Override
	public List searchLastSync(String tableName) {
		List list = syncDataMapper.searchLastSync(tableName);
		return list;
	}
	@Override
	public void updateDataSync(String tableName, String currentDate) {
		syncDataMapper.updateDataSync(tableName,currentDate);
		
	}
	@Override
	public void insertDataSync(String tableName, String currentDate) {
		syncDataMapper.insertDataSync(tableName,currentDate);
		
	}
	@Override
	public List searchCustInfo(String lastUpdateTime) {
		List list = syncDataMapper.searchCustInfo(lastUpdateTime);
		return list;
	}
	@Override
	public List searchCustomerID(int customerID1) {
		List list = syncDataMapper.searchCustomerID(customerID1);		
		return list;
	}
	@Override
	public void updateCustInfo(Map<String, Object> custMap) {
		syncDataMapper.updateCustInfo(custMap);
		
	}
	@Override
	public void insertCustInfo(Map<String, Object> custMap1) {
		syncDataMapper.insertCustInfo(custMap1);
		
	}
	@Override
	public List getSyncTheaterData(String lastUpdateTime) {
		List list = syncDataMapper.querySyncTheaterData(lastUpdateTime);
		return list;
	}
	@Override
	public List getCinemaID(int customerID1) {
		List list = syncDataMapper.queryCinemaID(customerID1);
		return list;
		
	}
	@Override
	public void updateCinemaInfo(Map<String, Object> map) {
		syncDataMapper.updateCinemaInfo(map);
		
	}
	@Override
	public void insertCinemaInfo(Map<String, Object> map) {
		syncDataMapper.insertCinemaInfo(map);
		
	}

}
