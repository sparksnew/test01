package com.oristartech.cinema.Const;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.web.util.UriTemplate;

public class test {
	public static void main(String[] args) throws URISyntaxException {
		Map<String, String> param = new HashMap<String, String>();
		param.put("abc", "\0");
		param.put("def", "\0");
		param.put("fgd", "\0");
		URIBuilder builder = new URIBuilder("www.baidu.com");
		if (param != null) {
			for (String key : param.keySet()) {
				builder.addParameter(key, param.get(key));
			}
		}
		URI uri = builder.build();
		System.out.println(uri);
		
	}
}
