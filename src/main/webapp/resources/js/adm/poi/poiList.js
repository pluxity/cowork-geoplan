$(function() {

	$("#treeview").hummingbird();
	getPoiInfoList(1);

	// 엑셀 다운로드할 항목 모달 띄우기
	$("#btnChkDown").click(function() {
		$(":checkbox[name='dw']").prop("checked", true);
		$("#downModal").modal('show');
	});
	// 엑셀 다운로드
	$("#btnDownload").click(function() {
		// var fieldNmList = arrToString($(":checkbox.seqChk[name='dw']:checked"));
		// var category2No = arrToString($(":checkbox[name='category2No']:checked"));
		$("#category2NoString").val(arrToString($(":checkbox[name='category2No']:checked")));
		$("#searchKeywordEd").val($("#searchKeyword").val());
		$("#searchTypeEd").val($("#searchType").val());
		$("#mapNo").val($("select[name=mapNo]", "#schFrm").val());
		$("#floorNo").val($("select[name=floorNo]", "#schFrm").val());
		$("#fieldNmList").val(arrToString($(":checkbox.seqChk[name='dw']:checked")));
		$("#poiListDownFrm").attr("action", "/adm/poi/poiExceldownload.do");
		$("#poiListDownFrm").submit();
		$("#downModal").modal("hide");
	});

	// POI 등록 모달 레이어 호출
	$("#btnPoiForm").click(function() {// poi 등록창 보이기
		hideAutoCompleteLayer();
		$('#poiRegistModal').modal('show');
		$("#poiRegistFrm")[0].reset();
		$(".iconsetView").html("");
		// 미리보기 이미지
		fnSetPreviewEvent(document.getElementById("poiImgR"), document.getElementById("imgR"));
	});

	// POI 일괄등록 모달 레이어 호출
	$("#btnPoiBatForm").click(function() {
		$('#poiBatRegistModal').modal('show');
		$("#poiBatRegistFrm")[0].reset();
	});

	// 대분류 선택시 중분류 옵션 처리
	$(document).on("change", "select[name=category1No]", function() {
		var thisForm = $(this).closest("form");
		var categoryList = "<option value='' selected>중분류</option>";
		if ($(this).val() != "") {
			$.post("/adm/poi/poiCategoryList.json", {"category1No" : $(this).val()}, function(res) {
				if (jResult(res)) {
					$.each(res.result, function(idx, item) {
					categoryList += "<option value='"+ item.category2No + "'>"+ item.category2Nm + "</option>";
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
	$(document).on("change", "select[name=category2No]", function() {
		var targetDom = $(this).closest("form").find(".iconsetView");
		if ($(this).val() != "") {
			ajaxTemplate("/adm/poi/poiCategoryView.json", {"category2No" : $(this).val()}, $("#iconsetTpl").html(), targetDom);
		} else {
			targetDom.html("");
		}
	});

	// 도면 선택시 층 목록
	$("select[name=mapNo]").change(function() {
		var thisForm = $(this).closest("form");
		var floorList = "<option value='' selected>층선택</option>";
		if ($(this).val() != "") {
			$.post("/api/viewer/mapInfo.json", {mapNo : $(this).val()},function(res) {
				if (jResult(res)) {
					$.each(res.floorInfoList, function(idx,item) {
						floorList += "<option value='"+ item.floorNo + "'>"+ item.floorNm + "</option>";
					});
					console.log(floorList);
					$("select[name=floorNo]", thisForm).html(floorList);
				}
			}, "json");
		} else {
			$("select[name=floorNo]", thisForm).html(floorList);
		}
	});

	// POI 등록
	$("#btnPoiRegist").click(function() {
		$("#poiRegistFrm").submit();
	});

	$("#poiRegistFrm").ajaxForm({
		url : '/adm/poi/poiRegist.json',
		type : 'post',
		dataType : 'json',
		beforeSubmit : function(data, frm, opt) {
			// 유효성 체크
			if (!jValidationFrm(frm))
				return false;
		},
		success : function(res) {
			if (jResult(res, true)) {
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
		url : '/adm/poi/poiBatRegist.json',
		type : 'post',
		dataType : 'json',
		beforeSubmit : function(data, frm, opt) {
			// 유효성 체크
			if (!jValidationFrm(frm))
				return false;
		},
		success : function(res) {
			if (jResult(res, true)) {
				$('#poiBatRegistModal').modal('hide');

				// POI리스트 재호출
				getPoiInfoList(1);
			}
		}
	});

	// POI명 검색
	$("#schFrm").submit(function() {
		getPoiInfoList(1);
		return false;
	});

	// 카테고리 트리 선택
	$("#poiCategoryTree").find(":checkbox").on("click", function() {
		getPoiInfoList(1);
	});

	// 체크 poi 삭제
	$("#btnPoiDel").click(function() {
		if ($(":checkbox[name='poiNo']:checked").length < 1) {
			alert("체크된 항목이 없습니다.");
			return false;
		} else {
			if (confirm("선택된 항목을 삭제하시겠습니까?")) {
				var poiNo = [];
				$(":checkbox[name='poiNo']:checked").each(function(i) {
					poiNo.push($(this).val());
				});
				$.post("/adm/poi/poiRemove.json", {"poiNoArr" : poiNo}, function(res) {
					if (jResult(res, true)) {
						var page = $(".pagination").find("li.active").children(".page-link").text();
						getPoiInfoList(page);
						$(".chkall").prop("checked", false);
					}//if (jResult(res, true)) 
				}, "json");						
			}// if (confirm("선택된 항목을 삭제하시겠습니까?"))
		}//if-else문
	});
	
	// 검색 조건 도면 선택시
	$("select[name=mapNo], select[name=floorNo]", "#schFrm").change(function() {
		$("#schFrm").submit();
	});

	// poi 미배치로 변경 - 체크선택
	$("#btnSelPosPoi").click(function() {
		if ($(":checkbox[name='poiNo']:checked").length < 1) {
			alert("체크된 항목이 없습니다.");
			return false;
		} else {
			if (confirm("선택된 POI를 미배치로 변경하시겠습니까?")) {
				var poiNo = [];
				$(":checkbox[name='poiNo']:checked").each(function(i) {
					poiNo.push($(this).val());
				});
				var params = {
//					"poiNoArr" : poiNo,
					"poiNoList" : poiNo,
					"positionYn" : "N",
					"rotX" : "0",
					"rotY" : "0",
					"rotZ" : "0",
					"sclX" : "100",
					"sclY" : "100",
					"sclZ" : "100"
				};
				ajaxFormData("/adm/poi/poiPositionModify.json", params, function(res) {
					if (jResult(res)) {
						var page = $(".pagination").find("li.active").children(".page-link").text();
						getPoiInfoList(page);
					}
				}, "json");
			}//if (confirm("선택된 POI를 미배치로 변경하시겠습니까?"))
		}//if-else문
	});

	// poi 미배치로 변경 - 단일
	$("#btnPosPoi").click(function() {
		if (confirm("선택된 POI를 미배치로 변경하시겠습니까?")) {
			var poiNo = $("input[name='poiNo']","#poiModifyFrm").val();
			var params = {
				"poiNo" : poiNo,
				"positionYn" : "N",
				"rotX" : "0",
				"rotY" : "0",
				"rotZ" : "0",
				"sclX" : "100",
				"sclY" : "100",
				"sclZ" : "100"
			};
			$.post("/adm/poi/poiModify.json", params, function(res) {
				if (jResult(res)) {
					$("#positionYnTxt").text("N");
					var page = $(".pagination").find("li.active").children(".page-link").text();
					getPoiInfoList(page);
				}
			}, "json");
		}//if (confirm("선택된 POI를 미배치로 변경하시겠습니까?"))
	});

	// POI 수정 처리
	$("#btnPoiModify").click(function() {
		$("#poiModifyFrm").submit();
	});

	$("#poiModifyFrm").ajaxForm(
			{
				url : '/adm/poi/poiModify.json',
				type : 'post',
				dataType : 'json',
				beforeSubmit : function(data, frm, opt) {
					// 유효성 체크
					if (!jValidationFrm(frm))
						return false;
				},
				success : function(res) {
					if (jResult(res, true)) {
						$('#poiModifyModal').modal('hide');
						var page = $(".pagination").find("li.active").children(".page-link").text();
						getPoiInfoList(page);
					}
				}
			});

	document.getElementById("btnImgRDelete").onclick = function() {
		fnDeletePoiImg(document.getElementById("poiImgR"), document.getElementById("imgR"), 0);
		// document.getElementById("imgR").src = '/resources/img/noPhoto.png';
		// document.getElementById("poiImgR").value='';
		// fnDeletePreImg(document.getElementById("poiImgR"),
		// document.getElementById("imgR"),'/adm/poi/poiImgRemove.json',
		// {'poiNo' : 0});
	}

	//리스트 정렬
	sortTableList(getPoiInfoList);

	$("div.modal-content").on("click", function(){				
		hideAutoCompleteLayer();
	});
	
	//auto complete(자동완성)
	$("input[name='dvcCd']").on("keyup click focus", function(e){
		e.stopPropagation();
		$(this).parent().find("ul").attr("class","input_autocomplete");
		getAutoCompleteList($(this).val());
	});
	
});

// POI 목록 호출
function getPoiInfoList(page) {
	// poi 중분류 체크리스트
	var category2No = [];
	$(":checkbox[name='category2No']:checked").each(function(i) {
		category2No.push($(this).val());
	});
	var searchType = $("#searchType").val();
	var searchKeyword = $("#searchKeyword").val();
	var mapNo = $("select[name=mapNo]", "#schFrm").val();
	var floorNo = $("select[name=floorNo]", "#schFrm").val();
	var sortBy = $("#sortBy").val();
	var sortType = $("#sortType").val();

	var params = {
		"page" : page,
		"searchType" : searchType,
		"searchKeyword" : searchKeyword,
		"category2NoArr" : category2No,
		"mapNo" : mapNo,
		"floorNo" : floorNo,
		"sortBy" : sortBy,
		"sortType" : sortType
	};
	// 리스트 호출
	ajaxTemplate("/adm/poi/poiList.json", params, $("#poiInfoListTpl").html(),$("#poiInfoList"), pagination, '');
}

// 페이징 처리 함수
function pagination(res) {
	if (res.total == 0)
		$("#poiInfoList").html("<tr><td colspan='10' class='text-center'>" + _SEARCH_NOT_EXIST+ "</td></tr>");
	$(".pagination").html(pageNavigator(res.poiInfo.page, res.total, res.poiInfo.pageSize,10, "getPoiInfoList"));
}

// poi 삭제
function deletePoiInfo(poiNo) {
	if (confirm("선택된 항목을 삭제하시겠습니까?")) {
		$.post("/adm/poi/poiRemove.json", {
			"poiNo" : poiNo
		}, function(res) {
			if (jResult(res, true)) {
				var page = $(".pagination").find("li.active").children(".page-link").text();
				getPoiInfoList(page);
			}
		}, "json");
	}
}

// poi 수정
function modifyPoiInfo(poiNo) {
	hideAutoCompleteLayer();
	$('#poiModifyModal').modal('show');
	// ajaxTemplate("/adm/poi/poiView.json", {"poiNo":poiNo},
	// $("#poiInfoFrmTpl").html(), $("#poiModifyFrm"), modifyPoiInfoCallback,
	// '');

	$.post("/adm/poi/poiView.json",{"poiNo" : poiNo},function(res) {
		if (jResult(res)) {
			var result = res.result;
			var resHtml = templateReplacer(result, $("#poiInfoFrmTpl").html());
			$("#poiModifyFrm").html(resHtml);

			// 배치인 경우 미배치버튼 생성
			if (result.positionYn == "Y")	$("#btnPosPoi").show();
			else	$("#btnPosPoi").hide();

			// 분류 처리
			$("select[name='category1No']", "#poiModifyFrm").val(result.category1No);
			$.post("/adm/poi/poiCategoryList.json",{"category1No" : result.category1No},function(res) {
				if (jResult(res)) {
					var categoryList = "<option value=''>중분류</option>";
					$.each(res.result,function(idx,item) {
						var selected = (result.category2No == item.category2No) ? "selected": "";
						categoryList += "<option value='"+ item.category2No+ "' "+ selected+ ">"+ item.category2Nm+ "</option>";
					});
					$("select[name=category2No]","#poiModifyFrm").html(categoryList);
				}
			}, "json");

			// 중분류 선택에 따른 아이콘셋 출력
			ajaxTemplate("/adm/poi/poiCategoryView.json", {"category2No" : result.category2No}, $("#iconsetTpl").html(), $(".iconsetView","#poiModifyFrm"));

			// 미리보기 이미지
			fnSetPreviewEvent(document.getElementById("poiImgM"), document.getElementById("imgM"));

			$("#btnImgMDelete").on("click",function() {
				fnDeletePoiImg(document.getElementById("poiImgM"),document.getElementById("imgM"),document.getElementById("poiNo").value);
			});			
			
			//auto complete(자동완성)
			$("input[name='dvcCd']").on("keyup click focus", function(e){
				e.stopPropagation();
				$(this).parent().find("ul").attr("class","input_autocomplete");
				getAutoCompleteList($(this).val());
			});
			$("div.modal-content").on("click", function(){				
				hideAutoCompleteLayer();
			});
		}
	}, "json"); //$.post("/adm/poi/poiView.json"
}

// POI 등록 및 수정시 대표이미지 삭제처리하는 함수
function fnDeletePoiImg(targetDom, previewDom, poiNo) {
	var para = document.location.href.split("/adm")[0];
	if (previewDom.src == para + '/resources/img/noPhoto.png') {
		alert("기존 이미지가 없습니다.");
		return false;
	} else if (confirm("이미지 삭제시 복구가 불가능합니다. 삭제하시겠습니까?")) {
		if (poiNo != 0) {
			fnDeletePreImg(targetDom, previewDom, '/adm/poi/poiImgRemove.json',{'poiNo' : poiNo});
		}
		fn_noPhoto(previewDom);
		targetDom.value = '';
	}
}

//자동완성 레이어 띄우는 함수
function getAutoCompleteList(keyword){
	//키워드 입력되어있을때만
	if (keyword != "") {
		var params = {
				"searchType" : "autoComplete",
				"searchKeyword" : keyword,
				"pageSize" : 0,
				"sortBy" : "dvc_cd",
				"sortType" : "ASC"
			};
	}
}

//자동완성 레이어 숨기는 함수
function hideAutoCompleteLayer(){
	$("ul.input_autocomplete").parent().css("display","none");
	$("ul.input_autocomplete").removeAttr("class");
}

//자동완성 레이어에서 객체코드 선택시 input에 그 객체코드 입력되게하는 함수
function dvcCdAutoComplete(input){
	var target = input.closest("form").find("input[name='dvcCd']");
	target.val(input.find("a").text());
	hideAutoCompleteLayer();
}

