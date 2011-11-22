/**
 * 
 */
package org.jhlabs.scany.engine.transaction;

import org.jhlabs.scany.engine.entity.Record;
import org.jhlabs.scany.engine.entity.RecordKey;
import org.jhlabs.scany.engine.entity.RecordKeyPattern;
import org.jhlabs.scany.engine.entity.Relation;
import org.jhlabs.scany.engine.index.AnyIndexerException;
import org.jhlabs.scany.engine.transaction.job.DeleteJob;
import org.jhlabs.scany.engine.transaction.job.InsertJob;
import org.jhlabs.scany.engine.transaction.job.JobQueue;
import org.jhlabs.scany.engine.transaction.job.MergeJob;
import org.jhlabs.scany.engine.transaction.job.UpdateJob;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 7. 오후 1:17:21</p>
 *
 */
public abstract class AbstractTransaction {

	protected Relation relation;
	
	public AbstractTransaction(Relation relation) {
		this.relation = relation;
	}
	
	protected JobQueue jobQueue;
	
	protected JobQueue committedJobQueue;
	
	public void begin() {
		if(jobQueue != null) {
			jobQueue.clear();
			jobQueue = null;
		}
		
		if(committedJobQueue != null) {
			committedJobQueue.clear();
			committedJobQueue = null;
		}
		
		jobQueue = new JobQueue();
	}
	
	public abstract void commit() throws AnyIndexerException;
	
	public abstract void rollback() throws AnyIndexerException;
	
	public void insert(Record record) {
		populateRecordKey(record);
		jobQueue.offer(new InsertJob(record));
	}
	
	public void update(Record record) {
		populateRecordKey(record);
		jobQueue.offer(new UpdateJob(record));
	}
	
	public void merge(Record record) {
		populateRecordKey(record);
		jobQueue.offer(new MergeJob(record));
	}
	
	public void delete(Record record) {
		populateRecordKey(record);
		jobQueue.offer(new DeleteJob(record));
	}
	
	public RecordKey newRecordKey() {
		return relation.newRecordKey();
	}
	
	private void populateRecordKey(Record record) {
		if(record.getRecordKey() == null) {
			RecordKeyPattern recordKeyPattern = relation.getRecordKeyPattern();
			String[] keyNames = recordKeyPattern.getKeyNames();
			
			if(keyNames != null) {
				RecordKey recordKey = newRecordKey();
			
				for(String keyName : keyNames) {
					recordKey.setKeyValue(keyName, record.getValue(keyName));
				}
				
				record.setRecordKey(recordKey);
			}
		}
	}
}
