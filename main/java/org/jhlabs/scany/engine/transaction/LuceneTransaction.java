package org.jhlabs.scany.engine.transaction;

import org.jhlabs.scany.engine.entity.Relation;

public class LuceneTransaction extends AbstractTransaction implements AnyTransaction {

	private Relation table;

	public LuceneTransaction(Relation table) {
		this.table = table;
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

