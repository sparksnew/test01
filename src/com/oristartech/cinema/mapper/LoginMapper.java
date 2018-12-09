package com.oristartech.cinema.mapper;

import org.apache.ibatis.annotations.Param;

public interface LoginMapper {
      
	 public int loginAction(@Param("username")String username,@Param("userpass")String userpass);
}
