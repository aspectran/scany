/*******************************************************************************
 * Copyright (c) 2008 Jeong Ju Ho.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Jeong Ju Ho - initial API and implementation
 ******************************************************************************/
package org.jhlabs.scany.context.builder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jhlabs.scany.context.ScanyContext;
import org.jhlabs.scany.context.ScanyContextException;
import org.jhlabs.scany.context.builder.xml.ScanyNodeParser;
import org.jhlabs.scany.context.builder.xml.SchemaNodeParser;
import org.jhlabs.scany.context.rule.ClientRule;
import org.jhlabs.scany.context.rule.LocalServiceRule;
import org.jhlabs.scany.context.rule.ServerRule;
import org.jhlabs.scany.engine.entity.Schema;

/**
 * 
 * @author gulendol
 *
 */
public class ScanyContextBuilder {

	private static Log log = LogFactory.getLog(ScanyContextBuilder.class);

	public ScanyContext build(String contextConfigLocation) throws ScanyContextException, IOException {
		File file = new File(contextConfigLocation);
		
		if(!file.isFile())
			throw new FileNotFoundException("Scany context configuration file is not found. " + contextConfigLocation);

		InputStream is = new FileInputStream(file);
		ScanyContext scanyContext = build(is);
		is.close();
		
		return scanyContext;
	}
	
	public ScanyContext build(InputStream is) throws ScanyContextException {
		try {
			ScanyConfigAssistant assistant = new ScanyConfigAssistant();
	
			loadScanyConfig(is, assistant);
			assistant.clearObjectStack();

			assistant.clearObjectStack();

			LocalServiceRule localServiceRule = assistant.getLocalServiceRule();
			ClientRule clientRule = assistant.getClientRule();
			ServerRule serverRule = assistant.getServerRule();
			
			ScanyContext scanyContext = new ScanyContext();
			scanyContext.setLocalServiceRule(localServiceRule);
			scanyContext.setClientRule(clientRule);
			scanyContext.setServerRule(serverRule);
			
			return scanyContext;
		} catch(Exception e) {
			log.error("Translets configuration error.");
			throw new ScanyContextBuilderException("Scany configuration error", e);
		}
	}
	
	private void loadScanyConfig(InputStream is, ScanyConfigAssistant assistant) throws Exception {
		ScanyNodeParser scanyNodeParser = new ScanyNodeParser(assistant);
		scanyNodeParser.parse(is);
	}
	
	private void loadSchemaConfig(ScanyConfigAssistant assistant) throws Exception {
		LocalServiceRule localServiceRule = assistant.getLocalServiceRule();
		ClientRule clientRule = assistant.getClientRule();
		ServerRule serverRule = assistant.getServerRule();
		
		ScanyNodeParser scanyNodeParser = new ScanyNodeParser(assistant);
		scanyNodeParser.parse(is);
	}

	public Schema loadSchema(String schemaConfigLocation) throws Exception {
		File file = new File(schemaConfigLocation);
		
		if(!file.isFile())
			throw new FileNotFoundException("Scany schema configuration file is not found. " + schemaConfigLocation);

		InputStream is = new FileInputStream(file);
		Schema schema = loadSchema(is);
		is.close();
		
		return schema;
	}
	
	private Schema loadSchema(InputStream is) throws Exception {
		ScanyConfigAssistant assistant = new ScanyConfigAssistant();
		SchemaNodeParser schemaNodeParser = new SchemaNodeParser(assistant);
		schemaNodeParser.parse(is);
	}
}
