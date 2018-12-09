package com.oristartech.cinema.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oristartech.cinema.service.CinemaDeviceInfoService;
import com.oristartech.cinema.service.TroubleListService;
import com.oristartech.cinema.utils.DataSourceContextHolder;

import net.sf.json.JSONObject;

@RequestMapping("/appDevice/")
@Controller
public class AppDeviceController {
	@Autowired
	private CinemaDeviceInfoService cinemaDeviceInfoService;
	@Autowired
	private TroubleListService troubleListService;
	//获得影院设备列表
	@RequestMapping(value="equipmentList")
	@ResponseBody
	public JSONObject getEquipmentList(String theaterId) {
		JSONObject json = new JSONObject();
		JSONObject json1 = new JSONObject();
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TECHEXCEL);
		List list = cinemaDeviceInfoService.getDeviceListByCustomerID(theaterId);
		DataSourceContextHolder.clearDbType();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Map map = (Map) list.get(i);
				try {
					json1.put("ProjectID", map.get("ProjectID"));
					json1.put("ItemID", map.get("ItemID"));
					json1.put("Name", map.get("Name"));
					json1.put("AssetNo", map.get("AssetNo"));
					json1.put("Description", map.get("Description"));
					json1.put("Field1", map.get("Field1"));
					Date WarranteeFrom = (Date) map.get("WarranteeFrom");
					json1.put("WarranteeFrom", WarranteeFrom.toString());
					Date WarranteeTo = (Date) map.get("WarranteeTo");
					json1.put("WarranteeTo", WarranteeTo.toString());
					json1.put("WarranteedBy", map.get("WarranteedBy"));
					json1.put("CustomerID", map.get("CustomerID"));
					json1.put("CompanyName", map.get("CompanyName"));
					json1.put("status", "1");
					json1.put("msg", "成功");
					json.put("logs", json1);
				} catch (Exception e) {
					json1.put("status", "0");
					json1.put("msg", "失败");
					json.put("logs", json1);
				}
			}
		}else{
			json1.put("status", "2");
			json1.put("msg", "没有相关数据");
			json.put("logs", json1);
		}
		return json;
	}
	//设备故障列表
	@RequestMapping(value="troubleList")
	@ResponseBody
	public JSONObject getTroubleListByDevice(String theaterId) {
		JSONObject json = new JSONObject();
		JSONObject json1 = new JSONObject();
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TECHEXCEL);
		List list = troubleListService.queryTroubleListByCustomerID(theaterId);
		DataSourceContextHolder.clearDbType();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Map map = (Map) list.get(i);
				try {
					json1.put("CUSTOMERID", map.get("CUSTOMERID"));
					json1.put("TRANSITIONNAME", map.get("TRANSITIONNAME"));
					json1.put("BUGDESCRIPTION", map.get("BUGDESCRIPTION"));
					json1.put("PROJECTID", map.get("PROJECTID"));
					json1.put("BUGID", map.get("BUGID"));
					json1.put("DATEASSIGNED", map.get("DATEASSIGNED"));
					json1.put("BUGTITLE", map.get("BUGTITLE"));
					json1.put("PROGRESSSTATUSNAME", map.get("PROGRESSSTATUSNAME"));
					json1.put("status", "1");
					json1.put("msg", "成功");
					json.put("logs_wc", json1);
				} catch (Exception e) {
					json1.put("status", "0");
					json1.put("msg", "失败");
					json.put("logs_wc", json1);
				}
			}
		}else{
			json1.put("status", "2");
			json1.put("msg", "没有相关数据");
			json.put("logs_wc", json1);
		}
		return json;
	}
}