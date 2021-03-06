package com.oristartech.cinema.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.oristartech.cinema.Const.Page;
import com.oristartech.cinema.mapper.DaoSupport;
import com.oristartech.cinema.utils.PageData;



@Service("appRoleService")
public class AppRoleService {
	@Resource(name = "daoSupport")
	private DaoSupport dao;

	/**
	 * 获取所有角色信息
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listAllAppRole(Page page) throws Exception {
		return (List<PageData>) dao.findForList("AppRoleMapper.AllRolelistPage", page);

	}
}
