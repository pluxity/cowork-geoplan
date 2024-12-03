<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<nav class="navbar navbar-expand navbar-dark bg-dark static-top">
		<a class="navbar-brand mr-1" href="/adm/main/index.do"><img src="/resources/img/logo_admin_header.gif" style="height:43px;">
			<span style="font-weight: bolder">실내공간정보관리시스템</span>
		</a>
		<span class="d-none d-md-inline-block ml-auto mr-0 my-2 my-md-0 viewer-title"></span>
	</nav>
	<div id="wrapper">

		<div id="sidebar-dimm"></div>
		<!-- Sidebar -->
		<div class="viewer-sidebar">
			<ul class="nav nav-tabs" style="margin-bottom:5px;">
			  <li class="nav-item">
			    <a class="nav-link active" data-toggle="tab" href="#poiTab">POI관리</a>
			  </li>
			  <li class="nav-item">
			    <a class="nav-link" data-toggle="tab" href="#route">길안내 경로 관리</a>
			  </li>
			</ul>
			<div class="tab-content" style="padding:5px;">
				<input type="hidden" name="mapNo" id="mapNo" value="${mapInfo.mapNo}">
				<input type="hidden" name="poiListTabType" id="poiListTabType" value="0">
  				<div class="tab-pane fade show active" id="poiTab">
  					<ul class="nav nav-pills nav-fill">
					  <li class="nav-item">
					    <a class="nav-link active poi-position" href="#" data-tab-type="0" data-poi-position="Y">배치</a>
					  </li>
					  <li class="nav-item">
					    <a class="nav-link poi-position" href="#" data-tab-type="1" data-poi-position="N">미배치(층)</a>
					  </li>
					  <li class="nav-item">
					    <a class="nav-link poi-position" href="#" data-tab-type="2" data-poi-position="N">미배치(전체)</a>
					  </li>
					</ul>
					<hr class="mg7070">
					<form name="schFrm" id="schFrm" method="post">
					<input type="hidden" name="mapNo" value="${mapInfo.mapNo}">
					<input type="hidden" name="floorNo" value="">
					<input type="hidden" name="positionYn" value="Y">
					<input type="hidden" name="searchType" id="searchType" value="poiNm">
					<input type="hidden" name="pageSize" value="10">
					<input type="hidden" name="page" value="1">
					<div class="row">
						<div class="col-md-6">
							<select class="form-control input-sm" name="category1No">
								<option value="">대분류</option>
								<c:forEach items="${poiCategory1List}" var="poiCategory1List">
								<option value="${poiCategory1List.category1No}">${poiCategory1List.category1Nm}</option>
								</c:forEach>
							</select>
						</div>
						<div class="col-md-6">
							<select class="form-control input-sm" name="category2No">
								<option value="">중분류</option>
							</select>
						</div>
					</div>
					<div class="input-group hr-margin">
				    	<input type="text" name="searchKeyword" id="searchKeyword" class="form-control text-primary border-primary small" placeholder="POI명을 입력하세요.">
				        <div class="input-group-append">
				        <button id="btnPoiSearch" class="btn btn-primary" type="submit">
				         <i class="fas fa-search fa-sm"></i>
				         </button>
				        </div>
				    </div>
				    </form>
				    <div id="leftPoiList">
		  				<table class="table">
						  	<colgroup>
								<col style="width:85%">
								<col style="width:15%">
							</colgroup>
						  	<tbody id="poiInfoList">
							</tbody>
						</table>
					</div>
					<div class="text-center">
						<ul class="pagination">
						</ul>
					</div>
					<hr class="hr-margin">
					<div class="text-center">
						<button type="button" class="btn btn-primary btn-md" id="btnPoiForm">POI 등록</button>
						<button type="button" class="btn btn-success btn-md" id="btnPoiBatForm">POI 일괄등록</button>
						<button type="button" class="btn btn-secondary btn-md" id="btnLodSetForm">LOD 설정</button>
						<!-- <button type="button" class="btn btn-secondary btn-md" id="btnTopoSave">토폴로지 저장</button> -->
					</div>
  				</div>
				<div class="tab-pane fade" id="route">
					<ul class="nav nav-pills nav-fill">
						<li class="nav-item">
							<a class="nav-link topo-type active" href="#" data-topo-type="일반인">일반인</a>
						</li>
						<li class="nav-item">
							<a class="nav-link topo-type" href="#" data-topo-type="교통약자">교통약자</a>
						</li>
					</ul>
					<hr class="hr-margin">
					<div>
						<input id="inputUploadRouteFile" type="file" style="display: none" />
						<button id="btnUploadRouteFile" onclick="javascript:fnUploadRouteFile();" type="button" class="btn btn-outline-primary btn-block">
							<i class="fa fa-folder-open"></i> 파일로부터 불러오기
						</button>
					</div>
					<hr class="hr-margin">
					<div class="d-flex" style="gap: .5rem">
						<button id="btnDownloadRouteFile" onclick="javascript:fnDownloadRouteFile();" type="button" class="btn btn-outline-primary w-50">
							<i class="far fa-save"></i> 파일로 저장</button>
						<button id="btnSaveRouteToDB" onclick="javascript:fnSaveRouteToDB();" type="button" class="btn btn-outline-primary w-50">
							<i class="fas fa-database"></i> DB에 저장</button>
					</div>
				</div>
			</div>
		</div>
		<!-- Sidebar -->
		<c:import url="/WEB-INF/views/adm/viewer/viewer.jsp" />

	</div>
	<div id="loadingLayer">
		<div class="layer_loading">
			<div>
				<img src="/resources/img/ajax-loader.gif" style=" width: 1rem; height: 1rem; ">
				<span>도면, POI 정보를 로딩 중입니다. </span>
			</div>
		</div>
	</div>
	<!-- poi 등록폼 -->
  	<div class="modal fade" id="poiRegistModal" tabindex="-1" role="dialog" aria-labelledby="" aria-hidden="true">
	  <div class="modal-dialog" role="document" style="max-width:700px;">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title">POI 등록</h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body">
	        <form name="poiRegistFrm" id="poiRegistFrm" method="post" enctype="multipart/form-data">
	        <input type="hidden" name="mapNo" value="${mapInfo.mapNo}" title="도면번호" required data-rules="require">
				<fieldset>
				<table class="table table-bordered">
					<colgroup>
						<col style="width:20%">
						<col style="width:80%">
					</colgroup>
					<tbody>
					<tr>
						<th>POI명</th>
						<td><input type="text" name="poiNm" class="form-control input-sm" maxlength="30" title="POI명" required data-rules="require"></td>
					</tr>
					<tr>
						<th>객체코드</th>
						<td><input type="text" name="dvcCd" class="form-control input-sm" maxlength="50" title="객체코드"></td>
					</tr>
					<tr>
						<th>대표이미지</th>
						<td >
							<div >
								<div style="float: left; width: 30%;">
									<img src="/resources/img/noPhoto.png" id="imgR" class="rounded" alt="대표이미지" style="width:40px;">
									<button type="button" class="btn btn-outline-secondary" id="btnImgRDelete"  style="margin-left: 10px"><i class="fas fa-trash-alt"></i></button>
								</div>
								<div style="float: right; width: 70%;">
									<input type="file" name="poiImg" id="poiImgR" class="form-control input-sm" title="대표이미지" data-rules="file" data-ext="jpg,jpeg,png,gif,bmp" accept=".jpg,.jpeg,.png,.gif,.bmp">
								</div>
							</div>
						</td>
					</tr>
					<tr>
						<th>분류</th>
						<td>
							<div class="row">
								<div class="col-md-6">
									<select class="form-control input-sm" name="category1No" title="대분류" required data-rules="require">
									<option value="">대분류</option>
									<c:forEach items="${poiCategory1List}" var="category1List">
									<option value="${category1List.category1No}"><c:out value="${category1List.category1Nm}" /></option>
									</c:forEach>
									</select>
								</div>
								<div class="col-md-6">
									<select class="form-control input-sm" name="category2No" title="중분류" required data-rules="require">
										<option value="" selected>중분류</option>
									</select>
								</div>
							</div>
							<div class="iconsetView">
							</div>
						</td>
					</tr>
					<tr>
						<th>층선택</th>
						<td>
						<select class="form-control input-sm" name="floorNo" title="층번호">
						</select>
						</td>
					</tr>
					<tr>
						<th>기타정보</th>
						<td><input type="text" name="etcDesc" class="form-control input-sm" maxlength="200" title="기타정보"></td>
					</tr>
					</tbody>
				</table>
				</fieldset>
			</form>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
	        <button type="button" class="btn btn-primary" id="btnPoiRegist">등록</button>
	      </div>
	    </div>
	  </div>
	</div>

	<!-- poi 일괄 등록폼 -->
  	<div class="modal fade" id="poiBatRegistModal" tabindex="-1" role="dialog" aria-labelledby="" aria-hidden="true">
	  <div class="modal-dialog" role="document" style="max-width:700px;">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title">POI 일괄등록</h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body">
	        <form name="poiBatRegistFrm" id="poiBatRegistFrm" method="post" enctype="multipart/form-data">
	        <input type="hidden" name="mapNo" value="${mapInfo.mapNo}" title="도면번호" required data-rules="require">
				<fieldset>
				<table class="table table-bordered">
					<colgroup>
						<col style="width:20%">
						<col style="width:80%">
					</colgroup>
					<tbody>
					<tr>
						<th>층선택</th>
						<td>
						<select class="form-control input-sm" name="floorNo" title="층번호">
						</select>
						</td>
					</tr>
					<tr>
						<th>일괄등록파일</th>
						<td>
							<a href="/resources/poi_data_template.xlsx"><i class="fas fa-download">샘플파일 다운로드</i></a><br>
							<input type="file" name="excelFile" class="form-control input-sm" title="일괄등록파일" required data-rules="require,file" data-ext="xls,xlsx" accept=".xls,.xlsx">
						</td>
					</tr>
					</tbody>
				</table>
				</fieldset>
			</form>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
	        <button type="button" class="btn btn-primary" id="btnPoiBatRegist">등록</button>
	      </div>
	    </div>
	  </div>
	</div>

	<!-- poi 수정폼 -->
  	<div class="modal fade" id="poiModifyModal" tabindex="-1" role="dialog" aria-labelledby="" aria-hidden="true">
	  <div class="modal-dialog" role="document" style="max-width:700px;">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title">POI 수정</h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body">
	        <form name="poiModifyFrm" id="poiModifyFrm" method="post" enctype="multipart/form-data">
			</form>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
	        <button type="button" class="btn btn-primary" id="btnPoiModify">수정</button>
	        <!--<button type="button" class="btn btn-warning" id="btnPosPoi">미배치로 변경</button>-->
	      </div>
	    </div>
	  </div>
	</div>

	<!-- lod 설정 폼 -->
  	<div class="modal fade" id="lodSetModal" tabindex="-1" role="dialog" aria-labelledby="" aria-hidden="true">
	  <div class="modal-dialog" role="document" style="max-width:1600px;">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title">LOD 설정</h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body">
	      <form name="lodSetFrm" id="lodSetFrm" method="post">
	      <input type="hidden" name="mapNo" value="${mapInfo.mapNo}" title="도면번호" required data-rules="require">
		  <fieldset>
	      <table class="table table-bordered">
			<colgroup>
				<col style="width:10%">
				<col style="width:40%">
				<col style="width:10%">
				<col style="width:40%">
			</colgroup>
			<tbody>
				<tr>
					<th>최장거리설정</th>
					<td>
						<div class="row">
							<div class="col-md-3">
								<button type="button" class="btn btn-secondary" id="btnLodMaxDist">현재화면 기준</button>
							</div>
							<div class="col-md-9">
								<input type="text" name="maxDist" id="maxDist" class="form-control input-sm" title="최장거리카메라값" required data-rules="require" value="" readonly>
							</div>
						</div>
					</td>
					<th>LOD 레벨수</th>
					<td>
						<select class="form-control input-sm" name="levlCnt" id="levelCnt" title="LOD레벨수" required data-rules="require">
							<option value="3">3단계</option>
							<option value="4">4단계</option>
							<option value="5">5단계</option>
							<option value="6">6단계</option>
							<option value="7">7단계</option>
							<option value="8">8단계</option>
							<option value="9">9단계</option>
							<option value="10">10단계</option>
						</select>
					</td>
				</tr>
				</tbody>
			</table>
			<ul class="nav nav-tabs" style="margin-bottom:5px;">
				<li class="nav-item">
			    	<a class="nav-link active" data-toggle="tab" href="#lodTab1">POI 크기</a>
			  	</li>
			  	<li class="nav-item">
			    	<a class="nav-link" data-toggle="tab" href="#lodTab2">LOD 설정</a>
			  	</li>
			</ul>
			<div class="tab-content" style="padding:5px;">
				<div id="lodTab1" class="tab-pane fade show active" style="overflow-y:auto;max-height:500px;">
					<table class="table table-bordered lodtable">
						<colgroup>
							<col style="width:10%">
							<col style="width:10%">
							<c:forEach begin="0" end="9" var="x">
							<col class="lod_${x}">
							</c:forEach>
						</colgroup>
						<tbody>
						<tr>
							<th>대분류</th>
							<th>중분류</th>
							<c:forEach begin="0" end="9" var="x">
							<th class="lod_${x}">
								${x+1}단계
								<select class="form-control input-sm iconSizeAll" id="iconSize${x}" title="아이콘 사이즈">
									<option value="">일괄적용</option>
									<option value="100">100%</option>
									<option value="80">80%</option>
									<option value="60">60%</option>
									<option value="40">40%</option>
									<option value="20">20%</option>
								</select>
							</th>
							</c:forEach>
						</tr>
					<c:forEach items="${poiCategoryTree}" var="poiCategory1" varStatus="status">
						<tr>
							<td rowspan="${fn:length(poiCategory1.categoryList)}"><c:out value="${poiCategory1.category1Nm}" /></td>
						<c:forEach items="${poiCategory1.categoryList}" var="poiCategory2" varStatus="status">
						<c:if test="${status.index ne 0}"><tr></c:if>
							<td>
								<c:out value="${poiCategory2.category2Nm}" />
								<input type="hidden" name="category1No" value="${poiCategory1.category1No}">
								<input type="hidden" name="category2No" value="${poiCategory2.category2No}">
							</td>
							<c:forEach begin="0" end="9" step="1" var="x">
							<td class="lod_${x}">
								<select class="form-control input-sm" name="iconSize${x}" title="아이콘 사이즈" required data-rules="require">
									<option value="100">100%</option>
									<option value="80">80%</option>
									<option value="60">60%</option>
									<option value="40">40%</option>
									<option value="20">20%</option>
								</select>
							</td>
							</c:forEach>
						<c:if test="${status.index ne 0}"></tr></c:if>
						</c:forEach>
						</tr>
					</c:forEach>
						</tbody>
					</table>
				</div>
				<div id="lodTab2" class="tab-pane fade" style="overflow-y:auto;max-height:500px;">
					<table class="table table-bordered lodtable">
						<colgroup>
							<col style="width:10%">
							<col style="width:10%">
							<c:forEach begin="0" end="9" var="x">
							<col class="lod_${x}">
							</c:forEach>
						</colgroup>
						<tbody>
						<tr>
							<th>대분류</th>
							<th>중분류</th>
							<c:forEach begin="0" end="9" var="x">
							<th class="lod_${x}">
								${x+1}단계
								<select class="form-control input-sm lodTypeAll" id="lodType${x}" title="LOD 타입">
									<option value="">일괄적용</option>
									<option value="0">아이콘+텍스트</option>
									<option value="1">아이콘</option>
									<option value="2">숨김</option>
								</select>
							</th>
							</c:forEach>
						</tr>
					<c:forEach items="${poiCategoryTree}" var="poiCategory1" varStatus="status">
						<tr>
							<td rowspan="${fn:length(poiCategory1.categoryList)}"><c:out value="${poiCategory1.category1Nm}" /></td>
						<c:forEach items="${poiCategory1.categoryList}" var="poiCategory2" varStatus="status">
						<c:if test="${status.index ne 0}"><tr></c:if>
							<td><c:out value="${poiCategory2.category2Nm}" /></td>
							<c:forEach begin="0" end="9" step="1" var="x">
							<td class="lod_${x}">
								<select class="form-control input-sm" name="lodType${x}" title="LOD 타입" required data-rules="require">
									<option value="0">아이콘+텍스트</option>
									<option value="1">아이콘</option>
									<option value="2">숨김</option>
								</select>
							</td>
							</c:forEach>
						<c:if test="${status.index ne 0}"></tr></c:if>
						</c:forEach>
						</tr>
					</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
			</fieldset>
			</form>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
	        <button type="button" class="btn btn-primary" id="btnPoiLodRegist">저장</button>
	      </div>
	    </div>
	  </div>
	</div>

 	<script type="text/template" id="poiInfoListTpl">
	<tr>
		<td onclick="moveToPoi('{poiNo}');" style="cursor:pointer;" title="{poiCategory.category1Nm}-{poiCategory.category2Nm}">{poiNm}</td>
		<td class="text-center">
			<div class="btn-group">
				<button type="button" class="btn btn-info btn-sm dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">관리</button>
				<div class="dropdown-menu">
	  				<a class="dropdown-item" href="#" onclick="modifyPoiInfo('{poiNo}');return false;">POI 속성 보기</a>
					<a class="dropdown-item" href="#" onclick="addPoiByMouse('{poiNo}');return false;">POI 배치 하기</a>
					<a class="dropdown-item" href="#" onclick="deletePoiInfo('{poiNo}');return false;">POI 삭제 하기</a>
					<a class="dropdown-item" href="#" onclick="chgNotPositPoi('{poiNo}');return false;">POI 미배치로 변경</a>
	  			</div>
			</div>
		</td>
	</tr>
	</script>
	<script type="text/template" id="iconsetTpl">
	<table class="table table-bordered" style="margin-top:10px;">
		<colgroup>
			<col style="width:20%">
			<col style="width:80%">
		</colgroup>
		<tbody>
		<tr>
			<th>아이콘셋명</th>
			<td>{poiIconset.iconsetNm}</td>
		</tr>
		<tr>
			<th>2D 아이콘</th>
			<td>
				<img src="{poiIconset.iconset2d0FilePath}" style="width:32px;">
				<img src="{poiIconset.iconset2d1FilePath}" style="width:32px;">
				<img src="{poiIconset.iconset2d2FilePath}" style="width:32px;">
				<img src="{poiIconset.iconset2d3FilePath}" style="width:32px;">
				<img src="{poiIconset.iconset2d4FilePath}" style="width:32px;">
			</td>
		</tr>
		<tr>
			<th>3D 아이콘</th>
			<td><img src="{poiIconset.iconset3dThumbFilePath}" style="width:32px;"></td>
		</tr>
		</tbody>
	</table>
	</script>
	<script type="text/template" id="iconsetTpl">
	<table class="table table-bordered" style="margin-top:10px;">
		<colgroup>
			<col style="width:20%">
			<col style="width:80%">
		</colgroup>
		<tbody>
		<tr>
			<th>아이콘셋명</th>
			<td>{poiIconset.iconsetNm}</td>
		</tr>
		<tr>
			<th>2D 아이콘</th>
			<td>
				<img src="{poiIconset.iconset2d0FilePath}" style="width:32px;">
				<img src="{poiIconset.iconset2d1FilePath}" style="width:32px;">
				<img src="{poiIconset.iconset2d2FilePath}" style="width:32px;">
				<img src="{poiIconset.iconset2d3FilePath}" style="width:32px;">
				<img src="{poiIconset.iconset2d4FilePath}" style="width:32px;">
			</td>
		</tr>
		<tr>
			<th>3D 아이콘</th>
			<td><img src="{poiIconset.iconset3dThumbFilePath}" style="width:32px;"></td>
		</tr>
		</tbody>
	</table>
	</script>
	<script type="text/template" id="poiInfoFrmTpl">
	<fieldset>
	<table class="table table-bordered">
		<colgroup>
			<col style="width:20%">
			<col style="width:80%">
		</colgroup>
		<tbody>
		<tr>
			<th>POI번호</th>
			<td><input type="text"  id="poiNo" name="poiNo" class="form-control input-sm" style="width:100px;" title="POI번호" required data-rules="require" readonly value="{poiNo}"></td>
		</tr>
		<tr>
			<th>POI명</th>
			<td><input type="text" name="poiNm" class="form-control input-sm" maxlength="30" title="POI명" required data-rules="require" value="{poiNm}"></td>
		</tr>
		<tr>
			<th>객체코드</th>
			<td><input type="text" name="dvcCd" class="form-control input-sm" maxlength="50" title="객체코드" value="{dvcCd}"></td>
		</tr>
		<tr>
			<th>대표이미지</th>
				<td >
					<div>
						<div style="float : left; width: 30%;">
							<img src="{imgFileNm}" id="imgM" class="rounded prev-img" alt="대표이미지" style="height:40px;" onerror="fn_noPhoto(this)">
							<button type="button" class="btn btn-outline-secondary" id="btnImgMDelete" style="margin-left: 10px"><i class="fas fa-trash-alt"></i></button>
							<input type="hidden" name="imgFileNo" value="{imgFileNo}"/>
						</div>
						<div style="float : right; width: 70%;">
							<input type="file" name="poiImg" id="poiImgM" class="form-control input-sm" title="대표이미지" data-rules="file" data-ext="jpg,jpeg,png,gif,bmp" accept=".jpg,.jpeg,.png,.gif,.bmp">
						</div>
					</div>
				</td>
		</tr>
		<tr>
			<th>분류</th>
			<td>
				<div class="row">
					<div class="col-md-6">
						<select class="form-control input-sm" name="category1No" title="대분류" required data-rules="require">
						<option value="">대분류</option>
						<c:forEach items="${poiCategory1List}" var="category1List">
						<option value="${category1List.category1No}"><c:out value="${category1List.category1Nm}" /></option>
						</c:forEach>
						</select>
					</div>
					<div class="col-md-6">
						<select class="form-control input-sm" name="category2No" title="중분류" required data-rules="require">
							<option value="" selected>중분류</option>
						</select>
					</div>
				</div>
				<div class="iconsetView">
					<table class="table table-bordered" style="margin-top:10px;">
						<colgroup>
							<col style="width:20%">
							<col style="width:80%">
						</colgroup>
						<tbody>
						<tr>
							<th>아이콘셋명</th>
							<td>{poiIconset.iconsetNm}</td>
						</tr>
						<tr>
							<th>2D 아이콘</th>
							<td>
								<img src="{poiIconset.iconset2d0FilePath}" style="width:32px;">
								<img src="{poiIconset.iconset2d1FilePath}" style="width:32px;">
								<img src="{poiIconset.iconset2d2FilePath}" style="width:32px;">
								<img src="{poiIconset.iconset2d3FilePath}" style="width:32px;">
								<img src="{poiIconset.iconset2d4FilePath}" style="width:32px;">
							</td>
						</tr>
						<tr>
							<th>3D 아이콘</th>
							<td><img src="{poiIconset.iconset3dThumbFilePath}" style="width:32px;"></td>
						</tr>
						</tbody>
					</table>
				</div>
			</td>
		</tr>
		<tr>
			<th>층선택</th>
			<td>
			<select class="form-control input-sm" name="floorNo" title="층번호">
			</select>
			</td>
		</tr>
		<tr>
			<th>배치</th>
			<td id="positionYnTxt">{positionYn}</td>
		</tr>
		<tr>
			<th>기타정보</th>
			<td><input type="text" name="etcDesc" class="form-control input-sm" maxlength="200" title="기타정보" value="{etcDesc}"></td>
		</tr>
		<tr>
			<th>등록자</th>
			<td>{regUsr}</td>
		</tr>
		<tr>
			<th>등록일</th>
			<td>{regDt}</td>
		</tr>
		</tbody>
	</table>
	</fieldset>
	</script>
	<script type="text/template" id="poiMenuTpl">
	<button type="button" class="close" style="padding:7px;line-height:0;" aria-label="Close" onclick="hidePoiMenu();">
         <span aria-hidden="true">×</span>
       </button>
	<a class="dropdown-item" href="#" onclick="modifyPoiInfo('poiNo');return false;">POI 속성 보기</a>
	<a class="dropdown-item" href="#" onclick="addPoiByMouse('poiNo');return false;">POI 배치 하기</a>
	<a class="dropdown-item" href="#" onclick="deletePoiInfo('poiNo');return false;">POI 삭제 하기</a>
	<a class="dropdown-item" href="#" onclick="chgNotPositPoi('poiNo');return false;">POI 미배치로 변경</a>
	</script>
