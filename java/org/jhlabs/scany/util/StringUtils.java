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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 문자열을 다루는 메쏘드를 모아 놓은 클래스입니다.
 * @author Jeong Ju Ho (http://labs.tistory.com)
 */
public class StringUtils {

	/**
	 * 빈 문자열("")
	 */
	public static final String EMPTY = "";

	/**
	 * null 문자열이면 "" 문자열을 돌려준다.
	 * ""문자열 이외의 문자열 바꾸려면 decode 함수 참조
	 * @param string 대상 문자열
	 * @return String
	 */
	public static String nullCheck(String string) {
		return string == null ? EMPTY : string;
	}

	/**
	 * 주어진 문자열의 값이 null 또는 ""인지를 반환한다. 
	 * @param string
	 * @return
	 */
	public static boolean isEmpty(String string) {
		return (string == null || string.length() == 0);
	}

	/**
	 * 발견한 모든 검색 문자열을 치환 문자열로 교체한다.
	 * @param string 대상 문자열
	 * @param search 발견 문자열
	 * @param replace 치환 문자열
	 * @return String
	 */
	public static String replace(String string, String search, String replace) {
		if(string == null || search == null || replace == null)
			return string;

		StringBuffer sb = new StringBuffer();

		int searchLen = search.length();
		int stringLen = string.length();
		int index = 0;
		int oldIndex = 0;

		while((index = string.indexOf(search, oldIndex)) >= 0) {
			sb.append(string.substring(oldIndex, index));
			sb.append(replace);
			oldIndex = index + searchLen;
		}

		if(oldIndex < stringLen)
			sb.append(string.substring(oldIndex, stringLen));

		return sb.toString();
	}

	/**
	 * 발견한 모든 검색 문자열을 치환 문자열로 교체합니다.
	 * 발견 문장 배열과 치환 문자열 배열은 서로 쌍을 이루어야 합니다.
	 * @param string 대상 문자열
	 * @param search 발견 문자열 배열
	 * @param replace 치환 문자열 배열
	 * @return String
	 */
	public static String replace(String string, String[] search, String[] replace) {
		if(string == null || search == null || replace == null)
			return string;

		StringBuffer sb = new StringBuffer(string);

		int loop = (search.length <= replace.length) ? search.length : replace.length;
		int start = 0;
		int end = 0;
		int searchLen = 0;
		int replaceLen = 0;

		for(int i = 0; i < loop; i++) {
			if(search[i] == null || replace[i] == null)
				continue;

			searchLen = search[i].length();
			replaceLen = replace[i].length();

			while(true) {
				if(sb.length() == 0)
					break;

				start = sb.indexOf(search[i], start + replaceLen);

				if(start == -1)
					break;

				end = start + searchLen;

				sb.replace(start, end, replace[i]);
			}
		}

		return sb.toString();
	}

	/**
	 * 대상문자열(str)에서 구분문자열(delim)을 기준으로 문자열을 분리하여
	 * 각 분리된 문자열을 배열에 할당하여 반환한다.
	 * @param string 분리 대상 문자열
	 * @param delim 구분 문자열
	 * @return 분리된 문자열을 순서대로 배열에 격납하여 반환한다.
	 */
	public static String[] split(String string, String delim) {
		if(isEmpty(string))
			return new String[0];

		int cnt = search(string, delim);
		String[] item = new String[cnt + 1];

		if(cnt == 0) {
			item[0] = string;
			return item;
		}

		int idx = 0;
		int pos1 = 0;
		int pos2 = string.indexOf(delim);
		int delimLen = delim.length();

		while(pos2 >= 0) {
			item[idx++] = (pos1 > pos2 - 1) ? EMPTY : string.substring(pos1, pos2);

			pos1 = pos2 + delimLen;
			pos2 = string.indexOf(delim, pos1);
		}

		if(pos1 < string.length())
			item[idx] = string.substring(pos1);

		if(item[cnt] == null)
			item[cnt] = EMPTY;

		return item;
	}

	/**
	 * 대상 문자열(str)에서 구분 문자열(delim)을 기준으로 문자열을 분리하여
	 * 각 분리된 문자열을 배열에 할당하여 반환한다.
	 * size를 지정하면 ""문자열이  나머지 문자열 전체를 가지는 최대 size개 원소의 배열을 반환합니다.
	 * @param string 분리 대상 문자열
	 * @param delim 구분 문자열
	 * @param size 결과 배열의 크기
	 * @return 분리된 문자열을 순서대로 배열에 격납하여 반환한다.
	 */
	public static String[] split(String string, String delim, int size) {
		String[] arr1 = new String[size];
		String[] arr2 = split(string, delim);

		for(int i = 0; i < arr1.length; i++) {
			if(i < arr2.length)
				arr1[i] = arr2[i];
			else
				arr1[i] = EMPTY;
		}

		return arr1;
	}

	/**
	 * 대상 문자열(str)에서 구분 문자열(delim)을 기준으로 문자열을 분리 후
	 * 리스트(List) 객체를 반환한다.
	 * @param string 분리 대상 문자열
	 * @param delim 구분 문자열
	 * @param list List
	 * @return List
	 */
	public static List split(String string, String delim, List list) {
		int currPos = 0;
		int nextPos = 0;

		int strLen = string.length();
		int delimLen = delim.length();

		while((nextPos = string.indexOf(delim, currPos)) >= 0) {
			if(currPos == nextPos)
				list.add(EMPTY);
			else
				list.add(string.substring(currPos, nextPos));

			currPos = nextPos + delimLen;
		}

		if(currPos < strLen)
			list.add(string.substring(currPos));
		else if(currPos >= strLen)
			list.add(EMPTY);

		return list;
	}

	/**
	 * 대상문자열(str)에서 지정문자열(keyw)이 검색된 횟수를,
	 * 지정문자열이 없으면 0 을 반환한다.
	 * @param string 대상문자열
	 * @param keyw 검색할 문자열
	 * @return 지정문자열이 검색되었으면 검색된 횟수를, 검색되지 않았으면 0 을 반환한다.
	 */
	public static int search(String string, String keyw) {
		int strLen = string.length();
		int keywLen = keyw.length();
		int pos = 0;
		int cnt = 0;

		if(keywLen == 0)
			return 0;

		while((pos = string.indexOf(keyw, pos)) != -1) {
			pos += keywLen;
			cnt++;

			if(pos >= strLen)
				break;
		}

		return cnt;
	}

	/**
	 * 대상문자열(str)에서 대소문자 구분없이 지정문자열(keyw)이 검색된 횟수를,
	 * 지정문자열이 없으면 0 을 반환한다.
	 * @param string 대상문자열
	 * @param keyw 검색할 문자열
	 * @return 지정문자열이 검색되었으면 검색된 횟수를, 검색되지 않았으면 0 을 반환한다.
	 */
	public static int searchIgnoreCase(String string, String keyw) {
		return search(string.toLowerCase(), keyw.toLowerCase());
	}

	/**
	 * 주어진 <code>delimiters</code>에 의해 분리된 문자열 배열을 반환한다.
	 * @param string
	 * @param delimiters
	 * @return
	 */
	public static String[] tokenize(String string, String delimiters) {
		if(string == null)
			return new String[0];

		StringTokenizer st = new StringTokenizer(string, delimiters);
		List tokens = new ArrayList();

		while(st.hasMoreTokens()) {
			tokens.add((String)st.nextToken());
		}

		return (String[])tokens.toArray(new String[tokens.size()]);
	}
	
    /**
	 * 문자열의 오른쪽에 지정한 문자를 채운다.
	 * @param string 문자열
	 * @param len 문자열 길이
	 * @param pad 채울 문자
	 * @return String
	 */
	public static String rightPad(String string, int len, char pad) {
		StringBuffer sb = new StringBuffer(string);
		sb.substring(0, len);

		for(int i = sb.length(); i < len; i++) {
			sb.append(pad);
		}

		return sb.toString();
	}
	
    /**
	 * 문자열의 길이를 바이트 단위로 계산하여 반환한다.
	 * null 또는 빈문자열이거나, UnsupportedEncodingException이 발생하면 0을 반환한다.
	 * @param string 문자열
	 * @return 문자열 길이
	 */
	public static int byteLength(String string) {
		if(isEmpty(string))
			return 0;
		
		try {
			return new String(string.getBytes(), "8859_1").length();
		} catch(UnsupportedEncodingException e) {
			return 0;
		}
	}
}
