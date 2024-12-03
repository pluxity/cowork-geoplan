$(function() {

	$("#treeview").hummingbird();
	getMapInfoList(1);

	//엑셀 다운로드할 항목 체크창 띄우기
	$("#btnChkDown").click(function(){
		$(":checkbox[name='dw']").prop("checked",true);
		$("#downModal").modal('show');
	});
	//엑셀 다운로드
	$("#btnDownload").click(function(){
		$("#searchKeywordEd").val($("#searchKeyword").val());
		$("#searchTypeEd").val($("#searchType").val());
		$("#category3NoStr").val(arrToString($(":checkbox[name='category3No']:checked")));
		
		$("#fieldNmList").val(arrToString($(":checkbox.seqChk[name='dw']:checked")));
		$("#mapListDownFrm").attr("action", "/adm/map/mapExceldownload.do");
		$("#mapListDownFrm").submit();
		$("#downModal").modal("hide");
	});
	
	// 도면 등록 모달 레이어 호출
	$("#btnMapForm").click(function() {
		// 맵등록창 보이기
		$('#mapRegistModal').modal('show');
		$("#mapRegistFrm")[0].reset();
	});

	// 대분류,중분류 옵션 처리
	$("#category1No, #category2No").change(function() {
		var thisId = $(this).attr("id");
		var nextObj = $(this).closest("div").next().find("select");
		var nextDataNm = nextObj.data("name");
		var defaultOpt = nextObj.attr("title");
		var categoryList = "<option value='' selected>"+defaultOpt+"</option>";

		if($(this).val() != "") {
			var param = (thisId == "category1No")? {category1No:$(this).val()}:{category2No:$(this).val()};
			$.post("/adm/map/mapCategoryList.json", param, function(res) {
				if(jResult(res)) {
					$.each(res.result, function(idx, item) {
						categoryList += "<option value='"+ item[nextDataNm+"No"] +"'>"+ item[nextDataNm+"Nm"] +"</option>";
		    		});
					nextObj.html(categoryList);
				}
			}, "json");
		} else {
			nextObj.html(categoryList);
		}
	});

	// 맵업로드 등록
	$("#btnMapRegist").click(function() {
		$("#mapRegistFrm").submit();
	});

	$("#mapRegistFrm").ajaxForm({
		url: '/adm/map/mapRegist.json',
    	type: 'post',
    	dataType : 'json',
    	beforeSubmit: function (data, frm, opt) {
    		// 도면코드 공백 제거
    		$("#mapCd").val($("#mapCd").val().replace(/ /gi, "")); 
    		// 유효성 체크
    		if(!jValidationFrm(frm)) return false;
    		$("#loadingLayer").show();
		},
        success: function(res) {
        	$("#loadingLayer").hide();        	
        	if(jResult(res, true)) {

				calculateCenterPos(res, function(){
					$('#mapRegistModal').modal('hide');
					$("#loadingLayer").hide();
					//도면리스트 재호출
					getMapInfoList(1);
				});
				
        		$('#mapRegistModal').modal('hide');        		
        		// 도면리스트 재호출
        		getMapInfoList(1);
        	}
        }         
    });

	// 도면명 검색
	$("#schFrm").submit(function() {
		//$("#mapCategoryTree").find(":checkbox").prop("checked", false);
		getMapInfoList(1);
		return false;
	});

	// 카테고리 트리 선택
	$("#mapCategoryTree").find(":checkbox").on("click", function() {
		//$("#schMapNm").val("");
		getMapInfoList(1);
	});	

	// 건물 마커 좌표등록 새창 열기
	$('#openMap').click(() => {
		window.open('/adm/map/2dMap.do')
	})
});


// 도면목록 호출
function getMapInfoList(page) {
	var mapNm = $("#schMapNm").val();
	// 카테고리3 체크리스트
	var category3No= [];
	$(":checkbox[name='category3No']:checked").each(function(i) {
		category3No.push($(this).val());
    });
	var params = {"page":page, "mapNm":mapNm, "category3NoArr":category3No};
	// 도면리스트 호출후 리스트 재호출
	ajaxTemplate("/adm/map/mapList.json", params, $("#mapInfoListTpl").html(), $("#mapInfoList"), pagination, '');
}

// 페이징 처리 함수
function pagination(res) {
	if(res.total == 0) $("#mapInfoList").html("<div class='col-md-3' style='font-weight:bold;'>"+_SEARCH_NOT_EXIST+"</div>");
	$(".pagination").html(pageNavigator(res.mapInfo.page, res.total, res.mapInfo.pageSize, 10, "getMapInfoList"));
}


/**
 * 맵 중심좌표 계산후 업데이트
 * @param result
 * @param callback
 * @returns
 */
function calculateCenterPos(result, callback) {

	var floorInfoList = result.floorInfoList;
	var mapInfo = result.mapInfo;

	var sbmDataArray = [];
	for(var i = 0; i < floorInfoList.length; i++) {
		var fileName = floorInfoList[i].floorFileNm.replace(".", "").replace("\\", "");
		var url = "/map/" + mapInfo.mapNo + "/" + mapInfo.mapVer + "/" + fileName;
		sbmDataArray.push({
			url: url,
		});
	}

	Px.Util.CalculateSbmCenter({urlDataList: sbmDataArray}, function(center){

		//console.log('center:', center);

		var formData = new FormData();
		formData.enctype = "multipart/form-data";
		formData.append('mapNo', mapInfo.mapNo);
		formData.append('centerPosJson', JSON.stringify(center));

		$.ajax({
			url: '/adm/map/mapModify.json',
			type: 'POST',
			dataType : 'json',
			enctype: 'multipart/form-data',
			contentType: false,
			processData: false,
			data : formData,
			success: function(res) {
				//console.log(res);
				if(callback) callback();
			},
			error : function(e){
				//console.log(e);
			}
		});
	});
}