package com.plx.app.security;

import java.util.List;

import org.springframework.context.ApplicationListener;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.stereotype.Component;

/**
 * @Project KNIS
 * @Class LogoutListener
 * @since 2020. 7. 23.
 * @author 류중규
 * @Description : spring security 세션타임 아웃 리스너
 */
@Component
public class LogoutListener implements ApplicationListener<SessionDestroyedEvent> {

    @Override
	public void onApplicationEvent(SessionDestroyedEvent event) {
        List<SecurityContext> lstSecurityContext = event.getSecurityContexts();

		for (SecurityContext securityContext : lstSecurityContext) {

			// User cu = (User) securityContext.getAuthentication().getPrincipal();

        }
    }

}