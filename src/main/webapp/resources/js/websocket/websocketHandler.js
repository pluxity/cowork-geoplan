const socket = new WebSocket("/ws");

socket.onopen = () => {
    console.log("Connected to WebSocket");
};

socket.onmessage = (event) => {
    const data = JSON.parse(event.data);

    const { payload, message } = data;

    const geoData = JSON.parse(message);

    if(!geoData["building_code"] || geoData["building_code"] !== mapCd) return false;

    if(payload === 'INPLAB') {
        // TODO lon, lat to location

    }

    if(geoData.location && geoData.id && geoData.floor) {
        Px.PointMesh.SetPoints(geoData);

        if(geoData.floor !== getCurrentFloorGroupNo()) {
            Px.PointMesh.HidePoint(geoData.floor);
        }

        renewPointMeshStatusByFloor(getCurrentFloorGroupNo());

    }

};

socket.onclose = () => {
    console.log("Disconnected from WebSocket");
};