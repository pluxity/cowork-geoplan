package com.plx.app.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.plx.app.constant.CmnConst;
import com.plx.app.util.WebUtils;

public class LogCheckInterceptor extends HandlerInterceptorAdapter {

	protected Log logger = LogFactory.getLog(getClass());
	protected Log logger_info = LogFactory.getLog("INFO_LOG");
	protected Log logger_error = LogFactory.getLog("ERROR_LOG");

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (request.getMethod().equals("OPTIONS")) {
			return true;
		}

		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		if(!isChkURI(CmnConst.NOT_LOG_URI, request) ) {
			// 사용자정보세션
			String usrId = SecurityContextHolder.getContext().getAuthentication().getName();

			String target = ((HandlerMethod)handler).getBean().getClass().getSimpleName();// className
			String packageName = ((HandlerMethod)handler).getBean().getClass().getPackage().getName();
			String sigName = ((HandlerMethod)handler).getMethod().getName();// method Name

			//StopWatch stopWatch = (StopWatch) request.getAttribute(CmnConst.LOG_STOP_WATCH_ATTRIBUTE_NAME);
			//stopWatch.stop();
			String strUrl = request.getRequestURI().toString() + ((request.getQueryString() != null)? "?" + request.getQueryString():"");

			String layerType = CmnConst.LAYER_CONTROLLER;
			String className = packageName + "." + target + "." + sigName;
			String remoteAddr = WebUtils.getClientIp(request);
			// 요청 파라미터 저장
			Enumeration<?> enu = request.getParameterNames();
			String paramName = "";
			String paramVal = "";
			String strParams = "";
			while (enu.hasMoreElements()) {
				paramName = (String) enu.nextElement();
				paramVal = request.getParameter(paramName);
				strParams += paramName + ":" + paramVal + ",";
			}

			StringBuffer sb = new StringBuffer();
			sb.append("[").append("LAYER").append("=").append(layerType);
			sb.append("|").append("IP").append("=").append(remoteAddr);
			sb.append("|").append("USER_ID").append("=").append(usrId);
			sb.append("|").append("URL").append("=").append(strUrl);
			sb.append("|").append("PARAMS").append("=").append(strParams);
			sb.append("|").append("CLASS_NAME").append("=").append(className);
			//sb.append("|").append("ELAPSED_TIME").append("=").append(stopWatch.toString());
			sb.append("|").append("S/F").append("=").append(CmnConst.LOG_FAIL);
			sb.append("|").append("ERROR_MSG").append("=").append((ex == null ? "" : ex.getMessage()));
			sb.append("]");
			logger_info.info(sb.toString());
		}
		super.afterCompletion(request, response, handler, ex);
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
