/**
 * 
 */
package org.jhlabs.scany.context;

import org.jhlabs.scany.context.rule.ClientRule;
import org.jhlabs.scany.context.rule.LocalServiceRule;
import org.jhlabs.scany.context.rule.ServerRule;
import org.jhlabs.scany.service.AnyService;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 6. 오후 10:06:29</p>
 *
 */
public class ScanyContext {

	private LocalServiceRule localServiceRule;
	
	private ClientRule clientRule;
	
	private ServerRule serverRule;
	
	public ScanyContext() {
		
	}
	
	public ScanyContext(LocalServiceRule localServiceRule) {
		
	}
	
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

	public AnyService getAnyService() {
		return null;
	}
	
}
