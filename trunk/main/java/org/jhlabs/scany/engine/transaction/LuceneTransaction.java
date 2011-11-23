package org.jhlabs.scany.engine.transaction;

import org.jhlabs.scany.engine.entity.Relation;
import org.jhlabs.scany.engine.index.AnyIndexerException;
import org.jhlabs.scany.engine.index.LuceneIndexer;
import org.jhlabs.scany.engine.transaction.job.Job;
import org.jhlabs.scany.engine.transaction.job.JobType;

public class LuceneTransaction extends AbstractTransaction implements AnyTransaction {

	public LuceneTransaction(Relation relation) {
		super(relation);
	}

	public void commit() throws AnyIndexerException {
		LuceneIndexer luceneIndexer = null;
		
		try {
			luceneIndexer = new LuceneIndexer(relation);
			
			Job job = jobQueue.peek();
			
			while(job != null) {
				if(job.getJobType() == JobType.INSERT) {
					luceneIndexer.insert(job.getRecord());
				} else if(job.getJobType() == JobType.UPDATE) {
					luceneIndexer.update(job.getRecord());
				} else if(job.getJobType() == JobType.MERGE) {
					luceneIndexer.merge(job.getRecord());
				} else if(job.getJobType() == JobType.DELETE) {
					luceneIndexer.delete(job.getRecord().getRecordKey());
				}
				
				committedJobQueue.offer(job);
				
				jobQueue.remove();
				job = jobQueue.peek();
			}
		} finally {
			try {
				if(luceneIndexer != null)
					luceneIndexer.close();
			} catch(Exception ignored) {}
		}
	}

	public void rollback() throws AnyIndexerException {
		if(committedJobQueue == null || committedJobQueue.size() == 0)
			return;
		
		LuceneIndexer luceneIndexer = null;
		
		try {
			luceneIndexer = new LuceneIndexer(relation);
			
			Job job = committedJobQueue.peek();
			
			while(job != null) {
				if(job.getJobType() == JobType.INSERT) {
					luceneIndexer.delete(job.getRecord().getRecordKey());
				} else if(job.getJobType() == JobType.UPDATE) {
					//ignore
				} else if(job.getJobType() == JobType.MERGE) {
					//ignore
				} else if(job.getJobType() == JobType.DELETE) {
					//ignore
				}
				
				committedJobQueue.remove();
				job = committedJobQueue.peek();
			}
		} finally {
			try {
				if(luceneIndexer != null)
					luceneIndexer.close();
			} catch(Exception ignored) {}
		}

	}
	
}

