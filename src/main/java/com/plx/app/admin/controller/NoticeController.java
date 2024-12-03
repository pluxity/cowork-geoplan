package com.plx.app.admin.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.plx.app.admin.service.MapInfoService;
import com.plx.app.admin.service.NoticeService;
import com.plx.app.admin.vo.MapInfoVO;
import com.plx.app.admin.vo.NoticeVO;
import com.plx.app.cmn.controller.BaseController;

@Controller
@RequestMapping(value = "/adm/notice")
public class NoticeController extends BaseController {

	/**
	 * 공지사항 서비스
	 */
	@Autowired
	NoticeService noticeService;

	/**
	 * 도면 서비스
	 */
	@Autowired
	MapInfoService mapInfoService;

	@RequestMapping(value = "/noticeList.do")
	public String noticeList(NoticeVO noticeVO, Model model) throws Exception {

		model.addAttribute("searchKeyword", noticeVO.getSearchKeyword());
		model.addAttribute("searchType", noticeVO.getSearchType());
		try {

			int grpNo = sessionLoginInfo().getGrpNo();

			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("grpNo", grpNo);
			// 도면 카테고리 트리
			List<LinkedHashMap<String, Object>> mapCategoryTree = noticeService.selectMaptreeByGrpNo(paramMap);
			model.addAttribute("mapCategoryTree", mapCategoryTree);

		} catch (SQLException se) {
			logger_error.error("SQLException", se);
		} catch (Exception e) {
			logger_error.error("Exception", e);
		}

		return "adm/notice/noticeList";
	}

	@RequestMapping(value = "/selectNoticeList.json")
	@ResponseBody
	public Map<String, Object> selectNoticeList(@ModelAttribute("NoticeVO") NoticeVO pNoticeVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("search.success");

		try {

			int offset = pNoticeVO.getPageSize() * (pNoticeVO.getPage() - 1);
			pNoticeVO.setOffset(offset);

			Map<String, Object> noticeList = noticeService.selectNoticeList(pNoticeVO);

			resultMap.put("result", noticeList.get("list")); // 목록
			resultMap.put("total", noticeList.get("total")); // 목록합계

			resultMap.put("notice", pNoticeVO);

		} catch (SQLException se) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("search.fail");
			logger_error.error("SQLException", se);
		} catch (Exception e) {
			resultCd = "fail";
			resultMsg = messageSourceAccessor.getMessage("search.fail");
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}

	@RequestMapping(value = "/selectNoticDetail.json")
	@ResponseBody
	public Map<String, Object> selectNoticDetail(@ModelAttribute("NoticeVO") NoticeVO pNoticeVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("search.success");

		try {
			NoticeVO res = noticeService.selectNotice(pNoticeVO);
			resultMap.put("result", res);
		} catch (SQLException se) {
			logger_error.error("SQLException", se);
		} catch (Exception e) {
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}

	@RequestMapping(value = "/noticeRegist.json")
	@ResponseBody
	public Map<String, Object> noticeRegist(@ModelAttribute("NoticeVO") NoticeVO pNoticeVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("regist.success");

		try {
			importLoginSession(pNoticeVO);
			int res = noticeService.insertNotice(pNoticeVO);
			resultMap.put("result", res);
		} catch (SQLException se) {
			logger_error.error("SQLException", se);
		} catch (Exception e) {
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}

	@RequestMapping(value = "/noticeUpdate.json")
	@ResponseBody
	public Map<String, Object> noticeUpdate(@ModelAttribute("NoticeVO") NoticeVO pNoticeVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("update.success");

		try {
			int res = noticeService.updateNotice(pNoticeVO);
			resultMap.put("result", res);
		} catch (SQLException se) {
			logger_error.error("SQLException", se);
		} catch (Exception e) {
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}

	@RequestMapping(value = "/updateNoticeActive.json")
	@ResponseBody
	public Map<String, Object> updateNoticeActive(@ModelAttribute("NoticeVO") NoticeVO pNoticeVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("update.success");

		try {
			int res = noticeService.updateNoticeActive(pNoticeVO);
			resultMap.put("result", res);
		} catch (SQLException se) {
			logger_error.error("SQLException", se);
		} catch (Exception e) {
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}

	@RequestMapping(value = "/noticeRemove.json")
	@ResponseBody
	public Map<String, Object> noticeRemove(@ModelAttribute("NoticeVO") NoticeVO pNoticeVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String resultCd = "success";
		String resultMsg = messageSourceAccessor.getMessage("delete.success");

		try {
			importLoginSession(pNoticeVO);
			int res = noticeService.deleteNotice(pNoticeVO);

			resultMap.put("result", res);
		} catch (SQLException se) {
			logger_error.error("SQLException", se);
		} catch (Exception e) {
			logger_error.error("Exception", e);
		}

		resultMap.put("resultCd", resultCd);
		resultMap.put("resultMsg", resultMsg);

		return resultMap;
	}

	/**
	 * @Method noticeListExcelDownload
	 * @since 2020. 07. 17.
	 * @author 이유리
	 * @param fieldNmList
	 * @param pNoticeVO
	 * @param model
	 * @return String
	 * @throws Exception
	 * @description 공지사항 목록 엑셀 다운로드
	 */
	@RequestMapping(value = "/noticeExceldownload.do")
	public String noticeListExcelDownload(@RequestParam(value = "fieldNmList") String fieldNmList, NoticeVO pNoticeVO,
			Model model) throws Exception {
		Map<String, Object> pMap = new HashMap<String, Object>();
		Map<String, Object> mapNameMap = new HashMap<String, Object>();
		String[] fieldNmArr = fieldNmList.trim().split(",");
		MapInfoVO pMapInfoVO = new MapInfoVO();
		pMapInfoVO.setPageSize(0);

		List<MapInfoVO> mapList = (List<MapInfoVO>) mapInfoService.selectMapInfoList(pMapInfoVO).get("list");
		for (MapInfoVO mapInfoVO : mapList) {
			mapNameMap.put("\"" + mapInfoVO.getMapNo() + "\"", mapInfoVO.getMapNm());
		}
		pMap.put("mapNameMap", mapNameMap);
		pNoticeVO.setPageSize(0);

		model.addAttribute("sheetName", "공지사항");
		model.addAttribute("fieldNmList", fieldNmArr);
		model.addAttribute("VOList", noticeService.selectNoticeList(pNoticeVO).get("list"));
		model.addAttribute("categoryMap", pMap);

		return "excelDownloadView";
	}

}
