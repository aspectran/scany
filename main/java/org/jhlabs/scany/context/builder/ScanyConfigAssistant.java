/*
 *  Copyright (c) 2009 Jeong Ju Ho, All rights reserved.
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
package org.jhlabs.scany.context.builder;

import org.jhlabs.scany.context.rule.ClientRule;
import org.jhlabs.scany.context.rule.LocalServiceRule;
import org.jhlabs.scany.context.rule.ServerRule;

/**
 * <p>Created: 2008. 04. 01 오후 10:25:35</p>
 */
public class ScanyConfigAssistant extends AbstractAssistant {

	private LocalServiceRule localServiceRule;
	
	private ClientRule clientRule;
	
	private ServerRule serverRule;
	
	public LocalServiceRule getLocalServiceRule() {
		return localServiceRule;
	}

	public void setLocalServiceRule(LocalServiceRule localServiceRule) {
		this.localServiceRule = localServiceRule;
	}

	public ClientRule getClientRule() {
		return clientRule;
	}

	public void setClientRule(ClientRule clientRule) {
		this.clientRule = clientRule;
	}

	public ServerRule getServerRule() {
		return serverRule;
	}

	public void setServerRule(ServerRule serverRule) {
		this.serverRule = serverRule;
	}
	
}
