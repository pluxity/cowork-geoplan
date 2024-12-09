package com.plx.app.admin.controller;

import com.plx.app.admin.service.EvacRouteService;
import com.plx.app.admin.vo.EvacRouteVO;
import com.plx.app.cmn.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


/**
 *  @Project SKT_TSOP
 *  @Class EvacRouteController
 *  @since 2020. 3. 2.
 *  @author NEWBIE
 *  @Description : 대피경로 Controller
 */
@Controller
@RequestMapping(value="/adm/evacRoute")
public class EvacRouteController extends BaseController {

	/**
	 * 대피경로
	 */
	@Autowired
	EvacRouteService evacRouteService;

	/**
	 * @Method selectEvacRoute
	 * @since  2020. 3. 2.
	 * @author NEWBIE
	 * @return Map<String,Object>
	 * @param pEvacRouteVO
	 * @return
	 * @throws Exception
	 * @description 대피경로 가져오기
	 */
	@RequestMapping(value="/getRoute.json")
	@ResponseBody
	public Map<String, Object> selectEvacRoute(@ModelAttribute("EvacRouteVO") EvacRouteVO pEvacRouteVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("search.success");

		try {
			EvacRouteVO res = evacRouteService.selectEvacRoute(pEvacRouteVO);
			resultMap.put("result", res);
		} catch(SQLException se) {
			logger_error.error("SQLException", se);
		} catch(Exception e) {
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	};

	@RequestMapping(value="/upsertRoute.json")
	@ResponseBody
	public Map<String, Object> upsertEvacRoute(@ModelAttribute("EvacRouteVO") EvacRouteVO pEvacRouteVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("search.success");

		try {
			int res = evacRouteService.upsertEvacRoute(pEvacRouteVO);
			resultMap.put("result", res);
		} catch(SQLException se) {
			logger_error.error("SQLException", se);
		} catch(Exception e) {
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	};

	@RequestMapping(value="/deleteRoute.json")
	@ResponseBody
	public Map<String, Object> deleteEvacRoute(@ModelAttribute("EvacRouteVO") EvacRouteVO pEvacRouteVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("search.success");

		try {
			int res = evacRouteService.deleteEvacRoute(pEvacRouteVO);
			resultMap.put("result", res);
		} catch(SQLException se) {
			logger_error.error("SQLException", se);
		} catch(Exception e) {
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);
		return resultMap;
	};
}
