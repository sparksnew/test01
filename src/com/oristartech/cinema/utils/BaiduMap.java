package com.oristartech.cinema.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class BaiduMap {
	public static String getAddressByLg(String lat, String lng) {
		String addr = "";
		String url = String.format("http://api.map.baidu.com/geocoder/v2/?ak=OuVX63Iqo8Zd1fLt5kmw3mKr&location="
				+ lat + "," + lng
				+ "&output=json");
		URL myURL = null;
		URLConnection httpsConn = null;
		try {
			myURL = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
		try {
			httpsConn = (URLConnection) myURL.openConnection();
			if (httpsConn != null) {
				InputStreamReader insr = new InputStreamReader(httpsConn.getInputStream(), "UTF-8");
				BufferedReader br = new BufferedReader(insr);
				String data = null;
				if ((data = br.readLine()) != null) {
					// //System.out.println(data);
					addr = data;
				}
				insr.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		System.out.println("addr=" + addr);
		String res = "";
		try {
			Map mapTypes = JSON.parseObject(addr);
			// //System.out.println("这个是用JSON类的parseObject来解析JSON字符串!!!");
			// //System.out.println(mapTypes);
			Map mapTypes1 = JSON.parseObject(mapTypes.get("result").toString());
			// //System.out.println(mapTypes1.get("formatted_address"));
			res = (String) mapTypes1.get("formatted_address");
		} catch (Exception e) {
			res = "";
		}
		return res;
	}

	public static void main(String[] args) {
		String addr = getAddressByLg("31.71099194", "120.4019789");// (38.9146943,121.612382);
		System.out.println(addr);
	}
}
