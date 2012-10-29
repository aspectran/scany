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


import org.jhlabs.scany.engine.index.RecordKeyException;
import org.jhlabs.scany.util.StringUtils;

/**
 * PrimaryKey 정보를 담고 있다.
 * 
 * @author Gulendol
 *
 */
public class RecordKeyPattern {
	
	private static final String RECORD_KEY = "_r_key_";
	
	private String pattern;
	
	private String separator;
	
	private String[] keyNames;

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern, String separator) {
		this.pattern = pattern;
		this.separator = separator;
		
		if(separator != null && separator.length() > 0) {
			keyNames = StringUtils.split(pattern, separator);
		}
	}

	public String getSeparator() {
		return separator;
	}
	
	public String[] getKeyNames() {
		return keyNames;
	}
	
	public boolean isJoinRecordKey() {
		return (keyNames != null && keyNames.length > 1);
	}
	
	public String getKeyName() {
		if(keyNames == null || keyNames.length == 0)
			return null;
		
		if(keyNames.length > 1)
			return RECORD_KEY;
		else
			return keyNames[0];
	}
	
	public boolean isKeyMember(String attributeName) {
		boolean exists = false;
		
		for(String keyName : keyNames) {
			if(keyName.equals(attributeName)) {
				exists = true;
				break;
			}
		}
		
		return exists;
	}

	/**
	 * 각 요소(Key)를 키패턴(KeyPattern)에 맞게 조합하여 PrimaryKey를 생성한다.
	 * @param keyPattern
	 * @return
	 */
	public String combine(Record record) throws RecordKeyException {
		String keyName = getKeyName();
		
		if(keyName == null)
			throw new IllegalArgumentException("레코드키가 정의되어 있지 않습니다.");
		
		if(keyNames.length == 1) {
			String keyValue = record.getValue(keyName);
			
			if(keyValue == null)
				throw new RecordKeyException("record key value is null.");

			return record.getValue(keyName);
		}
		
		String[] keyValues = new String[keyNames.length];

		for(int i = 0; i < keyNames.length; i++) {
			keyValues[i] = record.getValue(keyNames[i]);
			
			if(keyValues[i] == null || keyValues[i].length() < 1)
				throw new IllegalArgumentException("부적합한 Record Key입니다.");
		}
			
		// 키조합
		StringBuffer sb = new StringBuffer();
		
		for(int i = 0; i < keyValues.length; i++) {
			sb.append(keyValues[i]);
			
			if(i < keyValues.length - 1)
				sb.append(separator);
		}
		
		return sb.toString();
	}
}
