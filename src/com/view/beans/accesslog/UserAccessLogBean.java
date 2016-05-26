package com.view.beans.accesslog;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 记录用户的访问日志
 * 
 * @author jige1222
 * 
 */
@Component
@Scope("request")
public class UserAccessLogBean implements Serializable {

	private static final long serialVersionUID = -3199802313277109436L;

	public UserAccessLogBean() throws Exception {
		logInfo("get bean ###user:[" + getUserName() + "] ###request: ["
				+ getRequestUrl() + "] ###from [" + getRequestHost() + "]");
	}

	public HttpServletRequest getRequest() {
		HttpServletRequest request = null;
		try {
			ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			request = servletRequestAttributes.getRequest();
		} catch (Exception e) {
			e.printStackTrace();
			Logger.getLogger(getClass()).warn("没有获得request对象，可能是系统自调用");
			request = null;
		}
		return request;
	}

	public String getRequestUrl() {
		String res = "-No Url-";
		HttpServletRequest request = getRequest();
		if (request != null) {
			res = getRequest().getRequestURL().toString();
		}
		return res;
	}

	/**
	 * 获取用户的IP
	 * 
	 * @return 用户的IP; 如果是系统自调用，则返回 "-No Host-"
	 */
	public String getRequestHost() {
		String res = "-No Host-";
		HttpServletRequest request = getRequest();
		if (request != null) {
			res = getRequest().getRemoteHost();
		}
		return res;
	}

	public String getUserName() {
//		String userName = UserService.getCurrentUsername();
//		return userName;
		return null;
	}

	private void logInfo(String msg) {
		Logger.getLogger(getClass()).info(msg);
	}
}
