package com.plx.app.viewer.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AlarmResponseDTO implements Serializable {

    private Integer id;
    private Integer mapNo;
    private String areaName;
    private String tagId;
    private String displayName;
    private String regDt;

}


