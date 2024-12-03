package com.plx.app.admin.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.plx.app.admin.service.POICategoryService;
import com.plx.app.admin.vo.POICategoryVO;
import com.plx.app.admin.vo.POIIconsetVO;
import com.plx.app.cmn.controller.BaseController;
import com.plx.app.cmn.service.FileInfoService;
import com.plx.app.cmn.vo.FileInfoVO;
import com.plx.app.constant.CmnConst;

/**
 *  @Project KNIS
 *  @Class MapController
 *  @since 2019. 9. 2.
 *  @author redmoonk
 *  @Description : 도면 관리
 */
@Controller
@RequestMapping(value="/adm/poi")
public class POICategoryController extends BaseController {

	/**
	 * POI 카테고리 아이콘
	 */
	@Autowired
	POICategoryService poiCategoryService;

	/**
	 * 업로드파일처리
	 */
	@Autowired
	FileInfoService fileInfoService;



	/**
	 * @Method iconList
	 * @since  2019. 9. 30.
	 * @author NEWBIE
	 * @return String
	 * @param pPOIIconsetVO
	 * @param model
	 * @return
	 * @throws Exception
	 * @description
	 */
	@RequestMapping(value="/iconList.do")
	public String iconList(POIIconsetVO pPOIIconsetVO, Model model) throws Exception {
		return "adm/poi/iconList";
	}

	/**
	 * @Method categoryList
	 * @since  2019. 9. 30.
	 * @author NEWBIE
	 * @return String
	 * @param pPOICategoryVO
	 * @param model
	 * @return
	 * @throws Exception
	 * @description
	 */
	@RequestMapping(value="/categoryList.do")
	public String categoryList(POICategoryVO pPOICategoryVO, Model model) throws Exception {
		return "adm/poi/categoryList";
	}

	/**
	 * @Method poiIconList
	 * @since  2019. 9. 30.
	 * @author NEWBIE
	 * @return Map<String,Object>
	 * @param pPOIIconsetVO
	 * @return
	 * @throws Exception
	 * @description
	 */
	@RequestMapping(value="/poiIconList.json")
	@ResponseBody
	public Map<String, Object> poiIconList(@ModelAttribute("POIIconSetVO") POIIconsetVO pPOIIconsetVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("search.success");

		try {
			int offset = pPOIIconsetVO.getPageSize()*(pPOIIconsetVO.getPage()-1);
			pPOIIconsetVO.setOffset(offset);
			resultMap = poiCategoryService.selectPOIIconsetList(pPOIIconsetVO);
			resultMap.put("poiIconset", pPOIIconsetVO);
		} catch(SQLException se) {
			logger_error.error("SQLException", se);
			resultCd = "fail";
		} catch(Exception e) {
			logger_error.error("Exception", e);
			resultCd = "fail";
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}

	/**
	 * @Method poiIconView
	 * @since  2019. 9. 30.
	 * @author NEWBIE
	 * @return Map<String,Object>
	 * @param pPOIIconsetVO
	 * @return
	 * @throws Exception
	 * @description
	 */
	@RequestMapping(value="/poiIconView.json")
	@ResponseBody
	public Map<String, Object> poiIconView(@ModelAttribute("POIIconSetVO") POIIconsetVO pPOIIconsetVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("search.success");

		try {
			POIIconsetVO res = poiCategoryService.selectPOIIconsetDetail(pPOIIconsetVO);
			resultMap.put("result", res);

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
	 * @Method poiIconModify
	 * @since  2019. 9. 30.
	 * @author NEWBIE
	 * @return Map<String,Object>
	 * @param pPOIIconsetVO
	 * @param request
	 * @return
	 * @throws Exception
	 * @description
	 */
	@RequestMapping(value="/poiIconModify.json")
	@ResponseBody
	public Map<String, Object> poiIconModify(POIIconsetVO pPOIIconsetVO, MultipartHttpServletRequest request) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("update.success");

		try {

			FileInfoVO configFileVO = new FileInfoVO();

			configFileVO.setFilePath(CmnConst.UPLOAD_ICON_PATH +  pPOIIconsetVO.getIconsetNo() + "/" );
			configFileVO.setFileType("poiIcon");
			Map<String,FileInfoVO> fileVOMap = fileInfoService.setMultiFileUpload(configFileVO,request);
			importLoginSession(pPOIIconsetVO);
			poiCategoryService.updatePOIIconset(pPOIIconsetVO,fileVOMap);


		} catch(SQLException se) {
			logger_error.error("SQLException", se);
		} catch(Exception e) {
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);
		//////////////////////업데이트일 경우와 등록일 경우 구별해주기 위해서!///////////////////////
		//업데이트 시에는 페이지 그대로 있고, 새로 등록 시에는 첫 페이지로 이동하기 위해
		resultMap.put("update", "OK");

		return resultMap;
	}
	/**
	 * @Method poiIconRemove
	 * @since  2019. 9. 30.
	 * @author NEWBIE
	 * @return Map<String,Object>
	 * @param pPOIIconsetVO
	 * @return
	 * @throws Exception
	 * @description
	 */
	@RequestMapping(value="/poiIconRemove.json")
	@ResponseBody
	public Map<String, Object> poiIconRemove(@RequestBody POIIconsetVO pPOIIconsetVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("delete.success");

		try {
			int res = poiCategoryService.deletePOIIconset(pPOIIconsetVO);


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
	 * @Method poiIconRegist
	 * @since  2019. 9. 30.
	 * @author NEWBIE
	 * @return Map<String,Object>
	 * @param pPOIIconsetVO
	 * @param request
	 * @return
	 * @throws Exception
	 * @description
	 */
	@RequestMapping(value="/poiIconRegist.json")
	@ResponseBody
	public Map<String, Object> poiIconRegist(POIIconsetVO pPOIIconsetVO, MultipartHttpServletRequest request) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("regist.success");

		try {
			importLoginSession(pPOIIconsetVO);
			int res = poiCategoryService.insertPOIIconset(pPOIIconsetVO);

			if(res > 0) {

				FileInfoVO configFileVO = new FileInfoVO();

				configFileVO.setFilePath(CmnConst.UPLOAD_ICON_PATH +  pPOIIconsetVO.getIconsetNo() + "/" );
				configFileVO.setFileType("poiIcon");

				Map<String,FileInfoVO> fileVOMap = fileInfoService.setMultiFileUpload(configFileVO,request);

				poiCategoryService.updatePOIIconset(pPOIIconsetVO,fileVOMap);
			}

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
	 * @since  2019. 9. 30.
	 * @author NEWBIE
	 * @return Map<String,Object>
	 * @param pPOICategoryVO
	 * @return
	 * @throws Exception
	 * @description
	 */
	@RequestMapping(value="/poiCategoryList.json")
	@ResponseBody
	public Map<String, Object> poiCategoryList(POICategoryVO pPOICategoryVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("search.success");

		try {
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
	 * @Method poiCategoryList
	 * @since  2019. 9. 30.
	 * @author NEWBIE
	 * @return Map<String,Object>
	 * @param pPOICategoryVO
	 * @return
	 * @throws Exception
	 * @description
	 */
	@RequestMapping(value="/poiCategoryView.json")
	@ResponseBody
	public Map<String, Object> poiCategoryView(POICategoryVO pPOICategoryVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("search.success");

		try {
			POICategoryVO result = poiCategoryService.selectPOICategoryDetail(pPOICategoryVO);
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
	 * @Method insertPOICategory
	 * @since  2019. 10. 1.
	 * @author NEWBIE
	 * @return Map<String,Object>
	 * @param pPOICategoryVO
	 * @return
	 * @throws Exception
	 * @description
	 */
	@RequestMapping(value="/poiCategoryRegist.json")
	@ResponseBody
	public Map<String, Object> insertPOICategory(POICategoryVO pPOICategoryVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("criterion",pPOICategoryVO);
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("regist.success");

		try {
			importLoginSession(pPOICategoryVO);
			int result = poiCategoryService.insertPOICategory(pPOICategoryVO);
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
	 * @Method updatePOICategory
	 * @since  2019. 10. 1.
	 * @author NEWBIE
	 * @return Map<String,Object>
	 * @param pPOICategoryVO
	 * @return
	 * @throws Exception
	 * @description
	 */
	@RequestMapping(value="/poiCategoryModify.json")
	@ResponseBody
	public Map<String, Object> updatePOICategory(POICategoryVO pPOICategoryVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("criterion",pPOICategoryVO);
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("update.success");

		try {
			importLoginSession(pPOICategoryVO);
			int result = poiCategoryService.updatePOICategory(pPOICategoryVO);
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
	 * @Method deletePOICategory
	 * @since  2019. 10. 1.
	 * @author NEWBIE
	 * @return Map<String,Object>
	 * @param pPOICategoryVO
	 * @return
	 * @throws Exception
	 * @description
	 */
	@RequestMapping(value="/poiCategoryRemove.json")
	@ResponseBody
	public Map<String, Object> deletePOICategory(POICategoryVO pPOICategoryVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("criterion",pPOICategoryVO);
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("delete.success");

		try {
			Integer result = poiCategoryService.deletePOICategory(pPOICategoryVO);
			resultMap.put("result", result);

			if (result == null) {
				resultMsg = "POI 가 등록되어 있는 카테고리 입니다.";
			}

		} catch(SQLException se) {
			logger_error.error("SQLException", se);
		} catch(Exception e) {
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}

	@RequestMapping(value="/poiCategorySwitch.json")
	@ResponseBody
	public Map<String, Object> mapCategorySwitch(@RequestBody POICategoryVO pPOICategoryVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("criterion",pPOICategoryVO);
		String resultCd = "success";

		try {
			Integer result = poiCategoryService.poiCategorySwitch(pPOICategoryVO);
			resultMap.put("result", result);
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

}
