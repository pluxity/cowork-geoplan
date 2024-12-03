package com.plx.app.constant;

import java.util.ArrayList;
import java.util.List;

import com.plx.app.cmn.vo.MenuVO;

public class AdmMenu {

	public static final List<MenuVO> getAdmMenuList() {

		List<MenuVO> menuList = new ArrayList<MenuVO>();

		// 메뉴1
		MenuVO menu1 = new MenuVO();
		List<MenuVO> subMenuList1 = new ArrayList<MenuVO>();
		menu1.setMenuNm("도면 관리");
		menu1.setMenuIcon("<i class=\"fas fa-map-marked-alt\"></i>");
		menu1.setMenuDepth("1");
		menu1.setMenuKey("/adm/map/");

		MenuVO menu1_1 = new MenuVO();
		menu1_1.setMenuNm("도면 목록");
		menu1_1.setMenuDepth("2");
		menu1_1.setMenuUrl("/adm/map/mapList.do");
		subMenuList1.add(menu1_1);

		MenuVO menu1_2 = new MenuVO();
		menu1_2.setMenuNm("도면 분류");
		menu1_2.setMenuDepth("2");
		menu1_2.setMenuUrl("/adm/map/categoryList.do");
		subMenuList1.add(menu1_2);

		menu1.setSubMenuList(subMenuList1);

		// 메뉴2
		MenuVO menu2 = new MenuVO();
		List<MenuVO> subMenuList2 = new ArrayList<MenuVO>();
		menu2.setMenuNm("POI 관리");
		menu2.setMenuIcon("<i class=\"fas fa-map-marker-alt\"></i>");
		menu2.setMenuDepth("1");
		menu2.setMenuKey("/adm/poi/");

		MenuVO menu2_1 = new MenuVO();
		menu2_1.setMenuNm("POI 목록");
		menu2_1.setMenuDepth("2");
		menu2_1.setMenuUrl("/adm/poi/poiList.do");
		subMenuList2.add(menu2_1);

		MenuVO menu2_2 = new MenuVO();
		menu2_2.setMenuNm("POI 분류");
		menu2_2.setMenuDepth("2");
		menu2_2.setMenuUrl("/adm/poi/categoryList.do");
		subMenuList2.add(menu2_2);

		MenuVO menu2_3 = new MenuVO();
		menu2_3.setMenuNm("아이콘셋 목록");
		menu2_3.setMenuDepth("2");
		menu2_3.setMenuUrl("/adm/poi/iconList.do");
		subMenuList2.add(menu2_3);

		menu2.setSubMenuList(subMenuList2);

		// 메뉴3
		MenuVO menu3 = new MenuVO();
		List<MenuVO> subMenuList3 = new ArrayList<MenuVO>();
		menu3.setMenuNm("사용자 관리");
		menu3.setMenuIcon("<i class=\"fas fa-users\"></i>");
		menu3.setMenuDepth("1");
		menu3.setMenuKey("/adm/user/");

		MenuVO menu3_1 = new MenuVO();
		menu3_1.setMenuNm("사용자 목록");
		menu3_1.setMenuDepth("2");
		menu3_1.setMenuUrl("/adm/user/usrList.do");
		subMenuList3.add(menu3_1);

		MenuVO menu3_2 = new MenuVO();
		menu3_2.setMenuNm("그룹/권한 목록");
		menu3_2.setMenuDepth("2");
		menu3_2.setMenuUrl("/adm/user/usrgrpList.do");
		subMenuList3.add(menu3_2);

		menu3.setSubMenuList(subMenuList3);

		// 메뉴4
		MenuVO menu4 = new MenuVO();
		menu4.setMenuNm("유저 분석 관리");
		menu4.setMenuIcon("<i class='fas fa-chart-line'></i>");
		menu4.setMenuDepth("1");
		menu4.setMenuKey("/adm/analytics/");
		menu4.setMenuUrl("/adm/analytics/analytics.do");
		
		// 메뉴5
		MenuVO menu5 = new MenuVO();
		menu5.setMenuNm("시스템설정 관리");
		menu5.setMenuIcon("<i class='fas fa-cog'></i>");
		menu5.setMenuDepth("1");
		menu5.setMenuKey("/adm/setting/");
		menu5.setMenuUrl("/adm/setting/sysSettings.do");

		// 메뉴 추가
		menuList.add(menu1);
		menuList.add(menu2);
		menuList.add(menu3);
		menuList.add(menu4);
		menuList.add(menu5);

		return menuList;
	}

}
