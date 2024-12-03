$(function() {

	loadingCategory('category1', undefined, undefined, function(){$('#category1').children().eq(0).prop('selected',true).trigger('change')}); //최초 대분류 로딩

	$('#category1').on('change', function(){	//대분류 카테고리 선택시

		if(this.options[this.selectedIndex] === undefined){
			loadingCategory('category2', {category1No : 0});
			return;
		}
		
	    var categoryNo = this.options[this.selectedIndex].value;
	    var param = {category1No : categoryNo};
	    //console.log(param);
	    loadingCategory('category2',param);
	});

    $(document).on("click","#schBtn", function(){	//검색버튼 event
    	loadingIconset();
    });

    $(document).on("click",".iconsetNo_", function(){	//검색버튼 event
    	$('#iconsetNo').val(this.value);
    });

    $('.fa-edit').on('click', function(){	//카테고리 수정 모달창

        var type = 'modify';
        var categoryObj = getCategoryObj(this);
        var category = categoryObj.id;

        if(categoryObj.selectedIndex === -1) {
          alert ('카테고리를 선택해 주세요');
          return;
        }
        var categoryNo = categoryObj.options[categoryObj.selectedIndex].value;
        var param = {
            categoryType : category,
        };
        param[category+'No'] = categoryNo;
        showCategoryPop(type, category, param);
      });

      $('.fa-plus-square').on('click', function(){	//카테고리 등록 모달창
        var type = 'regist';
        var category = getCategoryObj(this).id;
        var param = {
            categoryType : category,
            category1No : $('#category1').val(),
            category2No : $('#category2').val(),
        }
        //param[categoryType] = categoryNo;
        showCategoryPop(type, category, param);
      });

      $('.fa-minus-square').on('click', function(){	//카테고리 삭제

        var categoryObj = getCategoryObj(this);
        var category = categoryObj.id;

        var param = {
            categoryType : category,
        }

        var categoryNo = categoryObj.options[categoryObj.selectedIndex].value;
        param[category+'No'] = categoryNo;

        //param[categoryType] = categoryNo;
        if(confirm("삭제하시겠습니까?")) deleteCategory(param);
      });

      $('[class*=fa-caret-square]').on('click', function(){	//카테고리 order 변경 버튼 변경대상두개를 swap1 swap2 에 담아 보낸다.

        var isGoingUp = $(this).hasClass('fa-caret-square-up');

        var categoryObj = getCategoryObj(this);
        var category = categoryObj.id;
        var optionLen = categoryObj.options.length;
        var selectedIdx = categoryObj.selectedIndex;

        var swap1;
        var swap2;

        if (isGoingUp) {	//위로 이동
        	if(selectedIdx === 0)return;	//선택된것이 최상단
        	swap1 = categoryObj[selectedIdx].value;
        	swap2 = categoryObj[selectedIdx-1].value;
        }

        if (!isGoingUp) {	//아래로 이동
        	if(selectedIdx === optionLen)return;	//선택된것이 최하단
        	swap1 = categoryObj[selectedIdx].value;
        	swap2 = categoryObj[selectedIdx+1].value;
        }
        swapCategoryOrder(category, swap1, swap2);
      });

      $('#btnPOICategoryRegist').on('click',function(){	//수정이나 생성버튼
    	    $("#POICategoryRegistFrm").submit();
      });

/*      $(document).on("click","#btnPOICategoryRegist", function(){	//검색버튼 event
    	  $("#POICategoryRegistFrm").submit();
      });
*/

      $("#POICategoryRegistFrm").ajaxForm({	//Ajax폼 서브밋
    	  type: 'post',
          dataType : 'json',
          beforeSubmit: function (data, frm, opt) {

        	  if(!jValidationFrm(frm)) return false;
        	  //console.log(data);
          	},
            success: function(res) {
            	if(jResult(res, true)){
                	$('.modal').modal('hide');
                	loadingCategory(res.criterion.categoryType);
            	}
            }
        });
});

//<--- 전역변수 --->
//categoryTemplate
var categoryTemplate = {
		category1 : '<option value="{category1No}">{category1Nm}</option>',
		category2 : '<option value="{category2No}">{category2Nm}</option>',
}

function getCategoryObj(btnObj){
	  return btnObj.parentElement.parentElement.firstElementChild.getElementsByTagName('select')[0];
	}

function loadingCategory(category, param, targetId, callback, callbackArg){

	  if(isValEmpty(param)){	//메인페이지 리로딩을 위해 설정함.
		  if(category === 'category1') param = {};
		  if(category === 'category2') param = {category1No : $('#category1').val()};

	  }
	  if(isValEmpty(targetId)) targetId = category;

	  if(targetId === 'category1') {	//모달창은 targetId가 다르다
		  if(param === undefined) param = {};

	  }
	  if(targetId === 'category2') {
		  if(param === undefined) param = {category1No : $('#category1').val()};

	  }

	  //이부분은 위아래 이동후 해당 selectbox 유지시키기위해
	  var selectedCategory = $('#'+category).val();
	  if(isValEmpty(callbackArg))
	  callbackArg  = {
			  categoryType : category,
			  categoryNo : selectedCategory
	  }
	  if(isValEmpty(callback))
	  callback = function (res) {
		  $('#'+callbackArg.categoryType).val(callbackArg.categoryNo);
	  }

	  ajaxTemplate('/adm/poi/poiCategoryList.json', param, categoryTemplate[category], $('#'+targetId), callback, callbackArg);
	}

/**
 * 카테고리 삭제
 * @param param
 * @returns
 */
function deleteCategory(param){
  $.ajax({
      method: "POST",
      url: '/adm/poi/poiCategoryRemove.json',
      data: param,
        success: function(res) {
        	if(jResult(res,true)) {
        		loadingCategory(res.criterion.categoryType, undefined, undefined, function(){
            		var jSelect = jQuery('#' + res.criterion.categoryType);
            		var firstValue =  jQuery('OPTION:eq(0)', jSelect).val();
            		
            		jQuery('#' + res.criterion.categoryType + ' OPTION:eq(0)').prop("selected", true);
            		
            		jSelect.val(firstValue);
            		jSelect.change();
            	});
        	}
        }
  });
}

/**
 * @param category : categoryType
 * @param cateNo1 : 스왑할 카테고리 넘버1
 * @param cateNo2 : 스왑할 카테고리 넘버2
 * @returns
 */
function swapCategoryOrder(category, cateNo1, cateNo2){
  var param = {
      categoryType : category,
      poiCategoryList : [
        {categoryNo : cateNo1},
        {categoryNo : cateNo2}
      ]
  }

  $.ajax({
      method: "POST",
      url: '/adm/poi/poiCategorySwitch.json',
      dataType: 'json',
      contentType: 'application/json; charset=utf-8',
      data: JSON.stringify(param),
      success: function(res) {
//          loadingCategory(category,undefined,undefined,callback,callbackArg);
          loadingCategory(category);
        }
  });
}

/**
 * 아이콘셋 로딩
 * @param param
 * @returns
 */
function loadingIconset (param) {

	if(param === undefined) {
		param = {
				page:0
		};
		var iconsetNm = $('#schIconsetNm').val();
		if(iconsetNm){
			param.iconsetNm = iconsetNm
		}
	}

    function selectIconsetCallback(res,param){
    	if($('#iconsetNo').val() === "") return;
    	$('[name=iconsetNo_][value='+$('#iconsetNo').val()+']').prop('checked',true);
    }

	ajaxTemplate('/adm/poi/poiIconList.json', param, $('#POIIconListTpl').html(), $('#poiIconList'),selectIconsetCallback,{});
}
/**
 * modal창 생성
 * @param type : 'regist' // 'modify'
 * @param category : 'categoryType'
 * @param res : ajax return
 * @returns
 */
function showCategoryPop(type, category, param) {
  if(type === 'modify'){
    $.ajax({
        method: "POST",
        url: '/adm/poi/poiCategoryView.json',
        data: param,
          success: function(res) {
        	//console.log(res);
            if(jResult(res)) createPop(type, category, res.result);
          }
    });
  } else if (type === 'regist') {
    var res = param;
    createPop(type, category, res);
  }
  function createPop(type, category, res) {	//popup 생성

    function selectCategoryCallback (res, param) {	//ajax callback
		Object.keys(param).forEach(function(key){
		  document.getElementById(key).value = param[key];
		});
    }

    $('#schIconsetNm').val("");	//아이콘 검색 초기화
    //기본 select box 생성
    if(category === 'category1'){

      $('#category1No').val("").closest('tr').hide();
      $('#category2No').val("").closest('tr').hide();
      $('#iconSearch').hide();
      $('#iconTable').hide();

    } else if (category === 'category2') {

    	loadingIconset();

    	loadingCategory('category1', {}, 'category1No', selectCategoryCallback, {'category1No': res.category1No});
    	$('#category1No').val("").closest('tr').show();
    	$('#category2No').val("").closest('tr').hide();

    	$('#iconSearch').show();
    	$('#iconTable').show();

    }

    var modalTitle = '';	//모달창 타이틀
    var btnTitle = '';		//등록/수정버튼
    var categoryNm = '';	//카테고리 이름

    if(type === 'regist'){
      modalTitle = '카테고리 등록';
      btnTitle = '등록';
      $('#iconsetNo').val("");
    } else if (type === 'modify'){
      modalTitle = '카테고리 수정';
      btnTitle = '수정';
      categoryNm = res[category+'Nm'];
      $('#categoryNo').val(res[category+'No']);
      $('#iconsetNo').val(res.poiIconset.iconsetNo);
    }
    $('#modalType').val(type);
    $('#mapCategoryModal').modal();
    $('#mapCategoryModal .modal-title').text(modalTitle);
    $('#btnPOICategoryRegist').text(btnTitle);
    $('#categoryNm').val(categoryNm);
    $('#categoryType').val(category);
    $("#POICategoryRegistFrm").attr("action", type === 'regist' ? '/adm/poi/poiCategoryRegist.json' : '/adm/poi/poiCategoryModify.json')
  }
}