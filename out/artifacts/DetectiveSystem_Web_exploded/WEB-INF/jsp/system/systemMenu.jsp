<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<jsp:include page="../include/include_css.jsp"></jsp:include>

<div class="well" style="padding: 5px 0px;">
	<ul class="nav nav-pills nav-stacked">
		<li class="nva-header">系统信息管理</li>
		<li <c:if test="${ param.activePage eq 'user' }">class="active"</c:if>><a
			href="../user/list">用户管理</a></li> </ul>
</div>