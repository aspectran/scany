/**
 * 
 */
package org.jhlabs.scany.engine.transaction;

import org.jhlabs.scany.engine.entity.Record;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 6. 오후 4:46:36</p>
 *
 */
public interface AnyTransaction {

	public void begin();
	
	public void commit();
	
	public void rollback();
	
	public void insert(Record record);
	
	public void update(Record record);
	
	public void merge(Record record);
	
	public void delete(Record record);

}
