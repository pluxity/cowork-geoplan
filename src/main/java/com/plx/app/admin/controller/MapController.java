package com.plx.app.admin.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.plx.app.admin.service.CamPosService;
import com.plx.app.admin.service.FloorInfoService;
import com.plx.app.admin.service.MapCategoryService;
import com.plx.app.admin.service.MapInfoService;
import com.plx.app.admin.service.POICategoryService;
import com.plx.app.admin.service.POILodInfoService;
import com.plx.app.admin.service.SystemInfoService;
import com.plx.app.admin.vo.FloorInfoVO;
import com.plx.app.admin.vo.MapCategoryVO;
import com.plx.app.admin.vo.MapHstVO;
import com.plx.app.admin.vo.MapInfoVO;
import com.plx.app.admin.vo.POICategoryVO;
import com.plx.app.admin.vo.POILodTypeVO;
import com.plx.app.cmn.controller.BaseController;
import com.plx.app.cmn.service.FileInfoService;
import com.plx.app.cmn.vo.FileInfoVO;
import com.plx.app.constant.CmnConst;
import com.plx.app.security.SecurityUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * @Project KNIS
 * @Class MapController
 * @since 2019. 9. 2.
 * @author redmoonk
 * @Description : 도면 관리
 */
@Controller
@RequestMapping(value = "/adm/map")
@Transactional
public class MapController extends BaseController {

	/**
	 * 도면정보
	 */
	@Autowired
	MapInfoService mapInfoService;

	/**
	 * 도면카테고리
	 */
	@Autowired
	MapCategoryService mapCategoryService;

	/**
	 * 층정보
	 */
	@Autowired
	FloorInfoService floorInfoService;

	/**
	 * 업로드파일처리
	 */
	@Autowired
	FileInfoService fileInfoService;

	/**
	 * POI 카테고리
	 */
	@Autowired
	POICategoryService poiCategoryService;

	/**
	 * POI LOD
	 */
	@Autowired
	POILodInfoService poiLodInfoService;

	/**
	 * 시스템 설정
	 */
	@Autowired
	SystemInfoService systemInfoService;
	
	/**
	 * 화각
	 */
	@Autowired
	CamPosService camPosService;
	
	/**
	 * @Method admMainIndex
	 * @since 2020. 1. 8.
	 * @author 류중규
	 * @return String
	 * @return
	 * @throws Exception
	 * @description 도면관리 메인
	 */
	@RequestMapping(value = "/index.do")
	public String admMainIndex() throws Exception {

		return "redirect:/adm/map/mapList.do";
	}

	/**
	 * @Method admMapList
	 * @since 2019. 9. 20.
	 * @author 류중규
	 * @return String
	 * @param pMapInfoVO
	 * @param model
	 * @return
	 * @throws Exception
	 * @description 도면 목록 페이지
	 */
	@RequestMapping(value = "/mapList.do")
	public String admMapList(MapInfoVO pMapInfoVO, Model model) throws Exception {

		try {
			System.out.println(BCrypt.hashpw("admin", BCrypt.gensalt()));

			// 도면 카테고리 트리
			List<LinkedHashMap<String, Object>> mapCategoryTree = mapCategoryService.mapCategoryTree();
			model.addAttribute("mapCategoryTree", mapCategoryTree);

			// 도면 카테고리 대분류
			MapCategoryVO pMapCategoryVO = new MapCategoryVO();
			List<MapCategoryVO> mapCategoryList = mapCategoryService.selectMapCategoryList(pMapCategoryVO);
			model.addAttribute("mapCategoryList", mapCategoryList);

		} catch (SQLException se) {
			logger_error.error("SQLException", se);
		} catch (Exception e) {
			logger_error.error("Exception", e);
		}

		return "adm/map/mapList";
	}

	/**
	 * @Method selectMapInfoList
	 * @since 2019. 9. 19.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param pMapInfoVO
	 * @param category3NoArr
	 * @return
	 * @throws Exception
	 * @description 도면 목록
	 */
	@RequestMapping(value = "/mapList.json")
	@ResponseBody
	public Map<String, Object> getMapInfoList(MapInfoVO pMapInfoVO,
			@RequestParam(value = "category3NoArr[]", required = false) List<String> category3NoArr) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = "";

		try {
			pMapInfoVO.setCategory3NoList(category3NoArr);
			int offset = pMapInfoVO.getPageSize() * (pMapInfoVO.getPage() - 1);
			pMapInfoVO.setOffset(offset);
			// 도면 권한 리스트 체크
			if (!SecurityUtils.hasRole("ROLE_ADMIN")) {
				pMapInfoVO.setGrpNo(sessionLoginInfo().getGrpNo());
			}

			Map<String, Object> mapInfoList = mapInfoService.selectMapInfoList(pMapInfoVO);

			resultMap.put("result", mapInfoList.get("list")); // 도면목록
			resultMap.put("total", mapInfoList.get("total")); // 목록합계
			resultMap.put("mapInfo", pMapInfoVO);

		} catch (SQLException se) {
			resultCd = "fail";
			logger_error.error("SQLException", se);
		} catch (Exception e) {
			resultCd = "fail";
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}

	/**
	 * @Method registMapInfo
	 * @since 2019. 9. 18.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param pMapInfoVO
	 * @param request
	 * @return
	 * @throws Exception
	 * @description 도면 등록
	 */
	@RequestMapping(value = "/mapRegist.json")
	@ResponseBody
	public Map<String, Object> registMapInfo(MapInfoVO pMapInfoVO, MultipartHttpServletRequest request)
			throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("regist.success");

		try {

			MapInfoVO testMapInfoVO = new MapInfoVO();
			testMapInfoVO.setMapCd(pMapInfoVO.getMapCd());

			if (mapInfoService.selectMapInfo(testMapInfoVO) != null) {
				// 도면 코드가 중복일 때
				resultMap.put("resultCd", "fail");
				resultMap.put("resultMsg", "사용중인 도면코드입니다. 도면코드는 중복 불가합니다.");
				return resultMap;
			}

			// 도면정보 등록
			importLoginSession(pMapInfoVO);
			int result1 = mapInfoService.insertMapInfo(pMapInfoVO);
			
			// 도면번호
			int mapNo = pMapInfoVO.getMapNo();
			if (result1 > 0) {
				// 도면 파일 경로
				String mapPath = CmnConst.UPLOAD_MAP_PATH + mapNo + "/";

				MultipartFile mapImg = request.getFile("mapImg");

				FileInfoVO pFileInfoVO1 = new FileInfoVO();
				pFileInfoVO1.setFilePath(mapPath);
				pFileInfoVO1.setFileType("mapImg");
				importLoginSession(pFileInfoVO1);

				FileInfoVO mapImgFileInfo = fileInfoService.setUploadFile(pFileInfoVO1, mapImg); // 도면이미지파일 업로드 처리
				
				pMapInfoVO.setImgFileNo(mapImgFileInfo.getFileNo()); // 도면이미지 파일번호

				// 도면압축파일 업로드 및 등록 처리 - 도면이력포함
				MapHstVO pMapHstVO = new MapHstVO();
				importLoginSession(pMapInfoVO);
				int result2 = mapInfoService.mapFileUpload(pMapInfoVO, request, pMapHstVO);
				if (result2 > 0) {
					resultCd = "fail";
					resultMsg = messageSourceAccessor.getMessage("regist.fail");
					// 도면 파일 업로드 실패시 등록된 도면 정보와 이미지 파일도 삭제
					fileInfoService.deleteFileInfo(pFileInfoVO1);
					mapInfoService.deleteMapInfo(pMapInfoVO);
				}

				// 층 정보 리턴 (백차장님 api를 통해 center_pos값 계산하기위함)
				FloorInfoVO pFloorInfoVO = new FloorInfoVO();
				pFloorInfoVO.setMapNo(pMapInfoVO.getMapNo());
				pFloorInfoVO.setMapVer(pMapInfoVO.getMapVer());
				List<FloorInfoVO> floorInfoList = floorInfoService.selectFloorInfoList(pFloorInfoVO);

				resultMap.put("floorInfoList", floorInfoList);
				resultMap.put("mapInfo", pMapInfoVO);

			} else {
				resultCd = "fail";
				resultMsg = messageSourceAccessor.getMessage("regist.fail");
			}
		} catch (SQLException se) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("regist.fail");
			logger_error.error("SQLException", se);
		} catch (Exception e) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("regist.fail");
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}

	/**
	 * @Method admMapView
	 * @since 2019. 9. 22.
	 * @author 류중규
	 * @return String
	 * @param pMapInfoVO
	 * @param model
	 * @return
	 * @throws Exception
	 * @description 도면정보
	 */
	@GetMapping(value = "/mapView.do")
	public String admMapView(@ModelAttribute("mapInfoVO") MapInfoVO pMapInfoVO, Model model) throws Exception {

		try {
			// 도면 기본 정보
			MapInfoVO result = mapInfoService.selectMapInfo(pMapInfoVO);
			model.addAttribute("mapInfoVO", result);

			// 도면 카테고리1 목록
			MapCategoryVO pMapCategoryVO = new MapCategoryVO();
			List<MapCategoryVO> mapCategory1List = mapCategoryService.selectMapCategoryList(pMapCategoryVO);
			model.addAttribute("mapCategory1List", mapCategory1List);

			// 도면 카테고리2 목록
			MapCategoryVO pMapCategory1VO = new MapCategoryVO();
			pMapCategory1VO.setCategory1No(result.getCategory1No());
			List<MapCategoryVO> mapCategory2List = mapCategoryService.selectMapCategoryList(pMapCategory1VO);
			model.addAttribute("mapCategory2List", mapCategory2List);

			// 도면 카테고리3 목록
			MapCategoryVO pMapCategory2VO = new MapCategoryVO();
			pMapCategory2VO.setCategory2No(result.getCategory2No());
			List<MapCategoryVO> mapCategory3List = mapCategoryService.selectMapCategoryList(pMapCategory2VO);
			model.addAttribute("mapCategory3List", mapCategory3List);

			// 층정보 리스트
			FloorInfoVO pFloorInfoVO = new FloorInfoVO();
			pFloorInfoVO.setMapNo(pMapInfoVO.getMapNo());
			pFloorInfoVO.setMapVer(result.getMapVer());
			List<FloorInfoVO> floorInfoList = floorInfoService.selectFloorInfoList(pFloorInfoVO);
			model.addAttribute("floorInfoList", floorInfoList);

			// 도면 이력 리스트
			MapHstVO pMapHstVO = new MapHstVO();
			pMapHstVO.setMapNo(pMapInfoVO.getMapNo());
			List<MapHstVO> mapHstList = mapInfoService.selectMapHstList(pMapHstVO);
			model.addAttribute("mapHstList", mapHstList);

		} catch (SQLException se) {
			logger_error.error("SQLException", se);
		} catch (Exception e) {
			logger_error.error("Exception", e);
		}

		return "adm/map/mapView";
	}

	/**
	 * @Method modifyMapInfo
	 * @since 2019. 9. 23.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param pMapInfoVO
	 * @param request
	 * @return
	 * @throws Exception
	 * @description 도면 수정 처리
	 */
	@RequestMapping(value = "/mapModify.json")
	@ResponseBody
	public Map<String, Object> modifyMapInfo(MapInfoVO pMapInfoVO, MultipartHttpServletRequest request)
			throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("update.success");

		try {
			// 도면번호
			int mapNo = pMapInfoVO.getMapNo();

			if (!StringUtils.isEmpty(pMapInfoVO.getMapCd())) {
				MapInfoVO testMapInfoVO = new MapInfoVO();
				testMapInfoVO.setMapCd(pMapInfoVO.getMapCd());
				testMapInfoVO = mapInfoService.selectMapInfo(testMapInfoVO);
				if (testMapInfoVO != null && testMapInfoVO.getMapNo() != pMapInfoVO.getMapNo()) {
					// 도면 코드가 중복일 때
					resultMap.put("resultCd", "fail");
					resultMap.put("resultMsg", "사용중인 도면코드입니다. 도면코드는 중복 불가합니다.");
					return resultMap;
				}
			}

			MultipartFile mapImg = request.getFile("mapImg");
			// image 파일 있을때만 업로드 시도
			FileInfoVO pFileInfoVO1 = new FileInfoVO();
			if (null != mapImg) {
				// 도면 파일 경로
				String mapPath = CmnConst.UPLOAD_MAP_PATH + mapNo + "/";
				// 도면이미지파일 업로드 등록
				pFileInfoVO1.setFilePath(mapPath);
				pFileInfoVO1.setFileType("mapImg");
				importLoginSession(pFileInfoVO1);
				FileInfoVO mapImgFileInfo = fileInfoService.setUploadFile(pFileInfoVO1, mapImg); // 도면이미지파일 업로드 처리
				pMapInfoVO.setImgFileNo(mapImgFileInfo.getFileNo()); // 도면이미지 파일번호
			}

			// 도면정보 수정
			importLoginSession(pMapInfoVO);
			int result = mapInfoService.updateMapInfo(pMapInfoVO);

			if (result == 0) {
				resultCd = "fail";
				resultMsg = messageSourceAccessor.getMessage("update.fail");
				// 수정 실패시 이미지 파일도 삭제
				if (pFileInfoVO1.getFileNo() != 0) {// 이미지 파일 업로드했을 경우만 삭제시도
					FileInfoVO pFileInfoVO = new FileInfoVO();
					pFileInfoVO.setFileNo(pFileInfoVO1.getFileNo());
					fileInfoService.deleteFileInfo(pFileInfoVO);
				}
			}
		} catch (SQLException se) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("update.fail");
			logger_error.error("SQLException", se);
		} catch (Exception e) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("update.fail");
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}

	/**
	 * @Method removeMapInfo
	 * @since 2019. 9. 24.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param pMapInfoVO
	 * @return
	 * @throws Exception
	 * @description 도면 삭제 처리
	 */
	@RequestMapping(value = "/mapRemove.json")
	@ResponseBody
	public Map<String, Object> removeMapInfo(MapInfoVO pMapInfoVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("delete.success");

		try {
			// 도면정보 삭제
			int result = mapInfoService.deleteMapInfo(pMapInfoVO);
			if (result == 0) {
				resultCd = "fail";
				resultMsg = messageSourceAccessor.getMessage("delete.fail");
			}
		} catch (SQLException se) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("delete.fail");
			logger_error.error("SQLException", se);
		} catch (Exception e) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("delete.fail");
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}

	/**
	 * @Method removeMapHst
	 * @since 2019. 9. 22.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param pMapHstVO
	 * @return
	 * @throws Exception
	 * @description 도면 이력 삭제
	 */
	@RequestMapping(value = "/mapHstRemove.json")
	@ResponseBody
	public Map<String, Object> removeMapHst(MapHstVO pMapHstVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("delete.success");

		try {
			// 현재 도면 버전 체크
			MapInfoVO pMapInfoVO = new MapInfoVO();
			pMapInfoVO.setMapNo(pMapHstVO.getMapNo());
			MapInfoVO mapInfo = mapInfoService.selectMapInfo(pMapInfoVO);
			if (mapInfo.getMapVer().equals(pMapHstVO.getMapVer())) {
				resultCd = "fail";
				resultMsg = "현재 도면에서 사용중인 버전은 삭제할 수 없습니다.";
			} else {
				// 현재 도면이 아닌 경우 삭제 처리
				int result = mapInfoService.deleteMapHst(pMapHstVO);
				if (result == 0) {
					resultCd = "fail";
					resultMsg = messageSourceAccessor.getMessage("delete.fail");
				}
			}

		} catch (SQLException se) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("delete.fail");
			logger_error.error("SQLException", se);
		} catch (Exception e) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("delete.fail");
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;

	}

	/**
	 * @Method getMapHstList
	 * @since 2019. 9. 22.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param pMapHstVO
	 * @param model
	 * @return
	 * @throws Exception
	 * @description 도면 이력 리스트
	 */
	@RequestMapping(value = "/mapHstList.json")
	@ResponseBody
	public Map<String, Object> getMapHstList(MapHstVO pMapHstVO, Model model) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("search.success");

		try {
			List<MapHstVO> mapHstList = mapInfoService.selectMapHstList(pMapHstVO);
			resultMap.put("result", mapHstList);

		} catch (SQLException se) {
			resultCd = "fail";
			logger_error.error("SQLException", se);
		} catch (Exception e) {
			resultCd = "fail";
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}

	/**
	 * @Method registMapHst
	 * @since 2019. 9. 22.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param pMapHstVO
	 * @param request
	 * @return
	 * @throws Exception
	 * @description 도면 이력 등록
	 */
	@RequestMapping(value = "/mapHstRegist.json")
	@ResponseBody
	public Map<String, Object> registMapHst(MapHstVO pMapHstVO, MultipartHttpServletRequest request) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("regist.success");

		try {
			// 도면압축파일 업로드 및 등록 처리 - 도면이력포함
			MapInfoVO pMapInfoVO = new MapInfoVO();

			importLoginSession(pMapInfoVO);

			pMapInfoVO.setMapNo(pMapHstVO.getMapNo());
			pMapInfoVO = mapInfoService.selectMapInfo(pMapInfoVO);
			int result = mapInfoService.mapFileUpload(pMapInfoVO, request, pMapHstVO);
			if (result > 0) {
				resultCd = "fail";
				resultMsg = messageSourceAccessor.getMessage("regist.fail");
			}

		} catch (SQLException se) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("regist.fail");
			logger_error.error("SQLException", se);
		} catch (Exception e) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("regist.fail");
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}

	@RequestMapping(value = "/viewer.do")
	public String admViewer(MapInfoVO pMapInfoVO, Model model) throws Exception {

		try {
			// 도면 정보
			// MapInfoVO mapInfo = mapInfoService.selectMapInfo(pMapInfoVO);
			// model.addAttribute("mapInfo", mapInfo);

			// 층정보 리스트
			// FloorInfoVO pFloorInfoVO = new FloorInfoVO();
			// pFloorInfoVO.setMapNo(pMapInfoVO.getMapNo());
			// String mapVer = ("".equals(pMapInfoVO.getMapVer()))?
			// mapInfo.getMapVer():pMapInfoVO.getMapVer(); // 도면버전 파라미터가 없는 경우 등록된 도면버전으로
			// pFloorInfoVO.setMapVer(mapVer);
			// List<FloorInfoVO> floorInfoList =
			// floorInfoService.selectFloorInfoList(pFloorInfoVO);
			// model.addAttribute("floorInfoList", floorInfoList);

			model.addAttribute("mapInfo", pMapInfoVO);
			model.addAttribute("systemInfoVO", systemInfoService.selectSystemInfo());

			// POI카테고리 대분류
			POICategoryVO pPOICategoryVO = new POICategoryVO();
			List<POICategoryVO> poiCategory1List = poiCategoryService.selectPOICategoryList(pPOICategoryVO);
			model.addAttribute("poiCategory1List", poiCategory1List);

			// poi 카테고리 분류 - lod팝업용
			List<LinkedHashMap<String, Object>> poiCategoryTree = poiCategoryService.poiCategoryTree();
			model.addAttribute("poiCategoryTree", poiCategoryTree);

			model.addAttribute("mapNo", pMapInfoVO.getMapNo());
			model.addAttribute("mapVer", pMapInfoVO.getMapVer());

		} catch (SQLException se) {
			logger_error.error("SQLException", se);
		} catch (Exception e) {
			logger_error.error("Exception", e);
		}

		return "adm/map/viewer";
	}

	/**
	 * @Method getPoiLodInfo
	 * @since 2019. 11. 13.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param pPOILodTypeVO
	 * @return
	 * @throws Exception
	 * @description poi lod 정보
	 */
	@RequestMapping(value = "/poiLodInfo.json")
	@ResponseBody
	public Map<String, Object> getPoiLodInfo(POILodTypeVO pPOILodTypeVO) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = "";

		try {
			// poi 정보
			POILodTypeVO poiLodInfo = poiLodInfoService.selectPoiLodInfo(pPOILodTypeVO);
			resultMap.put("result", poiLodInfo);
		} catch (SQLException se) {
			resultCd = "fail";
			logger_error.error("SQLException", se);
		} catch (Exception e) {
			resultCd = "fail";
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}

	/**
	 * @Method registPoiLodInfo
	 * @since 2019. 11. 13.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param pPOILodTypeVO
	 * @return
	 * @throws Exception
	 * @description poi lod 등록
	 */
	@RequestMapping(value = "/poiLodRegist.json")
	@ResponseBody
	public Map<String, Object> registPoiLodInfo(@RequestBody POILodTypeVO pPOILodTypeVO) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("regist.success");

		try {
			// poi lod 정보 등록
			importLoginSession(pPOILodTypeVO);
			int result = poiLodInfoService.insertPoiLodInfo(pPOILodTypeVO);
			if (result == 0) {
				resultCd = "fail";
				resultMsg = messageSourceAccessor.getMessage("regist.fail");
			}

		} catch (SQLException se) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("regist.fail");
			logger_error.error("SQLException", se);
		} catch (Exception e) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("regist.fail");
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}

	/**
	 * @Method mapListExcelDownload
	 * @since 2020. 07. 17.
	 * @author 이유리
	 * @param pMapInfoVO
	 * @param fieldNmList
	 * @param category3NoStr
	 * @param model
	 * @return String
	 * @throws Exception
	 * @description 도면 목록 엑셀 다운로드
	 */
	@RequestMapping(value = "/mapExceldownload.do")
	public String mapListExcelDownload(@RequestParam(value = "fieldNmList") String fieldNmList,
			// @RequestParam(value="category1NoStr", required=false) String category1NoStr,
			// @RequestParam(value="category2NoStr", required=false) String category2NoStr,
			// 대분류, 중분류가 하나씩밖에 없어서 지금은 필요X
			// 혹시 여러개가 생긴다면 로직 추가하고, xml select문 수정하고 jsp, js 에도 필요한 코드 추가하면 됨
			@RequestParam(value = "category3NoStr", required = false) String category3NoStr, MapInfoVO pMapInfoVO,
			Model model) throws Exception {

		List<String> category3NoList = new ArrayList<String>();

		String[] fieldNmArr = fieldNmList.trim().split(",");

		if (!StringUtils.isEmpty(category3NoStr)) {
			String[] category3NoArr = category3NoStr.trim().split(",");
			for (String string : category3NoArr) {
				category3NoList.add(string);
			}
			pMapInfoVO.setCategory3NoList(category3NoList);
		}
		pMapInfoVO.setPageSize(0);

		MapCategoryVO pMapCategoryVO = new MapCategoryVO();
		pMapCategoryVO.setPageSize(0);

		model.addAttribute("sheetName", "도면목록");
		model.addAttribute("fieldNmList", fieldNmArr);
		model.addAttribute("VOList", mapInfoService.selectMapInfoList(pMapInfoVO).get("list"));
		model.addAttribute("categoryMap", mapCategoryService.selectAllMapCategoryList(pMapCategoryVO));

		return "excelDownloadView";
	}

	@RequestMapping("/2dMap.do")
	public String admin2DMap(MapInfoVO pMapInfoVO, Model model) throws Exception {
		try {
			MapInfoVO mapInfo = mapInfoService.selectMapInfo(pMapInfoVO);
			model.addAttribute("mapInfo", mapInfo);
		} catch (Exception e) {
			logger_error.error("Exception", e);
		}

		return "adm/map/2dMap";
	}
}
