package com.plx.app.cmn.vo;

import java.io.Serializable;
import java.util.List;

import com.plx.app.admin.vo.UsrgrpMapVO;
import com.plx.app.admin.vo.UsrgrpMenuVO;
import com.plx.app.admin.vo.UsrgrpPoiVO;

import lombok.Data;

@Data
@SuppressWarnings("serial")
public class LoginInfoVO implements Serializable {

	public static final String LOGIN_INFO = "LOGIN_INFO";
	//public static final String GROUP_MENU_LIST = "MENU_LIST_INFO";

	private int usrNo;
	private String usrId = "";
	private String usrNm = "";
	private String usrDept = "";
	private Integer usrStatus;
	private String usrEmail = "";
	private int grpNo;
	private String grpNm = "";
	private String grpType = "";

	private List<UsrgrpMapVO> usrgrpMapList;	// 도면 리스트
	private List<UsrgrpPoiVO> usrgrpPoiList;	// poi대분류 리스트
	private List<UsrgrpMenuVO> usrgrpMenuList;	// 메뉴 리스트
	private List<MenuVO> leftMenuList;			// 좌측 메뉴 생성


}
