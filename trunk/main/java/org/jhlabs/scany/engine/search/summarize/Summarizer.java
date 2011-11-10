/**
 * 
 */
package org.jhlabs.scany.engine.search.summarize;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 8. 오후 12:55:05</p>
 *
 */
public interface Summarizer {
	
	/**
	 * Summarize.
	 *
	 * @param content the content
	 * @return the string
	 */
	public String summarize(String content);

}
