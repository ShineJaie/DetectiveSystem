<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<title>用户信息</title>
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
				<form class="form-horizontal form_user">
					<input type="hidden" name="id" id="id" />

					<fieldset>

						<legend>用户信息</legend>

						<div class="form-group">
							<label class="col-sm-2 control-label">用户账号</label>
							<div class="col-sm-10">
								<input type="text" class="form-control search_param"
									para_name="loginName" placeholder="用户账号">
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label">用户密码</label>
							<div class="col-sm-10">
								<input type="password" class="form-control search_param"
									para_name="password" placeholder="用户密码">
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label">用户姓名</label>
							<div class="col-sm-10">
								<input type="text" class="form-control search_param"
									para_name="realName" placeholder="用户姓名">
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label">用户邮箱</label>
							<div class="col-sm-10">
								<input type="email" class="form-control search_param"
									para_name="email" placeholder="用户邮箱">
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label">用户权限</label>
							<div class="col-sm-10">
								<select class="form-control search_param" para_name="permission">
									<option value=0>普通权限</option>
									<option value=1>超级管理员</option>
								</select>
							</div>
						</div>

					</fieldset>


					<div style="margin-left: 220px;">
						<input type="button" class="btn btn-primary act_save" value="保存" />
						<a href="./list" class="btn btn-danger"> 取消</a>
					</div>
				</form>
			</div>
		</div>
	</div>

	<jsp:include page="../../include/include_js.jsp"></jsp:include>

	<script type="text/javascript">
		$(document).ready(function() {
			require([ '/DetectiveSystem/uires/js/_v1/main.js' ], function(main) {
				require([ 'userList' ], function(userList) {
					userList.initpage_userInfo();
				});
			});
		});
	</script>
</body>
</html>