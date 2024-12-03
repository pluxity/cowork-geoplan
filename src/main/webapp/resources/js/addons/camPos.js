var camPos = (function () {
	var _data = {};

	function setData(camDataList) {
		if(!camDataList) return;
		camDataList.forEach((camData) => {
			const { floorNo } = camData;
			let { camPosJson } = camData;
			if(typeof(camPosJson) === 'string') {
				camPosJson = JSON.parse(camPosJson);
			} 
			_data[floorNo] = camPosJson;
		})
	}

	function setCurrentCamPos (id) {	//현재 화면값을 저장 id 필수.
		id = id === 0 ? '0' : id;
		if(!id) return;

		var camPos = Px.Camera.GetState();
		_data[id] = camPos;
		return _data[id];
	}

	function changeCamPos(id, callback) {	//cam 화면 이동
		var camPos = _data[id];
		if(!camPos) {
			Px.Camera.ExtendView();
			return;
		}

		camPos = Object.assign({},camPos);
		if(callback) camPos.onComplete = callback;
		Px.Camera.SetState(camPos);
		return camPos;
	}

	function getAllDatas() {	//모든 camPos데이터 반환
		return _data;
	}

	function getData(id) {	//id로 camPos 데이터 반환
		return _data[id];
	}

	function save(mapNo, floorNo) {	//mapNo 로 데이터 저장
		if(!mapNo) return;
		if(!confirm("현재 화면설정을 저장하시겠습니까?")) return;

		var formData = new FormData();
		formData.enctype = "multipart/form-data";
		formData.append('mapNo', mapNo);
		formData.append('floorNo', floorNo);
		formData.append('camPosJson', JSON.stringify(_data[floorNo]));

		$.ajax({
			url: '/adm/camPos/camPosModify.json',
			type: 'POST',
			dataType : 'json',
			enctype: 'multipart/form-data',
			contentType: false,
			processData: false,
			data : formData,
			success: function(res) {
				alert(res.resultMsg);
			},
			error : function(e){
				//console.log(e);
			}
		});
	}

	function getFloorCamPos(mapNo, floorNo, callback) {
		if (_data[floorNo]) {
			if(callback) callback(floorNo);
			return;
		}

		$.ajax({
			method: "post",
			url: "/adm/camPos/camPosInfo.json",
			data: { "mapNo": mapNo, "floorNo": floorNo },
			dataType: 'json',
			async: false,
			success: function (res) {
				if (jResult(res)) {
					const { camPosInfo } = res;
					const { floorNo, camPosJson } = camPosInfo;
					data[floorNo] = JSON.parse(camPosJson);
					
					if(callback) callback(floorNo);
				}
			}
		})
	}

	return {
		setData: setData,
		setCurrentCamPos: setCurrentCamPos,
		changeCamPos: changeCamPos,
		getAllDatas: getAllDatas,
		getData: getData,
		save: save,
		getFloorCamPos: getFloorCamPos,
	};
})();