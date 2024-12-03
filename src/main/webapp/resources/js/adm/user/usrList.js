$(function () {

	getUsrInfoList(1);

	//엑셀 다운로드할 항목 체크창 띄우기
	$("#btnChkDown").click(function(){
		$(":checkbox[name='dw']").prop("checked",true);
		$("#downModal").modal('show');
	});
	//엑셀 다운로드
	$("#btnDownload").click(function(){
		$("#searchKeywordEd").val($("#searchKeyword").val());
		$("#searchTypeEd").val($("#searchType").val());

		$("#fieldNmList").val(arrToString($(":checkbox.seqChk[name='dw']:checked")));
		$("#usrListDownFrm").attr("action", "/adm/user/usrExceldownload.do");
		$("#usrListDownFrm").submit();
		$("#downModal").modal("hide");
	});

	// 사용자 검색
	$("#schFrm").submit(function () {
		getUsrInfoList(1);
		return false;
	});

	// 사용자 등록 모달 레이어 호출
	$("#btnUsrForm").click(function () {
		$('#usrRegistModal').modal('show');
		$("#usrRegistFrm")[0].reset();
	});

	// 사용자 등록
	$("#btnUsrRegist").click(function () {
		$("#usrRegistFrm").submit();
	});
	$("#usrRegistFrm").ajaxForm({
		url: '/adm/user/usrRegist.json',
		type: 'post',
		dataType: 'json',
		beforeSubmit: function (data, frm, opt) {
			// 유효성 체크
			if (!jValidationFrm(frm)) return false;
			if (!formStrChk("id", $("#regUsrId"))) return false;
			if (!formStrChk("passwd", $("#regUsrPwd"))) return false;
			if (!formDiffChk($("#regUsrPwd"), $("#reRegUsrpwd"), "비밀번호가 일치하지 않습니다.")) return false;
		},
		success: function (res) {
			if (jResult(res, true)) {
				$('#usrRegistModal').modal('hide');
				getUsrInfoList(1);
			}
		}
	});

	// 사용자 수정 처리
	$("#btnUsrModify").click(function () {
		$("#usrModifyFrm").submit();
	});
	$("#usrModifyFrm").ajaxForm({
		url: '/adm/user/usrModify.json',
		type: 'post',
		dataType: 'json',
		beforeSubmit: function (data, frm, opt) {
			// 유효성 체크
			if (!jValidationFrm(frm)) return false;
			if ($("#updUsrPwd").val() != "") {
				if (!formStrChk("passwd", $("#updUsrPwd"))) return false;
				if (!formDiffChk($("#updUsrPwd"), $("#reUpdUsrPwd"), "비밀번호가 일치하지 않습니다.")) return false;
			}
		},
		success: function (res) {
			if (jResult(res, true)) {
				$('#usrModifyModal').modal('hide');
				var page = $(".pagination").find("li.active").children(".page-link").text();
				getUsrInfoList(page);
			}
		}
	});

	// 체크 삭제
	$("#btnUsrDel").click(function () {
		if ($(":checkbox[name='usrNo']:checked").length < 1) {
			alert("체크된 항목이 없습니다.");
			return false;
		} else {
			if (confirm("선택된 항목을 삭제하시겠습니까?")) {
				var usrNo = [];
				$(":checkbox[name='usrNo']:checked").each(function (i) {
					usrNo.push($(this).val());
				});

				$.post("/adm/user/usrRemove.json", {
					"usrNoArr": usrNo
				}, function (res) {
					if (jResult(res, true)) {
						var page = $(".pagination").find("li.active").children(".page-link").text();
						getUsrInfoList(page);
						$(".chkall").prop("checked", false);
					}
				}, "json");
			}
		}
	});

	//리스트 정렬
	sortTableList(getUsrInfoList);
});


// 사용자 목록 호출
function getUsrInfoList(page) {
	var searchType = $("#searchType").val();
	var searchKeyword = $("#searchKeyword").val();
	var sortBy = $("#sortBy").val();
	var sortType = $("#sortType").val();

	var params = {
		"page": page,
		"searchType": searchType,
		"searchKeyword": searchKeyword,
		"sortBy" : sortBy,
		"sortType" : sortType
	};
	// 리스트 호출
	ajaxTemplate("/adm/user/usrList.json", params, $("#usrInfoListTpl").html(), $("#usrInfoList"), function(res) {
		pagination(res);
		chkLoginedUser();
	}, '');
}

// 페이징 처리 함수
function pagination(res) {
	if (res.total == 0) $("#usrInfoList").html("<tr><td colspan='11' class='text-center'>" + _SEARCH_NOT_EXIST + "</td></tr>");
	$(".pagination").html(pageNavigator(res.usrInfo.page, res.total, res.usrInfo.pageSize, 10, "getUsrInfoList"));
}

// 사용자 삭제
function deleteUsrInfo(usrNo) {
	if (confirm("선택된 항목을 삭제하시겠습니까?")) {
		$.post("/adm/user/usrRemove.json", {
			"usrNo": usrNo
		}, function (res) {
			if (jResult(res, true)) {
				var page = $(".pagination").find("li.active").children(".page-link").text();
				getUsrInfoList(page);
			}
		}, "json");
	}
}

// 사용자 수정
function modifyUsrInfo(usrNo) {
	$('#usrModifyModal').modal('show');

	$.post("/adm/user/usrView.json", {
		"usrNo": usrNo
	}, function (res) {
		if (jResult(res)) {
			var result = res.result;
			var resHtml = templateReplacer(result, $("#usrInfoFrmTpl").html());
			$("#usrModifyFrm").html(resHtml);
			// 사용자 그룹
			$("select[name='grpNo']", "#usrModifyFrm").val(result.grpNo);
		}
	}, "json");

}

/**
 * 로그인 사용자 체크
 * @returns
 */
function chkLoginedUser () {
	//$.post("/adm/user/getLoginedUsers.json", {}, function (res) {
	$.post("/adm/user/getLoginedUsersBySec.json", {}, function (res) {
		if (jResult(res)) {
			var users = res.loginUsrList;
			var $isLogin = $('.is-login');
			for(var dom of $isLogin) {
				var usrId = dom.dataset.usrId;
				if(users.includes(usrId)) dom.innerText = '●';
			}
			$("#loginUsrCnt").text(res.loginUsrCnt);
		}
	}, "json");
}

function refreshBrowser (usrId) {
	$.post("/adm/user/getLoginedUsers.json", {}, function (res) {
		if (jResult(res)) {
			var users = res.users;
			var $isLogin = $('.is-login');
			for(var dom of $isLogin) {
				var usrId = dom.dataset.usrId;
				if(users.includes(usrId)) dom.innerText = '●';
			}
		}
	}, "json");
}


/**
 * 사용자 강제 로그아웃
 * @param usrId
 * @returns
 */
function expireUsrSession(usrId) {
	if (confirm("로그아웃 처리하시겠습니까?")) {
		$.post("/adm/user/expireUsrSession.json", {usrId:usrId}, function (res) {
			if (jResult(res, true)) {
				var page = $(".pagination").find("li.active").children(".page-link").text();
				getUsrInfoList(page);
			}
		}, "json");
	}
}
