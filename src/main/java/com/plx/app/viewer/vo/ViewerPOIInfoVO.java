package com.plx.app.viewer.vo;

import java.io.Serializable;

import com.plx.app.admin.vo.POIInfoVO;

import lombok.Data;


/**
 *  @Project KNIS
 *  @Class ViewerPOIInfoVO
 *  @since 2020. 4. 22.
 *  @author 유경식
 *  @Description : 뷰어용 POIINFO
 */
@Data
@SuppressWarnings("serial")
public class ViewerPOIInfoVO extends POIInfoVO implements Serializable {
	private String mapCd;
	private String evtTime;
}