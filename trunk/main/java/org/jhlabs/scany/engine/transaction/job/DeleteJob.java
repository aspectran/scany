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
public class DeleteJob extends AbstractJob implements Job {
	
	public DeleteJob(Record record) {
		super(JobType.DELETE, record);
	}

}