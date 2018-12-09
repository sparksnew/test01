package com.oristartech.cinema.utils;

public class SmartAgentAdress {
	//服务器地址
	//public static final String SeverAdress = "http://172.22.161.30:8081/smartagent/cinema/";
	public static final String SeverAdress = "http://localhost:8081/smartagent/cinema/";
	//TMS服务器Ip地址
	public static final String TMSSeverIp = "172.22.191.130";
	//TMS服务器端口地址
	public static final String TMSSeverPort = "80";
	//1.所有的影厅状态信息
	public static final String AllHallStatus = SeverAdress+"getAllHallStatus/";
	//2.影院各影厅当前放映信息
	public static final String AllShowInfo = SeverAdress+"getAllShowInfo";
	//public static final String ShowInfoOfHall = "http://172.22.191.130:80/tms_api/index.php/getAllShowInfo";
	//3.影院放映监控警告信息
	public static final String AlarmLog = SeverAdress+"getAlarmLog";
	//4.影厅当天排期信息
	public static final String HallShowInfo = SeverAdress+"getHallShowInfo/";
	//5.获取指定影厅下一场放映信息
	public static final String NextShowInfo = SeverAdress+"getNextShowInfo";
	//6.获取指定影厅的当前放映信息
	public static final String CurrentShowInfo = SeverAdress+"getCurrentShowInfo/";
	//7.查询SPL的详细信息
	public static final String SplDetailInfo = SeverAdress+"getSplDetailInfo/";
	//8.影院内容信息管理
	public static final String HallDevice = SeverAdress+"getHallDevice/";
	//9.获取影厅放映服务上的磁盘容量 
	public static final String PlayDiskInfo = SeverAdress+"getPlayDiskInfo";
	//10.影片导入信息
	public static final String ImportInfo = SeverAdress+"getImportInfo/";
	//11.影片分发信息 
	public static final String SendInfo = SeverAdress+"getSendInfo/";
	//12.影院内容信息管理 
	public static final String FilmList = SeverAdress+"getFilmList";
	//13.获取指定影片的密钥信息
	public static final String FilmKeyInfo = SeverAdress+"getFilmKeyInfo/";
	//14.获取指定影片在哪些厅存在
	public static final String FilmHallInfo = SeverAdress+"getFilmHallInfo/";
}
