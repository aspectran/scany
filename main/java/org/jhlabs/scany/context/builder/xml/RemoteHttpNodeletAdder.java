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
import org.jhlabs.scany.context.rule.RemoteHttpServiceRule;
import org.jhlabs.scany.context.rule.RemoteTcpServiceRule;
import org.jhlabs.scany.context.type.RemoteMode;
import org.jhlabs.scany.context.type.ServiceMode;
import org.jhlabs.scany.util.xml.EasyNodelet;
import org.jhlabs.scany.util.xml.EasyNodeletAdder;
import org.jhlabs.scany.util.xml.EasyNodeletParser;

/**
 * Translet Map Parser.
 * 
 * <p>Created: 2008. 06. 14 오전 4:39:24</p>
 */
public class RemoteHttpNodeletAdder implements EasyNodeletAdder {
	
	protected ScanyContextBuilderAssistant assistant;
	
	/**
	 * Instantiates a new content nodelet adder.
	 * 
	 * @param parser the parser
	 * @param assistant the assistant for Context Builder
	 */
	public RemoteHttpNodeletAdder(ScanyContextBuilderAssistant assistant) {
		this.assistant = assistant;
	}
	
	/**
	 * Process.
	 */
	public void process(String xpath, EasyNodeletParser parser) {
		parser.addNodelet(xpath, "/remote/http", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				RemoteHttpServiceRule hsr = new RemoteHttpServiceRule();
				assistant.pushObject(hsr);
			}
		});
		parser.addNodelet(xpath, "/remote/http/schema", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				RemoteHttpServiceRule hsr = (RemoteHttpServiceRule)assistant.peekObject();
				hsr.setSchemaConfigLocation(text);
			}
		});
		parser.addNodelet(xpath, "/remote/http/characterEncoding", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				RemoteHttpServiceRule hsr = (RemoteHttpServiceRule)assistant.peekObject();
				hsr.setCharacterEncoding(text);
			}
		});
		parser.addNodelet(xpath, "/remote/http/url", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				RemoteHttpServiceRule hsr = (RemoteHttpServiceRule)assistant.peekObject();
				hsr.setUrl(text);
			}
		});

		parser.addNodelet(xpath + "/remote/http", new MessageNodeletAdder(assistant));

		if(xpath.endsWith("/scany/client")) {
			parser.addNodelet(xpath, "/remote/http/end()", new EasyNodelet() {
				public void process(Properties attributes, String text) throws Exception {
					RemoteTcpServiceRule rtsr = (RemoteTcpServiceRule)assistant.popObject();
					ClientRule cr = (ClientRule)assistant.peekObject();
					cr.setServiceMode(ServiceMode.REMOTE);
					cr.setRemoteMode(RemoteMode.HTTP);
					cr.setAnyServiceRule(rtsr);
				}
			});
		}
	}

}
