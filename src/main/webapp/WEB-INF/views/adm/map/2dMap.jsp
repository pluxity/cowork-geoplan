<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
        <spring:eval expression="@prop['kakao.api.key']" var="kakaoApiKey" />
        <sec:authentication var="principal" property="principal" />

        <!doctype html>
        <html lang="ko">

        <head>
            <meta charset="UTF-8">
            <meta http-equiv="X-UA-Compatible" content="IE=edge">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <meta http-equiv="Cache-Control" content="no-cache">
            <meta http-equiv="Pragma" content="no-cache">
            <meta http-equiv="Expires" content="-1">
            <title>
                <spring:message code='html.head.title' />
            </title>
            <link href="<c:url value='/resources/img/favicon.ico' />" type="image/x-icon" rel="shortcut icon" />
            <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/common/reset.css' />">
            <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/adm/2dMap.css' />">
        </head>

        <body>
            <div id="map" data-lat="${mapInfo.lat}" data-lng="${mapInfo.lng}" style="width: 100vw; height: 100vh;"></div>
            <div class="searchArea">
                <div id="search">
                    <button><span></span></button>
                    <input type="text" id="searchInput" placeholder="검색어를 입력하세요.">
                </div>
                <div id="searchList"></div>
            </div>
            <script src="<c:url value='/resources/js/jquery/jquery.min.js' />"></script>
	        <script src="<c:url value='/resources/js/jquery/jquery.easing.min.js' />"></script>
	        <script src="<c:url value='/resources/js/jquery/jquery.form.min.js' />"></script>
            <script type="text/javascript"
                src="//dapi.kakao.com/v2/maps/sdk.js?appkey=${kakaoApiKey}&libraries=services"></script>
            <script type="text/javascript" src="<c:url value='/resources/js/cmn/pluxityMap.js'/>"></script>
            <!-- 페이지 스크립트 -->
            <script type="text/javascript" src="<c:url value='/resources/js/adm/map/2dMap.js'/>"></script>
        </body>

        </html>