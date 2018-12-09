package com.oristartech.cinema.utils;

public class DataSourceContextHolder {
	 public static final String DATA_SOURCE_CINEMA ="cinema";
	 public static final String DATA_SOURCE_TECHEXCEL="techexcel";
	 public static final String DATA_SOURCE_TICKET="ticket";
     private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();
     
     public static void setDbType(String dbType){
    	 contextHolder.set(dbType);
     }
     public static String getDbType(){
    	 return (String)contextHolder.get();
     }
     public static void clearDbType(){
    	 contextHolder.remove();
     }
}
