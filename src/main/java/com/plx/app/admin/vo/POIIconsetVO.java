package com.plx.app.admin.vo;

import java.io.Serializable;
import java.util.List;

import com.plx.app.cmn.vo.BaseVO;

import lombok.Data;

@Data
@SuppressWarnings("serial")
public class POIIconsetVO extends BaseVO implements Serializable {

	private Integer	iconsetNo      ;
	private String 	iconsetNm      ;
	private String 	iconsetDesc    ;
	private int 	iconset2d0     ;
	private int 	iconset2d1     ;
	private int 	iconset2d2     ;
	private int 	iconset2d3     ;
	private int 	iconset2d4     ;

	private String 	iconset2d0FilePath = "/resources/img/noPhoto.png";
	private String 	iconset2d1FilePath = "/resources/img/noPhoto.png";
	private String 	iconset2d2FilePath = "/resources/img/noPhoto.png";
	private String 	iconset2d3FilePath = "/resources/img/noPhoto.png";
	private String 	iconset2d4FilePath = "/resources/img/noPhoto.png";

	private String 	iconset3d      ;
	private String  iconset3dColor1;
	private String  iconset3dColor2;
	private String  iconset3dColor3;
	private String  iconset3dColor4;
	private int 	iconset3dThumb ;

	private String 	iconset3dThumbFilePath  = "/resources/img/noPhoto.png";

	private String 	iconsetType    ;
	private List<POIIconsetVO> iconsetList;

}














