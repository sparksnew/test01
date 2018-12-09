package com.oristartech.cinema.mapper;

import java.util.List;

import com.oristartech.cinema.pojo.RoleInfo;
import com.oristartech.cinema.pojo.UserInput;

public interface RoleInfoMapper {


	List queryRoleInfoByInput(UserInput userInput);

	

}
