<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
		<div id="content-wrapper" style="padding:10px 0px 10px 2px;position:relative;">
<%--			<div id="loadingLayer"><img src="/resources/img/ajax-loader.gif"> 도면 데이터를 로딩중입니다.</div>--%>
			<div class="container-fluid" style="height:100%;">
			    <div id="webGLContainer" style="width:100%;height:99%;">
				</div>
			</div>
			<div id="mapToolbarLeft">
				<div id="camPosTool" class="rounded">
			    	<button type="button" class="btn btn-dark btn-sm camPosTool" title="화각저장" data-btn-type="saveCampos">화각저장</button>
				</div>
			</div>
			<div id="mapToolbar">
				<div id="mapCtrlTool" class="rounded edit-tools">
					<button type="button" class="btn btn-dark btn-sm btnMapTool" title="지도 확대" data-btn-type="in"><i class="fas fa-search-plus"></i></button>
			    	<button type="button" class="btn btn-dark btn-sm btnMapTool" title="지도 축소" data-btn-type="out"><i class="fas fa-search-minus"></i></button>
			    	<button type="button" class="btn btn-dark btn-sm btnMapTool" title="지도(상)" data-btn-type="up"><i class="fas fa-arrow-up"></i></button>
			    	<button type="button" class="btn btn-dark btn-sm btnMapTool" title="지도 (하)" data-btn-type="down"><i class="fas fa-arrow-down"></i></button>
			    	<button type="button" class="btn btn-dark btn-sm btnMapTool" title="지도(좌)" data-btn-type="left"><i class="fas fa-undo"></i></button>
			    	<button type="button" class="btn btn-dark btn-sm btnMapTool" title="지도(우)" data-btn-type="right"><i class="fas fa-redo"></i></button>
			    	<button type="button" class="btn btn-dark btn-sm btnMapTool" title="지도(중앙)" data-btn-type="center"><i class="fas fa-expand"></i></button>
			    	<button type="button" class="btn btn-dark btn-sm btnMapTool" title="1인칭시점" data-btn-type="viewLook"><i class="fas fa-street-view"></i></button>
			    	<button type="button" class="btn btn-dark btn-sm btnMapTool" title="LOD설정" data-btn-type="lod">LOD</button>
				</div>
				<div id="evacRouteTool" class="rounded">
					<button type="button" class="btn btn-dark btn-sm btnMapTool" title="대피경로" data-btn-type="evacRouteToggle"">대피경로</button>
					<button type="button" class="btn btn-dark btn-sm evacRouteTool toggle" title="선 그리기" data-btn-type="drawEditor"">선 그리기</button>
<%--					<button type="button" class="btn btn-dark btn-sm evacRouteTool" title="화재 그리기" data-btn-type="createFire">화재 그리기</button>--%>
<%--					<button type="button" class="btn btn-dark btn-sm evacRouteTool" title="화재 제거" data-btn-type="removeFire"">화재 제거</button>--%>
					<button type="button" class="btn btn-dark btn-sm evacRouteTool toggle" title="위치점 제거" data-btn-type="removePoint"">점 제거</button>
					<button type="button" class="btn btn-dark btn-sm evacRouteTool toggle" title="연결 제거" data-btn-type="removeLink"">선 제거</button>
					<button type="button" class="btn btn-dark btn-sm evacRouteTool" title="모두 제거" data-btn-type="clearRoute"">모두 제거</button>
					<button type="button" class="btn btn-dark btn-sm evacRouteTool" title="저장" data-btn-type="saveRoute"">저장</button>
				</div>
			    <div id="poiEditTool" class="rounded edit-tools">
					<button type="button" class="btn btn-dark btn-sm btnMapTool" title="POI편집" data-btn-type="edit">POI편집</button>
					<button type="button" class="btn btn-dark btn-sm btnMapTool" title="POI이동" data-btn-type="translate"><i class="fas fa-crosshairs"></i></button>
					<button type="button" class="btn btn-dark btn-sm btnMapTool" title="POI회전" data-btn-type="rotate"><i class="fas fa-sync"></i></button>
					<button type="button" class="btn btn-dark btn-sm btnMapTool" title="POI크기" data-btn-type="scale"><i class="fas fa-expand-arrows-alt"></i></button>
				</div>
				<div id="topoEditTool" class="rounded edit-tools d-none">
					<button type="button" class="btn btn-radio btn-dark btn-sm btnMapTool topo-node" title="노드생성" data-act-type="addNode">노드생성</button>
<%--					<button type="button" class="btn btn-radio btn-dark btn-sm btnMapTool topo-node" title="노드이동" data-act-type="moveNode">노드이동</button>--%>
					<button type="button" class="btn btn-radio btn-dark btn-sm btnMapTool" title="링크생성" data-act-type="onewayLink">단방향링크</button>
					<button type="button" class="btn btn-radio btn-dark btn-sm btnMapTool" title="링크생성" data-act-type="twowayLink">양방향링크</button>
					<button type="button" class="btn btn-radio btn-dark btn-sm btnMapTool" title="삭제" data-act-type="delNode">노드, 링크삭제</button>
				</div>
				<!-- <div class="rounded active">
					<button type="button" class="btn btn-dark btn-sm btnMapTool" title="편집모드 변경" data-btn-type="changeMode">편집모드 변경</button>
				</div> -->
		    </div>
			<button type="button" id="btnPoiCategorySel" class="btn btn-light" style="position:absolute;left:30px;bottom:30px;z-index:9999;"><i class="fas fa-caret-up"></i> POI 대분류</button>
			<div id="poiCategorySel" class="list-group div-select-left-bottom div-topology-toggle-off"">
				<c:forEach items="${poiCategory1List}" var="category1List" varStatus="status">
				<span class="list-group-item list-group-item-action">
					<label><input type="checkbox" name="category1No" class="styled poiCategory1No" checked value="${category1List.category1No}"> <c:out value="${category1List.category1Nm}" /></label>
				</span>
				</c:forEach>
			</div>
			<div id="floorSelect">
		    	<select name="floorNo" id="floorNo" class="form-control input-sm">
		    	</select>
		    </div>
			<div id="topoFloorSelect" class="d-none">
				<select name="floorNo1" id="floorNo1" class="form-control input-sm">
		    	</select>
				<select name="floorNo2" id="floorNo2" class="form-control input-sm">
		    	</select>
			</div>
		</div>
		<div id="poiMenuPop" class="poiPopLayer dropdown-menu hide"></div>