/**
 * 
 */
package org.jhlabs.scany.service;

import java.util.ArrayList;
import java.util.List;

import org.jhlabs.scany.engine.entity.Record;
import org.jhlabs.scany.engine.entity.Table;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 7. 오후 1:17:21</p>
 *
 */
public abstract class AbstractScanyTransaction {

	private Table table;
	
	private List recordList;
	
	private List committedRecordList;
	
	public AbstractScanyTransaction(Table table) {
		this.table = table;
	}
	
	public void begin() {
		if(recordList != null && recordList.size() > 0)
			recordList.clear();
		
		if(committedRecordList != null && committedRecordList.size() > 0)
			committedRecordList.clear();
		
		recordList = new ArrayList();
		committedRecordList = null;
	}
	
	public abstract void commit();
	
	public abstract void rollback();
	
	public void insert(Record record) {
		recordList.add(record);
	}

	public List getRecordList() {
		return recordList;
	}

	public void setRecordList(List recordList) {
		this.recordList = recordList;
	}

	
	
	
}
