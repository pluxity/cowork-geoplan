package com.plx.app.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * @Project KNIS
 * @Class CustomAccessDeniedHandler
 * @since 2020. 7. 23.
 * @author 류중규
 * @Description : spring security 접근 거부 액세스 후 처리
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	private String errorPage;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {				
		
		String ajaxHeader = request.getHeader("X-Requested-With");
		String result = "";

		if (StringUtils.isEmpty(ajaxHeader) || !"XMLHttpRequest".equals(ajaxHeader)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Object principal = auth.getPrincipal();

			if (principal instanceof UserDetails) {
				String username = ((UserDetails) principal).getUsername();
				request.setAttribute("username", username);
			}

			errorPage = "/error/auth";
			response.sendRedirect(errorPage);

		} else {
			result = "Access Denied";

			response.getWriter().print(result);
			response.getWriter().flush();
		}
	}

}