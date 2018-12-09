<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">
<meta charset="utf-8" />
<title></title>
<meta name="description" content="overview & stats" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link href="static/css/bootstrap.min.css" rel="stylesheet" />
<link href="static/css/bootstrap-responsive.min.css" rel="stylesheet" />
<link rel="stylesheet" href="static/css/font-awesome.min.css" />
<!-- 下拉框 -->
<link rel="stylesheet" href="static/css/chosen.min.css" />
<link rel="stylesheet" href="static/css/ace.min.css" />
<link rel="stylesheet" href="static/css/ace-responsive.min.css" />
<link rel="stylesheet" href="static/css/ace-skins.min.css" />
<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
<!--提示框-->
<script type="text/javascript" src="static/js/jquery.tips.js"></script>

<script type="text/javascript">
	$(top.hangge());

	//保存
	function save() {
		
		if ($("#role_id").val() == "") {

			$("#role_id").tips({
				side : 3,
				msg : '选择角色',
				bg : '#AE81FF',
				time : 2
			});

			$("#role_id").focus();
			return false;
		}
		if ($("#customer_id").val() == "") {

			$("#customer_id").tips({
				side : 3,
				msg : '选择投资人',
				bg : '#AE81FF',
				time : 2
			});

			$("#customer_id").focus();
			return false;
		}
		if ($("#cinema_id").val() == "") {

			$("#cinema_id").tips({
				side : 3,
				msg : '选择影院',
				bg : '#AE81FF',
				time : 2
			});

			$("#cinema_id").focus();
			return false;
		}
		if ($("#loginname").val() == "") {

			$("#loginname").tips({
				side : 3,
				msg : '输入申请人',
				bg : '#AE81FF',
				time : 3
			});
			$("#loginname").focus();
			return false;
		} else {
			$("#loginname").val($.trim($("#loginname").val()));
		}
		$("#userForm").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
	}
</script>
</head>
<body style="overflow-x: hidden; overflow-y: hidden;">
	<form action="fcode/saveFcode.do" name="userForm" id="userForm"
		method="post">
		<input type="hidden" name="USER_ID" id="user_id"
			value="${pd.USER_ID }" />
		<div id="zhongxin">
			<table>
				<c:if test="${fx != 'head'}">
					<c:if test="${pd.CUSTOMER_ID!= '1'}">
						<tr class="info" height="50">
							<td style="width: 80px; text-align: right">所属投资人:</td>
							<td><select class="chzn-select" name="CUSTOMER_ID"
								id="customer_id" data-placeholder="请选择投资人"
								style="vertical-align: top; width: 300px;">
									<option value=""></option>
									<c:forEach items="${fcustomerList}" var="customer">
										<option value="${customer.CUSTOMERID }"
											<c:if test="${customer.CUSTOMERID == pd.CUSTOMER_ID }">selected</c:if>>${customer.CUSTOMERNAME }</option>
									</c:forEach>
							</select></td>
						</tr>
					</c:if>
					<c:if test="${pd.CUSTOMER_ID == '1'}">
						<input name="CUSTOMER_ID" id="customer_id" value="1" type="hidden" />
					</c:if>
				</c:if>

				<c:if test="${fx == 'head'}">
					<input name="CUSTOMER_ID" id="customer_id"
						value="${pd.CUSTOMER_ID }" type="hidden" />
				</c:if>


				<c:if test="${fx != 'head'}">
					<c:if test="${pd.ROLE_ID != '1'}">
						<tr class="info" height="50">
							<td style="width: 80px; text-align: right">角色权限:</td>
							<td><select class="chzn-select" name="ROLE_ID" id="role_id"
								data-placeholder="请选择角色"
								style="vertical-align: top; width: 300px;">
									<option value=""></option>
									<c:forEach items="${froleList}" var="role">
										<option value="${role.roleID }"
											<c:if test="${role.roleID == pd.ROLE_ID }">selected</c:if>>${role.roleName }</option>
									</c:forEach>
							</select></td>
						</tr>
					</c:if>
					<c:if test="${pd.ROLE_ID == '1'}">
						<input name="ROLE_ID" id="role_id" value="1" type="hidden" />
					</c:if>
				</c:if>

				<c:if test="${fx == 'head'}">
					<input name="ROLE_ID" id="role_id" value="${pd.ROLE_ID }"
						type="hidden" />
				</c:if>

				<tr class="info" height="50">
					<td style="width: 80px; text-align: right">所属影院:</td>
					<td><select class="chzn-select" name="CINEMA_ID"
						id="cinema_id" data-placeholder="请选择影院"
						style="vertical-align: top; width: 300px;">
							<option value=""></option>
					</select></td>
				</tr>
				<tr height="50">
					<td style="width: 80px; text-align: right">申请人:</td>
					<td><input type="text" name="USERNAME" id="loginname"
						value="${pd.USERNAME }" maxlength="32" placeholder="申请人"
						title="申请人" style="width: 286px;" /></td>
				</tr>
				<tr>
					<td style="text-align: center;" colspan=2><a
						class="btn btn-mini btn-primary" onclick="save();">保存</a> <a
						class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
					</td>
				</tr>
			</table>
		</div>

		<div id="zhongxin2" class="center" style="display: none">
			<br /> <br /> <br /> <br /> <img src="static/images/jiazai.gif" /><br />
			<h4 class="lighter block green"></h4>
		</div>

	</form>

	<!-- 引入 -->
	<script type="text/javascript">
		window.jQuery
				|| document
						.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");
	</script>
	<script src="static/js/bootstrap.min.js"></script>
	<script src="static/js/ace-elements.min.js"></script>
	<script src="static/js/ace.min.js"></script>
	<script type="text/javascript" src="static/js/chosen.jquery.js"></script>
	<script type="text/javascript" src="static/js/jquery.cookie.js"></script>
	<!-- 下拉框 -->

	<script type="text/javascript">
		/* $(top.hangge()); */
		$(function() {
			top.hangge();
			//单选框
			$(".chzn-select").chosen();
			$(".chzn-select-deselect").chosen({
				allow_single_deselect : true
			});
			$("#cinema_id").empty();
			var loginname = $.cookie('loginname');
			$("#loginname").val(loginname);
		});

		$(function() {
			//注册教师下拉框事件  
			$("#customer_id").change(function() {
				changChild($(this).val());
			});
		});

		function changChild(tid) {
			 var url = "<%=basePath%>fcode/getTheater.do?customerID=" + tid
					+ "&tm=" + new Date().getTime();
			var option = [];
			$.get(url, function(data) {
				debugger;
				// $("#cinema_id").empty();
				var length = $(data)[5][0].length;
				//var html = ""; 
				for (i = 0; i < length; i++) {
					//html = html + $(data)[5][0][i];
					//$("#cinema_id").append($(data)[5][0][i]);
					var name = $(data)[5][0][i].innerHTML;
					var value = $(data)[5][0][i].value;
					option.push('<option value=',value,'>', name,
									'</option>');
				}
				$("#cinema_id").html(option.join(''));
				//$("#cinema_id").chosen()
				$("#cinema_id").trigger('chosen:updated');
			});
		}
	</script>

</body>
</html>