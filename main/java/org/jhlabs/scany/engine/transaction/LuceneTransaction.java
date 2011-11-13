package org.jhlabs.scany.engine.transaction;

import org.jhlabs.scany.engine.entity.Relation;

public class LuceneTransaction extends AbstractTransaction implements AnyTransaction {

	private Relation relation;

	public LuceneTransaction(Relation relation) {
		this.relation = relation;
	}

	public void commit() {
		Job job = jobQueue.peek();
		
		while(job != null) {

			
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

