<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<!-- Begin Page Content -->
 	<div class="container-fluid">

		<!-- Page Heading -->
        <div class="d-sm-flex align-items-center justify-content-between mb-4">
        	<h1 class="h4 mb-0 text-gray-800">시스템 설정</h1>
		</div>


        <div class="row">
        	<div class="col-md-12">
	        	<form id="sysSettingsFrm" name="sysSettingsFrm" method="post"  enctype="multipart/form-data">
	        	<table class="table table-bordered" >
					<colgroup>
						<col style="width:10%">
						<col style="width:10%">
						<col style="width:80%">
					</colgroup>
					<tbody id="sysSettings">
					<tr>
						<th  colspan="2">POI 라인 길이</th>
						<td><input type="number" class="form-control input-sm" id="poiLength" name="poiLength" value="${systemInfoVO.poiLength}" size="2" min="1" max="20" step="1" data-rules='Integer' style="width:100px;"></td>
					</tr>
					<tr>
						<th  colspan="2">POI 아이콘 크기 비율</th>
						<td><input type="number" class="form-control input-sm" id="poiIconRatio" name="poiIconRatio" value="${systemInfoVO.poiIconRatio}" size="2" min="5" max="50" step="5" data-rules='Integer' style="width:100px;">
						</td>
					</tr>
					<tr>
						<th  colspan="2">POI 텍스트 크기 비율</th>
						<td><input type="number" class="form-control input-sm" id="poiTextRatio" name="poiTextRatio" value="${systemInfoVO.poiTextRatio}" size="2" min="5" max="50" step="5" data-rules='Integer' style="width:100px;"></td>
					</tr>
					</tbody>
				</table>
				<div style="float:right;">
					<button type="button" class="btn btn-danger btn-md" id="btnCancel" >취소</button>
					<button type="submit" class="btn btn-primary btn-md" id="btnSubmit">저장</button>
				</div>
				</form>
			</div>
        </div>
	</div>
    <!-- End of Main Content -->
