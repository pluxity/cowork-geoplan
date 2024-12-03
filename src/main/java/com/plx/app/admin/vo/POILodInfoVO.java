package com.plx.app.admin.vo;

import java.io.Serializable;

import com.plx.app.cmn.vo.BaseVO;

import lombok.Data;

/**
 *  @Project KNIS
 *  @Class POILodInfoVO
 *  @since 2019. 11. 10.
 *  @author 류중규
 *  @Description : POI LOD 정보
 */
@Data
@SuppressWarnings("serial")
public class POILodInfoVO extends BaseVO implements Serializable {

	private int mapNo;
	private Integer floorNo;
	private Integer category1No;
	private Integer category2No;
	private int iconSize0;
	private int iconSize1;
	private int iconSize2;
	private int iconSize3;
	private int iconSize4;
	private int iconSize5;
	private int iconSize6;
	private int iconSize7;
	private int iconSize8;
	private int iconSize9;
	private int lodType0;
	private int lodType1;
	private int lodType2;
	private int lodType3;
	private int lodType4;
	private int lodType5;
	private int lodType6;
	private int lodType7;
	private int lodType8;
	private int lodType9;

}
