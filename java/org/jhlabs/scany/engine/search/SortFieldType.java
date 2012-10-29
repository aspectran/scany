/*
 *  Copyright (c) 2008 Jeong Ju Ho, All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.jhlabs.scany.engine.search;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.search.SortField;
import org.jhlabs.scany.context.type.Type;

public final class SortFieldType extends Type {

	public static final SortFieldType SCORE;

	public static final SortFieldType DOC;
	
	public static final SortFieldType STRING;
	
	public static final SortFieldType INT;
	
	public static final SortFieldType FLOAT;
	
	private static final Map<Object, SortFieldType> types;
	
	static {
		SCORE = new SortFieldType(SortField.SCORE);
		DOC = new SortFieldType(SortField.DOC);
		STRING = new SortFieldType(SortField.STRING);
		INT = new SortFieldType(SortField.INT);
		FLOAT = new SortFieldType(SortField.FLOAT);

		types = new HashMap<Object, SortFieldType>();
		types.put(SCORE.getType(), SCORE);
		types.put(DOC.getType(), DOC);
		types.put(STRING.getType(), STRING);
		types.put(INT.getType(), INT);
		types.put(FLOAT.getType(), FLOAT);
	}

	private SortFieldType(Object sortFieldType) {
		super(sortFieldType);
	}

	public static SortFieldType valueOf(Object type) {
		return types.get(type);
	}
}
