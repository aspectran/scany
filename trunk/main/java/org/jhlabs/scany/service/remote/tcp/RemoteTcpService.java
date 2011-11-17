/**
 * 
 */
package org.jhlabs.scany.service.remote.tcp;

import org.jhlabs.scany.context.rule.LocalServiceRule;
import org.jhlabs.scany.engine.search.AnySearcher;
import org.jhlabs.scany.engine.transaction.AnyTransaction;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 6. 오후 4:46:36</p>
 *
 */
public class RemoteTcpService {
	
	private LocalServiceRule localServiceRule;

	public RemoteTcpService(LocalServiceRule localServiceRule) {
		this.localServiceRule = localServiceRule;
	}

	public AnyTransaction getTransaction(String relationId) {
		return null;
	}
	
	public AnySearcher getSercher(String relationId) {
		return null;
	}
	
}
