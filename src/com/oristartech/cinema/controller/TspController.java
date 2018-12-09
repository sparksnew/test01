package com.oristartech.cinema.controller;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oristartech.cinema.utils.HttpClientUtil;
import com.oristartech.cinema.utils.ParamLinked;
import com.oristartech.cinema.utils.SSLClient;
import com.oristartech.cinema.utils.SmartAgentAdress;

import net.sf.json.JSONObject;



@RequestMapping(value ="/TspCinema" ,produces ="application/json;charset=utf-8" )
@Controller
public class TspController {
	//1.
	@RequestMapping(value = "/QueryPlanInfo" ,produces="application/json;charset=utf-8")
	@ResponseBody
	public String AllHallStatus(String theaterCode) throws IOException {
		String res = HttpClientUtil.tmsGET(SmartAgentAdress.AllHallStatus,theaterCode);
		return res;
	}
}

