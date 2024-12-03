package com.plx.app.security;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *  @Project KNIS
 *  @Class CustomLoginFailureHandler
 *  @since 2020. 7. 23.
 *  @author 류중규
 *  @Description : 로그인 실패 핸들러
 */
public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

	/**
     * properties 파일 처리
     */
    @Resource(name="messageSourceAccessor")
    protected MessageSourceAccessor messageSourceAccessor;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
		HttpServletResponse response, AuthenticationException exception)
		throws IOException, ServletException {

		String resultMsg = "";
		ObjectMapper om = new ObjectMapper();

		if(exception instanceof BadCredentialsException) {
			resultMsg = messageSourceAccessor.getMessage("login.error.bad.credentials");
	    } else if(exception instanceof InternalAuthenticationServiceException) {
	    	resultMsg = messageSourceAccessor.getMessage("login.error.bad.credentials");
	    } else if(exception instanceof DisabledException) {
	     	resultMsg = messageSourceAccessor.getMessage("login.error.disaled");
	    } else if(exception instanceof CredentialsExpiredException) {
	      	resultMsg = messageSourceAccessor.getMessage("login.error.credential.expired");
	    }

		// 로그인 실패 exception 세션 제거
		clearAuthenticationAttributes(request);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCd", "fail");
		resultMap.put("resultMsg", resultMsg);

		String jsonString = om.writeValueAsString(resultMap);
		OutputStream out = response.getOutputStream();
		out.write(jsonString.getBytes());

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

}