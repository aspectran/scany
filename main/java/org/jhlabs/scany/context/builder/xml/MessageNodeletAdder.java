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
import org.jhlabs.scany.context.rule.MessageRule;
import org.jhlabs.scany.context.rule.RemoteHttpServiceRule;
import org.jhlabs.scany.context.rule.RemoteTcpServiceRule;
import org.jhlabs.scany.context.type.MessageFormat;
import org.jhlabs.scany.util.xml.EasyNodelet;
import org.jhlabs.scany.util.xml.EasyNodeletAdder;
import org.jhlabs.scany.util.xml.EasyNodeletParser;

/**
 * Translet Map Parser.
 * 
 * <p>Created: 2008. 06. 14 오전 4:39:24</p>
 */
public class MessageNodeletAdder implements EasyNodeletAdder {
	
	protected ScanyContextBuilderAssistant assistant;
	
	/**
	 * Instantiates a new content nodelet adder.
	 * 
	 * @param parser the parser
	 * @param assistant the assistant for Context Builder
	 */
	public MessageNodeletAdder(ScanyContextBuilderAssistant assistant) {
		this.assistant = assistant;
	}
	
	/**
	 * Process.
	 */
	public void process(String xpath, EasyNodeletParser parser) {
		parser.addNodelet(xpath, "/message", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				String format = attributes.getProperty("format");
				String encoding = attributes.getProperty("encoding");

				Object anyServiceRule = assistant.peekObject();
				
				if(anyServiceRule instanceof RemoteTcpServiceRule) {
					RemoteTcpServiceRule rtsr = (RemoteTcpServiceRule)anyServiceRule;
					rtsr.setMessageFormat(MessageFormat.valueOf(format));
					rtsr.setCharacterEncoding(encoding);
				} else if(anyServiceRule instanceof RemoteHttpServiceRule) {
					RemoteHttpServiceRule rhsr = (RemoteHttpServiceRule)assistant.peekObject();
					rhsr.setMessageFormat(MessageFormat.valueOf(format));
					rhsr.setCharacterEncoding(encoding);
				}
			}
		});
		parser.addNodelet(xpath, "/message/keysign", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				String encryption = attributes.getProperty("encryption");
				String compressable = attributes.getProperty("compressable");

				MessageRule mr = new MessageRule();
				mr.setEncryption(encryption);
				mr.setCompressable(Boolean.valueOf(compressable));
				mr.setText(text);

				Object anyServiceRule = assistant.peekObject();
				
				if(anyServiceRule instanceof RemoteTcpServiceRule) {
					RemoteTcpServiceRule rtsr = (RemoteTcpServiceRule)anyServiceRule;
					rtsr.setKeysignMessageRule(mr);
				} else if(anyServiceRule instanceof RemoteHttpServiceRule) {
					RemoteHttpServiceRule hsr = (RemoteHttpServiceRule)anyServiceRule;
					hsr.setKeysignMessageRule(mr);
				}
			}
		});
		parser.addNodelet(xpath, "/message/body", new EasyNodelet() {
			public void process(Properties attributes, String text) throws Exception {
				String encryption = attributes.getProperty("encryption");
				String compressable = attributes.getProperty("compressable");
				
				MessageRule mr = new MessageRule();
				mr.setEncryption(encryption);
				mr.setCompressable(Boolean.valueOf(compressable));

				Object anyServiceRule = assistant.peekObject();
				
				if(anyServiceRule instanceof RemoteTcpServiceRule) {
					RemoteTcpServiceRule hsr = (RemoteTcpServiceRule)assistant.peekObject();
					hsr.setBodyMessageRule(mr);
				} else if(anyServiceRule instanceof RemoteHttpServiceRule) {
					RemoteHttpServiceRule hsr = (RemoteHttpServiceRule)anyServiceRule;
					hsr.setBodyMessageRule(mr);
				}
			}
		});
	}

}
