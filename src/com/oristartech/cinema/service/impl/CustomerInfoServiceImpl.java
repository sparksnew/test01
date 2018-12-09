package com.oristartech.cinema.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oristartech.cinema.mapper.CustomerInfoMapper;
import com.oristartech.cinema.pojo.CustomerInfo;
import com.oristartech.cinema.pojo.UserInput;
import com.oristartech.cinema.service.CustomerInfoService;
@Service
public class CustomerInfoServiceImpl implements CustomerInfoService{
	@Autowired
	private CustomerInfoMapper customerMapper;
	@Override
	public List getCustomerInfoByInput(UserInput userInput) {
		List customerInfo = customerMapper.queryCustomerInfoByInput(userInput);
		return customerInfo;
	}
	@Override
	public String getCustomerTheaterIDs(String customerID) {
		String theaterids = customerMapper.queryCustomerTheaterIDs(customerID);
		return theaterids;
	}

}
