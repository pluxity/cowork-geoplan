package com.plx.app.admin.controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.plx.app.admin.service.MapInfoService;
import com.plx.app.admin.service.POICategoryService;
import com.plx.app.admin.service.POIInfoService;
import com.plx.app.admin.vo.MapInfoVO;
import com.plx.app.admin.vo.POICategoryVO;
import com.plx.app.admin.vo.POIIconsetVO;
import com.plx.app.admin.vo.POIInfoVO;
import com.plx.app.cmn.controller.BaseController;
import com.plx.app.cmn.service.FileInfoService;
import com.plx.app.cmn.vo.FileInfoVO;
import com.plx.app.constant.CmnConst;
import com.plx.app.security.SecurityUtils;

/**
 * @Project KNIS
 * @Class POIController
 * @since 2019. 9. 30.
 * @author 류중규
 * @Description : poi 관리
 */
@Controller
@RequestMapping(value = "/adm/poi")
public class POIController extends BaseController {

	/**
	 * poi 서비스
	 */
	@Autowired
	POIInfoService poiInfoService;

	/**
	 * poi 카테고리
	 */
	@Autowired
	POICategoryService poiCategoryService;

	/**
	 * 도면정보
	 */
	@Autowired
	MapInfoService mapInfoService;

	/**
	 * 업로드파일처리
	 */
	@Autowired
	FileInfoService fileInfoService;

	/**
	 * @Method admMainIndex
	 * @since 2020. 1. 8.
	 * @author 류중규
	 * @return String
	 * @return
	 * @throws Exception
	 * @description POI관리 메인
	 */
	@RequestMapping(value = "/index.do")
	public String admMainIndex() throws Exception {

		return "redirect:/adm/poi/poiList.do";
	}

	/**
	 * @Method admPoiList
	 * @since 2019. 10. 3.
	 * @author 류중규
	 * @return String
	 * @param pPOIInfoVO
	 * @param model
	 * @return
	 * @throws Exception
	 * @description poi 목록 페이지
	 */
	@RequestMapping(value = "/poiList.do")
	public String admPoiList(POIInfoVO pPOIInfoVO, Model model) throws Exception {

		try {
			// poi 카테고리 트리
			List<LinkedHashMap<String, Object>> poiCategoryTree = poiCategoryService.poiCategoryTree();
			model.addAttribute("poiCategoryTree", poiCategoryTree);

			// poi 카테고리 대분류
			POICategoryVO pPOICategoryVO = new POICategoryVO();
			// pPOICategoryVO.setCategory1No(0);
			List<POICategoryVO> poiCategory1List = poiCategoryService.selectPOICategoryList(pPOICategoryVO);
			model.addAttribute("poiCategory1List", poiCategory1List);

			// 도면 목록
			MapInfoVO pMapInfoVO = new MapInfoVO();
			pMapInfoVO.setPage(0);
			// 도면 권한 리스트 체크
			if (!SecurityUtils.hasRole("ROLE_ADMIN")) {
				pMapInfoVO.setGrpNo(sessionLoginInfo().getGrpNo());
			}
			Map<String, Object> mapInfoList = mapInfoService.selectMapInfoList(pMapInfoVO);
			model.addAttribute("mapInfoList", mapInfoList.get("list"));

		} catch (SQLException se) {
			logger_error.error("SQLException", se);
		} catch (Exception e) {
			logger_error.error("Exception", e);
		}

		return "adm/poi/poiList";
	}

	/**
	 * @Method getPoiInfoList
	 * @since 2019. 10. 3.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param pPOIInfoVO
	 * @param category2NoArr
	 * @return
	 * @throws Exception
	 * @description poi 목록
	 */
	@RequestMapping(value = "/poiList.json")
	@ResponseBody
	public Map<String, Object> getPoiInfoList(POIInfoVO pPOIInfoVO,
			@RequestParam(value = "category2NoArr[]", required = false) List<String> category2NoArr) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = "";

		try {
			if (!SecurityUtils.hasRole("ROLE_ADMIN")) {
				pPOIInfoVO.setGrpNo(sessionLoginInfo().getGrpNo());
			}

			pPOIInfoVO.setCategory2NoList(category2NoArr);
			int offset = pPOIInfoVO.getPageSize() * (pPOIInfoVO.getPage() - 1);
			pPOIInfoVO.setOffset(offset);
			Map<String, Object> poiInfoList = poiInfoService.selectPOIInfoList(pPOIInfoVO);

			resultMap.put("result", poiInfoList.get("list")); // 목록
			resultMap.put("total", poiInfoList.get("total")); // 목록합계
			resultMap.put("poiInfo", pPOIInfoVO);

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
	 * @Method registPoiInfo
	 * @since 2019. 10. 3.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param pPOIInfoVO
	 * @return
	 * @throws Exception
	 * @description poi 등록
	 */
	@RequestMapping(value = "/poiRegist.json")
	@ResponseBody
	public Map<String, Object> registPoiInfo(POIInfoVO pPOIInfoVO, MultipartHttpServletRequest request)
			throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("regist.success");

		try {
			// 객체코드 없을 경우 임시코드
			if ("".equals(pPOIInfoVO.getDvcCd())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				String dvcCd = sdf.format(new Date());
				pPOIInfoVO.setDvcCd(dvcCd);
			}

			// 객체코드 중복체크
			POIInfoVO chkPOIInfoVO = new POIInfoVO();
			chkPOIInfoVO.setDvcCd(pPOIInfoVO.getDvcCd());
			Map<String, Object> dupResult = poiInfoService.selectPOIInfoList(chkPOIInfoVO);
			int dupCnt = (int) dupResult.get("total");
			if (dupCnt > 0) {
				resultCd = "fail";
				resultMsg = "사용중인 객체코드입니다.";
			} else {
				// poi 이미지 파일 업로드
				MultipartFile poiImg = request.getFile("poiImg");
				if (poiImg != null) {
					pPOIInfoVO = this.saveImgFile(pPOIInfoVO, request.getFile("poiImg"));
				}
				// poi 등록
				importLoginSession(pPOIInfoVO);
				int result = poiInfoService.insertPOIInfo(pPOIInfoVO);
				if (result == 0) {
					resultCd = "fail";
					resultMsg = messageSourceAccessor.getMessage("regist.fail");
				}
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
	 * @Method batRegistPoiInfo
	 * @since 2019. 10. 17.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param excelFile
	 * @param pPOIInfoVO
	 * @return
	 * @throws Exception
	 * @description poi 일괄 등록
	 */
	@RequestMapping(value = "/poiBatRegist.json")
	@ResponseBody
	public Map<String, Object> batRegistPoiInfo(@RequestParam("excelFile") MultipartFile[] excelFile,
			POIInfoVO pPOIInfoVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("regist.success");

		try {
			importLoginSession(pPOIInfoVO);
			// poi 대분류 리스트
			POICategoryVO pPOICategoryVO = new POICategoryVO();
			List<POICategoryVO> poiCategory1List = poiCategoryService.selectPOICategoryList(pPOICategoryVO);
			// poi 중분류 리스트
			List<POICategoryVO> poiCategory2List = poiCategoryService.selectPOICategory2List(pPOICategoryVO);

			Map<String, Object> batResult = poiInfoService.insertExcelFile(excelFile, poiCategory1List,
					poiCategory2List, pPOIInfoVO);
			int errCnt = (int) batResult.get("errCnt");
			if (errCnt > 0) {
				resultCd = "fail";
				resultMsg = "등록 중 " + errCnt + "건이 실패하였습니다.";
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
	 * @Method removePoiInfo
	 * @since 2019. 10. 4.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param pPOIInfoVO
	 * @param poiNoArr
	 * @return
	 * @throws Exception
	 * @description poi 삭제
	 */
	@RequestMapping(value = "/poiRemove.json")
	@ResponseBody
	public Map<String, Object> removePoiInfo(POIInfoVO pPOIInfoVO,
			@RequestParam(value = "poiNoArr[]", required = false) List<String> poiNoArr) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("delete.success");

		try {
			if (null != poiNoArr)
				pPOIInfoVO.setPoiNoList(poiNoArr);
			int result = poiInfoService.deletePOIInfo(pPOIInfoVO);
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
	 * @Method viewPoiInfo
	 * @since 2019. 10. 11.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param pPOIInfoVO
	 * @return
	 * @throws Exception
	 * @description poi정보
	 */
	@RequestMapping(value = "/poiView.json")
	@ResponseBody
	public Map<String, Object> viewPoiInfo(POIInfoVO pPOIInfoVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = "";

		try {
			// poi 정보
			POIInfoVO result = poiInfoService.selectPOIInfo(pPOIInfoVO);

			resultMap.put("result", result);

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
	 * @Method modifyPoiInfo
	 * @since 2019. 10. 16.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param pPOIInfoVO
	 * @return
	 * @throws Exception
	 * @description POI수정
	 */
	@RequestMapping(value = "/poiModify.json")
	@ResponseBody
	public Map<String, Object> modifyPoiInfo(POIInfoVO pPOIInfoVO,
			@RequestParam(value = "poiNoArr[]", required = false) List<String> poiNoArr,
			MultipartHttpServletRequest request) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("update.success");

		try {
			// 체크 poi 미배치 일괄변경용
			if (null != poiNoArr)
				pPOIInfoVO.setPoiNoList(poiNoArr);

			if ("".equals(pPOIInfoVO.getDvcCd())) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				String dvcCd = sdf.format(new Date());
				pPOIInfoVO.setDvcCd(dvcCd);
			}

			// 수정 전에 중복 체크하고, 중복이면 return
			if (null == pPOIInfoVO.getPoiNoList() && !StringUtils.isEmpty(pPOIInfoVO.getDvcCd())) {
				// 일괄 미배치 시에는 중복체크할 필요 없으므로 if문 걸어줌
				POIInfoVO testPOIInfoVO = new POIInfoVO();
				testPOIInfoVO.setDvcCd(pPOIInfoVO.getDvcCd());
				testPOIInfoVO = poiInfoService.selectPOIInfo(testPOIInfoVO);

				if (testPOIInfoVO != null && testPOIInfoVO.getPoiNo() != pPOIInfoVO.getPoiNo()) {
					// 중복인 경우
					resultMap.put("resultCd", "fail");
					resultMap.put("resultMsg", "객체코드는 중복이 불가합니다.");
					return resultMap;
				}
			}

			// poi 이미지 파일 업로드
			MultipartFile poiImg = request.getFile("poiImg");
			if (poiImg != null) { // 이미지 파일 있을 때만
				if (pPOIInfoVO.getImgFileNo() != 0) {
					// 기존에 이미지 파일 있을 때만 이미지 파일 삭제
					FileInfoVO pFileInfoVO = new FileInfoVO();
					pFileInfoVO.setFileNo(pPOIInfoVO.getImgFileNo());
					fileInfoService.deleteFileInfo(pFileInfoVO);
				}
				pPOIInfoVO = this.saveImgFile(pPOIInfoVO, request.getFile("poiImg")); // 파일업로드
			}
			// 수정
			importLoginSession(pPOIInfoVO);
			int result = poiInfoService.updatePOIInfo(pPOIInfoVO);
			if (result == 0) {
				resultCd = "fail";
				resultMsg = messageSourceAccessor.getMessage("update.fail");
			}

		} catch (DataAccessException se) {
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
	 * @Method poiImgRemove
	 * @since 2020. 07. 08.
	 * @author 이유리
	 * @return Map<String,Object>
	 * @param pPOIInfoVO
	 * @return
	 * @throws Exception
	 * @description POI 이미지 삭제
	 */
	@RequestMapping(value = "/poiImgRemove.json")
	@ResponseBody
	public Map<String, Object> poiImgRemove(POIInfoVO pPOIInfoVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("delete.success");

		try {
			pPOIInfoVO = poiInfoService.selectPOIInfo(pPOIInfoVO);
			// 이미지 파일이 있는 경우에만 삭제 진행
			if (pPOIInfoVO.getImgFileNo() != 0) {
				FileInfoVO pFileInfoVO = new FileInfoVO();
				pFileInfoVO.setFileNo(pPOIInfoVO.getImgFileNo());
				fileInfoService.deleteFileInfo(pFileInfoVO);
				poiInfoService.updatePOIImgDelete(pPOIInfoVO);
			}
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
	 * @Method saveImgFile
	 * @since 2020. 06. 2X.
	 * @author 이유리
	 * @param POIInfoVO, MultipartFile
	 * @return POIInfoVO
	 * @throws Exception
	 * @description POI 이미지 업로드
	 */
	private POIInfoVO saveImgFile(POIInfoVO pPOIInfoVO, MultipartFile poiImg) throws Exception {

		FileInfoVO pFileInfoVO = new FileInfoVO();
		pFileInfoVO.setFilePath(CmnConst.UPLOAD_FILE_PATH + "poi/");
		pFileInfoVO.setFileType("poiImg");
		importLoginSession(pFileInfoVO);
		FileInfoVO poiImgFileInfo = fileInfoService.setUploadFile(pFileInfoVO, poiImg); // 이미지파일 업로드 처리
		pPOIInfoVO.setImgFileNo(poiImgFileInfo.getFileNo()); // 이미지 파일번호

		return pPOIInfoVO;
	}

	/**
	 * @Method poiListExcelDownload
	 * @since 2020. 07. 16.
	 * @author 이유리
	 * @param pPOIInfoVO
	 * @param category2NoString
	 * @param fieldNmList
	 * @param model
	 * @return String
	 * @throws Exception
	 * @description POI 목록 엑셀 다운로드
	 */
	@RequestMapping(value = "/poiExceldownload.do")
	public String poiListExcelDownload(
			@RequestParam(value = "category2NoString", required = false) String category2NoString,
			@RequestParam(value = "fieldNmList") String fieldNmList, POIInfoVO pPOIInfoVO, Model model)
			throws Exception {
		Map<String, String> categoryMap1 = new HashMap<String, String>();
		Map<String, String> categoryMap2 = new HashMap<String, String>();
		Map<String, Object> categoryMap = new HashMap<String, Object>();

		String sheetName = "";

		POICategoryVO pPOICategoryVO = new POICategoryVO();
		List<POICategoryVO> poiCategory1List = poiCategoryService.selectPOICategoryList(pPOICategoryVO);
		List<POICategoryVO> poiCategory2List = poiCategoryService.selectPOICategory2List(pPOICategoryVO);

		for (POICategoryVO poiCategoryVO : poiCategory1List) {
			categoryMap1.put(String.valueOf(poiCategoryVO.getCategory1No()), poiCategoryVO.getCategory1Nm());
		}
		for (POICategoryVO poiCategoryVO : poiCategory2List) {
			categoryMap2.put(String.valueOf(poiCategoryVO.getCategory2No()), poiCategoryVO.getCategory2Nm());
		}
		categoryMap.put("categoryMap1", categoryMap1);
		categoryMap.put("categoryMap2", categoryMap2);

		String[] fieldNmArr = fieldNmList.trim().split(",");

		if (!StringUtils.isEmpty(category2NoString)) {
			String[] category2NoArr = category2NoString.trim().split(",");

			List<String> category2NoList = new ArrayList<String>();
			for (String string : category2NoArr) {
				category2NoList.add(string);
			}
			pPOIInfoVO.setCategory2NoList(category2NoList);
		}

		pPOIInfoVO.setPageSize(0);

		model.addAttribute("sheetName", sheetName + "POI목록");
		model.addAttribute("fieldNmList", fieldNmArr);
		model.addAttribute("VOList", poiInfoService.selectPOIInfoList(pPOIInfoVO).get("list"));
		model.addAttribute("categoryMap", categoryMap);

		return "excelDownloadView";
	}

	/**
	 * @Method iconListExcelDownload
	 * @since 2020. 07. 17.
	 * @author 이유리
	 * @param pPOIIconsetVO
	 * @param fieldNmList
	 * @param model
	 * @return String
	 * @throws Exception
	 * @description 아이콘셋 목록 엑셀 다운로드
	 */
	@RequestMapping(value = "/iconExceldownload.do")
	public String iconListExcelDownload(@RequestParam(value = "fieldNmList") String fieldNmList,
			POIIconsetVO pPOIIconsetVO, Model model) throws Exception {

		String[] fieldNmArr = fieldNmList.trim().split(",");

		pPOIIconsetVO.setPageSize(0);

		model.addAttribute("sheetName", "아이콘셋_목록");
		model.addAttribute("fieldNmList", fieldNmArr);
		model.addAttribute("VOList", poiCategoryService.selectPOIIconsetList(pPOIIconsetVO).get("result"));

		return "excelDownloadView";
	}

	@RequestMapping(value = "/poiPositionModify.json")
	@ResponseBody
	public Map<String, Object> modifyPoiPositionInfo(POIInfoVO pPOIInfoVO,
											 @RequestParam(value = "poiNoArr[]", required = false) List<String> poiNoArr,
											 MultipartHttpServletRequest request) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("update.success");

		try {
			// 체크 poi 미배치 일괄변경용
			if (null != poiNoArr)
				pPOIInfoVO.setPoiNoList(poiNoArr);

			// 수정
			importLoginSession(pPOIInfoVO);
			int result = poiInfoService.updatePOIInfo(pPOIInfoVO);
			if (result == 0) {
				resultCd = "fail";
				resultMsg = messageSourceAccessor.getMessage("update.fail");
			}

		} catch (DataAccessException se) {
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
}
