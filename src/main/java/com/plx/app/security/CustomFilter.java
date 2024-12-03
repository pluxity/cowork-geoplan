package com.plx.app.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

/**
 * @Project KNIS
 * @Class CustomLoginSuccessHandler
 * @since 2021. 02. 01.
 * @author 유경식
 * @Description : spring security 커스텀필터 - 특정 키 밸류 값 파라미터에 있다면 강제 로그인처리
 */

@Component
public class CustomFilter extends GenericFilterBean {

    // private String loginKey = "test";
    // private String loginValue = "abcd";
    // private String loginId = "admin";
    // private String loginPw = "pluxity123!@#";


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
//        new SecurityUtils().customLogin(request, response);
        chain.doFilter(request, response);
    }
}
