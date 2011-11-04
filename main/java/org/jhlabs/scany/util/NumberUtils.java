/*******************************************************************************
 * Copyright (c) 2008 Jeong Ju Ho.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Jeong Ju Ho - initial API and implementation
 ******************************************************************************/
package org.jhlabs.scany.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 숫자을 다루는 메쏘드를 모아 놓은 클래스입니다.
 * @author Jeong Ju Ho (http://labs.tistory.com)
 * @version <pre>
 * 2008/02/09 Stringz에서 분리
 * 2003/04/26 초기 버전</pre>
 */
public class NumberUtils {
 
	private static final String PLUS = "+";
	
	private static final String COMMA = ",";
	
	private static final String DOT = ".";
	
	/**
	 * 수치 형태의 문자열을 int형으로 변환한다.
	 * 변환 실패시 기본 값(defval)를 돌려준다.
	 * 변환 전에 '+', ',' 문자는 제거한다. '-' 문자는 제거하지 않으므로 음수로 변환이 가능하다.
	 * @param string 문자열
	 * @param defval 기본 값
	 * @return int
	 */
	public static int parseInt(String string, int defval) {
		if(string == null)
			return defval;
		
		String temp = string.trim();

		if(StringUtils.search(temp, PLUS) > 0)
			temp = StringUtils.replace(temp, PLUS, StringUtils.EMPTY);
		
		if(StringUtils.search(temp, COMMA) > 0)
			temp = StringUtils.replace(temp, COMMA, StringUtils.EMPTY);

		try {
			return Integer.parseInt(temp);
		} catch(Exception e) {
			return defval;
		}
	}

    /**
	 * 수치 형태의 문자열을 long형으로 변환한다.
	 * 변환 실패시 기본 값(defval)를 돌려준다.
	 * 변환 전에 '+', ',' 문자는 제거한다. '-' 문자는 제거하지 않으므로 음수로 변환이 가능하다.
	 * @param string 문자열
	 * @param defval 기본 값
	 * @return long
	 */
	public static long parseLong(String string, long defval) {
		if(string == null)
			return defval;
		
		String temp = string.trim();

		if(StringUtils.search(temp, PLUS) > 0)
			temp = StringUtils.replace(temp, PLUS, StringUtils.EMPTY);
		
		if(StringUtils.search(temp, COMMA) > 0)
			temp = StringUtils.replace(temp, COMMA, StringUtils.EMPTY);
		
		try {
			return Long.parseLong(temp);
		} catch(Exception e) {
			return defval;
		}
	}

    /**
	 * 수치 형태의 문자열을 float형으로 변환한다.
	 * 변환 실패시 기본 값(defval)를 돌려준다.
	 * 변환 전에 '+', ',' 문자는 제거한다. '-' 문자는 제거하지 않으므로 음수로 변환이 가능하다.
	 * @param string 문자열
	 * @param defval 기본 값
	 * @return float
	 */
	public static float parseFloat(String string, float defval) {
		if(string == null)
			return defval;
		
		String temp = string.trim();

		if(StringUtils.search(temp, PLUS) > 0)
			temp = StringUtils.replace(temp, PLUS, StringUtils.EMPTY);
		
		if(StringUtils.search(temp, COMMA) > 0)
			temp = StringUtils.replace(temp, COMMA, StringUtils.EMPTY);
		
		try {
			return Float.parseFloat(temp);
		} catch(Exception e) {
			return defval;
		}
	}

    /**
	 * 숫자에 콤마(,)를 붙여서 반환한다.
	 * 숫자변환에 실패하면 원래 문자열(str)을 반환한다.
	 * @param number 대상 숫자
	 * @return String
	 */
	public static String formatNumber(String number) {
		NumberFormat nf = NumberFormat.getNumberInstance();

		try {
			if(StringUtils.search(number, DOT) > 0)
				return nf.format((new Double(number)).doubleValue());
			else
				return nf.format(longValue(number));
		} catch(Exception e) {
		}
		
		return number;
	}

    /**
	 * 숫자에 콤마(,)를 붙여서 반환한다.
	 * @param number 대상 숫자
	 * @return String
	 */
	public static String formatNumber(int number) {
		return formatNumber((new Integer(number)).longValue());
	}

    /**
	 * 숫자에 콤마(,)를 붙여서 반환한다.
	 * @param number 대상 숫자
	 * @return String
	 */
	public static String formatNumber(long number) {
		NumberFormat nf = NumberFormat.getNumberInstance();
		return nf.format(number);
	}

    /**
	 * 숫자에 콤마(,)를 붙여서 반환한다.
	 * @param number 대상 숫자
	 * @return String
	 */
	public static String formatNumber(double number) {
		NumberFormat nf = NumberFormat.getNumberInstance();
		return nf.format(number);
	}

    /**
	 * 숫자에 콤마(,)를 붙여서 반환한다.
	 * @param number 대상 숫자
	 * @param decimalFigures 소수점 이하 자리수
	 * @return String
	 */
	public static String formatNumber(double number, int decimalFigures) {
		if(decimalFigures > 0) {
			String pattern = StringUtils.rightPad("0.", decimalFigures + 2, '0');
		
			DecimalFormat df = new DecimalFormat(pattern);
			
			return df.format(number);
		} else {
			return formatNumber(number);
		}
	}
	
	/**
	 * 문자열을 int 형으로 변환한다.
	 * 예외 발생 예상시 parseInt 권장
	 * parseInt(str, 0) 과 동일하다.
	 * @param str 대상 문자열
	 * @return int
	 */
	public static int intValue(String string) {
		return (new Integer(string)).intValue();
	}

    /**
	 * 문자열을 long 형으로 변환한다.
	 * 예외 발생 예상시 parseLong 권장
	 * @param string 대상 문자열
	 * @return long
	 */
	public static long longValue(String string) {
		return (new Long(string)).longValue();
	}

}
