package com.oristartech.cinema.utils;

public class ParamLinked {
	@SuppressWarnings("unchecked")
    public static String link(String... key) {
		String param = "";
        for (String string : key) {
			if(string!=""){
				param = param+string+"/";
			}
		}
        param = (String) param.substring(0, param.lastIndexOf("/"));
		return param;
    }
}
