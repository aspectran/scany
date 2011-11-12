/**
 * 
 */
package org.jhlabs.scany.engine.transaction;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.jhlabs.scany.engine.entity.Record;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 7. 오후 1:17:21</p>
 *
 */
public abstract class AbstractTransaction {

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
	
	public abstract void commit();
	
	public abstract void rollback();
	
	public void insert(Record record) {
		jobQueue.offer(JobType.INSERT, record);
	}
	
	public void update(Record record) {
		jobQueue.offer(JobType.UPDATE, record);
	}
	
	public void merge(Record record) {
		jobQueue.offer(JobType.MERGE, record);
	}
	
	public void delete(Record record) {
		jobQueue.offer(JobType.DELETE, record);
	}
	
}
