/**
 * 
 */
package org.jhlabs.scany.service.remote.http;

import org.jhlabs.scany.engine.entity.Record;
import org.jhlabs.scany.engine.search.AnySearcherException;
import org.jhlabs.scany.engine.search.AnySearcher;
import org.jhlabs.scany.engine.search.AnySearcherModel;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 6. 오후 4:46:36</p>
 *
 */
public class RemoteHttpSearcher extends AnySearcherModel implements AnySearcher {

	public Record[] search(String queryString) throws AnySearcherException {
		return null;
	}

	public Record[] search(int pageNo) throws AnySearcherException {
		return null;
	}

	public Record[] search(String queryString, int pageNo) throws AnySearcherException {
		return null;
	}

	public Record[] random() throws AnySearcherException {
		return null;
	}

	public Record[] random(String queryString) throws AnySearcherException {
		return null;
	}

	public Record[] seek(int start, int maxRecords, boolean reverse) throws AnySearcherException {
		return null;
	}

}
