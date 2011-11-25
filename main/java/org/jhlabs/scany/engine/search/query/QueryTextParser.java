/**
 * 
 */
package org.jhlabs.scany.engine.search.query;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 25. 오전 9:06:52</p>
 *
 */
public interface QueryTextParser {

	public String parse(String queryText);
	
	public String[] getKeywords();
	
}
