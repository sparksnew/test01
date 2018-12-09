package com.oristartech.cinema.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.oristartech.cinema.Const.Page;
import com.oristartech.cinema.service.CustomerService;
import com.oristartech.cinema.utils.Const;
import com.oristartech.cinema.utils.PageData;


@Controller
@RequestMapping(value = "/customer")
public class CustomerController extends BaseController {
	String menuUrl = "customer/list.do"; // 菜单地址(权限用)

	@Resource(name = "customerService")
	private CustomerService customerService;

	/**
	 * 显示客户列表
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/list")
	public ModelAndView listCustomer(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		List<PageData> customerList;

		pd = this.getPageData();
		//pd = this.getPageData();
		String USERNAME = pd.getString("USERNAME");

		if (null != USERNAME && !"".equals(USERNAME)) {
			USERNAME = USERNAME.trim();
			pd.put("USERNAME", USERNAME);
		}
		System.out.println("USERNAME:" + USERNAME);
		page.setPd(pd);

		customerList = customerService.listAllCustomer(page);
		System.out.println("这是：" + customerList);
		mv.setViewName("customer/customer_list");
		mv.addObject("pd", pd);
		mv.addObject("customerList", customerList);
		mv.addObject(Const.SESSION_QX, this.getHC()); // 按钮权限
		System.out.println(mv);
		return mv;
	}

	public Map<String, String> getHC() {
		Subject currentUser = SecurityUtils.getSubject(); // shiro管理的session
		Session session = currentUser.getSession();
		return (Map<String, String>) session.getAttribute(Const.SESSION_QX);
	}
}
