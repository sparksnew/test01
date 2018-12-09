package com.oristartech.cinema.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.oristartech.cinema.service.AdviceService;
import com.oristartech.cinema.service.CinemaDeviceInfoService;
import com.oristartech.cinema.service.CinemaInfoService;
import com.oristartech.cinema.service.TicketSystemService;
import com.oristartech.cinema.utils.ApplicationContextUtil;
import com.oristartech.cinema.utils.DataSourceContextHolder;
import com.oristartech.cinema.utils.MessageUtils;
import com.oristartech.cinema.utils.RedisUtil;
import com.oristartech.cinema.utils.ToolUtils;

import net.sf.json.JSONArray;

@RequestMapping("/ticketSys")
@Controller
public class TictketSystemController {
	@Autowired
	private TicketSystemService ticketSystemService;
	@Autowired
	private CinemaInfoService cinemaInfoService;
	@Autowired
	private CinemaDeviceInfoService cinemaDeviceInfoService;
	@Autowired
	private AdviceService adviceService;

	@RequestMapping(value = "/baseinfo")
	@ResponseBody
	public JSONObject getBaseInfoByType(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取request参数
		JSONObject json = new JSONObject();
		String UserType = request.getParameter("UserType");
		// String theaterId = request.getParameter("cinemaCode");
		// String cinemaCode = request.getParameter("cinemaCode");
		String theaterId = request.getParameter("cinemaCode");
		String theaterCode = request.getParameter("cinemaCode");
		String date = request.getParameter("date");
		String length = request.getParameter("length");
		Integer length1 = Integer.valueOf(length);
		String type = request.getParameter("type");
		// try {
		if (UserType.equals("manage")) {
			json.put("PFBaseInfo", getComprehensive(theaterCode, date));
			json.put("PFBaseList", getHistoryBoxOffice(theaterCode, length1));
			type = "1";
			json.put("BugBaseInfoTheater", getTroubleData(theaterId, type));
			type = "0";
			json.put("BugBaseInfoDevice", getTroubleData(theaterId, type));

		} else if (UserType.equals("investor")) {
			// json.put("PFBaseInvList",
			// getAllTheaterPFList(date,theaterId));
			json.put("AllTheaterPFList", getBoxOffice1(theaterId, date));
			json.put("AllTheaterMPList", getAllTheaterMPList(theaterId, date));
			json.put("PFBaseInfo", getComprehensive(theaterId, date));
		} else if (UserType.equals("player")) {
			json.put("SchedualList", getSchedualData(theaterCode, date));
			type = "1";
			json.put("BugBaseInfoTheater", getTroubleData(theaterId, type));
			type = "0";
			json.put("BugBaseInfoDevice", getTroubleData(theaterId, type));
		} else {

		}
		json.put("validateCode", MessageUtils.SUCCESS_CODE);
		json.put("msg", MessageUtils.SUCCESS_MSG);
		return json;
	}

	@RequestMapping(value = "/getTheaterPosition")
	@ResponseBody
	public JSONObject getTheaterPosition(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取request参数
		JSONObject json = new JSONObject();
		String cinemaCode = request.getParameter("cinemaCode");
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TECHEXCEL);
		String cinemaID = adviceService.getEmpCinemaID(cinemaCode);
		String type = request.getParameter("type");
		type = "1";
		json.put("BugBaseInfoTheater", getTroubleData(cinemaID, type));
		type = "0";
		json.put("BugBaseInfoDevice", getTroubleData(cinemaID, type));
		DataSourceContextHolder.clearDbType();
		json.put("validateCode", MessageUtils.SUCCESS_CODE);
		json.put("msg", MessageUtils.SUCCESS_MSG);
		System.out.println("json=" + json);
		return json;
	}

	@RequestMapping(value = "/getTheaterBusniessData")
	@ResponseBody
	public void getTheaterBusniessData(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, NamingException, ClassNotFoundException, ParseException, IOException {
		// 获取request参数
		JSONObject json = new JSONObject();
		String date = request.getParameter("date");
		String length = request.getParameter("length");
		Integer length1 = Integer.valueOf(length);
		String theaterId = request.getParameter("theaterId");
		try {
			json.put("PFBaseInfo", getComprehensive(theaterId, date));
			json.put("PFBaseList", getHistoryBoxOffice(theaterId, length1));
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_MSG);
		} catch (Exception e) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}

	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/comprehensive")
	@ResponseBody
	public JSONObject getComprehensive(String theaterId, String date) throws ClassNotFoundException, ParseException {
		JSONObject json = new JSONObject();
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TICKET);
		// 票房，人次
		List ticketList = ticketSystemService.getComprehensiveInfo(theaterId, date);
		// 卖品
		BigDecimal pF = ticketSystemService.getPFInfo(theaterId, date);
		DataSourceContextHolder.clearDbType();
		try {
			for (int i = 0; i < ticketList.size(); i++) {
				Map map1 = (Map) ticketList.get(i);
				json.put("ACTUAL_MONEY", map1.get("ACTUAL_MONEY"));
				json.put("TOTAL_PERSON_NUM", map1.get("TOTAL_PERSON_NUM"));
				BigDecimal ActualMoney = (BigDecimal) map1.get("ACTUAL_MONEY");
				BigDecimal TotalPerson = (BigDecimal) map1.get("TOTAL_PERSON_NUM");
				BigDecimal TotalSeat = (BigDecimal) map1.get("TOTAL_SEAT");
				int r = TotalSeat.compareTo(BigDecimal.ZERO);
				if (r == 0 || r == -1) {
					json.put("SINGEL_SEAT_MNEY", 0);
				} else {
					BigDecimal SingelMoney = ActualMoney.divide(TotalSeat, 2, BigDecimal.ROUND_HALF_EVEN);
					json.put("SINGEL_SEAT_MNEY", SingelMoney);
				}
				BigDecimal Mp = (BigDecimal) map1.get("MPSR");
				int r3 = TotalPerson.compareTo(BigDecimal.ZERO);
				if (r3 == 0 || r3 == -1) {
					json.put("AVG_MONEY", 0);
					json.put("personMP", 0);
				} else {
					BigDecimal AvgMoney = ActualMoney.divide(TotalPerson, 2, BigDecimal.ROUND_HALF_EVEN);
					json.put("AVG_MONEY", AvgMoney);
					BigDecimal personMP = Mp.divide(TotalPerson, 1, BigDecimal.ROUND_HALF_EVEN);
					json.put("personMP", personMP);
				}

				int r2 = TotalPerson.compareTo(BigDecimal.ZERO);
				if (r2 == 0 || r2 == -1) {
					json.put("SPP", 0);
				} else {
					BigDecimal Spp = Mp.divide(TotalPerson, 2, BigDecimal.ROUND_HALF_EVEN);
					json.put("SPP", Spp);
				}
				BigDecimal BillCount = (BigDecimal) map1.get("BILL_COUNT");
				int r4 = BillCount.compareTo(BigDecimal.ZERO);
				if (r4 == 0 || r4 == -1) {
					json.put("payRate", 0);
				} else {
					BigDecimal payRate = BillCount.divide(TotalPerson, 4, BigDecimal.ROUND_HALF_EVEN);
					json.put("payRate", mul(payRate, 100) + "%");
				}
				json.put("MPSR", map1.get("MPSR"));
				json.put("XZHY", map1.get("XZHY"));
				json.put("CZJE", map1.get("CZJE"));
				json.put("LJHY", map1.get("LJHY"));
			}
			json.put("TOTAL_MP", pF);
			json.put("status", "1");
			json.put("msg", "成功");
		} catch (Exception e) {
			json.put("status", "0");
			json.put("msg", "失败");
		}

		return json;
	}

	/**
	 * 获取影院的数据信息（票房、人次、上座率、座位数、卖品收入、会员充值、SPP、新增会员）
	 * 
	 * @param theaterId
	 * @param context
	 * @param length
	 * @return
	 * @throws ClassNotFoundException
	 */
	@RequestMapping(value = "/historyboxoffice")
	@ResponseBody
	public JSONObject getHistoryBoxOffice(String theaterId, Integer length) throws ClassNotFoundException {
		long ti1 = System.currentTimeMillis();
		JSONObject json = new JSONObject();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = df.format(new Date());
		Date date = new Date();
		// String Type1 = "卖品收入";
		// String Type2 = "会员充值";
		ArrayList<Object> list = new ArrayList<Object>();
		ArrayList<Object> list1 = new ArrayList<Object>();
		int diff = -length + 1;
		Calendar calendar = Calendar.getInstance();// 日历对象
		calendar.setTime(date);// 设置当前日期
		calendar.add(Calendar.DATE, diff);
		String Date = df.format(calendar.getTime());
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TICKET);
		String name = "hisList-" + theaterId;
		List hisList = null;
		RedisUtil redisUtil = (RedisUtil) ApplicationContextUtil.getBean("redisUtil");
		if (redisUtil.hasKey("name")) {
			hisList = redisUtil.lGet(name, 0, -1);
		} else {
			hisList = ticketSystemService.historyBoxOffice(theaterId, Date, currentDate);
			redisUtil.lSet(name, hisList);
		}
		long ti2 = System.currentTimeMillis();
		System.out.println("T1=:" + (ti2 - ti1));
		List hisMpList = ticketSystemService.historyMp(theaterId, Date, currentDate);
		long ti3 = System.currentTimeMillis();
		System.out.println("T2=:" + (ti3 - ti2));
		DataSourceContextHolder.clearDbType();
		if (hisList != null && hisList.size() > 0) {
			for (int i = 0; i < hisList.size(); i++) {
				HashMap<String, Object> map1 = new HashMap<String, Object>();
				Map hisMap1 = (Map) hisList.get(i);
				map1.put("Day", hisMap1.get("SHOW_TIME"));
				map1.put("Sales", hisMap1.get("ACTUAL_MONEY"));
				map1.put("Person", hisMap1.get("TOTAL_PERSON_NUM"));
				map1.put("Seat", hisMap1.get("TOTAL_SEAT"));
				BigDecimal ActualMoney = (BigDecimal) hisMap1.get("ACTUAL_MONEY");
				BigDecimal TotalSeat = (BigDecimal) hisMap1.get("TOTAL_SEAT");
				int r1 = TotalSeat.compareTo(BigDecimal.ZERO);
				if (r1 == 0 || r1 == -1) {
					map1.put("Single", 0);
				} else {
					BigDecimal SingelMoney = ActualMoney.divide(TotalSeat, 2, BigDecimal.ROUND_HALF_EVEN);
					map1.put("Single", SingelMoney);
				}
				list.add(map1);
				json.put("HistoryBoxOffice", list);
			}
			try {
				json.put("status", "1");
				json.put("msg", "成功");
			} catch (Exception e) {
				json.put("status", "0");
				json.put("msg", "失败");
			}
		} else {
			json.put("status", "2");
			json.put("msg", "没有相关数据");
		}

		// 卖品
		if (hisMpList != null && hisMpList.size() > 0) {
			for (int k = 0; k < hisMpList.size(); k++) {
				HashMap<String, Object> map1 = new HashMap<String, Object>();
				Map hisMpMap = (Map) hisMpList.get(k);
				map1.put("Day", hisMpMap.get("SHOW_TIME"));
				map1.put("payRate", hisMpMap.get("GML"));
				map1.put("Mp", hisMpMap.get("MPSR"));
				BigDecimal Mp = (BigDecimal) hisMpMap.get("MPSR");
				BigDecimal billCount = (BigDecimal) hisMpMap.get("BILL_COUNT");
				BigDecimal totalPerson = (BigDecimal) hisMpMap.get("TOTAL_PERSON_NUM");
				int r2 = billCount.compareTo(BigDecimal.ZERO);
				if (r2 == 0 || r2 == -1) {
					map1.put("personMP", 0);
				} else {
					BigDecimal Spp = Mp.divide(totalPerson, 1, BigDecimal.ROUND_HALF_EVEN);
					map1.put("personMP", Spp);
				}
				list1.add(map1);
			}
			try {
				json.put("status", "1");
				json.put("HistoryMP", list1);
				json.put("msg", "成功");
			} catch (Exception e) {
				json.put("status", "0");
				json.put("msg", "失败");
			}
		} else {
			json.put("status", "2");
			json.put("msg", "没有相关数据");
		}
		long ti4 = System.currentTimeMillis();
		System.out.println("T4=:" + (ti4 - ti3));
		return json;
	}

	// 获得影院设备列表
	@RequestMapping(value = "/schedualdata")
	@ResponseBody
	public JSONObject getSchedualData(String theaterCode, String date)
			throws SQLException, ClassNotFoundException, ParseException {
		long t = System.currentTimeMillis();
		Map mp = TechexcelService.getRequest();
		JSONObject json = new JSONObject();
		SimpleDateFormat myFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if (!date.equals("")) {
			String date0 = myFmt.format(new Date());
			String date1 = date + " 05:30";
			Date dt1 = myFmt.parse(date0);
			Date dt2 = myFmt.parse(date1);
			String Time1 = "";
			String Time2 = "";
			// 判断当前事前是否过了今日的5：30，如果过了 就获取今日的排期，如果没过就还是昨日的排期
			if (dt1.getTime() < dt2.getTime()) {
				Date Date1 = myFmt.parse(date1);
				Calendar rightNow = Calendar.getInstance();
				rightNow.setTime(Date1);
				rightNow.add(Calendar.DAY_OF_YEAR, -1);// 日期减1天
				String date2 = myFmt.format(rightNow.getTime());
				Time1 = date2;
				Time2 = date1;
			} else {
				Date Date1 = myFmt.parse(date1);
				Calendar rightNow = Calendar.getInstance();
				rightNow.setTime(Date1);
				rightNow.add(Calendar.DAY_OF_YEAR, 1);// 日期加1天
				String date2 = myFmt.format(rightNow.getTime());
				Time1 = date1;
				Time2 = date2;
			}
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TICKET);
			List schedualList = ticketSystemService.getSchedualInfo(date, theaterCode);
			List schedualDetail = ticketSystemService.getSchedualDetail(Time1, Time2, theaterCode);
			DataSourceContextHolder.clearDbType();
			ArrayList<Object> list = new ArrayList<Object>();
			if (schedualList != null && schedualList.size() > 0 && schedualDetail != null
					&& schedualDetail.size() > 0) {
				for (int i = 0; i < schedualList.size(); i++) {
					Map schedualMap = (Map) schedualList.get(i);
					String hallName = (String) schedualMap.get("hallName");
					ArrayList<Object> list1 = new ArrayList<Object>();
					for (int j = 0; j < schedualDetail.size(); j++) {
						Map schedualDetail1 = (Map) schedualDetail.get(j);
						String hallName1 = (String) schedualDetail1.get("hallName");
						if (hallName1.equals(hallName)) {
							HashMap<String, Object> map1 = new HashMap<String, Object>();
							map1.put("showName", schedualDetail1.get("showName"));
							map1.put("moiveCode", schedualDetail1.get("moiveCode"));
							map1.put("duration", schedualDetail1.get("duration"));
							map1.put("showTime", schedualDetail1.get("showTime"));
							map1.put("audiences", schedualDetail1.get("audiences"));
							list1.add(map1);
						}
					}
					HashMap<String, Object> map2 = new HashMap<String, Object>();
					map2.put("hallName1", hallName);
					if (hallName.length() >= 5) {
						hallName = hallName.substring(0, 4);
						hallName = hallName + "...";
					}
					map2.put("hallName", hallName);
					map2.put("seats", schedualMap.get("seats"));
					map2.put("showList", list1);
					list.add(map2);
				}
				try {
					json.put("logs", list);
					json.put("status", "1");
					json.put("msg", "成功");
				} catch (Exception e) {
					json.put("status", "0");
					json.put("msg", "失败");
				}
			} else {
				json.put("status", "2");
				json.put("msg", "没有相关数据");
			}
			long t2 = System.currentTimeMillis();
			System.out.println("T4=:" + (t2 - t));
			return json;
		}
		return null;
	}

	public String getCustomerTheaterIDs(String customerID) throws SQLException, NamingException {
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
		List list = cinemaInfoService.getCinemaCode(customerID);
		DataSourceContextHolder.clearDbType();
		int i = 0;
		String theaterId = "";
		for (int k = 0; k < list.size(); k++) {
			Map map = (Map) list.get(k);
			String cinemaCode = (String) map.get("cinemaCode");
			if (i > 0) {
				theaterId = theaterId + ",'" + cinemaCode + "'";
			} else {
				theaterId = "'" + cinemaCode + "'";
				i++;
			}
		}
		return theaterId;
	}

	public ArrayList<Object> getCustomerTheaters(String customerID) throws SQLException, NamingException {
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
		List list = cinemaInfoService.getCinemaCode(customerID);
		DataSourceContextHolder.clearDbType();
		int i = 0;
		String theaterId = "";
		ArrayList<Object> list1 = new ArrayList<Object>();
		for (int k = 0; k < list.size(); k++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			Map map1 = (Map) list.get(k);
			map.put("theaterId", map1.get("cinemaID"));
			map.put("theaterCode", map1.get("cinemaCode"));
			map.put("theaterName", map1.get("cinemaName"));
			list1.add(map);
		}
		return list1;
	}

	@RequestMapping(value = "/alltheaterpf")
	@ResponseBody
	public JSONObject getAllTheaterPFList(String date, String theaterId)
			throws SQLException, NamingException, ClassNotFoundException {
		Map mp = TechexcelService.getRequest();
		JSONObject json = new JSONObject();
		String[] arr = theaterId.split(",");
		// ArrayList<Object> TheaterIDs = getCustomerTheaters(customerID);
		// if (TheaterIDs != null && !"".equals(TheaterIDs)) {
		// json.put("AllTheaterPFList", getBoxOffice1(TheaterIDs, date));
		// json.put("AllTheaterMPList", getAllTheaterMPList(TheaterIDs, date));
		// }

		return json;
	}

	/**
	 * 获取影院票房，人次，上座率，单座票房
	 * 
	 * @param response
	 * @param request
	 * 
	 * @param theaterIDs
	 * @param theaterName
	 * @param context
	 * @return
	 */
	@RequestMapping(value = "boxoffice1")
	@ResponseBody
	public JSONObject getBoxOffice1(String theaterId, String date) {
		Map mp = TechexcelService.getRequest();
		// String theaterId = getIDsByList(theaterIDs);
		JSONObject json = new JSONObject();
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TICKET);
		List ticketList = ticketSystemService.getBoxOfficeByTheaterIDs(theaterId, date);
		DataSourceContextHolder.clearDbType();
		ArrayList<Object> list = new ArrayList<Object>();
		if (ticketList != null && ticketList.size() > 0) {
			for (int i = 0; i < ticketList.size(); i++) {
				HashMap<String, Object> map1 = new HashMap<String, Object>();
				Map ticketMap = (Map) ticketList.get(i);
				map1.put("CINEMA_CODE", ticketMap.get("CINEMA_CODE"));
				map1.put("CINEMA_NAME", ticketMap.get("CINEMA_NAME"));
				// map1.put("THEATER_NAME", getNameByListID(theaterIDs,
				// ticketMap.get("CINEMA_CODE").toString()));
				map1.put("ACTUAL_MONEY", ticketMap.get("ACTUAL_MONEY"));
				map1.put("TOTAL_PERSON_NUM", ticketMap.get("TOTAL_PERSON_NUM"));
				map1.put("SZL", ticketMap.get("SZL"));
				map1.put("SINGEL_SEAT_MNEY", ticketMap.get("SINGEL_SEAT_MNEY"));
				map1.put("AVG_MONEY", ticketMap.get("AVG_MONEY"));
				list.add(map1);
			}
		}
		json.put("list", list);
		return json;
	}

	@RequestMapping(value = "alltheatermp")
	@ResponseBody
	public JSONObject getAllTheaterMPList(String theaterId, String date) throws SQLException, ClassNotFoundException {
		/*
		 * Map mp = TechexcelService.getRequest(); HttpServletResponse rs =
		 * (HttpServletResponse) mp.get("rs"); String jsonp = (String)
		 * mp.get("callback");
		 */
		JSONObject json = new JSONObject();
		// String theaterId = getIDsByList(theaterIDs);
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TICKET);
		List theaterMpList = ticketSystemService.getAllTheaterMPList(date, theaterId);
		DataSourceContextHolder.clearDbType();
		ArrayList<Object> list = new ArrayList<Object>();
		if (theaterMpList != null && theaterMpList.size() > 0) {
			for (int i = 0; i < theaterMpList.size(); i++) {
				HashMap<String, Object> map1 = new HashMap<String, Object>();
				Map theaterMpMap = (Map) theaterMpList.get(i);
				map1.put("CINEMA_CODE", theaterMpMap.get("CINEMA_CODE"));
				map1.put("CINEMA_NAME", theaterMpMap.get("CINEMA_NAME"));
				// map1.put("THEATER_NAME", getNameByListID(theaterIDs,
				// theaterMpMap.get("CINEMA_CODE").toString()));
				map1.put("MPSR", theaterMpMap.get("MPSR"));
				map1.put("SPP", theaterMpMap.get("SPP"));
				map1.put("BILL_COUNT", theaterMpMap.get("BILL_COUNT"));
				BigDecimal Mp = (BigDecimal) theaterMpMap.get("MPSR");
				BigDecimal TotalPerson = (BigDecimal) theaterMpMap.get("TOTAL_PERSON_NUM");
				// BigDecimal TotalSeat = (BigDecimal)
				// theaterMpMap.get("TOTAL_SEAT");
				int r3 = TotalPerson.compareTo(BigDecimal.ZERO);
				if (r3 == 0 || r3 == -1) {
					map1.put("personMP", 0);
				} else {
					BigDecimal personMP = Mp.divide(TotalPerson, 1, BigDecimal.ROUND_HALF_EVEN);
					map1.put("personMP", personMP);
				}
				BigDecimal BillCount = (BigDecimal) theaterMpMap.get("BILL_COUNT");
				int r4 = BillCount.compareTo(BigDecimal.ZERO);
				if (r4 == 0 || r4 == -1) {
					map1.put("payRate", "0.00%");
				} else {
					BigDecimal payRate = BillCount.divide(TotalPerson, 4, BigDecimal.ROUND_HALF_EVEN);
					// System.out.println(payRate);
					map1.put("payRate", mul(payRate, 100) + "%");
				}
				list.add(map1);
			}
		}
		json.put("list", list);
		return json;
	}

	public static String getIDsByList(ArrayList<Object> theaterIDs) {
		String theaterId = "";
		for (int i = 0; i < theaterIDs.size(); i++) {
			HashMap<String, Object> map = (HashMap<String, Object>) theaterIDs.get(i);
			try {
				String theaterId1 = map.get("theaterCode").toString();
				if (i > 0)
					theaterId = theaterId + ",'" + theaterId1 + "'";
				else
					theaterId = "'" + theaterId1 + "'";
			} catch (NullPointerException e) {

			}
		}
		return theaterId;
	}

	public static String getNameByListID(ArrayList<Object> theaterIDs, String theaterId) {
		String theaterName = "";
		for (int i = 0; i < theaterIDs.size(); i++) {
			HashMap<String, Object> map = (HashMap<String, Object>) theaterIDs.get(i);
			if (theaterId.equals(map.get("theaterCode").toString())) {
				theaterName = map.get("theaterName").toString();
				return theaterName;
			}
		}
		return theaterName;
	}

	/**
	 * 此方法用于获取报障的基本信息，例如报障位置，报障设备等基础信息此
	 * 
	 * @param type
	 * @param theaterId
	 * 
	 * @param params
	 * @param context
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/troubleDate")
	@ResponseBody
	public JSONObject getTroubleData(String theaterId, String type) throws Exception {
		JSONObject json = new JSONObject();
		json = getEquipment(theaterId, type); // 调用接口方法，此方法是直接通过数据库进行获取的信息

		return json;
	}

	// 获得影院设备列表
	@RequestMapping(value = "/equipment")
	@ResponseBody
	public JSONObject getEquipment(String theaterId, String type) throws Exception {
		Map mp = TechexcelService.getRequest();
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TECHEXCEL);
		JSONObject json = new JSONObject();
		List list = null;
		if (!"".equals(type) && type != null) {
			if (type.equals("1")) {
				list = cinemaDeviceInfoService.getTroubleField(theaterId);
			} else {
				list = cinemaDeviceInfoService.getEquipment(theaterId);
			}
		}
		DataSourceContextHolder.clearDbType();
		if (list != null && list.size() > 0) {
			JSONArray fieldJson = JSONArray.fromObject(list);
			json.put("logs", fieldJson);
			json.put("status", "1");
			json.put("msg", "成功");
		} else {
			json.put("status", "2");
			json.put("msg", "没有相关数据");
		}

		return json;
	}

	/**
	 * BigDecimal乘法
	 * 
	 * @param payRate
	 * @param v2
	 * @return
	 */
	public static double mul(BigDecimal payRate, double v2) {
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return payRate.multiply(b2).doubleValue();
	}

	// 获得影院设备列表
	@RequestMapping(value = "/equipmentList")
	@ResponseBody
	public JSONObject getEquipmentList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String cinemaCode = request.getParameter("cinemaCode");
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TECHEXCEL);
		JSONObject json = new JSONObject();
		String theaterId = adviceService.getEmpCinemaID(cinemaCode);
		List list = cinemaDeviceInfoService.getEquipment2(theaterId);
		DataSourceContextHolder.clearDbType();
		// if (list != null && list.size() > 0) {
		try {
			json.put("logs", list);
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_MSG);
		} catch (Exception e) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
		// } else {
		// json.put("validateCode", MessageUtils.FAIL_CODE);
		// json.put("msg", MessageUtils.FAIL_MSG);
		// }
		return json;
	}

	@RequestMapping(value = "/theatermpdata")
	@ResponseBody
	public JSONObject getTheaterMpData() {
		Map requestMap = TechexcelService.getRequest();
		HttpServletRequest request = (HttpServletRequest) requestMap.get("request");
		String theaterCode = request.getParameter("theaterCode");
		String date = request.getParameter("date");
		JSONObject json = new JSONObject();
		json.put("MPList", getMPList(theaterCode, date));
		return json;
	}

	@RequestMapping(value = "/theaterpfdata")
	@ResponseBody
	public JSONObject getTheaterPfData() {
		long time1 = System.currentTimeMillis();
		JSONObject json = new JSONObject();
		Map requestMap = TechexcelService.getRequest();
		HttpServletRequest request = (HttpServletRequest) requestMap.get("request");
		String theaterCode = request.getParameter("theaterCode");
		String date = request.getParameter("date");
		json.put("boxOfficeList", getMoviesBoxOffice(theaterCode, date));
		long time2 = System.currentTimeMillis();
		System.out.println("Time1=" + (time2 - time1));
		return json;
	}

	/**
	 * 获取卖品历史数据列表(销售量，销售额，总成本，总单数)
	 * 
	 * @param theaterId
	 * @param Date
	 * @param context
	 * @return
	 */
	public JSONObject getMPList(String theaterId, String Date) {
		JSONObject json = new JSONObject();
		Map requestMap = TechexcelService.getRequest();
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TICKET);
		List mpList = ticketSystemService.getMPList(theaterId, Date);
		DataSourceContextHolder.clearDbType();
		ArrayList<Object> list = new ArrayList<Object>();
		if (mpList != null && mpList.size() > 0) {
			for (int i = 0; i < mpList.size(); i++) {
				Map map = (Map) mpList.get(i);
				HashMap<String, Object> map1 = new HashMap<String, Object>();
				map1.put("CINEMA_CODE", map.get("CINEMA_CODE"));
				map1.put("CINEMA_NAME", map.get("CINEMA_NAME"));
				map1.put("MER_NAME", map.get("MER_NAME"));
				map1.put("MER_CODE", map.get("MER_CODE"));
				map1.put("SALE_AMOUNT", map.get("SALE_AMOUNT"));
				map1.put("SALE_MONEY", map.get("SALE_MONEY"));
				map1.put("SALE_COST", map.get("SALE_COST"));
				map1.put("BILL_COUNT", map.get("BILL_COUNT"));
				String storeNum1 = (String) map.get("storeNum");
				String storeNum = storeNum1;
				if (storeNum1.contains(",")) {
					storeNum = storeNum1.substring(0, storeNum1.indexOf(","))
							+ storeNum1.substring(storeNum1.indexOf(",") + 1, storeNum1.length());
				}
				map1.put("storeNum", storeNum);
				map1.put("lossRate", map.get("lossRate"));
				list.add(map1);
			}
			try {
				json.put("list", list);
				json.put("validateCode", MessageUtils.SUCCESS_CODE);
				json.put("msg", MessageUtils.SUCCESS_MSG);
			} catch (Exception e) {
				json.put("validateCode", MessageUtils.FAIL_CODE);
				json.put("msg", MessageUtils.FAIL_MSG);
			}

		} else {
			json.put("list", list);
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_MSG);
		}
		return json;
	}

	/**
	 * 获取影院票房历史数据列表(展示时间、票房、人次、计划可用场次，计划不可用场次、排片率、上座率、历史票房、上映总天数)
	 * 
	 * @param theaterId
	 * @param Date
	 * @param context
	 * @return
	 */
	public JSONObject getMoviesBoxOffice(String theaterId, String Date) {
		JSONObject json = new JSONObject();
		Map requestMap = TechexcelService.getRequest();
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TICKET);
		List moviesBoxOffice = ticketSystemService.getMoviesBoxOffice(theaterId, Date);
		DataSourceContextHolder.clearDbType();
		ArrayList<Object> list = new ArrayList<Object>();
		if (moviesBoxOffice != null && moviesBoxOffice.size() > 0) {
			for (int i = 0; i < moviesBoxOffice.size(); i++) {
				Map map = (Map) moviesBoxOffice.get(i);
				HashMap<String, Object> map1 = new HashMap<String, Object>();
				String cinemaName = (String) map.get("CINEMA_NAME");
				String cinemaName1 = cinemaName;
				if (cinemaName.contains("(")) {
					cinemaName1 = cinemaName.substring(0, cinemaName.indexOf("("));
				} else if (cinemaName.contains("（")) {
					cinemaName1 = cinemaName.substring(0, cinemaName.indexOf("（"));
				}
				map1.put("CINEMA_NAME", cinemaName1);
				map1.put("MOVIE_NAME", map.get("MOVIE_NAME"));
				map1.put("SHOW_TIME", map.get("SHOW_TIME"));
				map1.put("MOVIE_SALE", map.get("MOVIE_SALE"));
				map1.put("PPL", map.get("PPL"));
				map1.put("SZL", map.get("SZL"));
				map1.put("ZRC", map.get("ZRC"));
				map1.put("DAYS", map.get("DAYS"));
				map1.put("HIS_SALE", map.get("HIS_SALE"));
				map1.put("planTimesValid", map.get("planTimesValid"));
				map1.put("evgPrice", map.get("evgPrice"));
				map1.put("netPercent", map.get("netPercent"));
				map1.put("evgPerson", map.get("evgPerson"));
				map1.put("boxPercent", map.get("boxPercent"));
				list.add(map1);
			}
			try {
				json.put("list", list);
				json.put("validateCode", MessageUtils.SUCCESS_CODE);
				json.put("msg", MessageUtils.SUCCESS_MSG);
			} catch (Exception e) {
				json.put("validateCode", MessageUtils.FAIL_CODE);
				json.put("msg", MessageUtils.FAIL_MSG);
			}

		} else {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
		return json;
	}

	@RequestMapping(value = "/ScheduleList")
	@ResponseBody
	public void getScheduleList(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, NamingException, ClassNotFoundException, ParseException, IOException {
		// 获取request参数
		JSONObject json = new JSONObject();
		String theaterCode = request.getParameter("theaterCode");
		System.out.println(theaterCode);
		String date = request.getParameter("date");
		try {
			json.put("SchedualList", getSchedualData(theaterCode, date));
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_MSG);
		} catch (Exception e) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}

	}

	@RequestMapping(value = "/graildata")
	@ResponseBody
	public JSONObject getGrailData() throws ParseException {
		JSONObject json = new JSONObject();
		Map requestMap = TechexcelService.getRequest();
		HttpServletRequest request = (HttpServletRequest) requestMap.get("request");
		String date = request.getParameter("date");
		json.put("GrailData", getGrailData(date));
		return json;
	}

	public JSONObject getGrailData(String date) throws ParseException {
		JSONObject json = new JSONObject();
		Map requestMap = TechexcelService.getRequest();
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TICKET);
		List graildataList = ticketSystemService.getGrailData(date);
		DataSourceContextHolder.clearDbType();
		ArrayList<Object> list = new ArrayList<Object>();
		if (null != graildataList && graildataList.size() > 0) {
			for (int i = 0; i < graildataList.size(); i++) {
				Map graildataMap = (Map) graildataList.get(i);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("NAME", graildataMap.get("NAME"));
				map.put("CURRENT_BOX_OFFICE", graildataMap.get("CURRENT_BOX_OFFICE"));
				map.put("BOX_OFFICE_RATIO", graildataMap.get("BOX_OFFICE_RATIO"));
				map.put("BOX_OFFICE_SUM", graildataMap.get("BOX_OFFICE_SUM"));
				map.put("SCHEDULE_RATIO", graildataMap.get("SCHEDULE_RATIO"));
				map.put("PUBLIC_DAYS", graildataMap.get("PUBLIC_DAYS"));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				String FETCH_TIME1 = sdf.format(graildataMap.get("FETCH_TIME"));
				map.put("FETCH_TIME", FETCH_TIME1);
				list.add(map);
			}
			try {
				json.put("logs", list);
				json.put("validateCode", MessageUtils.SUCCESS_CODE);
				json.put("msg", MessageUtils.SUCCESS_MSG);
			} catch (Exception e) {
				json.put("validateCode", MessageUtils.FAIL_CODE);
				json.put("msg", MessageUtils.FAIL_MSG);
			}
		} else {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
		return json;
	}
}
