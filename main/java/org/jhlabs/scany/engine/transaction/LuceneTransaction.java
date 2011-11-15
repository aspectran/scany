package org.jhlabs.scany.engine.transaction;

import org.jhlabs.scany.engine.entity.Relation;
import org.jhlabs.scany.engine.transaction.job.Job;
import org.jhlabs.scany.engine.transaction.job.JobType;

public class LuceneTransaction extends AbstractTransaction implements AnyTransaction {

	private Relation relation;

	public LuceneTransaction(Relation relation) {
		this.relation = relation;
	}

	public void commit() {
		Job job = jobQueue.peek();
		
		while(job != null) {
			if(job.getJobType() == JobType.INSERT) {
				
			} else if(job.getJobType() == JobType.UPDATE) {
				
			} else if(job.getJobType() == JobType.MERGE) {
				
			} else if(job.getJobType() == JobType.DELETE) {
				
			}
			
			committedJobQueue.offer(job);
			
			jobQueue.remove();
			job = jobQueue.peek();
		}
		
	}

	public void rollback() {
		if(committedJobQueue != null && committedJobQueue.size() > 0) {
			
		}
	}
	
}

