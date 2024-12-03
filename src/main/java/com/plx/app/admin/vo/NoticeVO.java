package com.plx.app.admin.vo;

import java.io.Serializable;

import com.plx.app.cmn.vo.BaseVO;

import lombok.Data;


/**
 *  @Project SmartStationV3
 *  @Class NoticeVO
 *  @since 2020. 4. 22.
 *  @author 나동규
 *  @Description : 
 */
@Data
@SuppressWarnings("serial")
public class NoticeVO extends BaseVO implements Serializable {

	private Integer noticeNo;
	private String noticeTitle;
	private String noticeBody;
	
	private String sdate;
	private String edate;
	
	private String mapList;

	private boolean emergencyYn;
	private boolean activeYn;
}