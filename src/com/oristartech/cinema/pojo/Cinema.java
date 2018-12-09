package com.oristartech.cinema.pojo;

import org.apache.ibatis.type.Alias;

@Alias("cinema")
public class Cinema {
    private int cinemaID;
    private String cinemaName;
	public int getCinemaID() {
		return cinemaID;
	}
	public void setCinemaID(int cinemaID) {
		this.cinemaID = cinemaID;
	}
	public String getCinemaName() {
		return cinemaName;
	}
	public void setCinemaName(String cinemaName) {
		this.cinemaName = cinemaName;
	}
    
}
