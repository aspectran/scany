/**
 * 
 */
package org.jhlabs.scany.service.message;

import java.io.Serializable;

import org.jhlabs.scany.engine.search.SearchModel;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 15. 오후 6:37:32</p>
 *
 */
public class QueryMessage extends AbstractMessage implements Message, Serializable {

	private static final long serialVersionUID = -1180197750724560282L;

	private SearchModel searchModel;
	
	public QueryMessage(SearchModel searchModel) {
		this.searchModel = searchModel;
	}
	
}
