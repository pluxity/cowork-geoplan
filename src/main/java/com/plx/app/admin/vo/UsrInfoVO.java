package com.plx.app.admin.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.plx.app.cmn.vo.BaseVO;

import lombok.Data;

/**
 *  @Project KNIS
 *  @Class UsrInfoVO
 *  @since 2019. 12. 5.
 *  @author 류중규
 *  @Description : 사용자 VO
 */
@Data
@SuppressWarnings("serial")
public class UsrInfoVO extends BaseVO implements Serializable {

	private int usrNo;
	private String usrId = "";
	@JsonIgnore
	private String usrPwd = "";
	private String usrNm = "";
	private Integer usrStatus;
	private String usrEmail = "";
	private String usrTel = "";
	private String usrDept = "";
	private String lastLoginDt = "";
	private String lastPwdChangeDt = "";

	// 그룹 정보
	private String grpNm = "";
	private String grpType = "";

	private boolean changePwdFlag = false;
	private List<String> usrNoList; 		// 사용자 다중체크
}
