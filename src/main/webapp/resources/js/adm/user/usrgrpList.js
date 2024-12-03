$(function() {

	$("#treeview").hummingbird();

	getUsrgrpInfoList(1);

	// 엑셀 다운로드할 항목 체크창 띄우기
	$("#btnChkDown").click(function() {
		$(":checkbox[name='dw']").prop("checked", true);
		$("#downModal").modal('show');
	});
	// 엑셀 다운로드
	$("#btnDownload").click(function() {
		$("#searchKeywordEd").val($("#searchKeyword").val());
		$("#searchTypeEd").val($("#searchType").val());
		$("#grpTypeEd").val($("#grpType").val());
		$("#fieldNmList").val(arrToString($(":checkbox.seqChk[name='dw']:checked")));
		$("#usrGrpListDownFrm").attr("action","/adm/user/usrgrpExceldownload.do");
		$("#usrGrpListDownFrm").submit();
		$("#downModal").modal("hide");
	});


	// 사용자그룹 검색
	$("#schFrm").submit(function() {
		getUsrgrpInfoList(1);
		return false;
	});

	// 그룹타입 검색
	$("#grpType").change(function() {
		$("#schFrm").submit();
	});

	// 사용자그룹 등록 모달 레이어 호출
	$("#btnUsrForm").click(function() {
		$('#usrgrpRegistModal').modal('show');
		$("#usrgrpRegistFrm")[0].reset();
	});

	// 사용자그룹 등록
	$("#btnUsrRegist").click(function() {
		$("#usrgrpRegistFrm").submit();
	});
	$("#usrgrpRegistFrm").ajaxForm({
		url : '/adm/user/usrgrpRegist.json',
		type : 'post',
		dataType : 'json',
		beforeSubmit : function(data, frm, opt) {
			// 유효성 체크
			if (!jValidationFrm(frm))
				return false;
			// 그룹타입이 관리자가 아닌 경우 IP체크
			// if($("select[name='grpType']", frm).val() != "1") {
			// if(!formStrChk("ip", $("input[name='grpIp']", frm))) return
			// false;
			// }
		},
		success : function(res) {
			if (jResult(res, true)) {
				$('#usrgrpRegistModal').modal('hide');
				getUsrgrpInfoList(1);
			}
		}
	});

	// 사용자그룹 수정 처리
	$("#btnUsrModify").click(function() {
		$("#usrgrpModifyFrm").submit();
	});
	
	$("#usrgrpModifyFrm").ajaxForm(
			{
				url : '/adm/user/usrgrpModify.json',
				type : 'post',
				dataType : 'json',
				beforeSubmit : function(data, frm, opt) {
					// 유효성 체크
					if (!jValidationFrm(frm))
						return false;
					// 그룹타입이 관리자가 아닌 경우 IP체크
					// if($("select[name='grpType']", frm).val() != "1") {
					// if(!formStrChk("ip", $("input[name='grpIp']", frm)))
					// return false;
					// }
				},
				success : function(res) {
					if (jResult(res, true)) {
						$('#usrgrpModifyModal').modal('hide');
						var page = $(".pagination").find("li.active").children(".page-link").text();
						getUsrgrpInfoList(page);
					}
				}
			});

	// 체크 삭제
	$("#btnUsrDel").click(function() {
		if ($(":checkbox[name='grpNo']:checked").length < 1) {
			alert("체크된 항목이 없습니다.");
			return false;
		} else {
			if (confirm("선택된 항목을 삭제하시겠습니까?")) {
				var grpNo = [];
				$(":checkbox[name='grpNo']:checked").each(function(i) {
					grpNo.push($(this).val());
				});

				$.post("/adm/user/usrgrpRemove.json", {"grpNoArr" : grpNo}, function(res) {
					if (jResult(res, true)) {
						var page = $(".pagination").find("li.active").children(".page-link").text();
						getUsrgrpInfoList(page);
						$(".chkall").prop("checked", false);
					}
				}, "json");
			}
		}
	});


	// 도면 권한 관리 저장
	$("#btnUsrgrpMapSave").click(function() {
		// 트리 체크 항목 처리
		if ($(":checkbox[name=mapNo]:checked","#usrgrpRoleModifyFrm").length == 0) {
			alert("체크된 항목이 없습니다.");
			return false;
		}

		// 일반 그룹 도면 1개 선택만 가능하게
		/*
		 * if($("input[name='grpType']",
		 * "#usrgrpRoleModifyFrm").val() == "5") {
		 * if($(":checkbox[name=mapNo]:checked",
		 * "#usrgrpRoleModifyFrm").length > 1) { alert("일반 그룹은
		 * 1개 도면만 선택 가능합니다."); return false; } }
		 */

		var chkMapNo = [];
		$(":checkbox[name=mapNo]:checked","#usrgrpRoleModifyFrm").each(function() {
			chkMapNo.push($(this).val());
		});

		var grpNo = $("input[name='grpNo']","#usrgrpRoleModifyFrm").val();
		$.post("/adm/user/usrgrpMapRegist.json", {"grpNo" : grpNo,"mapNo" : chkMapNo}, function(res) {
			if (jResult(res, true)) {
			}
		}, "json");
	});

	// poi 권한 관리 저장
	$("#btnUsrgrpPoiSave").click(function() {
		// 체크 항목 처리
		/*
		 * if($(":checkbox[name=poiRoleType1]:checked",
		 * "#usrgrpRoleModifyFrm").length == 0) { alert("체크된 항목이
		 * 없습니다."); return false; }
		 */
		var poiRoleType1 = [];
		$(":checkbox[name=poiRoleType1]:checked","#usrgrpRoleModifyFrm").each(function() {
			poiRoleType1.push($(this).val());
		});
		var poiRoleType2 = [];
		$(":checkbox[name=poiRoleType2]:checked","#usrgrpRoleModifyFrm").each(function() {
			poiRoleType2.push($(this).val());
		});

		var grpNo = $("input[name='grpNo']", "#usrgrpRoleModifyFrm").val();
		$.post("/adm/user/usrgrpPoiRegist.json", {
			"grpNo" : grpNo,
			"poiRoleType1" : poiRoleType1,
			"poiRoleType2" : poiRoleType2
		}, function(res) {
			if (jResult(res, true)) {
			}
		}, "json");
	});

	// 메뉴 권한 관리 저장
	$("#btnUsrgrpMenuSave").click(function() {// 체크 항목 처리
		/*
		 * if($(":checkbox[name=allowUrl]:checked",
		 * "#usrgrpRoleModifyFrm").length == 0) { alert("체크된 항목이
		 * 없습니다."); return false; }
		 */
		var allowUrl = [];
		$(":checkbox[name=allowUrl]:checked", "#usrgrpRoleModifyFrm").each(function() {
			allowUrl.push($(this).val());
		});

		var grpNo = $("input[name='grpNo']", "#usrgrpRoleModifyFrm").val();
		$.post("/adm/user/usrgrpMenuRegist.json", {"grpNo" : grpNo,"allowUrl" : allowUrl}, function(res) {
			if (jResult(res, true)) {
			}
		}, "json");
	});

	// 권한 전체 체크박스
	$(".chkall").click(function() {
		var checked = $(this).is(":checked");
		var attNm = $(this).attr("name").replace("All", "");
		$(":checkbox[name='" + attNm + "']").prop("checked", checked);
	});

	//리스트 정렬
	sortTableList(getUsrgrpInfoList);
});

// 사용자그룹 목록 호출
function getUsrgrpInfoList(page) {
	var searchType = $("#searchType").val();
	var searchKeyword = $("#searchKeyword").val();
	var grpType = $("#grpType").val();
	var sortBy = $("#sortBy").val();
	var sortType = $("#sortType").val();
	
	var params = {
		"page" : page,
		"searchType" : searchType,
		"searchKeyword" : searchKeyword,
		"grpType" : grpType,
		"sortBy" : sortBy,
		"sortType" : sortType
	};
	// 리스트 호출
	ajaxTemplate("/adm/user/usrgrpList.json", params, $("#usrgrpInfoListTpl").html(), $("#usrgrpInfoList"), pagination, '');
}

// 페이징 처리 함수
function pagination(res) {
	if (res.total == 0) 	$("#usrgrpInfoList").html("<tr><td colspan='8' class='text-center'>" + _SEARCH_NOT_EXIST+ "</td></tr>");
	$(".pagination").html(pageNavigator(res.usrgrpInfo.page, res.total,res.usrgrpInfo.pageSize, 10, "getUsrgrpInfoList"));
}

// 사용자그룹 삭제
function deleteUsrgrpInfo(grpNo) {
	if (confirm("선택된 항목을 삭제하시겠습니까?")) {
		$.post("/adm/user/usrgrpRemove.json", {"grpNo" : grpNo}, function(res) {
			if (jResult(res, true)) {
				var page = $(".pagination").find("li.active").children(".page-link").text();
				getUsrgrpInfoList(page);
			}
		}, "json");
	}
}

// 사용자그룹 수정폼
function modifyUsrgrpInfo(grpNo) {
	$('#usrgrpModifyModal').modal('show');

	$.post("/adm/user/usrgrpView.json", {"grpNo" : grpNo},
		function(res) {
			if (jResult(res)) {
				var result = res.result;
				var resHtml = templateReplacer(result, $("#usrgrpInfoFrmTpl").html());
				$("#usrgrpModifyFrm").html(resHtml);
				$("select[name='grpType']", "#usrgrpModifyFrm").val(result.grpType);
			}
		}, "json");
}


// 사용자그룹 권한 수정폼
function modifyUsrgrpRole(grpNo, grpType) {
	$('#usrgrpRoleModifyModal').modal('show');
	$("input[name='grpNo']", "#usrgrpRoleModifyFrm").val(grpNo);
	$("input[name='grpType']", "#usrgrpRoleModifyFrm").val(grpType);
	// 모든 체크박스 초기화
	$("#usrgrpRoleModifyFrm").find(":checkbox").prop("checked", false);

	// 권한 데이터
	$.post("/adm/user/usrgrpRole.json", {"grpNo" : grpNo}, function(res) {
		if (jResult(res)) {
			// 도면 권한
			$.each(res.usrgrpMapList, function(idx, item) {
				$(":checkbox[name='mapNo'][value='" + item.mapNo + "']","#usrgrpRoleModifyFrm").prop("checked", true);
			});

			// POI 권한
			$.each(res.usrgrpPoiList, function(idx, item) {
				$(":checkbox[name='poiRoleType" + item.roleType+ "'][value='" + item.category1No + "']","#usrgrpRoleModifyFrm").prop("checked", true);
			});

			// 메뉴 권한
			$.each(res.usrgrpMenuList, function(idx, item) {
				$(":checkbox[name='allowUrl'][value='" + item.allowUrl + "']","#usrgrpRoleModifyFrm").prop("checked", true);
			});
		}
	}, "json");

	// 장비 권한

	// 메뉴 권한

}
