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
package org.jhlabs.scany.engine.search.query;

import org.jhlabs.scany.engine.entity.Attribute;
import org.jhlabs.scany.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 사용자가 입력한 질의 문장을 분석하여 검색단어와 질의 연산자를 구분하는 역할을 한다.
 * 질의에 방해되는 특수문자를 제거하고, 검색단어와 질의 연산자를 구분하여
 * 정화된 질의문을 반환한다.
 * 
 * // 특수문자: + - && || ! ( ) { } [ ] ^ " ~ * ? :
 * // Lucene 특수문자
 * "*", "~",
 * "+", "-",
 * ":", "!",
 * "^", "?",
 * "(", ")", 
 * "{", "}",
 * "[", "]",
 * "\"", "'",
 * "\\",
 * // 추가
 * "<", ">",
 * "`", "=",
 * 
 * <p>Created: 2007. 01. 19 오전 1:40:51</p>
 * 
 * @author Gulendol
 *
 */
public class QueryStringParser {

	protected static final String[] OPERATORS = { "AND", "OR", "NOT" };

	protected static final String[] OPERATOR_IMAGES = { "<AND>", "<OR>", "<NOT>" };

	/**
	 * 걸러질 특수문자들
	 */
	private static final String DELIMITERS = "*~+-:!^?(){}[]\"'\\<>`=\t\n\r\f;@#$%&/,. ";

	private Attribute[] queryAttributes;
	
	private String[] fields;
	
	public QueryStringParser() {
	}
	
	public QueryStringParser(Attribute[] queryAttributes) {
		this.queryAttributes = queryAttributes;
	}
	
	public String parse(String queryString) {
		strainQueryString(queryString);

		if(queryAttributes == null || queryAttributes.length == 0)
			return null;
		
		StringBuffer sb = new StringBuffer();

		for(int i = 0; i < fields.length; i++) {
			if(i > 0)
				sb.append(' ');

			// 연산자일 경우
			int op = -1;
			
			for(int j = 0; j < OPERATORS.length; j++) {
				if(fields[i].equals(OPERATOR_IMAGES[j])) {
					op = j;
					break;
				}
			}
			
			if(op != -1) {
				sb.append(fields[i]);
				continue;
			}
			
			
			if(isOperator(fields[i], false)) {
				fields[i] = '\"' + fields[i] + '\"';
			}

			sb.append('(');

			for(int k = 0; k < queryAttributes.length; k++) {
				if(k > 0) {
					sb.append(" OR ");
				}

				sb.append(queryAttributes[k].getName());
				sb.append(':');
				sb.append(fields[i]);
				
				if(queryAttributes[k].isPrefixQueryable()) {
					if(isPrefixQueryableToken(fields[i]))
						sb.append('*');
				}
			}

			sb.append(')');
		}

		// for Debug
		//System.out.println(sb.toString());

		return sb.toString();
	}
	
	/**
	 * 연산자를 제외한 키워드 배열을 반환한다.
	 * @return
	 */
	public String[] getKeywords() {
		int keywordCount = 0;

		for(int i = 0; i < fields.length; i++) {
			if(whatOperatorImage(fields[i]) == -1)
				keywordCount++;
		}

		String[] keywords = new String[keywordCount];
		
		for(int i = fields.length - 1; i >= 0; i--) {
			if(whatOperatorImage(fields[i]) == -1)
				keywords[--keywordCount] = fields[i];
		}
		
//		for(int i = 0; i < keywords.length; i++) {
//			System.out.println(keywords[i]);
//		}

		return keywords;
	}

	/**
	 * PrefixQuery 를 사용해야 하는지 여부를 토큰을 보고 판단한다.
	 * <pre>
	 * 다음에 해당하는 경우 PrefixQuery가 적용된다.
	 *   1. 한글: 1자일 경우
	 *   2. 영문: 4자 이상인 경우
	 * </pre>
	 * @param token
	 * @return
	 */
	private boolean isPrefixQueryableToken(String token) {
		int byteLen = StringUtils.byteLength(token);

		if(byteLen < 3) {
			// 한글일 경우
			if(byteLen != token.length())
				return true;
		} else if(byteLen > 3) {
			// 영문일 경우
			if(byteLen == token.length())
				return true;
		}
		
		return false;
	}
	
	/**
	 * 주어진 문자열이 Operator인지 여부 반환
	 * 
	 * @param string
	 * @param casesensitive 대소문자 구분여부
	 * @return
	 */
	public static boolean isOperator(String string, boolean casesensitive) {
		for(int i = 0; i < OPERATORS.length; i++) {
			if(casesensitive) {
				if(string.equals(OPERATORS[i])) {
					return true;
				}
			} else {
				if(string.equalsIgnoreCase(OPERATORS[i])) {
					return true;
				}
			}
		}

		return false;
	}
	
	/**
	 * 주어진 문자열이 Operator인지 여부 반환
	 * 
	 * @param operator
	 * @return
	 */
	private int whatOperator(String operator) {
		String upperOperator = operator.toUpperCase();
		
		for(int i = 0; i < OPERATOR_IMAGES.length; i++) {
			if(upperOperator.equals(OPERATOR_IMAGES[i])) {
				return i;
			}
		}

		return -1;
	}
	
	/**
	 * 주어진 문자열이 Operator Image인지 여부 반환
	 * 
	 * @param operatorImage
	 * @return
	 */
	private int whatOperatorImage(String operatorImage) {
		for(int i = 0; i < OPERATOR_IMAGES.length; i++) {
			if(operatorImage.equals(OPERATOR_IMAGES[i])) {
				return i;
			}
		}

		return -1;
	}
	
	/**
	 * 질의문을 분석하여 키워드와 연산자를 구분한다.
	 * @param queryString 질의문
	 */
	private void strainQueryString(String queryString) {
		if(StringUtils.isEmpty(queryString)) {
			fields = new String[0];
			return;
		}

		StringTokenizer st = new StringTokenizer(queryString, DELIMITERS);
		List<String> tokens = new ArrayList<String>();
		
		String token;
		boolean prevOp = true;
		int op = -1;
		
		while(st.hasMoreTokens()) {
			token = st.nextToken();

			op = whatOperator(token);
			
			// 연산자는 대문자로, 소문자로된 연산자는 키워드로 간주할 것이다.
			if(op != -1 && !prevOp) {
				tokens.add(OPERATOR_IMAGES[op]);
				prevOp = true;
			} else {
				tokens.add(token);
				prevOp = false;
			}
		}
		
		fields = (String[])tokens.toArray(new String[tokens.size()]);
	}
	
	public static void main(String[] args) {
		//System.out.println(strainQueryString("가나 And and and 다라 and", false));

	}
}
