(function () {

    let mapList = null;

    $.ajax({
        contentType:'application/json',
        dataType : 'json',
        url : `${_CONTEXT_PATH_}viewer/2d/getMapList.json`,
        async : false,
        type : 'GET',
        success:function(data){
            const { RESULT, MESSAGE, LIST } = data;
            if(RESULT !== 'success') {
                console.error(MESSAGE);
                return;
            }

            mapList = LIST;
        }
    });


    const markerList = [];
    mapList.forEach((map)=>{
        const { lat, lng, mapNm, mapNo } = map;
        if(lat!=='' && lng !== ''){
            markerList.push({
                position: new kakao.maps.LatLng(lat, lng),
                draggable: false,
                clickable: true,
                mapNm,
                mapNo, 
            });
        }
    });

    const plxMap = new PluxityMap('map','', markerList);

    // 도면리스트 생성
    const categoryList = document.getElementById('categoryList');
    mapList.forEach((map) => {
        if(!document.querySelector(`.category-item[data-category2-no="${map.category2No}"]`)) {
            const categoryItem = document.createElement('LI');
            categoryItem.classList.add('category-item');
            categoryItem.dataset.category2No = map.category2No;
            
            const categoryNm = document.createElement('SPAN');
            const tit = document.createElement('SPAN');
            tit.classList.add('tit');
            tit.innerText = map.category2Nm;

            const icon = document.createElement('SPAN');
            icon.classList.add('icon');
            
            const mapList = document.createElement('UL');
            mapList.classList.add('map-list');
            
            const mapItem = document.createElement('LI');
            const mapPin = document.createElement('SPAN');
            const mapNm = document.createElement('SPAN');
            mapItem.classList.add('map-item');
            mapItem.dataset.mapNo = map.mapNo;
            mapItem.dataset.lat = map.lat;
            mapItem.dataset.lng = map.lng;
            mapItem.dataset.mapNm = map.mapNm;
            mapNm.innerText = map.mapNm;
            
            mapItem.addEventListener('click' , (event) => mapSelectEvent(event));
            
            mapItem.append(mapPin);
            mapItem.append(mapNm);
            mapList.append(mapItem);
            categoryNm.append(tit);
            categoryNm.append(icon);
            categoryItem.append(categoryNm);
            categoryItem.append(mapList);
            categoryList.append(categoryItem);
        } else {
            const mapList = document.querySelector(`.category-item[data-category2-no="${map.category2No}"] > .map-list`);
            const mapItem = document.createElement('LI');
            mapItem.classList.add('map-item');
            const mapPin = document.createElement('SPAN');
            const mapNm = document.createElement('SPAN');
            mapItem.dataset.mapNm = map.mapNm;
            mapItem.dataset.mapNo = map.mapNo;
            mapItem.dataset.lat = map.lat;
            mapItem.dataset.lng = map.lng;
            mapNm.innerText = map.mapNm;
            
            mapItem.addEventListener('click' , (event) => mapSelectEvent(event));

            mapItem.append(mapPin);
            mapItem.append(mapNm);
            mapList.append(mapItem);
        }
    })

    document.querySelectorAll('.category-item').forEach((category)=> {
        const count = category.querySelectorAll('li').length;
        category.querySelector('span > .tit').innerText += ` (${count})`;
        category.addEventListener('click', () => {
            category.classList.toggle('on');
            document.querySelector('.map-list').scrollTo(0, 0);
        }) 
    });


    // 검색
    const searchInput = document.querySelector('#searchInput');

    searchInput.addEventListener('keyup', (event) => {
        plxMap.keywordSearch(event.target.value, event.keyCode, false);
    });

    searchInput.addEventListener('focus', (event) => {
        if(event.target.value) {
            plxMap.keywordSearch(event.target.value, '', false);
        }
    })

    if(document.getElementById('admin')) {
        document.querySelector('#admin > span').addEventListener('click', () => {
            location.href = `${_CONTEXT_PATH_}adm/main/index.do`;
        });
    }
    if(document.getElementById('logout')) {
        document.querySelector('#logout > span').addEventListener('click', () => {
            location.href = `${_CONTEXT_PATH_}login/logout.do`;
        });
    }
    if(document.getElementById('login')) {
        document.querySelector('#login > span').addEventListener('click', () => {
            location.href = `${_CONTEXT_PATH_}login/index.do`;
        });
    }

    const mapSelectEvent = (event) => {
        const { mapNm, mapNo, lat, lng } = event.currentTarget.dataset;
        
        if (document.querySelector('.map-item.on')) {
            document.querySelector('.map-item.on').classList.remove('on');
        }
        
        if(lat === '' && lng === '') {
            const contentBox = document.querySelector('.content-box');
            
            plxMap.init();

            if(contentBox) {
                contentBox.remove();
            }

            const popup = document.getElementById('coordEmptyPopup');
            const popupNode = document.importNode(popup.content.firstElementChild, true);

            const titleTxt = popupNode.querySelector('P.header-tit > SPAN.txt');
            titleTxt.innerHTML = `${mapNm}`;

            const hrefUri = `/viewer/index.do?mapNo=${mapNo}`;
            popupNode.querySelector('A').setAttribute('href', hrefUri);

            document.querySelector('body').appendChild(popupNode);
            popupNode.querySelector('.close').addEventListener('click', (evt) => {
                popupNode.remove();
            });
            return;
        }

        plxMap.panTo(lat, lng);

        event.currentTarget.classList.add('on');
    }

    document.getElementById('expand_button').addEventListener('pointerup', evt => {
        evt.preventDefault();

        const menuContainer = document.getElementById('menuContainer');
        menuContainer.classList.remove('collapsed');

        const btnCollapse = document.getElementById('collapse_button');
        btnCollapse.classList.remove('d-none');

        const btnExpand = document.getElementById('expand_button');
        btnExpand.classList.add('d-none');
    })
    document.getElementById('collapse_button').addEventListener('pointerup', evt => {
        evt.preventDefault();

        const menuContainer = document.getElementById('menuContainer');
        menuContainer.classList.add('collapsed');

        const btnCollapse = document.getElementById('collapse_button');
        btnCollapse.classList.add('d-none');

        const btnExpand = document.getElementById('expand_button');
        btnExpand.classList.remove('d-none');
    })

})();
