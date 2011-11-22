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
package org.jhlabs.scany.service;


/**
 * 
 * <p>Created: 2008. 01. 07 오전 3:35:55</p>
 * 
 * @author Gulendol
 * 
 */
public class NotSuchRelationException extends RuntimeException {

	/** @serial */
	static final long serialVersionUID = 6834919771549843895L;

	/**
	 * Simple constructor
	 */
	public NotSuchRelationException() {
	}

	/**
	 * Constructor to create exception with a message
	 * 
	 * @param msg
	 *            A message to associate with the exception
	 */
	public NotSuchRelationException(String relationId) {
		super("'" + relationId + "' is Not declared relation.");
	}

	/**
	 * Constructor to create exception to wrap another exception
	 * 
	 * @param cause
	 *            The real cause of the exception
	 */
	public NotSuchRelationException(Throwable cause) {
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
	public NotSuchRelationException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
