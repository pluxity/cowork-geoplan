<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<c:set var="mapNo" value="${mapInfoVO.mapNo}" />
	<!-- Begin Page Content -->
 	<div class="container-fluid">

		<!-- Page Heading -->
        <div class="d-sm-flex align-items-center justify-content-between mb-4">
        	<h1 class="h4 mb-0 text-gray-800">도면 상세</h1>
		</div>

        <div class="row">
        	<div class="col-md-12">
        		<form:form name="mapFrm" id="mapFrm" modelAttribute="mapInfoVO" enctype="multipart/form-data">
        		<form:hidden path="mapNo" />
				<table class="table table-bordered">
					<colgroup>
						<col />
						<col style="width:12%" />
						<col style="width:30%" />
						<col style="width:12%" />
						<col style="width:30%" />
					</colgroup>
					<tbody>
					<tr>
						<td rowspan="7" style="text-align:center;vertical-align:top;">
							<p><img id="mapImgPrev" src="/map/${mapInfoVO.mapNo}/${mapInfoVO.imgFileNm}" class="rounded" alt="대표이미지" style="width:256px;" onerror="fn_noPhoto(this)"></p>
							<input type="file" name="mapImg" id="mapImg" class="form-control" data-rules="file" data-ext="jpg,jpeg,png,gif,bmp" accept=".jpg,.jpeg,.png,.gif,.bmp">
						</td>
						<th>도면명</th>
						<td ><form:input path="mapNm" cssClass="form-control input-sm" maxlength="30" title="도면명" requried="true" data-rules="require" /></td>
						<th>도면코드</th>
						<td><form:input path="mapCd" id="mapCd" cssClass="form-control input-sm" maxlength="30" title="도면코드" requried="true" data-rules="require" /></td>
					</tr>
					<tr>
						<th>카테고리 분류</th>
						<td colspan="3">
							<div class="row">
								<div class="col-md-4">
									<form:select path="category1No" items="${mapCategory1List}" itemValue="category1No" itemLabel="category1Nm" cssClass="form-control input-sm" title="대분류" required="true" data-rules="require" />
								</div>
								<div class="col-md-4">
									<form:select path="category2No" items="${mapCategory2List}" itemValue="category2No" itemLabel="category2Nm" cssClass="form-control input-sm" title="중분류" required="true" data-rules="require" />
								</div>
								<div class="col-md-4">
									<form:select path="category3No" items="${mapCategory3List}" itemValue="category3No" itemLabel="category3Nm" cssClass="form-control input-sm" title="소분류" required="true" data-rules="require" />
								</div>
							</div>
						</td>
					</tr>
					<tr>
						<th>도면 설명</th>
						<td colspan="3"><form:textarea path="mapDesc" cssClass="form-control input-sm" title="도면설명" /></td>
					</tr>
					<tr>
						<th>층 정보</th>
						<td colspan="3">
						<c:forEach items="${floorInfoList}" var="flist">
							${flist.floorNm} / ${fn:replace(flist.floorFileNm, '.\\', '')}
							<c:if test="${mapInfoVO.defaultFloor eq flist.floorNo}">(기본층)</c:if>
							<br>
						</c:forEach>
						</td>
					</tr>
					<tr>
						<th>도면 버전</th>
						<td><form:select path="mapVer" items="${mapHstList}" itemValue="mapVer" itemLabel="mapVer" cssClass="form-control input-sm" title="도면버전" required="true" data-rules="require" /></td>
						<th>보안성 검토</th>
						<td>
							<div class="form-check form-check-inline">
							  <form:radiobutton cssClass="form-check-input" path="mapStts" label="미확인" value="0"/>
							</div>
							<div class="form-check form-check-inline">
							  <form:radiobutton cssClass="form-check-input" path="mapStts" label="확인" value="1"/>
							</div>
						</td>
					</tr>
					<tr>
						<th>2D좌표</th>
						<td colspan="3">
							<div class="d-flex" style="margin-top: 0.5rem; gap: 0.5rem;">
								<label style="flex:2;">위도<input type="text" name="lat" class="form-control input" value="${mapInfoVO.lat}"></label>
								<label style="flex:2;">경도<input type="text" name="lng" class="form-control input" value="${mapInfoVO.lng}"></label>
								<button type="button" id="openMap" class="btn btn-outline-secondary" data-map-no="${mapInfoVO.mapNo}">건물 마커 좌표등록</button>
							</div>
						</td>
					</tr>
					<tr>
						<th>등록자</th>
						<td>${mapInfoVO.regUsr}</td>
						<th>등록일</th>
						<td>${mapInfoVO.regDt}</td>
					</tr>
					</tbody>
				</table>
				</form:form>
			</div>
		</div>
		<div class="text-right">
			<button type="button" id="btnMapInfoSave" class="btn btn-primary">저장</button>
			<a href="/adm/map/mapList.do" class="btn btn-secondary">목록</a>
			<button type="button" id="btnMapInfoDel" class="btn btn-danger">삭제</button>
			<a href="/adm/map/viewer.do?mapNo=${mapNo}" class="btn btn-success" target="_blank">공간 관리</a>
		</div>

		<div class="row" style="padding-top:20px;">
        	<div class="col-md-12">
        		<form name="mapHstFrm" id="mapHstFrm" method="post" enctype="multipart/form-data">
        		<input type="hidden" name="mapNo" value="${mapNo}">
				<table class="table table-bordered">
					<colgroup>
						<col style="width:10%" />
						<col style="width:40%" />
						<col style="width:10%" />
						<col style="width:40%" />
					</colgroup>
					<tbody>
					<tr>
						<th>도면 수정 내용</th>
						<td><input type="text" name="hstDesc" id="hstDesc" class="form-control input-sm" title="수정 내용" data-rules="require"></td>
						<th>도면 파일</th>
						<td>
							<div class="input-group">
							  <input type="file" name="mapZip" id="mapZip" class="form-control" title="도면파일" data-rules="require,file" data-ext="zip" accept=".zip">
							  <div class="input-group-append">
							    <button class="btn btn-secondary" type="submit">업로드</button>
							  </div>
							</div>
						</td>
					</tr>
					</tbody>
				</table>
				</form>
			</div>
		</div>
		<div class="row">
        	<div class="col-md-12">
				<table class="table table-striped">
				  <thead class="thead-dark">
				    <tr>
				      <th scope="col" style="width:15%;">도면 버전</th>
				      <th scope="col" style="width:25%;">도면 파일명</th>
				      <th scope="col" style="width:25%;">수정 내용</th>
				      <th scope="col" style="width:10%;">등록자</th>
				      <th scope="col" style="width:10%;">등록일</th>
				      <th scope="col" style="width:15%;">관리</th>
				    </tr>
				  </thead>
				  <tbody id="mapHstList">
				  </tbody>
				</table>
			</div>
		</div>
		<!-- /.container-fluid -->
	</div>
    <!-- End of Main Content -->

    <script type="text/template" id="mapHstListTpl">
    <tr>
    	<td class="text-center">{mapVer}</td>
      	<td>{mapFileNm}</td>
      	<td>{hstDesc}</td>
      	<td class="text-center">{regUsr}</td>
      	<td class="text-center">{regDt}</td>
      	<td class="text-center">
      		<button class="btn btn-outline-secondary btn-sm" type="button" title="다운로드" onclick="downloadMapFile({mapFileNo});"> <i class="fas fa-download"></i></button>
      		<button class="btn btn-outline-secondary btn-sm" type="button" title="삭제" onclick="deleteMapHst({mapNo}, {mapVer}, {mapFileNo});"> <i class="fas fa-trash-alt"></i></button>
      		<button class="btn btn-outline-secondary btn-sm" type="button" title="지도보기" onclick="popAdmView({mapNo}, {mapVer});"> <i class="fas fa-map"></i></button>
      	</td>
    </tr>
	</script>


