$(function() {

	// POI 편집 버튼 정의
	$(".btnMapTool").on({
		click : function() {
			var btnType = $(this).data("btn-type");
			if(btnType == "edit") { // poi 편집모드 진입
				if($("#floorNo").val() == "") {
					alert("전체 도면에서는 POI배치가 불가능합니다.");
					return false;
				}

				$(this).toggleClass("on");
				if($(this).hasClass("on")) {
					Px.Edit.On();
					// poi 편집 콜백 처리
					Px.Edit.SetMouseUpCallback(poiEditCallback);
					// poi 이벤트 해제
					Px.Event.RemoveEventListener('pointerup', 'poi', showPoiMenu);
				} else {
					Px.Edit.Off();
					$("#poiEditTool > button").removeClass('on');
				}

			} else if(btnType == "translate" || btnType == "rotate" || btnType == "scale") { // poi 편집
				if(!$(this).hasClass("on") && $("#poiEditTool > button[data-btn-type='edit']").hasClass("on")) {
					Px.Edit.SetMode(btnType);

					$("#poiEditTool > button[data-btn-type!='edit']").removeClass('on');
					$(this).addClass("on");
				}

			} else if(btnType == "evacRouteToggle") { // 대피경로 토글

				if(!$(this).hasClass("on")) {
					activePoiCategory1No = [...document.querySelectorAll('#poiCategorySel input:checked')].map((input)=>input.value);
					$('#poiCategorySel input').prop("checked", false);

					//일단 강제 전체층 로드 poi는 굳이 로드 하지 않는다.
					Px.Model.Visible.ShowAll();

					Px.Poi.HideAll();
					evacRoute.loadRoute();

					$('.evacRouteTool').show();
					$('#floorSelect').hide();
					sidbarDimm(true);
					$(this).toggleClass("on");
				} else if(confirm('대피경로 편집모드를 끄시겠습니까?')) {

					evacRoute.clear();
					modelCollapse();

					$('.evacRouteTool').hide();
					$('#floorSelect').show();
					sidbarDimm(false);

					evacRoute.btnFunction.drawEditor(false);
					evacRoute.btnFunction.removePoint(false);
					evacRoute.btnFunction.removeLink(false);
					$(this).toggleClass("on");

					if(activePoiCategory1No.length > 0) {
						$('#poiCategorySel input').prop("checked", false);
						activePoiCategory1No.forEach((category1No) => {
							document.querySelector(`.poiCategory1No[value="${category1No}"]`).checked = true;
							Px.Poi.ShowByGroup(parseInt(category1No, 10));
						})
					}
				}
			} else if (btnType === 'changeMode') {
				const editTools = document.querySelectorAll('.edit-tools')
				editTools.forEach(elem => elem.classList.toggle('d-none') );

				getTopologyList(parseInt(mapNo, 10));
			}

			// poi메뉴 숨김
			hidePoiMenu();
		}
	});

	$(".evacRouteTool").on({
		click : function() {
			var btnType = this.dataset.btnType;

			var $this = $(this);
			var flag = !$this.hasClass("on");
			var toggleFlag = $this.hasClass('toggle');

			if(flag) {
				//모드해제
				evacRoute.btnFunction.drawEditor(false);
				evacRoute.btnFunction.removePoint(false);
				evacRoute.btnFunction.removeLink(false);

				$('.evacRouteTool').removeClass('on');

				if(toggleFlag)
					$this.addClass('on');

			} else {
				$this.removeClass('on');
			}
			evacRoute.btnFunction[btnType](flag);	//정의되어있는 함수 실행

		}
	});

	$("#topoEditTool > BUTTON").on({
		click : function() {
			if($(this).hasClass('disabled')) {
				alert(`${$(this).text()}은 단층에서만 가능합니다.`);
				return false;
			}

			if(document.querySelector('#floorNo1').value === '') {
				alert("전체 도면에서는 경로생성이 불가능합니다.");
				return false;
			}
			const jThis = $(this);
			jThis.siblings().removeClass('on');
			if(jThis.hasClass('on')) {
				jThis.removeClass('on');
				Px.Topology.Data.Cancel();

				return;
			}

			jThis.addClass('on');

			const actType = jThis.data('actType');
			setNodeAct(actType);


		}
	})



	$("#btnTopoSave").click(function() {
		const strTopology = Px.Topology.Data.Export(true);

		const param = {
			'mapNo' : parseInt(mapNo, 10),
			'topoJson' : strTopology,
			'topoType' : '일반인'
		}

		$.ajax({
			contentType:'application/json',
			dataType : 'json',
			data : param,
			url : `${_CONTEXT_PATH_}adm/topology/updateTopologyInfo.json`,
			type : 'PATCH',
			success:function(data){
			}
		});
	});


	$(".camPosTool").on({
		click : function() {
			const floorValue = document.querySelector('#floorNo').value;
			const floorNo = floorValue === '' ? 'all' : floorValue;
			camPos.setCurrentCamPos(floorNo === '' ? 'all' : floorNo);
			camPos.save(mapNo, floorNo);
		}
	});

	// 초기맵정보 로드
	loadMapInfo(mapNo, function(res){
		const { camPosList } = res;
		if(!camPosList) return;
		camPos.setData(camPosList);
	});

	// 맵초기화 전체로드
	initMap();

	// POI 클릭 시 이벤트
	Px.Event.On();
	Px.Event.AddEventListener('dblclick', 'poi', showPoiMenu);

	// 도면 휠 이벤트
	$("canvas").on('mousedown wheel resize ', function(e) {
		hidePoiMenu();
	});

	// poi메뉴 선택시 닫기
	$(document).on("click", "#poiMenuPop a", function() {
		hidePoiMenu();
	});

	// 층 셀렉트옵션 생성
	var floorListOpt = "";
	var floorListOpt1 = "<option value=''>층선택</option>";
	var floorListOpt2 = "<option value=''>전체</option>";
	$.each(floorInfoList, function(index, item) {
		floorListOpt += "<option value='"+item.floorId+"'>"+item.floorNm+"</option>";
	});
	$("select[name='floorNo']").html(floorListOpt1 + floorListOpt);
	$("#floorNo").html(floorListOpt2 + floorListOpt);
	$('#floorNo1').html(floorListOpt);
	$('#floorNo2').html(floorListOpt1);

	// 기본층 초기화
	var defaultFloor = mapInfo.defaultFloor; // 기본층
	//$("#floorNo").val(defaultFloor);
	//changeFloor(defaultFloor);

	// 층변경
	$(document).on("change", "#floorNo", function() {
		poiEditOff();
		changeFloor($(this).val());
		getPoiInfoList(1);

		const floorNo = $(this).val() === '' ? 'all' : $(this).val() ;
		camPos.getFloorCamPos(mapNo, floorNo, (floorNo) => camPos.changeCamPos(floorNo));
	});
	
	$('#floorNo1').change(function () {
		Px.Topology.Data.Cancel();
		Px.Topology.Data.HideAll();

		const actType = $('#topoEditTool > BUTTON.on').data('actType');
		if(typeof actType !== 'undefined' && actType !== null) {
			setNodeAct(actType);
		}

		const floorNo = $(this).val();
		Px.Model.Visible.HideAll();
		Px.Model.Visible.Show(floorNo);
		

		// camPos.getFloorCamPos(mapNo, floorNo, (floorNo) => camPos.changeCamPos(floorNo));

		Px.Topology.Data.Show(floorNo);
		Px.Topology.Data.SetCurrentFloor(floorNo);

		let floor2ListOpt = '';
		const currentFloorIndex = floorInfoList.findIndex((floor) => floor.floorId === floorNo);

		let closestFloorList = [ floorInfoList[currentFloorIndex - 1], floorInfoList[currentFloorIndex + 1] ];
		const floorMIdx = closestFloorList.findIndex(floor => floor && floor.floorNm === 'M')
		switch (floorMIdx) {
			case 0 :
				closestFloorList.unshift(floorInfoList[currentFloorIndex - 2]);
				break;
			case 1 :
				closestFloorList.push(floorInfoList[currentFloorIndex + 2]);
				break;
			default :
				break;
		}

		closestFloorList.forEach((floorInfo) => {
			if(floorInfo) {
				const { floorId, floorNm } = floorInfo
				floor2ListOpt += `<option value=${floorId}>${floorNm}</option>`;
			}
		})

		// for (let i = currentFloorIndex - 1; i <= currentFloorIndex + 1; i += 2) {
		// 	if (floorInfoList[i]) {
		// 		const { floorId, floorNm } = floorInfoList[i];
		// 		floor2ListOpt += `<option value=${floorId}>${floorNm}</option>`;
		// 	}
		// }

		$('#floorNo2').html(floorListOpt1 + floor2ListOpt);
		// nodeButtonActive();
		$('.topo-node.disabled').removeClass('disabled');
		Px.Camera.ExtendView();
	});
	
	$('#floorNo2').change(function() {
		nodeButtonActive();
		const floor1 = $('#floorNo1').val()
		const floor2 = $(this).val();
		Px.Model.Visible.HideAll();

		// modelCollapse(() => {
			if($(this).val() === '') {
				Px.Model.Visible.HideAll();
				Px.Model.Visible.Show(floor1);
				Px.Topology.Data.HideAll();
				Px.Topology.Data.Show(floor1);
				return;
			}
		// });

		Px.Model.Visible.ShowByArray([floor1, floor2]);
		Px.Camera.ExtendView();
		Px.Topology.Data.HideAll();
		// Px.Topology.Data.Show([floor1, floor2]);
		Px.Topology.Data.Show(floor1);
		Px.Topology.Data.Show(floor2);

		// const baseFloor = floor1 < floor2 ? floor1 : floor2;
		// const expandOption = {
		// 	name: baseFloor,
		// 	groupId: baseFloor,
		// 	interval: 10,
		// 	duration: 500,
		// 	onComplete: () => {
		// 		Px.Camera.ExtendView();
		// 	}
		// };

		// Px.Model.Expand(expandOption);
	});

	// 상단 도면명 출력
	$(".viewer-title").text(mapInfo.mapCategory.category2Nm + " " + mapInfo.mapNm);

	// 좌측 poi 리스트
	getPoiInfoList(1);

	// poi 카테고리 대분류 목록 출력
	$("#btnPoiCategorySel").click(function() {
		$(this).find("i").toggleClass('fa-caret-up fa-caret-down');
		$("#poiCategorySel").toggle();
	});

	// poi 대분류 가시화 처리
	$("#poiCategorySel :checkbox[name='category1No']").click(function() {
		var category1No = parseInt($(this).val());
		if($(this).prop("checked") == true) {
			Px.Poi.ShowByGroup(category1No);
		} else {
			Px.Poi.HideByGroup(category1No);
		}
	});

	// POI 등록 모달 레이어 호출
	$("#btnPoiForm").click(function() {
		// poi 등록창 보이기
		$('#poiRegistModal').modal('show');
		$("#poiRegistFrm")[0].reset();
		$(".iconsetView").html("");

		var floorNo = $("#floorNo").val();
		$("select[name='floorNo']", "#poiRegistFrm").val(floorNo);

		// 미리보기 이미지
		fnSetPreviewEvent(document.getElementById("poiImgR"), document.getElementById("imgR"));
	});

	// POI 일괄등록 모달 레이어 호출
	$("#btnPoiBatForm").click(function() {
		$('#poiBatRegistModal').modal('show');
		$("#poiBatRegistFrm")[0].reset();

		var floorNo = $("#floorNo").val();
		$("select[name='floorNo']", "#poiBatRegistFrm").val(floorNo);
	});

	// 중분류 셀렉트 생성
	$(document).on("change", "select[name=category1No]", function() {
		var thisForm = $(this).closest("form");
		var categoryList = "<option value='' selected>중분류</option>";
		if($(this).val() != "") {
			$.post("/api/viewer/poiCategoryList.json", {"category1No":$(this).val()}, function(res) {
				if(jResult(res)) {
					$.each(res.result, function(idx, item) {
						categoryList += "<option value='"+ item.category2No +"'>"+ item.category2Nm +"</option>";
		    		});
					$("select[name=category2No]", thisForm).html(categoryList);
				}
			}, "json");
		} else {
			$("select[name=category2No]", thisForm).html(categoryList);
		}

		$(".iconsetView").html("");
	});

	// 중분류 선택시 아이콘셋 출력
	$("#poiRegistFrm, #poiModifyFrm", document).on("change", "select[name=category2No]", function() {
		var targetDom = $(this).closest("form").find(".iconsetView");
		if($(this).val() != "") {
			ajaxTemplate("/adm/poi/poiCategoryView.json", {"category2No":$(this).val()}, $("#iconsetTpl").html(), targetDom);
		} else {
			targetDom.html("");
		}
	});

	// POI 등록
	$("#btnPoiRegist").click(function() {
		$("#poiRegistFrm").submit();
	});

	$("#poiRegistFrm").ajaxForm({
		url: '/adm/poi/poiRegist.json',
    	type: 'post',
    	dataType : 'json',
    	beforeSubmit: function (data, frm, opt) {
    		// 유효성 체크
    		if(!jValidationFrm(frm)) return false;
		},
        success: function(res) {
        	if(jResult(res, true)) {
        		$('#poiRegistModal').modal('hide');

        		// POI리스트 재호출
        		getPoiInfoList(1);
        		$('#imgR').prop("src", "/resources/img/noPhoto.png");
        	}
        }
    });

	// POI 일괄 등록
	$("#btnPoiBatRegist").click(function() {
		$("#poiBatRegistFrm").submit();
	});

	$("#poiBatRegistFrm").ajaxForm({
		url: '/adm/poi/poiBatRegist.json',
    	type: 'post',
    	dataType : 'json',
    	beforeSubmit: function (data, frm, opt) {
    		// 유효성 체크
    		if(!jValidationFrm(frm)) return false;
		},
        success: function(res) {
        	if(jResult(res, true)) {
        		$('#poiBatRegistModal').modal('hide');

        		// POI리스트 재호출
        		getPoiInfoList(1);
        	}
        }
    });

	// POI 검색
	$("#schFrm").submit(function() {
		getPoiInfoList(1);
		return false;
	});

	// poi 카테고리 선택
	$("#schFrm", document).on("change", "select[name=category1No], select[name=category2No]", function() {
		$("#schFrm").submit();
	});

	$(".tab-pane .nav-link").click(async function(){
		const jThis = $(this);
		jThis.parent().siblings().find('A').removeClass('active');
		jThis.addClass('active');
		if(jThis.hasClass('topo-type')) {
			// Px.Model.Visible.ShowAll();
			modelCollapse(async () => {
				await selectTopoType(jThis.text());
				document.querySelector('#floorNo1').dispatchEvent(new Event('change'));
			});
		}
		
	})

	$(".poi-position").click(function() {
		var positionYn = $(this).data("poi-position");
		var tabType = $(this).data("tab-type");

		$("#schFrm")[0].reset();
		$("input[name='positionYn']", "#schFrm").val(positionYn);
		$("#poiListTabType").val(tabType);

		$("#schFrm").submit();

		// $(".poi-position").removeClass("active");
		// $(this).addClass("active");
	});

	// poi 미배치로 변경 - 단일
	/*
	$("#btnPosPoi").click(function() {
		if(confirm("선택된 POI를 미배치로 변경하시겠습니까?")) {
			var poiNo = $("input[name='poiNo']", "#poiModifyFrm").val();
			$.post("/adm/poi/poiModify.json", {"poiNo":poiNo, "positionYn":"N"}, function(res) {
				if(jResult(res)) {
					$("#positionYnTxt").text("N");
					var page = $(".pagination").find("li.active").children(".page-link").text();
					getPoiInfoList(page);
				}
			}, "json");
		}
	});
	*/

	// poi 수정 처리
	$("#btnPoiModify").click(function() {
		var params = $("#poiModifyFrm").serialize();
		$("#poiModifyFrm").submit();
//		var params = $("#poiModifyFrm").serialize();
//		$.post("/adm/poi/poiModify.json", params, function(res) {
//			if(jResult(res, true)) {
//				$('#poiModifyModal').modal('hide');
//				var page = $(".pagination").find("li.active").children(".page-link").text();
//				getPoiInfoList(page);
//
//				var floorNo = $("#floorNo").val();
//				addPoiListToMap({"floorNo" : floorNo});
//			}
//		}, "json");
	});

	$("#poiModifyFrm").ajaxForm({
		url: '/adm/poi/poiModify.json',
    	type: 'post',
    	dataType : 'json',
    	beforeSubmit: function (data, frm, opt) {
    		// 유효성 체크
    		if(!jValidationFrm(frm)) return false;
		},
        success: function(res) {
        	if(jResult(res, true)) {
        		$('#poiModifyModal').modal('hide');
        		var page = $(".pagination").find("li.active").children(".page-link").text();
				getPoiInfoList(page);

				var floorNo = $("#floorNo").val();
				addPoiListToMap({"floorNo" : floorNo});
        	}
        }
    });

	document.getElementById("btnImgRDelete").onclick = function(){
		fnDeletePoiImg(document.getElementById("poiImgR"), document.getElementById("imgR"), 0);
	}

	// poi lod 레벨수 테이블 처리
	$("#levelCnt").change(function() {
		var thisVal = $(this).val();
		for(var i=0; i<10; i++) {
			if(i >= parseInt(thisVal)) $(".lod_"+i).hide();
			else $(".lod_"+i).show();
		}
	});

	// poi lod 카테고리별 일괄 적용
	$(".iconSizeAll, .lodTypeAll").change(function() {
		var objnm = $(this).attr("id");
		var thisval = $(this).val();
		$("select[name="+objnm+"]").val(thisval);
	});

	// lod 설정폼
	$("#btnLodSetForm").click(function() {
		$("#lodSetFrm").find(".nav-link").eq(0).click();

		//lod정보 호출
		$.post("/adm/map/poiLodInfo.json", {"mapNo":mapNo}, function(res) {
			if(jResult(res)) {
				var result = res.result;
				if(result == null) {
					// lod 설정이 없는 경우 기본 3단계
					$("#lodSetFrm")[0].reset();
					$("#levelCnt").change();
				} else {
					$("#maxDist").val(result.maxDist);
					$("#levelCnt").val(result.levelCnt);
					$("#levelCnt").change();

					$.each(result.poiLodInfoList, function(index, item) {
						// poi 중분류 기준으로 lod정보 매핑
						var obj = $(":hidden[name=category2No][value="+item.category2No+"]");
						var eqNo = $("#lodTab1").find(":hidden[name=category2No]").index(obj);
						// 1-10단계 처리
						for(var i=0; i<10; i++) {
							$("select[name='iconSize"+i+"']").eq(eqNo).val(item["iconSize"+i]);
							$("select[name='lodType"+i+"']").eq(eqNo).val(item["lodType"+i]);
						}

					});
				}

				$('#lodSetModal').modal('show');
			}
		}, "json");
	});

	// lod 정보 등록
	$("#btnPoiLodRegist").click(function() {
		// 유효성 체크
		var frm = $("#lodSetFrm");
		if(!jValidationFrm(frm)) return false;

		// lod 타입
		var allLodInfo = new Object();
		allLodInfo["mapNo"] = mapNo;
		allLodInfo["levelCnt"] = $("#levelCnt").val();
		allLodInfo["maxDist"] = $("#maxDist").val();

		// 카테고리별 lod 모든 단계1-10
		var lodInfoList = new Array();
		var i = 0;
		$("select[name=iconSize0]").each(function() {
				var lodInfo = new Object();
				lodInfo["mapNo"] = mapNo;
				lodInfo["category1No"] = $("#lodTab1").find(":hidden[name=category1No]").eq(i).val();
				lodInfo["category2No"] = $("#lodTab1").find(":hidden[name=category2No]").eq(i).val();
				for(var j=0; j<10; j++) {
					lodInfo["iconSize"+j] = $("select[name=iconSize"+j+"]").eq(i).val();
					lodInfo["lodType"+j] = $("select[name=lodType"+j+"]").eq(i).val();
				}
				lodInfoList.push(lodInfo);
			i++;
		});
		allLodInfo["poiLodInfoList"] = lodInfoList;

		// 등록
		$.ajax({
			contentType:'application/json',
			dataType : 'json',
			data : JSON.stringify(allLodInfo),
			url : '/adm/map/poiLodRegist.json',
			type : 'POST',
			success:function(data){
				if(jResult(data, true)) {
					$('#lodSetModal').modal('hide');
	        	}
			}
		});
	});

	// 현재화면 기준 카메라 거리값
	$("#btnLodMaxDist").click(function() {
		var curDist = Px.Lod.GetMaxDistance();
		$("#maxDist").val(curDist);
	});

	document.getElementById('inputUploadRouteFile').addEventListener('change', evt => {
		Px.Topology.Data.Clear()
		var file = evt.target.files[0];
		var reader = new FileReader();
		reader.onload = function(e) {
			Px.Topology.Data.Import(e.target.result);
			Px.Topology.Data.HideAll();
			modelExpand();
			Px.Topology.Data.Show(document.querySelector('#floorNo1').value);
		};
		modelCollapse(()=> {
			reader.readAsText(file);
		});
	});

	const tabList = document.querySelectorAll('.viewer-sidebar .nav-tabs .nav-link');
	tabList.forEach((tab) => {
		tab.addEventListener('click', async (event) => {
			if(event.currentTarget.classList.contains('active')) {
				return false;
			}

			const type = event.currentTarget.href.split('#')[1];
			const mapCtrlTool = document.querySelector('#mapCtrlTool');
			const evacRouteTool = document.getElementById('evacRouteTool');
			const poiEditTool = document.querySelector('#poiEditTool');
			const topoEditTool = document.querySelector('#topoEditTool');

			const floorSelect = document.querySelector('#floorSelect');
			const topoFloorSelect = document.querySelector('#topoFloorSelect');
			const categorySel = document.querySelector('#btnPoiCategorySel');
			const mapToolbarLeft = document.querySelector('#mapToolbarLeft');

			if(type === 'poiTab') {
				Px.Topology.Data.Clear();

				mapCtrlTool.classList.remove('d-none');
				evacRouteTool.classList.remove('d-none');
				poiEditTool.classList.remove('d-none');
				topoEditTool.classList.add('d-none');
				categorySel.classList.remove('d-none');
				mapToolbarLeft.classList.remove('d-none');
				
				floorSelect.classList.remove('d-none');
				topoFloorSelect.classList.add('d-none');
				
				floorSelect.querySelector('SELECT').value = '';
				modelCollapse(() => {
					changeFloor();
					camPos.changeCamPos('all');
				});
			} else {
				mapCtrlTool.classList.add('d-none');
				evacRouteTool.classList.add('d-none');
				poiEditTool.classList.add('d-none');
				topoEditTool.classList.remove('d-none');
				categorySel.classList.add('d-none');
				mapToolbarLeft.classList.add('d-none');
				
				floorSelect.classList.add('d-none');
				topoFloorSelect.classList.remove('d-none');

				if(document.getElementById('poiCategorySel').style.display === 'block') {
					categorySel.dispatchEvent(new Event('click'));
				}

				// Px.Model.Visible.ShowAll();
				await selectTopoType('일반인');
				topoFloorSelect.querySelector('SELECT').value = floorInfoList[0].floorNo;
				topoFloorSelect.querySelector('#floorNo1').dispatchEvent(new Event('change'));


				Px.Poi.HideAll();
				Px.Topology.Data.SetSize(0.5);
			}
		})
	})
});

/**
 * 노드 실행 Act
 * @param actType
 */
function setNodeAct(actType) {
	switch (actType) {
		case 'addNode' : {
			// Px.Topology.Data.CreateNode();
			Px.Topology.Data.CreatePoint();
			break;
		}
		case 'moveNode' : {
			// Px.Topology.Data.MoveNode();
			Px.Topology.Data.MoveObject()
			break;
		}
		case 'delNode' : {
			// Px.Topology.Data.Remove();
			Px.Topology.Data.DeleteObjectPointer()
			break;
		}
		case 'onewayLink' : {
			// Px.Topology.Data.Connect('oneway');
			Px.Topology.Data.ConnectLinkOneway()
			break;
		}
		case 'twowayLink' : {
			// Px.Topology.Data.Connect('twoway');
			Px.Topology.Data.ConnectLinkTwoway()
			break;
		}
		default : {
			console.error(actType);
			console.error('이상한 Actype 처리');
			break;
		}
	}
}

/**
 * POI 목록 호출
 * @param page
 * @returns
 */
function getPoiInfoList(page) {
	page = (page == "")? "1":page;
	$("input[name=page]", "#schFrm").val(page);
	if($("#poiListTabType").val() != "2") {
		var floorNo = $("#floorNo").val();
		$("input[name=floorNo]", "#schFrm").val(floorNo);
	} else {
		$("input[name=floorNo]", "#schFrm").val("");
	}

	var params = $("#schFrm").serializeArray();
	// 리스트 호출
	ajaxTemplate("/adm/poi/poiList.json", params, $("#poiInfoListTpl").html(), $("#poiInfoList"), pagination, '');
}

/**
 * 페이징 처리 함수
 * @param res
 * @returns
 */
function pagination(res) {
	if(res.total == 0) $("#poiInfoList").html("<tr><td colspan='9' class='text-center'>"+_SEARCH_NOT_EXIST+"</td></tr>");
	$(".pagination").html(pageNavigator(res.poiInfo.page, res.total, res.poiInfo.pageSize, 5, "getPoiInfoList"));
}

/**
 * poi 삭제
 * @param poiNo
 * @returns
 */
function deletePoiInfo(poiNo) {
	if(confirm("선택된 항목을 삭제하시겠습니까?")) {
		$.post("/adm/poi/poiRemove.json", {"poiNo":poiNo}, function(res) {
			if(jResult(res)) {
				// 도면 POI 제거
				Px.Poi.Remove(poiNo);
				// 페이지 재로드
				var page = $(".pagination").find("li.active").children(".page-link").text();
				getPoiInfoList(page);
			}
		}, "json");
	}
}

/**
 * poi 수정
 * @param poiNo
 * @returns
 */
function modifyPoiInfo(poiNo) {
	$('#poiModifyModal').modal('show');
	ajaxTemplate("/adm/poi/poiView.json", {"poiNo":poiNo}, $("#poiInfoFrmTpl").html(), $("#poiModifyFrm"), modifyPoiInfoCallback, '');
}

/**
 * poi수정 모달 콜백함수
 * @param res
 * @returns
 */
function modifyPoiInfoCallback(res) {
	var result = res.result;

	// 배치인 경우 미배치버튼 생성
	if(result.positionYn == "Y") $("#btnPosPoi").show();
	else $("#btnPosPoi").hide();

	// 분류 처리
	$("select[name='category1No']", "#poiModifyFrm").val(result.category1No);
	$.post("/adm/poi/poiCategoryList.json", {"category1No":result.category1No}, function(res) {
		if(jResult(res)) {
			var categoryList = "<option value=''>중분류</option>";
			$.each(res.result, function(idx, item) {
				var selected = (result.category2No == item.category2No)? "selected":"";
				categoryList += "<option value='"+ item.category2No +"' " + selected + ">"+ item.category2Nm +"</option>";
    		});
			$("select[name=category2No]", "#poiModifyFrm").html(categoryList);
		}
	}, "json");

	var floorListOpt = "<option value=''>층선택</option>";
	$.each(floorInfoList, function(index, item) {
		floorListOpt += "<option value='"+item.floorNo+"'>"+item.floorNm+"</option>";
	});
	$("select[name='floorNo']", "#poiModifyFrm").html(floorListOpt);
	$("select[name='floorNo']", "#poiModifyFrm").val(result.floorNo);

	// 미리보기 이미지
	fnSetPreviewEvent(document.getElementById("poiImgM"), document.getElementById("imgM"));

	$("#btnImgMDelete").on("click", function(){
		fnDeletePoiImg(document.getElementById("poiImgM"), document.getElementById("imgM"),document.getElementById("poiNo").value);
	});

	// 중분류 선택에 따른 아이콘셋 출력
	//ajaxTemplate("/adm/poi/poiCategoryView.json", {"category2No":result.category2No}, $("#iconsetTpl").html(), $(".iconsetView", "#poiModifyFrm"));
}

function fnUploadRouteFile() {
	const inputFile = document.getElementById('inputUploadRouteFile');
	inputFile.click();
}

const fnDownloadRouteFile = () => {
	modelCollapse(()=> {
		if(Px.Topology.Data.Export().length === 0) {
			alert('저장가능한 데이터가 없습니다.');
			return;
		}

		Px.Topology.Data.Download();
		setTimeout( ()=> {
			modelExpand(() => {
				document.getElementById('floorNo1').dispatchEvent(new Event('change'));
			});
		}, 0);
	});
}

const fnSaveRouteToDB = () => {
	modelCollapse(()=> {
		const topoJson = Px.Topology.Data.Export(true);
		const { topoType } = document.querySelector('.topo-type.active').dataset;

		if(topoJson === '[]') {
			alert('저장가능한 데이터가 없습니다.');
			return;
		}

		const param = {
			mapNo: parseInt(mapNo, 10),
			topoType,
			topoJson
		}

		$.ajax({
			contentType:'application/json',
			dataType : 'json',
			data : JSON.stringify(param),
			url : '/adm/topology/updateTopologyInfo.json',
			type : 'PATCH',
			success:function(data){
				modelExpand(() => {
					document.getElementById('floorNo1').dispatchEvent(new Event('change'));
				});
			}
		});
	});
}

//POI 등록 및 수정시 대표이미지 삭제처리하는 함수
function fnDeletePoiImg(targetDom,previewDom,poiNo) {
	var para = document.location.href.split("/adm")[0];
	if (previewDom.src == para+'/resources/img/noPhoto.png') {
		alert("기존 이미지가 없습니다.");
		return false;
	}else if (confirm("이미지 삭제시 복구가 불가능합니다. 삭제하시겠습니까?")) {
		if (poiNo != 0) {
			fnDeletePreImg(targetDom, previewDom,'/adm/poi/poiImgRemove.json', {'poiNo' : poiNo});
		}
		fn_noPhoto(previewDom);
		targetDom.value ='';
	}
}


/**
 * 마우스드래그 poi추가
 * @param poiNo
 * @returns
 */
function addPoiByMouse(poiNo) {
	if($("#floorNo").val() == "") {
		alert("전체 도면에서는 POI배치가 불가능합니다.");
		return false;
	}
	$.post("/adm/poi/poiView.json", {"poiNo":poiNo}, function(res) {
		if(jResult(res)) {
			var result = res.result;
			var type = null;
			var url  = null;
			var iconUrl = null;
			// 2D 아이콘
			if(result.poiIconset.iconsetType.indexOf("2D") != -1) {
				iconUrl = result.poiIconset.iconset2d0FilePath;
			}
			// 3D 아이콘
			if(result.poiIconset.iconsetType.indexOf("3D") != -1) {
				type = result.poiIconset.iconset3d.split('.').pop().toLowerCase(); // poi 타입
				if(type == "obj") {
					url = {'obj':result.poiIconset.iconset3d, 'mtl':result.poiIconset.iconset3d.replace(/\.\w+$/, ".mtl")}
				} else if(type == "fbx") {
					url = result.poiIconset.iconset3d;
				}
			}
			var id = poiNo;
			var group = result.category1No;
			var lineHeight = (result.poiIconset.iconsetType == "3D")? 0:_POI_LINE_HEIGHT;
			var displayText = result.poiNm;
			var property = {'mapNo':result.mapNo, 'floorNo':result.floorNo, 'floorGroup':result.floorInfo.floorGroup, 'dvcCd':result.dvcCd};

			Px.Poi.AddByMouse({
				type: type,
				url: url,
		        id: poiNo,
		        group: group,
		        iconUrl: iconUrl,
		        lineHeight: lineHeight,
		        displayText: displayText,
		        property: property,
				scale: {x: 0.1, y: 0.1, z: 0.1},
		        onComplete: function (id, x, y, z) {
		        	//console.log(id, x, y, z);
		        	var params = {"poiNo":id, "positionYn":"Y", "posX":x, "posY":y, "posZ":z, "dvcCd":property.dvcCd};
		        	ajaxFormData("/adm/poi/poiModify.json", params, function(res) {
		    			if(jResult(res)) {
		    				var page = $(".pagination").find("li.active").children(".page-link").text();
		    				getPoiInfoList(page);
		    				hidePoiMenu();
		    			}
		    		}, "json");
		        }
		    });
		}
	}, "json");
}

/**
 * poi 미배치 변경
 * @param poiNo
 * @returns
 */
function chgNotPositPoi(poiNo) {
	if(confirm("선택된 POI를 미배치로 변경하시겠습니까?")) {
		var params = {"poiNo":poiNo, "positionYn":"N", "rotX":"0", "rotY":"0", "rotZ":"0", "sclX":"0","sclY":"0", "sclZ":"0"};
		ajaxFormData("/adm/poi/poiPositionModify.json", params, function(res) {
			if(jResult(res)) {
				// 도면 POI 제거
				Px.Poi.Remove(poiNo);
				// 좌측 리스트 갱신
				var page = $(".pagination").find("li.active").children(".page-link").text();
				getPoiInfoList(page);
			}
		}, "json");
	}
}


/**
 * POI 편집 콜백 함수
 * @param data
 * @returns
 */
function poiEditCallback(data) {
	var poiNo = data.id;
	var posX = data.position.x;
	var posY = data.position.y;
	var posZ = data.position.z;
	//var rotX = data.rotation;
	var rotX = data.rotation.x;
	var rotY = data.rotation.y;
	var rotZ = data.rotation.z;
	var sclX = Math.round(data.scale.x);
	var sclY = Math.round(data.scale.y);
	var sclZ = Math.round(data.scale.z);

	//console.log(data);
	var params = {"poiNo":poiNo, "posX":posX, "posY":posY, "posZ":posZ, "rotX":rotX, "rotY":rotY, "rotZ":rotZ, "sclX":sclX, "sclY":sclY, "sclZ":sclZ};
	ajaxFormData("/adm/poi/poiModify.json", params, function(res) {
		if(jResult(res)) {
			//var page = $(".pagination").find("li.active").children(".page-link").text();
			//getPoiInfoList(page);
		}
	}, "json");
}


/**
 * poi 메뉴 보임
 * @param data
 * @returns
 */
var animateHandler = null;
function showPoiMenu(data) {
	var poiNo = data.id;
	//var lineHeight = (data.type == "")? 20:0;
	//animateHandler = requestAnimationFrame(showPoiMenu);
	Px.Poi.Get2DPosition(poiNo, function(x, y) {
		var canvasRect = document.getElementsByTagName('canvas')[0].getBoundingClientRect();
		x = x + canvasRect.left;
		y = y + canvasRect.top;

		var poiMenuHtml = $('#poiMenuTpl').html().replace(/poiNo/g, poiNo);
		var target = $('#poiMenuPop');
		target.html(poiMenuHtml);
		target.show();
		setLayerPos(target, x, y);
		//target.css({top:x + "px", left:y + "px", position:"absolute"});
	});
}

/**
 * poi 메뉴 레이어 숨김
 * @returns
 */
function hidePoiMenu() {
  //cancelAnimationFrame(animateHandler);
  $('#poiMenuPop').hide();
}

function poiEditOff() {
	Px.Edit.Off();
	// poi 이벤트 등록
	Px.Event.AddEventListener('dblclick', 'poi', showPoiMenu);
	$("#poiEditTool > button").removeClass('on');
}


/**
 * 층별 POI 리스트 도면에 추가
 * @param {검색조건 : 검색어} ex) {'floorNo', floorNo}
 * @returns
 */
function addPoiListToMap(param) {
	/*
	if(floorNo === undefined || floorNo == "") {
		Px.Poi.Clear();
	} else {
		Px.Poi.RemoveProperty('floorNo', floorNo);
	}
	*/
	Px.Poi.Clear();

	if(isValEmpty(param)) param = {};

	param.mapNo = mapNo;
	param.positionYn = "Y";

	return $.ajax({
		  type: "POST",
		  url: "/api/viewer/poiList.json",
		  data: param,
		  dataType:'json',
		  success: function(res) {
				if(jResult(res)) {
					var result = res.result; // poi 리스트

					//배열로 webgl처리
					var poiDataArray = [];

					$.each(result, function(idx, item) {
						var type = null;
						var url  = null;
						var iconUrl = null;
						// 2D 아이콘
						if(item.poiIconset.iconsetType.indexOf("2D") != -1) {
							iconUrl = item.poiIconset.iconset2d0FilePath;
						}
						// 3D 아이콘
						if(item.poiIconset.iconsetType.indexOf("3D") != -1) {
							type = item.poiIconset.iconset3d.split('.').pop().toLowerCase(); // poi 타입
							if(type == "obj") {
								url = {'obj':item.poiIconset.iconset3d, 'mtl':item.poiIconset.iconset3d.replace(/\.\w+$/, ".mtl")}
							} else if(type == "fbx") {
								url = item.poiIconset.iconset3d;
							}
						}
						var id = item.poiNo;
						var group = item.category1No;
						var position = {'x':parseFloat(item.posX), 'y':parseFloat(item.posY), 'z':parseFloat(item.posZ)};
						//var rotation = parseFloat(item.rotX);
						var rotation = {'x':parseFloat(item.rotX), 'y':parseFloat(item.rotY), 'z':parseFloat(item.rotZ)};
						var scale = {'x':item.sclX, 'y':item.sclY, 'z':item.sclZ};
						var lineHeight = (item.poiIconset.iconsetType == "3D")? 0:_POI_LINE_HEIGHT;
						var displayText = item.poiNm;
						var property = {
							'mapNo':item.mapNo,
							'floorNo':item.floorNo,
							'floorGroup':item.floorInfo.floorGroup,
							'dvcCd':item.dvcCd,
							'subCategory':item.category2No,
							'floorNo_category':item.floorNo+'_'+item.category1No,	// 층과 카테고리 조합 속성	(현재 사용하지 않음)
							'floorGroup_category':item.floorInfo.floorGroup+'_'+item.category1No	//그룹층과 카테고리 조합 속성
						};

						// poi정보 생성
						var poiInfo = {
							'type':type,
							'url':url,
							'id':id,
							'group':group,
							'position':position,
							'rotation':rotation,
							'scale':scale,
							'iconUrl':iconUrl,
							'lineHeight':lineHeight,
							'displayText':displayText,
							'property':property
						};

						poiDataArray.push(poiInfo);

						//console.log(poiInfo);
						//addPoi(poiInfo);
					});

					//배열로 넣어야한다.
		            Px.Poi.AddFromDataArray(poiDataArray, function() {	//일단 동기처리함.

		            	//console.log("POI LOADING COMPLETE");

		            	//카테고리로 가시화처리
		            	var notChkCategoryNo1 = getChkPoiCategory1NoArr(false);
		            	for (var i=0, size=notChkCategoryNo1.length; i<size; ++i){
		            		Px.Poi.HideByGroup(notChkCategoryNo1[i]);
		            	}
		            	//카테고리로 가시화처리 종료

		            	//등록콜백 실행
		                if(typeof(webglCallbacks.poiLoad) === 'function')  webglCallbacks.poiLoad();
		                webglCallbacks.poiLoad = undefined;

						$("#loadingLayer").hide();
						// 카메라 자유롭게
						Px.Camera.EnableScreenPanning();
		            });
				}
		  }
		});
}

function sidbarDimm (flag) {
	var divRect = $('.viewer-sidebar')[0].getBoundingClientRect();
	var dimmLayer = document.getElementById('sidebar-dimm');
	dimmLayer.style.width = divRect.width + 'px';
	dimmLayer.style.height = divRect.height + 'px';
	dimmLayer.style.left = divRect.left + 'px';
	dimmLayer.style.top = divRect.top + 'px';

	if(flag) dimmLayer.style.display = 'block';
	else dimmLayer.style.display = 'none';
}

const selectTopoType = async (type) => {
	Px.Topology.Data.Clear();
	await $.get("/adm/topology/getTopologyInfo.json", { mapNo, topoType: type }, (res) => {
		if(res.topologyInfo === null) {
			return;
		}

		const { topoJson } = res.topologyInfo;
		if (topoJson) {
			Px.Topology.Data.Import(JSON.parse(topoJson));
			// Px.Topology.Data.HideAll();
			modelExpand(() => Px.Camera.ExtendView());
		}
	});
}

const nodeButtonActive = () => {
	Px.Topology.Data.Cancel();
	const floorNo2 = document.querySelector('#floorNo2').value;
	const nodeButtons = document.querySelectorAll('.topo-node');
	
	nodeButtons.forEach((btn)=> {
		if(floorNo2 === '') {
			btn.classList.remove('disabled');
		} else {
			btn.classList.add('disabled');
			btn.classList.remove('on');
		}
	})
	
}