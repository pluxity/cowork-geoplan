<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- Begin Page Content -->
<div class="container-fluid">

	<!-- Page Heading -->
	<div class="d-sm-flex align-items-center justify-content-between mb-4">
		<h1 class="h4 mb-0 text-gray-800">아이콘셋 목록</h1>
	</div>
	<div class = "row px-3">
		<div class="col-md-4 p-0">
			<form name="schFrm" id="schFrm" onsubmit="return false">
				<div class="input-group mb-2 float-left">
					<input type="text" name="iconsetNm" id="schIconsetNm" class="form-control" placeholder="아이콘셋 이름을 입력하세요." aria-label="아이콘셋" aria-describedby="button-addon2">
					<div class="input-group-append">
						<button class="btn btn-primary" id="schBtn">검색</button>
					</div>
				</div>
			</form>
		</div>
		<div class="col-md-8 p-0">
			<div class="float-right">
				<button type="button" class="btn btn-success btn-md" id="btnChkDown"><i class="far fa-file-excel"></i> 엑셀 다운로드</button>
				<a href="#" id="createPoiIcon" class="btn btn-primary active" role="button" aria-pressed="true">아이콘셋 등록</a>
				<a href="#" id="deletePOIIconList" class="btn btn-danger active" role="button" aria-pressed="true">삭제</a>
			</div>
		</div>

		<table class="table table-bordered sort-list">
			<colgroup>
				<col style="width:5%">
				<col style="width:15%">
				<col style="width:15%">
				<col style="width:10%">
				<col style="width:10%">
				<col style="width:10%">
				<col style="width:10%">
				<col style="width:10%">
				<col style="width:10%">
				<col style="width:10%">
			</colgroup>
			<thead>
			    <tr>
			      	<th scope="col"><input type="checkbox" class="chkall"></th>
			      	<th scope="col"  data-sort-attr="iconset_nm">이름</th>
			      	<th scope="col">설명</th>
			      	<th scope="col">이미지 아이콘</th>
			      	<th scope="col">단계별 아이콘</th>
			      	<th scope="col">3D 아이콘</th>
			      	<th scope="col">등록자</th>
			      	<th scope="col" data-sort-attr="reg_dt">등록일</th>
			      	<th scope="col">관리</th>
			    </tr>
			</thead>
			<tbody id="poiIconList">
			</tbody>
		</table>
	</div>
	<div class="row">
		<div class="col-sm-12 col-md-12">
		<ul class="pagination paging-center">
		</ul>
	</div>
</div>
<!-- End of Main Content -->


<!-- POI 아이콘 모달 -->
<div class="modal fade" id="poiCategoryIconModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog" role="document" style="max-width:900px;">
	   <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title">타이틀 입력</h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body">
	      <input type="hidden" name="modalType" id="modalType" class="form-control input-sm">
	        <form name="poiIconFrm" id="poiIconFrm" method="post" action="/adm/poi/poiIconRegist.json" enctype="multipart/form-data">
			</form>
      </div>
      <div class="modal-footer">
      	<button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
        <button type="submit" class="btn btn-primary" id="btnPoiIconSubmit">등록</button>
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
	        <form name="iconListDownFrm" id="iconListDownFrm" method="post">
				<input type="hidden" id="sortType" name="sortType" value=""/>
				<input type="hidden" id="sortBy" name="sortBy" value=""/>
		      	<input type="hidden" id="fieldNmList" name="fieldNmList" />
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
							<th><input type="checkbox" class="seqChk" name="dw" value="iconsetNm"></th>
							<td>아이콘셋 이름</td>
						</tr>
						<tr>
							<th><input type="checkbox" class="seqChk" name="dw" value="iconsetDesc"></th>
							<td>아이콘셋 설명</td>
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

<script type="text/template" id="POIIconListTpl">
<tr data-iconset-no="{iconsetNo}">
	<th scope="row"><input type="checkbox" class="seqChk"></th>
	<td class="text-left">{iconsetNm}</td>
	<td class="text-left">{iconsetDesc}</td>
	<td>
		<img src="{iconset2d0FilePath}" class="rounded" alt="이미지 아이콘" title="이미지 아이콘" style="height : 32px; text-align: center;" onerror="fn_noPhoto(this)">
	</td>
	<td>
		<img src="{iconset2d1FilePath}" class="rounded" alt="1단계 아이콘" title="1단계 아이콘" style="height : 32px; text-align: center;" onerror="fn_noPhoto(this)">
		<img src="{iconset2d2FilePath}" class="rounded" alt="2단계 아이콘" title="2단계 아이콘" style="height : 32px; text-align: center;" onerror="fn_noPhoto(this)">
		<img src="{iconset2d3FilePath}" class="rounded" alt="3단계 아이콘" title="3단계 아이콘" style="height : 32px; text-align: center;" onerror="fn_noPhoto(this)">
		<img src="{iconset2d4FilePath}" class="rounded" alt="4단계 아이콘" title="4단계 아이콘" style="height : 32px; text-align: center;" onerror="fn_noPhoto(this)">
	</td>
	<td>
		<img src="{iconset3dThumbFilePath}" class="rounded" alt="3D 아이콘" title="3D 아이콘" style="height : 32px; text-align: center;" onerror="fn_noPhoto(this)">
	</td>
	<td>{regUsr}</td>
	<td>{regDt}</td>
	<td>
		<button type="button" class="btn btn-sm btn-info modifyPOIIcon">수정</button>
		<button type="button" class="btn btn-sm btn-danger deletePOIIcon">삭제</button>
	</td>
</tr>
</script>
<script type="text/template" id="POIIconModalTpl">
	<fieldset>
		<input type="hidden" name="iconsetNo" id="iconsetNo" class="form-control input-sm" value="{iconsetNo}">
		<table class="table table-bordered">
			<colgroup>
				<col style="width:20%">
				<col style="width:80%">
			</colgroup>
			<tbody>
				<tr>
					<th>아이콘셋 이름</th>
					<td><input type="text" name="iconsetNm" id="iconsetNm" class="form-control input-sm" value="{iconsetNm}" maxlength="30" data-rules="require" required title="아이콘셋 이름"></td>
				</tr>
				<tr>
					<th>설명</th>
					<td><input type="text" name="iconsetDesc" id="iconsetDesc" class="form-control input-sm" value="{iconsetDesc}" maxlength="30"></td>
				</tr>
				<tr>
					<th>기본 2D 아이콘</th>
					<td>
						<div class="row m-0">
							<div class="col-md-6" style="text-align: center;">
								<img src="{iconset2d0FilePath}" class="rounded" style="height : 64px" alt="대표이미지">
								<input type="file" name="poi2d_0" id="poi2d_0" class="form-control" data-rules="file" data-ext="jpg,jpeg,png,gif,bmp" accept=".jpg,.jpeg,.png,.gif,.bmp" title="기본 2D 아이콘">
							</div>
						</div>
					</td>
				</tr>
				<tr>
					<th>기본 3D 아이콘</th>
					<td>
						<div class="row m-0">
							<div class="col-md-6" style="text-align: center;">
								<img src="{iconset3dThumbFilePath}" class="rounded" style="height : 64px" alt="대표이미지" title="대표이미지" onerror="fn_noPhoto(this)">
								<input type="file" name="poi3d" id="poi3d" class="form-control" data-rules="file" data-ext="zip" accept=".zip" title="3D 아이콘">
							</div>
						</div>
					</td>
				</tr>
				<tr>
					<th>단계별 아이콘 종류</th>
					<td>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="radio" name="iconsetType" id="inlineRadio1" value="2D" checked>
							<label class="form-check-label" for="inlineRadio1">2D</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="radio" name="iconsetType" id="inlineRadio2" value="3D">
							<label class="form-check-label" for="inlineRadio2">3D</label>
						</div>
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="radio" name="iconsetType" id="inlineRadio3" value="2D3D">
							<label class="form-check-label" for="inlineRadio3">2D + 3D</label>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</fieldset>
</script>
