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

import java.io.InputStream;
import java.util.Properties;

import org.jhlabs.scany.context.builder.ScanyContextBuilderAssistant;
import org.jhlabs.scany.engine.entity.Schema;
import org.jhlabs.scany.util.xml.EasyNodelet;
import org.jhlabs.scany.util.xml.EasyNodeletParser;

/**
 * Translet Map Parser.
 * 
 * <p>Created: 2008. 06. 14 오전 4:39:24</p>
 */
public class SchemaNodeParser {
	
	private final EasyNodeletParser parser = new EasyNodeletParser();

	private final ScanyContextBuilderAssistant assistant;
	
	/**
	 * Instantiates a new translet map parser.
	 * 
	 * @param assistant the assistant for Context Builder
	 */
	public SchemaNodeParser(ScanyContextBuilderAssistant assistant) {
		//super(log);
		
		this.assistant = assistant;
		this.assistant.clearObjectStack();

		parser.setValidation(true);
		parser.setEntityResolver(new ScanyDtdResolver());

		addRootNodelets();
		addAnalyzersNodelets();
		addSummarizersNodelets();
		addRelationNodelets();
	}

	/**
	 * Parses the translet map.
	 * 
	 * @param inputStream the input stream
	 * 
	 * @return the translet rule map
	 * 
	 * @throws Exception the exception
	 */
	public void parse(InputStream inputStream) throws Exception {
		try {
			parser.parse(inputStream);
		} catch(Exception e) {
			throw new Exception("Error parsing scany schema configuration. Cause: " + e, e);
		}
	}

	/**
	 * Adds the translet map nodelets.
	 */
	private void addRootNodelets() {
		parser.addNodelet("/schema", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				Schema schema = new Schema();
				assistant.pushObject(schema);
			}
		});
		parser.addNodelet("/schema/end()", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				Schema schema = (Schema)assistant.popObject();
				assistant.setSchema(schema);
			}
		});
	}

	private void addAnalyzersNodelets() {
		parser.addNodelet("/schema", new AnalyzersNodeletAdder(assistant));
	}
	
	private void addSummarizersNodelets() {
		parser.addNodelet("/schema", new SummarizersNodeletAdder(assistant));
	}
	
	private void addRelationNodelets() {
	}

}
