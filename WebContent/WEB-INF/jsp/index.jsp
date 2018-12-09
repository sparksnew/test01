<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
	content="width=device-width,initial-scale=1,user-scalable=0" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/js/index.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/js/bootstrap.min.js"></script>
<link href="${pageContext.request.contextPath}/static/css/index.css"
	rel="stylesheet" type="text/css" />
<link
	href="${pageContext.request.contextPath}/static/css/bootstrap.min.css"
	rel="stylesheet" type="text/css" />
<link
	href="${pageContext.request.contextPath}/static/css/bootstrap-responsive.min.css"
	rel="stylesheet" type="text/css" />
<link
	href="${pageContext.request.contextPath}/static/css/bootstrap-timepicker.css"
	rel="stylesheet" type="text/css" />
</head>
<body>
	<ul id="myTab" class="nav nav-tabs">
		<li class="active"><a href="#cinema" data-toggle="tab">影院</a></li>
		<li><a href="#fcode" data-toggle="tab">邀请码</a></li>
		<li><a href="#customer" data-toggle="tab">客户</a></li>
		<li><a href="#appRole" data-toggle="tab">手机角色</a></li>
		<li><a href="#module" data-toggle="tab">模块</a></li>
	</ul>
	<div id="myTabContent" class="tab-content">
		<div class="tab-pane fade in active" id="cinema">
			<form action="cinema/list.do" method="post" name="cinemaForm"
				id="cinemaForm">
				<table>
					<tr>
						<td><span class="input-icon"> <input
								autocomplete="off" id="nav-search-input" type="text"
								name="USERNAME" value="${pd.USERNAME }" placeholder="这里输入关键词" />
								<!-- <i id="nav-search-icon" class="icon-search"></i> -->
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
								<th>编号</th>
								<th>名称</th>
								<th>编码</th>
								<th>地址</th>
								<th class="center">操作</th>
							</tr>
						</thead>
						<tbody>
							<!-- 开始循环 -->
							<c:forEach items="${cinemaList}" var="cinema">
								<tr>
									<td>${cinema.CINEMAID }</td>
									<td><a>${cinema.CINEMANAME }</a></td>
									<td>${cinema.CINEMACODE }</td>
									<td>${cinema.ADRESS }</td>
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
			</form>
		</div>
		<div class="tab-pane fade" id="fcode">
			<form action="fcode/list.do" method="post" name="fcodeForm"
				id="fcodeForm">
				<table>
					<tr>
						<td><span class="input-icon"> <input
								autocomplete="off" id="nav-search-input" type="text"
								name="USERNAME" value="${pd.USERNAME }" placeholder="这里输入关键词" />
								<!-- <i id="nav-search-icon" class="icon-search"></i> -->
						</span></td>
						<td><input class="span10 date-picker" name="lastLoginStart"
							id="lastLoginStart" value="${pd.lastLoginStart}" type="text"
							data-date-format="yyyy-mm-dd" readonly="readonly"
							style="width: 88px;" placeholder="开始日期" title="最近登录开始" /></td>
						<td><input class="span10 date-picker" name="lastLoginEnd"
							name="lastLoginEnd" value="${pd.lastLoginEnd}" type="text"
							data-date-format="yyyy-mm-dd" readonly="readonly"
							style="width: 88px;" placeholder="结束日期" title="最近登录结束" /></td>
						<td style="vertical-align: top;"><select class="chzn-select"
							name="ROLE_NAME" id="roleName" data-placeholder="请选择角色"
							style="vertical-align: top; width: 120px;">
								<option value=""></option>
								<option value="">全部</option>
								<c:forEach items="${froleList}" var="role">
									<option value="${role.roleName }"
										<c:if test="${pd.roleName==role.roleName}">selected</c:if>>${role.roleName }</option>
								</c:forEach>
						</select></td>
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
								<th>编号</th>
								<th>激活码</th>
								<th>创建时间</th>
								<th>发放者</th>
								<th>发放平台</th>
								<th>角色</th>
								<th>投资人</th>
								<th>城市</th>
								<th>影院</th>
								<th>申请人</th>
								<th>手机号</th>
								<th>有效时长(时)</th>
								<th>激活时间</th>
								<th>状态</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<!-- 开始循环 -->
							<c:choose>
								<c:when test="${not empty fcodeList}">
									<c:forEach items="${fcodeList}" var="fcode">
										<tr>
											<td>${fcode.invitationID}</td>
											<td><a>${fcode.fCode}</a></td>
											<td>${fcode.createDate}</td>
											<td>${fcode.userName}</td>
											<td>${fcode.deviceType}</td>
											<td>${fcode.roleName}</td>
											<td>${fcode.customerName}</td>
											<td>${fcode.cityName}</td>
											<td>${fcode.cinemaName}</td>
											<td>${fcode.applicant}</td>
											<td>${fcode.mobile}</td>
											<td>${fcode.timeLong}</td>
											<td>${fcode.activeTime}</td>
											<td>${fcode.codeStatus}</td>
											<td style="width: 60px;"><a
												class='btn btn-mini btn-danger' title="删除"
												onclick="control('${fcode.invitationID }');"><i
													class='icon-trash'></i></a></td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr class="main_info">
										<td colspan="10" class="center">没有相关数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
					<div class="page-header position-relative">
						<table style="width: 100%;">
							<tr>
								<td style="vertical-align: top;"><a
									class="btn btn-small btn-success" onclick="add();">新增</a></td>
								<td style="vertical-align: top;">
									<div class="pagination"
										style="float: right; padding-top: 0px; margin-top: 0px;">
										${page.pageStr}</div>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</form>
		</div>
		<!--/row-->
		<div class="tab-pane fade" id="customer">
			<form action="customer/list.do" method="post" name="customerForm"
				id="customerForm">
				<table>
					<tr>
						<td><span class="input-icon"> <input
								autocomplete="off" id="nav-search-input" type="text"
								name="USERNAME" value="${pd.USERNAME }" placeholder="这里输入关键词" />
								<!-- <i id="nav-search-icon" class="icon-search"></i> -->
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
								<th>编号</th>
								<th>名称</th>
								<th>简称</th>
								<th>联系人</th>
								<th>联系方式</th>
								<th>地址</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<!-- 开始循环 -->
							<c:forEach items="${customerList}" var="customer">
								<tr>
									<td>${customer.CUSTOMERID}</td>
									<td><a>${customer.CUSTOMERNAME}</a></td>
									<td>${customer.SHORTNAME}</td>
									<td>${customer.CONTACT}</td>
									<td>${customer.TEL}</td>
									<td>${customer.ADRESS}</td>

									<td style="width: 60px;"><input type="button" value="操作"
										onclick="control('${customer.CUSTOMERID }');"></input></td>
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
			</form>
		</div>
		<div class="tab-pane fade" id="appRole">
			<form action="appRole/list.do" method="post" name="appRoleForm"
				id="appRoleForm">
				<table>
					<tr>
						<td><span class="input-icon"> <input
								autocomplete="off" id="nav-search-input" type="text"
								name="USERNAME" value="${pd.USERNAME }" placeholder="这里输入关键词" />
								<!-- <i id="nav-search-icon" class="icon-search"></i> -->
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
			</form>
		</div>
		<!--/row-->
		<div class="tab-pane fade" id="module">
			<form action="appFunction/list.do" method="post"
				name="appFunctionForm" id="appFunctionForm">
				<table>
					<tr>
						<td><span class="input-icon"> <input
								autocomplete="off" id="nav-search-input" type="text"
								name="USERNAME" value="${pd.USERNAME }" placeholder="这里输入关键词" />
								<!-- <i id="nav-search-icon" class="icon-search"></i> -->
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
								<th>编号</th>
								<th>模块名称</th>
								<th>模块编码</th>
								<th>模块背景色</th>
								<th>模块图片名称</th>
								<th class="center">操作</th>
							</tr>
						</thead>
						<tbody>
							<!-- 开始循环 -->
							<c:forEach items="${functionList}" var="function">
								<tr>
									<td>${function.moduleID }</td>
									<td><a>${function.moduleName }</a></td>
									<td>${function.moduleCode }</td>
									<td>${function.modulebgColor }</td>
									<td>${function.moduleImageName }</td>
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
			</form>
		</div>
	</div>
</body>
</html>