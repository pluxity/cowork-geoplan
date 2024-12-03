<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
// 공통 메세지 처리
const _REGIST_SUCCESS = "<spring:message code='regist.success' />";
const _REGIST_FAIL	 = "<spring:message code='regist.fail' />";
const _UPDATE_SUCCESS	 = "<spring:message code='update.success' />";
const _UPDATE_FAIL	 = "<spring:message code='update.fail' />";
const _DELETE_SUCCESS	 = "<spring:message code='delete.success' />";
const _DELETE_FAIL	 = "<spring:message code='delete.fail' />";
const _SEARCH_SUCCESS	 = "<spring:message code='search.success' />";
const _SEARCH_FAIL	 = "<spring:message code='search.fail' />";
const _SEARCH_EXIST	 = "<spring:message code='search.exist' />";
const _SEARCH_NOT_EXIST	 = "<spring:message code='search.not.exist' />";
const _SEARCH_DUP_CHK	 = "<spring:message code='search.dup.chk' />";
const _ERR_SQL	 = "<spring:message code='err.sql' />";
const _ERR_SYS	 = "<spring:message code='err.sys' />";
const _FAIL_MSG	 = "<spring:message code='fail.msg' />";
const _FAIL_SQL	 = "<spring:message code='fail.sql' />";
const _SAVE_CONFIRM	 = "<spring:message code='save.confirm' />";
const _REGIST_CONFIRM	 = "<spring:message code='regist.confirm' />";
const _UPDATE_CONFIRM	 = "<spring:message code='update.confirm' />";
const _DELETE_CONFIRM	 = "<spring:message code='delete.confirm' />";
const _ACCESS_NO_AUTH	 = "<spring:message code='access.no.auth' />";

// 공통 변수
const _CURRENT_URL = "${requestScope['javax.servlet.forward.request_uri']}"; // 현재 url

// 공통 설정 변수
const _POI_LINE_HEIGHT = parseInt("${systemInfoVO.poiLength}");
const _POI_ICON_RATIO = parseInt("${systemInfoVO.poiIconRatio}");
const _POI_TEXT_RATIO = parseInt("${systemInfoVO.poiTextRatio}");

// 투명과 고정 값
const _OPACITY_TRANSPARENT_ = 50;

const _CONTEXT_PATH_ = '<c:url value="/"/>';

</script>


