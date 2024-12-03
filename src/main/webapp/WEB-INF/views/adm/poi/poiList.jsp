<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<!-- Begin Page Content -->
 	<div class="container-fluid">

		<!-- Page Heading -->
        <div class="d-sm-flex align-items-center justify-content-between mb-4">
        	<h1 class="h4 mb-0 text-gray-800">POI 목록</h1>
        	<button type="button" class="btn btn-success btn-md" id="btnChkDown"><i class="far fa-file-excel"></i> 엑셀 다운로드</button>
		</div>

        <div class="row">
          	<div class="col-md-2">
				<div id="poiCategoryTree" class="hummingbird-treeview well h-scroll-large map_tree">
					<ul id="treeview" class="hummingbird-base" style="padding:10px;">
						<c:forEach items="${poiCategoryTree}" var="poiCategory1" varStatus="status">
						<li>
							<i class="fa fa-minus-square"></i> <label> <input name="category1No" type="checkbox" value="${poiCategory1.category1No}"> <c:out value="${poiCategory1.category1Nm}" /></label>
			            	<ul style="display: block;">
			            		<c:forEach items="${poiCategory1.categoryList}" var="poiCategory2" varStatus="status">
			            		<li>
			            			<label> <input name="category2No" type="checkbox" value="${poiCategory2.category2No}">  <c:out value="${poiCategory2.category2Nm}" /></label>
								</li>
								</c:forEach>
							</ul>
						</li>
						</c:forEach>
					</ul>
      			</div>
          	</div>
          	<div class="col-md-10">
				<div class="row">
					<div class="col-md-9">
						<form name="schFrm" id="schFrm">
						<div class="form-row">
							<!--
							Total <span class="badge badge-primary">12,220</span> ( <strong style="color:#c00">1</strong> / 611 )
							-->
							<div class="col-md-3">
								<select class="form-control input-sm" name="mapNo">
									<option value="">도면선택</option>
									<c:forEach items="${mapInfoList}" var="mapList">
									<option value="${mapList.mapNo}"><c:out value="${mapList.mapNm}" /></option>
									</c:forEach>
								</select>
							</div>
							<div class="col-md-3">
								<select class="form-control input-sm" name="floorNo">
									<option value="">층선택</option>
								</select>
							</div>
							<div class="col-md-2">
								<select class="form-control input-sm" name="searchType" id="searchType" >
									<option value="poiNm">POI명</option>
									<option value="dvcCd">객체코드</option>
								</select>
							</div>
							<div class="col-md-4">
								<div class="input-group mb-3">
									<input type="text" name="searchKeyword" id="searchKeyword" class="form-control" placeholder="검색어를 입력하세요." aria-label="검색어" aria-describedby="button-addon2">
									<div class="input-group-append">
										<button class="btn btn-primary" type="submit">검색</button>
									</div>
								</div>
							</div>
						</div>
						</form>
					</div>
					<div class="col-md-3 text-right">
					
						<button type="button" class="btn btn-primary btn-md" id="btnPoiForm">등록</button>
						<button type="button" class="btn btn-success btn-md" id="btnPoiBatForm">일괄등록</button>
						<button type="button" class="btn btn-danger btn-md" id="btnPoiDel">삭제</button>
						<button type="button" class="btn btn-warning btn-md" id="btnSelPosPoi">미배치</button>
					</div>
				</div>
	          	<div class="row" style="padding:0 12px 0 16px;">
	          		<table class="table table-bordered sort-list">
						<colgroup>
							<col style="width:5%">
							<col style="width:8%">
							<col style="width:15%">
							<col style="width:15%">
							<col style="width:10%">
							<col style="width:10%">
							<col style="width:5%">
							<col style="width:5%">
							<col style="width:10%">
							<col style="width:10%">
						</colgroup>
						<thead>
						    <tr>
						      	<th scope="col"><input type="checkbox" class="chkall"></th>
						      	<th scope="col"  data-sort-attr="poi_no">POI번호</th>
						      	<th scope="col" data-sort-attr="poi_nm">POI이름</th>
						      	<th scope="col" data-sort-attr="dvc_cd">객체코드</th>
						      	<th scope="col">대분류</th>
						      	<th scope="col">중분류</th>
						      	<th scope="col">아이콘</th>
						      	<th scope="col">배치</th>
						      	<th scope="col" data-sort-attr="map_nm">도면명</th>
						      	<th scope="col">관리</th>
						    </tr>
						</thead>
						<tbody id="poiInfoList">
						</tbody>
					</table>
				</div>
				<div class="row">
					<div class="col-sm-12 col-md-12">
						<ul class="pagination paging-center">
						</ul>
					</div>
				</div>
			</div>
		</div>
		<!-- /.container-fluid -->

	</div>
    <!-- End of Main Content -->

    <!-- poi 등록폼 -->
  	<div class="modal fade" id="poiRegistModal" tabindex="-1" role="dialog" aria-labelledby="" aria-hidden="true" style="top:15%;">
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
						<td>
							<input type="text" name="dvcCd" class="form-control input-sm input_autocomplete" maxlength="50" title="객체코드">
							<div class="input_autocomplete" ><ul class=""></ul></div>
						</td>
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
						<th>도면</th>
						<td>
							<div class="row">
								<div class="col-md-6">
									<select class="form-control input-sm" name="mapNo" title="도면명" required data-rules="require">
									<option value="">도면선택</option>
									<c:forEach items="${mapInfoList}" var="mapList">
									<option value="${mapList.mapNo}"><c:out value="${mapList.mapNm}" /></option>
									</c:forEach>
									</select>
								</div>
								<div class="col-md-6">
									<select class="form-control input-sm" name="floorNo" title="층">
										<option value="" selected>층선택</option>
									</select>
								</div>
							</div>
						</td>
					</tr>
					<tr>
						<th>POI설명</th>
						<td><textarea type="text" name="etcDesc" style="resize: none;" class="form-control input-sm" maxlength="200" title="POI설명" ></textarea></td>
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
				<fieldset>
				<table class="table table-bordered">
					<colgroup>
						<col style="width:20%">
						<col style="width:80%">
					</colgroup>
					<tbody>
					<tr>
						<th>도면</th>
						<td>
							<div class="row">
								<div class="col-md-6">
									<select class="form-control input-sm" name="mapNo" title="도면명" required data-rules="require">
									<option value="">도면선택</option>
									<c:forEach items="${mapInfoList}" var="mapList">
									<option value="${mapList.mapNo}"><c:out value="${mapList.mapNm}" /></option>
									</c:forEach>
									</select>
								</div>
								<div class="col-md-6">
									<select class="form-control input-sm" name="floorNo" title="층">
										<option value="" selected>층선택</option>
									</select>
								</div>
							</div>
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
	        <button type="button" class="btn btn-warning" id="btnPosPoi">미배치로 변경</button>
	      </div>
	    </div>
	  </div>
	</div>

	<!-- POI 다운로드폼 -->
  	<div class="modal fade" id="downModal" tabindex="-1" role="dialog" aria-labelledby="" aria-hidden="true">
	  <div class="modal-dialog" role="document" style="max-width:700px;">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title">다운로드할 항목</h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body">
	        <form name="poiListDownFrm" id="poiListDownFrm" method="post">
				<input type="hidden" id="sortType" name="sortType" value=""/>
				<input type="hidden" id="sortBy" name="sortBy" value=""/>
		      	<input type="hidden" id="fieldNmList" name="fieldNmList" />
		      	<input type="hidden" id="category2NoString" name="category2NoString" />
		        <input type="hidden" id="mapNo" name="mapNo" />
	    	    <input type="hidden" id="floorNo" name="floorNo" />
	        	<input type="hidden" id="searchKeywordEd" name="searchKeyword" />
	        	<input type="hidden" id="searchTypeEd" name="searchType" />
	        	<fieldset>
					<table class="table table-bordered">
						<colgroup>
							<col style="width:20%">
							<col style="width:80%">
						</colgroup>
						<thead>
				    <tr>
				      	<th scope="col"><input type="checkbox" class="chkall" name="dw"></th>
				      	<th scope="col">데이터명</th>
				    </tr>
				</thead>
						<tbody>
						<tr>
							<th><input type="checkbox" class="seqChk" name="dw" value="poiNo"></th>
							<td>POI 번호</td>
						</tr>
						<tr>
							<th><input type="checkbox" class="seqChk" name="dw" value="poiNm"></th>
							<td>POI 이름</td>
						</tr>
						<tr>
							<th><input type="checkbox" class="seqChk" name="dw" value="dvcCd"></th>
							<td>객체코드</td>
						</tr>
						<tr>
							<th><input type="checkbox" class="seqChk" name="dw" value="category1No"></th>
							<td>대분류</td>
						</tr>
						<tr>
							<th><input type="checkbox" class="seqChk" name="dw" value="category2No"></th>
							<td>중분류</td>
						</tr>
						<tr>
							<th><input type="checkbox" class="seqChk" name="dw" value="mapNm"></th>
							<td>도면명</td>
						</tr>
						<tr>
							<th><input type="checkbox" class="seqChk" name="dw" value="positionYn"></th>
							<td>배치</td>
						</tr>
						<tr>
							<th><input type="checkbox" class="seqChk" name="dw" value="etcDesc"></th>
							<td>POI 설명</td>
						</tr>
						<tr>
							<th><input type="checkbox" class="seqChk" name="dw" value="regUsr"></th>
							<td>등록자</td>
						</tr>
						<tr>
							<th><input type="checkbox" class="seqChk" name="dw" value="regDt"></th>
							<td>등록일</td>
						</tr>
						</tbody>
					</table>
				</fieldset>
			</form>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
	        <button type="button" class="btn btn-primary" id="btnDownload">다운로드</button>
	      </div>
	    </div>
	  </div>
	</div>
	
 	<script type="text/template" id="poiInfoListTpl">
	<tr>
		<th scope="row"><input type="checkbox" class="seqChk" name="poiNo" value="{poiNo}"></th>
		<td class="text-center">{poiNo}</td>
		<td onclick="modifyPoiInfo('{poiNo}');" style="cursor:pointer">{poiNm}</td>
		<td class="text-center">{dvcCd}</td>
		<td class="text-center">{poiCategory.category1Nm}</td>
		<td class="text-center">{poiCategory.category2Nm}</td>
		<td class="text-center"><img src="{poiCategory.poiIconset.iconset2d0FilePath}" style="width:32px;"></td>
		<td class="text-center">{positionYn}</td>
		<td class="text-center">{mapNm}</td>
		<td class="text-center">
			<button type="button" class="btn btn-info btn-sm" onclick="modifyPoiInfo('{poiNo}');">수정</button>
			<button type="button" class="btn btn-danger btn-sm" onclick="deletePoiInfo('{poiNo}')">삭제</button>
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
			<td><input type="text" id="poiNo" name="poiNo" class="form-control input-sm" style="width:100px;" title="POI번호" required data-rules="require" readonly value="{poiNo}"></td>
		</tr>
		<tr>
			<th>POI명</th>
			<td><input type="text" name="poiNm" class="form-control input-sm" maxlength="30" title="POI명" required data-rules="require" value="{poiNm}"></td>
		</tr>
		<tr>
			<th>객체코드</th>
			<td>
				<input type="text" name="dvcCd" class="form-control input-sm" maxlength="50" title="객체코드" value="{dvcCd}">
				<div class="input_autocomplete" ><ul class=""></ul></div>
			</td>
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
				</div>
			</td>
		</tr>
		<tr>
			<th>도면</th>
			<td>{mapNm} {floorInfo.floorNm}</td>
		</tr>
		<tr>
			<th>배치</th>
			<td id="positionYnTxt">{positionYn}</td>
		</tr>
		<tr>
			<th>POI설명</th>
			<td><textarea type="text" name="etcDesc" style="resize: none;" class="form-control input-sm" maxlength="200" title="POI설명" >{etcDesc}</textarea></td>
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
	
	<script type="text/template" id="autoCompleteTpl">
		<li  onclick="dvcCdAutoComplete($(this));">
			<a href="javascript:;" data-dvc-cd="{dvcCd}" class="input_autocomplete">{dvcCd}</a>
		</li>
	</script>