package com.oristartech.cinema.pojo;

import java.util.Date;

public class RoleInfo {
	private  String   roleDetail  	;	
	private  Integer  roleID  		;	
	private  String   roleName  	;	
	private  Integer  level  		;	
	private  Integer  createID  	;	
	private  Date createDateTime;  	
	private  Integer  status  		;
	public String getRoleDetail() {
		return roleDetail;
	}
	public void setRoleDetail(String roleDetail) {
		this.roleDetail = roleDetail;
	}
	public Integer getRoleID() {
		return roleID;
	}
	public void setRoleID(Integer roleID) {
		this.roleID = roleID;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getCreateID() {
		return createID;
	}
	public void setCreateID(Integer createID) {
		this.createID = createID;
	}
	public Date getCreateDateTime() {
		return createDateTime;
	}
	public void setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
