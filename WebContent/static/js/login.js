$(function() {
	$("#login").click(
			function() {
				var username = $("#username").val();
				var userpass = $("#userpass").val();
				if (username == null || username == ""
						|| typeof (username) == undefined) {
					alert("用户名不能为空");
					return;
				}
				if (userpass == null || userpass == ""
						|| typeof (userpass) == undefined) {
					alert("密码不能为空");
					return;
				}
				$.ajax({
					url : "http://localhost:8080/cinemaPro/admin/loginAction",
					type : "get",
					dataType : "json",
					contentType : "application/json",
					data : {
						"username" : username,
						"userpass" : userpass
					},
					success : function(data) {
						debugger;
						if (data.status == 0) {
							alert("用户名或者密码错误");
							return;
						} else {
							window.location.href = "/cinemaPro/admin/to_index";
						}
					}
				});
				return false;
			});
});