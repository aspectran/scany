/**
 * 
 */
package org.jhlabs.scany.service.message;

import java.io.Serializable;

import org.jhlabs.scany.engine.transaction.job.JobQueue;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 15. 오후 6:37:32</p>
 *
 */
public class TransactionMessage extends AbstractMessage implements Message, Serializable {

	private static final long serialVersionUID = 1378325630457487475L;

	private JobQueue jobQueue;
	
}
