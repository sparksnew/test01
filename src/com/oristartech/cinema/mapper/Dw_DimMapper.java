package com.oristartech.cinema.mapper;

import org.apache.ibatis.annotations.Param;

public interface Dw_DimMapper {

	Integer getCountByTheaterCode(@Param("theaterCode")String theaterCode);
}
