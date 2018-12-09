package com.oristartech.cinema.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.oristartech.cinema.Const.Page;
import com.oristartech.cinema.service.CinemaService;
import com.oristartech.cinema.service.UserService;
import com.oristartech.cinema.utils.ConnCloudUtil;
import com.oristartech.cinema.utils.Const;
import com.oristartech.cinema.utils.MessageUtils;
import com.oristartech.cinema.utils.PageData;

@Controller
@RequestMapping(value = "/cinema")
public class CinemaController extends BaseController {

	String menuUrl = "cinema/list.do"; // 菜单地址(权限用)

	@Resource(name = "cinemaService")
	private CinemaService cinemaService;
    
	@Autowired
	private UserService userService;

	/**
	 * 更新用户登录日志
	 */
	@RequestMapping(value="/updateUserLoginLog")
	@ResponseBody
	public JSONObject updateUserLoginLog(HttpServletRequest request,HttpServletResponse response){
	    String cinemaCode = request.getParameter("cinemaCode");
	    String userID = request.getParameter("userID");
	    JSONObject json = new JSONObject();
	    int count = userService.updateUserLoginLog(cinemaCode, userID);
	    if(count==200){
	    	json.put("validateCode", MessageUtils.SUCCESS_CODE);
			json.put("msg", MessageUtils.SUCCESS_MSG);
	    }else{
	    	json.put("validateCode", MessageUtils.FAIL_CODE);
			json.put("msg", MessageUtils.FAIL_MSG1);
	    }
	    return json;
	}
	
	/**
	 * 获取所有影院信息
	 */
	@RequestMapping(value = "/getCinemaInfo")
	@ResponseBody
	public JSONObject getCinemaInfo(HttpServletRequest request, HttpServletResponse response) {
		String customerCode = request.getParameter("customerCode");
		String cinemaID = request.getParameter("cinemaID");
		String cinemaCode = request.getParameter("cinemaCode");
		String userID = request.getParameter("userID");
		JSONObject json = new JSONObject();
//		try {
			String result = ConnCloudUtil.getCinemaList(cinemaID,customerCode);
			if(result==null||result.length()==0){
				json.put("validateCode", MessageUtils.FAIL_CODE);
				json.put("msg", MessageUtils.FAIL_MSG);
				return json;
			}
			int count = userService.updateUserLoginLog(cinemaCode, userID);
			if(count==200){
				json = JSONObject.parseObject(result);
				json.put("validateCode", MessageUtils.SUCCESS_CODE);
				json.put("msg", MessageUtils.SUCCESS_MSG);
			}else{
				json = JSONObject.parseObject(result);
				json.put("validateCode", MessageUtils.FAIL_CODE);
				json.put("msg", MessageUtils.FAIL_MSG1);
			}
//		} catch (Exception e) {
//			json.put("validateCode", MessageUtils.FAIL_CODE);
//			json.put("msg", MessageUtils.FAIL_MSG);
//		}
		return json;
	}

	/**
	 * 显示影院列表
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/list")
	public ModelAndView listCinema(Page page) throws Exception {

		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		List<PageData> cinemaList;

		pd = this.getPageData();
		String USERNAME = pd.getString("USERNAME");

		if (null != USERNAME && !"".equals(USERNAME)) {
			USERNAME = USERNAME.trim();
			pd.put("USERNAME", USERNAME);
		}
		System.out.println("USERNAME:" + USERNAME);
		page.setPd(pd);
		// page.setPd(pd);

		cinemaList = cinemaService.listAllCinema(page);

		mv.setViewName("cinema/cinema_list");
		mv.addObject("pd", pd);
		mv.addObject("cinemaList", cinemaList);
		mv.addObject(Const.SESSION_QX, this.getHC()); // 按钮权限

		return mv;
	}

	public Map<String, String> getHC() {
		Subject currentUser = SecurityUtils.getSubject(); // shiro管理的session
		Session session = currentUser.getSession();
		return (Map<String, String>) session.getAttribute(Const.SESSION_QX);
	}
}

/*
 * @Controller
 * 
 * @RequestMapping(value = "/appFunction1") public class CinemaController
 * extends BaseController {
 * 
 * String menuUrl = "appFunction/list.do"; // 菜单地址(权限用)
 * 
 * @Resource(name = "appFunctionService") private AppFunctionService
 * appFunctionService;
 * 
 *//**
	 * 显示角色列表
	 * 
	 * @throws Exception
	 *//*
	 * @RequestMapping(value = "/list") public ModelAndView listCinema(Page
	 * page) throws Exception {
	 * 
	 * ModelAndView mv = this.getModelAndView(); PageData pd = new PageData();
	 * List<PageData> functionList; pd = this.getPageData(); String USERNAME =
	 * pd.getString("USERNAME"); if (null != USERNAME && !"".equals(USERNAME)) {
	 * USERNAME = USERNAME.trim(); pd.put("USERNAME", USERNAME); }
	 * System.out.println("USERNAME:" + USERNAME); page.setPd(pd); functionList
	 * = appFunctionService.listAllFunctionRole(page);
	 * mv.setViewName("appFunction/appFunction_list"); mv.addObject("pd", pd);
	 * mv.addObject("functionList", functionList);
	 * mv.addObject(Const.SESSION_QX, this.getHC()); // 按钮权限
	 * 
	 * return mv; }
	 * 
	 * public Map<String, String> getHC() { Subject currentUser =
	 * SecurityUtils.getSubject(); // shiro管理的session Session session =
	 * currentUser.getSession(); return (Map<String, String>)
	 * session.getAttribute(Const.SESSION_QX); } }
	 */
