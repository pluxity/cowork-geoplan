package com.plx.app.security;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plx.app.admin.service.SystemInfoService;
import com.plx.app.admin.service.UsrInfoService;
import com.plx.app.admin.service.UsrgrpInfoService;

/**
 * @Project KNIS
 * @Class CustomLoginSuccessHandler
 * @since 2020. 7. 23.
 * @author 류중규
 * @Description : spring security 로그인 성공후 처리
 */
@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

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
	 * properties 파일 처리
	 */
	@Resource(name = "messageSourceAccessor")
	protected MessageSourceAccessor messageSourceAccessor;

	/**
	 * 시스템 서비스
	 */
	@Autowired
	SystemInfoService systemInfoService;

	/**
	 * 로그인 성공 핸들링 오버라이드
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		Map<String,Object> resultMap = new SecurityUtils().doLogin(request, response);
		ObjectMapper om = new ObjectMapper();
		String jsonString = om.writeValueAsString(resultMap);
		OutputStream out = response.getOutputStream();
		out.write(jsonString.getBytes());
	}
}
