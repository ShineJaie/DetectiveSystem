package com.view.web.controller.system.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.detectiveSystem.entity.user.User;
import net.system.utils.MD5Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.view.service.system.user.UserService;
import com.view.utils.webTools.WebTools;

@Controller
public class LoginController {

	@Autowired
	UserService userService;

	/**
	 * 登录页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/login")
	public String login(Model model) {
		return "login";
	}

	/**
	 * 验证登录账户是否正确
	 * 
	 * @param searchBean
	 * @param model
	 * @return dataTables 所需要的数据格式
	 */
	@ResponseBody
	@RequestMapping(value = "/login/verify", produces = "application/json;charset=UTF-8")
	public String table_userList(HttpServletRequest request, User user) {
		User loginUser = userService.getUserByLoginName(user.getLoginName());
		if (loginUser == null) {
			return WebTools.convert2JsonResult(0, "error");
		} else {
			if (!loginUser.getPassword().equals(MD5Utils.encode(user.getPassword()))) {
				return WebTools.convert2JsonResult(0, "error");
			}
			request.getSession().setAttribute("username", loginUser.getLoginName());
			request.getSession().setAttribute("permission", loginUser.getPermission());
			return WebTools.convert2JsonResult(1, "ok");
		}
	}

	/**
	 * 登录后页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/home")
	public String act_home() {
		return "redirect:" + "suspects/list";
	}

	/**
	 * 登出请求
	 * 
	 * @param request
	 * @param response
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/loginout")
	public String loginout(HttpServletRequest request, HttpServletResponse response, User user, Model model) {

		request.getSession().removeAttribute("username");
		request.getSession().removeAttribute("permission");

		return "login";
	}
}
