package com.oristartech.cinema.controller;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.servlet.ServletContextEvent;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jhlabs.image.GaussianFilter;
import com.oristartech.cinema.service.FilmInfoService;
//import com.oristartech.cinema.utils.ApplicationContextUtil;
import com.oristartech.cinema.utils.DataSourceContextHolder;
//import com.oristartech.cinema.utils.RedisUtil;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.JPEGEncodeParam;
import com.sun.media.jai.codec.PNGDecodeParam;

import net.sf.json.JSONObject;

public class ContextLoaderListenerOverWrite3 extends ContextLoaderListener {    
    private FilmInfoService filmInfoService;  
    
    @Override    
    /**  
     * @description 重写ContextLoaderListener的contextInitialized方法  
     */    
    public void contextInitialized(ServletContextEvent event) {    
        super.contextInitialized(event);    
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext()); 
        
        //获取bean    
        filmInfoService = (FilmInfoService) applicationContext.getBean("filmInfoService");     
     //********************具体地业务代码*******************  
     // 全国累计票房、今日票房占比、首周票房
		final String boxOfficeUrl = "http://www.cbooo.cn/BoxOffice/GetHourBoxOffice";
		final String boxOfficeUrl1 = "http://dianying.nuomi.com/movie/boxrefresh";
		//下载最新图片
		final String ImgUrl = "http://theater.mtime.com/China_Beijing/";
		final String ImgUrl1 = "http://www.komovie.cn/movie";
		final String ImgUrl2 = "http://www.komovie.cn/movie/coming";
		final String ImgUrl3 = "https://movie.douban.com/cinema/nowplaying/beijing/";
		// String weekUrl =
		// "http://www.cbooo.cn/boxOffice/GetYearInfo_f?year=2017";
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 7); // 控制时
		calendar.set(Calendar.MINUTE, 0); // 控制分
		calendar.set(Calendar.SECOND, 0); // 控制秒
		Date time = calendar.getTime(); // 得出执行任务的时间,此处为今天的12：00：00
		Timer timer = new Timer();
		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(Calendar.HOUR_OF_DAY, 7); // 控制时
		calendar1.set(Calendar.MINUTE, 10); // 控制分
		calendar1.set(Calendar.SECOND, 0); // 控制秒
		Date time1 = calendar1.getTime(); // 得出执行任务的时间,此处为今天的12：00：00
		Timer timer1 = new Timer();
		
		Calendar calendar2 = Calendar.getInstance();
		calendar2.set(Calendar.HOUR_OF_DAY, 6); // 控制时
		calendar2.set(Calendar.MINUTE, 50); // 控制分
		calendar2.set(Calendar.SECOND, 0); // 控制秒
		Date time2 = calendar2.getTime(); // 得出执行任务的时间,此处为今天的12：00：00
		Timer timer2 = new Timer();
		try{
			//获取请求连接
			Connection con = Jsoup.connect(boxOfficeUrl1).ignoreContentType(true);
			//遍历生成参数
	        Document doc = con.post();
	      //获取请求连接
			Connection con2 = Jsoup.connect(ImgUrl).ignoreContentType(true);
			//遍历生成参数
	        Document doc2 = con.post();
			timer.scheduleAtFixedRate(new TimerTask() {
				public void run() {
					int kk = 0;
					try {
						httpPost1(boxOfficeUrl1, kk);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, time, 1000 * 60 * 60 * 24);// 这里设定将延时每天固定执行
			timer1.scheduleAtFixedRate(new TimerTask() {
				public void run() {
					int kk = 1;
					try {
						httpPost1(boxOfficeUrl1, kk);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, time1, 1000 * 60 * 60 *1);// 这里设定将延时每天固定执行
			timer2.scheduleAtFixedRate(new TimerTask() {
				public void run() {
					try {
						FetchImgs(ImgUrl);
						FetchImgs(ImgUrl1);
						FetchImgs(ImgUrl2);
						FetchImgs(ImgUrl3);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, time2, 1000 * 60 * 60 *1);// 这里设定将延时每天固定执行
		}catch(Exception e){
			try{
				//获取请求连接
				Connection con = Jsoup.connect(boxOfficeUrl).ignoreContentType(true);
				//遍历生成参数
		        Document doc = con.post();
				
				timer.scheduleAtFixedRate(new TimerTask() {
					public void run() {
						int kk = 0;
						try {
							httpPost(boxOfficeUrl, kk);
							FetchImgs(ImgUrl);
							FetchImgs(ImgUrl1);
							FetchImgs(ImgUrl2);
							FetchImgs(ImgUrl3);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}, time, 1000 * 60 * 60 * 24);// 这里设定将延时每天固定执行
				timer.scheduleAtFixedRate(new TimerTask() {
					public void run() {
						int kk = 1;
						try {
							httpPost(boxOfficeUrl, kk);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}, time, 1000 * 60 * 60 *1);// 这里设定将延时每天固定执行
			}catch(Exception e1){
				
			}
			
		}
    }
    public void httpPost(String Url, int kk) throws IOException {
		// 获取请求连接
		Connection con = Jsoup.connect(Url).ignoreContentType(true);
		Document doc = con.post();
		Element body = doc.body();
		String body1 = body.toString();
		String body2 = null;
		String body4 = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String current = sdf.format(new Date());
		body2 = body1.substring(body1.indexOf("Irank") - 2, body1.lastIndexOf("]"));
		body4 = body1.substring(body1.indexOf("[") + 1, body1.indexOf("]"));
		JSONObject boxOfficeJson = JSONObject.fromObject(body4);
		String sumBox = (String) boxOfficeJson.get("sumBoxOffice");
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
		if (kk == 0) {
			Integer recordNum = filmInfoService.queryTotalNum(current);
			if(recordNum==0){
				filmInfoService.insertTotalInfo(sumBox, current);
			}else{
				filmInfoService.updateTotalInfo(sumBox, current);
			}
		} else {
			filmInfoService.updateTotalInfo(sumBox, current);
		}
		DataSourceContextHolder.clearDbType();
		String[] body3 = body2.split("},");
		int j = 0;
		// 大盘信息
		for (int i = 0; i < body3.length; i++) {
			body3[i] = body3[i] + "}";
			j++;
			JSONObject json = JSONObject.fromObject(body3[i]);
			String movieName = json.getString("MovieName");
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TICKET);
			List movieCode1 =null;
			if(null!=movieName&&""!=movieName){
				movieCode1 = filmInfoService.getMovieCode(movieName);
			}
			String movieCode = null;
			if (null != movieCode1 && movieCode1.size() > 0) {
				Map map = (Map) movieCode1.get(0);
				String movieCode2 = (String) map.get("movieCode");
				movieCode = movieCode2.substring(movieCode2.length() - 8, movieCode2.length());
			}
			DataSourceContextHolder.clearDbType();
			String realBox = json.getString("BoxOffice");
			String totalBox = json.getString("sumBoxOffice");
			String showDays = json.getString("movieDay");
			String boxRate = json.getString("boxPer");
			String mId = json.getString("mId");
			String firstWeekBox = "0";
			if (movieCode != null) {
				DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
				if (kk == 0) {
					Integer boxNum =  filmInfoService.queryBoxNum(movieCode,current);
					if(boxNum==0){
						filmInfoService.insertFilmInfo(movieName, movieCode, realBox, totalBox, showDays, boxRate, current,
								mId,firstWeekBox);
					}else{
						filmInfoService.updateFilmInfo(movieCode, realBox, totalBox, showDays, boxRate, current,mId);
					}
				} else {
					filmInfoService.updateFilmInfo(movieCode, realBox, totalBox, showDays, boxRate, current,mId);
				}
				DataSourceContextHolder.clearDbType();
			}
		}
		if(kk==1){
			getWeekBox();
		}
	}

	private void getWeekBox() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String current = sdf.format(new Date());
		/*Calendar calendar = Calendar.getInstance();//此时打印它获取的是系统当前时间
	    calendar.add(Calendar.DATE, -1);    //得到前一天
	    String  yesterdayDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());*/
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
		List befInfo = filmInfoService.getWeekBoxInfo();
		List list = filmInfoService.getMovieMidList(current);
		DataSourceContextHolder.clearDbType();
		if(null!=befInfo&&befInfo.size()>0){
			for(int i=0;i<befInfo.size();i++){
				Map befMap = (Map) befInfo.get(i);
				String firstWeekBox = befMap.get("firstWeekBox").toString();
				String MovieCode = befMap.get("MovieCode").toString();
				String mid = befMap.get("mid").toString();
				if(!firstWeekBox.equals("0")){
					filmInfoService.chgWeekBox(MovieCode,firstWeekBox,mid,current);
				}
			}
		}
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map map = (Map) list.get(i);
				String mid = map.get("mid").toString();
				String firstWeekBox = map.get("firstWeekBox").toString();
				String movieCode = map.get("movieCode").toString();
				if(!mid.equals("0")){
					String mUrl = "http://www.cbooo.cn/m/" + mid;
					Connection connect = Jsoup.connect(mUrl);
					try {
						Document document = connect.get();
						Elements tags = document.getElementsByTag("table");
						Elements tags1 = document.getElementsByTag("h4");
						List<String> eachText2 = tags1.eachText();
						String firstWeekBox1="0";
			            for(int kk=0;kk<eachText2.size();kk++){
			            	String boxArea = eachText2.get(kk);
			            	if(!tags.isEmpty()&&boxArea.contains("内地票房")){
								List<String> eachText = tags.eachText();
								String text = eachText.get(0);
								if (text.contains("第1周")) {
									String keyword = text.substring(text.indexOf("第1周"), text.length());
									String[] split = keyword.split(" ");
									firstWeekBox1 = split[2];
									if(!firstWeekBox1.equals("0")){
										DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
										filmInfoService.addMovieInfo(mid, firstWeekBox1, current);
										DataSourceContextHolder.clearDbType();
									}
								}
							}
			            }
						
						if(firstWeekBox1.equals("0")){
							DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
							//Integer firstShowDay = filmInfoService.getFirstShowDay(movieCode);
							//if(firstShowDay<=7||mid.equals("0")){
								filmInfoService.addMovieInfo1(mid, current,movieCode);
							//}
							DataSourceContextHolder.clearDbType();
						}
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else if(firstWeekBox.equals("0")||firstWeekBox.equals("0.0")){
					DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
					//Integer firstShowDay = filmInfoService.getFirstShowDay(movieCode);
					//if(firstShowDay<=7){
						filmInfoService.addMovieInfo1(mid, current,movieCode);
					//}
					DataSourceContextHolder.clearDbType();
					
				}
				
			}
		}
	}

	public void httpPost1(String url, int kk) throws IOException {
//		RedisUtil redisUtil=(RedisUtil)ApplicationContextUtil.getBean("redisUtil");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String current = sdf.format(new Date());
		// 获取请求连接
		Connection con = Jsoup.connect(url).ignoreContentType(true);
		// 遍历生成参数
		Document doc = con.post();
		Element body = doc.body();
		String body1 = body.toString();
		String totalCountryBox = body1.substring(body1.indexOf("boxNum") + 8, body1.indexOf("message") - 2);
		if(!totalCountryBox.isEmpty()&&!totalCountryBox.equals("0")){
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
			if (kk == 0) {
				Integer recordNum = filmInfoService.queryTotalNum(current);
				if(recordNum==0){
					filmInfoService.insertTotalInfo(totalCountryBox, current);
//					redisUtil.set("totalCountryBox", totalCountryBox);
				}else{
					filmInfoService.updateTotalInfo(totalCountryBox, current);
//					redisUtil.set("totalCountryBox", totalCountryBox);
				}
			} else {
				filmInfoService.updateTotalInfo(totalCountryBox, current);
//				redisUtil.set("totalCountryBox", totalCountryBox);
			}
			DataSourceContextHolder.clearDbType();
			String body3 = body1.substring(body1.indexOf("[") + 1, body1.indexOf("]"));
	
			String[] body4 = body3.split("}}},");
			int j = 0;
			String attribute = null;
			List<Map<String, Object>> realBoxList = new ArrayList<>();
			for (int i = 0; i < body4.length; i++) {
				attribute = body4[i] + "}}}";
				j++;
				JSONObject json = JSONObject.fromObject(attribute);
				JSONObject filmInfo = (JSONObject) json.get("attribute");
				String movieName = (String) json.get("movieName");
				DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TICKET);
				List movieCode1 =null;
				if(null!=movieName&&""!=movieName){
					movieCode1 = filmInfoService.getMovieCode(movieName);
				}
				String movieCode = null;
				if (null != movieCode1 && movieCode1.size() > 0) {
					Map map = (Map) movieCode1.get(0);
					String movieCode2 = (String) map.get("movieCode");
					movieCode = movieCode2.substring(movieCode2.length() - 8, movieCode2.length());
				}
				DataSourceContextHolder.clearDbType();
				JSONObject attr1 = (JSONObject) filmInfo.get("1");
	        	String showDays = attr1.get("attrValue").toString();
	        	JSONObject attr2 = (JSONObject) filmInfo.get("2");
	        	String TotalBox=null;
	        	String TotalBox1 = (String) attr2.get("attrValue");
	        	if(TotalBox1.contains("亿")){
	        		String TotalBox2=TotalBox1.substring(0, TotalBox1.indexOf("亿"));
	        		Double TotalBox3 = Double.valueOf(TotalBox2)*10000;
	        		TotalBox = Double.valueOf(TotalBox3).toString();
	        	}else if(TotalBox1.contains("万")){
	        		TotalBox=TotalBox1.substring(0, TotalBox1.indexOf("万"));
	        	}else{
	        		TotalBox=TotalBox1;
	        	}
	        	JSONObject attr3 = (JSONObject) filmInfo.get("3");
	        	String RealBox = null;
				String RealBox1 = (String) attr3.get("attrValue").toString();
				if(RealBox1.contains("亿")){
					String RealBox2=RealBox1.substring(0, RealBox1.indexOf("亿"));
	        		Double RealBox3 = Double.valueOf(RealBox2)*10000;
	        		RealBox = Double.valueOf(RealBox3).toString();
				}else if(RealBox1.contains("万")){
					RealBox = RealBox1.substring(0, RealBox1.indexOf("万"));
				}else{
					RealBox = RealBox1;
				}
	        	JSONObject attr4 = (JSONObject) filmInfo.get("4");
	        	String BoxRate = (String) attr4.get("attrValue").toString();
				String firstWeekBox = "0";
				String mid = "0";
				Map<String, Object> realboxMap = new HashMap<>();
				realboxMap.put("movieName", movieName);
				realboxMap.put("MovieCode", movieCode);
				realboxMap.put("realBox", RealBox);
				realboxMap.put("TotalBox", TotalBox);
				realboxMap.put("ShowDays", showDays);
				realboxMap.put("BoxRate", BoxRate);
				realboxMap.put("StaticDate", current);
				realboxMap.put("mid", mid);
				realboxMap.put("firstWeekBox", firstWeekBox);
				realBoxList.add(realboxMap);
				if (movieCode != null) {
					DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
					if (kk == 0) {
						Integer boxNum =  filmInfoService.queryBoxNum(movieCode,current);
						if(boxNum==0){
							filmInfoService.insertFilmInfo(movieName, movieCode, RealBox, TotalBox, showDays, BoxRate, current,
									mid,firstWeekBox);
//							redisUtil.lSet("realBoxList", realBoxList);
						}else{
							filmInfoService.changeFilmInfo(movieCode, RealBox, TotalBox, showDays, BoxRate, current);
//							redisUtil.lSet("realBoxList", realBoxList);
						}
					} else {
						filmInfoService.changeFilmInfo(movieCode, RealBox, TotalBox, showDays, BoxRate, current);
//						redisUtil.lSet("realBoxList", realBoxList);
					}
					DataSourceContextHolder.clearDbType();
			  }
				if(firstWeekBox.equals("0")){
					DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
					filmInfoService.updateWeekBox(movieCode,current);
					DataSourceContextHolder.clearDbType();
				}
			}
			if(kk==0){
				addMid();
				getWeekBox();
			}
		}
	}
	public void addMid() throws IOException{
		//获取请求连接
		String url="http://www.cbooo.cn/comming";
		Connection con = Jsoup.connect(url).ignoreContentType(true);
		//遍历生成参数
        Document doc = con.post();
        Element body = doc.body();
        Elements a = body.getElementsByTag("a"); 
        for(int i=0;i<a.size();i++){
        	Element aa = a.get(i);
        	String aa1 = aa.toString();
        	if(aa1.contains("_blank")&&aa1.contains("title")){
        		if(aa1.contains("aname")){
	        		continue;
	        	}else{
	        		String mid = aa1.substring(aa1.indexOf("m/")+2, aa1.indexOf("title")-2);
	        		String mvName1 = aa1.substring(aa1.indexOf(">")+1, aa1.indexOf("</a>"));
	        		String mvName=mvName1;
	        		if(mvName1.contains("...")){
	        			mvName = mvName1.trim().substring(0, mvName1.indexOf("..."));
	        		}
	        		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
	            	filmInfoService.addMid(mid, mvName);
	            	DataSourceContextHolder.clearDbType();
	        	}
        	}
        }
	} 
    //-------------------------图片下载方法----------------------------
        private void FetchImgs(String url){
    		// 利用Jsoup获得连接
    		/*
    		 * "http://maoyan.com/films"; 有问题
    		 * "http://www.komovie.cn/movie"; 
    		 * "http://theater.mtime.com/China_Beijing/"; 
    		 * "http://www.komovie.cn/movie/coming"; 
    		 * "https://movie.douban.com/cinema/nowplaying/beijing/";
    		 */
        	String path = Thread.currentThread().getContextClassLoader().getResource(".").getPath();
    		String filepath = path.substring(0, path.lastIndexOf("lib/"))+"img";
    		String filepath1 = path.substring(0, path.lastIndexOf("lib/"))+"img1";
    		//String downloadUrl = "E:/img/";
    		File file = new File(filepath);
    		if(!file.exists()){
    			file.mkdir();
    		}
    		deleteFile(file);
    		int height = 140;
    		int width = 100;
    		String ftype = "jpg";
    		//String url = "http://theater.mtime.com/China_Beijing/";
    		//String url = "http://theater.mtime.com/China_Beijing/";
    		//String url = "https://movie.douban.com/cinema/nowplaying/beijing/";
    		Connection connect = Jsoup.connect(url);
    		try {
    			// 得到Document对象
    			Document document = connect.get();
    			// 查找所有img标签
    			Elements imgs = document.getElementsByTag("img");
    			System.out.println("共检测到下列图片URL：");
    			System.out.println("开始下载");
    			Map<String, String> picInfo = new HashMap<String, String>();
    			Map<Object, Object> pictureInfo = new HashMap<Object, Object>();
    			// 遍历img标签并获得src的属性
    			for (Element element : imgs) {
    				// 获取每个img标签URL "abs:"表示绝对路径
    				String imgSrc = element.attr("abs:data-src");
    				String[] imgUrls = imgSrc.split("@");
    				String imgUrl = imgUrls[0].toString();
    				String alt = element.attr("alt");
    				// 打印URL
    				System.out.println(imgUrl);
    				// 下载图片到本地
    				if (!imgUrl.equals("")) {
    					Map downImages = downImages(filepath, imgUrl, alt);
    					picInfo.putAll(downImages);
    				} else {

    				}
    			   if(url.contains("komovie")){
    			   }else{
    				   String filmName = alt;
    				   if(alt.contains("/")){
    					   filmName = alt.substring(0, alt.indexOf('/'));  
    				   }
    				   String imgSrc1 = element.attr("abs:src"); // 打印URL 
    				   String[] imgUrls1 = imgSrc1.split("@"); 
    				   String imgUrl1 = imgUrls1[0].toString(); 
    				   System.out.println(imgUrl1);
    				   //下载图片到本地 
    				   if(!imgUrl1.equals("")){ 
    					   if(alt.contains("《")||alt.contains("APP购票")){
    						   continue;
    					   }
    					  File[] filelist = file.listFiles();
    					  Boolean flag = true;
    						for (File f : filelist) {
    							String picName = (String) picInfo.get(f.getName());
    							if(picName==null||filmName.equals(picName.trim())){
    								flag=false;
    								break;
    							}
    						}
    						if(flag){
    							Map downImages = downImages(filepath, imgUrl1, filmName);
    							picInfo.putAll(downImages);
    						}
    				   }else{		  
    				    }  
    			   }
    			   String imgFilter ="http://www.komovie.cn/movie/coming";
    				String imgSrc2 = element.attr("abs:data-original");
    				String[] imgUrls2 = imgSrc2.split("@");
    				String imgUrl2 = imgUrls2[0].toString();
    				String alt1 = element.attr("alt");
    				// 打印URL
    				System.out.println(imgUrl2);
    				// 下载图片到本地
    				if (!imgUrl2.contains(imgFilter)&&!imgUrl2.equals("")) {
    					Map downImages = downImages(filepath, imgUrl2, alt1);
    					picInfo.putAll(downImages);
    				} else {

    				}
    			}
    			System.out.println("下载完成");
    			//File out = new File("E:/img2/");
    			File out = new File(filepath1);
    			if(!out.exists()){
    				out.mkdir();
    			}
    			deleteFile(out);
    			File[] filelist = file.listFiles();
    			//将图片经过裁剪上传至另一文件夹
    			for (File f : filelist) {
    				//if (f.isFile() && (f.getName().endsWith(".jpg") || f.getName().endsWith(".jpg"))) {
    				if (f.isFile()) {
    					String picName = f.getName();
    					String p_inUrl = filepath +"\\"+ picName;
    					String p_outUrl = filepath1 +"\\"+ picName;
    					File p_in = new File(p_inUrl);
    					File p_out = new File(p_outUrl);
    					uploadImage(p_in, p_out, height, width, ftype);
    				}
    			}
    			int i = 0;
    			for (File f2 : filelist) {
    				if (f2.isFile() && (f2.getName().endsWith(".jpg") || f2.getName().endsWith(".jpg"))) {
    					String picName = (String) picInfo.get(f2.getName());
    					if(picName==null||picName.contains("院线")||picName.contains("影城")||picName.contains("影院")){
    						System.out.println(picName);
    						continue;
    					}
    					String picShort = null;
    					if(picName.contains("（")&&(picName.contains("数字")||picName.contains("中国巨幕")||picName.contains("IMAX"))){
    					    picShort = picName.substring(0, picName.indexOf("（"));
    					}else if(picName.contains("_海报")){
    						picShort = picName.substring(0, picName.indexOf("_"));
    					}else if(picName.contains("(20")){
    						 picShort = picName.substring(0, picName.indexOf("("));
    					}else{
    						picShort=picName;
    					}
    					// String fileName = (String) picInfo.get("fileName");
    					JSONObject picFile = uploadPicfile(picShort,f2.getName());
    					String shortUrl = picFile.getString("shortUrl");
    					pictureInfo.put("picName", picName);
    					pictureInfo.put("shortUrl", shortUrl);
    					//System.out.println(picShort);
    					DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TICKET);
    					List list =null;
    					if(null!=picShort&&""!=picShort){
    						list = filmInfoService.getMovieCode(picShort);
    					}
    					DataSourceContextHolder.clearDbType();
    					//List<Map<String, Object>> imgUrlList = new ArrayList<>();
    					if(null!=list&&!list.isEmpty()&&list.size()>0){
    							Map codeMap = (Map) list.get(0);
    							//Map<String, Object> imgUrlMap = new HashMap<>();
    							String movieCode = (String) codeMap.get("movieCode");
    							String movieUrl = (String) pictureInfo.get("shortUrl");
    							
    							//imgUrlMap.put("movieCode", movieCode);
    							//imgUrlMap.put("movieUrl",movieUrl);
    							//imgUrlList.add(imgUrlMap);
//    							RedisUtil redisUtil = (RedisUtil)ApplicationContextUtil.getBean("redisUtil");
//    							redisUtil.lSet("imgUrlList", imgUrlList);
    							//System.out.println("movieCode="+movieCode+" movieUrl="+movieUrl+movieUrl.length());
    							if(!movieUrl.equals("0")){
    								String movieShadow = movieUrl.substring(0, movieUrl.indexOf("."))+"_shadow.jpg";
    								DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
    								String movieCode1 = movieCode.substring(movieCode.length()-8, movieCode.length());
    								Integer imgNum = filmInfoService.searchMovieInfo(movieCode1);
    								int kk = 0;
    								if(imgNum>0){
    									filmInfoService.updateMovieInfo(movieCode1,movieUrl,movieShadow);
    								}else{
    									filmInfoService.insertMovieInfo(movieCode1,movieUrl,movieShadow);
    								}
    								DataSourceContextHolder.clearDbType();
    							}
    						
    					}
    					
    					//System.out.println(pictureInfo);
    					i++;
    				}
    			}
    			//System.out.println(pictureInfo);
    			deleteFile(file);
    			deleteFile(out);
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
        private void deleteFile(File file) {
    		if (file.exists()) {// 判断文件是否存在
    			if (file.isFile()) {// 判断是否是文件
    				file.delete();// 删除文件
    			} else if (file.isDirectory()) {// 否则如果它是一个目录
    				File[] files = file.listFiles();// 声明目录下所有的文件 files[];
    				for (int i = 0; i < files.length; i++) {// 遍历目录下所有的文件
    					this.deleteFile(files[i]);// 把每个文件用这个方法进行迭代
    				}
    				// file.delete();//删除文件夹
    			}
    		} else {
    			System.out.println("所删除的文件不存在");
    		}
    	}

    	/**
    	 * 下载图片到指定目录
    	 *
    	 * @param filePath
    	 *            文件路径
    	 * @param imgUrl
    	 *            图片URL
    	 */
    	public static Map downImages(String filePath, String imgUrl, String alt) {
    		// 若指定文件夹没有，则先创建
    		File dir = new File(filePath);
    		if (!dir.exists()) {
    			dir.mkdirs();
    		}
    		// 截取图片文件名
    		String fileName = imgUrl.substring(imgUrl.lastIndexOf('/') + 1, imgUrl.length());
    		String fileName1 = System.currentTimeMillis() + "" + new Random().nextInt(10)
    				+ imgUrl.substring(imgUrl.lastIndexOf('.'), imgUrl.length());
    		Map<String, String> map = new HashMap<String, String>();
    		map.put(fileName1, alt);
    		// map.put("fileName", fileName1);
    		try {
    			// 文件名里面可能有中文或者空格，所以这里要进行处理。但空格又会被URLEncoder转义为加号
    			String urlTail = URLEncoder.encode(fileName, "UTF-8");
    			// 因此要将加号转化为UTF-8格式的%20
    			imgUrl = imgUrl.substring(0, imgUrl.lastIndexOf('/') + 1) + urlTail.replaceAll("\\+", "\\%20");

    		} catch (UnsupportedEncodingException e) {
    			e.printStackTrace();
    		}
    		// 写出的路径
    		File file = new File(filePath + File.separator + fileName1);

    		try {
    			// 获取图片URL
    			URL url = new URL(imgUrl);
    			// 获得连接
    			URLConnection connection = url.openConnection();
    			// 设置10秒的相应时间
    			connection.setConnectTimeout(10 * 1000);
    			// 获得输入流
    			InputStream in = connection.getInputStream();
    			// 获得输出流
    			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
    			// 构建缓冲区
    			byte[] buf = new byte[1024];
    			int size;
    			// 写入到文件
    			while (-1 != (size = in.read(buf))) {
    				out.write(buf, 0, size);
    			}
    			out.close();
    			in.close();
    		} catch (MalformedURLException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		return map;
    	}
    	// 按比例裁剪图片
    	public static void uploadImage(File p_in, File p_out, int height, int width, String ftype)
    			throws FileNotFoundException, IOException {
    		// 取得图片处理
    		InputStream l_in = new FileInputStream(p_in);
    		OutputStream l_out = new FileOutputStream(p_out);
    		chgPic(l_in, l_out, width, height, ftype);
    	}

    	// 按比例压缩图片
    	public static boolean chgPic(InputStream in, OutputStream out, int newWidth, int newHeight, String ftype) {
    		BufferedImage img = null;
    		FileInputStream newin = null;
    		File tempfile = null;
    		try {
    			if (ftype.compareToIgnoreCase("bmp") == 0) {
    				PNGDecodeParam decodeParam = new PNGDecodeParam();
    				String l_tempfile = "jpg";
    				tempfile = new File(l_tempfile);
    				JPEGEncodeParam encodeParam = new JPEGEncodeParam();
    				// 根据路径打开输出流
    				FileOutputStream tempout;
    				tempout = new FileOutputStream(tempfile);
    				ImageDecoder decoder = ImageCodec.createImageDecoder("BMP", in, decodeParam);
    				RenderedImage image = decoder.decodeAsRenderedImage();
    				ImageEncoder encoder = ImageCodec.createImageEncoder("JPEG", tempout, encodeParam);
    				encoder.encode(image);
    				tempout.close();

    				newin = new FileInputStream(tempfile);
    				img = ImageIO.read(newin);
    			} else {
    				img = ImageIO.read(in);
    			}

    			int width = img.getWidth(null);
    			int height = img.getHeight(null);

    			if (newWidth >= width) {
    				if (newHeight < height) {
    					width = (int) (width * newHeight / height);
    					height = newHeight;
    				}
    			} else {
    				if (newHeight >= height) {
    					height = (int) (height * newWidth / width);
    					width = newWidth;
    				} else {
    					if (height > width) {
    						width = (int) (width * newHeight / height);
    						height = newHeight;
    					} else {
    						height = (int) (height * newWidth / width);
    						width = newWidth;
    					}
    				}
    			}

    			BufferedImage img2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    			img2.getGraphics().drawImage(img, 0, 0, width, height, null);

    			if (ftype.compareToIgnoreCase("jpg") == 0 || ftype.compareToIgnoreCase("jpeg") == 0) {
    				ImageIO.write(img2, "jpg", out);
    			} else
    				ImageIO.write(img2, "png", out);

    			if (ftype.compareToIgnoreCase("bmp") == 0) {
    				ImageIO.write(img2, "jpg", out);
    				newin.close();
    				tempfile.delete();
    			}
    			return true;
    		} catch (Exception ex) {
    			ex.printStackTrace();
    			return false;
    		} finally {
    			try {
    				in.close();
    				out.close();
    			} catch (IOException e) {

    			}
    		}
    	}

    	@RequestMapping(value = "/uploadPicfile")
    	@ResponseBody
    	public JSONObject uploadPicfile(String picShort,String fileName) throws IOException {
    		//String storeUrl = "E:\\img2\\";
    		String path = Thread.currentThread().getContextClassLoader().getResource(".").getPath();
    		String filepath = path.substring(0, path.lastIndexOf("lib/"));
    		String imgUrl = filepath +"img1/"+ fileName;
    		String storePath = filepath + "webapps/img";
    		File storeDir = new File(storePath);
    		if (!(storeDir.exists() && storeDir.isDirectory())) {
    			storeDir.mkdirs();
    		}
    		JSONObject json = new JSONObject();
    		String RealityUrl = storePath + "/" + fileName;
    		File file = new File(imgUrl);
    		if (!file.exists())
    			throw new RuntimeException("文件不存在..");
    		
    		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TICKET);
    		List list =null;
			if(null!=picShort&&""!=picShort){
				list = filmInfoService.getMovieCode(picShort);
			}
    		DataSourceContextHolder.clearDbType();
    		if(null!=list&&list.size()>0){
    			Map map = (Map) list.get(0);
    			String movieCode = (String) map.get("movieCode");
    			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
    			String movieCode1 = movieCode.substring(movieCode.length()-8, movieCode.length());
    			Integer imgNum = filmInfoService.searchMovieInfo(movieCode1);
    			DataSourceContextHolder.clearDbType();
    			int kk = 0;
    			if(imgNum>0){
        			json.put("shortUrl", 0);
    				System.out.println(kk++);
    			}else{
    				uploadShadowPic(imgUrl,RealityUrl);
    				FileInputStream fis = new FileInputStream(file);
    				byte[] b = new byte[1024];
    				int len = 0;
    				FileOutputStream fos = new FileOutputStream(RealityUrl);
    				while ((len = fis.read(b)) != -1) {
    					fos.write(b, 0, len);
    				}
    				fos.close();
    				fis.close();
    				json.put("shortUrl", fileName);
    			}
    		}else{
    			json.put("shortUrl", 0);
    		}
    		return json;
    	} 
    	
    	public void uploadShadowPic(String filepath,String storePath){
    		//String filepath1 = "D:\\img";
    		//File file1 = new File(filepath1);
    		//File[] listFiles = file1.listFiles();
    		//for (File file2 : listFiles) {
    			//String filename = file2.getName();
    			//String filepath = filepath1+"\\"+filename;
    			String newFilePath = storePath.substring(0, storePath.indexOf("."))+"_shadow.jpg";
    			FileInputStream ips = null; 
    			FileOutputStream ops = null;
    			try{
    				File file=new File(filepath);
    				File blurfile=new File(newFilePath);
    				ips=new FileInputStream(file);
    				byte[] buf =new byte[ips.available()];
    				ips.read(buf);
    				byte[] blurimg=blurFile(file);
    				ops=new FileOutputStream(blurfile);
    				ops.write(blurimg);
    				ops.flush();
    				ops.close();
    				ips.close();
    			}catch (IOException e){ 
    				e.printStackTrace();
    			}finally {
    			}
    		//}
    	}
    	public static byte[] blurFile(File file) throws IOException {
        	FileInputStream ips = new FileInputStream(file); 
        	BufferedImage img = ImageIO.read(ips);
        	GaussianFilter gaussianFilter = new GaussianFilter();
        	gaussianFilter.setRadius(30);
        	gaussianFilter.filter(img, img);
        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
        	ImageIO.write(img, "jpg", baos);
        	ips.close();
        	ips=null;
        	return baos.toByteArray();
        }
}   