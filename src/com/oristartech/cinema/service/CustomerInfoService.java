package com.oristartech.cinema.service;

import java.util.List;

import com.oristartech.cinema.pojo.CustomerInfo;
import com.oristartech.cinema.pojo.UserInput;

public interface CustomerInfoService {

	List getCustomerInfoByInput(UserInput userInput);

	String getCustomerTheaterIDs(String customerID);

	

	

}
