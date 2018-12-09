package com.oristartech.cinema.mapper;

import java.util.List;

import com.oristartech.cinema.pojo.CustomerInfo;
import com.oristartech.cinema.pojo.UserInput;

public interface CustomerInfoMapper {

	List queryCustomerInfoByInput(UserInput userInput);

	String queryCustomerTheaterIDs(String customerID);




	

}
