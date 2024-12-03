<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<!-- Begin Page Content -->
 	<div class="container-fluid">

		<!-- Page Heading -->
        <div class="d-sm-flex align-items-center justify-content-between mb-4">
        	<h1 class="h4 mb-0 text-gray-800">도면 목록</h1>
		</div>

        <div class="row">
          	<div class="col-md-3">
				<div id="mapCategoryTree" class="hummingbird-treeview well h-scroll-large map_tree">
					<ul id="treeview" class="hummingbird-base" style="padding:10px;">
						<c:forEach items="${mapCategoryTree}" var="mapCategory1" varStatus="status">
						<li>
							<i class="fa fa-minus-square"></i> <label> <input name="category1No" type="checkbox" value="${mapCategory1.category1No}"> <c:out value="${mapCategory1.category1Nm}" /></label>
			            	<ul style="display: block;">
			            		<c:forEach items="${mapCategory1.categoryList}" var="mapCategory2" varStatus="status">
			            		<li>
			            			<i class="fa fa-minus-square"></i> <label> <input name="category2No" type="checkbox" value="${mapCategory2.category2No}">  <c:out value="${mapCategory2.category2Nm}" /></label>
									<ul style="display: block;">
										<c:forEach items="${mapCategory2.categoryList}" var="mapCategory3" varStatus="status">
										<li>
											<label> <input name="category3No" type="checkbox" value="${mapCategory3.category3No}"> <c:out value="${mapCategory3.category3Nm}" /></label>
										</li>
										</c:forEach>
									</ul>
								</li>
								</c:forEach>
							</ul>
						</li>
						</c:forEach>
					</ul>
      			</div>
          	</div>
          	<div class="col-md-9">
				<div class="row">
					<div class="col-md-5">
						<div class="form-row">
							<div class="col-md-12">
								<form name="schFrm" id="schFrm">
								<div class="input-group mb-3">
									<input type="text" name="mapNm" id="schMapNm" class="form-control" placeholder="도면명을 입력하세요." aria-label="도면명" aria-describedby="button-addon2">
									<div class="input-group-append">
										<button class="btn btn-primary" type="submit">검색</button>
									</div>
								</div>
								</form>
							</div>
						</div>
					</div>
					<div class="col-md-7 text-right">
						<button type="button" class="btn btn-success btn-md" id="btnChkDown"><i class="far fa-file-excel"></i> 엑셀 다운로드</button>
						<button type="button" class="btn btn-primary btn-md" id="btnMapForm">도면 등록</button>
					</div>
				</div>
	          	<div class="row" id="mapInfoList">
				</div>
				<div class="row">
				<div class="col-sm-12 col-md-12">
					<ul class="pagination">
					</ul>
				</div>
			</div>
		</div>
		</div>
		<!-- /.container-fluid -->

	</div>
    <!-- End of Main Content -->

    <!-- 도면 등록 모달 -->
  <div class="modal fade" id="mapRegistModal" tabindex="-1" role="dialog" aria-labelledby="" aria-hidden="true">
	  <div class="modal-dialog" role="document" style="max-width:700px;top:10%;">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title">도면 등록</h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body">
	        <form name="mapRegistFrm" id="mapRegistFrm" method="post" enctype="multipart/form-data">
				<fieldset>
				<table class="table table-bordered">
					<colgroup>
						<col style="width:15%">
						<col style="width:35%">
						<col style="width:15%">
						<col style="width:35%">
					</colgroup>
					<tbody>
					<tr>
						<th>도면명</th>
						<td ><input type="text" name="mapNm" id="mapNm" class="form-control input-sm" maxlength="30" title="도면명" required data-rules="require"></td>
						<th>도면코드</th>
						<td><input type="text" name="mapCd" id="mapCd" class="form-control input-sm" maxlength="30" title="도면코드" required data-rules="require"></td>
					</tr>
					<tr>
						<th>분류</th>
						<td  colspan="3">
							<div class="row">
								<div class="col-md-4">
									<select class="form-control input-sm" name="category1No" id="category1No" title="대분류" data-name="category1" required data-rules="require">
										<option value="" selected="">대분류</option>
										<c:forEach items="${mapCategoryList}" var="categoryList">
										<option value="${categoryList.category1No}"><c:out value="${categoryList.category1Nm}" /></option>
										</c:forEach>
									</select>
								</div>
								<div class="col-md-4">
									<select class="form-control input-sm" name="category2No" id="category2No" title="중분류" data-name="category2" required data-rules="require">
										<option value="">중분류</option>
									</select>
								</div>
								<div class="col-md-4">
									<select class="form-control input-sm" name="category3No" id="category3No" title="소분류" data-name="category3" required data-rules="require">
										<option value="" selected>소분류</option>
									</select>
								</div>
							</div>
						</td>
					</tr>
					<tr>
						<th>대표이미지</th>
						<td  colspan="3"><input type="file" name="mapImg" id="mapImg" class="form-control input-sm" title="대표이미지" required data-rules="require,file" data-ext="jpg,jpeg,png,gif,bmp" accept=".jpg,.jpeg,.png,.gif,.bmp"></td>
					</tr>
					<tr>
						<th>파일형식</th>
						<td colspan="3">
							<div class="d-flex align-items-center" style="gap: 0.5rem;">
								<input type="radio" id="sbm" value="sbm" name="fileType" checked><label for="sbm" style="margin:0;"> SBM 포맷</label>
								<input type="radio" id="fbx" value="fbx" name="fileType"><label for="fbx" style="margin:0;"> FBX, 3DS, glTF 포맷</label>
							</div>
						</td>
					</tr>
					<tr>
						<th>도면파일</th>
						<td colspan="3"><input type="file" name="mapZip" id="mapZip" class="form-control input-sm" title="도면파일" required data-rules="require,file" data-ext="zip" accept=".zip"></td>
					</tr>
					<tr>
						<th>2D좌표</th>
						<td colspan="3"><button type="button" id="openMap" class="btn btn-outline-secondary w-30">건물 마커 좌표등록</button>
							<div class="d-flex" style="margin-top: 0.5rem; gap: 0.5rem;">
								<input type="text" name="lat" class="form-control input w-50">
								<input type="text" name="lng" class="form-control input w-50">
							</div>
						</td>
					</tr>
					<tr>
						<th>도면설명</th>
						<td colspan="3"><textarea name="mapDesc" id="mapDesc" class="form-control input-sm" style="height:120px;" title="도면설명"></textarea></td>
					</tr>
					<tr>
						<th>사용여부</th>
						<td colspan="3">
							<div class="form-check form-check-inline">
							  <input class="form-check-input" type="radio" name="mapStts" id="mapStts1" value="0" checked>
							  <label class="form-check-label" for="mapStts1">사용</label>
							</div>
							<div class="form-check form-check-inline">
							  <input class="form-check-input" type="radio" name="mapStts" id="mapStts2" value="1">
							  <label class="form-check-label" for="mapStts2">미사용</label>
							</div>
						</td>
					</tr>
					</tbody>
				</table>
				</fieldset>
			</form>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
	        <button type="button" class="btn btn-primary" id="btnMapRegist">등록</button>
	      </div>
	    </div>
	  </div>
	</div>

 	<!-- 도면목록 다운로드폼 -->
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
	        <form name="mapListDownFrm" id="mapListDownFrm" method="post">
	        <input type="hidden" id="fieldNmList" name="fieldNmList" />
	        <input type="hidden" id="category3NoStr" name="category3NoStr" />
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
							<th><input type="checkbox" class="seqChk" name="dw" value="mapNm"></th>
							<td>도면 이름</td>
						</tr>
						<tr>
							<th><input type="checkbox" class="seqChk" name="dw" value="mapCd"></th>
							<td>도면 코드</td>
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
							<th><input type="checkbox" class="seqChk" name="dw" value="category3No"></th>
							<td>소분류</td>
						</tr>
						<tr>
							<th><input type="checkbox" class="seqChk" name="dw" value="mapDesc"></th>
							<td>도면 설명</td>
						</tr>
						<tr>
							<th><input type="checkbox" class="seqChk" name="dw" value="mapVer"></th>
							<td>도면 버전</td>
						</tr>
						<tr>
							<th><input type="checkbox" class="seqChk" name="dw" value="mapStts"></th>
							<td>보안검 검토</td>
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

 	<script type="text/template" id="mapInfoListTpl">
	<div class="col-md-3">
    	<div class="card mb-4 shadow-sm">
        	<img src="/map/{mapNo}/{imgFileNm}" class="rounded" alt="대표이미지" style="height:250px;" onerror="fn_noPhoto(this)">
           	<div class="card-body">
            	<p class="card-text">{mapNm}</p>
             	<div class="d-flex justify-content-between align-items-center">
               		<div class="btn-group" style="width:100%;">
                 		<a href="/adm/map/mapView.do?mapNo={mapNo}" class="btn btn-sm btn-outline-secondary">도면관리</a>
                 		<a href="/adm/map/viewer.do?mapNo={mapNo}" target="_blank" class="btn btn-sm btn-outline-secondary">공간관리</a>
						<a href="/viewer/index.do?mapNo={mapNo}" target="_blank" class="btn btn-sm btn-outline-secondary">3D뷰어</a>
               		</div>
             	</div>
           	</div>
		</div>
	</div>
	</script>
