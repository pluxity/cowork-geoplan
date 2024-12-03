package com.plx.app.admin.vo;

import java.io.Serializable;
import java.util.List;

import com.plx.app.cmn.vo.BaseVO;

import lombok.Data;

/**
 *  @Project KNIS
 *  @Class UsrInfoVO
 *  @since 2019. 12. 5.
 *  @author 류중규
 *  @Description : 사용자그룹 VO
 */
@Data
@SuppressWarnings("serial")
public class UsrgrpInfoVO extends BaseVO implements Serializable {

	private String grpNm = "";
	private String grpType = "";
	private String grpTypeNm = "";
	private String grpDesc = "";

	private List<String> grpNoList; 		// 다중체크
}
