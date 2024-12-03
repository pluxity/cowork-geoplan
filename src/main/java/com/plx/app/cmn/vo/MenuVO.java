package com.plx.app.cmn.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
@SuppressWarnings("serial")
public class MenuVO implements Serializable {

	private String menuNm = "";
	private String menuUrl = "";
	private String menuIcon = "";
	private String menuDepth = "";
	private String menuKey = "";
	private String linkTarget = "";
	private List<MenuVO> subMenuList;
}
