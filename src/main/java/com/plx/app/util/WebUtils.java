package com.plx.app.util;

import java.io.File;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WebUtils {

	protected Log logger = LogFactory.getLog(getClass());

	/**
	 * @Method clearXSSMinimum
	 * @since  2019. 9. 24.
	 * @author 류중규
	 * @return String
	 * @param value
	 * @return
	 * @description XSS 방지
	 */
	public static String clearXSSMinimum(String value) {
		if (value == null || value.trim().equals("")) {
			return "";
		}

		String returnValue = value;

		returnValue = returnValue.replaceAll("&", "&amp;");
		returnValue = returnValue.replaceAll("<", "&lt;");
		returnValue = returnValue.replaceAll(">", "&gt;");
		returnValue = returnValue.replaceAll("\"", "&#34;");
		returnValue = returnValue.replaceAll("\'", "&#39;");
		returnValue = returnValue.replaceAll("\\.", "&#46;");
		returnValue = returnValue.replaceAll("%2E", "&#46;");
		returnValue = returnValue.replaceAll("%2F", "&#47;");
		return returnValue;
	}

	/**
	 * @Method clearXSSMaximum
	 * @since  2019. 9. 24.
	 * @author 류중규
	 * @return String
	 * @param value
	 * @return
	 * @description XSS 방지
	 */
	public static String clearXSSMaximum(String value) {
		String returnValue = value;
		returnValue = clearXSSMinimum(returnValue);

		returnValue = returnValue.replaceAll("%00", null);

		returnValue = returnValue.replaceAll("%", "&#37;");

		// \\. => .

		returnValue = returnValue.replaceAll("\\.\\./", ""); // ../
		returnValue = returnValue.replaceAll("\\.\\.\\\\", ""); // ..\
		returnValue = returnValue.replaceAll("\\./", ""); // ./
		returnValue = returnValue.replaceAll("%2F", "");

		return returnValue;
	}

	/**
	 * @Method filePathBlackList
	 * @since  2019. 9. 24.
	 * @author 류중규
	 * @return String
	 * @param value
	 * @return
	 * @description 파일 경로 보안 체크
	 */
	public static String filePathBlackList(String value) {
		String returnValue = value;
		if (returnValue == null || returnValue.trim().equals("")) {
			return "";
		}

		returnValue = returnValue.replaceAll("\\.\\./", ""); // ../
		returnValue = returnValue.replaceAll("\\.\\.\\\\", ""); // ..\

		return returnValue;
	}

	/**
	 * 행안부 보안취약점 점검 조치 방안.
	 *
	 * @param value
	 * @return
	 */
	public static String filePathReplaceAll(String value) {
		String returnValue = value;
		if (returnValue == null || returnValue.trim().equals("")) {
			return "";
		}

		returnValue = returnValue.replaceAll("/", "");
		returnValue = returnValue.replaceAll("\\\\", "");
		returnValue = returnValue.replaceAll("\\.\\.", ""); // ..
		returnValue = returnValue.replaceAll("&", "");

		return returnValue;
	}

	public static String filePathWhiteList(String value) {
		return value;
	}

	 public static boolean isIPAddress(String str) {
		Pattern ipPattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");

		return ipPattern.matcher(str).matches();
    }

	 public static String removeCRLF(String parameter) {
		 return parameter.replaceAll("\r", "").replaceAll("\n", "");
	 }

	 public static String removeSQLInjectionRisk(String parameter) {
		 return parameter.replaceAll("\\p{Space}", "").replaceAll("\\*", "").replaceAll("%", "").replaceAll(";", "").replaceAll("-", "").replaceAll("\\+", "").replaceAll(",", "");
	 }

	 public static String removeOSCmdRisk(String parameter) {
		 return parameter.replaceAll("\\p{Space}", "").replaceAll("\\*", "").replaceAll("\\|", "").replaceAll(";", "");
	 }

	 /**
	 * @Method deleteDir
	 * @since  2019. 9. 24.
	 * @author 류중규
	 * @return void
	 * @param dirPath
	 * @description 하위 디렉토리까지 삭제
	 */
	public static void deleteDir(String dirPath) {
		 File dir = new File(dirPath);
		 if(dir.exists()) {
			 File[] fileList = dir.listFiles();
			 if(fileList != null) {
				 if(fileList.length > 0) {
					for (int i=0; i<fileList.length; i++) {
						if(fileList[i].isFile()) {
							if(fileList[i].delete()) {
								System.out.println("파일 삭제:" + fileList[i].getName());
							} else {
								System.out.println("파일 삭제 실패");
							}
						} else {
							// 재귀 처리
							deleteDir(fileList[i].getPath());
						}
					}
				 }
			 }
			 if(dir.delete()) {
				System.out.println("디렉토리 삭제:" + dir.getName());
			 } else {
				System.out.println("디렉토리 삭제 실패");
			}
		 }
	 }

	/**
	 * @Method isMobile
	 * @since  2020. 1. 8.
	 * @author 류중규
	 * @return boolean
	 * @param request
	 * @return
	 * @description 모바일체크
	 */
	public static boolean isMobile(HttpServletRequest request) {
		String userAgent = request.getHeader("user-agent");
		boolean mobile1 = userAgent.matches(".*(iPhone|iPad|Android|Windows CE|BlackBerry|Symbian|Windows Phone|webOS|Opera Mini|Opera Mobi|POLARIS|IEMobile|lgtelecom|nokia|SonyEricsson).*");
		boolean mobile2 = userAgent.matches(".*(LG|SAMSUNG|Samsung).*");

		if(mobile1 || mobile2) {
			return true;
		}

		return false;
	}

	/**
	 * @Method getClientIp
	 * @since  2020. 7. 19.
	 * @author 류중규
	 * @return String
	 * @param request
	 * @return
	 * @description 클라이언트 IP
	 */
	public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
             ip = request.getHeader("Proxy-Client-IP");
         }
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
             ip = request.getHeader("WL-Proxy-Client-IP");
         }
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
             ip = request.getHeader("HTTP_CLIENT_IP");
         }
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
             ip = request.getHeader("HTTP_X_FORWARDED_FOR");
         }
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
             ip = request.getRemoteAddr();
         }
         return ip;
    }

}
