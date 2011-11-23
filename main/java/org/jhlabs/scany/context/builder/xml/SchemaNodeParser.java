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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.lucene.analysis.Analyzer;
import org.jhlabs.scany.context.builder.SchemaConfigAssistant;
import org.jhlabs.scany.context.type.DirectoryType;
import org.jhlabs.scany.engine.entity.Attribute;
import org.jhlabs.scany.engine.entity.AttributeMap;
import org.jhlabs.scany.engine.entity.RecordKeyPattern;
import org.jhlabs.scany.engine.entity.Relation;
import org.jhlabs.scany.engine.entity.Schema;
import org.jhlabs.scany.engine.search.summarize.Summarizer;
import org.jhlabs.scany.util.xml.EasyNodelet;
import org.jhlabs.scany.util.xml.EasyNodeletParser;

/**
 * Translet Map Parser.
 * 
 * <p>Created: 2008. 06. 14 오전 4:39:24</p>
 */
public class SchemaNodeParser {
	
	private final EasyNodeletParser parser = new EasyNodeletParser();

	private final SchemaConfigAssistant assistant;
	
	/**
	 * Instantiates a new translet map parser.
	 * 
	 * @param assistant the assistant for Context Builder
	 */
	public SchemaNodeParser(SchemaConfigAssistant assistant) {
		//super(log);
		
		this.assistant = assistant;
		this.assistant.clearObjectStack();

		parser.setValidation(false);
		//parser.setEntityResolver(new ScanyDtdResolver());

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
				assistant.setSchema(schema);
			}
		});
		parser.addNodelet("/schema/end()", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
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
		parser.addNodelet("/schema/relation", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				String id = attributes.getProperty("id");
				String analyzerId = attributes.getProperty("analyzer");

				Schema schema = assistant.getSchema();
				Analyzer analyzer = schema.getAnalyzer(analyzerId);
				
				if(analyzer == null)
					throw new IllegalArgumentException("Could not set analyzer '" + analyzerId + "' on relation '" + id + "'.");
				
				Relation relation = new Relation();
				relation.setId(id);
				relation.setAnalyzer(analyzer);
				
				assistant.pushObject(relation);
			}
		});
		parser.addNodelet("/schema/relation/description", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				Relation relation = (Relation)assistant.peekObject();
				relation.setDescription(text);
			}
		});
		parser.addNodelet("/schema/relation/directory", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				String type = attributes.getProperty("type");
				String relative = attributes.getProperty("relative");
				Boolean relativeDirectory = Boolean.valueOf(relative);
				
				DirectoryType directoryType = DirectoryType.valueOf(type);
				
				if(type != null && type.length() > 0 && directoryType == null)
					throw new IllegalArgumentException("Unknown directory type '" + type + "'.");
				
				if(directoryType == null)
					directoryType = DirectoryType.FS;
				
				Relation relation = (Relation)assistant.peekObject();
				relation.setDirectoryType(directoryType);
				
				if(relativeDirectory == Boolean.TRUE) {
					String basetDirectory = assistant.getBaseDirectory();
					String directory = basetDirectory + "/" + text;
					relation.setDirectory(directory);
				} else{
					if(text != null && text.length() > 0)
						relation.setDirectory(text);
				}
			}
		});
		parser.addNodelet("/schema/relation/recordKeyPattern", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				String separator = attributes.getProperty("separator");
				
				RecordKeyPattern recordKeyPattern = new RecordKeyPattern();
				recordKeyPattern.setPattern(text, separator);
				
				Relation relation = (Relation)assistant.peekObject();
				relation.setRecordKeyPattern(recordKeyPattern);
			}
		});
		parser.addNodelet("/schema/relation/attributes", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				AttributeMap attributeMap = new AttributeMap();
				assistant.pushObject(attributeMap);
			}
		});
		parser.addNodelet("/schema/relation/attributes/attribute", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				String name = attributes.getProperty("name");
				Boolean storable = Boolean.valueOf(attributes.getProperty("storable"));
				Boolean indexable = Boolean.valueOf(attributes.getProperty("indexable"));
				Boolean queryable = Boolean.valueOf(attributes.getProperty("queryable"));
				String analyzerId = attributes.getProperty("analyzer");
				String summarizerId = attributes.getProperty("summarizer");
				String boost = attributes.getProperty("boost");

				Attribute attribute = new Attribute();
				attribute.setName(name);
				attribute.setDescription(text);
				attribute.setStorable(storable);
				attribute.setIndexable(indexable);
				attribute.setQueryable(queryable);
				
				Schema schema = assistant.getSchema();

				if(analyzerId != null && analyzerId.length() > 0) {
					Analyzer analyzer = schema.getAnalyzer(analyzerId);
					
					if(analyzer == null)
						throw new IllegalArgumentException("Could not set analyzer '" + analyzerId + "' on attribute '" + name + "'.");

					attribute.setAnalyzer(analyzer);
				}
				
				if(summarizerId != null && summarizerId.length() > 0) {
					Summarizer summarizer = schema.getSummarizer(summarizerId);
					
					if(summarizer == null)
						throw new IllegalArgumentException("Could not set summarizer '" + summarizerId + "' on attribute '" + name + "'.");

					attribute.setSummarizer(summarizer);
				}
				
				if(boost != null && boost.length() > 0) {
					try {
						attribute.setBoost(Float.valueOf(boost));
					} catch(NumberFormatException e) {
						throw new IllegalArgumentException("Check the boost value '" + boost + "' on attribute '" + name + "'.");
					}
				}
				
				AttributeMap attributeMap = (AttributeMap)assistant.peekObject();
				attributeMap.put(name, attribute);
			}
		});
		parser.addNodelet("/schema/relation/attributes/end()", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				AttributeMap attributeMap = (AttributeMap)assistant.popObject();
				Relation relation = (Relation)assistant.peekObject();
				relation.setAttributeMap(attributeMap);
			}
		});
		parser.addNodelet("/schema/relation/end()", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				Relation relation = (Relation)assistant.popObject();
				Schema schema = assistant.getSchema();
				relation.setSchema(schema);
				Map<String, Relation> relationMap = schema.getRelationMap();
				
				if(relationMap == null) {
					relationMap = new HashMap<String, Relation>();
					schema.setRelationMap(relationMap);
				}
				
				relationMap.put(relation.getId(), relation);
			}
		});
	}

}
