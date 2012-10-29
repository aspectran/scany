/**
 * 
 */
package org.jhlabs.scany.service.remote.http;

import java.util.Iterator;

import org.jhlabs.scany.engine.entity.Record;
import org.jhlabs.scany.engine.entity.RecordList;
import org.jhlabs.scany.engine.entity.Relation;
import org.jhlabs.scany.engine.search.AnySearcher;
import org.jhlabs.scany.engine.search.AnySearcherException;
import org.jhlabs.scany.engine.search.SearchModel;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 6. 오후 4:46:36</p>
 *
 */
public class RemoteHttpSearcher extends SearchModel implements AnySearcher {

	public RemoteHttpSearcher(Relation relation) throws AnySearcherException {
		super(relation);
	}

	public RecordList search(String queryString) throws AnySearcherException {
		return null;
	}

	public RecordList search(int pageNo) throws AnySearcherException {
		return null;
	}

	public RecordList search(String queryString, int pageNo) throws AnySearcherException {
		return null;
	}

	public RecordList random() throws AnySearcherException {
		return null;
	}

	public RecordList random(String queryString) throws AnySearcherException {
		return null;
	}

	public RecordList seek(int start, int maxRecords, boolean reverse) throws AnySearcherException {
		return null;
	}

	public Iterator<Record> interator(int numHitsToCollect) throws AnySearcherException {
		// TODO Auto-generated method stub
		return null;
	}

	public Iterator<Record> interator(String queryString, int numHitsToCollect) throws AnySearcherException {
		// TODO Auto-generated method stub
		return null;
	}

}
