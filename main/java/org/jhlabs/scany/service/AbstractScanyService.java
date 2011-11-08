/**
 * 
 */
package org.jhlabs.scany.service;

import org.jhlabs.scany.context.rule.BaseServiceRule;
import org.jhlabs.scany.engine.search.AnySearcher;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 6. 오후 4:46:36</p>
 *
 */
public abstract class AbstractScanyService {
	
	public abstract AnyTransaction getTransaction(String tableId);
	
	public abstract AnySearcher getSercher(String tableId);
	
}
