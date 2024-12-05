package com.plx.app.itf.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GeoLocationResponseDTO {
    String id;
    String location;
    String floor;
    String name;

    @Builder
    public GeoLocationResponseDTO(String id, String location, String floor, String name) {
        this.id = id;
        this.location = location;
        this.floor = floor;
        this.name = name;
    }
}
