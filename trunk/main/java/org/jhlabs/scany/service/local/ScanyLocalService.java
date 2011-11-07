/**
 * 
 */
package org.jhlabs.scany.service.local;

import org.jhlabs.scany.context.rule.LocalServiceRule;
import org.jhlabs.scany.engine.search.AnySearcher;
import org.jhlabs.scany.service.AbstractScanyService;
import org.jhlabs.scany.service.ScanyTransaction;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 6. 오후 4:46:36</p>
 *
 */
public class ScanyLocalService extends AbstractScanyService {
	
	private LocalServiceRule localServiceRule;

	public ScanyLocalService(LocalServiceRule localServiceRule) {
		this.localServiceRule = localServiceRule;
	}

	public ScanyTransaction getTransaction(String tableId) {
		return null;
	}
	
	public AnySearcher getSercher(String tableId) {
		return null;
	}

}
