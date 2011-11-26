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

import org.jhlabs.scany.context.ScanyContext;
import org.jhlabs.scany.context.ScanyContextException;
import org.jhlabs.scany.context.builder.xml.ScanyNodeParser;
import org.jhlabs.scany.context.builder.xml.SchemaNodeParser;
import org.jhlabs.scany.context.rule.ClientRule;
import org.jhlabs.scany.context.rule.LocalServiceRule;
import org.jhlabs.scany.context.type.ServiceMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author gulendol
 *
 */
public class ScanyContextBuilder {

	private static final Logger logger = LoggerFactory.getLogger(ScanyContextBuilder.class);

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

			loadSchemaConfig(assistant);
			
			ScanyContext scanyContext = new ScanyContext();
			scanyContext.setLocalServiceRule(assistant.getLocalServiceRule());
			scanyContext.setClientRule(assistant.getClientRule());
			scanyContext.setServerRule(assistant.getServerRule());
			
			return scanyContext;
		} catch(Exception e) {
			logger.error("Scany configuration error.", e);
			throw new ScanyContextBuilderException("Scany configuration error", e);
		}
	}
	
	private void loadScanyConfig(InputStream is, ScanyConfigAssistant assistant) throws Exception {
		ScanyNodeParser scanyNodeParser = new ScanyNodeParser(assistant);
		scanyNodeParser.parse(is);
	}
	
	private void loadSchemaConfig(ScanyConfigAssistant scanyConfigAssistant) throws Exception {
		LocalServiceRule localServiceRule = scanyConfigAssistant.getLocalServiceRule();
		ClientRule clientRule = scanyConfigAssistant.getClientRule();

		String localDirectory = null;
		String clientDirectory = null;
		String localSchemaConfigLocation = null;
		String clientSchemaConfigLocation = null;
		
		if(localServiceRule != null) {
			localDirectory = localServiceRule.getDirectory();
		}

		if(clientRule != null && clientRule.getServiceMode() == ServiceMode.LOCAL) {
			clientDirectory = ((LocalServiceRule)clientRule.getAnyServiceRule()).getDirectory();
		} 
		
		if(localServiceRule != null) {
			localSchemaConfigLocation = localServiceRule.getSchemaConfigLocation();
		}
		
		if(clientRule != null) {
			clientSchemaConfigLocation = clientRule.getSchemaConfigLocation();
		}
		
		if(localSchemaConfigLocation != null && localSchemaConfigLocation.equals(clientSchemaConfigLocation))
			clientSchemaConfigLocation = null;
			
		if(localSchemaConfigLocation != null) {
			SchemaConfigAssistant assistant = new SchemaConfigAssistant();
			assistant.setBaseDirectory(localDirectory);
			loadSchema(localSchemaConfigLocation, assistant);
			assistant.clearObjectStack();
			localServiceRule.setSchema(assistant.getSchema());
		}
		
		if(clientSchemaConfigLocation != null) {
			SchemaConfigAssistant assistant = new SchemaConfigAssistant();
			assistant.setBaseDirectory(clientDirectory);
			loadSchema(clientSchemaConfigLocation, assistant);
			assistant.clearObjectStack();
			clientRule.setSchema(assistant.getSchema());
		}
		
		if(localServiceRule != null && clientRule != null && clientRule.getSchema() == null) {
			clientRule.setSchema(localServiceRule.getSchema());
		}
	}

	private void loadSchema(String schemaConfigLocation, SchemaConfigAssistant assistant) throws Exception {
		File file = new File(schemaConfigLocation);
		
		if(!file.isFile())
			throw new FileNotFoundException("Scany schema configuration file is not found. " + schemaConfigLocation);

		InputStream is = new FileInputStream(file);
		loadSchema(is, assistant);
		is.close();
	}
	
	private void loadSchema(InputStream is, SchemaConfigAssistant assistant) throws Exception {
		SchemaNodeParser schemaNodeParser = new SchemaNodeParser(assistant);
		schemaNodeParser.parse(is);
	}
}
