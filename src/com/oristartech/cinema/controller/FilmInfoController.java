package com.oristartech.cinema.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oristartech.cinema.service.FilmInfoService;
import com.oristartech.cinema.utils.DataSourceContextHolder;
import com.oristartech.cinema.utils.MessageUtils;
import com.oristartech.cinema.utils.RedisCacheManager;
import com.oristartech.cinema.utils.ToolUtils;

import net.sf.json.JSONObject;

@RequestMapping("/ticketSys")
@Controller
public class FilmInfoController{
	@Autowired
	private FilmInfoService filmInfoService;
	@Autowired
    private RedisCacheManager redisCacheManager;
	@RequestMapping(value = "/moviecodedaily")
	public void getMovieCodeDaily(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try {
			String cinemaCode = request.getParameter("theaterId");
			if (cinemaCode == null || cinemaCode.length() <= 0) {
				json.put("validateCode", MessageUtils.FAIL_CODE);
				json.put("msg", MessageUtils.FAIL_MSG);
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String current = sdf.format(new Date());
			// 获取本影院当日上映影片编码
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TICKET);
			List movieCodeList = filmInfoService.getMovieCodeDaily(cinemaCode, current);
			DataSourceContextHolder.clearDbType();
			json.put("movieCodeList", movieCodeList);
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_SENDMESSAGE);
		} catch (Exception e) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
	}

	@RequestMapping(value = "/imgUrl")
	@ResponseBody
	public JSONObject getimgUrl(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		JSONObject json1 = new JSONObject();
		String cinemaCode = request.getParameter("theaterId");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String current = sdf.format(new Date());
		//获取排行榜影片信息
		Object picList1 = redisCacheManager.get("picList");
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TICKET);
		List rankList = filmInfoService.queryMovieRank(cinemaCode, current);
		DataSourceContextHolder.clearDbType();
		if(picList1==null){
			if (cinemaCode == null || cinemaCode.length() <= 0) {
				json.put("validateCode", MessageUtils.FAIL_CODE);
				json.put("msg", MessageUtils.FAIL_MSG);
			}
			List picList = new ArrayList<>();
			for (int i = 0; i < rankList.size(); i++) {
				Map rankMap = (Map) rankList.get(i);
				String movieCode = (String) rankMap.get("mvCode");
				DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
				// 获取影片图片地址
				Map imgMap = filmInfoService.getImgInfo(movieCode);
				String imgUrl = (String) imgMap.get("imgUrl");
				String imgshadow = (String) imgMap.get("imgshadow");
				DataSourceContextHolder.clearDbType();
				// rankMap.put("imgUrl", imgUrl);
				// Map imgMap = (Map) imgUrl.get(0);
				// String imgUrl1 = (String) imgMap.get("imgURL");
				rankMap.put("imgUrl", imgUrl);
				rankMap.put("imgshadow", imgshadow);
				picList.add(i, rankMap);
			}
			json.put("picList", picList);
			redisCacheManager.set("picList", picList);
		}else{
			json.put("picList", redisCacheManager.get("picList"));
		}
		//获取当日排名第一的影片信息
		if (rankList.size() > 0) {
			Object firstDetai = redisCacheManager.get("firstDetai");
			if(firstDetai==null){
				Map firstInfo = (Map) rankList.get(0);
				String firstCode = (String) firstInfo.get("mvCode");
				// firstList.add(firstInfo);
				DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TICKET);
				// 本影院累计票房、首日票房、首周票房
				List localInfolist = filmInfoService.getFilmLocalInfo(cinemaCode, firstCode);
				// 售卖渠道占比
				List sellChannelsList = filmInfoService.getSellChannels(cinemaCode, firstCode);
				// 会员消费占比
				List memberRatioList = filmInfoService.getMemberRatio(cinemaCode, firstCode);
				// 该影片每日明细:1.日期 2.分账票房3.排片占比4.票房占比5.人次
				List dailyDetailList = filmInfoService.getDailyDetail(cinemaCode, firstCode);
				DataSourceContextHolder.clearDbType();
				// 全国累计票房、今日票房占比、首周票房
				DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
				List totalInfolist = filmInfoService.getFilmTotalInfo(firstCode, current);
				List totalInfolist1 = new ArrayList<>();
				if (totalInfolist.size() > 0 && null != totalInfolist) {
					json.put("totalInfolist", totalInfolist);
				} else {
					List countryBoxList = filmInfoService.getCountryBox(firstCode);
					for (int i = 0; i < countryBoxList.size(); i++) {
						Map ctyBoxMap = (Map) countryBoxList.get(i);
						ctyBoxMap.put("BoxRate", "0");
						totalInfolist1.add(ctyBoxMap);
					}
					json.put("totalInfolist", totalInfolist1);
				}
				// 获取影片图片地址
				DataSourceContextHolder.clearDbType();
				redisCacheManager.set("firstDetai", firstCode,3600);
				redisCacheManager.set("localInfolist", localInfolist,3600);
				redisCacheManager.set("sellChannelsList", sellChannelsList,3600);
				redisCacheManager.set("memberRatioList", memberRatioList,3600);
				redisCacheManager.set("dailyDetailList", dailyDetailList,3600);
				redisCacheManager.set("totalInfolist", totalInfolist,3600);
				json1.put("firstDetai", redisCacheManager.get("firstDetai"));
				json1.put("localInfolist", redisCacheManager.get("firstDetai"));
				json1.put("sellChannelsList", redisCacheManager.get("firstDetai"));
				json1.put("memberRatioList", redisCacheManager.get("firstDetai"));
				json1.put("dailyDetailList", redisCacheManager.get("firstDetai"));
				json1.put("totalInfolist", redisCacheManager.get("firstDetai"));
			}else{
				json1.put("firstDetai", redisCacheManager.get("firstDetai"));
				json1.put("localInfolist", redisCacheManager.get("firstDetai"));
				json1.put("sellChannelsList", redisCacheManager.get("firstDetai"));
				json1.put("memberRatioList", redisCacheManager.get("firstDetai"));
				json1.put("dailyDetailList", redisCacheManager.get("firstDetai"));
				json1.put("totalInfolist", redisCacheManager.get("firstDetai"));
			}
			json.put("firstInfo", json1);
			//redisCacheManager.set("firstInfo", json,3600);
			//System.out.println(redisCacheManager.get("firstInfo"));
			//System.out.println("json=" + json);
			json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_MSG);
			// } catch (Exception e) {
			// e.printStackTrace();
			// json.put("validateCode", MessageUtils.FAIL_CODE);
			// json.put("msg", MessageUtils.FAIL_MSG);
			// }
		} else {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", "今日没有排片");
		}
		return json;
	}

	@RequestMapping(value = "/filminfo")
	@ResponseBody
	public JSONObject getFilmInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JSONObject json = new JSONObject();
		// try {
		String cinemaCode = request.getParameter("cinemaCode");
		String movieCode = request.getParameter("movieCode");
		// if(jsonp==null||jsonp.length()<=0){
		// ToolUtils.setUnsuccess(json, jsonp, rs);
		// }
		if (cinemaCode == null || cinemaCode.length() <= 0) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
		if (movieCode == null || movieCode.length() <= 0) {
			json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String current = sdf.format(new Date());
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TICKET);
		// 本影院累计票房、首日票房、首周票房
		List localInfolist = filmInfoService.getFilmLocalInfo(cinemaCode, movieCode);
		// 售卖渠道占比
		List sellChannelsList = filmInfoService.getSellChannels(cinemaCode, movieCode);
		// 会员消费占比
		List memberRatioList = filmInfoService.getMemberRatio(cinemaCode, movieCode);
		// 该影片每日明细:1.日期 2.分账票房3.排片占比4.票房占比5.人次
		List dailyDetailList = filmInfoService.getDailyDetail(cinemaCode, movieCode);
		DataSourceContextHolder.clearDbType();
		// 全国累计票房、今日票房占比、首周票房
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
		List totalInfolist = filmInfoService.getFilmTotalInfo(movieCode, current);
		System.out.println("totalInfolist=" + totalInfolist);
		List totalInfolist1 = new ArrayList<>();
		if (totalInfolist.size() > 0 && null != totalInfolist) {
			json.put("totalInfolist", totalInfolist);
		} else {
			List countryBoxList = filmInfoService.getCountryBox(movieCode);
			for (int i = 0; i < countryBoxList.size(); i++) {
				Map ctyBoxMap = (Map) countryBoxList.get(i);
				ctyBoxMap.put("BoxRate", "0");
				totalInfolist1.add(ctyBoxMap);
			}
			json.put("totalInfolist", totalInfolist1);
		}
		// 获取影片图片地址
		// List imgUrl = filmInfoService.getImgInfo(movieCode);
		DataSourceContextHolder.clearDbType();
		System.out.println("localInfolist=" + localInfolist);
		System.out.println("sellChannelsList=" + sellChannelsList);
		System.out.println("memberRatioList=" + memberRatioList);
		System.out.println("dailyDetailList=" + dailyDetailList);
		System.out.println("totalInfolist=" + totalInfolist);
		json.put("localInfolist", localInfolist);
		json.put("sellChannelsList", sellChannelsList);
		json.put("memberRatioList", memberRatioList);
		json.put("dailyDetailList", dailyDetailList);
		json.put("validateCode", MessageUtils.SUCCESS_CODE);
		json.put("msg", MessageUtils.SUCCESS_MSG);
		// } catch (Exception e) {
		// json.put("validateCode", MessageUtils.FAIL_CODE);
		// json.put("msg", MessageUtils.FAIL_MSG);
		// }
		return json;
	}

	@RequestMapping(value = "/filmBox")
	@ResponseBody
	public void getFilmBox() throws IOException {
		// 全国累计票房、今日票房占比、首周票房
		final String boxOfficeUrl = "http://www.cbooo.cn/BoxOffice/GetHourBoxOffice";
		final String boxOfficeUrl1 = "http://dianying.nuomi.com/movie/boxrefresh";
		// String weekUrl =
		// "http://www.cbooo.cn/boxOffice/GetYearInfo_f?year=2017";
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 7); // 控制时
		calendar.set(Calendar.MINUTE, 0); // 控制分
		calendar.set(Calendar.SECOND, 0); // 控制秒
		Date time = calendar.getTime(); // 得出执行任务的时间,此处为今天的12：00：00
		Timer timer = new Timer();
		Calendar calendar1 = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 7); // 控制时
		calendar.set(Calendar.MINUTE, 5); // 控制分
		calendar.set(Calendar.SECOND, 0); // 控制秒
		Date time1 = calendar1.getTime(); // 得出执行任务的时间,此处为今天的12：00：00
		Timer timer1 = new Timer();
		try {
			// 获取请求连接
			Connection con = Jsoup.connect(boxOfficeUrl1).ignoreContentType(true);
			// 遍历生成参数
			Document doc = con.post();

			timer.scheduleAtFixedRate(new TimerTask() {
				public void run() {
					int kk = 0;
					try {
						httpPost1(boxOfficeUrl1, kk);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}, time, 1000 * 60 * 60 * 24);// 这里设定将延时每天固定执行
			timer1.scheduleAtFixedRate(new TimerTask() {
				public void run() {
					int kk = 1;
					try {
						httpPost1(boxOfficeUrl1, kk);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}, time1, 1000 * 60 * 5);// 这里设定将延时每天固定执行
		} catch (Exception e) {
			try {
				// 获取请求连接
				Connection con = Jsoup.connect(boxOfficeUrl).ignoreContentType(true);
				// 遍历生成参数
				Document doc = con.post();

				timer.scheduleAtFixedRate(new TimerTask() {
					public void run() {
						int kk = 0;
						try {
							httpPost(boxOfficeUrl, kk);
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
				}, time, 1000 * 60 * 5);// 这里设定将延时每天固定执行
			} catch (Exception e1) {

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
			if (recordNum == 0) {
				filmInfoService.insertTotalInfo(sumBox, current);

			} else {
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
			List movieCode1 = filmInfoService.getMovieCode(movieName);
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
					Integer boxNum = filmInfoService.queryBoxNum(movieCode, current);
					if (boxNum == 0) {
						filmInfoService.insertFilmInfo(movieName, movieCode, realBox, totalBox, showDays, boxRate,
								current, mId, firstWeekBox);
					} else {
						filmInfoService.updateFilmInfo(movieCode, realBox, totalBox, showDays, boxRate, current, mId);
					}
				} else {
					filmInfoService.updateFilmInfo(movieCode, realBox, totalBox, showDays, boxRate, current, mId);
				}
				DataSourceContextHolder.clearDbType();
			}
		}
		if (kk == 1) {
			getWeekBox();
		}
		System.out.println(j);
	}

	private void getWeekBox() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String current = sdf.format(new Date());
		Calendar calendar = Calendar.getInstance();// 此时打印它获取的是系统当前时间
		/*
		 * calendar.add(Calendar.DATE, -1); //得到前一天 String yesterdayDate = new
		 * SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
		 * DataSourceContextHolder.setDbType(DataSourceContextHolder.
		 * DATA_SOURCE_CINEMA);
		 */
		List yesInfo = filmInfoService.getWeekBoxInfo();
		List list = filmInfoService.getMovieMidList(current);
		DataSourceContextHolder.clearDbType();
		if (null != yesInfo && yesInfo.size() > 0) {
			for (int i = 0; i < yesInfo.size(); i++) {
				Map yesMap = (Map) yesInfo.get(i);
				String firstWeekBox = yesMap.get("firstWeekBox").toString();
				String MovieCode = yesMap.get("MovieCode").toString();
				String mid = yesMap.get("MovieCode").toString();
				if (!firstWeekBox.equals("0")) {
					filmInfoService.chgWeekBox(MovieCode, firstWeekBox, mid, current);
				}
			}
		}
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map map = (Map) list.get(i);
				String mid = map.get("mid").toString();
				String firstWeekBox = map.get("firstWeekBox").toString();
				if (!mid.equals("0") && firstWeekBox.equals("0")) {
					String mUrl = "http://www.cbooo.cn/m/" + mid;
					Connection connect = Jsoup.connect(mUrl);
					try {
						Document document = connect.get();
						Elements tags = document.getElementsByTag("table");
						/*
						 * Elements title = document.getElementsByTag("title");
						 * String string2 = title.eachText().get(0); String
						 * filmName = string2.substring(0,
						 * string2.indexOf("_"));
						 */
						if (!tags.isEmpty()) {
							List<String> eachText = tags.eachText();
							String text = eachText.get(0);
							if (text.contains("第1周")) {
								String keyword = text.substring(text.indexOf("第1周"), text.length());
								String[] split = keyword.split(" ");
								String firstWeekBox1 = split[3];
								DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
								filmInfoService.addMovieInfo(mid, firstWeekBox1, current);
								DataSourceContextHolder.clearDbType();
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void httpPost1(String url, int kk) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String current = sdf.format(new Date());
		// 获取请求连接
		Connection con = Jsoup.connect(url).ignoreContentType(true);
		// 遍历生成参数
		Document doc = con.post();
		Element body = doc.body();
		String body1 = body.toString();
		String totalCountryBox = body1.substring(body1.indexOf("boxNum") + 8, body1.indexOf("message") - 2);
		if (!totalCountryBox.equals("0")) {
			DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
			if (kk == 0) {
				Integer recordNum = filmInfoService.queryTotalNum(current);
				if (recordNum == 0) {
					filmInfoService.insertTotalInfo(totalCountryBox, current);
				} else {
					filmInfoService.updateTotalInfo(totalCountryBox, current);
				}
			} else {
				filmInfoService.updateTotalInfo(totalCountryBox, current);
			}
			DataSourceContextHolder.clearDbType();
			String body3 = body1.substring(body1.indexOf("[") + 1, body1.indexOf("]"));

			String[] body4 = body3.split("}}},");
			// Map<String,String> filmMap =null;
			int j = 0;
			String attribute = null;
			for (int i = 0; i < body4.length; i++) {
				attribute = body4[i] + "}}}";
				// System.out.println(body3[i]);
				j++;
				// String showDays = attribute.substring(beginIndex, endIndex)
				JSONObject json = JSONObject.fromObject(attribute);
				JSONObject filmInfo = (JSONObject) json.get("attribute");
				String movieName = (String) json.get("movieName");
				DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_TICKET);
				List movieCode1 = filmInfoService.getMovieCode(movieName);
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
				String TotalBox = null;
				String TotalBox1 = (String) attr2.get("attrValue");
				if (TotalBox1.contains("亿")) {
					String TotalBox2 = TotalBox1.substring(0, TotalBox1.indexOf("亿"));
					Double TotalBox3 = Double.valueOf(TotalBox2) * 10000;
					TotalBox = Double.valueOf(TotalBox3).toString();
				} else if (TotalBox1.contains("万")) {
					TotalBox = TotalBox1.substring(0, TotalBox1.indexOf("万"));
				} else {
					TotalBox = TotalBox1;
				}
				JSONObject attr3 = (JSONObject) filmInfo.get("3");
				String RealBox = null;
				String RealBox1 = (String) attr3.get("attrValue").toString();
				if (RealBox1.contains("亿")) {
					String RealBox2 = RealBox1.substring(0, RealBox1.indexOf("亿"));
					Double RealBox3 = Double.valueOf(RealBox2) * 10000;
					RealBox = Double.valueOf(RealBox3).toString();
				} else if (RealBox1.contains("万")) {
					RealBox = RealBox1.substring(0, RealBox1.indexOf("万"));
				} else {
					RealBox = RealBox1;
				}
				JSONObject attr4 = (JSONObject) filmInfo.get("4");
				String BoxRate = (String) attr4.get("attrValue").toString();
				String firstWeekBox = "0";
				String mid = "0";
				if (movieCode != null) {
					DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
					if (kk == 0) {
						Integer boxNum = filmInfoService.queryBoxNum(movieCode, current);
						if (boxNum == 0) {
							filmInfoService.insertFilmInfo(movieName, movieCode, RealBox, TotalBox, showDays, BoxRate,
									current, mid, firstWeekBox);
						} else {
							filmInfoService.changeFilmInfo(movieCode, RealBox, TotalBox, showDays, BoxRate, current);
						}
					} else {
						filmInfoService.changeFilmInfo(movieCode, RealBox, TotalBox, showDays, BoxRate, current);
					}
					DataSourceContextHolder.clearDbType();
				}
			}
			if (kk == 0) {
				addMid();
				getWeekBox();
			}
			System.out.println(j);
		}
	}

	public void addMid() throws IOException {
		/*
		 * //获取请求连接 String url="http://www.cbooo.cn/comming"; Connection con =
		 * Jsoup.connect(url).ignoreContentType(true); //遍历生成参数 Document doc =
		 * con.post(); Element body = doc.body(); Elements attr =
		 * body.getElementsByTag("h3"); List<String> eachText = attr.eachText();
		 * String body1 = attr.toString(); String[] body2 =
		 * body1.split("</h3>"); for(int i=0;i<body2.length;i++){ String info =
		 * body2[i].substring(body2[i].indexOf("<a"), body2[i].length()); String
		 * mid = info.substring(info.indexOf("m/")+2, info.indexOf("m/")+8);
		 * String filmName = eachText.get(i);
		 * DataSourceContextHolder.setDbType(DataSourceContextHolder.
		 * DATA_SOURCE_CINEMA); filmInfoService.addMid(mid, filmName);
		 * DataSourceContextHolder.clearDbType();
		 * //System.out.println("filmName="+filmName+" mid="+mid); }
		 */
		// 获取请求连接
		String url = "http://www.cbooo.cn/comming";
		Connection con = Jsoup.connect(url).ignoreContentType(true);
		// 遍历生成参数
		Document doc = con.post();
		Element body = doc.body();
		Elements attr = body.getElementsByTag("h3");
		Elements a = body.getElementsByTag("a");
		for (int i = 0; i < a.size(); i++) {
			Element aa = a.get(i);
			String aa1 = aa.toString();
			if (aa1.contains("_blank") && aa1.contains("title")) {
				if (aa1.contains("aname")) {
					continue;
				} else {
					String mid = aa1.substring(aa1.indexOf("m/") + 2, aa1.indexOf("title") - 2);
					String mvName1 = aa1.substring(aa1.indexOf(">") + 1, aa1.indexOf("</a>"));
					String mvName = mvName1;
					if (mvName1.contains("...")) {
						mvName = mvName1.trim().substring(0, mvName1.indexOf("..."));
					}
					DataSourceContextHolder.setDbType(DataSourceContextHolder.DATA_SOURCE_CINEMA);
					filmInfoService.addMid(mid, mvName);
					DataSourceContextHolder.clearDbType();
				}
			}
		}
	}

}