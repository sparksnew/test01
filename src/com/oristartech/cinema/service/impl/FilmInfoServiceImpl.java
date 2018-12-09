package com.oristartech.cinema.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oristartech.cinema.mapper.FilmInfoMapper;
import com.oristartech.cinema.service.FilmInfoService;
@Service
public class FilmInfoServiceImpl implements FilmInfoService {
	@Autowired
	private FilmInfoMapper filmInfoMapper;
	@Override
	public List getMovieCode(String picShort) {
		String picTrue =picShort;
		List list = filmInfoMapper.searchMovieCode(picTrue);
		if(list.size()==0){
			list = filmInfoMapper.searchMovieCode1(picTrue);
			if(list.size()==0){
				if(picShort.contains(":")){
					picTrue = picShort.substring(0, picShort.indexOf(":"))+picShort.substring(picShort.indexOf(":")+1, picShort.length());
				}else if(picShort.contains("：")){
					picTrue = picShort.substring(0, picShort.indexOf("："))+picShort.substring(picShort.indexOf("：")+1, picShort.length());
				}
				list = filmInfoMapper.searchMovieCode1(picTrue);
			}
		}
		
		
		
		return list;
	}
	@Override
	public void insertMovieInfo(String movieCode, String movieUrl,String movieShadow) {
		filmInfoMapper.insertMovieInfo(movieCode,movieUrl,movieShadow);
	}
	@Override
	public Integer searchMovieInfo(String movieCode) {
		Integer movieNum = filmInfoMapper.searchMovieInfo(movieCode);
		return movieNum;
	}
	@Override
	public List getFilmLocalInfo(String cinemaCode,String movieCode) {
		List list = filmInfoMapper.getFilmLocalInfo(cinemaCode,movieCode);
		return list;
	}
	@Override
	public List getSellChannels(String cinemaCode, String movieCode) {
		List list = filmInfoMapper.getSellChannels(cinemaCode,movieCode);
		return list;
	}
	@Override
	public List getMemberRatio(String cinemaCode, String movieCode) {
		List list = filmInfoMapper.getMemberRatio(cinemaCode,movieCode);
		return list;
	}
	@Override
	public List getDailyDetail(String cinemaCode, String movieCode) {
		List list = filmInfoMapper.getDailyDetail(cinemaCode,movieCode);
		return list;
	}
	@Override
	public String queryMovieCode(String movieName) {
		String movieCode = filmInfoMapper.queryMovieCode(movieName);
		return movieCode;
	}
	@Override
	public void insertFilmInfo(String movieName, String movieCode, String realBox, String totalBox, String showDays,
			String boxRate, String current,String mId,String firstWeekBox) {
		filmInfoMapper.insertFilmInfo(movieName,movieCode,realBox,totalBox,showDays,boxRate,current,mId,firstWeekBox);
		
	}
	@Override
	public void addMovieInfo(String mid, String firstWeekBox,String current) {
		filmInfoMapper.addMovieInfo(mid,firstWeekBox,current);
	}
	@Override
	public void addMovieInfo1(String mid, String current,String movieCode) {
		filmInfoMapper.addMovieInfo1(mid,current,movieCode);
	}
	@Override
	public void insertTotalInfo(String sumBox, String current) {
		filmInfoMapper.insertTotalInfo(sumBox,current);
		
	}
	@Override
	public void updateTotalInfo(String sumBox, String current) {
		filmInfoMapper.updateTotalInfo(sumBox,current);
		
	}
	@Override
	public void updateFilmInfo(String movieCode, String realBox, String totalBox, String showDays,
			String boxRate, String current,String mid) {
		filmInfoMapper.updateFilmInfo(movieCode,realBox,totalBox,showDays,boxRate,current,mid);
		
	}
	@Override
	public List getMovieMidList(String current) {
		List list = filmInfoMapper.getMovieMidList(current);
		return list;
	}
	@Override
	public List getFilmTotalInfo(String movieCode,String current) {
		List list = filmInfoMapper.getFilmTotalInfo(movieCode,current);
		return list;
	}
	@Override
	public List getMovieCodeDaily(String cinemaCode, String current) {
		List list = filmInfoMapper.getMovieCodeDaily(cinemaCode,current);
		return list;
	}
	@Override
	public Map getImgInfo(String movieCode) {
		String name = "影片图片地址";
		String address = filmInfoMapper.getAddress(name);
		Map imgMap = filmInfoMapper.getImgInfo(movieCode);
		if(imgMap!=null){
			String imgUrl1 =(String) imgMap.get("imgURL"); 
			String imgshadow1 =(String) imgMap.get("imgshadow"); 
			String imgUrl = address+imgUrl1;
			String imgshadow = address+imgshadow1;
			imgMap.put("imgUrl", imgUrl);
			imgMap.put("imgshadow", imgshadow);
		}else{
			imgMap = new HashMap();
			String imgUrl1 = address+"20180522.jpg";
			imgMap.put("imgUrl",imgUrl1 );
			imgMap.put("imgshadow", imgUrl1);
		}
		/*int length = imgUrlList.size();
		for (int i = 0; i < length; i++) {
			Map map = address+imgUrlList.get(i);
		}*/
		return imgMap;
	}
	@Override
	public List queryMovieRank(String cinemaCode, String current) {
		List list = filmInfoMapper.queryMovieRank(cinemaCode, current);
		return list;
	}
	@Override
	public void addMid(String mid, String filmName) {
		filmInfoMapper.addMid(mid,filmName);
		
	}
	@Override
	public Integer queryTotalNum(String current) {
		Integer num = filmInfoMapper.queryTotalNum(current);
		return num;
	}
	@Override
	public Integer queryBoxNum(String movieCode, String current) {
		Integer num = filmInfoMapper.queryBoxNum(movieCode,current);
		return num;
	}
	@Override
	public void changeFilmInfo(String movieCode, String realBox, String totalBox, String showDays, String boxRate,
			String current) {
		filmInfoMapper.changeFilmInfo(movieCode,realBox,totalBox,showDays,boxRate,current);
		
	}
	@Override
	public List getWeekBoxInfo() {
		List yesList =  filmInfoMapper.getWeekBoxInfo();
		return yesList;
	}
	@Override
	public void chgWeekBox(String MovieCode, String firstWeekBox,String mid,String current) {
		filmInfoMapper.chgWeekBox(MovieCode,firstWeekBox,mid,current);
		
	}
	@Override
	public List getCountryBox(String movieCode) {
		List list = filmInfoMapper.getCountryBox(movieCode);
		return list;
	}
	@Override
	public void updateWeekBox(String movieCode,String current) {
		filmInfoMapper.updateWeekBox(movieCode,current);
		
	}
	@Override
	public void updateMovieInfo(String movieCode, String movieUrl,String movieShadow) {
		filmInfoMapper.updateMovieInfo(movieCode,movieUrl,movieShadow);
		
	}
	@Override
	public List getWeekFilmInfo(String movieCode) {
		List list = filmInfoMapper.getWeekFilmInfo(movieCode);
		return list;
	}
	@Override
	public Integer getFirstShowDay(String movieCode) {
		Integer firstShowDay = filmInfoMapper.getFirstShowDay(movieCode);
		return firstShowDay;
	}
	

}
