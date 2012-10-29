/**
 * 
 */
package org.jhlabs.scany.service;

import org.jhlabs.scany.engine.search.AnySearcher;
import org.jhlabs.scany.engine.search.AnySearcherException;
import org.jhlabs.scany.engine.search.SearchModel;
import org.jhlabs.scany.engine.transaction.AnyTransaction;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 6. 오후 4:46:36</p>
 *
 */
public abstract class AbstractService {
	
	public abstract AnyTransaction getTransaction(String tableId);
	
	public abstract AnySearcher getSearcher(String tableId) throws AnySearcherException;
	
	public abstract SearchModel getSearchModel(String relationId) throws AnySearcherException;
	
}
