

window.addEventListener('load', function(){

    let btnMenu = document.querySelector('.btn_menu_wrap > .btn_menu');
    let menuWrap = document.querySelector('.btn_menu_wrap');  
    let list = menuWrap.querySelector('.list');    
    let listLi = menuWrap.querySelectorAll('.list > li');   
    let menuList = document.querySelectorAll('.menu-list > li');
    let target; 

    let btnMapMenu = document.querySelector('.btn_map_wrap > .btn_menu');
    let btnEventMenu = document.querySelector('.btn_event_wrap > .btn_menu');
    let submenuLi = document.querySelectorAll('.submenu-list > li');

    let searchInputList = document.querySelectorAll('.input_search__poi');
    let searchList = document.querySelector('.search_list');
    let searchLi = document.querySelectorAll('.search_list > li');

    let miniPoiLI = document.querySelectorAll('.mini-poi > li');
    let miniMapIcon = document.querySelectorAll('.mini_map_icon > li');
    
    let floorWrap = document.querySelector('.floor_wrap');
    let btnAll = document.querySelector('.btn_all');
    let ulFloorli = document.querySelectorAll('.ul-floor > li ');

    let poiBox = document.querySelector('.poi_box');
    let poiBoxClose = document.querySelector('.poi_box .close');  

    let playerWrap = document.querySelectorAll('.player-wrap > span');

    let pathSearch = document.querySelectorAll('.pathSearch');

    //길찾기 버튼 클릭 시 
    btnMapMenu.addEventListener('click', menuMapClick);
    btnEventMenu.addEventListener('click', eventMenuClick);

    // 메뉴 버튼 클릭 시 
    btnMenu.addEventListener('click', menuClick);
    
    // 대메뉴 클릭 시
    for(li of listLi){
        li.addEventListener('click' , menu1Click);
    }

    // 중메뉴 클릭 시    

    for(li of menuList){
        
        li.addEventListener('click', menu2Click );

    }


    // 소메뉴 클릭 시 

    for(li of submenuLi){
        li.addEventListener('click' , subClick);
    }

    //길찾기 핀 클릭 시 
    let pin = document.querySelectorAll('.icon.pin');

    // for(icon of pin){
    //     icon.addEventListener('click', pinClick);
    // }

    function handlerInputSearch(event) {
        const cTarget = event.currentTarget;
        cTarget.removeAttribute('poi-id');
        const targetUL = cTarget.nextElementSibling.querySelector('UL');

        if(cTarget.value === '') {
            targetUL.classList.remove('on');

            return ;
        }
        targetUL.classList.add('on');

        $('.srch-list li').remove(); //해당 검색 내역 삭제

        // var $resultArea = $(".srch-list ul");
        const $resultArea = $(targetUL);
        const searchKeyword = cTarget.value;

        const isFindPathSearch = cTarget.classList.contains('start') || cTarget.classList.contains('end');

        const template = isFindPathSearch? $('#searchFindPathPoiListTpl').html() : $('#searchPoiListTpl').html();

        const maxLength = 5;

        const url = "/api/viewer/poiList.json";
        const param = {
            mapNo: mapNo,
            searchType: "poiNm",
            searchKeyword: searchKeyword,
            positionYn: "Y",
            pageSize: 5
        };

        ajaxTemplate(url, param, template, $resultArea);
        return false;
    }

    //검색창
    for(const searchInput of searchInputList) {
        searchInput.addEventListener('keyup' , handlerInputSearch, false);
        searchInput.addEventListener('search' , handlerInputSearch, false);
    }




    //미니poi

    // for(li of miniPoiLI) {
    //     li.addEventListener('click' , function(e){
    //         let t = e.currentTarget;

    //         t.classList.toggle('on');


    //     });    

    // }

    //미니맵아이콘

    //전체층

    btnAll.addEventListener('click',function(){
        floorWrap.classList.toggle('on');
    });

    //층수
    // for(li of ulFloorli){

    //     li.addEventListener('click',function(e){
    //         let t = e.currentTarget;
    //         let has = t.classList.contains('on');

    //         if(!has){
    //             for(li of ulFloorli){
    //                 li.classList.remove('on');
    //             }
    //             t.classList.add('on');
    //             PlxWebgl.Floor.change
    //         }
        
            
    //     });
    // }

    // //poi 팝업닫기

    // poiBoxClose.addEventListener('click',function(){
    //     poiBox.classList.remove('on');
    // });
    

    //플레이어


    for(li of playerWrap){

        li.addEventListener('click',function(e){
            let t = e.currentTarget;

            for(li of playerWrap){
                li.classList.remove('on');
                t.classList.add('on');
            }
        });

    }


    document.getElementById('btnEvacuationRoute').addEventListener('click', evacuationRouteClick);
    document.getElementById('btnLocator').addEventListener('click', locatorClick);

    
});


const btnDrive = document.querySelector('.btn_drive');
const findMap = document.querySelector('.find-map');
const customerType = document.querySelector('.customer-type');
const driveHeader = document.querySelector('.drive-header');
const drive3d = document.querySelector('.drive-3d');
const playDirection = document.getElementById('playDirection');

const findPathToggle = () => {
    findMap.classList.toggle('d-none');
    customerType.classList.toggle('d-none');
    driveHeader.classList.toggle('d-none');
    drive3d.classList.toggle('d-none');
    playDirection.className = 'play';
}

const findPathInit = () => {
    findMap.classList.remove('d-none');
    customerType.classList.remove('d-none');
    // document.querySelector('.drive').classList.toggle('d-none');
    driveHeader.classList.add('d-none');
    drive3d.classList.add('d-none');

    if(Px.Topology.Animation.IsPlaying()) {
        Px.Topology.Animation.Stop();
        camPos.changeCamPos('current');
    }
}



const menuClick = (e) => {

    let mapWrap = document.querySelector('.btn_map_wrap');
    let menuWrap = document.querySelector('.btn_menu_wrap');
    let eventWrap = document.querySelector('.btn_event_wrap');
    let has = menuWrap.classList.contains('on');

    if(mapWrap.classList.contains('on') || eventWrap.classList.contains('on')){

        eventWrap.classList.remove('on')
        mapWrap.classList.remove('on');

        menuWrap.classList.add('on'); 
        findPathInit();
        document.querySelector('BUTTON.btn_reset').dispatchEvent(new Event('click'));
        const { floorGroupNo } = document.querySelector('.floor_wrap .box .txt').dataset;
        
        if(floorGroupNo === 'all') {
            camPos.changeCamPos('all');        
            // Px.Model.Collapse({
            //     duration:0,
            //     onComplete: () => {
            //     }
            // });
        }
    } else if(!has) {
        menuWrap.classList.add('on');   

    } else {
        menuWrap.classList.remove('on');   

    }

}

const menu1Click = (e) => {   
    let menuWrap = document.querySelector('.btn_menu_wrap');
    let list = menuWrap.querySelector('.list');    
    let listLi = list.querySelectorAll('li');   
    let target = e.currentTarget;
    let hasOn = target.classList.contains('on'); 


    if(!hasOn){
        for(li of listLi){
            li.classList.remove('on'); 
        }
        target.classList.add('on'); 
    } else {
        return false;
    }
}

const menu2Click = (e) => {
    e.preventDefault();
    
    let t = e.currentTarget;
    let has = t.classList.contains('on');
    let menuList = document.querySelectorAll('.menu-list > li');    

    if(!has){

        for(li of menuList){
                li.classList.remove('on');
        }

        t.classList.add('on');
    }
}

const menuMapClick = () => {
    
    let mapWrap = document.querySelector('.btn_map_wrap');
    let menuWrap = document.querySelector('.btn_menu_wrap');
    let eventWrap = document.querySelector('.btn_event_wrap');

    let has = mapWrap.classList.contains('on');

    const targetFloor = Px.Model.GetHierarchy()[0]["name"];

    // if(has) {
    //     Px.Model.Collapse(() => {})
    // } else {
    //     Px.Model.Expand({
    //         "duration":10,
    //         "interval":10,
    //         "name": targetFloor,
    //         "onComplete":()=>{}});
    // }



    if(menuWrap.classList.contains('on') || eventWrap.classList.contains('on')){
        menuWrap.classList.remove('on');
        eventWrap.classList.remove('on');
        mapWrap.classList.add('on');
    } else if(!has) {
        mapWrap.classList.add('on');
    } else {
        mapWrap.classList.remove('on');  
        findPathInit();
        document.querySelector('BUTTON.btn_reset').dispatchEvent(new Event('click'));
        const { floorGroupNo } = document.querySelector('.floor_wrap .box .txt').dataset;
        
        if(floorGroupNo === 'all') {
            camPos.changeCamPos('all');
        }
    }
}

const eventMenuClick = () => {

    let mapWrap = document.querySelector('.btn_map_wrap');
    let menuWrap = document.querySelector('.btn_menu_wrap');
    let eventWrap = document.querySelector('.btn_event_wrap');

    let has = eventWrap.classList.contains('on');

    if(menuWrap.classList.contains('on') || mapWrap.classList.contains('on')){
        menuWrap.classList.remove('on');
        mapWrap.classList.remove('on');
        eventWrap.classList.add('on');
    } else if(!has) {
        eventWrap.classList.add('on');
    } else {
        eventWrap.classList.remove('on');
    }
}

// const pinClick = (e) => {
//     let t = e.target;        
//     let has = t.classList.contains('on');

//     if(!has){       
//         t.classList.add('on');
//     }else {
//         t.classList.remove('on');
//     }
    
// }

const subClick = (e) => {
    let t = e.currentTarget;    
    t.classList.toggle('on');

}

const evacuationRouteClick = (e) => {
    const t = e.currentTarget;

    document.querySelector('.ul-floor > [data-floor-group-no="all"]').dispatchEvent(new Event('click'));

    if(document.getElementById('btnLocator').classList.contains('on')) {
        document.getElementById('btnLocator').dispatchEvent(new Event('click'));
    }

    if(t.classList.contains('on')) {
        Px.Model.Collapse({
            duration:10,
            onComplete: () => {
                Px.Evac.HideAll();
                t.classList.remove('on');
            }
        });

    } else {

        const ulFloor = document.querySelector('UL.ul-floor');
        const floorLi = ulFloor.querySelectorAll('LI');

        Px.Model.Expand({
            "duration":10,
            "interval":10,
            "name": "1",
            "onComplete":()=>{
                Px.Evac.ShowAll();
                t.classList.add('on');

            }});
    }
}

const locatorClick = (e) => {

    if(document.getElementById('btnEvacuationRoute').classList.contains('on')) {
        alert('대피경로 종료 후 사용해주세요.');
        return;
    }

    const t = e.currentTarget;
    t.classList.toggle('on');

    checkLocator(t.classList.contains('on'));

    // const onFloor = document.querySelector('.ul-floor > LI.on');
    // const currentFloor = onFloor.dataset.floorGroupNo;
    //
    // if(t.classList.contains('on')) {
    //     currentFloor === 'all' ? Px.PointMesh.Show_AllFloorObject() : Px.PointMesh.Show(currentFloor);
    // } else {
    //     currentFloor === 'all' ? Px.PointMesh.Hide_AllFloorObject() : Px.PointMesh.Hide(currentFloor);
    // }
}

const checkLocator = (locatorStatus = document.getElementById('btnLocator').classList.contains('on'),
                      floorGroupNo = document.querySelector('.ul-floor > LI.on').dataset.floorGroupNo) => {

    if(locatorStatus) {
        floorGroupNo === 'all' ? Px.PointMesh.Show_AllFloorObject() : Px.PointMesh.Show(floorGroupNo);
    } else {
        floorGroupNo === 'all' ? Px.PointMesh.Hide_AllFloorObject() : Px.PointMesh.Hide(floorGroupNo);
    }

}



const onPointEnterAlarmAreaCallback = (data) => {

    const title = data["area_name"];
    const tagId = data["pointId"];
    document.querySelector('BODY').appendChild(new AlarmPopup(title, tagId));

    const body = {
        "mapNo": mapNo,
        "areaName": data["area_name"],
        "tagId": data["pointId"],
        "displayName": data.pointData["displayName"]
    }

    fetch("/api/viewer/alarms", {
        "method": "POST",
        "headers": {
            "Content-Type": "application/json"
        },
        "body": JSON.stringify(body)
    }).then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
    })
    .then(result => {
        console.log("Success:", result);
    })
    .catch(error => {
        console.error("Error:", error);
    });



}

const onPointExitAlarmAreaCallback = (data) => {

    console.log(data);

    // TODO 알람 삭제?

}

class AlarmPopup extends HTMLElement {

    constructor(title, pointId) {
        super().attachShadow({mode: 'open'});

        this.setStyle();
        this.render(title, pointId);
    }

    setStyle() {
        const style = document.createElement('style');
        style.textContent = `
            :host {
                position: fixed;
                top: 50%;
                left: 50%;
                font-size: 16px; 
                transform: translate(-50%, -50%);
            }
        .emergency-alert {
            width: 25em;
            height: 13.875em;
            flex-shrink: 0;
            border: 5px solid #D80004;
            background: #350506;
            display: flex;
            flex-direction: column;
            padding: 0 0 1em 0;
        }
        .alert-header {
            background-color: #D80004;
            color: white;
            margin: 0;
            padding: .75em;
            text-align: center;
            font-weight: 700;
            font-size: 1em;
        }

        .alert-content {
            flex: 1;
            display: flex;
            flex-direction: column;
            align-items: center;
            padding: 1em;
        }

        .alert-row > SPAN {
            display: block;
        }
        
        table {
            width: 100%;
            color : #C9C9C9;
            border-spacing: 1.25em 1em;
        }

        table th {
            text-align: left;
            font-weight: 500;
        }

        .text-red {
            color: #D80004;
        }
        .text-white {
            color: #FFFFFF;
        }

        .button-wrapper {
            display: flex;
            justify-content: center;
        }

        .alert-button {
            width: 10em;
            height: 2.75em;
            padding: 0.625em 1.25em;
            background: #3C6BF1;
            color: white;
            cursor: pointer;
            border: none;
        }
         @keyframes blink {
              0% {
                box-shadow: 0 0 10px 5px red;
              }
              50% {
                box-shadow: 0 0 20px 10px red;
              }
              100% {
                box-shadow: 0 0 10px 5px red;
              }
        }
        
        .blinking-border {
          animation: blink 1s infinite;
        }
        `;

        this.shadowRoot.appendChild(style);
    }


    connectedCallback() {
    }


    render(title = "위치 정보 없음", pointId = "없음") {

        const fragments = document.createDocumentFragment();
        const emergencyAlert = document.createElement('DIV');
        emergencyAlert.classList.add('emergency-alert');
        emergencyAlert.classList.add('blinking-border');
        emergencyAlert.setAttribute('role', 'alert');

        const date = new Date();
        const options = {
            year: 'numeric',
            month: 'long',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            hour12: false
        };

        const formatter = new Intl.DateTimeFormat('ko-KR', options);

        emergencyAlert.innerHTML = `
              <h2 class="alert-header">${title}</h2>
              
              <div class="alert-content">
                <table>
                    <tr>
                        <th class="alert-label">위치</th>
                        <td class="alert-value text-red">${title}</td>
                      </tr>
                      <tr>
                        <th class="alert-label">태그 정보</th>
                        <td class="alert-value text-red">${pointId}</td>
                      </tr>
                      <tr>
                        <th class="alert-label">발생 일시</th>
                        <td class="alert-value text-white">${formatter.format(date)}</td>
                      </tr>
                </table>
              </div>
    
              <div class="button-wrapper">
                <button class="alert-button" tabindex="0">이벤트 확인</button>
              </div>
        `;

        emergencyAlert.querySelector('BUTTON').addEventListener('click', ()=> {
            this.remove();
        })

        fragments.appendChild(emergencyAlert);
        this.shadowRoot.appendChild(fragments);
    }

}


customElements.define('alarm-popup', AlarmPopup);
