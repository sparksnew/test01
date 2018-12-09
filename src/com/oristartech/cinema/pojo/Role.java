package com.oristartech.cinema.pojo;

import org.apache.ibatis.type.Alias;

@Alias("role")
public class Role {
    private int roleID;
    private String roleName;
    private String roleDetail;
	public int getRoleID() {
		return roleID;
	}
	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRoleDetail() {
		return roleDetail;
	}
	public void setRoleDetail(String roleDetail) {
		this.roleDetail = roleDetail;
	}
    
}
