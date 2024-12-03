<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="description" content="">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>이벤트테스트</title>
<script src="<c:url value='/resources/js/jquery/jquery.min.js' />"></script>
<script>
$(function() {

	$("#btnTest1").click(function() {


		$("#result1").text("");

		var sampleData1 = {
				"building_id": "0001",
			    "service_type": $("#serviceType").val(),
			    "object_id": $("#dvcCd").val(),
			    "object_name": "테스트",
			    "alarm_grade_cd": $("#dvcStatus").val(),
			    "event_name": "테스트이벤트",
			    "event_time": Date.prototype.yyyymmdd() + " " + Date.prototype.hhmmss(),
			    "event_seq": $("#eventSeq").val() || Date.now()
		};

		$.ajax({
			type:"POST",
			dataType:"json",
			beforeSend : function(xhr){
	            xhr.setRequestHeader("Call-Key", "d0569057-2f97-4d51-87b6-da61c0147801");
	            xhr.setRequestHeader("Content-type","application/json");
	        },
			data: JSON.stringify(sampleData1),
			contentType:'application/json',
			url:"/itf/event/receive",
			success: function(res) {
				console.log(res);
				$("#result1").text(JSON.stringify(sampleData1) + JSON.stringify(res));
			},
			error: function(e) {
				//alert(e);
			}
		});

	});
});

var today = new Date();
Date.prototype.yyyymmdd = function() {
    var yyyy = today.getFullYear().toString();
    var mm = (today.getMonth() + 1).toString();
    var dd = today.getDate().toString();
    return  yyyy + "-" + (mm[1] ? mm : "0" + mm[0]) + "-" + (dd[1] ? dd : "0" + dd[0]);
}
Date.prototype.hhmmss = function() {
    var hh = today.getHours().toString();
    var mm = today.getMinutes().toString();
    var ss = today.getSeconds().toString();
    return (hh[1] ? hh : "0" + hh[0]) + ":" + (mm[1] ? mm : "0" + mm[0]) + ":" + (ss[1] ? ss : "0" + ss[0]);
}
</script>
</head>
<body>
<p>
	서비스타입
	<select id="serviceType">
		<option value="AC">AC</option>
		<option value="FM">FM</option>
		<option value="EL">EL</option>
	</select>
	객체코드
	<input type="text" id="dvcCd" value="">
	상태
	<select id="dvcStatus">
		<option value="0">정상</option>
		<option value="1">1등급</option>
		<option value="2">2등급</option>
		<option value="3">3등급</option>
		<option value="4">기타</option>
	</select>
	이벤트SEQ
	<input type="text" id="eventSeq" value="">
	<button id="btnTest1">발생</button>
</p>
<div id="result1" style="background-color:#000;color:#fff;padding:5px;height:100px;"></div>
</body>
</html>