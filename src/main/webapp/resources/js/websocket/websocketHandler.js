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
        geoData["location"] = applyHomography([geoData["lon"], geoData["lat"]]);
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

function applyHomography(point) {
    const H = [
        [2.94483114e+00, 1.92631663e+01, -2.58551147e+03],
        [2.28627747e+01, -2.90669428e+00, -4.44303655e+02],
        [-5.29496342e-02, 6.95871992e-03, 1.00000000e+00]
    ];
    let x = point[0], y = point[1];
    let vector = [x, y, 1];

    let xPrime = H[0][0] * vector[0] + H[0][1] * vector[1] + H[0][2] * vector[2];
    let yPrime = H[1][0] * vector[0] + H[1][1] * vector[1] + H[1][2] * vector[2];
    let wPrime = H[2][0] * vector[0] + H[2][1] * vector[1] + H[2][2] * vector[2];

    let modelX = xPrime / wPrime;
    let modelY = yPrime / wPrime;

    return `POINT Z(${modelX}, ${modelY}, 1.4)`;
}