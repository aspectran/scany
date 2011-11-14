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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.jhlabs.scany.context.builder.ScanyContextBuilderAssistant;
import org.jhlabs.scany.context.rule.HttpServiceRule;
import org.jhlabs.scany.util.xml.Nodelet;
import org.jhlabs.scany.util.xml.NodeletAdder;
import org.jhlabs.scany.util.xml.NodeletParser;
import org.w3c.dom.Node;

/**
 * Translet Map Parser.
 * 
 * <p>Created: 2008. 06. 14 오전 4:39:24</p>
 */
public class HttpServiceRuleNodeletAdder implements NodeletAdder {
	
	protected ScanyContextBuilderAssistant assistant;
	
	private String prefixPath;

	/**
	 * Instantiates a new content nodelet adder.
	 * 
	 * @param parser the parser
	 * @param assistant the assistant for Context Builder
	 */
	public HttpServiceRuleNodeletAdder(ScanyContextBuilderAssistant assistant) {
		this.assistant = assistant;
	}

	/**
	 * Gets the prefix path.
	 * 
	 * @return the prefixPath
	 */
	public String getPrefixPath() {
		return prefixPath;
	}

	/**
	 * Sets the prefix path.
	 * 
	 * @param prefixPath the prefixPath to set
	 */
	public void setPrefixPath(String prefixPath) {
		this.prefixPath = prefixPath;
	}
	
	/**
	 * Process.
	 */
	public void process(String xpath, NodeletParser parser) {
		parser.addNodelet(xpath, "/http", new Nodelet() {
			public void process(Node node, Properties attributes, String text) throws Exception {
				HttpServiceRule hsr = new HttpServiceRule();
				assistant.pushObject(hsr);
			}
		});
		parser.addNodelet(xpath, "/http/schema", new Nodelet() {
			public void process(Node node, Properties attributes, String text) throws Exception {
				HttpServiceRule hsr = (HttpServiceRule)assistant.peekObject();
				hsr.setSchemaConfigLocation(text);
			}
		});
		parser.addNodelet(xpath, "/http/characterEncoding", new Nodelet() {
			public void process(Node node, Properties attributes, String text) throws Exception {
				HttpServiceRule hsr = (HttpServiceRule)assistant.peekObject();
				hsr.setCharacterEncoding(text);
			}
		});
		parser.addNodelet(xpath, "/http/url", new Nodelet() {
			public void process(Node node, Properties attributes, String text) throws Exception {
				HttpServiceRule hsr = (HttpServiceRule)assistant.peekObject();
				hsr.setUrl(text);
			}
		});
		parser.addNodelet(xpath, "/http/parameters", new Nodelet() {
			public void process(Node node, Properties attributes, String text) throws Exception {
				Map<String, String> parameters = new LinkedHashMap<String, String>();
				assistant.pushObject(parameters);
			}
		});
		parser.addNodelet(xpath, "/http/parameters/parameter", new Nodelet() {
			public void process(Node node, Properties attributes, String text) throws Exception {
				@SuppressWarnings("unchecked")
				Map<String, String> parameters = (Map<String, String>)assistant.peekObject();
			}
		});
	}

}
