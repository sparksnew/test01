package com.oristartech.cinema.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface FilmInfoMapper {

	public List searchMovieCode(String picShort);
	
	public List searchMovieCode1(String picShort);

	public void insertMovieInfo(@Param("movieCode")String movieCode, @Param("movieUrl")String movieUrl,@Param("movieShadow")String movieShadow);

	public Integer searchMovieInfo(String movieCode);

	public List getFilmLocalInfo(@Param("cinemaCode")String cinemaCode, @Param("movieCode")String movieCode);

	public List getSellChannels(@Param("cinemaCode")String cinemaCode, @Param("movieCode")String movieCode);

	public List getMemberRatio(@Param("cinemaCode")String cinemaCode, @Param("movieCode")String movieCode);

	public List getDailyDetail(@Param("cinemaCode")String cinemaCode, @Param("movieCode")String movieCode);

	public String queryMovieCode(String movieName);

	public void insertFilmInfo(@Param("movieName")String movieName, @Param("movieCode")String movieCode,@Param("realBox") String realBox,
			@Param("totalBox")String totalBox, @Param("showDays")String showDays,@Param("boxRate")String boxRate, @Param("current")String current
			,@Param("mId")String mId,@Param("firstWeekBox")String firstWeekBox);

	public void addMovieInfo(@Param("mid")String mid, @Param("firstWeekBox")String firstWeekBox,@Param("current")String current);
	
	public void addMovieInfo1(@Param("mid")String mid,@Param("current")String current,@Param("movieCode")String movieCode);

	public void insertTotalInfo(@Param("sumBox")String sumBox, @Param("current")String current);

	public void updateTotalInfo(@Param("sumBox")String sumBox, @Param("current")String current);

	public void updateFilmInfo(@Param("movieCode")String movieCode, @Param("realBox")String realBox, 
			@Param("totalBox")String totalBox, @Param("showDays")String showDays,@Param("boxRate")String boxRate,
			@Param("current")String current,@Param("mid")String mid);

	public List getMovieMidList(String current);

	public List getFilmTotalInfo(@Param("movieCode")String movieCode,@Param("current")String current);

	public List getMovieCodeDaily(@Param("cinemaCode")String cinemaCode, @Param("current")String current);

	public Map getImgInfo(String movieCode);
	
	public String getAddress(@Param("name")String name);

	public List queryMovieRank(@Param("cinemaCode")String cinemaCode, @Param("current")String current);

	public void addMid(@Param("mid")String mid, @Param("filmName")String filmName);

	public Integer queryTotalNum(String current);

	public Integer queryBoxNum(@Param("movieCode")String movieCode, @Param("current")String current);

	public void changeFilmInfo(@Param("movieCode")String movieCode, @Param("realBox")String realBox, 
			@Param("totalBox")String totalBox, @Param("showDays")String showDays,@Param("boxRate")String boxRate,
			@Param("current")String current);

	public List getWeekBoxInfo();

	public void chgWeekBox(@Param("movieCode")String movieCode, @Param("firstWeekBox")String firstWeekBox,@Param("mid")String mid, @Param("current")String current);

	public List getCountryBox(String movieCode);

	public void updateWeekBox(@Param("movieCode")String movieCode,@Param("current")String current);

	public void updateMovieInfo(@Param("movieCode")String movieCode, @Param("movieUrl")String movieUrl,@Param("movieShadow")String movieShadow);

	public List getWeekFilmInfo(String movieCode);

	public Integer getFirstShowDay(String movieCode);




}
