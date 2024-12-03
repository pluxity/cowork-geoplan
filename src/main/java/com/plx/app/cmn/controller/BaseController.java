package com.plx.app.cmn.controller;

import java.lang.reflect.InvocationTargetException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.plx.app.cmn.vo.LoginInfoVO;

@Controller
public class BaseController {

	protected Log logger = LogFactory.getLog(getClass());
	protected Log logger_info = LogFactory.getLog("INFO_LOG");
	protected Log logger_error = LogFactory.getLog("ERROR_LOG");

	/**
	 * profile 설정값
	 */
	@Value("${spring.profiles.active}")
	protected String activeProfile;

    /**
     * properties 파일 처리
     */
    @Resource(name="messageSourceAccessor")
    protected MessageSourceAccessor messageSourceAccessor;

    @Autowired
	protected HttpServletRequest request;

    protected HttpSession session(){
    	ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true);
    }

    protected LoginInfoVO sessionLoginInfo(){
    	return (LoginInfoVO) session().getAttribute(LoginInfoVO.LOGIN_INFO);
	}

    protected void removeSession(){
	    session().removeAttribute(LoginInfoVO.LOGIN_INFO);
	    session().invalidate();
    }

    protected void setSession(String key, Object value){
    	session().setAttribute(key, value);
    }

    protected Object getSession(String key){
    	return session().getAttribute(key);
    }

    // 세션 아이디 VO 자동 매핑
    protected void importLoginSession(Object obj) throws IllegalAccessException, InvocationTargetException{
    	if(sessionLoginInfo()!= null){
    		BeanUtils.copyProperty(obj, "regUsr", sessionLoginInfo().getUsrId());
        	BeanUtils.copyProperty(obj, "updUsr", sessionLoginInfo().getUsrId());
    	}
	}

}
