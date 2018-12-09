$(function(){  
	$.ajax({
        url : "http://localhost:8080/cinemaPro/admin/list",
        type : "get",
        dataType:"json",
        contentType:"application/json",
        data:{
		 },
        success : function(data) {
        }
    });  
});