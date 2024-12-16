var evacRoute = {};

evacRoute.getRoute = function (param, callback) {   // 루트정보 가져오기

    param = param || {mapNo: 2}
    $.ajax({
        type: "POST",
        url: "/adm/evacRoute/getRoute.json",
        data: param,
        dataType: 'json',
        success: function (res) {
            //console.log(res);
            if (callback) callback(res.result);
        }
    });
}

evacRoute.remove = function (param, callback) { // 루트삭제

    param = param || {mapNo: 2}
    $.ajax({
        type: "POST",
        url: "/adm/evacRoute/deleteRoute.json",
        data: param,
        dataType: 'json',
        success: function (res) {
            //console.log(res);
            if (callback) callback(res.result);
        }
    });
}

evacRoute.upsert = function (param, callback) { // 루트upsert

    param = param || {mapNo: 2}
    $.ajax({
        type: "POST",
        url: "/adm/evacRoute/upsertRoute.json",
        data: param,
        dataType: 'json',
        success: function (res) {
            //console.log(res);
            if (callback) callback(res.result);
            alert('저장 되었습니다.');
        }
    });
}

evacRoute.loadRoute = function () {  //초기화

    Px.Evac.SetSize(2);
    Px.Evac.LoadArrowTexture('/resources/img/evacRoute/arrow.png', function () {
        evacRoute.getRoute({mapNo: mapNo}, function (result) {

            if(result) {
                //경로처리
                Px.Evac.Import(result.routeJson);
                //불효과 처리
                // var fireDatas = JSON.parse(result.fireJson);
                // fireEffect.setFireByCoordinate(fireDatas);
            }

            Px.Model.Expand({
                duration: 0, interval: 10, name: floorInfoList[0].floorId, onComplete: () => {
                }
            });
        });
    });
}

evacRoute.clear = function () { //경로초기화
    Px.Evac.Clear();
    if (typeof (fireEffect) === 'object' && typeof (fireEffect.removeEvacRouteFire) === 'function') {
        fireEffect.removeEvacRouteFire();
    }
}

evacRoute.btnFunction = {};
evacRoute.btnFunction.drawEditor = function (flag) {
    //console.log("drawEditor");
    if (flag) {
        Px.Model.Expand({
            duration: 0, interval: 10, name: floorInfoList[0].floorId, onComplete: () => {
                Px.Evac.DrawEditorOn();
            }
        });

    } else {
        Px.Evac.DrawEditorOff();
    }
}
evacRoute.btnFunction.removePoint = function (flag) {
    //console.log("removePoint");
    if (flag) Px.Evac.PointDeleterOn();
    else Px.Evac.PointDeleterOff();
}
evacRoute.btnFunction.removeLink = function (flag) {
    //console.log("removeLink");
    if (flag) Px.Evac.LinkDeleterOn();
    else Px.Evac.LinkDeleterOff();
}
evacRoute.btnFunction.clearRoute = function () {
    Px.Evac.Clear();
    Px.Fire.Clear();
}
evacRoute.btnFunction.saveRoute = function () { //저장

    Px.Model.Collapse({
        duration: 0, onComplete: () => {
            Px.Evac.DrawEditorOff();
            var evacData = Px.Evac.Export();
            var fireData = Px.Fire.GetData();

            var param = {
                mapNo: mapNo,
                routeJson: JSON.stringify(evacData),
                fireJson: JSON.stringify(fireData)
            }

            evacRoute.upsert(param, function (result) {
                //console.log(result, "upsertComplete");
            })
            Px.Model.Expand({
                duration: 0, interval: 10, name: floorInfoList[0].floorId, onComplete: () => {
                }
            });
        }
    });


}

evacRoute.btnFunction.removeFire = function () { //불삭제
    Px.Evac.DrawEditorOff();
    Px.Fire.DeleteByPointer();
}

evacRoute.btnFunction.createFire = function () { //불생성
    Px.Evac.DrawEditorOff();
    if (typeof (fireEffect) === 'object' && typeof (fireEffect.createEvacRouteFire) === 'function') {
        fireEffect.createEvacRouteFire();
    }
}