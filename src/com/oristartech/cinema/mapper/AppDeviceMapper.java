package com.oristartech.cinema.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface AppDeviceMapper {
	
	List queryDeviceListByCustomerID(String theaterId);

	List queryTroubleField(String theaterId);

	List queryEquipment(String theaterId);

	List queryEquipment1(@Param("theaterId")String theaterId, @Param("positionID")String positionID);

	List queryEquipment2(String theaterId);

}
