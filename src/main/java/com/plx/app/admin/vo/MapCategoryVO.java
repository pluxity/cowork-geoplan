
package com.plx.app.admin.vo;

import java.io.Serializable;
import java.util.List;

import com.plx.app.cmn.vo.BaseVO;

import lombok.Data;

/**
 *  @Project KNIS
 *  @Class MapCategoryVO
 *  @since 2019. 9. 2.
 *  @author redmoonk
 *  @Description : 맵카테고리 VO
 */
@Data
@SuppressWarnings("serial")
public class MapCategoryVO extends BaseVO implements Serializable {

	private String categoryType = "";
	private Integer categoryNo;
	private String categoryNm = "";
	private Integer category1No;
	private String category1Nm = "";
	private Integer category2No;
	private String category2Nm = "";
	private Integer category3No;
	private String category3Nm = "";
	private Integer orderNo;

	private List<MapCategoryVO> mapCategoryList;

}