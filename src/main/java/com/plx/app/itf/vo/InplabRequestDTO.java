package com.plx.app.itf.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

@Getter
public class InplabRequestDTO {

    String apikey;
    String id;

    @JsonProperty("building_code")
    String buildingCode;

    Double lon;
    Double lat;
    String floor;
    String name;

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}"; // JSON 변환 실패 시 빈 JSON 반환
        }
    }
}
