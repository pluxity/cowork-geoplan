$(function() {

	// 달력 설정
	$(".datepicker").datepicker({
		dateFormat: 'yy-mm-dd',
		monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
		dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
		changeMonth: true,
		changeYear: true,
		showMonthAfterYear: true,
		isRTL: false,
		showButtonPanel: true,
		closeText: '완료',
		currentText: 'Now',
		//showOn: "both",
		//buttonImage: "/resources/images/main/btn_cal.png",
		//buttonImageOnly: true,
		//buttonText: "달력",
		onClose: function (selectedDate) {
			if ($(this).attr("name") == "sdate") {
				$("input[name='edate']").datepicker("option", "minDate", selectedDate);
			}
		}
	});

	$(".monthpicker").monthpicker({
		pattern: 'yyyy-mm',
		monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
	})

	// 달력 입력값 고정
	$.each($('.datepicker'), function () {
		if ($(this).val().length == 8) {
			var day = $(this).val().substring(0, 4) + "-" + $(this).val().substring(4, 6) + "-" + $(this).val().substring(6, 8);
			$(this).val(day);
		}
	});


	// 시간선택 달력
	$(".datetimepicker").datetimepicker({
		dateFormat: 'yy-mm-dd',
		monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
		dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
		changeMonth: true,
		changeYear: true,
		showMonthAfterYear: true,
		// timepicker 설정
		timeFormat: 'HH:mm',
		controlType: 'select',
		oneLine: true,
		showOn: "both",
		buttonImage: "/resources/images/main/btn_cal.png",
		buttonImageOnly: true,
		buttonText: "달력",
		onClose: function (selectedDate) {
			if ($(this).attr("id") == "sdate") {
				$("#edate").datepicker("option", "minDate", selectedDate);
				//$("#edate").datepicker( "option", "maxDate", setDate(selectedDate,+3));
			}
		}
	});

	$.each($('.datetimepicker'), function () {
		if ($(this).val().length == 12) {
			var day = $(this).val().substring(0, 4) + "-" + $(this).val().substring(4, 6) + "-" + $(this).val().substring(6, 8);
			var time = $(this).val().substring(8, 10) + ":" + $(this).val().substring(10, 12);
			$(this).val(day + " " + time);
		}
	});

	// 체크박스 모두 체크 처리
	$(document).on("click", ".chkall", function() {
		if ($(this).prop("checked") == true) {
			$(this).closest("table").find(".seqChk").prop("checked", true);
		} else {
			$(this).closest("table").find(".seqChk").prop("checked", false);
		}
	});

	// 체크박스 전체 선택 해제 및 전체 선택
	$(document).on("click", ".seqChk", function() {
		if($(this).prop("checked") == true) {
			//전체 선택 되었을 경우 전체 선택란도 체크되게
			if ($(this).closest("table").find( "input:checked[class='seqChk']" ).length == $(this).closest("table").find(".seqChk").length) {
			//if ($( "input:checked" ).length == $(".seqChk").length) {
			//주요감시그룹 관리에서 주요 감시 그룹에 POI가 등록되어있는 경우 전체선택이 안먹는 문제 때문에 수정
				$(this).closest("table").find(".chkall").prop("checked",true);
			}
		} else {
			//전체 선택 체크가 해제되게
			$(this).closest("table").find(".chkall").prop("checked",false);
		}
	});

	$.fn.serializeObject = function() {
		"use strict"
		var result = {}
		var extend = function(i, element) {
			var node = result[element.name]
		    if ("undefined" !== typeof node && node !== null) {
		    	if ($.isArray(node)) {
		    		node.push(element.value)
		    	} else {
		    		result[element.name] = [node, element.value]
		    	}
		    } else {
		    	result[element.name] = element.value
		    }
		}
		$.each(this.serializeArray(), extend)
		return result
	}
});

/**
 * 각 목록에서 sorting 하기 위한 함수
 * @param func - 페이지 처리 함수
 * @returns
 */
function sortTableList(func){
	//정렬할 항목에 화살표 아이콘(?) append
	$("table.sort-list").find("th[data-sort-attr]").append('<i class="fas fa-angle-down" title="정렬">').css("cursor", "pointer");
	//on click 이벤트 주기
	$("table.sort-list").find("th[data-sort-attr]").on("click", function(){
		$("table.sort-list").find("i.double").attr("class", "fas fa-angle-down");
		if ($("#sortBy").val() == $(this).data("sort-attr")) {
			if ($("#sortType").val() == "DESC") {
				$(this).find("i").remove();
				$(this).append(' <i class="fas fa-angle-double-up double"></i>');
				$("#sortType").val("ASC");
			} else {
				$(this).find("i").remove();
				$(this).append(' <i class="fas fa-angle-double-down double"></i>');
				$("#sortType").val("DESC");
			}
		} else {
			$("#sortBy").val($(this).data("sort-attr"));
			$(this).find("i").remove();
			$(this).append(' <i class="fas fa-angle-double-down double"></i>');
			$("#sortType").val("DESC");
		}
		if (typeof (func) === 'function') {
			func(1);
		}
	});
}

/**
 * 쿠키 저장
 *
 * @param name
 * @param value
 * @param days
 */
function setCookie(name, value, days) {
	if (days) {
		var date = new Date();
		date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
		var expires = "; expires=" + date.toGMTString();
	} else {
		var expires = "";
	}
	document.cookie = name + "=" + value + expires + "; path=/";
}

/**
 * 쿠키 조회
 *
 * @param name
 * @returns
 */
function getCookie(name) {
	var i, x, y, ARRcookies = document.cookie.split(";");
	for (i = 0; i < ARRcookies.length; i++) {
		x = ARRcookies[i].substr(0, ARRcookies[i].indexOf("="));
		y = ARRcookies[i].substr(ARRcookies[i].indexOf("=") + 1);
		x = x.replace(/^\s+|\s+$/g, "");
		if (x == name) {
			return unescape(y);
		}
	}
}

/**
 * ajax 기본 결과 처리
 *
 * @param data
 * @param msg
 * @returns {Boolean}
 */
function jResult(data, msg) {
	if (data.resultCd == 'success') {
		if (msg)
			alert(data.resultMsg);
		return true;
	} else {
		if (data.resultMsg != "")
			alert(data.resultMsg);
		return false;
	}
}

function formEmptyChk(objname, msg) {
	if (!$(objname).val() || $(objname).val() == "") {
		if (msg != "" && typeof msg != "undefined") {
			alert(msg);
		} else {
			var objNm = $(objname).attr("title");
			alert(objNm + "을(를) 입력하세요.");
		}
		$(objname).focus();
		return false;
	} else {
		return true;
	}
}

/* 사이즈 범위 체크 */
function formRangeChk(objname, min, max, msg) {
	if ($(objname).val().length < min || $(objname).val().length > max) {
		alert(msg);
		$(objname).focus();
		return false;
	} else {
		return true;
	}
}

/* 값 비교 */
function formDiffChk(obj1, obj2, msg) {
	if (obj1.val() != obj2.val()) {
		alert(msg);
		obj1.val("");
		obj2.val("");
		obj1.focus();
		return false;
	} else {
		return true;
	}
}

/* 주요 요소 체크 */
function formStrChk(mode, obj) {
	var rep;
	var msg;

	if (mode == "id") {
		rep = /^[a-z]+[a-z0-9]{5,12}$/g;
		msg = "아이디는 6~12자리 영문소문자로 시작하는 영문,숫자 조합으로 입력하세요.";
	} else if (mode == "email") {
		rep = /^[-A-Za-z0-9_]+[-A-Za-z0-9_.]*[@]{1}[-A-Za-z0-9_]+[-A-Za-z0-9_.]*[.]{1}[A-Za-z]{2,5}$/;
		msg = "이메일을 바르게 입력하세요.";
	} else if (mode == "passwd") {
		rep = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,16}$/;
		msg = "비밀번호는 8~16자리 영문,숫자,특수문자 조합으로 입력하세요.";
	} else if (mode == "ip") {
		rep = /^(1|2)?\d?\d([.](1|2)?\d?\d){3}$/;
		msg = "IP주소를 바르게 입력하세요.";
	}

	if (!rep.test(obj.val())) {
		alert(msg);
		obj.val("");
		obj.focus();
		return false;
	} else {
		return true;
	}
}

/* 한글, 영문, 숫자, 아이디, 이메일 체크 */
function chkString(mode, obj) {
	var rep;
	var strTxt;
	var msg;

	if (mode == "num") {
		rep = /^[0-9]*$/;
		strTxt = "숫자";
	} else if (mode == "eng") {
		rep = /^[a-zA-Z]*$/;
		strTxt = "영문";
	} else if (mode == "kor") {
		rep = /^[ㄱ-ㅎㅏ-ㅣ가-힣]*$/;
		strTxt = "한글";
	} else if (mode == "eng_kor") {
		rep = /^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z]*$/;
		strTxt = "한글,영문";
	} else if (mode == "eng_num") {
		rep = /^[a-z0-9]*$/;
		strTxt = "영문소문자,숫자";
	}

	if (!rep.test($(objname).val())) {
		alert(strTxt + '만 입력하세요.');

		obj.val("");
		obj.focus();
		return false;
	} else {
		return true;
	}
}

/**
 * Array 로 들어온 Map 형식 데이터를 template 에서 ${key} 부분을 value 로 치환
 *
 * @param template
 *            (ex '<option value="{category1No}">{category1Nm}</option>')
 * @param datas (
 *            List<Map> 형태와 Map 형태 모두 지원함. 단, Map 안의 object 형식들은 아직 미지원)
 * @returns
 */
function templateReplacer(datas, template) {

	if (datas === undefined || template === undefined)
		return; // 파라미터값 예외처리
	var html = "";

	var isArray = Array.isArray(datas);

	function mapToHtml(data, template, keyObj, kes_) {

		// console.log(data._row);

		var res = '';
		var template_ = template;

		// 예전방식. data의 키값으로 돌렸다.

		// var keys = Object.keys(data);
		// for(var j = 0, keySize = keys.length; j < keySize; ++j){
		// var key = keys[j];
		// //var regEx = new RegExp('\\${' + key + '}','g'); // ${key} 값 치환
		// //var regEx = new RegExp('({)(\\s)*(\\b' + key + ')(\\s)*(})','g');
		// // ${key} ${ key} ${ key } 모두 가능
		// var regEx = new RegExp('{\\s*' + key + '\\s*}','g'); //정규식 간소화
		// template_ = template_.replace(regEx,data[key]);
		// }
		// res += template_;
		// return res;

		for (var j = 0, keySize = keys_.length; j < keySize; ++j) {
			var key = keys_[j];
			var regEx = new RegExp(key, 'g'); // 정규식 간소화

			var value = null;

			keyObj[key].forEach(function(k) {
				if (value === null)
					value = data[k];
				else
					value = value[k];
			});

			if (value === undefined)
				continue; // 매칭되지 않는 값일때 여기에 담아주면된다. onClick 달때 {key :
							// {value}} 넣기위해 이렇게 처리
			if (value == null)
				value = "";

			template_ = template_.replace(regEx, value);
		}
		res += template_;
		return res;
	} // mapToHTml 종료

	// 정규식 키값 미리 추출
	// 새로운 방식 template 에서 키값을 추출함
	var keys = template.match(/\{\s*[\_\w\.]+\s*\}/g); // array로 들어온다.
	var keyObj = {};

	if (!keys) { // 치환할게 아에 없는경우다.
		return template;
	}

	keys.forEach(function(key) {
		keyObj[key] = key.match(/\w+/g);
	});

	var keys_ = Object.keys(keyObj); // 최종 키값 추출

	// console.log(keys,keyObj,keys_);
	if (isArray) {

		for (var i = 0, datasSize = datas.length; i < datasSize; ++i) {
			// 순서 매길수있게 처리
			datas[i]['_row'] = {
				index : i,
				count : i + 1,
				doubleDigitCount : padStart(i + 1, 2),
				max : datasSize
			};
			// 순서 매길수있게 처리
			var template_ = template;
			html += mapToHtml(datas[i], template_, keyObj, keys_);
		}

	} else {
		html = mapToHtml(datas, template, keyObj, keys_);
	}
	return html;
}

function domTemplateReplacer(data, dom) {

	var tagDictionary = {
		input : 'value',
	}

	var template = dom.dataset.template;
	var text = templateReplacer(data, template);
	if (!text) return;

	var tagName = dom.tagName.toLowerCase();

	var value = tagDictionary[tagName];
	if (!value)		value = 'innerText';

	dom[value] = text;
}

/**
 * @param data
 * @param dom
 * @returns
 */
function nodeTreeTemplateReplacer(data, dom) {

	if (!dom)
		return;
	if (dom.length !== 1) {
		console.error('하나의 DOM 에 대해 노드트리를 순회합니다. 여러개를 사용할때는 단건 대상인 domTemplateReplacer (data, dom) 을 사용하세요');
		return;
	}

	if (dom.jquery !== undefined)
		dom = dom[0]; // jquery 제거

	domTemplateReplacer(data, dom);

	// 하위노드 모두 찾는다.
	var htmlCollection = dom.getElementsByTagName('*');

	for (var i = 0, iLen = htmlCollection.length; i < iLen; ++i) {
		var childNode = htmlCollection[i];
		domTemplateReplacer(data, childNode);
	}
}

/**
 * @param url
 *            (json 호출주소)
 * @param param
 * @param template
 *            (template String 형태로 넣을것)
 * @param dom
 *            (만들어진 돔이 들어갈곳. 바닐라 / Jquery 돔 모두 가능) //들어오지 않으면 비동기처리
 * @returns
 */
function ajaxTemplate(url, param, template, dom, callback, callbackArg) {

	var asyncFlag = true;
	if (isValEmpty(dom))
		asyncFlag = false; // targetDom이 들어오지 않으면 비동기처리
	else if (dom.jquery !== undefined)
		dom = dom[0];

	$.ajax({
		method : "POST",
		url : url,
		data : param,
		dataType : 'json',
		async : asyncFlag,
		success : function(res) {
			if (jResult(res)) {
				// console.log(res);
				var html = templateReplacer(res.result, template);
				if (!asyncFlag)
					return html; // 비동기 처리시 만들어진 html 리턴
				dom.innerHTML = html;
				if (typeof (callback) === 'function') {
					callback(res, callbackArg);
				}
			}
		}
	});
}

/**
 * Ajax 페이지번호
 *
 * @param currPage
 * @param totalCount
 * @param pageRow
 * @param pageBlock
 * @param fnPaging
 * @returns
 */
function pageNavigator(currPage, totalCount, pageRow, pageBlock, fnPaging) {
	var blockCount = Math.floor((currPage % pageBlock == 0 ? currPage - 1
			: currPage)
			/ pageBlock);
	var fBlock = blockCount * pageBlock + 1;
	var lBlock = blockCount * pageBlock + pageBlock;
	// var first = pageRow*(currPage-1);
	var last = pageRow * currPage;
	var lastPaging = Math.ceil((totalCount) / pageRow);

	if (totalCount <= last) {
		last = totalCount;
	}
	var html = "";
	if (currPage != 1) {
		// html += "";
	}
	if (currPage > pageBlock) {
		html += "<li class='page-item'><a class='page-link' href='#' onclick='"
				+ fnPaging + "(" + (fBlock - 1) + ")'>이전</a></li>";
	}

	for (fBlock; fBlock <= (lastPaging <= lBlock ? lastPaging : lBlock); fBlock++) {
		// html += "<a href='javascript:" + fnPaging + "("+fBlock+")'
		// style='text-decoration:none;' class='setPage
		// "+(fBlock==currPage?"cur":"")+"'>"+fBlock+"<a/>";

		if (fBlock == currPage)
			html += "<li class='page-item active' aria-current='page'><span class='page-link'>"
					+ fBlock + "</span></li>";
		else
			html += "<li class='page-item'><a class='page-link' href='#' onclick='"
					+ fnPaging + "(" + fBlock + ")'>" + fBlock + "</a></li>";
	}
	if (lBlock < lastPaging) {
		html += "<li class='page-item'><a class='page-link' href='#' onclick='"
				+ fnPaging + "(" + (lBlock + 1) + ")'>다음</a></li>";
	}

	if (currPage != lastPaging) {
		if (isNaN(lastPaging)) {
			// html += "";
		} else {
			// html += "";
		}
	}

	return html;
}

/**
 * url 파라미터 json 반환
 *
 * @returns
 */
function getUrlParams() {
	var params = {};
	window.location.search.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(str,
			key, value) {
		params[key] = value;
	});
	return params;
}

/**
 * 에러이미지 대체 사용법 : 해당 돔에 onerror="fn_noPhoto(this)" 달아줄것
 *
 * @param obj
 * @returns
 */
function fn_noPhoto(obj) {
	obj.src = '/resources/img/noPhoto.png';
}

/**
 * value 값이 비어있는지 확인 undefined '' null 체크.
 *
 * @param value
 * @returns
 */
function isValEmpty(val) {
	if (val === undefined || val === null || val === '')
		return true;
	return false;
}

/**
 * string.polyfill.js 를 사용할거면 str.padStart 로 사용할것. 그때는 이걸 삭제해야한다.
 *
 * @param str
 * @param width
 * @param padString
 * @returns
 */
function padStart(str, width, padString) {
	padString = padString || '0';
	str = str.toString();
	return str.length >= width ? str : new Array(width - str.length + 1)
			.join(padString)
			+ str;
}

/**
 * mobile이면 true 아니면 false
 *
 * @returns
 */
function mobileChk() {
	var check = false;
	(function(a) {
		if (/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino|android|ipad|playbook|silk/i
				.test(a)
				|| /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i
						.test(a.substr(0, 4)))
			check = true;
	})(navigator.userAgent || navigator.vendor || window.opera);
	return check;
}

/**
 * 팝업 레이어 위치 지정
 *
 * @param target
 * @param x
 * @param y
 * @returns
 */
function setLayerPos(target, x, y) {
	let sWidth = window.innerWidth;
	let sHeight = window.innerHeight;
	let oWidth = target.width();
	let oHeight = target.height();
	let divLeft = x;
	let divTop = y;
	if (divLeft + oWidth > sWidth)
		divLeft -= oWidth;
	if (divTop + oHeight > sHeight)
		divTop -= oHeight;
	if (divLeft < 0)
		divLeft = 0;
	if (divTop < 0)
		divTop = 0;

	target.css({
		"top" : divTop,
		"left" : divLeft,
		"position" : "absolute"
	});
	return target;
}

/**
 * @Descriptions Input 으로 업로드 한 이미지 파일 프리뷰 함수
 * @param targetDom -
 *            파일 업로드하는 INPUT Element
 * @param previewDom -
 *            표출하고자 하는 IMG Element
 * @returns
 */
function fnSetPreviewEvent(targetDom, previewDom) {

	targetDom.addEventListener('change', function(event) {

		var reader = new FileReader();

		reader.onload = (function(img) {
			return function(e) {
				img.src = e.target.result
			}
		})(previewDom);

		var files = event.target.files;

		if (files && files.length > 0) {
			reader.readAsDataURL(files[0]);
		} else {
			fn_noPhoto(previewDom);
		}
	});

	prevImgLayer(previewDom);
}

/**
 * 미리보기 이미지 레이어
 *
 * @param targetObj
 * @returns
 */
function prevImgLayer(targetObj) {
	targetObj
			.addEventListener(
					'mouseover',
					function(e) {
						var prevImg = "<div id='prevImg' style='position:absolute;z-index:99999;'><img src='"
								+ this.src
								+ "' class='rounded' alt='대표이미지' style='max-width:1080px;'></div>";
						$("body").append(prevImg);
						$("#prevImg").css("top", e.pageY + "px").css("left",
								e.pageX + "px").fadeIn("fast");

					});
	targetObj.addEventListener('mouseleave', function(e) {
		$("#prevImg").remove();

	});
}

/**
 * @Descriptions Input 으로 업로드 한 이미지 파일 삭제처리 함수
 * @param targetDom -
 *            파일 삭제하고자 하는 INPUT Element
 * @param previewDom -
 *            표출된 IMG Element
 * @param ajaxUrl-
 *            ajax로 보낼 url
 * @param ajaxUrl-
 *            ajax로 보낼때 필요한 param
 * @returns
 */
function fnDeletePreImg(targetDom, previewDom, ajaxUrl, jsonData) {
	if (jsonData == null) { // 혹시나 파라미터 값 없을 때 수행하지 않기 위해서
		return false;
	}
	$.ajax({
		url : ajaxUrl,
		type : 'post',
		dataType : 'json',
		data : jsonData,
		success : function(res) {
			// alert(res.resultMsg);
		}
	})
}

// 엑셀 다운로드에서 사용
// 선택된 항목을 하나의 String으로 변환(csv는 ','임)
function arrToString(target) {
	var str = [];
	target.each(function() {
		str.push($(this).val());
	});
	return str.toString();
	// var str = "";
	// var i = 0;
	// target.each(function (i) {
	// str += $(this).val();
	// if (i < target.length) {
	// str += ",";
	// i++;
	// }
	// });
	// return str;
}


/**
 * audio 파일 없을 때나 불러오지 못했을 때, src값 변경
 * @param obj : target dom
 * @param defaultAudioNm : audio파일의 실제 이름(ex: minor.mp3 , major.mp3, critical.mp3)
 */
function fnNoAudio(obj, defaultAudioNm){
	obj.src = "/resources/audio/"+defaultAudioNm;
}


/**
 * ajax FormData 로 보내기
 * @param url : ajax url
 * @param param : ajax param
 * @param callback : callback
 */
function ajaxFormData (url, param, callback) {

	var formData = new FormData();
	formData.enctype = "multipart/form-data";

	for(var [key,value] of Object.entries(param)) {
		formData.append(key, value);
	}

	return $.ajax({
		url: url,
		type: 'POST',
		dataType : 'json',
		enctype: 'multipart/form-data',
		contentType: false,
		processData: false,
		data : formData,
		success: function(res) {
			//console.log(res);
			if(callback) callback(res);
		},
		error : function(e){
			//console.log(e);
		}
	});
}

const fnChangeMouseCursor = (cursor) => {
	const body = document.querySelector('BODY');
	body.style.cursor = cursor || 'auto' ;
}