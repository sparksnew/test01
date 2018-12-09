package com.oristartech.cinema.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface TroubleListMapper {

	List queryTroubleListByCustomerID(String theaterId);

	String queryFlag(String deviceID);

	List queryBugId(String datetimes);

	void insertIncidentAssetLinks(@Param("projectID")int projectID, @Param("incidentID")int incidentID, @Param("linkedProjectID")int linkedProjectID, @Param("linkedAssetID")int linkedAssetID);	

}
