<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="../../include/include_css.jsp"></jsp:include>
<style type="text/css" class="init">
td.details-control {
	background:
		url('/DetectiveSystem/uires/DataTables-1.10.7/examples/resources/details_open.png')
		no-repeat center center;
	cursor: pointer;
}

tr.shown td.details-control {
	background:
		url('/DetectiveSystem/uires/DataTables-1.10.7/examples/resources/details_close.png')
		no-repeat center center;
}
</style>
<title>用户管理</title>
</head>
<body>

	<jsp:include page="../../include/header.jsp">
		<jsp:param value="system" name="activePage" />
	</jsp:include>

	<div class="container">
		<div class="row">
			<div class="col-md-4">
				<jsp:include page="../systemMenu.jsp">
					<jsp:param value="user" name="activePage" />
				</jsp:include>
			</div>
			<div class="col-md-8">
				<h4>用户管理</h4>

				<table style="margin: 8px;">
					<tr>
						<td><lable>姓名</lable></td>
						<td style="padding-left: 6px;"><input type="text"
							class="form-control search_realName" placeholder="姓名"></td>
						<td style="padding-left: 6px;">
							<button type="button" class="btn btn-info act_search">
								<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
								查询
							</button>
						</td>
					</tr>
				</table>

				<table id="userList" class="display">
					<thead>
						<tr>
							<th></th>
							<th>创建时间</th>
							<th>用户登录名</th>
							<th>用户名称</th>
							<th>用户权限</th>
							<th>用户邮箱</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>

					</tbody>
				</table>

				<div>
					<a href="./create" class="btn btn-primary"><span
						class="glyphicon glyphicon-plus" aria-hidden="true"></span> 新建用户</a>
				</div>
			</div>
		</div>
	</div>

	<jsp:include page="../../include/include_js.jsp"></jsp:include>

	<script type="text/javascript">
		$(document).ready(function() {
			require([ '/DetectiveSystem/uires/js/_v1/main.js' ], function(main) {
				require([ 'userList' ], function(userList) {
					userList.initpage_userList();
				});
			});
		});
	</script>


</body>
</html>