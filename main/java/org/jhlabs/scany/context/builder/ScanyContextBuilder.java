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
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jhlabs.scany.context.ScanyContext;
import org.jhlabs.scany.context.ScanyContextException;
import org.jhlabs.scany.context.builder.xml.ScanyNodeParser;
import org.jhlabs.scany.context.rule.ClientRule;
import org.jhlabs.scany.context.rule.LocalServiceRule;
import org.jhlabs.scany.context.rule.ServerRule;

/**
 * 
 * @author gulendol
 *
 */
public class ScanyContextBuilder {

	private final static Log log = LogFactory.getLog(ScanyContextBuilder.class);

	public static ScanyContext build(String contextConfigLocation) throws ScanyContextException, FileNotFoundException {
		File file = new File(contextConfigLocation);
		
		if(!file.isFile())
			throw new FileNotFoundException("scany context configuration file is not found. " + contextConfigLocation);

		InputStream is = new FileInputStream(file);
		
		return build(is);
	}
	
	public static ScanyContext build(InputStream is) throws ScanyContextException {
		try {
			ScanyContextBuilderAssistant assistant = new ScanyContextBuilderAssistant();
			
			ScanyNodeParser scanyNodeParser = new ScanyNodeParser(assistant);
			scanyNodeParser.parse(is);
	
			LocalServiceRule localServiceRule = assistant.getLocalServiceRule();
			ClientRule clientRule = assistant.getClientRule();
			ServerRule serverRule = assistant.getServerRule();
			
			ScanyContext scanyContext = new ScanyContext();
			scanyContext.setLocalServiceRule(localServiceRule);
			scanyContext.setClientRule(clientRule);
			scanyContext.setServerRule(serverRule);
			
			assistant.clearObjectStack();
	
			return scanyContext;
		} catch(Exception e) {
			log.error("Translets configuration error.");
			throw new ScanyContextBuilderException("Scany configuration error", e);
		}
	}
	
}
