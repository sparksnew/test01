package com.oristartech.cinema.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.oristartech.cinema.Const.Page;
import com.oristartech.cinema.mapper.DaoSupport;
import com.oristartech.cinema.utils.PageData;



@Service("customerService")
public class CustomerService {
	@Resource(name = "daoSupport")
	private DaoSupport dao;

	/**
	 * 获取所有客户信息
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAllCustomer(Page page) throws Exception {
		List<PageData> pageDatas = (List<PageData>) dao.findForList("CustomerMapper.AllCustomerlistPage", page);
		for (int i = 0; i < pageDatas.size(); i++) {
			System.out.println("这是：" + pageDatas.get(i));
		}

		return (List<PageData>) dao.findForList("CustomerMapper.AllCustomerlistPage", page);

	}

	/**
	 * 获取所有客户信息
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAllCustomer1(Page page) throws Exception {
		List<PageData> pageDatas = (List<PageData>) dao.findForList("CustomerMapper.AllCustomerlist", page);
		for (int i = 0; i < pageDatas.size(); i++) {
			System.out.println("这是：" + pageDatas.get(i));
		}

		return (List<PageData>) dao.findForList("CustomerMapper.AllCustomerlist", page);

	}
}
