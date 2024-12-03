package com.plx.app.admin.vo;

import java.io.Serializable;

import com.plx.app.cmn.vo.BaseVO;

import lombok.Data;

@Data
@SuppressWarnings("serial")
public class TopologyInfoVO extends BaseVO implements Serializable{
    private Integer topoNo;
    private int mapNo;
    private String topoType;
    private String topoJson;
}
