$(function() {
	// 도면 번호
	var mapNo = $("#mapNo").val();

	// 도면 이력 리스트
	getMapHstList(mapNo);
	
	// 대분류 선택시 중분류 옵션 처리
	$("#category1No").change(function() {
		$.post("/adm/map/mapCategoryList.json", {category1No:$("#category1No").val()}, function(res) {
			if(jResult(res)) {
				var mapCategoryList = "<option value='' selected>선택</option>";
				$.each(res.result, function(idx, item) {
					mapCategoryList += "<option value='"+ item.category2No +"'>"+ item.category2Nm +"</option>";
	    		});
				$("#category2No").html(mapCategoryList);
			}
		}, "json");
	});

	// 중분류 선택시 소분류 옵션 처리
	$("#category2No").change(function() {
		$.post("/adm/map/mapCategoryList.json", {category2No:$("#category2No").val()}, function(res) {
			if(jResult(res)) {
				var mapCategoryList = "<option value='' selected>선택</option>";
				$.each(res.result, function(idx, item) {
					mapCategoryList += "<option value='"+ item.category3No +"'>"+ item.category3Nm +"</option>";
	    		});
				$("#category3No").html(mapCategoryList);
			}
		}, "json");
	});

	// 도면정보 수정
	$("#btnMapInfoSave").click(function() {
		$("#mapFrm").submit();
	});

	// 도면정보 수정 처리
	$("#mapFrm").ajaxForm({
		url: '/adm/map/mapModify.json',
    	type: 'post',
    	dataType : 'json',
    	beforeSubmit: function (data, frm, opt) {
    		// 도면코드 공백 제거
    		$("#mapCd").val($("#mapCd").val().replace(/ /gi, "")); 
    		// 유효성 체크
    		if(!jValidationFrm(frm)) return false;
		},
        success: function(res) {
        	if(jResult(res, true)) {
        		location.reload();
        	}
        }
    });

	// 도면 이력 등록
	$("#mapHstFrm").ajaxForm({
		url: '/adm/map/mapHstRegist.json',
    	type: 'post',
    	dataType : 'json',
    	beforeSubmit: function (data, frm, opt) {
    		if(confirm("선택된 도면을 업로드시\n해당 도면이 사용자 화면에 즉시 적용됩니다.\n적용하시겠습니까?")) {
    			// 유효성 체크
        		if(!jValidationFrm(frm)) return false;
    		}
    		$("#loadingLayer").show();
		},
        success: function(res) {
        	$("#loadingLayer").hide();
        	if(jResult(res, true)) {
        		location.reload();
        	}
        }
    });

	// 도면 삭제
	$("#btnMapInfoDel").click(function() {
		if(confirm("도면을 삭제하시겠습니까?\n삭제한 도면은 복구되지 않습니다.")) {
			var params = {"mapNo":mapNo};
			$.post("/adm/map/mapRemove.json", params, function(res) {
				if(jResult(res)) {
					location.replace("/adm/map/mapList.do");
				}
			}, "json");
		}
	});
	
	//이미지 미리보기
	fnSetPreviewEvent(document.getElementById("mapImg"),document.getElementById("mapImgPrev"));

});


/**
 * 도면 이력 리스트
 * @param mapNo
 * @returns
 */
function getMapHstList(mapNo) {
	var params = {"mapNo":mapNo};
	ajaxTemplate("/adm/map/mapHstList.json", params, $("#mapHstListTpl").html(), $("#mapHstList"));
}


/**
 * 도면파일 다운로드
 * @param mapFileNo
 * @returns
 */
function downloadMapFile(mapFileNo) {
	location.href = "/download/file/?fileNo=" + mapFileNo;
}


/**
 * 도면 이력 삭제
 * @param mapNo
 * @param mapVer
 * @param mapFileNo
 * @returns
 */
function deleteMapHst(mapNo, mapVer, mapFileNo) {
	/*
	if($("#mapVer").val() == mapVer) {
		alert("현재 도면에서 사용중인 버전은 삭제할 수 없습니다.");
		return false;
	}
	*/
	var params = {"mapNo":mapNo, "mapVer":mapVer, "mapFileNo":mapFileNo}
	if(confirm("선택한 도면을 삭제하시겠습니까?\n삭제된 도면은 복구되지 않습니다.")) {
		$.post("/adm/map/mapHstRemove.json", params, function(res) {
			if(jResult(res)) {
				// 도면 이력 재호출
				getMapHstList(mapNo);
			}
		}, "json");
	}
}


/**
 * 관리자 뷰어 팝업
 * @param mapNo
 * @param mapVer
 * @returns
 */
function popAdmView(mapNo, mapVer) {
	window.open("/adm/map/viewer.do?mapNo="+mapNo+"&mapVer="+mapVer);
}

document.getElementById('openMap').addEventListener('click', (event) => {
	const { mapNo } = event.target.dataset;
	window.open(`/adm/map/2dMap.do?mapNo=${mapNo}`);
});