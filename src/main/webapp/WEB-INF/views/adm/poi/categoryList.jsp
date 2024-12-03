<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- Begin Page Content -->
<div class="container-fluid">

	<!-- Page Heading -->
	<div class="d-sm-flex align-items-center justify-content-between mb-4">
		<h1 class="h4 mb-0 text-gray-800">POI 분류</h1>
	</div>
	<div class="row px-3">
		<div class="col-md-6 bg-light">
			<div class="form-group mb-1">
				<label for="category1">대분류</label>
				<select class="form-control" id="category1" size="15">
				</select>
			</div>
			<div class="float-left">
				<i class="far fa-caret-square-up fa-lg cursor-pointer"></i>
				<i class="far fa-caret-square-down fa-lg cursor-pointer"></i>
			</div>
			<div class="float-right">
				<i class="far fa-plus-square fa-lg cursor-pointer"></i>
				<i class="far fa-minus-square fa-lg cursor-pointer"></i>
				<i class="far fa-edit fa-lg cursor-pointer"></i>
			</div>
		</div>
		<div class="col-md-6 bg-light">
			<div class="form-group mb-1">
				<label for="category2">중분류</label>
				<select class="form-control" id="category2" size="15">
				</select>
			</div>
			<div class="float-left">
				<i class="far fa-caret-square-up fa-lg cursor-pointer"></i>
				<i class="far fa-caret-square-down fa-lg cursor-pointer"></i>
			</div>
			<div class="float-right">
				<i class="far fa-plus-square fa-lg cursor-pointer"></i>
				<i class="far fa-minus-square fa-lg cursor-pointer"></i>
				<i class="far fa-edit fa-lg cursor-pointer"></i>
			</div>
		</div>
	</div>
</div>

<!-- 도면 등록 모달 -->
<div class="modal fade" id="mapCategoryModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog" role="document" style="max-width:700px;">
	   <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title">타이틀 입력</h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body">
	      <input type="hidden" name="modalType" id="modalType" class="form-control input-sm">
	        <form name="POICategoryRegistFrm" id="POICategoryRegistFrm" method="post" action="" enctype="multipart/form-data">
	        	<input type="hidden" name="categoryType" id="categoryType" class="form-control input-sm">
	        	<input type="hidden" name="categoryNo" id="categoryNo" class="form-control input-sm">
	        	<input type="hidden" name="iconsetNo" id="iconsetNo" class="form-control input-sm">
				<fieldset>
					<table class="table table-bordered">
						<colgroup>
							<col style="width:20%">
							<col style="width:80%">
						</colgroup>
						<tbody>
							<tr>
								<th>대분류</th>
								<td>
									<div class="row">
										<!-- <input type="hidden" name="category1No" id="category1No" value="1"> -->
										<div class="col-md-6">
											<select class="form-control input-sm" name="category1No" id="category1No" title="대분류">
												<option value="1" selected>서비스센터</option>
											</select>
										</div>
									</div>
								</td>
							</tr>
							<tr>
								<th>카테고리명</th>
								<td><input type="text" name="categoryNm" id="categoryNm" class="form-control input-sm" maxlength="30" data-rules="require" required title="카테고리명"></td>
							</tr>
						</tbody>
					</table>
				</fieldset>
			</form>
			<div class="input-group mb-2" id="iconSearch">
				<input type="text" name="iconsetNm" id="schIconsetNm" class="form-control" placeholder="아이콘셋 이름을 입력하세요." aria-label="아이콘셋" aria-describedby="button-addon2">
				<div class="input-group-append">
					<button type="button" class="btn btn-primary" id="schBtn">검색</button>
				</div>
			</div>
			<div style="overflow-y: auto; max-height: 300px;">
				<table class="table table-bordered" id="iconTable">
					<colgroup>
						<col style="width:5%">
						<col style="width:35%">
						<col style="width:15%">
						<col style="width:30%">
						<col style="width:15%">
					</colgroup>
					<thead>
				    	<tr>
					      	<th scope="col"></th>
					      	<th scope="col">이름</th>
					      	<th scope="col">2D 아이콘</th>
					      	<th scope="col">단계별 아이콘</th>
					      	<th scope="col">3D 아이콘</th>
				   		</tr>
				   	</thead>
					<tbody id="poiIconList">
					</tbody>
				</table>
			</div>

			</div>
      	<div class="modal-footer">
        	<button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
        	<button type="button" class="btn btn-primary" id="btnPOICategoryRegist">등록</button>
      	</div>
    </div>
  </div>
</div>
<!-- End of Main Content -->


<script type="text/template" id="POIIconListTpl">
	<tr data-iconset-no="{iconsetNo}">
		<th scope="row"><input type="radio" name="iconsetNo_" class="iconsetNo_" value="{iconsetNo}" /></th>
		<td>{iconsetNm}</td>
		<td>
			<img src="{iconset2d0FilePath}" class="rounded" style="height : 32px; text-align: center;" onerror="fn_noPhoto(this)">
		</td>
		<td>
			<img src="{iconset2d1FilePath}" class="rounded" style="height : 32px; text-align: center;" onerror="fn_noPhoto(this)">
			<img src="{iconset2d2FilePath}" class="rounded" style="height : 32px; text-align: center;" onerror="fn_noPhoto(this)">
			<img src="{iconset2d3FilePath}" class="rounded" style="height : 32px; text-align: center;" onerror="fn_noPhoto(this)">
			<img src="{iconset2d4FilePath}" class="rounded" style="height : 32px; text-align: center;" onerror="fn_noPhoto(this)">
		</td>
		<td>
			<img src="{iconset3dThumbFilePath}" class="rounded" style="height : 32px; text-align: center;" onerror="fn_noPhoto(this)">
		</td>
	</tr>
</script>