/*******************************************************************************
 * Copyright (c) 2008 Jeong Ju Ho.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Jeong Ju Ho - initial API and implementation
 ******************************************************************************/
package org.jhlabs.scany.engine.search;



/**
 * 
 * <p>Created: 2008. 01. 07 오전 3:35:55</p>
 * 
 * @author Gulendol
 * 
 */
public class InvalidSummarizerException extends RuntimeException {

	/** @serial */
	private static final long serialVersionUID = 3293845371916813620L;

	/**
	 * Simple constructor
	 */
	public InvalidSummarizerException() {
	}

	/**
	 * Constructor to create exception with a message
	 * 
	 * @param summarizerId
	 *            A message to associate with the exception
	 */
	public InvalidSummarizerException(String summarizerId) {
		super("Invalid summarizer '" + summarizerId + "'.");
	}

	/**
	 * Constructor to create exception with a message
	 * 
	 * @param summarizerId
	 *            A message to associate with the exception
	 */
	public InvalidSummarizerException(String summarizerId, String msg) {
		super("Invalid summarizer '" + summarizerId + "'. " + msg);
	}
	
	/**
	 * Constructor to create exception to wrap another exception
	 * 
	 * @param cause
	 *            The real cause of the exception
	 */
	public InvalidSummarizerException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor to create exception to wrap another exception and pass a
	 * message
	 * 
	 * @param msg
	 *            The message
	 * @param cause
	 *            The real cause of the exception
	 */
	public InvalidSummarizerException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
