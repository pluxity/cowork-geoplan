/**
 *
 * 설명 : PluxityMap 생성자
 * Parameter :
 *      targetElementId : 타겟 엘리먼트 ID
 *      mapOptions = { *
 *
 *      }
 * Description : 2D 지도 및 POI 관리 객체	(카카오맵 전용)
 */

const PluxityMap = function(targetElementId, mapOptions , markerList) {

    // 지도 객체
    let map;
    let marker;
    let overlay;
    const markers = {};

    const userOptionMap = {
        "gumihospital": {
            center: new kakao.maps.LatLng(36.153134716579224, 128.29034018758654),
        },
        "andongsisul" : {
            center: new kakao.maps.LatLng(36.55685886174321, 128.55850425042837),
        },
        "gyeongsancrc" : {
            center: new kakao.maps.LatLng(35.801985769893214, 128.74736133606507),
        }
    }

    if(typeof targetElementId === 'undefined' || typeof targetElementId !== 'string') console.error('TargetElement Id 설정 필수');

    // Default Feature Style
    const defaultOptions = {
        center: null,
        level: 3
    };

    let username = USERNAME;
    switch (username) {
        case 'gumihospital':
        case 'andongsisul':
        case 'gyeongsancrc':
            defaultOptions.center = userOptionMap[username].center;
            break;
        default:
            console.warn('USERNAME이 없거나 userOptionMap에 없습니다. 기본값 사용.');
            defaultOptions.center = new kakao.maps.LatLng(36.153134716579224, 128.29034018758654);
    }

    let options = mapOptions ? mapOptions : defaultOptions;

    /**
     * 초기화
     * @returns {*}
     */
    function init(){
        /**
         * Parameter : targetElementId, options(카카오 MAP Options)
         * Return : 카카오 Map 객체 객체
         * Descripton : PluxityMap 초기화
         */
        const container = document.getElementById(targetElementId);
        map = new kakao.maps.Map(container, options);
        var zoomControl = new kakao.maps.ZoomControl();

        if(markerList.length > 0) {
            markerList.forEach((markerItem) => {
                const { position, draggable, clickable, mapNm, mapNo } = markerItem;
                marker = new kakao.maps.Marker({
                    map,
                    position,
                    draggable,
                    clickable
                });

                markers[mapNo] = marker;

                if(clickable) {
                    setMarkerEvent(marker, 'click', {mapNm, mapNo});
                }
            })
        }
        // 지도 오른쪽에 줌 컨트롤이 표시되도록 지도에 컨트롤을 추가한다.
        map.addControl(zoomControl, kakao.maps.ControlPosition.BOTTOMRIGHT);
        return map;
    }

    function setMapEvent(eventType, callback) {
        /**
         * Target : 이벤트 타겟(MAP, MARKER 등)
         * EventType : 이벤트 타입(click, dblclick)등
         * Callback : 이벤트 종료 후 Callback
         *
         * Desc : 관리자모드에서 등록할 때 사용 하겠지?
         * return : 없음
         */
    }
    function setMarkerEvent(marker, eventType, mapData) {
        /**
         * Marker : 이벤트 타겟 MARKER
         * EventType : 이벤트 타입(click, dblclick)등
         * Callback : 이벤트 종료 후 Callback
         *
         * Desc : 관리자모드에서는 좌표 얻어오고 / 그냥 뷰어에선 3D 화면으로 이동할 지 팝업 띄워야되려나?
         * return : 없음
         */
        kakao.maps.event.addListener(marker, eventType, () => {
            const { La, Ma } = marker.getPosition();
            getAddress(Ma, La, (result, status) => {
                if(status === kakao.maps.services.Status.OK) {
                    let addressName = '';
    
                    if(result[0].road_address) {
                        ({ address_name: addressName } = result[0].road_address);
                    } else {
                        ({ address_name: addressName } = result[0].address);
                    }

                    const content = `<div class="poi_box">
                                <p class="header-tit">
                                    <span class="txt">${mapData.mapNm}</span>
                                    <span class="close"></span>
                                </p>
                                <ul>
                                    <li>
                                        <span class="tit">주소 : </span>
                                        <span class="txt">${addressName}</span>
                                    </li>
                                    <li>
                                        <span class="txt"><a href="/viewer/index.do?mapNo=${mapData.mapNo}" >3D 뷰어 이동</a></span>
                                    </li>
                                </ul>
                            </div>`;

                showOverlay(marker, content);
                document.querySelector('.poi_box .header-tit .close').addEventListener('click', () => closeOverlay());
                }
            })
        });
    }

    function setCenter(lng, lat) {
        /**
         * Parameters : Lng, Lat (EPSG:5189 좌표계 lon, lat)
         */
        map.setCenter(new kakao.maps.LatLng(lat, lng));
    }

    function panTo(lat, lng) {
        map.panTo(new kakao.maps.LatLng(lat, lng));
    }

    function keywordSearch(params, keyCode, markerActive) {
        /**
         * Params
         *
         * return 정보
         */
        const searchList = document.getElementById('searchList');

        if(params.length === 0) {
            searchList.innerHTML = '';
            return;
        }

        const ps = new kakao.maps.services.Places();
        ps.keywordSearch(params, (data, status, pagination) => {
            if(status === kakao.maps.services.Status.OK) {
                const bounds = new kakao.maps.LatLngBounds();
                let html = '';
                data.forEach((place) => {
                    const {
                        y: lat,
                        x: lng,
                        place_name: name,
                    } = place;

                    html += `<div data-lat=${lat} data-lng=${lng}>${name}</div>`;

                    bounds.extend(new kakao.maps.LatLng(lat, lng));
                })

                searchList.innerHTML = html;

                const searchItems = document.querySelectorAll('#searchList > div');

                searchItems.forEach((searchItem) => {
                    searchItem.addEventListener('click', (event) => {
                        const target = event.target;
                        const placeNm = event.target.innerText;
                        const { lng, lat } = target.dataset;
                        
                        document.getElementById('searchInput').value = placeNm;
                        searchList.innerHTML = '';
                        // 관리자 모드일때
                        if(markerActive) {
                            this.setMarker(lat, lng, true, false);
                        }
                        this.setCenter(lng, lat);
                    })
                })
                if (keyCode === 13) {
                    map.setBounds(bounds);
                }
            }
        })

    }

    function setMarker(lat, lng, draggable, clickable) {
        /**
         * lat: 위도, lng: 경도
         * draggable: 관리자모드에서만 true
         * clickable : 뷰어에서만 true
         * 
         * Desc : 마커 생성
         * return : {la: 경도, ma:위도}
         */
        if(marker) {
            marker.setMap(null);
        }

        if(overlay) {
             closeOverlay();
        }

        marker = new kakao.maps.Marker({
            position: new kakao.maps.LatLng(lat, lng),
            draggable,
            clickable,
        });
        marker.setMap(map);
        map.setLevel(3);
    }

    function getMarkerPosition() {
        /**
         * Desc : 현재 표시되어있는 마커의 좌표 리턴
         * return : {la: 경도, ma:위도}
         */
        return marker.getPosition();
    }
    
    function getAddress(lat, lng, callback) {
        /**
         * 
         * Desc : 좌표로 주소 리턴
         */
        const geocoder = new kakao.maps.services.Geocoder();
        geocoder.coord2Address(lng, lat, callback);
    }

    function showOverlay(marker, content) {
        if(overlay) {
            closeOverlay();
        }

        overlay = new kakao.maps.CustomOverlay({
            content,
            map: map,
            position: marker.getPosition()
        });

        overlay.setMap(map);
    }

    function closeOverlay() {
        overlay.setMap(null);
    }

    /**
     * @param {*} x
     * @param {*} y
     *
     * return : Kakao LONLAT
     */
    function getLonLatFromPixel(x, y) {
        /**
         * Parameter : x, y (Pixel XY 좌표)
         * Return : 좌표값 리턴
         */

        let lonlat = '';

        return lonlat;
    }

    /**
     * @param {*} lon
     * @param {*} lat
     *
     * return : Pixel Object(x, y)
     */
    function getPixelFromLonLat(lon, lat){
        /**
         * Parameter : lon, lat (Lon, Lat 현재 지도의 좌표계)
         * Return : Pixel Object(x, y)
         */

        let pixel = '';
        return pixel;
    }

    /**
     *
     * @param mapNo
     * @returns marker 정보
     */
    function getMarker(mapNo) {
        return markers[mapNo];
    }

    init();

    return {
        getMap: function() { return map; }
        ,setMapEvent
        ,setMarkerEvent
        ,setCenter
        ,panTo
        ,keywordSearch
        ,getLonLatFromPixel
        ,getPixelFromLonLat
        ,getMarkerPosition
        ,setMarker
        ,getAddress
        ,showOverlay
        ,closeOverlay
        ,getMarker
        ,init
    }
}