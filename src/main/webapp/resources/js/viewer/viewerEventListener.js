$(function () {

    $(".mini_map_icon > LI").on({
        mousedown: function () {
            var btnType = $(this).data("btn-type");
            if (btnType == "in") {
                Px.Camera.StartZoomIn();
            } else if (btnType == "out") {
                Px.Camera.StartZoomOut();
            } else if (btnType == "up") {
                Px.Camera.StartRotateUp();
            } else if (btnType == "down") {
                Px.Camera.StartRotateDown();
            } else if (btnType == "left") {
                Px.Camera.StartRotateLeft();
            } else if (btnType == "right") {
                Px.Camera.StartRotateRight();
            }
        },
        mouseup: function () {
            var btnType = $(this).data("btn-type");
            if (btnType == "in") {
                Px.Camera.StopZoomIn();
            } else if (btnType == "out") {
                Px.Camera.StopZoomOut();
            } else if (btnType == "up") {
                Px.Camera.StopRotateUp();
            } else if (btnType == "down") {
                Px.Camera.StopRotateDown();
            } else if (btnType == "left") {
                Px.Camera.StopRotateLeft();
            } else if (btnType == "right") {
                Px.Camera.StopRotateRight();
            }
        },
        touchstart: function () {
            var btnType = $(this).data("btn-type");
            if (btnType == "in") {
                Px.Camera.StartZoomIn();
            } else if (btnType == "out") {
                Px.Camera.StartZoomOut();
            } else if (btnType == "up") {
                Px.Camera.StartRotateUp();
            } else if (btnType == "down") {
                Px.Camera.StartRotateDown();
            } else if (btnType == "left") {
                Px.Camera.StartRotateLeft();
            } else if (btnType == "right") {
                Px.Camera.StartRotateRight();
            }
        },
        touchend: function () {
            var btnType = $(this).data("btn-type");
            if (btnType == "in") {
                Px.Camera.StopZoomIn();
            } else if (btnType == "out") {
                Px.Camera.StopZoomOut();
            } else if (btnType == "up") {
                Px.Camera.StopRotateUp();
            } else if (btnType == "down") {
                Px.Camera.StopRotateDown();
            } else if (btnType == "left") {
                Px.Camera.StopRotateLeft();
            } else if (btnType == "right") {
                Px.Camera.StopRotateRight();
            }
        },
        click: function () {
            var $this = $(this);
            var btnType = $this.data("btn-type");
            if($this.hasClass('disabled')) {
                return false;
            }
            jQuery(this).siblings().removeClass('on');

            if (btnType == "center") {
                Px.Camera.ExtendView(true, 1000, 50);
                // PlxWebgl.CamPos.changeCamPos('3D', ()=> {
                //     Px.Camera.ExtendView(false, 1000, 50);
                // });
            } else if (btnType === "viewLook") { // 1인칭 시점
                // if (window.viewerControl) viewerControl.btn(this);
                
                const floorBt = document.querySelector('.floor_wrap .btn_all'); 
                $this.toggleClass("on");
                if ($this.hasClass('on')) {
                    Px.Camera.FPS.On();
                    miniMapBtDisable($this[0].classList[0], true);
                    // miniMapList.forEach((li) => {
                    //     if(!li.classList.contains('icon04')) {
                    //         li.classList.add('disabled');    
                    //     }            
                    // })
                    floorBt.setAttribute('disabled', 'disabled');
                    floorBt.classList.add('disabled');
                }
                else {
                    if(Px.Camera.FPS.IsOn()) {
                        Px.Camera.FPS.Off();
                    } else {
                        Px.Util.PointPicker.Off();
                        Px.Camera.ExtendView();
                    }
                    miniMapBtDisable($this[0].classList[0], false);
                    // miniMapList.forEach((li) => {
                    //     if(!li.classList.contains('icon04')) {
                    //         li.classList.remove('disabled');
                    //     }
                    // })
                    floorBt.removeAttribute('disabled');
                    floorBt.classList.remove('disabled');

                }
            } else if (btnType === "transparent") {
                if (window.viewerControl) viewerControl.btn(this);
                $this.toggleClass("on");
                if ($this.hasClass('on')) {
                    miniMapBtDisable($this[0].classList[0], true);
                    Px.Model.Transparent.SetAll(_OPACITY_TRANSPARENT_);
                } else {
                    miniMapBtDisable($this[0].classList[0], false);
                    Px.Model.Transparent.Restore();
                }
            } else if (btnType == "lod") { // 1인칭 시점
                $this.toggleClass("on");
                if ($this.hasClass('on')) {
                    initPoiLod(mapNo);
                } else {
                    Px.Lod.SetLodData();
                }
            } else if(btnType === 'distance' || btnType === 'area') {
                $this.toggleClass("on");
                if($this.hasClass('on')){
                    miniMapBtDisable($this[0].classList[0], true);
                    fnChangeMouseCursor('move');
                    fnActiveMeasure(btnType);
                } else {
                    miniMapBtDisable($this[0].classList[0], false);
                    fnChangeMouseCursor();
                    Px.Util.Measure.Stop();

                    measurePopupBt = document.querySelector('.popup_layer__measure button');
                    if(measurePopupBt) {
                        measurePopupBt.dispatchEvent(new Event('click'));
                    }
                }
            } else if(btnType === 'screenshot') {
                Px.Util.TakeScreenshot(true);
            }
        }
    });
    $(document).on('click', '.layer-pop .btn-close', function () {
        poiPop.stop();
        $(this).closest('.layer-pop').fadeOut(200);
    });

    $('#poiDetailPop .close').click(() => {
        $('#poiDetailPop').hide();
    });

    $('.graph-header a').click((e) => {
        const $dom = $(e.target);
        $dom.siblings().removeClass('on');
        $dom.addClass('on');
    });

    $(document).on('click', '.airFlowManageChart > span', function () {
        $('#poiDetailPop').show();
    });
});

const miniMapBtDisable = (currentClassNm, disabled) => {
    const miniMapList = document.querySelectorAll('.mini_map_icon > li');
    miniMapList.forEach((li) => {
        if(!li.classList.contains(currentClassNm)) {
            if(disabled) {
                li.classList.add('disabled');
            } else {
                li.classList.remove('disabled');
            }
        }
    })
}

// start, end Poi 제거
const removeCustomPoi = () => {
    if(Px.Poi.GetData('start')) {
        Px.Poi.Remove('start');
    }
    
    if(Px.Poi.GetData('end')) {
        Px.Poi.Remove('end');
    }
}

// 거리, 면적재기 활성화
const fnActiveMeasure = (type) => {
    const popupMeasure = document.querySelector('DIV.popup_layer__measure');
    if(popupMeasure) {
        popupMeasure.remove();
    }
    if(type === 'distance') {
        Px.Util.Measure.Distance((distance, pos) => {
            fnCallbackMeasure(type, distance, pos);
        });
    } else if(type === 'area') {
        Px.Util.Measure.Area((area, pos) => {
            fnCallbackMeasure(type, area, pos);
        });
    }
}

// 거리, 면적재기 끝내기 콜백
const fnCallbackMeasure = (type, data, pos) => {
    // 팝업 겹쳐지게 중복으로 안생기게 이전 팝업 제거
    if(document.querySelector('.popup_layer__measure')) {
        document.querySelector('.popup_layer__measure').remove();
    }

    // 면적 측정 계속할때 이전 팝업 존재시 제거
    document.addEventListener('click', () => {
        if(document.querySelector('.popup_layer__measure')) {
            document.querySelector('.popup_layer__measure').remove();
        }
    })
    
    const typeKr = type === 'distance' ? '거리' : '면적';
    const unit = type === 'distance' ? 'm' : `m<sup>2</sup>`
    const fixedData = data.toFixed(2);

    const body = document.querySelector('body');

    const popupMeasure = document.querySelector('#popupMeasure')
    const popupNode = document.importNode(popupMeasure.content.firstElementChild, true);
    popupNode.querySelector('.header-tit > .txt').textContent = `${typeKr} 계산하기`;

    const dataTitle = popupNode.querySelector('li > SPAN.tit');
    const dataValue = popupNode.querySelector('li > SPAN.value');
    dataTitle.textContent = `${typeKr} : `;
    dataValue.innerHTML = `${fixedData}${unit}`;

    body.appendChild(popupNode);
    setPopupPos(body, popupNode, pos);

    popupNode.querySelector('BUTTON').addEventListener('click', () => {
        popupNode.remove();
        // 지우기 -> 삭제하고 기능 멈춤
        // Px.Util.Measure.Stop();
        // const activedLi = document.querySelector('UL.mini_map_icon > LI.on');
        // activedLi.classList.remove('on');
        // fnChangeMouseCursor();

        // fnActiveMeasure(type);
        if(document.querySelector('.mini_map_icon li.on')) {
            document.querySelector('.mini_map_icon li.on').classList.remove('on');
        }
        miniMapBtDisable('', false);
        fnChangeMouseCursor();
        Px.Util.Measure.Stop();
    });
}

// 길찾기 검색결과 선택했을 때
const fnClickFindPathPoiResult = (elem, poiId) => {
    document.querySelector('UL.search_list.on').classList.remove('on');

    const inputElement = elem.parentElement.parentElement.previousElementSibling;
    const type = elem.parentElement.parentElement.parentElement.children[0].classList.contains('start') ? 'start' : 'end';
    const { displayText, position, rotation, property } = Px.Poi.GetData(poiId);
    inputElement.value = displayText;
    inputElement.setAttribute('poi-id', poiId);

    Px.Poi.Add({
        id: type,
        group: 'customPoi',
        position,
        scale: {
            x: 0.1,
            y: 0.1,
            z: 0.1,
        },
        rotation,
        iconUrl: `/resources/img/icon/poiicon/customPoi/${type}.png`,
        lineHeight: config.poi.poiLineHeight,
        displayText: '',
        property: {
            floorId: property.floorId,
            type: 'customPoi',
            originalPoiId: poiId,
        }
    }, false);

    inputElement.value = `${property.poiData.floorInfo.floorNm} ${displayText}`;
    inputElement.setAttribute('poi-id', type);

    fnMoveToPoi(poiId, true, false);  
}

// 검색 결과 선택했을 때
const fnClickSearchResult = (elem, poiId) => {
    document.querySelector('UL.search_list.on').classList.remove('on');

    const inputElement = elem.parentElement.parentElement.previousElementSibling;
    const poiInfo = Px.Poi.GetData(poiId);
    inputElement.value = `${poiInfo.property.poiData.floorInfo.floorNm} ${poiInfo.displayText}`;
    inputElement.setAttribute('poi-id', poiId);

    const poiBox = document.querySelector('.poi_box');

    if(poiBox) {
        poiBox.classList.remove('on');
        poiBox.remove();
    }

    fnMoveToPoi(poiId);
}

/**
 * 
 * @param poiId
 * @param isInvisiblePopup undefined 이거나 false 일 경우에만 popup 보임
 */
const fnMoveToPoi = (poiId, isInvisiblePopup, categoryShow = true) => {

    const floorId = Px.Poi.GetData(poiId).property.floorId;
    const category1No = Px.Poi.GetData(poiId).property.category1No;

    // Px.Model.Visible.HideAll();
    // Px.Model.Visible.Show(floorId);

    const btnFloor = document.querySelector(`UL > LI[data-floor-group-no="${floorId}"]`)
    if(!btnFloor.classList.contains('on')) {
        btnFloor.dispatchEvent(new Event('click'));
    }

    if(categoryShow) {
        const btnCateOnOff = document.querySelector(`UL.mini-poi > LI[data-category1-no="${category1No}"]`);
        if(!btnCateOnOff.classList.contains('on')) {
            btnCateOnOff.dispatchEvent(new Event('click'));
        }
    }

    const poiInfo = Px.Poi.GetData(poiId);
    Px.Camera.MoveToPosition(650, 15, poiInfo.position.x, poiInfo.position.y, poiInfo.position.z, () => {
        if(isInvisiblePopup){
            return;
        }
        const { poiData } = Px.Poi.GetData(poiId).property;
        showPoiPopup(poiData);
    });
    // Px.Camera.MoveToPoi(poiId, true, 500, () => {
    //     const { poiData } = Px.Poi.GetData(poiId).property;
    //     showPoiPopup(poiData);
    // });
}
const changeFloor = (pFloorGroup) => {
    let floorGroupNo = pFloorGroup ? pFloorGroup : 'all' ;

    const onCategory1List = document.querySelectorAll('UL.mini-poi > LI.on:not([data-category1-no="all"])');

    const isAll = floorGroupNo === 'all';

    if(document.querySelector('.poi_box')) {
        document.querySelector('.close').dispatchEvent(new Event('click'));
    }

    // POI 전체 감춤
    Px.Poi.HideAll();
    // PathFinder 전체 감춤
    Px.Topology.PathFinder.HideAll();
    if (isAll) { // 전체층 처리
        Px.Model.Visible.ShowAll();
        camPos.changeCamPos(floorGroupNo);
    } else { // 특정 층
        Px.Model.Visible.HideAll();
        Px.Model.Visible.Show(floorGroupNo);
        camPos.changeCamPos(floorGroupNo);
        
        onCategory1List.forEach((elem) => {
            const category1No = elem.dataset.category1No;
            Px.Poi.ShowByPropertyArray({'floorId':`${floorGroupNo}`, 'category1No': parseInt(category1No, 10)});
        });

        // 현재 층 PathFinder 만 보임
        Px.Topology.PathFinder.Show(floorGroupNo);
    }
    Px.Poi.ShowByPropertyArray({type: 'customPoi', floorId: floorGroupNo});
    
}
const changeFloorEvent = () => {
    const floorList = document.querySelectorAll('.ul-floor>li');
    floorList.forEach((floor) => {
        floor.addEventListener('click', (event) => {
            const { floorGroupNo } = event.currentTarget.dataset;
            toggleFloorBtn(floorGroupNo);
            changeFloor(floorGroupNo);

            if(document.querySelector('UL.mini_map_icon > LI.on[data-btn-type="transparent"]')) {
                Px.Model.Transparent.SetAll(_OPACITY_TRANSPARENT_);
            }
        });
    });
}
const toggleFloorBtn = (floorGroupNo) => {
    const floorItemList = document.querySelectorAll('.ul-floor>li');
    for (const floorItem of floorItemList) {
        floorItem.classList.remove('on');
    }
    const targetFloor = document.querySelector(`.ul-floor>li[data-floor-group-no="${floorGroupNo}"]`);
    targetFloor.classList.add('on');

    const floorText = targetFloor.querySelector('SPAN.txt').innerText;
    document.querySelector('DIV.floor_wrap > BUTTON > SPAN.box > SPAN.txt').innerText = floorText;
    document.querySelector('DIV.floor_wrap > BUTTON > SPAN.box > SPAN.txt').dataset.floorGroupNo = floorGroupNo;

    return floorGroupNo;
}

const showMiniPoiCategory = () => {
    const miniPoiLI = document.querySelectorAll('.mini-poi > li');
    miniPoiLI.forEach((poi) => {
        poi.addEventListener('click', (event) => {
            const target = event.currentTarget;
            const { category1No } = target.dataset;
            target.classList.toggle('on');

            if(category1No === 'all') {
                if(target.classList.contains('on')) {
                    miniPoiLI.forEach((li) => li.classList.add('on'));
                } else {
                    miniPoiLI.forEach((li) => li.classList.remove('on'));
                }
            }
            const floorGroupNo = document.querySelector('DIV.floor_wrap > UL > LI.on').dataset.floorGroupNo;
            if(floorGroupNo === 'all') return;
            
            if(category1No === 'all') {
                if(target.classList.contains('on')) {
                    Px.Poi.ShowByProperty('floorId', `${floorGroupNo}`);
                } else {
                    Px.Poi.HideByProperty('floorId', `${floorGroupNo}`);
                }
                return;
            }

            if(target.classList.contains('on')) {
                // PlxWebgl.PoiCategory.showPoiCategory1(category1No);
                Px.Poi.ShowByPropertyArray({'floorId':`${floorGroupNo}`, 'category1No': parseInt(category1No, 10)});
            } else {
                Px.Poi.HideByPropertyArray({'floorId':`${floorGroupNo}`, 'category1No': parseInt(category1No, 10)});
                // PlxWebgl.PoiCategory.hidePoiCategory1(category1No);
            }
        });
    })  
}

const setPopupPos = (targetNode, contentNode, pos) => {
    const {x, y} = pos;

    if (targetNode.offsetWidth < x + contentNode.offsetWidth) {
        const width = targetNode.offsetWidth - contentNode.offsetWidth;
        contentNode.style.left = `${width}px`;
    } else {
        contentNode.style.left = `${x}px`;
    }

    if (targetNode.offsetHeight < y + contentNode.offsetHeight) {
        const height = targetNode.offsetHeight - contentNode.offsetHeight;
        contentNode.style.top = `${height}px`;
    } else {
        contentNode.style.top = `${y}px`;
    }

    targetNode.appendChild(contentNode);

    return contentNode;
}

const showPoiPopup = (poiData) => {
    const { poiNm, dvcCd, poiCategory } = poiData;
    const { category1Nm, category2Nm } = poiCategory;
    const { x, y } = Px.Poi.Get2DPosition(poiData.poiNo);
    const body = document.querySelector('body');
    let poiBox = document.querySelector('.poi_box');

    if(poiBox) {
        poiBox.classList.remove('on');
        poiBox.remove();
    }

    const poiPopup = document.querySelector('#poiPopup')
    const popupNode = document.importNode(poiPopup.content.firstElementChild, true);
    popupNode.querySelector('.header-tit > .txt').textContent = poiNm;

    const items = popupNode.querySelectorAll('li > .txt');
    items[0].textContent = dvcCd;
    items[1].textContent = `${category1Nm} > ${category2Nm}`;

    body.appendChild(popupNode);
    setPopupPos(body, popupNode, {x:x, y:y});

    popupNode.querySelector('.close').addEventListener('click', (evt) => {
        popupNode.remove();
    });
}

