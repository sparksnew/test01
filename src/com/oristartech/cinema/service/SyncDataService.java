package com.oristartech.cinema.service;

import java.util.List;
import java.util.Map;

public interface SyncDataService {

	List searchLastSync(String tableName);

	void updateDataSync(String tableName, String currentDate);

	void insertDataSync(String tableName, String currentDate);

	List searchCustInfo(String lastUpdateTime);

	List searchCustomerID(int customerID1);

	void updateCustInfo(Map<String, Object> custMap);

	void insertCustInfo(Map<String, Object> custMap1);

	List getSyncTheaterData(String lastUpdateTime);

	List getCinemaID(int customerID1);

	void updateCinemaInfo(Map<String, Object> map);

	void insertCinemaInfo(Map<String, Object> map);

}
