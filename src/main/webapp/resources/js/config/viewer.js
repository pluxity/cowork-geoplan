var globalConfig = {
		"poi" : {
			"iconRatio" : _POI_ICON_RATIO,	//poi 확대배율
			"textRatio" : _POI_TEXT_RATIO,	//poi 텍스트 확대 배율
			"lineHeight" : _POI_LINE_HEIGHT, //poi 길이
		},
		"modelExpand" : {	//모델링 간격 조절
			"name": '',
			"interval" : 10,	//수치를 넣을경우 강제로 해당 interval 고정 기본값 0
			"duration" : 50,	//모델링 벌려지는 시간 (단위 : ms)
			"maxInterval" : 700,	//모델링 최대 간격
			"totalInterval" : 1000,	//모델링 전체 간격의 합
		},
		"flag" : {
			"changeFloor" : true	//층 변경중인지 처리. 나중에 리팩토링할때 제외하는게 좋을듯
		},
		"touch" : {
			"timeGap" : 500,	//ms. 해당 시간동안 터치하고있어야 작동
			"distGap" : 20, 	//px 해당 범위를 벗어나게 되면 터치무브로 인식
		},
		"camera" : {
			"category1No" : 1 
		}
};
