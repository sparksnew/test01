package com.oristartech.cinema.service;

import java.util.List;

public interface CinemaDeviceInfoService {

	List getDeviceListByCustomerID(String theaterId);

	List getTroubleField(String theaterId);

	List getEquipment(String theaterId);

	List getEquipment1(String theaterId, String positionID);

	List getEquipment2(String theaterId);
}
