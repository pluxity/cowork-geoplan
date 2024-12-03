$(function() {
	addValidationEvents ();
});

function addValidationEvents () {	//최초

	var nodes = document.querySelectorAll('[data-events]');

	for(var i = 0, iLen = nodes.length; i < iLen; ++i){

		var node = nodes[i];
		var events = node.dataset.events.match(/\w+/g);
		if(events === null) continue;

		for (var j = 0, jLen = events.length; j < jLen; ++j){

			var eventTypes = validation.events[events[j]];
			if(eventTypes === undefined) continue;	//events 리스트 예외처리

			for(key in eventTypes){
				//console.log(node, key, eventTypes[key]);
				node.addEventListener(key,eventTypes[key], false);
			}
		}
	}
}

function jValidationFrm(form){

	if(form.jquery !== undefined) form = form[0];	//Jquery 제거

	var elements = form.elements;

	for(var i = 0, iLen = elements.length; i < iLen; ++i){

		if(!jValidationDom(elements[i])) return false
	}

	return true;
}

function jValidationDom(dom){

	if(dom.jquery !== undefined) dom = dom[0];	//Jquery 제거
	if(dom.dataset.rules === undefined) return true;	//validation 하지 않는다면 무조건 true 반환

	var validationTypeArr = dom.dataset.rules.match(/\w+/g);	//validation 여러개 체크

	for(var i = 0, iLen = validationTypeArr.length; i < iLen; ++i){
		var validationType = validationTypeArr[i];
		if (validation.rules[validationType] === undefined) console.log(validationType + " :: wrong validation Type...");
		if (!validation.rules[validationType](dom)) {
			//console.log(dom);
			return false;
		}
	}
	return true;
}

var validation = {};
validation.rules = {};
validation.events = {};

//validation 통과 : return true; 실패 : return false; 로 반환할것
//validation.rules.name 중 name을 해당 dom에 data-rules='name' 이런식으로 기재할것
//여러개의 validation 을 체크하고 싶다면 data-rules='name1 name2, name3' 등등 글자가 아닌것으로 구분할것

//validation 추가하는법
//validation.rules.name = function (obj){ .... } 형식으로 기재. 해당 name을 data-rules='name' 에 기재후 사용

//<--- validation.rules --->
validation.rules.require = function(obj){
	var tag = $(obj).prop("tagName").toLowerCase();
	var type = $(obj).prop("type").toLowerCase();
	if(tag == 'input' && type == 'radio'){
		var objName = $(obj).attr('name');
		if($('input[name='+objName+']:checked').length == 0) {
			alert($(obj).attr('title')+'는(은) 필수 항목입니다. ');
			obj.focus();
			return false;
		}
	}else if(tag == 'input' && type == 'checkbox'){
		var grpName = $(obj).attr('group');
		if($('input[group='+grpName+']:checked').length == 0) {
			alert(+grpName+'는(은) 필수 항목입니다. ');
			obj.focus();
			return false;
		}
	}else{
		if(!obj || !$(obj).val() || $(obj).val() == '*') {
			alert($(obj).attr('title')+'는(은) 필수 항목입니다. ');
			obj.focus();
			return false;
		}
	}
	return true;
}

validation.rules.maxLimit = function(obj){
	if(parseFloat($(obj).val()) > parseFloat($(obj).attr('maxNum'))) {
		alert('"'+$(obj).attr('title')+'"는(은) '+$(obj).attr('maxNum')+'보다 클 수 없습니다');
		obj.focus();
		return false;
	}
	return true;

}

validation.rules.minLimit = function(obj){
	if(parseFloat($(obj).val()) < parseFloat($(this).attr('minNum'))) {
		alert('"'+$(obj).attr('title')+'"는(은) '+$(obj).attr('minNum')+'보다 작을 수 없습니다');
		obj.focus();
		return false;
	}
	return true;
}

validation.rules.number = function(obj){
	var rgx=/^[0-9]/i;
	var num = $(obj).val().split(',').join('');
	if(!rgx.test(num) && num != "") {
		alert("숫자만 입력되어야 합니다.\n");
		obj.focus();
		return false;
	}
	return true;
}

validation.rules.Integer = function(obj){
	var rgx=/^[0-9-]/i;
	var num = $(obj).val().split(',').join('');
	if(!rgx.test(num) && num != "") {
		alert("정수만 입력되어야 합니다.\n");
		obj.focus();
		return false;
	}
	return true;
}

validation.rules.eng = function(obj){
	//2바이트 문자 금지
	var rgx=/[\u3131-\u3163\uac00-\ud7a3]/i;
	var eng = $(obj).val();
	if(rgx.test(eng)) {
		alert("한글은 입력할 수 없습니다.");
		obj.focus();
		return false;
	}
	return true;
}

validation.rules.file = function(obj) {

	var allowExt = $(obj).data('ext').match(/\w+/g);

	var fileChk = 0;
	$.each($(obj), function() {
		if($(this).val() != "") {
			var ext = $(this).val().split('.').pop().toLowerCase();
			if($.inArray(ext, allowExt) == -1) {
				fileChk++;
			}
		}
	});

	if(fileChk > 0) {
		var allowExtTxt = "";
		$.each(allowExt, function(v) {
			allowExtTxt += " " + allowExt[v];
		});

		alert('허용되지 않은 파일 형식입니다.\n' + allowExtTxt + '형식만 가능합니다.');
		$(obj).focus();
		return false;
	} else {
		return true;
	}
}

//<--- validation.events --->

//사용법 data-events="name" 을 기재한다. validation.events.name.keyup/click 식으로 만들어주면 해당 로직이 수행된다.
//Tip : 현재 dom 을 가져오고 싶다면. event.curretTarget 사용하면된다.

validation.events.number = {};
validation.events.currency = {};
validation.events.engOnly = {};
validation.events.textarea = {};

validation.events.number.keydown = function(event){
	var key;

	if(window.event)
	     key = window.event.keyCode; //IE
	else
	     key = event.which; //firefox

	// backspace or delete or tab or dot
	var event;
	if (key == 0 || key == 8 || key == 46 || key == 9 || key == 190 || key == 110 || key == 109 || key == 189){
	    event = e || window.event;
	    if (typeof event.stopPropagation != "undefined") {
	        event.stopPropagation();
	    } else {
	        event.cancelBubble = true;
	    }
	    return ;
	}

	if (key < 48 || (key > 57 && key < 96) || key > 105 || event.shiftKey) {
		event.preventDefault ? event.preventDefault() : event.returnValue = false;
	}


}
validation.events.number.keyup = function(event){
	var dom = event.currentTarget;
	var str = $(dom).val().replace(/[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/g, '');
	$(dom).val(str);
}
validation.events.currency.keyup = function(event){
	var dom = event.currentTarget;
	var num = $(dom).val().split(',').join('');
	$(this).val(num.replace(/\B(?=(\d{3})+(?!\d))/g, ","));

}
validation.events.engOnly.keydown = function(event){
	var dom = event.currentTarget;
    var key;

    if(window.event)
         key = window.event.keyCode; //IE
    else
         key = event.which; //firefox

    // backspace or delete or tab or dot
    var event;
    if (key == 0 || key == 8 || key == 46 || key == 9 || key == 190 || key == 110){

        event = e || window.event;
        if (typeof event.stopPropagation != "undefined") {
            event.stopPropagation();
        } else {
            event.cancelBubble = true;
        }
        return ;
    }

    if (key == 229) {
    	alert('한글은 입력되지 않습니다.\n한/영 자판키도 입력되지 않습니다.다른 곳에서 자판 변경 후 입력하세요!');
    	event.preventDefault ? event.preventDefault() : event.returnValue = false;
    }
}
validation.events.textarea.keyup = function(event){

	var dom = event.currentTarget;
	var limit = $(dom).attr('limit');
	var charLoc = stringByteLength(dom.value, limit);	//삭제 위치
	if(charLoc > 0){
		$(dom).val($(dom).val().substring(0,charLoc));
		alert(limit+'자를 넘을 수 없습니다');
	}
}



