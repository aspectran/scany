package org.jhlabs.scany.engine.transaction;

import org.jhlabs.scany.engine.entity.Relation;
import org.jhlabs.scany.engine.index.AnyIndexer;
import org.jhlabs.scany.engine.index.AnyIndexerException;
import org.jhlabs.scany.engine.index.LuceneIndexer;
import org.jhlabs.scany.engine.transaction.job.Job;
import org.jhlabs.scany.engine.transaction.job.JobType;

public class LuceneTransaction extends AbstractTransaction implements AnyTransaction {

	public LuceneTransaction(Relation relation) {
		super(relation);
	}

	synchronized public void commit() throws AnyIndexerException {
		AnyIndexer indexer = null;
		
		try {
			indexer = new LuceneIndexer(relation);
			
			synchronized(jobQueue) {
				Job job = jobQueue.peek();
				
				while(job != null) {
					if(job.getJobType() == JobType.INSERT) {
						indexer.insert(job.getRecord());
					} else if(job.getJobType() == JobType.UPDATE) {
						indexer.update(job.getRecord());
					} else if(job.getJobType() == JobType.MERGE) {
						indexer.merge(job.getRecord());
					} else if(job.getJobType() == JobType.DELETE) {
						indexer.delete(job.getRecord().getRecordKey());
					}
					
					jobQueue.remove();
					job = jobQueue.peek();
				}
			}
		} catch(AnyIndexerException e) {
			if(indexer != null)
				indexer.rollback();
			throw e;
		} finally {
			try {
				if(indexer != null)
					indexer.close();
			} catch(Exception ignored) {}
		}
	}

	public void rollback() throws AnyIndexerException {
		throw new UnsupportedOperationException("Only local service.");
	}
	
}

