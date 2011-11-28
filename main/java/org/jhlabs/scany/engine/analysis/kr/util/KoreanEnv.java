package org.jhlabs.scany.engine.analysis.kr.util;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.jhlabs.scany.engine.analysis.kr.ma.MorphException;

public class KoreanEnv {

	public static final String FILE_SYLLABLE_FEATURE = "syllable.dic";
	
	public static final String FILE_DICTIONARY = "total.dic";	
	
	public static final String FILE_JOSA = "josa.dic";
	
	public static final String FILE_EOMI = "eomi.dic";
	
	public static final String FILE_EXTENSION = "extension.dic";
	
	public static final String FILE_PREFIX = "prefix.dic";
	
	public static final String FILE_SUFFIX = "suffix.dic";	
	
	public static final String FILE_COMPOUNDS = "compounds.dic";	
	
	public static final String FILE_UNCOMPOUNDS = "uncompounds.dic";
		
	public static final String FILE_CJ = "cj.dic";
	
	public static final String FILE_KOREAN_PROPERTY = "org/jhlabs/scany/engine/analysis/kr/korean.properties";

	public static final String ENCODING = "utf-8";
	
	private Properties defaults;

	/**
	 * The props member gets its values from the configuration in the property file.
	 */
	private Properties props = null;
	
	private static KoreanEnv instance = null;
	
	/**
	 * The constructor loads property values from the property file.
	 */
	private KoreanEnv() throws MorphException {
		try {
			initDefaultProperties();
			props = loadProperties(defaults);
		} catch (MorphException e) {
			throw new MorphException ("Failure while initializing property values:\n"+e.getMessage());
		}
	}
	
	public static KoreanEnv getInstance() throws MorphException {
		if(instance==null)
			instance = new KoreanEnv();

		return instance;
	}
	
	/**
	 * Initialize the default property values.
	 */
	private void initDefaultProperties() {
		defaults = new Properties();
		
		defaults.setProperty(FILE_SYLLABLE_FEATURE,"org/jhlabs/scany/engine/analysis/kr/dic/syllable.dic");
		defaults.setProperty(FILE_DICTIONARY,"org/jhlabs/scany/engine/analysis/kr/dic/total.dic");
		defaults.setProperty(FILE_EXTENSION,"org/jhlabs/scany/engine/analysis/kr/dic/extension.dic");		
		defaults.setProperty(FILE_JOSA,"org/jhlabs/scany/engine/analysis/kr/dic/josa.dic");	
		defaults.setProperty(FILE_EOMI,"org/jhlabs/scany/engine/analysis/kr/dic/eomi.dic");	
		defaults.setProperty(FILE_PREFIX,"org/jhlabs/scany/engine/analysis/kr/dic/prefix.dic");		
		defaults.setProperty(FILE_SUFFIX,"org/jhlabs/scany/engine/analysis/kr/dic/suffix.dic");	
		defaults.setProperty(FILE_COMPOUNDS,"org/jhlabs/scany/engine/analysis/kr/dic/compounds.dic");	
		defaults.setProperty(FILE_UNCOMPOUNDS,"org/jhlabs/scany/engine/analysis/kr/dic/uncompounds.dic");
		defaults.setProperty(FILE_CJ,"org/jhlabs/scany/engine/analysis/kr/dic/cj.dic");
	}

	/**
	 * Given a property file name, load the property file and return an object
	 * representing the property values.
	 *
	 * @param propertyFile The name of the property file to load.
	 * @param def Default property values, or <code>null</code> if there are no defaults.
	 * @return The loaded SortedProperties object.
	 */
	private Properties loadProperties(Properties def) throws MorphException {
		Properties properties = new Properties();

		if (def != null) {
			properties = new Properties(def);
		}

		File file = null;
		try {
			file = FileUtil.getClassLoaderFile(FILE_KOREAN_PROPERTY);
			if (file != null) {
				properties.load(new FileInputStream(file));
				return properties;
			}
			
			byte[] in = FileUtil.readByteFromCurrentJar(FILE_KOREAN_PROPERTY);
			properties.load(new ByteArrayInputStream(in));
		} catch (Exception e) {
			throw new MorphException("Failure while trying to load properties file " + file.getPath(), e);
		}
		return properties;
	}
	
	
	/**
	 * Returns the value of a property.
	 *
	 * @param name The name of the property whose value is to be retrieved.
	 * @return The value of the property.
	 */
	public String getValue(String name) {
		return props.getProperty(name);
	}
}
