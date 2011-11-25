/**
 * 
 */
package org.jhlabs.scany.service.local;

import org.jhlabs.scany.context.rule.LocalServiceRule;
import org.jhlabs.scany.engine.entity.Relation;
import org.jhlabs.scany.engine.index.AnyIndexer;
import org.jhlabs.scany.engine.index.AnyIndexerException;
import org.jhlabs.scany.engine.index.LuceneIndexer;
import org.jhlabs.scany.engine.search.AnySearcher;
import org.jhlabs.scany.engine.search.AnySearcherException;
import org.jhlabs.scany.engine.search.LuceneSearcher;
import org.jhlabs.scany.engine.search.SearchModel;
import org.jhlabs.scany.engine.transaction.AnyTransaction;
import org.jhlabs.scany.engine.transaction.LuceneTransaction;
import org.jhlabs.scany.service.AbstractService;
import org.jhlabs.scany.service.AnyService;
import org.jhlabs.scany.service.NotSuchRelationException;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 6. 오후 4:46:36</p>
 *
 */
public class LocalService extends AbstractService implements AnyService {
	
	private LocalServiceRule localServiceRule;

	public LocalService(LocalServiceRule localServiceRule) {
		this.localServiceRule = localServiceRule;
	}
	
	public AnySearcher getSearcher(String relationId) throws AnySearcherException {
		Relation relation = localServiceRule.getSchema().getRelation(relationId);
		
		if(relation == null)
			throw new NotSuchRelationException(relationId);

		return new LuceneSearcher(relation);
	}
	
	public SearchModel getSearchModel(String relationId) throws AnySearcherException {
		Relation relation = localServiceRule.getSchema().getRelation(relationId);
		
		if(relation == null)
			throw new NotSuchRelationException(relationId);
		
		return new SearchModel(relation);
	}

	public AnyTransaction getTransaction(String relationId) {
		Relation relation = localServiceRule.getSchema().getRelation(relationId);
		
		if(relation == null)
			throw new NotSuchRelationException(relationId);
		
		return new LuceneTransaction(relation);
	}
	
	public AnyIndexer getIndexer(String relationId) throws AnyIndexerException {
		Relation relation = localServiceRule.getSchema().getRelation(relationId);
		
		if(relation == null)
			throw new NotSuchRelationException(relationId);
		
		return new LuceneIndexer(relation);
	}

}
