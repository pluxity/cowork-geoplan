<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<!-- Begin Page Content -->
 	<div class="container-fluid">

		<!-- Page Heading -->
        <div class="d-sm-flex align-items-center justify-content-between mb-4">
        	<h1 class="h4 mb-0 text-gray-800">사용자 목록</h1>
		</div>

        <div class="row">
			<div class="col-md-4">
				<form name="schFrm" id="schFrm">
					<div class="form-row">
					<div class="col-md-4">
						<select class="form-control input-sm" name="searchType" id="searchType" >
							<option value="id">아이디</option>
							<option value="name">사용자명</option>
							<option value="group">그룹명</option>
							<option value="dept">부서명</option>
						</select>
					</div>
					<div class="col-md-8">
						<div class="input-group mb-3">
							<input type="text" name="searchKeyword" id="searchKeyword" class="form-control" placeholder="검색어를 입력하세요." aria-label="검색어" aria-describedby="button-addon2">
							<div class="input-group-append">
								<button class="btn btn-primary" type="submit">검색</button>
							</div>&nbsp;
							<span class="btn btn-success">
							현재 로그인수 <span class="badge badge-light" id="loginUsrCnt">0</span>
							</span>
						</div>
					</div>
				</div>
				</form>
			</div>
			<div class="col-md-8 text-right">
				<button type="button" class="btn btn-success btn-md" id="btnChkDown"><i class="far fa-file-excel"></i> 엑셀 다운로드</button>
				<button type="button" class="btn btn-primary btn-md" id="btnUsrForm">등록</button>
				<button type="button" class="btn btn-danger btn-md" id="btnUsrDel">삭제</button>
			</div>
		</div>
         	<div class="row" style="padding:0 12px 0 16px;">
         		<table class="table table-bordered sort-list">
				<colgroup>
					<col style="width:3%">
					<col style="width:3%">
					<col style="width:5%">
					<col style="width:8%">
					<col>
					<col style="width:14%">
					<col style="width:13%">
					<col style="width:8%">
					<col style="width:13%">
					<col style="width:15%">
				</colgroup>
				<thead>
				    <tr>
				      	<th scope="col"><input type="checkbox" class="chkall"></th>
				      	<th scope="col">번호</th>
						<th scope="col">접속</th>
				      	<th scope="col" data-sort-attr="usr_id">아이디</th>
				      	<th scope="col" data-sort-attr="usr_nm">사용자명</th>
				      	<th scope="col" data-sort-attr="grp_nm">그룹명</th>
				      	<th scope="col" data-sort-attr="last_login_dt">마지막 로그인</th>
				      	<th scope="col">등록자</th>
				      	<th scope="col" data-sort-attr="reg_dt">등록일</th>
				      	<th scope="col">관리</th>
				    </tr>
				</thead>
				<tbody id="usrInfoList">
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

    <!-- 사용자 등록폼 -->
  	<div class="modal fade" id="usrRegistModal" tabindex="-1" role="dialog" aria-labelledby="" aria-hidden="true">
	  <div class="modal-dialog" role="document" style="max-width:700px;">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title">사용자 등록</h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body">
	        <form name="usrRegistFrm" id="usrRegistFrm" method="post">
				<fieldset>
				<table class="table table-bordered">
					<colgroup>
						<col style="width:20%">
						<col style="width:80%">
					</colgroup>
					<tbody>
					<tr>
						<th>아이디</th>
						<td><input type="text" name="usrId" id="regUsrId" class="form-control input-sm" maxlength="20" title="아이디" required data-rules="require"></td>
					</tr>
					<tr>
						<th>사용자명</th>
						<td><input type="text" name="usrNm" class="form-control input-sm" maxlength="30" title="사용자명" required data-rules="require"></td>
					</tr>
					<tr>
						<th>비밀번호</th>
						<td>
							<p style="padding-top:2px;padding-bottom:2px;">비밀번호는 8~16자리 영문,숫자,특수문자 조합으로 입력하세요.</p>
							<div class="row">
								<div class="col-md-5">
									<input type="password" name="usrPwd" id="regUsrPwd" class="form-control input-sm" maxlength="16" title="비밀번호" required data-rules="require">
								</div>
								<div class="col-md-7">
									<div class="input-group">
							  		<div class="input-group-prepend">
							    		<span class="input-group-text">확인</span>
							  		</div>
							  		<input type="password" name="reUsrpwd" id="reRegUsrpwd" class="form-control input-sm" maxlength="16" title="비밀번호확인" required data-rules="require">
							  	</div>
								</div>
							</div>
						</td>
					</tr>
					<tr>
						<th>사용자그룹</th>
						<td>
							<select class="form-control input-sm" name="grpNo" required data-rules="require" title="사용자그룹">
								<option value="">선택</option>
								<c:forEach items="${usrgrpList}" var="grpList">
								<option value="${grpList.grpNo}"><c:out value="${grpList.grpNm}" /></option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<%-- <tr>
						<th>사용자 IP</th>
						<td><input type="text" id="regUsrIp" name="usrIp" class="form-control input-sm" maxlength="15" title="사용자 IP" required data-rules="require"></td>
					</tr> --%>
					<tr>
						<th>연락처</th>
						<td><input type="text" name="usrTel" class="form-control input-sm" maxlength="15" title="연락처"></td>
					</tr>
					<tr>
						<th>부서명</th>
						<td><input type="text" name="usrDept" class="form-control input-sm" maxlength="30" title="부서명"></td>
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

	<!-- 사용자 수정폼 -->
  	<div class="modal fade" id="usrModifyModal" tabindex="-1" role="dialog" aria-labelledby="" aria-hidden="true">
	  <div class="modal-dialog" role="document" style="max-width:700px;">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title">사용자 수정</h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body">
	        <form name="usrModifyFrm" id="usrModifyFrm" method="post">
			</form>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
	        <button type="button" class="btn btn-primary" id="btnUsrModify">수정</button>
	      </div>
	    </div>
	  </div>
	</div>

	<!-- 사용자 다운로드폼 -->
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
	        <form name="usrListDownFrm" id="usrListDownFrm" method="post">
	        <input type="hidden" id="fieldNmList" name="fieldNmList" />
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
							<th><input type="checkbox" class="seqChk" name="dw" value="usrNo"></th>
							<td>사용자 번호</td>
						</tr>
						<tr>
							<th><input type="checkbox" class="seqChk" name="dw" value="usrId"></th>
							<td>아이디</td>
						</tr>
						<tr>
							<th><input type="checkbox" class="seqChk" name="dw" value="usrNm"></th>
							<td>사용자명</td>
						</tr>
						<tr>
							<th><input type="checkbox" class="seqChk" name="dw" value="grpNm"></th>
							<td>사용자 그룹명</td>
						</tr>
						<tr>
							<th><input type="checkbox" class="seqChk" name="dw" value="usrDept"></th>
							<td>부서명</td>
						</tr>
						<tr>
							<th><input type="checkbox" class="seqChk" name="dw" value="usrTel"></th>
							<td>연락처</td>
						</tr>
						<tr>
							<th><input type="checkbox" class="seqChk" name="dw" value="lastLoginDt"></th>
							<td>마지막 로그인</td>
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

 	<script type="text/template" id="usrInfoListTpl">
	<tr>
		<th scope="row"><input type="checkbox" class="seqChk" name="usrNo" value="{usrNo}"></th>
		<td class="text-center">{usrNo}</td>
		<td class="text-center text-success is-login" data-usr-id="{usrId}" style="font-size:20px;"></td>
		<td class="text-center" onclick="modifyUsrInfo('{usrNo}');" style="cursor:pointer">{usrId}</td>
		<td class="text-center">{usrNm}</td>
		<td class="text-center">{grpNm}</td>
		<td class="text-center">{lastLoginDt}</td>
		<td class="text-center">{regUsr}</td>
		<td class="text-center">{regDt}</td>
		<td class="text-center">
			<button type="button" class="btn btn-info btn-sm" onclick="modifyUsrInfo('{usrNo}');">수정</button>
			<button type="button" class="btn btn-danger btn-sm" onclick="deleteUsrInfo('{usrNo}')">삭제</button>
			<button type="button" class="btn btn-warning btn-sm" onclick="expireUsrSession('{usrId}');">로그아웃</button>
		</td>
	</tr>
	</script>
	<script type="text/template" id="usrInfoFrmTpl">
	<input type="hidden" name="usrNo" value="{usrNo}">
	<fieldset>
	<table class="table table-bordered">
		<colgroup>
			<col style="width:20%">
			<col style="width:80%">
		</colgroup>
		<tbody>
		<tr>
			<th>아이디</th>
			<td>{usrId}</td>
		</tr>
		<tr>
			<th>사용자명</th>
			<td><input type="text" name="usrNm" class="form-control input-sm" maxlength="30" title="사용자명" required data-rules="require" value="{usrNm}"></td>
		</tr>
		<tr>
			<th>비밀번호</th>
			<td>
				<p style="padding-top:2px;padding-bottom:2px;">비밀번호는 8~16자리 영문,숫자,특수문자 조합으로 입력하세요.</p>
				<div class="row">
					<div class="col-md-5">
						<input type="password" name="usrPwd" id="updUsrPwd" class="form-control input-sm" maxlength="16" title="비밀번호">
					</div>
					<div class="col-md-7">
						<div class="input-group">
				  		<div class="input-group-prepend">
				    		<span class="input-group-text">확인</span>
				  		</div>
				  		<input type="password" name="reUsrPwd" id="reUpdUsrPwd" class="form-control input-sm" maxlength="16" title="비밀번호확인">
				  	</div>
					</div>
				</div>
			</td>
		</tr>
		<tr>
			<th>사용자그룹</th>
			<td>
				<select class="form-control input-sm" name="grpNo" required data-rules="require" title="사용자그룹">
					<option value="">선택</option>
					<c:forEach items="${usrgrpList}" var="grpList">
					<option value="${grpList.grpNo}"><c:out value="${grpList.grpNm}" /></option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<th>연락처</th>
			<td><input type="text" name="usrTel" class="form-control input-sm" maxlength="15" title="연락처" value="{usrTel}"></td>
		</tr>
		<tr>
			<th>부서명</th>
			<td><input type="text" name="usrDept" class="form-control input-sm" maxlength="30" title="부서명" value="{usrDept}"></td>
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

