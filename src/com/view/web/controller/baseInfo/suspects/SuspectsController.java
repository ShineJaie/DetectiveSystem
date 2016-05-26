package com.view.web.controller.baseInfo.suspects;

import javax.servlet.http.HttpServletRequest;

import net.detectiveSystem.entity.suspects.Suspects;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.view.service.baseInfo.suspects.SuspectsService;
import com.view.utils.paging.Page4DataTable;
import com.view.utils.webTools.WebTools;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * 基础信息管理 打处对象管理
 */
@Controller
@RequestMapping("/suspects")
public class SuspectsController {

	@Autowired
	SuspectsService suspectsService;

	/**
	 * 打处对象管理页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", produces = "text/html;charset=UTF-8")
	public String act_suspectsList(Model model) {
		return "baseInfo/suspects/suspectsList";
	}

	/**
	 * 打处对象管理表格 数据
	 * 
	 * @param pager
	 * @param suspectsParams
	 * @return dataTables 所需要的数据格式
	 */
	@ResponseBody
	@RequestMapping(value = "/json", produces = "application/json;charset=UTF-8")
	public String table_suspectsList(HttpServletRequest request, Page4DataTable pager, SuspectsParams suspectsParams) {
		JSONObject res = suspectsService.getTableData(pager, suspectsParams, (Integer) request.getSession()
				.getAttribute("permission"));
		return res.toString();
	}

	/**
	 * 保存打处对象
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/save", produces = "application/json;charset=UTF-8")
	public String act_saveSuspects(HttpServletRequest request, Suspects suspects, String folderName) {

		folderName = WebTools.dealWithNullVal(folderName);

		if (suspects.getId() != null) {
			Integer permission = (Integer) request.getSession().getAttribute("permission");
			if (permission != 1) {
				return WebTools.convert2JsonResult(0, "无权限");
			}
		}

		String res = suspectsService.saveSuspects(suspects, folderName);

		return res;
	}

	/**
	 * 删除打处对象
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete", produces = "application/json;charset=UTF-8")
	public String act_deleteSuspects(HttpServletRequest request, @RequestParam(value = "id", required = true) Long id) {

		Integer permission = (Integer) request.getSession().getAttribute("permission");

		if (permission != 1) {
			return WebTools.convert2JsonResult(0, "无权限");
		}

		suspectsService.deleteSuspects(id);

		return WebTools.convert2JsonResult(1, "删除成功");
	}

	/**
	 * 新建打处对象页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/show")
	public String showSuspects(Model model) {
		return "baseInfo/suspects/suspectsInfo_show";
	}

	/**
	 * 新建打处对象页面
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/create")
	public String createSuspects(Model model) {
		return "baseInfo/suspects/suspectsInfo";
	}

	/**
	 * 修改打处对象信息
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/edit/data", produces = "application/json;charset=UTF-8")
	public String act_editSuspects(HttpServletRequest request, @RequestParam(value = "id", required = true) Long id) {

		/*Integer permission = (Integer) request.getSession().getAttribute("permission");

		if (permission != 1) {
			return WebTools.convert2JsonResult(0, "无权限");
		}*/

		id = WebTools.dealWithNullVal(id);

		JSONObject res = new JSONObject();

		res = suspectsService.editSuspects(id);

		return res.toString();
	}

	/**
	 * 上传附件图片
	 *
	 * @param request
	 * @param folderName
	 * @param type
	 *            0: 普通附件; 1: 附件图片
	 * @param suspectsId
	 * @param isUpload
	 *            0: 不上传; 1: 上传
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/upload", produces = "application/json;charset=UTF-8")
	public String act_upload(MultipartHttpServletRequest request, String folderName, Integer type,
							 Long suspectsId, Integer isUpload) {
		folderName = WebTools.dealWithNullVal(folderName);
		type = WebTools.dealWithNullVal(type);
		isUpload = WebTools.dealWithNullVal(isUpload);
		JSONObject res = suspectsService.uploadFile(request, folderName, type, suspectsId, isUpload);
		return res.toString();
	}

	/**
	 * 删除文件和其数据库中的数据
	 *
	 * @param filePath
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/file/delete", produces = "application/json;charset=UTF-8")
	public String act_fileDelete(String filePath, String id) {
		filePath = WebTools.dealWithNullVal(filePath);
		id = WebTools.dealWithNullVal(id);

		JSONObject res = new JSONObject();

		suspectsService.deleteFileAndDB(filePath, id);

		return res.toString();
	}
}
