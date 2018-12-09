package com.oristartech.cinema.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface SumIncomeMapper {

	public List<Map<String, Object>> getThirtyDayFilmIncome(@Param("theaterID") String theaterID,
			@Param("startDate") String startDate, @Param("endDate") String endDate);

	public List<Map<String, Object>> getThirtyDayGoodsIncome(@Param("theaterID") String theaterID,
			@Param("startDate") String startDate, @Param("endDate") String endDate);

	public List<Map<String, Object>> getThirtyDayOtherIncome(@Param("theaterID") String theaterID,
			@Param("startDate") String startDate, @Param("endDate") String endDate);

	public List<Map<String, Object>> getTodayFilmIncome(@Param("theaterID") String theaterID,
			@Param("today") String today);

	public List<Map<String, Object>> getTodayGoodsIncome(@Param("theaterID") String theaterID,
			@Param("today") String today);

	public List<Map<String, Object>> getTodayOtherIncome(@Param("theaterID") String theaterID,
			@Param("today") String today);

	public List<Map<String, Object>> getHistoryIncome(@Param("theaterID") String theaterID,
			@Param("startDate") String startDate, @Param("endDate") String endDate);

	public List<Map<String, Object>> getRealTimeTTIncome(@Param("theaterID") String theaterID,
			@Param("today") String today);
}
