package com.plx.app.admin.vo;

import java.io.Serializable;

import com.plx.app.cmn.vo.BaseVO;

import lombok.Data;

/**
 *  @Project KNIS
 *  @Class MapHstVO
 *  @since 2019. 9. 25.
 *  @author 류중규
 *  @Description : 도면 이력 vo
 */
@Data
@SuppressWarnings("serial")
public class MapHstVO extends BaseVO implements Serializable {

	private int mapNo;
	private String mapVer = "";
	private String hstDesc = "";
	private int mapFileNo;
	private String mapFileNm = "";

}
