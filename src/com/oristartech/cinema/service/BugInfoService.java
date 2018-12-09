package com.oristartech.cinema.service;

import java.util.List;

public interface BugInfoService {

	List getBugList(String theaterId, String deviceId);

	List getBugTrace(int bugID);

	List getBugStatus(int bugID);

	List bugDetail(String bugID);

	List getProblemDesc(int bugID);

	Integer updatemDesc(int bugID, String desc);

	void addImg(String bugID, String tomcatBigUrl, String tomcatSmUrl);

	int editTrouble(String remark, String evaluate, int bugID);

	void addUserBugs(int userID, int indentID, String type);

	List getTheaterInfoByDeviceCode(String deviceCode);

	List getUnsignBugList(String bugIDs);

	List getBugIDByUserID(String userID);



}
