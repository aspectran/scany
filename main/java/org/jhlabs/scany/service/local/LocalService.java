/**
 * 
 */
package org.jhlabs.scany.service.local;

import org.jhlabs.scany.context.rule.LocalServiceRule;
import org.jhlabs.scany.engine.search.AnySearcher;
import org.jhlabs.scany.engine.search.SearchModel;
import org.jhlabs.scany.engine.transaction.AnyTransaction;
import org.jhlabs.scany.service.AbstractService;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 6. 오후 4:46:36</p>
 *
 */
public class LocalService extends AbstractService {
	
	private LocalServiceRule localServiceRule;

	public LocalService(LocalServiceRule localServiceRule) {
		this.localServiceRule = localServiceRule;
	}

	public AnyTransaction getTransaction(String relationId) {
		return null;
	}
	
	public AnySearcher getSercher(String relationId) {
		return null;
	}
	
	public SearchModel getSearchModel(String relationId) {
		return null;
	}

}
