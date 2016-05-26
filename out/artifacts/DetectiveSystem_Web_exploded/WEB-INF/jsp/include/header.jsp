<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<nav class="navbar navbar-inverse">
	<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
				<span class="sr-only">杜桥责任区刑警队打处对象登记系统</span> <span class="icon-bar"></span>
				<span class="icon-bar"></span> <span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="javascript:void(0);">杜桥责任区刑警队打处对象登记系统</a>
		</div>
		<div class="collapse navbar-collapse"
			id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">
				<li
					<c:if test="${ param.activePage eq 'baseInfo' }">class="active"</c:if>><a
					href="../suspects/list">基础信息</a></li>
			</ul>
			<c:if test="${ sessionScope.permission eq 1 }">
				<ul class="nav navbar-nav">
					<li
						<c:if test="${ param.activePage eq 'system' }">class="active"</c:if>><a
						href="../user/list">系统管理</a></li>
				</ul>
			</c:if>
			<ul class="nav navbar-nav navbar-right">
				<li><a href="">欢迎：${ sessionScope.username }</a></li>
				<li><a href="../loginout">注销</a></li>
			</ul>
		</div>
		<!-- /.navbar-collapse -->
	</div>
	<!-- /.container -->
</nav>