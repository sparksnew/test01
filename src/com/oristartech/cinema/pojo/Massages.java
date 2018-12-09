package com.oristartech.cinema.pojo;

/**
 * 微信推送消息实体类
 * 
 * @author My
 * @CreateDate 2016-1-19
 * @param
 */
public class Massages {

	// 微信名称openid
	private String wxName;
	// 故障现象
	private String bugDetail;
	// 故障时间
	private String bugTime;
	// 备注
	private String remarks;

	public String getWxName() {
		return wxName;
	}

	public void setWxName(String wxName) {
		this.wxName = wxName;
	}

	public String getBugDetail() {
		return bugDetail;
	}

	public void setBugDetail(String bugDetail) {
		this.bugDetail = bugDetail;
	}

	public String getBugTime() {
		return bugTime;
	}

	public void setBugTime(String bugTime) {
		this.bugTime = bugTime;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
