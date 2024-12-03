package com.plx.app.security;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.plx.app.admin.service.SystemInfoService;
import com.plx.app.admin.service.UsrInfoService;
import com.plx.app.admin.service.UsrgrpInfoService;
import com.plx.app.admin.vo.UsrInfoVO;
import com.plx.app.admin.vo.UsrgrpMapVO;
import com.plx.app.admin.vo.UsrgrpMenuVO;
import com.plx.app.admin.vo.UsrgrpPoiVO;
import com.plx.app.cmn.vo.LoginInfoVO;
import com.plx.app.cmn.vo.MenuVO;
import com.plx.app.constant.AdmMenu;
import com.plx.app.constant.CmnConst;
import com.plx.app.util.CustomBeanUtils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @Project KNIS
 * @Class SecurityUtils
 * @since 2020. 7. 31.
 * @author 류중규
 * @Description : spring security utils
 */
@Component
public class SecurityUtils {
    /**
     * properties 파일 처리
     */
    @Resource(name = "messageSourceAccessor")
    protected MessageSourceAccessor messageSourceAccessor;

    /**
     * 시스템 서비스
     */

    private static String loginKey;
    private static String adminId;
    private static String userId;

    @Value("#{globalProp['login.key']}")
    public void setLoginKey(String loginKey) {
        SecurityUtils.loginKey = loginKey;
    }

    @Value("#{globalProp['login.admin.id']}")
    public void setAdminId(String adminId) {
        SecurityUtils.adminId = adminId;
    }

    @Value("#{globalProp['login.user.id']}")
    public void setUserId(String userId) {
        SecurityUtils.userId = userId;
    }

    /**
     * @Method hasRole
     * @since 2020. 7. 31.
     * @author 류중규
     * @return boolean
     * @param role
     * @return
     * @description 권한 체크
     */
    public static boolean hasRole(String role) {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null)
            return false;

        Authentication authentication = context.getAuthentication();
        if (authentication == null)
            return false;

        for (GrantedAuthority auth : authentication.getAuthorities()) {
            if (role.equals(auth.getAuthority()))
                return true;
        }

        return false;
    }

    // public void authWithoutPassword(User user) {
    // List<Privilege> privileges =
    // user.getRoles().stream().map(Role::getPrivileges).flatMap(Collection::stream)
    // .distinct().collect(Collectors.toList());
    // List<GrantedAuthority> authorities = privileges.stream().map(p -> new
    // SimpleGrantedAuthority(p.getName()))
    // .collect(Collectors.toList());
    // Authentication authentication = new UsernamePasswordAuthenticationToken(user,
    // null, authorities);
    // SecurityContextHolder.getContext().setAuthentication(authentication);
    // }

    /*
    public void customLogin(ServletRequest request, ServletResponse response) throws IOException, ServletException {

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        User user = new User(SecurityUtils.adminId, "pluxity123!@#", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        HttpServletRequest httpRequest = (HttpServletRequest) request; // class casting
        HttpServletResponse httpResponse = (HttpServletResponse) response; // class casting

        this.doLogin(httpRequest, httpResponse);
    }
    */
    /*
    public void customLogin(ServletRequest request, ServletResponse response) throws IOException, ServletException {

        String token = request.getParameter(SecurityUtils.loginKey);

        if(StringUtils.isEmpty(token)) return;

        RestApiService restApiService = CustomBeanUtils.getBean(RestApiService.class);
        Boolean isAuth = restApiService.chkToken(token);
        Map<String, String> jwt = this.parseJWT(token);
        String role = jwt.get("role");
        Boolean isAdmin = "S00".equals(role) || "S10".equals(role);

        if (isAuth) {
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(isAdmin ? "ROLE_ADMIN" : "ROLE_USER"));

            User user = new User(isAdmin ? SecurityUtils.adminId : SecurityUtils.userId, "1234", authorities);
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);

            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);

            if (request instanceof HttpServletRequest) {

                HttpServletRequest httpRequest = (HttpServletRequest) request; // class casting
                HttpServletResponse httpResponse = (HttpServletResponse) response; // class casting

                // String userAgent = httpRequest.getHeader("user-agent").toUpperCase();
                // HttpSession session = httpRequest.getSession(true);
                // CustomLoginSuccessHandler custom = new CustomLoginSuccessHandler();
                this.doLogin(httpRequest, httpResponse);
            }
        }
    }
    */

    public Map<String, Object> doLogin(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String resultCd = "success";
        String resultMsg = "";
        String locationUrl = "";
        boolean changePwFlag = false;

        UsrInfoService usrInfoService = CustomBeanUtils.getBean(UsrInfoService.class);
        SystemInfoService systemInfoService = CustomBeanUtils.getBean(SystemInfoService.class);
        UsrgrpInfoService usrgrpInfoService = CustomBeanUtils.getBean(UsrgrpInfoService.class);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            // 사용자 정보 조회
            UsrInfoVO pUsrInfoVO = new UsrInfoVO();

            String usrId = SecurityContextHolder.getContext().getAuthentication().getName();
            pUsrInfoVO.setUsrId(usrId);
            pUsrInfoVO = usrInfoService.selectUsrInfo(pUsrInfoVO);


            resultCd = "success";

            // 사용자,그룹 기본정보
            LoginInfoVO loginInfoVO = new LoginInfoVO();
            BeanUtils.copyProperties(pUsrInfoVO, loginInfoVO);

            // 도면 접근 권한
            UsrgrpMapVO pUsrgrpMapVO = new UsrgrpMapVO();
            pUsrgrpMapVO.setGrpNo(loginInfoVO.getGrpNo());
            List<UsrgrpMapVO> usrgrpMapList = usrgrpInfoService.selectUsrgrpMapList(pUsrgrpMapVO);
            loginInfoVO.setUsrgrpMapList(usrgrpMapList);

            // poi대분류 리스트
            UsrgrpPoiVO pUsrgrpPoiVO = new UsrgrpPoiVO();
            pUsrgrpPoiVO.setGrpNo(loginInfoVO.getGrpNo());
            List<UsrgrpPoiVO> usrgrpPoiList = usrgrpInfoService.selectUsrgrpPoiList(pUsrgrpPoiVO);
            loginInfoVO.setUsrgrpPoiList(usrgrpPoiList);

            // 관리자 메뉴 권한
            UsrgrpMenuVO pUsrgrpMenuVO = new UsrgrpMenuVO();
            pUsrgrpMenuVO.setGrpNo(loginInfoVO.getGrpNo());
            List<UsrgrpMenuVO> usrgrpMenuList = usrgrpInfoService.selectUsrgrpMenuList(pUsrgrpMenuVO);
            loginInfoVO.setUsrgrpMenuList(usrgrpMenuList);

            // 좌측 관리자 메뉴 생성
            List<MenuVO> leftMenuList = new ArrayList<MenuVO>();
            if (SecurityUtils.hasRole("ROLE_ADMIN")) {
                leftMenuList = AdmMenu.getAdmMenuList();
            } else {
                for (MenuVO menu : AdmMenu.getAdmMenuList()) {
                    for (UsrgrpMenuVO usrgrpMenu : usrgrpMenuList) {
                        if (menu.getMenuKey().equals(usrgrpMenu.getAllowUrl())) {
                            leftMenuList.add(menu);
                        }
                    }
                }
            }
            loginInfoVO.setLeftMenuList(leftMenuList);

            // 사용자정보 세션 생성
            HttpSession session = request.getSession(true);
            session.setAttribute(LoginInfoVO.LOGIN_INFO, loginInfoVO);

            // 로그인 시간 갱신
            String lastLoginDt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            pUsrInfoVO.setLastLoginDt(lastLoginDt);
            // usrInfoService.updateUsrInfo(pUsrInfoVO);

            // 비밀번호 변경일자 체크
            String lastChangePwDt = pUsrInfoVO.getLastPwdChangeDt();
            if (!StringUtils.isEmpty(lastChangePwDt)) {
                LocalDate startDate = LocalDate.now();
                LocalDate endDate = LocalDate.from(
                        LocalDateTime.parse(lastChangePwDt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                long days = ChronoUnit.DAYS.between(startDate, endDate);
                changePwFlag = days >= 180; // 6달
            } else {
                // changePwFlag = true; //최초로그인시 비밀번호 변경 안하도록 여기 주석 처리함
            }

            // 사용자 그룹 도면 조회
            int mapNo = 0;
            if (usrgrpMapList != null && usrgrpMapList.size() != 0 && !SecurityUtils.hasRole("ROLE_API")) {
                mapNo = usrgrpMapList.get(0).getMapNo();
            }

            locationUrl = CmnConst.LOCATION_URL_2D;
//            if (SecurityUtils.hasRole("ROLE_ADMIN")) {
//                locationUrl = CmnConst.LOCATION_URL_ADMIN;
//            } else {
//                if (usrgrpMapList.isEmpty()) {
//                    resultCd = "fail";
//                    resultMsg = "NONE_ACCESSIBLE_MAP";
//                    locationUrl = CmnConst.LOCATION_URL_ERROR;
//                } else {
//                    locationUrl = CmnConst.LOCATION_URL_2D;
//                //    locationUrl = CmnConst.LOCATION_URL_USER + mapNo; // 모바일 아닐 경우
//                }
//            }

        } catch (SQLException se) {
            resultCd = "fail";
            // resultMsg = messageSourceAccessor.getMessage("search.fail");
            // logger.error("SQLException", se);
            se.printStackTrace();
        } catch (Exception e) {
            resultCd = "fail";
            // resultMsg = messageSourceAccessor.getMessage("search.fail");
            // logger_error.error("Exception", e);
            e.printStackTrace();
        }

        // 로그인 실패 exception 세션 제거
        clearAuthenticationAttributes(request);

        resultMap.put("resultCd", resultCd);
        resultMap.put("resultMsg", resultMsg);

        // 리턴url 처리
        String returnUrl = getReturnUrl(request, response);
        if (!StringUtils.isEmpty(returnUrl)) {
            locationUrl = returnUrl;
        }
        resultMap.put("locationUrl", locationUrl);
        resultMap.put("changePwFlag", changePwFlag);

        return resultMap;

    }

    /**
     * @Method clearAuthenticationAttributes
     * @since 2020. 7. 23.
     * @author 류중규
     * @return void
     * @param request
     * @description 로그인 실패 exception 세션 제거
     */
    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return;
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    /**
     * 로그인 하기 전의 요청했던 URL을 알아낸다.
     *
     * @param request
     * @param response
     * @return
     */
    protected String getReturnUrl(HttpServletRequest request, HttpServletResponse response) {
        RequestCache requestCache = new HttpSessionRequestCache();
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest == null) {
            return request.getSession().getServletContext().getContextPath();
        }
        return savedRequest.getRedirectUrl();
    }

    private Map<String, String> parseJWT(String token) {

        String[] strArr = token.split("\\.");
        byte[] decodedBytes = Base64.decodeBase64(strArr[1]);

        String json = new String(decodedBytes);

        Gson gson = new Gson();

        Map<String,String> map = gson.fromJson(json, HashMap.class);

        return map;
    }

}
