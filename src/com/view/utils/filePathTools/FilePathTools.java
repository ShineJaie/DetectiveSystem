package com.view.utils.filePathTools;


import com.view.web.controller.system.login.LoginController;

/**
 * 获取目录路径的相关工具类
 */
public class FilePathTools {

	/**
	 * 获取webapp项目的WEB-INF目录地址
	 * 
	 * @return .../DetectiveSystem/WEB-INF/
	 */
	public static String getWebInfPath() {
		String path = LoginController.class.getClassLoader().getResource("").getPath().replace("/classes/", "/");
		return path;
	}

	/**
	 * 获取服务器的upload文件夹
	 *
	 * @return .../DetectiveSystem/upload/
	 */
	public static String getuploadPath() {
		String path = LoginController.class.getClassLoader().getResource("").getPath()
				.replace("/WEB-INF/classes/", "/upload/");
		return path;
	}

	/**
	 * 获取根目录的路径
	 * 
	 * @return .../DetectiveSystem
	 */
	public static String getRootPath() {
		String path = LoginController.class.getClassLoader().getResource("").getPath()
				.replace("/WEB-INF/classes/", "");
		return path;
	}
}
