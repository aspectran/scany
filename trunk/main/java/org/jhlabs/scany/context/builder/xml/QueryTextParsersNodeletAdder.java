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
package org.jhlabs.scany.context.builder.xml;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.jhlabs.scany.context.builder.SchemaConfigAssistant;
import org.jhlabs.scany.engine.entity.Schema;
import org.jhlabs.scany.engine.search.query.QueryTextParser;
import org.jhlabs.scany.util.xml.EasyNodelet;
import org.jhlabs.scany.util.xml.EasyNodeletAdder;
import org.jhlabs.scany.util.xml.EasyNodeletParser;

/**
 * Translet Map Parser.
 * 
 * <p>Created: 2008. 06. 14 오전 4:39:24</p>
 */
public class QueryTextParsersNodeletAdder implements EasyNodeletAdder {
	
	protected SchemaConfigAssistant assistant;
	
	/**
	 * Instantiates a new content nodelet adder.
	 * 
	 * @param parser the parser
	 * @param assistant the assistant for Context Builder
	 */
	public QueryTextParsersNodeletAdder(SchemaConfigAssistant assistant) {
		this.assistant = assistant;
	}

	/**
	 * Process.
	 */
	public void process(String xpath, EasyNodeletParser parser) {
		parser.addNodelet(xpath, "/queryTextParsers", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				Map<String, QueryTextParser> queryTextParserMap = new HashMap<String, QueryTextParser>(); 
				assistant.pushObject(queryTextParserMap);
			}
		});
		parser.addNodelet(xpath, "/queryTextParsers/queryTextParser", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				String id = attributes.getProperty("id");
				String classType = attributes.getProperty("class");
				
				QueryTextParser queryTextParser = null;
				
				try {
					Class<?> clazz = Class.forName(classType);
					queryTextParser = (QueryTextParser)clazz.newInstance();
				} catch(Exception e) {
					throw new RuntimeException("Error setting QueryTextParser Class.  Cause: " + e, e);
				}
				
				assistant.pushObject(id);
				assistant.pushObject(queryTextParser);
			}
		});

		parser.addNodelet(xpath + "/queryTextParsers/queryTextParser/properties", new PropertyNodeletAdder(assistant));

		parser.addNodelet(xpath, "/queryTextParsers/queryTextParser/end()", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				QueryTextParser queryTextParser = (QueryTextParser)assistant.popObject();
				String id = (String)assistant.popObject();

				@SuppressWarnings("unchecked")
				Map<String, QueryTextParser> queryTextParserMap = (Map<String, QueryTextParser>)assistant.peekObject();
				queryTextParserMap.put(id, queryTextParser);
			}
		});
		parser.addNodelet(xpath, "/queryTextParsers/end()", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				@SuppressWarnings("unchecked")
				Map<String, QueryTextParser> queryTextParserMap = (Map<String, QueryTextParser>)assistant.popObject();

				Schema schema = assistant.getSchema();
				schema.setQueryTextParserMap(queryTextParserMap);
			}
		});
	}

}
