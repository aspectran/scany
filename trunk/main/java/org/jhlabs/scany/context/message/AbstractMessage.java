/**
 * 
 */
package org.jhlabs.scany.context.message;

import org.jhlabs.scany.context.type.MessageFormat;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 15. 오후 6:37:32</p>
 *
 */
public abstract class AbstractMessage {

	private MessageFormat messageFormat;

	public MessageFormat getMessageFormat() {
		return messageFormat;
	}

	public void setMessageFormat(MessageFormat messageFormat) {
		this.messageFormat = messageFormat;
	}
	
}
