package com.view.utils.webTools;

import java.io.Serializable;

import org.json.JSONObject;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 网站工具类
 */
@Component("webTools")
public class WebTools implements Serializable, ApplicationContextAware {
	private static final long serialVersionUID = 1081810445795847343L;

	/** ApplicationContext */
	public static ApplicationContext appContext = null;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		appContext = applicationContext;
	}

	public ApplicationContext getAppContext() {
		return appContext;
	}

	/**
	 * 处理 null 字符串
	 * 
	 * @param str
	 * @return null 输出"", 否则输出 .trim()
	 */
	public static String dealWithNullVal(String str) {
		if (str == null) {
			return "";
		} else {
			return str.trim();
		}
	}

	/**
	 * 处理 null 数字
	 * 
	 * @param input
	 * @return null 返回 0
	 */
	public static Integer dealWithNullVal(Integer input) {
		if (input == null) {
			return 0;
		} else {
			return input;
		}
	}
	
	/**
	 * 处理 null 数字
	 * 
	 * @param input
	 * @return null 返回 0
	 */
	public static Long dealWithNullVal(Long input) {
		if (input == null) {
			return 0L;
		} else {
			return input;
		}
	}

	/**
	 * 处理 null 数字
	 * 
	 * @param input
	 * @return null 返回 0d
	 */
	public static Double dealWithNullVal(Double input) {
		if (input == null || input.isInfinite() || input.isNaN()) {
			return 0d;
		} else {
			return input;
		}
	}

	/**
	 * 程序返回结果转换成json字符串, 给提示框使用
	 * 
	 * @param status
	 *            状态
	 * @param data
	 *            显示内容
	 * @return {"status", "data"}
	 */
	public static String convert2JsonResult(String status, String data) {
		JSONObject res = new JSONObject();
		res.put("status", status);
		res.put("data", data);
		return res.toString();
	}

	/**
	 * 程序返回结果转换成json字符串, 给提示框使用
	 * 
	 * @param status
	 *            状态, 0:error, 1:success
	 * @param data
	 *            显示内容
	 * @return {"error/success", "success"}
	 */
	public static String convert2JsonResult(Integer status, String data) {
		JSONObject res = new JSONObject();

		if (0 == status) {
			res.put("status", "error");
		} else if (1 == status) {
			res.put("status", "success");
		}

		res.put("data", data);
		return res.toString();
	}

}
