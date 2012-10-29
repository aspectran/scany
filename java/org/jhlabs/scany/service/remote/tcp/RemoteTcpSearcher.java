/**
 * 
 */
package org.jhlabs.scany.service.remote.tcp;

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
public class RemoteTcpSearcher extends SearchModel implements AnySearcher {

	public RemoteTcpSearcher(Relation relation) throws AnySearcherException {
		super(relation);
		// TODO Auto-generated constructor stub
	}

	public RecordList search(String queryString) throws AnySearcherException {
		// TODO Auto-generated method stub
		return null;
	}

	public RecordList search(int pageNo) throws AnySearcherException {
		// TODO Auto-generated method stub
		return null;
	}

	public RecordList search(String queryString, int pageNo) throws AnySearcherException {
		// TODO Auto-generated method stub
		return null;
	}

	public RecordList random() throws AnySearcherException {
		// TODO Auto-generated method stub
		return null;
	}

	public RecordList random(String queryString) throws AnySearcherException {
		// TODO Auto-generated method stub
		return null;
	}

	public RecordList seek(int start, int maxRecords, boolean reverse) throws AnySearcherException {
		// TODO Auto-generated method stub
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
