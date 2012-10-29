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


public final class ServiceMode extends Type {

	public static final ServiceMode LOCAL;

	public static final ServiceMode HTTP;
	
	public static final ServiceMode REMOTE;
	
	private static final Map<String, ServiceMode> types;
	
	static {
		LOCAL = new ServiceMode("local");
		HTTP = new ServiceMode("http");
		REMOTE = new ServiceMode("remote");

		types = new HashMap<String, ServiceMode>();
		types.put(LOCAL.toString(), LOCAL);
		types.put(HTTP.toString(), HTTP);
		types.put(REMOTE.toString(), REMOTE);
	}

	private ServiceMode(String type) {
		super(type);
	}

	public static ServiceMode valueOf(String type) {
		if(type == null)
			return null;
		
		return types.get(type);
	}
}
