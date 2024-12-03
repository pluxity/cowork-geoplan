package com.plx.app.interceptor;

import com.plx.app.admin.service.MapInfoService;
import com.plx.app.admin.vo.MapInfoVO;
import com.plx.app.admin.vo.UsrgrpMapVO;
import com.plx.app.admin.vo.UsrgrpMenuVO;
import com.plx.app.cmn.vo.LoginInfoVO;
import com.plx.app.cmn.vo.MenuVO;
import com.plx.app.constant.AdmMenu;
import com.plx.app.constant.CmnConst;
import com.plx.app.security.SecurityUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class RoleCheckInterceptor extends HandlerInterceptorAdapter {

	protected Log logger = LogFactory.getLog(getClass());
	protected Log logger_info = LogFactory.getLog("INFO_LOG");
	protected Log logger_error = LogFactory.getLog("ERROR_LOG");

	/**
	 * 도면정보
	 */
	@Autowired
	private MapInfoService mapInfoService;

	@Value("#{globalProp['uri.gnfire']}")
	private String gnfire;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		String currnetUri = request.getRequestURI();
        String currentPage = currnetUri.replaceAll(request.getContextPath(), "");

		// JWT 가 있을 경우 JWT 검증 후에 바로 리턴
		String roleStr = request.getParameter("role");
		if(!StringUtils.isEmpty(roleStr) && ("ERSSGIS".equals(roleStr) || "GNP".equals(roleStr))) {
//			String jwtVerification = getJWTVerification(jwtStr);
//			JSONParser resParser = new JSONParser();
//			Object resObj = resParser.parse(jwtVerification);
//			JSONObject resData = (JSONObject)resObj;

//			int mapNo = Integer.parseInt(request.getParameter("mapNo"));
//			MapInfoVO pMapInfoVO = new MapInfoVO();
//			pMapInfoVO.setMapNo(mapNo);
//			MapInfoVO mapInfoVO = mapInfoService.selectMapInfo(pMapInfoVO);

			// 검증된 도면인지 확인
//			boolean isVerified = "0".equals(mapInfoVO.getMapStts());

			return super.preHandle(request, response, handler);
		}

        // 사용자정보세션
        HttpSession session = request.getSession(true);
		LoginInfoVO loginInfo = (LoginInfoVO)session.getAttribute(LoginInfoVO.LOGIN_INFO);
		
		if(!SecurityUtils.hasRole("ROLE_ADMIN")) {
			// 사용자뷰어 페이지 도면 권한별 체크
			// map_stts 가 1인 도면은 검증 된 도면이라 
			// 모든 사용자에게 열어줘야함. 비 로그인 사용자에게도
			if(isChkURI(CmnConst.VIEWER_URI, request)) {
				if(!StringUtils.isEmpty(request.getParameter("mapNo"))) {
					int mapNo = Integer.parseInt(request.getParameter("mapNo"));
					MapInfoVO pMapInfoVO = new MapInfoVO();
					pMapInfoVO.setMapNo(mapNo);
					MapInfoVO mapInfoVO = mapInfoService.selectMapInfo(pMapInfoVO);

					// 검증된 도면인지 확인
					boolean isVerified = "1".equals(mapInfoVO.getMapStts());

					if(loginInfo == null) {
						if(!isVerified) {
							logger_info.debug("도면 접근 권한 없음");
							response.sendRedirect(request.getContextPath() + "/error/auth");
							return false;
						}
					} else {
						List<UsrgrpMapVO> usrgrpMapList = loginInfo.getUsrgrpMapList();
						boolean mapRoleChk = false;
						for(UsrgrpMapVO usrgrpMap : usrgrpMapList) {
							if(mapNo == usrgrpMap.getMapNo()) {
								mapRoleChk = true;
								break;
							}
						}
						if(!mapRoleChk && !isVerified) {
							logger_info.debug("도면 접근 권한 없음");
							response.sendRedirect(request.getContextPath() + "/error/auth");
							return false;
						}
					}
				}
			}

			// 관리자 페이지 권한별 체크
			List<MenuVO> admMenuList = AdmMenu.getAdmMenuList();
			boolean menuUrlChk = false;
			for(MenuVO menu : admMenuList) {
				if(currentPage.startsWith(menu.getMenuKey())) {
					menuUrlChk = true;
					break;
				}
			}
			if(menuUrlChk) {
				List<UsrgrpMenuVO> usrgrpMenuList = loginInfo.getUsrgrpMenuList();
				boolean menuRoleChk = false;
				for(UsrgrpMenuVO usrgrpMenu : usrgrpMenuList) {
					if(currentPage.contains(usrgrpMenu.getAllowUrl())) {
						menuRoleChk = true;
						break;
					}
				}
				if(!menuRoleChk) {
					logger_info.debug("관리자메뉴 접근 권한 없음");
					response.sendRedirect(request.getContextPath() + "/error/auth");
					return false;
				}
			}
		}

		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		super.postHandle(request, response, handler, modelAndView);
	}

	/**
	 * URL 패턴에 따라 인증 체크
	 * @param request
	 * @return
	 */
	private boolean isChkURI(String[] notURI, HttpServletRequest request) {
		String uri = request.getRequestURI();
		for(int i=0; i<notURI.length; i++){
			String path = request.getContextPath() + notURI[i];
			if(uri.startsWith(path)){
				return true;
			}
		}
		return false;
	}

}
