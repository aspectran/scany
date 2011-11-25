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

import org.apache.lucene.document.Field;


public final class WithTermVector extends Type {

	public static final WithTermVector NO;

	public static final WithTermVector YES;
	
	public static final WithTermVector WITH_POSITIONS;
	
	public static final WithTermVector WITH_OFFSETS;
	
	public static final WithTermVector WITH_POSITIONS_OFFSETS;

	public static final WithTermVector ALL;
	
	private static final Map<String, WithTermVector> types;
	
	static {
		NO = new WithTermVector(Field.TermVector.NO);
		YES = new WithTermVector(Field.TermVector.YES);
		WITH_POSITIONS = new WithTermVector(Field.TermVector.WITH_POSITIONS);
		WITH_OFFSETS = new WithTermVector(Field.TermVector.WITH_OFFSETS);
		WITH_POSITIONS_OFFSETS = new WithTermVector(Field.TermVector.WITH_POSITIONS_OFFSETS);
		ALL = new WithTermVector(Field.TermVector.WITH_POSITIONS_OFFSETS);

		types = new HashMap<String, WithTermVector>();
		types.put("no", NO);
		types.put("yes", YES);
		types.put("withPositions", WITH_POSITIONS);
		types.put("withOffsets", WITH_OFFSETS);
		types.put("withPositionsOffsets", WITH_POSITIONS_OFFSETS);
		types.put("all", ALL);
	}

	private WithTermVector(Field.TermVector type) {
		super(type);
	}

	public static WithTermVector valueOf(String type) {
		if(type == null)
			return null;
		
		return types.get(type);
	}
}
