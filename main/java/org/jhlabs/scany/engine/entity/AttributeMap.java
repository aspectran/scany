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
package org.jhlabs.scany.engine.entity;

import java.util.LinkedHashMap;


/**
 * <p>Created: 2009. 01. 04 오후 3:3:13</p>
 */
public class AttributeMap extends LinkedHashMap<String, Attribute> {

	/** @serial */
	private static final long serialVersionUID = 5793481770995089479L;
	
	public Attribute put(Attribute attribute) {
		return put(attribute.getName(), attribute);
	}
	
	public String[] getAttributeNames() {
		return keySet().toArray(new String[size()]);
	}
	
}
