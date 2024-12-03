(function () {
    const { lat, lng } = document.getElementById('map').dataset;
    let plxMap;

    const latlng = new kakao.maps.LatLng(lat, lng);
    if(lat !== '' && lng !== '') {
        const option = {
            center: latlng,
            level: 3,
        }

        const marker = [{
            position: latlng,
            draggable: true,
            clickable: false,
        }];

        plxMap = new PluxityMap('map', option, marker);
    } else {
        plxMap = new PluxityMap('map');
    }
    
    // 검색
    const searchInput = document.querySelector('#searchInput');

    searchInput.addEventListener('keyup', (event) => {
        plxMap.keywordSearch(event.target.value, event.keyCode, true);
    });

    searchInput.addEventListener('focus', (event) => {
        if(event.target.value) {
            plxMap.keywordSearch(event.target.value, '', false);
        }
    });

    // 창닫을때 좌표 저장 
    window.addEventListener('beforeunload', (event) => {
        const currentPosition = plxMap.getMarkerPosition();

        window.opener.document.getElementsByName('lat')[0].value = currentPosition.Ma;
        window.opener.document.getElementsByName('lng')[0].value = currentPosition.La;

    });
})();
