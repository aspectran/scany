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


import java.util.HashMap;
import java.util.Map;

import org.jhlabs.scany.context.builder.ScanyContextBuilder;

/**
 * PrimaryKey 정보를 담고 있다.
 * 
 * @author Gulendol
 *
 */
public class RecordKey {
	
	public static final String RECORD_KEY = "_r_key_";
	
	private Map<String, String> keyMembers = new HashMap<String, String>();
	
	private boolean wildcard;
	
	private RecordKeyPattern recordKeyPattern;

	public RecordKey(RecordKeyPattern recordKeyPattern) {
		this.recordKeyPattern = recordKeyPattern;
	}

	public void setRecordKey(String recordKey) throws RecordKeyException {
		separate(recordKey);
	}

	public String getKeyValue(String keyName) {
		return keyMembers.get(keyName);
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
		
		keyMembers.put(keyName, keyValue);
	}
	
	public String combine() throws RecordKeyException {
		return recordKeyPattern.combine(this);
	}

	/**
	 * PrimaryKey를 각 요소(Key)로 분리한다.
	 * keyPattern 지정을 별도로 할 수 있다.
	 * @param primaryKey
	 * @param keyPattern
	 * @throws MultipartRequestzException
	 */
	public void separate(String recordKey) throws RecordKeyException {
		recordKeyPattern.separate(recordKey, this);
	}
	
	/**
	 * 와일드카드 문자를 하나 이상 포함하고 있는지 여부 
	 * @return
	 */
	public boolean hasWildcard() {
		return wildcard;
	}
}
