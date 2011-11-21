/**
 * 
 */
package org.jhlabs.scany.service.local;

import org.jhlabs.scany.context.rule.LocalServiceRule;
import org.jhlabs.scany.engine.entity.Relation;
import org.jhlabs.scany.engine.search.AnySearcher;
import org.jhlabs.scany.engine.search.AnySearcherException;
import org.jhlabs.scany.engine.search.LuceneSearcher;
import org.jhlabs.scany.engine.search.SearchModel;
import org.jhlabs.scany.engine.transaction.AnyTransaction;
import org.jhlabs.scany.engine.transaction.LuceneTransaction;
import org.jhlabs.scany.service.AbstractService;
import org.jhlabs.scany.service.NotDeclaredRelationException;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 6. 오후 4:46:36</p>
 *
 */
public class LocalService extends AbstractService {
	
	private LocalServiceRule localServiceRule;

	public LocalService(LocalServiceRule localServiceRule) {
		this.localServiceRule = localServiceRule;
	}

	public AnyTransaction getTransaction(String relationId) {
		Relation relation = localServiceRule.getSchema().getRelation(relationId);
		
		if(relation == null)
			throw new NotDeclaredRelationException(relationId);
		
		return new LuceneTransaction(relation);
	}
	
	public AnySearcher getSercher(String relationId) throws AnySearcherException {
		Relation relation = localServiceRule.getSchema().getRelation(relationId);
		
		if(relation == null)
			throw new NotDeclaredRelationException(relationId);

		return new LuceneSearcher(relation);
	}
	
	public SearchModel getSearchModel(String relationId) throws AnySearcherException {
		Relation relation = localServiceRule.getSchema().getRelation(relationId);
		
		if(relation == null)
			throw new NotDeclaredRelationException(relationId);
		
		return new SearchModel(relation);
	}

}
