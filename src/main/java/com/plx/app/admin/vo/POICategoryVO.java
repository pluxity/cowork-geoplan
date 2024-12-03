package com.plx.app.admin.vo;

import java.io.Serializable;
import java.util.List;

import com.plx.app.cmn.vo.BaseVO;

import lombok.Data;

@Data
@SuppressWarnings("serial")
public class POICategoryVO extends BaseVO implements Serializable {

	private Integer category1No  ;
	private String category1Nm  ;
	private Integer category2No  ;
	private String category2Nm  ;
	private Integer iconsetNo    ;
	private Integer orderNo      ;

	private Integer poiCount;

	private String categoryType = "";
	private Integer categoryNo;
	private String categoryNm = "";
	private Integer mapNo;

	private String categoryCode;

	private POIIconsetVO poiIconset ;
	private List<POICategoryVO> poiCategoryList;

}
