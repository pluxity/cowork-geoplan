package com.plx.app.admin.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 *  @Project KNIS
 *  @Class UsrgrpMapVO
 *  @since 2019. 12. 26.
 *  @author 류중규
 *  @Description : 사용자그룹 도면 VO
 */
@Data
@SuppressWarnings("serial")
public class UsrgrpMapVO implements Serializable {
	private int grpNo;
	private int mapNo;
	private String regUsr = "";
	private String regDt = "";
	private String mapNm = "";

	private int category2No;
	private String category2Nm = "";
	private String lat = "";
	private String lng = "";

	private List<String> usrgrpMapNoList;
}
