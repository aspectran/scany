/**
 * 
 */
package org.jhlabs.scany.service.local;

import org.jhlabs.scany.context.rule.LocalServiceRule;
import org.jhlabs.scany.engine.search.AnySearcher;
import org.jhlabs.scany.service.AbstractAnyService;
import org.jhlabs.scany.service.AnyTransaction;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 6. 오후 4:46:36</p>
 *
 */
public class AnyLocalService extends AbstractAnyService {
	
	private LocalServiceRule localServiceRule;

	public AnyLocalService(LocalServiceRule localServiceRule) {
		this.localServiceRule = localServiceRule;
	}

	public AnyTransaction getTransaction(String tableId) {
		return null;
	}
	
	public AnySearcher getSercher(String tableId) {
		return null;
	}

}
