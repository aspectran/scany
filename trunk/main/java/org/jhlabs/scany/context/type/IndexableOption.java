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


public final class IndexableOption extends Type {

	public static final IndexableOption TRUE;

	public static final IndexableOption FALSE;
	
	public static final IndexableOption TOKENIZE;
	
	private static final Map<String, IndexableOption> types;
	
	static {
		TRUE = new IndexableOption("true");
		FALSE = new IndexableOption("false");
		TOKENIZE = new IndexableOption("tokenize");

		types = new HashMap<String, IndexableOption>();
		types.put(TRUE.toString(), TRUE);
		types.put(FALSE.toString(), FALSE);
		types.put(TOKENIZE.toString(), TOKENIZE);
	}

	private IndexableOption(String type) {
		super(type);
	}

	public static IndexableOption valueOf(String type) {
		if(type == null)
			return null;
		
		return types.get(type);
	}
}
