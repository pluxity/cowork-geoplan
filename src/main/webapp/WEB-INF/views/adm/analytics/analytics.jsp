<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<!-- Begin Page Content -->
 	<div class="container-fluid">

		<!-- Page Heading -->
        <div class="d-sm-flex align-items-center justify-content-between mb-4">
            <h1 class="h4 mb-0 text-gray-800">사용 패턴 분석</h1>
            <button type="button" class="btn btn-success btn-md" id="btnDataExport"><i class="far fa-file-excel"></i> 엑셀 다운로드</button>
            </div>
            
            <div class="row mb-4">
                <div class="col-md-10 ml-5 mb-1">
                    <form name="schFrm" id="schFrm">
                        <div class="form-row">
                            <div class="col-md-1 ml-1">
                                <select class="form-control input-sm" name="chartType" id="chartType">
                                    <option value="DAYS">날짜별</option>
                                    <option value="WEEKDAY">요일별</option>
                                </select>
                            </div>
                            <div class="col-md-6 days-content" style="display: flex; gap: .5rem;">
                                <!-- 소그룹별 옵션 -->
                                <select class="form-control input-sm d-flex" id="category3">
                                    <option value="all">시스템총합</option>
                                    <c:forEach items="${mapCategory3List}" var="category">
                                        <option value="${category.category3No}"><c:out value="${category.category3Nm}" /></option>
                                    </c:forEach>
                                </select>
                                <select id="daysType" class="form-control input-sm" style="width:110px">
                                    <option value="day">일자별</option>
                                    <option value="month">월별</option>
                                </select>
                                <div class="w-100 d-flex daySelect" >
                                    <input type="text" title="시작일" name="sdate" class="form-control datepicker" autocomplete="off" placeholder="">
                                    <span class="pt-2">~</span>
                                    <input type="text" title="종료일" name="edate" class="form-control datepicker" autocomplete="off" placeholder="">
                                </div>
                                <div class="w-100 d-flex d-none monthSelect">
                                    <input type="text" title="시작일" name="smonth" class="form-control monthpicker" autocomplete="off" placeholder="">
                                    <span class="pt-2">~</span>
                                    <input type="text" title="종료일" name="emonth" class="form-control monthpicker" autocomplete="off" placeholder="">
                                </div>
                                    <div class="btn btn-primary" onclick="getAnalyticsData()" style="width: 10rem;">검색</div>
                            </div>
                            <div class="col-md-3 weekday-content d-none">
                                <!-- 도면별 옵션 -->
                                <select class="form-control input-sm d-flex" id="map">
                                    <c:forEach items="${mapInfoList}" var="mapList">
									<option value="${mapList.mapNo}"><c:out value="${mapList.mapNm}" /></option>
									</c:forEach>
                                </select>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <div class="row" id="chartArea" style="width: 1480px; height: 640px; margin: auto;">
                <div id="noDataErrMsg" style="display:none; top: 46%; position: absolute; left: 45%; font-size: 3rem; z-index: 999; opacity: 0.5; font-weight: bold;" >No Data to display</div>
            </div>
            </div>

            <!-- <div class="row">
            
            </div> -->
		</div>
		</div>
		<!-- /.container-fluid -->

	</div>
    <!-- End of Main Content -->
    <script src="/resources/js/lib/plotly-2.6.3.min.js"></script>


 