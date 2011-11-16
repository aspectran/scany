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

import org.apache.lucene.analysis.Analyzer;
import org.jhlabs.scany.context.builder.ScanyContextBuilderAssistant;
import org.jhlabs.scany.context.rule.LocalServiceRule;
import org.jhlabs.scany.util.xml.Nodelet;
import org.jhlabs.scany.util.xml.NodeletAdder;
import org.jhlabs.scany.util.xml.NodeletParser;
import org.w3c.dom.Node;

/**
 * Translet Map Parser.
 * 
 * <p>Created: 2008. 06. 14 오전 4:39:24</p>
 */
public class AnalyzersNodeletAdder implements NodeletAdder {
	
	protected ScanyContextBuilderAssistant assistant;
	
	/**
	 * Instantiates a new content nodelet adder.
	 * 
	 * @param parser the parser
	 * @param assistant the assistant for Context Builder
	 */
	public AnalyzersNodeletAdder(ScanyContextBuilderAssistant assistant) {
		this.assistant = assistant;
	}

	/**
	 * Process.
	 */
	public void process(String xpath, NodeletParser parser) {
		parser.addNodelet(xpath, "/analyzers", new Nodelet() {
			public void process(Node node, Properties attributes, String text) throws Exception {
				Map<String, Analyzer> analyzers = new HashMap<String, Analyzer>(); 
				assistant.pushObject(analyzers);
			}
		});
		parser.addNodelet(xpath, "/analyzers/analyzer", new Nodelet() {
			public void process(Node node, Properties attributes, String text) throws Exception {
				String id = attributes.getProperty("id");
				String clazz = attributes.getProperty("class");
				
				
				
				LocalServiceRule lsr = new LocalServiceRule();
				assistant.pushObject(lsr);
			}
		});
		parser.addNodelet(xpath, "/analyzers/end()", new Nodelet() {
			public void process(Node node, Properties attributes, String text) throws Exception {
				Map<String, Analyzer> analyzers = new HashMap<String, Analyzer>(); 
				assistant.pushObject(analyzers);
			}
		});
	}

}
