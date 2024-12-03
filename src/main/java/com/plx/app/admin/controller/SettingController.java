package com.plx.app.admin.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.plx.app.admin.service.SystemInfoService;
import com.plx.app.admin.vo.SystemInfoVO;
import com.plx.app.cmn.controller.BaseController;
import com.plx.app.cmn.service.FileInfoService;
import com.plx.app.cmn.vo.FileInfoVO;
import com.plx.app.constant.CmnConst;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * @Project KNIS
 * @Class SettingController
 * @since 2020. 7. 24.
 * @author 이유리
 * @Description :
 */
@Controller
@RequestMapping(value="/adm/setting")
public class SettingController extends BaseController {


	/**
	 * 시스템 서비스
	 */
	@Autowired
	SystemInfoService systemInfoService;

	/**
	 * 파일 서비스
	 */
	@Autowired
	FileInfoService fileInfoService;

	/**
	 * @Method sysSettings
	 * @since 2020. 7. 24.
	 * @author 이유리
	 * @return String
	 * @param model
	 * @return
	 * @throws Exception
	 * @description 시스템설정 페이지
	 */
	@RequestMapping(value="/sysSettings.do")
	public String sysSettings(Model model) throws Exception {

		SystemInfoVO systemInfoVO = systemInfoService.selectSystemInfo();
		model.addAttribute("systemInfoVO", systemInfoVO);

		return "adm/setting/sysSettings";
	}

	/**
	 * @Method getSysSettings
	 * @since 2020. 7. 24.
	 * @author 이유리
	 * @return Map<String,Object>
	 * @return
	 * @throws Exception
	 * @description 시스템설정 정보
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

	/**
	 * @Method modifySysSettings
	 * @since 2020. 7. 24.
	 * @author 이유리
	 * @return Map<String,Object>
	 * @param pSystemInfoVO
	 * @param request
	 * @return
	 * @throws Exception
	 * @description 시스템설정 수정
	 */
	@RequestMapping(value="/sysSettingsModify.json")
	@ResponseBody
	public Map<String, Object> modifySysSettings(@ModelAttribute("SystemInfoVO") SystemInfoVO pSystemInfoVO, MultipartHttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("update.success");

		try {
			// 시스템정보 저장
			importLoginSession(pSystemInfoVO);
			systemInfoService.updateSystemInfo(pSystemInfoVO);

		} catch(SQLException se) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("update.fail");
			logger_error.error("SQLException", se);
		} catch(Exception e) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("update.fail");
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}
}
