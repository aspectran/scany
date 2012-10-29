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


public final class SpoolingMode extends Type {

	public static final SpoolingMode FILE;

	public static final SpoolingMode FTP;
	
	public static final SpoolingMode JDBC;
	
	public static final SpoolingMode JNDI;
	
	private static final Map<String, SpoolingMode> types;
	
	static {
		FILE = new SpoolingMode("file");
		FTP = new SpoolingMode("ftp");
		JDBC = new SpoolingMode("jdbc");
		JNDI = new SpoolingMode("jndi");

		types = new HashMap<String, SpoolingMode>();
		types.put(FILE.toString(), FILE);
		types.put(FTP.toString(), FTP);
		types.put(JDBC.toString(), JDBC);
		types.put(JNDI.toString(), JNDI);
	}

	private SpoolingMode(String type) {
		super(type);
	}

	public static SpoolingMode valueOf(String type) {
		if(type == null)
			return null;
		
		return types.get(type);
	}
}
