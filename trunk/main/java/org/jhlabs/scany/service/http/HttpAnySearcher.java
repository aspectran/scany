/**
 * 
 */
package org.jhlabs.scany.service.http;

import org.jhlabs.scany.engine.entity.Record;
import org.jhlabs.scany.engine.search.AnySearchException;
import org.jhlabs.scany.engine.search.AnySearcher;
import org.jhlabs.scany.engine.search.AnySearcherModel;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 6. 오후 4:46:36</p>
 *
 */
public class HttpAnySearcher extends AnySearcherModel implements AnySearcher {

	public Record[] search(String queryString) throws AnySearchException {
		// TODO Auto-generated method stub
		return null;
	}

	public Record[] search(int pageNo) throws AnySearchException {
		// TODO Auto-generated method stub
		return null;
	}

	public Record[] search(String queryString, int pageNo) throws AnySearchException {
		// TODO Auto-generated method stub
		return null;
	}

	public Record[] random() throws AnySearchException {
		// TODO Auto-generated method stub
		return null;
	}

	public Record[] random(String queryString) throws AnySearchException {
		// TODO Auto-generated method stub
		return null;
	}

	public Record[] seek(int start, int maxRecords, boolean reverse) throws AnySearchException {
		// TODO Auto-generated method stub
		return null;
	}

}
