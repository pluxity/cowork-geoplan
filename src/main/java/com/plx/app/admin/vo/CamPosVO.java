package com.plx.app.admin.vo;

import java.io.Serializable;

import lombok.Data;

@Data
@SuppressWarnings("serial")
public class CamPosVO implements Serializable{
    private int camPosNo;
    private int mapNo;
    private String floorNo;
    private String camPosJson = "";
}
