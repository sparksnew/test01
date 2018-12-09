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
<!-- jsp文件头和头部 -->
<%@ include file="./top.jsp"%>
</head>
<body>


	<div class="container-fluid" id="main-container">


		<div id="page-content" class="clearfix">

			<div class="row-fluid">
				<form action="appRole/list.do" method="post" name="appRoleForm"
					id="appRoleForm">
					<table>
						<tr>
							<td><span class="input-icon"> <input
									autocomplete="off" id="nav-search-input" type="text"
									name="USERNAME" value="${pd.USERNAME }" placeholder="这里输入关键词" />
									<i id="nav-search-icon" class="icon-search"></i>
							</span></td>

							<td style="vertical-align: top;"><button
									class="btn btn-mini btn-light" onclick="search();" title="检索">
									<i id="nav-search-icon" class="icon-search"></i>
								</button></td>
						</tr>
					</table>
					<div class="row-fluid">

						<table id="table_report"
							class="table table-striped table-bordered table-hover">

							<thead>
								<tr>


									<th>角色编号</th>
									<th>角色名称</th>
									<th>级别</th>
									<th>角色介绍</th>
									<th class="center">操作</th>
								</tr>
							</thead>

							<tbody>

								<!-- 开始循环 -->
								<c:forEach items="${roleList}" var="role">
									<tr>

										<td>${role.roleID }</td>
										<td><a>${role.roleName }</a></td>
										<td>${role.level }</td>
										<td>${role.roleDetail }</td>

										<td style="width: 60px;"><input type="button" value="操作"
											onclick="control();"></input></td>
									</tr>
								</c:forEach>



							</tbody>
						</table>
						<div class="page-header position-relative">
							<table style="width: 100%;">
								<tr>

									<td style="vertical-align: top;">
										<div class="pagination"
											style="float: right; padding-top: 0px; margin-top: 0px;">
											${page.pageStr}</div>
									</td>
								</tr>
							</table>
						</div>
					</div>




					<!-- PAGE CONTENT ENDS HERE -->
			</form>
			</div>
			<!--/row-->
		</div>
		<!--/#page-content-->
	</div>
	<!--/.fluid-container#main-container-->

	<!-- 返回顶部  -->
	<a href="#" id="btn-scroll-up" class="btn btn-small btn-inverse"> <i
		class="icon-double-angle-up icon-only"></i>
	</a>

	<!-- 引入 -->
	<script type="text/javascript">
		window.jQuery
				|| document
						.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");
	</script>
	<script src="static/js/bootstrap.min.js"></script>
	<script src="static/js/ace-elements.min.js"></script>
	<script src="static/js/ace.min.js"></script>

	<script type="text/javascript" src="static/js/chosen.jquery.min.js"></script>
	<!-- 下拉框 -->
	<script type="text/javascript"
		src="static/js/bootstrap-datepicker.min.js"></script>
	<!-- 日期框 -->
	<script type="text/javascript" src="static/js/bootbox.min.js"></script>
	<!-- 确认窗口 -->
	<!-- 引入 -->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	<!--提示框-->
	<script type="text/javascript">
		/* $(top.hangge()); */

		//检索
		function search() {
			top.hangge();
			top.jzts();
			$("#appRole").submit();
		}
	</script>

</body>
</html>

