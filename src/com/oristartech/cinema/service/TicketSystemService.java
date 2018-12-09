package com.oristartech.cinema.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TicketSystemService {

	List getComprehensiveInfo(String theaterId, String date);

	BigDecimal getPFInfo(String theaterId, String date);

	List historyBoxOffice(String theaterId, String string, String currentDate);

	List historyMp(String theaterId, String date, String currentDate);

	List getSchedualInfo(String date, String theaterCode);

	List getSchedualDetail(String Time1, String Time2, String theaterCode);

	List getBoxOfficeByTheaterIDs(String theaterId, String date);

	List getAllTheaterMPList(String date, String theaterId);

	List getMPList(String theaterId, String date);

	List getMoviesBoxOffice(String theaterId, String date);

	List getGrailData(String date);
}
