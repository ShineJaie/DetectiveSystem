package com.view.web.controller.system.user;

import javax.servlet.http.HttpServletRequest;

import net.detectiveSystem.entity.user.User;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.view.service.system.user.UserService;
import com.view.utils.paging.Page4DataTable;
import com.view.utils.webTools.WebTools;

/**
 * 系统信息管理 用户管理
 */
@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;

	/**
	 * 用户管理页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", produces = "text/html;charset=UTF-8")
	public String act_allUserList(HttpServletRequest request, Model model) {

		Integer permission = (Integer) request.getSession().getAttribute("permission");

		if (permission != 1) {
			return "404";
		}

		return "system/user/userList";
	}

	/**
	 * 用户管理表格 数据
	 * 
	 * @param searchBean
	 * @param model
	 * @return dataTables 所需要的数据格式
	 */
	@ResponseBody
	@RequestMapping(value = "/json", produces = "application/json;charset=UTF-8")
	public String table_userList(HttpServletRequest request, Page4DataTable pager, String realName) {

		Integer permission = (Integer) request.getSession().getAttribute("permission");

		if (permission != 1) {
			return WebTools.convert2JsonResult(0, "无权限");
		}

		realName = WebTools.dealWithNullVal(realName);
		JSONObject res = userService.getTableData(pager, realName);
		return res.toString();
	}

	/**
	 * 保存用户
	 * 
	 * @param user
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/save", produces = "application/json;charset=UTF-8")
	public String act_saveUser(HttpServletRequest request, User user) {

		Integer permission = (Integer) request.getSession().getAttribute("permission");

		if (permission != 1) {
			return WebTools.convert2JsonResult(0, "无权限");
		}

		if (user.getLoginName().isEmpty()) {
			return WebTools.convert2JsonResult(0, "用户账号不能为空");
		}

		if (user.getPassword().isEmpty()) {
			return WebTools.convert2JsonResult(0, "用户密码不能为空");
		}

		String res = userService.saveUser(user);

		return res;
	}

	/**
	 * 删除用户
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete", produces = "application/json;charset=UTF-8")
	public String act_deleteUser(HttpServletRequest request, @RequestParam(value = "id", required = true) Long id) {

		Integer permission = (Integer) request.getSession().getAttribute("permission");

		if (permission != 1) {
			return WebTools.convert2JsonResult(0, "无权限");
		}

		id = WebTools.dealWithNullVal(id);

		userService.deleteUser(id);

		return WebTools.convert2JsonResult(1, "删除成功");
	}

	/**
	 * 新建用户页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/create")
	public String createUser(HttpServletRequest request, Model model) {

		Integer permission = (Integer) request.getSession().getAttribute("permission");

		if (permission != 1) {
			return WebTools.convert2JsonResult(0, "无权限");
		}

		return "system/user/userInfo";
	}
}
