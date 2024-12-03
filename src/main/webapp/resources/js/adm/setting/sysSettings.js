$(function() {
	$("#sysSettingsFrm").ajaxForm({
		url : '/adm/setting/sysSettingsModify.json',
		type : 'post',
		dataType : 'json',
		beforeSubmit : function(data, frm, opt) {
			// 유효성 체크
			if (!jValidationFrm(frm))
				return false;
		},
		success : function(res) {
			jResult(res, true)
		}
	});
});



//input type='file'에 audio 파일 선택시 audio 태그의 src를 바꿔주는 함수
//단, audio 태그의 id가 input의 id에 'Ad'를 붙인 값이어야 함
function handleFiles(event) {
	var files = event.target.files;
//	$("#"+event.target.id+"Ad").attr("src", URL.createObjectURL(files[0]));
	document.getElementById(event.target.id+"Ad").src = URL.createObjectURL(files[0]);
	document.getElementById(event.target.id+"Ad").load();
	$("label[for='"+event.target.id+"']").text(files[0].name);
}


