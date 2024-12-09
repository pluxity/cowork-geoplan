$(function() {

	// 도면 컨트롤 버튼 정의
	$(".btnMapTool").on({
		mousedown : function() {
			var btnType = $(this).data("btn-type");
			if(btnType == "in") {
				Px.Camera.StartZoomIn();
			} else if(btnType == "out") {
				Px.Camera.StartZoomOut();
			} else if(btnType == "up") {
				Px.Camera.StartRotateUp();
			} else if(btnType == "down") {
				Px.Camera.StartRotateDown();
			} else if(btnType == "left") {
				Px.Camera.StartRotateLeft();
			} else if(btnType == "right") {
				Px.Camera.StartRotateRight();
			}
		},
		mouseup : function() {
			var btnType = $(this).data("btn-type");
			if(btnType == "in") {
				Px.Camera.StopZoomIn();
			} else if(btnType == "out") {
				Px.Camera.StopZoomOut();
			} else if(btnType == "up") {
				Px.Camera.StopRotateUp();
			} else if(btnType == "down") {
				Px.Camera.StopRotateDown();
			} else if(btnType == "left") {
				Px.Camera.StopRotateLeft();
			} else if(btnType == "right") {
				Px.Camera.StopRotateRight();
			}
		},
		touchstart : function() {
			var btnType = $(this).data("btn-type");
			if(btnType == "in") {
				Px.Camera.StartZoomIn();
			} else if(btnType == "out") {
				Px.Camera.StartZoomOut();
			} else if(btnType == "up") {
				Px.Camera.StartRotateUp();
			} else if(btnType == "down") {
				Px.Camera.StartRotateDown();
			} else if(btnType == "left") {
				Px.Camera.StartRotateLeft();
			} else if(btnType == "right") {
				Px.Camera.StartRotateRight();
			}
		},
		touchend : function() {
			var btnType = $(this).data("btn-type");
			if(btnType == "in") {
				Px.Camera.StopZoomIn();
			} else if(btnType == "out") {
				Px.Camera.StopZoomOut();
			} else if(btnType == "up") {
				Px.Camera.StopRotateUp();
			} else if(btnType == "down") {
				Px.Camera.StopRotateDown();
			} else if(btnType == "left") {
				Px.Camera.StopRotateLeft();
			} else if(btnType == "right") {
				Px.Camera.StopRotateRight();
			}
		},
		click : function() {
			var $this = $(this);
			var btnType = $this.data("btn-type");

			if(btnType == "center") {
				// 도면 초기 위치
				camPos.changeCamPos('3D');
				//Px.Camera.ExtendView();
			} else if (btnType === "view2d") {
				if(window.viewerControl) viewerControl.btn(this);
				$this.toggleClass("on");
				if($this.hasClass('on')) {
					camPos.changeCamPos('2D', Px.Camera.SetOrthographic);
				} else {
					camPos.changeCamPos('3D', Px.Camera.SetPerspective);
				}
			}  else if (btnType === "viewLook") {
				if(window.viewerControl) viewerControl.btn(this);
				$this.toggleClass("on");
				if($this.hasClass('on')) Px.Camera.FPS.On();
				else Px.Camera.FPS.Off();
			} else if (btnType === "transparent"){
				if(window.viewerControl) viewerControl.btn(this);
				$this.toggleClass("on");
				var opacity = 50;

				if($this.hasClass('on')) Px.Model.Transparent.SetAll(opacity);
				else Px.Model.Transparent.Restore();
			} else if (btnType === "minimapZoomIn") {
				controlMiniMap.zoomIn();
			} else if (btnType === "minimapZoomOut") {
				controlMiniMap.zoomOut();
			} else if(btnType == "lod") { // 1인칭 시점
				$this.toggleClass("on");
				if($this.hasClass('on')) {
					initPoiLod(mapNo);
				} else {
					Px.Lod.SetLodData();
				}
			}
		}
	});

});
var gFloorGroup = "";	//현재그룹층 전역변수. 관리자와 뷰어의 돔구조가 다르기때문에 사용
var webglCallbacks = {};	//await async promise 등으로 대체할지 아니면 전역 콜백을 사용할지 고려해 보아야한다.
var timeStamp=0;
/*
 * webglCallbacks.initialize : webgl초기화 이후 실행
 * webglCallbacks.poiLoad : poi를 엔진에 등록된 이후 실행
 * webglCallbacks.loadSbm : SBM 메모리에 적재된 이후 실행(렌더링 이후 아님)
 * webglCallbacks.onComplete : 모든게 완료되었을때 실행
 * */


/**
 * 도면 기본정보 호출
 * @param mapNo
 * @returns
 */
function loadMapInfo(mapNo, callback) {
	timeStamp = Date.now();
	$.ajax({
		method: "post",
		url: "/api/viewer/mapInfo.json",
		data: {"mapNo":mapNo, "mapVer":mapVer},
		dataType: 'json',
		async : false,
	    success: function(res) {
	    	if(jResult(res)) {
				// 도면 정보
		    	mapInfo = res.mapInfo;
		    	// 층 정보
		    	floorInfoList = res.floorInfoList;
				floorGrouping();

				if(callback) callback(res);
			}
	    }
	});
}

/**
 * 맵초기화 - 전체층 로드
 * @returns
 */
function initMap() {
	// WebGL 엔진 초기화
	$("#loadingLayer").show();
	var mapVer = mapInfo.mapVer;
	var container = document.getElementById('webGLContainer');

	Px.Core.Initialize(container, function(){

		Px.Util.SetBackgroundColor('#1b1c2f');	//백그라운드 색깔지정
		// 현제 페이지에 해당하는 Sbm전체 로드 처리
	    var sbmDataArray = [];
	    for(var i = 0; i < floorInfoList.length; i++) {
	    	var fileName = floorInfoList[i].floorFileNm;

	    	var url = `${_CONTEXT_PATH_}map/${mapNo}/${mapVer}/${fileName}`;
	    	//var id = floorInfoList[i].floorId;	//기존 sbm floorId
	    	var id = floorInfoList[i].floorNo;		//층 펼치기 위해서 수정
			var displayName = floorInfoList[i].floorNm;

			var baseFloor = floorInfoList[i].floorBase;
			var groupId = floorInfoList[i].floorGroup;

	    	sbmDataArray.push({
	    		url: url,
	    		id: id,
	    		displayName: displayName,
				baseFloor: baseFloor,
				groupId: groupId
	    	});
	    }

	    // Sbm 데이터 배열로 로드
		if(mapInfo.fileType === 'sbm') {
			Px.Loader.LoadSbmUrlArray({
				urlDataList: sbmDataArray,
				center: mapInfo.centerPosJson? JSON.parse(mapInfo.centerPosJson):"",
				onLoad: function() {
					//console.log('sbmload 완료')
					// poi 리스트 맵추가
					addPoiListToMap();
					// 로딩레이어 종료
					// $("#loadingLayer").hide();
					if(typeof(webglCallbacks.loadSbm) === 'function')  webglCallbacks.loadSbm();
				}
			});
		} else {
			const {
				mapNo,
				mapVer,
			} = mapInfo;

			const modelArray = floorInfoList.map((floorInfo) => {
				const {
					floorNo, floorFileNm, floorGroup, floorId, floorBase,
				} = floorInfo;
				return {
					type: 'fbx',
					order: floorBase,
					name: floorId,
					url: `${_CONTEXT_PATH_}map/${mapNo}/${mapVer}/${floorFileNm}`,
				};
			});

			const onLoad = ()=> {
				addPoiListToMap();
				// $("#loadingLayer").hide();
				if(typeof(webglCallbacks.loadSbm) === 'function')  webglCallbacks.loadSbm();
			}

			Px.Loader.LoadModelArray(modelArray, () => onLoad());

		}
	    //전역에 등록된 콜백 실행
	    if(typeof(webglCallbacks.initialize) === 'function')  webglCallbacks.initialize();

		camPos.changeCamPos('all');

		Px.Topology.Data.SetSize(0.2);

	}, function() {
		console.error('WebGL 초기화 실패');
	});
}


/**
 * 층선택 처리
 * @param floorNo
 * @returns
 */
function changeFloor(floorNo) {
	if(floorNo === undefined || floorNo == "") {
		// 전체층 선택
		Px.Model.Visible.ShowAll(); // 모든 모델 보이기
	} else {
		// Sbm전체를 숨긴후 FloorID에 해당하는 Sbm층만 가시화 처리
		var floorNo = floorInfoList.filter((floorInfo) => floorInfo.floorId === floorNo)[0].floorId;

		Px.Model.Visible.HideAll();
		Px.Model.Visible.Show(floorNo);
		Px.Camera.ExtendView();
	}

	addPoiListToMap({"floorNo":floorNo});
}

/**
 * 층선택 처리
 * @param groupNo ( 숫자형태만 사용할것 ex) 1 "1" ) ExtendView = false 면 카메라 줌아웃 하지않음
 * @returns
 */
function changeFloorGroup(groupNo, extendViewFlag, callback) {

	gFloorGroup = groupNo.toString();
	if(isValEmpty(groupNo)) {
		// 전체층 선택
		Px.Model.Visible.ShowAll();
	} else {
		// Sbm전체를 숨긴후 groupNo에 해당하는 Sbm층만 가시화 처리
		groupNo = parseInt(groupNo);
		var floorNoList = floorGroups[groupNo].floorNoList;
		if(floorNoList === undefined) return;
		Px.Model.Visible.HideAll();
		Px.Model.Visible.ShowByArray(floorNoList);

		if(extendViewFlag != false) Px.Camera.ExtendView();	//extendViewFlag = false 면 카메라 줌아웃 하지않음

	}
	return addPoiListToMap({"floorGroup":groupNo}, callback);
}

/**
 * sbm 모두 삭제
 * @returns
 */
function deleteAllSbm() {
    Px.Model.DeleteAll(function(){
        //console.log('모두제거');
    });
}

/**
 * sbm 개별 삭제
 * @param floorId
 * @returns
 */
function deleteSbm(floorId) {
	if(Array.isArray(floorId)) { // 배열
		 Px.Model.DeleteByArray(floorId, function(result) {
			 //console.log(result, '제거');
		 });
	} else { // 단일
		Px.Model.Delete(floorId, function (result) {
	        //console.log(result, '제거');
	    });
	}
}


/**
 * POI 추가
 * @param poiInfo
 * @returns
 */
function addPoi(poiInfo) {

	Px.Poi.Add({
		 type: poiInfo.type,
		 url: poiInfo.url,
		 id: poiInfo.id,
		 group: poiInfo.group,	//대분류
		 position: poiInfo.position,
		 rotation: poiInfo.rotation,
		 scale: poiInfo.scale,
		 iconUrl: poiInfo.iconUrl,
		 lineHeight: poiInfo.lineHeight,
		 displayText: poiInfo.displayText,
		 property: poiInfo.property,
		 onComplete: function () {
		     //console.log('poi 추가');
			 // POI 가시화 처리
			 var chkCategoryNo1 = getChkPoiCategory1NoArr(true);
			 if(chkCategoryNo1.length > 0) {
				 if($.inArray(poiInfo.group, chkCategoryNo1) == -1) {
					 Px.Poi.Hide(poiInfo.id);
				 }
			 }
		 }
	});
}



/**
 * 전역변수로 등록된  floorInfoList 를 {groupNo : floorId} 형태의 Map 으로 저장
 * @returns
 */
function floorGrouping() {
	floorGroups = {};

	for(var i=0, size=floorInfoList.length; i<size; ++i){
		var floorGroup = floorInfoList[i].floorGroup;
		var floorNo = floorInfoList[i].floorNo;
		var isMain = floorInfoList[i].isMain;
		var floorNm = floorInfoList[i].floorNm;

		if(floorGroups.hasOwnProperty(floorGroup)) {	//키값이 있을때
			floorGroups[floorGroup].floorNoList.push(floorNo);
		} else 	{
			floorGroups[floorGroup] = {};
			floorGroups[floorGroup].floorNoList = [floorNo];
			floorGroups[floorGroup].floorGroup = floorGroup;
		}
		if(isMain === "True") {
			floorGroups[floorGroup].name = floorNm;
		}
	}

	if(typeof(globalConfig) === "object" && globalConfig.modelExpand) {	//modelExpand 프로퍼티가 존재하면 interval 값 계산
		var config = globalConfig.modelExpand;

		var	 floorLen = Object.keys(floorGroups).length;
		var interval = config.totalInterval / (floorLen - 1);
		interval = interval > config.maxInterval ? config.maxInterval : interval;
		config.interval = interval;
	}
}

/**
 * POI 대분류 가시화 체크 목록
 * param flag = false 시에는 체크하지 않은 리스트 반환 undefined null 등이면 true 처리
 * @returns
 */
function getChkPoiCategory1NoArr(flag) {
	var poiCategory1NoChecked = [];		//체크된 카테고리
	var poiCategory1NoNotChecked = [];	//체크안된 카테고리

	$(".poiCategory1No").each(function() {
		var category1No = null;

		if($(this).is(":checkbox")) {
			category1No = $(this).val();
			if($(this).is(":checked") == true) {
				poiCategory1NoChecked.push(parseInt(category1No));
			} else poiCategory1NoNotChecked.push(parseInt(category1No));
		} else {
			category1No = $(this).data("category1-no");
			if($(this).hasClass("on")) {
				poiCategory1NoChecked.push(parseInt(category1No));
			} else poiCategory1NoNotChecked.push(parseInt(category1No));
		}
	});

	if(isValEmpty(flag)) flag = true;

	return flag ? poiCategory1NoChecked : poiCategory1NoNotChecked;
}

/**
 * POI번호로 위치이동
 * @param poiNo
 * @returns
 */
function moveToPoi(poiNo, callback) {
	Px.Camera.MoveToPoi(poiNo, true, 500, function() {
		if(typeof(callback) === 'function')  callback();
	});
}

/**
 * 좌표로 위치이동
 * @param x
 * @param y
 * @param z
 * @returns
 */
function moveToPos(x, y, z) {

}


/**
 * 팝업 레이어 위치 지정
 * @param target
 * @param x
 * @param y
 * @returns
 */
function setLayerPos(target, x, y){
	var sWidth = window.innerWidth;
	var sHeight = window.innerHeight;
	var oWidth = target.width();
	var oHeight = target.height();
	var divLeft = x;
	var divTop = y;
	if( divLeft + oWidth > sWidth ) divLeft -= oWidth;
	if( divTop + oHeight > sHeight ) divTop -= oHeight;
	if( divLeft < 0 ) divLeft = 0;
	if( divTop < 0 ) divTop = 0;

	target.css({"top": divTop, "left": divLeft, "position": "absolute"});
	return target;
}

/**
 * poi lod 설정
 * @param mapNo
 * @returns
 */
function initPoiLod(mapNo) {
	$.post("/api/viewer/poiLodInfo.json", {"mapNo":mapNo}, function(res) {
		if(jResult(res)) {
			var result = res.result;
			if(result != null) {
				var lodData = {
						usedLodCount: result.levelCnt,
			            maxDistance: result.maxDist,
			            data: result.poiLodInfoList
				};
				//console.log(lodData);
		        Px.Lod.SetLodData(lodData);
			}
		}
	});
}

function getTopologyList(mapNo) {
	$.get("/adm/topology/getTopologyList.json", {"mapNo":mapNo}, function(res) {
		if(jResult(res)) {
			console.log('topology',res);
		}
	});
}


var controlMiniMap = function () {
	var _zoomLevel = 1;

	function zoomIn () {
		if(_zoomLevel > 11) return;
		Px.Minimap.SetZoomLevel(++_zoomLevel);
	}

	function zoomOut () {
		if(_zoomLevel < 2) return;
		Px.Minimap.SetZoomLevel(--_zoomLevel);
	}

	return {
		zoomIn : zoomIn,
		zoomOut : zoomOut
	}
}();

var modelExpand = function (callback) {
	Px.Model.Visible.ShowAll();
	var option = {
			name: floorInfoList[0].floorId,
			groupId: floorInfoList[0].floorId,
			interval : 10,
			duration : 50,
			onComplete : function() {
				// globalConfig.flag.changeFloor = false;
				//console.log("펼치기");
				if(typeof(callback) === 'function') callback();
				// if(typeof(webglCallbacks.onComplete) === 'function') webglCallbacks.onComplete();
			}
	}
	// Px.Model.Expand(option);
}

var modelCollapse = function (callback) {
	Px.Model.Visible.ShowAll();

	var option = {
			duration : 0,
			onComplete : function() {
				//console.log("합치기");
				if(typeof(callback) === 'function') callback();
			}
	}
	Px.Model.Collapse(option);
}


