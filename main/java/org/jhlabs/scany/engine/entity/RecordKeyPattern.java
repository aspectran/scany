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

/**
 * PrimaryKey 정보를 담고 있다.
 * 
 * @author Gulendol
 *
 */
public class RecordKeyPattern {
	
	public static final String RECORD_KEY = "_r_key_";
	
	private String pattern;
	
	private String separator;

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}
	
	/**
	 * 각 요소(Key)를 키패턴(KeyPattern)에 맞게 조합하여 PrimaryKey를 생성한다.
	 * @param keyPattern
	 * @return
	 * @throws MultipartRequestzException
	 */
	public String combine(RecordKey recordKey) throws RecordKeyException {
		try {
			String[] keyNames = StringUtils.split(pattern, separator);
			String[] keyValues = new String[keyNames.length];
	
			if(keyNames.length == 0)
				throw new IllegalArgumentException("스키마 설정정보에서 KeyPattern이 지정되지 않았습니다.");
			
			for(int i = 0; i < keyNames.length; i++) {
				keyValues[i] = recordKey.getKeyValue(keyNames[i]);
				
				if(keyValues[i] == null || keyValues[i].length() < 1)
					throw new IllegalArgumentException("부적합한 Record Key입니다.");
			}
	
			if(recordKey.hasWildcard()) {
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
					sb.append(separator);
			}
			
			return sb.toString();

		} catch(Exception e) {
			throw new RecordKeyException("PrimaryKey를 조합하는 중 오류가 발생했습니다.", e);
		}
	}

	/**
	 * PrimaryKey를 각 요소(Key)로 분리한다.
	 * @param primaryKey
	 * @throws MultipartRequestzException
	 */
	public void separate(String recordKeyString, RecordKey recordKey) throws RecordKeyException {
		try {
			String[] keyNames = StringUtils.split(pattern, separator);
			String[] keyValues = StringUtils.split(recordKeyString, separator);
	
			if(keyNames.length != keyValues.length)
				throw new IllegalArgumentException("부적합한 Record Key입니다.");
			
			for(int i = 0; i < keyNames.length; i++) {
				recordKey.setKeyValue(keyNames[i], keyValues[i]);
			}
		} catch(Exception e) {
			throw new RecordKeyException("RecordKey를 분리하는 중 오류가 발생했습니다.", e);
		}
	}
}
