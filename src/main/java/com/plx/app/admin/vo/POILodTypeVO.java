package com.plx.app.admin.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.plx.app.cmn.vo.BaseVO;

import lombok.Data;

/**
 *  @Project KNIS
 *  @Class POILodType
 *  @since 2019. 11. 10.
 *  @author 류중규
 *  @Description : POI LOD 타입
 */
@Data
@SuppressWarnings("serial")
public class POILodTypeVO extends BaseVO implements Serializable {

	private int mapNo;
	private Integer floorNo;
	private int levelCnt;
	private BigDecimal maxDist;

	private List<POILodInfoVO> poiLodInfoList;
}
