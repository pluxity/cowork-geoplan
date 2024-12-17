/**
 * 길찾기 관련 클라스
 * @var _tMap 토폴로지 정보 담은 JSON Object { 일반인: topoJsonString, 교통약자: topoJsonString }
 *
 */
class Direction {

    /**
     * tMap 접근 및 수정을 위한 getter/setter
     * @param tMap
     */
    get getTopologyMap() { return this._tMap; }
    set setTopologyMap(tMap) { this._tMap = tMap; }

    /**
     * 생성자
     * this._tMap = Topology Data Map
     */
    constructor() {
        this.init().then(res => {
            this._tMap = res;
        });

        this.doTopologySettings();
        this.setDirectionHandler();
    }

    async init() {
        const tMap = await this.getTopologyList();
        if(!tMap) {
            return false;
        }

        return tMap;
    }

    /**
     * Px.Topology 설정 함수
     */
    doTopologySettings() {
        Px.Topology.Data.SetSize(.2);
    }

    /**
     * 현재 토폴로지 타입 변경
     * @param tType
     */
    setCurrentTopo(tType) {
        const tJson = this._tMap[tType];

        if(tJson.length < 1){
            return false;
        }
        Px.Topology.Data.Clear();
        Px.Topology.Data.Import(tJson);
        return true;
    }

    /**
     * 서버로부터 토폴로지 정보 가져오는 함수
     * @returns {Topology List}
     */
    async getTopologyList() {
        const url = `${_CONTEXT_PATH_}adm/topology/getTopologyList.json?mapNo=${mapNo}`
        const result = await axios(url).then((res) => res.data).catch((error) => console.error(error));

        const { resultCd, list } = result;

        if (resultCd === 'success' && list.length > 1) {
            const topoMap = {};
            for(const topoInfo of list) {
                topoMap[topoInfo.topoType] = topoInfo.topoJson;
            };

            return topoMap;
        }

        return false;
    }

    /**
     * POI Id로 길찾기용 poi정보로 변경
     * @param poiId
     * @returns {{id:POI ID, floorName: floorNo}}
     */
    getPoiInfo(poiId) {
        const result = {};
        const { position, property } = Px.Poi.GetData(poiId);

        result.x = position.x;
        result.y = position.y;
        result.z = position.z;
        result.floorName = property.floorId;

        return result;
    }

    /**
     * **********************************************************
     * **************** 길찾기 관련 이벤트 리쓰너 *******************
     * **************** ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ *******************
     * **********************************************************
     */

    /**
     * 길찾기 관련 EventListener
     */
    setDirectionHandler() {
        // 길찾기 버튼 리쓰너
        document.querySelector('BUTTON.btn_direction').addEventListener('click', this.handleBtnDirection);

        // 사용자 지정위치 POI 찍는 버튼
        const pointPins = document.querySelectorAll('.pin');
        pointPins.forEach((pin) => pin.addEventListener('click', (evt) => {
            evt.target.classList.add('on');
            const floorName = document.querySelector('.floor_wrap .box .txt').dataset.floorGroupNo;
            if(floorName === 'all') {
                pin.classList.remove('on');
                alert('전체 도면에서는 위치선택이 불가능합니다.');
                return false;
            }
            const type = evt.currentTarget.parentElement.children[0].classList.contains('start') ? 'start' : 'end';
            this.selectPoint(type, floorName);
        }));

        // 초기화버튼 이벤트 리스너
        document.querySelector('BUTTON.btn_reset').addEventListener('click', (evt) => {
            const inputSearchList = document.querySelectorAll('DIV.left_btn_wrap INPUT[type="search"]');
            inputSearchList.forEach((elem) => {
                elem.value = '';
                elem.removeAttribute('poi-id');
            }, false);
            removeCustomPoi();
            Px.Topology.PathFinder.Clear();
            document.querySelector('.radio-wrap .radio01 input').checked = true;
            document.querySelector('.drive').classList.remove('on');
        });

        /**
         * 여기 부터는 모의 주행 버튼 Listener
         */

        // 모의주행
        document.querySelector('.btn_drive').addEventListener('click', (event) => {
            document.querySelector('.drive').classList.remove('on');
            findPathToggle();
            camPos.setCurrentCamPos('current');
        });

        // 모의 주행 Play, Pause, Resume 버튼
        document.getElementById('playDirection').addEventListener('click', (evt)=> {
            const currentTarget = evt.currentTarget;
            const hasPlay = currentTarget.classList.contains('play');
            
            if(hasPlay) { // 시작 버튼 일 때
                const speed = parseInt(document.querySelector('.speed-wrap input:checked').value, 10);
                const height = 1.65;
                const camRotationSpeed = 0.1;

                if(Px.Topology.Animation.IsPause && Px.Topology.Animation.IsPause()) {  // Resume 버튼일 때
                    Px.Topology.Animation.Resume();
                } else {
                    Px.Topology.Animation.Play(speed, height, camRotationSpeed, this.onAnimationChangeFloor, this.onAnimationComplete);
                }
            } else {    // 정지 버튼 일 때
                Px.Topology.Animation.Pause();
            }

            currentTarget.classList.toggle('play');
            currentTarget.classList.toggle('pause');
        });
        
        // 모의 주행 정지 버튼
        document.getElementById('stopDirection').addEventListener('click', (evt)=> {
            // 일단은 Stop 시에 다 종료하고 화면도 원래대로
            Px.Topology.Animation.Stop();
            Px.Topology.Data.Clear();
            camPos.changeCamPos('current');
            document.getElementById('playDirection').className = 'play';
            // Px.Camera.ExtendView();
        });

        // 속도 조절 Radio
        document.querySelectorAll('.speed-wrap input').forEach((check)=> check.addEventListener('click', (event) => {
            if(Px.Topology.Animation.IsPlaying()){
                Px.Topology.Animation.SetSpeed(parseInt(event.target.value, 10));
            }
        }));

        // 모의 주행 종료 버튼
        document.querySelector('.btn_drive_end').addEventListener('click', () => {
            findPathToggle();
            camPos.changeCamPos('current');
            if(Px.Topology.Animation.IsPlaying()) {
                Px.Topology.Animation.Stop();
            }
        });
    }

    /**
     * 길찾기 애니메이션 시에 층변경 콜백
     * @param floorName
     */
    onAnimationChangeFloor(floorName) {
        // 층 변경 될 시..
        toggleFloorBtn(floorName); // viewerEventListener 에 있음
        Px.Model.Visible.HideAll();
        Px.Model.Visible.Show(floorName);
        Px.Topology.PathFinder.HideAll();
        Px.Topology.PathFinder.Show(floorName);
        Px.Poi.HideAll();
        Px.Poi.ShowByPropertyArray({'type': 'customPoi', floorId: floorName});
    }
    /**
     * 길찾기 애니메이션 종료 콜백
     */
    onAnimationComplete() {
        // 아마도 뭔가 알림을 줘야하지 않을까 싶긴해요
        // camPos.changeCamPos('current');
    }

    /**
     * 길찾기 버튼에 핸들러
     * @param evt
     */
    handleBtnDirection(evt) {
        const checkedVaidation = pxDirection.doCheckDirectionValidation();
        if(typeof checkedVaidation === 'string' ) {
            console.error(checkedVaidation);
            return;
        }
        const startPoi = pxDirection.getPoiInfo(checkedVaidation.startPoiId);
        const endPoi = pxDirection.getPoiInfo(checkedVaidation.endPoiId);
        const { topoType } = checkedVaidation;
        if (!pxDirection.setCurrentTopo(topoType)) {
            document.querySelector('BUTTON.btn_reset').dispatchEvent(new Event('click'));
            Px.Poi.Remove('start');
            Px.Poi.Remove('end');
            document.querySelector('.radio-wrap .radio01 input').checked = true;
            alert('경로 정보가 없습니다. 관리자에게 문의하세요.');
            return;
        }

        Px.Topology.Data.HideAll();
        try {
            Px.Topology.PathFinder.FindByPosition(startPoi, endPoi, true);
        } catch (e) {
            document.querySelector('BUTTON.btn_reset').dispatchEvent(new Event('click'));
            Px.Poi.HideByGroup('customPoi');
            document.querySelector('.radio-wrap .radio01 input').checked = true;
            alert('경로 정보가 올바르지 않습니다. 관리자에게 문의하세요.');
            return;
        }

        pxDirection.onAnimationChangeFloor(startPoi.floorName);
        // document.querySelector(`.ul-floor>li[data-floor-group-no='${startPoi.floorName}']`).dispatchEvent(new Event('click'));
        // Px.Poi.ShowByGroup('customPoi');

        if (startPoi.floorName !== endPoi.floorName) {
            const floorList = PlxWebgl.Floor.getAllFloors();

            Px.Camera.ExtendView();
            // Px.Model.Expand({
            //     name: floorList[0],
            //     group: floorList[0],
            //     interval: 10,
            //     duration: 50,
            //     onComplete: () => {
            //         Px.Camera.ExtendView();
            //     }
            // });
        }

        document.querySelector('.drive').classList.add('on');
    }

    /**
     * Validation 체크
     * @returns {string|{startPoiId: string, topoType: string, endPoiId: string}}
     */
    doCheckDirectionValidation() {
        // 시작점 POI ID 체크
        const startPoiId = document.querySelector('.input_search__poi.start').getAttribute('poi-id');
        if(!startPoiId) {
            return '시작 지점 없음';
        }
        // 종료좀 POI ID 체크
        const endPoiId = document.querySelector('.input_search__poi.end').getAttribute('poi-id');
        if(!endPoiId) {
            return '끝지점 없음';
        }
        // 경로조회 체크여부
        const checkedRadio = document.querySelector('INPUT[type="radio"]:checked')
        if(!checkedRadio) {
            return '라디오 버튼 체크 해야함';
        }

        return {
            startPoiId,
            endPoiId,
            topoType : checkedRadio.getAttribute('topo-type'),
        }
    }

    /**
     * 위치점 선택
     * @param type
     */
    selectPoint(type, floorName) {
        if(Px.Poi.GetData(type)) {
            Px.Poi.Remove(type);
        }

        Px.Poi.AddByMouse({
            id: type,
            group: 'customPoi',
            iconUrl: `/resources/img/icon/poiicon/customPoi/${type}.png`,
            lineHeight: config.poi.poiLineHeight,
            displayText: '',
            property: {
                floorId: floorName,
                type: 'customPoi',
            },
            scale: {
                x: 0.1,
                y: 0.1,
                z: 0.1,
            },

            onComplete: (id, x, y, z) => {
                const targetInput = document.querySelector(`.${type}`);
                // targetInput.value = `{x:${x}, y:${y}, z:${z}}`;
                targetInput.value = '사용자 지정위치';
                targetInput.setAttribute('poi-id', type);
                document.querySelector('.pin.on').classList.remove('on');
            },
        });
    }
}

const pxDirection = new Direction();