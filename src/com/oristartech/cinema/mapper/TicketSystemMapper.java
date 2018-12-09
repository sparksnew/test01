package com.oristartech.cinema.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
@SuppressWarnings("rawtypes")
public interface TicketSystemMapper {

	List queryComprehensiveInfo(@Param("theaterId")String theaterId,@Param("date")String date);

	BigDecimal queryPFInfo(@Param("theaterId")String theaterId, @Param("date")String date);

	List queryHistoryBoxOffice(@Param("theaterId")String theaterId, @Param("Date")String Date, @Param("currentDate")String currentDate);

	List queryHistoryMp(@Param("theaterId")String theaterId, @Param("Date")String Date, @Param("currentDate")String currentDate);

	List querySchedualInfo(@Param("date")String date, @Param("theaterCode")String theaterCode);

	List querySchedualDetail(@Param("Time1")String Time1, @Param("Time2")String Time2, @Param("theaterCode")String theaterCode);

	List queryBoxOffice1(@Param("theaterId")String theaterIDs, @Param("date")String date);

	List queryAllTheaterMPList(@Param("date")String date, @Param("theaterId")String theaterId);

	List queryMPList(@Param("Date")String Date, @Param("theaterId")String theaterId);

	List queryMoviesBoxOffice(@Param("Date")String Date, @Param("theaterId")String theaterId);

	List queryGrailData(@Param("date")String date);
}
