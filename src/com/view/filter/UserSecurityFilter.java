package com.view.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserSecurityFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException,
			ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;

		String pathInfo = request.getPathInfo();
		if (pathInfo.endsWith("login") || pathInfo.endsWith("loginout") || pathInfo.endsWith("verify")
				|| pathInfo.endsWith("home")) {
			arg2.doFilter(arg0, arg1);
		} else {
			String username = request.getSession().getAttribute("username") == null ? null : request.getSession()
					.getAttribute("username").toString();
			if (username == null) {
				response.sendRedirect(request.getContextPath() + "/action/login");
			} else {
				arg2.doFilter(arg0, arg1);
			}
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
