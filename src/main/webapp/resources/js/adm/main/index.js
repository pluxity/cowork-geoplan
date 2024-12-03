$(function() {

	// 비밀번호 변경 모달 레이어 호출
	$("#btnUsrPwdFrm").click(function() {
		$('#usrPwdModal').modal('show');
		$("#usrPwdFrm")[0].reset();
	});

	// 비밀번호 변경
	$("#btnModifyUsrPwd").click(function() {
		$("#usrPwdFrm").submit();
	});
	$("#usrPwdFrm").ajaxForm({
		url: '/adm/main/usrPwdModify.json',
    	type: 'post',
    	dataType : 'json',
    	beforeSubmit: function (data, frm, opt) {
    		// 유효성 체크
    		if(!jValidationFrm(frm)) return false;
    		if(!formStrChk("passwd", $("#usrPwd"))) return false;
			if(!formDiffChk($("#usrPwd"), $("#reUsrPwd"), "비밀번호가 일치하지 않습니다.")) return false;
		},
        success: function(res) {
        	if(jResult(res, true)) {
        		$('#usrPwdModal').modal('hide');
        	}
        }
    });

});
