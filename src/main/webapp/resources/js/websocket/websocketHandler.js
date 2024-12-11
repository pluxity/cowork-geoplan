const socket = new WebSocket("/ws");

socket.onopen = () => {
    console.log("Connected to WebSocket");
};

socket.onmessage = (event) => {
    const data = JSON.parse(event.data);

    if(data.location && data.id && data.floor) {
        Px.PointMesh.SetPoints(data);
    }

};

socket.onclose = () => {
    console.log("Disconnected from WebSocket");
};