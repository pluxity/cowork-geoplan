package com.plx.app.viewer.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.plx.app.admin.service.CamPosService;
import com.plx.app.admin.service.FloorInfoService;
import com.plx.app.admin.service.MapCategoryService;
import com.plx.app.admin.service.MapInfoService;
import com.plx.app.admin.service.POICategoryService;
import com.plx.app.admin.service.POILodInfoService;
import com.plx.app.admin.service.SystemInfoService;
import com.plx.app.admin.service.TopologyInfoService;
import com.plx.app.admin.vo.CamPosVO;
import com.plx.app.admin.vo.FloorInfoVO;
import com.plx.app.admin.vo.MapCategoryVO;
import com.plx.app.admin.vo.MapInfoVO;
import com.plx.app.admin.vo.POICategoryVO;
import com.plx.app.admin.vo.POILodTypeVO;
import com.plx.app.admin.vo.SystemInfoVO;
import com.plx.app.admin.vo.TopologyInfoVO;
import com.plx.app.cmn.controller.BaseController;
import com.plx.app.security.SecurityUtils;
import com.plx.app.viewer.service.ViewerService;
import com.plx.app.viewer.vo.AlarmRequestDTO;
import com.plx.app.viewer.vo.AlarmResponseDTO;
import com.plx.app.viewer.vo.ViewerPOIInfoVO;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value="/api/viewer")
@RequiredArgsConstructor
public class ViewerApiController extends BaseController {

	/**
	 * 도면정보
	 */
	private final MapInfoService mapInfoService;

	/**
	 * 도면카테고리
	 */
	private final MapCategoryService mapCategoryService;

	/**
	 * 층정보
	 */
	private final FloorInfoService floorInfoService;

	/**
	 * POI 카테고리
	 */
	private final POICategoryService poiCategoryService;

	/**
	 * poi lod 서비스
	 */
	private final POILodInfoService poiLodInfoService;

	/**
	 * 시스템 서비스
	 */
	private final SystemInfoService systemInfoService;

	/**
	 * 뷰어 서비스
	 */
	private final ViewerService viewerService;
	
	/**
	 * Topology 서비스
	 */
	private final TopologyInfoService topologyInfoService;
	
	/**
	 * CamPos 서비스
	 */
	private final CamPosService camPosService;

	/**
	 * @Method getMapInfo
	 * @since  2019. 10. 17.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @return
	 * @throws Exception
	 * @description 도면층정보
	 */
	@RequestMapping(value="/mapInfo.json")
	public Map<String, Object> getMapInfo(MapInfoVO pMapInfoVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";

		try {
			// 도면 정보
			MapInfoVO mapInfo = mapInfoService.selectMapInfo(pMapInfoVO);
			// 도면버전 파라미터가 없는 경우 등록된 도면버전으로
			String mapVer = ("".equals(pMapInfoVO.getMapVer()))? mapInfo.getMapVer():pMapInfoVO.getMapVer();
			mapInfo.setMapVer(mapVer);
			resultMap.put("mapInfo", mapInfo);

			// 층정보 리스트
			FloorInfoVO pFloorInfoVO = new FloorInfoVO();
			pFloorInfoVO.setMapNo(pMapInfoVO.getMapNo());
			pFloorInfoVO.setMapVer(mapVer);
			List<FloorInfoVO> floorInfoList = floorInfoService.selectFloorInfoList(pFloorInfoVO);
			resultMap.put("floorInfoList", floorInfoList);

			CamPosVO pCamPosVO = new CamPosVO();
			pCamPosVO.setMapNo(pMapInfoVO.getMapNo());
			List<CamPosVO> camPosList = camPosService.selectCamPosList(pCamPosVO);
			resultMap.put("camPosList", camPosList);

		} catch(SQLException se) {
			resultCd = "fail";
			logger_error.error("SQLException", se);
		} catch(Exception e) {
			resultCd = "fail";
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);

		return resultMap;
	}

	/**
	 * @Method poiCategoryList
	 * @since  2020. 08. 04.
	 * @author 유경식
	 * @return Map<String,Object>
	 * @param pPOICategoryVO
	 * @return
	 * @throws Exception
	 * @description POI 카테고리 리스트
	 */
	@RequestMapping(value="/poiCategoryDetailList.json")
	//@ResponseBody
	public Map<String, Object> getPoiCategoryDetailList(POICategoryVO pPOICategoryVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("search.success");

		try {
			// poi대분류 권한 체크
	         if(!SecurityUtils.hasRole("ROLE_ADMIN") && sessionLoginInfo() != null) {
	          	pPOICategoryVO.setGrpNo(sessionLoginInfo().getGrpNo());
	         }

			List<POICategoryVO> result = poiCategoryService.selectPOICategoryDetailList(pPOICategoryVO);
			resultMap.put("result", result);

		} catch(SQLException se) {
			logger_error.error("SQLException", se);
		} catch(Exception e) {
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);


		return resultMap;
	}


	/**
	 * @Method poiCategoryList
	 * @since  2019. 10. 19.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param pPOICategoryVO
	 * @return
	 * @throws Exception
	 * @description POI 카테고리 리스트
	 */
	@RequestMapping(value="/poiCategoryList.json")
	//@ResponseBody
	public Map<String, Object> getPoiCategoryList(
			POICategoryVO pPOICategoryVO,
			HttpServletRequest httpRequest
	) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("search.success");

		String mapNo = request.getParameter("mapNo");

		try {
			// poi대분류 권한 체크
			 if (!SecurityUtils.hasRole("ROLE_ADMIN") && sessionLoginInfo() != null) {
	         	pPOICategoryVO.setGrpNo(sessionLoginInfo().getGrpNo());
	         }

			List<POICategoryVO> result = poiCategoryService.selectPOICategoryList(pPOICategoryVO);
			resultMap.put("result", result);

		} catch(SQLException se) {
			logger_error.error("SQLException", se);
		} catch(Exception e) {
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);


		return resultMap;
	}

	/**
	 * @Method getPoiInfoList
	 * @since  2019. 11. 13.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @return
	 * @throws Exception
	 * @description poi 목록
	 */
	@RequestMapping(value="/poiList.json")
	//@ResponseBody
	public Map<String, Object> getPoiInfoList(ViewerPOIInfoVO pViewerPOIInfoVO){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = "";

		try {
			// 권한 체크
 			if (!SecurityUtils.hasRole("ROLE_ADMIN") && sessionLoginInfo() != null) {
 				pViewerPOIInfoVO.setGrpNo(sessionLoginInfo().getGrpNo());
 //	        	pPOIInfoVO.setGrpNo(sessionLoginInfo().getGrpNo());
 	        }

			pViewerPOIInfoVO.setPage(0);
			List<ViewerPOIInfoVO> result = viewerService.selectPOIInfoList(pViewerPOIInfoVO);

			resultMap.put("result", result); // 목록
			resultMap.put("total", (result == null? 0 : result.size()));

		} catch(SQLException se) {
			resultCd = "fail";
			logger_error.error("SQLException", se);
		} catch(Exception e) {
			resultCd = "fail";
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}


	/**
	 * @Method getPoiLodInfo
	 * @since  2019. 11. 13.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param pPOILodTypeVO
	 * @return
	 * @throws Exception
	 * @description poi lod 정보
	 */
	@RequestMapping(value="/poiLodInfo.json")
	public Map<String, Object> getPoiLodInfo(POILodTypeVO pPOILodTypeVO) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = "";

		try {
			// poi 정보
			POILodTypeVO poiLodInfo = poiLodInfoService.selectPoiLodInfo(pPOILodTypeVO);
			resultMap.put("result", poiLodInfo);
		} catch(SQLException se) {
			resultCd = "fail";
			logger_error.error("SQLException", se);
		} catch(Exception e) {
			resultCd = "fail";
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}

	@RequestMapping(value="/mapCategoryList.json")
	public Map<String, Object> getMapCategoryList(@ModelAttribute("mapCategoryVO") MapCategoryVO pMapCategoryVO) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = "";

		try {
			// 도면 카테고리 리스트
			List<MapCategoryVO> mapCategoryList = mapCategoryService.selectMapCategoryList(pMapCategoryVO);
			resultMap.put("result", mapCategoryList);
		} catch(SQLException se) {
			resultCd = "fail";
			logger_error.error("SQLException", se);
		} catch(Exception e) {
			resultCd = "fail";
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}

	@RequestMapping(value="/mapList.json")
	public Map<String, Object> getMapInfoList(MapInfoVO pMapInfoVO) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = "";

		try {
			// 도면 권한 리스트 체크
			if (!SecurityUtils.hasRole("ROLE_ADMIN") && sessionLoginInfo() != null) {
	        	pMapInfoVO.setGrpNo(sessionLoginInfo().getGrpNo());
	        }

			pMapInfoVO.setPage(0); // 페이징 없이
			Map<String, Object> mapInfoList = mapInfoService.selectMapInfoList(pMapInfoVO);
			resultMap.put("result", mapInfoList.get("list")); // 도면목록
		} catch(SQLException se) {
			resultCd = "fail";
			logger_error.error("SQLException", se);
		} catch(Exception e) {
			resultCd = "fail";
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}

	/**
	 * @Method viewPoiInfo
	 * @since  2019. 10. 11.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @return
	 * @throws Exception
	 * @description poi정보
	 */
	@RequestMapping(value="/poiView.json")
//	public Map<String, Object> viewPoiInfo(POIInfoVO pPOIInfoVO) throws Exception {
	public Map<String, Object> viewPoiInfo(ViewerPOIInfoVO pViewerPOIInfoVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = "";

		try {
			// poi 정보
//			POIInfoVO result = poiInfoService.selectPOIInfo(pPOIInfoVO);
			ViewerPOIInfoVO result = viewerService.selectPOIInfo(pViewerPOIInfoVO);

			resultMap.put("result", result);

		} catch(SQLException se) {
			resultCd = "fail";
			logger_error.error("SQLException", se);
		} catch(Exception e) {
			resultCd = "fail";
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}


	/**
	 * @Method getSysSettings
	 * @since  2020. 07. 28.
	 * @author 이유리
	 * @return Map<String,Object>
	 * @throws Exception
	 * @description 시스템 설정값 가져오는 메소드
	 */
	@RequestMapping(value="/sysSettings.json")
	@ResponseBody
	public Map<String, Object> getSysSettings() throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("search.success");

		try {
			SystemInfoVO pSystemInfoVO = systemInfoService.selectSystemInfo();

			resultMap.put("result", pSystemInfoVO);
		} catch(SQLException se) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("search.fail");
			logger_error.error("SQLException", se);
		} catch(Exception e) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("search.fail");
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}

	@RequestMapping(value="/topologyList.json")
	@ResponseBody
	public Map<String, Object> getTopologyInfo(TopologyInfoVO pTopologyInfoVO) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("search.success");

		try {
			pTopologyInfoVO = topologyInfoService.selectTopologyInfo(pTopologyInfoVO);
			resultMap.put("result", pTopologyInfoVO);
		} catch(SQLException se) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("search.fail");
			logger_error.error("SQLException", se);
		} catch(Exception e) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("search.fail");
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}

	@PostMapping("/alarms")
	@ResponseBody
	public Map<String, Object> postAlarm(@RequestBody AlarmRequestDTO dto) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("save.success");

		try {
			viewerService.saveAlarm(dto);
		} catch(SQLException se) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("save.fail");
			logger_error.error("SQLException", se);
		} catch(Exception e) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("save.fail");
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}

	@GetMapping("/alarms")
	@ResponseBody
	public Map<String, Object> getAlarms() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("search.success");

		Map<String, Object> params = new HashMap<>();
		try {
			List<AlarmResponseDTO> list = viewerService.getAlarms(params);
			resultMap.put("result", list);
		} catch(SQLException se) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("search.fail");
			logger_error.error("SQLException", se);
		} catch(Exception e) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("search.fail");
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}
}
