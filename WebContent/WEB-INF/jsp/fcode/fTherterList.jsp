<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="fcode/getTheater.do" name="userForm" id="userForm">
		<td><select class="chzn-select" name="CINEMA_ID" id="cinema_id"
			data-placeholder="请选择影院" style="vertical-align: top; width: 300px;">
				<option value=""></option>
				<c:forEach items="${fTheaterList}" var="theater">
					<option value="${theater.cinemaID }"
						<c:if test="${theater.cinemaID == pd.CINEMA_ID }">selected</c:if>>${theater.cinemaName }</option>
				</c:forEach>
		</select></td>
	</form>
</body>
</html>