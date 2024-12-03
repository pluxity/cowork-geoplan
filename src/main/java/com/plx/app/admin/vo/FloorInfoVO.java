package com.plx.app.admin.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.plx.app.cmn.vo.BaseVO;

import lombok.Data;

/**
 *  @Project KNIS
 *  @Class FloorInfoVO
 *  @since 2019. 9. 25.
 *  @author 류중규
 *  @Description : 층정보 vo
 */
@Data
@SuppressWarnings("serial")
public class FloorInfoVO extends BaseVO implements Serializable {

	private int mapNo;
	private String mapVer = "";
	private Integer floorNo;
	private Integer floorGroup;
	private int floorBase;
	private String floorId = "";
	private String floorNm = "";
	private String floorFileNm = "";
	private String floorFileType = "";
	private BigDecimal floorLvl;
	private String isMain = "";
}
