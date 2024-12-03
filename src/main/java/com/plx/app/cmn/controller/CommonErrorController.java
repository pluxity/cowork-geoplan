package com.plx.app.cmn.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 *  @Project KNIS
 *  @Class CommonErrorController
 *  @since 2019. 12. 16.
 *  @author 류중규
 *  @Description : 에러 처리 컨트롤러
 */
@Controller
@RequestMapping("/error")
public class CommonErrorController extends BaseController {

	@RequestMapping(value = "/throwable")
	public String throwable(HttpServletRequest request, HttpServletResponse response, Model model) {
		String msg = messageSourceAccessor.getMessage("fail.msg");
		logger.info("Error Throwable");
		pageErrorLog(request);
		model.addAttribute("msg", msg);
		response.setStatus(200); // 보안 점검으로 200 으로 고정

		return "cmn/error";
	}

	@RequestMapping(value = "/exception")
	public String exception(HttpServletRequest request, HttpServletResponse response, Model model) {
		String msg = messageSourceAccessor.getMessage("fail.msg");
		logger.info("Error Exception");
		pageErrorLog(request);
		model.addAttribute("msg", msg);
		response.setStatus(200); // 보안 점검으로 200 으로 고정

		return "cmn/error";
	}

	@RequestMapping(value = "/400")
	public String pageError400(HttpServletRequest request, HttpServletResponse response, Model model) {
		String msg = messageSourceAccessor.getMessage("fail.msg");
		logger.info("Error 400");
		pageErrorLog(request);
		model.addAttribute("msg", msg);
		response.setStatus(200); // 보안 점검으로 200 으로 고정

		return "cmn/error";
	}

	@RequestMapping(value = "/403")
	public String pageError403(HttpServletRequest request, HttpServletResponse response, Model model) {
		String msg = messageSourceAccessor.getMessage("fail.msg");
		logger.info("Error 403");
		pageErrorLog(request);
		model.addAttribute("msg", msg);
		response.setStatus(200); // 보안 점검으로 200 으로 고정

		return "cmn/error";
	}

	@RequestMapping(value = "/404")
	public String pageError404(HttpServletRequest request, HttpServletResponse response, Model model) {
		String msg = messageSourceAccessor.getMessage("fail.msg");
		pageErrorLog(request);
		logger.info("Error 404");
		model.addAttribute("msg", msg);
		response.setStatus(200); // 보안 점검으로 200 으로 고정

		return "cmn/error";
	}

	@RequestMapping(value = "/500")
	public String pageError500(HttpServletRequest request, HttpServletResponse response, Model model) {
		String msg = messageSourceAccessor.getMessage("fail.msg");
		logger.info("Error 500");
		pageErrorLog(request);
		model.addAttribute("msg", msg);
		response.setStatus(200); // 보안 점검으로 200 으로 고정

		return "cmn/error";
	}

	@RequestMapping(value = "/auth")
	public String urlAuth(HttpServletRequest request, Model model) {
		logger.info("url auth check");
		//pageErrorLog(request);
		String msg = messageSourceAccessor.getMessage("access.no.auth");
		model.addAttribute("msg", msg);

		return "cmn/error";
	}

	private void pageErrorLog(HttpServletRequest request) {
		logger.info("status_code : " + request.getAttribute("javax.servlet.error.status_code"));
		logger.info("exception_type : " + request.getAttribute("javax.servlet.error.exception_type"));
		logger.info("message : " + request.getAttribute("javax.servlet.error.message"));
		logger.info("request_uri : " + request.getAttribute("javax.servlet.error.request_uri"));
		logger.info("exception : " + request.getAttribute("javax.servlet.error.exception"));
		logger.info("servlet_name : " + request.getAttribute("javax.servlet.error.servlet_name"));
	}

}
