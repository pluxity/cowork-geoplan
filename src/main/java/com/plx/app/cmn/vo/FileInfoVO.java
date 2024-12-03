package com.plx.app.cmn.vo;

import java.io.Serializable;

import lombok.Data;

/**
 *  @Project KNIS
 *  @Class FileInfoVO
 *  @since 2019. 9. 5.
 *  @author redmoonk
 *  @Description : 파일 공통 VO
 */
@Data
@SuppressWarnings("serial")
public class FileInfoVO extends BaseVO implements Serializable {

	private int fileNo;
	private String fileType = "";
	private String filePath = "";
	private String fileSaveNm = "";
	private String fileRealNm = "";

}
