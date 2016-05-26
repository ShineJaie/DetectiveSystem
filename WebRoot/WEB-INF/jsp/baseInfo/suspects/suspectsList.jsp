<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <jsp:include page="../../include/include_css.jsp"></jsp:include>
    <title>用户管理</title>
</head>
<body>

<jsp:include page="../../include/header.jsp">
    <jsp:param value="baseInfo" name="activePage"/>
</jsp:include>

<div class="container">
    <%--<div class="row">--%>
        <%--<div class="col-md-4">--%>
            <%--<jsp:include page="../baseInfoMenu.jsp">--%>
                <%--<jsp:param value="suspects" name="activePage"/>--%>
            <%--</jsp:include>--%>
        <%--</div>--%>
        <div class="inner_container">
            <h4>打处对象管理</h4>

            <table style="margin: 8px;">
                <tr>
                    <td>
                        <lable>人员</lable>
                    </td>
                    <td style="padding: 6px;"><input type="text"
                                                     class="form-control search_param" para_name="name"
                                                     placeholder="人员"></td>

                    <td style="padding-left: 15px;">
                        <lable>绰号</lable>
                    </td>
                    <td><input type="text"
                               class="form-control search_param" para_name="nickname"
                               placeholder="绰号"></td>
                </tr>
                <tr>
                    <td>
                        <lable>手机号</lable>
                    </td>
                    <td style="padding: 6px;"><input type="text"
                                                     class="form-control search_param" para_name="mobilephone"
                                                     placeholder="手机号"></td>

                    <td style="padding-left: 15px;">
                        <lable>关系人</lable>
                    </td>
                    <td><input type="text"
                               class="form-control search_param" para_name="relationMan"
                               placeholder="关系人"></td>
                </tr>
                <tr>
                </tr>
                <tr>
                    <td>
                        <lable>QQ号</lable>
                    </td>
                    <td style="padding: 6px;"><input type="text"
                                                     class="form-control search_param" para_name="qq"
                                                     placeholder="QQ号"></td>

                    <td style="padding-left: 15px;">
                        <lable>微信号</lable>
                    </td>
                    <td><input type="text"
                               class="form-control search_param" para_name="wechat"
                               placeholder="微信号"></td>
                </tr>
                <tr>
                    <td>
                        <lable>作案手法</lable>
                    </td>
                    <td style="padding: 6px;"><input type="text"
                                                     class="form-control search_param" para_name="modusOperandi"
                                                     placeholder="作案手法"></td>
                    <td style="padding-left: 15px;">
                        <button type="button" class="btn btn-info act_search">
                            <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
                            查询
                        </button>
                    </td>
                </tr>
            </table>

            <table id="suspectsList" class="display">
                <thead>
                <tr>
                    <th>人员</th>
                    <th>绰号</th>
                    <th>身份证号</th>
                    <th>手机号码</th>
                    <th>QQ号</th>
                    <th>微信号</th>
                    <th>关系人</th>
                    <th>简要案情</th>
                    <th>作案手法</th>
                    <th>备注</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>

                </tbody>
            </table>

            <div>
                <a href="./create" class="btn btn-primary"><span
                        class="glyphicon glyphicon-plus" aria-hidden="true"></span> 新建打处对象</a>
            </div>
        </div>
    <%--</div>--%>
</div>

<jsp:include page="../../include/include_js.jsp"></jsp:include>

<script type="text/javascript">
    $(document).ready(function () {
        require(['/DetectiveSystem/uires/js/_v1/main.js'], function (main) {
            require(['suspectsList'], function (suspectsList) {
                suspectsList.initpage_suspectsList();
            });
        });
    });
</script>


</body>
</html>