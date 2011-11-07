/**
 * 
 */
package org.jhlabs.scany.engine.entity;

import java.util.List;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 7. 오후 1:17:21</p>
 *
 */
public class Transaction {

	private Table table;
	
	private List recordList;
	
	public Transaction(Table table) {
		this.table = table;
	}
	
	public void addRecord(Record record) {
		recordList.add(record);
	}

	public List getRecordList() {
		return recordList;
	}

	public void setRecordList(List recordList) {
		this.recordList = recordList;
	}

	
	
	
}
