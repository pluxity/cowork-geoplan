package com.plx.app.admin.vo;

import java.io.Serializable;
import java.util.List;

import com.plx.app.cmn.vo.BaseVO;

import lombok.Data;

/**
 *  @Project KNIS
 *  @Class MapInfoVO
 *  @since 2019. 9. 25.
 *  @author 류중규
 *  @Description : 도면정보 vo
 */
@Data
@SuppressWarnings("serial")
public class MapInfoVO extends BaseVO implements Serializable {

	private int mapNo;
	private String mapNm = "";
	private String mapStts = "";
	private String mapDesc = "";
	private int imgFileNo;
	private String imgFileNm = "";
	private String mapVer = "";
	private String fileType = "";
	private Integer defaultFloor;
	private Integer category1No;
	private Integer category2No;
	private Integer category3No;
	private String camPosJson = "";
	private String centerPosJson = "";
	private String mapCd = "";
	private String lat = "";
	private String lng = "";

	private List<String> category3NoList; 	// Tree 다중체크 조회용
	private MapCategoryVO mapCategory;
}
