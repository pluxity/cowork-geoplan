package com.plx.app.admin.vo;

import java.io.Serializable;

import com.plx.app.cmn.vo.BaseVO;

import lombok.Data;

@Data
@SuppressWarnings("serial")
public class AnalyticsVO extends BaseVO implements Serializable {
    private int analyticsNo;
    private int mapNo;
    private String regDt = "";

    private String mapNm = "";
    
    private String sDate = "";
    private String eDate = "";

    private int category3No;

}
