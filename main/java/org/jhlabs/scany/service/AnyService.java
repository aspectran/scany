/**
 * 
 */
package org.jhlabs.scany.service;

import org.jhlabs.scany.engine.index.AnyIndexer;
import org.jhlabs.scany.engine.index.AnyIndexerException;
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
public interface AnyService {
	
	public AnySearcher getSercher(String relationId) throws AnySearcherException;
	
	public SearchModel getSearchModel(String relationId) throws AnySearcherException;

	public AnyTransaction getTransaction(String relationId);
	
	public AnyIndexer getIndexer(String relationId) throws AnyIndexerException;
	
}
