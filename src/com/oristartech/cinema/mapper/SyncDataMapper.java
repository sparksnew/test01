package com.oristartech.cinema.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface SyncDataMapper {

	List searchLastSync(String tableName);

	void updateDataSync(@Param("tableName")String tableName, @Param("currentDate")String currentDate);

	void insertDataSync(@Param("tableName")String tableName, @Param("currentDate")String currentDate);

	List searchCustInfo(String lastUpdateTime);

	List searchCustomerID(int customerID1);

	void updateCustInfo(Map<String, Object> custMap);

	void insertCustInfo(Map<String, Object> custMap1);

	List querySyncTheaterData(String lastUpdateTime);

	List queryCinemaID(int customerID1);

	void updateCinemaInfo(Map<String, Object> map);

	void insertCinemaInfo(Map<String, Object> map);

}
