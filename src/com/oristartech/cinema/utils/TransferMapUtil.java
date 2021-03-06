package com.oristartech.cinema.utils;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class TransferMapUtil {
    public static Map<String,Object> transfer(HttpServletRequest request) {
    	try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Map<String,Object> map = new HashMap<String,Object>();
    	Enumeration<String> en = request.getParameterNames();
    	while(en.hasMoreElements()){
    		String key = en.nextElement();
    		String value = request.getParameter(key);
    		map.put(key, value);
    	}
    	return map;
    }
}
