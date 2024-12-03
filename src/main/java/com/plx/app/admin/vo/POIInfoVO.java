package com.plx.app.admin.vo;

import java.io.Serializable;
import java.util.List;

import com.plx.app.cmn.vo.BaseVO;

import lombok.Data;

/**
 *  @Project KNIS
 *  @Class PoiInfoVO
 *  @since 2019. 9. 25.
 *  @author 류중규
 *  @Description : poi vo
 */
@Data
@SuppressWarnings("serial")
public class POIInfoVO extends BaseVO implements Serializable {

	//poi
	private int poiNo;
	private String poiNm = "";
	private String dvcCd = "";
	private Integer mapNo;
	private Integer floorNo;
	private Integer floorGroup;
	private String positionYn = "";
	private Integer category1No;
	private Integer category2No;
	private String posX = "";
	private String posY = "";
	private String posZ = "";
	private String rotX = "";
	private String rotY = "";
	private String rotZ = "";
	private int sclX;
	private int sclY;
	private int sclZ;
	private String fdirX = "";
	private String fdirY = "";
	private String poiStatus = "";
	private String poiStatusNm = "";
	private String urlLink = "";
	private String etcDesc = "";

	//private MapInfoVO mapInfo;
	private String mapNm = "";
	private FloorInfoVO floorInfo;
	private POICategoryVO poiCategory;
	private POIIconsetVO poiIconset;

	private List<String> category2NoList; 	// Tree 다중체크 조회용
	private List<String> poiNoList; 		// poi 다중체크
	
	//ImgFileUpload 관련
	private int imgFileNo;
	private String imgFileNm = "";
}
