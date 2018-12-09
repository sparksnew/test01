package com.oristartech.cinema.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface BugInfoMapper {
	List queryBugInfo(@Param("theaterId")String theaterId, @Param("deviceId")String deviceId);

	List queryBugTrace(int bugID);

	List queryBugStatus(int bugID);

	List queryBugDetail(String bugID);

	List queryBugDesc(int bugID);

	void updatemDesc(@Param("bugID")int bugID, @Param("desc")String desc);

	Integer selectDesc(@Param("bugID")int bugID, @Param("desc")String desc);

	void addImg(@Param("bugID")String bugID, @Param("tomcatBigUrl")String tomcatBigUrl, @Param("tomcatSmUrl")String tomcatSmUrl);

	void editTrouble(@Param("remark")String remark, @Param("evaluate")String evaluate, @Param("bugID")int bugID);

	int queryTroubleEdit(int bugID);

	void addUserBugs(@Param("userID")int userID, @Param("indentID")int indentID, @Param("type")String type);

	List getTheaterInfoByDeviceCode(@Param("deviceCode")String deviceCode);

	List getUnsignBugList(@Param("bugIDs")String bugIDs);

	List getBugIDByUserID(String userID);
}
