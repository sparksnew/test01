package com.oristartech.cinema.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oristartech.cinema.mapper.TicketSystemMapper;
import com.oristartech.cinema.service.TicketSystemService;
@Service
public class TicketSystemServiceImpl implements TicketSystemService {
	@Autowired
	private TicketSystemMapper ticketSystemMapper;
	@Override
	public List getComprehensiveInfo(String theaterId, String date) {
		List list = ticketSystemMapper.queryComprehensiveInfo(theaterId,date);
		return list;
	}
	@Override
	public BigDecimal getPFInfo(String theaterId, String date) {
		BigDecimal pF = ticketSystemMapper.queryPFInfo(theaterId, date);
		return pF;
	}
	@Override
	public List historyBoxOffice(String theaterId, String Date, String currentDate) {
		List hisList = ticketSystemMapper.queryHistoryBoxOffice(theaterId,Date,currentDate);
		return hisList;
	}
	@Override
	public List historyMp(String theaterId, String Date, String currentDate) {
		List hisMpList = ticketSystemMapper.queryHistoryMp(theaterId,Date,currentDate);
		return hisMpList;
	}
	@Override
	public List getSchedualInfo(String date, String theaterCode) {
		List schedualInfo = ticketSystemMapper.querySchedualInfo(date,theaterCode);
		return schedualInfo;
	}
	@Override
	public List getSchedualDetail(String Time1, String Time2, String theaterCode) {
		List schedualDetail = ticketSystemMapper.querySchedualDetail(Time1,Time2,theaterCode);
		return schedualDetail;
	}
	@Override
	public List getBoxOfficeByTheaterIDs(String theaterIDs, String date) {
		List list = ticketSystemMapper.queryBoxOffice1(theaterIDs,date);
		return list;
	}
	@Override
	public List getAllTheaterMPList(String date, String theaterId) {
		List list = ticketSystemMapper.queryAllTheaterMPList(date,theaterId);
		return list;
	}
	@Override
	public List getMPList(String theaterId, String date) {
		List list = ticketSystemMapper.queryMPList(date,theaterId);
		return list;
	}
	@Override
	public List getMoviesBoxOffice(String theaterId, String date) {
		List list = ticketSystemMapper.queryMoviesBoxOffice(date,theaterId);
		return list;
	}
	@Override
	public List getGrailData(String date) {
		List list = ticketSystemMapper.queryGrailData(date);
		return list;
	}
	
	

}
