/**
 * 
 */
package org.jhlabs.scany.engine.transaction.job;


/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 8. 오후 11:45:55</p>
 *
 */
public class JobType {

	public static final JobType INSERT;
	public static final JobType UPDATE;
	public static final JobType MERGE;
	public static final JobType DELETE;
	
	static {
		INSERT = new JobType("insert");
		UPDATE = new JobType("update");
		MERGE = new JobType("merge");
		DELETE = new JobType("delete");
	}

	private final String jobType;
	
	public JobType(String jobType) {
		this.jobType = jobType;
	}
	
	public String toString() {
		return jobType;
	}
}
