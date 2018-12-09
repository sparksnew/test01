
package com.oristartech.cinema.service;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.oristartech.cinema.Const.Page;
import com.oristartech.cinema.mapper.DaoSupport;
import com.oristartech.cinema.utils.PageData;



@Service("cinemaService")
public class CinemaService {
	
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**
	 * 获取所有影院信息
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listAllCinema(Page page)throws Exception{
		List<PageData>  pageDatas = (List<PageData>)dao.findForList("CinemaMapper.AllCinemalistPage", page);
		for(int i=0;i<pageDatas.size();i++){
			System.out.println("这是："+ pageDatas.get(i));
		}
		
		return (List<PageData>) dao.findForList("CinemaMapper.AllCinemalistPage", page);
		
	}
	
}