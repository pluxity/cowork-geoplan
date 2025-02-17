package com.plx.app.viewer.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.plx.app.admin.service.AnalyticsService;
import com.plx.app.admin.service.MapInfoService;
import com.plx.app.admin.service.SystemInfoService;
import com.plx.app.admin.vo.AnalyticsVO;
import com.plx.app.admin.vo.MapInfoVO;
import com.plx.app.admin.vo.SystemInfoVO;
import com.plx.app.admin.vo.UsrgrpMapVO;
import com.plx.app.cmn.controller.BaseController;
import com.plx.app.viewer.service.ViewerService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Project KNIS
 * @Class FrontViewerController
 * @since 2019. 12. 4.
 * @author
 * @Description : 사용자뷰어
 */
@Controller
@RequestMapping(value = "/viewer")
@RequiredArgsConstructor
public class FrontViewerController extends BaseController {

	/**
	 * 뷰어 서비스
	 */
	private final ViewerService viewerService;

	/**
	 * 시스템 설정
	 */
	private final SystemInfoService systemInfoService;


	/**
	 * 도면정보
	 */
	private final MapInfoService mapInfoService;
	
	/**
	 * 분석차트
	 */
	private final AnalyticsService analyticsService;


	/**
	 * @Method frontViewer
	 * @since 2019. 12. 4.
	 * @author
	 * @return String
	 * @param model
	 * @throws Exception
	 * @description 사용자뷰어
	 */
	@RequestMapping(value = "/index.do")
	public String frontViewer(
			Model model,
			@RequestParam(value = "mapNo", required = true) int mapNo,
			@RequestParam(value = "floorNo", required = false, defaultValue = "0") int floorNo
		) {

		try {
			// 시스템 설정값 넘겨줌
			SystemInfoVO pSystemInfoVO = systemInfoService.selectSystemInfo(); // 시스템
			model.addAttribute("systemInfoVO", pSystemInfoVO);

			MapInfoVO pMapInfoVO = new MapInfoVO();
			pMapInfoVO.setMapNo(mapNo);

			// 도면정보
			MapInfoVO mapInfoVO = mapInfoService.selectMapInfo(pMapInfoVO);
			model.addAttribute("mapNo", mapInfoVO.getMapNo());
			model.addAttribute("mapCd", mapInfoVO.getMapCd());
//			model.addAttribute("mapNo", mapNo);

			AnalyticsVO analyticsVO = new AnalyticsVO();
			analyticsVO.setMapNo(mapNo);
			analyticsService.insertAnalyticsInfo(analyticsVO);

		} catch (SQLException se) {
			logger_error.error("SQLException", se);
		} catch (Exception e) {
			logger_error.error("Exception", e);
		}

		return "viewer/index";
	}

	/**
	 * @Method front2DViewer
	 * @since 2021. 12. 17
	 * @author Na
	 * @return String
	 * @throws Exception
	 * @description 2D 사용자 뷰어
	 */
	@RequestMapping(value = "/2d/index.do")
	public String front2DViewer(Model model) throws Exception {

//		try {
//			UsrgrpMapVO pUsrgrpMapVO = new UsrgrpMapVO();
//			pUsrgrpMapVO.setGrpNo(sessionLoginInfo().getGrpNo());
//			List<UsrgrpMapVO> mapList =  viewerService.selectMapList(pUsrgrpMapVO);
//			model.addAttribute("mapList", new Gson().toJson(mapList));
//		} catch (Exception e) {
//			logger_error.error("Exception", e);
//		}

		return "viewer/2d/index";
	}

	@ResponseBody
	@RequestMapping(value = "/2d/getMapList.json", method = RequestMethod.GET)
	public Map<String, Object> getGrpMapList() throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		String resultCd = "success";

		List<UsrgrpMapVO> mapList = null;
		try {
			if(sessionLoginInfo() != null) {
				UsrgrpMapVO pUsrgrpMapVO = new UsrgrpMapVO();
				pUsrgrpMapVO.setGrpNo(sessionLoginInfo().getGrpNo());

				// 검증 된 Map + 권한 Map
//				mapList = viewerService.selectMapList(null);
				mapList = viewerService.selectMapList(pUsrgrpMapVO);
			} else {
				mapList =  viewerService.selectMapList(null);
			}

			result.put("RESULT", resultCd);
			result.put("LIST", mapList);
		} catch (Exception e) {
			resultCd = "fail";
			logger_error.error("Exception", e);
			result.put("RESULT", resultCd);
			result.put("MESSAGE", e.getMessage());
		}

		return result;
	}

}
