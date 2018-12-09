/**
 * 
 */
/**
 * @author Administrator
 *
 */
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
import com.oristartech.cinema.service.AppRoleService;
import com.oristartech.cinema.utils.Const;
import com.oristartech.cinema.utils.PageData;



@Controller
@RequestMapping(value = "/appRole")
public class AppRoleController extends BaseController {

	String menuUrl = "appRole/list.do"; // 菜单地址(权限用)

	@Resource(name = "appRoleService")
	private AppRoleService appRoleService;

	/**
	 * 显示角色列表
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/list")
	public ModelAndView listCinema(Page page) throws Exception {

		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		List<PageData> roleList;
		pd = this.getPageData();
		String USERNAME = pd.getString("USERNAME");
		if (null != USERNAME && !"".equals(USERNAME)) {
			USERNAME = USERNAME.trim();
			pd.put("USERNAME", USERNAME);
		}
		System.out.println("USERNAME:" + USERNAME);
		page.setPd(pd);
		roleList = appRoleService.listAllAppRole(page);
		mv.setViewName("appRole/appRole_list");
		mv.addObject("pd", pd);
		mv.addObject("roleList", roleList);
		mv.addObject(Const.SESSION_QX, this.getHC()); // 按钮权限

		return mv;
	}

	public Map<String, String> getHC() {
		Subject currentUser = SecurityUtils.getSubject(); // shiro管理的session
		Session session = currentUser.getSession();
		return (Map<String, String>) session.getAttribute(Const.SESSION_QX);
	}
}