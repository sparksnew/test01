package com.oristartech.cinema.controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.oristartech.cinema.service.SyncDataService;
import com.oristartech.cinema.utils.DataSourceContextHolder;
@RequestMapping("/syncdata")
@Controller
public class SyncDataController {
	@Autowired
	private SyncDataService syncDataService;
	private static String LastUpdateTime = null;
	
	/**
	 * 同步客户信息
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	@RequestMapping(value ="/syncCustomData")
	@ResponseBody
	public void syncCustomData() throws SQLException, ClassNotFoundException {
		GetLastUpdateTime("customers");
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TECHEXCEL);
		List list = syncDataService.searchCustInfo(LastUpdateTime);
		DataSourceContextHolder.clearDbType();
		String temp_param = "--";
		if(null !=list && list.size()>0){
			for(int i=0;i<list.size();i++){
				JSONObject json = new JSONObject();
				Map map = (Map) list.get(i);
				String CustomerID = map.get("CustomerID").toString();
				int CustomerID1 = Integer.valueOf(CustomerID);
				json.put("CustomerID", CustomerID1);
				// 客户名称
				String CompanyName = (String) map.get("CompanyName");
				if (CompanyName != null && !"".equals(CompanyName)) {
					json.put("CompanyName", CompanyName);
				} else {
					json.put("CompanyName", temp_param);
				}
				// 客户编码
				String CustomerNo = (String) map.get("customerno");
				if (CustomerNo != null && !"".equals(CustomerNo)) {
					json.put("CustomerNo", CustomerNo);
				} else {
					json.put("CustomerNo", temp_param);
				}
				// 创建时间
				SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date DOBC = (Date) map.get("DOBC");
				if (DOBC != null && !"".equals(DOBC)) {
					String DOBC1 = formate.format(DOBC);
					json.put("DOBC", DOBC1);
				} else {
					json.put("DOBC", temp_param);
				}
				// 父级ID
				if (map.get("ParentCompanyID") != null && !"".equals(map.get("ParentCompanyID"))) {
					json.put("ParentCompanyID", map.get("ParentCompanyID"));
				} else {
					json.put("ParentCompanyID", temp_param);
				}
				// 级别
				String fieldInteger2 = map.get("fieldInteger4").toString();
				if (fieldInteger2 != null && !"".equals(fieldInteger2) && fieldInteger2 != "null") {
					json.put("fieldInteger4", fieldInteger2);
				} else {
					json.put("fieldInteger4", temp_param);
				}
				// 简称
				Object fieldInteger8 = map.get("fieldInteger8");
				if(fieldInteger8 != null && !"".equals(fieldInteger8) && fieldInteger8 != "null"){
					String fieldInteger3 = fieldInteger8.toString();
					json.put("fieldInteger8", fieldInteger3);
				}else {
					json.put("fieldInteger8", temp_param);
				}
						
				/*if (fieldInteger3 != null && !"".equals(fieldInteger3) && fieldInteger3 != "null") {
				} else {
					json.put("fieldInteger8", temp_param);
				}*/
				// 联系人
				if (map.get("FieldShortText6") != null && !"".equals(map.get("FieldShortText6")) && map.get("FieldShortText6") != "null") {
					json.put("FieldShortText6", map.get("FieldShortText6"));
				} else {
					json.put("FieldShortText6", temp_param);
				}// 联系人电话
				if (map.get("FieldShortText7") != null && !"".equals(map.get("FieldShortText7")) && map.get("FieldShortText7") != "null") {
					json.put("FieldShortText7", map.get("FieldShortText7"));
				} else {
					json.put("FieldShortText7", temp_param);
				}
				// 联系人地址
				if (map.get("FieldShortText5") != null && !"".equals(map.get("FieldShortText5")) && map.get("FieldShortText5") != "null") {
					json.put("FieldShortText5", map.get("FieldShortText5"));
				} else {
					json.put("FieldShortText5", temp_param);
				}
				
				DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
				List custIdList = syncDataService.searchCustomerID(CustomerID1);
				DataSourceContextHolder.clearDbType();
				if (custIdList!= null&&custIdList.size()>0) {
					UpdateCustom(json, "Update");
				} else {
					UpdateCustom(json, "Insert");
				}
			}
		}
	}
	
	/**
	 * 同步影院信息
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	@RequestMapping(value ="/syncTheaterData")
	@ResponseBody
	public void syncTheaterData() throws SQLException, ClassNotFoundException {
		GetLastUpdateTime("cinema");
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TECHEXCEL);
		List list = syncDataService.getSyncTheaterData(LastUpdateTime);
		DataSourceContextHolder.clearDbType();
		String temp_param = "--";
		if(null !=list && list.size()>0){
			for(int i=0;i<list.size();i++){
				Map map = (Map) list.get(i);
				JSONObject json = new JSONObject();
				String CustomerID = map.get("CustomerID").toString();
				int CustomerID1 = Integer.valueOf(CustomerID);
				json.put("CustomerID", CustomerID1);
				String CompanyName = (String) map.get("CompanyName");
				if (CompanyName != null && !"".equals(CompanyName)) {
					json.put("CompanyName", CompanyName);
				} else {
					json.put("CompanyName", temp_param);
				}
				
				// 客户编码
				String CustomerNo = (String) map.get("customerno");
				if (CustomerNo != null && !"".equals(CustomerNo)) {
					json.put("CustomerNo", CustomerNo);
				} else {
					json.put("CustomerNo", temp_param);
				}
				// 创建时间
				SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date DOBC = (Date) map.get("DOBC");
				if (DOBC != null && !"".equals(DOBC)) {
					String DOBC1 = formate.format(DOBC);
					json.put("DOBC", DOBC1);
				} else {
					json.put("DOBC", temp_param);
				}
				if (map.get("parentcustomerid") != null && !"".equals(map.get("parentcustomerid"))) {
					json.put("ParentCompanyID", map.get("parentcustomerid").toString());
				} else {
					json.put("ParentCompanyID", temp_param);
				}
				// 电话
				String Phone = (String) map.get("Phone");
				if (Phone != null && !"".equals(Phone)) {
					json.put("Phone", Phone);
				} else {
					json.put("Phone", temp_param);
				}
				//
				Object fieldInteger2 = map.get("fieldInteger2");
				if (fieldInteger2 != null && !"".equals(fieldInteger2) && fieldInteger2 != "null") {
					String field2 = fieldInteger2.toString();
					json.put("fieldInteger2", field2);
				} else {
					json.put("fieldInteger2", temp_param);
				}
				//
				Object fieldInteger3 = map.get("fieldInteger3");
				if (fieldInteger3 != null && !"".equals(fieldInteger3) && fieldInteger3 != "null") {
					String field3 = fieldInteger3.toString();
					json.put("fieldInteger3", field3);
				} else {
					json.put("fieldInteger3", temp_param);
				}
				//
				Date fieldDate1 = (Date) map.get("fieldDate1");
				System.out.println("fieldDate1:" + fieldDate1);
				if (fieldDate1 != null && !"".equals(fieldDate1)) {
					String fieldDate11 = formate.format(fieldDate1);
					json.put("fieldDate1", fieldDate11);
				} else {
					json.put("fieldDate1", temp_param);
				}
				DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
				List custIdList = syncDataService.getCinemaID(CustomerID1);
				DataSourceContextHolder.clearDbType();
				if(null !=custIdList && custIdList.size()>0){
					UpdateTheater(json, "Update");
				}else{
					UpdateTheater(json, "Insert");
				}
			}
		}
	}
	
	/**
	 * 获取最后一次更新时间
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	@RequestMapping(value = "/getLastUpdateTime")
	@ResponseBody
	public String GetLastUpdateTime(String tableName) throws SQLException, ClassNotFoundException {
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
		List list = syncDataService.searchLastSync(tableName);
		DataSourceContextHolder.clearDbType();
		SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDate = formate.format(new Date());
		if(null != list && list.size() > 0){
			for(int i=0;i<list.size();i++){
				Map map = (Map) list.get(i);
				Date LastUpdateTime1 = (Date) map.get("lastSync");
				LastUpdateTime = formate.format(LastUpdateTime1);
				DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
				syncDataService.updateDataSync(tableName,currentDate);
				DataSourceContextHolder.clearDbType();
			}
		}else{
			LastUpdateTime = currentDate;
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
			syncDataService.insertDataSync(tableName,currentDate);
			DataSourceContextHolder.clearDbType();
		}
		return LastUpdateTime;
	}

	@RequestMapping(value ="/updateCustom")
	@ResponseBody
	public void UpdateCustom(JSONObject json, String Operation) {
		int temp_param = 0;
		if (Operation.equals("Update")) {
			Map<String,Object> custMap = new HashMap<String,Object>();
			custMap.put("CompanyName", json.getString("CompanyName"));
			custMap.put("fieldInteger8", json.getString("fieldInteger8"));
			Integer fieldInteger4 = (Integer) json.getInteger("fieldInteger4");
			if (fieldInteger4.equals("--")) {
				custMap.put("fieldInteger4", temp_param);
			} else {
				custMap.put("fieldInteger4", fieldInteger4);
			}
			custMap.put("FieldShortText6", json.getString("FieldShortText6"));
			custMap.put("FieldShortText7", json.getString("FieldShortText7"));
			custMap.put("FieldShortText5", json.getString("FieldShortText5"));
			custMap.put("CustomerID", json.getString("CustomerID"));
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
			syncDataService.updateCustInfo(custMap);
			DataSourceContextHolder.clearDbType();
			
		} else if (Operation.equals("Insert")) {
				Map<String,Object> custMap1 = new HashMap<String,Object>();
				custMap1.put("CustomerID", json.getString("CustomerID"));
				custMap1.put("CompanyName", json.getString("CompanyName"));
				custMap1.put("fieldInteger8", json.getString("fieldInteger8"));
				Integer fieldInteger4 = (Integer) json.getInteger("fieldInteger4");
				if (fieldInteger4.equals("--")) {
					custMap1.put("fieldInteger4", temp_param);
				} else {
					custMap1.put("fieldInteger4", fieldInteger4);
				}
				custMap1.put("FieldShortText6", json.getString("FieldShortText6"));
				custMap1.put("FieldShortText7", json.getString("FieldShortText7"));
				custMap1.put("FieldShortText5", json.getString("FieldShortText5"));
				DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
				syncDataService.insertCustInfo(custMap1);
				DataSourceContextHolder.clearDbType();
		}
	}

	

	public void UpdateTheater(JSONObject json, String Operation) {
		int temp_param = 9999;
		System.out.println("UpdateTheater=" + json.getString("CustomerNo"));
		if (Operation.equals("Update")) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("cinemaName", json.getString("CompanyName"));
			map.put("cinemaCode", json.getString("CustomerNo"));
			map.put("createDateTime", json.getString("DOBC"));
			String ParentCompanyID = json.getString("ParentCompanyID");
			if (ParentCompanyID.equals("--")) {
				map.put("customerID", temp_param);
			} else {
				map.put("customerID", ParentCompanyID);
			}
			map.put("phone", json.getString("Phone"));
			String fieldDate1 = json.getString("fieldDate1");
			if (fieldDate1.equals("--")) {
				map.put("startDate", null);
			} else {
				map.put("startDate", fieldDate1);
			}
			String fieldInteger2 = json.getString("fieldInteger2");
			if (fieldInteger2.equals("--")) {
				map.put("mainChains", temp_param);
			} else {
				map.put("mainChains", fieldInteger2);
			}
			String fieldInteger3=json.getString("fieldInteger3");
			if (fieldInteger3.equals("--")) {
				map.put("businessStatus", temp_param);
			} else {
				map.put("businessStatus", fieldInteger3);
			}
			String CustomerID = json.getString("CustomerID");
			Integer CustomerID1 = Integer.valueOf(CustomerID);
			map.put("CustomerID", CustomerID1);
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
			syncDataService.updateCinemaInfo(map);
			DataSourceContextHolder.clearDbType();
		} else if (Operation.equals("Insert")) {
			Map<String,Object> map = new HashMap<String,Object>();
			String CustomerID = json.getString("CustomerID");
			Integer CustomerID1 = Integer.valueOf(CustomerID);
			map.put("cinemaID", CustomerID1);
			map.put("cinemaName", json.getString("CompanyName"));
			map.put("cinemaCode", json.getString("CustomerNo"));
			map.put("createDateTime", json.getString("DOBC"));
			String ParentCompanyID = json.getString("ParentCompanyID");
			if (ParentCompanyID.equals("--")) {
				map.put("customerID", temp_param);
			} else {
				map.put("customerID", ParentCompanyID);
			}
			map.put("phone", json.getString("Phone"));
			String fieldDate1 = json.getString("fieldDate1");
			if (fieldDate1.equals("--")) {
				map.put("startDate", null);
			} else {
				map.put("startDate", fieldDate1);
			}
			String mainChains = json.getString("fieldInteger2");
			if (mainChains.equals("--")) {
				map.put("mainChains", temp_param);
			} else {
				map.put("mainChains", mainChains);
			}
			String businessStatus=json.getString("fieldInteger3");
			if (businessStatus.equals("--")) {
				map.put("businessStatus", temp_param);
			} else {
				map.put("businessStatus", businessStatus);
			}
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
			syncDataService.insertCinemaInfo(map);
			DataSourceContextHolder.clearDbType();
		}
	}

}
