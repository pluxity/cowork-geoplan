<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:eval expression="@prop['kakao.api.key']" var="kakaoApiKey"/>
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
    <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/viewer/2d/index.css' />">
</head>

<body>
    <div id="navbar">
        <div class="left-area">
            <div id="logo">
<%--                <img src="/resources/img/logo_admin_header.gif" style="width:3rem;"/>--%>
            </div>
            <div>
                <span id="mainTitle">3D 모델기반 실내안전지도</span>
                <span id="subTitle">Viewer</span>
            </div>
        </div>
        <div class="right-area">
            <c:if test="${sessionScope.LOGIN_INFO ne null}">
                <div id="admin"><span></span></div>
            </c:if>
            <c:if test="${sessionScope.LOGIN_INFO ne null}">
                <div id="logout"><span></span></div>
            </c:if>
            <c:if test="${sessionScope.LOGIN_INFO eq null}">
                <div id="login"><span></span></div>
            </c:if>
        </div>
    </div>
    <div id="wrapper">
        <div id="menuContainer">
            <ul id="categoryList">
            </ul>
            <div id="collapse_button" class="btn_expand_collapse">
                <svg style="transform: scaleX(-1);" viewBox="0 0 24 24" focusable="false" class="dyAbMb"><path d="M0 0h24v24H0z" fill="none"></path><path d="M8.59,16.59L13.17,12L8.59,7.41L10,6l6,6l-6,6L8.59,16.59z"></path></svg>
            </div>
            <div id="expand_button" class="btn_expand_collapse d-none">
                <svg style="transform: scaleX(-1);" viewBox="0 0 24 24" focusable="false" class="dyAbMb"><path d="M0 0h24v24H0z" fill="none"></path><path d="M8.59,16.59L13.17,12L8.59,7.41L10,6l6,6l-6,6L8.59,16.59z"></path></svg>
            </div>
        </div>
        <div id="mapContainer">
            <div id="map" style="width: 100vw; height: 85vh;"></div>
            <div class="searchArea">
                <div id="search">
                    <button><span></span></button>
                    <input type="text" id="searchInput" placeholder="검색어를 입력하세요.">
                </div>
                <div id="searchList"></div>
            </div>
        </div>
    </div>
    <div id="footer">
<%--        <div id="footerMapNm">경남도청</div>--%>
<%--        <div id="footerAddress" class="footer-data">--%>
<%--            <img src="/resources/img/icon/map/map-pin.png" />--%>
<%--            <span class="txt">주소 경상남도 창원시 의창구 중앙대로 300 (우 51154)</span>--%>
<%--        </div>--%>
<%--        <div id="footerPhone" class="footer-data">--%>
<%--            <img src="/resources/img/icon/map/phone.png"/>--%>
<%--            <span class="txt">대표전화 055-211-2114</span>--%>
<%--        </div>--%>
    </div>

    <template id="coordEmptyPopup">
        <div class="content-box">
            <p class="header-tit">
                <span class="txt"></span>
                <span class="close"></span>
            </p>
            <ul>
                <li>
                    <span class="tit">주소 : </span>
                    <span class="txt">등록된 좌표가 없습니다. 관리자에게 문의하세요.</span>
                </li>
                <li>
                    <span class="txt"><a href="" target="_self">3D 뷰어 이동</a></span>
                </li>
            </ul>
        </div>
    </template>

    <template id="searchResult">
        <div data-lat=${lat} data-lng=${lng}>${name}</div>
    </template>
    <!-- 카카오 지도 스크립트 -->
    <script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=${kakaoApiKey}&libraries=services"></script>
    <script type="text/javascript" src="<c:url value='/resources/js/cmn/pluxityMap.js'/>"></script>
     <!-- Constant -->
    <c:import url="/WEB-INF/views/cmn/constant.jsp" />
    <!-- 페이지 스크립트 -->
    <script type="text/javascript" src="<c:url value='/resources/js/jquery/jquery.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/resources/js/viewer/2d/index.js'/>"></script>
</body>
</html>