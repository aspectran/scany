/**
 * 
 */
package org.jhlabs.scany.engine.transaction.job;

import org.jhlabs.scany.engine.entity.Record;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 8. 오후 11:45:55</p>
 *
 */
public class AbstractJob implements Job {
	
	private final JobType jobType;
	
	private final Record record;
	
	public AbstractJob(JobType jobType, Record record) {
		this.jobType = jobType;
		this.record = record;
	}
	
	public JobType getJobType() {
		return jobType;
	}
	
	public Record getRecord() {
		return record;
	}
}