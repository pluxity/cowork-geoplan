$(function() {
	getPoiIconsetList();	//최초 로딩

	//엑셀 다운로드할 항목 모달 띄우기
	$("#btnChkDown").click(function(){
		$(":checkbox[name='dw']").prop("checked",true);
		$("#downModal").modal('show');
	});
	//엑셀 다운로드
	$("#btnDownload").click(function(){
		$("#searchKeywordEd").val($("#schIconsetNm").val());

		$("#fieldNmList").val(arrToString($(":checkbox.seqChk[name='dw']:checked")));
		$("#iconListDownFrm").attr("action", "/adm/poi/iconExceldownload.do");
		$("#iconListDownFrm").submit();
		$("#downModal").modal("hide");
	});


	$('.color-picker-background').colorpicker({	//모달창 colorpicker
    	}).on('colorpickerChange', function(event) {
    		$(this).parent().css('background-color', event.color.toString());
      });


    $('#btnPoiIconSubmit').on("click",function(){	//모달창 submit
    	$('#poiIconFrm').submit();
    });

    $('#schBtn').on("click", function(){	//검색버튼 event
    	getPoiIconsetList();
    });

    $('#createPoiIcon').on("click", function(){
    	insertForm();
    });

    $('#deletePOIIconList').on("click", function(){
    	var iconsetList = [];
    	$('.seqChk:checked').each(function(i,v){
    		iconsetList.push( {iconsetNo : parseInt(v.parentNode.parentNode.dataset.iconsetNo) } );
    	});

    	//console.log(iconsetList);

    	deleteIconset({iconsetList : iconsetList});

    })

    $(document).on("click",".modifyPOIIcon",function(){
    	var iconsetNo = this.parentNode.parentNode.dataset.iconsetNo;
    	updateForm(iconsetNo);
    });

    $(document).on("click",".deletePOIIcon",function(){
    	var iconsetNo = this.parentNode.parentNode.dataset.iconsetNo;
    	deleteIconset({iconsetNo : iconsetNo});
    });


    $("#poiIconFrm").ajaxForm({
        type: 'post',
        dataType : 'json',
        beforeSubmit: function (data, frm, opt) {
        	validationByType();	//단계별 아이콘 종류에 따른 data-rule재정렬
        	if(!jValidationFrm(frm)) return false;
        	},
          success: function(res) {
          	$('.modal').modal('hide');
          	//////////////////////업데이트 시에는 페이지 그대로 있고, 새로 등록 시에는 첫 페이지로 이동하기 위해/////////////////////
          	if (res.update != null) {
          		var page = $(".pagination").find("li.active").children(".page-link").text();
			}
          	getPoiIconsetList(page);
          }
	  });

	// 색상코드 표시
	$(document).on("change", "input[type='color']", function() {
		let color = $(this).val()
		$(this).next().text(color);
	});

    //리스트 정렬
    sortTableList(getPoiIconsetList);
});


/* $function 종료 */


/**
 * @param param
 * @returns
 */
function getPoiIconsetList (page) {

	var param = {
//			iconsetNm : $('#schIconsetNm').val(),
			searchKeyword : $('#schIconsetNm').val(),
			page : page,
			"sortBy" : $("#sortBy").val(),
			"sortType" : $("#sortType").val()

	};

	ajaxTemplate('/adm/poi/poiIconList.json', param, $('#POIIconListTpl').html(), $('#poiIconList'), pagination);
}


/**
 * iconset삭제
 * @param param
 * @returns
 */
function deleteIconset(param){

	if(param === undefined) return;
	$.ajax({
	    method: "POST",
	    url: '/adm/poi/poiIconRemove.json',
	    dataType : 'json',
	    contentType: 'application/json; charset=utf-8',
	    data: JSON.stringify(param),
	    beforeSend : function(xhr, opts) {
	        if (!confirm("삭제하시겠습니까?")) {
	            xhr.abort();
	        }
	    },
	    success: function(res) {
	    	//console.log(res);
	     	//////////////////////삭제 후에도 현재 페이지에 남아있기 위해/////////////////////
	    	var page = $(".pagination").find("li.active").children(".page-link").text();
	      	getPoiIconsetList(page);
	      }
	});
}

/**
 * 이미지 로딩 실패시 띄울 디폴트 이미지
 * @param obj
 * @returns
 */
function fn_noPhoto(obj) {
	obj.src = '/resources/img/noPhoto.png';
}

function paging(){
	pageNavigator(currPage, totalCount, pageRow, pageBlock, fnPaging);
}

function updateForm (iconsetNo) {

	function callback(res) {
		$("input[name=iconsetType][value='" + res.result.iconsetType + "']").prop('checked', true);
		//setDivColor();
	}

	ajaxTemplate('/adm/poi/poiIconView.json', {iconsetNo : iconsetNo}, $('#POIIconModalTpl').html(), $('#poiIconFrm'), callback);
	$('.modal-title').text('아이콘 수정');
	$('#btnPoiIconSubmit').text("수정");

	$("#poiIconFrm").attr("action", '/adm/poi/poiIconModify.json');
	$('#modalType').val('modify');
	$('#poiCategoryIconModal').modal('show');
}

function insertForm () {
	var defaultForm = {
			iconsetNo : '',
			iconsetNm : '',
			iconsetDesc : '',
			iconset2d0FilePath : '/resources/img/noPhoto.png',
			iconset2d1FilePath : '/resources/img/noPhoto.png',
			iconset2d2FilePath : '/resources/img/noPhoto.png',
			iconset2d3FilePath : '/resources/img/noPhoto.png',
			iconset2d4FilePath : '/resources/img/noPhoto.png',
			iconset3dThumbFilePath : '/resources/img/noPhoto.png',
			iconset3dColor1: '#fe451a',
			iconset3dColor2: '#ea7b22',
			iconset3dColor3: '#f0bb05',
			iconset3dColor4: '#a0d218'
	}

	var html = templateReplacer(defaultForm, $('#POIIconModalTpl').html());
	$('#poiIconFrm').html(html);
//	setDivColor();
	$('.modal-title').text('아이콘 등록');
	$('#btnPoiIconSubmit').text("등록");
	$("#poiIconFrm").attr("action", '/adm/poi/poiIconRegist.json');

	$('#poiIconFrm img').hide();
	$('#modalType').val('regist');
	$('#poiCategoryIconModal').modal('show');
}


/**
 * 아이콘 타입에 따른validation
 * @returns
 */
function validationByType () {
	var iconsetType = $('[name=iconsetType]:checked').val();

	dataset_3d = $('#poi3d')[0].dataset;
	dataset_2d = $('#poi2d_0')[0].dataset;

	dataset_2d.rules = dataset_2d.rules.replace(/require/g,'');
	dataset_3d.rules = dataset_3d.rules.replace(/require/g,'');

	var modalType = $('#modalType').val();


	if(iconsetType === '2D') {
		dataset_2d.rules += ' require';
	}
	if(iconsetType === '3D') {
		dataset_3d.rules += ' require';
	}
	if(iconsetType === '2D3D') {
		dataset_2d.rules += ' require';
		dataset_3d.rules += ' require';
	}

	if(modalType === 'modify'){
		if(!(/noPhoto/.test($('#poi2d_0').siblings()[0].src))) {
			dataset_2d.rules = dataset_2d.rules.replace(/require/g,'');
		}
		if(!(/noPhoto/.test($('#poi3d').siblings()[0].src))) {
			dataset_3d.rules = dataset_3d.rules.replace(/require/g,'');
		}
	}
}

//페이징 처리 함수
function pagination(res) {
	if(res.total == 0) $("#poiIconList").html("<tr><td colspan='9' class='text-center'>"+_SEARCH_NOT_EXIST+"</td></tr>");
	$(".pagination").html(pageNavigator(res.poiIconset.page, res.total, res.poiIconset.pageSize, 10, "getPoiIconsetList"));
}