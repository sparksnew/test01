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
				<form action="fcode/list.do" method="post" name="fcodeForm"
					id="fcodeForm">
					<table>
						<tr>
							<td><span class="input-icon"> <input
									autocomplete="off" id="nav-search-input" type="text"
									name="USERNAME" value="${pd.USERNAME }" placeholder="这里输入关键词" />
									<i id="nav-search-icon" class="icon-search"></i>
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
	<script type="text/javascript">
		window.jQuery || document.write("__tag_128$66_\x3C/script>");
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
			$("#fcodeForm").submit();
		};

		//菜单权限
		function control(ROLE_ID) {
          bootbox.confirm("确定要删除["+ROLE_ID+"]吗?", function(result) {
			if(result) {
				top.jzts();
			       <%--  $.ajax({
				         type: "POST",
				         url: '<%=basePath%>fcode/Delete.do',
				         data : {
					         ROLE_ID : ROLE_ID
				         },
				         dataType : 'json',
				         cache : false,
				         success : function(data) { 
                              debugger;
                              nextPage(${page.currentPage});
				         }
			        }); --%>
			        var url = "<%=basePath%>fcode/Delete.do?ROLE_ID="+ROLE_ID+"&tm="+new Date().getTime();
					$.get(url,function(data){
						nextPage(${page.currentPage});
					});
			}
		 });
	   };
       $(function() {
			
			//日期框
			$('.date-picker').datepicker();
			
			//下拉框
			$(".chzn-select").chosen(); 
			$(".chzn-select-deselect").chosen({allow_single_deselect:true}); 
			
			//复选框
			$('table th input:checkbox').on('click' , function(){
				var that = this;
				$(this).closest('table').find('tr > td:first-child input:checkbox')
				.each(function(){
					this.checked = that.checked;
					$(this).closest('tr').toggleClass('selected');
				});
					
			});
			
		});
       
        //新增
		function add(){
			top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="激活码发放";
			 diag.URL = '<%=basePath%>fcode/newcode.do';
			 diag.Width = 450;
			 diag.Height = 400;
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 if('${page.currentPage}' == '0'){
						 top.jzts();
						 setTimeout("self.location=self.location",100);
					 }else{
						 nextPage(${page.currentPage});
					 }
				}
				diag.close();
			 };
			 diag.show();
		}
	</script>

</body>
</html>
