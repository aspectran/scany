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
package org.jhlabs.scany.context.type;

import java.util.HashMap;
import java.util.Map;


public final class QueryableOption extends Type {

	public static final QueryableOption TRUE;

	public static final QueryableOption FALSE;
	
	public static final QueryableOption PREFIX;
	
	private static final Map<String, QueryableOption> types;
	
	static {
		TRUE = new QueryableOption("true");
		FALSE = new QueryableOption("false");
		PREFIX = new QueryableOption("tokenize");

		types = new HashMap<String, QueryableOption>();
		types.put(TRUE.toString(), TRUE);
		types.put(FALSE.toString(), FALSE);
		types.put(PREFIX.toString(), PREFIX);
	}

	private QueryableOption(String type) {
		super(type);
	}

	public static QueryableOption valueOf(String type) {
		if(type == null)
			return null;
		
		return types.get(type);
	}
}
