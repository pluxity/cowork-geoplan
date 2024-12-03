<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<sec:authentication var="principal" property="principal" />
	<!-- Begin Page Content -->
 	<div class="container-fluid">
		<!-- Page Heading -->
        <div class="d-sm-flex align-items-center justify-content-between mb-4">
        	<h1 class="h4 mb-0 text-gray-800">사용자 정보</h1>
		</div>

        <div class="row">
        	<div class="col-md-12">
	        	<table class="table table-bordered">
					<colgroup>
						<col style="width:10%">
						<col style="width:40%">
						<col style="width:10%">
						<col style="width:40%">
					</colgroup>
					<tbody>
					<tr>
						<th>아이디</th>
						<td>${principal.username}</td>
						<th>비밀번호</th>
						<td><button type="button" class="btn btn-primary" id="btnUsrPwdFrm">비밀번호 변경</button>
						</td>
					</tr>
					<tr>
						<th>사용자명</th>
						<td>${sessionScope.LOGIN_INFO.usrNm}</td>
						<th>그룹명</th>
						<td>${sessionScope.LOGIN_INFO.grpNm}</td>
					</tr>
					<tr>
						<th>부서명</th>
						<td>${sessionScope.LOGIN_INFO.usrDept}</td>
						<th>E-Mail</th>
						<td>${sessionScope.LOGIN_INFO.usrEmail}</td>
					</tr>
					<tr>
						<th>도면 권한</th>
						<td>
						<c:choose>
							<c:when test="${sessionScope.LOGIN_INFO.grpType eq 'ROLE_ADMIN'}">도면 전체</c:when>
							<c:otherwise>
								<c:forEach items="${sessionScope.LOGIN_INFO.usrgrpMapList}" var="mapList">
								${mapList.mapNm}<br>
								</c:forEach>
							</c:otherwise>
						</c:choose>
						</td>
						<th>POI 권한</th>
						<td>
						<c:choose>
							<c:when test="${sessionScope.LOGIN_INFO.grpType eq 'ROLE_ADMIN'}">POI 전체</c:when>
							<c:otherwise>
								<c:forEach items="${sessionScope.LOGIN_INFO.usrgrpPoiList}" var="poiList">
								${poiList.category1Nm}
								<c:if test="${poiList.roleType eq '1'}">(표출)</c:if>
								<c:if test="${poiList.roleType eq '2'}">(제어)</c:if>
								<br>
								</c:forEach>
							</c:otherwise>
						</c:choose>
						</td>
					</tr>
					</tbody>
				</table>
			</div>
        </div>
	</div>
    <!-- End of Main Content -->

    <!-- 사용자 등록폼 -->
  	<div class="modal fade" id="usrPwdModal" tabindex="-1" role="dialog" aria-labelledby="" aria-hidden="true">
	  <div class="modal-dialog" role="document" style="max-width:500px;">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title">비밀번호 변경</h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body">
	        <form name="usrPwdFrm" id="usrPwdFrm" method="post">
				<fieldset>
				<table class="table table-bordered">
					<colgroup>
						<col style="width:30%">
						<col style="width:70%">
					</colgroup>
					<tbody>
					<tr>
						<th>현재 비밀번호</th>
						<td><input type="password" name="beforeUsrPwd" id="beforeUsrPwd" class="form-control input-sm" maxlength="16" title="현재 비밀번호" required data-rules="require"></td>
					</tr>
					<tr>
						<th>변경 비밀번호</th>
						<td><input type="password" name="usrPwd" id="usrPwd" class="form-control input-sm" maxlength="16" title="변경 비밀번호" required data-rules="require"></td>
					</tr>
					<tr>
						<th>비밀번호 확인</th>
						<td><input type="password" name="reUsrPwd" id="reUsrPwd" class="form-control input-sm" maxlength="16" title="확인 비밀번호" required data-rules="require"></td>
					</tr>
					</tbody>
				</table>
				</fieldset>
			</form>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
	        <button type="button" class="btn btn-primary" id="btnModifyUsrPwd">수정</button>
	      </div>
	    </div>
	  </div>
	</div>
