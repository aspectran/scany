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

import java.util.Properties;

import org.jhlabs.scany.context.builder.ScanyContextBuilderAssistant;
import org.jhlabs.scany.context.rule.ClientRule;
import org.jhlabs.scany.context.rule.FileSpoolTransactionRule;
import org.jhlabs.scany.context.rule.LocalServiceRule;
import org.jhlabs.scany.context.rule.SpoolingRule;
import org.jhlabs.scany.context.type.ServiceMode;
import org.jhlabs.scany.context.type.SpoolingMode;
import org.jhlabs.scany.util.xml.EasyNodelet;
import org.jhlabs.scany.util.xml.EasyNodeletAdder;
import org.jhlabs.scany.util.xml.EasyNodeletParser;

/**
 * Translet Map Parser.
 * 
 * <p>Created: 2008. 06. 14 오전 4:39:24</p>
 */
public class LocalNodeletAdder implements EasyNodeletAdder {
	
	protected ScanyContextBuilderAssistant assistant;
	
	/**
	 * Instantiates a new content nodelet adder.
	 * 
	 * @param parser the parser
	 * @param assistant the assistant for Context Builder
	 */
	public LocalNodeletAdder(ScanyContextBuilderAssistant assistant) {
		this.assistant = assistant;
	}
	
	/**
	 * Process.
	 */
	public void process(String xpath, EasyNodeletParser parser) {
		parser.addNodelet(xpath, "/local", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				LocalServiceRule lsr = new LocalServiceRule();
				assistant.pushObject(lsr);
			}
		});
		parser.addNodelet(xpath, "/local/schema", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				LocalServiceRule lsr = (LocalServiceRule)assistant.peekObject();
				lsr.setSchemaConfigLocation(text);
			}
		});
		parser.addNodelet(xpath, "/local/directory", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				LocalServiceRule lsr = (LocalServiceRule)assistant.peekObject();
				lsr.setDirectory(text);
			}
		});
		parser.addNodelet(xpath, "/local/characterEncoding", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				LocalServiceRule lsr = (LocalServiceRule)assistant.peekObject();
				lsr.setCharacterEncoding(text);
			}
		});
		parser.addNodelet(xpath, "/local/spooling", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				SpoolingRule sr = new SpoolingRule();
				assistant.pushObject(sr);
			}
		});
		parser.addNodelet(xpath, "/local/spooling/directory", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				SpoolingRule sr = (SpoolingRule)assistant.peekObject();
				sr.setSpoolingMode(SpoolingMode.FILE);
				
				FileSpoolTransactionRule fstr = new FileSpoolTransactionRule();
				fstr.setDirectory(text);
				
				sr.setSpoolTransactionRule(fstr);
			}
		});
		parser.addNodelet(xpath, "/local/spooling/end()", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				SpoolingRule sr = (SpoolingRule)assistant.popObject();
				LocalServiceRule lsr = (LocalServiceRule)assistant.peekObject();
				
				lsr.setSpoolingRule(sr);
			}
		});

		if(xpath.endsWith("/scany")) {
			parser.addNodelet(xpath, "/local/end()", new EasyNodelet() {
				public void process(Properties attributes, String text) throws Exception {
					LocalServiceRule lsr = (LocalServiceRule)assistant.popObject();
					assistant.setLocalServiceRule(lsr);
				}
			});
		} else if(xpath.endsWith("/scany/client")) {
			parser.addNodelet(xpath, "/local/end()", new EasyNodelet() {
				public void process(Properties attributes, String text) throws Exception {
					LocalServiceRule lsr = (LocalServiceRule)assistant.popObject();

					if(lsr.getDirectory() != null || lsr.getDirectory().length() > 0) {
						ClientRule cr = (ClientRule)assistant.peekObject();
						cr.setServiceMode(ServiceMode.LOCAL);
						cr.setAnyServiceRule(lsr);
					}
				}
			});
		}
	}

}
