package com.plx.app.admin.vo;

import java.io.Serializable;
import java.util.List;

import com.plx.app.cmn.vo.BaseVO;

import lombok.Data;

/**
 *  @Project KNIS
 *  @Class UsrgrpMenuVO
 *  @since 2019. 12. 26.
 *  @author 류중규
 *  @Description : 사용자그룹 관리자메뉴 VO
 */
@Data
@SuppressWarnings("serial")
public class UsrgrpMenuVO implements Serializable {
	private int grpNo;
	private String allowUrl = "";
	private String regUsr = "";
	private String regDt = "";

	private List<String> allowUrlList;
}
