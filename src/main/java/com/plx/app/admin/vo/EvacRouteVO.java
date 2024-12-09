package com.plx.app.admin.vo;

import com.plx.app.cmn.vo.BaseVO;
import lombok.Data;

import java.io.Serializable;


/**
 *  @Project SKT_TSOP
 *  @Class POIInfoVO
 *  @since 2019. 10. 31.
 *  @author Master
 *  @Description :
 */
@Data
@SuppressWarnings("serial")
public class EvacRouteVO extends BaseVO implements Serializable {

	private Integer routeNo;
	private String routeNm;
	private Integer mapNo;
	private String routeJson;
	private String fireJson;
}