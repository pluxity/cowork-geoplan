

window.addEventListener('load', function(){

    let btnMenu = document.querySelector('.btn_menu_wrap > .btn_menu');
    let menuWrap = document.querySelector('.btn_menu_wrap');  
    let list = menuWrap.querySelector('.list');    
    let listLi = menuWrap.querySelectorAll('.list > li');   
    let menuList = document.querySelectorAll('.menu-list > li');
    let target; 

    let btnMapMenu = document.querySelector('.btn_map_wrap > .btn_menu');
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
    let has = menuWrap.classList.contains('on');

    if(mapWrap.classList.contains('on')){

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
    }

    else if(!has){
        menuWrap.classList.add('on');   

    }  else{
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
    let has = mapWrap.classList.contains('on');

    if(menuWrap.classList.contains('on')){

        menuWrap.classList.remove('on');
        mapWrap.classList.add('on'); 
    }

    else if(!has){
        mapWrap.classList.add('on');   

    }  else{
        mapWrap.classList.remove('on');  
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