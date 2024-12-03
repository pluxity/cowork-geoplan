package com.plx.app.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {
	/**
	 * 한글 2byte 반환, 영문1byte 반환
	 * 
	 * @param str
	 * @return
	 */
	public static final int getByteSizeToComplex(String str) {
		StringUtils.defaultIfEmpty(str, "");

		int en = 0;
		int ko = 0;
		int etc = 0;

		char[] string = str.toCharArray();

		for (int j = 0; j < string.length; j++) {
			if (string[j] >= 'A' && string[j] <= 'z') {
				en++;
			} else if (string[j] >= '\uAC00' && string[j] <= '\uD7A3') {
				ko++;
				ko++;
			} else {
				etc++;
			}
		}

		return (en + ko + etc);
	}

	/**
	 * <PRE>
	 * Comment : 입력값 형식 만들기
	 * </PRE>
	 * 
	 * @return String
	 * @param obj
	 * @return
	 */
	public static String getAgumentNames(Object[] obj) {
		String objNames = "";
		// 입력값 만들기
		for (int i = 0; i < obj.length; i++) {
			if (obj[i] != null) {
				if ((i + 1) == obj.length)
					objNames += obj[i].getClass().getSimpleName();
				else
					objNames += obj[i].getClass().getSimpleName() + ", ";
			}
		}

		return objNames;
	}

	/**
	 * <PRE>
	 * Comment :
	 * </PRE>
	 * 
	 * @return String
	 * @param String
	 * @return
	 */
	public static String convert2CamelCase(String underScore) {

		// '_' 가 나타나지 않으면 이미 camel case 로 가정함.
		// 단 첫째문자가 대문자이면 camel case 변환 (전체를 소문자로) 처리가
		// 필요하다고 가정함. --> 아래 로직을 수행하면 바뀜
		if (underScore.indexOf('_') < 0 && Character.isLowerCase(underScore.charAt(0))) {
			return underScore;
		}
		StringBuilder result = new StringBuilder();
		boolean nextUpper = false;
		int len = underScore.length();

		for (int i = 0; i < len; i++) {
			char currentChar = underScore.charAt(i);
			if (currentChar == '_') {
				nextUpper = true;
			} else {
				if (nextUpper) {
					result.append(Character.toUpperCase(currentChar));
					nextUpper = false;
				} else {
					result.append(Character.toLowerCase(currentChar));
				}
			}
		}
		return result.toString();
	}

	/**
	 * <PRE>
	 * Comment :
	 * </PRE>
	 * 
	 * @return String
	 * @param String
	 * @return
	 */
	public static boolean numberCheck(String str) {
		if (str == null || str.length() == 0)
			return false;

		boolean nFlag = true;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) != '0' && str.charAt(i) != '1' && str.charAt(i) != '2' && str.charAt(i) != '3'
					&& str.charAt(i) != '4' && str.charAt(i) != '5' && str.charAt(i) != '6' && str.charAt(i) != '7'
					&& str.charAt(i) != '8' && str.charAt(i) != '9') {
				nFlag = false;
				break;
			}
		}

		return nFlag;
	}

}
