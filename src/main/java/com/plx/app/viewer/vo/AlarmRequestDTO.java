package com.plx.app.viewer.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AlarmRequestDTO implements Serializable {

    private Integer mapNo;
    private String areaName;
    private String tagId;
    private String displayName;
}
