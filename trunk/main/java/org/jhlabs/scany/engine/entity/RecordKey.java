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
package org.jhlabs.scany.engine.entity;


import org.jhlabs.scany.context.builder.ScanyContextBuilder;
import org.jhlabs.scany.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * PrimaryKey 정보를 담고 있다.
 * 
 * @author Gulendol
 *
 */
public class RecordKey {
	
	public static final String PRIMARY_KEY = "_p_key_";
	
	private Map keys = new HashMap();
	
	private boolean wildcard;
	
	private String keyPattern;

	/**
	 * 생성자.
	 * 주어진 인자를 이용해 디코딩
	 * @param primaryKey 인코딩된 키
	 * @param keyPattern 키형식
	 * @throws MultipartRequestzException
	 */
	public RecordKey(String primaryKey, Relation schema) throws RecordKeyException {
		this(schema);
		
		decode(primaryKey);
	}
	
	/**
	 * 생성자.
	 * 주어진 인자를 이용해 디코딩
	 * @param keyPattern 키형식
	 * @throws MultipartRequestzException
	 */
	public RecordKey(Relation schema) {
		this.keyPattern = schema.getKeyPattern();
	}
	
	/**
	 * PrimaryKey의 한 요소(Key)의 값을 반한다.
	 * @param keyName
	 * @return
	 */
	public String getKeyValue(String keyName) {
		return (String)keys.get(keyName);
	}

	/**
	 * PrimaryKey에 한 요소(Key) 값을 추가한다.
	 * <pre>
	 * 다음과 같은 문자는 특수문자로 분류되어 있으므로 키값으로 절대 입력하면 안된다.
	 *     + - && || ! ( ) { } [ ] ^ " ~ * ? : \
	 * </pre>
	 * @param keyName
	 * @param keyValue
	 */
	public void setKeyValue(String keyName, Object keyValue) {
		setKeyValue(keyName, keyValue.toString());
	}
	
	/**
	 * PrimaryKey에 한 요소(Key) 값을 추가한다.
	 * <pre>
	 * 다음과 같은 문자는 특수문자로 분류되어 있으므로 키값으로 절대 입력하면 안된다.
	 *     + - && || ! ( ) { } [ ] ^ " ~ * ? : \
	 * </pre>
	 * @param keyName
	 * @param keyValue 문자열
	 */
	public void setKeyValue(String keyName, String keyValue) {
		keyName = keyName.trim();
		keyValue = keyValue.trim();
		
		if(!wildcard) {
			for(int i = 0; i < ScanyContextBuilder.WILDCARDS.length; i++) {
				if(keyValue.indexOf(ScanyContextBuilder.WILDCARDS[i]) != -1) {
					wildcard = true;
					break;
				}
					
			}
		}
		
		keys.put(keyName, keyValue);
	}
	
	/**
	 * PrimaryKey에 한 요소(Key)를 추가한다.<pre>
	 * 아래와 같은 문자는 특수문자로 분류되어 있으므로 키값으로 절대 입력하면 안된다.
	 *     + - && || ! ( ) { } [ ] ^ " ~ * ? : \</pre>
	 * @param keyName
	 * @param keyValue int형, 내부적으로 문자열로 변환된다.
	 */
	public void setKeyValue(String keyName, int keyValue) {
		setKeyValue(keyName, (new Integer(keyValue)).toString());
	}
	
	public String encode() throws RecordKeyException {
		return encode(this.keyPattern);
	}
	
	/**
	 * 각 요소(Key)를 키패턴(KeyPattern)에 맞게 조합하여 PrimaryKey를 생성한다.
	 * @param keyPattern
	 * @return
	 * @throws MultipartRequestzException
	 */
	public String encode(String keyPattern) throws RecordKeyException {
		try {
			String[] keyNames = StringUtils.split(keyPattern, ScanyContextBuilder.KEY_DELIMITER);
			String[] keyValues = new String[keyNames.length];
	
			if(keyNames.length == 0)
				throw new IllegalArgumentException("스키마 설정정보에서 KeyPattern이 지정되지 않았습니다.");
			
			for(int i = 0; i < keyNames.length; i++) {
				keyValues[i] = (String)keys.get(keyNames[i]);
				
				if(keyValues[i] == null || keyValues[i].length() < 1)
					throw new IllegalArgumentException("부적합한 Primary Key입니다.");
			}
	
			if(wildcard) {
				for(int i = 0; i < ScanyContextBuilder.WILDCARDS.length; i++) {
					if(ScanyContextBuilder.WILDCARDS[i].equals(keyValues[0].substring(0, 1)))
						throw new IllegalArgumentException("PrimaryKey에서 와일드카드 문자('?', '*')는 첫번째 인자가 될 수 없습니다.");
				}
		
				// groupId:*:* == > groupId:*
				for(int i = keyValues.length - 1; i >= 0; i--) {
					if(!"*".equals(keyValues[i]))
						break;
		
					keyValues[i] = null;
				}
			}
			
			// 키조합
			StringBuffer sb = new StringBuffer();
			
			for(int i = 0; i < keyValues.length; i++) {
				if(keyValues[i] == null) {
					sb.append("*");
					break;
				}
				
				sb.append(keyValues[i]);
				
				if(i < keyValues.length - 1)
					sb.append(ScanyContextBuilder.KEY_DELIMITER);
			}
			
			return sb.toString();

		} catch(Exception e) {
			throw new RecordKeyException("PrimaryKey를 인코딩하는 중 오류가 발생했습니다.", e);
		}
	}

	/**
	 * PrimaryKey를 각 요소(Key)로 분리한다.
	 * keyPattern 지정을 별도로 할 수 있다.
	 * @param primaryKey
	 * @param keyPattern
	 * @throws MultipartRequestzException
	 */
	public void decode(String primaryKey, String keyPattern) throws RecordKeyException {
		this.keyPattern = keyPattern;
		decode(primaryKey);
	}
	
	/**
	 * PrimaryKey를 각 요소(Key)로 분리한다.
	 * @param primaryKey
	 * @throws MultipartRequestzException
	 */
	public void decode(String primaryKey) throws RecordKeyException {
		try {
			String[] keyNames = StringUtils.split(keyPattern, ScanyContextBuilder.KEY_DELIMITER);
			String[] keyValues = StringUtils.split(primaryKey, ScanyContextBuilder.KEY_DELIMITER);
	
			if(keyNames.length != keyValues.length)
				throw new IllegalArgumentException("부적합한 Primary Key입니다.");
			
			if(keys.size() != 0)
				keys.clear();
	
			for(int i = 0; i < keyNames.length; i++) {
				setKeyValue(keyNames[i], keyValues[i]);
			}
		} catch(Exception e) {
			throw new RecordKeyException("PrimaryKey를 디코딩하는 중 오류가 발생했습니다.", e);
		}
	}
	
	/**
	 * 와일드카드 문자를 하나 이상 포함하고 있는지 여부 
	 * @return
	 */
	public boolean hasWildcard() {
		return wildcard;
	}
}
