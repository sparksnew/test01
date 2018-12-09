package com.oristartech.cinema.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	//获取当前时期格式化yyyy-MM-dd HH:mm:ss
    public static String getCurrentDate(){
    	SimpleDateFormat sdft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	return sdft.format(new Date());
    }
    
    //将字符串转化为Date
    public static Date parseStrToDate(String str) throws ParseException{
    	SimpleDateFormat sdft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    return sdft.parse(str);
    }
    
    //将Date转化为字符串
    public static String parseDateToStr(Date date){
    	SimpleDateFormat sdft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	return sdft.format(date);
    }
    
    //获取当前时间再加上48小时的时间字符串
    public static String later48HoursTime(String date,int timeLong) throws ParseException{
    	Date currentDate = parseStrToDate(date);
    	Date later48Date = new Date(currentDate.getTime()+(timeLong*60*60*1000));
    	return parseDateToStr(later48Date);
    }
    
    //获取30天前的日期
    public static String laterThirtyDay(){
    	SimpleDateFormat sdft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Calendar theCa = Calendar.getInstance();
    	theCa.setTime(new Date());
    	theCa.add(theCa.DATE,-30);//最后一个数字30可改，30天的意思
    	Date start = theCa.getTime();
    	String startDate = sdft.format(start);//三十天之前日期
    	return startDate.substring(0, 10);
    }
}
