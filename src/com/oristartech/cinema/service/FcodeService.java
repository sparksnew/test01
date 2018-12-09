package com.oristartech.cinema.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.oristartech.cinema.Const.Page;
import com.oristartech.cinema.mapper.DaoSupport;
import com.oristartech.cinema.utils.PageData;



@Service("fcodeService")
public class FcodeService {
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
	public List<PageData> listAllFcode(Page page) throws Exception {
		List<PageData> pageDatas = (List<PageData>) dao.findForList("FcodeMapper.AllFcodelistPage", page);
		// for (int i = 0; i < pageDatas.size(); i++) {
		// System.out.println("这是：" + pageDatas.get(i));
		// }

		return (List<PageData>) dao.findForList("FcodeMapper.AllFcodelistPage", page);

	}

	/**
	 * 获取所有角色信息
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAllRole(Page page) throws Exception {
		List<PageData> pageDatas = (List<PageData>) dao.findForList("FcodeMapper.AllRolelist", page);
		// for (int i = 0; i < pageDatas.size(); i++) {
		// System.out.println("这是：" + pageDatas.get(i));
		// }

		return (List<PageData>) dao.findForList("FcodeMapper.AllRolelist", page);

	}

	/**
	 * 获取所有角色信息
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> ListAllCustomers(Page page) throws Exception {
		List<PageData> pageDatas = (List<PageData>) dao.findForList("FcodeMapper.AllCustomerslistPage", page);
		return (List<PageData>) dao.findForList("FcodeMapper.AllCustomerslistPage", page);

	}

	/**
	 * 
	 * @param customerID
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> ListAllTheaters1(Page page) throws Exception {
		return (List<PageData>) dao.findForList("FcodeMapper.AllTheaterslist1", page);
	}

	/**
	 * 
	 * @param customerID
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> ListAllTheaters(Page page) throws Exception {
		return (List<PageData>) dao.findForList("FcodeMapper.AllTheaterslist", page);
	}

	/**
	 * 
	 * @param customerID
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void saveFcode(PageData pd) throws Exception {
		dao.save("FcodeMapper.saveFcode", pd);
	}

	public void deleteFcode(String codeID) throws Exception {
		dao.delete("FcodeMapper.deleteFcode", codeID);
	}
}
