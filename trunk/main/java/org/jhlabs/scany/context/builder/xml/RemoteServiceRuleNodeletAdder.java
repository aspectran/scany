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
import org.jhlabs.scany.context.rule.MessageRule;
import org.jhlabs.scany.context.rule.RemoteServiceRule;
import org.jhlabs.scany.context.rule.ServerRule;
import org.jhlabs.scany.util.xml.Nodelet;
import org.jhlabs.scany.util.xml.NodeletAdder;
import org.jhlabs.scany.util.xml.NodeletParser;
import org.w3c.dom.Node;

/**
 * Translet Map Parser.
 * 
 * <p>Created: 2008. 06. 14 오전 4:39:24</p>
 */
public class RemoteServiceRuleNodeletAdder implements NodeletAdder {
	
	protected ScanyContextBuilderAssistant assistant;
	
	/**
	 * Instantiates a new content nodelet adder.
	 * 
	 * @param parser the parser
	 * @param assistant the assistant for Context Builder
	 */
	public RemoteServiceRuleNodeletAdder(ScanyContextBuilderAssistant assistant) {
		this.assistant = assistant;
	}
	
	/**
	 * Process.
	 */
	public void process(String xpath, NodeletParser parser) {
		parser.addNodelet(xpath, "/remote", new Nodelet() {
			public void process(Node node, Properties attributes, String text) throws Exception {
				RemoteServiceRule rsr = new RemoteServiceRule();
				assistant.pushObject(rsr);
			}
		});
		parser.addNodelet(xpath, "/remote/schema", new Nodelet() {
			public void process(Node node, Properties attributes, String text) throws Exception {
				RemoteServiceRule rsr = (RemoteServiceRule)assistant.peekObject();
				rsr.setSchemaConfigLocation(text);
			}
		});
		parser.addNodelet(xpath, "/remote/characterEncoding", new Nodelet() {
			public void process(Node node, Properties attributes, String text) throws Exception {
				RemoteServiceRule rsr = (RemoteServiceRule)assistant.peekObject();
				rsr.setCharacterEncoding(text);
			}
		});
		parser.addNodelet(xpath, "/remote/connection/host", new Nodelet() {
			public void process(Node node, Properties attributes, String text) throws Exception {
				String port = attributes.getProperty("port");

				RemoteServiceRule rsr = (RemoteServiceRule)assistant.peekObject();
				rsr.setHost(text);
				
				if(port != null && port.length() > 0)
					rsr.setPort(Integer.valueOf(port));
			}
		});
		parser.addNodelet(xpath, "/remote/connection/timeout", new Nodelet() {
			public void process(Node node, Properties attributes, String text) throws Exception {
				if(text != null && text.length() > 0) {
					RemoteServiceRule rsr = (RemoteServiceRule)assistant.peekObject();
					rsr.setTimeout(Integer.valueOf(text));
				}
			}
		});
		parser.addNodelet(xpath, "/remote/message/keysign", new Nodelet() {
			public void process(Node node, Properties attributes, String text) throws Exception {
				String encryption = attributes.getProperty("encryption");
				String compressable = attributes.getProperty("compressable");
				
				MessageRule mr = new MessageRule();
				mr.setEncryption(encryption);
				mr.setCompressable(Boolean.valueOf(compressable));
				mr.setText(text);

				RemoteServiceRule rsr = (RemoteServiceRule)assistant.peekObject();
				rsr.setKeysignMessageRule(mr);
			}
		});
		parser.addNodelet(xpath, "/remote/message/body", new Nodelet() {
			public void process(Node node, Properties attributes, String text) throws Exception {
				String encryption = attributes.getProperty("encryption");
				String compressable = attributes.getProperty("compressable");
				
				MessageRule mr = new MessageRule();
				mr.setEncryption(encryption);
				mr.setCompressable(Boolean.valueOf(compressable));

				RemoteServiceRule rsr = (RemoteServiceRule)assistant.peekObject();
				rsr.setBodyMessageRule(mr);
			}
		});

		if(xpath.endsWith("/scany/client")) {
			parser.addNodelet(xpath, "/remote/end()", new Nodelet() {
				public void process(Node node, Properties attributes, String text) throws Exception {
					RemoteServiceRule lsr = (RemoteServiceRule)assistant.popObject();
					ClientRule cr = (ClientRule)assistant.peekObject();
					cr.setAnyServiceRule(lsr);
				}
			});
		} else if(xpath.endsWith("/scany/server")) {
			parser.addNodelet(xpath, "/remote/end()", new Nodelet() {
				public void process(Node node, Properties attributes, String text) throws Exception {
					RemoteServiceRule rsr = (RemoteServiceRule)assistant.popObject();
					ServerRule sr = (ServerRule)assistant.peekObject();
					sr.setRemoteServiceRule(rsr);
				}
			});
		}
	}

}
