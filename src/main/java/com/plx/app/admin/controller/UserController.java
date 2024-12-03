package com.plx.app.admin.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plx.app.admin.service.MapCategoryService;
import com.plx.app.admin.service.POICategoryService;
import com.plx.app.admin.service.UsrInfoService;
import com.plx.app.admin.service.UsrgrpInfoService;
import com.plx.app.admin.vo.POICategoryVO;
import com.plx.app.admin.vo.UsrInfoVO;
import com.plx.app.admin.vo.UsrgrpInfoVO;
import com.plx.app.admin.vo.UsrgrpMapVO;
import com.plx.app.admin.vo.UsrgrpMenuVO;
import com.plx.app.admin.vo.UsrgrpPoiVO;
import com.plx.app.cmn.controller.BaseController;
import com.plx.app.cmn.vo.MenuVO;
import com.plx.app.constant.AdmMenu;
import com.plx.app.itf.controller.WebsocketHandler;

/**
 *  @Project KNIS
 *  @Class UserController
 *  @since 2019. 12. 18.
 *  @author 류중규
 *  @Description : 사용자 관리
 */
@Controller
@RequestMapping(value="/adm/user")
public class UserController extends BaseController {

	/**
	 * 사용자 정보
	 */
	@Autowired
	UsrInfoService usrInfoService;

	/**
	 *  사용자그룹 정보
	 */
	@Autowired
	UsrgrpInfoService usrgrpInfoService;

	/**
	 * 도면카테고리
	 */
	@Autowired
	MapCategoryService mapCategoryService;

	/**
	 * POI 카테고리 아이콘
	 */
	@Autowired
	POICategoryService poiCategoryService;

	/**
	 * spring security sessionRegistry
	 */
	@Autowired
	@Qualifier("sessionRegistry")
	private SessionRegistry sessionRegistry;

	/**
	 * @Method admMainIndex
	 * @since  2020. 1. 8.
	 * @author 류중규
	 * @return String
	 * @return
	 * @throws Exception
	 * @description 사용자관리 메인
	 */
	@RequestMapping(value="/index.do")
	public String admMainIndex() throws Exception {

		return "redirect:/adm/user/usrList.do";
	}

	/**
	 * @Method admUsrList
	 * @since  2019. 12. 18.
	 * @author 류중규
	 * @return String
	 * @param pUsrInfoVO
	 * @param model
	 * @return
	 * @throws Exception
	 * @description 사용자 목록 페이지
	 */
	@RequestMapping(value="/usrList.do")
	public String admUsrList(UsrInfoVO pUsrInfoVO, Model model) throws Exception {

		// 사용자그룹 리스트
		UsrgrpInfoVO pUsrgrpInfoVO = new UsrgrpInfoVO();
		pUsrgrpInfoVO.setPage(0);
		Map<String, Object> map = usrgrpInfoService.selectUsrgrpInfoList(pUsrgrpInfoVO);
		model.addAttribute("usrgrpList", map.get("list"));

		return "adm/user/usrList";
	}


	/**
	 * @Method getUsrInfoList
	 * @since  2019. 12. 18.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param pUsrInfoVO
	 * @return
	 * @throws Exception
	 * @description 사용자 목록
	 */
	@RequestMapping(value="/usrList.json")
	@ResponseBody
	public Map<String, Object> getUsrInfoList(UsrInfoVO pUsrInfoVO) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = "";

		try {
			pUsrInfoVO.setPageSize(20);
			int offset = pUsrInfoVO.getPageSize()*(pUsrInfoVO.getPage()-1);
			pUsrInfoVO.setOffset(offset);
			// 사용자 목록
			Map<String, Object> usrInfoList = usrInfoService.selectUsrInfoList(pUsrInfoVO);

			resultMap.put("result", usrInfoList.get("list")); // 목록
			resultMap.put("total", usrInfoList.get("total")); // 목록합계
			resultMap.put("usrInfo", pUsrInfoVO);

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
	 * @Method registUsrInfo
	 * @since  2019. 12. 18.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param pUsrInfoVO
	 * @return
	 * @throws Exception
	 * @description 사용자 등록
	 */
	@RequestMapping(value="/usrRegist.json")
	@ResponseBody
	public Map<String, Object> registUsrInfo(UsrInfoVO pUsrInfoVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("regist.success");

		try {
			// 아이디 중복체크
			UsrInfoVO chkUsrInfoVO = new UsrInfoVO();
			chkUsrInfoVO.setUsrId(pUsrInfoVO.getUsrId());
			Map<String, Object> dupResult = usrInfoService.selectUsrInfoList(chkUsrInfoVO);
			int dupCnt = (int) dupResult.get("total");
			if(dupCnt > 0) {
				resultCd = "fail";
				resultMsg = "사용중인 아이디입니다.";
			} else {
				// 비밀번호 암호화
				String encUsrPwd = BCrypt.hashpw(pUsrInfoVO.getUsrPwd(), BCrypt.gensalt());
				pUsrInfoVO.setUsrPwd(encUsrPwd);

				// 사용자 등록
				importLoginSession(pUsrInfoVO);
				int result = usrInfoService.insertUsrInfo(pUsrInfoVO);
				if(result == 0) {
					resultCd = "fail";
					resultMsg = messageSourceAccessor.getMessage("regist.fail");
				}
			}

		} catch(SQLException se) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("regist.fail");
			logger_error.error("SQLException", se);
		} catch(Exception e) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("regist.fail");
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}

	/**
	 * @Method viewUsrInfo
	 * @since  2019. 12. 18.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param pUsrInfoVO
	 * @return
	 * @throws Exception
	 * @description 사용자 정보
	 */
	@RequestMapping(value="/usrView.json")
	@ResponseBody
	public Map<String, Object> viewUsrInfo(UsrInfoVO pUsrInfoVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = "";

		try {
			// 사용자 정보
			UsrInfoVO result = usrInfoService.selectUsrInfo(pUsrInfoVO);
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
	 * @Method modifyUsrInfo
	 * @since  2019. 12. 18.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param pUsrInfoVO
	 * @return
	 * @throws Exception
	 * @description 사용자 정보 수정 처리
	 */
	@RequestMapping(value="/usrModify.json")
	@ResponseBody
	public Map<String, Object> modifyUsrInfo(UsrInfoVO pUsrInfoVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("update.success");

		try {
			if(!StringUtils.isEmpty(pUsrInfoVO.getUsrPwd())) {
				// 비밀번호 암호화
				String encUsrPwd = BCrypt.hashpw(pUsrInfoVO.getUsrPwd(), BCrypt.gensalt());
				pUsrInfoVO.setUsrPwd(encUsrPwd);
			}

			// 수정
			importLoginSession(pUsrInfoVO);
			int result = usrInfoService.updateUsrInfo(pUsrInfoVO);
			if(result == 0) {
				resultCd = "fail";
				resultMsg = messageSourceAccessor.getMessage("update.fail");
			}

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

	/**
	 * @Method removeUsrInfo
	 * @since  2019. 12. 18.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param pUsrInfoVO
	 * @param usrNoArr
	 * @return
	 * @throws Exception
	 * @description 사용자 삭제
	 */
	@RequestMapping(value="/usrRemove.json")
	@ResponseBody
	public Map<String, Object> removeUsrInfo(UsrInfoVO pUsrInfoVO,
			@RequestParam(value="usrNoArr[]", required=false) List<String> usrNoArr) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("delete.success");

		try {
			if(null != usrNoArr) pUsrInfoVO.setUsrNoList(usrNoArr);
			int result = usrInfoService.deleteUsrInfo(pUsrInfoVO);
			if(result == 0) {
				resultCd = "fail";
				resultMsg = messageSourceAccessor.getMessage("delete.fail");
			}
		} catch(SQLException se) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("delete.fail");
			logger_error.error("SQLException", se);
		} catch(Exception e) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("delete.fail");
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}



	/**
	 * @Method admUsrgrpList
	 * @since  2019. 12. 27.
	 * @author 류중규
	 * @return String
	 * @param pUsrgrpInfoVO
	 * @param model
	 * @return
	 * @throws Exception
	 * @description 사용자 그룹 목록 페이지
	 */
	@RequestMapping(value="/usrgrpList.do")
	public String admUsrgrpList(UsrgrpInfoVO pUsrgrpInfoVO, Model model) throws Exception {

		// 도면 카테고리 트리
		List<LinkedHashMap<String, Object>> mapCategoryTree = mapCategoryService.mapCategoryTree();
		model.addAttribute("mapCategoryTree", mapCategoryTree);

		// poi 대분류 리스트
		POICategoryVO pPOICategoryVO = new POICategoryVO();
		List<POICategoryVO> poiCategoryList = poiCategoryService.selectPOICategoryList(pPOICategoryVO);
		model.addAttribute("poiCategoryList", poiCategoryList);

		// 관리자 메뉴 리스트
		List<MenuVO> admMenuList = AdmMenu.getAdmMenuList();
		model.addAttribute("admMenuList", admMenuList);

		return "adm/user/usrgrpList";
	}

	/**
	 * @Method getUsrgrpInfoList
	 * @since  2019. 12. 27.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param pUsrgrpInfoVO
	 * @return
	 * @throws Exception
	 * @description 사용자 그룹 목록
	 */
	@RequestMapping(value="/usrgrpList.json")
	@ResponseBody
	public Map<String, Object> getUsrgrpInfoList(UsrgrpInfoVO pUsrgrpInfoVO) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = "";

		try {
			pUsrgrpInfoVO.setPageSize(20);
			int offset = pUsrgrpInfoVO.getPageSize()*(pUsrgrpInfoVO.getPage()-1);
			pUsrgrpInfoVO.setOffset(offset);
			// 사용자그룹 목록
			Map<String, Object> usrgrpInfoList = usrgrpInfoService.selectUsrgrpInfoList(pUsrgrpInfoVO);

			resultMap.put("result", usrgrpInfoList.get("list")); // 목록
			resultMap.put("total", usrgrpInfoList.get("total")); // 목록합계
			resultMap.put("usrgrpInfo", pUsrgrpInfoVO);

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
	 * @Method registUsrgrpInfo
	 * @since  2019. 12. 27.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param pUsrgrpInfoVO
	 * @return
	 * @throws Exception
	 * @description 사용자그룹 등록
	 */
	@RequestMapping(value="/usrgrpRegist.json")
	@ResponseBody
	public Map<String, Object> registUsrgrpInfo(UsrgrpInfoVO pUsrgrpInfoVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("regist.success");

		try {
			// 사용자그룹 등록
			importLoginSession(pUsrgrpInfoVO);
			int result = usrgrpInfoService.insertUsrgrpInfo(pUsrgrpInfoVO);
			if(result == 0) {
				resultCd = "fail";
				resultMsg = messageSourceAccessor.getMessage("regist.fail");
			}

		} catch(SQLException se) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("regist.fail");
			logger_error.error("SQLException", se);
		} catch(Exception e) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("regist.fail");
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}

	/**
	 * @Method viewUsrgrpInfo
	 * @since  2019. 12. 27.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param pUsrgrpInfoVO
	 * @return
	 * @throws Exception
	 * @description 사용자그룹 정보
	 */
	@RequestMapping(value="/usrgrpView.json")
	@ResponseBody
	public Map<String, Object> viewUsrgrpInfo(UsrgrpInfoVO pUsrgrpInfoVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = "";

		try {
			// 사용자그룹 정보
			UsrgrpInfoVO result = usrgrpInfoService.selectUsrgrpInfo(pUsrgrpInfoVO);
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

	@RequestMapping(value="/usrgrpModify.json")
	@ResponseBody
	public Map<String, Object> modifyUsrgrpInfo(UsrgrpInfoVO pUsrgrpInfoVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("update.success");

		try {
			// 수정
			importLoginSession(pUsrgrpInfoVO);
			int result = usrgrpInfoService.updateUsrgrpInfo(pUsrgrpInfoVO);
			if(result == 0) {
				resultCd = "fail";
				resultMsg = messageSourceAccessor.getMessage("update.fail");
			}

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

	/**
	 * @Method removeUsrgrpInfo
	 * @since  2019. 12. 27.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param pUsrgrpInfoVO
	 * @param grpNoArr
	 * @return
	 * @throws Exception
	 * @description 사용자그룹 삭제
	 */
	@RequestMapping(value="/usrgrpRemove.json")
	@ResponseBody
	public Map<String, Object> removeUsrgrpInfo(UsrgrpInfoVO pUsrgrpInfoVO,
			@RequestParam(value="grpNoArr[]", required=false) List<String> grpNoArr) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("delete.success");

		try {
			if(null != grpNoArr) pUsrgrpInfoVO.setGrpNoList(grpNoArr);
			int result = usrgrpInfoService.deleteUsrgrpInfo(pUsrgrpInfoVO);
			if(result == 0) {
				resultCd = "fail";
				resultMsg = messageSourceAccessor.getMessage("delete.fail");
			}
		} catch(SQLException se) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("delete.fail");
			logger_error.error("SQLException", se);
		} catch(Exception e) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("delete.fail");
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}


	/**
	 * @Method registUsrgrpMap
	 * @since  2020. 1. 3.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param grpNo
	 * @param mapNoList
	 * @return
	 * @throws Exception
	 * @description 사용자그룹 도면권한 등록
	 */
	@RequestMapping(value="/usrgrpMapRegist.json")
	@ResponseBody
	public Map<String, Object> registUsrgrpMap(@RequestParam(value="grpNo") int grpNo,
			@RequestParam(value="mapNo[]", required=false) List<String> mapNoList) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("save.success");

		try {
			UsrgrpMapVO pUsrgrpMapVO = new UsrgrpMapVO();
			pUsrgrpMapVO.setGrpNo(grpNo);

			// 도면권한 삭제
			usrgrpInfoService.deleteUsrgrpMap(pUsrgrpMapVO);

			// 도면 권한 등록
			if(null != mapNoList) {
				if(mapNoList.size() > 0) {
					pUsrgrpMapVO.setUsrgrpMapNoList(mapNoList);
					importLoginSession(pUsrgrpMapVO);
					int result = usrgrpInfoService.insertUsrgrpMap(pUsrgrpMapVO);
					if(result == 0) {
						resultCd = "fail";
						resultMsg = messageSourceAccessor.getMessage("save.fail");
					}
				}
			}

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

	/**
	 * @Method registUsrgrpPoi
	 * @since  2020. 1. 7.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param grpNo
	 * @param poiRoleType1
	 * @param poiRoleType2
	 * @return
	 * @throws Exception
	 * @description poi 권한 저장
	 */
	@RequestMapping(value="/usrgrpPoiRegist.json")
	@ResponseBody
	public Map<String, Object> registUsrgrpPoi(@RequestParam(value="grpNo") int grpNo,
			@RequestParam(value="poiRoleType1[]", required=false) List<String> poiRoleType1,
			@RequestParam(value="poiRoleType2[]", required=false) List<String> poiRoleType2) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("save.success");

		try {
			UsrgrpPoiVO pUsrgrpPoiVO = new UsrgrpPoiVO();
			pUsrgrpPoiVO.setGrpNo(grpNo);

			// poi권한 삭제
			usrgrpInfoService.deleteUsrgrpPoi(pUsrgrpPoiVO);

			importLoginSession(pUsrgrpPoiVO);

			// poi 표출 권한 등록
			if(null != poiRoleType1) {
				if(poiRoleType1.size() > 0) {
					pUsrgrpPoiVO.setCategory1NoList(poiRoleType1);
					pUsrgrpPoiVO.setRoleType("1");
					int result = usrgrpInfoService.insertUsrgrpPoi(pUsrgrpPoiVO);
					if(result == 0) {
						resultCd = "fail";
						resultMsg = messageSourceAccessor.getMessage("save.fail");
					}
				}
			}

			// poi 제어 권한 등록
			if(null != poiRoleType2) {
				if(poiRoleType2.size() > 0) {
					pUsrgrpPoiVO.setCategory1NoList(poiRoleType2);
					pUsrgrpPoiVO.setRoleType("2");
					int result = usrgrpInfoService.insertUsrgrpPoi(pUsrgrpPoiVO);
					if(result == 0) {
						resultCd = "fail";
						resultMsg = messageSourceAccessor.getMessage("save.fail");
					}
				}
			}

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


	/**
	 * @Method registUsrgrpMenu
	 * @since  2020. 1. 14.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param grpNo
	 * @param allowUrlList
	 * @return
	 * @throws Exception
	 * @description 관리자 메뉴 등록
	 */
	@RequestMapping(value="/usrgrpMenuRegist.json")
	@ResponseBody
	public Map<String, Object> registUsrgrpMenu(@RequestParam(value="grpNo") int grpNo,
			@RequestParam(value="allowUrl[]", required=false) List<String> allowUrlList) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("save.success");

		try {
			UsrgrpMenuVO pUsrgrpMenuVO = new UsrgrpMenuVO();
			pUsrgrpMenuVO.setGrpNo(grpNo);

			// 메뉴권한 삭제
			usrgrpInfoService.deleteUsrgrpMenu(pUsrgrpMenuVO);

			// 메뉴 권한 등록
			if(null != allowUrlList) {
				if(allowUrlList.size() > 0) {
					pUsrgrpMenuVO.setAllowUrlList(allowUrlList);
					importLoginSession(pUsrgrpMenuVO);
					int result = usrgrpInfoService.insertUsrgrpMenu(pUsrgrpMenuVO);
					if(result == 0) {
						resultCd = "fail";
						resultMsg = messageSourceAccessor.getMessage("save.fail");
					}
				}
			}

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



	/**
	 * @Method viewUsrgrpRole
	 * @since  2020. 1. 3.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param grpNo
	 * @return
	 * @throws Exception
	 * @description 사용자그룹 도면정보
	 */
	@RequestMapping(value="/usrgrpRole.json")
	@ResponseBody
	public Map<String, Object> viewUsrgrpRole(@RequestParam(value="grpNo") int grpNo) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("search.success");

		try {
			// 도면권한 목록
			UsrgrpMapVO pUsrgrpMapVO = new UsrgrpMapVO();
			pUsrgrpMapVO.setGrpNo(grpNo);
			List<UsrgrpMapVO> usrgrpMapList = usrgrpInfoService.selectUsrgrpMapList(pUsrgrpMapVO);
			resultMap.put("usrgrpMapList", usrgrpMapList);

			// POI권한 목록
			UsrgrpPoiVO pUsrgrpPoiVO = new UsrgrpPoiVO();
			pUsrgrpPoiVO.setGrpNo(grpNo);
			List<UsrgrpPoiVO> usrgrpPoiList = usrgrpInfoService.selectUsrgrpPoiList(pUsrgrpPoiVO);
			resultMap.put("usrgrpPoiList", usrgrpPoiList);

			// 메뉴 권한 목록
			UsrgrpMenuVO pUsrgrpMenuVO = new UsrgrpMenuVO();
			pUsrgrpMenuVO.setGrpNo(grpNo);
			List<UsrgrpMenuVO> usrgrpMenuList = usrgrpInfoService.selectUsrgrpMenuList(pUsrgrpMenuVO);
			resultMap.put("usrgrpMenuList", usrgrpMenuList);

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
	 * @Method viewLoginedUsers
	 * @since  2020. 05. 22.
	 * @author 유경식
	 * @return Map<String,Object>
	 * @param
	 * @throws Exception
	 * @description 사용자 접속여부
	 */
	@RequestMapping(value="/getLoginedUsers.json")
	@ResponseBody
	public Map<String, Object> viewLoginedUsers() throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("search.success");

		WebsocketHandler websocketHandler = new WebsocketHandler();
		resultMap.put("users", websocketHandler.getLoginIdSet());

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);
		return resultMap;
	}

	/**
	 * @Method getLoginedUsersBySec
	 * @since 2020. 7. 24.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @return
	 * @throws Exception
	 * @description 현재 접속자수 - spring security
	 */
	@RequestMapping(value = "/getLoginedUsersBySec.json")
	@ResponseBody
	public Map<String, Object> getLoginedUsersBySec() throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = "";

		// 현재 접속중인 아이디 추출후 저장
		List<String> loginUsrList = new ArrayList<String>();
		List<Object> principals = sessionRegistry.getAllPrincipals();
		for (Object	principal : principals) {
			if (principal instanceof User) {
				List<SessionInformation> sessionInformation = sessionRegistry.getAllSessions(principal, false);
	            if (!sessionInformation.isEmpty()) {
	            	loginUsrList.add(((User) principal).getUsername());
	            }
			}
		}
		/*
		for (int i = 0; i < principals.size(); i++) {
			List<SessionInformation> userSessions = sessionRegistry.getAllSessions(principals.get(i), false);
			if (userSessions != null) {
				for (SessionInformation sessionInformation : userSessions) {
					if (sessionInformation.isExpired() == false) {
						User session = (User) sessionInformation.getPrincipal();
						loginUsrList.add(session.getUsername().toString());
					}
				}
			}
		}
		*/

		resultMap.put("loginUsrCnt", loginUsrList.size());
		resultMap.put("loginUsrList", loginUsrList);
		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}

	/**
	 * @Method expireUsrSession
	 * @since  2020. 7. 25.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param usrId
	 * @return
	 * @throws Exception
	 * @description 사용자 강제 로그아웃
	 */
	@RequestMapping(value = "/expireUsrSession.json")
	@ResponseBody
	public Map<String, Object> expireUsrSession(@RequestParam(value = "usrId") String usrId) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = "로그아웃되었습니다.";

		// sessionRegistry.getSessionInformation(usrId).expireNow();
		// 현재 접속중인 관리자
		List<Object> allUserList = sessionRegistry.getAllPrincipals();
		for (int i = 0; i < allUserList.size(); i++) {
			List<SessionInformation> userSessions = sessionRegistry.getAllSessions(allUserList.get(i), false);
			if (userSessions != null) {
				for (SessionInformation sessionInformation : userSessions) {
					if (sessionInformation.isExpired() == false) {
						User session = (User) sessionInformation.getPrincipal();
						// 요청 아이디만 세션 만료 처리
						if ((session.getUsername().equals(usrId))) {
							sessionInformation.expireNow();
						}
					}
				}
			}
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}

	/**
	 * @Method usrExcelDownload
	 * @since  2020. 06. 30.
	 * @author 이유리
	 * @param fieldNmList
	 * @param pUsrInfoVO
	 * @param model
	 * @return String
	 * @throws Exception
	 * @description 사용자 목록 엑셀 다운로드
	 */
	@RequestMapping(value="/usrExceldownload.do")
	public String usrExcelDownload(@RequestParam(value="fieldNmList") String fieldNmList, UsrInfoVO pUsrInfoVO, Model model) throws Exception {

		String[] fieldNmArr = fieldNmList.trim().split(",");

		pUsrInfoVO.setPageSize(0);

		model.addAttribute("sheetName", "사용자목록");
		model.addAttribute("fieldNmList", fieldNmArr);
		model.addAttribute("VOList", usrInfoService.selectUsrInfoList(pUsrInfoVO).get("list"));

		return "excelDownloadView";
	}

	/**
	 * @Method usrGrpExcelDownload
	 * @since  2020. 07. 17.
	 * @author 이유리
	 * @param pUsrgrpInfoVO
	 * @param fieldNmList
	 * @param model
	 * @return String
	 * @throws Exception
	 * @description 사용자 그룹 목록 엑셀 다운로드
	 */
	@RequestMapping(value="/usrgrpExceldownload.do")
	public String usrGrpExcelDownload(@RequestParam(value="fieldNmList") String fieldNmList, UsrgrpInfoVO pUsrgrpInfoVO, Model model) throws Exception {

		String[] fieldNmArr = fieldNmList.trim().split(",");

		pUsrgrpInfoVO.setPageSize(0);

		model.addAttribute("sheetName", "사용자그룹목록");
		model.addAttribute("fieldNmList", fieldNmArr);
		model.addAttribute("VOList", usrgrpInfoService.selectUsrgrpInfoList(pUsrgrpInfoVO).get("list"));

		return "excelDownloadView";
	}

	/*
	@RequestMapping(value="/ad/AdAdRLogL.do")
	public String adminRLogList(Model model) throws Exception {

		ArrayList<String> id = new ArrayList<String>();

		// 현재 접속중인 아이디 추출후 Array 저장
		List<Object> allUserList = sessionRegistry.getAllPrincipals();
        for(int i=0; i<allUserList.size(); i++) {
            List<SessionInformation> userSessions = sessionRegistry.getAllSessions(allUserList.get(i), false);
            if (userSessions != null) {
    		    for (SessionInformation sessionInformation : userSessions) {
        		    if(sessionInformation.isExpired() == false) {
    		    		User session = (User) sessionInformation.getPrincipal();
    		    		id.add(session.getUsername().toString());
    		    	}
    		    }
    		}
        }

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);

		// 현재 접속중인 아이디 정보 추출
		if(id.size() > 0) {
			List<AdAdAdminModel> adAdAdminList = adAdLogService.getAdminLogInfo(map);
			model.addAttribute("adAdAdminList", adAdAdminList);
		}

		model.addAttribute("total", id.size()); // 접속 세션수

		return "ad/AdAdRLogL.tiles";
	}


	/**
	 * @author 류중규
	 * @version 1.0,  2014. 6. 18.
	 * @see  adminSessionExpire
	 * @description 관리자 세션 강제 종료
	 * @param id
	 * @param model
	 * @return
	 * @throws Exception String
	 */
	/*
	@RequestMapping(value="/ad/AdAdSessExpire.json")
	public String adminSessionExpire(@RequestParam(value="id", required=true) String id, Model model) throws Exception {

		// 현재 접속중인 관리자
		List<Object> allUserList = sessionRegistry.getAllPrincipals();
        for(int i=0; i<allUserList.size(); i++) {
            List<SessionInformation> userSessions = sessionRegistry.getAllSessions(allUserList.get(i), false);
            if (userSessions != null) {
    		    for (SessionInformation sessionInformation : userSessions) {
        		    if(sessionInformation.isExpired() == false) {
    		    		User session = (User) sessionInformation.getPrincipal();
    		    		// 요청 아이디만 세션 만료 처리
    		    		if((session.getUsername().equals(id))) {
      		    			sessionInformation.expireNow();
    		    		}
    		    	}
    		    }
    		}
        }

		return "cm/json";
	}
	*/


}
