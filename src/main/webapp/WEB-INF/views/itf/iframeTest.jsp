<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Document</title>
<style>
.ifrm3d {
    overflow: hidden;
    border: none;
    width: 1800px;
    height: 880px;
}
</style>
</head>
<body>
    <p>
        <select id="floorNo">
            <option value="0">B6</option>
            <option value="1">1F</option>
            <option value="2">7F</option>
            <option value="3">PH2</option>
            <option value="4">실외</option>
        </select>
        <button id="btnChgFloor">층변경</button>
    </p>
    <p>
        <input type="text" id="evtId" value="">
        <button id="btnEvent">이벤트이동</button>
    </p>
    <p>
        <input type="text" id="dvcCd" value="A11">
        <button id="btnDevice">장비이동</button>
    </p>
    <iframe id="ifrm3d" class="ifrm3d" src="http://127.0.0.1:8270/viewer/index.do?mapCd=0001&token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIzRCIsInJvbGUiOiJTMTAiLCJ0ZW5hbnROYW1lIjoiU0vthZTroIjsvaQiLCJibGRJZExpc3QiOltdLCJ0ZW5hbnRJZCI6IjAwMDEiLCJ1c2VyTmFtZSI6Iuy1nO2YhO2YuCIsImV4cCI6MTYxMzk4OTcyNCwidXNlcklkIjoiM0QiLCJpYXQiOjE2MTM5NjgxMjR9.vO2ClZ2sLCOb2GGMQdQGXIPFNhceZXxEvReu1iL6kkvy4E4FQE-gF4TYK8aCLOGzdXzUeoj_1AQJ29AR0WCpmg"></iframe>
</body>

<script>
const btnChgFloor = document.getElementById('btnChgFloor');
const btnEvent = document.getElementById('btnEvent');
const ifrm3d = document.getElementById('ifrm3d');

btnChgFloor.addEventListener('click', function(e) {
    const floorNo = document.getElementById('floorNo').value;
    ifrm3d.contentWindow.postMessage({eventType:'floor', floorNo:floorNo, mapCd:'0001'}, 'http://127.0.0.1:8270');
});

btnEvent.addEventListener('click', function(e) {
    const evtId = document.getElementById('evtId').value;
    ifrm3d.contentWindow.postMessage({eventType:'event', evtId:evtId, mapCd:'0001'}, 'http://127.0.0.1:8270');
});

btnDevice.addEventListener('click', function(e) {
    const dvcCd = document.getElementById('dvcCd').value;
    ifrm3d.contentWindow.postMessage({eventType:'device', dvcCd:dvcCd, mapCd:'0001'}, 'http://127.0.0.1:8270');
});
</script>
</html>
