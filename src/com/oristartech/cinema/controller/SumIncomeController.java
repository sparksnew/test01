package com.oristartech.cinema.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.oristartech.cinema.service.CinemaDeviceInfoService;
import com.oristartech.cinema.service.CinemaInfoService;
import com.oristartech.cinema.service.SumIncomeService;
import com.oristartech.cinema.service.TicketSystemService;
//import com.oristartech.cinema.utils.ApplicationContextUtil;
import com.oristartech.cinema.utils.DataSourceContextHolder;
import com.oristartech.cinema.utils.DateUtil;
import com.oristartech.cinema.utils.MessageUtils;
//import com.oristartech.cinema.utils.RedisObjectSerializer;
//import com.oristartech.cinema.utils.RedisUtil;
import com.oristartech.cinema.utils.ToolUtils;
import com.oristartech.cinema.controller.TictketSystemController;

@Controller
@RequestMapping(value = "/income")
public class SumIncomeController {

	@Autowired
	private SumIncomeService service;
	@Autowired
	private TicketSystemService ticketSystemService;
	@Autowired
	private CinemaInfoService cinemaInfoService;
	@Autowired
	private CinemaDeviceInfoService cinemaDeviceInfoService;

	@RequestMapping(value = "/getTotalInfo", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public JSONObject getTotalInfo(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		String theaterID = request.getParameter("theaterID");
		if (theaterID == null || theaterID.length() <= 0) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
		// try {
		String startDate = DateUtil.laterThirtyDay();
		String endDate = DateUtil.getCurrentDate().substring(0, 10);
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TICKET);
		JSONObject thirtyJSON = service.getHistoryIncome(theaterID, startDate, endDate);
		JSONObject todayJSON = service.getRealTimeTTIncome(theaterID, endDate);
		DataSourceContextHolder.clearDbType();
		json.put("thirtyJSON", thirtyJSON);
		json.put("todayJSON", todayJSON);
		json.put("validateCode", MessageUtils.SUCCESS_CODE);
		json.put("msg", MessageUtils.SUCCESS_MSG);
		// } catch (Exception e) {
		//// e.printStackTrace();
		// json.put("validateCode", MessageUtils.FAIL_CODE);
		// json.put("msg", MessageUtils.FAIL_MSG);
		// }
		return json;
	}

	@RequestMapping(value = "/getSumIncome", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public void getSumIncome(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		HttpServletResponse rs = ToolUtils.getResponseType(response);
		String jsonp = request.getParameter("callback");
		String theaterID = request.getParameter("theaterID");
		// System.out.println("theaterID="+theaterID);
		if (theaterID == null || theaterID.length() <= 0) {
			ToolUtils.setUnsuccess(json, jsonp, rs);
		}
		try {
			String startDate = DateUtil.laterThirtyDay();
			// System.out.println("startDate=" + startDate);
			String endDate = DateUtil.getCurrentDate().substring(0, 10);
			// System.out.println("endDate="+endDate);
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TICKET);
			JSONObject thirtyJSON = service.getThirtyDayIncome(theaterID, startDate, endDate);
			JSONObject todayJSON = service.getTodayIncome(theaterID, endDate);
			DataSourceContextHolder.clearDbType();
			json.put("thirtyJSON", thirtyJSON);
			json.put("todayJSON", todayJSON);
			ToolUtils.setSuccess(json, jsonp, rs);
		} catch (Exception e) {
			ToolUtils.setUnsuccess(json, jsonp, rs);
		}
	}

	@RequestMapping(value = "getRealTimePFIncome")
	public void getRealTimePFIncome(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject json = new JSONObject();
		String theaterID = request.getParameter("theaterID");
		// System.out.println("theaterID="+theaterID);
		if (theaterID == null || theaterID.length() <= 0) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
		try {
			String endDate = DateUtil.getCurrentDate().substring(0, 10);
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TICKET);
			JSONObject todayJSON = service.getRealTimePFIncome(theaterID, endDate);
			DataSourceContextHolder.clearDbType();
			json.put("RealTimePFIncome", todayJSON);
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_MSG);
		} catch (Exception e) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
	}

	@RequestMapping(value = "getRealTimeMPIncome")
	public void getRealTimeMPIncome(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject json = new JSONObject();
		String theaterID = request.getParameter("theaterID");
		// System.out.println("theaterID="+theaterID);
		if (theaterID == null || theaterID.length() <= 0) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
		try {
			String endDate = DateUtil.getCurrentDate().substring(0, 10);
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TICKET);
			JSONObject todayJSON = service.getRealTimeMPIncome(theaterID, endDate);
			DataSourceContextHolder.clearDbType();
			json.put("RealTimeMPIncome", todayJSON);
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_MSG);
		} catch (Exception e) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
	}

	@RequestMapping(value = "getRealTimeQTIncome")
	public void getRealTimeQTIncome(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject json = new JSONObject();
		String theaterID = request.getParameter("theaterID");
		// System.out.println("theaterID="+theaterID);
		if (theaterID == null || theaterID.length() <= 0) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
		try {
			String endDate = DateUtil.getCurrentDate().substring(0, 10);
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TICKET);
			JSONObject todayJSON = service.getRealTimeQTIncome(theaterID, endDate);
			DataSourceContextHolder.clearDbType();
			json.put("RealTimeQTIncome", todayJSON);
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_MSG);
		} catch (Exception e) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
	}

	@RequestMapping(value = "getBoxInfo")
	@ResponseBody
	public JSONObject getBoxInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long startTime = System.currentTimeMillis();
		JSONObject json = new JSONObject();
		String theaterId = request.getParameter("theaterId");
		int length = Integer.parseInt(request.getParameter("length").toString());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = df.format(new Date());
		Date date = new Date();
		ArrayList<Object> list = new ArrayList<Object>();
		int diff = -length + 1;
		Calendar calendar = Calendar.getInstance();// 日历对象
		calendar.setTime(date);// 设置当前日期
		calendar.add(Calendar.DATE, diff);
		String Date = df.format(calendar.getTime());
		if (theaterId == null || theaterId.length() <= 0) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
		// try {
		String name = theaterId + "hisList";
		List hisList = null;
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TICKET);
		hisList = ticketSystemService.historyBoxOffice(theaterId, Date, currentDate);
		JSONObject todayJSON = service.getRealTimePFIncome(theaterId, currentDate);
		json.put("PFBaseInfo", getComprehensive(theaterId, currentDate));
		DataSourceContextHolder.clearDbType();
		json.put("RealData", todayJSON);
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
			}
		}
		json.put("HistoryBoxOffice", list);
		json.put("validateCode", MessageUtils.SUCCESS_CODE);
		json.put("msg", MessageUtils.SUCCESS_MSG);
		long endTime = System.currentTimeMillis();
		long time = endTime - startTime;
		System.out.println("一共耗时时长为:" + time);
		// } catch (Exception e) {
		// json.put("validateCode", MessageUtils.FAIL_CODE);
		// json.put("msg", MessageUtils.FAIL_MSG);
		// e.printStackTrace();
		// }
		return json;
	}

	@RequestMapping(value = "getSaleInfo")
	@ResponseBody
	public JSONObject getSaleInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject json = new JSONObject();
		String theaterId = request.getParameter("theaterId");
		int length = Integer.parseInt(request.getParameter("length").toString());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = df.format(new Date());
		Date date = new Date();
		ArrayList<Object> list = new ArrayList<Object>();
		int diff = -length + 1;
		Calendar calendar = Calendar.getInstance();// 日历对象
		calendar.setTime(date);// 设置当前日期
		calendar.add(Calendar.DATE, diff);
		String Date = df.format(calendar.getTime());
		if (theaterId == null || theaterId.length() <= 0) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
		// try {
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TICKET);
		List hisMpList = ticketSystemService.historyMp(theaterId, Date, currentDate);
		JSONObject todayJSON = service.getRealTimeMPIncome(theaterId, currentDate);
		System.out.println("todayJSON=" + todayJSON);
		json.put("PFBaseInfo", getComprehensive(theaterId, currentDate));
		DataSourceContextHolder.clearDbType();
		json.put("RealData", todayJSON);
		DataSourceContextHolder.clearDbType();
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
				list.add(map1);
			}
			json.put("HistoryMP", list);
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_MSG);
		} else {
			json.put("HistoryMP", list);
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_MSG);
		}
		// } catch (Exception e) {
		// json.put("validateCode", MessageUtils.FAIL_CODE);
		// json.put("msg", MessageUtils.FAIL_MSG);
		// e.printStackTrace();
		// }
		return json;
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
					json.put("payRate", TictketSystemController.mul(payRate, 100) + "%");
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
}
