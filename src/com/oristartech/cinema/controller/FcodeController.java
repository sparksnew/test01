package com.oristartech.cinema.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.oristartech.cinema.Const.Page;
import com.oristartech.cinema.service.CustomerService;
import com.oristartech.cinema.service.FcodeService;
import com.oristartech.cinema.utils.Const;
import com.oristartech.cinema.utils.PageData;



/**
 * 
 * @author xy
 * 
 */
@Controller
@RequestMapping(value = "/fcode")
public class FcodeController extends BaseController {

	String menuUrl = "fcode/list.do"; // 菜单地址(权限用)

	@Resource(name = "fcodeService")
	private FcodeService fcodeService;
	@Resource(name = "customerService")
	private CustomerService customerService;

	/**
	 * 显示激活码列表
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/list")
	public ModelAndView listFcode(Page page) throws Exception {

		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		List<PageData> fcodeList;
		List<PageData> froleList;

		pd = this.getPageData();
		String USERNAME = pd.getString("USERNAME");

		if (null != USERNAME && !"".equals(USERNAME)) {
			USERNAME = USERNAME.trim();
			pd.put("USERNAME", USERNAME);
		}

		String lastLoginStart = pd.getString("lastLoginStart");
		String lastLoginEnd = pd.getString("lastLoginEnd");
		System.out.println(lastLoginStart);
		System.out.println(lastLoginEnd);
		String roleName = pd.getString("ROLE_NAME");
		if (null != roleName && !"".equals(roleName)) {
			roleName = roleName.trim();
			pd.put("roleName", roleName);
		}
		if (lastLoginStart != null && !"".equals(lastLoginStart)) {
			lastLoginStart = lastLoginStart + " 00:00:00";
			pd.put("lastLoginStart", lastLoginStart);
		}
		if (lastLoginEnd != null && !"".equals(lastLoginEnd)) {
			lastLoginEnd = lastLoginEnd + " 00:00:00";
			pd.put("lastLoginEnd", lastLoginEnd);
		}
		System.out.println("USERNAME:" + USERNAME + ",lastLoginStart:" + lastLoginStart + ",lastLoginEnd:"
				+ lastLoginEnd + ",roleName:" + roleName);
		page.setPd(pd);

		fcodeList = fcodeService.listAllFcode(page);
		froleList = fcodeService.listAllRole(page);

		mv.setViewName("fcode/fcode_list");
		mv.addObject("pd", pd);
		mv.addObject("fcodeList", fcodeList);
		mv.addObject("froleList", froleList);
		mv.addObject(Const.SESSION_QX, this.getHC()); // 按钮权限

		return mv;
	}

	@RequestMapping(value = "/Delete")

	public ModelAndView Delete(Page page) {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String ROLE_ID = pd.getString("ROLE_ID");
		System.out.println("ROLE_ID:" + ROLE_ID);
		try {
			pd = this.getPageData();
			fcodeService.deleteFcode(ROLE_ID);
			System.out.println("删除成功");
		} catch (Exception e) {
			// logger.error(e.toString(), e);
		}
		mv.setViewName("fcode/fcode_list");
		mv.addObject("pd", pd);
		return mv;
	}

	public Map<String, String> getHC() {
		Subject currentUser = SecurityUtils.getSubject(); // shiro管理的session
		Session session = currentUser.getSession();
		return (Map<String, String>) session.getAttribute(Const.SESSION_QX);
	}

	/**
	 * 新增激活码窗口列表
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/newcode")
	public ModelAndView NewFcode(Page page) throws Exception {

		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		List<?> fcustomerList;
		List<?> froleList;
		List<?> fTheaterList;
		pd = this.getPageData();

		fcustomerList = customerService.listAllCustomer1(page);
		froleList = fcodeService.listAllRole(page);
		fTheaterList = fcodeService.ListAllTheaters1(page);
		mv.setViewName("fcode/fcode_add");
		mv.addObject("pd", pd);
		mv.addObject("fcustomerList", fcustomerList);
		mv.addObject("froleList", froleList);
		mv.addObject("fTheaterList", fTheaterList);
		mv.addObject(Const.SESSION_QX, this.getHC()); // 按钮权限

		return mv;
	}

	@RequestMapping(value = "/getTheater")

	public ModelAndView getTheater(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String customerID = pd.getString("customerID");
		System.out.println("customerID:" + customerID);
		pd = this.getPageData();

		if (null != customerID && !"".equals(customerID)) {
			customerID = customerID.trim();
			pd.put("customerID", customerID);
		}
		System.out.println("customerID:" + customerID);
		page.setPd(pd);
		List<PageData> fTheaterList;
		fTheaterList = fcodeService.ListAllTheaters(page);
		mv.setViewName("fcode/fTherterList");
		mv.addObject("pd", pd);
		mv.addObject("fTheaterList", fTheaterList);
		mv.addObject(fTheaterList);
		System.out.println(fTheaterList);
		mv.addObject(Const.SESSION_QX, this.getHC()); // 按钮权限
		return mv;
	}

	@RequestMapping(value = "/saveFcode")

	public ModelAndView saveFcode(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		SimpleDateFormat sdft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = sdft.parse(sdft.format(new Date()));
		pd.put("fcode", createCode());
		pd.put("createDate", date);
		pd.put("deviceType", 1);
		pd.put("timeLong", 48);
		pd.put("codeStatus", 1);
		fcodeService.saveFcode(pd);
		mv.addObject("msg", "success");
		mv.setViewName("save_result");
		return mv;
	}

	public static String createCode() {
		Random random = new Random();
		String val = "";
		for (int i = 0; i < 6; i++) {
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			if ("char".equalsIgnoreCase(charOrNum)) {
				int upperOrLower = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val += (char) (upperOrLower + random.nextInt(26));
			} else {
				val += random.nextInt(10);
			}
			val = val.toUpperCase();
		}
		return val;
	}
}
