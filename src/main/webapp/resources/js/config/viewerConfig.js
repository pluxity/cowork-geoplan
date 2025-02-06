let is2D = false;
const webglCallbacks = {
    initialize: () => { // webgl초기화 이후 실행
        // PlxWebgl.control.camPos.setData('{"test":{"position":{"x":-1317.61868286876,"y":6467.67399691508,"z":-10298.426886887142},"rotation":{"x":-2.5808207041821594,"y":-0.10792742551600473,"z":-3.0740459150260966},"target":{"x":100,"y":100,"z":100}}}');
        // PlxWebgl.control.camPos.changeCamPos('test');
        $('#loadingLayer').show();
        Px.Camera.FPS.SetHeightOffset(1.7);
        Px.Camera.FPS.SetMoveSpeed(50);

        Px.Util.Measure.SetSize(.1);
        Px.Topology.PathFinder.SetSize(0.1);

        Px.Evac.LoadArrowTexture('/resources/img/evacRoute/arrow.png', function() {  });
    },
    beforeDeployPoi: (poi) => { // poi 등록전 커스터마이징

    },
    afterDeployPoi: (poi) => { // poi 등록후 커스터마이징
        Px.Poi.SetTextSize(poi.id, 70);
        Px.Poi.SetIconSize(poi.id, 70);
    },
    loadModeling: () => { // SBM 메모리에 적재된 이후 실행(렌더링 이후 아님)
        // console.log('loadModeling callback');
    },
    loadPoiCategory1: (datas) => {
        // console.log('loadPoiCategory1 callback');
    },
    onComplete: () => { // 모든게 완료되었을때 실행

    },
    afterInitialize: () => {

        const allFloorGroups = PlxWebgl.Floor.getAllFloorGroups();
        const sortedFloorGroup = Object.values(allFloorGroups).sort(function (a, b) {
            return a.floorGroup - b.floorGroup;
        });
        var html = '<li class="on" data-floor-group-no="all"><span class="txt">전체층</span></li>';

        sortedFloorGroup.forEach((floor) => {
            const { floorGroup, floorNm } = floor;
            html += `<li data-floor-group-no=${floorGroup}><span class="txt">${floorNm}</span></li>`
        });
        document.querySelector('.ul-floor').innerHTML = html;

        changeFloorEvent();
        
        PlxWebgl.PoiCategory.hidePoiCategory1All();

        const category1List = PlxWebgl.PoiCategory.getCategory1();
        const ulCategory1List = document.getElementById('category1List');
        category1List.forEach((cate1Info) => {
            const targetLi = ulCategory1List.querySelector(`LI[data-category1-no="${cate1Info.id}"]`);
            const ulMenuList = targetLi.querySelector('.menu-list');

            const liHeader = document.createElement('LI');
            liHeader.classList.add('li-header');
            const spanHeaderNm = document.createElement('SPAN');
            spanHeaderNm.classList.add('txt');
            spanHeaderNm.innerText = `${cate1Info.name} POI 그룹정보`;
            liHeader.append(spanHeaderNm);

            ulMenuList.append(liHeader);

            const category2List = PlxWebgl.PoiCategory.getCategory2(cate1Info.no);
            category2List.forEach((cate2Info) => {

                const liContent = document.createElement('LI');
                liContent.addEventListener('pointerup', (e) => {
                    e.preventDefault();
                    e.stopImmediatePropagation();

                    let t = e.currentTarget;
                    let has = t.classList.contains('on');
                    let menuList = document.querySelectorAll('.menu-list > li');

                    if(!has){
                        for(li of menuList){
                            li.classList.remove('on');
                        }
                        t.classList.add('on');
                    }
                });

                const spanCate2Title = document.createElement('SPAN');
                spanCate2Title.classList.add('tit');
                const spanCate2TitleText = document.createElement('SPAN');
                spanCate2TitleText.classList.add('txt');
                spanCate2TitleText.innerText = cate2Info.name;
                spanCate2Title.append(spanCate2TitleText);

                const ulSubmenuList = document.createElement('UL');
                ulSubmenuList.classList.add('submenu-list');

                const poiList = Px.Poi.GetPoiDataByProperty('category2No', cate2Info.no);
                if(poiList.length === 0) {
                    const tempLi = document.createElement('LI');
                    const spanPoiNm = document.createElement('SPAN');
                    spanPoiNm.classList.add('txt');
                    spanPoiNm.innerText = 'POI 데이터 없음';

                    tempLi.append(spanPoiNm);
                    ulSubmenuList.append(tempLi);
                } else {
                    poiList.forEach((poiInfo) => {
                        const tempLi = document.createElement('LI');
                        tempLi.dataset.poiId = poiInfo.id;
                        const spanPoiNm = document.createElement('SPAN');
                        spanPoiNm.classList.add('txt');
                        spanPoiNm.innerText = `${poiInfo.property.poiData.floorInfo.floorNm} ${poiInfo.displayText}`;

                        tempLi.append(spanPoiNm);
                        tempLi.addEventListener('pointerup', (e) => {
                            const poiId = e.currentTarget.dataset.poiId;
                            fnMoveToPoi(poiId);
                        })
                        ulSubmenuList.append(tempLi);
                    });
                }
                liContent.append(spanCate2Title);
                liContent.append(ulSubmenuList);

                ulMenuList.append(liContent);
            });
        })

        /* ********************************************************************************* */
        Px.Minimap.Attach(document.getElementById('minimap')); //미니맵 처리
        Px.Minimap.SetZoomLevel(0.7);

        $('#loadingLayer').hide();
        Px.Minimap.HideFov(); // 미니맵 화각 오프
        camPos.changeCamPos('all');

        Px.Camera.EnableScreenPanning();

        Px.PointMesh.LoadCountTextFont('/resources/css/webfonts/Open_Sans_Bold.json');

        // fetch("/api/objects")
        //     .then(res => res.json())
        //     .then(data => {
        //         // const filteredData = data.filter((item) => item.floor);
        //         const filteredData = data.filter((item) => item["spaceId"]).map((item) => {
        //             item.floor = item["spaceId"];
        //             return item;
        //         });
        //
        //         Px.PointMesh.SetPoints(filteredData);
        //         Px.PointMesh.Show_AllFloorObject();
        //     });

        Px.PointMesh.AddEventListener('onPointEnterAlarmArea', onPointEnterAlarmAreaCallback);
        Px.PointMesh.AddEventListener('onPointExitAlarmArea', onPointExitAlarmAreaCallback);

        Px.PointMesh.AddEventListener('onAreaClick', (data)=> {

            document.querySelector('.area_box')?.remove();

            const { type, area_name, points_in_area } = data;

            const areaBox = document.createElement('DIV');
            areaBox.className = 'area_box';

            const headerTit = document.createElement('P');
            headerTit.className = 'header-tit';
            headerTit.innerHTML = `<span class="txt">${area_name}</span>
                    <span class="close"></span>`;

            areaBox.appendChild(headerTit);
            areaBox.querySelector('.close').addEventListener('click', () => {
                document.querySelector('.area_box').remove();
            })


            const tagList = document.createElement('UL');

            if(points_in_area.length < 1) {
                tagList.innerHTML = `<li><span>해당 공간에는 아무도 없습니다.</span></li>`;
            } else {
                for(const point of points_in_area) {
                    tagList.innerHTML +=
                        `<li>
                            <span class="tit">이름 : </span>
                            <span class="txt">${point.displayName}</span>
                            <span>&nbsp;/&nbsp;</span>
                            <span class="tit">태그 : </span>
                            <span class="txt">${point.id}</span>
                        </li>`;

                }


            }

            areaBox.appendChild(tagList);

            document.querySelector('BODY').appendChild(areaBox);

        });
        Px.PointMesh.AddEventListener('onPointClick', (data) => {
            const { displayName, id } = data;

            document.querySelector('.tag_box')?.remove();

            const areaBox = document.createElement('DIV');
            areaBox.className = 'tag_box';

            const headerTit = document.createElement('P');
            headerTit.className = 'header-tit';
            headerTit.innerHTML = `<span class="txt">${displayName}</span>
                    <span class="close"></span>`;

            areaBox.appendChild(headerTit);
            areaBox.querySelector('.close').addEventListener('click', () => {
                document.querySelector('.tag_box').remove();
            })


            const tagInfo = document.createElement('UL');

            tagInfo.innerHTML +=
                    `<li>
                        <span class="tit">이름 : </span>
                        <span class="txt">${displayName}</span>
                        <span>&nbsp; / &nbsp;</span>
                        <span class="tit">태그 : </span>
                        <span class="txt">${id}</span>
                    </li>`;



            areaBox.appendChild(tagInfo);

            document.querySelector('BODY').appendChild(areaBox);
        });



        fetch(`/adm/evacRoute/getRoute.json?mapNo=${mapNo}`)
            .then(res => res.json())
            .then(data => {
                if(data.result == null) {
                    return;
                }
                Px.Evac.Import(data.result.routeJson);
                Px.Evac.HideAll();

                renewPointMeshStatusByFloor();
            });

    },
    changePoiCategory1: (poiCategory1Nos) => {
    },
    changeFloorGroup: async (currentFloorIds, isAll) => {

        // console.log(currentFloorIds);
        // $('#poi-pop').hide();
        // $('#poi-pop-0').hide();

        // $('.floor').removeClass('on');

        // let $targetDom = null;
        // if (isAll) {
        //     $targetDom = $('.floor[data-floor-group-no="4"');
        // } else {
        //     $targetDom = $('.floor[data-floor-group-no="' + currentFloorIds[0] + '"');
        // }
        // $targetDom.addClass('on');
        // const floorNm = $targetDom[0].dataset.floorGroupNm;

        // $('.floor-area .state').text(floorNm);

        // if (currentFloorIds[0] === '4' && !isAll && energyFlag) { // 전력흐름도 상태
        //     PlxWebgl.Floor.changeFloorGroup();
        // }

        // if (isAll && energyFlag) Px.Poi.SetVisibleOptionAll(false, false, false);
        // else Px.Poi.SetVisibleOptionAll(true, true, true);

        // if (!isAll) {
        //     PlxWebgl.CamPos.changeCamPos('3D');
        // } else {

        // }
    },
    events: [{
        type: 'poi',
        action: 'pointer-up',
        callback: (res) => {

        },
    },
    {
        type: 'poi',
        action: 'click',
        callback: (res) => {

        },
    },
    {
        type: 'poi',
        action: 'dblclick',
        callback: (res) => {
            showPoiPopup(res.property.poiData);
        },
    },
    ],
    poiEvents: [

    ],
};

const config = {
    api: {
        getMap: { // MAP INFO 가져오는 url
            url: `${_CONTEXT_PATH_}api/viewer/mapInfo.json`,
            param: null,
            callback: (res) => {
                const { camPosList } = res;
		        if(!camPosList) return;
		        camPos.setData(camPosList);
            },
        },
        getPoi: { // POI 가져오는 url
            url: `${_CONTEXT_PATH_}api/viewer/poiList.json`,
            param: null,
            callback: null,
            isSync: true,
        },
        modifyPoi: { // POI 수정 url
            url: `${_CONTEXT_PATH_}adm/poi/poiModify.json`,
            param: null,
            callback: null,
        },
        getLod: {
            url: `${_CONTEXT_PATH_}api/viewer/poiLodInfo.json`,
            param: null,
            callback: null,
        },
        getPoiCategory: {
            url: `${_CONTEXT_PATH_}api/viewer/poiCategoryList.json`,
            param: null,
            callback: (res) => {
                if (res.result.length > 0) {
                    category1List = res.result;
                    let html = '';

                    if(category1List.length > 1) {
                        html += `<li class="poi00" data-category1-no='all'>                            
                        <span class="txt">전체</span>
                        </li>`;
                    }

                    let html2 = '';

                    category1List.forEach((cate1Info) => {
                        const paddedCateNo = String(cate1Info.category1No).padStart(2, '0');
                        html += `<li class="poi${paddedCateNo}" data-category1-no=${cate1Info.category1No}>
                                <span class="icon"></span>
                                <span class="txt">${cate1Info.category1Nm}</span>
                            </li>`
                        html2 += `<li class="menu_${paddedCateNo}" data-category1-no=${cate1Info.category1No}>
                                <span class="icon-wrap">
                                <span class="icon"></span>
                                <span class="txt">${cate1Info.category1Nm}</span>
                                </span>
                                <ul class="menu-list"></ul>
                            </li>`
                    })

                    document.querySelector('.mini-poi').innerHTML = html;
                    document.getElementById('category1List').innerHTML = html2;

                }
                const liCate1List = document.querySelectorAll('#category1List > LI');
                liCate1List.forEach((liCate1Item) => {
                    liCate1Item.addEventListener('pointerup', (evt) => {
                        const menuWrap = document.querySelector('.btn_menu_wrap');
                        const list = menuWrap.querySelector('.list');
                        const listLi = list.querySelectorAll('li');
                        const target = evt.currentTarget;
                        const hasOn = target.classList.contains('on');

                        if (!hasOn) {
                            for (li of listLi) {
                                li.classList.remove('on');
                            }
                            target.classList.add('on');
                        } else {
                            target.classList.remove('on');
                            return false;
                        }
                    });
                });


                showMiniPoiCategory();

            }
        },
        getPoiCategoryDetail: {
            url: `${_CONTEXT_PATH_}api/viewer/poiCategoryDetailList.json`,
            param: null,
            callback: (res) => {
            },
        },
    },
    camPos: { // 해당부분은 데모용
        dataStr: '{"test":{"position":{"x":-1317.61868286876,"y":6467.67399691508,"z":-10298.426886887142},"rotation":{"x":-2.5808207041821594,"y":-0.10792742551600473,"z":-3.0740459150260966},"target":{"x":100,"y":100,"z":100}}}',
    },
    modelExpand: { // 모델링 간격 조절
        interval: 1000, // 수치를 넣을경우 강제로 해당 interval 고정 기본값 0
        duration: 2000, // 모델링 벌려지는 시간 (단위 : ms)
        maxInterval: 700, // 모델링 최대 간격
        totalInterval: 1000, // 모델링 전체 간격의 합
    },
    modelCollapse: { // 모델링 접기
        duration: 2000, // 모델링 접히는데 걸리는시간 (단위 : ms)
    },
    mapInfo: {
        mapNo: mapNo, // 불러올 mapNo
        backGroundColor: 0xf2f6ff, // 배경색
        type: 'MULTI', // SBM or FBX
    },
    poi: {
        poiLineHeight: _POI_LINE_HEIGHT, // poi 붉은선 길이
        iconRatio: 1, // icon 확대시 배율
        textRatio: 1, // text 확대시 배율
        useDynamicIcons: false,
        statusArr: [0, 1, 2, 3, 4, 5],
    },
    contextPath: '', // contextPath 가 다를경우 기입
    detectResize: true, // resize 감지처리여부
    useFireEffect: false, // 화재효과 사용 여부
    isDebug: false, // console.log를 통한 debuging 여부
};

const renewPointMeshStatusByFloor = (floorGroupNo = 'all') => {

    const pointMeshStatus = Px.PointMesh.GetStatus();
    document.getElementById('totalCount').innerText = `${pointMeshStatus["floor"].total.length}` ?? "0";
    document.getElementById('currentFloorCount').innerText = pointMeshStatus["floor"][`${floorGroupNo !== 'all' ? floorGroupNo : 'total'}`]?.length ?? "0";

}

const getCurrentFloorGroupNo = () => {
    const currentFloorGroup = document.querySelector('.ul-floor > li.on');
    return currentFloorGroup?.dataset.floorGroupNo ?? 'all';
}