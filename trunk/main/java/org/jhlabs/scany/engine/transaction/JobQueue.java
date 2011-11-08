/**
 * 
 */
package org.jhlabs.scany.engine.transaction;

import java.util.LinkedList;

import org.jhlabs.scany.engine.entity.Record;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 8. 오후 11:45:55</p>
 *
 */
public class JobQueue extends LinkedList<Job> {
	
	private static final long serialVersionUID = 8014732370875518324L;

	public boolean offer(JobType jobType, Record record) {
		return offer(new Job(jobType, record));
	}
	
}
