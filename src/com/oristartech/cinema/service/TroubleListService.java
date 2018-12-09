package com.oristartech.cinema.service;

import java.util.List;

public interface TroubleListService {

	List queryTroubleListByCustomerID(String theaterId);

	String getFlag(String deviceID);

	List getBugId(String datetimes);

	void addIncidentAssetLinks(int projectID, int incidentID, int linkedProjectID, int linkedAssetID);

    

}
