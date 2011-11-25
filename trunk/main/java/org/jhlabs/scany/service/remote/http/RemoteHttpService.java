/**
 * 
 */
package org.jhlabs.scany.service.remote.http;

import org.jhlabs.scany.engine.index.AnyIndexer;
import org.jhlabs.scany.engine.index.AnyIndexerException;
import org.jhlabs.scany.engine.search.AnySearcher;
import org.jhlabs.scany.engine.search.AnySearcherException;
import org.jhlabs.scany.engine.search.SearchModel;
import org.jhlabs.scany.engine.transaction.AnyTransaction;
import org.jhlabs.scany.service.AnyService;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 6. 오후 4:46:36</p>
 *
 */
public class RemoteHttpService implements AnyService {

	public AnySearcher getSearcher(String relationId) throws AnySearcherException {
		// TODO Auto-generated method stub
		return null;
	}

	public SearchModel getSearchModel(String relationId) throws AnySearcherException {
		// TODO Auto-generated method stub
		return null;
	}

	public AnyTransaction getTransaction(String relationId) {
		// TODO Auto-generated method stub
		return null;
	}

	public AnyIndexer getIndexer(String relationId) throws AnyIndexerException {
		throw new UnsupportedOperationException("Only local service.");
	}

}
