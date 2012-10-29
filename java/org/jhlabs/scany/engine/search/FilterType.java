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

import org.jhlabs.scany.context.type.Type;

public final class FilterType extends Type {

	public static final FilterType EQUAL;

	public static final FilterType TEXT_RANGE;
	
	public static final FilterType INT_RANGE;
	
	public static final FilterType LONG_RANGE;
	
	public static final FilterType FLOAT_RANGE;
	
	public static final FilterType DOUBLE_RANGE;
	
	
	private static final Map<Object, FilterType> types;
	
	static {
		EQUAL = new FilterType("equal");
		TEXT_RANGE = new FilterType("text-range");
		INT_RANGE = new FilterType("int-range");
		LONG_RANGE = new FilterType("long-range");
		FLOAT_RANGE = new FilterType("float-range");
		DOUBLE_RANGE = new FilterType("double-range");

		types = new HashMap<Object, FilterType>();
		types.put(EQUAL.getType(), EQUAL);
		types.put(TEXT_RANGE.getType(), TEXT_RANGE);
		types.put(INT_RANGE.getType(),INT_RANGE);
		types.put(LONG_RANGE.getType(), LONG_RANGE);
		types.put(FLOAT_RANGE.getType(), FLOAT_RANGE);
		types.put(DOUBLE_RANGE.getType(), DOUBLE_RANGE);
	}

	private FilterType(Object FilterTypeType) {
		super(FilterTypeType);
	}

	public static FilterType valueOf(Object type) {
		return types.get(type);
	}
}
