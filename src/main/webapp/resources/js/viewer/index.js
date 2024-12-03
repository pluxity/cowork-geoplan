PlxWebgl.init(document.getElementById('webGLContainer'), config, webglCallbacks);
$(function () {

	$('.top-ico-menu > .btn').click(function () { //상단 6개버튼
		var desc = this.dataset.desc;
		var $this = $(this);
		var flag = $this.hasClass('on');
		var btns = $('.top-ico-menu > .btn');

		viewerControl.btn(this);

		for (var i = 0, iLen = btns.length; i < iLen; ++i) {
			var btnDom = btns[i];
			if (btnDom.classList.contains('on')) {
				eventControl.topBtnControl(btnDom.dataset.desc, false);
			}
		}
		//.btn-rt-pop 일시에 작동
		if (this.classList.contains('btn-rt-pop')) {

			$('html').addClass('scrl-no');
			var name = $this.attr('layer-name');

			$('.rt-layer').fadeOut(100, function () {
				$(this).css({
					'right': '-270px'
				}).removeClass('on');
			});
			$('.rt-layer[layer-name=' + name + ']').show().animate({
				right: 30
			}, 200, function () {
				$(this).animate({
					right: 0
				}, 100, function () {
					$(this).addClass('on');
				});
				$('html').removeClass('scrl-no');
			});

		} else {
			/* 아이콘 버튼 */
			//.top-ico-menu .btn
			$('.rt-layer').fadeOut(100, function () {
				$(this).css({
					'right': '-270px'
				}).removeClass('on');
			});
		}

		if (!flag) eventControl.topBtnControl(desc, !flag); // 해당 버튼 이벤트 수행

		if (this.classList.contains('btn-lft-pop')) { //왼쪽은 클릭시 on 하지 않음
			return;
		}

		// 나머지 오른쪽 버튼들 on 관련 로직
		if (flag) {
			$this.removeClass('on');
		} else {
			$('.top-ico-menu .btn').removeClass('on');
			$this.addClass('on');
		}
	});

	// 마우스 오른쪽 메뉴 금지
	window.addEventListener('contextmenu', function (e) { // Not compatible with IE < 9
		e.preventDefault();
	}, false);

	// $('#search').on("keyup click", function () {
	// 	drawHTML.searchPoiList(this);
	// });
	//
	// $('INPUT.input_search__poi').on('click', function() {
	// 	drawHTML.searchPoiList(this);
	// });
});

const api = {
	getPoiDetail(param) {
		// return $.ajax({
		// 	method: 'POST',
		// 	url: '/api/viewer/poiView.json',
		// 	dataType: 'json',
		// 	contentType: 'application/json; charset=utf-8',
		// 	data: JSON.stringify(param),
		// 	success: function(res) {
		// 		return res;
		// 	  }
		// });

		return $.ajax({
			type: 'POST',
			url: `${_CONTEXT_PATH_}api/viewer/poiView.json`,
			data: param,
			dataType: 'json',
			success: function (res) {
				return res;
			}
		});
	},
	getDeviceDetail(param) {
		return $.ajax({
			type: 'POST',
			url: `${_CONTEXT_PATH_}api/viewer/deviceInfo.json`,
			data: param,
			dataType: 'json',
			success: function (res) {
				return res;
			}
		});
	}
}

var controlMiniMap = function () {
	var _zoomLevel = 1;

	function zoomIn() {
		if (_zoomLevel > 11) return;
		Px.Minimap.SetZoomLevel(++_zoomLevel);
	}

	function zoomOut() {
		if (_zoomLevel < 2) return;
		Px.Minimap.SetZoomLevel(--_zoomLevel);
	}

	return {
		zoomIn: zoomIn,
		zoomOut: zoomOut
	}
}();

const viewerControl = {
	btn(obj) {
		var $obj = $(obj);
		if ($obj.hasClass('on')) {
			$obj.addClass('off');
		} else {
			$obj.removeClass('off');
		}
	},
	poiPop(param, evtTime, callback) {
		const option = {
			isMagnify: true
		}
		const _param = {
			dvcCd: param.dvcCd,
			poiNo: param.poiNo
		}
		const pois = PlxWebgl.Poi.getPoiByProperty(_param);
		evtTime = evtTime ? evtTime.replace(/[\s-\:]/g, '') : null;
		if (pois != null) {
			$(".layer-pop").hide();
			hideEnergyLine();

			if (pois.length === 1) {
				const poi = pois[0];
				const poiNo = poi.id;
				const moveToPoiPromise = PlxWebgl.Poi.moveToPoiPromise(poi, option);
				const _promise = () => {
					return Promise.all([moveToPoiPromise(), api.getPoiDetail({ poiNo, evtTime })]).then((res) => {
						const pos = Px.Poi.Get2DPosition(poiNo);
						drawHTML.poiPop(res[1], pos, evtTime);

						if (typeof callback === 'function') callback();
					}).catch((rej) => console.log(rej));
				}
				PlxWebgl.Tween.enqueue(_promise);
			} else {
				PlxWebgl.Poi.moveToPoiByParams(_param);
			}
		}
	}
};

const drawHTML = {

	//좌측 poi 카테고리 중분류 생성
	//param : category1No
	category2: (category1No) => {
		const url = `${_CONTEXT_PATH_}api/viewer/poiCategoryList.json`;
		const param = {
			mapNo: mapNo,
			category1No: category1No
		};
		const template = $('#leftCategory2Tpl').html();

		const $target = $('#depth2-box .depth2-lst');

		ajaxTemplate(url, param, template, $target);
		nodeTreeTemplateReplacer($('.btn.left-category1[data-category1-no="' + category1No + '"').data(), $('#depth2-box .d-tit')); //타이틀 수정

		return false;
	},

	//좌측 중분류 카테고리 눌렀을때 해당 poi 생성
	poiList: (obj) => {
		//publish
		if ($(obj).hasClass('on')) {
			$('.depth2-item .btn').removeClass('on');
			$('.depth3-lst').slideUp(200);
		} else {

			var url = "/api/viewer/poiList.json";
			var param = {
				mapNo: mapNo,
				category2No: obj.parentNode.dataset.category2No,
				positionYn: "Y"
			};

			var template = $('#leftPoiListTpl').html();
			target = obj.parentNode.lastElementChild;

			var callback = function () {
				$('.depth2-item .btn').removeClass('on');
				$(obj).addClass('on');
				$('.depth3-lst').slideUp(200);
				$(obj).closest('.depth2-item').find('.depth3-lst').slideDown(200);
			}
			ajaxTemplate(url, param, template, target, callback);
		}
		//publish

		return false;
	},
	searchPoiList: (obj) => {

		//publish:ST...
		$('.srch-list li').remove(); //해당 검색 내역 삭제

		// var $resultArea = $(".srch-list ul");
		var $resultArea = $(obj.nextElementSibling.querySelector('UL'));
		var searchKeyword = obj.value;

		if (searchKeyword.length < 1) { // 검색어 Length 만족 못할 때
			$resultArea.hide();
			return;
		}

		$resultArea.show();

		var template = $('#searchPoiListTpl').html();

		//publish:END...

		var maxLength = 5;
		var value;

		var url = "/api/viewer/poiList.json";
		var param = {
			mapNo: mapNo,
			searchType: "poiNm",
			searchKeyword: searchKeyword,
			positionYn: "Y",
			pageSize: 5
		};

		ajaxTemplate(url, param, template, $resultArea);
		return false;
	},

	poiPop: (res, pos, evtTime) => {

		const {
			result
		} = res;
		const {
			dvcCd,
			deviceInfo,
			resDeviceInfo,
			floorInfo,
			poiCategory,
			poiNm,
			poiNo,
		} = result;
		const {
			floorNm
		} = floorInfo;
		const {
			category1No,
			category2No,
			category1Nm,
			category2Nm
		} = poiCategory;

		poiPop.stop();
		const $poiPop = $('#poi-pop');
		$poiPop.hide();

		// 	<li class="type {typeClass}">
		// 	<span class="title">{name}</span>
		// 	<span class="cont">{value}{unit}</span>
		// </li>

		const {
			info_list,
			url_list,
			detail,
		} = resDeviceInfo;

		const isException = /exception/i.test(detail)
		const isInfoListEmpty = info_list && info_list.length === 0;

		if (isException && isInfoListEmpty) {	//exception 나는 경우임.
			console.error(detail);
			$('#poi-pop-title').html(poiNm);
			$('#poi-pop-contents').html($('#poiPopContentErrTpl').html());
			setLayerPos($poiPop, pos.x, pos.y);
			$poiPop.show();
			return $poiPop;
		} else if (!isException && isInfoListEmpty) {
			// console.error(detail);
			$('#poi-pop-title').html(poiNm);
			$('#poi-pop-contents').html($('#poiPopContentNoneTpl').html());
			setLayerPos($poiPop, pos.x, pos.y);
			$poiPop.show();
			//return $poiPop;

		} else if (info_list) {
			const contentDatas = info_list.map((info) => {
				info.typeClass = "";
				return info;
			});

			const contentHtml = templateReplacer(contentDatas, $('#poiPopContentTpl').html());

			$('#poi-pop-contents').html(contentHtml);
			$('#poi-pop-title').html(poiNm);

			$poiPop.show();
			setLayerPos($poiPop, pos.x, pos.y);
			poiPop.run(poiNo);
		}

		if(url_list != null) {
			cctvControl.open(url_list, pos.x, pos.y, evtTime);
		}

		return $poiPop;
	},

	areaSearch: (data) => {

		if (!data || data.length === 0) return;

		var poiNoList = [];

		for (var i = 0, iLen = data.length; i < iLen; ++i) {
			if (i < 4) {
				data[i].isSelected = "on";
				poiNoList.push(data[i].id);
			}
		}

		var html = templateReplacer(data, $('#areaSearchListTpl').html());
		$('#area-search-list').html(html);
		var param = {
			poiNoList: poiNoList
		};
		cctvControl.getCCTVInfo(param, cctvControl.openMulti);
	},
};

const eventControl = {
	topBtnControl(type, flag) {
		switch (type) {
			case 'areaSearch':
				viewerControl.areaSearch(flag);
				break;
			case 'watchGroup':
				if (flag) bookmarkPoiList();
				break;
			case 'evacRoute':
				if (flag) {
					if (isValEmpty(gFloorGroup)) {
						evacRoute.loadRoute();
					} else {
						viewerControl.changeFloor(null, true, evacRoute.loadRoute)
					}
				} else {
					Px.Evac.Clear();
				}
				break;
			case 'virtualPatrol':
				if (flag) drawHTML.virtualPatrolList();
				else {
					virtualPatrol.control.clear();
					$('.lst-view').hide();
					$('.lst-wrap').show();
				}
				break;
			case 'energy':
				if (flag) drawEnergyLine();
				else hideEnergyLine();
				break;
		}
	}
}

/**
 * 주요감시그룹 리스트
 */
var bookmarkPoiList = function () {
	$.post("/viewer/bookmarkPoiList.json", {
		mapNo: mapNo
	}, function (res) {
		if (jResult(res)) {
			var html = templateReplacer(res.result, $("#bookmarkListTpl").html());
			$("#bookmarkPoiList").html(html);
		}
	});
}

class PoiPop {
	constructor() {
		this._setTimeoutId = null;
		this._poiNo = null;
		this._flag = false;
		this._interval = 60000;	// 인터벌 1분
	}

	setParam(_param) {
		this.param = _param;
	}
	getParam() {
		return this.param;
	}

	run(poiNo) {
		if (this._flag) {
			stop();
		}
		this._poiNo = poiNo;
		this._flag = true;
		setTimeout(() => this.scheduler(), this._interval);
	}

	stop() {
		clearTimeout(this._setTimeoutId);
		this._flag = false;
	}

	scheduler() {
		this.draw();
		clearTimeout(this._setTimeoutId);
		this._setTimeoutId = setTimeout(() => this.scheduler(), this._interval);
	}

	draw() {
		api.getPoiDetail({ poiNo: this._poiNo }).then((res) => {
			const {
				result
			} = res;
			const {
				dvcCd,
				deviceInfo,
				resDeviceInfo,
				floorInfo,
				poiCategory,
				poiNm,
			} = result;
			const {
				floorNm
			} = floorInfo;
			const {
				category1No,
				category2No,
				category1Nm,
				category2Nm
			} = poiCategory;

			const {
				info_list,
				url_list
			} = resDeviceInfo;

			if (info_list && info_list.length > 0) {
				const contentDatas = info_list.map((info) => {
					info.typeClass = "";
					return info;
				});

				const contentHtml = templateReplacer(contentDatas, $('#poiPopContentTpl').html());

				$('#poi-pop-contents').html(contentHtml);
				$('#poi-pop-title').html(poiNm);
			}
		});
	}
}

const poiPop = new PoiPop();