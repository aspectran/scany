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

/**
 * 레코드 정보를 담고 있다.
 * 
 * @author Gulendol
 *
 */
public class Record implements java.io.Serializable {

	private static final long serialVersionUID = 478994437917671858L;

	private RecordKey recordKey;

	private Map<String, String> values = new HashMap<String, String>();

	public RecordKey getRecordKey() {
		return recordKey;
	}

	public void setRecordKey(RecordKey recordKey) {
		this.recordKey = recordKey;
	}

	public Map<String, String> getValues() {
		return values;
	}

	public void setValues(Map<String, String> values) {
		this.values = values;
	}
	
	public String getValue(String attributeName) {
		return values.get(attributeName);
	}
	
	public String setValue(String attributeName, String value) {
		return values.put(attributeName, value);
	}
	
}
