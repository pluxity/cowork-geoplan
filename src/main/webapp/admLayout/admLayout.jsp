<%@ page contentType="text/html; charset=utf-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author" content="">
<title><spring:message code='html.head.title' /></title>
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/adm/admin.css' />">
<link href="<c:url value='/resources/img/favicon.ico' />" type="image/x-icon" rel="shortcut icon" />
<link href="<c:url value='/resources/css/fontawesome/all.min.css' />" rel="stylesheet">
<link href="<c:url value='/resources/css/adm/sb-admin.min.css' />" rel="stylesheet">
<link href="<c:url value='/resources/css/adm/admin.css' />" rel="stylesheet">
<link href="<c:url value='/resources/css/adm/bootstrap/dataTables.bootstrap4.min.css' />" rel="stylesheet">
<link href="<c:url value='/resources/css/adm/bootstrap/bootstrap-colorpicker.min.css' />" rel="stylesheet">
<link href="<c:url value='/resources/css/jquery/jquery-ui.min.css' />" rel="stylesheet">
<link href="<c:url value='/resources/css/adm/tree/hummingbird-treeview.css' />" rel="stylesheet">
</head>

<body id="page-top">
	<t:insertAttribute name="admTop" />
	<div id="wrapper">
		<t:insertAttribute name="admSide" />
		<div id="content-wrapper">
			<t:insertAttribute name="content" />
		</div>
	</div>
	<t:insertAttribute name="admFooter" />
	<c:import url="/WEB-INF/views/cmn/constant.jsp" /> <!-- Constant -->

	<script src="<c:url value='/resources/js/jquery/jquery.min.js' />"></script>
	<script src="<c:url value='/resources/js/jquery/jquery.easing.min.js' />"></script>
	<script src="<c:url value='/resources/js/jquery/jquery.form.min.js' />"></script>
	<script src="<c:url value='/resources/js/jquery/jquery.dataTables.min.js' />"></script>
	<script src="<c:url value='/resources/js/jquery/jquery-ui.min.js' />"></script>
	<script src="<c:url value='/resources/js/jquery/jquery-ui-timepicker-addon.js' />"></script>
	<script src="<c:url value='/resources/js/adm/bootstrap/dataTables.bootstrap4.min.js' />"></script>
	<script src="<c:url value='/resources/js/adm/bootstrap/bootstrap.bundle.min.js' />"></script>
	<script src="<c:url value='/resources/js/adm/bootstrap/bootstrap-colorpicker.min.js' />"></script>
	<script src="<c:url value='/resources/js/jquery/jquery.mtz.monthpicker.js' />"></script>
	<script src="<c:url value='/resources/js/cmn/common.js' />"></script>
	<script src="<c:url value='/resources/js/cmn/validation.js' />"></script>
	<script src="<c:url value='/resources/js/adm/sb-admin.min.js' />"></script>
	<script src="<c:url value='/resources/js/adm/admin.js' />"></script>
	<script src="<c:url value='/resources/js/lib/hummingbird-treeview.js' />"></script>
	<script src="<c:url value='/resources/js/lib/Px.Engine.js' />"></script>
	<script src="/resources/js${fn:replace(requestScope['javax.servlet.forward.request_uri'], '.do', '.js')}" ></script>
	

</body>
</html>
