package com.plx.app.admin.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 *  @Project KNIS
 *  @Class UsrgrpPoiVO
 *  @since 2019. 12. 26.
 *  @author 류중규
 *  @Description : 사용자그룹 poi 표출/제어 VO
 */
@Data
@SuppressWarnings("serial")
public class UsrgrpPoiVO implements Serializable {
	private int grpNo;
	private Integer category1No;
	private String category1Nm = "";
	private String roleType = "";
	private String regUsr = "";
	private String regDt = "";

	private List<String> category1NoList;
}
