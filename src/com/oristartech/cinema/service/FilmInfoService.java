package com.oristartech.cinema.service;

import java.util.List;
import java.util.Map;

public interface FilmInfoService {

	List getMovieCode(String picShort);

	void insertMovieInfo(String movieCode, String movieUrl, String movieShadow);

	Integer searchMovieInfo(String movieCode);

	List getFilmLocalInfo(String cinemaCode, String movieCode);

	List getSellChannels(String cinemaCode, String movieCode);

	List getMemberRatio(String cinemaCode, String movieCode);

	List getDailyDetail(String cinemaCode, String movieCode);

	String queryMovieCode(String movieName);

	void insertFilmInfo(String movieName, String movieCode, String realBox, String totalBox, String showDays,
			String boxRate, String current,String mId,String firstWeekBox);

	void addMovieInfo(String mid, String firstWeekBox, String current);

	void insertTotalInfo(String sumBox, String current);

	void updateTotalInfo(String sumBox, String current);

	void updateFilmInfo(String movieCode, String realBox, String totalBox, String showDays,
			String boxRate, String current,String mId);

	List getMovieMidList(String current);

	List getFilmTotalInfo(String movieCode, String current);

	List getMovieCodeDaily(String cinemaCode, String current);

	Map getImgInfo(String movieCode);

	List queryMovieRank(String cinemaCode, String current);

	void addMid(String mid, String filmName);

	Integer queryTotalNum(String current);

	Integer queryBoxNum(String movieCode, String current);

	void changeFilmInfo(String movieCode, String realBox, String totalBox, String showDays, String boxRate,
			String current);

	List getWeekBoxInfo();

	void chgWeekBox(String MovieCode, String firstWeekBox,String mid, String current);

	List getCountryBox(String movieCode);

	void updateWeekBox(String movieCode, String current);

	void updateMovieInfo(String movieCode1, String movieUrl, String movieShadow);

	List getWeekFilmInfo(String movieCode);

	Integer getFirstShowDay(String movieCode);

	void addMovieInfo1(String mid, String current, String movieCode);



}
