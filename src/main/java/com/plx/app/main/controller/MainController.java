package com.plx.app.main.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plx.app.admin.service.SystemInfoService;
import com.plx.app.admin.service.UsrInfoService;
import com.plx.app.admin.service.UsrgrpInfoService;
import com.plx.app.admin.vo.UsrInfoVO;
import com.plx.app.cmn.controller.BaseController;
import com.plx.app.constant.CmnConst;
import com.plx.app.security.SecurityUtils;
import com.plx.app.util.WebUtils;

/**
 * @Project KNIS
 * @Class MainController
 * @since 2019. 12. 17.
 * @author 류중규
 * @Description : 메인 컨트롤러
 */
@Controller
public class MainController extends BaseController {

	/**
	 * 사용자 정보
	 */
	@Autowired
	UsrInfoService usrInfoService;

	/**
	 * 사용자그룹 정보
	 */
	@Autowired
	UsrgrpInfoService usrgrpInfoService;

	/**
	 * 시스템 서비스
	 */
	@Autowired
	SystemInfoService systemInfoService;

	// /**
	// * 사용자 IP 체크할 것인지
	// */
	// @Value("#{globalProp['login.ip.check']}")
	// private boolean isIpCheck;

	/**
	 * @Method mainIndex
	 * @since 2019. 12. 17.
	 * @author 류중규
	 * @return String
	 * @return
	 * @throws Exception
	 * @description 메인 페이지
	 */
	@RequestMapping(value = "/")
	public String mainIndex(Model model) throws Exception {

		// 사용자 세션 정보
		String locationUrl = "";

		// 사용자 그룹 도면 조회
		int mapNo = 0;
		if (sessionLoginInfo().getUsrgrpMapList() != null && sessionLoginInfo().getUsrgrpMapList().size() != 0) {
			mapNo = sessionLoginInfo().getUsrgrpMapList().get(0).getMapNo();
		}

		locationUrl = CmnConst.LOCATION_URL_2D;
//		if (SecurityUtils.hasRole("ROLE_ADMIN")) {
//			locationUrl = CmnConst.LOCATION_URL_ADMIN;
//		} else {
//			if ( ! WebUtils.isMobile(request)) {
//				locationUrl = CmnConst.LOCATION_URL_USER + mapNo; //모바일 아닐 경우
//			} else {
//				locationUrl = CmnConst.LOCATION_URL_MUSER + mapNo; //모바일일 경우
//			}
//		}

		return "redirect:" + locationUrl;
	}

	/**
	 * @Method loginIndex
	 * @since 2019. 12. 17.
	 * @author 류중규
	 * @return String
	 * @return
	 * @throws Exception
	 * @description 로그인 페이지
	 */
	@RequestMapping(value = "/login/index.do")
	public String loginIndex(Model model, HttpServletResponse response) throws Exception {

		return "login/index";
	}

	/**
	 * @Method admMainIndex
	 * @since 2020. 1. 8.
	 * @author 류중규
	 * @return String
	 * @return
	 * @throws Exception
	 * @description 관리자 메인 페이지
	 */
	@RequestMapping(value = "/adm/main/index.do")
	public String admMainIndex(Model model) throws Exception {

		return "adm/main/index";
	}

	/**
	 * @Method modifyUsrPwd
	 * @since 2020. 05. 06.
	 * @author 유경식
	 * @return Map<String,Object>
	 * @param beforeUsrPwd
	 * @param usrPwd
	 * @return
	 * @throws Exception
	 * @description 사용자 비밀번호 변경
	 */
	@PostMapping(value = "/adm/main/usrPwdModify.json")
	@ResponseBody
	public Map<String, Object> modifyUsrPwd(@RequestParam(value = "beforeUsrPwd", required = true) String beforeUsrPwd,
			@RequestParam(value = "usrPwd", required = true) String usrPwd) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("update.success");

		try {
			// 사용자정보 조회
			UsrInfoVO pUsrInfoVO = new UsrInfoVO();
			pUsrInfoVO.setUsrId(sessionLoginInfo().getUsrId());
			UsrInfoVO rUsrInfo = usrInfoService.selectUsrInfo(pUsrInfoVO);

			if (null != rUsrInfo) {
				// 기존 비밀번호 확인
				if (BCrypt.checkpw(beforeUsrPwd, rUsrInfo.getUsrPwd())) {
					// 비밀번호 변경
					String encUsrPwd = BCrypt.hashpw(usrPwd, BCrypt.gensalt());
					pUsrInfoVO.setUsrPwd(encUsrPwd);

					// 수정
					importLoginSession(pUsrInfoVO);
					int result = usrInfoService.updateUsrInfo(pUsrInfoVO);
					if (result == 0) {
						resultCd = "fail";
						resultMsg = messageSourceAccessor.getMessage("update.fail");
					}
				} else {
					resultCd = "fail";
					resultMsg = "비밀번호가 일치하지 않습니다.";
				}
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
	 * @Method delayUsrPwdModify
	 * @since 2020. 05. 06.
	 * @author 유경식
	 * @return Map<String,Object>
	 * @param UsrInfoVO
	 * @return
	 * @throws Exception
	 * @description 사용자 비밀번호 변경
	 */
	@PostMapping(value = "/adm/main/delayUsrPwdModify.json")
	@ResponseBody
	public Map<String, Object> delayUsrPwdModify(UsrInfoVO pUsrInfoVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("update.success");

		try {
			// 사용자정보 조회

			pUsrInfoVO.setUsrId(sessionLoginInfo().getUsrId());
			importLoginSession(pUsrInfoVO);
			int result = usrInfoService.updateUsrInfo(pUsrInfoVO);
			if (result == 0) {
				resultCd = "fail";
				resultMsg = messageSourceAccessor.getMessage("update.fail");
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
	 * @Method healthChk
	 * @since 2020. 05. 06.
	 * @author 유경식
	 * @return Map<String,Object>
	 * @param UsrInfoVO
	 * @return
	 * @throws Exception
	 * @description 클라이언트 접속 체크
	 */
	@GetMapping(value = "/healthChk")
	@ResponseBody
	public Map<String, Object> healthChk() throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		resultMap.put("resultCd", resultCd);

		return resultMap;
	}

}