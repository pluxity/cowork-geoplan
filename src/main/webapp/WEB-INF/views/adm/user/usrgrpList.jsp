<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<!-- Begin Page Content -->
 	<div class="container-fluid">

		<!-- Page Heading -->
        <div class="d-sm-flex align-items-center justify-content-between mb-4">
        	<h1 class="h4 mb-0 text-gray-800">그룹/권한 목록</h1>
		</div>

        <div class="row">
			<div class="col-md-5">
				<form name="schFrm" id="schFrm">
				<div class="form-row">
					<!--
					Total <span class="badge badge-primary">12,220</span> ( <strong style="color:#c00">1</strong> / 611 )
					-->
					<div class="col-md-3">
						<select class="form-control input-sm" name="grpType" id="grpType" >
							<option value="">그룹타입</option>
							<option value="ROLE_USER">일반</option>
							<option value="ROLE_ADMIN">관리자</option>
						</select>
					</div>
					<div class="col-md-3">
						<select class="form-control input-sm" name="searchType" id="searchType" >
							<option value="name">그룹명</option>
						</select>
					</div>
					<div class="col-md-6">
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
			<div class="col-md-7 text-right">
				<button type="button" class="btn btn-success btn-md" id="btnChkDown"><i class="far fa-file-excel"></i> 엑셀 다운로드</button>
				<button type="button" class="btn btn-primary btn-md" id="btnUsrForm">등록</button>
				<button type="button" class="btn btn-danger btn-md" id="btnUsrDel">삭제</button>
			</div>
		</div>
         	<div class="row" style="padding:0 12px 0 16px;">
         		<table class="table table-bordered sort-list">
				<colgroup>
					<col style="width:3%">
					<col style="width:5%">
					<col style="width:20%">
					<col style="width:10%">
					<col style="width:10%">
					<col style="width:12%">
					<col style="width:15%">
				</colgroup>
				<thead>
				    <tr>
				      	<th scope="col"><input type="checkbox" class="chkall"></th>
				      	<th scope="col" data-sort-attr="grp_no">번호</th>
				      	<th scope="col" data-sort-attr="grp_nm">그룹명</th>
				      	<th scope="col">그룹타입</th>
				      	<th scope="col">등록자</th>
				      	<th scope="col" data-sort-attr="reg_dt">등록일</th>
				      	<th scope="col">관리</th>
				    </tr>
				</thead>
				<tbody id="usrgrpInfoList">
				</tbody>
			</table>
		</div>
		<div class="row">
			<div class="col-sm-12 col-md-12">
				<ul class="pagination paging-center">
				</ul>
			</div>
		</div>
		<!-- /.container-fluid -->
	</div>
    <!-- End of Main Content -->

    <!-- 사용자그룹 등록폼 -->
  	<div class="modal fade" id="usrgrpRegistModal" tabindex="-1" role="dialog" aria-labelledby="" aria-hidden="true">
	  <div class="modal-dialog" role="document" style="max-width:700px;">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title">사용자그룹 등록</h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body">
	        <form name="usrgrpRegistFrm" id="usrgrpRegistFrm" method="post">
				<fieldset>
				<table class="table table-bordered">
					<colgroup>
						<col style="width:20%">
						<col style="width:80%">
					</colgroup>
					<tbody>
					<tr>
						<th>그룹명</th>
						<td><input type="text" name="grpNm" class="form-control input-sm" maxlength="30" title="그룹명" required data-rules="require"></td>
					</tr>
					<tr>
						<th>그룹타입</th>
						<td>
							<select class="form-control input-sm" name="grpType" required data-rules="require" title="그룹타입">
								<option value="">선택</option>
								<option value="ROLE_USER">일반</option>
								<option value="ROLE_ADMIN">관리자</option>
							</select>
						</td>
					</tr>
					<tr>
						<th>설명</th>
						<td><textarea name="mapDesc" class="form-control input-sm" style="height:120px;" title="그룹설명"></textarea></td>
					</tr>
					</tbody>
				</table>
				</fieldset>
			</form>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
	        <button type="button" class="btn btn-primary" id="btnUsrRegist">등록</button>
	      </div>
	    </div>
	  </div>
	</div>

	<!-- 사용자그룹 수정폼 -->
  	<div class="modal fade" id="usrgrpModifyModal" tabindex="-1" role="dialog" aria-labelledby="" aria-hidden="true">
	  <div class="modal-dialog" role="document" style="max-width:700px;">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title">사용자그룹 수정</h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body">
	        <form name="usrgrpModifyFrm" id="usrgrpModifyFrm" method="post">
			</form>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
	        <button type="button" class="btn btn-primary" id="btnUsrModify">수정</button>
	      </div>
	    </div>
	  </div>
	</div>

	<!-- 사용자그룹 권한 수정폼 -->
  	<div class="modal fade" id="usrgrpRoleModifyModal" tabindex="-1" role="dialog" aria-labelledby="" aria-hidden="true">
	  <div class="modal-dialog" role="document" style="max-width:700px;">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title">사용자그룹 권한 수정</h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body">
	      	<form name="usrgrpRoleModifyFrm" id="usrgrpRoleModifyFrm" method="post">
	      	<input type="hidden" name="grpNo">
	      	<input type="hidden" name="grpType">
	      	<div class="float-left" style="padding:5px;"><b>도면 권한</b></div>
	      	<div class="float-right"><button type="button" class="btn btn-primary btn-sm" id="btnUsrgrpMapSave">저장</button></div>
	        <div id="mapCategoryRole" class="hummingbird-treeview well h-scroll-large" style="width:100%;height:200px;border:1px solid silver;overflow:auto;margin-bottom:20px;">
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
										<i class="fa fa-minus-square"></i> <label> <input name="category3No" type="checkbox" value="${mapCategory3.category3No}"> <c:out value="${mapCategory3.category3Nm}" /></label>
										<ul style="display:block;">
											<c:forEach items="${mapCategory3.categoryList}" var="mapCategory4" varStatus="status">
											<li>
												<label> <input name="mapNo" type="checkbox" value="${mapCategory4.mapNo}"> <c:out value="${mapCategory4.mapNm}" /></label>
											</li>
											</c:forEach>
										</ul>
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
     		<div class="float-left" style="padding:5px;"><b>장비 권한</b></div>
	      	<div class="float-right"><button type="button" class="btn btn-primary btn-sm" id="btnUsrgrpPoiSave">저장</button></div>
     		<div id="poiCategoryRole" style="width:100%;height:200px;border:1px solid silver;overflow:auto;margin-bottom:20px;">
     			<table class="table table-bordered">
					<colgroup>
						<col>
						<col style="width:20%">
					</colgroup>
					<thead>
					    <tr>
					      	<th scope="col">장비 분류</th>
					      	<th scope="col">표출<input type="checkbox" name="poiRoleType1All" value="all" class="chkall"></th>
					      	<!-- <th scope="col">제어<input type="checkbox" name="poiRoleType2All" value="all" class="chkall"></th> -->
					    </tr>
					</thead>
					<tbody>
					<c:forEach items="${poiCategoryList}" var="list">
						<tr>
							<td class="text-left">${list.category1Nm}</td>
							<td class="text-center"><input type="checkbox" name="poiRoleType1" value="${list.category1No}"></td>
							<!-- <td class="text-center"><input type="checkbox" name="poiRoleType2" value="${list.category1No}"></td> -->
						</tr>
					</c:forEach>
					</tbody>
				</table>
     		</div>

     		<div class="float-left" style="padding:5px;"><b>메뉴 권한</b></div>
	      	<div class="float-right"><button type="button" class="btn btn-primary btn-sm" id="btnUsrgrpMenuSave">저장</button></div>
     		<div id="menuRole" style="width:100%;height:200px;border:1px solid silver;overflow:auto;margin-bottom:20px;">
     			<table class="table table-bordered">
					<colgroup>
						<col>
						<col>
						<col style="width:20%">
					</colgroup>
					<thead>
					    <tr>
					      	<th scope="col">메뉴명</th>
					      	<th scope="col">URL구분</th>
					      	<th scope="col">접근권한<input type="checkbox" name="allowUrlAll" value="all" class="chkall"></th>
					    </tr>
					</thead>
					<tbody>
						<c:forEach items="${admMenuList}" var="menuList">
						<tr>
							<td class="text-left">${menuList.menuNm}</td>
							<td class="text-left">${menuList.menuKey}</td>
							<td class="text-center"><input type="checkbox" name="allowUrl" value="${menuList.menuKey}"></td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
     		</div>
     	  </form>
	      </div>
	    </div>
	  </div>
	</div>

	<!-- 사용자 그룹 다운로드폼 -->
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
	        <form name="usrGrpListDownFrm" id="usrGrpListDownFrm" method="post">
	        <input type="hidden" id="fieldNmList" name="fieldNmList" />
	        <input type="hidden" id="grpTypeEd" name="grpType" />
	        <input type="hidden" id="searchKeywordEd" name="searchKeyword" />
	        <input type="hidden" id="searchTypeEd" name="searchType" />
	        <input type="hidden" id="sortBy" name="sortBy" />
	        <input type="hidden" id="sortType" name="sortType" />
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
							<th><input type="checkbox" class="seqChk" name="dw" value="grpNo"></th>
							<td>그룹 번호</td>
						</tr>
						<tr>
							<th><input type="checkbox" class="seqChk" name="dw" value="grpNm"></th>
							<td>그룹명</td>
						</tr>
						<tr>
							<th><input type="checkbox" class="seqChk" name="dw" value="grpTypeNm"></th>
							<td>그룹타입</td>
						</tr>
						<tr>
							<th><input type="checkbox" class="seqChk" name="dw" value="grpDesc"></th>
							<td>그룹 설명</td>
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


 	<script type="text/template" id="usrgrpInfoListTpl">
	<tr>
		<th scope="row"><input type="checkbox" class="seqChk" name="grpNo" value="{grpNo}"></th>
		<td class="text-center">{grpNo}</td>
		<td class="text-center" onclick="modifyUsrgrpInfo('{grpNo}');" style="cursor:pointer">{grpNm}</td>
		<td class="text-center">{grpTypeNm}</td>
		<td class="text-center">{regUsr}</td>
		<td class="text-center">{regDt}</td>
		<td class="text-center">
			<button type="button" class="btn btn-dark btn-sm" onclick="modifyUsrgrpRole('{grpNo}', '{grpType}')">권한</button>
			<button type="button" class="btn btn-info btn-sm" onclick="modifyUsrgrpInfo('{grpNo}');">수정</button>
			<button type="button" class="btn btn-danger btn-sm" onclick="deleteUsrgrpInfo('{grpNo}')">삭제</button>
		</td>
	</tr>
	</script>
	<script type="text/template" id="usrgrpInfoFrmTpl">
	<input type="hidden" name="grpNo" value="{grpNo}">
	<fieldset>
	<table class="table table-bordered">
		<colgroup>
			<col style="width:20%">
			<col style="width:80%">
		</colgroup>
		<tbody>
			<tr>
				<th>그룹명</th>
				<td><input type="text" name="grpNm" class="form-control input-sm" maxlength="30" title="그룹명" required data-rules="require" value="{grpNm}"></td>
			</tr>
			<tr>
				<th>그룹타입</th>
				<td>
					<select class="form-control input-sm" name="grpType" required data-rules="require" title="그룹타입">
						<option value="">그룹타입</option>
								<option value="ROLE_USER">일반</option>
								<option value="ROLE_ADMIN">관리자</option>
					</select>
				</td>
			</tr>
			<tr>
				<th>설명</th>
				<td><textarea name="mapDesc" class="form-control input-sm" style="height:120px;" title="그룹설명">{grpDesc}</textarea></td>
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