<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
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
    <title>打处对象信息</title>
</head>
<body>

<jsp:include page="../../include/header.jsp">
    <jsp:param value="baseInfo" name="activePage"/>
</jsp:include>

<div class="container">
    <div class="row">
        <div class="col-md-4">
            <jsp:include page="../baseInfoMenu.jsp">
                <jsp:param value="suspects" name="activePage"/>
            </jsp:include>
        </div>
        <div class="col-md-8 inner_container">
            <form class="form-horizontal form_user">
                <input type="hidden" name="id" id="id"/>

                <fieldset>

                    <legend>打处对象详细信息</legend>

                    <div class="form-group imgs_content">

                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">人员姓名</label>

                        <div class="col-sm-9">
                            <input type="text" class="form-control add_param"
                                   para_name="name" disabled>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">绰号</label>

                        <div class="col-sm-9">
                            <input type="text" class="form-control add_param"
                                   para_name="nickname" disabled>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">身份证号</label>

                        <div class="col-sm-9">
                            <input type="text" class="form-control add_param"
                                   para_name="idCard" disabled>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">手机号码</label>

                        <div class="col-sm-9">
                            <input type="text" class="form-control add_param"
                                   para_name="mobilephone" disabled>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">QQ号</label>

                        <div class="col-sm-9">
                            <input type="text" class="form-control add_param"
                                   para_name="qq" disabled>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">微信号</label>

                        <div class="col-sm-9">
                            <input type="text" class="form-control add_param"
                                   para_name="wechat" disabled>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">关系人</label>

                        <div class="col-sm-9">
                            <input type="text" class="form-control add_param"
                                   para_name="relationMan" disabled>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">简要案情</label>

                        <div class="col-sm-9">
								<textarea class="form-control add_param"
                                          para_name="caseBrief" rows="3" disabled></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">作案手法</label>

                        <div class="col-sm-9">
                            <input type="text" class="form-control add_param"
                                   para_name="modusOperandi" disabled>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">备注</label>

                        <div class="col-sm-9">
                            <input type="text" class="form-control add_param"
                                   para_name="remark" disabled>
                        </div>
                    </div>


                </fieldset>


                <div style="margin-left: 220px;">
                    <a href="./list" class="btn btn-danger"> 返回</a>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="../../include/include_js.jsp"></jsp:include>

<script type="text/javascript">

</script>
</body>
</html>