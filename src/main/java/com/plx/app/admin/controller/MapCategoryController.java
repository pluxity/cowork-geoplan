package com.plx.app.admin.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plx.app.admin.service.MapCategoryService;
import com.plx.app.admin.vo.MapCategoryVO;
import com.plx.app.cmn.controller.BaseController;


/**
 *  @Project KNIS
 *  @Class MapCategoryController
 *  @since 2019. 9. 2.
 *  @author redmoonk
 *  @Description : 맵관련 공통 api
 */
@Controller
@RequestMapping(value="/adm/map")
public class MapCategoryController extends BaseController {


	/**
	 * 맵카테고리 정보
	 */
	@Autowired
	MapCategoryService mapCategoryService;

	/**
	 * @Method admMapCategoryList
	 * @since  2019. 9. 11.
	 * @author PLUXITY
	 * @return String
	 * @return
	 * @throws Exception
	 * @description
	 */
	@RequestMapping(value="/categoryList.do")
	public String admMapCategoryList() throws Exception {

		return "adm/map/categoryList";
	}

	/**
	 * @Method selectMapCategoryList
	 * @since  2019. 9. 4.
	 * @author redmoonk
	 * @return Map<String,Object>
	 * @param pMapCategoryVO
	 * @return
	 * @throws Exception
	 * @description 맵카테고리 리스트
	 */
	@RequestMapping(value="/mapCategoryList.json")
	@ResponseBody
	public Map<String, Object> selectMapCategoryList(@ModelAttribute("mapCategoryVO") MapCategoryVO pMapCategoryVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("criterion",pMapCategoryVO);
		String resultCd = "success";

		try {
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

		return resultMap;
	}

	/**
	 * @Method selectMapCategory
	 * @since  2019. 9. 11.
	 * @author newbie
	 * @return Map<String,Object>
	 * @param pMapCategoryVO
	 * @return
	 * @throws Exception
	 * @description
	 */
	@RequestMapping(value="/mapCategoryView.json")
	@ResponseBody
	public Map<String, Object> mapCategoryView(@ModelAttribute("mapCategoryVO") MapCategoryVO pMapCategoryVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("criterion",pMapCategoryVO);
		String resultCd = "success";

		try {
			MapCategoryVO mapCategoryList = mapCategoryService.selectMapCategory(pMapCategoryVO);
			resultMap.put("result", mapCategoryList);

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
	 * @Method updateMapCategory
	 * @since  2019. 9. 11.
	 * @author newbie
	 * @return Map<String,Object>
	 * @param pMapCategoryVO
	 * @return
	 * @throws Exception
	 * @description
	 */
	@RequestMapping(value="/mapCategoryModify.json")
	@ResponseBody
	public Map<String, Object> mapCategoryModify(@ModelAttribute("mapCategoryVO") MapCategoryVO pMapCategoryVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("criterion",pMapCategoryVO);
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("update.success");

		try {
			importLoginSession(pMapCategoryVO);
			Integer result = mapCategoryService.updateMapCategory(pMapCategoryVO);
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
	 * @Method deleteMapCategory
	 * @since  2019. 9. 11.
	 * @author newbie
	 * @return Map<String,Object>
	 * @param pMapCategoryVO
	 * @return
	 * @throws Exception
	 * @description
	 */
	@RequestMapping(value="/mapCategoryRemove.json")
	@ResponseBody
	public Map<String, Object> mapCategoryRemove(@ModelAttribute("mapCategoryVO") MapCategoryVO pMapCategoryVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("criterion",pMapCategoryVO);
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("delete.success");
		try {
			Integer result = mapCategoryService.deleteMapCategory(pMapCategoryVO);

			if(result == -1) {
				resultMap.put("result", "해당 카테고리에 등록된 도면이 존재합니다.");
				resultCd = "fail";
			}
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
	 * @Method insertMapCategory
	 * @since  2019. 9. 11.
	 * @author newbie
	 * @return Map<String,Object>
	 * @param pMapCategoryVO
	 * @return
	 * @throws Exception
	 * @description
	 */
	@RequestMapping(value="/mapCategoryRegist.json")
	@ResponseBody
	public Map<String, Object> insertMapCategory(@ModelAttribute("mapCategoryVO") MapCategoryVO pMapCategoryVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("criterion",pMapCategoryVO);
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("regist.success");

		try {
			importLoginSession(pMapCategoryVO);
			Integer result = mapCategoryService.insertMapCategory(pMapCategoryVO);
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

	@RequestMapping(value="/mapCategorySwitch.json")
	@ResponseBody
	public Map<String, Object> mapCategorySwitch(@RequestBody MapCategoryVO pMapCategoryVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("criterion",pMapCategoryVO);
		String resultCd = "success";

		try {
			Integer result = mapCategoryService.mapCategorySwitch(pMapCategoryVO);
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
