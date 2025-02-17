const socket = new WebSocket("/ws");

socket.onopen = () => {
    console.log("Connected to WebSocket");
};

socket.onmessage = (event) => {
    const data = JSON.parse(event.data);

    if(!data["building_code"] || data["building_code"] !== mapCd) {
        return false;
    }

    if(data.location && data.id && data.floor) {
        Px.PointMesh.SetPoints(data);

        if(data.floor !== getCurrentFloorGroupNo()) {
            Px.PointMesh.HidePoint(data.floor);
        }

        renewPointMeshStatusByFloor(getCurrentFloorGroupNo());

    }

};

socket.onclose = () => {
    console.log("Disconnected from WebSocket");
};