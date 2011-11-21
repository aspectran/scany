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

import org.jhlabs.scany.context.builder.ScanyContextBuilderAssistant;
import org.jhlabs.scany.engine.entity.Schema;
import org.jhlabs.scany.engine.search.summarize.Summarizer;
import org.jhlabs.scany.util.xml.EasyNodelet;
import org.jhlabs.scany.util.xml.EasyNodeletAdder;
import org.jhlabs.scany.util.xml.EasyNodeletParser;

/**
 * Translet Map Parser.
 * 
 * <p>Created: 2008. 06. 14 오전 4:39:24</p>
 */
public class SummarizersNodeletAdder implements EasyNodeletAdder {
	
	protected ScanyContextBuilderAssistant assistant;
	
	/**
	 * Instantiates a new content nodelet adder.
	 * 
	 * @param parser the parser
	 * @param assistant the assistant for Context Builder
	 */
	public SummarizersNodeletAdder(ScanyContextBuilderAssistant assistant) {
		this.assistant = assistant;
	}

	/**
	 * Process.
	 */
	public void process(String xpath, EasyNodeletParser parser) {
		parser.addNodelet(xpath, "/summarizers", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				Map<String, Summarizer> summarizerMap = new HashMap<String, Summarizer>(); 
				assistant.pushObject(summarizerMap);
			}
		});
		parser.addNodelet(xpath, "/summarizers/summarizer", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				String id = attributes.getProperty("id");
				String classType = attributes.getProperty("class");
				
				Summarizer summarizer = null;
				
				try {
					Class<?> clazz = Class.forName(classType);
					summarizer = (Summarizer)clazz.newInstance();
				} catch(Exception e) {
					throw new RuntimeException("Error setting Summarizer Class.  Cause: " + e, e);
				}
				
				assistant.pushObject(summarizer);
				assistant.pushObject(id);
			}
		});

		parser.addNodelet(xpath + "/summarizers/summarizer/properties", new PropertyNodeletAdder(assistant));
		
		parser.addNodelet(xpath, "/summarizers/summarizer/end()", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				String id = (String)assistant.popObject();
				Summarizer summarizer = (Summarizer)assistant.popObject();
				
				@SuppressWarnings("unchecked")
				Map<String, Summarizer> summarizerMap = (Map<String, Summarizer>)assistant.peekObject();
				summarizerMap.put(id, summarizer);
			}
		});
		parser.addNodelet(xpath, "/summarizers/end()", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				@SuppressWarnings("unchecked")
				Map<String, Summarizer> summarizerMap = (Map<String, Summarizer>)assistant.popObject();

				Schema schema = (Schema)assistant.peekObject();
				schema.setSummarizerMap(summarizerMap);
			}
		});
	}

}
