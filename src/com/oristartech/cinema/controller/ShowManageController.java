package com.oristartech.cinema.controller;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oristartech.cinema.service.AdviceService;
import com.oristartech.cinema.service.CinemaInfoService;
import com.oristartech.cinema.utils.DataSourceContextHolder;
import com.oristartech.cinema.utils.HttpClientUtil;
import com.oristartech.cinema.utils.MessageUtils;
import com.oristartech.cinema.utils.ParamLinked;
import com.oristartech.cinema.utils.SSLClient;
import com.oristartech.cinema.utils.SmartAgentAdress;

import net.sf.json.JSONObject;



@RequestMapping(value ="/showManage" ,produces ="application/json;charset=utf-8" )
@Controller
public class ShowManageController {
	@Autowired
	private CinemaInfoService cinemaInfoService;
	//1.所有的影厅状态信息
	@RequestMapping(value = "/AllHallStatus" ,produces="application/json;charset=utf-8")
	@ResponseBody
	public String AllHallStatus(String theaterCode) throws IOException {
		String res = HttpClientUtil.tmsGET(SmartAgentAdress.AllHallStatus,theaterCode);
		return res;
	}
	//2.影院各影厅当前放映信息
	@RequestMapping(value = "/AllShowInfo",produces="application/json;charset=utf-8")
	@ResponseBody
	public String AllShowInfo(String theaterCode,String hallID) throws IOException {
		Map<String, String> params= new HashMap<String, String>();
		params.put("theaterCode", theaterCode);
		params.put("hallID", hallID);
		String res = HttpClientUtil.tmsGETs(SmartAgentAdress.AllShowInfo,params);
		return res;
	}
	//3.影院放映监控警告信息
	@RequestMapping(value = "/AlarmLog" ,produces="application/json;charset=utf-8")
	@ResponseBody
	public String AlarmLog(String theaterCode,String hallID,String endDate,String IsAll) throws IOException {
		Map<String, String> params= new HashMap<String, String>();
		params.put("theaterCode", theaterCode);
		params.put("hallID", hallID);
		params.put("endDate", endDate);
		params.put("isAll", IsAll);
		String res = HttpClientUtil.tmsGETs(SmartAgentAdress.AlarmLog,params);
		return res;
	}
	//4.影厅当天排期信息
	@RequestMapping(value = "/HallShowInfo" ,produces="application/json;charset=utf-8")
	@ResponseBody
	public String HallShowInfo(String theaterCode,String hallID) throws IOException {
		String res = HttpClientUtil.tmsGET(SmartAgentAdress.HallShowInfo,theaterCode,hallID);
		return res;
	}
	//5.获取指定影厅下一场放映信息
	@RequestMapping(value = "/NextShowInfo" ,produces="application/json;charset=utf-8")
	@ResponseBody
	public String NextShowInfo(String theaterCode,String hallID) throws IOException {
		Map<String, String> params= new HashMap<String, String>();
		params.put("theaterCode", theaterCode);
		params.put("hallID", hallID);
		String res = HttpClientUtil.tmsGETs(SmartAgentAdress.NextShowInfo,params);
		return res;
	}
	//6.获取指定影厅的当前放映信息
	@RequestMapping(value = "/CurrentShowInfo",produces="application/json;charset=utf-8")
	@ResponseBody
	public String CurrentShowInfo(String theaterCode,String hallID) throws IOException {
		String res = HttpClientUtil.tmsGET(SmartAgentAdress.CurrentShowInfo,theaterCode,hallID);
		return res;
	}
	//7.查询SPL的详细信息
	@RequestMapping(value = "/SplDetailInfo",produces="application/json;charset=utf-8")
	@ResponseBody
	public String SplDetailInfo(String theaterCode,String splID) throws IOException {
		String res = HttpClientUtil.tmsGET(SmartAgentAdress.SplDetailInfo,theaterCode,splID);
		return res;
	}
	//8.影厅设备状态信息
	@RequestMapping(value = "/HallDevice",produces="application/json;charset=utf-8")
	@ResponseBody
	public String HallDevice(String theaterCode,String hallID) throws IOException {
		String res = HttpClientUtil.tmsGET(SmartAgentAdress.HallDevice,theaterCode,hallID);
		return res;
	}
	//9.获取影厅放映服务上的磁盘容量 
	@RequestMapping(value = "/PlayDiskInfo",produces="application/json;charset=utf-8")
	@ResponseBody
	public String PlayDiskInfo(String theaterCode,String hallID) throws IOException {
		Map<String, String> params= new HashMap<String, String>();
		params.put("theaterCode", theaterCode);
		params.put("hallID", hallID);
		String res = HttpClientUtil.tmsGETs(SmartAgentAdress.PlayDiskInfo,params);
		return res;
	}
	//10.影片导入信息 
	@RequestMapping(value = "/ImportInfo",produces="application/json;charset=utf-8")
	@ResponseBody
	public String ImportInfo(String theaterCode) throws IOException {
		String res = HttpClientUtil.tmsGET(SmartAgentAdress.ImportInfo,theaterCode);
		return res;
	}
	//11.影片分发信息 
	@RequestMapping(value = "/SendInfo",produces="application/json;charset=utf-8")
	@ResponseBody
	public String SendInfo(String theaterCode,String hallID) throws IOException {
		Map<String, String> params= new HashMap<String, String>();
		params.put("theaterCode", theaterCode);
		params.put("hallID", hallID);
		String res = HttpClientUtil.tmsGETs(SmartAgentAdress.SendInfo,params);
		return res;
	}
	//12.影院内容信息管理 
	@RequestMapping(value = "/FilmList",produces="application/json;charset=utf-8")
	@ResponseBody
	public String FilmList(String theaterCode,String type,String search,String pageSize,String page) throws IOException {
		
		Map<String, String> params= new HashMap<String, String>();
		params.put("theaterCode", theaterCode);
		params.put("type", type);
		params.put("search", search);
		params.put("pageSize", pageSize);
		params.put("page", page);
		String res = HttpClientUtil.tmsGETs(SmartAgentAdress.FilmList,params);
		return res;
	}
	//13.获取指定影片的密钥信息 
		@RequestMapping(value = "/FilmKeyInfo",produces="application/json;charset=utf-8")
		@ResponseBody
		public String FilmKeyInfo(String theaterCode,String cplUuid) throws IOException {
			String res = HttpClientUtil.tmsGET(SmartAgentAdress.FilmKeyInfo,theaterCode,cplUuid);
			return res;
		}
	//14.获取指定影片在哪些厅存在
		@RequestMapping(value = "/FilmHallInfo",produces="application/json;charset=utf-8")
		@ResponseBody
		public String FilmHallInfo(String theaterCode,String cplUuid) throws IOException {
			String res = HttpClientUtil.tmsGET(SmartAgentAdress.FilmHallInfo,theaterCode,cplUuid);
			return res;
		}
		//15.获取影厅座位售出状态
		@RequestMapping(value = "/QueryPlanSeat",produces="application/json;charset=utf-8")
		@ResponseBody
		public JSONObject QueryPlanSeat(String theaterCode,String planTime,String hallID) throws IOException, ParseException {
			JSONObject json = new JSONObject();
			List planSeat =null;
			try{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				long timeMsec = sdf.parse(planTime).getTime();
				long startMsec = timeMsec-30*60*1000;
				long endMsec = timeMsec+30*60*1000;
				Date dateStart = new Date(startMsec);
				String startTime = sdf.format(dateStart);
				Date dateEnd = new Date(endMsec);
				String endTime = sdf.format(dateEnd);
				DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TICKET);
				planSeat = cinemaInfoService.queryPlanSeat(theaterCode,startTime,endTime,hallID);
				Map planSeatMap= (Map) planSeat.get(0);
				DataSourceContextHolder.clearDbType();
				json.put("personNum", planSeatMap.get("PERSON_NUM"));
				json.put("personNumReject", planSeatMap.get("PERSON_NUM_REJECT"));
				json.put("totalSeatValid", planSeatMap.get("TOTAL_SEAT_VALID"));
				json.put("totalSeatInvalid", planSeatMap.get("TOTAL_SEAT_INVALID"));
				json.put("validateCode", MessageUtils.SUCCESS_CODE);
				json.put("msg", MessageUtils.SUCCESS_SENDMESSAGE);
			}catch(Exception e){
				json.put("validateCode", MessageUtils.FAIL_CODE);
				json.put("msg", MessageUtils.FAIL_MSG);
			}
			return json;
			
			
		}
}

