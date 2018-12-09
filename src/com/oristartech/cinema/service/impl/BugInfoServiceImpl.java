package com.oristartech.cinema.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oristartech.cinema.mapper.AppDeviceMapper;
import com.oristartech.cinema.mapper.BugInfoMapper;
import com.oristartech.cinema.service.BugInfoService;
import com.oristartech.cinema.service.CinemaDeviceInfoService;
@Service
public class BugInfoServiceImpl implements BugInfoService {
	@Autowired
	private BugInfoMapper bugInfoMapper;

	@Override
	public List getBugList(String theaterId, String deviceId) {
		List bugList = bugInfoMapper.queryBugInfo(theaterId,deviceId);
		return bugList;
	}

	@Override
	public List getBugTrace(int bugID) {
		List bugTrace = bugInfoMapper.queryBugTrace(bugID);
		return bugTrace;
	}

	@Override
	public List getBugStatus(int bugID) {
		List bugStatus = bugInfoMapper.queryBugStatus(bugID);
		return bugStatus;
	}

	@Override
	public List bugDetail(String bugID) {
		List bugDetail = bugInfoMapper.queryBugDetail(bugID);
		return bugDetail;
	}

	@Override
	public List getProblemDesc(int bugID) {
		List bugDesc = bugInfoMapper.queryBugDesc(bugID);
		return bugDesc;
	}

	@Override
	public Integer updatemDesc(int bugID, String desc) {
		bugInfoMapper.updatemDesc(bugID,desc);
		Integer num = bugInfoMapper.selectDesc(bugID,desc);
		return num;
	}

	@Override
	public void addImg(String bugID, String tomcatBigUrl, String tomcatSmUrl) {
		bugInfoMapper.addImg(bugID,tomcatBigUrl,tomcatSmUrl);
		
	}

	@Override
	public int editTrouble(String remark, String evaluate, int bugID) {
		bugInfoMapper.editTrouble(remark,evaluate,bugID);
		int num = bugInfoMapper.queryTroubleEdit(bugID);
		return num;
	}

	@Override
	public void addUserBugs(int userID, int indentID, String type) {
		bugInfoMapper.addUserBugs(userID, indentID, type);
		
	}

	@Override
	public List getTheaterInfoByDeviceCode(String deviceCode) {
		List list = bugInfoMapper.getTheaterInfoByDeviceCode(deviceCode);
		return list;
	}

	@Override
	public List getUnsignBugList(String bugIDs) {
		List list = bugInfoMapper.getUnsignBugList(bugIDs);
		return list;
	}

	@Override
	public List getBugIDByUserID(String userID) {
		List BugID = bugInfoMapper.getBugIDByUserID(userID);
		return BugID;
	}
	

}
