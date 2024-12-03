<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <sec:authentication var="principal" property="principal" />
    <!doctype html>
    <html lang="ko" class="scrl-no">

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
        <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/common/reset.css' />">
        <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/common/index.css' />">
        <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/main/index.css' />">
        <link href="<c:url value='/resources/img/favicon.ico' />" type="image/x-icon" rel="shortcut icon" />
        <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/jquery/jquery-ui.min.css' />">
    </head>

    <body>
        <div class="container">
            <div class="main">
                <!-- 3D영역 -->
                <div id="webGLContainer" class="web3d"></div>
                <div class="left_btn_wrap">
                    <div class="btn_menu_wrap">
                        <span class="btn_menu">
                            <span class="icon"></span>
                        </span>
                        <ul id="category1List" class="list"></ul>
                    </div>
                    <div class="btn_map_wrap">
                        <span class="btn_menu">
                            <span class="icon"></span>
                        </span>
                        <ul class="list">
                            <li class="find-map">
                                <div class="li-header">
                                    <span class="txt">길찾기</span>
                                    <!-- <span class="close"></span> -->
                                </div>
                                <div class="input-wrap">
                                    <div class="input">
                                        <input class="input_search__poi start" type="search" placeholder="출발지를 입력하세요.">
                                        <div class="srch-list">
                                            <ul class="scroll-wrap search_list">
                                            </ul>
                                        </div>
                                        <span class="icon pin"></span>
                                    </div>
                                    <div class="input bottom">
                                        <input class="input_search__poi end" type="search" placeholder="도착지를 입력하세요.">
                                        <div class="srch-list">
                                            <ul class="scroll-wrap search_list">
                                            </ul>
                                        </div>
                                        <span class="icon pin"></span>
                                    </div>
                                </div>
                                <button class="btn_reset">
                                    <span class="txt">초기화</span>
                                </button>
                            </li>
                            <li class="customer-type">
                                <p class="tit">경로조회 선택</p>
                                <div class="radio-wrap">
                                    <span class="radio01">
                                        <input type="radio" name="type" id="type01" topo-type="일반인" checked>
                                        <label for="type01">일반인</label>
                                    </span>
                                    <span>
                                        <input type="radio" name="type" id="type02" topo-type="교통약자">
                                        <label for="type02">교통약자</label>
                                    </span>
                                </div>
                                <button class="btn_direction">
                                    <span class="txt">길찾기</span>
                                </button>
                            </li>
                            <!-- 모의 주행 drive-3d 클래스에 on클릭 시 나타남 -->
                            <li class="drive">
                                <div class="part01">
                                <button class="btn_drive">
                                <span class='txt'>모의주행</span>
                                </button>
                            </div>
                            </li>
                            <li class="drive-header d-none">
                                <div class="li-header">
                                    <span class='txt'>모의주행</span>
                                </div>
                            </li>
                            <li class="drive-3d d-none">
                                <div class="part02">
                                    <div class="player-wrap">
                                        <span id="playDirection" class="play">
                                            <span></span>
                                        </span>
<%--                                        <span id="pauseDirection" class="pause">--%>
<%--                                            <span></span>--%>
<%--                                        </span>--%>
                                        <span id="stopDirection" class="stop">
                                            <span></span>
                                        </span>
                                    </div>
                                    <div class="speed-wrap">
                                        <p class="tit">주행속도</p>
                                        <div class="radio-wrap">
                                            <span>
                                                <input type="radio" name="speed" id="speed01" value=5 checked>
                                                <label for="speed01">1X</label>
                                            </span>
                                            <span>
                                                <input type="radio" name="speed" id="speed02" value=10>
                                                <label for="speed02">2X</label>
                                            </span>
                                            <span>
                                                <input type="radio" name="speed" id="speed03" value=15>
                                                <label for="speed03">3X</label>
                                            </span>
                                        </div>
                                        <button class="btn_drive_end">
                                            <span class='txt'>모의주행종료</span>
                                        </button>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div class="btn_event_wrap">
                        <span class="btn_menu">
                            <span class="icon"></span>
                        </span>
                        <ul class="list">
                            <li class="find-map">
                                <div class="li-header">
                                    <span class="txt">이벤트 리스트</span>
                                    <!-- <span class="close"></span> -->
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div class="search_wrap">
                        <div class="search">
                            <button class="btn_search btn-srch">
                                <span class="icon"></span>
                            </button>
                            <input id="search" type="search" class="input_search__poi search-input" placeholder="검색어를 입력하세요.">
                            <div class="srch-list">
                                <ul class="scroll-wrap search_list">
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
                <div>
                    <ul class="mini-poi"></ul>
                </div>
                <div class="right_btn_wrap">
                    <div class="floor_wrap">
                        <button class="btn_all">
                            <span class="icon"></span>
                            <span class="box">
                                <span class="txt" data-floor-group-no="all">층 선택</span>
                            </span>
                        </button>
                        <ul class="ul-floor"></ul>
                    </div>
                    <div class="mini_map_wrap">
                        <div class="mini_map">
                            <div class="round_bg">
                                <!-- 3d 미니맵 -->
                                <div id="minimap" class="map_bg"></div>
                            </div>
                        </div>
                        <ul class="mini_map_icon">
                            <li class="icon01 d-none" data-btn-type="screenshot">
                                <span class="icon"></span>
                            </li>
                            <li class="icon02" data-btn-type="transparent">
                                <span class="icon"></span>
                            </li>
                            <li class="icon03" data-btn-type="center">
                                <span class="icon"></span>
                            </li>
                            <li class="icon04" data-btn-type="viewLook">
                                <span class="icon"></span>
                            </li>
                            <li class="icon05" data-btn-type="distance">
                                <span class="icon"></span>
                            </li>
                            <li class="icon06" data-btn-type="area">
                                <span class="icon"></span>
                            </li>
                        </ul>
                    </div>
                </div>
                <!-- poi_box on 일때 나타남 -->
                <template id="poiPopup">
                    <div class="poi_box">
                        <p class="header-tit">
                            <span class="txt"></span>
                            <span class="close"></span>
                        </p>
                        <ul>
                            <li>
                                <span class="tit">객체코드 : </span>
                                <span class="txt"></span>
                            </li>
                            <li>
                                <span class="tit">구분 : </span>
                                <span class="txt"></span>
                            </li>
                        </ul>
                    </div>
                </template>
            </div>
            <span id="warningMsg">"이 자료는 공개제한 등급이므로 공개 또는 유출되지 않도록 주의하시기 바랍니다."</span>
        </div>
        <!-- LoadingLayer -->
        <div id="loadingLayer">
            <img src="/resources/img/ajax-loader.gif" style="vertical-align:middle;padding-left:3px;"> <span>도면 데이터를
                로딩중입니다.</span>
        </div>
        <template id="popupMeasure">
            <div class="popup_layer__measure">
                <p class="header-tit">
                    <span class="txt"></span>
                </p>
                <ul>
                    <li>
                        <span class="tit"></span>
                        <span class="value"></span>
                    </li>
                    <li>
                        <button>지우기</button>
                    </li>
                </ul>
            </div>
        </template>


        <!-- 좌상단 poi 검색결과 리스트 -->
        <template id="searchPoiListTpl">
            <li onClick="javascript:fnClickSearchResult(this, {poiNo})">
                <a href="javascript:;" >{floorInfo.floorNm} {poiNm}</a>
            </li>
        </template>
        <!-- 길찾기 POI 검색결과 리스트 -->
        <template id="searchFindPathPoiListTpl">
            <li onClick="javascript:fnClickFindPathPoiResult(this, {poiNo})">
                <a href="javascript:;" >{floorInfo.floorNm} {poiNm}</a>
            </li>
        </template>



        <c:import url="/WEB-INF/views/cmn/constant.jsp" /> <!-- Constant -->

        <script type="text/javascript">
            var mapNo = ${mapNo};
            var mapCd = "${mapCd}";
            var mapVer = "";
        </script>


        <!-- 라이브러리 -->
        <script src="<c:url value='/resources/js/jquery/jquery.min.js' />"></script>
        <script src="<c:url value='/resources/js/jquery/jquery.form.min.js' />"></script>
        <script src="<c:url value='/resources/js/jquery/jquery-ui.min.js' />"></script>
        <script src="<c:url value='/resources/js/jquery/jquery-ui-timepicker-addon.js' />"></script>
        <script src="<c:url value='/resources/js/jquery/jquery.mtz.monthpicker.js' />"></script>
        <!-- 공통 -->
        <script src="<c:url value='/resources/js/cmn/common.js' />"></script>
        <script src="<c:url value='/resources/js/cmn/validation.js' />"></script>
        <!-- 뷰어 제어 -->
<%--        <script src="<c:url value='/resources/js/lib/inflate.min.js' />"></script>--%>
        <script src="<c:url value='/resources/js/lib/Px.Engine.js'/>"></script>
        <script src="<c:url value='/resources/js/lib/Plx.Viewer.js'/>"></script>
		<script src="<c:url value='/resources/js/addons/camPos.js' />"></script>
        <script src="<c:url value='/resources/js/config/viewerConfig.js'/>"></script>
        <script src="<c:url value='/resources/js/main/index_pbl.js'/>"></script>

        <script src="<c:url value='/resources/js/viewer/index.js'/>"></script>
        <script src="<c:url value='/resources/js/viewer/viewerEventListener.js'/>"></script>
        <script src="<c:url value='/resources/js/viewer/direction.js'/>"></script>

    </body>

    </html>