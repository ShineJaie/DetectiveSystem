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
<jsp:include page="../jsp/include/include_css.jsp"></jsp:include>
<title>用户登录</title>
</head>
<body>
	<div class="container" style="padding-top: 60px;">

		<div class="row warn_content" style="display: none;">
			<div class="alert alert-danger alert-dismissible" role="alert">
				<button type="button" class="close" data-dismiss="alert"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<span class="glyphicon glyphicon-exclamation-sign"
					aria-hidden="true"></span> <span class="sr-only">Error:</span>
				登录失败，请检查您的用户名和密码！
			</div>
		</div>

		<div class="row">
			<div class="col-md-8">
				<div class="jumbotron" style="background: #99CCCC;">
					<h1>内部管理系统</h1>
					<p style="font-weight: bold;">杜桥责任区刑警队打处对象登记系统</p>
					<p style="font-weight: bold;">开发者：阮吉文</p>
					<p style="font-weight: bold;" hidden="hidden">开发者：吴仙杰</p>
					<div class="alert alert-warning" role="alert">
						<a href="#" class="alert-link">系统不支持IE浏览器，请用谷歌、火狐浏、360等浏览器</a>
					</div>
					<p>
						<!-- <a class="btn btn-primary btn-lg" href="mailto:391940025@qq.com">Email
							Mr.Wu</a> -->
					</p>
				</div>
			</div>

			<div class="col-md-4" style="padding-top: 90px;">
				<form class="form-horizontal">
					<div class="form-group">
						<div class="input-group">
							<input type="text" class="form-control val_loginName"
								name="loginName" placeholder="用户名"><span
								class="input-group-addon glyphicon glyphicon-user"
								style="top: 0px;"></span>
						</div>
					</div>
					<div class="form-group">
						<div class="input-group">
							<input type="password" class="form-control val_password"
								name="password" placeholder="密码"> <span
								class="input-group-addon glyphicon glyphicon-lock"
								style="top: 0px;"></span>
						</div>
					</div>
					<div class="form-group">
						<button type="button" class="btn btn-primary act_login">登录</button>
						<button type="reset" class="btn btn-default">重置</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<jsp:include page="../jsp/include/include_js.jsp"></jsp:include>

	<script type="text/javascript">
		$(document).ready(function() {
			$(".act_login").off("click").on("click", function() {
				var loginName = $(".val_loginName").val();
				var password = $(".val_password").val();
				$.post("/DetectiveSystem/action/login/verify", {
					loginName : loginName,
					password : password
				}, function(data) {
					if (data["status"] == "success") {
						window.location.href = "/DetectiveSystem/action/home";
					} else {
						$(".warn_content").show();
					}
				}, "json");
			});
		});
	</script>

</body>
</html>